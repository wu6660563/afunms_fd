package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.afunms.common.util.SysUtil;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostPagingResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * 把cpu的采集数据成成sql放入的内存列表中
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

        Hashtable paginghash = (Hashtable) ipdata.get("paginghash");
        if (paginghash != null && paginghash.size() > 0) {
            if (paginghash.get("Percent_Used") != null) {
                String pused = ((String) paginghash.get("Percent_Used"))
                        .replaceAll("%", "");
                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                String allipstr = SysUtil.doip(ip);
                String tablename = "pgused" + allipstr;
                String sql = "insert into "
                        + tablename
                        + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                        + "values('" + ip + "','','','','','','','',0,'"
                        + pused + "','" + time + "')";
                execute(sql);
            }
        }

    }

    public void execute(String sql) {
        DBManager manager = new DBManager();// 数据库管理对象
        try {
            manager.executeUpdate(sql);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
