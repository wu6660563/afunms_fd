package com.afunms.common.util;

import ChartDirector.BarLayer;
import ChartDirector.Chart;
import ChartDirector.LegendBox;
import ChartDirector.XYChart;

public class CreateBarPic {

    /**
     * @author lgw
     * @date Feb 28, 2011 1:30:27 PM
     * @param args void
     * @Description: TODO(�����������������) 
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//      CreateBarPic cbp = new CreateBarPic();
//      double[] data1 = {85, 156, 179, 211, 123,50,70};
//       double[] data2 = {97, 87, 56, 267, 157,40,120};
//       String[] labels = {"�й�", "����", "����", "�¼���", "�ձ�","���","�¹�"};
//       TitleModel tm = new TitleModel();
//       tm.setBgcolor(0xffffff);
//       tm.setXpic(280);
//       tm.setYpic(200);
//       tm.setX1(30);
//       tm.setX2(15);
//       tm.setX3(230);
//       tm.setX4(140);
//       tm.setX5(90);
//       tm.setX6(170);
//       int color1 = 0x80ff80;
//       int color2 = 0x8080ff;
//      cbp.createCylindricalPic(data1,data2,labels,tm,"�й�","�ձ�",color1,color2);
        CreateBarPic cbp = new CreateBarPic();

         TitleModel tm = new TitleModel();
         tm.setBgcolor(0xffffff);
         tm.setXpic(280);//ͼƬ���
         tm.setYpic(200);//ͼƬ�߶�
         tm.setX1(35);//ͼ��߾���
         tm.setX2(25);//ͼ�ϱ߾���
         tm.setX3(230);//��ͼ���
         tm.setX4(140);//��ͼ�߶�
         tm.setX5(20);//����ͼ�������֮��ľ���
         tm.setX6(170);//����ͼ���붥��֮��ľ���
         int barwidth = 60;//ͼƬ�����ӵĿ��
         int color1 = 0x80ff80;
         int color2 = 0x8080ff;
        double[] data0 = {2000,3000,4000};//����
        double[] data1 = {5000,6000,7000};
        String[] dataName = {"��������","ƽ������","�������"};//ÿ�����ݶ�Ӧ������
        String[] labels = {"����","����"};//���ݵ�����
        int[] color = {0x80ff80,0x8080ff,0xffffff};//ÿ�����ݶ�Ӧ����ɫ
        cbp.createCompareThreeBarPic(data0,data1,dataName,labels,color,tm,barwidth);
    }
    /**
     * 
     * @author lgw
     * @date Feb 28, 2011 4:28:50 PM
     * @param data1
     * @param data2
     * @param labels
     * @param tm
     * @param a1
     * @param a2
     * @param color1
     * @param color2
     * @return String
     * @Description: TODO(������״ͼ)
     */
//  public String createCylindricalPic(double[] data1,double[] data2,String[] labels,TitleModel tm,String a1,String a2,int color1,int color2)
//  {
//      Chart.setLicenseCode(CommonMethod.keycode);
//      if(data1.length!=0 && data2.length!=0 && labels.length!=0 )
//      {
//          XYChart c = new XYChart(tm.getXpic(), tm.getYpic());
//          c.setBackground(tm.getBgcolor());
//          c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xf8f8f8, 0xffffff,c.dashLineColor(0xcccccc, Chart.DotLine),c.dashLineColor(0xcccccc, Chart.DotLine));
//           c.addLegend(tm.getX5(), tm.getX6(), false, "����", 9).setBackground(Chart.Transparent);
//          c.addTitle(tm.getTopTitle(), "����", 14);
//          c.yAxis().setTitle(tm.getLeftTitle()).setFontStyle("����");
//          c.xAxis().setLabelStyle("����");
//          c.xAxis().setLabels(labels);
//          //c.xAxis().setLabels(labels).setFontAngle(45); 
//          c.setTransparentColor(0xff8080);        
//          BarLayer layer = c.addBarLayer2(Chart.Stack, 8);
//          layer.addDataSet(data1, color1, a1);
//          layer.addDataSet(data2, color2, a2);
//          layer.setDataLabelStyle("����", 8);
//          layer.setBarShape(Chart.CircleShape);
//          String picname = CommonMethod.getPicName()+".png";
//          String str = CommonMethod.checkFile()+"/"+picname;
//          c.makeChart(str);
//          System.out.println("����ͼƬ·����  "+str);
//          return picname;
//      }else{
//          return null;
//      }
//      
//  }
    public void createResponseTimePic(String ip,String responsevalue,String maxresponse,String avgresponse){
        double[] r_data1 = {new Double(responsevalue), new Double(maxresponse),new Double(avgresponse)};
         String[] r_labels = {"��ǰ��Ӧʱ��(ms)", "�����Ӧʱ��(ms)","ƽ����Ӧʱ��(ms)"};      
         TitleModel tm = new TitleModel();
         tm.setPicName(ip+"response");//
         tm.setBgcolor(0xffffff);
         tm.setXpic(450);//ͼƬ����
         tm.setYpic(180);//ͼƬ�߶�
         tm.setX1(30);//�������
         tm.setX2(20);//�������
         tm.setX3(400);//��ͼ���
         tm.setX4(130);//��ͼ�߶�
         tm.setX5(10);
         tm.setX6(115);
         createTimeBarPic(r_data1,r_labels,tm,40);
         
    }
    public String createCylindricalPic(double[] data0,double[] data1,String[] labels,TitleModel tm,String a1,String a2,int color1,int color2)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
 
         XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffff, 0, 1);
         c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xf8f8f8, 0xffffff);
            //c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xffffff);
            LegendBox legendBox = c.addLegend(tm.getX5(), tm.getX6(), false, "����", 8);
            legendBox.setBackground(Chart.Transparent, Chart.Transparent);
            
        //XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffcc, 0, 1);
        //c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), c.linearGradientColor(60, 40, 60, 280, 0xffffff,0xffffff), -1, 0xffffff, 0xffffff);
        //c.addLegend(tm.getX5(), tm.getX6(), false, "����", 9).setBackground(Chart.Transparent);
        c.xAxis().setColors(0x000000);
        c.xAxis().setLabels(labels);
        c.setBackground(0xffffff);
        c.xAxis().setLabelStyle("����");
        
        
        //c.yAxis().setColors(0x000000);
        //c.setLabelFormat("{percent|0}%");
        
        BarLayer layer = c.addBarLayer2(Chart.Percentage);
        layer.addDataSet(data0, color1, a1);
        layer.addDataSet(data1, color2, a2);
        layer.setBorderColor(Chart.Transparent);
        layer.setDataLabelStyle().setAlignment(Chart.Center);
        layer.setDataLabelStyle("����", 8);
        layer.setLegend(Chart.ReverseLegend);
        layer.setBarShape(Chart.CircleShape);
        layer.setDataLabelFormat("{percent|0}%");
        //layer.setAggregateLabelStyle("Times New Roman Bold Italic", 10, 0x663300);
        //layer.setBarWidth(50)
        layer.set3D(15);
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    
    public String createBarPic(double[] data0,double[] data1,String[] labels,TitleModel tm,String a1,String a2,int color1,int color2)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
 
        XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffcc, 0, 1);
        //c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xffffff, 0xffffff);
        c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), c.linearGradientColor(60, 40, 60, 280, 0xffffff,0xffffff), -1, 0xffffff, 0xffffff);
        c.addLegend(tm.getX5(), tm.getX6(), false, "����", 9).setBackground(Chart.Transparent);
        c.xAxis().setColors(0x000000);
        c.xAxis().setLabels(labels);
        c.setBackground(0xffffff);
        //c.xAxis().setLabelStyle("����");
        //c.yAxis().setColors(0x000000);
        //c.setLabelFormat("{percent|0}%");
        
        c.xAxis().setLabelStyle("����");
        c.xAxis().setLabels(labels).setFontAngle(10);
        BarLayer layer = c.addBarLayer2(Chart.Percentage);
        layer.addDataSet(data0, color1, a1);
        layer.addDataSet(data1, color2, a2);
        layer.setBorderColor(Chart.Transparent);
        layer.setDataLabelStyle().setAlignment(Chart.Center);
        layer.setLegend(Chart.ReverseLegend);
        layer.setBarShape(Chart.CircleShape);
        //layer.setBarShape(Chart.CircleShape);
        layer.setDataLabelFormat("{percent|0}%");
        layer.set3D(10);
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    
    public String createCylindricalPicc(double[] data0,double[] data1,double[] data2,double[] data3,String[] labels,TitleModel tm,String a1,String a2,String a3,String a4,int color1,int color2,int color3,int color4)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
 
        XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffcc, 0, 1);
        //c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xffffff, 0xffffff);
        c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), c.linearGradientColor(60, 40, 60, 280, 0xffffff,0xffffff), -1, 0xffffff, 0xffffff);
        c.addLegend(tm.getX5(), tm.getX6(), false, "����", 9).setBackground(Chart.Transparent);
        c.xAxis().setColors(0x000000);
        c.xAxis().setLabels(labels);
        c.setBackground(0xffffff);
        c.xAxis().setLabelStyle("����");
        c.yAxis().setColors(0x000000);
        
        BarLayer layer = c.addBarLayer2(Chart.Percentage);
        layer.addDataSet(data0, color1, a1);
        layer.addDataSet(data1, color2, a2);
        layer.addDataSet(data2, color3, a3);
        layer.addDataSet(data3, color4, a4);
        layer.setBorderColor(Chart.Transparent);
        layer.setDataLabelStyle().setAlignment(Chart.Center);
        layer.setDataLabelStyle("����", 8);
        layer.setLegend(Chart.ReverseLegend);
        layer.setBarShape(Chart.CircleShape);
        layer.setDataLabelFormat("{percent|0}%");
        layer.setBarWidth(60);
        layer.set3D(10);
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    public String createCylindricalPiccc(double[] data0,double[] data1,double[] data2,double[] data3,double[] data4,double[] data5,String[] labels,TitleModel tm,String a1,String a2,String a3,String a4,String a5,String a6,int color1,int color2,int color3,int color4,int color5,int color6)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
        
        XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffcc, 0, 1);
        //c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xffffff, 0xffffff);
        c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), c.linearGradientColor(60, 40, 60, 280, 0xffffff,0xffffff), -1, 0xffffff, 0xffffff);
        c.addLegend(tm.getX5(), tm.getX6(), false, "����", 9).setBackground(Chart.Transparent);
        c.xAxis().setColors(0x000000);
        c.xAxis().setLabels(labels);
        c.setBackground(0xffffff);
        c.xAxis().setLabelStyle("����");
        c.yAxis().setColors(0x000000);
        
        BarLayer layer = c.addBarLayer2(Chart.Percentage);
        layer.addDataSet(data0, color1, a1);
        layer.addDataSet(data1, color2, a2);
        layer.addDataSet(data2, color3, a3);
        layer.addDataSet(data3, color4, a4);
        layer.addDataSet(data4, color5, a5);
        layer.addDataSet(data5, color6, a6);
        layer.setBorderColor(Chart.Transparent);
        layer.setDataLabelStyle().setAlignment(Chart.Center);
        layer.setDataLabelStyle("����", 8);
        layer.setLegend(Chart.ReverseLegend);
        layer.setBarShape(Chart.CircleShape);
        layer.setDataLabelFormat("{percent|2}%");
        layer.setBarWidth(60);
        layer.set3D(10);
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    
    /**
     * 
     * @author lgw
     * @date Mar 8, 2011 11:01:56 AM
     * @param data
     * @param labels
     * @param tm
     * @param barwidth
     * @return String
     * @Description: TODO(������ʱ����ʾ��Ӧ��)
     */
    public String createTimeBarPic(double[] data,String[] labels,TitleModel tm,int barwidth)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
        XYChart c = new XYChart(tm.getXpic(), tm.getYpic());
        c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xf8f8f8, 0xffffff);
        BarLayer layer = c.addBarLayer3(data);
        layer.set3D(10);
        c.xAxis().setLabelStyle("����");

        layer.setBarShape(Chart.CircleShape);
        layer.setBarWidth(barwidth);
        layer.setAggregateLabelStyle("Times New Roman Bold Italic", 10, 0x663300);
        c.xAxis().setLabels(labels);
        c.xAxis().setTitle(tm.getTopTitle(),"����");
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    
    /**
     * 
     * @author lgw
     * @date Mar 8, 2011 11:01:56 AM
     * @param data
     * @param labels
     * @param tm
     * @param barwidth
     * @return String
     * @Description: TODO(������ʱ����ʾ��Ӧ��)
     */
    public String createRoundTimeBarPic(double[] data,String[] labels,TitleModel tm,int barwidth)
    {
        Chart.setLicenseCode(CommonMethod.keycode);
        XYChart c = new XYChart(tm.getXpic(), tm.getYpic());
        c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xf8f8f8, 0xffffff);
        BarLayer layer = c.addBarLayer3(data);
        layer.set3D(10);
        c.xAxis().setLabelStyle("����");

        layer.setBarShape(Chart.DiamondShape);
        layer.setBarWidth(barwidth);
        //layer.setBarShape(10);
        layer.setAggregateLabelStyle("Times New Roman Bold Italic", 10, 0x663300);
        c.xAxis().setLabels(labels);
        c.xAxis().setTitle(tm.getTopTitle(),"����");
        String picname = tm.getPicName()+".png";
        String str = CommonMethod.checkFile()+"/"+picname;
        c.makeChart(str);
        return picname;
    }
    
    /**
     * 
     * @author lgw
     * @date Mar 9, 2011 9:17:19 AM
     * @param data0 ����һ
     * @param data1 ���ݶ�
     * @param labels  x�������Ӧ������
     * @param tm 
     * @param a1 
     * @param a2
     * @param color1 a1 ��Ӧ����ɫ
     * @param color2 a2 ��Ӧ����ɫ
     * @param barwidth  ���ӵĿ��
     * @param angle x������ĽǶ�
     * @return String
     * @Description: TODO(��ͨ��3D��ͼ)
     */
    public String createNormalBarPic(double[] data0,double[] data1,String[] labels,TitleModel tm,String a1,String a2,int color1,int color2,int barwidth,int angle)
    {
            Chart.setLicenseCode(CommonMethod.keycode);
            XYChart c = new XYChart(tm.getXpic(), tm.getYpic(), 0xffffff, 0, 1);
            c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xffffff);
            LegendBox legendBox = c.addLegend(tm.getX5(), tm.getX6(), false, "����", 8);
            legendBox.setBackground(Chart.Transparent, Chart.Transparent);
            //legendBox.setKeySize(16, 32);
            
            c.xAxis().setLabelStyle("����");
            c.xAxis().setLabels(labels).setFontAngle(angle);
            BarLayer layer = c.addBarLayer2(Chart.Percentage);
            layer.addDataSet(data0, color1, a1);
            layer.addDataSet(data1, color2, a2);
            layer.setBorderColor(Chart.Transparent);
            layer.setDataLabelStyle().setAlignment(Chart.Center);
            layer.setLegend(Chart.ReverseLegend);
            layer.setBarShape(Chart.CircleShape);
            layer.setDataLabelFormat("{percent|0}%");
            layer.set3D(barwidth);
            layer.setBarWidth(barwidth);
            String picname = tm.getPicName()+".png";
            String str = CommonMethod.checkFile()+"/"+picname;
            c.makeChart(str);

        return picname;
    }
    /**
     * 
     * @author lgw
     * @date Mar 15, 2011 6:18:57 PM
     * @param data0
     * @param data1
     * @param dataName
     * @param labels
     * @param color
     * @param tm
     * @param barwidth
     * @return String
     * @Description: TODO(�������ӱȽϵ�ͼ)
     */
    public String createCompareThreeBarPic(double[] data0, double[] data1,String[] dataName,String[] labels,int[] color,TitleModel tm,int barwidth)
    {
            Chart.setLicenseCode(CommonMethod.keycode);
            double[] data2 = {data0[0],data1[0]};
            double[] data3 = {data0[1],data1[1]};
            double[] data4 = {data0[2],data1[2]};
            XYChart c = new XYChart(tm.getXpic(), tm.getYpic());
            c.setPlotArea(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4(), 0xf8f8f8, 0xffffff);
            c.addLegend(tm.getX5(), tm.getX6(), false,"����",9).setBackground(Chart.Transparent);
            c.xAxis().setLabels(labels);
            c.xAxis().setTickOffset(0.5);
            c.xAxis().setLabelStyle("����");
            BarLayer layer = c.addBarLayer2(Chart.Side, 9);
            layer.addDataSet(data2, color[0], dataName[0]);
            layer.addDataSet(data3, color[1], dataName[1]);
            layer.addDataSet(data4, color[2], dataName[2]);
            layer.setBarShape(Chart.NoShape, 0);
            layer.setBarShape(Chart.NoShape, 1);
            layer.setBarShape(Chart.NoShape, 2);
            layer.setBarWidth(barwidth);
            layer.setDataLabelStyle();
            String picname = tm.getPicName()+".png";
            String str = CommonMethod.checkFile()+"/"+picname;
            c.makeChart(str);
            return picname;

    }
}
