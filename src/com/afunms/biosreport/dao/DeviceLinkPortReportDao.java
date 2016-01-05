package com.afunms.biosreport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.biosreport.model.DeviceLinkPortReport;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.model.Hua3VPNFileConfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.Link;
import com.afunms.topology.service.TopoLinkInfoService;
import com.afunms.topology.service.TopoNodeInfoService;

public class DeviceLinkPortReportDao extends BaseDao 
			implements DaoInterface{

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String UTILHDX = "utilhdx";
	private static final String UTILHDXPERC = "utilhdxperc";
	
	public DeviceLinkPortReportDao(){
		super("system_bios_linkport");
	}
	
	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		DeviceLinkPortReport linkport = (DeviceLinkPortReport)vo;
		
		
//		boolean startu = (linkport.getStartUtilhdx() == null || linkport.getStartUtilhdx().trim().length()<1); 
//		if(strIsNull(linkport.getStartUtilhdx()) 
//				&& strIsNull(linkport.getStartUtilhdxperc())
//				&& strIsNull(linkport.getEndUtilhdx())
//				&& strIsNull(linkport.getEndUtilhdxperc())) {
//			return false;
//		}
		//System.out.println(linkport.toString());
		StringBuffer sql = new StringBuffer(
				"insert into " + table + "(start_ip,start_name,start_port,start_utilhdx,start_utilhdxperc," +
						"end_ip,end_name,end_port,end_utilhdx,end_utilhdxperc,collecttime) values('");
		sql.append(linkport.getStartDeviceIP());
		sql.append("','");
		sql.append(linkport.getStartDeviceName());
		sql.append("','");
		sql.append(linkport.getStartPort());
		sql.append("','");
		sql.append(linkport.getStartUtilhdx());
		sql.append("','");
		sql.append(linkport.getStartUtilhdxperc());
		sql.append("','");
		sql.append(linkport.getEndDeviceIP());
		sql.append("','");
		sql.append(linkport.getEndDeviceName());
		sql.append("','");
		sql.append(linkport.getEndPort());
		sql.append("','");
		sql.append(linkport.getEndUtilhdx());
		sql.append("','");
		sql.append(linkport.getEndUtilhdxperc());
		sql.append("','");
		sql.append(linkport.getCollecttime());
		sql.append("')");
		System.out.println(sql.toString());
		return this.saveOrUpdate(sql.toString());
	}
	
	private boolean strIsNull(String val){
		return (val== null || val.trim().length()<=0);
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub

		DeviceLinkPortReport vo = new DeviceLinkPortReport();
		try {
			vo.setId(rs.getInt("id"));
			vo.setStartDeviceIP(rs.getString("start_ip"));
			vo.setStartDeviceName(rs.getString("start_name"));
			vo.setStartPort(rs.getString("start_port"));
			vo.setStartUtilhdx(rs.getString("start_utilhdx"));
			vo.setStartUtilhdxperc(rs.getString("start_utilhdxperc"));
			vo.setEndDeviceIP(rs.getString("end_ip"));
			vo.setEndDeviceName(rs.getString("end_name"));
			vo.setEndPort(rs.getString("end_port"));
			vo.setEndUtilhdx(rs.getString("end_utilhdx"));
			vo.setEndUtilhdxperc(rs.getString("end_utilhdxperc"));
			vo.setCollecttime(rs.getString("collecttime"));
		} catch (Exception e) {
			SysLogger.error("DeviceLinkPortReportDao.loadFromRS()", e);
			vo = null;
		}
		return vo;
	}
	
	// 获取当前所有设备信息
	// 根据设备信息从链路表中获取链路信息
	public List<Link> getAllLink(){
		// 直接从LinkDao调用
		List<Link> linkList = new ArrayList<Link>();
		LinkDao linkDao = new LinkDao();
		
		try{
			//System.out.println("getAllLink()");
			linkList = linkDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			linkDao.close();
		}
		return linkList;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public void execute(String startTime,String endTime){
		List<Link> links = getAllLink();
		System.out.println("links size : " + links.size());
		DeviceLinkPortReportDao dao;
		int i=0;
		for (Link link : links) {
			i++;
			
			if(link != null){
				dao = new DeviceLinkPortReportDao();
				try{
					dao.save(getLinkPort(link,startTime,endTime));
			
				} catch (Exception e){
					e.printStackTrace();
				} finally {
					dao.close();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param link
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public DeviceLinkPortReport getLinkPort(Link link, String startTime,String endTime){
		System.out.println("1111111");
		DeviceLinkPortReport linkport = new DeviceLinkPortReport();
	//	Map ipmap = getIP(link.getLinkName());
		Map ipmap = getIP(link);
		System.out.println("22222");
		linkport.setStartDeviceIP((String)ipmap.get("startip"));
		linkport.setStartDeviceName((String)ipmap.get("startname"));
		linkport.setStartPort((String)ipmap.get("startport"));
		
		linkport.setEndDeviceIP((String)ipmap.get("endip"));
		linkport.setEndDeviceName((String)ipmap.get("endname"));
		linkport.setEndPort((String)ipmap.get("endport"));
		
		Map inMap = utilhdxS(link, startTime,endTime, "in",UTILHDX);
		if(inMap.size()>0) {
			linkport.setStartUtilhdx((String)inMap.get("avg"));
		}
		System.out.println("33333");
		Map outMap = utilhdxS(link, startTime,endTime, "out",UTILHDX);
		if(outMap.size()>0) {
			linkport.setEndUtilhdx((String)outMap.get("avg"));
		}
		System.out.println("44444");
		inMap = utilhdxS(link, startTime,endTime, "in",UTILHDXPERC);
		if(inMap.size()>0)
		linkport.setStartUtilhdxperc((String)inMap.get("avg"));
		outMap = utilhdxS(link, startTime,endTime, "out",UTILHDXPERC);
		if(outMap.size()>0)
		linkport.setEndUtilhdxperc((String)outMap.get("avg"));
		System.out.println("5555555");
//		linkport.setCollecttime(""+Calendar.getInstance().get(Calendar.YEAR)
//				+Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
		linkport.setCollecttime(createTime(startTime,endTime));
		
		System.out.println(linkport);
		return linkport;
	}
 
	public String createTime(String start,String end){
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		try {
			calStart.setTime(sdf.parse(start));
			calEnd.setTime(sdf.parse(end));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long cha = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
		cal.setTime(now);
		return sdf.format(new Date(calStart.getTimeInMillis()+(cha/2)));
	}
	
	
	public String getNodeAlias(int id, String ip){
		
		
		if(id < 1 || ip == null || ip.trim().length() < 1){
			return "";
		}
		//List<NodeDTO> nodes = getAllNodes();
		for(NodeDTO node : BiosReportBaseDao.nodeList) {
			if(node.getId() == id && node.getIpaddress().equals(ip)){
				return node.getName();
			}
		}
		return "";
	}
	
	public Map utilhdxS(Link link,String startTime, String endTime,String inOrOut, String tableType){
		String conditionKey = "";
		if(UTILHDX.equals(tableType)){
			if("in".equals(inOrOut)){
				conditionKey = "InBandwidthUtilHdx";
			} else if("out".equals(inOrOut)) {
				conditionKey = "OutBandwidthUtilHdx";
			}
		} else if (UTILHDXPERC.equals(tableType)){
			if("in".equals(inOrOut)){
				conditionKey = "InBandwidthUtilHdxPerc";
			} else if("out".equals(inOrOut)) {
				conditionKey = "OutBandwidthUtilHdxPerc";
			}
		}
		
		Map<String,String> info = getIP(link);
		String queryTable = tableType + get_IP(info.get("startip"));
		if(!tableIsExist(queryTable)){
			System.out.println("导出链路端口报表时，发现表 "+queryTable +" 不存在！in"
					+this.getClass().getName());
			return new HashMap();
		}
		
		String utilhdxCondition = " where entity='"+conditionKey+"' and "
				+ " collecttime > '" + startTime +"' and collecttime <='"
				+ endTime +"'";
		String utilhdxSQL = getCommonSQL(queryTable,utilhdxCondition);
		//System.out.println(utilhdxSQL);
		return getCommonVal(utilhdxSQL);
	}
	
	public String getCommonSQL(String table,String condition){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table 
		+ " "+condition;
		//System.out.println(sql);
		return sql;
	}
	public String get_IP(String ip){
		return ip.replace('.', '_');
	}
	
	/**
     * 	测试传入表在数据库中是否存在，主要用于数据归档处理
     * @param tableName
     * @return
     */
    public boolean tableIsExist(String tableName){
    	if(tableName != null && !"".equals(tableName.trim())){
    		rs = conn.executeQuery("select * from information_schema.tables WHERE table_name = '"+ tableName+"';");
    		try {
				while(rs.next()){
					return true;
				}
				return false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				SysLogger.error("系统测试当前表"+tableName +"是否存在！in DeviceLinkPortReportDao.tableIsExist()", e);
			} 
    	}else {
    		return false;
    	}
    	return false;
    }
	/**
	 * 由于此方法获取设备ip信息是通过解析linkname来获取，不保证准确性
	 * @param linkname
	 * @return
	 */
//	public Map<String,String> getIP(String linkname){
//		Map<String, String> map = new HashMap<String,String>();
//		if(linkname == null || linkname.trim().length() <= 0) {
//			return map;
//		}
//		
//		int pos = linkname.indexOf('/');
//		String start = linkname.substring(0, pos);
//		String end = linkname.substring(pos+1);
//		
//		int start_pos = start.indexOf('_');
//		int end_pos = end.indexOf('_');
//		
//		map.put("startip", start.substring(0,start_pos));
//		map.put("startport" , start.substring(start_pos+1));
//		map.put("endip" ,end.substring(0,end_pos));
//		map.put("endport", end.substring(end_pos+1));
//		
//		return map;
//	}
	/**
	 * 获取链路设备ip port 信息
	 * @param link
	 * @return
	 */
	public Map<String,String> getIP(Link link){
		Map<String, String> map = new HashMap<String,String>();
	
		String startport = link.getStartIndex() == null ? "" : link.getStartIndex();
		String endport = link.getEndIndex() == null ? "" : link.getEndIndex();
		
		String startip = link.getStartIp();
		String[] startInfo = new String[2];
		String[] endInfo = new String[2];
		if (startip == null || startip.trim().length() == 0) {
			startInfo = getIP(link.getStartId());
			startip = startInfo[0];
		}
		String endip = link.getStartIp();
		if (endip == null || endip.trim().length() == 0) {
			endInfo = getIP(link.getEndId());
			endip = endInfo[0];
		}
		
		map.put("startip", startip);
		map.put("startport" , startport);
		map.put("endip" ,endip);
		map.put("endport", endport);
		map.put("startname", startInfo[1]);
		map.put("endname", endInfo[1]);
		
		return map;
	}
	/**
	 * 传入设备id，获取设备的ip信息
	 * 
	 * @param nodeid
	 * @return ip
	 */
	public String[] getIP(int nodeId){
		String[] s = new String[]{"",""};
        HostNodeDao hostNodeDao = new HostNodeDao();
        BaseVo vo = null;
        try {
            vo = hostNodeDao.findByID(String.valueOf(nodeId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(vo);
        s[0] = nodeDTO.getIpaddress();
        s[1] = nodeDTO.getName();
        String ip = nodeUtil.conversionToNodeDTO(vo).getIpaddress();
		//return ip == null ? "" : ip;
        return s;
	}
	
	/**
	 * 获取通用数据的方法
	 * 
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String,String> getCommonVal(String sql){
		Map<String,String> map = new HashMap<String, String>();
		
		try {
			rs = conn.executeQuery(sql);
			while(rs.next()){
				String max = rs.getString("max_val");
				map.put("max", max);
				String avg = rs.getString("avg_val");
				map.put("avg", avg);
				String min = rs.getString("min_val");
				map.put("min", min);
				
				//System.out.println("max=" + max + ", avg=" + avg +", min=" + min);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = null;
			return map;
		} finally {
			if (map.size()<1) {
				map.put("max", "0");
				map.put("avg", "0");
				map.put("min", "0");
			}
		}
		return map;
	}
	
	public static void main(String[] args) {
		DeviceLinkPortReportDao dao = new DeviceLinkPortReportDao();
//		DeviceLinkPortReport linkport = new DeviceLinkPortReport();
//		linkport.setStartDeviceIP("192.168.201.4");
//		linkport.setStartDeviceName("IT管控监测展示服务器");
//		linkport.setStartPort("8080");
//		linkport.setStartUtilhdx("12323.232");
//		linkport.setStartUtilhdxperc("23.2");
//		
//		linkport.setEndDeviceIP("192.168.201.6");
//		linkport.setEndDeviceName("IT管控监测采集服务器");
//		linkport.setEndPort("80804");
//		linkport.setEndUtilhdx("123.232");
//		linkport.setEndUtilhdxperc("45.3");
//		linkport.setCollecttime(SysUtil.getCurrentTime());
		//dao.save(linkport);
		//dao.getIP("sd");
		//dao.test("2012-08-01 00:00:00", "2012-08-28 23:59:59");
//		dao.getIP("192.168.1.112_0/192.168.1.53_0");
//		String str = "    ";
//		int length = str.trim().length();
//		System.out.println(length);
		int id = 6;
		Link link = new Link();
		link.setStartId(15);
		link.setEndId(13);
		Map map = dao.getIP(link);
		
		System.out.println(map.get("startip"));
		System.out.println(map.get("endip"));
		System.out.println(map.get("startport"));
		System.out.println(map.get("endport"));
		System.out.println(map.get("startname"));
		System.out.println(map.get("endname"));
		
		
	}
}
