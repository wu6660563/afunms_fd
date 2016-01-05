/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.capreport.manage;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.manage.IISManager;
import com.afunms.application.manage.TomcatManager;
import com.afunms.application.manage.WeblogicManager;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DateE;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.system.model.User;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * ����һ���ڵ㣬Ҫ��server.jsp����һ���ڵ�;
 * ɾ�������һ���ڵ���Ϣ��������ֱ�Ӳ���server.jsp,����������updateXml������xml.
 */
public class MidCapreportManager extends BaseManager implements ManagerInterface {
	DateE datemanager = new DateE();

	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");

	I_HostCollectData hostmanager = new HostCollectDataManager();

	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String midlist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		String bid[] = bids.split(",");
		Vector rbids = new Vector();
		if (bid != null && bid.length > 0) {
			for (int i = 0; i < bid.length; i++) {
				if (bid[i] != null && bid[i].trim().length() > 0)
					rbids.add(bid[i].trim());
			}
		}

		WeblogicConfigDao configdao = new WeblogicConfigDao();
		List weblogiclist = null;
		try {
			if (operator.getRole() == 0) {
				weblogiclist = configdao.loadAll();
			} else
				weblogiclist = configdao.getWeblogicByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		if (weblogiclist == null)
			weblogiclist = new ArrayList();
		request.setAttribute("weblogiclist", weblogiclist);

		List tomcatlist = null;
		TomcatDao dao = new TomcatDao();
		try {
			if (operator.getRole() == 0) {
				tomcatlist = dao.loadAll();
			} else
				tomcatlist = dao.getTomcatByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		if (tomcatlist == null)
			tomcatlist = new ArrayList();
		request.setAttribute("tomcatlist", tomcatlist);

		List iislist = null;
		IISConfigDao iisdao = new IISConfigDao();
		try {
			if (operator.getRole() == 0) {
				iislist = iisdao.loadAll();
			} else
				iislist = iisdao.getIISByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iisdao.close();
		}
		if (iislist == null)
			iislist = new ArrayList();
		request.setAttribute("iislist", iislist);
		return "/capreport/tomcat/list.jsp";
	}

	/**
	 * @date 2011-4-19
	 * @author wxy add
	 * @�м����������ҵ���ѯ
	 * @return
	 */
	private String find() {

		int dbflag = 0;
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		String bidtexts = getParaValue("bidtext");
		dbflag = getParaIntValue("dbflag");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);

		User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		String bidd[] = bids.split(",");
		Vector temp = new Vector();
		Vector rbids = new Vector();
		if (bidd != null && bidd.length > 0) {
			for (int i = 0; i < bidd.length; i++) {
				if (bidd[i] != null && bidd[i].trim().length() > 0)
					temp.add(bidd[i].trim());
			}

		}

		if (bidtexts != null && !bidtexts.equals("")) {
			String bidtext[] = bidtexts.split(",");
			for (int i = 0; i < bidtext.length; i++) {
				if (temp.contains(bidtext[i])) {
					rbids.add(bidtext[i]);
				}
			}
		}
		List weblogiclist = null;
		List iislist = null;
		List tomcatlist = null;
		if (rbids != null && rbids.size() > 0) {

			WeblogicConfigDao configdao = new WeblogicConfigDao();

			try {

				weblogiclist = configdao.getWeblogicByBID(rbids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				configdao.close();
			}

			// tomcat
			TomcatDao dao = new TomcatDao();
			try {

				tomcatlist = dao.getTomcatByBID(rbids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}

			// IIS
			IISConfigDao iisdao = new IISConfigDao();
			try {

				iislist = iisdao.getIISByBID(rbids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				iisdao.close();
			}

		}
		if (weblogiclist == null)
			weblogiclist = new ArrayList();
		request.setAttribute("weblogiclist", weblogiclist);
		if (tomcatlist == null)
			tomcatlist = new ArrayList();
		request.setAttribute("tomcatlist", tomcatlist);
		if (iislist == null)
			iislist = new ArrayList();
		request.setAttribute("iislist", iislist);
		// ///
		if (dbflag == 1) {

			return "/capreport/tomcat/list.jsp";
		} else if (dbflag == 2) {
			if (tomcatlist == null)
				tomcatlist = new ArrayList();
			request.setAttribute("list", tomcatlist);
			return "/capreport/tomcat/tomcatlist.jsp";
		} else if (dbflag == 3) {
			if (iislist == null)
				iislist = new ArrayList();
			request.setAttribute("list", iislist);
			return "/capreport/tomcat/iislist.jsp";
		} else if (dbflag == 4) {
			if (weblogiclist == null)
				weblogiclist = new ArrayList();
			request.setAttribute("list", weblogiclist);
			return "/capreport/tomcat/weblogiclist.jsp";
		} else if (dbflag == 5) {
			return "/capreport/tomcat/eventlist.jsp";
		} else {
			return "/capreport/tomcat/list.jsp";
		}

	}

	private String eventlist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		String bid[] = bids.split(",");
		Vector rbids = new Vector();
		if (bid != null && bid.length > 0) {
			for (int i = 0; i < bid.length; i++) {
				if (bid[i] != null && bid[i].trim().length() > 0)
					rbids.add(bid[i].trim());
			}
		}

		WeblogicConfigDao configdao = new WeblogicConfigDao();
		List weblogiclist = null;
		try {
			if (operator.getRole() == 0) {
				weblogiclist = configdao.loadAll();
			} else
				weblogiclist = configdao.getWeblogicByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		if (weblogiclist == null)
			weblogiclist = new ArrayList();
		request.setAttribute("weblogiclist", weblogiclist);

		List tomcatlist = null;
		TomcatDao dao = new TomcatDao();
		try {
			if (operator.getRole() == 0) {
				tomcatlist = dao.loadAll();
			} else
				tomcatlist = dao.getTomcatByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		if (tomcatlist == null)
			tomcatlist = new ArrayList();
		request.setAttribute("tomcatlist", tomcatlist);

		List iislist = null;
		IISConfigDao iisdao = new IISConfigDao();
		try {
			if (operator.getRole() == 0) {
				iislist = iisdao.loadAll();
			} else
				iislist = iisdao.getIISByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iisdao.close();
		}
		if (iislist == null)
			iislist = new ArrayList();
		request.setAttribute("iislist", iislist);
		return "/capreport/tomcat/eventlist.jsp";
	}

	private String midping() {
		TomcatManager tomcatManager = new TomcatManager();
		IISManager iisManager = new IISManager();
		WeblogicManager weblogiManager = new WeblogicManager();
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("tomcatcheckbox");
		String[] idsiis = getParaArrayValue("iischeckbox");
		String[] idsweblogic = getParaArrayValue("weblogiccheckbox");

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		// User operator =
		// (User)session.getAttribute(SessionConstant.CURRENT_USER);
		/*
		 * String bids = operator.getBusinessids(); String bid[] =
		 * bids.split(","); Vector rbids = new Vector(); if(bid != null &&
		 * bid.length>0){ for(int i=0;i<bid.length;i++){ if(bid[i] != null &&
		 * bid[i].trim().length()>0) rbids.add(bid[i].trim()); } }
		 */
		// tomcat=====================================================
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {

				Node tomcatNode = PollingEngine.getInstance().getTomcatByID(Integer.parseInt(ids[i]));
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = tomcatManager.getCategory(tomcatNode.getIpAddress(), "TomcatPing", "ConnectUtilization",
						starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("tomcat", tomcatNode);
				ipmemhash.put("pinghash", pinghash);
				orderList.add(ipmemhash);
			}
		}
		// iis
		List orderListiis = new ArrayList();
		if (idsiis != null && idsiis.length > 0) {
			for (int i = 0; i < idsiis.length; i++) {

				Node iisNode = PollingEngine.getInstance().getIisByID(Integer.parseInt(idsiis[i]));
				Hashtable pinghashiis = new Hashtable();
				try {
					pinghashiis = iisManager.getCategory(iisNode.getIpAddress(), "IISPing", "ConnectUtilization",
						starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhashiis = new Hashtable();
				ipmemhashiis.put("iis", iisNode);
				ipmemhashiis.put("pinghashiis", pinghashiis);
				orderListiis.add(ipmemhashiis);
			}
		}
		// -----------------end------------iis
		// weblogic=============================================
		List orderListweblogic = new ArrayList();
		if (idsweblogic != null && idsweblogic.length > 0) {
			for (int i = 0; i < idsweblogic.length; i++) {

				Node weblogicNode = PollingEngine.getInstance().getWeblogicByID(Integer.parseInt(idsweblogic[i]));
				Hashtable pinghashweblogic = new Hashtable();
				try {
					pinghashweblogic = weblogiManager.getCategory(weblogicNode.getIpAddress(), "WeblogicPing",
						"ConnectUtilization", starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhashweblogic = new Hashtable();
				ipmemhashweblogic.put("weblogic", weblogicNode);
				ipmemhashweblogic.put("pinghashiis", pinghashweblogic);
				orderListweblogic.add(ipmemhashweblogic);
			}
		}
		// ===================end weblogic
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum")) {
			returnList = (List) session.getAttribute("pinglist");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					Node node = (Node) _pinghash.get("tomcat");
					// String osname = monitoriplist.getOssource().getOsname();
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************
		// iis================================
		List returnListiis = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum")) {
			returnListiis = (List) session.getAttribute("pinglist");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderListiis;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					Node node = (Node) _pinghash.get("iis");
					// String osname = monitoriplist.getOssource().getOsname();
					Hashtable pinghashiis = (Hashtable) _pinghash.get("pinghashiis");
					if (pinghashiis == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					if (pinghashiis.get("avgpingcon") != null)
						pingconavg = (String) pinghashiis.get("avgpingcon");
					if (pinghashiis.get("downnum") != null)
						downnum = (String) pinghashiis.get("downnum");
					List ipdiskListiis = new ArrayList();
					ipdiskListiis.add(ip);
					ipdiskListiis.add(equname);
					ipdiskListiis.add(node.getType());
					ipdiskListiis.add(pingconavg);
					ipdiskListiis.add(downnum);
					returnListiis.add(ipdiskListiis);
				}
			}
		}
		// iis---------------------end
		// weblogic================================
		List returnListweblogic = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum")) {
			returnListweblogic = (List) session.getAttribute("pinglist");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderListweblogic;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					Node node = (Node) _pinghash.get("weblogic");
					// String osname = monitoriplist.getOssource().getOsname();
					Hashtable pinghashiis = (Hashtable) _pinghash.get("pinghashiis");
					if (pinghashiis == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					if (pinghashiis.get("avgpingcon") != null)
						pingconavg = (String) pinghashiis.get("avgpingcon");
					if (pinghashiis.get("downnum") != null)
						downnum = (String) pinghashiis.get("downnum");
					List ipdiskListweblogic = new ArrayList();
					ipdiskListweblogic.add(ip);
					ipdiskListweblogic.add(equname);
					ipdiskListweblogic.add(node.getType());
					ipdiskListweblogic.add(pingconavg);
					ipdiskListweblogic.add(downnum);
					returnListweblogic.add(ipdiskListweblogic);
				}
			}
		}
		// weblogic---------------------end
		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {

					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping
								.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);

				ipdiskList = null;
			}
		}
		// iis-===================================
		List listiis = new ArrayList();
		if (returnListiis != null && returnListiis.size() > 0) {
			for (int m = 0; m < returnListiis.size(); m++) {
				List ipdiskList = (List) returnListiis.get(m);
				for (int n = m + 1; n < returnListiis.size(); n++) {

					List _ipdiskList = (List) returnListiis.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping
								.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnListiis.remove(m);
							returnListiis.add(m, _ipdiskList);
							returnListiis.remove(n);
							returnListiis.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnListiis.remove(m);
							returnListiis.add(m, _ipdiskList);
							returnListiis.remove(n);
							returnListiis.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				listiis.add(ipdiskList);
				ipdiskList = null;
			}
		}
		// weblogic===================================
		List listweblogic = new ArrayList();
		if (returnListweblogic != null && returnListweblogic.size() > 0) {
			for (int m = 0; m < returnListweblogic.size(); m++) {
				List ipdiskList = (List) returnListweblogic.get(m);
				for (int n = m + 1; n < returnListweblogic.size(); n++) {

					List _ipdiskList = (List) returnListweblogic.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping
								.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnListweblogic.remove(m);
							returnListweblogic.add(m, _ipdiskList);
							returnListweblogic.remove(n);
							returnListweblogic.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnListweblogic.remove(m);
							returnListweblogic.add(m, _ipdiskList);
							returnListweblogic.remove(n);
							returnListweblogic.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				listweblogic.add(ipdiskList);
				ipdiskList = null;
			}
		}
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		// HostNodeDao dao = new HostNodeDao();
		request.setAttribute("pinglist", list);
		session.setAttribute("pinglistiis", listiis);
		session.setAttribute("pinglistweblogic", listweblogic);
		return "/capreport/tomcat/midping.jsp";
	}

	// ֮��zhushouzhidb��ͨ�ʱ���
	public void createDocContext(String file) throws DocumentException, IOException {
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 14, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("�м��ͨ�ʱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		List pinglistiis = (List) session.getAttribute("pinglistiis");
		List pinglistweblogic = (List) session.getAttribute("pinglistweblogic");
		Table aTable = new Table(5);
		this.setTableFormat(aTable);
		// int width[] = { 50, 50, 50, 70, 50};
		// aTable.setWidths(width);
		// aTable.setWidth(100); // ռҳ���� 100%
		// aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		// aTable.setAutoFillEmptyCells(true); // �Զ�����
		// aTable.setBorderWidth(1); // �߿���
		// aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		// aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		// aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		// aTable.setBorder(2);// �߿�
		// aTable.endHeaders();

		aTable.addCell(this.setCellFormat(new Cell("���"), true));
		Cell cell1 = new Cell("IP��ַ");
		Cell cell11 = new Cell("�豸����");
		Cell cell15 = new Cell("ƽ����ͨ��");
		Cell cell4 = new Cell("崻�����(��)");
		this.setCellFormat(cell1, true);
		this.setCellFormat(cell11, true);
		this.setCellFormat(cell15, true);
		this.setCellFormat(cell4, true);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell15);
		aTable.addCell(cell4);
		/*
		 * if (pinglist != null && pinglist.size() > 0) { for (int i = 0; i <
		 * pinglist.size(); i++) { List _pinglist = (List) pinglist.get(i);
		 * String ip = (String) _pinglist.get(0); String equname = (String)
		 * _pinglist.get(1); String osname = (String) _pinglist.get(2); String
		 * avgping = (String) _pinglist.get(3); String downnum = (String)
		 * _pinglist.get(4);
		 */

		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				Cell cell5 = new Cell(i + 1 + "");
				Cell cell6 = new Cell(ip);
				Cell cell8 = new Cell(equname);
				Cell cell10 = new Cell(avgping);
				Cell cell13 = new Cell(downnum);
				this.setCellFormat(cell5, false);
				this.setCellFormat(cell6, false);
				this.setCellFormat(cell8, false);
				this.setCellFormat(cell10, false);
				this.setCellFormat(cell10, false);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell8);
				aTable.addCell(cell10);
				aTable.addCell(cell13);

			}
		}
		Cell cell = null;
		if (pinglistiis != null && pinglistiis.size() > 0) {
			for (int i = 0; i < pinglistiis.size(); i++) {
				List _pinglist = (List) pinglistiis.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				cell = new Cell(i + 1 + +pinglist.size() + "");
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(ip);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(equname);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(avgping);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(downnum);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
			}
		}
		if (pinglistweblogic != null && pinglistweblogic.size() > 0) {
			for (int i = 0; i < pinglistweblogic.size(); i++) {
				List _pinglist = (List) pinglistweblogic.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				cell = new Cell(i + 1 + pinglist.size() + pinglistiis.size() + "");
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(ip);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(equname);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(avgping);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(downnum);
				this.setCellFormat(cell, false);
				aTable.addCell(cell);

			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	// ����������ͨ�ʱ���zhushouzhi
	public String createdoc() {
		String file = "/temp/shujukuliantonglvbaobiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContext(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/tomcat/download.jsp";
	}

	// ֮��zhushouzhidb��ͨ�ʱ���
	public void createPDFContext(String file) throws DocumentException, IOException {
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 14, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 14, Font.NORMAL);
		Paragraph title = new Paragraph("�м��ͨ�ʱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		List pinglistiis = (List) session.getAttribute("pinglistiis");
		List pinglistweblogic = (List) session.getAttribute("pinglistweblogic");
		Table aTable = new Table(5);
		this.setTableFormat(aTable);
		// int width[] = { 50, 50, 50, 70, 50 };
		// aTable.setWidths(width);
		// aTable.setWidthPercentage(100);

		Cell cell2 = new Cell(new Phrase("���", contextFont));
		Cell cell1 = new Cell(new Phrase("IP��ַ", contextFont));
		Cell cell11 = new Cell(new Phrase("�豸����", contextFont));
		Cell cell15 = new Cell(new Phrase("ƽ����ͨ��", contextFont));
		Cell cell4 = new Cell(new Phrase("崻�����(��)", contextFont));
		this.setCellFormat(cell2, true);
		this.setCellFormat(cell11, true);
		this.setCellFormat(cell1, true);
		this.setCellFormat(cell15, true);
		this.setCellFormat(cell4, true);
		aTable.addCell(cell2);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell15);
		aTable.addCell(cell4);
		/*
		 * if (pinglist != null && pinglist.size() > 0) { for (int i = 0; i <
		 * pinglist.size(); i++) { List _pinglist = (List) pinglist.get(i);
		 * String ip = (String) _pinglist.get(0); String equname = (String)
		 * _pinglist.get(1); String osname = (String) _pinglist.get(2); String
		 * avgping = (String) _pinglist.get(3); String downnum = (String)
		 * _pinglist.get(4);
		 */

		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				Cell cell5 = new Cell(new Phrase(i + 1 + ""));
				Cell cell6 = new Cell(new Phrase(ip));
				Cell cell8 = new Cell(new Phrase(equname, contextFont));
				Cell cell10 = new Cell(new Phrase(avgping));
				Cell cell13 = new Cell(new Phrase(downnum));
				this.setCellFormat(cell5, false);
				this.setCellFormat(cell6, false);
				this.setCellFormat(cell8, false);
				this.setCellFormat(cell10, false);
				this.setCellFormat(cell13, false);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell8);
				aTable.addCell(cell10);
				aTable.addCell(cell13);

			}
		}
		Cell cell = null;
		if (pinglistiis != null && pinglistiis.size() > 0) {
			for (int i = 0; i < pinglistiis.size(); i++) {
				List _pinglist = (List) pinglistiis.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				cell = new Cell(new Phrase(i + 1 + +pinglist.size() + ""));
				aTable.addCell(cell);
				this.setCellFormat(cell, false);
				cell = new Cell(new Phrase(ip));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(equname, contextFont));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				this.setCellFormat(cell, false);
				cell = new Cell(new Phrase(avgping));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(downnum));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
			}
		}
		if (pinglistweblogic != null && pinglistweblogic.size() > 0) {
			for (int i = 0; i < pinglistweblogic.size(); i++) {
				List _pinglist = (List) pinglistweblogic.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String portname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				cell = new Cell(new Phrase(i + 1 + pinglist.size() + pinglistiis.size() + ""));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(ip));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(equname, contextFont));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(avgping));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);
				cell = new Cell(new Phrase(downnum));
				this.setCellFormat(cell, false);
				aTable.addCell(cell);

			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	// ����������ͨ�ʱ���zhushouzhi
	public String createPDF() {
		String file = "/temp/shujukuliantonglvbaobiao.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createPDFContext(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/tomcat/download.jsp";
	}

	public String downloadselfpingexcel() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable reporthash = new Hashtable();
		List pinglist = (List) session.getAttribute("pinglist");
		List pinglistiis = (List) session.getAttribute("pinglistiis");
		List pinglistweblogic = (List) session.getAttribute("pinglistweblogic");
		reporthash.put("pinglist", pinglist);
		reporthash.put("pinglistiis", pinglistiis);
		reporthash.put("pinglistweblogic", pinglistweblogic);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		report.createReport_midping("temp/midnms_report.xls");// excel�ۺϱ���
		SysLogger.info("filename" + report.getFileName());
		request.setAttribute("filename", report.getFileName());
		return "/capreport/tomcat/download.jsp";
	}

	public String execute(String action) {
		if (action.equals("midlist"))
			return midlist();
		if (action.equals("find"))
			return find();
		if (action.equals("midping"))
			return midping();
		if (action.equals("createdoc"))
			return createdoc();
		if (action.equals("eventlist"))
			return eventlist();
		if (action.equals("midevent"))
			return midevent();
		if (action.equals("createPDF"))
			return createPDF();
		if (action.equals("downloadselfpingexcel"))
			return downloadselfpingexcel();

		// ����
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	// zhushouzhi--------------------hostping--pdf
	public String midevent() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("tomcatcheckbox");
		String[] idsiis = getParaArrayValue("iischeckbox");
		String[] idsweblogic = getParaArrayValue("weblogiccheckbox");
		Hashtable allcpuhash = new Hashtable();

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
		List orderList = new ArrayList();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				Node tomcatNode = PollingEngine.getInstance().getTomcatByID(Integer.parseInt(ids[i]));
				if (tomcatNode == null)
					continue;
				EventListDao eventdao = new EventListDao();
				// �õ��¼��б�
				StringBuffer s = new StringBuffer();
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='"
						+ totime + "' ");
				s.append(" and nodeid=" + tomcatNode.getId());

				List infolist = eventdao.findByCriteria(s.toString());
				if (infolist != null && infolist.size() > 0) {
					// mainreport = mainreport+ " \r\n";
					int pingvalue = 0;
					for (int j = 0; j < infolist.size(); j++) {
						EventList eventlist = (EventList) infolist.get(j);
						if (eventlist.getContent() == null)
							eventlist.setContent("");
						String content = eventlist.getContent();
						if (content.indexOf("TOMCAT����ֹͣ") > 0) {
							pingvalue = pingvalue + 1;
						}
					}
					String equname = tomcatNode.getAlias();
					String ip = tomcatNode.getIpAddress();
					List ipeventList = new ArrayList();
					ipeventList.add(ip);
					ipeventList.add(equname);
					ipeventList.add(pingvalue + "");
					orderList.add(ipeventList);
				}
			}
		}
		// iis-----------------------------------------------------
		List orderListiis = new ArrayList();
		if (idsiis != null && idsiis.length > 0) {
			for (int i = 0; i < idsiis.length; i++) {
				Node iisNode = PollingEngine.getInstance().getIisByID(Integer.parseInt(idsiis[i]));
				if (iisNode == null)
					continue;
				EventListDao eventdao = new EventListDao();
				// �õ��¼��б�
				StringBuffer s = new StringBuffer();
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='"
						+ totime + "' ");
				s.append(" and nodeid=" + iisNode.getId());

				List infolist = eventdao.findByCriteria(s.toString());
				if (infolist != null && infolist.size() > 0) {
					// mainreport = mainreport+ " \r\n";
					int pingvalue = 0;
					for (int j = 0; j < infolist.size(); j++) {
						EventList eventlist = (EventList) infolist.get(j);
						if (eventlist.getContent() == null)
							eventlist.setContent("");
						String content = eventlist.getContent();
						if (content.indexOf("IIS����ֹͣ") > 0) {
							pingvalue = pingvalue + 1;
						}
					}
					String equname = iisNode.getAlias();
					String ip = iisNode.getIpAddress();
					List ipeventList = new ArrayList();
					ipeventList.add(ip);
					ipeventList.add(equname);
					ipeventList.add(pingvalue + "");
					orderListiis.add(ipeventList);
				}
			}
		}
		// weblogic-------------------------------------------------------------
		List orderListweblogic = new ArrayList();
		if (idsweblogic != null && idsweblogic.length > 0) {
			for (int i = 0; i < idsweblogic.length; i++) {
				Node weblogicNode = PollingEngine.getInstance().getWeblogicByID(Integer.parseInt(idsweblogic[i]));
				if (weblogicNode == null)
					continue;
				EventListDao eventdao = new EventListDao();
				// �õ��¼��б�
				StringBuffer s = new StringBuffer();
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='"
						+ totime + "' ");
				s.append(" and nodeid=" + weblogicNode.getId());

				List infolist = eventdao.findByCriteria(s.toString());
				if (infolist != null && infolist.size() > 0) {
					// mainreport = mainreport+ " \r\n";
					int pingvalue = 0;
					for (int j = 0; j < infolist.size(); j++) {
						EventList eventlist = (EventList) infolist.get(j);
						if (eventlist.getContent() == null)
							eventlist.setContent("");
						String content = eventlist.getContent();
						if (content.indexOf("WEBLOGIC����ֹͣ") > 0) {
							pingvalue = pingvalue + 1;
						}
					}
					String equname = weblogicNode.getAlias();
					String ip = weblogicNode.getIpAddress();
					List ipeventList = new ArrayList();
					ipeventList.add(ip);
					ipeventList.add(equname);
					ipeventList.add(pingvalue + "");
					orderListweblogic.add(ipeventList);
				}
			}
		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("ping")) {
			returnList = (List) session.getAttribute("eventlist");
		} else {
			returnList = orderList;
		}
		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}
		// iis-------------------------------------------------------------------------------
		List returnListiis = new ArrayList();
		if (orderflag.equalsIgnoreCase("ping")) {
			returnListiis = (List) session.getAttribute("eventlist");
		} else {
			returnListiis = orderListiis;
		}
		List listiis = new ArrayList();
		if (returnListiis != null && returnListiis.size() > 0) {
			for (int m = 0; m < returnListiis.size(); m++) {
				List ipdiskList = (List) returnListiis.get(m);
				for (int n = m + 1; n < returnListiis.size(); n++) {
					List _ipdiskList = (List) returnListiis.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnListiis.remove(m);
							returnListiis.add(m, _ipdiskList);
							returnListiis.remove(n);
							returnListiis.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				listiis.add(ipdiskList);
				ipdiskList = null;
			}
		}
		// weblogic=-------------------------------------------------------------------
		List returnListweblogic = new ArrayList();
		if (orderflag.equalsIgnoreCase("ping")) {
			returnListweblogic = (List) session.getAttribute("eventlist");
		} else {
			returnListweblogic = orderListweblogic;
		}
		List listweblogic = new ArrayList();
		if (returnListweblogic != null && returnListweblogic.size() > 0) {
			for (int m = 0; m < returnListweblogic.size(); m++) {
				List ipdiskList = (List) returnListweblogic.get(m);
				for (int n = m + 1; n < returnListweblogic.size(); n++) {
					List _ipdiskList = (List) returnListweblogic.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnListweblogic.remove(m);
							returnListweblogic.add(m, _ipdiskList);
							returnListweblogic.remove(n);
							returnListweblogic.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				listweblogic.add(ipdiskList);
				ipdiskList = null;
			}
		}
		// setListProperty(capReportForm, request, list);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("eventlist", list);
		session.setAttribute("eventlist", list);
		request.setAttribute("eventlistiis", listiis);
		session.setAttribute("eventlistiis", listiis);
		request.setAttribute("eventlistweblogic", listweblogic);
		session.setAttribute("eventlistweblogic", listweblogic);
		return "/capreport/tomcat/midevent.jsp";
	}

}
