package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.CPUcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * 
 * 
 * 把网络设备的cpu采集结果生成sql语句
 * 
 * @author 网络设备cpu采集结果生成sql实现类
 * 
 */

public class NetcpuResultTosql implements ResultTosql {

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
        if (ipdata.containsKey("cpu")) {
            Vector cpuVector = (Vector) ipdata.get("cpu");
            if (cpuVector != null && cpuVector.size() > 0) {
                // 得到CPU平均
                CPUcollectdata cpudata = (CPUcollectdata) cpuVector
                        .elementAt(0);
                if (cpudata.getRestype().equals("dynamic")) {
                    Calendar tempCal = (Calendar) cpudata.getCollecttime();
                    Date cc = tempCal.getTime();
                    String time = sdf.format(cc);
                    String allipstr = SysUtil.doip(ip);
                    String tablename = "cpu" + allipstr;
                    long count = 0;
                    if (cpudata.getCount() != null) {
                        count = cpudata.getCount();
                    }
                    StringBuffer sql = new StringBuffer(150);
                    sql.append("insert into ");
                    sql.append(tablename);
                    sql.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,"
                            + "count,thevalue,collecttime)");
                    sql.append("values('");
                    sql.append(ip);
                    sql.append("','");
                    sql.append(cpudata.getRestype());
                    sql.append("','");
                    sql.append(cpudata.getCategory());
                    sql.append("','");
                    sql.append(cpudata.getEntity());
                    sql.append("','");
                    sql.append(cpudata.getSubentity());
                    sql.append("','");
                    sql.append(cpudata.getUnit());
                    sql.append("','");
                    sql.append(cpudata.getChname());
                    sql.append("','");
                    sql.append(cpudata.getBak());
                    sql.append("','");
                    sql.append(count);
                    sql.append("','");
                    sql.append(cpudata.getThevalue());
                    sql.append("','");
                    sql.append(time);
                    sql.append("')");
                    execute(sql.toString());
                }
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
