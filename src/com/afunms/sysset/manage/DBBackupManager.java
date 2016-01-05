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
		// 数据库 表 列表
		if ("list".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getTableList();			
		}
		// 备份文件列表
		if ("dbbackuplist".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getDBBackupList();
		}
		// 调用备份
		if("backup".equals(action))  
		{
			return backup();
		}
		// 调用导入备份
		if("load".equals(action))
		{
			return load();
		}
		// 删除备份
		if("delete".equals(action)){
			//return deleteDBBackupFile();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
		
	}
	//获取数据库中所有的表名
	private String getTableList()
	{
		DBBackupDao dao = new DBBackupDao();
		List<String> list = dao.findByCriteria("show tables");
		request.setAttribute("tablesname", list);
		return "/sysset/dbbackup/list.jsp";
	}
	
	/** 
	 * 获取 所有表的列表
	 * @return
	 */
	private String getDBBackupList(){
		DBBackupDao dao = new DBBackupDao();
		setTarget("/sysset/dbbackup/dbbackuplist.jsp");
		return list(dao);
	}
	
	/**
	 * 删除备份文件
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
			request.setAttribute("msg", "数据库备份文件删除成功！");
			return getDBBackupList();
		}else{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "数据库备份文件删除失败！");
	    	return null;
		}
	}
	
	/**
	 * 备份  如果备份成功后 则在数据库中添加记录
	 * 如果添加记录失败 则删除备份文件
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
			request.setAttribute("msg", "数据库备份成功！");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "数据库备份发生错误，请检查日志文件！");
	    	return null;
		}
		
	}
	/**
	 * 导入备份文件
	 * @return
	 */
	private String load(){
		DBBackupDao dao = new DBBackupDao();
		String filename = request.getParameter("filename");
		boolean result = dao.load(dao.getFilepath()+filename);
		if(result)
		{
			request.setAttribute("msg", "数据库导入成功！");
			request.setAttribute("result", "true");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "数据库导入发生错误，请检查日志文件！");
	    	return null;
		}
	}
	
	
	
	

}
