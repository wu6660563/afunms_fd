/**
 * <p>Description:utility class,includes some methods which are often used</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.text.ParseException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jdom.Element;

import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;

public class SysUtil
{
   private SysUtil()
   {	   
   }

   /**
    * תΪ����
    */
   public static String getChinese(String ss)
   {
      String strpa = "";
      try
      {
         if (ss != null)
            strpa = new String(ss.getBytes("ISO-8859-1"), "GB2312");
         else
            strpa = ss;
      }
      catch (Exception e)
      {
         strpa = "";
      }
      return strpa;
   }

   /**
    * �õ���ǰ����
    */
   public static String getCurrentDate()
   {
       SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd");
       String currentDate = timeFormatter.format(new java.util.Date());
       return currentDate;
   }

   /**
    * �õ���ǰʱ��
    */
   public static String getCurrentTime()
   {
       SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String currentTime = timeFormatter.format(new java.util.Date());
       return currentTime;
   }

  /**
   * �õ���ǰ���
   */
   public static int getCurrentYear()
   {
       Calendar cal = new GregorianCalendar();
       return cal.get(Calendar.YEAR);
   }

  /**
   * �õ���ǰ�·�
   */
   public static int getCurrentMonth()
   {
       Calendar cal = new GregorianCalendar();
       return cal.get(Calendar.MONTH) + 1;
   }

   /**
    * ��null�滻��""
    */
   public static String ifNull(String str)
   {
       if (str == null||str.equals("null"))
          return "";
       else
          return str;
   }

   /**
    * ��null�滻�ɱ���ַ�
    */
   public static String ifNull(String str, String replaceStr)
   {
       if (str == null || "".equals(str))
          return replaceStr;
       else
          return str;
   }

  /**
   * ��һ��������ת����ʱ��
   */
   public static String longToTime(long timeLong)
   {
       Date date = new Date(timeLong);
       SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return timeFormatter.format(date);
   }

  /**
   * ��һ������"yyyy-mm-dd hh24:mi:ss"ʱ����ת����һ��������
   */
   public static long timeToLong(String time)
   {
      long timeLong = 0;
      try
      {
         SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         Date date = dateFormat.parse(time);
         timeLong = date.getTime() / 1000;
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }
      return timeLong;
   }

   /**
    * ��������ʱ�����ʱ���
    */
   public static String diffTwoTime(String time1,String time2)
   {
      if(time1==null||time2==null)
         return "";

      StringBuffer timeStr = new StringBuffer(10);
      long diffTime = timeToLong(time2) - timeToLong(time1);
      long hh24 = diffTime/3600;
      long surplus = diffTime%3600;

      long mi = surplus/60;
      long ss = surplus%60;

      if(hh24<10) timeStr.append("0");
      timeStr.append(hh24);

      timeStr.append(":");
      if(mi<10) timeStr.append("0");
      timeStr.append(mi);

      timeStr.append(":");
      if(ss<10) timeStr.append("0");
      timeStr.append(ss);

      return timeStr.toString();
   }   
   
   /**
    * �õ����ڵ�ʱ��,�ó����ͱ�ʾ
    */
   public static long getCurrentLongTime()
   {
	   return (long)(new java.util.Date()).getTime()/1000;	   
   }
      
   /**
    * �õ���ǰСʱ
    */
   public static int getCurrentHour()
   {
      Calendar cal = Calendar.getInstance();
      int curHour = cal.get(Calendar.HOUR_OF_DAY);
      return curHour;
   }
   
   public static String formatString(String s)
   {
       if(s == null || s.equals(""))
           return s;
       
       StringBuffer stringbuffer = new StringBuffer();
       for(int i = 0; i <= s.length() - 1; i++)
          if(s.charAt(i) == '\r')
             stringbuffer = stringbuffer.append("<br>");
          else
             stringbuffer = stringbuffer.append(s.substring(i, i + 1));

      return stringbuffer.toString();
   } 
   
   /**
    * һ�����м���
    */
   public static int getDaysOfMonth(int year,int month)
   {
      if(year<1000||year>3000||month<1||month>12)
        return 0;

      int days = 0;
      if(month==1||month==3||month==5||month==7||month==8||month==10||month==12)
         days = 31;
      else if(month==4||month==6||month==9||month==11)
         days = 30;
      else //month==2
      {
         if(year%400==0||year%4==0&&year%100!=0)
           days = 29;
        else
           days = 28;
      }
      return days;
   }
   
   /**
    * �õ�ĳ�������
    * interval ����������
    */
   public static String getDate(int interval)
   {
	   Calendar cal = Calendar.getInstance();
  	   cal.add(Calendar.DATE,interval);
   	   String date = new java.sql.Date(cal.getTimeInMillis()).toString();
   	   return date;
   }
   
   public static double formatDouble(double a, double b)
   {
	   if(b==0) return 0;
	   
	   DecimalFormat df = new DecimalFormat("#.00");
	   return Double.parseDouble(df.format(a/b));
   }
   
   public static String getLongID()
   {
	   return String.valueOf((long)((new java.util.Date()).getTime() * Math.random()));
   }
   
   /**
    * ��һ������"yyyy-MM-dd"������ת����һ��������
    */
   public static long dateToLong(String date)
   {
      long timeLong = 0;
      try
      {
          SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
          Date dateTime = dateFormat.parse(date);
          timeLong = dateTime.getTime() / 1000;
      }
      catch (ParseException e)
      {
          timeLong = 0;
      }
      return timeLong;
   }  
   
   /**
    * ������������֮������� 
    */
   public static int getDaysBetweenTwoDates(long beginDate,long endDate)
   {
       long result = 0;
	   if(beginDate>endDate)
		  result = (beginDate - endDate)/86400;
       else
          result = (endDate - beginDate)/86400;
	   return (int)result;
   }
   public static String doip(String ip){
//		  String newip="";
//		  for(int i=0;i<3;i++){
//			int p=ip.indexOf(".");
//			newip+=ip.substring(0,p);
//			ip=ip.substring(p+1);
//		  }
//		 newip+=ip;
		 //System.out.println("newip="+newip);
	   	 ip = ip.replaceAll("\\.", "_");
		 return ip;
	}
   public static List checkSize(String sizestr){
	   List rvalue = new ArrayList();
	   float size = Float.parseFloat(sizestr);
			String unit = "";
			float _size=0.0f;
			//if(_size >= size)
			_size=size*1.0f/1024;
			unit = "K";
			if(_size>=1024.0f){
				_size=_size/1024;
				unit = "M";
				if(_size>=1024.0f){
					_size=_size/1024;
					unit = "G";
				}
			}
			rvalue.add(0, Math.round(_size)+"");
			rvalue.add(1, unit);
	   return rvalue;
   }
   
   //�������ж���
   
   public static int checkTel(String str)
   {
     if (str.length() != 11)
       return -1;

     if ((str.startsWith("130")) || (str.startsWith("131")) || 
       (str.startsWith("132")) || (str.startsWith("133")))
       return 1;

     return 0;
   }
   
   public static String getStrByLength(String strParameter, int limitLength)
   {
     String return_str = strParameter;
     int temp_int = 0;
     int cut_int = 0;
     byte[] b = strParameter.getBytes();

     for (int i = 0; i < b.length; ++i) {
       if (b[i] >= 0) {
         ++temp_int;
       } else {
         temp_int += 2;
         ++i;
       }
       ++cut_int;

       if (temp_int >= limitLength) {
         if ((temp_int % 2 != 0) && (b[(temp_int - 1)] < 0))
           --cut_int;

         return_str = return_str.substring(0, cut_int);
         break;
       }
     }
     return return_str;
   }
   
   public static String getDay()
   {
     SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
     String str = s.format(Long.valueOf(System.currentTimeMillis()));
     return str;
   }

   public static String getSecond()
   {
     SimpleDateFormat s = new SimpleDateFormat("HHmmss");
     String str = s.format(Long.valueOf(System.currentTimeMillis()));
     return str;
   }

   public static String makeString(int length)
   {
     StringBuffer s = new StringBuffer();
     for (int i = 0; i < length; ++i)
       s.append("");

     return s.toString();
   }

   public static String CheckStr(String str, int length)
   {
     if (str.getBytes().length > length)
       return "�澯��Ϣ����";
     return str;
   }
   
	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,int recover){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	Hashtable createeventdata = ShareData.getCreateEventdata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	
	 	Host host = null;
		try{
			host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(host == null)return;
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress)){
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress,date);	
//				if(recover == 1){
//					if(subentity.equalsIgnoreCase("ping"))
//						createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//				}
				
 			}else{
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress);		 				
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
	 			if(checkday == 1){
	 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
	 				if (subvalue/(1000*60*60*24)>=1){
		 				//����һ�죬���ٷ���Ϣ
			 			Smscontent smscontent = new Smscontent();
			 			String time = sdf.format(date.getTime());
			 			smscontent.setLevel(flag+"");
			 			smscontent.setObjid(objid);
			 			smscontent.setMessage(content);
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype(subtype);
			 			smscontent.setSubentity(subentity);
			 			smscontent.setIp(ipaddress);//���Ͷ���
			 			SmscontentDao smsmanager=new SmscontentDao();
			 			smsmanager.sendURLSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
						sendeddata.put(subtype+":"+subentity+":"+ipaddress,date);
//						if(recover == 1){
//							if(subentity.equalsIgnoreCase("ping"))
//								createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//						}
			 		}
	 			}else{
	 				//createEvent("poll",host.getAlias(),host.getBid(),content,flag,subtype,subentity,ipaddress,objid);
	 				if(recover == 1){
						if(subentity.equalsIgnoreCase("ping"))
							createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
					}
	 				//createEvent("poll",sysLocation,getBid(),nm.getAlarmInfo()+"��ǰֵ:"+CEIString.round(new Double(memorydata.getThevalue()).doubleValue(),2)+" ��ֵ:"+nm.getLimenvalue0(),flag,"host","memory");
	 			}
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
   
}
