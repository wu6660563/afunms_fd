package com.afunms.system.util;

import java.util.List;

import com.afunms.system.model.Menu;
import com.afunms.system.dao.MenuDao;

public class CreateMenu 
{
	private String rootPath;//����Ϊ�˵�ͼƬ��ʾԤ���ģ����ڻ�û����˵���ͼƬ	
	public CreateMenu(String path)
	{
	    rootPath = path + "/";
	}
	
	public String getMenus(int role)
	{
		MenuDao dao = new MenuDao();
	    Menu vo = null;
	    List list = dao.loadByRole(role);
	    if(list.size()==0)
	    	return "û�в˵���¼,�������...";

	    StringBuffer wholeMenu = new StringBuffer(1000);
	    String url = "";
	    wholeMenu.append("<script type=\"text/javascript\">\n");
	    wholeMenu.append("var tmpMenu = null;\n");
	    wholeMenu.append("var tmpItem = null;\n");
	    wholeMenu.append("var menuBar = new WebFXMenuBar;\n");
	    int subCode = 0;       
	    for(int i=0;i<list.size();i++)
        {
           vo = (Menu)list.get(i);
           subCode = Integer.parseInt(vo.getId().substring(2,4));
          
           if(subCode==0)//��һ���˵�
           {
    		   wholeMenu.append("tmpMenu = new WebFXMenu;\n");
    		   wholeMenu.append("menuBar.add(new WebFXMenuButton(\"");
    		   wholeMenu.append(vo.getTitle());
    		   wholeMenu.append("\",null,\"");
    		   wholeMenu.append(vo.getTitle());
    		   wholeMenu.append("\",tmpMenu));\n");
           }
           else//�����˵�
           {
        	   url = vo.getUrl();
        	   wholeMenu.append("tmpItem = new WebFXMenuItem(\"");
        	   wholeMenu.append(vo.getTitle());
        	   wholeMenu.append("\",\"");
        	   wholeMenu.append(url);
        	   if(url.indexOf("?")==-1)
        	       wholeMenu.append("?menu=");
        	   else
        	       wholeMenu.append("&menu=");
        	   wholeMenu.append(vo.getId());
        	   wholeMenu.append("\",\"" + vo.getTitle() + "\");\n");
        	   wholeMenu.append("tmpItem.target=\"mainFrame\";\n");
        	   wholeMenu.append("tmpMenu.add(tmpItem);\n");
           }
        }//end_for
	    wholeMenu.append("webfxLayout.writeMenu();\n");
	    wholeMenu.append("</script>");
        return wholeMenu.toString();
	}
}
