package com.afunms.polling.impl;

import java.io.Serializable;


import java.util.List;

import com.afunms.report.abstraction.*;
import com.afunms.report.base.*;
import com.afunms.polling.om.*;
import com.afunms.polling.impl.*;
import com.afunms.common.util.*;




public class IpResourceReport extends ImplementorReport1
{
    public void createReport(){
    	
    	try{
    	setHead("");    	
    	setNote(""); 
    	
    	setTimeStamp(SysUtil.getCurrentDate());
    	setTableHead(new String[]{"���","IP��ַ","MAC��ַ","�Ƿ��","�Ƿ��Ͷ���","ʹ���ߣ����ţ�"});    	
    	setColWidth(new int[]{2,5,6,5,6,6});
    	table = new String[6][tableHead.length];
    	for(int i=0;i<6;i++)
    	{
    		table[i][0] = String.valueOf(i+1);  //���
    		table[i][1] = "";
    		table[i][2] = "";
    		table[i][3] = "" ;
    		table[i][4] = "";     		    			
        	table[i][5] = "";
    	}
    	
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }       
}