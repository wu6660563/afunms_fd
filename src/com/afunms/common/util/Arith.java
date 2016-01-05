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


import java.math.BigDecimal;

/**

* ����Java�ļ����Ͳ��ܹ���ȷ�ĶԸ������������㣬����������ṩ��

* ȷ�ĸ��������㣬�����Ӽ��˳����������롣

*/

public class Arith{
    //Ĭ�ϳ������㾫��

    private static final int DEF_DIV_SCALE = 2;
    //����಻��ʵ����

    private Arith(){

    }
    /**

    * �ṩ��ȷ�ļӷ����㡣

    * @param v1 ������

    * @param v2 ����

    * @return ���������ĺ�

    */

    public static double add(double v1,double v2){

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();

    }



    /**

    * �ṩ��ȷ�ļ������㡣

    * @param v1 ������

    * @param v2 ����

    * @return ���������Ĳ�

    */

    public static double sub(double v1,double v2){

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }



    /**

    * �ṩ��ȷ�ĳ˷����㡣

    * @param v1 ������

    * @param v2 ����

    * @return ���������Ļ�

    */

    public static double mul(double v1,double v2){

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();

    }



    /**

    * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��

    * С�����Ժ�10λ���Ժ�������������롣

    * @param v1 ������

    * @param v2 ����

    * @return ������������

    */

    public static double div(double v1,double v2){

        return div(v1,v2,DEF_DIV_SCALE);

    }



    /**

    * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ

    * �����ȣ��Ժ�������������롣

    * @param v1 ������

    * @param v2 ����

    * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��

    * @return ������������

    */

    public static double div(double v1,double v2,int scale){

        if(scale<0){

            throw new IllegalArgumentException(

                "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();

    }



    /**

    * �ṩ��ȷ��С��λ�������봦��

    * @param v ��Ҫ�������������

    * @param scale С���������λ

    * @return ���������Ľ��

    */

    public static double round(double v,int scale){

        if(scale<0){

            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }

        BigDecimal b = new BigDecimal(Double.toString(v));

        BigDecimal one = new BigDecimal("1");

        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();

    }
    
    /**
	 * @author zhubinhua
	 * @param str     ��Ҫ��ʽ�����ַ���
	 * @param n       ����С������
	 * @param flag    �Ƿ������������
	 * 
	 */
	public static String floatToStr(String str, int n, int flag) {
		String result = null;
		
		float f = Float.parseFloat(str);
		float temp = new Double(Math.pow(10, n)).floatValue();
		
		if(flag == 1) {  //��������
			f = (float)(Math.round(f * temp) / temp);
		} else if(flag == 0) {
			f = (float)(Math.floor(f * temp) / temp);
		}
		
		result = String.valueOf(f);
		
		//��0������С��λ��
		int count = result.length() - result.indexOf(".") - 1;
		if(count < n) {
			for(int i=0; i<n-count; i++) {
				result = result + "0";
			}
		}
		
		return result;
	}

};
