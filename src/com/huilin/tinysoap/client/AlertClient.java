// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   AlertClient.java

package com.huilin.tinysoap.client;

import java.util.Date;

// Referenced classes of package com.huilin.tinysoap.client:
//            TinySoap

public class AlertClient
{

    public AlertClient()
    {
    }

    public static String sendAlert(String soapUri, String appId, String title, String content, String wapurl, String uid, String misId)
    {
        String seqCode = String.valueOf((new Date()).getTime());
        wapurl = "<![CDATA[" + wapurl + "]]>";
        String soapbody = "<SendAlertRequest><AppId>" + appId + "</AppId><SeqCode>" + seqCode + "</SeqCode><Alert><Title>" + title + "</Title><Content>" + content + "</Content><PhoneNo></PhoneNo><Uid>" + uid + "</Uid><MisId>" + misId + "</MisId><URL>" + wapurl + "</URL></Alert></SendAlertRequest>";
        return TinySoap.send(soapUri, soapbody, "SendAlertRequest");
    }

    public static String sendSms(String soapUri, String appId, String content, String phoneNo)
    {
        String seqCode = String.valueOf((new Date()).getTime());
        String soapbody = "<SendSmsRequest><AppId>" + appId + "</AppId><SeqCode>" + seqCode + "</SeqCode><Content>" + content + "</Content><PhoneNo>" + phoneNo + "</PhoneNo></SendSmsRequest>";
        return TinySoap.send(soapUri, soapbody, "SendSmsRequest");
    }
}
