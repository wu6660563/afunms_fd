/*
 * Created on 2005-7-4
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.initialize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.afunms.common.util.*;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.snmp.trap.SNMPTrapConfig;
import com.afunms.polling.snmp.trap.SNMPTrapHandler;


/**
 * ClassName: SnmpTrapsListener.java
 * <p>{@link SnmpTrapsListener} SNMP Trap ¼àÌý
 * 
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2012 10:08:36 AM
 */
public class SnmpTrapsListener {

    private static SnmpTrapsListener instance = null;

    private static List<SNMPTrapConfig> LIST = new ArrayList<SNMPTrapConfig>();
   
    private static List<DefaultUdpTransportMapping> defaultUdpTransportMappingList = new ArrayList<DefaultUdpTransportMapping>();

    private static List<Snmp> snmpList = new ArrayList<Snmp>();

    static {
    	
        String SNMPTrapFile = "WEB-INF/classes/snmp-trap.xml";
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new File(ResourceCenter.getInstance()
                    .getSysPath()
                    + SNMPTrapFile));
            @SuppressWarnings("unchecked")
            List<Element> tableNames = (List<Element>) doc.getRootElement()
                    .getChildren("snmp-trap");
            for (Element element : tableNames) {
                boolean enable = Boolean
                        .valueOf(element.getChildText("enable"));
                String port = element.getChildText("port");
                String className = element.getChildText("class-name");
                String description = element.getChildText("description");

                @SuppressWarnings("unchecked")
                Class<SNMPTrapHandler> handlerClass = (Class<SNMPTrapHandler>) Class.forName(className);
                SNMPTrapHandler handler = handlerClass.newInstance();
                
                SNMPTrapConfig config = new SNMPTrapConfig();
                config.setDescription(description);
                config.setEnable(enable);
                config.setPort(Integer.valueOf(port.trim()));
                config.setHandler(handler);

                LIST.add(config);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private SnmpTrapsListener() {
    }

    public static synchronized SnmpTrapsListener getInstance() {
    	if (instance == null)
            instance = new SnmpTrapsListener();
        return instance;
    }

    public void listen() {
        try {
            for (SNMPTrapConfig config : LIST) {
                if (config.isEnable()) {
                    UdpAddress udpAddress = new UdpAddress(config.getPort());
                    TransportMapping transport = new DefaultUdpTransportMapping(udpAddress);
                    Snmp snmp = new Snmp(transport);
                    transport.listen();
                    snmp.addCommandResponder(config.getHandler());
                    snmpList.add(snmp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            for (Snmp snmp : snmpList) {
                snmp.close();
            }
            for (DefaultUdpTransportMapping defaultUdpTransportMapping : defaultUdpTransportMappingList) {
                defaultUdpTransportMapping.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SnmpTrapsListener.getInstance().listen();

    }

}
