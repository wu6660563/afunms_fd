package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;


/**
 * 
 * ��cpu�Ĳɼ��������temp��sql
 * 
 * @author konglq
 *
 */
public class NetHostDatatempCpuRTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    
	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
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
      //�Ƿ���������ģʽ
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");
        if (dataresult != null && dataresult.size() > 0) {
            Vector cpuVector = (Vector)dataresult.get("cpu");
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
            if(null!=cpuVector && cpuVector.size()>0) {
                CPUcollectdata vo = (CPUcollectdata) cpuVector.elementAt(0);
    
                String deleteSql = "delete from nms_cpu_data_temp where nodeid='" +node.getId() + "'";
                
                String hendsql="insert into nms_cpu_data_temp(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values(";
                String endsql=")";
                
                Calendar tempCal = (Calendar) vo.getCollecttime();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                Vector list=new Vector();
                StringBuffer sbuffer = new StringBuffer(200);
                sbuffer.append(hendsql);
                sbuffer.append("'").append(node.getId()).append("',");
                sbuffer.append("'").append(node.getIpAddress()).append("',");
                sbuffer.append("'").append(nodeDTO.getType()).append("',");
                sbuffer.append("'").append(nodeDTO.getSubtype()).append("',");
                sbuffer.append("'").append(vo.getCategory()).append("',");
                sbuffer.append("'").append(vo.getEntity()).append("',");
                sbuffer.append("'").append(vo.getSubentity()).append("',");
                sbuffer.append("'").append(vo.getThevalue()).append("',");
                sbuffer.append("'").append(vo.getChname()).append("',");
                sbuffer.append("'").append(vo.getRestype()).append("',");
                sbuffer.append("'").append(time).append("',");
                sbuffer.append("'").append(vo.getUnit()).append("',");
                sbuffer.append("'").append(vo.getBak()).append("'");
                sbuffer.append(endsql);
                
                DBManager manager = new DBManager();// ���ݿ�������
                try {
                    manager.addBatch(deleteSql);
                    manager.addBatch(sbuffer.toString());
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
