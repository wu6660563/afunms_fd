package com.afunms.application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CompareRuleHelper {

	public static void main(String[] args) {
		CompareRuleHelper bc = new CompareRuleHelper();
		File f = new File("D:/172.25.25.240_20101231-17-43cfg.cfg");
		boolean b = false;
		
		String[] compareWords = {
				"service timestamps log datetime"
				};
//		String[] compareWords = {" ip address 200.10.11.255 255.255.255.255","service timestamps debug uptime","no service password-encryption","hostname 2621"};
//		String[] compareWords = {" ip address 200.10.11.255 255.255.255.255a","service timestamps debug uptime","no service password-encryptiona","hostname 2621"};
		int[] flag = {-1};
		b = bc.contentSimpleWords(f, compareWords, flag);
		System.out.println(b);
		
//		List<String> lines = new ArrayList<String>();
//		lines.add("(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])");
//		lines.add("username\\s.+\\sprivilege 15 password 0 liwei");
//		b = bc.contentSimpleLines(f, lines, 4);
//		System.out.println(b);
	}

	/**
	 * @param args
	 * filename�Ǵ�����ļ�����
	 * compareWords����Ҫ�Ƚϵ��ַ� �ж�����ļ����Ƿ������ǰ���ַ�
	 */
	public boolean contentSimpleWords(File filename, String compareWords) {
		if (null == filename || null == compareWords || "".equals(compareWords)) {
			return false;
		}

		FileReader fr = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String lineStr = "";
			Pattern p = Pattern.compile(compareWords);
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					flag = true;
					break;
				}
			}
			return flag;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(br, fr);
		}
		return false;
	}
	
	/**
	 * lines���ǰ�����һ�л��߶��е���䡣 
	 * ��flag=0,���ж��ļ����Ƿ������Щlines����κ�һ�С�
	 * ��flag=1,���ж��ļ����Ƿ������Щlines��������С�������Щ�ж�����һ����ֵġ�
	 * ��flag=2�����ж��ļ��Ƿ���������У����ǲ���һ�����
	 * @param filename
	 * @param lines
	 * @param flag
	 * @return
	 */
	public boolean contentSimpleWords(File filename, String[] compareWords, int[] flag){
		if (null == filename || null == compareWords || null == flag) {
			return false;
		}
		if(compareWords.length != flag.length)return false;
		
		try {
			boolean bFlag = contentSimpleWords(filename,compareWords[0]);
			for (int i = 1; i < flag.length; i++) {
				boolean cFlag = contentSimpleWords(filename, compareWords[i]);
				if (flag[i] == 0) {
					bFlag = bFlag || cFlag;
				}else if (flag[i] == 1) {
					bFlag = bFlag && cFlag;
				}
				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlagΪtrueʱ����һ��flagΪ�����㣬��ֱ������
						i++;
				}
			}
			return bFlag;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	public boolean contentSimpleWords(File filename, String[] compareWords, int[] flag, boolean[] flag1){
		if (null == filename || null == compareWords || null == flag || null == flag1) {
			return false;
		}
		if(compareWords.length != flag.length || flag.length != flag1.length)return false;
		
		try {
			boolean bFlag = contentSimpleWords(filename,compareWords[0]);
			if(!flag1[0])bFlag = !bFlag; 
			for (int i = 1; i < flag.length; i++) {
				boolean cFlag = contentSimpleWords(filename, compareWords[i]);
				if (!flag1[i]) {
					cFlag = !cFlag;
				}
				if (flag[i] == 0) {
					bFlag = bFlag || cFlag;
				}else if (flag[i] == 1) {
					bFlag = bFlag && cFlag;
				}
				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlagΪtrueʱ����һ��flagΪ�����㣬��ֱ������
						i++;
				}
			}
			return bFlag;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;		
	}
	/**
	 * ��һ����contentSimpleLines(File filename, List<String> lines, int flag)
	 * �޸ĵĲ�����flag�Ĳ�����0.���������� 1.�������κ��� 2.Ӧ�ð������� 3.����������
	 * @param filename
	 * @param lines
	 * @param flag
	 * @return
	 */
	public boolean contentSimpleLines(File filename, List<String> lines, int flag) {
		if (null == filename || null == lines || (flag > 3 && flag < 0)) {
			return false;
		}

		FileReader fr = null;
		BufferedReader br = null;
		boolean bFlag = false;
		String lineStr = "";

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if(flag == 0){//0.����������
			try {
				for (int i = 0; i < lines.size(); i++) {
					br.mark(10000);
					while (null != (lineStr = br.readLine())) {
						if (lineStr.length() == 0)
							continue;
						
						bFlag = lineStr.matches(lines.get(i));
						if (bFlag) {
							br.reset();
							break;
						}
					}
					if(!bFlag){
						return false;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return true;
		}else if(flag == 1){//1.�������κ���
			try {
				while (null != (lineStr = br.readLine())) {
					if (lineStr.length() == 0)
						continue;

					for (int i = 0; i < lines.size(); i++) {
						bFlag = lineStr.matches((String) lines.get(i));
						if (bFlag) {
							return false;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return true;			
		}else if(flag == 2){//2.Ӧ�ð�������
			StringBuffer sb1 = new StringBuffer();
			if (lines.size() >= 1) {
				Iterator<String> iter = lines.iterator();
				while (iter.hasNext()) {
					sb1.append(iter.next()).append("\r\n");
				}
			}
			if (sb1.length() == 0)
				return false;

			try {
				StringBuffer sb2 = new StringBuffer();
				while (null != (lineStr = br.readLine())) {
					sb2.append(lineStr).append("\r\n");
				}
				Pattern p = Pattern.compile(sb1.toString());

				Matcher m = p.matcher(sb2);
				if (m.find()) {
					bFlag = true;
				}
				return bFlag;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return bFlag;
		}else{// 3.����������
			StringBuffer sb1 = new StringBuffer();
			if (lines.size() >= 1) {
				Iterator<String> iter = lines.iterator();
				while (iter.hasNext()) {
					sb1.append(iter.next()).append("\r\n");
				}
			}
			if (sb1.length() == 0)
				return false;

			try {
				StringBuffer sb2 = new StringBuffer();
				while (null != (lineStr = br.readLine())) {
					sb2.append(lineStr).append("\r\n");
				}
				Pattern p = Pattern.compile(sb1.toString());

				Matcher m = p.matcher(sb2);
				if (!m.find()) {
					bFlag = true;
				}
				return bFlag;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return bFlag;
		}
	}
	
	/**
	 * �ر���Դ
	 * @param br
	 * @param fr
	 */
	void close(BufferedReader br, FileReader fr) {
		if (br != null) {
			try {
				br.close();
				br = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fr != null) {
			try {
				fr.close();
				fr = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}}
