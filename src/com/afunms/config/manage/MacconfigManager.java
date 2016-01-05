package com.afunms.config.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.Fileupload;
import com.afunms.config.dao.DistrictDao;
import com.afunms.config.dao.MacconfigDao;
import com.afunms.config.model.DistrictConfig;
import com.afunms.config.model.Macconfig;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.topology.util.ExcelUtil;



public class MacconfigManager  extends BaseManager implements ManagerInterface{

	
	/**
	 * 查询所有的方法
	 * @return
	 */
	private String list() {
		String where = getWhere();
		String jsp = "/config/macconfig/list.jsp";
		MacconfigDao dao = new MacconfigDao();		
		setTarget(jsp); 
		list(dao , where);
		
	    DistrictDao districtDao = new DistrictDao();
	    List districtList = null;
		try {
			districtList = districtDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    districtDao.close();
	    
	    Hashtable hashtable = new Hashtable();
	    
	    List list = (List)request.getAttribute("list");
	    if(list!=null&&list.size()>0){
	    	for(int i = 0 ; i < list.size() ; i++){
	    		Macconfig macconfig = (Macconfig)list.get(i);
	    		
	    		if(districtList!=null&&districtList.size()>0){
	    			for(int j = 0 ; j < districtList.size() ; j++){
	    				DistrictConfig districtConfig = (DistrictConfig)districtList.get(j);
	    				if(macconfig.getDiscrictid() == districtConfig.getId()){
	    					hashtable.put(macconfig.getId(), districtConfig);
	    				}
		    		}
	    		}
	    		
	    	}
	    }
	    request.setAttribute("hashtable", hashtable);
	    request.setAttribute("districtList", districtList);
        return jsp;
	}
	
	private String getWhere(){
		String condition = getParaValue("condition");
		String sql = "";
		
		request.setAttribute("condition", condition);
		if(condition == null || condition.trim().length() == 0){
			return sql;
		}
		sql = sql + " where";
		if("mac".equals(condition)){
			sql = sql + getMacSql();
		}
		if("district".equals(condition)){
			sql = sql + getDistrictIdSql();
		}
		return sql;
	}
	
	private String getMacSql(){
		String sql = "";
		String searchMac = getParaValue("searchMac");
		if(searchMac==null || searchMac.trim().length() == 0){
			sql = " mac = mac";
		}else {
			sql = " mac='" + searchMac.trim()+"'";
		}
		request.setAttribute("searchMac", searchMac);
		return sql;
	}
	
	private String getDistrictIdSql(){
		String sql = "";
		String searchDistrictId = getParaValue("searchDistrictId");
		if(searchDistrictId==null || searchDistrictId.trim().length() == 0 || "-1".equals(searchDistrictId)){
			sql = " discrictid = discrictid";
			searchDistrictId = "-1";
		}else{
			sql = " discrictid = " + searchDistrictId;
		}
		request.setAttribute("searchDistrictId", searchDistrictId);
		return sql;
	}
	
	
	
    
	/**
	 * 删除方法
	 * @return
	 */
	public String delete()
	{
		String[] ids = getParaArrayValue("checkbox");
		MacconfigDao dao = new MacconfigDao();
		try
		{
			if(ids!=null && ids.length>0){
				dao.delete(ids);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			dao.close();
		}
			
        return "/macconfig.do?action=list";
	}
	
	private String ready_edit(){
		
		MacconfigDao dao = new MacconfigDao();
		BaseVo vo = null;
		try{
	    	   vo = dao.findByID(getParaValue("id"));       
	       }catch(Exception e){
	    	   e.printStackTrace();
	       }finally{
	    	   dao.close();
	       }
	    request.setAttribute("vo",vo);
		return "/config/macconfig/edit.jsp";
	}
	/**
	 * 修改方法
	 * @return
	 */
	   private String update()
       {    	   
		   Macconfig vo=new Macconfig();
		vo.setId(getParaIntValue("id"));
    	vo.setMac(getParaValue("mac"));
    	vo.setMacdesc(getParaValue("macdesc")); 
    	vo.setDiscrictid(this.getParaIntValue("discrictid"));
    	MacconfigDao dao = new MacconfigDao();
        try
        {
        	
        	  dao.update(vo);	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	dao.close();
        }
          
        return "/macconfig.do?action=list";
    }
	/**
	 * 添加方法
	 * @return
	 */
	public String add()
    {   
		
		Macconfig vo=new Macconfig();
		MacconfigDao dao = new MacconfigDao();
		
		
		vo.setId(getParaIntValue("id"));
    	vo.setMac(getParaValue("mac"));
    	vo.setDiscrictid(getParaIntValue("discrictid")); 
    	vo.setMacdesc(getParaValue("macdesc"));
    	
	        try{
	        	dao.save(vo);
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
	        	dao.close();
	        }
        return "/macconfig.do?action=list";
    }  
	
	 public String execute(String action) {
		// TODO Auto-generated method stub
		 
		if(action.equals("list"))
		{
			return list();
		}
		if(action.equals("add"))
		{
			return add();
		}
		if(action.equals("delete"))
		{
			return delete();
		}
	      if(action.equals("update"))
	      {
	            return update();
	      }
		if(action.equals("ready_edit"))
		{
            return ready_edit();
		}
		if(action.equals("ready_add")){
			return "/config/macconfig/add.jsp";
		}
		if("toImportExcel".equals(action)){
			return toImportExcel();
		}
		if("importExcel".equals(action)){
			return importExcel();
		}
		if("exportExcel".equals(action)){
			return exportExcel();
		}
        	
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

    private String toImportExcel(){
        DistrictDao districtDao=new DistrictDao();
	    List list = null;
	    try {
			list = districtDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			districtDao.close();
		}
		request.setAttribute("list", list);
		return "/config/macconfig/importexcel.jsp";
	}
    
    
    private String importExcel(){
        String discrictid = "";
        String saveDirPath = ResourceCenter.getInstance().getSysPath() + "WEB-INF/macConfig";
        Fileupload fileupload = new Fileupload(saveDirPath);
        fileupload.doupload(request);
        List formFieldList = fileupload.getFormFieldList();
        List allMacList = null;
        for(int i = 0 ; i < formFieldList.size() ; i++){
        	List formField = (List)formFieldList.get(i);
        	String formFieldType = (String)formField.get(0);
        	String formFieldName = (String)formField.get(1);
        	String formFieldValue = (String)formField.get(2);
        	if("file".equals(formFieldType)){
        		if("fileName".equals(formFieldName)){
        			List excellist = ExcelUtil.readExcel(new File(formFieldValue));
        			allMacList = getAllMacList(excellist);
        		}
        		
        	}else if("formField".equals(formFieldType)){
        		if("discrictid".equals(formFieldName)){
        			discrictid = formFieldValue;
        		}
        	}
        }
        MacconfigDao macconfigDao = new MacconfigDao();
        List allMacConfiglist = null;
        try {
        	allMacConfiglist = macconfigDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			macconfigDao.close();
		}
		if(allMacConfiglist==null){
			allMacConfiglist = new ArrayList();
		}
        List macConfigList = new ArrayList();
        if(allMacList!=null&&allMacList.size()>0){
        	for(int i = 0 ; i < allMacList.size(); i++){
        		String mac = (String)allMacList.get(i);
        		for(int j = 0 ; j < allMacConfiglist.size() ; j++){
        			Macconfig macConfigflag = new Macconfig();
        			macConfigflag = (Macconfig)allMacConfiglist.get(j);
            		if(mac.trim().equals(macConfigflag.getMac().trim())){
            			allMacConfiglist.remove(j);
            		}
        		}
    			Macconfig macConfig = new Macconfig();
        		macConfig.setMac(mac);
        		macConfig.setDiscrictid(Integer.parseInt(discrictid));
        		macConfig.setMacdesc("");
        		allMacConfiglist.add(macConfig);
        	}
        }
        macconfigDao = new MacconfigDao();
        
        try {
			macconfigDao.deleteAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			macconfigDao.close();
		}
		
		macconfigDao = new MacconfigDao();
		
		try {
			macconfigDao.saveBatch(allMacConfiglist);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			macconfigDao.close();
		}
        
		return list();
	}
    
    private String exportExcel(){
    	
    	String where = getWhere();
    	
    	System.out.println(where+"========wheres=======");
    	
		String jsp = "/config/macconfig/list.jsp";
		MacconfigDao dao = new MacconfigDao();		
		setTarget(jsp); 
		list(dao , where);
		
    	List list = (List)request.getAttribute("list");
    	
    	
    	AbstractionReport1 abstractionReport1 = new ExcelReport1(new IpResourceReport());
    	abstractionReport1.createReport_macconfiglist("temp/macconfiglist_report.xls", "", list);
    	request.setAttribute("filename", abstractionReport1.getFileName());

    	return "/topology/ipregional/download.jsp";
    }
    
    
    private List getAllMacList(List excellist){
    	List allMacList = new ArrayList();
    	if(excellist!=null&&excellist.size()>0){
    		for(int i = 0 ; i < excellist.size(); i++){
    			List numList = (List)excellist.get(i);
    			if(numList!=null && numList.size()>0){
    				for(int j = 0 ; j < numList.size(); j++){
    	    			List cellList = (List)numList.get(j);
    	    			if(cellList!=null && cellList.size()>0){
    	    				for(int k = 0 ; k < cellList.size(); k++){
    	    					String mac = (String)cellList.get(k);
    	    					if(mac!=null&&mac.trim().length()>0){
    	    						allMacList.add(mac);
    	    					}
    	    					
    	    				}
    	    			}
    	    		}
    			}
    		}
    	}
    	return allMacList;
    }
	 
	 
	 
//	  /**
//	    * 分页显示记录
//	    * targetJsp:目录jsp
//	    */
//	   protected String list(DaoInterface dao)
//	   {
//		   String targetJsp = null;
//		   	int perpage = getPerPagenum();
//	       List list = dao.listByPage(getCurrentPage(),perpage);
//	       if(list==null) return null;
//	       
//	       request.setAttribute("page",dao.getPage());
//	       request.setAttribute("list",list);
//	       targetJsp = getTarget(); 
//		   return targetJsp;
//	   }

}
