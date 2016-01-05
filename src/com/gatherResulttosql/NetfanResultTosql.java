package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Interfacecollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetfanResultTosql implements ResultTosql {

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
	    if(ipdata.containsKey("fan")){
	        //风扇
	        String allipstr = SysUtil.doip(ip);
	        Vector fanVector = (Vector) ipdata.get("fan");
	        if (fanVector != null && fanVector.size() > 0) {
	            //得到风扇
	            Interfacecollectdata fandata = (Interfacecollectdata) fanVector.elementAt(0);
	            if (fandata.getRestype().equals("dynamic")) {
	                Calendar tempCal = (Calendar) fandata.getCollecttime();
	                Date cc = tempCal.getTime();
	                String time = sdf.format(cc);
	                String tablename = "fan" + allipstr;
	                long count = 0;
	                if(fandata.getCount() != null){
	                    count = fandata.getCount();
	                }
	                StringBuffer sBuffer = new StringBuffer();
	                sBuffer.append("insert into ");
	                sBuffer.append(tablename);
	                sBuffer.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) ");
	                sBuffer.append("values('");
	                sBuffer.append(ip);
	                sBuffer.append("','");
	                sBuffer.append(fandata.getRestype());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getCategory());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getEntity());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getSubentity());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getUnit());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getChname());
	                sBuffer.append("','");
	                sBuffer.append(fandata.getBak());
	                sBuffer.append("','");
	                sBuffer.append(count);
	                sBuffer.append("','");
	                sBuffer.append(fandata.getThevalue());
	                sBuffer.append("','");
	                sBuffer.append(time);
	                sBuffer.append("')");
	                execute(sBuffer.toString());
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
