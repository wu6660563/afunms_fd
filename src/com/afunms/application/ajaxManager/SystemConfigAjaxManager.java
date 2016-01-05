package com.afunms.application.ajaxManager;

import net.sf.json.JSONObject;

import com.afunms.common.base.AjaxBaseManager;
import com.afunms.common.base.AjaxManagerInterface;
import com.afunms.common.util.ReflactUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.system.dao.SystemConfigDao;
import java.util.*;

public class SystemConfigAjaxManager extends AjaxBaseManager implements AjaxManagerInterface{

	public void execute(String action){
		if(action.equals("updateSystemconfigFlag")){
			Map<String,String> map = new HashMap<String,String>(); 
			String retMessage = "�޸ĳɹ�";
			//�޸����ݿ��е�ϵͳ����ģʽ  
			//�޸����ݿ��е���Դ����ʾģʽ
			String flagkey = getParaValue("flagkey");
			String flagvalue = getParaValue("flagvalue");
			SystemConfigDao systemConfigDao = new SystemConfigDao();
			try {
				boolean flag = systemConfigDao.updateSystemConfigByVariablenameAndValue(flagkey, flagvalue);
				if(!flag){
					retMessage = "�޸�ʧ��";
				}
				//�޸��ڴ��е�ϵͳ����ģʽ
				//�޸��ڴ��е���Դ����ʾģʽ
				//ͨ������ ִ��set����
				ReflactUtil.invokeSet(PollingEngine.getInstance(), flagkey, flagvalue);
			} catch (Exception e) {
				e.printStackTrace();
				retMessage = "�޸�ʧ��";
			} finally{
				systemConfigDao.close();
			}
			map.put("message", retMessage);
			JSONObject json = JSONObject.fromObject(map);
			out.print(json);
			out.flush();
		}
	}
}
