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
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.model.TreeNode;

public class ManageXmlOperator extends XmlOperator {
	private List tempNodeList;

	public ManageXmlOperator() {
	}

	/**
	 * ���ڵ��Ƿ��Ѿ�����
	 */
	public boolean isIdExist(String hostId) {
		if (tempNodeList == null)
			return false;

		boolean exist = false;
		for (int i = 0; i < tempNodeList.size(); i++) {
			XmlInfo xmlInfo = (XmlInfo) tempNodeList.get(i);
			if (xmlInfo.getId().equals(hostId)) {
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
	public void init4editNodes() {
		init4updateXml();
		tempNodeList = new ArrayList();

		List eleNodes = nodes.getChildren();
		int len = eleNodes.size();
		for (int i = 0; i < len; i++) {
			Element element = (Element) eleNodes.get(i);
			XmlInfo xmlInfo = new XmlInfo();
			xmlInfo.setId(element.getChildText("id"));
			xmlInfo.setExist(false);
			tempNodeList.add(xmlInfo);
		}
	}

	/**
	 * �༭�����,ɾ��û�õĽ��
	 */
	public void deleteNodes() {
		if (tempNodeList == null)
			return;

		for (int i = 0; i < tempNodeList.size(); i++) {
			XmlInfo xmlInfo = (XmlInfo) tempNodeList.get(i);
			if (!xmlInfo.isExist())
				deleteNodeByID(xmlInfo.getId());
		}
	}

	/**
	 * ���ӽ��(�����е���Դ������)���豸�����ʱʹ��
	 */
	public void addNode(String nodeId, int index, String category) {
		Node node = PollingEngine.getInstance().getNodeByCategory(category, Integer.parseInt(nodeId.substring(3)));
		if (node == null) {
			SysLogger.error("add a null node=" + nodeId);
			return;
		}
		String eleImage = null;
		if (node.getCategory() == 4)
			eleImage = NodeHelper.getServerTopoImage(((Host) node).getSysOid());
		else
			eleImage = NodeHelper.getTopoImage(node.getCategory());

		addNode(nodeId, node.getCategory(), eleImage, node.getIpAddress(), node
				.getAlias(), String.valueOf(index * 30), "15");
	}

	/**
	 * ���ӽ��(�����е���Դ������)������ͼʱʹ��
	 */
	public void addNode(String nodeId, int index) {
		Node node = null;
		TreeNodeDao treeNodeDao = new TreeNodeDao();
		TreeNode vo = (TreeNode) treeNodeDao.findByNodeTag(nodeId.substring(0,3));
		if(vo!=null&&vo.getName()!=null&&!"".equals(vo.getName())){
			node = PollingEngine.getInstance().getNodeByCategory(vo.getName(), Integer.parseInt(nodeId.substring(3)));
		}
		if (node==null) {
			SysLogger.error("add a null node=" + nodeId);
			return;
		}

		String eleId = String.valueOf(nodeId);
		String eleImage = null;
		if (node.getCategory() == 4)
			eleImage = NodeHelper.getServerTopoImage(((Host) node).getSysOid());
		else
			eleImage = NodeHelper.getTopoImage(node.getCategory());

		addNode(eleId, node.getCategory(), eleImage, node.getIpAddress(), node
				.getAlias(), String.valueOf(index * 30), "15");
	}

	/**
	 * ����ʾ���� yangjun add
	 */
	public void addDemoNode(String nodeId, String categroy, String image,
			String alias, String x, String y) {
		Element eleNode = new Element("node");
		Element eleId = new Element("id");
		Element eleImg = new Element("img");
		Element eleX = new Element("x");
		Element eleY = new Element("y");
		Element eleIp = new Element("ip");
		Element eleAlias = new Element("alias");
		Element eleInfo = new Element("info");
		Element eleMenu = new Element("menu");
		Element relationMap = new Element("relationMap");// ��������ͼ

		eleId.setText(nodeId);
		eleId.setAttribute("category", categroy);
		eleImg.setText(image);
		eleX.setText(x);
		eleY.setText(y);
		SysLogger.info("id: " + nodeId + "  ����:ʾ���豸 ͼƬ:" + image);
		eleIp.setText(alias);
		eleAlias.setText(alias);
		eleInfo.setText("ʾ���豸");
		eleMenu.setText(NodeHelper.getMenuItem(nodeId));
		relationMap.setText("");
		eleNode.addContent(eleId);
		eleNode.addContent(eleImg);
		eleNode.addContent(eleX);
		eleNode.addContent(eleY);
		eleNode.addContent(eleIp);
		eleNode.addContent(eleAlias);
		eleNode.addContent(eleInfo);
		eleNode.addContent(eleMenu);
		eleNode.addContent(relationMap);
		nodes.addContent(eleNode);
	}

	/**
	 * Ϊ�༭������׼��
	 */
	public void init4editLines() {
		init4updateXml();

		root.removeContent(lines);
		lines = new Element("lines");
		root.addContent(lines);
	}

	// ����ʾ����·
	public void addLine(int id,String lineId, String id1, String id2,String width) {
		Element demoLine = new Element("demoLine");
		Element a = new Element("a");
		Element b = new Element("b");
		Element color = new Element("color");
		Element dash = new Element("dash");
		Element lineWidth = new Element("lineWidth");// yangjun add
		Element lineInfo = new Element("lineInfo");// yangjun add
		Element lineMenu = new Element("lineMenu");// yangjun add

		demoLine.setAttribute("id", lineId);
		a.setText(id1);
		b.setText(id2);
		color.setText("blue");
		dash.setText("Solid");
		lineWidth.setText(width);
		lineInfo.setText("ʾ����·");
		lineMenu.setText(NodeHelper.getMenuLine(id,lineId));

		demoLine.addContent(a);
		demoLine.addContent(b);
		demoLine.addContent(color);
		demoLine.addContent(dash);
		demoLine.addContent(lineWidth);
		demoLine.addContent(lineInfo);
		demoLine.addContent(lineMenu);
		demoLines.addContent(demoLine);
	}

	// �޸���·��Ϣ yangjun add
	public void updateLine(String lineId, String tag, String txt) {
		List eleLines = lines.getChildren();
		for (int i = 0; i < eleLines.size(); i++) {
			Element ele = (Element) eleLines.get(i);
			if (ele.getAttributeValue("id").equals(lineId)) {
				ele.getChild(tag).setText(txt);
				break;
			}
		}
	}

	// �޸���·��Ϣ yangjun add
	public void updateAssLine(String lineId, String tag, String txt) {
		List eleLines = assistantLines.getChildren();
		for (int i = 0; i < eleLines.size(); i++) {
			Element ele = (Element) eleLines.get(i);
			if (ele.getAttributeValue("id").equals(lineId)) {
				ele.getChild(tag).setText(txt);
				break;
			}
		}
	}

	// �޸���·��Ϣ yangjun add
	public void updateDemoLine(String lineId, String tag, String txt) {
		List eleLines = demoLines.getChildren();
		for (int i = 0; i < eleLines.size(); i++) {
			Element ele = (Element) eleLines.get(i);
			if (ele.getAttributeValue("id").equals(lineId)) {
				ele.getChild(tag).setText(txt);
				break;
			}
		}
	}

	// �޸Ľڵ���Ϣ yangjun add
	public void updateNode(String nodeId, String tag, String txt) {
		List eleNodes = nodes.getChildren();
		for (int i = 0; i < eleNodes.size(); i++) {
			Element ele = (Element) eleNodes.get(i);
			if (ele.getChildText("id").equals(nodeId)) {
				ele.getChild(tag).setText(txt);
				break;
			}
		}
	}

	// �������Ľڵ�id yangjun add
	public int findMaxNodeId() {
		int id = 0;
		List eleNodes = nodes.getChildren();
		for (int i = 0; i < eleNodes.size(); i++) {
			Element ele = (Element) eleNodes.get(i);
			if (Integer.parseInt(ele.getChildText("id").substring(3)) > id) {
				id = Integer.parseInt(ele.getChildText("id").substring(3));
			}
		}
		return id + 1;
	}

	// ��������ʾ����·id yangjun add
	public int findMaxDemoLineId() {
		int id = 0;
		List eleLines = demoLines.getChildren();
		if (eleLines.size() > 0) {
			for (int i = 0; i < eleLines.size(); i++) {
				Element ele = (Element) eleLines.get(i);
				if (Integer.parseInt(ele.getAttributeValue("id").substring(2)) > id) {
					id = Integer.parseInt(ele.getAttributeValue("id").substring(2));
				}
			}
		}
		return id + 1;
	}
	//���ҵ����ͼ���нڵ������λ��
	public Hashtable getAllXY(){
		Hashtable hash = new Hashtable();
		List eleNodes = nodes.getChildren();
		if (eleNodes.size() > 0) {
			for (int i = 0; i < eleNodes.size(); i++) {
				Element ele = (Element) eleNodes.get(i);
				hash.put(ele.getChildText("id"), ele.getChildText("x")+","+ele.getChildText("y"));
			}
		}
		return hash;
	}
}
