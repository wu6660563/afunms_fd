/**
 * 
 * 根据XML的TASK信息来建立任务对象
 * 并把任务对象保存在列表TaskList中
 * 
 * 一个建立定时任务对象。
 * 一个是可以从人物列表中把任务对象注销
 */
package com.gathertask;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.gatherdb.GatherDatatempsqlRun;
import com.gatherdb.GathersqlRun;
import com.gatherdb.nmsmemorydate;
import java.util.Hashtable;
import com.gathertask.dao.Taskdao;
import com.gathertask.MaintainTask;





public class TaskManager {

    /**
     * 日志
     */
	private static SysLogger logger = SysLogger.getLogger(TaskManager.class.getName());

	/**
	 * 采集任务管理实例
	 */
	private static TaskManager instance;

	/**
     * 所有采集任务的计时器
     */
    private Timer allCollectTaskTimer = new Timer();

    /**
     * 维护任务的计时器
     */
    private Timer maintainTimer = new Timer();

    /**
     * 采集管理是否启动
     */
    private boolean isStarted = false;
 
    /**
     * 维护任务是否启动
     */
    private boolean isStartMaintainTask = false;

    /**
     * 用于存储所有添加入计时器的采集任务
     */
	private Hashtable<String, BaseTask> timerTaskHashtable = new Hashtable<String, BaseTask>(); 

	/**
	 * 采集任务的线程池
	 */
	private ThreadPool threadPool = new ThreadPool();

	/**
	 * 私有的构造方式用于单例模式
	 */
	private TaskManager() {
	}

	/**
	 * 获取任务管理类实例
	 * @return
	 */
	public static TaskManager getInstance() {
	    if (instance == null) {
	        instance = new TaskManager();
	    }
	    return instance;
	}

	public void start() {
	    if (!isStarted()) {
	        threadPool.setName("GC");
	        threadPool.setMaxThreads(500);
	        getThreadPool().start();
	        addAllNodeGatherIndicatorsToTimer();
	        createMaintainTask();
	        setStarted(true);
	    }
	}

	/**
	 * 创建所有的任务
	 * @author 聂林
	 */
    public void addAllNodeGatherIndicatorsToTimer() {
		Taskdao taskdao = new Taskdao();
		Hashtable runtask = taskdao.GetRunTaskList();
		if (null != runtask) {
		    //如果不为空则循环
    	    if (logger.isInfoEnabled()) {
                logger.info("数据库需要采集采集指标个数为：" + runtask.size());
            }
    	    List<NodeGatherIndicators> list = new ArrayList<NodeGatherIndicators>();
    	    Enumeration allvalue = runtask.elements();
    	    while (allvalue.hasMoreElements()) {
    	        NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) allvalue.nextElement();
    	        if ("1".equals(nodeGatherIndicators.getIsCollection())) {
    	            list.add(nodeGatherIndicators);
    	        }
            }
    	    addNodeGatherIndicatorsToTimer(list);
		} 
	}

    public void addNodeGatherIndicatorsToTimer(List<NodeGatherIndicators> list) {
        List<BaseTask> baseTaskList = new ArrayList<BaseTask>();
        for (NodeGatherIndicators nodeGatherIndicators : list) {
            BaseTask BaseTask = createBaseTask(nodeGatherIndicators);
            baseTaskList.add(BaseTask);
        }
        addBaseTaskToTimer(baseTaskList);
    }

    public void deleteNodeGatherIndicatorsFromTimer(List<NodeGatherIndicators> list) {
        for (NodeGatherIndicators nodeGatherIndicators : list) {
            deleteBaseTaskFromTimer(String.valueOf(nodeGatherIndicators.getId()));
        }
    }

    /**
     * 通过采集指标生成任务
     * @param nodeGatherIndicators
     * @return
     */
    public BaseTask createBaseTask(NodeGatherIndicators nodeGatherIndicators) {
        BaseTask baseTask = new BaseTask();
        baseTask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//运行类得路径
        baseTask.setTaskid(nodeGatherIndicators.getId()+"");
        baseTask.setNodeid((String)nodeGatherIndicators.getNodeid());
        baseTask.setTaskname(nodeGatherIndicators.getName());
        baseTask.setRunclasspath(nodeGatherIndicators.getClasspath());
        baseTask.setGather(nodeGatherIndicators);

        long intervaltime=Integer.parseInt(nodeGatherIndicators.getPoll_interval());
        if(nodeGatherIndicators.getInterval_unit().equals("m")) {
            intervaltime = intervaltime * 1000 * 60;
        }
        if(nodeGatherIndicators.getInterval_unit().equals("h")) {
            intervaltime = intervaltime * 1000 * 60 * 60;
        }
        if(nodeGatherIndicators.getInterval_unit().equals("d")) {
            intervaltime = intervaltime * 1000 * 60 * 60 * 24;
        }
        baseTask.setIntervaltime(intervaltime);
        return baseTask;
    }

    /**
     * 批量添加采集任务至计时器中，返回添加成功数
     * @param list
     * @return
     */
    private int addBaseTaskToTimer(List<BaseTask> list) {
        int successNum = 0;
        for (BaseTask BaseTask : list) {
            boolean result = addBaseTaskToTimer(BaseTask);
            if (result) {
                successNum ++;
            }
        }
        return successNum;
    }

    /**
     * 添加采集任务至计时器中，成功则返回 true
     * @param BaseTask
     * @return
     */
    private boolean addBaseTaskToTimer(BaseTask baseTask) {
        // 先从计时器中删除该采集任务
        deleteBaseTaskFromTimer(baseTask);
        // 加入至计时器中
        Hashtable<String, BaseTask> hashtable = getTimerTaskHashtable();
        hashtable.put(baseTask.getTaskid(), baseTask);
        Timer timer = getAllCollectTaskTimer();
        timer.schedule(baseTask, 0, baseTask.getIntervaltime());
        return true;
    }

    /**
     * 从计时器中删除采集任务，成功则返回 true
     * @param BaseTask
     * @return
     */
    private boolean deleteBaseTaskFromTimer(BaseTask baseTask) {
        if (baseTask != null) {
            deleteBaseTaskFromTimer(baseTask.getTaskid());
        }
        return true;
    }

    /**
     * 从计时器中删除采集任务，成功则返回 true
     * @param taskId
     * @return
     */
    private boolean deleteBaseTaskFromTimer(String taskId) {
        Hashtable<String, BaseTask> hashtable = getTimerTaskHashtable();
        BaseTask baseTask = hashtable.get(taskId);
        if (baseTask != null) {
            baseTask.cancel();
        }
        hashtable.remove(taskId);
        return true;
    }

    /**
     * 建立一个维护进程
     * 5分钟定时检查一次timer是否需要运行，或是定时时间已经改变
     */
    public boolean createMaintainTask() {
        if (!isStartMaintainTask()) {
            MaintainTask maintainTask = new MaintainTask();
            Timer timer = getMaintainTimer();
            timer.schedule(maintainTask, 0L, 60 * 1000L);
            setStartMaintainTask(true);
        }
        return isStartMaintainTask();
    }

    /**
	 * 
	 * 根据id把采集任务停止
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====停止任务=="+id);
    	
    	if(null!=nmsmemorydate.TaskList.get(id) )
    	{
    	((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//注销该任务
    	nmsmemorydate.TaskList.remove(id+"");
    	nmsmemorydate.RunGatherLinst.remove(id+"");
    	
    	}
    	
    }
	
    
    
	/**
	 * 
	 * 取消所有的采集任务
	 * 
	 * @param id
	 */
    public  void canceAlllTask()
    {
    	if(nmsmemorydate.TaskList.size()>0)
    	{
    	 Enumeration allvalue=nmsmemorydate.TaskList.elements(); 
    	 Enumeration key=nmsmemorydate.TaskList.keys();
		  while(allvalue.hasMoreElements())     
		  {     
		      
    	     //(Timer) allvalue.nextElement(); 
    	      String id=(String)key.nextElement();
    	    if(null!=(Timer) allvalue.nextElement() )
    	    {
    	    ((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//注销该任务
    	     nmsmemorydate.TaskList.remove(id);
    	     nmsmemorydate.RunGatherLinst.remove(id);
    	    }
    	    
		}
		  
		  nmsmemorydate.TaskList.clone();
		  nmsmemorydate.RunGatherLinst.clone();
    	}
	  logger.info("======完成注销采集任务=====");
    	
    }
    
	

    
    /**
     * 建立所有的采集任务
     * @param taskinterval 任务的采集频率
     * @param taskid 任务的id
     * @param taskname 任务的名称
     * @param tasktype 任务的类型
     * @param tasksubtype
     */
        public  void createOneTask(NodeGatherIndicators nodeGatherIndicators)
        {
            Timer timer=null;
            BaseTask btask=null;
             
            //根据Hashtable 中的参数来判断来判断启动采集的任务
            if(null!=nmsmemorydate.TaskList && nmsmemorydate.TaskList.size()>0&& nmsmemorydate.TaskList.containsKey(nodeGatherIndicators.getId()))
            {
                //停止原来的timer，列表并且从内存中删除对应的对象
                timer=(Timer)nmsmemorydate.TaskList.get(nodeGatherIndicators.getId());
                timer.cancel();
                nmsmemorydate.TaskList.remove(nodeGatherIndicators.getId());
                nmsmemorydate.RunGatherLinst.remove(nodeGatherIndicators.getId());
            }else
              {//建立定时采集任务
                timer = new Timer();
                btask=new BaseTask();
                btask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//运行类得路径
                btask.setTaskid(nodeGatherIndicators.getId()+"");
                btask.setNodeid((String)nodeGatherIndicators.getNodeid());
                btask.setTaskname(nodeGatherIndicators.getName());
                btask.setRunclasspath(nodeGatherIndicators.getClasspath());
                btask.setGather(nodeGatherIndicators);
                
                long intervaltime=Integer.parseInt(nodeGatherIndicators.getPoll_interval());
                if(nodeGatherIndicators.getInterval_unit().equals("m"))
                {
                    
                    intervaltime=intervaltime*1000*60;
                }
                if(nodeGatherIndicators.getInterval_unit().equals("h"))
                {
                    intervaltime=intervaltime*1000*60*60;
                }
                if(nodeGatherIndicators.getInterval_unit().equals("d"))
                {
                    intervaltime=intervaltime*1000*60*60*24;
                }
                
                long in=0;
                if(nmsmemorydate.TaskList.size()>300)
                {
                    in=(nmsmemorydate.TaskList.size()/5)*200;
                    
                }else
                {
                    in=nmsmemorydate.TaskList.size()*200;
                }
                
                timer.schedule(btask, in, intervaltime);//按分钟执行定时任务
                nmsmemorydate.TaskList.put(nodeGatherIndicators.getId()+"", timer);//把TIMER对象到任务队里
                nmsmemorydate.RunGatherLinst.put(nodeGatherIndicators.getId()+"", nodeGatherIndicators);
            }
            
        }

        

            
            
            /**
             * 数据分离模式入库定时任务
             * 1分钟入库一次
             */
                public void CreateDataTempTask()
                {
                    //if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
                    //{
                    Timer timer=null;
                    GatherDatatempsqlRun btask=null;
                    timer = new Timer();
                    btask=new GatherDatatempsqlRun();
                    timer.schedule(btask, 20000, 10*1000);//按分钟执行定时任务
                    
                    nmsmemorydate.GatherDatatempsqlTasktimer=timer;
                    //}
                        
               }
                
                
                /**
                 * 
                 * 垃圾回收
                 */
                    public void CreateGCTask()
                    {
                        //if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
                        //{
                        Timer timer=null;
                        GcTask btask=null;
                        timer = new Timer();
                        btask=new GcTask();
                        timer.schedule(btask, 20000, 5*1000*60);//按分钟执行定时任务
                        
                        //nmsmemorydate.GatherDatatempsqlTasktimer=timer;
                        //}
                            
                   }
                
            
            
            /**
             * 建立一个维护进程
             * 5分钟定时检查一次timer是否需要运行，或是定时时间已经改变
             */
                public void CreateGahterSQLTask()
                {
                    if(!nmsmemorydate.GathersqlTaskStatus)
                    {
                    Timer timer=null;
                    GathersqlRun btask=null;
                    timer = new Timer();
                    btask=new GathersqlRun();
                    timer.schedule(btask, 0, 5*1000);//5秒钟入库一次
                    nmsmemorydate.GathersqlTaskStatus=true;//设置标记为启动
                    nmsmemorydate.GathersqlTasktimer=timer;
                    }
                        
               }
	public static void main(String [] arg) {
	}

    /**
     * @return the timer
     */
    public Timer getAllCollectTaskTimer() {
        return allCollectTaskTimer;
    }

    /**
     * @param allCollectTaskTimer the allCollectTaskTimer to set
     */
    public void setAllCollectTaskTimer(Timer allCollectTaskTimer) {
        this.allCollectTaskTimer = allCollectTaskTimer;
    }

    /**
     * @return the isStartMaintainTask
     */
    public boolean isStartMaintainTask() {
        return isStartMaintainTask;
    }

    /**
     * @param isStartMaintainTask the isStartMaintainTask to set
     */
    public void setStartMaintainTask(boolean isStartMaintainTask) {
        this.isStartMaintainTask = isStartMaintainTask;
    }

    /**
     * @return the isStarted
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * @param isStarted the isStarted to set
     */
    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    /**
     * @return the maintainTimer
     */
    public Timer getMaintainTimer() {
        return maintainTimer;
    }

    /**
     * @param maintainTimer the maintainTimer to set
     */
    public void setMaintainTimer(Timer maintainTimer) {
        this.maintainTimer = maintainTimer;
    }

    /**
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * @param timerTaskHashtable the timerTaskHashtable to set
     */
    public void setTimerTaskHashtable(
            Hashtable<String, BaseTask> timerTaskHashtable) {
        this.timerTaskHashtable = timerTaskHashtable;
    }

    /**
     * @return the timerTaskHashtable
     */
    public Hashtable<String, BaseTask> getTimerTaskHashtable() {
        return timerTaskHashtable;
    }

    /**
     * @param instance the instance to set
     */
    public static void setInstance(TaskManager instance) {
        TaskManager.instance = instance;
    }

	
    
}
