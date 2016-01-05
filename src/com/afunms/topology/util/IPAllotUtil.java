/*
 * @(#)IPAllotUtil.java     v1.01, Aug 22, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.polling.om.IpMac;
import com.afunms.topology.dao.IpMacDao;

/**
 * 
 * ClassName: IPAllotUtil.java
 * <p>
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Aug 22, 2013 11:31:24 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class IPAllotUtil {

	// keyΪipǰ��λ��valueΪ��IP�ε�����ip
	public Map<String, List<String>> sort() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<IpMac> ipList = null;
		IpMacDao dao = new IpMacDao();
		try {
			ipList = dao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		if (ipList != null && ipList.size() > 0) {
			for (int i = 0; i < ipList.size(); i++) {
				IpMac ipalias = (IpMac) ipList.get(i);
				String str = ipalias.getIpaddress().trim();
				if (str != null && !"".equals(str)
						&& !"null".equalsIgnoreCase(str)) {
					int k = str.lastIndexOf("."); // �õ����һ�� ��.�� ������
					String aa = str.substring(0, k).trim();
					if (map.containsKey(aa)) {
						List<String> bb = map.get(aa);
						
						boolean flag = false;
						for (int j = 0; j < bb.size(); j++) {
							if(bb.get(j).trim().equals(str)) {
								flag = true;
								break;
							}
						}
						if(!flag) {
							bb.add(str);
						}
						
						
					} else {
						List<String> c = new ArrayList<String>();
						c.add(str);
						map.put(aa, c);
					}
				}
			}
		}
		
		
		return map;
	}

	/*
	 * ip��������
	 * 
	 * ����ip����Ķ������ô˷�������
	 */
	public String[] ipsort(String[] ip) {
		// ��ip�����Ϊ�ȳ�����
		for (int i = 0; i < ip.length; i++) {
			String[] temp = ip[i].split("\\.");
			ip[i] = "";
			for (int j = 0; j < temp.length; j++) {
				if (Integer.parseInt(temp[j]) / 10 == 0) { // ����ip��Ϊ0-9ʱ��ǰ�油��00��
					temp[j] = "00" + temp[j];
				} else if (Integer.parseInt(temp[j]) / 100 == 0) { // ����ip��Ϊ10-99ʱ��ǰ�油��0��
					temp[j] = "0" + temp[j];
				}
				ip[i] += temp[j] + "."; // ������װ����
			}
			ip[i] = ip[i].substring(0, ip[i].length() - 1); // ȥ�����һ������ġ�.��
		}

		Arrays.sort(ip);// ��������

		// ��ԭip
		for (int i = 0; i < ip.length; i++) {
			String[] temp = ip[i].split("\\.");
			ip[i] = "";
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].startsWith("00")) { // ȥ��ǰ���"00"
					temp[j] = temp[j].substring(2);
				} else if (temp[j].startsWith("0")) { // ȥ��ǰ���"0"
					temp[j] = temp[j].substring(1);
				}
				ip[i] += temp[j] + ".";
			}
			ip[i] = ip[i].substring(0, ip[i].length() - 1);
		}
		return ip;
	}

}
