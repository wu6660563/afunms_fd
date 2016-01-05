/**
 * <p>Description: app_db_node</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.afunms.application.model.MQchannelConfig;
import com.afunms.application.model.MQConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;
import com.afunms.system.model.User;

public class MQchannelConfigDao extends BaseDao implements DaoInterface {

	public MQchannelConfigDao() {
		super("nms_mqchannelconfig");
	}
	public boolean delete(String []ids){
		return super.delete(ids);
	}
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		
		MQchannelConfig vo=new MQchannelConfig();
		
		try {
			vo.setId(rs.getInt("id"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setChlindex(rs.getInt("chlindex"));
			vo.setChlname(rs.getString("chlname"));
			vo.setLinkuse(rs.getString("linkuse"));
			vo.setSms(rs.getInt("sms"));
			vo.setBak(rs.getString("bak"));
			vo.setReportflag(rs.getInt("reportflag"));
			vo.setConnipaddress(rs.getString("connipaddress"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	return vo;
	}

	public void fromLastToMqChannelconfig(){
		SysLogger.info("begin process fromLastToMqChannelconfig---------------");		
		List list=new ArrayList();
		List list1=new ArrayList();
		List shareList = new ArrayList();
		Hashtable mqchlhash= new Hashtable();
		Session session=null;
		MQchannelConfig mqchannelconfig = null;
		Vector configV = new Vector();
		try{
		
		//从MQ配置表里获取列表
		try{
			String sql = "select * from nms_mqchannelconfig order by ipaddress,chlname";
			list1=findByCriteria(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (list1 != null && list1.size()>0){
			for(int i=0;i<list1.size();i++){
				mqchannelconfig = (MQchannelConfig)list1.get(i);	
				//IP:通道名称:连接IP
				mqchlhash.put(mqchannelconfig.getIpaddress()+":"+mqchannelconfig.getChlname().trim()+":"+mqchannelconfig.getConnipaddress(),mqchannelconfig);
			}
		}
		
		
		//从内存中得到所有MQ采集信息
		Hashtable sharedata = ShareData.getMqdata();
		
		//从数据库得到监视MQ列表
		MQConfigDao configdao = new MQConfigDao();
		shareList = configdao.getMQByFlag(1);		
		if (shareList != null && shareList.size()>0){
			for(int i=0;i<shareList.size();i++){
				MQConfig mqconf = (MQConfig)shareList.get(i);
				if(sharedata.get(mqconf.getIpaddress()+":"+mqconf.getManagername()) != null){
					Hashtable mqData = (Hashtable)sharedata.get(mqconf.getIpaddress()+":"+mqconf.getManagername());
					if (mqData == null )continue;
					Vector vector = (Vector)mqData.get("mqValue");
					if (vector !=null && vector.size()>0){
						for(int k=0;k<vector.size();k++){
							Hashtable cAttr = (Hashtable)vector.get(k);
							cAttr.put("ip", mqconf.getIpaddress());
							//if (disk.getEntity().equals("AllSize")){
								list.add(cAttr);
								SysLogger.info("add --- "+mqconf.getIpaddress());								
							//}
						}
					}									
				}
			}
		}
		//判断采集到的MQ信息是否已经在MQ配置表里已经存在，若不存在则加入
		if (list != null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Hashtable cAttr = (Hashtable)list.get(i);
				String ip = (String)cAttr.get("ip");
				String chlname = (String)cAttr.get("name");
				chlname = chlname.trim();
				String connName = (String)cAttr.get("connName");
				connName = connName.trim();
				SysLogger.info("$$$$==================="+ip+":"+chlname+":"+connName);				
				if (!mqchlhash.containsKey(ip+":"+chlname+":"+connName)){
					SysLogger.info("==================="+ip+":"+chlname);					
					mqchannelconfig = new MQchannelConfig();
					mqchannelconfig.setChlname(chlname);
					mqchannelconfig.setBak("");
					mqchannelconfig.setIpaddress(ip);
					mqchannelconfig.setLinkuse("");					
					mqchannelconfig.setConnipaddress(connName);
					mqchannelconfig.setSms(new Integer(1));// 0：不告警 1：告警，默认的情况是不发送短信
					mqchannelconfig.setReportflag(new Integer(0));// 0：不存在于报表 1：存在于报表，默认的情况是不存在于报表
					conn = new DBManager();
					save(mqchannelconfig);
					mqchlhash.put(ip+":"+chlname+":"+connName,mqchannelconfig);					
				}
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	// TODO Auto-generated method stub
}
	
	public Hashtable getByAlarmflag(Integer smsflag) {
		List list = new ArrayList();
		Session session=null;
		Hashtable retValue = new Hashtable();
		try{
			String sql = "select * from nms_mqchannelconfig where sms="+smsflag+" order by ipaddress";
			list=findByCriteria(sql);	
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					MQchannelConfig mqchannelconfig = (MQchannelConfig)list.get(i);					
					retValue.put(mqchannelconfig.getIpaddress()+":"+mqchannelconfig.getChlname()+":"+mqchannelconfig.getConnipaddress(), mqchannelconfig);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			//this.error("getByIpAndRediskflag ip error:"+e);
		}
	// TODO Auto-generated method stub
		return retValue;
}
	
	public boolean save(BaseVo vo) {
		MQchannelConfig vo1=(MQchannelConfig)vo;
		StringBuffer sql=new StringBuffer();
		
		sql.append("insert into nms_mqchannelconfig(ipaddress,chlindex,chlname,linkuse,sms,bak,reportflag,connipaddress)values('");
		sql.append(vo1.getIpaddress());
		sql.append("',");
		sql.append(vo1.getChlindex());
		sql.append(",'");
		sql.append(vo1.getChlname());
		sql.append("','");
		sql.append(vo1.getLinkuse());
		sql.append("',");
		sql.append(vo1.getSms());
		sql.append(",'");
		sql.append(vo1.getBak());
		sql.append("',");
		sql.append(vo1.getReportflag());
		sql.append(",'");
		sql.append(vo1.getConnipaddress());
		sql.append("')");
		
		return saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		MQchannelConfig vo1=(MQchannelConfig)vo;
		boolean result=false;
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_mqchannelconfig set ipaddress='");
		sql.append(vo1.getIpaddress());
		sql.append("',chlindex=");
		sql.append(vo1.getChlindex());
		sql.append(",chlname='");
		sql.append(vo1.getChlname());
		sql.append("',linkuse='");
		sql.append(vo1.getLinkuse());
		sql.append("',sms=");
		sql.append(vo1.getSms());
		sql.append(",bak='");
		sql.append(vo1.getBak());
		sql.append("',reportflag=");
		sql.append(vo1.getReportflag());
		sql.append(",connipaddress='");
		sql.append(vo1.getConnipaddress());
		sql.append("'where id=");
		sql.append(vo1.getId());
		return saveOrUpdate(sql.toString());
	}
	
	   public List getMqChanelByBID(Vector bids){
		   List rlist = new ArrayList();
		   StringBuffer sql = new StringBuffer();
		   String wstr = "";
		   if(bids != null && bids.size()>0){
			   for(int i=0;i<bids.size();i++){
				   if(wstr.trim().length()==0){
					   wstr = wstr+" where ( netid like '%,"+bids.get(i)+",%' "; 
				   }else{
					   wstr = wstr+" or netid like '%,"+bids.get(i)+",%' ";
				   }
				   
			   }
			   wstr=wstr+")";
		   }
		   sql.append("select * from nms_mqchannelconfig "+wstr);
		   //SysLogger.info(sql.toString());
		   return findByCriteria(sql.toString());
	   }

}