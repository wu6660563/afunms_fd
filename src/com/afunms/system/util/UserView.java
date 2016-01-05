/**
 * <p>Description:user helper</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.util;

import java.util.List;

import com.afunms.system.dao.*;
import com.afunms.system.model.*;

import com.afunms.common.base.BaseDao;

public class UserView
{
   private List deptList;
   private List positionList;
   private List roleList;
   
   public UserView()
   {
       BaseDao dao = null; 
       dao = new DepartmentDao();
       deptList = dao.loadAll();
      
       dao = new PositionDao();
       positionList = dao.loadAll();
       
       RoleDao rd = new RoleDao();
       roleList = rd.loadAll(false);
   }
   /**
    * �õ���ɫselectbox
    * ���޸�admin �û���ʱ�� ѡ���Ϊ������
    * konglq
    */
    public String getRoleBox(int index,int role, boolean isUpdateOperation) {
        StringBuffer sb = new StringBuffer(1000);
        Role vo = null;
        sb.append("<select size=1 name='role' style='width:108px;'>");
        if (index == 0) {
            sb.append("<option value='" + 0 + "' selected>supperadmin</option>");
        } else {
            for(int i=0;i<roleList.size();i++) {
                vo = (Role)roleList.get(i);
                if(index==vo.getId()) {
                    sb.append("<option value='" + vo.getId() + "' selected>");
                } else if (isUpdateOperation) {
                    sb.append("<option value='" + vo.getId() + "'>");
                }
                sb.append(vo.getRole());
                sb.append("</option>");
            }
        }
        sb.append("</select>"); 
        return sb.toString();
    }

   /**
    * �õ���ɫselectbox
    */
   public String getRoleBox(int index)
   {
      StringBuffer sb = new StringBuffer(1000);
      sb.append("<select size=1 name='role' style='width:108px;'>");

      Role vo = null;
      for(int i=0;i<roleList.size();i++)
      {
         vo = (Role)roleList.get(i);
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
   
   public String getRoleBox()
   {
      return getRoleBox(0);
   }

   /**
    * �õ��Ա�selectbox
    */
   public String getSexBox(int index)
   {
      StringBuffer sb = new StringBuffer(500);
      sb.append("<select size=1 name='sex' style='width:108px;'>");
      if(index==1)
      {
         sb.append("<option value=1 selected>��</option>");
         sb.append("<option value=2>Ů</option>");
      }
      else
      {
         sb.append("<option value=1>��</option>");
         sb.append("<option value=2 selected>Ů</option>");
      }
      sb.append("</select>");
      return sb.toString();
   }

   public String getSexBox()
   {
      return getSexBox(1);
   }
   
   /**
    * �õ�����selectbox
    */
   public String getDeptBox(int index, boolean isUpdateOperation)
   {
      StringBuffer sb = new StringBuffer(1000);
      sb.append("<select size=1 name='dept' style='width:108px;'>");

      Department vo = null;
      for(int i=0;i<deptList.size();i++)
      {
         vo = (Department)deptList.get(i);
         if(index==vo.getId()) {
             sb.append("<option value='" + vo.getId() + "' selected>");
         } else if (isUpdateOperation) {
             sb.append("<option value='" + vo.getId() + "'>");
         }
         sb.append(vo.getDept());
         sb.append("</option>");
      }
      sb.append("</select>");
      return sb.toString();
   }

   public String getDeptBox()
   {
      return getDeptBox(0, true);
   }

   /**
    * �õ�ְ��selectbox
    */
   public String getPositionBox(int index, boolean isUpdateOperation)
   {
      StringBuffer sb = new StringBuffer(1000);
      sb.append("<select size=1 name='position' style='width:108px;'>");

      Position vo = null;
      for(int i=0;i<positionList.size();i++)
      {
         vo = (Position)positionList.get(i);
         if(index==vo.getId()) {
             sb.append("<option value='" + vo.getId() + "' selected>");
         } else if (isUpdateOperation) {
             sb.append("<option value='" + vo.getId() + "'>");
         }
         sb.append(vo.getName());
         sb.append("</option>");
      }
      sb.append("</select>");
      return sb.toString();
   }

   public String getPositionBox()
   {
      return getPositionBox(0, true);
   }   
   
   public String getDept(int id)
   {
       Department tmpObj = new Department();
       tmpObj.setId(id);
       
       int index = deptList.indexOf(tmpObj); 
       if(index==-1) return "";
       return ((Department)deptList.get(index)).getDept();
   }
   
   public String getPosition(int id)
   {
       Position tmpObj = new Position();
       tmpObj.setId(id);
       
       int index = positionList.indexOf(tmpObj); 
       if(index==-1) return "";
       return ((Position)positionList.get(index)).getName();
   }
   
   public String getRole(int id)
   {
       Role tmpObj = new Role();
       tmpObj.setId(id);
       
       int index = roleList.indexOf(tmpObj); 
       if(index==-1) return "";
       return ((Role)roleList.get(index)).getRole();
   }   
}
