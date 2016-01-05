/**
 * <p>Description:action center,at the same time, the control legal power</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.alertalarm;

import java.io.FileOutputStream;

import com.afunms.common.util.ShareData;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.afunms.initialize.ResourceCenter;
import com.afunms.application.model.*;
import com.afunms.application.dao.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class CreateAlarmXml {
	private Hashtable sendeddata = ShareData.getSendeddata();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public void CreateAlarmXml(List alarmArray){
	   
	    //System.out.println("����CreateAlarmXml����");
		boolean ret = false;
		String indent = "	";		
		String fileName = "demo.xml";
		FileOutputStream fos = null;
		Element root = new Element("alarmTree");
		root.setAttribute("alarmNum", alarmArray.size()+"");  //�ô�Ӧ���Ǹ�ͳ�����ݣ�				
		for(int i=0;i<alarmArray.size();i++){
			AlarmInfo alarm = (AlarmInfo)alarmArray.get(i);
			if(alarm.getType()!= null && !alarm.getType().equalsIgnoreCase("null") && alarm.getType().trim().length()>0){
				if(alarm.getType().equalsIgnoreCase("grapes")){
					//������Ӧ�ĸ澯
					GrapesConfig grapesconfig = new GrapesConfig();
					GrapesConfigDao dao = new GrapesConfigDao();
					try{
						grapesconfig = dao.getGrapesByIp(alarm.getIpaddress());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						dao.close();
					}
					createSMS(alarm.getContent(),grapesconfig);
				}else if(alarm.getType().equalsIgnoreCase("radar")){
					//������Ӧ�ĸ澯
					RadarConfig radarconfig = new RadarConfig();
					RadarConfigDao dao = new RadarConfigDao();
					try{
						radarconfig = dao.getRadarByIp(alarm.getIpaddress());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						dao.close();
					}
					createRadarSMS(alarm.getContent(),radarconfig);
				}else if(alarm.getType().equalsIgnoreCase("plot")){
					//������Ӧ�ĸ澯
					PlotConfig plotconfig = new PlotConfig();
					PlotConfigDao dao = new PlotConfigDao();
					try{
						plotconfig = dao.getPlotByIp(alarm.getIpaddress());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						dao.close();
					}
					createPlotSMS(alarm.getContent(),plotconfig);
				}
			}
			
			
			//Vector tempV = (Vector)alarmArray.get(i);			
			Element alarmNode = new Element("alarmNode");
			alarmNode.setAttribute("ip", alarm.getIpaddress());
			alarmNode.setAttribute("level", alarm.getLevel1().toString());
			alarmNode.setAttribute("content", alarm.getContent());			
			root.addContent(alarmNode);
		}		
		
		Format format = Format.getCompactFormat();
		format.setEncoding("gb2312");
		format.setIndent(indent);
		XMLOutputter serializer = new XMLOutputter(format);
		try
		{
			
			fos = new FileOutputStream(ResourceCenter.getInstance().getSysPath() + fileName);
			Document doc = new Document(root);
			serializer.output(doc, fos);
			fos.close();
			serializer = null;
			fos = null;
			ret = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fos = null;
			serializer = null;
			ret = false;
		}
   }
   
	 public void createSMS(String content,GrapesConfig grapesconfig){
		 	//��������		 	
		 	//���ڴ����õ�ǰ���IP��PING��ֵ
		 	Calendar date=Calendar.getInstance();
		 	try{
	 			if (!sendeddata.containsKey(grapesconfig.getName()+":"+grapesconfig.getIpaddress())){
	 				//�����ڣ��������ţ�������ӵ������б���
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(date.getTime());
		 			smscontent.setLevel("2");
		 			smscontent.setObjid(grapesconfig.getId()+"");
		 			smscontent.setMessage(content);
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("grapes");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(grapesconfig.getIpaddress());
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendGrapesSmscontent(smscontent);	
					//sendeddata.put(grapesconfig.getName()+":"+grapesconfig.getIpaddress(),date);		 					 				
	 			}else{
	 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
	 				Calendar formerdate =(Calendar)sendeddata.get(grapesconfig.getName()+":"+grapesconfig.getIpaddress());		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			cc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(cc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();			 			
		 			if (subvalue/(1000*60*60*24)>=1){
		 				//����һ�죬���ٷ���Ϣ
		 				Smscontent smscontent = new Smscontent();
			 			String time = sdf.format(date.getTime());
			 			smscontent.setLevel("2");
			 			smscontent.setObjid(grapesconfig.getId()+"");
			 			smscontent.setMessage(content);
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype("grapes");
			 			smscontent.setSubentity("ping");
			 			smscontent.setIp(grapesconfig.getIpaddress());
			 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
			 			//���Ͷ���
			 			SmscontentDao smsmanager=new SmscontentDao();
			 			smsmanager.sendGrapesSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
						//sendeddata.put(grapesconfig.getName()+":"+grapesconfig.getIpaddress(),date);	
			 		}	
	 			}	 			 			 			 			 	
		 	}catch(Exception e){
		 		e.printStackTrace();
		 	}
		 }
	 public void createRadarSMS(String content,RadarConfig radarconfig){
		 	//��������		 	
		 	//���ڴ����õ�ǰ���IP��PING��ֵ
		 	Calendar date=Calendar.getInstance();
		 	try{
	 			if (!sendeddata.containsKey(radarconfig.getName()+":"+radarconfig.getIpaddress())){
	 				//�����ڣ��������ţ�������ӵ������б���
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(date.getTime());
		 			smscontent.setLevel("2");
		 			smscontent.setObjid(radarconfig.getId()+"");
		 			smscontent.setMessage(content);
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("radar");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(radarconfig.getIpaddress());
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendRadarSmscontent(smscontent);	
					//sendeddata.put(radarconfig.getName()+":"+radarconfig.getIpaddress(),date);		 					 				
	 			}else{
	 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
	 				Calendar formerdate =(Calendar)sendeddata.get(radarconfig.getName()+":"+radarconfig.getIpaddress());		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			cc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(cc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();			 			
		 			if (subvalue/(1000*60*60*24)>=1){
		 				//����һ�죬���ٷ���Ϣ
		 				Smscontent smscontent = new Smscontent();
			 			String time = sdf.format(date.getTime());
			 			smscontent.setLevel("2");
			 			smscontent.setObjid(radarconfig.getId()+"");
			 			smscontent.setMessage(content);
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype("radar");
			 			smscontent.setSubentity("ping");
			 			smscontent.setIp(radarconfig.getIpaddress());
			 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
			 			//���Ͷ���
			 			SmscontentDao smsmanager=new SmscontentDao();
			 			smsmanager.sendRadarSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
						//sendeddata.put(radarconfig.getName()+":"+radarconfig.getIpaddress(),date);	
			 		}	
	 			}	 			 			 			 			 	
		 	}catch(Exception e){
		 		e.printStackTrace();
		 	}
		 }
	 
	 public void createPlotSMS(String content,PlotConfig plotconfig){
		 	//��������		 	
		 	//���ڴ����õ�ǰ���IP��PING��ֵ
		 	Calendar date=Calendar.getInstance();
		 	try{
	 			if (!sendeddata.containsKey(plotconfig.getName()+":"+plotconfig.getIpaddress())){
	 				//�����ڣ��������ţ�������ӵ������б���
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(date.getTime());
		 			smscontent.setLevel("2");
		 			smscontent.setObjid(plotconfig.getId()+"");
		 			smscontent.setMessage(content);
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("plot");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(plotconfig.getIpaddress());
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendPlotSmscontent(smscontent);	
					//sendeddata.put(plotconfig.getName()+":"+plotconfig.getIpaddress(),date);		 					 				
	 			}else{
	 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
	 				Calendar formerdate =(Calendar)sendeddata.get(plotconfig.getName()+":"+plotconfig.getIpaddress());		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			cc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(cc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();			 			
		 			if (subvalue/(1000*60*60*24)>=1){
		 				//����һ�죬���ٷ���Ϣ
		 				Smscontent smscontent = new Smscontent();
			 			String time = sdf.format(date.getTime());
			 			smscontent.setLevel("2");
			 			smscontent.setObjid(plotconfig.getId()+"");
			 			smscontent.setMessage(content);
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype("plot");
			 			smscontent.setSubentity("ping");
			 			smscontent.setIp(plotconfig.getIpaddress());
			 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
			 			//���Ͷ���
			 			SmscontentDao smsmanager=new SmscontentDao();
			 			smsmanager.sendPlotSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
						//sendeddata.put(plotconfig.getName()+":"+plotconfig.getIpaddress(),date);	
			 		}	
	 			}	 			 			 			 			 	
		 	}catch(Exception e){
		 		e.printStackTrace();
		 	}
		 }

}
