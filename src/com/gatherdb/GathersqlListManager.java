package com.gatherdb;


import java.text.SimpleDateFormat;
import java.util.LinkedList;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.common.util.threadPool.ThreadPoolRunnable;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.node.service.DataSendService;


/**
 * 
 * �ɼ�����sql�ڴ����ݹ�������б��뷽��
 * @author konglq
 *
 */
public class GathersqlListManager implements Runnable {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
	
	/**
	 * 
	 */
	private LinkedList<ResultToDB> linkedList = new LinkedList<ResultToDB>();

	/**
	 * ���� SQL �����̳߳�
	 */
	private ThreadPool threadPool = new ThreadPool();

	/**
	 * ����ģʽ
	 */
	private static GathersqlListManager instance = null;

	/**
     * �����߳�
     */
	private Thread thread;
	
	private GathersqlListManager() {
	}

	public static GathersqlListManager getInstance() {
	    if (instance == null) {
            instance = new GathersqlListManager();
        }
	    return instance;
	}

	public void start() {
	    threadPool.setName("DB");
	    threadPool.setMaxThreads(50);
        threadPool.setMinSpareThreads(5);
        threadPool.setMaxSpareThreads(5);
        threadPool.start();
        this.thread = new Thread(this);
        this.thread.start();
        
	}

	public void run() {
	    while (true) {
            try {
                // �ȴ�ʱ���� ����˯��
                synchronized (this) {
                    while (linkedList == null || linkedList.size() == 0) {
                        this.wait();
                    }
                    ResultToDB resultToDB = this.linkedList.removeFirst();
                    executeToDB(resultToDB);
                }
            } catch (Exception e) {
            	SysLogger.error("GathersqlListManager-->run()", e);
            	e.printStackTrace();
            }
        }
	}

	public synchronized void addToQueue(ResultToDB resultToDB) {
		try {
			this.linkedList.addLast(resultToDB);
//			
//			//�ж��Ƿ���Ҫ���л�(һ������������һ����Ҫ���л�������Ҫ���������л���Task����������Ҫ���л�������Ҫ���������л���Task)
//			//ͨ��initialize-config���������ļ������ý��
//			boolean isSerial = ResourceCenter.getInstance().isSerialObject();
//			if(isSerial){
//				String className = resultToDB.getResultTosql().getClass().getName();
//				DBAttribute attribute = resultToDB.getAttribute();
//				attribute.setAttribute("className", className);
//				Date date = new Date();
//				SerialObejctUtil.serialObject(attribute,sdf.format(date)+"_"+UUID.randomUUID()+".obj");	//���л�
//			}
			String className = resultToDB.getResultTosql().getClass().getName();
			DBAttribute attribute = resultToDB.getAttribute();
			attribute.setAttribute("className", className);
			DataSendService.sendDBAttribute(attribute);
		} catch (Exception e) {
			SysLogger.error("GathersqlListManager-->addToQueue()", e);
			e.printStackTrace();
		}
	    this.notify();
	}
	
	public void executeToDB(final ResultToDB resultToDB) {
	    ThreadPoolRunnable threadPoolRunnable = new ThreadPoolRunnable () {

            public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
                return null;
            }

            public void runIt(
                    ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
                try {
                    ResultTosql resultTosql = resultToDB.getResultTosql();
                    DBAttribute attribute = resultToDB.getAttribute();
                    resultTosql.executeResultToDB(attribute);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        threadPool.add(threadPoolRunnable);
	}
	
	

    /**
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

	
//	/**
//	 * ��sql���뵽�ڴ���У�������ݲ���ΪDHCC-DB ��ʾ�������
//	 * @param sql �ַ�������2����ʽ��һ����sql��һ���Ǳ�ʾ����ڣ�DHCC-DB��
//	 */
//	public static void Addsql(String sql) {
//		if(sql.equals("DHCC-DB")) {
//		 if(qflg==true)
//		 {
//		 //System.out.println("=====������1start=======");
//	     //System.out.println("=**=������1=="+GathersqlListManager.queue.size());	
//		 if(GathersqlListManager.queue.size()>1)
//		 {
//	     qflg=!qflg;
//	     idbstatus=true;
//		 DBManager pollmg = new DBManager();// ���ݿ�������
//		 pollmg.excuteBatchSql(GathersqlListManager.queue);
//		 pollmg.close();
//		 pollmg=null;
//		 idbstatus=false;
//		 //System.out.println("=====������1end=======");
//		 }
//		
//		 
//		 
//		 }else if(qflg==false)
//		 {
//			 //System.out.println("=====������2start=======");
//			 if(GathersqlListManager.queue2.size()>1)
//			 {
//			 //System.out.println("=**=������2=="+GathersqlListManager.queue2.size());	
//			 idbstatus=true;
//			 qflg=!qflg;
//			 DBManager pollmg = new DBManager();// ���ݿ�������
//			 pollmg.excuteBatchSql(GathersqlListManager.queue2);
//			 pollmg.close();
//			 pollmg=null;
//			 
//			 idbstatus=false;
//			 
//			 }
//			 //System.out.println("=====������2end=======");
//			 
//		 }
//		 
//		}else
//		 {
//		    if(qflg)
//            {
//                
//                synchronized (queue){
//            //System.out.println("==�������1");    
//            queue.offer(sql);
//                }
//            }else
//            {
//                synchronized (queue2){
//             //System.out.println("==�������2");
//             queue2.offer(sql);
//              }
//            }
//		 }
//	}
//	
//	
//	
//	/**
//	 * ���ݷ���ģʽsql���
//	 * ��Ϊ�����������keyΪ DHCC-DB ��ʾ�����������ӿڣ���keyֵΪsql��ɾ�����ʱ
//	 * ��ʾ�ѽ������sql���뵽�ڴ��б���,��������Ŀ����Ϊ�˱�֤�̵߳İ�ȫ�����ݵ�������
//	 * @param sql
//	 */
//	public  static void AdddateTempsql(String key,Vector sql)
//	{
//		if(key.equals("DHCC-DB"))
//		{
//			
//			
//			 //System.out.println("===��ʱ������="+datatempflg);
//			
//			 if(datatempflg==true)
//			 {
//			 if(GathersqlListManager.datatemplist.size()>0)
//			 {
//				 datatempflg=!datatempflg;
//				 idbdatatempstatus=true;
//				//System.out.println("=========================99999999====��ʼ��ʱ�������=========="+GathersqlListManager.datatemplist.size());
//				
//				 if(GathersqlListManager.datatemplist.size()>0)
//				 {
//					//Vector list=new Vector();
//					DBManager dbm=new DBManager();
//					
//				   Iterator it = GathersqlListManager.datatemplist.keySet().iterator();   
//				  while (it.hasNext()){   
//				       String keys; 
//				       
//				       
//				       keys=(String)it.next();
//				      // System.out.println("======*****==key===="+keys);
//				       //list= GathersqlListManager.datatemplist.get(keys);
//				       if(null!=GathersqlListManager.datatemplist.get(keys))
//				       {
//				    	   //logger.info(keys);
//				    	   dbm.addBatch(keys);
//				    	   for(int i=0;i<GathersqlListManager.datatemplist.get(keys).size();i++)
//				    	   {
//				    		   //logger.info(GathersqlListManager.datatemplist.get(keys).get(i).toString());
//				    		   dbm.addBatch((String)GathersqlListManager.datatemplist.get(keys).get(i).toString());
//				    	   }
//				    	   
//				       }
//				       //GathersqlListManager.datatemplist.remove(keys);
//				       
//				   } 
//				  dbm.executeBatch();
//				  dbm.close();
//				  dbm=null;
//				  it=null;
//				 }
//				  GathersqlListManager.datatemplist.clear();
//				  //System.out.println("=999999999====��ʼ��ʱ����������=========="+GathersqlListManager.datatemplist.size());
//	
//				  idbdatatempstatus=false;
//			 }
//			 
//			
//			
//			 }else if(datatempflg==false)
//			 {
//				 
//				 
//				 
//				 
//				 if(GathersqlListManager.datatemplist2.size()>0)
//					{
//						//System.out.println("=========================777777====��ʼ��ʱ�������=========="+GathersqlListManager.datatemplist.size());
//					     datatempflg=!datatempflg;
//					     idbdatatempstatus=true;
//						 if(GathersqlListManager.datatemplist2.size()>0)
//						 {
//							//Vector list=new Vector();
//							DBManager dbm=new DBManager();
//							
//						   Iterator it = GathersqlListManager.datatemplist2.keySet().iterator();   
//						  while (it.hasNext()){   
//						       String keys; 
//						       
//						       
//						       keys=(String)it.next();
//						       //System.out.println("======*****==key===="+keys);
//						       //list= GathersqlListManager.datatemplist.get(keys);
//						       if(null!=GathersqlListManager.datatemplist2.get(keys))
//						       {
//						    	   //logger.info(keys);
//						    	   dbm.addBatch(keys);
//						    	   for(int i=0;i<GathersqlListManager.datatemplist2.get(keys).size();i++)
//						    	   {
//						    		   //logger.info(GathersqlListManager.datatemplist2.get(keys).get(i).toString());
//						    		   dbm.addBatch((String)GathersqlListManager.datatemplist2.get(keys).get(i).toString());
//						    	   }
//						    	   
//						       }
//						       //GathersqlListManager.datatemplist.remove(keys);
//						       
//						   } 
//						  dbm.executeBatch();
//						  dbm.close();
//						  dbm=null;
//						  it=null;
//						 }
//						  GathersqlListManager.datatemplist2.clear();
//						  //System.out.println("=777777====��ʼ��ʱ����������=========="+GathersqlListManager.datatemplist.size());
//			
//						  idbdatatempstatus=false;
//				 
//				 
//				 
//			 }
//			
//			
//			
//		}
//			 
//		}else if(key.startsWith("delete") || key.startsWith("DELETE"))
//		 {
//			
//			
//			if(datatempflg)
//			{
//				
//			//System.out.println("==������ʱ����1");	
//			datatemplist.put(key, sql);	
//			}else
//			{
//			 //System.out.println("==������ʱ����2");
//			 datatemplist2.put(key, sql);	
//			}
//			
//		 }
//		
//	}

	

}
