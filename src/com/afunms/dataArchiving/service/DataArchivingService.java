package com.afunms.dataArchiving.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAbstract;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.dataArchiving.dao.DataArchivingDao;
import com.afunms.dataArchiving.dao.DataArchivingHistoryDao;
import com.afunms.dataArchiving.dao.DataArchivingNodeDao;
import com.afunms.dataArchiving.model.DataArchiving;
import com.afunms.dataArchiving.model.DataArchivingHistory;
import com.afunms.dataArchiving.model.DataArchivingNode;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

public class DataArchivingService {

    private static SysLogger logger = SysLogger.getLogger(DataArchivingService.class.getName());
    
    public static List<String> TABLE_NAME_LIST = new ArrayList<String>();

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    static {
        String performanceDataTableFile = "WEB-INF/classes/performanceDataTable.xml";
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new File(ResourceCenter.getInstance().getSysPath() + performanceDataTableFile));
            List<Element> tableNames = (List<Element>) doc.getRootElement().getChildren("table-name");
            for (Element element : tableNames) {
                TABLE_NAME_LIST.add(element.getText());
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    private boolean isStarted = false;

    private DataArchivingCalculateRunnable dataArchivingCalculateRunnable = null;

    private DataArchivingDeleteRunnable dataArchivingDeleteRunnable = null;

    private DataArchivingCalculate dataArchivingCalculate = null;

    private DataArchivingDelete dataArchivingDelete = null;

    private DataArchivingMonitorRunnable dataArchivingMonitorRunnable = null;

    private Timer timer = null;

    public DataArchivingService() {
        setDataArchivingCalculate(new DataArchivingCalculate(this));
        setDataArchivingDelete(new DataArchivingDelete(this));
        setDataArchivingCalculateRunnable(new DataArchivingCalculateRunnable(this));
        setDataArchivingDeleteRunnable(new DataArchivingDeleteRunnable(this));
        setDataArchivingMonitorRunnable(new DataArchivingMonitorRunnable(this));
        timer = new Timer();
    }

    public boolean start() {
        if (!isStarted()) {
            logger.info("开始数据归档服务!!!");
            getDataArchivingCalculateRunnable().start();
            getDataArchivingDeleteRunnable().start();
            getDataArchivingMonitorRunnable().start();
            TimerTask timerTask = new TimerTask() {
                
                @Override
                public void run() {
                    getDataArchivingCalculateRunnable().runIt();
                    getDataArchivingDeleteRunnable().runIt();
                }
            };
            timer.schedule(timerTask, 2000L, 60 * 60 * 1000L);
            setStarted(true);
        }
        return isStarted();
    }

    public void calculate() {
        dataArchivingCalculate.execute();
    }

    public void delete() {
        dataArchivingDelete.execute();
    }

    public List<DataArchiving> getAllDataArchiving() {
        List<DataArchiving> list = null;
        DataArchivingDao dao = new DataArchivingDao();
        try {
            list = dao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (list == null) {
            list = new ArrayList<DataArchiving>();
        }
        return list;
    }

    public DataArchiving getDataArchivingById(String dataArchivingId) {
        DataArchiving DataArchiving = null;
        DataArchivingDao dao = new DataArchivingDao();
        try {
            DataArchiving = (DataArchiving) dao.findByID(dataArchivingId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return DataArchiving;
    }

    public List<DataArchiving> getChildDataArchiving(String dataArchivingId) {
        List<DataArchiving> list = null;
        DataArchivingDao dataArchivingDao = new DataArchivingDao();
        try {
            list = dataArchivingDao.getChildDataArchiving(dataArchivingId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataArchivingDao.close();
        }
        if (list == null) {
            list = new ArrayList<DataArchiving>();
        }
        return list;
    }

    public void updateDataArchiving(DataArchiving dataArchiving) {
        DataArchivingDao dataArchivingDao = new DataArchivingDao();
        try {
            dataArchivingDao.update(dataArchiving);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataArchivingDao.close();
        }
    }

    public List<BaseVo> getAllHostNode() {
        List<BaseVo> list = null;
        HostNodeDao dao = new HostNodeDao();
        try {
            list = dao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (list == null) {
            list = new ArrayList<BaseVo>();
        }
        return list;
    }

    public List<NodeDTO> getNodeDTOList() {
        NodeUtil nodeUtil = new NodeUtil();
        return nodeUtil.conversionToNodeDTO(getAllHostNode());
    }

    public List<DataArchivingNode> getDataArchivingNodeList(String dataArchivingId, String nodeId, String nodeType, String nodeSubtype) {
        List<DataArchivingNode> list = null;
        DataArchivingNodeDao LastDataArchivingNodeDao = new DataArchivingNodeDao();
        try {
            list = LastDataArchivingNodeDao.findByNode(dataArchivingId, nodeId, nodeType, nodeSubtype);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            LastDataArchivingNodeDao.close();
        }
        return list;
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
    private void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    /**
     * @return the archivingRunnable
     */
    public DataArchivingCalculateRunnable getDataArchivingCalculateRunnable() {
        return dataArchivingCalculateRunnable;
    }

    /**
     * @param archivingRunnable the archivingRunnable to set
     */
    public void setDataArchivingCalculateRunnable(DataArchivingCalculateRunnable dataArchivingCalculateRunnable) {
        this.dataArchivingCalculateRunnable = dataArchivingCalculateRunnable;
    }

    /**
     * @return the dataArchivingDeleteRunnable
     */
    public DataArchivingDeleteRunnable getDataArchivingDeleteRunnable() {
        return dataArchivingDeleteRunnable;
    }

    /**
     * @param dataArchivingDeleteRunnable the dataArchivingDeleteRunnable to set
     */
    public void setDataArchivingDeleteRunnable(
                    DataArchivingDeleteRunnable dataArchivingDeleteRunnable) {
        this.dataArchivingDeleteRunnable = dataArchivingDeleteRunnable;
    }

    /**
     * @return the dataArchivingCalculate
     */
    public DataArchivingCalculate getDataArchivingCalculate() {
        return dataArchivingCalculate;
    }

    /**
     * @param dataArchivingCalculate the dataArchivingCalculate to set
     */
    public void setDataArchivingCalculate(
                    DataArchivingCalculate dataArchivingCalculate) {
        this.dataArchivingCalculate = dataArchivingCalculate;
    }

    /**
     * @return the dataArchivingDelete
     */
    public DataArchivingDelete getDataArchivingDelete() {
        return dataArchivingDelete;
    }

    /**
     * @param dataArchivingDelete the dataArchivingDelete to set
     */
    public void setDataArchivingDelete(DataArchivingDelete dataArchivingDelete) {
        this.dataArchivingDelete = dataArchivingDelete;
    }

    /**
     * @return the dataArchivingMonitorRunnable
     */
    public DataArchivingMonitorRunnable getDataArchivingMonitorRunnable() {
        return dataArchivingMonitorRunnable;
    }

    /**
     * @param dataArchivingMonitorRunnable the dataArchivingMonitorRunnable to set
     */
    public void setDataArchivingMonitorRunnable(
                    DataArchivingMonitorRunnable dataArchivingMonitorRunnable) {
        this.dataArchivingMonitorRunnable = dataArchivingMonitorRunnable;
    }

}


