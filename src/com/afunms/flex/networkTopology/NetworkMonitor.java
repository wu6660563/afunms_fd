/*
 * Facade class used to manage the network monitor demo app.
 */

package com.afunms.flex.networkTopology;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import org.xml.sax.SAXException;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SessionConstant;
import com.afunms.flex.networkTopology.FeedThread;
import com.afunms.flex.networkTopology.vo.Device;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.system.model.User;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.model.TreeNode;

import flex.messaging.FlexContext;

public class NetworkMonitor {
	private static FeedThread _thread;
	private static DataSource _dataSource;
	private static Properties props      = new Properties();

	public Device[] getDevices(String xml) {
//		ServletContext context = FlexContext.getServletContext();
//		String path = context.getRealPath("/flex/data/init.properties");
//		String xml=readValue(path,"map_a");
		return getDataSource(xml).getDevices();
	}

	private DataSource getDataSource(String xml) {
		_dataSource = new DataSource(xml);
		return _dataSource;
	}

//	 读取properties信息
    public String readValue(String filePath, String key) {
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            String s = new String(key.getBytes("UTF-8"), "ISO-8859-1");
            String value = props.getProperty(s);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
	public Device getDeviceDetail(String id) {
		return _dataSource.getDeviceDetail(id);
	}

//	public boolean startFeed() {
//		if (_thread == null) {
//			_thread = new FeedThread();
//			DataSource dataSource = getDataSource();
//			_thread.setDataSource(dataSource);
//			_thread.running = true;
//			_thread.start();
//		}
//
//		return getFeedStatus();
//	}

	public boolean stopFeed() {
		if (_thread != null) {
			_thread.running = false;
			_thread = null;
		}

		return getFeedStatus();
	}

	public boolean getFeedStatus() {
		if (_thread == null)
			return false;

		return _thread.running;
	}
	
	/**
	 *  得到业务视图列表 
	 * @return
	 */
	public Hashtable getBussinessviewHash(){
		Hashtable bussinessviewHash = new Hashtable();
//		String viewId = request.getParameter("viewId");
//		ManageXml manageXml = null;
		List bussinessview = null;
		ManageXmlDao manageXmlDao = new ManageXmlDao();
		try {
			bussinessview = (ArrayList)manageXmlDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manageXmlDao.close();
		}
//		if(manageXml == null){
//			return list();
//		}
		
		for(int k=0; k<bussinessview.size(); k++){
			ManageXml manageXml = (ManageXml)bussinessview.get(k);
			if(manageXml.getTopoType() != 1){
				continue;
			}
			//为业务视图的情况
			NodeDependDao nodeDependDao = new NodeDependDao(); 
			List<NodeDepend> list = null;
			try {
				list = nodeDependDao.findByXml(manageXml.getXmlName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				nodeDependDao.close();
			}
			
			List<NodeDTO> nodeDTOList = new ArrayList<NodeDTO>();
			List<Node> nodeList = new ArrayList<Node>();
			Hashtable<Node, String> nodeTagHash = new Hashtable<Node, String>();
			Hashtable<Node, TreeNode> treeNodeHash = new Hashtable<Node, TreeNode>();
			if(list!=null ){
				for(int i = 0 ; i < list.size(); i++){//为业务视图的情况
					NodeDepend nodeDepend = list.get(i);
					String nodeId = nodeDepend.getNodeId();
					String nodeTag = nodeId.substring(0, 3);
					String node_id = nodeId.substring(3);
					
					TreeNodeDao treeNodeDao = new TreeNodeDao();
					TreeNode vo = null;
					try {
						vo = (TreeNode) treeNodeDao.findByNodeTag(nodeTag);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						treeNodeDao.close();
					}
					
					// 此处为 从数据库表里获取数据 暂未使用 但功能已完成
					NodeUtil nodeUtil = new NodeUtil();
					List<BaseVo> baseVolist = nodeUtil.getByNodeTag(nodeTag, vo.getCategory());
					List<NodeDTO> AllNodeDTOList = nodeUtil.conversionToNodeDTO(baseVolist);
					if(AllNodeDTOList != null){
						for (NodeDTO nodeDTO : AllNodeDTOList) {
							if(nodeDTO.getNodeid().equalsIgnoreCase(node_id)){
								nodeDTOList.add(nodeDTO);
							}
						}
					}
				}
			}
			bussinessviewHash.put(manageXml, nodeDTOList);
		}
		return bussinessviewHash;
	}
	
	public List<NodeDTO> getBussinessviewNode(ManageXml manageXml){
	    List<NodeDTO> nodeDTOList = new ArrayList<NodeDTO>();
        if(manageXml == null || manageXml.getTopoType() != 1){
            return nodeDTOList;
        }
        //为业务视图的情况
        NodeDependDao nodeDependDao = new NodeDependDao(); 
        List<NodeDepend> list = null;
        try {
            list = nodeDependDao.findByXml(manageXml.getXmlName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeDependDao.close();
        }
            
        if(list!=null ){
            for(int i = 0 ; i < list.size(); i++){//为业务视图的情况
                NodeDepend nodeDepend = list.get(i);
                String nodeId = nodeDepend.getNodeId();
                String nodeTag = nodeId.substring(0, 3);
                String node_id = nodeId.substring(3);
                
                TreeNodeDao treeNodeDao = new TreeNodeDao();
                TreeNode vo = null;
                try {
                    vo = (TreeNode) treeNodeDao.findByNodeTag(nodeTag);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    treeNodeDao.close();
                }
                NodeUtil nodeUtil = new NodeUtil();
                List<BaseVo> baseVolist = nodeUtil.getByNodeTag(nodeTag, null);
                List<NodeDTO> AllNodeDTOList = nodeUtil.conversionToNodeDTO(baseVolist);
                if(AllNodeDTOList != null){
                    for (NodeDTO nodeDTO : AllNodeDTOList) {
                        if(nodeDTO.getNodeid().equalsIgnoreCase(node_id)){
                            nodeDTOList.add(nodeDTO);
                        }
                    }
                }
            }
        }
        return nodeDTOList;
    }

	public List<NodeDTO> getBussinessviewNode(NodeDTO nodeDTO){
	    ManageXml manageXml = null;
	    ManageXmlDao manageXmlDao = new ManageXmlDao();
        try {
            manageXml = (ManageXml) manageXmlDao.findByID(nodeDTO.getNodeid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manageXmlDao.close();
        }
        return getBussinessviewNode(manageXml);
	}
	/**
	 * 根据用户获取业务视图
	 * @param user
	 * @return
	 */
	public Hashtable<ManageXml, List<NodeDTO>> getBussinessviewHash(User user){
	    Hashtable<ManageXml, List<NodeDTO>> manageXmlHashtable = new Hashtable<ManageXml, List<NodeDTO>>();
	    if (user == null) {
	        return manageXmlHashtable;
	    }
        String bid = user.getBusinessids();
        String[] bids = null;
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        } else if (bid == null || bid.trim().length() == 0) {
            result = false;
        } else {
            bids = bid.trim().split(",");
            if (bids == null || bids.length == 0) {
                result = false;
            } else {
                result = true;
            }
        }
        int topotype = 1;   // 业务视图
        List<ManageXml> list = null;
        if (result) {
            ManageXmlDao manageXmlDao = new ManageXmlDao();
            try {
                list = (List<ManageXml>) manageXmlDao.findByTopoTypeAndBid(topotype, bids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                manageXmlDao.close();
            }
        }
        if (list == null || list.size() == 0) {
            return manageXmlHashtable;
        }
        for (ManageXml manageXml : list) {
            manageXmlHashtable.put(manageXml, getBussinessviewNode(manageXml));
        }
        return manageXmlHashtable;
    }
}