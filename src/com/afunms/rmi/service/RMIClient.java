package com.afunms.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClient extends Remote {

    RMIAttribute sendMessage(RMIClientAction clientAction) throws RemoteException;
}
