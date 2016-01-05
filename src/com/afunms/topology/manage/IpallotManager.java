/*
 * @(#)IpallotManager.java     v1.01, Aug 22, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.manage;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ping.PingUtil;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.topology.util.IPAllotUtil;

/**
 * 
 * ClassName:   IpallotManager.java
 * <p>
 *
 * @author      Œ‚∆∑¡˙
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 22, 2013 11:27:09 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class IpallotManager extends BaseManager implements ManagerInterface {

	
	public String list() {
//		 IpAliasDao dao = new IpAliasDao();
		IPAllotUtil ipa = new IPAllotUtil();
		Map<String, List<String>> map = ipa.sort();
		Object str = null;
		String[] key = new String[map.size()];
		int i = 0;
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			str = iter.next();
			key[i] = str.toString().substring(0, str.toString().length());
			i++;
		}
		key = ipa.ipsort(key); // ≈≈–Ú

		request.setAttribute("key", key);
		String jsp = "/topology/ipallot/list.jsp";
		return jsp;
	}
	
	
	private String read() {
		String targetJsp = "/topology/ipallot/read.jsp";
		Object key = getParaValue("key");

		IPAllotUtil ipa = new IPAllotUtil();
		List<String> _value = null;

		Map<String, List<String>> map = ipa.sort();
		Iterator iter = map.keySet().iterator();
		_value = map.get(key);

		String[] value = new String[_value.size()];
		for (int i = 0; i < _value.size(); i++) {
			value[i] = _value.get(i);
		}

		value = ipa.ipsort(value);

		request.setAttribute("value", value);
		request.setAttribute("key", key);

		return targetJsp;
	}
	
	private String readPie() {
		String targetJsp = "/topology/ipallot/readPie.jsp";
		String tmp = getParaValue("tmp");
		String tmpOnline = getParaValue("tmpOnline");
		String tmpOther = getParaValue("tmpOther");
		String title = getParaValue("title");

		request.setAttribute("tmp", tmp);
		request.setAttribute("tmpOnline", tmpOnline);
		request.setAttribute("tmpOther", tmpOther);
		request.setAttribute("title", title);

		return targetJsp;
	}

	public String execute(String action) {
		if (action.equals("list"))
			return list();

		if (action.equals("read"))
			return read();

		if (action.equals("readPie"))
			return readPie();

		if (action.equals("downloadnetworklistfuck"))
			return downloadnetworklistfuck();

		if (action.equals("reportlist")) {
			return reportList();
		}
		if (action.equals("readword"))
			return readWord();
		if (action.equals("scanInlineIp")) {
			return scanInlineIp();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;

	}
	
	public String scanInlineIp() {
		//key 10.76.144	±Ì æÕ¯∂Œ
		String key = getParaValue("key");
		Hashtable<String,String> ippingHash = new Hashtable<String,String>();
		
		IPAllotUtil ipa = new IPAllotUtil();
		List<String> _value = null;

		Map<String, List<String>> map = ipa.sort();
		Iterator iter = map.keySet().iterator();
		_value = map.get(key);

		String[] value = new String[_value.size()];
		for (int i = 0; i < _value.size(); i++) {
			value[i] = _value.get(i);
		}

		value = ipa.ipsort(value);
		
//		String[] ips = new String[255];
//		for (int i = 0; i < 255; i++) {
//			System.out.println(">>>>>>>>>>>>>>>>>"+i);
//			String ipAddress = key+"."+i;
//			int[] result = PingUtil.ping(ipAddress);
//			int connect = result[0];
//			 
//			ippingHash.put(ipAddress, String.valueOf(connect));
//		}
		
		request.setAttribute("value", value);
		request.setAttribute("key", key);
		request.setAttribute("ippingHash", ippingHash);
		return "/topology/ipallot/read.jsp";
	}

	private String downloadnetworklistfuck() {
		Hashtable reporthash = new Hashtable();
		Object key = getParaValue("key");

		IPAllotUtil ipa = new IPAllotUtil();
		List<String> _value = null;

		Map<String, List<String>> map = ipa.sort();
		Iterator iter = map.keySet().iterator();
		_value = map.get(key);

		String[] value = new String[_value.size()];
		for (int i = 0; i < _value.size(); i++) {
			value[i] = _value.get(i);
		}

		value = ipa.ipsort(value);

		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);

		report.createReport_ipallotlist("/temp/ipallotlist_report.xls", value,
				key);

		request.setAttribute("filename", report.getFileName());
		return "/topology/ipallot/downloadreport.jsp";
	}

	private String reportList() {
		Object key = getParaValue("key");

		IPAllotUtil ipa = new IPAllotUtil();
		List<String> _value = null;

		Map<String, List<String>> map = ipa.sort();
		Iterator iter = map.keySet().iterator();
		_value = map.get(key);

		String[] value = new String[_value.size()];
		for (int i = 0; i < _value.size(); i++) {
			value[i] = _value.get(i);
		}

		value = ipa.ipsort(value);

		request.setAttribute("value", value);
		request.setAttribute("key", key);

		return "/topology/ipallot/reportlist.jsp";

	}

	private String readWord() {

		Object key = getParaValue("key");

		IPAllotUtil ipa = new IPAllotUtil();
		List<String> _value = null;

		Map<String, List<String>> map = ipa.sort();
		Iterator iter = map.keySet().iterator();
		_value = map.get(key);

		String[] value = new String[_value.size()];
		for (int i = 0; i < _value.size(); i++) {
			value[i] = _value.get(i);
		}

		value = ipa.ipsort(value);

		request.setAttribute("value", value);
		request.setAttribute("key", key);
		return "/topology/ipallot/readWord.jsp";
	}

}

