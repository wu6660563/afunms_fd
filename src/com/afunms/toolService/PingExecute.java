package com.afunms.toolService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class PingExecute {

	private List<String> resulttemp = new ArrayList<String>();
	private static Map<String, List<String>> map = new HashMap<String, List<String>>();
	private String res = null;
	private static Hashtable<String, Process> protable=new Hashtable<String, Process>();
	@SuppressWarnings("static-access")
	public synchronized List<String> getResult() {
		return resulttemp;
	}

	public void executePing(String order, String ip, String id) {
		List<String> overflag = new ArrayList<String>();
		overflag.add("true");
		overflag.set(0, "false");
		map.put(ip + id + "over", overflag);
		getResult().clear();
		Runtime t = Runtime.getRuntime();
		InputStream in = null;
		InputStreamReader r = null;
		BufferedReader re = null;
		try {
			Process p = t.exec(order);
			protable.put(ip+id, p);
			in = p.getInputStream();
			r = new InputStreamReader(in);
			re = new BufferedReader(r);
			res = null;
			while (true) {
				res = re.readLine();
				
				//if(res.trim().length()==0)continue;
				////System.out.println(res);
				if (res == null) {
					overflag.set(0, "true");
					map.put(ip + id + "over", overflag);
					break;
				}
				getResult().add(res);
				map.put(ip + id, getResult());
			}
//			System.out.println("======================>"+protable.get(ip+id));
			if(protable.get(ip+id)!=null){
				protable.get(ip+id).destroy();
				protable.remove(ip+id);
			}
			if (in != null) {
				in.close();
			}
			if (re != null) {
				re.close();
			}
			if (r != null) {
				r.close();
			}

		} catch (IOException e) {
		} finally {
			//return result;
		}
	}

	List<String> result = new ArrayList<String>();

	public List<String> readResult(String ip, String id) {
		//System.out.println("session id==="+id);
		//System.out.println("׼��������!");
		if (map.get(ip + id) != null) {
			//System.out.println("MAP��������,����SIZEΪ"+map.get(ip+id).size()+"!");
			try {
				if (map.get(ip + id + "over").get(0).equals("true")) {
					//System.out.println("PING����ִ�����!");
					if (map.get(ip + id).size() == 0) {
						//System.out.println("MAP��ȡ���!");
						map.get(ip + id).remove(ip + id);
						map.get(ip + id).remove(ip + id + "over");
						//System.out.println("ɾ��MAP�еĶ���!");	 
						return null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			//System.out.println("��ʼ��ȡ����!");
			result.clear();
			result.addAll(map.get(ip + id));
			map.get(ip + id).clear();
			return result;
		} else {
			//System.out.println("PING����,���Զ�����!");
			return null;
		}
	}

	public String getRes() {
		return res;
	}
	public void closePing(String ip, String id){
		Process p=protable.get(ip+id);
//		System.out.println("---------tracert----�ر��߳�==>"+p);
		if(protable.get(ip+id)!=null){
			protable.get(ip+id).destroy();
			protable.remove(ip+id);
		}
		if(p!=null){
//			System.out.println("----tracert-------�رճɹ�------------------");
			p.destroy();
		}
	}
}
