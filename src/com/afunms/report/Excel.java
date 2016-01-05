/**
 * @author sunqichang/孙启昌
 * Created on May 20, 2011 3:07:48 PM
 */
package com.afunms.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

/**
 * @author sunqichang/孙启昌
 * 
 */
public class Excel {
	private static Logger log = Logger.getLogger(Excel.class);

	private WritableWorkbook wb = null;

	private WritableSheet sheet = null;

	/**
	 * @param path
	 */
	public Excel(String path) {
		try {
			wb = Workbook.createWorkbook(new File(path));
			sheet = wb.createSheet("报表", 1);
		} catch (IOException e) {
			log.error("", e);
		}
	}

	/**
	 * 插入图片
	 * 
	 * @param path
	 */
	public void insertChart(String path) {
		File file = new File(path);
		// 向sheet里面增加图片,0, 10, 10, 12分别代表列,行,图片宽度占多少列,高度占位多少行
		sheet.addImage(new WritableImage(0, 10, 10, 12, file));
	}

	/**
	 * 插入表格
	 * 
	 * @param tableal
	 */
	public void insertTable(ArrayList<String[]> tableal) {
		try {
			WritableFont labelFont = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD, false);
			WritableCellFormat labelFormat0 = new WritableCellFormat(labelFont);
			labelFormat0.setShrinkToFit(true);
			labelFormat0.setBackground(jxl.format.Colour.GRAY_25);
			labelFormat0.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			labelFormat0.setAlignment(Alignment.CENTRE);
			labelFormat0.setVerticalAlignment(VerticalAlignment.CENTRE);
			WritableCellFormat labelFormat = new WritableCellFormat(labelFont);
			labelFormat.setShrinkToFit(true);
			labelFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			labelFormat.setAlignment(Alignment.CENTRE);
			labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			Label tmpLabel = null;
			for (int k = 0; k < tableal.size(); k++) {
				String[] row = tableal.get(k);
				for (int j = 0; j < row.length; j++) {
					if (k == 0) {
						tmpLabel = new Label(j, k, row[j], labelFormat0);
					} else {
						tmpLabel = new Label(j, k, row[j], labelFormat);
					}
					sheet.addCell(tmpLabel);
				}
			}
			CellView cv = new CellView();
			cv.setAutosize(true);
			for (int i = 0; i < tableal.get(0).length; i++) {
				// 设置列宽自适应
				sheet.setColumnView(i, cv);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 保存excel
	 */
	public void saveExcel() {
		if (wb != null) {
			try {
				wb.write();
			} catch (IOException e) {
				log.error("", e);
			} finally {
				try {
					wb.close();
				} catch (WriteException e) {
					log.error("", e);
				} catch (IOException e) {
					log.error("", e);
				}
			}
			log.info("finish!");
		}
	}

}
