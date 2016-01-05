package com.afunms.config.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Hua3VPNFileConfig;

public class Hua3VPNFileConfigDao extends BaseDao implements DaoInterface {

	public Hua3VPNFileConfigDao() {
		super("nms_hua3vpncfg");
	}

	public List loadByIp(String ipaddress) {
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from nms_hua3vpncfg where ipaddress='");
		sql.append(ipaddress);
		sql.append("'");
		rs = conn.executeQuery(sql.toString());
		try {
			while (rs.next())
				list.add(loadFromRS(rs));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List listByPage(int curpage,int perpage)
	{	
		return listByPage(curpage,"",perpage);	   
	}
	public List listByPage(int curpage, String where, int perpage) {
		List list = new ArrayList();
		try {
			rs = conn.executeQuery("select count(*) from (select max(id) 'id',ipaddress,fileName,descri,backup_time,file_size,bkp_type from nms_hua3vpncfg group by ipaddress) t");
			if (rs.next())
				jspPage = new JspPage(perpage, curpage, rs.getInt(1));

			rs = conn.executeQuery("select max(id) 'id',ipaddress,fileName,descri,backup_time,file_size,bkp_type from nms_hua3vpncfg group by ipaddress;");
			// SysLogger.info("select * from " + table + " " + where );
			int loop = 0;
			while (rs.next()) {
				loop++;
				if (loop < jspPage.getMinNum())
					continue;
				list.add(loadFromRS(rs));
				if (loop == jspPage.getMaxNum())
					break;
			}
		} catch (Exception e) {
			SysLogger.error("BaseDao.listByPage()", e);
			list = null;
		} finally {
			conn.close();
		}
		return list;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		Hua3VPNFileConfig h3 = (Hua3VPNFileConfig) vo;
		StringBuffer sql = new StringBuffer(
				"insert into nms_hua3vpncfg(id,ipaddress,fileName,descri,backup_time,file_size,bkp_type) values(");
		sql.append(this.getNextID());
		sql.append(",'");
		sql.append(h3.getIpaddress());
		sql.append("','");
		sql.append(h3.getFileName());
		sql.append("','");
		sql.append(h3.getDescri());
		sql.append("','");
		sql.append(h3.getBackupTime());
		sql.append("',");
		sql.append(h3.getFileSize());
		sql.append(",'");
		sql.append(h3.getBkpType());
		sql.append("')");
		return this.saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub

		Hua3VPNFileConfig vo = new Hua3VPNFileConfig();
		try {
			vo.setId(rs.getInt("id"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setFileName(rs.getString("fileName"));
			vo.setDescri(rs.getString("descri"));
			vo.setFileSize(rs.getInt("file_size"));
			vo.setBackupTime(rs.getTimestamp("backup_time"));
			vo.setBkpType(rs.getString("bkp_type"));
		} catch (Exception e) {
			SysLogger.error("Hua3VPNFileConfigDao.loadFromRS()", e);
			vo = null;
		}
		return vo;
	}

	/*
	 * 获得含有配置文件的设备，List返回含有最近一次备份文件更新的设备。
	 */
	public List getDeviceWithLastModify() {
		List list = null;
		String sql = "select max(id) 'id',ipaddress,fileName,descri,max(backup_time) 'backup_time',file_size,bkp_type from nms_hua3vpncfg  group by ipaddress";
		list = this.findByCriteria(sql);
		return list;
	}
	/*
	 * 获得含有配置文件的设备，List返回含有最近一次备份文件更新的设备。
	 */
	public List getDeviceByIps(List iplist,String type) {
		StringBuffer sBuffer=new StringBuffer();
		for (int i = 0; i < iplist.size(); i++) {
			if (i<iplist.size()-1) {
				sBuffer.append(iplist.get(i)+"','");
			}else {
				sBuffer.append(iplist.get(i)+"'");
			}
			
		}
		List list = null;
		String sql = "select max(id) 'id',ipaddress,fileName,descri,max(backup_time) 'backup_time',file_size,bkp_type from nms_hua3vpncfg where ipaddress in('"+sBuffer.toString()+") and bkp_type='"+type+"' group by ipaddress";
		list = this.findByCriteria(sql);
		return list;
	}

}
