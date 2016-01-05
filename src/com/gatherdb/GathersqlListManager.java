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
 * 采集数据sql内存数据管理对象列表与方法
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
	 * 处理 SQL 语句的线程池
	 */
	private ThreadPool threadPool = new ThreadPool();

	/**
	 * 单例模式
	 */
	private static GathersqlListManager instance = null;

	/**
     * 所属线程
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
                // 等待时间间隔 进行睡眠
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
//			//判断是否需要序列化(一区推至三区，一区需要序列化，不需要启动反序列化的Task，三区不需要序列化，但需要启动反序列化的Task)
//			//通过initialize-config里面配置文件来配置解决
//			boolean isSerial = ResourceCenter.getInstance().isSerialObject();
//			if(isSerial){
//				String className = resultToDB.getResultTosql().getClass().getName();
//				DBAttribute attribute = resultToDB.getAttribute();
//				attribute.setAttribute("className", className);
//				Date date = new Date();
//				SerialObejctUtil.serialObject(attribute,sdf.format(date)+"_"+UUID.randomUUID()+".obj");	//序列化
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
//	 * 把sql放入到内存队列，如果传递参数为DHCC-DB 表示数据入口
//	 * @param sql 字符参数有2个方式，一个是sql，一个是表示是入口（DHCC-DB）
//	 */
//	public static void Addsql(String sql) {
//		if(sql.equals("DHCC-DB")) {
//		 if(qflg==true)
//		 {
//		 //System.out.println("=====入库队列1start=======");
//	     //System.out.println("=**=入库队列1=="+GathersqlListManager.queue.size());	
//		 if(GathersqlListManager.queue.size()>1)
//		 {
//	     qflg=!qflg;
//	     idbstatus=true;
//		 DBManager pollmg = new DBManager();// 数据库管理对象
//		 pollmg.excuteBatchSql(GathersqlListManager.queue);
//		 pollmg.close();
//		 pollmg=null;
//		 idbstatus=false;
//		 //System.out.println("=====入库队列1end=======");
//		 }
//		
//		 
//		 
//		 }else if(qflg==false)
//		 {
//			 //System.out.println("=====入库队列2start=======");
//			 if(GathersqlListManager.queue2.size()>1)
//			 {
//			 //System.out.println("=**=入库队列2=="+GathersqlListManager.queue2.size());	
//			 idbstatus=true;
//			 qflg=!qflg;
//			 DBManager pollmg = new DBManager();// 数据库管理对象
//			 pollmg.excuteBatchSql(GathersqlListManager.queue2);
//			 pollmg.close();
//			 pollmg=null;
//			 
//			 idbstatus=false;
//			 
//			 }
//			 //System.out.println("=====入库队列2end=======");
//			 
//		 }
//		 
//		}else
//		 {
//		    if(qflg)
//            {
//                
//                synchronized (queue){
//            //System.out.println("==放入队列1");    
//            queue.offer(sql);
//                }
//            }else
//            {
//                synchronized (queue2){
//             //System.out.println("==放入队列2");
//             queue2.offer(sql);
//              }
//            }
//		 }
//	}
//	
//	
//	
//	/**
//	 * 数据分离模式sql入口
//	 * 分为两种情况，当key为 DHCC-DB 表示调用数据入库接口，当key值为sql的删除语句时
//	 * 表示把结果生成sql放入到内存列表中,这样做的目的是为了保证线程的安全和数据的完整性
//	 * @param sql
//	 */
//	public  static void AdddateTempsql(String key,Vector sql)
//	{
//		if(key.equals("DHCC-DB"))
//		{
//			
//			
//			 //System.out.println("===临时入库队列="+datatempflg);
//			
//			 if(datatempflg==true)
//			 {
//			 if(GathersqlListManager.datatemplist.size()>0)
//			 {
//				 datatempflg=!datatempflg;
//				 idbdatatempstatus=true;
//				//System.out.println("=========================99999999====开始临时数据入库=========="+GathersqlListManager.datatemplist.size());
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
//				  //System.out.println("=999999999====开始临时数据入库结束=========="+GathersqlListManager.datatemplist.size());
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
//						//System.out.println("=========================777777====开始临时数据入库=========="+GathersqlListManager.datatemplist.size());
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
//						  //System.out.println("=777777====开始临时数据入库结束=========="+GathersqlListManager.datatemplist.size());
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
//			//System.out.println("==放入临时队列1");	
//			datatemplist.put(key, sql);	
//			}else
//			{
//			 //System.out.println("==放入临时队列2");
//			 datatemplist2.put(key, sql);	
//			}
//			
//		 }
//		
//	}

	

}
