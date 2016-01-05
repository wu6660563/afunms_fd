package com.afunms.polling.snmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.afunms.application.dao.DBDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.SysUtil;
import com.afunms.initialize.ResourceCenter;
import com.dhcc.pdm.contract;
import com.ibm.as400.util.commtrace.Data;
import com.informix.util.dateUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LoadInformixFile {

	private Element root;

	public LoadInformixFile(String path) {
		root = getRoot(path);
	}

	public LoadInformixFile(){
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
	
	
	public Hashtable getInformixConfig(){
		Hashtable table=new Hashtable();
		ArrayList databaselist = new ArrayList();//���ݿ���Ϣ
		ArrayList loglist = new ArrayList();//��־�ļ���Ϣ
		ArrayList spaceList = new ArrayList();//�ռ���Ϣ
		ArrayList configList = new ArrayList();//������Ϣ
		ArrayList sessionList = new ArrayList();//�Ự��Ϣ
		ArrayList lockList = new ArrayList();//����Ϣ��Ϣ
		ArrayList iolist = new ArrayList();//IO��Ϣ
		ArrayList aboutlist = new ArrayList();//��Ҫ��Ϣ
		
		try {
			List list=XPath.selectNodes(root, "//content/databaselist/column");
			Iterator it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				databaselist.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/informixlog/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				loglist.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/informixspaces/column");
			 it=list.iterator();
			 Map<String,Integer>names=new HashMap<String,Integer>();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				String name=(String)tb.get("dbspace");
				if(names.get(name)==null){
					spaceList.add(tb);
					String path=(String)tb.get("fname");
		  			int len=path.lastIndexOf("\\");
		  			if(len>0){
		  				tb.put("fname",path.substring(0, len));
		  			}
					names.put(name,1);
				}else{
				  	for(int i=0;i<spaceList.size();i++){
				  		Hashtable col=(Hashtable)spaceList.get(i);
				  		if(name.equals(col.get("dbspace"))){
				  			String v_page_size=(String)col.get("pages_size");
				  			String v_page_userd=(String)col.get("pages_used");
				  			String c_page_size=(String)tb.get("pages_size");
				  			String c_page_userd=(String)tb.get("pages_used");
				  			float f_v_size=0;
				  			if(v_page_size!=null&&!"".equals(v_page_size))
				  				f_v_size=Float.parseFloat(v_page_size);
				  			float f_v_userd=0;
				  			if(v_page_userd!=null&&!"".equals(v_page_userd))
				  				f_v_userd=Float.parseFloat(v_page_userd);
				  			float f_c_size=0;
				  			if(c_page_size!=null&&!"".equals(c_page_size))
				  				f_c_size=Float.parseFloat(c_page_size);
				  			float f_c_userd=0;
				  			if(c_page_userd!=null&&!"".equals(c_page_userd))
				  				f_c_userd=Float.parseFloat(c_page_userd);
				  			float total=f_v_size+f_c_size;
				  			float userd=f_v_userd+f_c_userd;
				  			col.put("pages_size",total);
				  			col.put("pages_used",userd);
				  			col.put("pages_free",total-userd);
				  			col.put("percent_free",(total-userd)*100/total);
				  			//col.put("");
				  			String path=(String)tb.get("fname");
				  			int len=path.lastIndexOf("\\");
				  			if(len>0){
				  				col.put("fname",path.substring(0, len));
				  			}
				  			break;
				  		}
				  	}
				}
			}
			
			list=XPath.selectNodes(root, "//content/configList/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				configList.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/sessionList/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				sessionList.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/lockList/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				lockList.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/iolist/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				iolist.add(tb);
			}
			
			list=XPath.selectNodes(root, "//content/aboutlist/column");
			 it=list.iterator();
			while(it.hasNext()){
				Element ele=(Element)it.next();
				List<Element> children=ele.getChildren();
				Hashtable tb=new Hashtable();
				for(Element child:children){
					tb.put(child.getName(),child.getText());
				}
				aboutlist.add(tb);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		table.put("informixspaces", spaceList);//�ռ���Ϣ
		table.put("informixlog", loglist);//��־��Ϣ
		table.put("databaselist", databaselist);//���ݿ���Ϣ
		table.put("configList", configList);//������Ϣ
		table.put("sessionList", sessionList);//�Ự��Ϣ
		table.put("lockList", lockList);//����Ϣ��Ϣ
		table.put("iolist", iolist);//IO��Ϣ
		table.put("aboutlist", aboutlist);//��Ҫ�ļ���Ϣ
		return table;
	}
	
	public String getStatus(){
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
	
	public Hashtable getInformixFile(){
		Hashtable table=new Hashtable();
		table.put("informix",getInformixConfig());
		table.put("status",getStatus());
		return table;
	}
	
	/**
	 * ����Informix��log�ļ�
	 * @author HONGLI	
	 * @param ipaddress
	 * @return
	 */
	public Hashtable loadInformixFile(String ipaddress){
		Hashtable retHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		String collecttime = "";
		try{
			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/"+ipaddress+"_informix.log";				
			//D:\Tomcat5.0\webapps\afunms
//			String filename = "D:/Tomcat5.0/webapps/afunms" + "/linuxserver/"+ipaddress+"_informix.log";				
			File file=new File(filename);
			if(!file.exists()){
				//�ļ�������,������澯
//				try{
////					createFileNotExistSMS(ipaddress);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
				return retHash;
			}
			file = null;
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			try {
				String strLine = null;
				//�����ļ�����
				while((strLine=br.readLine())!=null){
					fileContent.append(strLine + "\n");
					//SysLogger.info(strLine);
				}
			} catch (RuntimeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				if(isr != null){
					isr.close();
				}
				if(fis != null){
					fis.close();
				}
				if(br != null){
					br.close();
				}
			}
    		copyFile(ipaddress,getMaxNum(ipaddress));
		} catch (Exception e){
			e.printStackTrace();
		}
    	Pattern tmpPt = null;
    	Matcher mr = null;
    	
	     //----------------�������ݲɼ�ʱ������--���������---------------------        	
		tmpPt = Pattern.compile("(cmdbegin:collectiontime)(.*)(cmdbegin:onstat-f)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find()){
			collecttime = mr.group(2);
		}
		if (collecttime != null && collecttime.length()>0 ){
			collecttime = collecttime.trim();
		}
		
		//----------------����onstat -p--���������---------------------  
		/**
		 *  �ȴ������߳����� lokwaits
		 *  ����������    ovbuff
		 *	���ݿ������    ovlock
		 *	������         deadlks
		 *	���ݿ⻺������Buffer�ȴ����  bufwaits
		 *	�����ڴ��������  bufreads_cached
	     *  �����ڴ�д������  bufwrits_cached
		 */
		String lokwaits = "";//�ȴ������߳�����
		String ovbuff = "";//����������
		String ovlock = "";//���ݿ������
		String deadlks = "";//������
		String bufwaits = "";//���ݿ⻺������Buffer�ȴ����
		String bufreads_cached = "";//�����ڴ��������
		String bufwrits_cached = "";//�����ڴ�д������
		String lockContent = "";
		tmpPt = Pattern.compile("(cmdbegin:onstat-p)(.*)(cmdbegin:onstat-d)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find()){
			lockContent = mr.group(2);
		}
		if (lockContent != null && lockContent.length()>0){
			String[] lockInforLineStrings = lockContent.split("\n");
			for(int i=0; i<lockInforLineStrings.length-1; i++){//����ÿһ��
				String[] headStrings = lockInforLineStrings[i].split("\\s++");//������
				String[] detailStrings = lockInforLineStrings[i+1].split("\\s++");//������
				for(int j=0; j<headStrings.length; j++){
					String line = headStrings[j];
					if(line.equals("lokwaits")){
						lokwaits = detailStrings[j];
					}
					if(line.equals("ovbuff")){
						ovbuff = detailStrings[j];
					}
					if(line.equals("ovlock")){
						ovlock = detailStrings[j];
					}
					if(line.equals("deadlks")){
						deadlks = detailStrings[j];
					}
					if(line.equals("bufwaits")){
						bufwaits = detailStrings[j];
					}
					if(line.equals("bufreads")){//�����ڴ��  %cached
						bufreads_cached = detailStrings[j+1];//detailStrings[j]Ϊ���ڴ�Ĵ�С
					}
					if(line.equals("bufwrits")){//�����ڴ��  %cached
						bufwrits_cached = detailStrings[j+1];
					}
				}
			}
		}
		//----------------����onstat-f--���������---------------------
		/**
		 * ���ݿ����ܼ��FOREGROUND WRITE���
		 */
		String fgWrites = "";//���ݿ����ܼ��FOREGROUND WRITE���
		String stateContent = "";
		tmpPt = Pattern.compile("(cmdbegin:onstat-f)(.*)(cmdbegin:onstat-g)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find()){
			stateContent = mr.group(2);
		}
		if (stateContent != null && stateContent.length()>0){
			String[] stateInforLineStrings = stateContent.split("\n");
			for(int i=0; i<stateInforLineStrings.length-1; i++){//����ÿһ��
				String line = stateInforLineStrings[i];//������
				String[] detailStrings = stateInforLineStrings[i+1].split("\\s++");//������  //�˴����ڹؼ������пո� ����˲�����ͨ���㷨
				if(line.indexOf("Fg Writes") != -1){
					fgWrites = detailStrings[0];
				}
			}
		}
		
		//----------------����onstat-g--���������---------------------
		/**
		 * ���ݿ�ȴ�����
		 */
		String waitingThreadsContent = "";
		List<String> waitingThreads = new ArrayList<String>();//���ݿ�ȴ�����
		tmpPt = Pattern.compile("(cmdbegin:onstat-g)(.*)(cmdbegin:onstat-p)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find()){
			waitingThreadsContent = mr.group(2);
		}
		if (waitingThreadsContent != null && waitingThreadsContent.length()>0){
			String[] waitingInforLineStrings = waitingThreadsContent.split("\n");
			for(int i=0; i<waitingInforLineStrings.length; i++){//����ÿһ��
				String detailString = waitingInforLineStrings[i];//������
				String[] detailStrings = detailString.split("\\s++");
				if(detailStrings.length > 8 && detailStrings.length < 15 && !detailStrings[0].equals("tid")){
					waitingThreads.add(waitingInforLineStrings[i].trim());
				}
			}
		}
		retHash.put("waitingThreads", waitingThreads);//���ݿ�ȴ�����
		retHash.put("fgWrites", fgWrites);//���ݿ����ܼ��FOREGROUND WRITE���
		retHash.put("lokwaits", lokwaits);//�ȴ������߳�����
		retHash.put("ovbuff", ovbuff);//����������
		retHash.put("ovlock", ovlock);//���ݿ������ 
		retHash.put("deadlks", deadlks);//������ 
		retHash.put("bufwaits", bufwaits);//���ݿ⻺������Buffer�ȴ����
		retHash.put("bufreads_cached", bufreads_cached);//�����ڴ��������
		retHash.put("bufwrits_cached", bufwrits_cached);//�����ڴ�д������
		retHash.put("collecttime", collecttime);//informix���ݿ�����ʱ��
		
		return retHash;
	}
	
	
	/**
	 * ����Informix��bar_act.log�ļ�
	 * @author HONGLI	
	 * @param ipaddress
	 * @return
	 */
	public Hashtable loadInformixBarActLogFile(String ipaddress){
		Hashtable retHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		String collecttime = "";
		try{
//			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/"+ipaddress+"_bar_act.log";				
			//D:\Tomcat5.0\webapps\afunms
			String filename = "D:/Tomcat5.0/webapps/afunms" + "/linuxserver/"+ipaddress+"_bar_act.log";				
			File file=new File(filename);
			if(!file.exists()){
				//�ļ�������,������澯
//				try{
////					createFileNotExistSMS(ipaddress);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
				return retHash;
			}
			file = null;
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			try {
				String strLine = null;
				//�����ļ�����
				while((strLine=br.readLine())!=null){
					fileContent.append(strLine + "\n");
					//SysLogger.info(strLine);
				}
			} catch (RuntimeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				if(isr != null){
					isr.close();
				}
				if(fis != null){
					fis.close();
				}
				if(br != null){
					br.close();
				}
			}
//    		copyFile(ipaddress,getMaxNum(ipaddress));
		} catch (Exception e){
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//�����ݿ��ж�ȡ�ϴα��ݵ�����  2010-08-20 01:38:15
		String lastBackDate = "2011-05-06 04:05:55";
		//ȡ��nms_informixother���һ����¼
		IpTranslation tranfer = new IpTranslation();
		String hex = tranfer.formIpToHex(ipaddress);
		DBDao dbDao = new DBDao();
		DBVo dbmonitorlist = null;
		try{
			dbmonitorlist = (DBVo)(((ArrayList)dbDao.findByCondition(" where ip_address = '"+ipaddress+"'")).get(0));
			lastBackDate = dbDao.getInformix_nmsbaractBackTime(hex+":"+dbmonitorlist.getDbName());
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			dbDao.close();
		}
		String thisBackDate = "";//�˴α��ݵ�����
		List<String> actlogList = new ArrayList<String>();
		//���ݿ��ϴ�0�����ݵ����ڵ�����
		String[] lines = fileContent.toString().split("\\n");

		//�õ����һ��log��Ϣ������
		for(int i=lines.length-1; i>0; i--){//�����ȡ
			if(lines[i] != null && !lines[i].trim().equals("")){
				if(lines[i].split("\\s")[1].matches("(\\d{4})-(\\d{1,2})-(\\d{1,2})") && lines[i].split("\\s")[2].matches("(\\d{2}):(\\d{2}):(\\d{2})")){
					thisBackDate = lines[i].split("\\s")[1] +" "+ lines[i].split("\\s")[2];
					break;
				}
			}
		}
//		int lastBackDateToThisBackDay = 0;
		if(lastBackDate == null || lastBackDate.equals("")){//���ݿ���δ�����bar_act.log������
			//����ȫ��
			lines = arrybaractlogs(lines);
			retHash.put("backdate", thisBackDate);//�˴α��ݵ�����
			retHash.put("baractlogs", lines);
//			retHash.put("lastBackDateToThisBackDay", lastBackDateToThisBackDay+"");//���ݿ��ϴ�0�����ݵ����ڵ�����
			return retHash;
		}
		//�õ����һ�α���log֮�����±��ݵĵ�һ������
		String newFirstBackDate = "";//�±��ݵĵ�һ������
		try {
			for(int i=0; i<lines.length; i++){//�����ȡ
				if(lines[i] != null && !lines[i].trim().equals("")){
					if(lines[i].split("\\s")[1].matches("(\\d{4})-(\\d{1,2})-(\\d{1,2})") && lines[i].split("\\s")[2].matches("(\\d{2}):(\\d{2}):(\\d{2})")){
						if(sdf.parse(lines[i].split("\\s")[1] +" "+ lines[i].split("\\s")[2]).getTime() > sdf.parse(lastBackDate).getTime()){//log���������� > ���ݿ����ϴα��������
							newFirstBackDate = lines[i].split("\\s")[1] +" "+ lines[i].split("\\s")[2];
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("��һ�α��ݵ�����=="+lastBackDate);
//		System.out.println("�õ����һ�α���log֮�����±��ݵĵ�һ������=="+newFirstBackDate);
//		System.out.println("���α��ݵĽ�������=="+thisBackDate);
		//��ȡ���һ�α���ʱ ����ӵ�log����
		String fgWrites = "";//���ݿ����ܼ��FOREGROUND WRITE���
		if(newFirstBackDate != null && !newFirstBackDate.equals("")){
			String content = "";
			Pattern tmpPt = null;
			Matcher mr = null;
			tmpPt = Pattern.compile("("+newFirstBackDate+")(.*)",Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if(mr.find()){
				content = mr.group(2);
			}
			//���
			String line = null;
			if(content != null && content.length() != 0){
				lines = content.split("\\n");
				lines[0] = newFirstBackDate+lines[0];//�����ϱ����˵��ĵ�һ��
			}
		}else{
			lines = new String[0];
		}
//		try {
//			long d = (new Date().getTime() - sdf.parse(lastBackDate).getTime())/(24*3600*1000);
//			lastBackDateToThisBackDay = (int)d;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		lines = arrybaractlogs(lines);
		retHash.put("backdate", thisBackDate);//�˴α��ݵ�����
//		retHash.put("lastBackDateToThisBackDay", lastBackDateToThisBackDay+"");//���ݿ��ϴ�0�����ݵ����ڵ�����
		retHash.put("baractlogs", lines);//���ݿ�ONBAR��־����WARN��ERROR��Ϣ
		return retHash;
	}
	
	/**
	 * ������ʱ�俪ͷ���ַ���  ��ӵ���ʱ�俪ͷ���ַ����Ľ�β
	 * @param lines
	 * @return
	 */
	public String[] arrybaractlogs(String[] lines){
		String[] strings = null;
		Vector<String> tempVector = new Vector<String>();
		for(int i=0; i<lines.length; i++){
			String line = lines[i].trim();
			if(line == null || line.equals("")){
				continue;
			}
			String string = line.split("\\s")[0];
			if(!string.matches("(\\d{4})-(\\d{1,2})-(\\d{1,2})")){
				line = tempVector.get(tempVector.size()-1)+line;
				tempVector.remove(tempVector.size()-1);
				tempVector.add(line);
			}else{
				tempVector.add(line);
			}
		}
		strings = new String[tempVector.size()];
		for(int i=0; i<tempVector.size(); i++){
			strings[i] = tempVector.get(i);
		}
		tempVector = null;
		return strings;
	}
	
	
	public void copyFile(String ipAddress,String max){
		try { 
			String currenttime = SysUtil.getCurrentTime();
			currenttime = currenttime.replaceAll("-", "");
			currenttime = currenttime.replaceAll(" ", "");
			currenttime = currenttime.replaceAll(":", "");
			String ipdir = ipAddress.replaceAll("\\.", "-");
			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver_bak/"+ipdir;
			File file=new File(filename);
			if(!file.exists())file.mkdir();
	        String cmd   =   "cmd   /c   copy   "+ResourceCenter.getInstance().getSysPath() + "linuxserver\\" + ipAddress + ".log"+" "+ResourceCenter.getInstance().getSysPath() + "linuxserver_bak\\" +ipdir+"\\"+ ipAddress+"-" +currenttime+ ".log";             
	        //SysLogger.info(cmd);
	        Process   child   =   Runtime.getRuntime().exec(cmd);   
	    }catch (IOException e){    
	        e.printStackTrace();
	    }   
	}
	
    public String getMaxNum(String ipAddress){
    	String maxStr = null;
		File logFolder = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/");
   		String[] fileList = logFolder.list();
   		for(int i=0;i<fileList.length;i++) {//��һ�����µ��ļ�
   			if(!fileList[i].startsWith(ipAddress)) continue;
   			return ipAddress;
   		}
   		return maxStr;
    }
    
    public static void main(String[] args) {
    	LoadInformixFile loadInformixFile = new LoadInformixFile();
//    	Hashtable hash = loadInformixFile.loadInformixBarActLogFile("127.0.0.1");
//    	String[] strs = (String[])hash.get("baractlogs");
//    	for(String str:strs){
//    		System.out.println(str);
//    	}
    	String[] strString = new String[4];
    	strString[0] = "2011-05-20 13:16:51 1396842  4030516 Could not open XBSA library /usr/tivoli/tsm/client/informix/bin/bsashr10.o, so trying default path.";
    	strString[1] = "2011-05-20 13:16:51 1396842  4030516 An unexpected error occurred:  Could not load module /informix/lib/ibsad001_64.o.";
    	strString[2] = "System error: No such file or directory .";
    	strString[3] = "No such file or directory";
    	String[] str = loadInformixFile.arrybaractlogs(strString);
    	for(String s:str){
    		System.out.println(s);
    	}
    }
}
