package com.afunms.realtime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp;
import com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp;
/********************************************************
 *Title:MemoryControler
 *Description:amcharts �ڴ����ݲɼ���������
 *Company  dhcc
 *@author zhangcw
 * Mar 10, 2011 11:07:03 AM
 ********************************************************
 */
public class MemoryControler extends SnmpMonitor{
	private Logger logger=Logger.getLogger(MemoryControler.class);
	private DoubleDataQueue doubleDataQueue=null;
	public static void main(String args[]) {
	}
	/**
	 * ��������
	 * @param fileName �ļ���
	 * @param blackFlag �Ƿ���Ҫ���ɳ�ʼ������
	 * @param nodeID
	 * @param cx
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String generateData(String fileName,boolean blackFlag,int nodeID,HttpServletRequest rq,ServletContext cx){
		String webAppPath=cx.getRealPath("/");
		String path=webAppPath+"amcharts_data/"+fileName;
		//SysLogger.info("The amcharts data path is:"+path);
		HttpSession session=rq.getSession();
		this.doubleDataQueue=(DoubleDataQueue)session.getAttribute("memoryqueue");
		if(null==this.doubleDataQueue){
			this.doubleDataQueue=new DoubleDataQueue();
		}
		if(true==blackFlag){//���ɿ��ļ� ǰn-1������Ϊnull ���һ������Ϊ0.0
			this.doubleDataQueue.initWithLastData(0.0);//���һ������Ϊ0
			this.doubleDataQueue.setDataList(false);//������ʵ����
			session.setAttribute("memoryqueue", this.doubleDataQueue);
		}else{
			DoubleDataModel doubleDM=null;
			doubleDM=getMemoryData(nodeID);//       �ɼ�����
			if(null==doubleDM){//�ɼ�����ʧ�� 
				return "failed:�ɼ�����ʧ��";
			}else{
				if(false==this.doubleDataQueue.isDataList()){
					this.doubleDataQueue.getList().removeLast();//����ʼ����0ֵȥ��
					this.doubleDataQueue.setDataList(true);
				}
				this.doubleDataQueue.enqueue(doubleDM);
				session.setAttribute("memoryqueue", this.doubleDataQueue);
			}
		}
		SysLogger.info("The nodeID is:"+nodeID);
		int size=this.doubleDataQueue.getLENGTH();
		SimpleDateFormat smft=new SimpleDateFormat("ss");//�������ڸ�ʽ
		String date;
		String data;
		StringBuffer dataXML=new StringBuffer("");
		dataXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		dataXML.append("<chart><series>");
		for(int i=0;i<size;i++){
			date=smft.format(this.doubleDataQueue.getList().get(i).getDate());
			// <value xid="0">1850</value>
			dataXML.append("<value xid=\"").append(i).append("\">").append(date).append("</value>");
		}

		dataXML.append("</series><graphs><graph gid=\"1\">");
		for(int i=0;i<size;i++){
			data=this.doubleDataQueue.getList().get(i).getFirstData()+"";
			//  <value xid="0">-0.447</value>
				dataXML.append("<value xid=\"").append(i).append("\">").append(data).append("</value>");
		}
		dataXML.append("</graph><graph gid=\"2\">");
		for(int i=0;i<size;i++){
			data=this.doubleDataQueue.getList().get(i).getSecondData()+"";
			//  <value xid="0">-0.447</value>
				dataXML.append("<value xid=\"").append(i).append("\">").append(data).append("</value>");
		}
		dataXML.append("</graph></graphs></chart>");
		write(path, dataXML.toString());
		return "success";
	}
	/**
	 * д�ļ�
	 * @param path
	 * @param content
	 * void
	 */
	public  void write(String path, String content) {
		try {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
				f.createNewFile();
			} else {
				if (f.createNewFile()) {
				} else {
					logger.error("�ļ�����ʧ�ܣ�");
				}
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(content);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * �ɼ�����
	 * @param DoubleDataModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DoubleDataModel getMemoryData(int nodeID){
		DoubleDataModel doubleDataModel=new DoubleDataModel();
		Host hostNode = (Host)PollingEngine.getInstance().getNodeByID(nodeID);
		if(null==hostNode){
			SysLogger.info("�ýڵ㲻���ڣ����ܲɼ����ݣ�");
			return null;
		}else if(!hostNode.isManaged()){
			SysLogger.info("�ýڵ㱻�������ܲɼ����ݣ�");//��������ʲô��˼���������������˴��ο�WindowsCpuSnmp.java
			return null;
		}
		List<NodeGatherIndicators> gatherlist = new ArrayList<NodeGatherIndicators>();
		NodeDTO nodeDTO = null;// * ����Ϊ��׼���ܼ��ָ��
		NodeUtil nodeutil = new NodeUtil();//����Ϊ ͨ��  ���� �� �������� ��ȡ �豸
		nodeDTO = nodeutil.creatNodeDTOByNode(hostNode);
		NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();//�豸���ܼ��ָ�� dao
		try {
			if(Constant.TYPE_HOST.equalsIgnoreCase(nodeDTO.getType())){//������
				if (Constant.TYPE_HOST_SUBTYPE_WINDOWS.equalsIgnoreCase(nodeDTO.getSubtype())) {//windows������
					gatherlist = indicatorsdao.findByNodeIdAndTypeAndSubtype(hostNode.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
				}
			}else if(Constant.TYPE_NET.equalsIgnoreCase(nodeDTO.getType())){//�����豸
				/*if (Constant.TYPE_NET_SUBTYPE_H3C.equalsIgnoreCase(nodeDTO.getSubtype())) {//H3C
					gatherlist = indicatorsdao.findByNodeIdAndTypeAndSubtype(hostNode.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			indicatorsdao.close();
		}
		/**Window �������ڴ����ݲɼ�**/
		if(Constant.TYPE_HOST.equalsIgnoreCase(nodeDTO.getType())&&Constant.TYPE_HOST_SUBTYPE_WINDOWS.equalsIgnoreCase(nodeDTO.getSubtype())){
			if (gatherlist != null && gatherlist.size() > 0) {
				for (int i = 0; i < gatherlist.size(); i++) {
					NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) gatherlist.get(i);
					if ("virtualmemory".equalsIgnoreCase(nodeGatherIndicators.getName())) {
						// ���������ڴ����ݵĲɼ�
						WindowsVirtualMemorySnmp windowsvirtualsnmp = null;
						try {
							windowsvirtualsnmp = (WindowsVirtualMemorySnmp) Class.forName(
							"com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp").newInstance();
							Hashtable returnHash = windowsvirtualsnmp.collect_Data(nodeGatherIndicators);
							HostCollectDataManager hostdataManager = new HostCollectDataManager();
							hostdataManager.createHostItemData(hostNode.getIpAddress(),	returnHash, "host", "windows", "virtualmemory");
							Vector memoryVector = (Vector) returnHash.get("memory");
							
							if (memoryVector != null && memoryVector.size() > 0) {
								for (int ii = 0; ii< memoryVector.size(); ii++) {
									Memorycollectdata memorydata = (Memorycollectdata) memoryVector.get(ii);
									if ("VirtualMemory".equalsIgnoreCase(memorydata.getSubentity())
											&& "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
										doubleDataModel.setFirstData(Math.round(Float.parseFloat(memorydata.getThevalue()))+0.0);
										doubleDataModel.setSecondData(Math.round(Float.parseFloat(memorydata.getThevalue()))+0.0);
										doubleDataModel.setDate(memorydata.getCollecttime().getTime());
									} else if ("PhysicalMemory".equalsIgnoreCase(memorydata.getSubentity())
											&& "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
										doubleDataModel.setSecondData(Math.round(Float.parseFloat(memorydata.getThevalue()))+0.0);
										doubleDataModel.setDate(memorydata.getCollecttime().getTime());
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if ("physicalmemory".equalsIgnoreCase(nodeGatherIndicators.getName())) {
						// ���������ڴ�Ĳɼ�
						WindowsPhysicalMemorySnmp windowsphysicalsnmp = null;
						try {
							windowsphysicalsnmp = (WindowsPhysicalMemorySnmp) Class.forName(
											"com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp").newInstance();
							Hashtable returnHash = windowsphysicalsnmp.collect_Data(nodeGatherIndicators);
							HostCollectDataManager hostdataManager = new HostCollectDataManager();
							hostdataManager.createHostItemData(hostNode.getIpAddress(),returnHash, "host", "windows","physicalmemory");
							Vector memoryVector = (Vector) returnHash.get("memory");
							if (memoryVector != null && memoryVector.size() > 0) {
								for (int si = 0; si < memoryVector.size(); si++) {
									Memorycollectdata memorydata = (Memorycollectdata) memoryVector.elementAt(si);
									if (!memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory"))
										continue;
									if (memorydata.getRestype().equals("dynamic")) {
										doubleDataModel.setSecondData(Math.round(Float.parseFloat(memorydata.getThevalue()))+0.0);
										doubleDataModel.setDate(memorydata.getCollecttime().getTime());
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		/**H3C CPU���ݲɼ�**/
		else if(Constant.TYPE_NET.equalsIgnoreCase(nodeDTO.getType())&&Constant.TYPE_NET_SUBTYPE_H3C.equalsIgnoreCase(nodeDTO.getSubtype())){
		SysLogger.info(" ��ʱ��֧�ָ��豸���ݲɼ�");
			/*if (gatherlist != null && gatherlist.size() > 0) {
				for (int i = 0; i < gatherlist.size(); i++) {
					NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) gatherlist.get(i);
					if ("cpu".equalsIgnoreCase(nodeGatherIndicators.getName())) {
						// ����CPU�����ʵĲɼ�
						H3CCpuSnmp	h3cCpuSnmp=null;
						try {
							h3cCpuSnmp = (H3CCpuSnmp) Class.forName(
							"com.afunms.polling.snmp.cpu.H3CCpuSnmp").newInstance();
							Hashtable returnHash = h3cCpuSnmp.collect_Data(nodeGatherIndicators);
							HostCollectDataManager hostdataManager = new HostCollectDataManager();
							hostdataManager.createHostItemData(hostNode.getIpAddress(),
									returnHash, "host", "h3c", "cpu");
							Vector  cpuUseRatio = (Vector) returnHash.get("cpu");
							if (cpuUseRatio != null && cpuUseRatio.size() > 0) {
									CPUcollectdata cpudata = (CPUcollectdata) cpuUseRatio.get(0);
									DoubleDataModel dm=new DoubleDataModel();
									dm.setData(Double.valueOf(cpudata.getThevalue()));
									dm.setDate(cpudata.getCollecttime().getTime());
									DoubleDataQueue.enqueue(dm);//�������һ������
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}*/
		}else {
			SysLogger.info("��֧�ָ��豸��CPU���ݲɼ���");
			return null;
		}
		return doubleDataModel;
	}
}
