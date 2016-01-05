package com.afunms.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.config.dao.HaweitelnetconfDao;
import com.afunms.config.model.Huaweitelnetconf;

/**
 * 
 * telnet ��ȡ�����ļ���vpn������Ϣ������
 * 
 * @author konglq
 * 
 */
public class Huawei3comtelnetUtil {

	public static Hashtable telnetconf = new Hashtable();

	/**
	 * 
	 * ��ʼ��telnet���ӵ��ڴ��б� ������Ԫ��id��Ϊkey�����ݿ�����Ϊֵ
	 * 
	 */
	public static void inittelnetlist() 
	{
		HaweitelnetconfDao dao = new HaweitelnetconfDao();
		// Huaweitelnetconf mo=new Huaweitelnetconf();

		List list = new ArrayList();
		try{
			list = dao.loadEnableVpn();
		}catch(Exception e){
			
		}finally{
			dao.close();
		}
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				Huaweitelnetconf mo = new Huaweitelnetconf();
				mo = (Huaweitelnetconf) list.get(i);
				telnetconf.put(mo.getId(), mo);
			}
		}
		//dao.close();
	}

	private static String readfile(String filename) 
	{
		StringBuffer buffer = new StringBuffer();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));

			String cc = br.readLine();

			while (cc != null) {
				buffer.append(cc);
				buffer.append("\r\n");
				cc = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// �ļ�������Ϊ

		//System.out.println(buffer.toString());
		return buffer.toString();
	}

	/**
	 * ��ȡ�����ļ���������vpn�Ķ˿� ʹ�ýӿڵ�����Ϊkey ��vpn��������Ϊvalue
	 * 
	 * @param conf
	 *            �ɼ��������ַ���
	 * @return ����vpn�������б�
	 */
	public static Hashtable Getvpnlist(String conf) 
	{
		Hashtable list = new Hashtable();
		// System.out.println("======================="+conf);
		if (null != conf) {
			// ���������ļ�
			String[] datelist = conf.split("\r\n");
			//System.out.println(datelist.length);
			//
			if (null != datelist && datelist.length > 0) 
			{

				boolean flgvpn = false;// �ҵ��˿ڵı��

				String infname = "";

				for (int i = 0; i < datelist.length; i++) 
				{

					// System.out.println("=&&&&====="+datelist[i]);
					// ���ҵ����ڣ�Ȼ����ݶ˿����ҵ���Ӧ�����ã����°Ѷ˿ڱ������Ϊfalse
					String inf = datelist[i].trim();
					if (inf.indexOf("interface") == 0) 
					{
						// System.out.println("---"+i);
						infname = inf.replaceAll("interface", "").trim();
						flgvpn = true;
					}
					// ���Ҷ˿ڵ�vpn������Ϣ
					if (flgvpn && inf.indexOf("interface") == -1) {// �ӿ�
						if (inf.indexOf("ip binding vpn-instance") >= 0) 
						{
							list.put(infname, inf.replace("ip binding vpn-instance", "").trim());
							flgvpn = false;
						}
					}
				}// ѭ������
			}
		}
		return list;
	}
	public static void main(String[] ars) {
		// Huawei3comtelnetUtil
		String ss = Huawei3comtelnetUtil.readfile("d://NE_08config");
		Huawei3comtelnetUtil.Getvpnlist(ss);
	}
}
