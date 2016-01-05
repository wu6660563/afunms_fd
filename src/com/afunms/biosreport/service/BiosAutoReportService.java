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
//	 *  模板中填充网络设备数据
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
//	 *  模板中填充Windows 服务器设备数据
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
//	 *  模板中填充AIX服务器设备数据
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
//	 *  模板中填充oracle数据库设备数据
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
//	 * 1. 创建目录
//	 * 2. 模板构造template
//	 * 3. 
//	 */
//	
//	
//	public void report(){
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("皕杰报表开始运行...");
//			
//			File dir = new File("d:/temp");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//				
//			//从输入流创建报表模板对象
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
//			//保存模板对象到指定输出流
//			FileOutputStream out = new FileOutputStream("d:/temp/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//设置参数和变量
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //用户自行获取参数值
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
//				vars.put(varNames[i], i+"_v"); //用户自行获取变量值
//			}
//			dao.close();
//			//创建报表运算对象
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//设置数据库连接
//			//在服务器上运行时，会自动根据系统设置获取数据源，可不用进行设置
//			//manager.setConnection(getConnection());
//
//			//调用运算方法
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("报表运算出错!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//导出excel
//			out = new FileOutputStream("d:/temp/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//
//			//导出pdf
//			out = new FileOutputStream("d:/temp/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//导出doc
//			out = new FileOutputStream("d:/temp/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//分页生成html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//导出txt
//			FileWriter fw = new FileWriter("d:/temp/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//保存计算完成的报表对象
//			out = new FileOutputStream("d:/temp/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//从保存的报表对象创建
//			in = new FileInputStream("d:/temp/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//再次导出excel
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
//			System.out.println("皕杰报表开始运行...");
//			
//			File dir = new File("d:/temp1");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//				
//			//从输入流创建报表模板对象
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().WINDOWS_SERVER_REPORT_LOCATION; 
//
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//保存模板对象到指定输出流
//			FileOutputStream out = new FileOutputStream("d:/temp1/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//设置参数和变量
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //用户自行获取参数值
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
//				vars.put(varNames[i], i+"_v"); //用户自行获取变量值
//			}
//			wdao.close();
//			//创建报表运算对象
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//设置数据库连接
//			//在服务器上运行时，会自动根据系统设置获取数据源，可不用进行设置
//			//manager.setConnection(getConnection());
//
//			//调用运算方法
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("报表运算出错!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//导出excel
//			out = new FileOutputStream("d:/temp1/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//
//			//导出pdf
//			out = new FileOutputStream("d:/temp1/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//导出doc
//			out = new FileOutputStream("d:/temp1/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//分页生成html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp1/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//导出txt
//			FileWriter fw = new FileWriter("d:/temp1/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//保存计算完成的报表对象
//			out = new FileOutputStream("d:/temp1/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//从保存的报表对象创建
//			in = new FileInputStream("d:/temp1/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//再次导出excel
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
//			System.out.println("皕杰报表开始运行...");
//			
//			File dir = new File("d:/temp_aix_report");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//			
//			//从输入流创建报表模板对象
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().AIX_SERVER_REPORT_LOCATION; 
//			
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//保存模板对象到指定输出流
//			FileOutputStream out = new FileOutputStream("d:/temp_aix_report/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//设置参数和变量
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //用户自行获取参数值
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
//				vars.put(varNames[i], i+"_v"); //用户自行获取变量值
//			}
//			
//			wdao.close();
//			//创建报表运算对象
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//设置数据库连接
//			//在服务器上运行时，会自动根据系统设置获取数据源，可不用进行设置
//			//manager.setConnection(getConnection());
//			
//			//调用运算方法
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("报表运算出错!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//导出excel
//			out = new FileOutputStream("d:/temp_aix_report/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//			
//			//导出pdf
//			out = new FileOutputStream("d:/temp_aix_report/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//导出doc
//			out = new FileOutputStream("d:/temp_aix_report/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//分页生成html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp_aix_report/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//导出txt
//			FileWriter fw = new FileWriter("d:/temp_aix_report/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//保存计算完成的报表对象
//			out = new FileOutputStream("d:/temp_aix_report/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//从保存的报表对象创建
//			in = new FileInputStream("d:/temp_aix_report/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//再次导出excel
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
//			System.out.println("皕杰报表开始运行...");
//			
//			File dir = new File("d:/temp_oracle_report");
//			if(!dir.exists() || !dir.isDirectory())
//				dir.mkdir();
//			
//			//从输入流创建报表模板对象
//			
//			String netDeviceBRT = ResourceCenter.getInstance().getSysPath() 
//			+ BiosConstant.getInstance().DB_ORACLE_REPORT_LOCATION; 
//			
//			FileInputStream in = new FileInputStream(netDeviceBRT);
//			BiosReportTemplate template = new BiosReportTemplate(in);
//			in.close();
//			
//			//保存模板对象到指定输出流
//			FileOutputStream out = new FileOutputStream("d:/temp_oracle_report/bios_test.brt");
//			template.save(out);
//			out.close();
//			
//			//设置参数和变量
//			Map params = new HashMap();
//			String[] paramNames = template.getParamNames();
//			System.out.println("paramNames:" + paramNames);
//			for (int i = 0; i < paramNames.length; i++) {
//				for(int j=0; j<10; j++){
//					params.put(paramNames[i], j+""); //用户自行获取参数值
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
//				vars.put(varNames[i], i+"_v"); //用户自行获取变量值
//			}
//			wdao.close();
//			//创建报表运算对象
//			BiosReportManager manager = new BiosReportManager(template, params, vars);
//			
//			// System.out.println(manager.getCellView(1, 1));
//			
//			//设置数据库连接
//			//在服务器上运行时，会自动根据系统设置获取数据源，可不用进行设置
//			//manager.setConnection(getConnection());
//			
//			//调用运算方法
//			try {
//				manager.calc();
//			} catch (BiosReportException e) {
//				System.out.println("报表运算出错!\n [" + e.getMessage() + "]");
//				return;
//			}
//			
//			//导出excel
//			out = new FileOutputStream("d:/temp_oracle_report/test.xls");
//			manager.toExcel(out, null);
//			out.close();
//			
//			//导出pdf
//			out = new FileOutputStream("d:/temp_oracle_report/test.pdf");
//			manager.toPdf(out);
//			out.close();
//			
//			
//			//导出doc
//			out = new FileOutputStream("d:/temp_oracle_report/test.doc");
//			manager.toDoc(out);
//			out.close();
//			
//			
//			
//			//分页生成html
//			for (int i = 1; i <= manager.getPageCount(); i++) {
//				FileWriter fw = new FileWriter("d:/temp_oracle_report/test_page" + i + ".html");
//				fw.write(manager.toHtml(i, null));
//				fw.close();
//			}
//			
//			//导出txt
//			FileWriter fw = new FileWriter("d:/temp_oracle_report/test.txt");
//			String txt = manager.toTxt();
//			fw.write(txt, 0, txt.length());
//			fw.close();
//			
//			//保存计算完成的报表对象
//			out = new FileOutputStream("d:/temp_oracle_report/test.brf");
//			manager.toBrf(out);
//			out.close();
//			
//			//从保存的报表对象创建
//			in = new FileInputStream("d:/temp_oracle_report/test.brf");
//			manager = new BiosReportManager(in);
//			in.close();
//			
//			//再次导出excel
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
