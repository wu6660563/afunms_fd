/*
 * @(#)SerialObejctUtil.java     v1.01, Jul 10, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherdb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.afunms.common.util.SysLogger;

/**
 * 
 * ClassName:   SerialObejctUtil.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 26, 2013 3:53:49 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SerialObejctUtil {
	
	
	public static Object reserialObject(String fileName){
		//反序列化
		ObjectInputStream in = null;
		Object obj = null;
		try {
//			String path = ResourceCenter.getInstance().getSysPath();
			in = new ObjectInputStream(
					new FileInputStream(fileName));
			obj = (Object) in.readObject();
		} catch (FileNotFoundException e) {
			SysLogger.info("reserialObject-->文件没找到:fileName:"+fileName);
			obj = null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	
	public static boolean serialObject(Object obj, String fileName) {
		boolean flag = false;
		// 序列化
		ObjectOutputStream out = null;
//		String path = ResourceCenter.getInstance().getSysPath();
		String path = "E:/third-tomcat/filePath";
		try {
			out = new ObjectOutputStream(new FileOutputStream(
					path + "/" + fileName));
			out.writeObject(obj);
			out.flush();
			flag = true;
		} catch (FileNotFoundException e) {
			SysLogger.info("reserialObject-->文件没找到:fileName:"+fileName);
			flag = false;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	
}

