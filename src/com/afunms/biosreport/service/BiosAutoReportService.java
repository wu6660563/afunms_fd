package com.afunms.biosreport.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.dao.AixServerReportDao;
import com.afunms.biosreport.dao.BiosReportBaseDao;
import com.afunms.biosreport.dao.DbOracleReportDao;
import com.afunms.biosreport.dao.NetDeviceReportDao;
import com.afunms.biosreport.dao.WindowsServerReportDao;
import com.afunms.biosreport.model.AixServerReportModel;
import com.afunms.biosreport.model.DbOracleReportModel;
import com.afunms.biosreport.model.NetDeviceReport;
import com.afunms.biosreport.model.WindowsServerReportModel;
import com.afunms.initialize.ResourceCenter;


/**
 * 
 * @author Administrator
 *
 */
public class BiosAutoReportService {

	
//	/**
//	 *  ģ������������豸����
//	 */
//	private void addNetDeviceData(BiosReportTemplate template,NetDeviceReport device, 
//			int row, int colunm){
//		//template.setCellPropValue(row, colunm, template.getCellPropValue(row, arg1, arg2), arg3)
//		template.setCellValue(row, colunm, device.getIpAddress()+"");
//		System.out.println("IP:"+device.getIpAddress());
//		
//		template.setCellValue(row, colunm+1, device.getAliasName()+"");
//		template.setCellValue(row, colunm+2, device.getAvergeConnectivity()+"");
//		template.setCellValue(row, colunm+3, device.getCpuAvgVal()+"");
//		template.setCellValue(row, colunm+4, device.getCpuMaxVal()+"");
//		template.setCellValue(row, colunm+5, device.getPhysicsMemoryAvgVal()+"");
//		template.setCellValue(row, colunm+6, device.getPhysicsMemoryMaxVal()+"");
//		template.setCellValue(row, colunm+7, device.getOutFlowAvgVal());
//		template.setCellValue(row, colunm+8, device.getOutFlowMaxVal());
//		template.setCellValue(row, colunm+9, device.getOutFlowBandWidthRate());
//		template.setCellValue(row, colunm+10, device.getInFlowAvgVal());
//		template.setCellValue(row, colunm+11, device.getInFlowMaxVal());
//		template.setCellValue(row, colunm+12, device.getInFlowBandWidthRate());
//		template.setCellValue(row, colunm+13, device.getAlarmCommon()+"");
//		template.setCellValue(row, colunm+14, device.getAlarmSerious()+"");
//		template.setCellValue(row, colunm+15, device.getAlarmUrgency()+"");
//
//	}
//	/**
//	 *  ģ�������Windows �������豸����
//	 */
//	private void addWindowsServerData(BiosReportTemplate template,WindowsServerReportModel device, 
//			int row, int colunm){
//		template.setCellValue(row, colunm, device.getIpAddress()+"");
//		System.out.println("IP:"+device.getIpAddress());
//		
//		template.setCellValue(row, colunm+1, device.getAliasName()+"");
//		template.setCellValue(row, colunm+2, device.getAvergeConnectivity()+"");
//		template.setCellValue(row, colunm+3, device.getDiskSpaceUsingAvgVal());
//		template.setCellValue(row, colunm+4, device.getCpuAvgVal()+"");
//		template.setCellValue(row, colunm+5, device.getCpuMaxVal()+"");
//		template.setCellValue(row, colunm+6, device.getPhyMemAvgVal()+"");
//		template.setCellValue(row, colunm+7, device.getPhyMemMaxVal()+"");
//		template.setCellValue(row, colunm+8, device.getVirMemAvgVal());
//		template.setCellValue(row, colunm+9, device.getVirMemMaxVal());
//		template.setCellValue(row, colunm+10, device.getAlarmCommon()+"");
//		template.setCellValue(row, colunm+11, device.getAlarmSerious()+"");
//		template.setCellValue(row, colunm+12, device.getAlarmUrgency()+"");
//		
//	}
//	
//	/**
//	 *  ģ�������AIX�������豸����
//	 */
//	private void addAixServerData(BiosReportTemplate template,AixServerReportModel device, 
//			int row, int colunm){
//		template.setCellValue(row, colunm, device.getIpAddress()+"");
//		System.out.println("IP:"+device.getIpAddress());
//		
//		template.setCellValue(row, colunm+1, device.getAliasName()+"");
//		template.setCellValue(row, colunm+2, device.getAvergeConnectivity()+"");
//		template.setCellValue(row, colunm+3, device.getFileSysUsingAvgVal());
//		template.setCellValue(row, colunm+4, device.getCpuAvgVal()+"");
//		template.setCellValue(row, colunm+5, device.getCpuMaxVal()+"");
//		template.setCellValue(row, colunm+6, device.getPhyMemAvgVal()+"");
//		template.setCellValue(row, colunm+7, device.getPhyMemMaxVal()+"");
//		template.setCellValue(row, colunm+8, device.getPageSpaceAvgVal());
//		template.setCellValue(row, colunm+9, device.getPageSpaceMaxVal());
//		template.setCellValue(row, colunm+10, device.getAlarmCommon()+"");
//		template.setCellValue(row, colunm+11, device.getAlarmSerious()+"");
//		template.setCellValue(row, colunm+12, device.getAlarmUrgency()+"");
//		
//	}
//	
//	/**
//	 *  ģ�������oracle���ݿ��豸����
//	 */
//	private void addDbOracleData(BiosReportTemplate template,DbOracleReportModel device, 
//			int row, int colunm){
//		template.setCellValue(row, colunm, device.getIpAddress()+"");
//		template.setCellValue(row, colunm+1, device.getAliasName()+"");
//		template.setCellValue(row, colunm+2, device.getAvergeConnectivity()+"");
//		template.setCellValue(row, colunm+3, device.getBufferCache());
//		template.setCellValue(row, colunm+4, device.getDictionaryCache());
//		template.setCellValue(row, colunm+5, device.getPga());
//		template.setCellValue(row, colunm+6, device.getSga());
//		template.setCellValue(row, colunm+7, device.getOpenCur());
//		template.setCellValue(row, colunm+8, device.getMemorySort());
//		template.setCellValue(row, colunm+9, device.getAlarmCommon()+"");
//		template.setCellValue(row, colunm+10, device.getAlarmSerious()+"");
//		template.setCellValue(row, colunm+11, device.getAlarmUrgency()+"");
//		
//	}
//
//	/**
//	 * 1. ����Ŀ¼
//	 * 2. ģ�幹��template
//	 * 3. 
//	 */
//	
//	
//	public void report(){
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("�z�ܱ���ʼ����...");
//			
//			File dir = new File("d:/temp");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//				
//			//����������������ģ�����
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().NET_DEVICE_REPORT_LOCATION; 
//
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//		
//			
//			//����ģ�����ָ�������
//			FileOutputStream out = new FileOutputStream("d:/temp/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//���ò����ͱ���
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //�û����л�ȡ����ֵ
//					System.out.println("paramNames"+i+":" + params.get(paramNames[i]));
//				}
//			}
//	//		BiosReportBaseDao dao = new BiosReportBaseDao();
//	//		List<NetDeviceReport> list = dao.get dao.getNetDeviceReportInfo("2012-03-18 00:00:00",
//	//		"2012-03-25 23:59:59");
//	
//			NetDeviceReportDao dao = new NetDeviceReportDao();
//			List<NetDeviceReport> list = dao.getDevice("2012-03-18 00:00:00",
//							"2012-04-25 23:59:59");
//			
////			int row = 6;
////			int col = 1;
////			int nx=0;
////			for(NetDeviceReport n : list){
////				//template.setCellValue(row, col+1, n.getAliasName()+"");
////				new NetDeviceReportDao().insertData(n, "1");
////				addNetDeviceData(template, n, row, col);
////				row++;
////				if(nx > 20){
////					break;
////				}
////				nx++;
////		
////			}
//				
//			
//			Map vars = new HashMap();
//			String[] varNames = template.getVarNames();
//			System.out.println("varNames:"+varNames);
//			for (int i = 0; i < varNames.length; i++) {
//				System.out.println("varNames"+i+":"+varNames[i]);
//				vars.put(varNames[i], i+"_v"); //�û����л�ȡ����ֵ
//			}
//			dao.close();
//			//���������������
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//�������ݿ�����
//			//�ڷ�����������ʱ�����Զ�����ϵͳ���û�ȡ����Դ���ɲ��ý�������
//			//manager.setConnection(getConnection());
//
//			//�������㷽��
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("�����������!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//����excel
//			out = new FileOutputStream("d:/temp/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//
//			//����pdf
//			out = new FileOutputStream("d:/temp/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//����doc
//			out = new FileOutputStream("d:/temp/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//��ҳ����html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//����txt
//			FileWriter fw = new FileWriter("d:/temp/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//���������ɵı������
//			out = new FileOutputStream("d:/temp/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//�ӱ���ı�����󴴽�
//			in = new FileInputStream("d:/temp/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//�ٴε���excel
//			out = new FileOutputStream("d:/temp/test_2.xls");
//			manager.toPagedExcel(out, null);
//			out.close();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	
//	public void report1(){
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("�z�ܱ���ʼ����...");
//			
//			File dir = new File("d:/temp1");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//				
//			//����������������ģ�����
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().WINDOWS_SERVER_REPORT_LOCATION; 
//
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//����ģ�����ָ�������
//			FileOutputStream out = new FileOutputStream("d:/temp1/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//���ò����ͱ���
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //�û����л�ȡ����ֵ
//					System.out.println("paramNames"+i+":" + params.get(paramNames[i]));
//				}
//			}
//	//		BiosReportBaseDao dao = new BiosReportBaseDao();
//	//		List<NetDeviceReport> list = dao.get dao.getNetDeviceReportInfo("2012-03-18 00:00:00",
//	//		"2012-03-25 23:59:59");
//	
//			
//			WindowsServerReportDao wdao = new WindowsServerReportDao();
//			List<WindowsServerReportModel> wlist = wdao.getDevice("2012-03-18 00:00:00",
//							"2012-04-25 23:59:59");
//			
//			int row = 5;
//			int col = 1;
//			int nx=0;
//			for(WindowsServerReportModel n : wlist){
//
//				//template.setCellValue(row, col, row+"");
//				System.out.println(template.getCellValue(row, col));
//		
//				//template.setCellValue(row, col+1, n.getAliasName()+"");
//				addWindowsServerData(template, n, row, col);
//				row++;
//				if(nx > 20){
//					break;
//				}
//				nx++;
//		
//			}
//				
//			
//			Map vars = new HashMap();
//			String[] varNames = template.getVarNames();
//			System.out.println("varNames:"+varNames);
//			for (int i = 0; i < varNames.length; i++) {
//				System.out.println("varNames"+i+":"+varNames[i]);
//				vars.put(varNames[i], i+"_v"); //�û����л�ȡ����ֵ
//			}
//			wdao.close();
//			//���������������
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//�������ݿ�����
//			//�ڷ�����������ʱ�����Զ�����ϵͳ���û�ȡ����Դ���ɲ��ý�������
//			//manager.setConnection(getConnection());
//
//			//�������㷽��
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("�����������!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//����excel
//			out = new FileOutputStream("d:/temp1/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//
//			//����pdf
//			out = new FileOutputStream("d:/temp1/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//����doc
//			out = new FileOutputStream("d:/temp1/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//��ҳ����html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp1/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//����txt
//			FileWriter fw = new FileWriter("d:/temp1/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//���������ɵı������
//			out = new FileOutputStream("d:/temp1/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//�ӱ���ı�����󴴽�
//			in = new FileInputStream("d:/temp1/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//�ٴε���excel
//			out = new FileOutputStream("d:/temp1/test_2.xls");
//			manager.toPagedExcel(out, null);
//			out.close();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	public void report2(){
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("�z�ܱ���ʼ����...");
//			
//			File dir = new File("d:/temp_aix_report");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//			
//			//����������������ģ�����
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().AIX_SERVER_REPORT_LOCATION; 
//			
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//����ģ�����ָ�������
//			FileOutputStream out = new FileOutputStream("d:/temp_aix_report/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//���ò����ͱ���
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //�û����л�ȡ����ֵ
//					System.out.println("paramNames"+i+":" + params.get(paramNames[i]));
//				}
//			}
//			//		BiosReportBaseDao dao = new BiosReportBaseDao();
//			//		List<NetDeviceReport> list = dao.get dao.getNetDeviceReportInfo("2012-03-18 00:00:00",
//			//		"2012-03-25 23:59:59");
//			
//			
//			AixServerReportDao wdao = new AixServerReportDao();
//			List<AixServerReportModel> wlist = wdao.getDevice("2012-03-18 00:00:00",
//			"2012-04-25 23:59:59");
//			
//			int row = 5;
//			int col = 1;
//			int nx=0;
//			for(AixServerReportModel n : wlist){
//				
//				//template.setCellValue(row, col, row+"");
//				System.out.println(template.getCellValue(row, col));
//				
//				//template.setCellValue(row, col+1, n.getAliasName()+"");
//				addAixServerData(template, n, row, col);
//				row++;
//				if(nx > 20){
//					break;
//				}
//				nx++;
//				
//			}
//			
//			
//			Map vars = new HashMap();
//			String[] varNames = template.getVarNames();
//			System.out.println("varNames:"+varNames);
//			for (int i = 0; i < varNames.length; i++) {
//				System.out.println("varNames"+i+":"+varNames[i]);
//				vars.put(varNames[i], i+"_v"); //�û����л�ȡ����ֵ
//			}
//			
//			wdao.close();
//			//���������������
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//�������ݿ�����
//			//�ڷ�����������ʱ�����Զ�����ϵͳ���û�ȡ����Դ���ɲ��ý�������
//			//manager.setConnection(getConnection());
//			
//			//�������㷽��
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("�����������!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//����excel
//			out = new FileOutputStream("d:/temp_aix_report/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//			
//			//����pdf
//			out = new FileOutputStream("d:/temp_aix_report/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//����doc
//			out = new FileOutputStream("d:/temp_aix_report/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//��ҳ����html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp_aix_report/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//����txt
//			FileWriter fw = new FileWriter("d:/temp_aix_report/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//���������ɵı������
//			out = new FileOutputStream("d:/temp_aix_report/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//�ӱ���ı�����󴴽�
//			in = new FileInputStream("d:/temp_aix_report/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//�ٴε���excel
//			out = new FileOutputStream("d:/temp_aix_report/test_2.xls");
//			manager.toPagedExcel(out, null);
//			out.close();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	//oracle 
//	public void report3(){
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("�z�ܱ���ʼ����...");
//			
//			File dir = new File("d:/temp_oracle_report");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//			
//			//����������������ģ�����
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().DB_ORACLE_REPORT_LOCATION; 
//			
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//����ģ�����ָ�������
//			FileOutputStream out = new FileOutputStream("d:/temp_oracle_report/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//���ò����ͱ���
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //�û����л�ȡ����ֵ
//					System.out.println("paramNames"+i+":" + params.get(paramNames[i]));
//				}
//			}
//			//		BiosReportBaseDao dao = new BiosReportBaseDao();
//			//		List<NetDeviceReport> list = dao.get dao.getNetDeviceReportInfo("2012-03-18 00:00:00",
//			//		"2012-03-25 23:59:59");
//			
//			
//			DbOracleReportDao wdao = new DbOracleReportDao();
//			List<DbOracleReportModel> wlist = wdao.getDevice("2012-03-18 00:00:00",
//			"2012-04-25 23:59:59");
//			
//			int row = 5;
//			int col = 1;
//			int nx=0;
//			for(DbOracleReportModel n : wlist){
//				
//				//template.setCellValue(row, col, row+"");
//				System.out.println(template.getCellValue(row, col));
//				
//				//template.setCellValue(row, col+1, n.getAliasName()+"");
//				addDbOracleData(template, n, row, col);
//				row++;
//				if(nx > 20){
//					break;
//				}
//				nx++;
//				
//			}
//			
//			
//			Map vars = new HashMap();
//			String[] varNames = template.getVarNames();
//			System.out.println("varNames:"+varNames);
//			for (int i = 0; i < varNames.length; i++) {
//				System.out.println("varNames"+i+":"+varNames[i]);
//				vars.put(varNames[i], i+"_v"); //�û����л�ȡ����ֵ
//			}
//			wdao.close();
//			//���������������
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//�������ݿ�����
//			//�ڷ�����������ʱ�����Զ�����ϵͳ���û�ȡ����Դ���ɲ��ý�������
//			//manager.setConnection(getConnection());
//			
//			//�������㷽��
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("�����������!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//����excel
//			out = new FileOutputStream("d:/temp_oracle_report/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//			
//			//����pdf
//			out = new FileOutputStream("d:/temp_oracle_report/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//����doc
//			out = new FileOutputStream("d:/temp_oracle_report/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//��ҳ����html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp_oracle_report/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//����txt
//			FileWriter fw = new FileWriter("d:/temp_oracle_report/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//���������ɵı������
//			out = new FileOutputStream("d:/temp_oracle_report/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//�ӱ���ı�����󴴽�
//			in = new FileInputStream("d:/temp_oracle_report/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//�ٴε���excel
//			out = new FileOutputStream("d:/temp_oracle_report/test_2.xls");
//			manager.toPagedExcel(out, null);
//			out.close();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
