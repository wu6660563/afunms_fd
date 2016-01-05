package com.afunms.inform.util;

import java.text.DateFormat;
import java.util.Date;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.afunms.inform.dao.NodeDataDao;

import com.afunms.initialize.ResourceCenter;
import com.afunms.report.base.ImplementorReport;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;

public class PortUtilReport extends ImplementorReport
{
	private int nodeId;
	private String ifIndex;

	public void createReport()
    {
    	head = "�˿������ʱ���";
    	tableHead = new String[]{"���","ʱ��","���������","����������"};   	
    	colWidth = new int[]{1,2,2,2};
    	
    	NodeDataDao dao = new NodeDataDao();
    	String[][] result = dao.portUtilStat(nodeId, timeStamp, ifIndex);
    	table = new String[result.length][tableHead.length];
    	for(int i=0;i<result.length;i++)
    	{    	
 	        table[i][0] = String.valueOf(i+1);  //��� 
	        table[i][1] = String.valueOf(i+1) + ":00";
		    table[i][2] = result[i][0];
			table[i][3] = result[i][1];
    	}
    	
    	//------------------��ͼ------------------------
		TimeSeriesCollection datasetOut = new TimeSeriesCollection();
		TimeSeriesCollection datasetIn = new TimeSeriesCollection();
		TimeSeries outSeries = new TimeSeries("����������",Hour.class);
		TimeSeries inSeries = new TimeSeries("���������",Hour.class);
		
		Date curDate = new Date();
		try
		{
		    curDate = DateFormat.getDateInstance().parse(timeStamp);
		}
		catch(Exception e){}
		
		for(int i=0;i<result.length;i++)
		{
			inSeries.add(new Hour(i,new Day(curDate)), Double.parseDouble(result[i][0]));
			outSeries.add(new Hour(i,new Day(curDate)), Double.parseDouble(result[i][1]));
		}
		datasetOut.addSeries(outSeries);
		datasetIn.addSeries(inSeries);
		chartKey = ChartCreator.createTimeSeriesChart(datasetOut,datasetIn,"X-ʱ��(h)","Y-������(%)","����/���������",500,350);
		chart = (JFreeChartBrother)ResourceCenter.getInstance().getChartStorage().get(chartKey);		
    }       	
	
	public void setIfIndex(String ifIndex) {
		this.ifIndex = ifIndex;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}	
}
