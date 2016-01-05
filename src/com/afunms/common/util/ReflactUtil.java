package com.afunms.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author HONGLI  Feb 19, 2011
 * ���乤����
 *
 */
public class ReflactUtil {
	
	/**  
	    * java����bean��get����  
	    *   
	    * @param objectClass  
	    * @param fieldName  
	    * @return  
	    */  
	   @SuppressWarnings("unchecked")   
	   public static Method getGetMethod(Class objectClass, String fieldName) {   
	       StringBuffer sb = new StringBuffer();   
	       sb.append("get");   
	       sb.append(fieldName.substring(0, 1).toUpperCase());   
	       sb.append(fieldName.substring(1));   
	       try {   
	           return objectClass.getMethod(sb.toString());   
	       } catch (Exception e) {   
	       }   
	       return null;   
	   }  

	   
	   /**  
	     * java����bean��set����  
	     *   
	     * @param objectClass  
	     * @param fieldName  
	     * @return  
	     */  
	    @SuppressWarnings("unchecked")   
	    public static Method getSetMethod(Class objectClass, String fieldName) {   
	        try {   
	            Class[] parameterTypes = new Class[1];   
	            Field field = objectClass.getDeclaredField(fieldName);   
	            parameterTypes[0] = field.getType();   
	            StringBuffer sb = new StringBuffer();   
	            sb.append("set");   
	            sb.append(fieldName.substring(0, 1).toUpperCase());   
	            sb.append(fieldName.substring(1));   
	            Method method = objectClass.getMethod(sb.toString(), parameterTypes);   
	            return method;   
	        } catch (Exception e) {     
	            e.printStackTrace();   
	        }   
	        return null;   
	    }  
	    
	    /**  
	     * ִ��set����  
	     *   
	     * @param o ִ�ж���  
	     * @param fieldName ����  
	     * @param value ֵ  
	     * @throws Exception 
	     */  
	    public static void invokeSet(Object o, String fieldName, Object value) throws Exception {   
	        Method method = getSetMethod(o.getClass(), fieldName);   
	        try {   
	            method.invoke(o, new Object[] { value });   
	        } catch (Exception e) {   
	            e.printStackTrace();  
	            throw e;
	        } 
	    }   
	  
	    /**  
	     * ִ��get����  
	     *   
	     * @param o ִ�ж���  
	     * @param fieldName ����  
	     * @throws Exception 
	     */  
	    public static Object invokeGet(Object o, String fieldName) throws Exception {   
	        Method method = getGetMethod(o.getClass(), fieldName);   
	        try {   
	            return method.invoke(o, new Object[0]);   
	        } catch (Exception e) {   
	            e.printStackTrace();  
	            throw e;
	        }   
	    }  
}
