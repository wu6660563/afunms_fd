package com.afunms.system.manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DnsConfigDao;
import com.afunms.application.dao.Dnsmonitor_historyDao;
import com.afunms.application.dao.Dnsmonitor_realtimeDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.DnsConfig;
import com.afunms.application.model.Dnsmonitor_realtime;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DateE;
import com.afunms.common.util.InitCoordinate;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.dao.DepartmentDao;
import com.afunms.system.model.TimeGratherConfig;
import com.afunms.system.model.User;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.util.KeyGenerator;



public class SystemConifgManager extends BaseManager implements ManagerInterface{

	
	public String execute(String action) { 
		if(action.equals("collectwebflag")){
			return collectwebflag();
		}
		return null;
	}
	
	/**
	 * 修改系统运行模式
	 * 0 :采集与访问集成
	 * 1 :采集与访问分离
	 * @return
	 */
	private String collectwebflag(){
		return "/config/systemconfig/editcollectwebflag.jsp";
	}
}
