package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

import com.afunms.application.dao.DBDao;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class DBDataTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        DBDao dao = new DBDao();
        try {
            dao.processOracleData(dataresult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public void execute(String sql) {
        DBManager manager = new DBManager();// ���ݿ�������
        try {
            manager.executeUpdate(sql);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

}
