package com.afunms.util.connectionPool;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

class ConnectionReaper extends Thread {

    private DBConnectionPool pool;
    private final long delay = 300000;

    ConnectionReaper(DBConnectionPool pool) {
        this.pool = pool;
    }

    public void run() {
        while (true) {
            try {
                sleep(delay);
            } catch (InterruptedException e) {
            }
            pool.reapIdleConnections();
        }
    }
}

/**
 * ����?
 */
public class DBConnectionPool {


    private Vector poolConnections;

    /**
     * ��������?
     * min number of connections of current pool
     */
    private int minConn;

    /**
     * ����״��?
     * max number of connections of current pool
     */
    private int maxConn;

    /**
     * ������
     * name of pool
     */
    private String name;

    /**
     * JDBC��?
     * password to connect to database
     */
    private String password;

    /**
     * ��ʾ����
     * url to database
     */
    private String URL;

    /**
     * JDBC��?
     * user to connect to database
     */
    private String user;

    /**
     * ������ʼ?
     * Maximum number of seconds a Connection can go unused before it is closed
     */
    private int connectUseTimeout;

    /**
     * ���״�Ӵ?
     * number of times a connection can be re-used before connection to database is closed and re-opened (optional parameter)
     */
    private int connectUseCount;

    /**
     * ����ӵ״ʼ�ȹԹ�ʼ��ӻ���ӹ�����������?
     * Maximum number of seconds a Thread can checkout a Connection before it is closed and returned to the pool.  This is a protection against the Thread dying and leaving the Connection checked out indefinately
     */
    private int connectCheckOutTimeout;
    private PrintWriter log;

    /**
     * ��������?
     */
    private int numRequests;//��Ҫ��������

    /**
     * ��������?
     */
    private int numWaits;//�ȴ��еĿͻ�������

    /**
     * Ҿ��ӵ����
     */
    private int numCheckOut;//�Ѿ�ȡ����������
//   private String driverClassName;
    private ConnectionReaper reaper;

    /**
     * �ҵ����������Ӷ���connection������(���ӳ�)�е�index
     * <p>Find the given connection in the pool
     *
     * @return Index into the pool, or -1 if not found
     */
    private int find(java.sql.Connection con, java.util.Vector vec) {
        int index = -1;
        // Find the matching Connection in the pool
        if ((con != null) &&
                (vec != null)) {
            for (int i = 0; i < vec.size(); i++) {
                ConnectionObject co = (ConnectionObject)
                        vec.elementAt(i);
                if (co.getCon() == con) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * <p>Sets the last access time for the given ConnectionObject
     *
     * @param co ��Ҫ����������ʱ���ConnectionObject����
     */
    private void touch(ConnectionObject co) {
        if (co != null) {
            co.setLastAccess(System.currentTimeMillis());
        }
    }

    /**
     * �ر�ConnectionObject�е�����
     * <p>Closes the connection in the given ConnectionObject
     *
     * @param connectObject ConnectionObject
     */
    private void close(ConnectionObject connectionObject) {
        if (connectionObject != null) {
            if (connectionObject.getCon() != null) {
                try {

                    // Close the connection
                    connectionObject.getCon().close();
                } catch (Exception ex) {
                    // Ignore any exceptions during close
                }

                // Clear the connection object reference
                connectionObject.setCon(null);
            }
        }
    }

    /**
     * ������ʹ�õ����ӷ��ظ����ӳ�
     *
     * @param con �ͻ������ͷŵ�����
     */
    public synchronized void freeConnection(Connection con) {
        // Find the connection in the pool
        int index = find(con, poolConnections);

        if (index != -1) {
            ConnectionObject co = (ConnectionObject) poolConnections.elementAt(index);

            // If the use count exceeds the max, remove it from
            // the pool.
            if ((connectUseCount > 0) && (co.getUseCount() >= connectUseCount)) {
                removeFromPool(index);
            } else {
                // Clear the use count and reset the time last used
                touch(co);
                co.setInUse(false);
            }
        }
        numCheckOut--;
        notifyAll();
//    System.out.println("how many connections in the pool now? " + poolConnections.size()) ;
    }

    /**
     * ��ָ��index��ConnectionObject��������ӳ���ɾ��
     * <p>Removes the ConnectionObject from the pool at the
     * given index
     *
     * @param index Index into the pool vector
     */

    private synchronized void removeFromPool(int index) {
        // Make sure the pool and index are valid
        if (poolConnections != null) {
            if (index < poolConnections.size()) {
                // Get the ConnectionObject and close the connection
                ConnectionObject co = (ConnectionObject) poolConnections.elementAt(index);
                close(co);
                // Remove the element from the pool
                poolConnections.removeElementAt(index);
            }
        }
        notifyAll();
    }

    /**
     * ɾ����������
     *
     * @param co ��Ҫ�����ӳ���ɾ����ConnectionObject ����
     */

    private synchronized void removeConnection(ConnectionObject co) {
    	System.out.println("=ɾ������������==");
        poolConnections.remove(co);
        try {
            co.getCon().close();
        } catch (SQLException ex) {
            System.out.println("��ʱ���ӹر�ʧ��");
        }
    }

    /**
     * �����ӳػ��һ����������.��û�п��е������ҵ�ǰ������С���������
     * ������,�򴴽�������.
     * ���ȡ�õ��ǲ����õ����ӣ����ų������ӳ������һ������
     */
    public synchronized ConnectionObject getConnection() {
        ConnectionObject con = new ConnectionObject();
        con = null;
        System.out.println("============���ݿ����ӳش�С============"+poolConnections.size());
        
        
        if (poolConnections.size() > 0) {
            // ��ȡ�����е�һ����������
            try {
                int poolSize = poolConnections.size();
//        System.out.println(poolSize);
                for (int i = 0; i < poolSize; i++) {
                    ConnectionObject co = (ConnectionObject) poolConnections.elementAt(i);
                    if (co.isAvailable()) {
                        con = co;
                        boolean flg=true;
                       
                        	
                        	try{
                        	co.getCon().createStatement();
                        	}
                        	catch(Exception e)
                        	{
                        		System.out.println("=����������==");
                            	removeFromPool(i);
                            	flg=false;
                        	}
                        		
                            	
                        	
                            if(flg)
                            {
                        	System.out.println("=��������=="+con.getCon().isClosed());
                        	break;
                            }
                       
                        
                        //System.out.println("Check out the NO." + (i + 1) + " connection of pool " + name);
                        
                    }
                }
            } catch (SQLException e) {
                System.out.println("�����ӳ�" + name + "ɾ��һ����Ч����");
            }
        }
        if ((con == null) && (numCheckOut < maxConn)) {
            con = newConnection();
        }
        if (con != null) {
            con.setLastAccess(System.currentTimeMillis());
            con.setInUse(true);
            int count = con.getUseCount();
            con.setUseCount(count++);
            touch(con);
            numCheckOut++;
        }
        return con;
    }

    /**
     * �����ӳػ�ȡ��������.����ָ���ͻ������ܹ��ȴ����ʱ��
     * �μ�ǰһ��getConnection()����.
     *
     * @param timeout �Ժ���Ƶĵȴ�ʱ������
     */
    public synchronized ConnectionObject getConnection(long timeout) {
        long startTime = new Date().getTime();
        ConnectionObject con;
        while ((con = getConnection()) == null) {
            try {
                wait(timeout);
            } catch (InterruptedException e) {
            }
            if ((new Date().getTime() - startTime) >= timeout) {
                // wait()���ص�ԭ���ǳ�ʱ
                return null;
            }
        }
        return con;
    }

    /**
     * �ر���������
     */
    public synchronized void release() {
        Enumeration allConnections = poolConnections.elements();
        while (allConnections.hasMoreElements()) {
            ConnectionObject co = (ConnectionObject) allConnections.nextElement();
            try {
                co.isAvailable();
                co.getCon().close();
                System.out.println("�ر����ӳ�" + name + "�е�һ������");
            } catch (SQLException e) {
                System.out.println("�޷��ر����ӳ�" + name + "�е�����");
            }
        }
        poolConnections.removeAllElements();
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

    /**
     * �����µ�����ConnectionObject���󣬼������ӳ��У����ҷ���
     */
    private ConnectionObject newConnection() {
        ConnectionObject co = new ConnectionObject();
        try {
            if (user == null) {
                co.setCon(DriverManager.getConnection(URL));
            } else {
//        Class.forName(driverClassName).newInstance();
/*	java.sql.Connection con = DriverManager.getConnection(URL, user, password);
        if  (con.getMetaData()!=null)
        {
          System.out.println("kao");
        }
*/
//        Class.forName(driverClassName).newInstance();
                co.setCon(DriverManager.getConnection(URL, user, password));
            }
//      log("���ӳ�" + name+"����һ���µ�����");
        } catch (Exception e) {
            System.out.println("�޷���������URL������: " + URL);
            e.printStackTrace();
            return null;
        }
        co.setInUse(false);
        co.setUseCount(0);
        co.setLastAccess(0);
        co.setStartTime(System.currentTimeMillis());
        poolConnections.addElement(co);

        return co;
    }

    /**
     * �����µ����ӳ�
     *
     * @param name                   ���ӳ�����
     * @param URL                    ���ݿ��JDBC URL
     * @param user                   ���ݿ��ʺ�,�� null
     * @param password               ����,�� null
     * @param minConn                �����ӳ����ٱ��ֵ���С������
     * @param maxConn                �����ӳ������������������
     * @param connectCheckOutTimeout �����Ӵ����ӳ�ȡ������û�з��ص��������ʱ�䣬����ʱ�䣬��Ϊ����������Ч�������ӳ���ɾ��
     * @param connectUseCount        ����������ʹ�õ�������
     * @param connectUseTimeout      ��������������е��ʱ�䣬����ʱ�䣬�ر�
     * @param logFile                ��־�ļ���·���������д��־�ļ������Բ���Ҫ�ò�����
     */
    public DBConnectionPool(String name, String URL, String user, String password, int minConn, int maxConn, int useTimeout, int useCount, int checkOutTimeout, String logFile) {
        this.name = name;
        this.URL = URL;
        this.user = user;
        this.password = password;
        this.minConn = minConn;
        this.maxConn = maxConn;
        this.connectCheckOutTimeout = checkOutTimeout;
        this.connectUseCount = useCount;
        this.connectUseTimeout = useTimeout;
        this.numCheckOut = 0;
        this.numRequests = 0;
        this.numWaits = 0;
//        try {
//            log = new PrintWriter(new FileWriter(logFile, true), true);
//        } catch (IOException e) {
//            System.err.println("�޷�����־�ļ�: " + logFile);
//            log = new PrintWriter(System.err);
//        }

/*      try
      {
//        Class.forName(driverClassName).newInstance();
        Connection con = DriverManager.getConnection(URL,user,password);
        if (con.toString()!=null)
        {
          System.out.println("ok yet");
        }
        else
        {
          System.out.println("not ok yet");
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
*/
        poolConnections = new Vector();
        try {
            fillPool(this.minConn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("�޷��������ӳ�: " + name);
        }
        reaper = new ConnectionReaper(this);
        reaper.start();

    }

    /**
     * ������ӳ��е�����
     * Check  all connections to make sure they haven't:
     * 1) gone idle for too long<br>
     * 2) been checked out by a thread for too long (cursor leak)<br>
     */

    public synchronized void reapIdleConnections() {
        long now = System.currentTimeMillis();
        long idleTimeout = now - (connectUseTimeout * 1000);
//    long useTimeout = now - (connectUseTimeout * 1000);
        long checkoutTimeout = now - (connectCheckOutTimeout * 1000);
        for (Enumeration e = poolConnections.elements(); e.hasMoreElements();) {
            ConnectionObject co = (ConnectionObject) e.nextElement();
            if (co.isInUse() && (co.getLastAccess() < checkoutTimeout)) {
                System.out.println("ConnectionPool " + name + " Warning : found timeout connection\n");
                removeConnection(co);
                notifyAll();
            } else {
                if (co.getLastAccess() < idleTimeout) {
                    if (co.isInUse()) {
                        removeConnection(co);
                        notifyAll();
                    }
                }
            }
        }
        // Now ensure that the pool is still at it's minimum size
        try {
            if (poolConnections != null) {
                if (poolConnections.size() < minConn) {
                    fillPool(minConn);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * �����ӳ���䵽ָ����С
     * <p>Brings the pool to the given size
     */
    private synchronized void fillPool(int size) throws Exception {
        // Loop while we need to create more connections
        int i = 0;
        System.out.println("*********�������ӳش�С******************"+poolConnections.size());
        
        
        while (poolConnections.size() < size) {
            ConnectionObject co = newConnection();
            // Do some sanity checking on the first connection in the pool
            if (poolConnections.size() == 1) {
                // Get the maximum number of simultaneous connections
                // as reported by the JDBC driver
                java.sql.DatabaseMetaData md = co.getCon().getMetaData();
//        System.out.println(md.getMaxConnections());
                if ((md.getMaxConnections() != 0) && (maxConn > md.getMaxConnections()))
                    maxConn = md.getMaxConnections();
            }
            // Give a warning if the size of the pool will exceed
            // the maximum number of connections allowed by the
            // JDBC driver
            i++;
            //System.out.println(i);
            if ((maxConn > 0) && (size > maxConn)) {
                System.out.println("WARNING: Size of pool will exceed safe maximum of " + maxConn);
            }
            if (i == size)
                break;
        }
    }

    public Vector getPoolConnections() {
        return poolConnections;
    }
}
