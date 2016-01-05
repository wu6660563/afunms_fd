/**
 * <p>Description:polling resource</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-28
 */

package com.afunms.polling;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.base.BaseTask;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.Node;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.task.MonitorTimer;
import com.afunms.system.dao.SystemConfigDao;


public class PollingEngine
{
	private List nodeList;
	private List dbList;
	private List tomcatList;
	private List weblogicList;
	private List mqList;
	private List dominoList;
	private List wasList;
	private List cicsList;	
	private List linkList;	
	private List firewallList;
	private List webList;
	private List mailList;
	private List ftpList;
	private List iisList;
	private List socketList;
	private List busList;
	private List proList;
	private List intfceList;
	private List jbossList;
	private List dnsList;
	private List apacheList;
	//�㽭�ƶ�ר���豸
	private List ggsnList;
	private List sgsnList;
	private List panelList;
	private List xmlList;
	
	private List upslist;
	private List airlist;
	
	private PollingThread lastThread; //���һ���߳� 
	private int nodesTotal;  //��ǰ���һ���߳��еĽڵ���
	private int threadsTotal;  //�߳�����
	private ExecutorService threadExecutor;
	private String currentDate;
	private int runningDays;   //ϵͳ�������е�����
	private static String collectwebflag = "0";  //ϵͳ����ģʽ   0���ɼ�����ʼ���    1���ɼ�����ʷ���
	private static String treeshowflag = "0";    //��Դ��ʾģʽ   0:�������豸�ڵ�      1����ʾ���豸�ڵ�
	private static final int MAX_THREADS = ResourceCenter.getInstance().getMaxThreads(); 
	private static final int PER_THREAD_NODES = ResourceCenter.getInstance().getPerThreadNodes(); //һ���̹߳�������moid��	
    private static PollingEngine instance = new PollingEngine();
    private static List addiplist = new ArrayList();
    private HashMap<String, List> AllTypeMap;// yangjun add
    
    
    public static PollingEngine getInstance()
    {       
       return instance;
    }
    
	public String getCurrentDate()
    {
    	return currentDate;
    }
    
    public static List getAddiplist() {
		return addiplist;
	}

	public static void setAddiplist(List addiplist) {
		PollingEngine.addiplist = addiplist;
	}
    private PollingEngine()
    {
    	threadExecutor = Executors.newFixedThreadPool(MAX_THREADS);
    	setCurrentDate(SysUtil.getCurrentDate());
    	SystemConfigDao configdao = new SystemConfigDao();
    	try{
    		setCollectwebflag(configdao.getSystemCollectByVariablename("collectwebflag"));
    	}catch(Exception e){
    		
    	}finally{
    		configdao.close();
    	}
    	nodeList = new ArrayList();  
    	dbList = new ArrayList(); 
    	tomcatList = new ArrayList();
    	weblogicList = new ArrayList();
    	mqList = new ArrayList();
    	dominoList = new ArrayList();
    	wasList = new ArrayList();
    	cicsList = new ArrayList();
    	firewallList = new ArrayList();
    	webList = new ArrayList();
    	mailList = new ArrayList();
    	ftpList = new ArrayList();
    	iisList = new ArrayList();
    	socketList = new ArrayList();
    	busList = new ArrayList();
    	proList = new ArrayList();
    	intfceList = new ArrayList();
    	jbossList=new ArrayList();
    	dnsList=new ArrayList();
    	apacheList = new ArrayList();
    	ggsnList = new ArrayList();
    	sgsnList = new ArrayList();
    	airlist = new ArrayList();
    	upslist = new ArrayList();
    	AllTypeMap = new HashMap<String, List>();// yangjun add
    }
	
    public static String getCollectwebflag() {
		return collectwebflag;
	}

	public static void setCollectwebflag(String collectwebflag) {
		PollingEngine.collectwebflag = collectwebflag;
	}

	/**
     * �̼߳����̳߳ؿ�ʼ����     
     */
    private void addThread(Thread newThread)  
    {
    	threadExecutor.execute(newThread);    	
    }
        
    public synchronized void setCurrentDate(String cd)
    {
    	currentDate = cd;
    	runningDays++;
    	System.out.println("----------------ϵͳ���� �� " + runningDays + " ��----------------");    	
    }
    
    public static String getTreeshowflag() {
		return treeshowflag;
	}

	public static void setTreeshowflag(String treeshowflag) {
		PollingEngine.treeshowflag = treeshowflag;
	}

	public List getJBossList() 
	{
		return jbossList;	
	}
	public List getNodeList() 
	{
		return nodeList;	
	}
	public List getDbList() 
	{
		return dbList;	
	}
	public List getTomcatList() 
	{
		return tomcatList;	
	}
	public List getWeblogicList() 
	{
		return weblogicList;	
	}
	public List getMqList() 
	{
		return mqList;	
	}
	public List getDominoList() 
	{
		return dominoList;	
	}
	public List getWasList() 
	{
		return wasList;	
	}
	public List getCicsList() 
	{
		return cicsList;	
	}
	public List getFirewallList() 
	{
		return firewallList;	
	}
	public List getWebList() 
	{
		return webList;	
	}
	public List getMailList() 
	{
		return mailList;	
	}
	public List getFtpList() 
	{
		return ftpList;	
	}
	public List getIisList() 
	{
		return iisList;	
	}
	public List getSocketList() 
	{
		return socketList;	
	}
	public List getBusList() 
	{
		return busList;	
	}
	public List getProList() 
	{
		return proList;	
	}
	public List getIntfceList(){
		return intfceList;
	}
	public List getDnsList() 
	{
		return dnsList;	
	}
	public List getApacheList() 
	{
	  return apacheList;	
	  }
	public List getGgsnList() {
		return ggsnList;
	}

	public List getSgsnList() {
		return sgsnList;
	}
	public List getUpsList(){
		return upslist;
	}
	public List getairList(){
		return airlist;
	}
	public static List loaderList;
	public void mapRefresh(){ 
	    try
	    {        	
	    	if(PollingEngine.loaderList!=null)
	    	{ 
	            List list = PollingEngine.loaderList;
	            Iterator it = list.iterator();
	            while(it.hasNext())
	            {      
	            	try{
	            	    Element element = (Element)it.next();        	
	            	    NodeLoader loader = (NodeLoader)Class.forName(element.getChildText("class")).newInstance();
	            	    try{
	            	    	loader.loading();
	            	    }catch(Exception e){
	            	    	e.printStackTrace();
	            	    }finally{
	            	    	loader.close();
	            	    }  
	            	    loader.notify();
	            	    loader.getNmDao().close();
	            	    loader = null;
	            	}catch(Exception e){
	            		
	            	}
	            }
	    	}
	    	else{
	    		SysLogger.info("δ����/WEB-INF/classes/node-loader.xml"); 
	    	} 
	    }
        catch(Exception e)
	    {
    	    SysLogger.error("PollingEngine.doPolling2()",e);
	    }    	   
	}
	/**
	 * ��ʼ��ѯ
	 */
	public void doPolling()
	{	   	    		
   	    SAXBuilder builder = new SAXBuilder();
	    try
	    {        	
		    Document doc = builder.build(new File(ResourceCenter.getInstance().getSysPath() + "WEB-INF/classes/node-loader.xml")); 
            List list = doc.getRootElement().getChildren("loader");
            PollingEngine.loaderList=list;
            Iterator it = list.iterator();
            while(it.hasNext())
            {      
            	try{
            	    Element element = (Element)it.next();        	
            	    NodeLoader loader = (NodeLoader)Class.forName(element.getChildText("class")).newInstance();
            	    try{
            	    	loader.loading();
            	    }catch(Exception e){
            	    	e.printStackTrace();
            	    }finally{
            	    	loader.close();
            	    }  
            	    loader.notify();
            	    loader.getNmDao().close();
            	    loader = null;
            	}catch(Exception e){
            		
            	}
            }
            SysLogger.info("--------------���ؽڵ���ɣ�----------------");           
	    }
        catch(Exception e)
	    {
    	    SysLogger.error("PollingEngine.doPolling()",e);
	    }    	   
        SysLogger.info("--------------��ѯ��ʼ��----------------");
        SysLogger.info("--------------���� " + threadsTotal + " ����ѯ�߳�," + nodesTotal + " �������ӽڵ�----------------");
        DiskconfigDao diskdao = new DiskconfigDao();
        try{
        	ShareData.setAlldiskalarmdata(diskdao.getByAlarmflag(99));
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	diskdao.close();
        }
        
        //loadTasks();//���ﱻֹͣ��,�Ժ�����ʱ��Ҫ�޸�
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getNodeByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);
		
		int index = nodeList.indexOf(tmp);
		if(index!=-1)
		   return (Node)nodeList.get(index);
		else
		   return null;	
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getDbByID(int id)
	{
		Node node = null;
		if(dbList != null && dbList.size()>0){
			for(int k=0;k<dbList.size();k++){
				node = null;
				Node tnode = (Node)dbList.get(k);
				if(tnode != null){
					if(tnode.getId()==id){					
						node = tnode;
						break;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getWeblogicByID(int id)
	{
		Node node = null;
		if(weblogicList != null && weblogicList.size()>0){
			for(int k=0;k<weblogicList.size();k++){
				Node _node = (Node)weblogicList.get(k);
				if(_node != null){
					//SysLogger.info(_node.getId()+"============"+id);
					if(_node.getId()==id){
						
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getTomcatByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(tomcatList != null && tomcatList.size()>0){
			for(int k=0;k<tomcatList.size();k++){
				Node _node = (Node)tomcatList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;	
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getMqByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(mqList != null && mqList.size()>0){
			for(int k=0;k<mqList.size();k++){
				Node _node = (Node)mqList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getDominoByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(dominoList != null && dominoList.size()>0){
			for(int k=0;k<dominoList.size();k++){
				Node _node = (Node)dominoList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getWasByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(wasList != null && wasList.size()>0){
			for(int k=0;k<wasList.size();k++){
				Node _node = (Node)wasList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��ID����ĳ���ڵ�
	 */
	public Node getCicsByID(int id) {
		Node node = null;
		if (cicsList != null && cicsList.size() > 0) {
			for (int k = 0; k < cicsList.size(); k++) {
				Node _node = (Node) cicsList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getFirewallByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);
		
		int index = firewallList.indexOf(tmp);
		if(index!=-1)
		   return (Node)firewallList.get(index);
		else
		   return null;	
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getWebByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(webList != null && webList.size()>0){
			for(int k=0;k<webList.size();k++){
				Node _node = (Node)webList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;	
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getMailByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(mailList != null && mailList.size()>0){
			for(int k=0;k<mailList.size();k++){
				Node _node = (Node)mailList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;
	}
	public Node getBusByID(int id)
	{
		Node node = null;
		if(busList != null && busList.size()>0){
			for(int k=0;k<busList.size();k++){
				Node _node = (Node)busList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;	
	}
	public Node getProByID(int id)
	{
		Node node = null;
		if(proList != null && proList.size()>0){
			for(int k=0;k<proList.size();k++){
				Node _node = (Node)proList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;	
	}
	public Node getIntfaceByID(int id)
	{
		Node node = null;
		if(intfceList != null && intfceList.size()>0){
			for(int k=0;k<intfceList.size();k++){
				Node _node = (Node)intfceList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;	
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getFtpByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(ftpList != null && ftpList.size()>0){
			for(int k=0;k<ftpList.size();k++){
				Node _node = (Node)ftpList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getIisByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(iisList != null && iisList.size()>0){
			for(int k=0;k<iisList.size();k++){
				Node _node = (Node)iisList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getSocketByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(socketList != null && socketList.size()>0){
			for(int k=0;k<socketList.size();k++){
				Node _node = (Node)socketList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getNodeByIP(String ip)
	{
		Node node = null;
		if(nodeList != null && nodeList.size()>0){
			for(int k=0;k<nodeList.size();k++){
				Node _node = (Node)nodeList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	/**
	 * ��IP����ĳ���ڵ� 
	 */
	public Node getDbByIP(String ip)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(dbList != null && dbList.size()>0){
			for(int k=0;k<dbList.size();k++){
				Node _node = (Node)dbList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
						//return node;
					}
				}
			}
		}
		return node;
	}
	
	public Node getTomcatByIP(String ip)
	{
		Node node = null;
		if(tomcatList != null && tomcatList.size()>0){
			for(int k=0;k<tomcatList.size();k++){
				Node _node = (Node)tomcatList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getWeblogicByIP(String ip)
	{
		Node node = null;
		if(weblogicList != null && weblogicList.size()>0){
			for(int k=0;k<weblogicList.size();k++){
				Node _node = (Node)weblogicList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getMqByIP(String ip)
	{
		Node node = null;
		if(mqList != null && mqList.size()>0){
			for(int k=0;k<mqList.size();k++){
				Node _node = (Node)mqList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getDominoByIP(String ip)
	{
		Node node = null;
		if(dominoList != null && dominoList.size()>0){
			for(int k=0;k<dominoList.size();k++){
				Node _node = (Node)dominoList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getWasByIP(String ip)
	{
		Node node = null;
		if(wasList != null && wasList.size()>0){
			for(int k=0;k<wasList.size();k++){
				Node _node = (Node)wasList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getCicsByIP(String ip)
	{
		Node node = null;
		if(cicsList != null && cicsList.size()>0){
			for(int k=0;k<cicsList.size();k++){
				Node _node = (Node)cicsList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getFirewallByIP(String ip)
	{
		Node node = null;
		if(firewallList != null && firewallList.size()>0){
			for(int k=0;k<firewallList.size();k++){
				Node _node = (Node)firewallList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getWebByIP(String ip)
	{
		Node node = null;
		if(webList != null && webList.size()>0){
			for(int k=0;k<webList.size();k++){
				Node _node = (Node)webList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getMailByIP(String ip)
	{
		Node node = null;
		if(mailList != null && mailList.size()>0){
			for(int k=0;k<mailList.size();k++){
				Node _node = (Node)mailList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getFtpByIP(String ip)
	{
		Node node = null;
		if(ftpList != null && ftpList.size()>0){
			for(int k=0;k<ftpList.size();k++){
				Node _node = (Node)ftpList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getIisByIP(String ip)
	{
		Node node = null;
		if(iisList != null && iisList.size()>0){
			for(int k=0;k<iisList.size();k++){
				Node _node = (Node)iisList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public Node getSocketByIP(String ip)
	{
		Node node = null;
		if(socketList != null && socketList.size()>0){
			for(int k=0;k<socketList.size();k++){
				Node _node = (Node)socketList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}

	public LinkRoad getLinkByID(int id)
	{
		LinkRoad link = null; 
		for(int i=0;i<linkList.size();i++)
		{
		    LinkRoad temp = (LinkRoad)linkList.get(i);
		    if(temp.getId()==id)
		    {
		        link = temp;
		        break;
		    }
		}
		return link;	
	}

	public void deleteLinkByID(int id)
	{
		int start = linkList.size() - 1;
		for(int i=start;i>=0;i--)
		{
		    LinkRoad temp = (LinkRoad)linkList.get(i);
		    if(temp.getId()==id)
		    {
		        linkList.remove(temp);
		        break;
		    }
		}		
	}
	
	/**
	 * ��IDɾ��ĳ���ڵ� 
	 */
	public void deleteNodeByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = nodeList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)nodeList.get(index)).setManaged(false);
		    nodeList.remove(index);
		}
		refreshAllTypeMap("net_wireless",nodeList);// yangjun add
		refreshAllTypeMap("net_switch_router",nodeList);// yangjun add
		refreshAllTypeMap("net_server",nodeList);// yangjun add
		refreshAllTypeMap("net_router",nodeList);// yangjun add
		refreshAllTypeMap("net_switch",nodeList);// yangjun add
		refreshAllTypeMap("net_firewall",nodeList);// yangjun add
		refreshAllTypeMap("atm",nodeList);// hukelei add
	}
	
	/**
	 * ��IDɾ��ĳ���ڵ� 
	 */
	public void deleteDbByID(int id)
	{
		Node node = null;
		if (dbList != null && dbList.size() > 0) {
			for (int k = 0; k < dbList.size(); k++) {
				Node _node = (Node) dbList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						node = _node;
						node.setManaged(false);
						dbList.remove(node);
						break;
					}
				}
			}
		}
		refreshAllTypeMap("dbs",dbList);// yangjun add
	}
	
	public void deleteTomcatByID(int id)
	{
		Node node = null;
		if (tomcatList != null && tomcatList.size() > 0) {
			for (int k = 0; k < tomcatList.size(); k++) {
				Node _node = (Node) tomcatList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						node = _node;
						node.setManaged(false);
						tomcatList.remove(node);
						break;
					}
				}
			}
		}
		refreshAllTypeMap("tomcat",tomcatList);// yangjun add
	}
	public void deleteFtpByID(int id)
	{
		if (ftpList != null && ftpList.size() > 0) {
			for (int k = 0; k < ftpList.size(); k++) {
				Node _node = (Node) ftpList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)ftpList.get(index)).setManaged(false);
						    ftpList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("ftp",ftpList);// yangjun add
	}
	public void deleteIisByID(int id)
	{
		if (iisList != null && iisList.size() > 0) {
			for (int k = 0; k < iisList.size(); k++) {
				Node _node = (Node) iisList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)iisList.get(index)).setManaged(false);
						    iisList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("iis",iisList);// yangjun add
	}
	public void deleteSocketByID(int id)
	{
		if (socketList != null && socketList.size() > 0) {
			for (int k = 0; k < socketList.size(); k++) {
				Node _node = (Node) socketList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)socketList.get(index)).setManaged(false);
						    socketList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("socket",socketList);// yangjun add
	}
	public void deleteMailByID(int id)
	{
		if (mailList != null && mailList.size() > 0) {
			for (int k = 0; k < mailList.size(); k++) {
				Node _node = (Node) mailList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)mailList.get(index)).setManaged(false);
						    mailList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("mail",mailList);// yangjun add
	}
	public void deleteWebByID(int id)
	{
		if (webList != null && webList.size() > 0) {
			for (int k = 0; k < webList.size(); k++) {
				Node _node = (Node) webList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)webList.get(index)).setManaged(false);
						    webList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("wes",webList);// yangjun add
	}
	public void deleteMqByID(int id)
	{
		if (mqList != null && mqList.size() > 0) {
			for (int k = 0; k < mqList.size(); k++) {
				Node _node = (Node) mqList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)mqList.get(index)).setManaged(false);
						    mqList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("mqs",mqList);// yangjun add
	}
	
	public void deleteDominoByID(int id)
	{
		if (dominoList != null && dominoList.size() > 0) {
			for (int k = 0; k < dominoList.size(); k++) {
				Node _node = (Node) dominoList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)dominoList.get(index)).setManaged(false);
						    dominoList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("domino",dominoList);// yangjun add
	}
	public void deleteWeblogicByID(int id) {
		if (weblogicList != null && weblogicList.size() > 0) {
			for (int k = 0; k < weblogicList.size(); k++) {
				Node _node = (Node) weblogicList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)weblogicList.get(index)).setManaged(false);
						    weblogicList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("weblogic",weblogicList);// yangjun add
	}
	public void deleteWasByID(int id) {
		if (wasList != null && wasList.size() > 0) {
			for (int k = 0; k < wasList.size(); k++) {
				Node _node = (Node) wasList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)wasList.get(index)).setManaged(false);
						    wasList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("was",wasList);// yangjun add
	}
	public void deleteCicsByID(int id) {
		if (cicsList != null && cicsList.size() > 0) {
			for (int k = 0; k < cicsList.size(); k++) {
				Node _node = (Node) cicsList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)cicsList.get(index)).setManaged(false);
						    cicsList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("cics",cicsList);// yangjun add
	}
	public void deleteFirewallListByID(int id) {
		Node node = null;
		if (firewallList != null && firewallList.size() > 0) {
			for (int k = 0; k < firewallList.size(); k++) {
				Node _node = (Node) firewallList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						node = _node;
						node.setManaged(false);
						firewallList.remove(node);
						break;
					}
				}
			}
		}
		refreshAllTypeMap("firewallList",firewallList);// yangjun add
	}
	public void deleteBusByID(int id)
	{
		if (busList != null && busList.size() > 0) {
			for (int k = 0; k < busList.size(); k++) {
				Node _node = (Node) busList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)busList.get(index)).setManaged(false);
						    busList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("bussiness",busList);// yangjun add
	}
	public void deleteProByID(int id)
	{
		if (proList != null && proList.size() > 0) {
			for (int k = 0; k < proList.size(); k++) {
				Node _node = (Node) proList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)proList.get(index)).setManaged(false);
						    proList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("process",proList);// yangjun add
	}
	public void deleteIntfaceByID(int id)
	{
		if (intfceList != null && intfceList.size() > 0) {
			for (int k = 0; k < intfceList.size(); k++) {
				Node _node = (Node) intfceList.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						int index = k;
						if(index!=-1)
						{
						    ((Node)intfceList.get(index)).setManaged(false);
						    intfceList.remove(index);
						}
						break;
					}
				}
			}
		}
		refreshAllTypeMap("interface",intfceList);// yangjun add
	}
	
	/**
	 * ������ͳ�ƽڵ�ĸ��� 
	 */
	public int getExchangeTotal()
	{
		int total = 0;		
		for(int i=0;i<nodeList.size();i++)
		{
		    if(((Node)nodeList.get(i)).getCategory()<4)
		       total ++;
		}   
		return total;
	}	

	/**
	 * ������ͳ�ƽڵ�ĸ��� 
	 */
	public int getNodesTotal(int category)
	{
		int total = 0;
		for(int i=0;i<nodeList.size();i++)
		{
		    if(((Node)nodeList.get(i)).getCategory()==category)
		       total ++;
		}   
		return total;
	}	
	
	/**
	 * �ѽڵ������ѯ�߳�
	 */
	public void addNode(Node node)
	{	
		
//		if(nodesTotal % PER_THREAD_NODES==0)
//		{	
//			lastThread = new PollingThread();
//			addThread(lastThread);
//			threadsTotal++;
//		} 
//		//SysLogger.info("poll ip : "+node.getIpAddress());		
//		lastThread.getNodeList().add(node);	
		nodeList.add(node);	
		nodesTotal++;
		refreshAllTypeMap("net_wireless",nodeList);// yangjun add
		refreshAllTypeMap("net_switch_router",nodeList);// yangjun add
		refreshAllTypeMap("net_server",nodeList);// yangjun add
		refreshAllTypeMap("net_router",nodeList);// yangjun add
		refreshAllTypeMap("net_switch",nodeList);// yangjun add
		refreshAllTypeMap("net_firewall",nodeList);// yangjun add
		refreshAllTypeMap("net_atm",nodeList);// hukelei add
		
	}
	
	public void addTomcat(Node node)
	{	
		tomcatList.add(node);	
		refreshAllTypeMap("tomcat",tomcatList);// yangjun add
	}
	
	public void addDb(Node node)
	{	
		dbList.add(node);	
		refreshAllTypeMap("dbs",dbList);// yangjun add
	}
	public void addWeblogic(Node node)
	{	
		weblogicList.add(node);	
		refreshAllTypeMap("weblogic",weblogicList);// yangjun add
	}
	public void addMq(Node node)
	{	
		mqList.add(node);	
		refreshAllTypeMap("mqs",mqList);// yangjun add
	}
	public void addWas(Node node)
	{	
		wasList.add(node);	
		refreshAllTypeMap("was",wasList);// yangjun add
	}
	public void addCics(Node node)
	{	
		cicsList.add(node);	
		refreshAllTypeMap("cics",cicsList);// yangjun add
	}
	public void addDomino(Node node)
	{	
		dominoList.add(node);	
		refreshAllTypeMap("domino",dominoList);// yangjun add
	}
	public void addFirewall(Node node)
	{	
		firewallList.add(node);	
		refreshAllTypeMap("firewall",firewallList);// yangjun add
	}
	public void addWeb(Node node)
	{	
		webList.add(node);	
		refreshAllTypeMap("wes",webList);// yangjun add
	}
	public void addMail(Node node)
	{	
		mailList.add(node);
		refreshAllTypeMap("mail",mailList);// yangjun add
	}
	public void addFtp(Node node)
	{	
		ftpList.add(node);	
		refreshAllTypeMap("ftp",ftpList);// yangjun add
	}
	public void addIis(Node node)
	{	
		iisList.add(node);	
		refreshAllTypeMap("iis",iisList);// yangjun add
	}
	public void addSocket(Node node)
	{	
		socketList.add(node);	
		refreshAllTypeMap("socket",socketList);// yangjun add
	}
	public void addBus(Node node)
	{	
		busList.add(node);	
		refreshAllTypeMap("bussiness",busList);// yangjun add
	}
	public void addPro(Node node)
	{	
		proList.add(node);	
		refreshAllTypeMap("process",proList);// yangjun add
	}
	public void addIntface(Node node)
	{	
		intfceList.add(node);	
		refreshAllTypeMap("interface",intfceList);// yangjun add
	}
	public List getLinkList() {
		return linkList;
	}

	public void setLinkList(List linkList) {
		this.linkList = linkList;
	}
	
	public List getPanelList() {
		return panelList;
	}

	public void setPanelList(List panelList) {
		this.panelList = panelList;
	}
	
	public List getXmlList() {
		return xmlList;
	}

	public void setXmlList(List xmlList) {
		this.xmlList = xmlList;
	}
	
    private void loadTasks()
    {
    	SysLogger.info("����UPDATEXML....................");
    	ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
    	SAXBuilder builder = new SAXBuilder();
        try
		{        	
            Document doc = builder.build(new File(ResourceCenter.getInstance().getSysPath() + "WEB-INF/classes/task.xml"));
            List list = doc.getRootElement().getChildren("task");
            Iterator it = list.iterator();
            while(it.hasNext())
            {            	
            	Element element = (Element)it.next();
            	if(element.getChildText("run").equals("false")) continue;
            	
            	BaseTask task = (BaseTask)Class.forName(element.getChild("class").getText()).newInstance();
            	task.setDescr(element.getChildText("descr"));
            	service.scheduleAtFixedRate(task,            	
            	Integer.parseInt(element.getChildText("delay"))*60,
            	Integer.parseInt(element.getChildText("interval"))*60,
            	TimeUnit.SECONDS);
            }    
		}
        catch(Exception e)
		{
        	SysLogger.error("SysInitializtion.loadTasks()",e);
		}    	   
    } 
    
    /**
     * <p>Task��δ���</p>
     * <p>�������ͺ�ID�����ݿ��в��Ҹ�node</p>
     * @author HONGLI
     * @param category
     * @param id
     * @return
     */
    public Node getNodeListByCategory(String category, int id){
    	Node node = null;
    	List retList = null;
    	if("node".equals(category)){
    		retList = nodeList;
    		//��Ӳɼ�node��Ϣ�ķ���
    		
    	}else if("tomcat".equals(category)){
    		retList = tomcatList;
    	}else if("dbs".equals(category)){
    		retList = dbList;
    	}else if("weblogic".equals(category)){
    		retList = weblogicList;
    	}else if("mqs".equals(category)){
    		retList = mqList;
    	}else if("was".equals(category)){
    		retList = wasList;
    	}else if("cics".equals(category)){
    		retList = cicsList;
    	}else if("domino".equals(category)){
    		retList = dominoList;
    	}else if("firewall".equals(category)){
    		retList = firewallList;
    	}else if("wes".equals(category)){
    		retList = webList;
    	}else if("mail".equals(category)){
    		retList = mailList;
    	}else if("ftp".equals(category)){
    		retList = ftpList;
    	}else if("iis".equals(category)){
    		retList = iisList;
    	}else if("socket".equals(category)){
    		retList = socketList;
    	}else if("bussiness".equals(category)){
    		retList = busList;
    	}else if("process".equals(category)){
    		retList = proList;
    	}else if("interface".equals(category)){
    		retList = intfceList;
    	}
    	//�����ڴ�
		refreshAllTypeMap(category,retList);
    	return node;
    }
    
    public HashMap<String, List> getAllTypeMap() {// yangjun add
		return AllTypeMap;
	}

	public void addToAllTypeMap(String key, List list) {// yangjun add
		AllTypeMap.put(key, list);
	}

	// ͨ�����ͺ�id�����豸
	public Node getNodeByCategory(String category, int id) {// yangjun add
		List list = AllTypeMap.get(category);
		Node node = null;
		if (list != null && list.size() > 0) {
			for (int k = 0; k < list.size(); k++) {
				Node _node = (Node) list.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}

	// ͨ�����ͺ�idɾ���豸yangjun add
	public void deleteNodeByCategory(String category, int id) {
		List list = AllTypeMap.get(category);
		if (list != null && list.size() > 0) {
			for (int k = 0; k < list.size(); k++) {
				Node _node = (Node) list.get(k);
				if (_node != null) {
					if (_node.getId() == id) {
						list.remove(_node);
						break;
					}
				}
			}
		}
		refreshAllTypeMap(category,list);
	}
	
	//����map
	public void refreshAllTypeMap(String category,List list){
		if (AllTypeMap.containsKey(category)) {
			AllTypeMap.remove(category);
		}
		//SysLogger.info(category+"====="+category+"  list:"+list.size());
		addToAllTypeMap(category, list);// yangjun add
	}
	
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getJBossByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(jbossList != null && jbossList.size()>0){
			for(int k=0;k<jbossList.size();k++){
				Node _node = (Node)jbossList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getJBossByIP(String ip)
	{
		Node node = null;
		if(jbossList != null && jbossList.size()>0){
			for(int k=0;k<jbossList.size();k++){
				Node _node = (Node)jbossList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public void deleteJBossByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = jbossList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)jbossList.get(index)).setManaged(false);
		    jbossList.remove(index);
		}
		refreshAllTypeMap("jboss",jbossList);
	}
	
	public void addJBoss(Node node)
	{	
		jbossList.add(node);	
		refreshAllTypeMap("jboss",jbossList);
	}
	
	/**
	 * ��ID����ĳ���ڵ�  by Gaoguangfei
	 */
	public Node getDnsByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(dnsList != null && dnsList.size()>0){
			for(int k=0;k<dnsList.size();k++){
				Node _node = (Node)dnsList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getDnsByIP(String ip)
	{
		Node node = null;
		if(dnsList != null && dnsList.size()>0){
			for(int k=0;k<dnsList.size();k++){
				Node _node = (Node)dnsList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public void deleteDnsByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = dnsList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)dnsList.get(index)).setManaged(false);
		    dnsList.remove(index);
		}
		refreshAllTypeMap("dns",dnsList);
	}
	
	public void addDns(Node node)
	{	
		dnsList.add(node);	
		refreshAllTypeMap("dns",dnsList);
	}
	/**
	 * ��ID����ĳ���ڵ� 
	 */
	public Node getApacheByID(int id)
	{
		Node node = null;
		//tmp.setIpAddress(ip);
		if(apacheList != null && apacheList.size()>0){
			for(int k=0;k<apacheList.size();k++){
				Node _node = (Node)apacheList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getApacheByIP(String ip)
	{
		Node node = null;
		if(apacheList != null && apacheList.size()>0){
			for(int k=0;k<apacheList.size();k++){
				Node _node = (Node)apacheList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public void deleteApacheByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = apacheList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)apacheList.get(index)).setManaged(false);
		    apacheList.remove(index);
		}
		refreshAllTypeMap("apache",apacheList);
	}
	
	public void addApache(Node node)
	{	
		apacheList.add(node);	
		refreshAllTypeMap("apache",apacheList);
	}
	//end by gaoguanfei
	public Node getSgsnByID(int id)
	{
		Node node = null;
		if(sgsnList != null && sgsnList.size()>0){
			for(int k=0;k<sgsnList.size();k++){
				Node _node = (Node)sgsnList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getSgsnByIP(String ip)
	{
		Node node = null;
		if(sgsnList != null && sgsnList.size()>0){
			for(int k=0;k<sgsnList.size();k++){
				Node _node = (Node)sgsnList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public void deleteSgsnByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = sgsnList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)sgsnList.get(index)).setManaged(false);
		    sgsnList.remove(index);
		}
		refreshAllTypeMap("sgsn",sgsnList);
	}
	
	public void addSgsn(Node node)
	{	
		sgsnList.add(node);	
		refreshAllTypeMap("sgsn",sgsnList);
	}
	public Node getGgsnByID(int id)
	{
		Node node = null;
		if(ggsnList != null && ggsnList.size()>0){
			for(int k=0;k<ggsnList.size();k++){
				Node _node = (Node)ggsnList.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public void addUps(Node node)
	{	
		upslist.add(node);	
		refreshAllTypeMap("ups",upslist);
	}
	public Node getUpsByID(int id)
	{
		Node node = null;
		if(upslist != null && upslist.size()>0){
			for(int k=0;k<upslist.size();k++){
				Node _node = (Node)upslist.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getUpsByIP(String ip)
	{
		Node node = null;
		if(upslist != null && upslist.size()>0){
			for(int k=0;k<upslist.size();k++){
				Node _node = (Node)upslist.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public void addAir(Node node)
	{	
		airlist.add(node);	
		refreshAllTypeMap("air",airlist);
	}
	public Node getAirByID(int id)
	{
		Node node = null;
		if(airlist != null && airlist.size()>0){
			for(int k=0;k<airlist.size();k++){
				Node _node = (Node)airlist.get(k);
				if(_node != null){
					if(_node.getId()==id){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public Node getAirByIP(String ip)
	{
		Node node = null;
		if(airlist != null && airlist.size()>0){
			for(int k=0;k<airlist.size();k++){
				Node _node = (Node)airlist.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	public void deleteAirByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = airlist.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)airlist.get(index)).setManaged(false);
		    airlist.remove(index);
		}
		refreshAllTypeMap("air",airlist);
	}
	
	public void deleteUpsByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = upslist.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)upslist.get(index)).setManaged(false);
		    upslist.remove(index);
		}
		refreshAllTypeMap("ups",upslist);
	}
	
	public Node getGgsnByIP(String ip)
	{
		Node node = null;
		if(ggsnList != null && ggsnList.size()>0){
			for(int k=0;k<ggsnList.size();k++){
				Node _node = (Node)ggsnList.get(k);
				if(_node != null){
					if(_node.getIpAddress().equalsIgnoreCase(ip)){
						node = _node;
						break;
					}
				}
			}
		}
		return node;
	}
	
	public void deleteGgsnByID(int id)
	{
		Node tmp = new Node();
		tmp.setId(id);		
		int index = ggsnList.indexOf(tmp);
		if(index!=-1)
		{
		    ((Node)ggsnList.get(index)).setManaged(false);
		    ggsnList.remove(index);
		}
		refreshAllTypeMap("ggsn",ggsnList);
	}
	
	public void addGgsn(Node node)
	{	
		ggsnList.add(node);	
		refreshAllTypeMap("ggsn",ggsnList);
	}
}