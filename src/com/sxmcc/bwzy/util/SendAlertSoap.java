package com.sxmcc.bwzy.util;


import java.net.URLEncoder;



 
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2008-12-20
 * Time: 15:06:31
 * To change this template use File | Settings | File Templates.
 */
public class SendAlertSoap {
    public static String sendSMS(String soapUri, String corpid, String password, String dest, String userid, String tmsg, String sendUserid, String sendSeqId) {

 
//    String URL="http://10.204.16.248:8081";
        try {
            tmsg = URLEncoder.encode(tmsg, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
             return null;
            //return "<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>" + sendSeqId + "</MsgId><RspCode>00000010</RspCode><RspDesc>��Ϣ���뷢������<RspDesc/></sxsms>";
        }
        String mtype = "0";   //�������ͣ��Ƿ���������,0������  1:���û�����
        String soapBody = "<msgSend><corpId>" + corpid + "</corpId><password>" + password + "</password><dest>" + dest + "</dest>"
                + "<userId>" + userid + "</userId><msg>" + tmsg + "</msg><mtype>" + mtype + "</mtype><sendUser>"
                + sendUserid + "</sendUser><sendSeqId>" + sendSeqId + "</sendSeqId></msgSend>";
        String soapURI = soapUri + "/WebService/OAToSMS";
        String soapAction = "OAToSMS";
        String result = SendSOAP.send(soapURI, soapBody, "");

        //����ֵ��һ��xml����:<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>ff8080811d17d52e011d1d1062ac000a</MsgId><RspCode>00000000</RspCode></sxsms>
        //����rspCode='00000000' ��ʾ�ɹ�������������Ϊʧ��

        return result;


    }

    public static String sendPush(String soapUri, String corpid, String password, String dest, String userid, String title, String tmsg, String sendUserid, String sendSeqId) {

        try {
            tmsg = URLEncoder.encode(tmsg, "GBK");
            title = URLEncoder.encode(title, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
             return null;
            //return "<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>" + sendSeqId + "</MsgId><RspCode>00000010</RspCode><RspDesc>��Ϣ���뷢������<RspDesc/></sxsms>";
        }
        String docType = "1";

        String soapBody = "<wapPush><corpId>" + corpid + "</corpId><password>" + password + "</password><dest>" + dest + "</dest>"
                + "<userId>" + userid + "</userId><title>" + title + "</title><msgUrl>" + tmsg + "</msgUrl><sendUser>"
                + sendUserid + "</sendUser><sendSeqId>" + sendSeqId + "</sendSeqId></wapPush>";
        String soapURI = soapUri + "/WebService/OAToWapPush";
        String soapAction = "OAToWapPush";
        String result = SendSOAP.send(soapURI, soapBody, "");
        //����ֵ��һ��xml����:<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>ff8080811d17d52e011d1d1062ac000a</MsgId><RspCode>00000000</RspCode></sxsms>
        //����rspCode='00000000' ��ʾ�ɹ�������������Ϊʧ��

        return result;


    }

    public static String sendWapPush(String soapUri, String corpid, String password, String dest, String userid, String title, String tmsg, String sendUserid, String sendSeqId) {

        try {
            tmsg = URLEncoder.encode(tmsg, "GBK");
            title = URLEncoder.encode(title, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
           // return "<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>" + sendSeqId + "</MsgId><RspCode>00000010</RspCode><RspDesc>��Ϣ���뷢������<RspDesc/></sxsms>";
        }
        String docType = "1";

        String soapBody = "<wapPush><corpId>" + corpid + "</corpId><password>" + password + "</password><dest>" + dest + "</dest>"
                + "<userId>" + userid + "</userId><title>" + title + "</title><msgUrl>" + tmsg + "</msgUrl><sendUser>"
                + sendUserid + "</sendUser><sendSeqId>" + sendSeqId + "</sendSeqId></wapPush>";
        String soapURI = soapUri + "/WebService/AlertWapPush";
        String soapAction = "AlertWapPush";
        String result = SendSOAP.send(soapURI, soapBody, "");
        //����ֵ��һ��xml����:<?xml version='1.0' encoding='utf-8'?><sxsms><MsgId>ff8080811d17d52e011d1d1062ac000a</MsgId><RspCode>00000000</RspCode></sxsms>
        //����rspCode='00000000' ��ʾ�ɹ�������������Ϊʧ��

        return result;


    }

    public static void main(String[] args) {

        String tmsg = "���Ų���";



        String corpid = "99991"; //��Ҫ����ƽ̨������� ,�������ɷŵ��������������ļ��У������޸�
        String password = "12345678"; //��Ҫ����ƽ̨��������
        String dest = "13643467507";  //�����˵��ֻ�����
        String userid = "";   //����û�û���ֻ����룬���Դ��ݣգɣ� �磺maliang
        String mtype = "0";   //�������ͣ��Ƿ���������,0������  1:���û�����
        String sendUserid = "maliang"; //�����û�������  ������Ϊ��;
        String sendSeqId = "ff8080811d17d52e011d1d1062ac000a";//���ͷ�����ţ������λ
        String title = "���Զ���soap��ʽ";
 
        String soapuri = "http://10.204.16.246:8081";
        String result = SendAlertSoap.sendSMS(soapuri,corpid, password, dest,userid, tmsg,sendUserid,sendSeqId);
        // String result = SendAlertSoap.sendWapPush(soapuri,corpid, password, dest,userid, title,tmsg,sendUserid,sendSeqId);
            
       // System.out.println(result.getRspDesc());
//		System.out.println("�ͻ��˷��ͳɹ�");

    }

   


}
