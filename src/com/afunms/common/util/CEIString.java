/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

/**
 * Title:        ��������ƽ̨
 * Description:
 * �ַ�������� �滻/��ʽ�ж�
 * Copyright:    Copyright (c) 2001
 * Company:      ��̩����
 * @author
 * @version 1.0
 */


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.math.*;

public class CEIString extends Object {

    public static String version = "1.0"; // version

    public CEIString() {
    }

    public String NULL2Empty(String str) {
        if (str == null)
            return "";

        else
            return str;
    }


    /**
     * �滻�ַ��� .
     * <p/>
     * <p>  str:  ԭ�ַ���
     * <p/>
     * <p>  sou:  ���滻�ַ���
     * <p/>
     * <p>  des�� ���滻�ַ���.
     *
     * @return	a string
     */


    public static String replace(String str, String sou, String des) {

        if (str == null)
            return "";


        StringBuffer sb = new StringBuffer(str);
        String s1 = "";
        String s2 = "";

        boolean end = false;
        int index = str.indexOf(sou);
        int index2 = 0;
        int soulen = sou.length();
        String result = "";

        if (index >= 0) {
            while (!end) {
                s1 = str.substring(0, index);
                s2 = str.substring(index + soulen, str.length());
                str = s1 + des + s2;
                index = str.indexOf(sou);
                if (index >= 0)
                    end = false;
                else
                    end = true;

            }
            return str;
        } else
            return str;
    }


    /**
     * �滻�ַ����еĻس���13�������У�10��Ϊ��des
     *
     * @see replace(String str,String sou,String des)
     */

    public static String replaceLineTo(String str, String des) {


        char c = (char) 13;

        String sou = "" + c;

        String result = replace(str, sou, des);

        // c=(char)10;

        // sou=""+c;

        // result=replace(result,sou,des);

        return result;

    }

    /**
     * �滻�ַ����еĻس���13�������У�10��Ϊ��<pre>"<br><br>"</pre>
     *
     * @see replace(String str,String sou,String des)
     */
    public static double round(double v,int scale) {
    	if (scale < 0 || new Double(v).isNaN()){
    		return 0.0;
    	}
    	BigDecimal b = new BigDecimal(Double.toString(v));
    	BigDecimal one = new BigDecimal("1");
    	return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
        //return replaceLineTo(str, "<br>");
    }

    
    public static String replaceLineToBR(String str) {


        return replaceLineTo(str, "<br>");
    }
    
    /**
     * �ж��ַ����Ƿ��� ���ڸ�ʽ
     */
    public static boolean isDate(String str) {
        try {
            java.sql.Date thisDate = java.sql.Date.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * �ж��ַ����Ƿ��� Float��ʽ
     */

    public boolean isFloat(String stringtemp) {
        boolean blreturn = false;
        try {
            float folattemp = new Float(stringtemp).floatValue();
            blreturn = true;
        } catch (Exception e) {
            blreturn = false;
        }
        return blreturn;
    }

    /**
     * �ж��ַ����Ƿ��� ָ������
     */
    public boolean isLength(String str, int len) {
        if (str == null)
            return false;
        else {
            int length = str.length();
            if (length != len)
                return false;
        }
        return true;
    }

    /**
     * �ж��ַ����Ƿ�Ϊ NULL ���㳤��
     */
    public boolean isNull(String str) {
        if (str == null)
            return true;
        else {
            if (str.length() == 0)
                return true;
            else
                return false;
        }
    }

    /**
     * �ж��ַ����Ƿ��� ����
     */
    public boolean isNumber(String stringtemp) {
        if (isFloat(stringtemp)) {
            int index = stringtemp.indexOf(".");
            if (index >= 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    /**
     * �ж��ַ����Ƿ� ��ָ���ĳ�����
     */
    public boolean inLength(String str, int min, int max) {
        if (str == null)
            return false;
        else {
            int length = str.length();
            if (length < min)
                return false;
            else if (length > max)
                return false;

        }
        return true;
    }

    /**
     * �ж��ַ����Ƿ�����ĸ
     */
    public boolean isChar(String str) {
        if (isNull(str))
            return false;
        else {
            int len = str.length();
            char c = ' ';
            for (int i = 0; i < len; i++) {
                c = str.charAt(i);
                if (!(((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))))
                    return false;
            }

        }
        return true;

    }

    /**
     * �ж��ַ����Ƿ��� ���ֻ���ĸ
     */
    public boolean isCharNumber(String str) {
        if (isNull(str))
            return false;
        else {
            int len = str.length();
            char c = ' ';
            for (int i = 0; i < len; i++) {
                c = str.charAt(i);
                if (!(((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9'))))
                    return false;
            }
        }
        return true;
    }

    public int toInt(String str) {
        int result = 0;
        try {
            if (isNull(str))
                return 0;
            else {
                result = (new Integer(str)).intValue();
            }
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    public long toLong(String str) {
        long result = 0;
        try {
            if (isNull(str))
                return 0;
            else {
                result = (new Integer(str)).longValue();
            }
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    public float toFloat(String str) {
        float result = 0;
        try {
            if (isNull(str))
                return (float) 0.0;
            else {
                result = (new Float(str)).floatValue();
            }
            return result;
        } catch (Exception e) {
            return (float) 0.0;
        }
    }

    public String getArrayNameByValue(String arrayName[], String arrayValue[], String aValue) {
        int size = arrayValue.length;
        for (int i = 0; i < size; i++) {
            if (arrayValue[i].equalsIgnoreCase(aValue))
                return arrayName[i];
        }
        return "";
    }

    public boolean inStrings(String domain, String locals[]) {
        if (domain == null)
            return false;
        if (domain.equals(""))
            return false;
        for (int i = 0; i < locals.length; i++) {
            if (domain.equalsIgnoreCase(locals[i])) {
                return true;
            }
        }
        return false;
    }


    public void printVector(Vector v) {
        if (v == null) {
            System.out.println("vector is null !");
            return;
        }
        if (v.size() == 0) {
            System.out.println("vector is null !");
            return;
        }
        for (int i = 0; i < v.size(); i++) {
            System.out.println((String) v.elementAt(i));
        }

    }

    public boolean inVector(String key, Vector v) {
        if (v == null)
            return false;
        if (v.size() == 0)
            return false;
        if (key == null)
            return false;
        String str = "";
        for (int i = 0; i < (v.size() - 1); i++) {
            str = (String) v.elementAt(i);
            if (key.equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    public String[] convertStringToArray(String s, String token) {
        StringTokenizer st = new StringTokenizer(s, token);
        int size = 0;
        String str = "";
        while (st.hasMoreTokens()) {
            str = (String) st.nextElement();
            size++;
        }
        String res[] = new String[size];
        st = new StringTokenizer(s, token);
        int i = 0;
        while (st.hasMoreElements()) {
            res[i] = (String) st.nextElement();
            i++;
        }
        return res;

    }

    public String getDate() {
        return (new Date()).toString();
    }


    public static String native2Unicode(String s) {
        if (s == null) {
            return "";
        }
// add by cg.
        /*
        if (DBProperties.getDrivers().equals("oracle.jdbc.driver.OracleDriver")) {
            return s;
        } else if (s.length() == 0)
            return s;*/
        byte[] buffer = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            buffer[i] = (byte) s.charAt(i);
        }
        return new String(buffer);
    }


    public static String unicode2Native(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
// add by cg.
        /*
        if (DBProperties.getDrivers().equals("oracle.jdbc.driver.OracleDriver")) {
            return s;
        }
*/
        char[] buffer = new char[s.length() * 2];
        char c;
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 0x100) {
                c = s.charAt(i);
                byte[] buf = ("" + c).getBytes();
                //SysLogger.info(buf.length+"====length");
                if(buf.length>1){
                	buffer[j++] = (char) buf[0];
                    buffer[j++] = (char) buf[1];
                }else{
                	buffer[j++] = (char) buf[0];
                	buffer[j++] = (char) buf[0];
                }
                
            } else {
                buffer[j++] = s.charAt(i);
            }
        }
        return new String(buffer, 0, j);
    }

    public static String moneyToWan(double money) {
        //java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.0000");
        java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
        return df.format(money / 10000);
/*
          money=money/100;
          System.out.println(Math.rint(money));
          System.out.println(Math.round(money));
          money=Math.round(money);
          money=money/100;

          //java.text.DecimalFormat nf = new java.text.DecimalFormat("###,##0.0000");

        return (money)+"";
*/
    }

	/** 
	 * strSplit����
	 * @param   strSource Ҫ���ָ���ַ���; delimiter �ָ��    
	 * @return  String[]�͵ķָ�����ַ�����
	 */
	public static String[] strSplit(String strSource,String delimiter)
	{		
		int 	  intPos=0;
		String    str=null;
		Vector 	  vector = new Vector();
		String[]  strRet = new String[1];

		//У���������
		if (strSource == null) return (new String[0]);
		if (delimiter == null) return null;
		if ((strSource.trim()).equals("")) return (new String[0]);
		//
		intPos = strSource.indexOf(delimiter);
		String strTemp = "";
		while (intPos != -1)
		{
			//�ж��Ƿ�Ϊת���
			if(intPos!=0)
			{
				if (strSource.substring(intPos-1,intPos).equals("\\"))
				{
					strTemp = strTemp + strSource.substring(0,intPos-1)+delimiter;
					strSource =strSource.substring(intPos+1);
					intPos = strSource.indexOf(delimiter);
					continue;
				}
			}
			//��ת���
			str = strTemp.equals("")?strSource.substring(0,intPos).trim():strTemp+strSource.substring(0,intPos).trim();
			strSource = strSource.substring(intPos+1);
			vector.addElement(str);
			strTemp= "";
			intPos = strSource.indexOf(delimiter);
		}
		vector.addElement(strSource.trim());

		strRet = new String[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
				strRet[i] = (String)vector.elementAt(i);
			}
		return strRet;
	}	
    
    
    public static void main(String args[]) throws Exception {
        double m = 37850.0;
        double m1 = 3.78771;
        //System.out.println(m);
        //System.out.println(m1+"");
        System.out.println(moneyToWan(m));
    }

    /**
     * �õ���ǰ����
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = timeFormatter.format(new java.util.Date());
        return currentDate;
    }
}
