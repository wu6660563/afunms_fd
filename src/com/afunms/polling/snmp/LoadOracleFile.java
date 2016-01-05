package com.afunms.polling.snmp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.afunms.common.util.ShareData;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;

public class LoadOracleFile {

	private Element root;
	private Hashtable sendeddata = ShareData.getProcsendeddata();

	public LoadOracleFile() {
		// root=getRoot(".");
	}

	/**
	 * 
	 * @param path
	 *            Ҫ������xml�����ļ���·��
	 */
	public LoadOracleFile(String path) {
		root = getRoot(path);
	}

	/**
	 * 
	 * @param path
	 *            Ҫ������oracle�����ļ���·��
	 * @return xml�ļ��ĸ�Ԫ��
	 */
	private Element getRoot(String path) {
		Element root = null;
		SAXBuilder sb = new SAXBuilder();
		try {
			Document dc = sb.build(new FileReader(path));
			root = dc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
			System.out.println("��ʼ��oracle�ļ�����");
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��ʼ��oracle�ļ�����");
			throw new RuntimeException(e);
		}
		return root;
	}

	/**
	 * 
	 * @return ����xml�ļ��е�Tablespace��Ϣ
	 */
	public Vector<Hashtable<String, String>> getTableSpace() {
		Vector<Hashtable<String, String>> vector = new Vector<Hashtable<String, String>>();
		try {
			Map<String,Integer>names=new HashMap<String,Integer>();
			List list = XPath.selectNodes(root, "//content/tablespace/row");//ȡ��tablespace�µ�����row
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Hashtable<String, String> tab = new Hashtable<String, String>();
				Element ele = (Element) itr.next();
				List<Element> children = ele.getChildren();
				String name="";
				for (Element child : children) {//��row������Ҫ����Ϣȡ�����ŵ�hashtable��
					//name=child.getName();
					tab.put(child.getName().toLowerCase(), child.getText());
				}
				name=tab.get("tablespace");
				if(names.get(name)==null){
					 vector.add(tab);
					 names.put(name, 1);
				}	   
				else{
					for(Hashtable<String,String> tt:vector){
						String tname=tt.get("tablespace");
						if(name.equals(tname)){
							String tem1=tt.get("free_mb");
							String tem2=tab.get("free_mb");
							String total1=tt.get("size_mb");
							String total2=tab.get("size_mb");
							float t1=0;
							if(total1!=null&&!"".equals(total1))
								t1=Float.parseFloat(total1);
							float t2=0;
							if(total2!=null&&!"".equals(total2))
								t2=Float.parseFloat(total2);
							float used=Float.parseFloat(tem1)+Float.parseFloat(tem2);
							Float f=(used)/(t1+t2);
							tt.put("percent_free",String.valueOf(f*100));
							tt.put("size_mb",String.valueOf(t1+t2));
							tt.put("free_mb",String.valueOf(used));
							String path=tab.get("file_name");
							int len=path.lastIndexOf("\\");
							if(len!=-1){
								String tpath=path.substring(0, len);
								tt.put("file_name",tpath);
							}	
							break;
						}
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return vector;
	}

	/**
	 * 
	 * @return ����xml�ļ��е�session��Ϣ
	 */
	public Vector<Hashtable<String, String>> getSession() {
		Vector<Hashtable<String, String>> vector = new Vector<Hashtable<String, String>>();
		try {
			List list = XPath.selectNodes(root, "//content/session/row");
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Hashtable<String, String> tab = new Hashtable<String, String>();
				Element ele = (Element) itr.next();
				List<Element> children = ele.getChildren();
				for (Element child : children) {
					tab.put(child.getName(), child.getText());
				}
				vector.add(tab);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return vector;
	}

	/**
	 * 
	 * @return ����xml�ļ��е�oracleSys��Ϣ
	 */
	public Hashtable<String, String> getOracleSys() {
		Hashtable<String, String> tab = new Hashtable<String, String>();
		try {
			Element ele = (Element) XPath.selectSingleNode(root,"//content/oracleSys/row");
			List<Element> children = ele.getChildren();
			for (Element child : children) {
				tab.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return tab;
	}

	/**
	 * 
	 * @return ����xml�ļ��е�oracleRollbackInfo��Ϣ
	 */
	public Vector<Hashtable<String, String>> getRollbackInfon() {
		Vector<Hashtable<String, String>> vector = new Vector<Hashtable<String, String>>();
		try {
			List list = XPath.selectNodes(root,
					"//content/oracleRollbackInfo/row");
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Hashtable<String, String> tab = new Hashtable<String, String>();
				Element ele = (Element) itr.next();
				List<Element> children = ele.getChildren();
				for (Element child : children) {
					tab.put(child.getName(), child.getText());
				}
				vector.add(tab);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return vector;
	}

	/**
	 * 
	 * @return ����xml�ļ��е�oracleLockInfo��Ϣ
	 */
	public Vector<Hashtable<String, String>> getLockInfo() {
		Vector<Hashtable<String, String>> vector = new Vector<Hashtable<String, String>>();
		try {
			List list = XPath.selectNodes(root, "//content/oracleLockInfo/row");
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Hashtable<String, String> tab = new Hashtable<String, String>();
				Element ele = (Element) itr.next();
				List<Element> children = ele.getChildren();
				for (Element child : children) {
					tab.put(child.getName(), child.getText());
				}
				vector.add(tab);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return vector;
	}

	/**
	 * 
	 * @return ����oracle�����ļ���oracleMem����Ϣ
	 */
	public Hashtable<String, String> getOracleMem() {
		Hashtable<String, String> tab = new Hashtable<String, String>();
		try {
			List list = XPath.selectNodes(root, "//content/oracleMem/row");
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/oracleMem");
			list = element.getChildren();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Element ele = (Element) itr.next();
				if (!"row".equals(ele.getName())) {
					tab.put(ele.getName(), ele.getText());
				} else {
					List<Element> chidren = ele.getChildren();
					for (Element child : chidren) {
						tab.put(child.getName(), child.getText());
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return tab;
	}
	
	/**
	 * 
	 * @return �õ�oracle�����ļ��е�status��Ϣ
	 */
	public String getStatus(){
		String str=null;
		try {
			Element ele = (Element) XPath.selectSingleNode(root,
			"//content/status");
			str=ele.getText();
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return str;
	}

	
	/**
	 * 
	 * @return �õ� oracle  �����ļ�������������Ϣ
	 */
	public Hashtable<String,Object> getOracleInit(){
		Hashtable table=new Hashtable();
		table.put("sysValue",getOracleSys());
		table.put("memValue",getOracleMem());
		table.put("tableinfo_v",getTableSpace());
		table.put("rollbackinfo_v",getRollbackInfon());
		table.put("lockinfo_v",getLockInfo());
		table.put("info_v",getSession());
		table.put("status", getStatus());
		return table;
	}
	public static void main(String[] args) {
		LoadOracleFile init = new LoadOracleFile("g://Oracle.xml");
		Hashtable table=init.getOracleInit();
		System.out.println(table.size());
	}
	
}
