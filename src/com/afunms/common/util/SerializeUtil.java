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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 26, 2013 3:53:49 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SerializeUtil {
	
    private static SysLogger logger = SysLogger.getLogger(SerializeUtil.class);

    /**
     * deserialize:
     * <p>�����л�����
     *
     * @param fileName
     * @param delete
     * @return
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public static Object deserialize(String fileName, boolean delete){
		//�����л�
		ObjectInputStream in = null;
		Object obj = null;
		try {
			in = new ObjectInputStream(
					new FileInputStream(fileName));
			obj = (Object) in.readObject();
		} catch (FileNotFoundException e) {
		    logger.info("reserialObject-->�ļ�û�ҵ�:fileName:"+fileName);
			obj = null;
		} catch (IOException e) {
		    logger.error("�����л� ����IO ��ȡ����:" + fileName, e);
		} catch (ClassNotFoundException e) {
		    logger.error("�����л� ������δ�ҵ�:" + fileName, e);
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
			    logger.error("�����л� ����IO�رմ���:" + fileName, e);
			}
		}
		return obj;
	}
	
	
	/**
	 * serialize:
	 * <p>���л�����
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
		// ���л�
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
		    logger.error("���л� ����reserialObject-->�ļ�û�ҵ�:fileName:" + fileName, e);
			flag = false;
		} catch (IOException e) {
		    logger.error("���л� ����IO д�����:" + fileName, e);
		    flag = false;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			    logger.error("���л� �����ر�IO����:" + fileName, e);
			}
		}
		return flag;
	}
	
}

