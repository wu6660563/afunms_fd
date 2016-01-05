package com.afunms.chart.amchart.manage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jfree.data.xy.XYSeries;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.model.User;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class AmChartManage extends BaseManager implements ManagerInterface{

	public String execute(String action) { 
	   if(action.equals("createAmChartdoc")){
	    	return createAmChartdoc();
	    } 
	    if(action.equals("jfreeChart")){
	    	return jfreeChart();
	    }  
	    if(action.equals("createImgAmChart")){
	    	return createImgAmChart();
	    } 
	    if(action.equals("portDetailImage")){
	    	return  portDetailImage();
	    } 
	    if(action.equals("portDetailDoc")){
	    	return  portDetailDoc();
	    }   
	    if(action.equals("bandWidthDetailImage")){
	    	return  bandWidthDetailImage();
	    } 
	    if(action.equals("bandWidthDetailDoc")){
	    	return  bandWidthDetailDoc();
	    }   
	    if(action.equals("cpuDetailImage")){
	    	return  cpuDetailImage();
	    } 
	    if(action.equals("cpuDetailDoc")){
	    	return  cpuDetailDoc();
	    }   
	    if(action.equals("memoryDetailImage")){
	    	return  memoryDetailImage();
	    } 
	    if(action.equals("memoryDetailDoc")){
	    	return  memoryDetailDoc();
	    }   
	    setErrorCode(ErrorMessage.ACTION_NO_FOUND);
	    return null; 
	}  
	
	public String createAmChartdoc() {
		String file = "/temp/amchart.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createAmChartDocContext(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	public void createAmChartDocContext(String file) throws DocumentException,
			IOException {
		String ip=request.getParameter("ip"); 
		String ifindex=request.getParameter("ifindex"); 
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",
				BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("�˿�ʵʱ���");
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		
		
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");

		Table firstTable = new Table(4);
		int width[] = { 35, 50, 35, 50};
		firstTable.setWidths(width);
		 firstTable.setWidth(100); // ռҳ���� 90%
		//firstTable.setAutoFillEmptyCells(true); // �Զ�����
		firstTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ 
		firstTable.setBorderWidth(1); // �߿���
		firstTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		firstTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		firstTable.setSpacing(0);// ����Ԫ��֮��ļ��
		firstTable.setBorder(2);// �߿�
		firstTable.endHeaders();
 
		Cell cell1 = new Cell("IP��ַ: ");
		Cell cell2 = new Cell(ip);
		Cell cell3 = new Cell("�˿�����: ");   
		Cell cell4 = new Cell(ifindex);    
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER); 
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER); 
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER); // ����
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER); 
		firstTable.addCell(cell1);
		firstTable.addCell(cell2);
		firstTable.addCell(cell3);
		firstTable.addCell(cell4);
		document.add(firstTable);
		
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		String imgName= user.getUserid() + "_" + ip +"_"+ ifindex+"_port.png"; 
		Image img = Image.getInstance(ResourceCenter.getInstance().getSysPath()+"resource/image/jfreechart/"+ imgName);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.ALIGN_CENTER);// ����ͼƬ��ʾλ�� 
	  
		document.add(img);
		 
		Paragraph title1 = new Paragraph(""); //���һ���ո�
		title.setAlignment(Element.ALIGN_CENTER); 
		document.add(title1);
		
		//TODO �ڶ��ű����
		
		int coloumNum=3;
		Table secondTable = new Table(coloumNum); 
		int width2[] =new int[coloumNum];
		for(int i=0;i<coloumNum;i++){
			width2[0]=50;
			if(i!=0){ 
				width2[i]=200;
			}
		}
		secondTable.setWidths(width2); 
		secondTable.setWidth(100); // ռҳ���� 90% 
		//secondTable.setAutoFillEmptyCells(true);
		secondTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		secondTable.setAutoFillEmptyCells(true); // �Զ�����
		secondTable.setBorderWidth(1); // �߿���
		secondTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		secondTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		secondTable.setSpacing(0);// ����Ԫ��֮��ļ��
		secondTable.setBorder(2);// �߿�
		secondTable.endHeaders(); 
		
		Cell b=new Cell("ʱ��");
		secondTable.addCell(b);
		Cell secondCell1=new Cell("�������");
		secondTable.addCell(secondCell1);
		Cell secondCell2=new Cell("��������");
		secondTable.addCell(secondCell2);
		for(int i=0;i<30;i++){
			
			secondTable.addCell(value1[i][0]); 
			String temp=value2[0][i];
			if(temp.equals("null")){temp="";}
			secondTable.addCell(temp); //������� 
			String temp2=value2[1][i];
			if(temp2.equals("null")){temp2="";}
			secondTable.addCell(temp2);//�������� 
		}  
		document.add(secondTable); 
		
		document.close();
	}
	public String jfreeChart(){
		amchartDataRead(); 
		return "/test1.jsp";
    }

	
	SAXBuilder builder = new SAXBuilder();; 
	String [][] value1=new String[30][2];;
	String [][] value2 =new String[2][30];  
	public String portDetailImage(){ 
		String title="����";
		String type="port"; 
		String xName1="�������";
		String xName2="��������";
		String xUnit="ʱ���";
		String yName="ֵ";
		String yUnit="kb";
		createJfreeChartImage(title,type,xName1,xName2,xUnit,yName,yUnit);
		return "/test1.jsp";
	}	 
	public String portDetailDoc(){  
		String docTitle="����";
		String type="port"; 
		String secondTableTitle1="ʱ��";
		String secondTableTitle2="�������";
		String secondTableTitle3="��������"; 
		String fileName =createDocument(docTitle, type, secondTableTitle1, secondTableTitle2, secondTableTitle3) ;  
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public String bandWidthDetailImage(){ 
		String title="����";
		String type="bandwidth"; 
		String xName1="��ڴ���������";
		String xName2="���ڴ���������";
		String xUnit="ʱ���";
		String yName="ֵ";
		String yUnit="%";
		createJfreeChartImage(title,type,xName1,xName2,xUnit,yName,yUnit);
		return "/test1.jsp";
	}	 
	public String bandWidthDetailDoc(){  
		String docTitle="����������";
		String type="bandwidth"; 
		String secondTableTitle1="ʱ���";
		String secondTableTitle2="��ڴ���������";
		String secondTableTitle3="���ڴ���������"; 
		String fileName =createDocument(docTitle, type, secondTableTitle1, secondTableTitle2, secondTableTitle3) ;  
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	public String cpuDetailImage(){ 
		String title="CPUʵʱ������";
		String type="cpu"; 
		String xName1=""; 
		String xUnit="ʱ���";
		String yName="ֵ";
		String yUnit="%";
		createJfreeChartImage(title,type,xName1,xUnit,yName,yUnit);
		return "/test1.jsp";
	}	 
	public String cpuDetailDoc(){  
		String docTitle="CPUʵʱ������";
		String type="cpu"; 
		String secondTableTitle1="ʱ���";
		String secondTableTitle2="������"; 
		String fileName =createDocument(docTitle, type, secondTableTitle1, secondTableTitle2) ;  
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	public String memoryDetailImage(){ 
		String title="�ڴ�ʵʱ������";
		String type="memory"; 
		String xName1=""; 
		String xUnit="ʱ���";
		String yName="ֵ";
		String yUnit="%";
		createJfreeChartImage(title,type,xName1,xUnit,yName,yUnit);
		return "/test1.jsp";
	}	 
	public String memoryDetailDoc(){  
		String docTitle="�ڴ�ʵʱ������";
		String type="memory"; 
		String secondTableTitle1="ʱ���";
		String secondTableTitle2="������"; 
		String fileName =createDocument(docTitle, type, secondTableTitle1, secondTableTitle2) ;  
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	
	public void createJfreeChartImage(String title,String type,String xName1,String xName2,String xUnit,String yName,String yUnit){ 

		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex"); 
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		try { 
			String outImgName=user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type;
			String xmlFileName = user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type+".xml";
			String fileName= ResourceCenter.getInstance().getSysPath()+"/amcharts_data/"+xmlFileName; 
			org.jdom.Document doc = builder.build(new File(fileName));  
        	List series= doc.getRootElement().getChild("series").getContent(); 
        	for(int i=0;i<series.size();i++){
        		org.jdom.Element ei =(org.jdom.Element)series.get(i);
        		value1[i][0]=ei.getAttributeValue("xid");  
        		value1[i][1]=ei.getText();
        	} 
        	List graphList= doc.getRootElement().getChild("graphs").getContent() ; 
        	for(int k=0;k<graphList.size();k++){
        		org.jdom.Element ek = (org.jdom.Element)graphList.get(k); 
        		List childList = ek.getContent();  
        		for(int j=0;j<childList.size();j++){
        			org.jdom.Element ej =(org.jdom.Element)childList.get(j);
        			value2[k][j]=ej.getText(); 
        		}
        	}
        	int XYSeriesSize=2; 
	   		XYSeries [] xs=new XYSeries[XYSeriesSize];
	   		for(int index=0;index< xs.length;index++)
	   		{
	   			String name=xName1;
	   			if(index==1)
	   			{
	   				name=xName2;
	   			}
	   			xs[index]=new XYSeries(""+name+"");
	   			for(int i=0;i<30;i++){
	   				double x=0;
	   				double y=0;
	   				if(value1[i][0]!=null&&value2[index][i]!=null){
	   					x=Double.parseDouble(value1[i][0].equals("null")?"0":value1[i][0]);
	   					y=Double.parseDouble(value2[index][i].equals("null")?"0":value2[index][i]);
	   				} 
	   				xs[index].add(x,y);
	   			} 
	   		} 
	   		ChartGraph cg=new ChartGraph();	  
	   		cg.xywave(xs, xUnit, yUnit, title, outImgName, 693, 300) ; 
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}          
	}
	public void createJfreeChartImage(String title,String type,String xName1,String xUnit,String yName,String yUnit){ 

		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex"); 
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		try { 
			String outImgName=user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type;
			String xmlFileName = user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type+".xml";
			String fileName= ResourceCenter.getInstance().getSysPath()+"/amcharts_data/"+xmlFileName; 
			org.jdom.Document doc = builder.build(new File(fileName));  
        	List series= doc.getRootElement().getChild("series").getContent(); 
        	for(int i=0;i<series.size();i++){
        		org.jdom.Element ei =(org.jdom.Element)series.get(i);
        		value1[i][0]=ei.getAttributeValue("xid");  
        		value1[i][1]=ei.getText();
        	} 
        	List graphList= doc.getRootElement().getChild("graphs").getContent() ; 
        	for(int k=0;k<graphList.size();k++){
        		org.jdom.Element ek = (org.jdom.Element)graphList.get(k); 
        		List childList = ek.getContent();  
        		for(int j=0;j<childList.size();j++){
        			org.jdom.Element ej =(org.jdom.Element)childList.get(j);
        			value2[k][j]=ej.getText(); 
        		}
        	}
        	int XYSeriesSize=1; 
	   		XYSeries [] xs=new XYSeries[XYSeriesSize];
	   		for(int index=0;index< xs.length;index++)
	   		{
	   			String name=xName1; 
	   			xs[index]=new XYSeries(""+name+"");
	   			for(int i=0;i<30;i++){
	   				double x=0;
	   				double y=0;
	   				if(value1[i][0]!=null&&value2[index][i]!=null){
	   					x=Double.parseDouble(value1[i][0].equals("null")?"0":value1[i][0]);
	   					y=Double.parseDouble(value2[index][i].equals("null")?"0":value2[index][i]);
	   				} 
	   				xs[index].add(x,y);
	   			} 
	   		} 
	   		ChartGraph cg=new ChartGraph();	  
	   		cg.xywave(xs, xUnit, yUnit, title, outImgName, 693, 300) ; 
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}          
	}
	public String createDocument(String docTitle,String type,String secondTableTitle1,String secondTableTitle2){ 

		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex"); 
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		String fileName = "temp/"+type+".doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileFullPath = ResourceCenter.getInstance().getSysPath() + fileName;// ��ȡϵͳ�ļ���·��  
		try{ 
			String imgName= user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type+".png"; 
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			RtfWriter2.getInstance(document, new FileOutputStream(fileFullPath));
			document.open();
			BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",BaseFont.NOT_EMBEDDED);// ������������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);// ����������
			Font contextFont = new Font(bfChinese, 10, Font.NORMAL);// ���������� 
			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black); 
			Paragraph title = new Paragraph(docTitle);
			title.setAlignment(Element.ALIGN_CENTER); // ���ñ����ʽ���뷽ʽ
			document.add(title);
			//TODO ��һ�ű����
			Table firstTable = new Table(4);
			int width[] = { 35, 50, 35, 50};
			firstTable.setWidths(width);
			firstTable.setWidth(100); // ռҳ���� 90%
			//firstTable.setAutoFillEmptyCells(true); // �Զ�����
			firstTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ 
			firstTable.setBorderWidth(1); // �߿���
			firstTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			firstTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			firstTable.setSpacing(0);// ����Ԫ��֮��ļ��
			firstTable.setBorder(2);// �߿�
			firstTable.endHeaders(); 
			Cell cell1 = new Cell("IP��ַ: ");
			Cell cell2 = new Cell(ip);
			Cell cell3 = new Cell("�˿�����: ");   
			Cell cell4 = new Cell(ifindex);    
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER); 
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER); 
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER); // ����
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER); 
			firstTable.addCell(cell1);
			firstTable.addCell(cell2);
			firstTable.addCell(cell3);
			firstTable.addCell(cell4);
			document.add(firstTable);
			 
			Image img = Image.getInstance(ResourceCenter.getInstance().getSysPath()+"resource/image/jfreechart/"+ imgName);
			img.setAbsolutePosition(0, 0);
			img.setAlignment(Image.ALIGN_CENTER);// ����ͼƬ��ʾλ�� 
		  
			document.add(img);
			 
			Paragraph title1 = new Paragraph(""); //���һ���ո�
			title.setAlignment(Element.ALIGN_CENTER); 
			document.add(title1);
			
			//TODO �ڶ��ű����
			int coloumNum=2;
			Table secondTable = new Table(coloumNum); 
			int width2[] =new int[coloumNum];
			for(int i=0;i<coloumNum;i++){
				width2[0]=50;
				if(i!=0){ 
					width2[i]=200;
				}
			}
			secondTable.setWidths(width2); 
			secondTable.setWidth(100); // ռҳ���� 90% 
			//secondTable.setAutoFillEmptyCells(true);
			secondTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			secondTable.setAutoFillEmptyCells(true); // �Զ�����
			secondTable.setBorderWidth(1); // �߿���
			secondTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			secondTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			secondTable.setSpacing(0);// ����Ԫ��֮��ļ��
			secondTable.setBorder(2);// �߿�
			secondTable.endHeaders(); 
			Cell b=new Cell(secondTableTitle1);
			secondTable.addCell(b);
			Cell secondCell1=new Cell(secondTableTitle2);
			secondTable.addCell(secondCell1); 
			for(int i=0;i<30;i++){ 
				secondTable.addCell(value1[i][0]); 
				String temp=value2[0][i];
				if(temp.equals("null")){temp="";}
				secondTable.addCell(temp); //�������  
			}  
			document.add(secondTable); 
			
			
			document.close();
		}catch(Exception e){
			SysLogger.info(e.getMessage());
			e.printStackTrace(); 
		}
		return fileFullPath;
	}
	
	public String createDocument(String docTitle,String type,String secondTableTitle1,String secondTableTitle2,String secondTableTitle3){ 

		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex"); 
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		String fileName = "temp/"+type+".doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileFullPath = ResourceCenter.getInstance().getSysPath() + fileName;// ��ȡϵͳ�ļ���·��  
		try{ 
			String imgName= user.getUserid() + "_" + ip +"_"+ ifindex+"_"+type+".png"; 
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			RtfWriter2.getInstance(document, new FileOutputStream(fileFullPath));
			document.open();
			
			BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",BaseFont.NOT_EMBEDDED);// ������������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);// ����������
			Font contextFont = new Font(bfChinese, 10, Font.NORMAL);// ���������� 
			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black); 
			Paragraph title = new Paragraph(docTitle);
			title.setAlignment(Element.ALIGN_CENTER); // ���ñ����ʽ���뷽ʽ
			document.add(title);
			//���� Table ���
			Table firstTable = new Table(4);
			int width[] = { 35, 50, 35, 50};
			firstTable.setWidths(width);
			firstTable.setWidth(100); // ռҳ���� 90%
			//firstTable.setAutoFillEmptyCells(true); // �Զ�����
			firstTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ 
			firstTable.setBorderWidth(1); // �߿���
			firstTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			firstTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			firstTable.setSpacing(0);// ����Ԫ��֮��ļ��
			firstTable.setBorder(2);// �߿�
			firstTable.endHeaders(); 
			Cell cell1 = new Cell("IP��ַ: ");
			Cell cell2 = new Cell(ip);
			Cell cell3 = new Cell("�˿�����: ");   
			Cell cell4 = new Cell(ifindex);    
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER); 
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER); 
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER); // ����
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER); 
			firstTable.addCell(cell1);
			firstTable.addCell(cell2);
			firstTable.addCell(cell3);
			firstTable.addCell(cell4);
			document.add(firstTable);
			 
			Image img = Image.getInstance(ResourceCenter.getInstance().getSysPath()+"resource/image/jfreechart/"+ imgName);
			img.setAbsolutePosition(0, 0);
			img.setAlignment(Image.ALIGN_CENTER);// ����ͼƬ��ʾλ�� 
		  
			document.add(img);
			 
			Paragraph title1 = new Paragraph(""); //���һ���ո�
			title.setAlignment(Element.ALIGN_CENTER); 
			document.add(title1);
			
			//TODO �ڶ��ű����
			
			int coloumNum=3;
			
			Table secondTable = new Table(coloumNum); 
			int width2[] =new int[coloumNum];
			for(int i=0;i<coloumNum;i++){
				width2[0]=50;
				if(i!=0){ 
					width2[i]=200;
				}
			}
			secondTable.setWidths(width2); 
			secondTable.setWidth(100); // ռҳ���� 90% 
			//secondTable.setAutoFillEmptyCells(true);
			secondTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			secondTable.setAutoFillEmptyCells(true); // �Զ�����
			secondTable.setBorderWidth(1); // �߿���
			secondTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			secondTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			secondTable.setSpacing(0);// ����Ԫ��֮��ļ��
			secondTable.setBorder(2);// �߿�
			secondTable.endHeaders(); 
			
			Cell b=new Cell(secondTableTitle1);
			secondTable.addCell(b);
			Cell secondCell1=new Cell(secondTableTitle2);
			secondTable.addCell(secondCell1);
			Cell secondCell2=new Cell(secondTableTitle3);
			secondTable.addCell(secondCell2);
			for(int i=0;i<30;i++){ 
				secondTable.addCell(value1[i][0]); 
				String temp=value2[0][i];
				if(temp.equals("null")){temp="";}
				secondTable.addCell(temp); //������� 
				String temp2=value2[1][i];
				if(temp2.equals("null")){temp2="";}
				secondTable.addCell(temp2);//�������� 
			}  
			document.add(secondTable); 
			
			document.close();
		}catch(Exception e){
			SysLogger.info(e.getMessage());
			e.printStackTrace(); 
		}
		return fileFullPath;
	}
	
	public void amchartDataRead(){
		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex"); 
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		Hashtable ajaxManagerMap = new Hashtable(); 
		String outImgName= user.getUserid() + "_" + ip +"_"+ ifindex+"_port";
		String title="�˿�����";
		String xmlFileName = user.getUserid() + "_" + ip +"_"+ ifindex+"_port.xml";
		String fileName= ResourceCenter.getInstance().getSysPath()+"/amcharts_data/"+xmlFileName; 
		try { 
			org.jdom.Document doc = builder.build(new File(fileName)); 
	        	List series= doc.getRootElement().getChild("series").getContent(); 
	        	for(int i=0;i<series.size();i++){
	        		org.jdom.Element ei =(org.jdom.Element)series.get(i);
	        		value1[i][0]=ei.getAttributeValue("xid");  
	        		value1[i][1]=ei.getText();
	        	}
	        	List graphList=   doc.getRootElement().getChild("graphs").getContent(); 
	        	//List graphList =  parentDocument.getRootElement().getChildren("graph"); 
	        	for(int k=0;k<graphList.size();k++){
	        		org.jdom.Element ek = (org.jdom.Element)graphList.get(k);
	        		List childList = ek.getContent();
	        		for(int j=0;j<childList.size();j++){
	        			org.jdom.Element ej =(org.jdom.Element)childList.get(j);
	        			value2[k][j]=ej.getText();
	        		}
	        	}
	   		XYSeries [] xs=new XYSeries[2];
	   		for(int index=0;index< 2;index++)
	   		{
	   			String name="�������";
	   			if(index==1)
	   			{
	   				name="��������";
	   			}
	   			xs[index]=new XYSeries(""+name+"");
	   			for(int i=0;i<30;i++){
	   				double x=0;
	   				double y=0;
	   				if(value1[i][0]!=null&&value2[index][i]!=null){
		   			  x=Double.parseDouble(value1[i][0].equals("null")?"0":value1[i][0]);
		   			  y=Double.parseDouble(value2[index][i].equals("null")?"0":value2[index][i]);
	   				} 
	   				xs[index].add(x,y);
	   			} 
	   		} 
	   		ChartGraph cg=new ChartGraph();	  
	   		cg.xywave(xs, "ʱ���", "kb", title, outImgName, 693, 300) ; 
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String createImgAmChart(){
		String widthStr = request.getParameter("width") == null ? "650": request.getParameter("width");
		String heightStr = (request.getParameter("height") == null) ? "300": request.getParameter("height");
		String ip = request.getParameter("ip");
		String ifindex = request.getParameter("ifindex");
		String type=request.getParameter("type")+""; 
		// ҳ��flash�Ŀ�Ⱥ͸߶�
		int width = Integer.parseInt(widthStr);
		int height = Integer.parseInt(heightStr);
		BufferedImage result = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		// ҳ���ǽ�һ����������Ϊ�������ݽ�����,�������ͼ��Խ��,����ʱ��Խ��
		for (int y = 0; y < height; y++) {
			int x = 0;
			String[] row = request.getParameter("r" + y).split(",");
			for (int c = 0; c < row.length; c++) {
				String[] pixel = row[c].split(":"); // ʮ��������ɫ����
				int repeat = pixel.length > 1 ? Integer.parseInt(pixel[1]) : 1;
				for (int l = 0; l < repeat; l++) {
					result.setRGB(x, y, Integer.parseInt(pixel[0], 16));
					x++;
				}
			}
		} 
//		Graphics2D g = result.createGraphics();
//		// ����ͼ��ƽ����
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.drawImage(result, 0, 0, width, height, null);
//		g.dispose(); 
//		ServletOutputStream f = response.getOutputStream();
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(f);
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(result);
//		param.setQuality((float) (100 / 100.0), true);// ����ͼƬ����,100���,Ĭ��70
//		encoder.encode(result, param);

		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER); 
		String imgName= user.getUserid() + "_" + ip +"_"+ ifindex+"_port.png"; 
		
//		HttpSession session = request.getSession();
//		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER); // �û�����
//		String name = vo.getId()+"";
		String fileName = ResourceCenter.getInstance().getSysPath()+"resource/image/jfreechart/"+ imgName;
		File file = new File(fileName); 
		try { 
			ImageIO.write(result, "png", file);
		} catch (IOException e) { 
			e.printStackTrace();
		}
//		f.close();
		//return "/monitor.do?action=portdetail&id="+user.getId()+"&ip="+ip+"&ifindex="+ifindex+"&runFlag=false";
		String url="/monitor.do?action="+type+"&id="+user.getId()+"&ipaddress="+ip+"&runFlag=false";
		return url; 
	}
}
