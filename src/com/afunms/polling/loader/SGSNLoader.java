/**
 * <p>Description:loading db nodes</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-8
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.List;

import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.OthersNode;
import com.afunms.topology.dao.OtherNodeDao;
import com.afunms.topology.model.OtherNode;
import com.afunms.common.base.BaseVo;

public class SGSNLoader extends NodeLoader
{
    public void loading()
    {
    	OtherNodeDao dao = new OtherNodeDao();
    	List list = dao.findByCondition(" where category=92 and managed=1");
    	clearRubbish(list);
    	for(int i=0;i<list.size();i++)
    	{
    		OtherNode vo = (OtherNode)list.get(i);  
    		loadOne(vo);
    	}
    }
    public void clearRubbish(List baseVoList){
    	List nodeList=PollingEngine.getInstance().getSgsnList()   ; //�õ��ڴ��е�list
		for(int index=0;index<nodeList.size();index++){
			if(nodeList.get(index) instanceof  OthersNode){ 
				OthersNode node=( OthersNode)nodeList.get(index);
    			if(baseVoList==null){ 
					nodeList.remove(node); 
    			}else{
    				boolean flag=false;
					for(int j=0;j<baseVoList.size();j++)
					{ 
						OtherNode  hostNode=(OtherNode )baseVoList.get(j);
						if(node.getId()==hostNode.getId()){
							flag=true;
						} 
					} 
					if(!flag){
						nodeList.remove(node);
					}
    			}
			}
		}
	}
    public void loadOne(BaseVo baseVo)
    {
    	OtherNode vo = (OtherNode)baseVo;
    	OthersNode others = new OthersNode();
    	others.setId(vo.getId());
    	others.setAlias(vo.getName());
    	others.setIpAddress(vo.getIpAddress());
    	others.setName(vo.getAlais());
    	others.setManaged(vo.getManaged());
    	others.setSendphone(vo.getSendphone());
    	others.setSendemail(vo.getSendemail());
    	others.setSendmobiles(vo.getSendmobiles());
    	others.setBid(vo.getBid());
    	others.setCategory(92);
    	others.setStatus(0);
    	others.setType("SGSN");
		//---------------���ر����Ӷ���-------------------			
//		PollingEngine.getInstance().addSgsn(others);

		Node node=PollingEngine.getInstance().getSgsnByID(others.getId());
		if(node!=null){ 
			PollingEngine.getInstance().getSgsnList().remove(node);
		} 
		PollingEngine.getInstance().addSgsn(others); 
    }
}