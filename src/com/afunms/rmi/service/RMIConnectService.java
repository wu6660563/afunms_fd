package com.afunms.rmi.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public class RMIConnectService {

    public static boolean isConnect(String host, String port, String name) {
        Remote remote = connect(host, port, name);
        if (remote == null) {
            return false;
        }
        return true;
    }
    
    public static RMIClient connect(String host, String port, String name) {
        String url = "//" + host + ":" + port +"/" +name;
        return connect(url);
    }

    public static RMIClient connect(String url) {
        RMIClient client = null;
        try {
            client = (RMIClient) Naming.lookup(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            client = null;
        } catch (RemoteException e) {
            e.printStackTrace();
            client = null;
        } catch (NotBoundException e) {
            e.printStackTrace();
            client = null;
        }
        return client;
    }
}
