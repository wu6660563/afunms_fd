package com.afunms.rmi.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServerService {

    /**
     * 注册表接受调用的端口号
     */
    private int port;

    /**
     * 注册表所在的主机(远程或本地)
     */
    private String host;

    /**
     * 未经注册表解释的简单字符串
     */
    private String name;

    /**
     * 是否开启
     */
    private boolean start;

    /**
     * 客户端
     */
    private RMIClient client;

    /**
     * 实例
     */
    private static RMIServerService instance = null;
    /**
     * 单例的构造方法
     */
    private RMIServerService() {
    }

    /**
     * 获取 RMI 服务端实例
     * @return
     */
    public static RMIServerService getInstance() {
        if (instance == null) {
            instance = new RMIServerService();
        }
        return instance;
    }

    /**
     * 初始化
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
     * 启动 Server
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
