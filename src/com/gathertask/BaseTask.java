package com.gathertask;

/**
 * 该程序是用来实现按时间执行各种承载点或拨测点协议
 */
import java.lang.reflect.Method;
import java.util.TimerTask;

import com.afunms.common.util.threadPool.ThreadPoolRunnable;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.indicators.model.NodeGatherIndicators;

/**
 * 
 * @author konglq
 * 
 */
public class BaseTask extends TimerTask {

    private String runclasspath = "";
    private String runtime = "";
    private String runtype = "";
    private String taskid = "";
    private String taskname = "";
    private String nodeid = "";
    private long intervaltime = 0L;
    private NodeGatherIndicators gather = new NodeGatherIndicators();

    /**
     * 任务主线程 采集任务是用方法
     */
    public void run() {

        try {
            ThreadPoolRunnable threadPoolRunnable = new ThreadPoolRunnable(){

                    public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
                        return null;
                    }

                    public void runIt(
                            ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
                        try {
                            Object runner = Class.forName(runclasspath.trim()).newInstance();
                            Method mt = runner.getClass().getMethod("collect_Data",
                                    NodeGatherIndicators.class);
                            mt.invoke(runner, gather);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                
            TaskManager taskManager = TaskManager.getInstance();
            taskManager.getThreadPool().add(threadPoolRunnable);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public String getRunclasspath() {
        return runclasspath;
    }

    public void setRunclasspath(String runclasspath) {
        this.runclasspath = runclasspath;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getruntype() {
        return runtype;
    }

    public void setruntype(String runtype) {
        this.runtype = runtype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public NodeGatherIndicators getGather() {
        return gather;
    }

    public void setGather(NodeGatherIndicators gather) {
        this.gather = gather;
    }

    /**
     * @return the intervaltime
     */
    public long getIntervaltime() {
        return intervaltime;
    }

    /**
     * @param intervaltime
     *            the intervaltime to set
     */
    public void setIntervaltime(long intervaltime) {
        this.intervaltime = intervaltime;
    }

}
