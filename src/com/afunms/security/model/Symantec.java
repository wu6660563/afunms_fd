/**
 * <p>Description: nms_symantec��¼</p>
 * <p>Company: ���������ϴ�����Ƽ��ɷ����޹�˾</p>
 * @author ������
 * @project ��������
 * @date 2005-3-14
 */

package com.afunms.security.model;

public class Symantec
{
   private String beginDate;
   private String machine;
   private String machineIp;
   private String virus;
   private String virusFile;
   private String dealWay;

   public void setBegintime(String beginDate)
   {
      this.beginDate = beginDate;
   }

   public String getBeginDate()
   {
      return beginDate;
   }

   public void setMachine(String newMachine)
   {
      machine = newMachine;
   }

   public String getMachine()
   {
      return machine;
   }

   public void setMachineIp(String newMachineIp)
   {
      machineIp = newMachineIp;
   }

   public String getMachineIp()
   {
      return machineIp;
   }

   public void setVirus(String newVirus)
   {
      virus = newVirus;
   }

   public String getVirus()
   {
      return virus;
   }

   public void setVirusFile(String newVirusFile)
   {
      virusFile = newVirusFile;
   }

   public String getVirusFile()
   {
      return virusFile;
   }

   public void setDealWay(String newDealWay)
   {
      dealWay = newDealWay;
   }

   public String getDealWay()
   {
      return dealWay;
   }
}
