/**
 * <p>Description:action center,at the same time, the control legal power</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.alertalarm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.FileViewer;

import com.afunms.initialize.ResourceCenter;

import java.util.*;

public class AlertAlarm {
	
	
	public String [][] readAlertAlarm(){
		
	       String fileName = "demo.xml";
			
			String[][] nodeInfo = null;
			Date formerTime = ShareData.getFormerUpdateTime(); //��ȡ��Դ���Ĵ洢��������޸�ʱ�� 
			Date newUpdateTime = getNewUpdateTime();     //��ȡ��ǰ�ļ��������޸�ʱ��
//System.out.println(formerTime+"-----------"+newUpdateTime);			
			if(formerTime.compareTo(newUpdateTime)<0){   //�Ƚϵ�ǰ���ļ�����ʱ�����Դ���ĵ�ʱ���Ƿ���ȣ�����ȣ�˵���ļ������£�
				ShareData.setFormerUpdateTime(newUpdateTime);  //������Դ���ĵ�ʱ��  
				SAXBuilder builder = new SAXBuilder();
				Document doc = null;
				try {
					doc = builder.build(new FileInputStream(ResourceCenter.getInstance().getSysPath() + fileName));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Element root = doc.getRootElement();
				//��ȡ���ڵ���������
				String[] alarmNum = new String[1];
				alarmNum[0] = new String(root.getAttributeValue("alarmNum"));
				//System.out.println(alarmNum[0]);
				
				//���ӽڵ���������
				List rootList = root.getChildren();
				nodeInfo = new String[rootList.size()][3];
				for (int i = 0; i < rootList.size(); i++)
				{
					Element nodes = (Element) rootList.get(i);
					
					if (nodes.getName().equalsIgnoreCase("alarmNode"))
					{
						nodeInfo[i][0] = new String(nodes.getAttributeValue("ip"));
						nodeInfo[i][1] = new String(nodes.getAttributeValue("level"));
						nodeInfo[i][2] = new String(nodes.getAttributeValue("content"));
					}
				}
			} 	
			return nodeInfo;
	}
	
    private static Date getNewUpdateTime(){
    	
		FileViewer f = new FileViewer();                
		f.setPath(ResourceCenter.getInstance().getSysPath());             
		f.refreshList();  
		Date newFileDate = null;
		while(f.nextFile()){
			if(newFileDate==null){
				newFileDate = f.getFileTimeStampDate();
			}else{
				if(newFileDate.compareTo(f.getFileTimeStampDate())<0){
					newFileDate = f.getFileTimeStampDate();
				} 
			}
	    }
		//System.out.println("����ʱ�䣺"+newFileDate);
		return newFileDate;
    }
}