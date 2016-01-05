package com.afunms.polling.snmp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.afunms.common.util.SysLogger;
import java.util.*;
public class LoadSQLServerFile {

	private Element root;

	public LoadSQLServerFile(String path) {
		root = getRoot(path);
	}

	/**
	 * 
	 * @param path
	 *            Ҫ������sqlServer�����ļ���·��
	 * @return xml�ļ��ĸ�Ԫ��
	 */
	private Element getRoot(String path) {
		Element root = null;
		SAXBuilder sb = new SAXBuilder();
		try {
			Document dc = sb.build(new FileInputStream(path));
			root = dc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
			System.out.println("��ʼ��sqlServer�ļ�����");
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��ʼ��sqlServer�ļ�����");
			throw new RuntimeException(e);
		}
		return root;
	}

	/**
	 * 
	 * @return ����Ҫ������sql�������ļ���sqlserverProcessesTabInfo��Ϣ
	 */
	public Vector<Hashtable<String, String>> getSQLProcessesTabInfo() {
		Vector<Hashtable<String, String>> vector = new Vector<Hashtable<String, String>>();
		try {
			List list = XPath.selectNodes(root,
					"//content/sqlserverProcessesTabInfo/row");
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Element ele = (Element) itr.next();// �õ����е���Ԫ��
				List<Element> children = ele.getChildren();
				Hashtable<String, String> tab = new Hashtable<String, String>();
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
	 * @return ����Ҫ������sql�����ļ���
	 */
	public Hashtable<String, Object> getSQLServerDB() {
		Hashtable<String, Object> tables = new Hashtable<String, Object>();
		Vector<String> names = new Vector<String>();
		Hashtable<String, Hashtable<String, String>> datas = new Hashtable<String, Hashtable<String, String>>();
		Hashtable<String, Hashtable<String, String>> logs = new Hashtable<String, Hashtable<String, String>>();
		try {
			List list = XPath.selectNodes(root, "//content/sqlserverDB/row");
			Map<String,Integer>dbnames=new HashMap<String,Integer>();
			Map<String,Integer>lognames=new HashMap<String,Integer>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Element ele = (Element) itr.next();
				if (ele.getChild("dbname") != null) {
					// ���ݿ���Ϣ�ļ�
					Hashtable<String, String> data = new Hashtable<String, String>();
					String name= ele.getChildText("dbname");
					if(dbnames.get(name)==null){
						data.put("dbname",name);
						data.put("size", ele.getChildText("size"));
						if (ele.getChild("usedperc") != null) {
							data.put("usedperc", ele
									.getChildText("usedperc"));
						}
						if (ele.getChild("usedsize") != null)
							data.put("usedsize", ele
									.getChildText("usedsize"));
						names.add(name);
						datas.put(name, data);
						dbnames.put(name,1);
					}else{
						Hashtable<String,String> col=datas.get(name);
						String v_size=col.get("size");
						String v_usedprec=col.get("usedperc");
						String v_usedsize=col.get("usedsize");
						String c_size=ele.getChildText("size");
						String usedperc=ele.getChildText("usedperc");
						String c_usedsize= ele.getChildText("usedsize");
						float f_v_size=0;
						if(v_size!=null&&!"".equals(v_size)){
							f_v_size=Float.parseFloat(v_size);
						}
						float f_v_usedprec=0;
						if(v_usedprec!=null&&!"".equals(v_usedprec)){
							f_v_usedprec=Float.parseFloat(v_usedprec);
						}
						float f_v_usedsize=0;
						if(v_usedsize!=null&&!"".equals(v_usedsize)){
							f_v_usedsize=Float.parseFloat(v_usedsize);
						}
						float f_c_size=0;
						if(c_size!=null&&!"".equals(c_size)){
							f_c_size=Float.parseFloat(c_size);
						}
						float f_c_usedprec=0;
						if(usedperc!=null&&!"".equals(usedperc)){
							f_c_usedprec=Float.parseFloat(usedperc);
						}
						float f_c_usedsize=0;
						if(c_usedsize!=null&&!"".equals(c_usedsize)){
							f_c_usedsize=Float.parseFloat(c_usedsize);
						}
						float total=f_v_size+f_c_size;
						float usedsize=f_c_usedsize+f_v_usedsize;
						col.put("size",String.valueOf(total));
						col.put("usedsize", String.valueOf(usedsize));
						col.put("usedperc",String.valueOf(usedsize/total*100));
					}
					
				} else if (ele.getChild("logname") != null) {// ��־��¼��Ϣ
					String name=ele.getChildText("logname");
					if(lognames.get(name)==null){
						Hashtable<String, String> data = new Hashtable<String, String>();
						data.put("logname", name);
						data.put("size", ele.getChildText("size"));
						if (ele.getChild("usedperc") != null) {
							data.put("usedperc", ele
									.getChildText("usedperc"));
						}
						if (ele.getChild("usedsize") != null)
							data.put("usedsize", ele
									.getChildText("usedsize"));
						logs.put(name, data);
						lognames.put(name,1);
					}else{
						Hashtable<String,String> col=logs.get(name);
						String v_size=col.get("size");
						String v_usedprec=col.get("usedperc");
						String v_usedsize=col.get("usedsize");
						String c_size=ele.getChildText("size");
						String usedperc=ele.getChildText("usedperc");
						String c_usedsize= ele.getChildText("usedsize");
						float f_v_size=0;
						if(v_size!=null&&!"".equals(v_size)){
							f_v_size=Float.parseFloat(v_size);
						}
						float f_v_usedprec=0;
						if(v_usedprec!=null&&!"".equals(v_usedprec)){
							f_v_usedprec=Float.parseFloat(v_usedprec);
						}
						float f_v_usedsize=0;
						if(v_usedsize!=null&&!"".equals(v_usedsize)){
							f_v_usedsize=Float.parseFloat(v_usedsize);
						}
						float f_c_size=0;
						if(c_size!=null&&!"".equals(c_size)){
							f_c_size=Float.parseFloat(c_size);
						}
						float f_c_usedprec=0;
						if(usedperc!=null&&!"".equals(usedperc)){
							f_c_usedprec=Float.parseFloat(usedperc);
						}
						float f_c_usedsize=0;
						if(c_usedsize!=null&&!"".equals(c_usedsize)){
							f_c_usedsize=Float.parseFloat(c_usedsize);
						}
						float total=f_v_size+f_c_size;
						float usedsize=f_c_usedsize+f_v_usedsize;
						col.put("size",String.valueOf(total));
						col.put("usedsize", String.valueOf(usedsize));
						col.put("usedperc",String.valueOf(usedsize/total*100));
					}
					
				}
			}
			tables.put("database", datas);
			tables.put("logfile", logs);
			tables.put("names", names);
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tables;
	}

	/**
	 * 
	 * @return ����Ҫ����������sql�����ļ���sqlServerSysTabInfo��Ϣ
	 */
	public Hashtable<String, String> getSQLServerSysTabInfo() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/sqlServerSysTabInfo/row");
			table.put("productlevel", element.getChildText("productlevel"));
			table.put("VERSION", element.getChildText("version"));
			table.put("MACHINENAME", element.getChildText("machineName"));
			table.put("IsSingleUser", element.getChildText("isSingleUser"));
			table.put("ProcessID", element.getChildText("ProcessID"));
			table.put("IsIntegratedSecurityOnly", element
					.getChildText("isIntegratedSecurityOnly"));
			table.put("IsClustered", element.getChildText("isClustered"));
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return �õ�SQLServer��״̬��Ϣ
	 */
	public String getStatus() {
		String str = null;
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/status");
			str = element.getText();
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return str;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���pages��Ϣ
	 */
	public Hashtable<String, String> getPages() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/page");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���pageConnection��Ϣ
	 */
	public Hashtable<String, String> getPageConn() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/pageConnection");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���lock��Ϣ
	 */
	public Hashtable<String, String> getLocks() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/lock");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���caches��Ϣ
	 */
	public Hashtable<String, String> getCaches() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/caches");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���mems��Ϣ
	 */
	public Hashtable<String, String> getMems() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/mems");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���sqls��Ϣ
	 */
	public Hashtable<String, String> getSqls() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/sqls");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return ���Ҫ������xml�ļ���scans��Ϣ
	 */
	public Hashtable<String, String> getScans() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		try {
			Element element = (Element) XPath.selectSingleNode(root,
					"//content/scans");
			List<Element> children = element.getChildren();
			for (Element child : children) {
				table.put(child.getName(), child.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return table;
	}

	/**
	 * 
	 * @return�õ�SQLServer�����õ�������Ϣ
	 */
	@SuppressWarnings("unchecked")
	public Hashtable getSQLInital() {
		Hashtable table = new Hashtable();
		table.put("info_v", getSQLProcessesTabInfo());
		table.put("dbValue", getSQLServerDB());
		table.put("sysValue", getSQLServerSysTabInfo());
		table.put("status", getStatus());

		Hashtable retValue = new Hashtable();
		retValue.put("pages",getPages());//�������ͳ��
		retValue.put("pageConnection",getPageConn());//���ݿ�ҳ����ͳ��
		retValue.put("locks",getLocks());//����ϸ
		retValue.put("caches",getCaches());//������ϸ
		retValue.put("mems", getMems());//�ڴ��������
		retValue.put("sqls",getSqls());//SQLͳ��
		retValue.put("scans",getScans());//���ʷ�������ϸ 
		table.put("retValue", retValue);
		return table;
	}

	public static void main(String[] args) {
		LoadSQLServerFile init = new LoadSQLServerFile("g://SQL2.xml");
		init.getSQLInital();
	}
}
