package com.afunms.data.service;

import java.util.Iterator;
import java.util.List;


import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;

public class NodeDataService {

    /**
     * 日志
     */
    private static final SysLogger sysLogger = SysLogger.getLogger(NodeDataService.class.getName());

	protected NodeDTO nodeDTO;

	protected BaseVo baseVo;
	/**
	 * 默认的构造方法，是该方法构造后，必须要使用 {@link #init(String, String, String)} 来初始化
	 */
	public NodeDataService() {
	    
	}

    public NodeDataService(BaseVo baseVo) {
        init(baseVo);
    }

    public NodeDataService(NodeDTO nodeDTO) {
        init(nodeDTO);
    }

    public NodeDataService(String nodeid, String type, String subtype) {
	    init(nodeid, type, subtype);
	}

	public void init(String nodeid, String type, String subtype){
	    NodeUtil nodeUtil = new NodeUtil();
	    List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(type, subtype);
        List<NodeDTO> nodeDTOList = nodeUtil.conversionToNodeDTO(list);
        Iterator<NodeDTO> iterator = nodeDTOList.iterator();
        NodeDTO nodeDTO = null;
        while (iterator.hasNext()) {
            NodeDTO elem = (NodeDTO) iterator.next();
            if (String.valueOf(elem.getId()).equals(nodeid)) {
                nodeDTO = elem;
                break;
            }
        }
        init(nodeDTO);
	}

	public void init(BaseVo baseVo) {
	    if (baseVo == null) {
	        return;
	    }
	    if (baseVo instanceof NodeDTO) {
	        NodeDTO nodeDTO = (NodeDTO) baseVo;
	        setNodeDTO(nodeDTO);
	    } else {
	        setBaseVo(baseVo);
	        NodeUtil nodeUtil = new NodeUtil();
	        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(baseVo);
	        init(nodeDTO);
	    }
	}

	/**
	 * 获取 nodedDTO
     * @return the nodeDTO
     */
    public NodeDTO getNodeDTO() {
        return nodeDTO;
    }

    /**
     * @param nodeDTO the nodeDTO to set
     */
    public void setNodeDTO(NodeDTO nodeDTO) {
        this.nodeDTO = nodeDTO;
    }

    public BaseVo getBaseVo() {
        return baseVo;
    }

    public void setBaseVo(BaseVo baseVo) {
        this.baseVo = baseVo;
    }

    public String getNodeid() {
        return getNodeDTO().getNodeid();
    }

    public String getType() {
        return getNodeDTO().getType();
    }

    public String getSubtype() {
        return getNodeDTO().getSubtype();
    }

    public String getIpaddress() {
        return getNodeDTO().getIpaddress();
    }
}
