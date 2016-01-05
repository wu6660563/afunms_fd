package com.afunms.detail.service.diskInfo;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import com.afunms.common.util.ChartGraph;
import com.afunms.detail.reomte.model.DiskInfo;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.temp.dao.DiskTempDao;
import com.afunms.temp.dao.MemoryTempDao;
import com.afunms.temp.dao.PingTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.DiskForAS400Dao;
import com.afunms.topology.model.DiskForAS400;
import com.afunms.topology.model.HostNode;


public class DiskInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	
	public DiskInfoService() {
		super();
	}
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public DiskInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	public DiskInfoService(String nodeid) {
		super();
		this.nodeid = nodeid;
	}

	public List<DiskInfo> getCurrDiskInfo(){
		String[] subentities = null;
		return getCurrDiskInfo(subentities);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DiskForAS400> getCurrDiskForAS400Info(){
		List<DiskForAS400> diskForAS400List = null;
		DiskForAS400Dao diskForAS400Dao = new DiskForAS400Dao();
		try {
			diskForAS400List = diskForAS400Dao.findByNodeid(nodeid);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			diskForAS400Dao.close();
		}
		return diskForAS400List;
	}
	
	public List<DiskInfo> getCurrDiskInfo(String[] subentities){
		DiskTempDao diskTempDao = new DiskTempDao();
		List<DiskInfo> list = null;
		try {
			list = diskTempDao.getDiskInfoList(nodeid, type, subtype, subentities);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			diskTempDao.close();
		}
		return list;
	}
	
	/**
	 * ��ȡ��ǰ����������ͼ�������ظ�ͼȫ·�����ƣ����ͼƬ����δָ����
	 * ������һ��Ĭ�ϵ�ͼƬ����
	 * @param diskInfoList	--- ��������
	 * @param imgTitle		--- ͼƬ����
	 * @param imgName		--- ͼƬ����
	 * @param width			--- ͼƬ���
	 * @param heigth		--- ͼƬ�߶�
	 * @return imgPath      --- ͼƬȫ·��
	 */
	public String getCurrDiskInfoUtilizationImg(List<DiskInfo> diskInfoList, String imgTitle, String imgName, int width, int heigth){
		String rowKeys[]={""};					// �������йؼ���
		String[] columnKeys = null;				// �������йؼ���
		double[][] data = null;					// ����
		if(diskInfoList == null){
			diskInfoList = new ArrayList<DiskInfo>();
		}
		int size = diskInfoList.size();
		columnKeys = new String[size];
		data = new double[1][size];
		for(int i = 0 ; i < size; i++){
			DiskInfo diskInfo = diskInfoList.get(i);
			columnKeys[i] = diskInfo.getSindex();
			//System.out.println(diskInfo.getSindex() + "=================================" + diskInfo.getUtilization());
			data[0][i] = Double.valueOf(diskInfo.getUtilization());
		}
		return drawDiskInfoImg(rowKeys, columnKeys, data, imgTitle, imgName, width, heigth);
	}
	
	
	private String drawDiskInfoImg(String[] rowKeys, String[] columnKeys, double[][] data, String imgTitle, String imgName, int width, int heigth){
		if(imgName == null){
			imgName = this.nodeid + "-" + this.type + "-" + this.subtype;
		}
		ChartGraph cg = new ChartGraph();
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);//.createCategoryDataset(rowKeys, columnKeys, data);
		cg.zhu(imgTitle, imgName, dataset, width, heigth);
		return "resource/image/jfreechart/" + imgName + ".png";
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * �õ�����sindex
	 * @return
	 */
	public List getCurrDiskSindex(){
		List sindexList = null;
		DiskTempDao diskTempDao = new DiskTempDao();
		try {
				sindexList = diskTempDao.getCurrDiskSindex(nodeid, type, subtype);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			diskTempDao.close();
		}
		return sindexList;
	}
	
	public List<NodeTemp> getCurrDiskInfo(String sindex){
		List<NodeTemp> nodeTempList = null;
		DiskTempDao diskTempDao = new DiskTempDao();
		try {
			nodeTempList = diskTempDao.getCurrDiskListInfo(nodeid, type, subtype,sindex);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			diskTempDao.close();
		}
		return nodeTempList;
	}
	
	/**
	 * �õ���ǰӲ�̵���Ϣ
	 * @return
	 */
	public Hashtable getCurrDiskListInfo(){
		Hashtable currDiskHashtable = new Hashtable();
		List sindexsList = getCurrDiskSindex();
		DecimalFormat df=new DecimalFormat("#.##");
		for(int i=0;i<sindexsList.size();i++){
			Hashtable diskItemHashtable = new Hashtable();
			List<NodeTemp> diskList = getCurrDiskInfo(String.valueOf(sindexsList.get(i)));
			for(int j=0;j<diskList.size();j++){
				NodeTemp nodeTemp = diskList.get(j);
				String subentity = nodeTemp.getSubentity();
				String thevalue = nodeTemp.getThevalue();
				String unit = nodeTemp.getUnit();
				diskItemHashtable.put(subentity, df.format(Double.valueOf(thevalue))+unit);
				if("Utilization".equals(subentity)){
					diskItemHashtable.put("Utilizationvalue", df.format(Double.valueOf(thevalue)));
				}
			}
			diskItemHashtable.put("name", String.valueOf(sindexsList.get(i)));
			currDiskHashtable.put(i, diskItemHashtable);
		}
		return currDiskHashtable;
	}

	
	public List getDiskperflistInfo(){
		List diskInfoList = null;
		DiskTempDao diskTempDao = new DiskTempDao();
		try {
			diskInfoList = diskTempDao.getDiskperflistInfo(nodeid);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			diskTempDao.close();
		}
		return diskInfoList;
	} 
	
	public Vector<Diskcollectdata> getDiskInfoVector(){
		Vector<Diskcollectdata> diskInfoVector = null;
		DiskTempDao diskTempDao = new DiskTempDao();
		try {
			diskInfoVector = diskTempDao.getDiskInfoVector(nodeid, type, subtype);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			diskTempDao.close();
		}
		return diskInfoVector;
	}
	
	public Hashtable<Integer, Hashtable<String, String>> getCurDayDiskValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getDiskValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<Integer, Hashtable<String, String>> getDiskValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        Hashtable diskValueHashtable = new Hashtable();
        try {
            diskValueHashtable = hostlastmanager.getDisk(ipaddress,
                    "Disk", startTime, toTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return diskValueHashtable;
    }
    
    /**
	 * ���ݼ�ص�Node�б�õ�ƽ������������
	 * @param monitorNodelist
	 * @return
	 */
	public List<Diskcollectdata> getDiskNodeTempList(HostNode hostNode) {
		if(hostNode == null){
			return null;
		}
		DiskTempDao diskTempDao = new DiskTempDao();
		List<Diskcollectdata> nodeTempList = null;
		try {
			nodeTempList = diskTempDao.getDiskNodeTempList(hostNode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			diskTempDao.close();
		}
		return nodeTempList;
	}
    
}
