/*
 * @(#)IpManagerBasicManager.java     v1.01, Feb 20, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.ipresource.manage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.ipresource.dao.IpManageBasicDao;
import com.afunms.ipresource.model.IpManageBasicVo;
import com.afunms.polling.om.IpMac;
import com.afunms.topology.dao.IpMacDao;

/**
 * ClassName: IpManageBasicManager.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Feb 24, 2014 5:24:41 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class IpManageBasicManager extends BaseManager implements
		ManagerInterface {

	public String execute(String action) {
		if (action.equals("list"))
			return list();
		if (action.equals("toAdd"))
			return toAdd();
		if (action.equals("add"))
			return add();
		if (action.equals("toEdit"))
			return toEdit();
		if (action.equals("edit"))
			return edit();
		if (action.equals("addIp"))
			return addIp();
		if (action.equals("delete"))
			return delete();
		if (action.equals("refresh"))
			return refresh();
		if (action.equals("addManage"))
			return addManage();
		if (action.equals("cancelManage"))
			return cancelManage();

		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	private String addManage() {
		String id = getParaValue("id");
		IpManageBasicDao dao = new IpManageBasicDao();
		try {
			dao.setManage(id, "1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}

	private String cancelManage() {
		String id = getParaValue("id");
		IpManageBasicDao dao = new IpManageBasicDao();
		try {
			dao.setManage(id, "0");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}

	private String list() {
		IpManageBasicDao dao = new IpManageBasicDao();
		String ipaddress = getParaValue("ipaddress");
		String where = " order by ipaddress asc";
		if (ipaddress != null && !"null".equals(ipaddress)
				&& !"".equals(ipaddress)) {
			where = " where ipaddress like '%" + ipaddress + "%' order ipaddress asc";
		}
		
		if("false".equals(getParaValue("argument"))) {
			where = " order by ipaddress asc";
		}
		list(dao, where);
		return "/topology/ipmanage/list.jsp";
	}

	private String toAdd() {
		String jsp = "/topology/ipmanage/add.jsp";
		return jsp;
	}

	private String delete() {
		String[] ids = request.getParameterValues("checkbox");
		IpManageBasicDao dao = new IpManageBasicDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}

	private String refresh() {
		// 刷新数据，将 基础表 里面的数据剔除重复，然后再将ipmac里面表的没出现的数据copy到基础表里面
		IpManageBasicDao ipmanagedao = new IpManageBasicDao();
		List<IpManageBasicVo> ipmanageList = null;
		try {
			ipmanageList = ipmanagedao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ipmanagedao.close();
		}

		// 增加ipmac多出来的未出现的
		IpMacDao ipmacDao = new IpMacDao();
		List<IpMac> ipmacList = null;
		try {
			ipmacList = ipmacDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ipmacDao.close();
		}

		Hashtable<String, IpMac> ipmacHash = new Hashtable<String, IpMac>();
		if (ipmacList != null && ipmacList.size() > 0) {
			for (int i = 0; i < ipmacList.size(); i++) {
				IpMac ipmacvo = ipmacList.get(i);
				if (ipmacvo != null && ipmacvo.getIpaddress() != null
						&& !"".equals(ipmacvo.getIpaddress())
						&& !"null".equals(ipmacvo.getIpaddress())) {
					ipmacHash.put(ipmacvo.getIpaddress().trim(), ipmacvo);
				}
			}
		}

		// 剔除重复
		Hashtable<String, IpManageBasicVo> ipmanageHash = new Hashtable<String, IpManageBasicVo>();
		if (ipmanageList != null && ipmanageList.size() > 0) {
			for (int i = 0; i < ipmanageList.size(); i++) {
				IpManageBasicVo vo = ipmanageList.get(i);
				if (vo != null && vo.getIpaddress() != null
						&& !"".equals(vo.getIpaddress())
						&& !"null".equals(vo.getIpaddress())) {
					ipmanageHash.put(vo.getIpaddress().trim(), vo);
				}
			}
		}
		Iterator iter1 = ipmacHash.keySet().iterator();
		while (iter1.hasNext()) {
			String key = (String) iter1.next();
			if (!ipmanageHash.containsKey(key)) {
				IpManageBasicVo basicVo = IpMacToIpManage(ipmacHash.get(key));
				if (basicVo != null && basicVo.getIpaddress() != null
						&& !"".equals(basicVo.getIpaddress())
						&& !"null".equals(basicVo.getIpaddress())) {
					ipmanageHash.put(basicVo.getIpaddress().trim(), basicVo);
				}
			}
		}

		ipmanagedao = new IpManageBasicDao();
		try {
			ipmanagedao.truncateTable();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ipmanagedao.close();
		}
		DBManager dbManager = new DBManager();
		Iterator iter2 = ipmanageHash.keySet().iterator();
		while (iter2.hasNext()) {
			String key = (String) iter2.next();
			IpManageBasicVo vo = ipmanageHash.get(key);
			StringBuffer sql = new StringBuffer();
			sql
					.append("insert into ip_manage_basic(relateipaddr,ifindex,ipaddress,mac,ifband,ifmanage,collecttime,bak) values('");
			sql.append(vo.getRelateipaddr());
			sql.append("','");
			sql.append(vo.getIfindex());
			sql.append("','");
			sql.append(vo.getIpaddress());
			sql.append("','");
			sql.append(vo.getMac());
			sql.append("','");
			sql.append(vo.getIfband());
			sql.append("','");
			sql.append(vo.getIfmanage());
			sql.append("','");
			Calendar cal = Calendar.getInstance();
			cal.setTime(vo.getCollecttime().getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(cal.getTime());
			sql.append(date);
			sql.append("','");
			sql.append(vo.getBak());
			sql.append("')");
			dbManager.addBatch(sql.toString());
		}
		try {
			dbManager.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.close();
		}
		return list();
	}

	private IpManageBasicVo IpMacToIpManage(IpMac ipmac) {
		IpManageBasicVo vo = new IpManageBasicVo();
		vo.setRelateipaddr(ipmac.getRelateipaddr());
		vo.setIfindex(ipmac.getIfindex());
		vo.setIpaddress(ipmac.getIpaddress());
		vo.setMac(ipmac.getMac());
		vo.setCollecttime(ipmac.getCollecttime());
		vo.setIfband(ipmac.getIfband());
		vo.setIfmanage("1"); // 管理
		vo.setBak(ipmac.getBak());
		return vo;
	}

	private String addIp() {
		IpManageBasicVo vo = new IpManageBasicVo();
		String sindex = getParaValue("sindex");
		vo.setIpaddress(sindex);
		vo.setIfband("1");
		vo.setIfmanage("1");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		vo.setCollecttime(cal);

		IpManageBasicDao dao = new IpManageBasicDao();
		// 先查询一下，如果存在，就不插入
		try {
			IpManageBasicVo mangevo = dao.findByIpaddress(vo.getIpaddress());
			if (mangevo != null) {
				return list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		dao = new IpManageBasicDao();
		try {
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		String alarmId = getParaValue("alarmId");
		// 清除告警
		CheckEventDao checkEventDao = new CheckEventDao();
		try {
			checkEventDao.deleteByAlarmIdAndSindex(alarmId, sindex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkEventDao.close();
		}
		return list();
	}

	private String add() {
		IpManageBasicVo vo = createIpManageBasicVo();

		// 先查询一下，如果存在，就不插入
		IpManageBasicDao dao = new IpManageBasicDao();
		try {
			IpManageBasicVo mangevo = dao.findByIpaddress(vo.getIpaddress());
			if (mangevo == null) {
				return list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		dao = new IpManageBasicDao();
		try {
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}

	private String toEdit() {
		String id = getParaValue("id");
		IpManageBasicDao dao = new IpManageBasicDao();
		IpManageBasicVo vo = (IpManageBasicVo) dao.findByID(id);
		request.setAttribute("vo", vo);
		return "/topology/ipmanage/edit.jsp";
	}

	private String edit() {
		IpManageBasicDao dao = new IpManageBasicDao();
		IpManageBasicVo vo = null;
		try {
			vo = (IpManageBasicVo) dao.findByID(getParaValue("id"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setMac(getParaValue("mac"));
		vo.setBak(getParaValue("bak"));
		vo.setIfmanage(getParaValue("ifmanage"));
		
		dao = new IpManageBasicDao();
		try {
			dao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return "/ipmanage.do?action=list&jp=1&argument=false";
	}

	private IpManageBasicVo createIpManageBasicVo() {
		IpManageBasicVo vo = new IpManageBasicVo();
		vo.setId(Long.valueOf(getParaIntValue("id")));
		vo.setRelateipaddr(getParaValue("relateipaddr"));
		vo.setIfindex(getParaValue("ifindex"));
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setMac(getParaValue("mac"));
		String ifband = getParaValue("ifband");
		if (ifband == null || "".equals(ifband) || "null".equals(ifband)
				|| "0".equals(ifband)) {
			ifband = "0";
		} else {
			ifband = "1";
		}
		vo.setIfband(ifband);
		String ifmanage = getParaValue("ifmanage");
		if (ifmanage == null || "null".equals(ifmanage) || "".equals(ifmanage)) {
			ifmanage = "1";
		}
		vo.setIfmanage(ifmanage);
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		vo.setCollecttime(cal);
		vo.setBak(getParaValue("bak"));
		return vo;
	}

}
