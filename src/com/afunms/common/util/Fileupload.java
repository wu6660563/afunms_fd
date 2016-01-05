package com.afunms.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;


/**
 * ���ڱ��е��ļ��ϴ�
 * @author hkmw
 *
 */
public class Fileupload {
	
	/**
	 * �����Ŀ¼
	 */
	private String saveDirPath; 
	
	/**
	 * �����Ŀ¼File
	 */
	private File saveDirFile; 
	
	/**
	 * ������б�
	 * ���ڻ�û�н�����д��һ���� �ʱ�����List��װ 
	 * ��һ��Ԫ��Ϊ�ֶ�����(����һ���Ǳ����� һ�����ļ�����)
	 * �ڶ���Ԫ��Ϊ�ֶ���
	 * ������Ԫ��Ϊ�ֶ�ֵ (������ļ����� ��ֵΪ�ļ��� , �ļ����Ǿ����ļ���)
	 * 
	 */
	private List formFieldList;
	
	/**
	 * 
	 */
	public Fileupload() {
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * @param saveDirPath
	 */
	public Fileupload(String saveDirPath) {
		this.saveDirPath = saveDirPath;
		init();
	}

	public String getSaveDirPath() {
		return saveDirPath;
	}

	public void setSaveDirPath(String saveDirPath) {
		this.saveDirPath = saveDirPath;
		init();
	}

	public File getSaveDirFile() {
		return saveDirFile;
	}

	public void setSaveDirFile(File saveDirFile) {
		this.saveDirFile = saveDirFile;
	}
	
	public List getFormFieldList() {
		return formFieldList;
	}

	public void setFormFieldList(List formFieldList) {
		this.formFieldList = formFieldList;
	}

	public void init(){
		formFieldList = new ArrayList();
		if(saveDirPath!=null){
			this.saveDirFile = new File(this.saveDirPath);
		}
		if(this.saveDirFile==null){
			return ;
		}
		if(!this.saveDirFile.isDirectory()||!this.saveDirFile.exists()){
			this.saveDirFile.mkdirs();
		}
	}
	
	public void doupload(HttpServletRequest request){
		try{
			System.out.println(ServletFileUpload.isMultipartContent(request));
			if(ServletFileUpload.isMultipartContent(request)){
				DiskFileItemFactory dff = new DiskFileItemFactory();//�����ö���
				dff.setRepository(this.saveDirFile);//ָ���ϴ��ļ�����ʱĿ¼
				dff.setSizeThreshold(1024000);//ָ�����ڴ��л������ݴ�С,��λΪbyte
				ServletFileUpload sfu = new ServletFileUpload(dff);//�����ö���
				sfu.setFileSizeMax(5000000);//ָ�������ϴ��ļ������ߴ�
				sfu.setSizeMax(10000000);//ָ��һ���ϴ�����ļ����ܳߴ�
				List items = null;   
				try {   
	                items = sfu.parseRequest(request);   
	            } catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {   
	                System.out.println("size limit exception!");   
	            } catch(Exception e) {   
	                e.printStackTrace();   
	            }   
				Iterator iter = items==null?null:items.iterator();   
				while(iter.hasNext()){
					List list = new ArrayList();
					FileItem fis = (FileItem)iter.next();//�Ӽ����л��һ���ļ���
					if(!fis.isFormField() && fis.getName().length()>0){//���˵����з��ļ���
						
						String fileName = fis.getName().substring(fis.getName().lastIndexOf("\\"));//����ϴ��ļ����ļ���
						BufferedInputStream in = new BufferedInputStream(fis.getInputStream());//����ļ�������
						System.out.println(this.saveDirPath+"=======this.saveDirPath+fileName=========");
						System.out.println(fileName);
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(this.saveDirPath+fileName)));//����ļ������
						Streams.copy(in, out, true);//��ʼ���ļ�д����ָ�����ϴ��ļ���
						System.out.println(this.saveDirPath+fileName+"=======this.saveDirPath+fileName=========");
						list.add("file");
						list.add(fis.getFieldName());
						list.add(this.saveDirPath+fileName);
					}else if(fis.isFormField()){
						String filename = fis.getFieldName();
						String value = fis.getString();
						System.out.println(filename);
						System.out.println(value);
						list.add("formField");
						list.add(fis.getFieldName());
						list.add(value);
					}
					formFieldList.add(list);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	

	
	
}
