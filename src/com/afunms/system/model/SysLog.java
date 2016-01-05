/**
 * <p>Description:mapping table NMS_SYS_LOG</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.system.model;

import com.afunms.common.base.BaseVo;

public class SysLog extends BaseVo
{
   private int id;
   private int userid;
   private String event;
   private String logtime;
   private String quittime;		// 退出时间 yag add 2012-08-22
   private String ip;
   private String user;
   private int type;			// 登录记录类型 yag add 2012-12-27
  
   public void setId(int id)
   {
      this.id = id;
   }

   public int getId()
   {
      return id;
   }

   
   
   public int getUserid() {
	   return userid;
   }

   public void setUserid(int userid) {
	   this.userid = userid;
   }
	
   public void setEvent(String event)
   {
      this.event = event;
   }

   public String getEvent()
   {
      return event;
   }

   public void setLogTime(String logtime)
   {
      this.logtime = logtime;
   }

   public String getLogTime()
   {
      return logtime;
   }

   public void setIp(String ip)
   {
      this.ip = ip;
   }

   public String getIp()
   {
      return ip;
   } 

   public void setUser(String user)
   {
      this.user = user;
   }

   public String getUser()
   {
      return user;
   }

	public String getQuittime() {
		return quittime;
	}

	public void setQuittime(String quittime) {
		this.quittime = quittime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
