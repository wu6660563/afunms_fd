/**
 * <p>Description:link,which connects two ports</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-10
 */

package com.afunms.polling.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.afunms.common.util.Arith;
import com.afunms.common.util.SysUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.api.I_HostLastCollectData;

public class LinkRoad {     
	private int id;

	private int startId;

	private int endId;

	private String startIndex;

	private String endIndex;

	private String linkName;

	private String startDescr;

	private String endDescr;

	private String startIp;

	private String endIp;

	protected String lastTime;// �ϴ���ѯʱ��

	private int assistant;

	private int type;
	
	private int showinterf;

	private boolean isAlarm;

	private String maxSpeed;// yangjun add

	private String maxPer;// yangjun add
	
	/*
	 * ��·��������  ===start====  nielin add
	 */
	private String uplinkSpeed;		// nielin add   ��������
	
	private String downlinkSpeed;		// nielin add   ��������
	
	private String ping;		// nielin add       ������
	
	private String allSpeedRate;		// nielin add    ����������
	/*
	 * ��·��������  ===end====
	 */

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public LinkRoad() {
		lastTime = SysUtil.getCurrentTime();
	}

	public int getAssistant() {
		return assistant;
	}

	public void setAssistant(int assistant) {
		this.assistant = assistant;
	}

	public String getEndDescr() {
		return endDescr;
	}

	public void setEndDescr(String endDescr) {
		this.endDescr = endDescr;
	}

	public int getEndId() {
		return endId;
	}

	public void setEndId(int endId) {
		this.endId = endId;
	}

	public String getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getStartDescr() {
		return startDescr;
	}

	public void setStartDescr(String startDescr) {
		this.startDescr = startDescr;
	}

	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public boolean isAlarm() {
		return isAlarm;
	}

	public void setAlarm(boolean isAlarm) {
		this.isAlarm = isAlarm;
	}
	
	
	

	/**
	 * @return the uplinkSpeed
	 */
	public String getUplinkSpeed() {
		return uplinkSpeed;
	}

	/**
	 * @param uplinkSpeed the uplinkSpeed to set
	 */
	public void setUplinkSpeed(String uplinkSpeed) {
		this.uplinkSpeed = uplinkSpeed;
	}

	/**
	 * @return the downlinkSpeed
	 */
	public String getDownlinkSpeed() {
		return downlinkSpeed;
	}

	/**
	 * @param downlinkSpeed the downlinkSpeed to set
	 */
	public void setDownlinkSpeed(String downlinkSpeed) {
		this.downlinkSpeed = downlinkSpeed;
	}

	/**
	 * @return the ping
	 */
	public String getPing() {
		return ping;
	}

	/**
	 * @param ping the ping to set
	 */
	public void setPing(String ping) {
		this.ping = ping;
	}

	/**
	 * @return the allSpeedRate
	 */
	public String getAllSpeedRate() {
		return allSpeedRate;
	}

	/**
	 * @param allSpeedRate the allSpeedRate to set
	 */
	public void setAllSpeedRate(String allSpeedRate) {
		this.allSpeedRate = allSpeedRate;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof LinkRoad))
			return false;

		LinkRoad that = (LinkRoad) obj;
		if (startId == that.startId && endId == that.endId
				&& startIndex.equals(that.startIndex)
				&& endIndex.equals(that.endIndex))
			return true;
		else if (startId == that.endId && endId == that.startId
				&& startIndex.equals(that.endIndex)
				&& endIndex.equals(that.startIndex))
			return true;
		else
			return false;
	}

	public int hashCode() {
		int result = 1;
		result = result * 31 + startId;
		result = result * 31 + endId;
		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(startIndex);
		sb.append("@");
		sb.append(startIp);
		sb.append("<--->");
		sb.append(endIndex);
		sb.append("@");
		sb.append(endIp);
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Ϊ����ͼ��·��ʾ�ṩ��Ϣ
	 */
	public String getShowMessage() {
		com.afunms.polling.base.Node startnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(startId);
		com.afunms.polling.base.Node endnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(endId);
		String start_inutilhdx = "0";
		String start_oututilhdx = "0";
		String start_inutilhdxperc = "0";
		String start_oututilhdxperc = "0";
		String end_inutilhdx = "0";
		String end_oututilhdx = "0";
		String end_inutilhdxperc = "0";
		String end_oututilhdxperc = "0";
		String starOper = "";
		String endOper = "";
		//��Ҫ���ֲ�ʽ�ж�
		//ȡ�˿�����
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		String runmodel = PollingEngine.getCollectwebflag(); 
		Vector end_vector = new Vector();
		Vector start_vector = new Vector();
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "ifOutBroadcastPkts", "ifInBroadcastPkts",
				"ifOutMulticastPkts", "ifInMulticastPkts",
				"OutBandwidthUtilHdx", "InBandwidthUtilHdx",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdxPerc" };
		try {
			if("0".equals(runmodel)){
			   	//�ɼ�������Ǽ���ģʽ	
				end_vector = hostlastmanager.getInterface_share(endnode.getIpAddress(), netInterfaceItem, "index", "", "");
				start_vector = hostlastmanager.getInterface_share(startnode.getIpAddress(), netInterfaceItem, "index", "", "");
			}else{
				//�ɼ�������Ƿ���ģʽ
				start_vector = hostlastmanager.getInterface(startnode.getIpAddress(),netInterfaceItem,"index","",""); 
				end_vector = hostlastmanager.getInterface(endnode.getIpAddress(),netInterfaceItem,"index","",""); 
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (startnode != null) {
			try {
				for (int i = 0; i < start_vector.size(); i++) {
					String[] strs = (String[]) start_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(startIndex)) {
						starOper = strs[3].trim();
						start_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_oututilhdxperc = strs[10].replaceAll("%", "");
						start_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (endnode != null) {
			try {
				for (int i = 0; i < end_vector.size(); i++) {
					String[] strs = (String[]) end_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(endIndex)) {
						endOper = strs[3].trim();;
						end_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_oututilhdxperc = strs[10].replaceAll("%", "");
						end_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
				.parseInt(end_inutilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;
		int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
				.parseInt(end_oututilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;

		double upperc = 0;
		try {
			if (start_oututilhdxperc != null
					&& start_oututilhdxperc.trim().length() > 0
					&& end_inutilhdxperc != null
					&& end_inutilhdxperc.trim().length() > 0)
				upperc = Arith.div((Double
						.parseDouble(start_oututilhdxperc) + Double
						.parseDouble(end_inutilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double downperc = 0;
		try {
			if (start_inutilhdxperc != null
					&& start_inutilhdxperc.trim().length() > 0
					&& end_oututilhdxperc != null
					&& end_oututilhdxperc.trim().length() > 0)
				downperc = Arith.div((Double
						.parseDouble(start_inutilhdxperc) + Double
						.parseDouble(end_oututilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// yangjun add
		StringBuffer alarmMsg = new StringBuffer(500);
		String starStatus = "";
		String endStatus = "";
		if(starOper != null){
			starStatus = "up".equalsIgnoreCase(starOper) ? "(<font color='green'>up</font>)" : "(<font color='red'>down</font>)";
			if (!"up".equalsIgnoreCase(starOper)) {
				alarmMsg.append("���˿�" + startDescr
						+ " <font color='red'>down</font><br>");
				isAlarm = true;
//				System.out.println(startnode.getIpAddress()+" ���˿� "+startDescr+" down");
//				createSMS("network","interface",startnode.getIpAddress(),startnode.getId()+"",startnode.getIpAddress()+" ���˿� "+startDescr+" down",2,1);
			}else{
				isAlarm = false;
			}
		} else {
			starStatus = "(<font color='red'>down</font>)";
			alarmMsg.append("���˿�" + startDescr + " <font color='red'>down</font><br>");
//			System.out.println(startnode.getIpAddress()+" ���˿�=== "+startDescr+" down");
			//createSMS("network","interface",startnode.getIpAddress(),startnode.getId()+"",startnode.getIpAddress()+" ���˿� "+startDescr+" down",2,1);
//			this.setAlarm(true);
			isAlarm = true;
		}
		if(endOper != null){
			endStatus = "up".equalsIgnoreCase(endOper) ? "(<font color='green'>up</font>)" : "(<font color='red'>down</font>)";
			if (!"up".equalsIgnoreCase(endOper)) {
				alarmMsg.append("�յ�˿�" + endDescr
						+ " <font color='red'>down</font><br>");
//				System.out.println(endnode.getIpAddress()+" �յ�˿� "+endDescr+" down");
//				createSMS("network","interface",endnode.getIpAddress(),endnode.getId()+"",endnode.getIpAddress()+" �յ�˿� "+endDescr+" down",2,1);
//				this.setAlarm(true);
				isAlarm = isAlarm||true;
			} else {
				isAlarm = isAlarm||false;
			}
		} else {
			endStatus = "(<font color='red'>down</font>)";
			alarmMsg.append("�յ�˿�" + endDescr + " <font color='red'>down</font><br>");
//			System.out.println(endnode.getIpAddress()+" �յ�˿� ==="+endDescr+" down");
			//createSMS("network","interface",endnode.getIpAddress(),endnode.getId()+"",endnode.getIpAddress()+" �յ�˿� "+endDescr+" down",2,1);
//			this.setAlarm(true);
			isAlarm = isAlarm||true;
		}
//		if (if1 != null) {
//			starStatus = if1.getOperStatus() == 1 ? "(<font color='green'>up</font>)"
//					: "(<font color='red'>down</font>)";
//			if (if1.getOperStatus() != 1) {
//				alarmMsg.append("���˿�" + startDescr
//						+ " <font color='red'>down</font><br>");
//				this.setAlarm(true);
//			}
//		} else {
//			starStatus = "(<font color='red'>down</font>)";
//			alarmMsg.append("���˿�" + startDescr
//					+ " <font color='red'>down</font><br>");
//			this.setAlarm(true);
//		}
//		
//		if (if2 != null) {
//			endStatus = if2.getOperStatus() == 1 ? "(<font color='green'>up</font>)"
//					: "(<font color='red'>down</font>)";
//			if (if2.getOperStatus() != 1) {
//				alarmMsg.append("�յ�˿�" + endDescr
//						+ " <font color='red'>down</font><br>");
//				this.setAlarm(true);
//			}
//		} else {
//			endStatus = "(<font color='red'>down</font>)";
//			alarmMsg.append("�յ�˿�" + endDescr
//					+ " <font color='red'>down</font><br>");
//			this.setAlarm(true);
//		}

		if (maxSpeed != null && !"".equals(maxSpeed) && !"null".equals(maxSpeed)) {
			if (upspeed > Integer.parseInt(maxSpeed)) {
				alarmMsg.append("<font color='red'>��·�������ٳ�����ֵ (" + maxSpeed
						+ " KB/��) </font><br>");
				isAlarm = isAlarm||true;
			} else {
				isAlarm = isAlarm||false;
			}
			if (downspeed > Integer.parseInt(maxSpeed)) {
				alarmMsg.append("<font color='red'>��·�������ٳ�����ֵ (" + maxSpeed
						+ " KB/��) </font><br>");
				isAlarm = isAlarm||true;
			} else {
				isAlarm = isAlarm||false;
			}
		}
		if (maxPer != null && !"".equals(maxPer) && !"null".equals(maxPer)) {
			if (upperc > Double.parseDouble(maxPer)) {
				if(upperc>=100){
					upperc = upperc/10;
				}
				alarmMsg.append("<font color='red'>���д��������ʳ�����ֵ (" + maxPer
						+ "%) </font><br>");
				isAlarm = isAlarm||true;
			} else {
				isAlarm = isAlarm||false;
			}
			if (downperc > Double.parseDouble(maxPer)) {
				if(downperc>=100){
					downperc = upperc/10;
				}
				alarmMsg.append("<font color='red'>���д��������ʳ�����ֵ (" + maxPer
						+ "%) </font><br>");
				isAlarm = isAlarm||true;
			} else {
				isAlarm = isAlarm||false;
			}
		}
		if(startnode == null ||endnode == null )return "";
		// end
		StringBuffer msg = new StringBuffer(400);  
		msg.append("��·����:");
		msg.append(linkName);
		msg.append("<br>");
		msg.append("��Դ����:");
		msg.append("��·");
		msg.append("<br/><br/>");
		msg.append("<font color='green'>���˿�:");
		msg.append(startDescr + "</font>" + starStatus);
		msg.append("<br>");
		msg.append("���IP:");
		msg.append(startnode.getIpAddress());
		msg.append("<br>");
		msg.append("<font color='green'>�յ�˿�:");
		msg.append(endDescr + "</font>" + endStatus);
		msg.append("<br>");
		msg.append("�յ�IP:");
		msg.append(endnode.getIpAddress());
		msg.append("<br>");
		msg.append("<font color='green'>��������:");
		msg.append(downspeed + "KB/��");
		msg.append("</font><br>");
		msg.append("<font color='green'>��������:");
		msg.append(upspeed + "KB/��");
		msg.append("</font><br>");
		msg.append("<font color='green'>���д���������:");
		msg.append(downperc + "%");
		msg.append("</font><br>");
		msg.append("<font color='green'>���д���������:");
		msg.append(upperc + "%");
		msg.append("</font><br>");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar date = Calendar.getInstance();
			Date cc = date.getTime();
			String time = sdf.format(cc);
			lastTime = time;
		} catch (Exception e) {

		}
//		if(alarmMsg!=null&&!"".equalsIgnoreCase(alarmMsg.toString())){
			if (isAlarm) {
				msg.append("<font color='red'>--������Ϣ:--</font><br>");
				msg.append(alarmMsg.toString());
//				SysLogger.info(alarmMsg.toString());
			} 
//			else {
//				msg.append("<font color='red'>--������Ϣ:--</font><br>");
//				msg.append(alarmMsg.toString());
//				this.setAlarm(true);
////			}
//		} else {
//			if(isAlarm){
//				this.setAlarm(false);
//			} 
//		}
		msg.append("����ʱ��:" + lastTime + "<br>");
		
		
		/*
		 * ��·���� start =========== nielin
		 */
		try {
			this.uplinkSpeed = String.valueOf(upspeed);
			
			this.downlinkSpeed = String.valueOf(downspeed);
			
			this.allSpeedRate = String.valueOf(downperc + upperc);
			
			int linkflag = 100;
			if ("down".equalsIgnoreCase(starOper)||"down".equalsIgnoreCase(endOper)) {
				linkflag = 0;
			}
			
			this.ping = String.valueOf(linkflag);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * ��·���� end =========== nielin
		 */
		
		
		return msg.toString();
	}
	
	public String getMessage(int type) {
		com.afunms.polling.base.Node startnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(startId);
		com.afunms.polling.base.Node endnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(endId);
		String start_inutilhdx = "0";
		String start_oututilhdx = "0";
		String start_inutilhdxperc = "0";
		String start_oututilhdxperc = "0";
		String end_inutilhdx = "0";
		String end_oututilhdx = "0";
		String end_inutilhdxperc = "0";
		String end_oututilhdxperc = "0";
		//ȡ�˿�����
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		String runmodel = PollingEngine.getCollectwebflag(); 
		Vector end_vector = new Vector();
		Vector start_vector = new Vector();
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "ifOutBroadcastPkts", "ifInBroadcastPkts",
				"ifOutMulticastPkts", "ifInMulticastPkts",
				"OutBandwidthUtilHdx", "InBandwidthUtilHdx",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdxPerc" };
		try {
			if("0".equals(runmodel)){
			   	//�ɼ�������Ǽ���ģʽ	
				end_vector = hostlastmanager.getInterface_share(endnode.getIpAddress(), netInterfaceItem, "index", "", "");
				start_vector = hostlastmanager.getInterface_share(startnode.getIpAddress(), netInterfaceItem, "index", "", "");
			}else{
				//�ɼ�������Ƿ���ģʽ
				start_vector = hostlastmanager.getInterface(startnode.getIpAddress(),netInterfaceItem,"index","",""); 
				end_vector = hostlastmanager.getInterface(endnode.getIpAddress(),netInterfaceItem,"index","",""); 
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (startnode != null) {
			try {
				for (int i = 0; i < start_vector.size(); i++) {
					String[] strs = (String[]) start_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(startIndex)) {
						start_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_oututilhdxperc = strs[10].replaceAll("%", "");
						start_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (endnode != null) {
			try {
				for (int i = 0; i < end_vector.size(); i++) {
					String[] strs = (String[]) end_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(endIndex)) {
						end_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_oututilhdxperc = strs[10].replaceAll("%", "");
						end_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
				.parseInt(end_inutilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;
		int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
				.parseInt(end_oututilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;

		double upperc = 0;
		try {
			if (start_oututilhdxperc != null
					&& start_oututilhdxperc.trim().length() > 0
					&& end_inutilhdxperc != null
					&& end_inutilhdxperc.trim().length() > 0)
				upperc = Arith.div((Double
						.parseDouble(start_oututilhdxperc) + Double
						.parseDouble(end_inutilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double downperc = 0;
		try {
			if (start_inutilhdxperc != null
					&& start_inutilhdxperc.trim().length() > 0
					&& end_oututilhdxperc != null
					&& end_oututilhdxperc.trim().length() > 0)
				downperc = Arith.div((Double
						.parseDouble(start_inutilhdxperc) + Double
						.parseDouble(end_oututilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(startnode == null ||endnode == null )return "#.#";
		if(type==1){
			return upperc + "%";
		}else if(type==2){
			return downperc + "%";
		}else if(type==3){
			return upspeed + "KB/��";
		}else if(type==4){
			return downspeed + "KB/��";
		}else {
			return "#.#";
		}
	}

	public String getLinkWidth(int utilhdx,int utilhdxperc) {
		com.afunms.polling.base.Node startnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(startId);
		com.afunms.polling.base.Node endnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByID(endId);
		String start_inutilhdx = "0";
		String start_oututilhdx = "0";
		String start_inutilhdxperc = "0";
		String start_oututilhdxperc = "0";
		String end_inutilhdx = "0";
		String end_oututilhdx = "0";
		String end_inutilhdxperc = "0";
		String end_oututilhdxperc = "0";
		//ȡ�˿�����
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		String runmodel = PollingEngine.getCollectwebflag(); 
		Vector end_vector = new Vector();
		Vector start_vector = new Vector();
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "ifOutBroadcastPkts", "ifInBroadcastPkts",
				"ifOutMulticastPkts", "ifInMulticastPkts",
				"OutBandwidthUtilHdx", "InBandwidthUtilHdx",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdxPerc" };
		try {
			if("0".equals(runmodel)){
			   	//�ɼ�������Ǽ���ģʽ	
				end_vector = hostlastmanager.getInterface_share(endnode.getIpAddress(), netInterfaceItem, "index", "", "");
				start_vector = hostlastmanager.getInterface_share(startnode.getIpAddress(), netInterfaceItem, "index", "", "");
			}else{
				//�ɼ�������Ƿ���ģʽ
				start_vector = hostlastmanager.getInterface(startnode.getIpAddress(),netInterfaceItem,"index","",""); 
				end_vector = hostlastmanager.getInterface(endnode.getIpAddress(),netInterfaceItem,"index","",""); 
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (startnode != null) {
			try {
				for (int i = 0; i < start_vector.size(); i++) {
					String[] strs = (String[]) start_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(startIndex)) {
						start_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						start_oututilhdxperc = strs[10].replaceAll("%", "");
						start_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (endnode != null) {
			try {
				for (int i = 0; i < end_vector.size(); i++) {
					String[] strs = (String[]) end_vector.get(i);
					String index = strs[0];
					if (index.equalsIgnoreCase(endIndex)) {
						end_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
						end_oututilhdxperc = strs[10].replaceAll("%", "");
						end_inutilhdxperc = strs[11].replaceAll("%", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
				.parseInt(end_inutilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;
		int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
				.parseInt(end_oututilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;

		double upperc = 0;
		try {
			if (start_oututilhdxperc != null
					&& start_oututilhdxperc.trim().length() > 0
					&& end_inutilhdxperc != null
					&& end_inutilhdxperc.trim().length() > 0)
				upperc = Arith.div((Double
						.parseDouble(start_oututilhdxperc) + Double
						.parseDouble(end_inutilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double downperc = 0;
		try {
			if (start_inutilhdxperc != null
					&& start_inutilhdxperc.trim().length() > 0
					&& end_oututilhdxperc != null
					&& end_oututilhdxperc.trim().length() > 0)
				downperc = Arith.div((Double.parseDouble(start_inutilhdxperc) + Double.parseDouble(end_oututilhdxperc)), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
         
		if(startnode == null ||endnode == null )return "#.#";
		DecimalFormat df=new DecimalFormat("#.##");
		if(type==1){
			return df.format(Double.parseDouble((upperc==0?1:(1+upperc*utilhdxperc/10))+""));
		}else if(type==2){
			return df.format(Double.parseDouble((downperc==0?1:(1+downperc*utilhdxperc/10)) + ""));
		}else if(type==3){
			return df.format(Double.parseDouble((upspeed==0?1:(upspeed>=utilhdx?utilhdxperc:(upspeed*utilhdxperc/utilhdx))) + ""));
		}else if(type==4){
			return df.format(Double.parseDouble((downspeed==0?1:(downspeed>=utilhdx?utilhdxperc:(downspeed*utilhdxperc/utilhdx))) + ""));
		}else {
			return "1";
		}
	}
	
	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getMaxPer() {
		return maxPer;
	}

	public void setMaxPer(String maxPer) {
		this.maxPer = maxPer;
	}

	public int getShowinterf() {
		return showinterf;
	}

	public void setShowinterf(int showinterf) {
		this.showinterf = showinterf;
	}
	
}
