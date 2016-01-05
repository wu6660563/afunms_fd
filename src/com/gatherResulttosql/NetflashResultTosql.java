package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Flashcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetflashResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");	

    /**
	 * 
	 * 把采集数据生成sql放入的内存列表中
	 */
	public void CreateResultTosql(Hashtable ipdata,String ip) {
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
        if(ipdata.containsKey("flash")){
            Vector flashVector = (Vector) ipdata.get("flash");
            String allipstr = SysUtil.doip(ip);
            if (flashVector != null && flashVector.size() > 0) {
                Flashcollectdata flashdata = (Flashcollectdata) flashVector.elementAt(0);
                if (flashdata.getRestype().equals("dynamic")) {
                    Calendar tempCal = (Calendar) flashdata.getCollecttime();
                    Date cc = tempCal.getTime();
                    String time = sdf.format(cc);
                    String tablename = "flash" + allipstr;
                    long count = 0;
                    if(flashdata.getCount() != null){
                        count = flashdata.getCount();
                    }
                    StringBuffer sql = new StringBuffer(200);
                    sql.append("insert into ");
                    sql.append(tablename);
                    sql.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) ");
                    sql.append("values('");
                    sql.append(ip);
                    sql.append("','");
                    sql.append(flashdata.getRestype());
                    sql.append("','");
                    sql.append(flashdata.getCategory());
                    sql.append("','");
                    sql.append(flashdata.getEntity());
                    sql.append("','");
                    sql.append(flashdata.getSubentity());
                    sql.append("','");
                    sql.append(flashdata.getUnit());
                    sql.append("','");
                    sql.append(flashdata.getChname());
                    sql.append("','");
                    sql.append(flashdata.getBak());
                    sql.append("','");
                    sql.append(count);
                    sql.append("','");
                    sql.append(flashdata.getThevalue());
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
