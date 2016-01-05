/**
 * <p>Description:tomcat jvm stat report</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-20
 */

package com.afunms.application.util;

import com.afunms.polling.node.Tomcat;
import com.afunms.polling.PollingEngine;
import com.afunms.inform.dao.NewDataDao;
import com.afunms.report.base.ImplementorReport;
import com.afunms.report.jfree.ChartCreator;

public class TomcatJvmReport extends ImplementorReport
{
   private int nodeId;
   private String queryDate;
   
   public TomcatJvmReport()
   {
   }
   
   public void setQueryDate(String queryDate)
   {
	   this.queryDate = queryDate;
   }
   
   public void setNodeId(int nodeId)
   {
	   this.nodeId = nodeId;
   }

   //�������
   public void createReport()
   {
	   Tomcat host = (Tomcat)PollingEngine.getInstance().getNodeByID(nodeId);
       setHead(host.getAlias() + "  JVM�����ڴ汨��");
   	   setNote("������Դ�������������");
      
	   NewDataDao dao = new NewDataDao();
	   double[][] dataSet = dao.multiStat(queryDate,nodeId,"051002",true,3);
	   if(dataSet==null)
	   {
		   setTable(null);
		   setChartKey(null);
	       return;
	   }
	   
	   /**
	    * ͼ����col��row�����෴
	    * dao��colָ��ͼ�ĺ����꣬row��ͼ��������
	    */
	   String[] rowKeys = dao.getRowKeys();
	   String[] colKeys = dao.getColKeys();
      
       setTableHead(new String[]{"���","ʱ��","JVM�����ڴ�"});
       setColWidth(new int[]{2,2,3});
   	   setTable(new String[colKeys.length][tableHead.length]); 
	   for(int i=0;i<colKeys.length;i++)
	   {
	       table[i][0] = String.valueOf(i+1);  //���
	       table[i][1] = String.valueOf(colKeys[i]);  //ʱ��
	       table[i][2] = String.valueOf(dataSet[0][i]).replace(".0", "");
	   }

       //--------��ͼ---------
       chartKey = ChartCreator.createLineChart(dataSet,rowKeys,colKeys,"ʱ��","�����ڴ�(M)","",650,400);
   }
}
