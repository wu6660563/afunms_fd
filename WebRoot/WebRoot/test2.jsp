<%@ page language="java" import="javax.imageio.*,java.awt.*,java.awt.image.*,java.io.File,java.io.IOException" pageEncoding="UTF-8"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.initialize.ResourceCenter"%>
<%
			String widthStr = request.getParameter("width") == null ? "650" : request.getParameter("width"); 
		String heightStr = (request.getParameter("height") == null) ? "300" : request.getParameter("height");
		String imageName = request.getParameter("imageName")+"";
		//页面flash的宽度和高度
		int width = Integer.parseInt(widthStr);
		int height = Integer.parseInt(heightStr);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//页面是将一个个像素作为参数传递进来的,所以如果图表越大,处理时间越长
		for (int y = 0; y < height; y++) {
			int x = 0;
			String[] row = request.getParameter("r" + y).split(",");
			for (int c = 0; c < row.length; c++) {
				String[] pixel = row[c].split(":"); // 十六进制颜色数组
				int repeat = pixel.length > 1 ? Integer.parseInt(pixel[1]) : 1;
				for (int l = 0; l < repeat; l++) {
					result.setRGB(x, y, Integer.parseInt(pixel[0], 16));
					x++;
				}
			}
		}
    	response.setContentType("image/png");
		response.addHeader("Content-Disposition","attachment;filename=\""+imageName+".png\"");
		Graphics2D g = result.createGraphics();
		// 处理图形平滑度
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(result, 0, 0, width, height, null);
		g.dispose(); 
		try {  
			 ImageIO.write(result, "JPEG", response.getOutputStream());// 输出图片
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
%>
