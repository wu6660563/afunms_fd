/**
 * <p>Description:ͨ��http�������ռ�������symantec����</p>
 * <p>Company: ���������ϴ�����Ƽ��ɷ����޹�˾</p>
 * @author ������
 * @project ��������
 * @date 2005-03-15
 */

package com.afunms.security.util;

import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.net.*;

import com.afunms.security.dao.SymantecDao;
import com.afunms.common.util.DBManager;
import com.afunms.security.model.SymantecLog;
import com.afunms.common.util.SysUtil;

public class HttpSymantecLog
{
  private String beginDate;     //����
  private String beginTime;     //����ʱ��
  private String machine;       //������
  private String machine_ip;    //����ip
  private String virus;         //����
  private String virus_file;    //����Ⱦ�ļ�
  private String deal_way;      //����ʽ
  private int historyRow;       //��־�ļ���ʼ������
  private String logFile;       //��־�ļ�  
  private SymantecLog slvo;     //��־��¼
  private SymantecDao dao;      
  
  public HttpSymantecLog()
  {
  }

  public void init(int logID)
  {
     beginDate = SysUtil.getCurrentDate();
     //�ѵ�ǰ����ת����һ����־�ļ���
     logFile = beginDate.substring(5,7) + beginDate.substring(8,10) + beginDate.substring(0,4) + ".log";
     try
     {
        dao = new SymantecDao();
        slvo = dao.findLogByID(logID); //ֻ��һ�м�¼
        if(logFile.equals(slvo.getLogFile()))
           historyRow = slvo.getLogRow();
        else
           historyRow = 1;
     }
     catch(Exception mse)
     {
        historyRow = 1;         //��һ�ζ����ڼ���
     }
  }
  
  //���Ӹ����е�http������
  private URLConnection connectHttpServer()
  {
     String info = null;
     URLConnection urlConn = null;
     try
     {
        URL url = new URL("http://" + slvo.getIp() + ":5166/" + logFile);
        System.out.println("����" + slvo.getIp() + ",Symantec������");
        urlConn = url.openConnection();
        urlConn.connect();
     }
     catch(NoRouteToHostException nre)
     {
        info = "ping��ͨ:" + slvo.getIp();
     }
     catch(FileNotFoundException fe)
     {
        info = "�Ҳ����ļ�:" + slvo.getLogFile();
     }
     catch(ConnectException ce)
     {
        info = "http������û����:" + slvo.getIp();
     }
     catch(Exception e)
     {
        info = "δ֪����:" + e.getClass();
     }

     if(info!=null)
     {
        slvo.setInfo(info);
        urlConn = null;
     }
     else  //��������,û�д���
        slvo.setInfo("����");

     return urlConn;
  }

  //������־
  public synchronized void beginTransaction()
  {  	 
     URLConnection uc = connectHttpServer();
     if(uc==null) return;

     DataInputStream dis = null;
     String oneRow = null;
     int realNew = 0,row=0,loop=0; //ʵ�ʲ���ļ�¼����
     DBManager db = null;
     try
     {    
        dis = new DataInputStream(new BufferedInputStream(uc.getInputStream()));
        db = new DBManager();
        while((oneRow = dis.readLine())!=null)
        {
          row++;
          if(row<historyRow) continue; //���ϴζ������п�ʼ
          
          if(dealOneRow(oneRow))
          {
             StringBuffer sqlBf = new StringBuffer(100);
             sqlBf.append("insert into nms_symantec(begintime,machine,machine_ip,virus,virus_file,deal_way)values(");
             sqlBf.append("'");
             sqlBf.append(beginTime);
             sqlBf.append("','");
             sqlBf.append(machine);
             sqlBf.append("','");
             sqlBf.append(machine_ip);
             sqlBf.append("','");
             sqlBf.append(virus.replace('\'','-'));
             sqlBf.append("','");
             sqlBf.append(virus_file.replace('\\','/'));
             sqlBf.append("','");
             sqlBf.append(deal_way);
             sqlBf.append("')");
            
             try
             {
                db.addBatch(sqlBf.toString());
                realNew++;
                loop++;
                if(loop>200)  //ÿ200���ύһ��
                {
                   db.executeBatch();
                   loop = 0;
                }
             }
             catch(Exception e)
             {
             }
          }//end_if_ok
        }//end_while
        db.executeBatch();
        //��oracle procedure�����������ӵĹ���
        System.out.println("������" + realNew + "��Symantec��־");
    }
    catch (Exception e)
    {
       System.out.println("������־����:" + e.getMessage());
    }
    finally
    {
       db.close();
       slvo.setLogFile(logFile);
       slvo.setLogRow(row);
       dao.finish(slvo);
    }
  }

  //���ַ�ת��ʱ��
  private String stringToTime(String hexStr)
  {
    int c1,c2;
    int year=0,month=0,day=0,hour=0,minute=0,second=0;
    String result;

    for(int i=0;i<12;i+=2)
    {
       c1 = hexToDec(hexStr.substring(i,i+1));
       c2 = hexToDec(hexStr.substring(i+1,i+2));
       if(i==0)
           year = c1*16 + c2 + 1970;
       else if(i==2)
           month = c1*16 + c2 + 1;
       else if(i==4)
           day = c1*16 + c2;
       else if(i==6)
           hour = c1*16 + c2;
       else if(i==8)
           minute = c1*16 + c2;
       else if(i==10)
           second = c1*16 + c2;
    }
    String tmpM = null;
    String tmpD = null;    
    if(month>9)
        tmpM = "" + month;
     else
        tmpM = "0" + month;

     if(day>9)
        tmpD = "" + day;
     else
        tmpD = "0" + day;
     
     result = year + "-" + tmpM + "-" + tmpD + " " + hour + ":" + minute + ":" + second;
    
    return result;
  }

  private int hexToDec(String h)
  {
     if(h.equals("A"))
        return 10;
     else if(h.equals("B"))
        return 11;
     else if(h.equals("C"))
        return 12;
     else if(h.equals("D"))
        return 13;
     else if(h.equals("E"))
        return 14;
     else if(h.equals("F"))
        return 15;
     else
        return Integer.parseInt(h);
  }

  //������������ʽ
  private String getDealWay(String dw)
  {
    if(dw.equals("1"))
       return "���뱻��Ⱦ�ļ�";
    else if(dw.equals("2"))
       return "����������Ⱦ�ļ�";
    else if(dw.equals("3"))
       return "ɾ������Ⱦ�ļ�";
    else if(dw.equals("4"))
       return "����¼,������";
    else if(dw.equals("5"))
       return "�������";
    else if(dw.equals("6"))
       return "�������";
    else
       return "δ֪";
  }

  private String getMachineIP(String mip)
  {
     if(mip==null||"".equals(mip)||mip.indexOf("(IP)")==-1)
        return "";

     return mip.substring(5);
  }

  //����һ��
  private boolean dealOneRow(String one_Row)
  {
    String oneRow = SysUtil.getChinese(one_Row); //ת��������
    boolean ok = true;
    try
    {
      StringTokenizer st = new StringTokenizer(oneRow, ",");
      int i = 0;
      while(st.hasMoreTokens())
      {
         if(i==0)
            beginTime = stringToTime(st.nextToken()); //ʱ��
         else if(i==4)
            machine = st.nextToken();        //������
         else if(i==6)
            virus = st.nextToken();          //������
         else if(i==7)
         {	
            String vf = st.nextToken().replace('\'','-');    
         	virus_file = vf.replace('\\','/');     //����Ⱦ�ļ�         	
         }   
         else if(i==10)
            deal_way = getDealWay(st.nextToken());    //����ʽ
         else if(i==30)
            machine_ip = getMachineIP(st.nextToken());  //����ip
         else
            st.nextToken();
         i++;
      }//end_while
      try
      {
         machine_ip = machine_ip.trim();
      }
      catch(Exception e)
      {
         machine_ip = null;
      }
      if(machine_ip==null||"".equals(machine_ip)||"δ֪".equals(deal_way))
          ok = false;  //���û��ip��ַ���ߴ���ʽΪ"δ֪",�򲻲���
      if("255.255.255.255".equals(machine_ip))
          ok = false;
    }
    catch(Exception e)
    {
       ok = false;
    }
    return ok;
  }
}
