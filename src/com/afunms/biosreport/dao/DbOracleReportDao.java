package com.afunms.biosreport.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.util.IpTranslation;
import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.model.DbOracleReportModel;
import com.afunms.biosreport.model.NetDeviceReport;
import com.afunms.indicators.model.NodeDTO;

/**
 * Oracle 数据库持久层数据操作类
 * 
 * @author Administrator
 *
 */
public class DbOracleReportDao extends BiosReportBaseDao{

	public List<DbOracleReportModel> getDevice(String start, String end){
		ArrayList<DbOracleReportModel> device = new ArrayList<DbOracleReportModel>();
		List<NodeDTO> list = super.getNodesByKind(BiosConstant.getInstance().DB_ORACLE);
		int nx=0;
		for(NodeDTO n : list){
			device.add(createNetDevice(n, start, end));
//			nx++;
//			if(nx >2){
//				break;
//			}
		}
//        device.add(create());
		return device;
	}
	
	public DbOracleReportModel create(){
		DbOracleReportModel v = new DbOracleReportModel();
		v.setNodeid("123");
		v.setAlarmCommon("1");
		v.setAlarmSerious("2");
		v.setAlarmUrgency("3");
		v.setAliasName("sfsd");
		v.setAvergeConnectivity("23.2");
		v.setBufferCache("231");
		//v.setCollectTime(createTime());
		v.setDictionaryCache("12");
		v.setIpAddress("192.168.1.18");
		v.setMemorySort("1233");
		return v;
	}
	
	
	/**
	 * 构造报表中的单个网络设备的数据信息
	 * 
	 * @param node
	 * @param start
	 * @param end
	 * @return
	 */
	public DbOracleReportModel createNetDevice(NodeDTO node, String start, String end ){
		
		System.out.println(node.toString());
		
		DbOracleReportModel netDevice = new DbOracleReportModel(node.getIpaddress(),node.getName(),"");
		
		String ip = node.getIpaddress();
		if(null == ip || ip.length()<1){
			return null;
		}
		
		String _ip = ip.replace('.', '_');
		
		netDevice.setNodeid(node.getNodeid());
		netDevice.setCollectTime(createTime(start,end));
		
	    // 获取平均连通率
		// Oracle ping信息存储在oraping开头的表中
	    String pingTable = "oraping" + _ip;
	    Map<String,String> pingMap = getCommonVal(getCommonSQL(pingTable, start, end));
	    netDevice.setAvergeConnectivity(pingMap.get("avg"));
	    
	    /**
	     * nms_oramemperfvalue
	     * 缓冲区命中率：buffercache
	     * 数据字典命中率：dictionarycache
	     * 内存中的排序：pctmemorysorts
	     * 
	     *  IpTranslation tranfer = new IpTranslation();
	     * os.setServerip(tranfer.formIpToHex(serverIP) + ":"
                            + oracleEntity.getId());
	     */
	    
	    
	    System.out.println("IP"+node.getIpaddress() + " nodeid " + node.getNodeid()
	    		+ " subtype:" + node.getType());
	    
	    // 告警数
	    Map<String,String> alarmMap = getAlarmValue(Integer.parseInt(node.getNodeid()), node.getType(), start, end);
	    netDevice.setAlarmCommon(alarmMap.get("1")); // 普通告警数
	    netDevice.setAlarmSerious(alarmMap.get("2")); // 严重告警数
	    netDevice.setAlarmUrgency(alarmMap.get("3")); // 紧急告警数
		
	    getOtherInfo(node,netDevice);
	    
	    System.out.println(netDevice.toString());
	    
		return netDevice;
	}
	
	private void getOtherInfo(NodeDTO node, DbOracleReportModel device){

        OracleEntity oracleEntity = null;
        OraclePartsDao partdao = new OraclePartsDao();
        try {
            oracleEntity = (OracleEntity) partdao.findByID(node.getNodeid());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (partdao != null)
                partdao.close();
        }
        if (oracleEntity == null) {
            return ;
        }
        
        IpTranslation tranfer = new IpTranslation();
        String serverIp = tranfer.formIpToHex(node.getIpaddress()) + ":"
        	+ oracleEntity.getId();
        System.out.println("IP:" + node.getIpaddress() + " serverIP:"+serverIp );
        
        DBDao dao = new DBDao();
        try{
        	Map<String,String> map = dao.getOracle_nmsoramemperfvalue(serverIp);
        	device.setBufferCache(map.get("buffercache"));			// 缓冲区命中率
        	device.setDictionaryCache(map.get("dictionarycache"));	// 数据字典命中率
        	device.setMemorySort(map.get("pctmemorysorts"));		// 内存中的排序
        	map = dao.getOracle_nmsoramemvalue(serverIp);
        	
        	/**
        	 * pga(%) total_pga_inuse / total_pga_allocated
        	 */
        	String in = map.get("total_PGA_allocated");
        	String all = map.get("total_PGA_inuse");
        	double allocated = Double.valueOf(in.substring(0, in.length()-2));
        	double inuse = Double.valueOf(all.substring(0,all.length()-2));
        	
        	System.out.println("allocated:"+allocated +",inuse:"+inuse);
        	
        	if(inuse > 0 ){
        		device.setPga(String.valueOf((100*inuse) / allocated));
        	}else{
        		device.setPga("0");
        	}
        	
        	System.out.println("pga="+ inuse + " / " + allocated );
        	
        	/**
        	 * sga(%) java_pool + shared_pool + large_pool + streams_pool / sga_sum
        	 */
        	//device.setPga(map.get("aggregate_pga_target_parameter"));	// pga -- 总计pga目标
        	//device.setSga(map.get("shared_pool"));	// sga --共享池
        	double java_pool = checkPara(map.get("java_pool"));
        	double shared_pool = checkPara(map.get("shared_pool"));
        	double large_pool = checkPara(map.get("large_pool"));
        	double streams_pool = checkPara(map.get("streams_pool"));
        	double sga_sum = checkPara(map.get("sga_sum"));
        	double sga = 0.0;
        	if(sga_sum >0){
        		sga = (100 * (java_pool + shared_pool + large_pool + streams_pool)) / sga_sum;
        	}
        	
        	System.out.println("sga: java_pool" + java_pool + " shared " + shared_pool +
        			" large " + large_pool + " streams " + streams_pool + "----------/"
        			+ " bytes " + sga_sum);
        	
        	device.setSga(String.valueOf(sga));
        	map = dao.getOracle_nmsoracursors(serverIp);
        	device.setOpenCur(map.get("opencur"));	// 打开的游标数
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dao.close();
        }
	}
	
	/**
	 * 对数据库中获取到的参数进行检查，防止有null错误转换
	 * @param str
	 * @return
	 */
	private Double checkPara(String str){
		Double d = 0.0;
		if(null == str || str.length() <= 0){
			return d;
		} else {
			try{
				d = Double.parseDouble(str);
			}catch(Exception e){
				d= 0.0;
			}
			return d;
		}
	}
	
	/**
	 * 插入数据 system_bios_db_oracle
	 * 
	 * @param device
	 */
	public boolean insertData(DbOracleReportModel vo){
		if(null == vo){
			return false;
		}
		boolean flag = true;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into system_bios_db_oracle (" +
				"nodeid, collecttime, ip_address, alias, ping, buffer_cache,"
			  + "dictionary_cache, pga, sga, open_cur, memory_sort, "
			  + "alarm_common, alarm_serious, alarm_urgency) values('");
	    sql.append(vo.getNodeid());
		sql.append("','");
		sql.append(vo.getCollectTime());
		sql.append("','");
		sql.append(vo.getIpAddress());
		sql.append("','");
		sql.append(vo.getAliasName());
		sql.append("','");
		sql.append(vo.getAvergeConnectivity());
		sql.append("','");
		sql.append(vo.getBufferCache());
		sql.append("','");
		sql.append(vo.getDictionaryCache());
		sql.append("','");
		sql.append(vo.getPga());
		sql.append("','");
		sql.append(vo.getSga());
		sql.append("','");
		sql.append(vo.getOpenCur());
		sql.append("','");
		sql.append(vo.getMemorySort());
		sql.append("','");
		sql.append(vo.getAlarmCommon());
		sql.append("','");
		sql.append(vo.getAlarmSerious());
		sql.append("','");
		sql.append(vo.getAlarmUrgency());
		sql.append("')");
		System.out.println(sql);
		System.out.println("INSRT SQL :"  + sql.toString());
		try{
		   saveOrUpdate(sql.toString());
	   }catch(Exception e){
		   flag = false;
	   }
	   return flag;
	
	}
}
