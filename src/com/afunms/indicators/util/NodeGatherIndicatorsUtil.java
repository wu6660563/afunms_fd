package com.afunms.indicators.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import oracle.jdbc.dbaccess.DBType;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.model.Business;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.model.GatherIndicators;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.MonitorNodeDTO;


/**
 * ����Ϊ ���豸���ʱ ���豸�������Ĭ�ϵؼ��ָ��
 * @author Administrator
 *
 */

public class NodeGatherIndicatorsUtil {
	/**
	 * 
	 * ����ɾ���ɼ�ָ�꣬����ɾ��idΪnotdelnodeid�Ĳɼ�ָ�� 
	 * @param name ָ������
	 * @param type ָ������
	 * @param subtype ָ���������
	 * @param notdelnodeid ����ɾ���ɼ�ָ���nodeid
	 * @param nodes ��Ҫɾ����nodeids
	 * 
	 * konglq
	 */
	public void deleteGatherIndicatorsFortype(String name,String type,String subtype,String notdelnodeid,String[] nodeids)
	{
		
		NodeGatherIndicatorsDao nodegdao = new NodeGatherIndicatorsDao();
		
	    String sql= "delete from nms_gather_indicators_node where nodeid!='"+notdelnodeid+"'" +
	    		" and name='"+name+"' and type='"+type+"' and subtype='"+subtype+"' ";
	    
	    StringBuffer st=new StringBuffer(sql);
	    
	    if(null!=nodeids && nodeids.length>0)
	    {
	    	st.append(" and ");
	    	for(int i=0;i<nodeids.length;i++)
	    	{
	    		if(i!=nodeids.length-1)
	    		{
	    		st.append("nodeid='").append(nodeids[i]).append("'").append(" or ");
	    		}else
	    		{
	    			st.append("nodeid='").append(nodeids[i]).append("'");
	    		}
	    		
	    	}
	    	
	    }
	    
	    try{
	    	
	    	
	    
		nodegdao.saveOrUpdate(st.toString());	
	    }catch(Exception e)
	    {}finally{
	    	
	    	nodegdao.close();
	    	
	    };
	}
	
	/**
	 * 
	 * ����ɾ����Ԫ�����вɼ�ָ��
	 * @param nodes ��Ҫɾ�Ĳɼ�ָ������б� 
	 * konglq
	 */
	public void deleteNodesGatherIndicators(String[] nodeids)
	{
		
		NodeGatherIndicatorsDao nodegdao = new NodeGatherIndicatorsDao();
		
	    String sql= "delete from nms_gather_indicators_node where nodeid='";
	    
	    StringBuffer st=new StringBuffer(sql);
	    
	    if(null!=nodeids && nodeids.length>0)
	    {
	    	
	    	for(int i=0;i<nodeids.length;i++)
	    	{
	    		
	    		if(i!=nodeids.length-1)
	    		{
	    		st.append(nodeids[i]).append("'").append(" or nodeid='");
	    		}else
	    		{
	    			st.append(nodeids[i]).append("'");
	    		}
	    		
	    	}
	    	
	    }
	    
	     
	    try{
	    	
	    	
	    
		nodegdao.saveOrUpdate(st.toString());	
	    }catch(Exception e)
	    {}finally{
	    	
	    	nodegdao.close();
	    	
	    };
	}
	
	
	/**
	 * ��Ӳɼ�ָ��
	 * @param nodegath ��ֵ����
	 * 
	 * konglq
	 */
	public void addGatherIndicatorsFornode(List nodelist)
	{
		
	  NodeGatherIndicatorsDao nodegdao = new NodeGatherIndicatorsDao();
	    try{
		nodegdao.saveBatch(nodelist);	
	    }catch(Exception e)
	    {}finally{	
	    	nodegdao.close();
	    };
		
		
	}
	
	
	
	
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public void addGatherIndicatorsForNode(String nodeid , String type , String subtype){
		GatherIndicatorsUtil util = new GatherIndicatorsUtil();
		List<GatherIndicators> list = util.getGatherIndicatorsByTypeAndSubtype(type, subtype);
		
		if(list == null || list.size() ==0 ){
			return;
		}
		
		List<NodeGatherIndicators> nodeGatherIndicatorsList = new ArrayList<NodeGatherIndicators>();
		
		
		for(int i = 0 ; i < list.size(); i++){
			GatherIndicators gatherIndicators = list.get(i);
			NodeGatherIndicators nodeGatherIndicators = createGatherIndicatorsForNode(gatherIndicators);
			nodeGatherIndicators.setNodeid(nodeid);
			nodeGatherIndicatorsList.add(nodeGatherIndicators);
		}
		
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.saveBatch(nodeGatherIndicatorsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		
	}
	
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
	 * @param nodeid �ڵ�id
	 * @param type ����
	 * @param subtype ������
	 * @param flag �Ƿ���Ĭ�ϲɼ���ʽ1����ʾsnmp��0��������
	 * @param indiname
	 * @param Collecttype �ɼ���ʽ
	 * konglq
	 */
	
	public void addGatherIndicatorsOtherForNode(String nodeid , String type , String subtype,String flag,int Collecttype){
		
		GatherIndicatorsUtil util = new GatherIndicatorsUtil();
		List<GatherIndicators> list = util.getGatherIndicatorsByTypeAndSubtype(type, subtype,flag,Collecttype);
		if(list == null || list.size() ==0 ){
			return;
		}
		
		List<NodeGatherIndicators> nodeGatherIndicatorsList = new ArrayList<NodeGatherIndicators>();
		
		
		for(int i = 0 ; i < list.size(); i++){
			GatherIndicators gatherIndicators = list.get(i);
			NodeGatherIndicators nodeGatherIndicators = createGatherIndicatorsForNode(gatherIndicators);
			nodeGatherIndicators.setNodeid(nodeid);
			nodeGatherIndicatorsList.add(nodeGatherIndicators);
		}
		
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.saveBatch(nodeGatherIndicatorsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		
	}
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public void addGatherIndicatorsForNode(String nodeid , String type , String subtype,String flag){
		GatherIndicatorsUtil util = new GatherIndicatorsUtil();
		List<GatherIndicators> list = util.getGatherIndicatorsByTypeAndSubtype(type, subtype,flag);
		
		if(list == null || list.size() ==0 ){
			return;
		}
		
		List<NodeGatherIndicators> nodeGatherIndicatorsList = new ArrayList<NodeGatherIndicators>();
		
		
		for(int i = 0 ; i < list.size(); i++){
			GatherIndicators gatherIndicators = list.get(i);
			NodeGatherIndicators nodeGatherIndicators = createGatherIndicatorsForNode(gatherIndicators);
			nodeGatherIndicators.setNodeid(nodeid);
			nodeGatherIndicatorsList.add(nodeGatherIndicators);
		}
		
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.saveBatch(nodeGatherIndicatorsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		
	}
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 * @param flag
	 * @param indiname
	 */
	public void addGatherIndicatorsForNode(String nodeid , String type , String subtype,String flag,String indiname){
		GatherIndicatorsUtil util = new GatherIndicatorsUtil();
		List<GatherIndicators> list = util.getGatherIndicatorsByTypeAndSubtype(type, subtype,flag,indiname);
		
		if(list == null || list.size() ==0 ){
			return;
		}
		
		List<NodeGatherIndicators> nodeGatherIndicatorsList = new ArrayList<NodeGatherIndicators>();
		
		
		for(int i = 0 ; i < list.size(); i++){
			GatherIndicators gatherIndicators = list.get(i);
			NodeGatherIndicators nodeGatherIndicators = createGatherIndicatorsForNode(gatherIndicators);
			nodeGatherIndicators.setNodeid(nodeid);
			nodeGatherIndicatorsList.add(nodeGatherIndicators);
		}
		
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.saveBatch(nodeGatherIndicatorsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		
	}
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public void addGatherIndicatorsForNode(String nodeid , String[] ids){
		GatherIndicatorsUtil util = new GatherIndicatorsUtil();
		if(ids == null || ids.length == 0)return;
		List<GatherIndicators> list = util.getGatherIndicatorsByIds(ids);
		
		if(list == null || list.size() ==0 ){
			return;
		}
		
		List<NodeGatherIndicators> nodeGatherIndicatorsList = new ArrayList<NodeGatherIndicators>();
		
		
		for(int i = 0 ; i < list.size(); i++){
			GatherIndicators gatherIndicators = list.get(i);
			NodeGatherIndicators nodeGatherIndicators = createGatherIndicatorsForNode(gatherIndicators);
			nodeGatherIndicators.setNodeid(nodeid);
			nodeGatherIndicatorsList.add(nodeGatherIndicators);
		}
		
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.saveBatch(nodeGatherIndicatorsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		
	}
	
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ��ȡ���豸������ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public List<NodeGatherIndicators> getGatherIndicatorsForNode(String nodeid , String type , String subtype){
		List<NodeGatherIndicators> list = null;
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			list = nodeGatherIndicatorsDao.findByNodeIdAndTypeAndSubtype(nodeid, type, subtype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		return list;
	}
	/**
	 * ͨ�� �豸���� �� �����ͺ��豸id ��ȡ���豸������ָ��
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public List<NodeGatherIndicators> getGatherIndicatorsForNode(String nodeid , String type , String subtype,String isDeflaut){
		List<NodeGatherIndicators> list = null;
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			list = nodeGatherIndicatorsDao.findByNodeIdAndTypeAndSubtype(nodeid, type, subtype,isDeflaut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
		return list;
	}
	
	public void deleteAllGatherIndicatorsForNode(String nodeid , String type , String subtype){
		NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
		try {
			nodeGatherIndicatorsDao.deleteByNodeIdAndTypeAndSubtype(nodeid, type, subtype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			nodeGatherIndicatorsDao.close();
		}
	}
	
	/**
	 * ͨ����׼�ļ��ָ�� ת���� �豸�ļ��ָ��
	 * @param performanceIndicators
	 * @return
	 */
	public NodeGatherIndicators createGatherIndicatorsForNode(GatherIndicators gatherIndicators){
		
		String name = gatherIndicators.getName();
		String type = gatherIndicators.getType();
		String subtype = gatherIndicators.getSubtype();
		String alias = gatherIndicators.getAlias();
		String description = gatherIndicators.getDescription();
		String category = gatherIndicators.getCategory();
		String isDefault = gatherIndicators.getIsDefault();
		String isCollection = gatherIndicators.getIsCollection();
		String poll_interval = gatherIndicators.getPoll_interval();
		String interval_unit = gatherIndicators.getInterval_unit();
		String classpath = gatherIndicators.getClasspath();
		
		
		NodeGatherIndicators nodeGatherIndicators = new NodeGatherIndicators();
		
		nodeGatherIndicators.setName(name);
		nodeGatherIndicators.setType(type);
		nodeGatherIndicators.setSubtype(subtype);
		nodeGatherIndicators.setAlias(alias);
		nodeGatherIndicators.setDescription(description);
		nodeGatherIndicators.setCategory(category);
		nodeGatherIndicators.setIsDefault(isDefault);
		nodeGatherIndicators.setIsCollection(isCollection);
		nodeGatherIndicators.setPoll_interval(poll_interval);
		nodeGatherIndicators.setInterval_unit(interval_unit);
		nodeGatherIndicators.setClasspath(classpath);
		return nodeGatherIndicators;
	}
	
	
	/**
	 * ͨ���豸�ļ��ָ�� ת���� �豸�ļ��ָ��(������һ��)
	 * @param performanceIndicators
	 * @return
	 */
	public NodeGatherIndicators createGatherIndicatorsForNode(NodeGatherIndicators nodeGatherIndicators){
		
		String nodeid = nodeGatherIndicators.getNodeid();
		String name = nodeGatherIndicators.getName();
		String type = nodeGatherIndicators.getType();
		String subtype = nodeGatherIndicators.getSubtype();
		String alias = nodeGatherIndicators.getAlias();
		String description = nodeGatherIndicators.getDescription();
		String category = nodeGatherIndicators.getCategory();
		String isDefault = nodeGatherIndicators.getIsDefault();
		String isCollection = nodeGatherIndicators.getIsCollection();
		String poll_interval = nodeGatherIndicators.getPoll_interval();
		String interval_unit = nodeGatherIndicators.getInterval_unit();
		String classpath = nodeGatherIndicators.getClasspath();
		
		
		NodeGatherIndicators _nodeGatherIndicators = new NodeGatherIndicators();
		
		_nodeGatherIndicators.setNodeid(nodeid);
		_nodeGatherIndicators.setName(name);
		_nodeGatherIndicators.setType(type);
		_nodeGatherIndicators.setSubtype(subtype);
		_nodeGatherIndicators.setAlias(alias);
		_nodeGatherIndicators.setDescription(description);
		_nodeGatherIndicators.setCategory(category);
		_nodeGatherIndicators.setIsDefault(isDefault);
		_nodeGatherIndicators.setIsCollection(isCollection);
		_nodeGatherIndicators.setPoll_interval(poll_interval);
		_nodeGatherIndicators.setInterval_unit(interval_unit);
		_nodeGatherIndicators.setClasspath(classpath);
		return _nodeGatherIndicators;
	}
	
	public List<NodeDTO> getNodeListByTypeAndSubtype(String type , String subtype){
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> nodelist = nodeUtil.getNodeByTyeAndSubtype(type, subtype);
		List<NodeDTO> list = nodeUtil.conversionToNodeDTO(nodelist);
//		if(type.equalsIgnoreCase("host")){
//			HostNodeDao dao = new HostNodeDao();
//			//��ȡ�����ӵķ������б�
//			try{
//				nodelist = dao.loadMonitorByMonCategory(1, 4);
//			}catch(Exception e){
//				
//			}finally{
//				dao.close();
//			}
//		}else if(type.equalsIgnoreCase("db")){
//			DBTypeVo dBTypeVo = null;
//			if("Oracle".equals(subtype)){
//				OraclePartsDao dao = new OraclePartsDao();
//				//��ȡ�����ӵķ������б�
//				try{
//					nodelist = dao.loadAll();
//				}catch(Exception e){
//					
//				}finally{
//					dao.close();
//				}
//			}else{
//				DBTypeDao typeDao = new DBTypeDao();
//				try {
//					dBTypeVo = typeDao.findByDbtype(subtype);
//				} catch (RuntimeException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				DBDao dao = new DBDao();
//				//��ȡ�����ӵķ������б�
//				try{
//					nodelist = dao.getDbByType(dBTypeVo.getId());
//				}catch(Exception e){
//					
//				}finally{
//					dao.close();
//				}
//			}
//		}
//		
//		
//		List nodeDTOlist = new ArrayList();
//		if(nodelist != null && nodelist.size() > 0){
//			for(int i = 0 ; i < nodelist.size() ; i ++){
//				NodeDTO nodeDTO = creatNodeDTOByNode((BaseVo)nodelist.get(i));
//				nodeDTO.setType(type);
//				nodeDTO.setSubtype(subtype);
//				nodeDTOlist.add(nodeDTO);
//			}
//		}
		
		
		
		return list;
	}
	
	
	public NodeDTO creatNodeDTOByNode(BaseVo vo){
		NodeDTO nodeDTO = new NodeDTO();
		
		if (vo instanceof HostNode) {
			HostNode hostNode = (HostNode) vo;
			nodeDTO.setId(hostNode.getId());
			nodeDTO.setName(hostNode.getAlias());
			nodeDTO.setIpaddress(hostNode.getIpAddress());
			nodeDTO.setNodeid(String.valueOf(hostNode.getId()));
			nodeDTO.setBusinessName(getBusinessNameForNode(hostNode.getBid()));
		} else if (vo instanceof DBVo) {
			DBVo dbVo = (DBVo) vo;
			nodeDTO.setId(dbVo.getId());
			nodeDTO.setName(dbVo.getAlias());
			nodeDTO.setIpaddress(dbVo.getIpAddress());
			nodeDTO.setNodeid(String.valueOf(dbVo.getId()));
			nodeDTO.setBusinessName(getBusinessNameForNode(dbVo.getBid()));
		} else if (vo instanceof OracleEntity) {
			OracleEntity dbVo = (OracleEntity) vo;
			nodeDTO.setId(dbVo.getId());
			nodeDTO.setName(dbVo.getAlias());
			nodeDTO.setNodeid(String.valueOf(dbVo.getId()));
			nodeDTO.setBusinessName(getBusinessNameForNode(dbVo.getBid()));
		}
		
		return nodeDTO;
	}
	
	
	public String getBusinessNameForNode(String bid){
		BusinessDao bussdao = new BusinessDao();
	   	List allbuss = null;
		try {
			allbuss = bussdao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   	if(bid == null){
	   		bid="";
	   	}
	   	String id[] = bid.split(",");
	   	List bidlist = new ArrayList();
	   	if(id != null &&id.length>0){
			for(int j=0;j<id.length;j++){
		    		bidlist.add(id[j]);
			}
		}
		String bussName = "";
		if( allbuss.size()>0){
			for(int k=0;k<allbuss.size();k++){
				Business buss = (Business)allbuss.get(k);
				if(bidlist.contains(buss.getId()+"")){
					bussName = bussName + ',' + buss.getName();
				}
			}
		}
		
		return bussName;
	}
	
	public void refreshShareDataGather(){
		NodeGatherIndicatorsDao gatherDao = new NodeGatherIndicatorsDao();
        Hashtable gatherHashtable = new Hashtable();
		try{
			gatherHashtable = gatherDao.getAllGather();
		}catch(Exception e){
			
		}finally{
			gatherDao.close();
			gatherDao = null;
		}
		if(gatherHashtable == null)gatherHashtable = new Hashtable();
		ShareData.setGatherHash(gatherHashtable);
	}
	
	public List<NodeDTO> getNodeListByTypeAndSubtype(String type , String subtype, String bid){
        NodeUtil nodeUtil = new NodeUtil();
        nodeUtil.setBid(bid);
        List<BaseVo> nodelist = nodeUtil.getNodeByTyeAndSubtype(type, subtype);
        List<NodeDTO> list = nodeUtil.conversionToNodeDTO(nodelist);
        return list;
    }

	public List<NodeGatherIndicators> loadAll() {
	    List<NodeGatherIndicators> list = null;
	    NodeGatherIndicatorsDao gatherDao = new NodeGatherIndicatorsDao();
	    try {
	        list = (List<NodeGatherIndicators>) gatherDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            list = new ArrayList<NodeGatherIndicators>();
        }
	    return list;
	}
}
