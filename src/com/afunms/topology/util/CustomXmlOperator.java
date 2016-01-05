/**
 * <p>Description:operate topo xml</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2006-12-25
 */

package com.afunms.topology.util;

import java.util.*;

import org.jdom.Element;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.*;
import com.afunms.polling.base.*;
import com.afunms.polling.node.*;

public class CustomXmlOperator extends XmlOperator
{
	private List tempNodeList;
	
	public CustomXmlOperator()
	{
	}
		
	/**
	 * ���ڵ��Ƿ��Ѿ�����
	 */
	public boolean isIdExist(String hostId)
	{
		if(tempNodeList==null) return false;
		
		boolean exist = false;
		for(int i=0;i<tempNodeList.size();i++)
		{
			XmlInfo xmlInfo = (XmlInfo)tempNodeList.get(i);
			if(xmlInfo.getId().equals(hostId))
			{
		        exist = true;
		        xmlInfo.setExist(true);
		        break;
			}
		}
		return exist;
	}
	
	/**
	 * Ϊ�༭�����׼��,�����н��Ĳ�����Ϣ�ȷŵ��ڴ���
	 */
	public void init4editNodes()
	{
        init4updateXml();
		tempNodeList = new ArrayList();
		
		List eleNodes = nodes.getChildren();
		int len = eleNodes.size();
		for(int i = 0;i < len; i ++)
		{
			Element element = (Element)eleNodes.get(i);
			XmlInfo xmlInfo = new XmlInfo();
			xmlInfo.setId(element.getChildText("id"));
			xmlInfo.setExist(false);
			tempNodeList.add(xmlInfo);
		}	
	}
		
	/**
	 * �༭�����,ɾ��û�õĽ��
	 */
	public void deleteNodes()
	{
		if(tempNodeList==null) return;
		
		for(int i=0;i<tempNodeList.size();i++)
		{
			XmlInfo xmlInfo = (XmlInfo)tempNodeList.get(i);
			if(!xmlInfo.isExist())
				deleteNodeByID(xmlInfo.getId());
		}
	}
	
					 		
	/**
	 * ���ӽ��(�����е���Դ������)
	 */
	public void addNode(int nodeId,int index)
	{
		Node node = PollingEngine.getInstance().getNodeByID(nodeId);	
		if(node == null)
		{
			SysLogger.error("add a null node=" + nodeId);
			return;
		}
		
		String eleId = String.valueOf(nodeId);
		String eleImage = null;
		if(node.getCategory()==4)
		   eleImage = NodeHelper.getServerTopoImage(((Host)node).getSysOid());
	    else
	       eleImage = NodeHelper.getTopoImage(node.getCategory());	
		
		addNode(eleId,node.getCategory(), eleImage,node.getIpAddress(),node.getAlias(),String.valueOf(index*30),"15");				
	}
		
	/**
	 * Ϊ�༭������׼��
	 */
	public void init4editLines()
	{
		init4updateXml();
		
		root.removeContent(lines);
		lines = new Element("lines");
		root.addContent(lines);							
	}
	
	//��ͼ�ϴ�����·
	public void addLine(String id1, String id2)
	{
		Element line = new Element("line");
	  	Element a = new Element("a");
		Element b = new Element("b");
		Element color = new Element("color");
		Element dash = new Element("dash");
		Element lineInfo = new Element("lineInfo");//yangjun add
		Element lineMenu = new Element("lineMenu");//yangjun add
		
		a.setText(id1);
		b.setText(id2);
	    color.setText("blue");
		dash.setText("Solid");
		lineInfo.setText("��·:" + id1+"_" + id2 + "<br>���ID:"+ id1 +"<br>�յ�ID:" + id2); 		
//	    /lineMenu.setText(NodeHelper.getMenuLine(id1,id2));
		
		line.addContent(a);
		line.addContent(b);
		line.addContent(color);
		line.addContent(dash);
		line.addContent(lineInfo);
	    line.addContent(lineMenu);
		lines.addContent(line);		
	}
	
	//ɾ����ͼ��· yangjun add
	public void deleteLine(String sid,String eid)
	{
		List eleLines = lines.getChildren();
		int len = eleLines.size() - 1;
		for(int i=len; i>=0; i--) //����ֻ���ý���������ܳ���
		{			
			Element line = (Element)eleLines.get(i);
			if((line.getChildText("a").equals(sid)&&line.getChildText("b").equals(eid))||
					(line.getChildText("b").equals(sid)&&line.getChildText("a").equals(eid)))		
			   line.getParentElement().removeContent(line);			
		}				
	}
}
