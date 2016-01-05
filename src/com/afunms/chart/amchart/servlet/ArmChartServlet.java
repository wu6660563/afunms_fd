package com.afunms.chart.amchart.servlet; 
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ���Servlet����ͼ��ҳ�洫���ĵ���ͼƬ����
 * 
 */
@SuppressWarnings("serial")
public class ArmChartServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			String widthStr = request.getParameter("width") == null ? "650" : request.getParameter("width"); 
			String heightStr = (request.getParameter("height") == null) ? "300" : request.getParameter("height");
			String imageName = request.getParameter("imageName")+"";
			//ҳ��flash�Ŀ�Ⱥ͸߶�
			int width = Integer.parseInt(widthStr);
			int height = Integer.parseInt(heightStr);
			BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			//ҳ���ǽ�һ����������Ϊ�������ݽ�����,�������ͼ��Խ��,����ʱ��Խ��
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
	    	response.setContentType("image/png");
			response.addHeader("Content-Disposition","attachment;filename=\""+imageName+".png\"");
			Graphics2D g = result.createGraphics();
			// ����ͼ��ƽ����
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(result, 0, 0, width, height, null);
			g.dispose();
			ServletOutputStream f = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(f);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(result);
			param.setQuality((float) (100 / 100.0), true);// ����ͼƬ����,100���,Ĭ��70
			encoder.encode(result, param);
	//		HttpSession session = request.getSession();
	//		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER); // �û�����
	//		String name = vo.getId()+"";
			//String fileName = ResourceCenter.getInstance().getSysPath() + "temp/" + name + "port.png";
			//File file = new File(fileName);  
			try { 
				//ImageIO.write(result, "png", file);
				 ImageIO.write(result, "JPEG", response.getOutputStream());// ���ͼƬ
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			f.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	 
	
}