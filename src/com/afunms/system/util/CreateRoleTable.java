/**
 * <p>Description:create role table</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-08
 */

package com.afunms.system.util;

import java.util.List;

import com.afunms.system.dao.AccreditDao;
import com.afunms.system.dao.RoleDao;
import com.afunms.system.model.Accredit;
import com.afunms.system.model.Role;
import com.afunms.common.util.SysLogger;

public class CreateRoleTable
{
  private int rows;

  //���ڱ༭
  public String getTable(int role_id,boolean isAdmin)
  {
     StringBuffer table = new StringBuffer(2000);
     table.append("<table border=1 cellspacing=0 cellpadding=5 bordercolorlight='#000000' bordercolordark='#FFFFFF'>");
     table.append("<tr bgcolor='#D4E1D5'><td width=100 align='center'>һ���˵�</td><td width=300 align='center'>");
     table.append("�����˵�</td><td width=150 align='center'>Ȩ ��</td></tr>");

     AccreditDao dao = null;
     try
     {
     	dao = new AccreditDao();
     	int[] menusNum = dao.getMenuNumByFirst(isAdmin);                
     	List list = dao.loadRoleMenu(role_id);            
        rows = 0;
        int first = 0;
        boolean beforeFirst = false; //�Ƿ������һ���˵�֮��
        for(int i=0;i<list.size();i++)
        {
        	
           Accredit vo = (Accredit)list.get(i);
           if(vo.getMenu().substring(2,4).equals("00")) //˵����һ���˵�
           {
              table.append("<tr class='othertr' align='center'><td rowspan='");
              table.append(menusNum[first]);
              table.append("' bgcolor='#D4E1D5'>");
              table.append(vo.getTitle());
              table.append("</td>");
              first++;
              beforeFirst = true;
           }
           else
           {
              if(!beforeFirst)
              {
                 table.append("<tr class='othertr'>");
                 beforeFirst = true;
              }
              table.append("<td align='center' class='othertr' bgcolor='#ECECEC'>");
              table.append("<font color='#8A2BE2'>");
              table.append(vo.getTitle());
              table.append("</font>");
              table.append("</td><td align='center' bgcolor='#ECECEC'>");
              if(isAdmin)
              	 table.append(getAdminOperate(vo.getOperate(),rows,vo.getMenu()));
              else
                 table.append(getOperate(vo.getOperate(),rows,vo.getMenu()));
              table.append("</td></tr>");
              rows++;
           } //end_if_else
        } //end_for
        table.append("</table>");
     }
     catch (Exception e)
     {
        SysLogger.error("CreateRoleTable.getTable()",e);
     }
     finally
     {
        dao.close();
     }
     return table.toString();
  }

  //���ر���ܵ�����
  public int getRows()
  {
     return rows;
  }

  /**
   * @param opr ���ò���
   * @param index �ڼ���select��
   * @param menuid �˵�id
   * @return
   */
  private String getOperate(int opr, int index, String menu)
  {
     StringBuffer selOpr = new StringBuffer(2000);
     selOpr.append("<select size=1 name='selectopr");
     selOpr.append(index);
     selOpr.append("' style='width:60px;'>");
     selOpr.append("<option value='");
     selOpr.append(menu);
     if(opr==1)
        selOpr.append(",1' selected>");
     else
        selOpr.append(",1'>");
     selOpr.append("����</option><option value='");
     selOpr.append(menu);
     if(opr==2)
        selOpr.append(",2' selected>");
     else
        selOpr.append(",2'>");
     selOpr.append("ֻ��</option><option value='");
     selOpr.append(menu);
     if(opr==3)
        selOpr.append(",3' selected>");
     else
        selOpr.append(",3'>");
     selOpr.append("��ȫ</option></select>");

     return selOpr.toString();
  }

  //ר����superadmin
  private String getAdminOperate(int opr, int index, String menu)
  {
     StringBuffer selOpr = new StringBuffer(2000);
     selOpr.append("<select size=1 name='selectopr");
     selOpr.append(index);
     selOpr.append("' style='width:60px;'>");
     selOpr.append("<option value='");
     selOpr.append(menu);
     if(opr==0)
        selOpr.append(",0' selected>");
     else
        selOpr.append(",0'>");
     selOpr.append("����</option><option value='");
     selOpr.append(menu);
     if(opr!=0)
        selOpr.append(",1' selected>");
     else
        selOpr.append(",1'>");
     selOpr.append("����</option></select>");
     return selOpr.toString();
  }
  
  //��ɫѡ�� ������
  public String getRoleBox(int index)
  {
     StringBuffer sb = new StringBuffer(1000);
     sb.append("<select size=1 name='role' style='width:100px;' onchange='toRole()'>");

     RoleDao dao = new RoleDao();
     List list = dao.loadAll(false);
     dao.close();
     Role vo = null;
     for(int i=0;i<list.size();i++)
     {
        vo = (Role)list.get(i);
        if(index==vo.getId())
            sb.append("<option value='" + vo.getId() + "' selected>");
        else
            sb.append("<option value='" + vo.getId() + "'>");
        sb.append(vo.getRole());
        sb.append("</option>");
     }
     sb.append("</select>");
     return sb.toString();
  }
}
