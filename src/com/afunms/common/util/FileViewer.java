/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

/**
 * Title:        ��������ƽ̨
 * Description:
 * �ַ�������� �滻/��ʽ�ж�
 * Copyright:    Copyright (c) 2001
 * Company:      ��̩����
 * @author
 * @version 1.0
 */


import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
public class FileViewer{ 
	File myDir;        
	File[] contents;        
	Vector vectorList;        
	Iterator currentFileView;        
	File currentFile;        
	String path;    
	
	public FileViewer(){                
		path=new String("");                                        
		vectorList=new Vector(); 
	}
		public FileViewer(String path){                
			this.path=path;                
			vectorList = new Vector();        
			
		}               
		
		/**         
		 * * ���������·��        */        
		public void setPath(String path){     
			this.path=path;        
			}               
		
		/***         
		 * * ���ص�ǰĿ¼·��        */        
		public String getDirectory(){                
			return myDir.getPath();       
			}              
		
		/**         
		 * * ˢ���б�        */        
		public void refreshList(){               
			if(this.path.equals("")) path="c:\\";
                myDir = new File(path); 
                
				vectorList.clear();             
				contents = myDir.listFiles();              
					 //����װ��·�����ļ�               
					for(int i=0;i<contents.length;i++){
						vectorList.add(contents[i]);  
					} 
			  //System.out.println("�ļ��ĸ���Ϊ:"+contents.length);
		      currentFileView=vectorList.iterator();        
		    }        

		/**        
		 *  * �ƶ���ǰ�ļ����ϵ�ָ��ָ����һ����Ŀ         
		 *  * @return �ɹ�����true,����false        */       
		public boolean nextFile(){               
			while(currentFileView.hasNext()){                        
				currentFile=(File)currentFileView.next();                        
				return true;                }                
			return false;       
			}  
		
		/**         * ���ص�ǰָ����ļ�������ļ�����        */      
		public String getFileName(){                
			return currentFile.getName();        
			}  
		
		/**         * ���ص�ǰָ����ļ�������ļ��ߴ�        */        
		public String getFileSize(){                
			return new Long(currentFile.length()).toString();        
			} 
		
		/**         * ���ص�ǰָ����ļ����������޸�����        */               
		public String getFileTimeStampString(){                
			return new Date(currentFile.lastModified()).toString();  
			}     
		
		/**         * ���ص�ǰָ����ļ����������޸�����        */               
		public Date getFileTimeStampDate(){                
			return new Date(currentFile.lastModified());  
			} 
		
		/**         * ���ص�ǰָ����ļ������Ƿ���һ���ļ�Ŀ¼        */      
		public boolean getFileType(){                
			return currentFile.isDirectory();        
			}

   }