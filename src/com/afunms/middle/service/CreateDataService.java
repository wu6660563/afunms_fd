/*
 * @(#)CreateDataService.java     v1.01, Oct 23, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.List;

import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.indicators.model.NodeDTO;

/**
 * ClassName: CreateDataService.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Oct 23, 2013 3:38:06 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class CreateDataService {

	/**
	 * getNetworkPortByNode:
	 * <p>
	 *
	 * @param dtovo
	 * @return
	 *
	 * @since   v1.01
	 */
	public List<InterfaceInfo> getNetworkPortByNode(NodeDTO dtovo) {
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(
				dtovo.getNodeid(), dtovo.getType(), dtovo.getSubtype());
		List<InterfaceInfo> interfaceList = interfaceInfoService.getCurrAllInterfaceInfos(null);
		return interfaceList;
	}

}
