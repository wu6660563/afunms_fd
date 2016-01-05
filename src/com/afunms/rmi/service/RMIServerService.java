package com.afunms.rmi.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServerService {

    /**
     * ע�����ܵ��õĶ˿ں�
     */
    private int port;

    /**
     * ע������ڵ�����(Զ�̻򱾵�)
     */
    private String host;

    /**
     * δ��ע�����͵ļ��ַ���
     */
    private String name;

    /**
     * �Ƿ���
     */
    private boolean start;

    /**
     * �ͻ���
     */
    private RMIClient client;

    /**
     * ʵ��
     */
    private static RMIServerService instance = null;
    /**
     * �����Ĺ��췽��
     */
    private RMIServerService() {
    }

    /**
     * ��ȡ RMI �����ʵ��
     * @return
     */
    public static RMIServerService getInstance() {
        if (instance == null) {
            instance = new RMIServerService();
        }
        return instance;
    }

    /**
     * ��ʼ��
     * @param host
     * @param port
     * @param name
     * @param clientClassName
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public void init(String host, int port, String name, String clientClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        setHost(host);
        setPort(port);
        setName(name);
        Class<RMIClient> clientClass = (Class<RMIClient>) Class.forName(clientClassName);
        RMIClient client = clientClass.newInstance();
        setRMIClient(client);
    }

    /**
     * ���� Server
     * @return
     */
    public boolean start() {
        try {
            if (!isStart()) {
                RMIClient clientService = getRMIClient();
                String host = getHost();
                int port = getPort();
                String name = getName();
                String url = "//" + host + ":" + port +"/" + name;
                LocateRegistry.createRegistry(port);
                Naming.rebind(url, clientService);
                start = true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isStart();
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the start
     */
    public boolean isStart() {
        return start;
    }

    /**
     * @return the rMIClientService
     */
    public RMIClient getRMIClient() {
        return this.client;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param start the start to set
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * @param client the client to set
     */
    public void setRMIClient(RMIClient client) {
        this.client = client;
    }

    

}
