package com.afunms.sysset.util;

import java.util.List;

import com.afunms.sysset.dao.MiddlewareDao;
import com.afunms.sysset.model.Middleware;

public class MiddlewareView 
{
	private List<Middleware> listFather = null;
	private String sql = "select * from afunms.nms_manage_nodetype where father_id=0";

	public MiddlewareView() 
	{
		MiddlewareDao dao = new MiddlewareDao();
		// ������еĸ��ڵ�
		listFather = dao.findByCriteria(sql);
	}

	/**
	 * ���� ѡ��"����"������
	 */
	public String getMiddlewareBox(int index)
	{
		StringBuffer sb = new StringBuffer(300);
		sb.append("<select size=1 name='father_id' style='width:150px;'>");
		sb.append("<option value='0'></option>");

		for (int i = 0; i < listFather.size(); i++) 
		{
			Middleware vo = (Middleware) listFather.get(i);
			if (index == vo.getId())
				sb.append("<option value='" + vo.getId() + "' selected>");
			else
				sb.append("<option value='" + vo.getId() + "'>");
			sb.append(vo.getText());
			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	  public String getMiddlewareBox()
	   {
		   return getMiddlewareBox(0);
	   }

}
