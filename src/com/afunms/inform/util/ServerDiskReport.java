/**
 * <p>Description:server disk stat report</p>
 * <p>Company: ���������ϴ�����Ƽ��ɷ����޹�˾</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2006-11-20
 */


package com.afunms.inform.util;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import com.afunms.polling.node.Host;
import com.afunms.polling.PollingEngine;
import com.afunms.inform.dao.NodeDataDao;
import com.afunms.initialize.ResourceCenter;
import com.afunms.report.base.ImplementorReport;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;

public class ServerDiskReport extends ImplementorReport
{
   private int nodeId;   
   
   public ServerDiskReport()
   {
   }

   public void setNodeId(int nodeId)
   {
	   this.nodeId = nodeId;
   }

   //�������
   public void createReport()
   {
	   Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);
	   head = host.getAlias() + "  Ӳ�������ʱ���";
	   note = "������Դ�������������";

	   NodeDataDao dao = new NodeDataDao();
       String[][] tmpTable = dao.diskStat(timeStamp,nodeId);
       if(tmpTable == null)
 	   {
 	       table = null;
 	       chart = null;
 	       return;
 	   }
       String year = timeStamp.substring(0,4);
       String month = timeStamp.substring(5,7);
       int row = tmpTable.length;
       int col = tmpTable[0].length;
       tableHead = new String[row+2];
       colWidth = new int[row+2]; 
       colWidth[0] = 2;
       colWidth[1] = 5;
       tableHead[0] = "���";
       tableHead[1] = "����";
       table = new String[col-1][tableHead.length];
       for(int i=2; i<row+2; i++)
       {
    	  tableHead[i] = tmpTable[i-2][0]+": ��(%)";
    	  colWidth[i] = 3;
       }
      
       for(int j=0; j<col-1; j++)
       {
    	  table[j][0] = String.valueOf(j+1);//���
    	  table[j][1] = year+"-"+month+"-"+String.valueOf(j+1);//ȡ����    	  
    	  for(int k=2; k<row+2;k++)
    	  {
    		  table[j][k] = tmpTable[k-2][j+1];//����
    	  }
       }
	   //--------��ͼ--------------
      TimeSeries[] series = new TimeSeries[row]; 
 	  TimeSeries series1 = null;
	  
 	  String tmpData = "";
 	  for(int j=0; j<row; j++)
 	  {
 		  series1 = new TimeSeries(tmpTable[j][0]+": ��",Day.class);
 		  for(int i=0;i<col-1;i++)
 		  {
 			  tmpData = tmpTable[j][i+1];
 			  if(tmpData=="") tmpData = "0";
 			  series1.add(new Day(i+1,Integer.parseInt(month),Integer.parseInt(year)), Double.parseDouble(tmpData));
 		  }
 		  series[j] = series1;
 	  }	
 	  chartKey = ChartCreator.createMultiTimeSeriesChart(series,"DAY","X-����(d)","Y-Ӳ��������(%)",host.getAlias()+"-Ӳ�������ʱ���",500,350);
 	  chart = (JFreeChartBrother)ResourceCenter.getInstance().getChartStorage().get(chartKey);	   
   }
}
