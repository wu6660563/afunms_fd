package com.afunms.system.util;

import java.util.ArrayList;
import java.util.List;

import com.afunms.system.model.Function;



public class CreateMenuTable3 {
	
	private StringBuffer table ;
	private String rootPath;
	private CreateRoleFunctionTable crft;
	public CreateMenuTable3(String rootPath){
		table = new StringBuffer();
		this.rootPath = rootPath+"/";
		crft = new CreateRoleFunctionTable();
	}
	
	/**
	 *  ����menu_list��ƴ�Ӳ˵�
	 * @param menu_list
	 * @return
	 */
	public String createMenuTable(List<Function> menu_list){	
		Function root = crft.getFunctionRoot(menu_list);
		List<Function> menuSecond_list = crft.getFunctionChild(root, menu_list);
		getMenuTable(menuSecond_list,menu_list);
		return table.toString();
	}
	

	/**
	 * 	ƴ�Ӷ����˵�
	 * @param menuSecond_list
	 * @param menu_list
	 * @return
	 */
	private StringBuffer getMenuTable(List<Function> menuSecond_list,List<Function> menu_list){
		table.append("<style type=\"text/css\">" +
				"<!--" +
				"body {" +
				"margin-left: 0px;" +
				"margin-top: 0px;" +
				"background-color: #ababab;" +
				"}" +
				"-->" +
				"</style>");
		table.append("<table id=\"container-menu-bar1\" class=\"container-menu-bar1\">");
		for(int i = 0 ; i < menuSecond_list.size() ; i++){
			Function function = menuSecond_list.get(i);
			table.append("<tr>");
			table.append("<td style='vertical-align: top;'>");
			table.append("<table id=\""+ function.getFunc_desc()+ "-" + "menu-bar\" class=\"menu-bar\">");
			table.append("<tr>");
			table.append("<td>");
			table.append("<table id=\""+ function.getFunc_desc()+ "-" + "menu-bar-header\" class=\"menu-bar-header\">");
			table.append("<tr>");
			table.append("<td align=\"left\">");
			table.append("<img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" />");
			table.append("</td>");
			table.append("<td class=\"menu-bar-title\">");
			table.append("<div id=\""+"menu"+function.getFunc_desc()+"\">");
			table.append("<a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">");
			table.append(function.getCh_desc());
			table.append("</a>");
			table.append("</div>");
			table.append("</td>");
			table.append("<td align=\"right\">");
			table.append("<img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" />");
			table.append("</td>");
			table.append("</tr>");
			table.append("</table>");
			table.append("<table id=\"" + function.getFunc_desc()+ "-" + "menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\">");
			table.append("<tr>");
			table.append("<td>");
			//-----------------------------------------------------------------------
			table.append("<table id=\"" + function.getFunc_desc()+ "-" + "menu-bar-body-list\" class=\"menu-bar-body-list\">");
			table.append("<tr>");
			table.append("<td>");
			table.append("<div id=\""+"menu"+function.getFunc_desc()+"child"+"\">");
			
			
			List<Function> menuThird_list = crft.getFunctionChild(function , menu_list);
			
			createThirdMenuTable(menuThird_list,menu_list );
			
			table.append("</div>");
			
			table.append("</td>");
			table.append("</tr>");
			table.append("</table>");
			//-----------------------------------------------------------------------
			
			table.append("</td>");
			table.append("</tr>");
			table.append("</table>");
			
//			table.append("<table id=\""+ function.getFunc_desc()+ "-" + "menu-bar-footer\" class=\"menu-bar-footer\">");
//			table.append("<tr>");
//			table.append("<td align=\"left\" valign=\"bottom\">" +
//							"<img src=\"/afunms/resource/image/global/menuBar_footer_left.jpg\" width=\"5\" height=\"12\" />" +
//						 "</td>");
//			table.append("<td align=\"right\" valign=\"bottom\">" +
//							"<img src=\"/afunms/resource/image/global/menuBar_footer_right.jpg\" width=\"5\" height=\"12\" />" +
//						 "</td>");
//			table.append("</tr>");
//			table.append("</table>");
			table.append("</td>");
			table.append("</tr>");
			table.append("</table>");
			
			table.append("</td>");
			table.append("</tr>");
		}
		
		table.append("</table>");
		return null;
	}
	
//	/**
//	 *  ƴ�������˵�
//	 * @param menuThird_list
//	 * @param menu_list
//	 * @return
//	 */
//	private StringBuffer createThirdMenuTable(List<Function> menuThird_list,List<Function> menu_list){
//		table.append("<ul>");
//		for(int i = 0 ; i < menuThird_list.size() ; i++){
//			table.append("<li>");
//			if(menuThird_list.get(i).getIsCurrentWindow()==1){
//				table.append("<img src="+rootPath+menuThird_list.get(i).getImg_url()+
//					" width=18 border=0 >&nbsp;"+"<a href=\"javascript:void(null)\""
//					+"onClick='window.open(\""+rootPath+menuThird_list.get(i).getUrl()+
//					"\",\"fullScreenWindow\", \"toolbar=no,height=\"+window.screen.height + \",\"+\"width=\" + " +
//					"(window.screen.width-8)+ \",scrollbars=no\"+\"screenX=0,screenY=0\")'>&nbsp;"+
//					menuThird_list.get(i).getCh_desc()+"</a>");
//			}else{				
//				table.append("<img src="+rootPath+menuThird_list.get(i).getImg_url()+
//						" width=18 border=0>&nbsp;"+"<a href="+rootPath+menuThird_list.get(i).getUrl()+
//						">&nbsp;"+menuThird_list.get(i).getCh_desc()+"</a>");
//			}
//			table.append("</li>");
//		}
//		table.append("</ul>");
//		return table;
//	}
	
	/**
	 *  ƴ�������˵�
	 * @param menuThird_list
	 * @param menu_list
	 * @return
	 */
	private StringBuffer createThirdMenuTable(List<Function> menuThird_list,List<Function> menu_list){
		table.append("<ul>");
		for(int i = 0 ; i < menuThird_list.size() ; i++){
			table.append("<li>");
			String width = menuThird_list.get(i).getWidth();
			String height = menuThird_list.get(i).getHeight();
			if(width == null || width.length() == 0 ){
				width = "(window.screen.width-8)";
			}
			if(height == null || height.length() == 0 ){
				height = "window.screen.height";
			}
			if(menuThird_list.get(i).getIsCurrentWindow()==1){
				table.append("<img src="+rootPath+menuThird_list.get(i).getImg_url()+
					" width=18 border=0 >&nbsp;"+"<a href=\"javascript:void(null)\""
					+"onClick='window.open(\""+rootPath+menuThird_list.get(i).getUrl()+
					"\",\"fullScreenWindow\", \"toolbar=no,height=\"+" + height + " + \",\"+\"width=\" + " +
					width + "+ \",scrollbars=yes,resizable=yes,screenX=0,screenY=0\")'>&nbsp;"+
					menuThird_list.get(i).getCh_desc()+"</a>");
			}else{				
				table.append("<img src="+rootPath+menuThird_list.get(i).getImg_url()+
						" width=18 border=0>&nbsp;"+"<a href="+rootPath+menuThird_list.get(i).getUrl()+
						">&nbsp;"+menuThird_list.get(i).getCh_desc()+"</a>");
			}
			table.append("</li>");
		}
		table.append("</ul>");
		return table;
	}
	
	
	
	
}
