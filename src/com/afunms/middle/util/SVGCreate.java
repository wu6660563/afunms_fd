/*
 * @(#)SVGCreate.java     v1.01, 2013 12 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseVo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.middle.model.SVGLine;
import com.afunms.middle.model.SVGNode;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.ManageXml;

/**
 * ClassName:   SVGCreate.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 3 10:16:29
 */
public class SVGCreate {

	private static final String headBytes = "<%@page contentType=\"text/html; charset=GB2312\"%>\r\n";

	private static final String PATH = ResourceCenter.getInstance().getSysPath() + "/resource/svg/";
	
	private static final String PATH_IMAGE = ResourceCenter.getInstance().getSysPath() + "/resource/svg/";

    private static final String TOPO_PATH = ResourceCenter.getInstance().getSysPath() + "/resource/xml/";

    private static final String TEMPLATE_FILE = "template.xml";

    private static SAXBuilder builder;

    private static Document TEMPLATE_DOCUMENT = null;
    
    /**
     * deviceIdsHash:
     * <p>��������SVG�е�����ͼ�豸ID���Ա�ID�����ڱ�����ʹ�ã�
     *    keyΪ�豸ID��valueΪ�Ա�ID
     * @since   v1.01
     */
    private static Hashtable<String, String> deviceIdsHash = new Hashtable<String, String>();
    
    private static int index = 1;

    static {
        builder = new SAXBuilder();
        TEMPLATE_DOCUMENT = getDocument(PATH + TEMPLATE_FILE);
    }

    /**
     * getDocument:
     * <p>ͨ�� XML ���ƻ�ȡ Document
     *
     * @param   xmlName
     *          - XML ����
     * @return  document
     *          - �ĵ�
     *
     * @since   v1.01
     */
    public static Document getDocument(String xmlName) {
        Document document = null;
        try {
            document = builder.build(new File(xmlName));// ����ĵ�����
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * getDocument:
     * <p>ͨ�� XML ���ƻ�ȡ Document
     *
     * @param   xmlName
     *          - XML ����
     * @return  document
     *          - �ĵ�
     *
     * @since   v1.01
     */
    public static Document getDocument(InputStream inputStream) {
        Document document = null;
        try {
            document = builder.build(inputStream);// ����ĵ�����
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * create:
     * <p>���� {@link ManageXml} ���� SVG DOCUMENT
     *
     * @param manageXml
     * @return
     *
     * @since   v1.01
     */
    public static String createSVG(String xmlName) {
        Document svgDocument = TEMPLATE_DOCUMENT;
        List<Element> childList = svgDocument.getRootElement().getChildren();
        Element SVGDefsElement = null;
        for (Element childElement : childList) {
            if (childElement.getName().equals("defs")) {
                SVGDefsElement = childElement; 
            }
        }
        List<Element> SVGSymbolElmentList = SVGDefsElement.getChildren("symbol");
        List<Element> SVGGElementList = svgDocument.getRootElement().getChildren();
        Element SVGNodeClassElement = null;
        Element SVGLineClassElement = null;
        for (Element element : SVGGElementList) {
            if ("NodeClass".equals(element.getAttributeValue("id"))) {
                SVGNodeClassElement = element;
            } else if ("LineClass".equals(element.getAttributeValue("id"))) {
                SVGLineClassElement = element;
            }
        }
        if (SVGNodeClassElement == null) {
            SVGNodeClassElement = new Element("g");
            SVGNodeClassElement.setAttribute("id", "NodeClass");
            svgDocument.getRootElement().addContent(SVGNodeClassElement);
        }
        if (SVGLineClassElement == null) {
            SVGLineClassElement = new Element("g");
            SVGLineClassElement.setAttribute("id", "LineClass");
            svgDocument.getRootElement().addContent(SVGLineClassElement);
        }
        

        Hashtable<String, SVGNode> allSVGNodeHashtable = new Hashtable<String, SVGNode>();
        Hashtable<String, Element> allSVGSymbolHashtable = new Hashtable<String, Element>();
        
        Hashtable<String, String> indicatorsHash = new Hashtable<String, String>();
        indicatorsHash.put("CPU(%):", "80");
        List<List<Element>> elementKeyList = new ArrayList<List<Element>>();
        List<List<Element>> elementValueList = new ArrayList<List<Element>>();
        
        
        Document topoDocument = getTopoDocument(TOPO_PATH + xmlName);
        List<Element> topoElementlist = topoDocument.getRootElement().getChild("nodes").getChildren("node");
        for (Element topoElement : topoElementlist) {
            SVGNode node = createSVGNode(topoElement);
            if (node == null) {
                continue;
            }
            int nodeIndex = getIndex();
            allSVGNodeHashtable.put(topoElement.getChildText("id"), node);
            Element SVGNodeElement = createSVGNodeElement(node, nodeIndex);
            SVGNodeClassElement.addContent(SVGNodeElement);
            
            Element SVGSymbolElement = allSVGSymbolHashtable.get(node.getLink());
            if (SVGSymbolElement == null) {
                SVGSymbolElement = createSVGSymbolElement(node);
                SVGDefsElement.addContent(SVGSymbolElement);
                allSVGSymbolHashtable.put(SVGSymbolElement.getAttributeValue("id"), SVGSymbolElement);
            }
            deviceIdsHash.put(node.getId(), String.valueOf(nodeIndex));
            
            elementKeyList.add(createStaticText(indicatorsHash, node));
            elementValueList.add(createTextValue(indicatorsHash, node));
        }
        
        Element keyElement = new Element("g");
        keyElement.setAttribute("id", "TextClass");
        for (List<Element> keyList : elementKeyList) {
        	for (Element element : keyList) {
        		keyElement.addContent(element);
			}
		}
        svgDocument.getRootElement().addContent(keyElement);
        
        Element valueElement = new Element("g");
        valueElement.setAttribute("id", "MeasurementClass");
        for (List<Element> valueList : elementValueList) {
        	for (Element element : valueList) {
        		valueElement.addContent(element);
			}
		}
        svgDocument.getRootElement().addContent(valueElement);
        
        topoElementlist = topoDocument.getRootElement().getChild("lines").getChildren("line");
        for (Element topoElement : topoElementlist) {
            SVGLine line = createSVGLine(topoElement, allSVGNodeHashtable);
            Element SVGLineElement = createSVGLineElement(line);
            SVGLineClassElement.addContent(SVGLineElement);
        }
        topoElementlist = topoDocument.getRootElement().getChild("assistant_lines").getChildren("assistant_line");
        for (Element topoElement : topoElementlist) {
            SVGLine line = createSVGLine(topoElement, allSVGNodeHashtable);
            Element SVGLineElement = createSVGLineElement(line);
            SVGLineClassElement.addContent(SVGLineElement);
        }
        topoElementlist = topoDocument.getRootElement().getChild("demoLines").getChildren("demoLine");
        for (Element topoElement : topoElementlist) {
            SVGLine line = createSVGLine(topoElement, allSVGNodeHashtable);
            Element SVGLineElement = createSVGLineElement(line);
            SVGLineClassElement.addContent(SVGLineElement);
        }
        String content = saveDocument(svgDocument);
        return content;
    }
    
    /**
     * createStaticText:
     * <p> ������̬�ı� ����SVG����ʾ��CPU�ȹ̶��ı���Ϣ
     *
     * @param indicatorsHash
     * @param node
     * @return
     *
     * @since   v1.01
     */
    public static List<Element> createStaticText(Hashtable<String, String> indicatorsHash, SVGNode node) {
    	List<Element> eleList = new ArrayList<Element>();
    	Set<String> keySet = indicatorsHash.keySet();
    	for (String string : keySet) {
    		
    		int node_x = Integer.parseInt(node.getX());
    		int node_y = Integer.parseInt(node.getY());
    		
    		Element textElement = new Element("text");
    		textElement.setAttribute("id", String.valueOf(getIndex()));
    		textElement.setAttribute("x", String.valueOf(node_x - 20));
    		textElement.setAttribute("y", String.valueOf(node_y + Integer.parseInt(node.getImageHeight()) + 10));
    		textElement.setAttribute("font-size", "16");
    		textElement.setAttribute("fill", "rgb(0,0,0)");
    		textElement.setAttribute("stroke", "rgb(0,0,0)");
    		textElement.setText(string);
    		
    		eleList.add(textElement);
		}
    	return eleList;
    }
    
    /**
     * createTextValue:
     * <p> �����ı���Ϣֵ
     *
     * @param indicatorsHash
     * @param node
     * @return
     *
     * @since   v1.01
     */
    public static List<Element> createTextValue(Hashtable<String, String> indicatorsHash, SVGNode node){
    	List<Element> eleList = new ArrayList<Element>();
    	Set<String> keySet = indicatorsHash.keySet();
    	for (String string : keySet) {
    		Element useElement = new Element("g");
    		useElement.setAttribute("id", String.valueOf(getIndex()));
    		int node_x = Integer.parseInt(node.getX());
    		int node_y = Integer.parseInt(node.getY());
    		
    		Element textElement = new Element("text");
    		textElement.setAttribute("x", String.valueOf(node_x + 40));
    		textElement.setAttribute("y", String.valueOf(node_y + Integer.parseInt(node.getImageHeight()) + 10));
    		textElement.setAttribute("font-size", "16");
    		textElement.setAttribute("fill", "rgb(0,0,0)");
    		textElement.setAttribute("stroke", "rgb(0,0,0)");
    		textElement.setText(indicatorsHash.get(string));
    		useElement.addContent(textElement);
    		
    		eleList.add(useElement);
		}
    	return eleList;
    }
    

    /**
     * createSVGNode:
     * <p> ��������ͼ�ڵ㴴�� SVGNode
     *
     * @param   topoElement
     *          - ����ͼ�ڵ�
     * @return  {@link SVGNode}
     *          - SVG �ڵ�
     *
     * @since   v1.01
     */
    public static SVGNode createSVGNode(Element topoElement) {
        String topoId = topoElement.getChildText("id");
        String nodeTag = topoId.substring(0, 3);
        String nodeId = topoId.substring(3);

        String id = "";
        String x = topoElement.getChildText("x").replace("px", "");
        String y = topoElement.getChildText("y").replace("px", "");
        String link = "";
        String image = "";
        if (!nodeTag.contains("hin")) {
            BaseVo baseVo = getBaseVo(nodeTag, nodeId);
            if (baseVo == null) {
            	return null;
            }
            NodeDTO nodeDTO = getNodeDTO(baseVo);
            if (nodeDTO == null) {
                return null;
            }
            
            NodeAlarmService service = new NodeAlarmService();
            int alarmLevel = service.getMaxAlarmLevel(nodeDTO);
            if (alarmLevel > 0) {
                alarmLevel = 1;
            }
            image = getImage(baseVo, nodeDTO);
            id = nodeDTO.getType() + ":" + nodeDTO.getSubtype() + ":" + nodeDTO.getNodeid();
            link = nodeDTO.getType() + ":" + nodeDTO.getSubtype() + "@" + (alarmLevel + 1);
            image = nodeDTO.getType() + "_" + image + "_" + alarmLevel + ".png";
        } else {
            id = topoElement.getChildText("id");
            String category = topoElement.getChild("id").getAttributeValue("category");
            link = category + ":" + category + "@" + 1;
            if ("����ǽ".equals(category)) {
            	image = "firewall_0.png";
            } else if ("·����".equals(category)) {
            	image = "net_router_0.png";
            } else if ("������".equals(category)) {
            	image = "net_switch_0.png";
            } else if ("������".equals(category)) {
            	image = "host_server_0.png";
            } else if ("WAP".equals(category)) {
            	image = "wap_0.png";
            } else if ("���㽻����".equals(category)) {
            	image = "net_switch1_0.png";
            } else if ("��ͼ".equals(category)) {
            	image = "area_0.png";
            } else {
            	System.out.println("===========" + category);
            }
        }
        
        int[] size = getImageSize(image);
        
        SVGNode node = new SVGNode();
        node.setId(id);
        node.setX(x);
        node.setY(y);
        node.setLink(link);
        node.setImage(image);
        node.setImageWidth(String.valueOf(size[0]));
        node.setImageHeight(String.valueOf(size[1]));
        return node;
    }

    /**
     * createSVGNode:
     * <p>���� SVGNode ���� SVG�ĵ��ڵ�
     *
     * @param   node
     *          - SVGNode
     * @return  {@link Element}
     *          - SVG�ĵ��ڵ�
     *
     * @since   v1.01
     */
    public static Element createSVGNodeElement(SVGNode node,int newId) {

        Element element = new Element("g");
        element.setAttribute("id", String.valueOf(newId));
        
        Element useElement = new Element("use");
        useElement.setAttribute("x", node.getX());
        useElement.setAttribute("y", node.getY());
        useElement.setAttribute("width", node.getImageWidth());
        useElement.setAttribute("height", node.getImageHeight());
        useElement.setAttribute("xlinkHref", "#" + node.getLink());
        element.addContent(useElement);
        
        return element;
    }

    /**
     * createSVGSymbolElement:
     * <p>���� SVG�ڵ� ���� SVG�ĵ��� Symbol �ڵ�
     *
     * @param   node
     *          - SVG �ڵ�
     * @return  {@link Element}
     *          - SVG�ĵ��� Symbol �ڵ�
     *
     * @since   v1.01
     */
    public static Element createSVGSymbolElement(SVGNode node) {
        Element element = new Element("symbol");
        element.setAttribute("id", node.getLink());
        element.setAttribute("viewBox", "0 0 " +  node.getImageWidth() + " " + node.getImageHeight());

        Element imageElement = new Element("image");
        imageElement.setAttribute("width", node.getImageWidth());
        imageElement.setAttribute("height", node.getImageHeight());
        imageElement.setAttribute("x", "0");
        imageElement.setAttribute("y", "0");
        imageElement.setAttribute("xlinkHref", node.getImage());

        element.addContent(imageElement);
        return element;
    }

    public static SVGLine createSVGLine(Element topoElement, Hashtable<String, SVGNode> allSVGNodeHashtable) {
        String id = topoElement.getAttributeValue("id");
        String color = topoElement.getChildText("color");
        String a = topoElement.getChildText("a");
        String b = topoElement.getChildText("b");
        if ("green".equalsIgnoreCase(color)) {
            color = "rgb(0,255,0)";
        } else if ("red".equalsIgnoreCase(color)) {
            color = "rgb(255,0,0)";
        } else if ("blue".equalsIgnoreCase(color)) {
            color = "rgb(0,0,255)";
        }
        String width = topoElement.getChildText("lineWidth");

        SVGNode startSVGNode = allSVGNodeHashtable.get(a);
        String x = startSVGNode.getX();
        String y = startSVGNode.getY();
        String imageWidth = startSVGNode.getImageWidth();
        String imageHeight = startSVGNode.getImageHeight();

        String path = "M " + (Integer.valueOf(x) + Integer.valueOf(imageWidth) / 2);
        path = path + " " + (Integer.valueOf(y) + Integer.valueOf(imageHeight) / 2);
        
        SVGNode endSVGNode = allSVGNodeHashtable.get(b);
        x = endSVGNode.getX();
        y = endSVGNode.getY();
        imageWidth = endSVGNode.getImageWidth();
        imageHeight = endSVGNode.getImageHeight();

        path = path + "L " + (Integer.valueOf(x) + Integer.valueOf(imageWidth) / 2);
        path = path + " " + (Integer.valueOf(y) + Integer.valueOf(imageHeight) / 2);
        
        int lineIndex = getIndex();

        SVGLine line = new SVGLine();
        line.setId(String.valueOf(lineIndex));
        deviceIdsHash.put(id, String.valueOf(lineIndex));
        line.setColor(color);
        line.setWidth(width);
        line.setEndSVGNode(allSVGNodeHashtable.get(id));
        line.setStartSVGNode(allSVGNodeHashtable.get(id));
        line.setPath(path);
        return line;
    }

    public static Element createSVGLineElement(SVGLine line) {
        Element element = new Element("g");
        element.setAttribute("id", line.getId());
        
        Element pathElement = new Element("path");
        pathElement.setAttribute("stroke-width", line.getWidth());
        pathElement.setAttribute("d", line.getPath());
        pathElement.setAttribute("stroke", line.getColor());
        
        element.addContent(pathElement);

        return element;
    }

    public static BaseVo getBaseVo(String nodeTag, String nodeId) {
        NodeUtil nodeUtil = new NodeUtil();
        List<BaseVo> list = nodeUtil.getByNodeTag(nodeTag, null);
        BaseVo baseVo = null;
        for (BaseVo baseVoPer : list) {
        	NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(baseVoPer);
        	if (nodeDTO.getNodeid().equals(nodeId)) {
        		baseVo = baseVoPer;
                break;
            }
		}
        return baseVo;
    }

    public static NodeDTO getNodeDTO(BaseVo baseVo) {
        NodeUtil nodeUtil = new NodeUtil();
    	return nodeUtil.conversionToNodeDTO(baseVo);
    }

    public static NodeDTO getNodeDTO(String nodeTag, String nodeId) {
        NodeUtil nodeUtil = new NodeUtil();
        List<BaseVo> list = nodeUtil.getByNodeTag(nodeTag, null);
        List<NodeDTO> nodeDTOList = nodeUtil.conversionToNodeDTO(list);
        NodeDTO nodeDTO = null;
        for (NodeDTO nodeDTOPer : nodeDTOList) {
            if (nodeDTOPer.getNodeid().equals(nodeId)) {
                nodeDTO = nodeDTOPer;
                break;
            }
        }
        return nodeDTO;
    }

    public static String getImage(BaseVo baseVo, NodeDTO nodeDTO) {
    	String image = null;
    	int category = 0;
    	if (baseVo instanceof HostNode) {
			HostNode hostNode = (HostNode) baseVo;
			category = hostNode.getCategory();
			if (category == 1) {
				image = "router";
			} else if (category == 3) {
				image = "switch";
			} 
		}
    	if (image == null) {
    		image = nodeDTO.getSubtype();
    	}
    	return image;
    }
    public static int[] getImageSize(String imageName) {
        int[] size = new int[2];
        File picture = new File(PATH_IMAGE + imageName);
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(picture);
            BufferedImage sourceImg =ImageIO.read(fileInputStream);
            if (sourceImg != null) {
                size[0] = sourceImg.getWidth();
                size[1] = sourceImg.getHeight();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public static Document getTopoDocument(String xmlName) {
    	FileInputStream fileInputStream = null;
    	Document document = null;
		try {
			File file = new File(xmlName);
			fileInputStream = new FileInputStream(file);
			fileInputStream.skip(headBytes.getBytes().length);
			document = getDocument(fileInputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fileInputStream != null)fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return document;
    }

    public static String saveDocument(Document document) {
    	String content = "";
    	try {
			Format format = Format.getCompactFormat();
			format.setIndent("	");
			XMLOutputter serializer = new XMLOutputter(format);
			FileOutputStream fos = new FileOutputStream(PATH + "������ά_��ɽ�ص�_network.svg");
			serializer.output(document, fos);
			fos.close();
			FileInputStream fileInputStream = new FileInputStream(PATH + "������ά_��ɽ�ص�_network.svg");
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String read = null;
			
			while ( (read = bufferedReader.readLine()) != null) {
				content += read + "\n";
			}
			content = content.replaceAll("xlinkHref", "xlink:href");
			content = content.replaceAll("xmlns=\"\" ", "");
//			System.out.println(content);
			FileOutputStream fileOutputStream = new FileOutputStream(PATH + "������ά_��ɽ�ص�_network.svg");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			outputStreamWriter.write(content);
			outputStreamWriter.flush();
			outputStreamWriter.close();
			fileOutputStream.close();
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
    }
    
    public static int getIndex(){
    	return index++;
    }

    /**
     * main:
     * <p>
     *
     * @param args
     *
     * @since   v1.01
     */
    public static void main(String[] args) {

    }
}

