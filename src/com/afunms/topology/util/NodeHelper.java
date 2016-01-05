/**
 * <p>Description:node helper</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-25
 */

package com.afunms.topology.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.dao.PanelModelDao;
import com.afunms.config.model.IpaddressPanel;
import com.afunms.config.model.PanelModel;
import com.afunms.indicators.util.Constant;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.NodeCategory;
import com.afunms.polling.node.Host;

public class NodeHelper {
	private static HashMap categoryMap;

	private static HashMap categoryNameMap;

	private static List categoryList;
	static {
	    categoryNameMap = new HashMap();
		categoryMap = new HashMap();
		categoryList = new ArrayList();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(ResourceCenter.getInstance()
					.getSysPath()
					+ "WEB-INF/classes/node-category.xml"));
			List list = doc.getRootElement().getChildren("category");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element ele = (Element) it.next();
				NodeCategory category = new NodeCategory();
				String id = ele.getAttributeValue("id");
				category.setId(Integer.parseInt(id));
				category.setCnName(ele.getChildText("cn_name"));
				category.setEnName(ele.getChildText("en_name"));
				category.setTopoImage("image/topo/"
						+ ele.getChildText("topo_image"));
				category.setAlarmImage("image/topo/"
						+ ele.getChildText("alarm_image"));
				category.setLostImage("image/topo/"
						+ ele.getChildText("lost_image"));
				categoryMap.put(id, category);
				categoryNameMap.put(category.getEnName(), category);
				categoryList.add(category);
			}
		} catch (Exception e) {
			SysLogger.error("NodeHelper.static", e);
		}
	}

	private static NodeCategory getCategory(int id) {
		if (categoryMap.get(String.valueOf(id)) != null)
			return (NodeCategory) categoryMap.get(String.valueOf(id));
		else {
			System.out.println("category is not exist,id=" + id);
			return (NodeCategory) categoryMap.get("1000");
		}
	}

	private static NodeCategory getCategory(String category) {
        if (categoryNameMap.get(category) != null)
            return (NodeCategory) categoryNameMap.get(category);
        else {
            System.out.println("category is not exist,categoryName=" + category);
            return (NodeCategory) categoryNameMap.get("1000");
        }
    }

	public static List getAllCategorys() {
		return categoryList;
	}

	/**
	 * 节点类别(中文描述)
	 */
	public static String getNodeCategory(int category) {
		return getCategory(category).getCnName();
	}

	/**
     * 节点类别(中文描述)
     */
    public static String getNodeCategory(String category) {
        return getCategory(category).getCnName();
    }

    /**
	 * 节点类别(英文描述)
	 */
	public static String getNodeEnCategory(int category) {
		return getCategory(category).getEnName();
	}

	/**
	 * 节点在拓扑图上的图标
	 */
	public static String getTopoImage(int category) {
		return getCategory(category).getTopoImage();
	}

	/**
	 * 节点在拓扑图上的报警时的图标
	 */
	public static String getAlarmImage(int category) {
		return getCategory(category).getAlarmImage();
	}

	/**
     * 节点在拓扑图上的图标
     */
    public static String getTopoImage(String category) {
        return getCategory(category).getTopoImage();
    }

    /**
     * 节点在拓扑图上的报警时的图标
     */
    public static String getAlarmImage(String category) {
        return getCategory(category).getAlarmImage();
    }
	/**
	 * 节点在拓扑图上的报警时的图标
	 */
	public static String getLostImage(int category) {
		return getCategory(category).getLostImage();
	}

	/**
     * 节点在拓扑图上的报警时的图标
     */
    public static String getLostImage(String category) {
        return getCategory(category).getLostImage();
    }
	/**
	 * 节点状态描述
	 */
	public static String getStatusDescr(int status) {
		String descr = null;
		if (status == 0)
			descr = "正常";
		else if (status == 1)
			descr = "普通告警";
		else if (status == 2)
			descr = "严重告警";
		else if (status == 3)
			descr = "紧急告警";
		else
			descr = "不被管理";
		return descr;
	}

	/**
	 * 节点状态标志
	 */
	public static String getStatusImage(int status) {
		String image = null;
		if (status == 0)
			//image = "status_ok.gif";
			image = "alert_blue.gif";
		else if (status == 1)
			//image = "status_ok.gif";
			image = "alarm_level_1.gif";
		else if (status == 2)
			//image = "status_busy.gif";
			image = "alarm_level_2.gif";
		else if (status == 3)
			// image = "status_down.gif";
			image = "alert.gif";
		else
			image = "unmanaged.gif";
		return "image/topo/" + image;
	}
	
	public static String getTypeImage(String category) {
		//SysLogger.info("category===="+category);
		String image = "";
		if ("net_switch".equals(category))
			image = "switch.gif";
		else if ("middleware".equals(category))
			image = "mid.gif";
		else if ("net_server".equals(category))
			image = "host.gif";
		else if ("safeequip".equals(category))
			image = "safe.gif";
		else if ("net_firewall".equals(category))
			image = "firewall.gif";
		else if ("services".equals(category))
			image = "service.gif";
		else if ("bussiness".equals(category))
			image = "bus.gif";
		else if ("interface".equals(category))
			image = "int.gif";
		else if ("storage".equals(category))
			image = "store.gif";
		else if ("ups".equals(category))
			image = "ups.gif";
		else if ("net_router".equals(category))
			image = "route.gif";
		else if ("dbs".equals(category))
			image = "dbs.gif";
		else if ("tomcat".equals(category))
			image = "tomcat.gif";
		else if ("weblogic".equals(category))
			image = "weblogic.gif";
		else if ("mail".equals(category))
			image = "email.gif";
		else if ("ftp".equals(category))
			image = "ftp.gif";
		else if ("wes".equals(category))
			image = "web.gif";
		else if ("socket".equals(category))
			image = "port.gif";
		else if ("domino".equals(category))
			image = "domino.gif";
		else if ("cics".equals(category))
			image = "cics.gif";
		else if ("dns".equals(category))
			image = "dns.gif";
		else if ("apache".equals(category))
			image = "apache.gif";
		else if ("jboss".equals(category))
			image = "jboss.gif";
		else if ("iis".equals(category))
			image = "iis.gif";
		else if ("mqs".equals(category))
			image = "mq_js.gif";
		else if ("was".equals(category))
			image = "was.gif";
		else if ("as400".equals(category))
			image = "a_as400.gif";
		else if ("net_atm".equals(category))
			image = "atm.gif";
		else if (category.equals("1.3.6.1.4.1.311.1.1.3.1.1"))// win_xp
			image = "a_windows.gif";
		else if (category.equals("1.3.6.1.4.1.311.1.1.3.1.2")
				|| category.equals("1.3.6.1.4.1.311.1.1.3.1.3"))// win_2000
			image = "a_windows.gif";
		else if (category.equals("1.3.6.1.4.1.311.1.1.3.1"))// win_nt
			image = "a_windows.gif";
		else if (category.equals("1.3.6.1.4.1.311.1.1.3"))// win_nt
			image = "a_windows.gif";
		else if (category.equals("1.3.6.1.4.1.2021.250.10")
				|| category.equals("1.3.6.1.4.1.8072.3.2.10"))// linux
			image = "a_linux.gif";
		else if (category.startsWith("1.3.6.1.4.1.42.")) // sun_solaris
			image = "a_sun.gif";
		else if (category.startsWith("1.3.6.1.4.1.2.")) // ibm_aix
			image = "a_aix.gif";
		else if (category.startsWith("1.3.6.1.4.1.11.")) // hp_ux
			image = "a_hp.gif";
		else if (category.startsWith("1.3.6.1.4.1.9.")) // cisco
			image = "a_cisco.gif";
		else if (category.startsWith("1.3.6.1.4.1.25506")) // huawei
			image = "a_h3c.gif";
		else if (category.startsWith("1.3.6.1.4.1.2011")) // huawei
			image = "a_h3c.gif";
		else if (category.startsWith("1.3.6.1.4.1.4881.")) // 锐捷
			image = "a_redg.gif";
		else if (category.startsWith("1.3.6.1.4.1.5651.")) // 迈普
			image = "a_maip.gif";
		else if (category.startsWith("1.3.6.1.4.1.171.")) // DLink
			image = "a_dlink.gif";
		else if (category.startsWith("1.3.6.1.4.1.2272.")) // 北电
			image = "a_northtel.gif";
		else if (category.startsWith("1.3.6.1.4.1.89.")) // Radware
			image = "a_rad.gif";
		else if (category.startsWith("1.3.6.1.4.1.3902.")) // 中兴
			image = "a_zte.gif";
		else if (category.startsWith("1.3.6.1.4.1.3320")) // bdcom
			image = "a_bdcom.gif";
		else if (category.equals("4"))
			image = "host.gif";
		else if (category.equals("2")||category.equals("3")||category.equals("7"))
			image = "switch.gif";
		else if (category.equals("1"))
			image = "route.gif";
		else if (category.equals("9"))
			image = "atm.gif";
		else if (category.equals("52")) // mysql
			image = "mysql.gif";
		else if (category.equals("53")) // Oracle
			image = "oracle.gif";
		else if (category.equals("54")) // sql-server
			image = "sqlserver.gif";
		else if (category.equals("55")) // Sybase
			image = "sybase.gif";
		else if (category.equals("59")) // db2
			image = "db2.gif";
		else if (category.equals("60")) // Informix
			image = "informix.gif";
		else if (category.equals("ggsn")) // ggsn
			image = "GGSN.JPG";
		else if (category.equals("sgsn")) // sgsn
			image = "SGSN.JPG";
		else if (category.equals("91")) // 
			image = "GGSN.JPG";
		else if (category.equals("92")) // 
			image = "SGSN.JPG";
		else
			image = "unmanaged.gif";
		return "dtree/img/" + image;
	}
	
	
	public static String getSubTypeImage(String subtype) {
		SysLogger.info("subtype===="+subtype);
		String image = "";
		if (subtype.equals(Constant.TYPE_HOST_SUBTYPE_WINDOWS))// win_xp
			image = "a_windows.gif";
		else if (subtype.equals(Constant.TYPE_HOST_SUBTYPE_LINUX))// linux
			image = "a_linux.gif";
		else if (subtype.startsWith(Constant.TYPE_HOST_SUBTYPE_SOLARIS)) // sun_solaris
			image = "a_sun.gif";
		else if (subtype.startsWith(Constant.TYPE_HOST_SUBTYPE_AIX)) // ibm_aix
			image = "a_aix.gif";
		else if (subtype.startsWith(Constant.TYPE_HOST_SUBTYPE_HPUNIX)) // hp_ux
			image = "a_hp.gif";
		else if (subtype.startsWith(Constant.TYPE_HOST_SUBTYPE_TRU64)) // tru64
			image = "a_hp.gif";
		else if (subtype.startsWith(Constant.TYPE_NET_SUBTYPE_CISCO))// cisco
			image = "a_cisco.gif";
		else if (subtype.startsWith(Constant.TYPE_NET_SUBTYPE_H3C)) // huawei
			image = "a_h3c.gif";
		else if (subtype.startsWith(Constant.TYPE_NET_SUBTYPE_ZTE)) // huawei
			image = "a_zte.gif";
		else if (subtype.startsWith(Constant.TYPE_NET_SUBTYPE_DLINK))//d_link
			image = "a_dlink.gif";
		else if (subtype.startsWith(Constant.TYPE_NET_SUBTYPE_BDCOM)) // bdcom
			image = "a_bdcom.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_MYSQL)) // mysql
			image = "mysql.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_ORACLE)) // Oracle
			image = "oracle.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_SQLSERVER)) // sql-server
			image = "sqlserver.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_SYBASE)) // Sybase
			image = "sybase.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_DB2)) // db2
			image = "db2.gif";
		else if (subtype.equals(Constant.TYPE_DB_SUBTYPE_INFORMIX)) // Informix
			image = "informix.gif";
		else if (subtype.equals("ggsn")) // ggsn
			image = "GGSN.JPG";
		else if (subtype.equals("sgsn")) // sgsn
			image = "SGSN.JPG";
		else if (subtype.equals("91")) // 
			image = "GGSN.JPG";
		else if (subtype.equals("92")) // 
			image = "SGSN.JPG";
		else if ("as400".equals(subtype))
			image = "a_as400.gif";
		else if ("net_atm".equals(Constant.TYPE_NET_SUBTYPE_ATM))
			image = "atm.gif";
		else if ("atm".equals(Constant.TYPE_NET_SUBTYPE_ATM))
			image = "atm.gif";
		else
			image = "unmanaged.gif";
		return "dtree/img/" + image;
	}
	
	/**
	 * 节点状态标志
	 */
	public static String getCurrentStatusImage(int status) {
		String image = null;
		if (status == 0)
			image = "status_ok.gif";
		else if (status == 1)
			image = "alarm_level_1.gif";
		else if (status == 2)
			image = "alarm_level_2.gif";
		else if (status == 3)
			 image = "alert.gif";
		else
			image = "unmanaged.gif";
		return "image/topo/" + image;
	}

	/**
	 * 根据sysOid得到服务器在拓扑图上的图标
	 */
	public static String getServerTopoImage(String sysOid) {
		String fileName = null;
		if (sysOid == null) {
			fileName = "server.gif";
			return "image/topo/" + fileName;
		}
		if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.1"))// win_xp
			fileName = "win_xp.gif";
		else if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.2")
				|| sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.3"))// win_2000
			fileName = "win_2000.gif";
		else if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1"))// win_nt
			fileName = "win_nt.gif";
		else if (sysOid.equals("1.3.6.1.4.1.2021.250.10")
				|| sysOid.equals("1.3.6.1.4.1.8072.3.2.10"))// linux
			fileName = "linux.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.42.")) // sun_solaris
			fileName = "solaris.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.2.")) // ibm_aix
			fileName = "ibm.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.11.")) // hp_ux
			fileName = "hp.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.36.")) // compaq
			fileName = "compaq.gif";
		else
			fileName = "server.gif";
		return "image/topo/" + fileName;
	}

	/**
	 * 根据sysOid得到服务器在拓扑图上的报警时图标
	 */
	public static String getServerAlarmImage(String sysOid) {
		String fileName = null;
		if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.1"))// win_xp
			fileName = "win_xp_alarm.gif";
		else if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.2")
				|| sysOid.equals("1.3.6.1.4.1.311.1.1.3.1.3"))// win_2000
			fileName = "win_2000_alarm.gif";
		else if (sysOid.equals("1.3.6.1.4.1.311.1.1.3.1"))// win_nt
			fileName = "win_nt_alarm.gif";
		else if (sysOid.equals("1.3.6.1.4.1.2021.250.10")
				|| sysOid.equals("1.3.6.1.4.1.8072.3.2.10"))// linux
			fileName = "linux_alarm.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.42.")) // sun_solaris
			fileName = "solaris_alarm.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.2.")) // ibm_aix
			fileName = "ibm_alarm.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.11.")) // hp_ux
			fileName = "hp_alarm.gif";
		else if (sysOid.startsWith("1.3.6.1.4.1.36.")) // compaq
			fileName = "compaq_alarm.gif";
		else
			fileName = "server_alarm.gif";
		return "image/topo/" + fileName;
	}

	public static String getHostOS(String sysOid) {
		String os = null;
		if (sysOid.startsWith("1.3.6.1.4.1.311.")) // win
			os = "windows";
		else if (sysOid.equals("1.3.6.1.4.1.2021.250.10")
				|| sysOid.equals("1.3.6.1.4.1.8072.3.2.10"))// linux
			os = "linux";
		else if (sysOid.startsWith("1.3.6.1.4.1.42.")) // sun_solaris
			os = "solaris";
		else if (sysOid.startsWith("1.3.6.1.4.1.2.")) // ibm_aix
			os = "aix";
		else if (sysOid.startsWith("1.3.6.1.4.1.36.")) // compaq
			os = "tru64";
		else if (sysOid.startsWith("1.3.6.1.4.1.9.")) // cisco
			os = "cisco";
		else
			os = "";
		return os;
	}

	/**
	 * 系统快照状态标志
	 */
	public static String getSnapStatusImage(int status) {
		String image = null;
		if (status == 1)
			image = "status5.png";
		else if (status == 2)
			image = "status2.png";
		else if (status == 3)
			image = "status1.png";
		return "image/topo/" + image;
	}

	/**
	 * 系统快照状态标志
	 */
	public static String getSnapStatusImage(int status, int category) {
		String image = null;
		if (category == 1) {
			// 路由器
			if (status == 1)
				image = "router-alarm-1.gif";
			else if (status == 2)
				image = "router-alarm-1.gif";
			else if (status == 3)
				image = "router-alarm.gif";
			else 
				image = "Drouter-B-32.gif";

		} else if (category == 2) {
			// 交换机
			if (status == 1)
				image = "switch-alarm-1.gif";
			else if (status == 2)
				image = "switch-alarm-1.gif";
			else if (status == 3)
				image = "switch-alarm.gif";
			else 
				image = "Switch-B-32.gif";
		} else if (category == 3) {
			// 服务器
			if (status == 1)
				image = "server-alarm-1.gif";
			else if (status == 2)
				image = "server-alarm-1.gif";
			else if (status == 3)
				image = "server-alarm.gif";
			else 
				image = "server-B-24.gif";
		} else if (category == 4) {
			// 数据库
			if (status == 1)
				image = "db-24-alarm-1.gif";
			else if (status == 2)
				image = "db-24-alarm-1.gif";
			else if (status == 3)
				image = "db-24-alarm.gif";
			else 
				image = "db-24.gif";
		} else if (category == 5) {
			// 中间件
			if (status == 1)
				image = "middleware3_alarm_1.gif";
			else if (status == 2)
				image = "middleware3_alarm_1.gif";
			else if (status == 3)
				image = "middleware3_alarm.gif";
			else 
				image = "middleware3.gif";
		} else if (category == 6) {
			// 服务
			if (status == 1)
				image = "service_alarm_1.gif";
			else if (status == 2)
				image = "service_alarm_1.gif";
			else if (status == 3)
				image = "service_alarm.gif";
			else 
				image="service.gif";
		} else if (category == 7) {
			// 防火墙
			if (status == 1)
				image = "firewall/firewall-alarm-1.gif";
			else if (status == 2)
				image = "firewall/firewall-alarm-1.gif";
			else if (status == 3)
				image = "firewall/firewall-alarm.gif";
			else 
				image="firewall/firewall.gif";
		} else if (category == 8) {
			// 数据库
			if (status == 2)
				image = "add-services-alarm.gif";
			else
				image = "add-services.gif";
		}
		return "image/topo/" + image;
	}

	/**
	 * 报警级别标志
	 */
	public static String getAlarmLevelImage(int level) {
		String image = null;
		if (level == 1)
			image = "alarm_level_1.gif";
		else if (level == 2)
			image = "alarm_level_2.gif";
		else if (level == 3)
			image = "alarm_level_3.gif";
		return "image/topo/" + image;
	}

	/**
	 * 报警级别描述
	 */
	public static String getAlarmLevelDescr(int level) {
		String descr = null;
		if (level == 1)
			descr = "注意";
		else if (level == 2)
			descr = "故障";
		else if (level == 3)
			descr = "严重";
		return descr;
	}

	// 示意设备右键菜单
	public static String getMenuItem(String nodeId) {
		String menuItem = "<a class=\"deleteline_menu_out\" onmouseover=\"deleteMenuOver();\" onmouseout=\"deleteMenuOut();\" "
				+ "onclick=\"deleteHintMeta('"
				+ nodeId
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;删除设备</a><br/>"
				+

				// "<a class=\"property_menu_out\" "
				// +"onmouseover=\"propertyMenuOver();\"
				// onmouseout=\"propertyMenuOut();\" " +
				// "onclick=\"javascript:window.showModalDialog('/afunms/submap.do?action=hintProperty&nodeId="+nodeId+"',"
				// +
				// " window, 'dialogwidth:500px; dialogheight:300px; status:no;
				// help:no;resizable:0');\"" +">&nbsp;&nbsp;&nbsp;&nbsp;图元属性
				// </a><br/>"+

				"<a class=\"relationmap_menu_out\" onmouseover=\"relationMapMenuOver();\" onmouseout=\"relationMapMenuOut();\" "
				+ "onclick=\"javascript:window.showModalDialog('/afunms/submap.do?action=relationList&nodeId="
				+ nodeId
				+ "',"
				+ " window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;关联拓扑图 </a><br/>";
		return menuItem;

	}

	//网络实体设备右键菜单 yangjun add
	public static String getMenu(String nodeId, String ip, String category) {
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeId.substring(3)));
		String sysoid="";
		String width="";
		int height=0;
//		if(host!=null){
//			sysoid=host.getSysOid();
//			PanelModelDao panelModelDao = new PanelModelDao();
//			PanelModel panelModel = (PanelModel)panelModelDao.loadPanelModel(sysoid);
//			if(panelModel!=null){
//				width=panelModel.getWidth();
//				height=Integer.parseInt(panelModel.getHeight());
//			}
//	    }
		if(host!=null){
			sysoid=host.getSysOid();
			IpaddressPanelDao ipaddressPanelDao = new IpaddressPanelDao();
			IpaddressPanel ipaddressPanel = null;
			try {
				ipaddressPanel = ipaddressPanelDao.loadIpaddressPanel(ip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ipaddressPanelDao.close();
			}
			if(ipaddressPanel!=null){
			PanelModelDao panelModelDao = new PanelModelDao();
			PanelModel panelModel = null;
			try {
				panelModel = (PanelModel)panelModelDao.loadPanelModel(sysoid,ipaddressPanel.getImageType());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				panelModelDao.close();
			}
			if(panelModel!=null){
				width=panelModel.getWidth();
				height=Integer.parseInt(panelModel.getHeight());
				}
			}
		}

		String menuItem = "<a class=\"ping_menu_out\" onmouseover=\"pingMenuOver();\" onmouseout=\"pingMenuOut();\""
				+ " onclick=\"javascript:resetProcDlg();window.showModelessDialog('/afunms/tool/ping.jsp?ipaddress="
				+ ip
				+ "',"
				+ " window, 'dialogHeight:500px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');"
				+ "timingCloseProDlg(8000);\" title=\"ping "
				+ ip
				+ "\">&nbsp;&nbsp;&nbsp;&nbsp;Ping </a><br/>"
				+

//				"<a class=\"telnet_menu_out\" onmouseover=\"telnetMenuOver();\" onmouseout=\"telnetMenuOut();\" "
//				+ "onclick=\"javascript:window.open('/afunms/network.do?action=telnet&&ipaddress="
//				+ ip
//				+ "','window', "
//				+ "'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;Telnet</a><br/>"
//				+
//				
//				
//				
//				"<a class=\"trace_menu_out\" onmouseover=\"traceMenuOver();\" onmouseout=\"traceMenuOut();\" "
//				+ "onclick=\"javascript:window.open('/afunms/tool/tracerouter.jsp?ipaddress="
//				+ ip
//				+ "','window', "
//				+ "'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;Traceroute</a><br/>"
//				+

//				"<a class=\"detail_menu_out\" onmouseover=\"detailMenuOver();\" onmouseout=\"detailMenuOut();\" "
//				+ "onclick=\"showalert('"
//				+ nodeId
//				+ "')"
//				+ "\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;查看信息 </a><br/>"
//				+
				
				"<a class=\"sbmb_menu_out\" onmouseover=\"sbmbMenuOver();\" onmouseout=\"sbmbMenuOut();\" "
				+ "onclick=\"showpanel('"
				+ ip+"','"+width+"','"+height
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;设备面板 </a><br/>"
				+
				
				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成别名拓扑图 </a><br/>"
				+

				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成IP拓扑图 </a><br/>"
				+

				"<a class=\"relationmap_menu_out\" onmouseover=\"relationMapMenuOver();\" onmouseout=\"relationMapMenuOut();\" "
				+ "onclick=\"javascript:window.showModalDialog('/afunms/submap.do?action=relationList&nodeId="
				+ nodeId
				+ "&category=" 
				+ category
				+"',"
				+ " window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;关联拓扑图 </a><br/>"
				+

				"<a class=\"property_menu_out\" onmouseover=\"propertyMenuOver();\" onmouseout=\"propertyMenuOut();\" "
				+ "onclick=\"javascript:window.showModalDialog('/afunms/submap.do?action=equipProperty&type="
				+ category
				+ "&nodeId="
				+ nodeId
				+ "',"
				+ " window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;图元属性 </a><br/>"
				+

				"<a class=\"equipRelatedApplications_menu_out\" onmouseover=\"deleteEquipRelatedApplicationsMenuOver();\" onmouseout=\"deleteEquipRelatedApplicationsMenuOut();\" "
				+ "onclick=\"addApplication('"
				+ nodeId
				+ "','"
				+ ip
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;设备相关应用</a><br/>"+
				
//				"<a class=\"deleteEquip_menu_out\" onmouseover=\"deleteEquipMenuOver();\" onmouseout=\"deleteEquipMenuOut();\" "
//				+ "onclick=\"javascript:window.showModalDialog('/afunms/nodedepend.do?action=dependpanel&nodeId="
//				+ nodeId
//				+ "&ip=" 
//				+ ip
//				+ "&category=" 
//				+ category
//				+ "',"
//				+ " window, 'dialogwidth:520px; dialogheight:500px; status:no; help:no;resizable:0');\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;设备依赖关系</a><br/>"+
				
//				"<a class=\"deleteEquip_menu_out\" onmouseover=\"deleteEquipMenuOver();\" onmouseout=\"deleteEquipMenuOut();\" "
//				+ "onclick=\"deleteEquip('"
//				+ nodeId
//				+ "','"
//				+ category
//				+ "')"
//				+ "\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;系统删除设备</a><br/>"
//				
//				+ 
				"<a class=\"deleteEquip_menu_out\" onmouseover=\"deleteEquipMenuOver();\" onmouseout=\"deleteEquipMenuOut();\" "
				+ "onclick=\"removeEquip('"
				+ nodeId
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;拓扑图删除设备</a><br/>";

		return menuItem;
	}
    //主机实体设备右键菜单 yangjun add
	public static String getHostMenu(String nodeId, String ip, String category) {
		String menuItem = "<a class=\"ping_menu_out\" onmouseover=\"pingMenuOver();\" onmouseout=\"pingMenuOut();\""
				+ " onclick=\"javascript:resetProcDlg();window.showModelessDialog('/afunms/tool/ping.jsp?ipaddress="
				+ ip
				+ "',"
				+ " window, 'dialogHeight:500px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');"
				+ "timingCloseProDlg(8000);\" title=\"ping "
				+ ip
				+ "\">&nbsp;&nbsp;&nbsp;&nbsp;Ping </a><br/>"
				+

				
				//<img src="/afunms/resource/image/toolbar/telnet.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("/afunms/network.do?action=telnet&ipaddress=172.25.25.254","onetelnet", "height=0, width= 0, top=0, left= 0")'>Telnet</a>
				
			
				
				"<a class=\"trace_menu_out\" onmouseover=\"traceMenuOver();\" onmouseout=\"traceMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/tool/tracerouter.jsp?ipaddress="
				+ ip
				+ "','window', "
				+ "'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;Traceroute</a><br/>"
				+

				"<a class=\"detail_menu_out\" onmouseover=\"detailMenuOver();\" onmouseout=\"detailMenuOut();\" "
				+ "onclick=\"showalert('"
				+ nodeId
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;查看信息 </a><br/>"
				+

				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成别名拓扑图 </a><br/>"
				+

				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成IP拓扑图 </a><br/>";

		return menuItem;
	}
	public static String getMenuItem(String nodeId, String ip) {
		String menuItem = "<a class=\"ping_menu_out\" onmouseover=\"pingMenuOver();\" onmouseout=\"pingMenuOut();\""
				+ " onclick=\"javascript:resetProcDlg();window.showModelessDialog('/afunms/tool/ping.jsp?ipaddress="
				+ ip
				+ "',"
				+ " window, 'dialogHeight:500px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');"
				+ "timingCloseProDlg(8000);\" title=\"ping "
				+ ip
				+ "\">&nbsp;&nbsp;&nbsp;&nbsp;Ping </a><br/>"
				+

				
				
				"<a class=\"trace_menu_out\" onmouseover=\"traceMenuOver();\" onmouseout=\"traceMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/tool/tracerouter.jsp?ipaddress="
				+ ip
				+ "','window', "
				+ "'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;Traceroute</a><br/>"
				+
				/*
				 * "<a class=\"detail_menu_out\"
				 * onmouseover=\"detailMenuOver();\"
				 * onmouseout=\"detailMenuOut();\" " +
				 * "onclick=\"javascript:window.open('/afunms/detail/dispatcher.jsp?id=" +
				 * nodeId + "','window', " +
				 * "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\"" +
				 * ">&nbsp;&nbsp;&nbsp;&nbsp;查看信息 </a><br/>"+
				 */
				"<a class=\"detail_menu_out\" onmouseover=\"detailMenuOver();\" onmouseout=\"detailMenuOut();\" "
				+ "onclick=\"showalert('"
				+ nodeId
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;查看信息 </a><br/>"
				+

				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成别名拓扑图 </a><br/>"
				+

				"<a class=\"download_menu_out\" onmouseover=\"downloadMenuOver();\" onmouseout=\"downloadMenuOut();\" "
				+ "onclick=\"javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', "
				+ "'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;生成IP拓扑图 </a><br/>";

		return menuItem;
	}

	// 实体链路右键菜单
	public static String getMenuItem(String lineId, String startId, String endId) {
		String menuItem =

//		"<a class=\"detail_menu_out\" onmouseover=\"detailMenuOver();\" onmouseout=\"detailMenuOut();\" "
//				+ "onclick=\"showLineInfo("
//				+ lineId
//				+ ")\""
//				+ ">&nbsp;&nbsp;&nbsp;&nbsp;查看信息 </a><br/>"
//				+

				"<a class=\"property_menu_out\" onmouseover=\"propertyMenuOver();\" onmouseout=\"propertyMenuOut();\" "
				+ "onclick=\"javascript:window.showModalDialog('/afunms/submap.do?action=linkProperty&lineId="
				+ lineId
				+ "',"
				+ " window, 'dialogwidth:350px; dialogheight:250px; status:no; help:no;resizable:0');\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;链路属性 </a><br/>"
				+

				"<a class=\"deleteline_menu_out\" onmouseover=\"deleteLineMenuOver();\" onmouseout=\"deleteLineMenuOut();\" "
				+ "onclick=\"deleteLink("
				+ lineId
				+ ")"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;删除链路</a><br/>"
				+

				"<a class=\"editline_menu_out\" onmouseover=\"editLineMenuOver();\" onmouseout=\"editLineMenuOut();\" "
				+ "onclick=\"editLink("
				+ lineId
				+ ")\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;修改链路 </a><br/>";

		return menuItem;
	}

	// 示意链路右键菜单
	public static String getMenuLine(int id,String lineId) {
		String menuItem =

		"<a class=\"property_menu_out\" onmouseover=\"propertyMenuOver();\" onmouseout=\"propertyMenuOut();\" "
				+ "onclick=\"javascript:window.showModalDialog('/afunms/link.do?action=readyEditLine&id="
				+ id
				+ "',"
				+ " window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;链路属性 </a><br/>"
				+

				"<a class=\"deleteline_menu_out\" onmouseover=\"deleteLineMenuOver();\" onmouseout=\"deleteLineMenuOut();\" "
				+ "onclick=\"deleteLine('"
				+ lineId
				+ "')"
				+ "\""
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;删除链路</a><br/>"
		// +

		// "<a class=\"editline_menu_out\" onmouseover=\"editLineMenuOver();\"
		// onmouseout=\"editLineMenuOut();\" " +
		// "onclick=\"editLink("+lineId+","+startId+","+endId+")\"" +
		// ">&nbsp;&nbsp;&nbsp;&nbsp;修改链路 </a><br/>"
		;

		return menuItem;
	}
}