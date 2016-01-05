package com.afunms.sysset.manage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.sysset.dao.DBBackupDao;
import com.afunms.sysset.model.DBBackup;

public class DBBackupManager extends BaseManager implements ManagerInterface
{
	
	public String execute(String action) {
		// ���ݿ� �� �б�
		if ("list".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getTableList();			
		}
		// �����ļ��б�
		if ("dbbackuplist".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getDBBackupList();
		}
		// ���ñ���
		if("backup".equals(action))  
		{
			return backup();
		}
		// ���õ��뱸��
		if("load".equals(action))
		{
			return load();
		}
		// ɾ������
		if("delete".equals(action)){
			//return deleteDBBackupFile();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
		
	}
	//��ȡ���ݿ������еı���
	private String getTableList()
	{
		DBBackupDao dao = new DBBackupDao();
		List<String> list = dao.findByCriteria("show tables");
		request.setAttribute("tablesname", list);
		return "/sysset/dbbackup/list.jsp";
	}
	
	/** 
	 * ��ȡ ���б���б�
	 * @return
	 */
	private String getDBBackupList(){
		DBBackupDao dao = new DBBackupDao();
		setTarget("/sysset/dbbackup/dbbackuplist.jsp");
		return list(dao);
	}
	
	/**
	 * ɾ�������ļ�
	 * @return
	 */
	private String deleteDBBackupFile(){
		boolean result = false;
		String[] id = request.getParameterValues("id");
		List<String> list = new ArrayList<String>();
		DBBackupDao dao = new DBBackupDao();
		try{
			for(int i = 0 ; i< id.length;i++ ){
				DBBackup dbBackup = (DBBackup)dao.findByID(id[i]);
				list.add(dbBackup.getFilename());
			}
			result = dao.delete(id);
			result = true;
		}catch(Exception e){
			result = false;
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i = 0 ; i< list.size();i++ ){
			new File(dao.getFilepath() + list.get(i)).delete();
		}
		if(result){
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ���ļ�ɾ���ɹ���");
			return getDBBackupList();
		}else{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ���ļ�ɾ��ʧ�ܣ�");
	    	return null;
		}
	}
	
	/**
	 * ����  ������ݳɹ��� �������ݿ�����Ӽ�¼
	 * �����Ӽ�¼ʧ�� ��ɾ�������ļ�
	 * @return
	 */
	private String backup(){
		String[] tables = getParaArrayValue("checkbox");
		int radio = getParaIntValue("radio");
		DBBackupDao dao = new DBBackupDao();
		boolean result = dao.backup(tables,radio);
		if(result)
		{
			DBBackup dbBackup = new DBBackup();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String time = sdf.format(new Date());
			String filename = dao.getFilename();
			dbBackup.setFilename(filename+".sql");
			dbBackup.setTime(time);
			try{
				dao = new DBBackupDao();
				result = dao.save(dbBackup);
			}catch(Exception e){
				new File(dao.getFilepath() + filename+".sql").delete();
			}finally{
				dao.close();
			}
		}
		if(result)
		{
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ�ݳɹ���");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ�ݷ�������������־�ļ���");
	    	return null;
		}
		
	}
	/**
	 * ���뱸���ļ�
	 * @return
	 */
	private String load(){
		DBBackupDao dao = new DBBackupDao();
		String filename = request.getParameter("filename");
		boolean result = dao.load(dao.getFilepath()+filename);
		if(result)
		{
			request.setAttribute("msg", "���ݿ⵼��ɹ���");
			request.setAttribute("result", "true");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿ⵼�뷢������������־�ļ���");
	    	return null;
		}
	}
	
	
	
	

}
