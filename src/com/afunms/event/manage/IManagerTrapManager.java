package com.afunms.event.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.IManagerTrapDao;

public class IManagerTrapManager extends BaseManager implements
		ManagerInterface {

	private static SysLogger logger = SysLogger.getLogger(IManagerTrapManager.class);
	
	public String execute(String action) {
		// TODO Auto-generated method stub
		if ("list".equals(action)) {
			return list();
		}
		return null;
	}

	public String list() {
		String jsp = "/alarm/event/imanagertrap/imanagertrap_list.jsp";
		String name = getParaValue("name");
		String deviceIP = getParaValue("deviceIP");
		
		if(name == null || name.trim().length() == 0) {
			name = "-1";
		}

	    if (deviceIP == null || deviceIP.trim().length() == 0) {
	    	deviceIP = "-1";
	    }
	    
	    // ÎªÁË·ÖÒ³
	    IManagerTrapDao imanagerTrapDao = new IManagerTrapDao();
	    List pagelist = new ArrayList();
	    String wherestr = "where 1=1 ";
	    try {
	    	if (!"-1".equalsIgnoreCase(name)) {
                wherestr = wherestr + " and name = '" + name + "'";
            }
            if (!"-1".equalsIgnoreCase(deviceIP)) {
                wherestr = wherestr + " and device_ip = '" + deviceIP + "'";
            }
            list(imanagerTrapDao, wherestr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	imanagerTrapDao.close();
        }
        pagelist = (List) request.getAttribute("list");
        
        request.setAttribute("list", pagelist);
        request.setAttribute("name", name);
        request.setAttribute("deviceIP", deviceIP);

        return jsp;
	}
}
