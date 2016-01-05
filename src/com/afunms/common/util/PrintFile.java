/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;


public class PrintFile {
	public SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * @param f
	 *            �ļ�·��
	 * @param ht
	 *            ����ļ���СС��1k���ļ�
	 * @param allfile
	 *            �ڸ�·���µ������ļ�
	 */
	public void funFile(File f, Hashtable ht, Hashtable allfile, String ymstr,int size) {
		File[] codefile = f.listFiles();
		if (codefile != null && codefile.length != 0) {
			for (int i = 0; i < codefile.length; i++) {
				String fileName = codefile[i].getName();
				//if (!codefile[i].isDirectory() && fileName.startsWith(ymstr)) {// ���������׺��Ϊ.log���ļ�,����Ҫ��ȥ��
					int temp = (int) Math.ceil((codefile[i].length() + 0.0) / 1024);
					int k = allfile.size();
					allfile.put(k, fileName);
					if (temp < size) {
						int j = ht.size();
						ht.put(j++, fileName);
					}
				//}
			}
		} else {
			ht.put(0, "NOELEMENT");
		}

	}


	
	/**
	 * 
	 * @param file
	 *            �ļ�·��
	 * @param newFile
	 *            �����ɵ��������ļ�
	 * @param ht
	 *            ���������ļ��������ļ��Ĵ�СС��1k
	 */
	public void checkNewFile(String path, Hashtable newFile, Hashtable ht,String endtime,int inter,int size) {
		int times = 60/inter;
		int t=0;
		for(int i=0;i<times;i++){
			String endname="";
			if(t==0){
				endname = "00.dat";
			}else{
				if((t)*inter == 60) break;
				endname = (t)*inter+".dat";
			}
			File tempfile = new File(path+endtime+endname);
			System.out.println(path+endtime+endname+"========"+tempfile.exists());
			if(tempfile.exists()){
				int k = newFile.size();
				newFile.put(k, tempfile.getName());
				int temp = (int) Math
					.ceil((tempfile.length() + 0.0) / 1024);
				if (temp < size) {
					k = ht.size();
					ht.put(k, tempfile.getName());
				}
			}else{
					//�澯
			}
			t=t+1;
		}

	}
	
	/**
	 * 
	 * @param file
	 *            �ļ�·��
	 * @param newFile
	 *            �����ɵ��������ļ�
	 * @param ht
	 *            ���������ļ��������ļ��Ĵ�СС��1k
	 */
	public int checkNewFile(String path, Hashtable newFile, Hashtable ht,String onetime,String twotime,int inter,int size) {
		int flag = 0;
		int times = 24/inter;
		int t=0;
		String onetime_2 = onetime.substring(6);
		String twotime_2 = twotime.substring(6);
		System.out.println(onetime+"========="+onetime_2);
		System.out.println(twotime+"========="+twotime_2);
		if(onetime_2.equalsIgnoreCase("00")){
			//��ʼ����ļ�
			flag = 1;
			File tempfile = new File(path+"/"+onetime+".000");
			if(tempfile.exists()){
				int k = newFile.size();
				newFile.put(k, tempfile.getName());
				int temp = (int) Math.ceil((tempfile.length() + 0.0) / 1024);
				if (temp < size) {
					k = ht.size();
					ht.put(k, tempfile.getName());
					
				}
			}
		}else if(twotime_2.equalsIgnoreCase("00")){
			//��ʼ����ļ�
			flag = 2;
			File tempfile = new File(path+"/"+twotime+".000");
			if(tempfile.exists()){
				int k = newFile.size();
				newFile.put(k, tempfile.getName());
				int temp = (int) Math.ceil((tempfile.length() + 0.0) / 1024);
				if (temp < size) {
					k = ht.size();
					ht.put(k, tempfile.getName());
					
				}
			}
			
		}else{
			for(int i=0;i<times;i++){
				if(i*inter==Integer.parseInt(onetime_2)){
					//��ʼ����ļ�
					flag = 1;
					File tempfile = new File(path+"/"+onetime+".000");
					if(tempfile.exists()){
						int k = newFile.size();
						newFile.put(k, tempfile.getName());
						int temp = (int) Math.ceil((tempfile.length() + 0.0) / 1024);
						if (temp < size) {
							k = ht.size();
							ht.put(k, tempfile.getName());
							
						}
					}
				}else if(i*inter==Integer.parseInt(twotime_2)){
					//��ʼ����ļ�
					flag = 2;
					File tempfile = new File(path+"/"+twotime+".000");
					if(tempfile.exists()){
						int k = newFile.size();
						newFile.put(k, tempfile.getName());
						int temp = (int) Math.ceil((tempfile.length() + 0.0) / 1024);
						if (temp < size) {
							k = ht.size();
							ht.put(k, tempfile.getName());
							
						}
					}
				}
			}
			
		}
		return flag;

	}

	public SimpleDateFormat getSDF(String fmtStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmtStr);
		return sdf;
	}

	public long getLong(String str,String fmtStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmtStr);
		long time = 0;
		try {
			time = sdf.parse(str.trim().toString()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
}
