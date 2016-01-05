/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Hashtable;

import net.sf.hibernate.*;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.CommonUtil;
import com.afunms.topology.dao.HostNodeDao;
import org.apache.commons.beanutils.BeanUtils;
import com.afunms.polling.snmp.Hostlastcollectdata;
import com.afunms.polling.snmp.CiscoSnmp;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.*;

import com.afunms.polling.node.Host;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.alertalarm.CreateAlarmXml;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AlarmUpdateTask extends TimerTask
{

	//ȡ��ȥ10�����ڷ����ĸ澯��Ϣ
	public void run()
	{
		Connection con = null;			
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sendcalen = Calendar.getInstance();
 		Date cc = sendcalen.getTime();
 		String curdate = formatter.format(cc);			 			
		
		
		String dateStr = CommonUtil.getCurrentDate();  //ȡ��ǰ����
		String endTime = CommonUtil.getCurrentTime();  //ȡ��ǰʱ��
		String beginTime = CommonUtil.getLaterTenSecondTime();  //ȡ��ȥ10���ʱ��
		
		String fromTime = curdate+" "+beginTime;
		String toTime = curdate+" "+endTime;
		AlarmInfoDao alarmdao = new AlarmInfoDao();
		String queryStr = "select ipaddress,level1,content,type from nms_alarminfo where level1 = '2' and recordtime >='"+fromTime+"' and recordtime<='"+toTime+"' ";
		List list = new ArrayList();
		try{
			list = alarmdao.findByCriteria(queryStr);
		}catch(Exception ex){
		}finally{
			try{
				alarmdao.close();
			}catch(Exception exp){
				exp.printStackTrace();
			}
		}

		
		//String queryStr = "select Ipaddress,AlarmData,AlarmTime,SeverityoftheAlarm,SourceOftheAlarm,AlarmDetailInfor from dbo.alarminfor where AlarmData = '"+dateStr.trim()+"' and AlarmTime BETWEEN  '"+beginTime.trim()+"' AND '"+endTime.trim()+"'";
		//System.out.println("�����澯��"+queryStr);
		//String[][] tmpResults = ResObjsContainer.persistService.executeQuery(queryStr, connection); //���µ�ʱ��εı�����Ϣ�Ĳ�ѯ,�ݶ�Ϊȡ���ؼ���ĸ澯
        if(list !=null && list.size()>0){        	
        	CreateAlarmXml cax = new CreateAlarmXml();
        	cax.CreateAlarmXml(list);  //���ɸ澯��Ϣ��XML�ļ�
        }
	}
}
