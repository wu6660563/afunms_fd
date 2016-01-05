package com.afunms.capreport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.afunms.capreport.model.UtilReport;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;



public class UtilReportDao  extends BaseDao implements DaoInterface{

	public UtilReportDao()
	{
		super("nms_report");
	}
	public BaseVo loadFromRS(ResultSet rs)
	{
		// TODO Auto-generated method stub
		UtilReport vo = new UtilReport();
		try{
			vo.setId(rs.getInt("id"));
			vo.setName(rs.getString("name"));
			vo.setType(rs.getString("type"));
			vo.setIds(rs.getString("ids"));
			vo.setCollecttime(rs.getString("collecttime"));
			vo.setBegintime("begintime");
			vo.setEndtime("endtime");
		}catch(Exception e){
			e.printStackTrace();
		}
		return vo;
	}
	 public UtilReport findByBid(String id){
		 UtilReport vo=new UtilReport();
	     try
	     {
	         rs = conn.executeQuery("select * from nms_userreport where id="+id);
	         while(rs.next())
	        	vo=(UtilReport) loadFromRS(rs);
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("BusinessNodeDao:findByBid()",e);
	         
	     }
	     finally
	     {
	    	 if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	 if (conn!=null) {
	    		 conn.close();
			}
	         
	     }
	     return vo;
	  }
	public boolean save(BaseVo vo) 
	{
		return false;
		
	}
	public void commit()
	{
		this.conn.commit();
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

}
