package com.afunms.rmi.service;

import java.io.PrintStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.afunms.rmi.service.RMIClient;



public class RMIClientImpl extends UnicastRemoteObject implements RMIClient, Serializable{

    public RMIClientImpl() throws RemoteException {
        super();
    }

    private static final long serialVersionUID = 9216797171187476902L;
    
    private static PrintStream printStream = null;

    public RMIAttribute sendMessage(RMIClientAction clientAction) throws RemoteException {
        RMIAttribute attribute = null;
        try {
            getLog().println("¥¶¿Ì clientAction :" + clientAction.getAction());
            attribute = RMIServerHandleActionService.getInstance().handleAction(clientAction);
        } catch (ClassNotFoundException e) {
            getLog().println("Œ¥’“µΩ "+  clientAction.getAction() + "≈‰÷√");
            getLog().println(e);
        } catch (InstantiationException e) {
            getLog().println(e);
        } catch (IllegalAccessException e) {
            getLog().println(e);
        }
        return attribute;
    }

    public static PrintStream getLog() {
        if (RMIClientImpl.printStream == null) {
            RMIClientImpl.printStream = System.out;
        }
        return RMIClientImpl.printStream;
    }

    public static void setLog(PrintStream printStream) {
        RMIClientImpl.printStream = printStream;
    }
}
