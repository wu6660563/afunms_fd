package com.afunms.application.manage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.afunms.application.dao.ClusterDao;
import com.afunms.application.dao.UpAndDownLogDao;
import com.afunms.application.dao.UpAndDownMachineDao;
import com.afunms.application.model.Cluster;
import com.afunms.application.model.UpAndDownLog;
import com.afunms.application.model.UpAndDownMachine;
import com.afunms.application.util.RemoteClientInfo;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.system.model.User;

public class MachineUpAndDownManager extends BaseManager implements
		ManagerInterface {

	public String execute(String action) {
		// TODO Auto-generated method stub
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("ready_add")) {
			return readyAdd();
		}
		if (action.equals("add")) {
			return add();
		}
		if (action.equals("ready_edit")) {
			return ready_edit();
		}
		if (action.equals("reboot")) {
			return reboot();
		}
		if (action.equals("delete")) {
			return delete();
		}
		if (action.equals("update")) {
			return update();
		}
		if (action.equals("refresh")) {
			return refresh();
		}
		if (action.equals("addCluster")) {
			return addCluster();
		}
		if (action.equals("ready_addCluster")) {
			return ready_addCluster();
		}
		if (action.equals("ready_editName")) {
			return ready_editName();
		}
		if (action.equals("clusterRefresh")) {
			return clusterRefresh();
		}
		if (action.equals("shutdown")) {
			return shutdown();
		}
		if (action.equals("clusterList")) {
			return clusterList();
		}
		//从服务器组中移除
		if (action.equals("delOrAdd")) {
			return delOrAdd();
		}
		//重启服务器组
		if (action.equals("rebootAll")) {
			return rebootAll();
		}
		//关闭服务器组
		if (action.equals("shutdownAll")) {
			return shutdownAll();
		}
		//改变服务器重启和关闭的顺序
		if (action.equals("updateSequence")) {
			return updateSequence();
		}
		if (action.equals("clusterDetailList")) {
			return clusterDetailList();
		}
		// 删除服务器组
		if (action.equals("deleteCluster")) {
			return deleteCluster();
		}
		// 修改服务器组
		if (action.equals("editCluster")) {
			return editCluster();
		}
		// 保存修改服务器组信息
		if (action.equals("saveCluster")) {
			return saveCluster();
		}
		// 操作审计信息
		if(action.equals("logList"))
		{
			return logList();
		}

		return null;
	}
	
	private String logList()
	{
		UpAndDownLogDao dao = new UpAndDownLogDao();
		this.setTarget("/config/vpntelnet/machineUpAndDown/logList.jsp");
		return list(dao);
	}


	private String shutdown() {
		int id = this.getParaIntValue("id");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		UpAndDownMachine machine = (UpAndDownMachine) dao.findByID(id + "");
		dao.close();
		RemoteClientInfo info = ShareData.getIp_clientInfoHash().get(
				machine.getIpaddress());
		if (info != null) {
			String serverType=machine.getServerType();
			if (serverType.equals("windows")) {
				info.executeCmd("shutdown -f -t 0");
			}
			if (serverType.equals("linux")||serverType.equals("unix")) {
				info.executeCmd(" shutdown  now");
			}
			if (serverType.equals("aix")) {
				info.executeCmd("shutdown -F");
				
			}
			if (serverType.equals("as400")) {
				info.executeCmd("PWRDWNSYS *IMMED");
				
			}
		}
		info.closeConnection();
		machine.setMonitorStatus(0);
		machine.setLasttime(new Timestamp(new Date().getTime()));
		UpAndDownMachineDao udao = new UpAndDownMachineDao();
		udao.updateWithTime(machine);
		udao.close();
		//gzm add beign
		UpAndDownLog log = new UpAndDownLog();
		User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		log.setOper_user(user.getName());
		log.setOperid(user.getId()+"");
		log.setIp_address(machine.getIpaddress());
		log.setOper_time(new Timestamp(new Date().getTime()));
		log.setAction(2);//1重启，2关机
		UpAndDownLogDao logDao = new UpAndDownLogDao();
		logDao.save(log);
		logDao.close();
          //gzm add end 
		ShareData.getIp_clientInfoHash().remove(machine.getIpaddress());
		return list();
	}


/**
 * 改变服务器重启和关闭的顺序
 * @return
 */
	public String updateSequence() {
		DBManager dbManager = new DBManager();
		String sql = "";
		int id = 0;
		int sequence = 0;
		String ids = getParaValue("ids");
		String values = getParaValue("values");
		String[] idsArr = new String[ids.split(".").length];
		String[] valuesArr = new String[values.split(".").length];
		idsArr = ids.split("\\.");
		valuesArr = values.split("\\.");

		try {

			for (int i = 0; i < idsArr.length; i++) {
				id = Integer.parseInt(idsArr[i]);
				sequence = Integer.parseInt(valuesArr[i]);
				sql = "update nms_remote_up_down_machine set sequence="
						+ sequence + " where id=" + id;
				dbManager.addBatch(sql);
			}
			dbManager.executeBatch();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbManager.close();
		}

		return clusterDetailList();
	}

	private String refresh() {
		return list();
	}

	private String clusterRefresh() {
		return clusterDetailList();
	}

	private String delete() {
		String[] ids = getParaArrayValue("checkbox");
		if (ids != null && ids.length > 0) {
			UpAndDownMachineDao dao = new UpAndDownMachineDao();
			dao.delete(ids);
			dao.close();
		}
		return list();
	}

	// 从服务器集群中移到默认组中
	private String delOrAdd() {
		//String type = getParaValue("type");
		String[] ids = getParaArrayValue("checkbox");
		DBManager db = new DBManager();
		if (ids != null && ids.length > 0) {
			boolean result = false;
			try {

				for (int i = 0; i < ids.length; i++)
					db.addBatch("update nms_remote_up_down_machine set clusterId=1 where id=" + ids[i]);

				db.executeBatch();
				result = true;
			} catch (Exception ex) {
				SysLogger.error("BaseDao.delete()", ex);
				result = false;
			}finally{
				db.close();
			}
		}
		return clusterDetailList();
	}

	private String ready_edit() {
		int id = this.getParaIntValue("id");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		UpAndDownMachine machine = (UpAndDownMachine) dao.findByID(id + "");
		dao.close();
		ClusterDao clusterdao = new ClusterDao();
		List list = clusterdao.loadAll();
		request.setAttribute("clusterList", list);
		request.setAttribute("machine", machine);
		return "/config/vpntelnet/machineUpAndDown/edit.jsp";
	}

	private String update() {
		int id = this.getParaIntValue("id");
		String lasttime = this.getParaValue("lasttime");
		String ipaddress = getParaValue("ipaddress");
		String name = this.getParaValue("name");
		String serverType = this.getParaValue("serverType");
		String username = this.getParaValue("user");
		String passwd = this.getParaValue("password");
		int isJoin = this.getParaIntValue("isJoin");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		UpAndDownMachine machine = new UpAndDownMachine();
		machine.setId(id);
		machine.setName(name);
		machine.setIpaddress(ipaddress);
		machine.setLasttime(new Timestamp(new Date().getTime()));
		machine.setServerType(serverType);
		machine.setUsername(username);
		machine.setPasswd(passwd);
		machine.setIsJoin(isJoin);
		dao.update(machine);
		dao.close();
		return list();
	}

	private String edit() {
		return "";
	}

	private String reboot() {
		int id = this.getParaIntValue("id");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		UpAndDownMachine machine = (UpAndDownMachine) dao.findByID(id + "");
		dao.close();
		RemoteClientInfo info = ShareData.getIp_clientInfoHash().get(
				machine.getIpaddress());
		if (info != null) {
			String serverType=machine.getServerType();
      if (serverType.equals("windows")) {
				
				info.executeCmd("shutdown -r -f -t 0");
				
			}
			if (serverType.equals("linux")||serverType.equals("unix")) {
				info.executeCmd(" shutdown -r  now");
				
			}
			if (serverType.equals("aix")) {
				info.executeCmd("shutdown –Fr");
				
			}
			info.closeConnection();
			machine.setMonitorStatus(0);
			machine.setLasttime(new Timestamp(new Date().getTime()));
			UpAndDownMachineDao udao = new UpAndDownMachineDao();
			udao.updateWithTime(machine);
			udao.close();
			//gzm add beign
			UpAndDownLog log = new UpAndDownLog();
			User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			log.setOper_user(user.getName());
			log.setOperid(user.getId()+"");
			log.setIp_address(machine.getIpaddress());
			log.setOper_time(new Timestamp(new Date().getTime()));
			log.setAction(1);//1重启，2关机
			UpAndDownLogDao logDao = new UpAndDownLogDao();
			logDao.save(log);
			logDao.close();
              //gzm add end 
			ShareData.getIp_clientInfoHash().remove(machine.getIpaddress());
		}
		return list();
	}

	private String rebootAll() {
		
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		int id = getParaIntValue("clusterId");
		List list = dao.loadClusterList(id);
		dao.close();
		UpAndDownMachineDao udao = new UpAndDownMachineDao();
		int second=0;
		for (int j = 0; j < list.size(); j++) {
			UpAndDownMachine machine = (UpAndDownMachine) list.get(j);
			RemoteClientInfo info = ShareData.getIp_clientInfoHash().get(
					machine.getIpaddress());
			 second=j*5;
			if (info != null) {
				String serverType=machine.getServerType();
				if (serverType.equals("windows")) {
					
					info.executeCmd("shutdown -r -f -t 0");
					
				}
				if (serverType.equals("linux")||serverType.equals("unix")) {
					info.executeCmd(" shutdown -r  now");
					
				}
				if (serverType.equals("aix")) {
					info.executeCmd("shutdown –Fr");
					
				}
				info.closeConnection();
				machine.setMonitorStatus(0);
				machine.setLasttime(new Timestamp(new Date().getTime()));
				
				udao.addBatchUpdateAllTime(machine);
				
				//gzm add beign
				UpAndDownLog log = new UpAndDownLog();
				User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
				log.setOper_user(user.getName());
				log.setOperid(user.getId()+"");
				log.setIp_address(machine.getIpaddress());
				log.setOper_time(new Timestamp(new Date().getTime()));
				log.setAction(1);//1重启，2关机
				UpAndDownLogDao logDao = new UpAndDownLogDao();
				logDao.save(log);
				logDao.close();
	              //gzm add end
				ShareData.getIp_clientInfoHash().remove(machine.getIpaddress());
			}
		}
	       udao.executeBatch();
	       udao.close();
	       
		return clusterDetailList();
	}
	private String shutdownAll() {
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		int id = getParaIntValue("clusterId");
		List list = dao.loadClusterList(id);
		dao.close();
		UpAndDownMachineDao udao = new UpAndDownMachineDao();
		int second=0;
		boolean flag=true;
		Integer[] packet=null;
		for (int j = 0; j < list.size(); j++) {
			UpAndDownMachine machine = (UpAndDownMachine) list.get(j);
			RemoteClientInfo info = ShareData.getIp_clientInfoHash().get(
					machine.getIpaddress());
			second=j*5;
			if (info != null) {
				String serverType=machine.getServerType();
				if (serverType.equals("windows")) {
					info.executeCmd("shutdown -f -t 0");
				}
				if (serverType.equals("linux")||serverType.equals("unix")) {
					info.executeCmd(" shutdown  now");
				}
				if (serverType.equals("aix")) {
					info.executeCmd("shutdown -F");
					
				}
				if (serverType.equals("as400")) {
					info.executeCmd("PWRDWNSYS *IMMED");
					
				}
				try {
					while (flag) {
						PingUtil pingU=new PingUtil(machine.getIpaddress());
						 packet=pingU.ping();
						if (packet[0]!=null) {
							flag=false;
						}else {
							Thread.sleep(5000);
						}
					
					}
					flag=true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			info.closeConnection();
			machine.setMonitorStatus(0);
			machine.setLasttime(new Timestamp(new Date().getTime()));
			
			udao.addBatchUpdateAllTime(machine);
			
			//gzm add beign
			UpAndDownLog log = new UpAndDownLog();
			User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			log.setOper_user(user.getName());
			log.setOperid(user.getId()+"");
			log.setIp_address(machine.getIpaddress());
			log.setOper_time(new Timestamp(new Date().getTime()));
			log.setAction(2);//1重启，2关机
			UpAndDownLogDao logDao = new UpAndDownLogDao();
			logDao.save(log);
			logDao.close();
	          //gzm add end 

			ShareData.getIp_clientInfoHash().remove(machine.getIpaddress());
		}
          udao.executeBatch();
          udao.close();
		return clusterDetailList();
	}
	private String add() {
		String ipaddress = getParaValue("ipaddress");
		String name = this.getParaValue("name");
		String serverType = this.getParaValue("serverType");
		String username = this.getParaValue("user");
		String passwd = this.getParaValue("password");
		int clusterId = this.getParaIntValue("clusterId");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		UpAndDownMachine machine = new UpAndDownMachine();
		machine.setName(name);
		machine.setIpaddress(ipaddress);
		machine.setLasttime(new Timestamp(new Date().getTime()));
		machine.setServerType(serverType);
		machine.setUsername(username);
		machine.setPasswd(passwd);
		Object re = ShareData.getIp_clientInfoHash().get(ipaddress);
		if(re == null)
		{
			machine.setMonitorStatus(0);
		}
		else
			machine.setMonitorStatus(1);
		if (clusterId == 0) {
			machine.setClusterId(0);
		} else {
			machine.setClusterId(clusterId);	
		}
		
		dao.save(machine);
		dao.close();
		return list();
	}

	private String addCluster() {
		ClusterDao clusterDao = new ClusterDao();
		String name = getParaValue("name");
		String serverType = this.getParaValue("serverType");

		Cluster cluster = new Cluster();
		cluster.setName(name);
		if (serverType.equals("mix")) {
			cluster.setServerType("混合");
		} else {
			cluster.setServerType(serverType);
		}
		cluster.setCreatetime(new Timestamp(new Date().getTime()));
		clusterDao.save(cluster);

		return clusterList();
	}

	public String readyAdd() {
		ClusterDao dao = new ClusterDao();
		List list = dao.loadAll();
	//	Cluster cluster=(Cluster)list.get(1);
		request.setAttribute("clusterList", list);
		return "/config/vpntelnet/machineUpAndDown/add.jsp";
	}

	private String list() {
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		List list = dao.loadAll();
		dao.close();
		request.setAttribute("machineList", list);
		return "/config/vpntelnet/machineUpAndDown/list.jsp";
	}

	private String clusterList() {
		ClusterDao dao = new ClusterDao();

		List list = dao.loadAll();
		UpAndDownMachineDao machineDao=new UpAndDownMachineDao();
		HashMap<Integer, Integer> map=machineDao.countById();
		request.setAttribute("totalMap", map);
		request.setAttribute("clusterList", list);
		
		return "/config/vpntelnet/machineUpAndDown/cluster.jsp";
	}

	// 创建服务器名称
	private String ready_addCluster() {
		return "/config/vpntelnet/machineUpAndDown/addCluster.jsp";
	}

	// 修改所属服务器组
	private String ready_editName() {
		int id = getParaIntValue("id");
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		Cluster cluster = dao.findDataById(id);
		ClusterDao clusterDao = new ClusterDao();
		List list = clusterDao.loadAll();
		request.setAttribute("id", id);
		request.setAttribute("cluster", cluster);
		request.setAttribute("clusterList", list);
		return "/config/vpntelnet/machineUpAndDown/editClusterItem.jsp";
	}

	// 服务器组的详细列表
	private String clusterDetailList() {
		UpAndDownMachineDao dao = new UpAndDownMachineDao();
		int id = getParaIntValue("id");
		List list = dao.loadClusterList(id);
		request.setAttribute("machineList", list);
		request.setAttribute("clusterId", id);
		return "/config/vpntelnet/machineUpAndDown/clusterList.jsp";
	}

	// 删除服务器组及详细列表
	private String deleteCluster() {
		String[] ids = getParaArrayValue("checkbox");
		if (ids != null && ids.length > 0) {
			ClusterDao clusterDao = new ClusterDao();
			try {
				clusterDao.delete(ids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				clusterDao.close();
			}
			// 修改服务器组
			UpAndDownMachineDao dao = new UpAndDownMachineDao();
			try {
				dao.updateClusterId(ids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}

		}
		return clusterList();
	}
	//修改服务器组
	public String editCluster(){
		int id = this.getParaIntValue("id");
		ClusterDao dao = new ClusterDao();
		Cluster cluster = (Cluster) dao.findByID(id + "");
		dao.close();
		request.setAttribute("cluster", cluster);
		return "/config/vpntelnet/machineUpAndDown/editCluster.jsp";
	}
	//保存修改服务器组信息
	public String saveCluster(){

		ClusterDao clusterDao = new ClusterDao();
		int id = getParaIntValue("id");
		String name = getParaValue("name");
		String serverType = this.getParaValue("serverType");

		Cluster cluster = new Cluster();
		cluster.setId(id);
		cluster.setName(name);
		if (serverType.equals("mix")) {
			cluster.setServerType("混合");
		} else {
			cluster.setServerType(serverType);
		}
		cluster.setCreatetime(new Timestamp(new Date().getTime()));
		clusterDao.update(cluster);

		return clusterList();
	
	}
}