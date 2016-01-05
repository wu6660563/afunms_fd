package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Diskcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostdiskResultosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

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
        String allipstr = SysUtil.doip(ip);

        if (ipdata.containsKey("disk")) {
            Vector diskVector = (Vector) ipdata.get("disk");
            if (diskVector != null && diskVector.size() > 0) {
                DBManager manager = new DBManager();
                try {
                    for (int si = 0; si < diskVector.size(); si++) {
                        Diskcollectdata diskdata = (Diskcollectdata) diskVector.elementAt(si);
                        if (diskdata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) diskdata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            String tablename = "diskincre" + allipstr;
                            String sql = "insert into "
                                    + tablename
                                    + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) "
                                    + "values('" + ip + "','"
                                    + diskdata.getCategory() + "','"
                                    + diskdata.getSubentity() + "','"
                                    + diskdata.getRestype() + "','"
                                    + diskdata.getEntity() + "','"
                                    + diskdata.getThevalue() + "','" + time
                                    + "','" + diskdata.getUnit() + "')";
                            manager.addBatch(sql);
                        }
                        if (diskdata.getEntity().equals("Utilization")) {
                            Calendar tempCal = (Calendar) diskdata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            String tablename = "disk" + allipstr;
                            String sql = "insert into "
                                    + tablename
                                    + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) "
                                    + "values('" + ip + "','"
                                    + diskdata.getCategory() + "','"
                                    + diskdata.getSubentity() + "','"
                                    + diskdata.getRestype() + "','"
                                    + diskdata.getEntity() + "','"
                                    + diskdata.getThevalue() + "','" + time
                                    + "','" + diskdata.getUnit() + "')";
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
