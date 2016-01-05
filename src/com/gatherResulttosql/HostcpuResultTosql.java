package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
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
 * 将采集主机的cpu采集结果转换成sql并放到内存类表中
 * 
 * @author konglq
 * 
 */
public class HostcpuResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 
     * 把cpu的采集数据成成sql放入的内存列表中
     */
    public void CreateResultTosql(Hashtable ipdata, String ip) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("ipdata", ipdata);
        attribute.setAttribute("ip", ip);
        attribute.setAttribute("method", "CreateResultTosql");
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    /**
     * 
     * 把cpu的采集数据成成sql放入的内存列表中
     */
    public void CreateLinuxResultTosql(Hashtable ipdata, String ip) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("ipdata", ipdata);
        attribute.setAttribute("ip", ip);
        attribute.setAttribute("method", "CreateLinuxResultTosql");
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        String method = (String) attribute.getAttribute("method");
        if (method.equals("CreateLinuxResultTosql")) {
            excuteLinuxResultToDB(attribute);
            return;
        }
        String ip = (String) attribute.getAttribute("ip"); // ip
        Hashtable ipdata = (Hashtable) attribute.getAttribute("ipdata"); // ipdata
        String allipstr = SysUtil.doip(ip);
        if (ipdata != null) {
            // 处理主机设备的数据
            if (ipdata.containsKey("cpu")) {
                // CPU
                Vector cpuVector = (Vector) ipdata.get("cpu");
                if (cpuVector != null && cpuVector.size() > 0) {
                    // 得到CPU平均
                    CPUcollectdata cpudata = (CPUcollectdata) cpuVector
                            .elementAt(0);
                    if (cpudata.getRestype().equals("dynamic")) {
                        Calendar tempCal = (Calendar) cpudata.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        String tablename = "cpu" + allipstr;
                        Long count = cpudata.getCount();
                        if (count == null) {
                            count = 0L;
                        }
                        String sql = "insert into "
                                + tablename
                                + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                                + "values('" + ip + "','"
                                + cpudata.getRestype() + "','"
                                + cpudata.getCategory() + "','"
                                + cpudata.getEntity() + "','"
                                + cpudata.getSubentity() + "','"
                                + cpudata.getUnit() + "','"
                                + cpudata.getChname() + "','"
                                + cpudata.getBak() + "'," + count
                                + ",'" + cpudata.getThevalue() + "','" + time
                                + "')";
                        execute(sql);
                    }
                }
            }
        }
    }

    public void excuteLinuxResultToDB(DBAttribute attribute) {
        String ip = (String) attribute.getAttribute("ip"); // ip
        Hashtable ipdata = (Hashtable) attribute.getAttribute("ipdata"); // ipdata

        CPUcollectdata cpudata = null;
        String allipstr = SysUtil.doip(ip);

        Vector cpuVector = (Vector) ipdata.get("cpu");
        if (cpuVector != null && cpuVector.size() > 0) {
            Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ip);
            if (ipAllData == null)
                ipAllData = new Hashtable();
            ipAllData.put("cpu", cpuVector);
            ShareData.getSharedata().put(ip, ipAllData);
            // 得到CPU平均
            cpudata = (CPUcollectdata) cpuVector.elementAt(0);
            if (cpudata.getRestype().equals("dynamic")) {
                Calendar tempCal = (Calendar) cpudata.getCollecttime();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                String tablename = "cpu" + allipstr;
                Long count = cpudata.getCount();
                if (count == null) {
                    count = 0L;
                }
                String sql = "insert into "
                        + tablename
                        + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                        + "values('" + ip + "','" + cpudata.getRestype()
                        + "','" + cpudata.getCategory() + "','"
                        + cpudata.getEntity() + "','" + cpudata.getSubentity()
                        + "','" + cpudata.getUnit() + "','"
                        + cpudata.getChname() + "','" + cpudata.getBak() + "',"
                        + count + ",'" + cpudata.getThevalue()
                        + "','" + time + "')";
                execute(sql);
            }

        }
        // CPU详细信息
        List cpuperflist = (List) ipdata.get("cpuperflist");
        if (cpuperflist != null && cpuperflist.size() > 0) {
            Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ip);
            if (ipAllData == null)
                ipAllData = new Hashtable();
            ipAllData.put("cpuperflist", cpuperflist);
            ShareData.getSharedata().put(ip, ipAllData);
            Hashtable cpuperfhash = (Hashtable) cpuperflist.get(0);
            String[] items1 = { "usr", "sys", "wio", "idle" };
            String[] items2 = { "user", "nice", "system", "iowait", "steal",
                    "idle" };
            String nice = (String) cpuperfhash.get("%nice");
            if (cpudata != null) {
                Calendar tempCal = (Calendar) cpudata.getCollecttime();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                String tablename = "cpudtl" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager.prepareStatement(sql);
                    if (nice == null || nice.equalsIgnoreCase("null")) {
                        String values1[] = new String[4];
                        values1[0] = (String) cpuperfhash.get("%usr");
                        values1[1] = (String) cpuperfhash.get("%sys");
                        values1[2] = (String) cpuperfhash.get("%wio");
                        values1[3] = (String) cpuperfhash.get("%idle");
                        for (int i = 0; i < items1.length; i++) {
                            Long count = cpudata.getCount();
                            if (count == null) {
                                count = 0L;
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, cpudata.getRestype());
                            preparedStatement.setString(3, cpudata.getCategory());
                            preparedStatement.setString(4, cpudata.getEntity());
                            preparedStatement.setString(5, items1[i]);
                            preparedStatement.setString(6, cpudata.getUnit());
                            preparedStatement.setString(7, cpudata.getChname());
                            preparedStatement.setString(8, cpudata.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, values1[i]);
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    } else {
                        // Linux
                        String values2[] = new String[6];
                        values2[0] = (String) cpuperfhash.get("%user");
                        values2[1] = (String) cpuperfhash.get("%nice");
                        values2[2] = (String) cpuperfhash.get("%system");
                        values2[3] = (String) cpuperfhash.get("%iowait");
                        values2[4] = (String) cpuperfhash.get("%steal");
                        values2[5] = (String) cpuperfhash.get("%idle");
                        for (int i = 0; i < items2.length; i++) {
                            if (values2[4] == null
                                    || values2[4].equalsIgnoreCase("null")) {
                                continue;
                            }
                            Long count = cpudata.getCount();
                            if (count == null) {
                                count = 0L;
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, cpudata.getRestype());
                            preparedStatement.setString(3, cpudata.getCategory());
                            preparedStatement.setString(4, cpudata.getEntity());
                            preparedStatement.setString(5, items2[i]);
                            preparedStatement.setString(6, cpudata.getUnit());
                            preparedStatement.setString(7, cpudata.getChname());
                            preparedStatement.setString(8, cpudata.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, values2[i]);
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    manager.close();
                }
            }

        }
    }

    public void execute(String sql) {
        DBManager pollmg = new DBManager();// 数据库管理对象
        try {
            pollmg.executeUpdate(sql);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            pollmg.close();
        }
    }

}
