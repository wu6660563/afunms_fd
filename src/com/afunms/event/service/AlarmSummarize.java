package com.afunms.event.service;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.afunms.common.util.CreateAlarmMetersPic;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;


/********************************************************
 *Title:AlarmSummarize
 *Description:�澯��Ϣ����service��
 *Company  dhccs
 *@author zhangcw
 * Mar 25, 2011 11:44:59 AM
 ********************************************************
 */
public class AlarmSummarize {
	private DBManager conn;
	private ResultSet rs;
	private double closedRatio=0;//�ر���
	public HashMap<String, ?> getData(String filename){
//		HashMap<String, Object> dataMap=new HashMap<String, Object>();
//		conn=new DBManager();
//		try{
//			String[][] tabledata=gettableData();
//			dataMap.put("tabledata", tabledata);
//			String piedata=getpieData();
//			dataMap.put("piedata", piedata);
//			String columndata=getcolumnData();
//			dataMap.put("columndata", columndata);
//			String dayalarmData=getDayAlarmData();
//			dataMap.put("dayalarmData", dayalarmData);
//			String weekalarmData=getWeekAlarmData();
//			dataMap.put("weekalarmData", weekalarmData);
//			String closedAlarmPicFile=this.closedAlarmPic(filename);
//			dataMap.put("closedAlarmPicFile", closedAlarmPicFile);
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}finally{
//			conn.close();
//		}
//		return dataMap;
	    return null;
	}

	/**
	 * ���ɱ����������
	 * @return
	 */
	public String[][] getAlarmTypeSizeByDay(){
		String [][] dataStr=new String[][] {
	        {"���","��ʾ","��ͨ","����","����"},
			{"����澯","0","0","0","0"},
			{"�������澯","0","0","0","0"},
			{"���ݿ�澯","0","0","0","0"},
			{"�м���澯","0","0","0","0"},
			{"Ӧ�ø澯","0","0","0","0"},
			{"�洢�澯","0","0","0","0"},
			{"ҵ��澯","0","0","0","0"},
			{"��ȫ�澯","0","0","0","0"}};
		try {
			String subtype="";
			int level=0;
			//--�޸�Ϊֻ��ѯ��������
			StringBuilder sb=new StringBuilder();
			sb.append(" select subtype,level1,count(1) as cnt from system_eventlist ");
			sb.append(" where to_days(recordtime) = to_days(now()) ");
			sb.append(" group by subtype,level1; ");
			conn = new DBManager();
			rs = conn.executeQuery(sb.toString()); 
			while (rs.next()) {
			    subtype = rs.getString("subtype"); 
			    if (subtype.equalsIgnoreCase("net") || subtype.equalsIgnoreCase("dns")) {
			        //����澯
	        	    level=rs.getInt("level1");
	        	    dataStr[1][level+1]= String.valueOf((Integer.parseInt(dataStr[1][level+1])+Integer.parseInt(rs.getString("cnt"))));
			    } else if(subtype.equalsIgnoreCase("host")){
			        //�������澯 
	        		level=rs.getInt("level1");
	        		dataStr[2][level+1]= String.valueOf((Integer.parseInt(dataStr[3][level+1])+Integer.parseInt(rs.getString("cnt"))));
        		} else if(subtype.equalsIgnoreCase("db")){//���ݿ�澯 
        		    level=rs.getInt("level1");
        		    dataStr[3][level+1]=rs.getString("cnt");
        		} else if(subtype.equalsIgnoreCase("domino")
	        			 ||subtype.equalsIgnoreCase("tomcat")
	        			 ||subtype.equalsIgnoreCase("cics")
	        			 ||subtype.equalsIgnoreCase("mq")
	        			 ||subtype.equalsIgnoreCase("wasserver")
	        			 ||subtype.equalsIgnoreCase("weblogic")
	        			 ||subtype.equalsIgnoreCase("iis")
	        			 ||subtype.equalsIgnoreCase("jboss")
	        			 ||subtype.equalsIgnoreCase("apache")){ //�м���澯 
        		    level=rs.getInt("level1");
        		    dataStr[4][level+1]= String.valueOf((Integer.parseInt(dataStr[5][level+1])+Integer.parseInt(rs.getString("cnt"))));
        		} 
			    //�д��������
			    //6Ӧ�ø澯 
			    //7�洢�澯 
			    //8ҵ��澯 
        		else if(subtype.equalsIgnoreCase("bus")){//ҵ��澯  
        		    level=rs.getInt("level1");
        		    dataStr[8][level+1]=rs.getString("cnt");
        		}
			    //9��ȫ�澯
		    }
		} catch(Exception e) {
	         SysLogger.error("AlarmSummarize:",e);
		} finally {
		    conn.close();
		}
		return dataStr;
	}

	/**
	 * ���ɸ澯״̬��ͼ����
	 * @return
	 */
	public String getAlarmState() {
	    StringBuffer dataStr=new StringBuffer();
	    Map<String, String> map=new TreeMap<String, String>();
	    //��ʼֵΪ0
	    map.put("0", "0");
	    map.put("1", "0");
	    map.put("2", "0");
	    try {
	        //--�޸�Ϊֻ��ѯ��������
	        StringBuilder sb=new StringBuilder();
			sb.append(" select managesign as sign,count(1) as cnt from system_eventlist ");
			sb.append(" where to_days(recordtime) = to_days(now()) ");
			sb.append(" group by (managesign) ; ");
			conn = new DBManager();
			rs = conn.executeQuery(sb.toString());
			while(rs.next()) {
			    map.put(rs.getString("sign"), rs.getString("cnt"));
			}
         } catch(Exception e) {
             SysLogger.error("AlarmSummarize:",e);
	     } finally {
             conn.close();
         }
         int unprocess = Integer.valueOf(map.get("0"));
         int process = Integer.valueOf(map.get("1"));
         int closed = Integer.valueOf(map.get("2"));
         if ((unprocess+process+closed) != 0) {
             this.closedRatio=closed/(unprocess+process+closed);
         }
         if (unprocess==0&&process==0&&closed==0) {
             dataStr.append("0");
         } else {
             dataStr.append("δ����;").append(unprocess).append(";false;FFCC00\n");
             dataStr.append("������;").append(process).append(";false;6666FF\n");
             dataStr.append("�ر�;").append(closed).append(";false;CC33FF\n");
         }
         return dataStr.toString();
	}

	/**
	 * ���ɵ��յ�Сʱ�澯��
	 * @return
	 */
	public String getAlarmSizeByDay(){
		Map<Integer, Integer> map=new TreeMap<Integer, Integer>();
		for(int i=0;i<24;i++){
			map.put(i, 0);
		}
		try {
		    conn = new DBManager();
		    rs = conn.executeQuery("select HOUR(recordtime)  as h,count(1) as cnt from system_eventlist where  DATE(recordtime)=CURDATE() group by h;");
		    while(rs.next()) {
		        map.put(rs.getInt("h"), rs.getInt("cnt"));
		    }
	    } catch(Exception e) {
	        SysLogger.error("AlarmSummarize:",e);
        } finally {
            conn.close();
        }
		StringBuffer dataStr=new StringBuffer();
		for(int i=0;i<24;i++){
			dataStr.append(i).append(";").append(map.get(i)).append("\n");
		}
		return dataStr.toString();
	}
	/**
	 * ���ɽ�һ�ܸ澯���� ��ͼ
	 * @return
	 */
	public String getAlarmSizeByWeek(){
		Map<Integer, Integer> map=new TreeMap<Integer, Integer>();
		Calendar cal = Calendar.getInstance();
		for(int i=0;i<7;i++){
			 int day = cal.get(Calendar.DATE);
			 map.put(day, 0);
			 cal.add(Calendar.DATE, -1);
		}
		try {
		    conn = new DBManager();
		    rs = conn.executeQuery("select DAY(recordtime)  as d,count(1) as cnt from system_eventlist where  DATEDIFF(CURDATE(),recordtime)<7 group by d;");
		    while(rs.next()) {
		        map.put(rs.getInt("d"), rs.getInt("cnt")); 
		    }
		} catch(Exception e) {
		    SysLogger.error("AlarmSummarize:",e);
		} finally {
            conn.close();
        }
		StringBuffer dataStr=new StringBuffer();
		for(Integer date : map.keySet()) {
		    dataStr.append(date).append(";").append(map.get(date)).append("\n");
		}  
		return dataStr.toString();
	}

	/**
	 * ��һ��Ƶ�ʽϸߵı������� ��ͼ
	 * @return
	 */
	public String getAlarmTypeSizeByWeek(){
	    String[] titleStr=new String[] {"����澯","����������","���ݿ⾯��","�м������","Ӧ�ø澯","����澯","�洢�澯","ҵ��澯","��ȫ�澯"};
		String[] colorStr=new String[] {"#33CCFF","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#23f266"};
		String[] dataStr=new String[]  {"0","0","0","0","0","0","0","0","0"};
		try {
		    String subtype="";
		    conn = new DBManager();
		    rs = conn.executeQuery("select subtype,count(1) as cnt from system_eventlist where DATEDIFF(CURDATE(),recordtime)<7 group by subtype;");
			while (rs.next()) {
			    subtype=rs.getString("subtype");
			    if (subtype.equalsIgnoreCase("net")||subtype.equalsIgnoreCase("dns")) {
			        // 1 ����澯
			        dataStr[0]= String.valueOf((Integer.parseInt(dataStr[0])+Integer.parseInt(rs.getString("cnt")))); 
			    } else if (subtype.equalsIgnoreCase("host")) {
	        	    // 3 �������澯
	        		dataStr[1]=rs.getString("cnt");
	        	} else if(subtype.equalsIgnoreCase("db")){
	        	    // 4 ���ݿ�澯
	        	    dataStr[2]=rs.getString("cnt");
        	    } else if (subtype.equalsIgnoreCase("domino")
        	            ||subtype.equalsIgnoreCase("tomcat")
	        			||subtype.equalsIgnoreCase("cics")
	        			||subtype.equalsIgnoreCase("mq")
	        			||subtype.equalsIgnoreCase("wasserver")
	        			||subtype.equalsIgnoreCase("weblogic")
	        			||subtype.equalsIgnoreCase("iis")
	        			||subtype.equalsIgnoreCase("jboss")
	        			||subtype.equalsIgnoreCase("apache")) {
        	        // 5 �м���澯
        	        dataStr[3]= String.valueOf((Integer.parseInt(dataStr[4])+Integer.parseInt(rs.getString("cnt"))));
    	        } else if(subtype.equalsIgnoreCase("bus")){
    	            // 8 ҵ��澯
    	            dataStr[6]=rs.getString("cnt");
	            }
		    }
		} catch(Exception e) {
		    SysLogger.error("AlarmSummarize:",e);
	    } finally {
            conn.close();
        }
		StringBuffer xmlStr=new StringBuffer();
		xmlStr.append("<?xml version='1.0' encoding='gb2312'?>");
		xmlStr.append("<chart><series>");
		for (int i=0;i<titleStr.length;i++) {
			xmlStr.append("<value xid='").append(i).append("'>").append(titleStr[i]).append("</value>");
			
		}
		xmlStr.append("</series><graphs>");
		xmlStr.append("<graph gid='0'>");
		for (int i=0;i<colorStr.length;i++) {
			xmlStr.append("<value xid='").append(i).append("' color='").append(colorStr[i]).append("'>").append(dataStr[i]).append("</value>");
		}
		xmlStr.append("</graph>");
		xmlStr.append("</graphs></chart>");
		return xmlStr.toString();
	}

	public String getClosedAlarmPic(String filename) {
	    getAlarmState();
	    return closedAlarmPic(filename);
	}
	/**
	 * ���ɸ澯�ر��ʱ�������
	 * @param filename ���ɱ���ͼƬ���ļ���
	 * @return
	 */
	public String closedAlarmPic(String filename){
	    CreateAlarmMetersPic createAlarmMetersPic=new CreateAlarmMetersPic();
		String file=createAlarmMetersPic.createClosedAlarmPic(filename, this.closedRatio);
        return file;
	}
	/**
	 * ���Է���
	 * @param args
	 */
	public static void main(String args[])
	{

	}
}
