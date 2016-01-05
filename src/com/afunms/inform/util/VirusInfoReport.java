/**
 * <p>Description:virus report,subclass of implementor report</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-20
 */

package com.afunms.inform.util;

import java.util.List;

import com.afunms.report.base.ImplementorReport;
import com.afunms.inform.dao.InformDao;
import com.afunms.inform.model.*;

public class VirusInfoReport extends ImplementorReport
{
	private String orderField; //�����ֶ�
	
	public void setOrderField(String orderField)
	{
	   	this.orderField = orderField;
	}
	
    public void createReport()
    {
    	head = "�������ݱ���";
    	
    	note = "������Դ�������������";
    	
    	tableHead = new String[]{"���","IP��ַ","��Ⱦ����","��Ⱦ����","��Ⱦ�������Ĳ���","��Ⱦ���������ļ�"};
    	
    	colWidth = new int[]{2,4,4,4,8,8};
    	
    	InformDao dao = new InformDao();
    	List list = dao.queryVirusInfo(timeStamp,orderField);
    	table = new String[list.size()][tableHead.length];
    	for(int i=0;i<list.size();i++)
    	{
    		VirusInfo vo = (VirusInfo)list.get(i);
 	        table[i][0] = String.valueOf(i+1);  //��� 
	        table[i][1] = vo.getIp();
		    table[i][2] = String.valueOf(vo.getNumOfTimes());
			table[i][3] = String.valueOf(vo.getNumOfVirus());
			table[i][4] = vo.getVirusName();
			table[i][5] = vo.getVirusFile();
    	}
    }       
}