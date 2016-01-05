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
	 * filename是传入的文件名称
	 * compareWords是需要比较的字符 判断这个文件里是否包含当前的字符
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
	 * lines里是包含了一行或者多行的语句。 
	 * 若flag=0,则判断文件了是否包含这些lines里的任何一行。
	 * 若flag=1,则判断文件了是否包含这些lines里的所有行。并且这些行都是在一起出现的。
	 * 若flag=2，则判断文件是否包含所有行，但是不必一起出现
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
				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlag为true时，下一个flag为或运算，则直接跳过
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
				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlag为true时，下一个flag为或运算，则直接跳过
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
	 * 第一个是contentSimpleLines(File filename, List<String> lines, int flag)
	 * 修改的部分是flag的参数项0.包含所有行 1.不包含任何行 2.应该包含集合 3.不包含集合
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
		
		if(flag == 0){//0.包含所有行
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
		}else if(flag == 1){//1.不包含任何行
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
		}else if(flag == 2){//2.应该包含集合
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
		}else{// 3.不包含集合
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
	 * 关闭资源
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
