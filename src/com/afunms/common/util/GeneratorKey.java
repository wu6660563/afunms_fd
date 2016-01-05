package com.afunms.common.util;

import java.sql.ResultSet;

import com.afunms.common.util.DBManager;

public class GeneratorKey
{
   private static GeneratorKey keygen = new GeneratorKey();  
   private GeneratorKey() {}

   public static GeneratorKey getInstance()
   {
	   return keygen;
   }

   public synchronized int getNextKey()
   {
	   DBManager db = new DBManager();
	   int id = 0;
	   try
	   {
		   ResultSet rs = db.executeQuery("select id from nms_userReport order by id desc limit 1");
		   if(rs.next())
		   {
			   id = rs.getInt(1);
			   
		   }
			   
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }finally{
		   db.close();
	   }
	  
	   return id;
   }  
   public synchronized int getKey()
   {
	   DBManager db = new DBManager();
	   int id = 0;
	   try
	   {
		   ResultSet rs = db.executeQuery("select id from nms_comp_rule order by id desc limit 1");
		   if(rs.next())
		   {
			   id = rs.getInt(1);
			   
		   }
		   
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }finally{
		   db.close();
	   }
	   
	   return id;
   }  
}
