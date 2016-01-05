package com.afunms.topology.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class ExcelUtil {
	/**��ȡExcel�ļ�������  
	* @param file  ����ȡ���ļ�  
	* @return  
	*/  
	public static List readExcel(File file){   
	    StringBuffer sb = new StringBuffer();  
	    
	    List returnList = new ArrayList();
	    
	    Workbook wb = null;   
	    try {   
	        //����Workbook��������������   
	        wb=Workbook.getWorkbook(file);   
	    } catch (BiffException e) {   
	        e.printStackTrace();   
	    } catch (IOException e) {   
	        e.printStackTrace();   
	    }   
	       
	    if(wb==null)   
	        return null;   
	       
	    try {
			//�����Workbook����֮�󣬾Ϳ���ͨ�����õ�Sheet��������������   
			Sheet[] sheet = wb.getSheets();   
			   
			if(sheet!=null&&sheet.length>0){   
			    //��ÿ�����������ѭ��   
			    for(int i=0;i<sheet.length;i++){
			        //�õ���ǰ�����������   
			        int rowNum = sheet[i].getRows(); 
			        List listNum = new ArrayList();
			        for(int j=0;j<rowNum;j++){
			            //�õ���ǰ�е����е�Ԫ��   
			            Cell[] cells = sheet[i].getRow(j); 
			            List ListCells = new ArrayList();
			            if(cells!=null&&cells.length>0){   
			                //��ÿ����Ԫ�����ѭ��   
			                for(int k=0;k<cells.length;k++){ 
			                    //��ȡ��ǰ��Ԫ���ֵ   
			                    String cellValue = cells[k].getContents(); 
			                    
			                    ListCells.add(cellValue);
			                }   
			            } 
			            listNum.add(ListCells);
			        }
			        returnList.add(listNum);
			    }   
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally{
			if(wb!=null){
				//���ر���Դ���ͷ��ڴ�   
			    wb.close();  
			}
		} 
	     
	    return returnList;
	} 
	
	public static void main(String[] args){
		File file = new File("C:/Documents and Settings/hkmw/����/�½��ļ��� (14)/���⳵������ϸ��1.xls");
		System.out.println(readExcel(file));
	}
}
