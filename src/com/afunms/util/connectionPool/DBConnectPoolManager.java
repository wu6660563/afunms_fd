package com.afunms.util.connectionPool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


/**
 * Explanation:
 * java.sql.Connection getConnection(String name[,time:long])
 * variable name is the name of connectionpool,which is defined in the configurable file.
 * variable time is a optional parameters, referes to the maximum number of millisecend a client can wait for.
 * <p/>
 * freeConnection(String name,Connection con)
 * variable name is the name of connectionpool
 * variable con is the current connection which would be return to connectionpool.
 */
public class DBConnectPoolManager {
    private static DBConnectPoolManager instance; // Ψһʵ��
//  private static int clients;
    private PrintWriter log;
    private Hashtable pools = new Hashtable();
    private Vector drivers = new Vector();

    /**
     * ��������˽���Է�ֹ�������󴴽�����ʵ��
     */
    private DBConnectPoolManager() {
        init();
    }

    /**
     * �����Ӷ��󷵻ظ�������ָ�������ӳ�
     *
     * @param name �������ļ��ж�������ӳ�����
     * @param con  ���Ӷ���
     */
    public void freeConnection(Connection con) {
        DBConnectionPool pool = (DBConnectionPool) pools.get("name");
        if (pool != null) {
            pool.freeConnection(con);
        }
    }

    /**
     * ���һ�����õ�(���е�)����.���û�п�������,������������С�����������
     * ����,�򴴽�������������
     *
     * @param name �������ļ��ж�������ӳ�����
     * @return Connection �������ӻ�null
     */
    public Connection getConnection() {
        DBConnectionPool pool = (DBConnectionPool) pools.get("name");
        if (pool != null) {
            return pool.getConnection().getCon();
        }
        return null;
    }

    /**
     * ���һ����������.��û�п�������,������������С���������������,
     * �򴴽�������������.����,��ָ����ʱ���ڵȴ������߳��ͷ�����.
     *
     * @param name ���ӳ�����
     * @param time �Ժ���Ƶĵȴ�ʱ��
     * @return Connection �������ӻ�null
     */
    public Connection getConnection(long time) {
        DBConnectionPool pool = (DBConnectionPool) pools.get("name");
        if (pool != null) {
            return pool.getConnection(time).getCon();
        }
        return null;
    }

    /**
     * ����Ψһʵ��.����ǵ�һ�ε��ô˷���,�򴴽�ʵ��
     *
     * @return DBConnectionManager Ψһʵ��
     */
    static synchronized public DBConnectPoolManager getInstance() {
        if (instance == null) {
            System.out.println("DBConnectpoolManager not init yet , now getInstance");
            instance = new DBConnectPoolManager();
        }
//    clients++;
//    System.out.println("clients = " + clients );
        return instance;
    }

    /**
     * �ر���������,�������������ע��
     */
    public synchronized void release() {
        // �ȴ�ֱ�����һ���ͻ��������

//    if (--clients != 0) {
//    return;
//    }

        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements()) {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
            pool.release();
        }
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements()) {
            Driver driver = (Driver) allDrivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("����JDBC�������� " + driver.getClass().getName() + "��ע��");
            } catch (SQLException e) {
                System.out.println("�޷���������JDBC���������ע��: " + driver.getClass().getName());
            }
        }
    }


    /**
     * ����ָ�����Դ������ӳ�ʵ��.
     *
     * @param props ���ӳ�����
     */
    private void createPools() {
        //String logFile = DBProperties.getLogfile();
        String url = DBProperties.getUrl();
        if (url == null) {
            System.out.println("û��Ϊ���ӳ�ָ��URL");
            return;
        }
        String user = DBProperties.getUser();
        String password = DBProperties.getPassword();
        int max = DBProperties.getMaxconn();
        int min = DBProperties.getMinconn();
        int connectCheckOutTimeout = DBProperties.getConnectCheckOutTimeout();
        int connectUseTimeout = DBProperties.getConnectUseTimeout();
        int connectUseCount = DBProperties.getConnectUseCount();

        String driverClassName = DBProperties.getDrivers();
        DBConnectionPool pool = new DBConnectionPool("name", url, user, password, min, max, connectUseTimeout, connectUseCount, connectCheckOutTimeout, "");


        pools.put("name", pool);//���õ�hash����
        //System.out.println("�ɹ��������ӳ�");
    }

    /**
     * ��ʼ�����ӳأ���ȡ�����ļ���������Ӧ�����ӳ�
     *
     * @roseuid 39FB9F19011F
     */
    private void init() {
        //InputStream is = getClass().getResourceAsStream("/db.properties");
        Properties dbProps = new Properties();
        loadDrivers();
        createPools();
    }

    /**
     * װ�غ�ע������JDBC��������
     *
     * @param props ����
     */
    private void loadDrivers() {
        String driverClasses = DBProperties.getDrivers();
        StringTokenizer st = new StringTokenizer(driverClasses);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();
            try {
                Driver driver = (Driver) Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
                drivers.addElement(driver);
                //System.out.println("�ɹ�ע��JDBC��������" + driverClassName);
            } catch (Exception e) {
                System.out.println("�޷�ע��JDBC��������: " + driverClassName + ", ����: " + e);
            }
        }
    }


    /**
     * ���ı���Ϣд����־�ļ�
     */
    private void log(String msg) {
        log.println(new Date() + ": " + msg);
    }

    /**
     * ���ı���Ϣ���쳣д����־�ļ�
     */
    private void log(Throwable e, String msg) {
        log.println(new Date() + ": " + msg);
        e.printStackTrace(log);
    }

    public Vector getPoolConnections() {
        DBConnectionPool pool = (DBConnectionPool) pools.get("name");
        return pool.getPoolConnections();
    }
}
