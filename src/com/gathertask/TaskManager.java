/**
 * 
 * ����XML��TASK��Ϣ�������������
 * ����������󱣴����б�TaskList��
 * 
 * һ��������ʱ�������
 * һ���ǿ��Դ������б��а��������ע��
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
     * ��־
     */
	private static SysLogger logger = SysLogger.getLogger(TaskManager.class.getName());

	/**
	 * �ɼ��������ʵ��
	 */
	private static TaskManager instance;

	/**
     * ���вɼ�����ļ�ʱ��
     */
    private Timer allCollectTaskTimer = new Timer();

    /**
     * ά������ļ�ʱ��
     */
    private Timer maintainTimer = new Timer();

    /**
     * �ɼ������Ƿ�����
     */
    private boolean isStarted = false;
 
    /**
     * ά�������Ƿ�����
     */
    private boolean isStartMaintainTask = false;

    /**
     * ���ڴ洢����������ʱ���Ĳɼ�����
     */
	private Hashtable<String, BaseTask> timerTaskHashtable = new Hashtable<String, BaseTask>(); 

	/**
	 * �ɼ�������̳߳�
	 */
	private ThreadPool threadPool = new ThreadPool();

	/**
	 * ˽�еĹ��췽ʽ���ڵ���ģʽ
	 */
	private TaskManager() {
	}

	/**
	 * ��ȡ���������ʵ��
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
	 * �������е�����
	 * @author ����
	 */
    public void addAllNodeGatherIndicatorsToTimer() {
		Taskdao taskdao = new Taskdao();
		Hashtable runtask = taskdao.GetRunTaskList();
		if (null != runtask) {
		    //�����Ϊ����ѭ��
    	    if (logger.isInfoEnabled()) {
                logger.info("���ݿ���Ҫ�ɼ��ɼ�ָ�����Ϊ��" + runtask.size());
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
     * ͨ���ɼ�ָ����������
     * @param nodeGatherIndicators
     * @return
     */
    public BaseTask createBaseTask(NodeGatherIndicators nodeGatherIndicators) {
        BaseTask baseTask = new BaseTask();
        baseTask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//�������·��
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
     * ������Ӳɼ���������ʱ���У�������ӳɹ���
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
     * ��Ӳɼ���������ʱ���У��ɹ��򷵻� true
     * @param BaseTask
     * @return
     */
    private boolean addBaseTaskToTimer(BaseTask baseTask) {
        // �ȴӼ�ʱ����ɾ���òɼ�����
        deleteBaseTaskFromTimer(baseTask);
        // ��������ʱ����
        Hashtable<String, BaseTask> hashtable = getTimerTaskHashtable();
        hashtable.put(baseTask.getTaskid(), baseTask);
        Timer timer = getAllCollectTaskTimer();
        timer.schedule(baseTask, 0, baseTask.getIntervaltime());
        return true;
    }

    /**
     * �Ӽ�ʱ����ɾ���ɼ����񣬳ɹ��򷵻� true
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
     * �Ӽ�ʱ����ɾ���ɼ����񣬳ɹ��򷵻� true
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
     * ����һ��ά������
     * 5���Ӷ�ʱ���һ��timer�Ƿ���Ҫ���У����Ƕ�ʱʱ���Ѿ��ı�
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
	 * ����id�Ѳɼ�����ֹͣ
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====ֹͣ����=="+id);
    	
    	if(null!=nmsmemorydate.TaskList.get(id) )
    	{
    	((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//ע��������
    	nmsmemorydate.TaskList.remove(id+"");
    	nmsmemorydate.RunGatherLinst.remove(id+"");
    	
    	}
    	
    }
	
    
    
	/**
	 * 
	 * ȡ�����еĲɼ�����
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
    	    ((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//ע��������
    	     nmsmemorydate.TaskList.remove(id);
    	     nmsmemorydate.RunGatherLinst.remove(id);
    	    }
    	    
		}
		  
		  nmsmemorydate.TaskList.clone();
		  nmsmemorydate.RunGatherLinst.clone();
    	}
	  logger.info("======���ע���ɼ�����=====");
    	
    }
    
	

    
    /**
     * �������еĲɼ�����
     * @param taskinterval ����Ĳɼ�Ƶ��
     * @param taskid �����id
     * @param taskname ���������
     * @param tasktype ���������
     * @param tasksubtype
     */
        public  void createOneTask(NodeGatherIndicators nodeGatherIndicators)
        {
            Timer timer=null;
            BaseTask btask=null;
             
            //����Hashtable �еĲ������ж����ж������ɼ�������
            if(null!=nmsmemorydate.TaskList && nmsmemorydate.TaskList.size()>0&& nmsmemorydate.TaskList.containsKey(nodeGatherIndicators.getId()))
            {
                //ֹͣԭ����timer���б��Ҵ��ڴ���ɾ����Ӧ�Ķ���
                timer=(Timer)nmsmemorydate.TaskList.get(nodeGatherIndicators.getId());
                timer.cancel();
                nmsmemorydate.TaskList.remove(nodeGatherIndicators.getId());
                nmsmemorydate.RunGatherLinst.remove(nodeGatherIndicators.getId());
            }else
              {//������ʱ�ɼ�����
                timer = new Timer();
                btask=new BaseTask();
                btask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//�������·��
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
                
                timer.schedule(btask, in, intervaltime);//������ִ�ж�ʱ����
                nmsmemorydate.TaskList.put(nodeGatherIndicators.getId()+"", timer);//��TIMER�����������
                nmsmemorydate.RunGatherLinst.put(nodeGatherIndicators.getId()+"", nodeGatherIndicators);
            }
            
        }

        

            
            
            /**
             * ���ݷ���ģʽ��ⶨʱ����
             * 1�������һ��
             */
                public void CreateDataTempTask()
                {
                    //if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
                    //{
                    Timer timer=null;
                    GatherDatatempsqlRun btask=null;
                    timer = new Timer();
                    btask=new GatherDatatempsqlRun();
                    timer.schedule(btask, 20000, 10*1000);//������ִ�ж�ʱ����
                    
                    nmsmemorydate.GatherDatatempsqlTasktimer=timer;
                    //}
                        
               }
                
                
                /**
                 * 
                 * ��������
                 */
                    public void CreateGCTask()
                    {
                        //if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
                        //{
                        Timer timer=null;
                        GcTask btask=null;
                        timer = new Timer();
                        btask=new GcTask();
                        timer.schedule(btask, 20000, 5*1000*60);//������ִ�ж�ʱ����
                        
                        //nmsmemorydate.GatherDatatempsqlTasktimer=timer;
                        //}
                            
                   }
                
            
            
            /**
             * ����һ��ά������
             * 5���Ӷ�ʱ���һ��timer�Ƿ���Ҫ���У����Ƕ�ʱʱ���Ѿ��ı�
             */
                public void CreateGahterSQLTask()
                {
                    if(!nmsmemorydate.GathersqlTaskStatus)
                    {
                    Timer timer=null;
                    GathersqlRun btask=null;
                    timer = new Timer();
                    btask=new GathersqlRun();
                    timer.schedule(btask, 0, 5*1000);//5�������һ��
                    nmsmemorydate.GathersqlTaskStatus=true;//���ñ��Ϊ����
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
