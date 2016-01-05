package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.IpMac;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetHostipmacRttosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
	/**
	 * 把结果生成sql
	 * @param dataresult 采集结果
	 * @param node 网元节点
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {
	    DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("dataresult", dataresult);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
	}

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");
        if(dataresult != null && dataresult.size()>0){
            Vector ipmacVector = (Vector)dataresult.get("ipmac");
            if(ipmacVector != null && ipmacVector.size()>0){
                String deleteSql = "delete from ipmac where relateipaddr='" + node.getIpAddress() + "'";
                execute(deleteSql);
                DBManager manager = new DBManager();
                try {
                    for(int i=0;i<ipmacVector.size();i++){
                        try{
                            IpMac ipmac = (IpMac) ipmacVector.elementAt(i); 
                            String mac = ipmac.getMac();
                            if(mac == null){
                                mac = "";
                            }
                            mac = CommonUtil.removeIllegalStr(mac);
                            String sqll = "";
                            String time = sdf.format(ipmac.getCollecttime().getTime());
                            sqll = "insert into ipmac(relateipaddr,ifindex,ipaddress,mac,collecttime,ifband,ifsms)values('";
                            sqll = sqll + ipmac.getRelateipaddr() + "','" + ipmac.getIfindex() + "','" + ipmac.getIpaddress() + "','";
                            sqll = sqll + new String(mac.getBytes(),"UTF-8") + "','" + time + "','" + ipmac.getIfband() + "','" + ipmac.getIfsms() + "')";                                  
                            manager.addBatch(sqll);
                        }catch(Exception e) {
                            
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
