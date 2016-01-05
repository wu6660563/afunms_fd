package com.afunms.common.util;

import java.util.ArrayList;
import java.util.List;
import com.afunms.initialize.ResourceCenter;
import ChartDirector.AngularMeter;
import ChartDirector.Chart;

public class CreateMetersPic {

    /**
     * @author lgw
     * @date Feb 28, 2011 5:09:48 PM
     * @param args
     *            void
     * @Description: TODO(�����������������)
     */
    public static void main(String[] args) {
        try{
        CreateMetersPic c=new CreateMetersPic();
        
        c.createPic("127001", 50.0, "D:/apache-tomcat-afunms/webapps/afunms/resource/image/jfreechart",
                "�ڴ�������","avgpmemory") ;
        System.out.println("��ͼ��ϣ�" );
        }catch(Exception ex){
            System.out.println(""+ex.getMessage().toString());
            
        } 
    } 

    int [] x=Chart.blueMetalGradient ;
    /**
     * ָ���ڲ���ɫ
     */
    int pointFillColor=0x80cccccc; 
    /**
     * ָ��߿���ɫ
     */
    int pointBorderColor=0x26261a; 
     
    int innerAreaColor1=0xFF9900;
    int innerAreaColor2=0xffff00;
    int innerAreaColor3=0x234793;
    
    
    
    public void createChartByParam(String ip, String value,String bgImagePath,String title,String type)
    {
        Chart.setLicenseCode(CommonMethod.keycode); 
        AngularMeter m = new AngularMeter(150, 150 , -2); 
        m.setDefaultFonts("Times New Roman","Bold");  //������Bold ������ʾȴ���Ǵ��壬���� 
        //m.setDefaultFonts("simsun.ttc"); 
        m.setColors(x);//�Ǳ�����ɫ��ʽ 
        m.setBackground(0xffffff); //���ñ�����ɫ
        m.setMeter(79, 79, 60, -135, 135);//�����Ǳ�������Ϊ80�����Ϊ60  
        m.setScale(0, 100, 10, 5, 0);//���ÿ̶�Ϊ0-100��10��ӵ��һ�����̶ȱ�־��5��һ��С�̶ȿ̶ȱ�־����С�̶�Ϊ1  
        m.setLineWidth(0, 2, 1); //��������Ϊ ���ߵĿ��,  ���̶ȵĿ�ȣ�С�̶ȵĿ��  
//      m.addRing(0, 40, Chart.metalColor(0xfff0ff));// �������ö���� ����ӻ�����ǰ��Ļ� 
//      m.addRing(2, 40, 0x6666FF); 
        //TODO
        //���򻮷�  
        m.addZone(0, 20, innerAreaColor1);
        m.addZone(20, 40, innerAreaColor2); 
        m.addZone(40, 100, innerAreaColor3);//���򻮷֣��� 0-60�ȣ�������ɫΪ�� 
        double valueD=0;
        try{
            valueD=Double.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        //TODO
      
        m.addText(79, 122,  title, "����", 10, Chart.TextColor, Chart.Center);  //����ı�  
        m.addText(79, 105, m.formatValue(valueD, "2"), "Arial", 8, 0x000000, Chart.Center).setBackground(0xffffff, 0xffffff, -1);//����ı� 
 
        //TODO 
       // setShape  ��������ֵ [ 0,1 ,2 ,3 ,4]
        m.addPointer(valueD, pointFillColor,pointBorderColor ).setShape(Chart.ArrowPointer2);  //���ָ����ɫ����״  
      
        if (bgImagePath == "") {
            m.setBgImage(ResourceCenter.getInstance().getSysPath()+ "resource/image/dashboard.png");
        } else {
            m.setBgImage(bgImagePath);
        } 
        String picname =ip+type+ ".png";
        String str = CommonMethod.checkFile() + "/" + picname;
        m.makeChart(str);  
    }
    /** 
     * @param ip//IP
     * @param value//�Ǳ���ֵ
     * @param bgImagePath //����ͼƬ·��
     * @param title �Ǳ��̱���
     * @param type (�����ļ����Ƶ���ɲ��֣�ǰΪ[IP+type]���磨127.0.0.1cup��
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ)
     */
    public void createPic(String ip, Double value, String bgImagePath,
            String title, String type) {
        MeterModel mm = new MeterModel();
        mm.setBgColor(0xffffff);
        mm.setInnerRoundColor(0xececec);
        mm.setOutRingColor(0x80ff80);
        mm.setTitle(title);
        mm.setPicx(150);//
        mm.setPicy(150);//
        mm.setMeterX(80);//
        mm.setMeterY(80);//
        mm.setPicName(ip + type);//
        mm.setValue(value);//
        mm.setMeterSize(60);// �����Ǳ��̴�С
        mm.setTitleY(79);// ���ñ�������߾���
        mm.setTitleTop(122);// ���ñ����붥������
        mm.setValueY(78);// ����ֵ����߾���
        mm.setValueTop(105);// ����ֵ�붥������ 
        mm.setOutPointerColor(pointBorderColor);// ����ָ���ⲿ��ɫ
        mm.setInPointerColor(pointFillColor);// ����ָ���ڲ���ɫ
        mm.setFontSize(10);// ���������С
        List<StageColor> sm = new ArrayList<StageColor>();
        StageColor sc1 = new StageColor();
        sc1.setColor(innerAreaColor3);
        sc1.setStart(0);
        sc1.setEnd(60);
        StageColor sc2 = new StageColor();
        sc2.setColor(innerAreaColor2);
        sc2.setStart(60);
        sc2.setEnd(80);
        StageColor sc3 = new StageColor();
        sc3.setColor(innerAreaColor1);
        sc3.setStart(80);
        sc3.setEnd(100);
        sm.add(sc1);
        sm.add(sc2);
        sm.add(sc3);
        mm.setList(sm);
        this.createSimpleMeter(mm,bgImagePath);

    }  
    /**
     *  
     * @param mm
     * @param bgImagePath
     */
    public void createSimpleMeter(MeterModel mm, String bgImagePath) {
        Chart.setLicenseCode(CommonMethod.keycode);
        if (!mm.equals("")) {
            AngularMeter m = new AngularMeter(mm.getPicx(), mm.getPicy(),
                    0x80ff80, 0xffffff, -2); 
            m.setDefaultFonts("Times New Roman","Bold"); 

            m.setColors(x);//�Ǳ�����ɫ��ʽ 
            m.setBackground(mm.getBgColor());
            m.setMeter(mm.getMeterX(), mm.getMeterY(), mm.getMeterSize(), -135,
                    135);
            m.setScale(0, 100, 10, 5, 0);
            m.setLineWidth(0, 2, 1);
            if (bgImagePath == "") {
                m.setBgImage(ResourceCenter.getInstance().getSysPath()
                        + "resource/image/dashboard.png");
            } else {
                m.setBgImage(bgImagePath);
            }
            // m.addRing(0, 90,
            // Chart.metalColor(mm.getInnerRoundColor()));//��Բ��ɫ
            // m.addRing(88, 90, mm.getOutRingColor());//��Ȧ��ɫ
            if (!mm.getList().isEmpty()) {
                for (int i = 0; i < mm.getList().size(); i++) {
                    m.addZone(mm.getList().get(i).getStart(), mm.getList().get(i).getEnd(), mm.getList().get(i).getColor());
                }
            }
            m.addText(mm.getTitleY(), mm.getTitleTop(), mm.getTitle(), "����", mm.getFontSize(), 0x000000, Chart.Center);
            m.addPointer(mm.getValue(), mm.getInPointerColor(),mm.getOutPointerColor()).setShape(Chart.ArrowPointer2);
            m.addText(mm.getValueY(), mm.getValueTop(),m.formatValue(mm.getValue(), "2"), "����", mm.getFontSize(),0x000000, Chart.Center).setBackground(0xffffff, 0xffffff,-1);

            String picname = mm.getPicName() + ".png";
            String str = CommonMethod.checkFile() + "/" + picname;
            m.makeChart(str); 
        }
    }
    /**
     * 
     * @author wxy
     * @date Mar 12, 2011 9:26:05 AM
     * @param mm
     * @return String
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ-CPU������)
     */
    public void createCpuPic(String ip, int cpuper) {
        // CreateMetersPic cmp = new CreateMetersPic();
        MeterModel mm = new MeterModel();
        mm.setBgColor(0xffffff);
        mm.setInnerRoundColor(0xececec);
        mm.setOutRingColor(0x80ff80);
        mm.setTitle("CPU������");
        mm.setPicx(150);//
        mm.setPicy(150);//
        mm.setMeterX(80);//
        mm.setMeterY(80);//
        mm.setPicName(ip + "cpu");//
        mm.setValue(cpuper);//
        mm.setMeterSize(60);// �����Ǳ��̴�С
        mm.setTitleY(79);// ���ñ�������߾���
        mm.setTitleTop(122);// ���ñ����붥������
        mm.setValueY(78);// ����ֵ����߾���
        mm.setValueTop(105);// ����ֵ�붥������ 
        mm.setOutPointerColor(pointBorderColor);// ����ָ���ⲿ��ɫ
        mm.setInPointerColor(pointFillColor);// ����ָ���ڲ���ɫ
        mm.setFontSize(10);// ���������С
        List<StageColor> sm = new ArrayList<StageColor>();
        StageColor sc1 = new StageColor();
        sc1.setColor(innerAreaColor3);
        sc1.setStart(0);
        sc1.setEnd(60);
        StageColor sc2 = new StageColor();
        sc2.setColor(innerAreaColor2);
        sc2.setStart(60);
        sc2.setEnd(80);
        StageColor sc3 = new StageColor();
        sc3.setColor(innerAreaColor1);
        sc3.setStart(80);
        sc3.setEnd(100);
        sm.add(sc1);
        sm.add(sc2);
        sm.add(sc3);
        mm.setList(sm);
        this.createSimpleMeter(mm);
    }

    /**
     * 
     * @author wxy
     * @date Mar 12, 2011 9:26:05 AM
     * @param mm
     * @return String
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ-CPU���������)
     */
    public void createMaxCpuPic(String ip, String cpumax) {

        MeterModel mm = new MeterModel();
        mm = new MeterModel();
        mm.setBgColor(0xffffff);
        mm.setInnerRoundColor(0xececec);
        mm.setOutRingColor(0x80ff80);
        mm.setTitle("CPU������");
        mm.setPicx(150);//
        mm.setPicy(150);//
        mm.setMeterX(80);//
        mm.setMeterY(80);//
        mm.setPicName(ip + "cpumax");//
        mm.setValue(new Double(cpumax.replaceAll("%", "")));//
        mm.setMeterSize(60);// �����Ǳ��̴�С
        mm.setTitleY(79);// ���ñ�������߾���
        mm.setTitleTop(122);// ���ñ����붥������
        mm.setValueY(78);// ����ֵ����߾���
        mm.setValueTop(105);// ����ֵ�붥������ 
        mm.setOutPointerColor(pointBorderColor);// ����ָ���ⲿ��ɫ
        mm.setInPointerColor(pointFillColor);// ����ָ���ڲ���ɫ
        mm.setFontSize(10);// ���������С
        List<StageColor> sm = new ArrayList<StageColor>();
        StageColor sc1 = new StageColor();
        sc1.setColor(innerAreaColor3);
        sc1.setStart(0);
        sc1.setEnd(60);
        StageColor sc2 = new StageColor();
        sc2.setColor(innerAreaColor2);
        sc2.setStart(60);
        sc2.setEnd(80);
        StageColor sc3 = new StageColor();
        sc3.setColor(innerAreaColor1);
        sc3.setStart(80);
        sc3.setEnd(100);
        sm.add(sc1);
        sm.add(sc2);
        sm.add(sc3);
        mm.setList(sm);
        createSimpleMeter(mm);
    }


    /**
     * 
     * @author wxy
     * @date Mar 12, 2011 9:26:05 AM
     * @param mm
     * @return String
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ-CPUƽ��������)
     */
    public void createAvgCpuPic(String ip, String avgcpu) {
        MeterModel mm = new MeterModel();
        mm = new MeterModel();
        mm.setBgColor(0xffffff);
        mm.setInnerRoundColor(0xececec);
        mm.setOutRingColor(0x80ff80);
        mm.setTitle("CPU������");
        mm.setPicx(150);//
        mm.setPicy(150);//
        mm.setMeterX(80);//
        mm.setMeterY(80);//
        mm.setPicName(ip + "cpuavg");//
        mm.setValue(new Double(avgcpu.replaceAll("%", "")));//
        mm.setMeterSize(60);// �����Ǳ��̴�С
        mm.setTitleY(79);// ���ñ�������߾���
        mm.setTitleTop(122);// ���ñ����붥������
        mm.setValueY(78);// ����ֵ����߾���
        mm.setValueTop(105);// ����ֵ�붥������
        mm.setOutPointerColor(pointBorderColor);// ����ָ���ⲿ��ɫ
        mm.setInPointerColor(pointFillColor);// ����ָ���ڲ���ɫ
        mm.setFontSize(10);// ���������С
        List<StageColor> sm = new ArrayList<StageColor>();
        StageColor sc1 = new StageColor();
        sc1.setColor(innerAreaColor3);
        sc1.setStart(0);
        sc1.setEnd(60);
        StageColor sc2 = new StageColor();
        sc2.setColor(innerAreaColor2);
        sc2.setStart(60);
        sc2.setEnd(80);
        StageColor sc3 = new StageColor();
        sc3.setColor(innerAreaColor1);
        sc3.setStart(80);
        sc3.setEnd(100);
        sm.add(sc1);
        sm.add(sc2);
        sm.add(sc3);
        mm.setList(sm);
        createSimpleMeter(mm);
    }

    /**
     * 
     * @author lgw
     * @date Mar 1, 2011 9:26:05 AM
     * @param mm
     * @return String
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ)
     */
    public String createSimpleMeter(MeterModel mm) {
        Chart.setLicenseCode(CommonMethod.keycode);
        if (!mm.equals("")) {
            AngularMeter m = new AngularMeter(mm.getPicx(), mm.getPicy(),
                    0x80ff80, 0xffffff, -2);
            m.setDefaultFonts("Times New Roman","Bold");   
            m.setColors(x);//�Ǳ�����ɫ��ʽ 
            m.setBackground(mm.getBgColor());
            m.setMeter(mm.getMeterX(), mm.getMeterY(), mm.getMeterSize(), -135,
                    135);
            m.setScale(0, 100, 10, 5, 0);
            m.setLineWidth(0, 2, 1);
            m.setBgImage(ResourceCenter.getInstance().getSysPath()
                    + "resource/image/dashboard.png");
            // m.addRing(0, 90,
            // Chart.metalColor(mm.getInnerRoundColor()));//��Բ��ɫ
            // m.addRing(88, 90, mm.getOutRingColor());//��Ȧ��ɫ
            if (!mm.getList().isEmpty()) {
                for (int i = 0; i < mm.getList().size(); i++) {
                    m.addZone(mm.getList().get(i).getStart(), mm.getList().get(
                            i).getEnd(), mm.getList().get(i).getColor());
                }
            }
            m.addText(mm.getTitleY(), mm.getTitleTop(), mm.getTitle(), "����", mm
                    .getFontSize(), 0x000000, Chart.Center);
            m.addPointer(mm.getValue(), mm.getInPointerColor(),
                    mm.getOutPointerColor()).setShape(Chart.ArrowPointer2);
            m.addText(mm.getValueY(), mm.getValueTop(),
                    m.formatValue(mm.getValue(), "2"), "����", mm.getFontSize(),
                    0x000000, Chart.Center).setBackground(0xffffff, 0xffffff,
                    -1);

            String picname = mm.getPicName() + ".png";
            String str = CommonMethod.checkFile() + "/" + picname;
            m.makeChart(str);
            return picname;
        }
        return null;
    }

    /**
     * 
     * @author konglq
     * @date Mar 12, 2011 9:26:05 AM
     * @param mm
     * @return String
     * @Description: TODO(���Ƽ򵥵��Ǳ���ͼ-ͨ�÷���)
     * @ip ip��ַ
     * @cpuper ������
     * @title ����
     * 
     * 
     * ���ɵ�ͼƬ����ip+publicΪ��ǵ�ͼƬ
     * 
     */
    public void createpubliccpuPic(String ip, int cpuper, String title) {
        // CreateMetersPic cmp = new CreateMetersPic();
        MeterModel mm = new MeterModel();
        mm.setBgColor(0xffffff);
        mm.setInnerRoundColor(0xececec);
        mm.setOutRingColor(0x80ff80);
        mm.setTitle(title);
        mm.setPicx(150);//
        mm.setPicy(150);//
        mm.setMeterX(80);//
        mm.setMeterY(80);//
        mm.setPicName(ip + "public");//
        mm.setValue(cpuper);//
        mm.setMeterSize(60);// �����Ǳ��̴�С
        mm.setTitleY(79);// ���ñ�������߾���
        mm.setTitleTop(122);// ���ñ����붥������
        mm.setValueY(78);// ����ֵ����߾���
        mm.setValueTop(105);// ����ֵ�붥������
        mm.setOutPointerColor(pointBorderColor);// ����ָ���ⲿ��ɫ
        mm.setInPointerColor(pointFillColor);// ����ָ���ڲ���ɫ
        mm.setFontSize(10);// ���������С
        List<StageColor> sm = new ArrayList<StageColor>();
        StageColor sc1 = new StageColor();
        sc1.setColor(innerAreaColor3);
        sc1.setStart(0);
        sc1.setEnd(60);
        StageColor sc2 = new StageColor();
        sc2.setColor(innerAreaColor2);
        sc2.setStart(60);
        sc2.setEnd(80);
        StageColor sc3 = new StageColor();
        sc3.setColor(innerAreaColor1);
        sc3.setStart(80);
        sc3.setEnd(100);
        sm.add(sc1);
        sm.add(sc2);
        sm.add(sc3);
        mm.setList(sm);
        this.createSimpleMeter(mm);
    }

}
