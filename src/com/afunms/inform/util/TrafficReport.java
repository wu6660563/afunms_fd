/**
 * <p>Description:oracle���ͳ�Ʊ���</p>
 * <p>Company: ���������ϴ�����Ƽ��ɷ����޹�˾</p>
 * @author ������
 * @project �����������
 * @date 2005-9-6
 */

package com.afunms.inform.util;

import java.text.DateFormat;
import java.util.Date;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;

import com.afunms.polling.node.Host;
import com.afunms.polling.PollingEngine;
import com.afunms.inform.dao.NodeDataDao;
import com.afunms.initialize.ResourceCenter;
import com.afunms.report.base.ImplementorReport;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;

public class TrafficReport extends ImplementorReport
{
   private int nodeId;
   private String moid;
   private boolean isValue;
   	
   public TrafficReport()
   {
   }
   
   public boolean isValue() 
   {
		return isValue;
   }
	
   public void setValue(boolean isValue) 
   {
		this.isValue = isValue;
   }

   public void setNodeId(int nodeId)
   {
	   this.nodeId = nodeId;
   }

   public void setMoid(String moid)
   {
       this.moid = moid;
   }

   //�������
   public void createReport()
   {
	  String tmpStr = "";
	  String tmpUnit = "";
	  if(moid.equals("003002")&&isValue)
	  {
		  tmpStr = "�������";
		  tmpUnit = "kbps";
	  }
	  else if(moid.equals("003002")&&!isValue)
	  {
		  tmpStr = "���������";
		  tmpUnit = "%";
	  }
	  else if(moid.equals("003003")&&isValue)
	  {
		  tmpStr = "��������";
		  tmpUnit = "kbps";
	  }
	  else if(moid.equals("003003")&&!isValue)
	  {
		  tmpStr = "����������";
		  tmpUnit = "%";
	  }
	  else if(moid.equals("003004"))
	  {
		  tmpStr = "�ӿڳ��ڴ����ʱ���";
		  tmpUnit = "%";
	  }
	  else if(moid.equals("003005"))
	  {
		  tmpStr = "�ӿڳ��ڶ����ʱ���";
		  tmpUnit = "%";
	  }
	  Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);
	  head = host.getAlias() + " �ӿ�" + tmpStr + "����";
	  note = "������Դ�������������";
	  NodeDataDao dao = new NodeDataDao();
      String[][] tmpTable = dao.trafficStat(timeStamp,nodeId,moid,isValue);
	  if(tmpTable == null)
	  {
	      table = null;
	      chart = null;
	      return;
	  }
	  int row = tmpTable.length;
      int col = tmpTable[0].length;
      tableHead = new String[row+2];
      colWidth = new int[row+2];
      colWidth[0] = 2;
      colWidth[1] = 5;
      tableHead[0] = "���";
      tableHead[1] = "ʱ��";
      table = new String[col-1][tableHead.length];
      for(int i=0; i<row; i++)
      {
    	  tableHead[i+2] = tmpTable[i][24];
    	  colWidth[i+2] = 3;
      }
      
      for(int j=0; j<col-1; j++)
      {
    	  table[j][0] = String.valueOf(j+1);
    	  table[j][1] = j + ":00";
    	  for(int k=0; k<row;k++)
    	  {
    		  table[j][k+2] = tmpTable[k][j];
    	  }
      }

      //--------��ͼ---------
      TimeSeries[] series = new TimeSeries[row]; 
	  TimeSeries series1 = null;
	  Date curDate = new Date();
	  try
	  {
	      curDate = DateFormat.getDateInstance().parse(timeStamp);
	  }
	  catch(Exception e){}
	  
	  String tmpData = "";
	  for(int j=0; j<row; j++)
	  {
		  series1 = new TimeSeries(tmpTable[j][24],Hour.class);
		  for(int i=0;i<col-1;i++)
		  {
			  tmpData = tmpTable[j][i];
			  if(tmpData=="")
				  tmpData = "0";
			  series1.add(new Hour(i,new Day(curDate)), Double.parseDouble(tmpData));
		  }
		  series[j] = series1;
	  }	
	  chartKey = ChartCreator.createMultiTimeSeriesChart(series,"HOUR","X-ʱ��(h)","Y-"+tmpStr+tmpUnit,host.getAlias()+"-"+tmpStr,500,350);
	  chart = (JFreeChartBrother)ResourceCenter.getInstance().getChartStorage().get(chartKey);
   }
}
