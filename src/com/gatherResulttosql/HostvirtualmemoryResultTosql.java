package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Memorycollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostvirtualmemoryResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * 根据采集结果生成对应的sql放入到内存列表中
     */
    public void CreateResultTosql(Hashtable ipdata, String ip) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("ipdata", ipdata);
        attribute.setAttribute("ip", ip);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable ipdata = (Hashtable) attribute.getAttribute("ipdata");
        String ip = (String) attribute.getAttribute("ip");

        if (ipdata.containsKey("memory")) {
            Vector memoryVector = (Vector) ipdata.get("memory");

            String allipstr = SysUtil.doip(ip);

            if (memoryVector != null && memoryVector.size() > 0) {
                DBManager manager = new DBManager();
                try {
                    for (int si = 0; si < memoryVector.size(); si++) {
                        Memorycollectdata memorydata = (Memorycollectdata) memoryVector
                                .elementAt(si);
                        if (!memorydata.getSubentity().equalsIgnoreCase(
                                "VirtualMemory"))
                            continue;
                        if (memorydata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) memorydata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            String tablename = "memory" + allipstr;
                            String sql = "insert into "
                                    + tablename
                                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                                    + "values('" + ip + "','"
                                    + memorydata.getRestype() + "','"
                                    + memorydata.getCategory() + "','"
                                    + memorydata.getEntity() + "','"
                                    + memorydata.getSubentity() + "','"
                                    + memorydata.getUnit() + "','"
                                    + memorydata.getChname() + "','"
                                    + memorydata.getBak() + "',"
                                    + memorydata.getCount() + ",'"
                                    + memorydata.getThevalue() + "','" + time
                                    + "')";
                            manager.addBatch(sql);
                        }
                    }
                    manager.executeBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    manager.close();
                }
            }
        }

    }

}
