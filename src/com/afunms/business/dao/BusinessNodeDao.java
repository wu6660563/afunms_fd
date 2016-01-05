package com.afunms.business.dao;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.application.model.DBVo;
import com.afunms.business.model.BusinessNode;

public class BusinessNodeDao extends BaseDao implements DaoInterface{
	
	public BusinessNodeDao() {
		super("nms_businessnode");
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
	
		BusinessNode vo = new BusinessNode();
		 try
	      {   
			  vo.setId(rs.getInt("id"));
	          vo.setBid(rs.getInt("bid"));
	          vo.setName(rs.getString("name"));
	          vo.setDesc(rs.getString("bn_desc"));
	          vo.setCollecttype(rs.getInt("collecttype"));
	          vo.setFlag(rs.getInt("flag"));
	          vo.setMethod(rs.getString("method"));}
	         
	      catch(Exception e)
	      {
	          SysLogger.error("BusinessNodeDao.loadFromRS()",e);
	          vo = null;
	      }
	      return vo;
	}

	 /**
	    * 列出所有方法
	    */
	  public List loadAll()
	  {
	     List list = new ArrayList(5);
	     try
	     {
	         rs = conn.executeQuery("select * from nms_businessnode order by id");
	         while(rs.next())
	        	list.add(loadFromRS(rs)); 
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("BusinessNodeDao:loadAll()",e);
	         list = null;
	     }
	     finally
	     {
	         conn.close();
	     }
	     return list;
	  }
	  public List findByBid(String bid){
		     List list = new ArrayList(5);
		     try
		     {
		         rs = conn.executeQuery("select * from nms_businessnode where bid="+bid+" order by id");
		         while(rs.next())
		        	list.add(loadFromRS(rs)); 
		     }
		     catch(Exception e)
		     {
		         SysLogger.error("BusinessNodeDao:findByBid()",e);
		         list = null;
		     }
		     finally
		     {
		         conn.close();
		     }
		     return list;
		  }
	    /**
	    * 修改方法
	    */
	  public boolean update(BaseVo baseVo) {
	    boolean flag = true;
		// TODO Auto-generated method stub
		BusinessNode vo = (BusinessNode)baseVo;	
		BusinessNode pvo = (BusinessNode)findByID(vo.getId()+"");
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_businessnode set bid =");
		sql.append(vo.getBid());
		sql.append(",name='");
		sql.append(vo.getName());
		sql.append("',bn_desc='");
		sql.append(vo.getDesc());
		sql.append("',collecttype=");
		sql.append(vo.getCollecttype());
		sql.append(",flag=");
		sql.append(vo.getFlag());
		sql.append(",method='");
		sql.append(vo.getMethod());
		sql.append("' where id="+vo.getId());
		System.out.println(sql);
		String allipstr = pvo.getBid()+"_"+pvo.getId();
		try{
		 saveOrUpdate(sql.toString());
		if (!String.valueOf(vo.getBid()).equals(String.valueOf(pvo.getBid()))){
			CreateTableManager ctable = new CreateTableManager();
			conn = new DBManager();
			try{
	    		ctable.deleteTable(conn,"bnode",allipstr,"bnode");//Ping
	  			ctable.deleteTable(conn,"bnodehour",allipstr,"bnodehour");//Ping
	  			ctable.deleteTable(conn,"bnodeday",allipstr,"bnodeday");//Ping
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }finally{
	    		  conn.close();
	    	  }
	    	  allipstr = "";
	    	  String newallipstr = vo.getBid()+"_"+vo.getId();
	    	  conn = new DBManager();
	    	  try{
	    		  ctable.createBNodeTable(conn,"bnode",newallipstr,"bnode");//Ping
	  			ctable.createBNodeTable(conn,"bnodehour",newallipstr,"bnodehour");//Ping
	  			ctable.createBNodeTable(conn,"bnodeday",newallipstr,"bnodeday");//Ping
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }finally{
	    		  conn.close();
	    	  }
		}
		
		}catch(Exception e){
			 flag = false;
			e.printStackTrace();
		}
		return flag;
		
	}
	   /**
	    * 添加方法
	    */
	   public boolean save(BaseVo basevo) {
		// TODO Auto-generated method stub
		   boolean flag = true;
		    BusinessNode vo = (BusinessNode)basevo;	  
		    StringBuffer sql = new StringBuffer();
		    //int id = getNextID();	
		    sql.append("insert into nms_businessnode(id,bid,name,flag,bn_desc,collecttype,method)values(");
		    sql.append(vo.getId());
			sql.append(",");
			sql.append(vo.getBid());
			sql.append(",'");
			sql.append(vo.getName());
			sql.append("',");
			sql.append(vo.getFlag());
			sql.append(",'");
			sql.append(vo.getDesc());
			sql.append("',");
			sql.append(vo.getCollecttype());
			sql.append(",'");
			sql.append(vo.getMethod());
			sql.append("')");
			//System.out.println(sql);
			String allipstr = vo.getBid()+"_"+vo.getId();
		   try{
			   saveOrUpdate(sql.toString());
			   CreateTableManager ctable = new CreateTableManager();    
		    	  conn = new DBManager();
		    	  try{
		    		  ctable.createBNodeTable(conn,"bnode",allipstr,"bnode");//Ping
		  			ctable.createBNodeTable(conn,"bnodehour",allipstr,"bnodehour");//Ping
		  			ctable.createBNodeTable(conn,"bnodeday",allipstr,"bnodeday");//Ping
		  			
		    	  }catch(Exception e){
		    		  e.printStackTrace();
		    	  }finally{
		    		  conn.close();
		    	  }
		   }catch(Exception e){
			   flag = false;
		   }
		   return flag;
	}
	   

		 /**
		  * 根据id删除这条记录
		  * @param id
		  * @return
		  */
		  public boolean delete(String id)
		   {
			   boolean result = false;
			   try
			   {
				   //BusinessNode bnode = (BusinessNode)findByID(id);  
				   conn.addBatch("delete from nms_businessnode where id=" + id);
				   conn.executeBatch();
//				   String allipstr = bnode.getBid()+"_"+bnode.getId();
//				   try{
//	            		  CreateTableManager ctable = new CreateTableManager();   
//	            		  ctable.deleteTable(conn,"bnode",allipstr,"bnode");//原始数据表
//	            			ctable.deleteTable(conn,"bnodehour",allipstr,"bnodehour");//Hour
//	            			ctable.deleteTable(conn,"bnodeday",allipstr,"bnodeday");//Day 
//	            	}catch(Exception ex){
//	            		  
//	            	}
				   result = true;
			   }
			   catch(Exception e)
			   {
				   SysLogger.error("BusinessNodeDao.delete()",e); 
			   }
			   finally
			   {
				   conn.close();
			   }
			   return result;
		   }
		  
		  public List findByCondition(String view,String value)
		   {	  
			  
			  return findByCriteria("select * from nms_businessnode where name like '%"+ value +"%' and bid = "+ view +"");  
			  
		   }
//		  public List findByCondition1(String view,String value)
//		   {	
//			  return findByCriteria("select * from nms_businessnode where name like '%"+ value +"%' and bid = "+ view +"");  
//		   }
//		  public List findByCondition2(String view,String value)
//		   {	 
//			  return findByCriteria("select * from nms_businessnode where name like '%"+ value +"%'");  
//		   }
//		  public List findByCondition3(String view,String value)
//		   {
//			  return findByCriteria("select * from nms_businessnode");
//		   }
		  //quzhi  
		   public List getBussinessByFlag(int flag){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
			   sql.append("select * from nms_businessnode where flag = "+flag);
			   return findByCriteria(sql.toString());
		   }
}
