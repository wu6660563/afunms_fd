/**
 * <p>Description:create ip list report</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-01-18
 */

package com.afunms.ipresource.util;

import java.util.List;

import com.afunms.report.base.ImplementorReport;
import com.afunms.ipresource.dao.IpResourceDao;
import com.afunms.ipresource.model.IpResource;
import com.afunms.common.util.SysUtil;

public class IpResourceReport extends ImplementorReport
{
    public void createReport()
    {
    	setHead("");    	
    	setNote("");   
    	setTimeStamp(SysUtil.getCurrentDate());
    	setTableHead(new String[]{"���","IP��ַ","MAC��ַ","ֱ���豸","ֱ���˿�"});    	
    	setColWidth(new int[]{2,5,6,5,6});
    	
    	IpResourceDao dao = new IpResourceDao();
    	List list = dao.loadAll();
    	table = new String[list.size()][tableHead.length];
    	for(int i=0;i<list.size();i++)
    	{
    		IpResource vo = (IpResource)list.get(i);
 	        table[i][0] = String.valueOf(i+1);  //��� 	        
		    table[i][1] = vo.getIpAddress();
		    table[i][2] = vo.getMac();
			table[i][3] = vo.getNode();
			table[i][4] = vo.getIfIndex() + "(" + vo.getIfDescr() + ")";
    	}
    }       
}