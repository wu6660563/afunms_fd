/*
 * @(#)SerialObejctUtil.java     v1.01, Jul 10, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.afunms.common.util.SysLogger;

/**
 * 
 * ClassName:   SerializeUtil.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 26, 2013 3:53:49 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SerializeUtil {
	
    private static SysLogger logger = SysLogger.getLogger(SerializeUtil.class);

    /**
     * deserialize:
     * <p>反序列化对象
     *
     * @param fileName
     * @param delete
     * @return
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public static Object deserialize(String fileName, boolean delete){
		//反序列化
		ObjectInputStream in = null;
		Object obj = null;
		try {
			in = new ObjectInputStream(
					new FileInputStream(fileName));
			obj = (Object) in.readObject();
		} catch (FileNotFoundException e) {
		    logger.info("reserialObject-->文件没找到:fileName:"+fileName);
			obj = null;
		} catch (IOException e) {
		    logger.error("反序列化 出错：IO 读取错误:" + fileName, e);
		} catch (ClassNotFoundException e) {
		    logger.error("反序列化 出错：类未找到:" + fileName, e);
		} finally {
			try {
				if (in != null) {
				    in.close();
				}
				if (delete) {
				    File file = new File(fileName);
	                if (file.exists()) {
	                    file.delete();
	                }
				}
			} catch (IOException e) {
			    logger.error("反序列化 出错：IO关闭错误:" + fileName, e);
			}
		}
		return obj;
	}
	
	
	/**
	 * serialize:
	 * <p>序列化对象
	 *
	 * @param obj
	 * @param fileName
	 * @return
	 *
	 * @since   v1.01
	 */
	@SuppressWarnings("static-access")
    public static boolean serialize(Object obj, String fileName) {
		boolean flag = false;
		// 序列化
		ObjectOutputStream out = null;
		try {
		    File file = new File(fileName);
		    if (file.exists()) {
		        file.delete();
		    }
		    file.createNewFile();
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(obj);
			out.flush();
			flag = true;
		} catch (FileNotFoundException e) {
		    logger.error("序列化 出错：reserialObject-->文件没找到:fileName:" + fileName, e);
			flag = false;
		} catch (IOException e) {
		    logger.error("序列化 出错：IO 写入错误:" + fileName, e);
		    flag = false;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			    logger.error("序列化 出错：关闭IO错误:" + fileName, e);
			}
		}
		return flag;
	}
	
}

