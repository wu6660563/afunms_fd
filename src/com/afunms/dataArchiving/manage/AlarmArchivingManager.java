package com.afunms.dataArchiving.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.dataArchiving.dao.AlarmArchivingKindDao;
import com.afunms.dataArchiving.model.AlarmArchivingKind;

/**
 * 告警数据归档Manager类
 * 
 * @author Administrator
 *
 */
public class AlarmArchivingManager extends BaseManager implements ManagerInterface{

	public String execute(String action) {
		// TODO Auto-generated method stub
		if("list".equals(action)){
			return list();
		}
	    setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	public String list(){
		String jsp = "/alarm/archiving/alarmArchiving.jsp";
		AlarmArchivingKindDao dao = new AlarmArchivingKindDao();
		List<AlarmArchivingKind> list = new ArrayList<AlarmArchivingKind>();
		try{
			list = dao.getArchivingKind();
		}catch(Exception e){
			e.printStackTrace();
			list = null;
		}
		request.setAttribute("list", list);
		return jsp;
	}
	

}
