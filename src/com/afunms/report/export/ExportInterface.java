/**
 * @author sunqichang/������
 * Created on Jun 1, 2011 4:34:24 PM
 */
package com.afunms.report.export;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.lowagie.text.DocumentException;

/**
 * @author sunqichang/������
 * 
 * �����ӿ�
 */
public interface ExportInterface {

	/**
	 * �������
	 * 
	 * @param title
	 *            ����
	 * @param cols
	 *            ����
	 * @param timefromto
	 *            ʱ��
	 */
	public void insertTitle(String title, int cols, String timefromto) throws Exception;

	/**
	 * ������
	 * 
	 * @param tableal
	 *            ���ArrayList<String[]>
	 * @throws IOException
	 */
	public void insertTable(ArrayList<String[]> tableal) throws Exception;

	/**
	 * ����ͼƬ
	 * 
	 * @param path
	 *            ͼƬ·��
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void insertChart(String path) throws Exception;

	/**
	 * �����ļ�
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception;

}
