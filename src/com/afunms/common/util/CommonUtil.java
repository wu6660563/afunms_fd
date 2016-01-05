/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CommonUtil 
{		
    private CommonUtil()
    {
    }
    
	/**
	 * ��һ���ַ�����ʽ��ip��ַת����һ��������������ǷǷ����ݣ��򷵻�0
	 * 
	 * @param ip
	 * @return
	 */
	static public long ip2long(String ip) {
		long result = 0;
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				result = result * 256 + part;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
	
	//0a:0a:98:a0:00:a1 -> 10.10.152.160   161
	static public String[] IPPort2String(String ip, int len){
		String[] s = ip.split(":");
		if( len>0 && s.length>len ){
		
			String retip = ""+Long.parseLong(s[0],16);
			for (int i = 1; i < len; ++i) {
				retip = retip+"."+Long.parseLong(s[i],16);
			}
			String tmp=s[len];
			for( int i=len+1; i<s.length; ++i){
				tmp = tmp+s[i];
			}
			
			String retport = ""+Long.parseLong(tmp,16);
			
			String[] ret = new String[2];
			ret[0] = retip;
			ret[1] = retport;
			return ret;
		}
		
		return new String[0];
		
	}
	
	
	public static String demoChangeStringToHex(final String ip) {
		String returnString = "";
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				System.out.println(part);
				//String tmpHex = Integer.toHexString(part & 0xFF);
				String tmpHex = Integer.toHexString(part);
				if(tmpHex.length()==1){
					tmpHex= "0"+tmpHex;
				}
				returnString = returnString+" "+tmpHex;
				System.out.println(part+"--->"+tmpHex);
				//result = result * 256 + part;
			}
		} catch (Exception e) {
			//result = 0;
		}
		System.out.println(returnString.trim());
		return returnString.trim();
		/*
		
	    int changeLine = 1;
	    String s = "Convert a string to HEX/����ˤ���/���";
	    if (ip != null) {
	        s = ip;
	    }
	    System.out.println(s);
	    for (int i = 0; i < s.length(); i++) {
	        byte[] ba = s.substring(i, i + 1).getBytes();
	        // & 0xFF for preventing minus
	        String tmpHex = Integer.toHexString(ba[0] & 0xFF);
	        System.out.print("0x" + tmpHex.toUpperCase());
	        System.out.print(" ");
	        if (changeLine++ % 8 == 0) {
	            System.out.println("");
	        }
	        // Multiply byte according
	        if (ba.length == 2) {
	            tmpHex = Integer.toHexString(ba[1] & 0xff);
	            System.out.print("0x" + tmpHex.toUpperCase());
	            System.out.print(" ");
	            if (changeLine++ % 8 == 0) {
	                System.out.println("");
	            }
	        }
	    }

	    System.out.println(""); // change line
	    System.out.println(""); // change line
	    */
	}
    public static String getDateAndTime()
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		String currentTime = formatter.format(date);
		return currentTime;
    }
    //����2008-01-18 16:00:00�еģ�20080118�����ʽ������
    public static String getCurrentDate(){  	 
    	String [] strArray = getDateAndTime().split(" ");
    	String dateFormat = strArray[0];
		String [] dateArray = dateFormat.split("-");
		String dateStr = dateArray[0]+dateArray[1]+dateArray[2];
    	return dateStr;
    }
    
	/**
	 * getCurrentTime����
	 * ȡ�� 12:22:22 ������ʽ��ʱ��
	 * @param
	 * @return
	 */
	public static String getCurrentTime()
	{
	    Date today;
	    SimpleDateFormat formatter
    		= new SimpleDateFormat("HH:mm:ss");
	    
	    today = new Date();
	    String returnStr = formatter.format(today);
	    today = null;
	    formatter = null;
	    return returnStr;
	}
	
	  //������ʱ10���ӣ����ڼ������10�����ڵı�����Ϣ
    public static String getLaterTenSecondTime(){		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date1;
		String timeFormat = null;
		try {
			date1 = format1.parse(getDateAndTime());
			long Time=(date1.getTime()/1000)-60;
			date1.setTime(Time*1000);
			String mydate1 = format1.format(date1);
			//System.out.println("mydate1:"+mydate1);
			
			String [] strArray = mydate1.split(" ");
			timeFormat = strArray[1];
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return timeFormat;
    }
    
    /**
     * �ַ���ת����ʮ������ֵ
     * @param bin String ���ǿ�����Ҫת����ʮ�����Ƶ��ַ���
     * @return 
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * ʮ������ת���ַ���
     * @param hex String ʮ������
     * @return String ת������ַ���
     */
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    /** 
     * java�ֽ���ת�ַ��� 
     * @param b 
     * @return 
     */
    public static String byte2hex(byte[] b) { //һ���ֽڵ�����

        // ת��16�����ַ���

        String hs = "";
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            //����ת��ʮ�����Ʊ�ʾ

            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); //ת�ɴ�д

    }

    /**
     * �ַ���תjava�ֽ���
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("���Ȳ���ż��");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // ��λһ�飬��ʾһ���ֽ�,��������ʾ��16�����ַ�������ԭ��һ�������ֽ�

            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }

    public static void main(String[] args) {
//        String content = "����������EDF%&^%#_|~";
//        System.out.println(bin2hex(content));
//        System.out.println(hex2bin(bin2hex(content)));
    	String ipaddress = "192.168.0.1";
    	System.out.println(doip(ipaddress));
    }
    
	public String getDate(String swdate){
		String[] num = swdate.split(":");
		String num1 = Integer.valueOf(num[0],16).toString();
		String num2 = Integer.valueOf(num[1],16).toString();
		String num3 = Integer.valueOf(num[2],16).toString();
		String num4 = Integer.valueOf(num[3],16).toString();
		String num5 = Integer.valueOf(num[4],16).toString();
		String num6 = Integer.valueOf(num[5],16).toString();
		String num7 = Integer.valueOf(num[6],16).toString();
		String num8 = Integer.valueOf(num[7],16).toString();
		String swyear = Integer.parseInt(num1)*256+Integer.parseInt(num2)+"";
		String swnewdate = swyear+"-"+num3+"-"+num4+" "+num5+":"+num6+":"+num7+":"+num8;
		return swnewdate;
		
	}
   
	/**
	 * ����key���õ�hash�е�value  ��key�����ڣ��򷵻�defaultValue
	 * @param hash 
	 * @param key 
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(Hashtable hash,String key,String defaultValue){
		if(hash == null){
			return defaultValue;
		}
		if(hash.containsKey(key)){
			return String.valueOf(hash.get(key));
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * �Զ˿�������Ϣ��������
	 * <p>���磺 ���� ����    ״̬ ���� ���</p>
	 * <p>     [23, eth0, , 1, 2144, 20256]</P>
	 * @param ifvector  
	 * @param orderflag  Ҫ������ֶ�
	 * @param sorttype   ���������  ������߽���
	 * @param netInterfaceItem  key
	 * @author HONGLI
	 * @return
	 */ 
	public static Vector<String[]> sortInterface(Vector<String[]> ifvector, String orderflag, String sorttype, String[] netInterfaceItem){

		for(int i=0; i < ifvector.size(); i++){
			for(int j=i+1;j<ifvector.size();j++){
				String[] strs_1 = (String[])ifvector.get(i);
				String[] strs_2 = (String[])ifvector.get(j);
				for(int m=0; m<netInterfaceItem.length; m++){
					if(orderflag.equalsIgnoreCase(netInterfaceItem[m])){
						String str1 = getNumStrFromString(strs_1[m]);
						String str2 = getNumStrFromString(strs_2[m]);
						double d1 = Double.parseDouble(str1);
						double d2 = Double.parseDouble(str2);
						if(sorttype.equalsIgnoreCase("asc")){//���� ��С����
							if(d1 > d2){
								ifvector.add(j, strs_1);
								ifvector.remove(j+1);
								ifvector.add(i, strs_2);
								ifvector.remove(i+1);
							}
						}else{//"desc" ���� �Ӵ�С
							if(d1 < d2){
								ifvector.add(j, strs_1);
								ifvector.remove(j+1);
								ifvector.add(i, strs_2);
								ifvector.remove(i+1);
							}
						}
					}
				}
			}
		}
		return ifvector;
	}
	
	/**
	 * �Զ˿�������Ϣ��������
	 * <p>���磺 ���� ����    ״̬ ���� ���</p>
	 * <p>     [23, eth0, , 1, 2144, 20256]</P>
	 * @param ifvector  
	 * @param orderflag  Ҫ������ֶ�
	 * @param sorttype   ���������  ������߽���
	 * @param netInterfaceItem  key
	 * @author HONGLI
	 * @return
	 */
	public static List<Hashtable> sortInterfaceList(List<Hashtable> netflowList, String orderflag, String sorttype, String[] netInterfaceItem){
		for(int i=0; i < netflowList.size(); i++){
			for(int j=i+1;j<netflowList.size();j++){
				Hashtable hash_1 = (Hashtable)netflowList.get(i);
				Hashtable hash_2 = (Hashtable)netflowList.get(j);
				for(int m=0; m<netInterfaceItem.length; m++){
					if(orderflag.equalsIgnoreCase(netInterfaceItem[m])){
						String str1 = getNumStrFromString((String)hash_1.get(orderflag));
						String str2 = getNumStrFromString((String)hash_2.get(orderflag));
						double d1 = Double.parseDouble(str1);
						double d2 = Double.parseDouble(str2);
						if(sorttype.equalsIgnoreCase("asc")){//���� ��С����
							if(d1 > d2){
								netflowList.add(j, hash_1);
								netflowList.remove(j+1);
								netflowList.add(i, hash_2);
								netflowList.remove(i+1);
							}
						}else{//"desc" ���� �Ӵ�С
							if(d1 < d2){
								netflowList.add(j, hash_1);
								netflowList.remove(j+1);
								netflowList.add(i, hash_2);
								netflowList.remove(i+1);
							}
						}
					}
				}
			}
		}
		return netflowList;
	}
	
	/**
	 * <p>��Hashtableת����HashMap ����</p>
	 * @param data 
	 * @return 
	 */
	public static HashMap converHashTableToHashMap(Hashtable data){
		if(data == null){
			return null;
		}
		HashMap retHashMap = new HashMap();
		Iterator iter = data.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String key = String.valueOf(entry.getKey()); 
		    String value = String.valueOf(entry.getValue()); 
		    retHashMap.put(key, value);
		}
		return retHashMap;
	}
	
	public static String doip(String ip){
//		  String newip="";
//		  for(int i=0;i<3;i++){
//			int p=ip.indexOf(".");
//			newip+=ip.substring(0,p);
//			ip=ip.substring(p+1);
//		  }
//		 newip+=ip;
		ip = ip.replaceAll("\\.", "_").trim();
		 //System.out.println("newip="+newip);
		 return ip;
	}
	
	/**
	 * ��ȡ�ַ����е����� �磺��1012eKb/��aaa��    ���ȡ����1012��
	 * @return
	 */
	public static String getNumStrFromString(String str){
//		 String str = "1012eKb/��aaa";
		 Pattern pattern = Pattern.compile("\\d");
		 Matcher matcher = pattern.matcher(str);
		 StringBuffer numStr = new StringBuffer();
		 while(matcher.find()){
			numStr.append(matcher.group(0));
		 }
//		 System.out.println(numStr);
		return numStr.toString();
	}
	
	/**
	 * ������ʽ���˵�����(�����ַ�)
	 * @author HONGLI
	 * @param str  ��Ҫ���˵��ַ���
	 * @return
	 */
	public static String removeIllegalStr(String str){
		if(str == null){
			return "";
		}
		Pattern pattern = Pattern.compile("\\p{ASCII}");
		Matcher matcher = pattern.matcher(str);
		String numStr = "";
		while(matcher.find()){
			numStr = numStr + matcher.group(0);
		}
		return numStr;
	}
	
	/**
	 * ʹ���ַ������˵�����(�������ķ����ַ�)   ���ַ����е������滻Ϊ��-��
	 * @author HONGLI
	 * @param charsetName �ַ��� ������Ϊ��GB2312��  
	 * @param str         ��Ҫת���������ַ���
	 * @return
	 */
	public static String removeIllegalStr(String charsetName, String str){
		if(str == null || str.equals("")){
			return "";
		}
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			boolean b = Charset.forName(charsetName).newEncoder().canEncode(c);
			if(b){
				sBuffer.append(c);
			}else{
				sBuffer.append("-");
			}
		}
		return sBuffer.toString();
	}
	
}
