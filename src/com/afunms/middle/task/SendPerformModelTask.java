/*
 * @(#)SendPerformTask.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import com.afunms.initialize.ResourceCenter;
import com.afunms.middle.model.Counter;
import com.afunms.middle.model.HeaderData;
import com.afunms.middle.model.PerformanceModelData;
import com.afunms.middle.model.PerformanceVo;
import com.afunms.middle.model.SendData;
import com.afunms.middle.util.SendDataConstant;

/**
 * ClassName:   SendPerformModelTask.java
 * <p> 性能模型数据定时task
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 5, 2014 11:13:22 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendPerformModelTask extends TimerTask {

	@Override
	public void run() {
		//retundata
		SendData<PerformanceModelData> sendData = new SendData<PerformanceModelData>();
		
		HeaderData headerData = new HeaderData();
		headerData.setSystype(Integer.parseInt(SendDataConstant.HEADER_SYSTYPE));
		headerData.setSysname(SendDataConstant.HEADER_SYSNAME);
		headerData.setSysdomain(Integer.parseInt(SendDataConstant.HEADER_SYSDOMAIN));
		headerData.setMsgtype(Integer.parseInt(SendDataConstant.HEADER_MSGTYPE_4));
		sendData.setHeader(headerData);
		
		//PING/CPU/MEMORY/ResponseTime
		Set<PerformanceModelData> models = new HashSet<PerformanceModelData>();
		
		PerformanceModelData model = new PerformanceModelData();
		model.setType("publicIndicators");
		
		PerformanceVo perform = new PerformanceVo();
		Set<Counter> counters = new HashSet<Counter>();
		perform.setGroup("publicIndicators");
		
		Counter ping_conter1 = new Counter();
		ping_conter1.setIndex(1);
		ping_conter1.setName("Ping_Utilization");
		ping_conter1.setAlias("ping利用率");
		ping_conter1.setDesc("ping利用率");
		ping_conter1.setType(0);
		ping_conter1.setUnit("%");
		counters.add(ping_conter1);
		
		Counter reponseTime_conter1 = new Counter();
		reponseTime_conter1.setIndex(2);
		reponseTime_conter1.setName("Ping_ResponseTime");
		reponseTime_conter1.setAlias("响应时间");
		reponseTime_conter1.setDesc("响应时间");
		reponseTime_conter1.setType(0);
		reponseTime_conter1.setUnit("ms");
		counters.add(reponseTime_conter1);
		
		Counter cpu_conter1 = new Counter();
		cpu_conter1.setIndex(3);
		cpu_conter1.setName("CPU_Utilization");
		cpu_conter1.setAlias("响应时间");
		cpu_conter1.setDesc("响应时间");
		cpu_conter1.setType(0);
		cpu_conter1.setUnit("ms");
		counters.add(cpu_conter1);
		
		Counter mem_conter1 = new Counter();
		mem_conter1.setIndex(4);
		mem_conter1.setName("Memory_Utilization");
		mem_conter1.setAlias("内存利用率");
		mem_conter1.setDesc("内存利用率");
		mem_conter1.setType(0);
		mem_conter1.setUnit("%");
		counters.add(mem_conter1);
		
		perform.setCounters(counters);
		model.setPerf(perform);
		models.add(model);
		
//		//ping
//		PerformanceModelData pingModel = new PerformanceModelData();
//		pingModel.setType("连通率情况");
//		
//		PerformanceVo ping_perform = new PerformanceVo();
//		Set<Counter> ping_counters = new HashSet<Counter>();
//		ping_perform.setGroup("Ping");
//		
//		Counter ping_conter1 = new Counter();
//		ping_conter1.setIndex(1);
//		ping_conter1.setName("Utilization");
//		ping_conter1.setAlias("ping利用率");
//		ping_conter1.setDesc("ping利用率");
//		ping_conter1.setType(0);
//		ping_conter1.setUnit("%");
//		ping_counters.add(ping_conter1);
//		ping_perform.setCounters(ping_counters);
//		pingModel.setPerf(ping_perform);
//		models.add(pingModel);
//		
//		//ResponseTime
//		PerformanceModelData responseTimeModel = new PerformanceModelData();
//		responseTimeModel.setType("响应时间");
//		
//		PerformanceVo responseTime_perform = new PerformanceVo();
//		Set<Counter> responseTime_counters = new HashSet<Counter>();
//		responseTime_perform.setGroup("ResponseTime");
//		
//		Counter responseTime_conter1 = new Counter();
//		responseTime_conter1.setIndex(1);
//		responseTime_conter1.setName("ResponseTime");
//		responseTime_conter1.setAlias("响应时间");
//		responseTime_conter1.setDesc("响应时间");
//		responseTime_conter1.setType(0);
//		responseTime_conter1.setUnit("ms");
//		responseTime_counters.add(responseTime_conter1);
//		responseTime_perform.setCounters(responseTime_counters);
//		responseTimeModel.setPerf(responseTime_perform);
//		models.add(responseTimeModel);
//		
//		//CPU
//		PerformanceModelData cpu_model = new PerformanceModelData();
//		cpu_model.setType("CPU利用率");
//		
//		PerformanceVo cpu_perform = new PerformanceVo();
//		Set<Counter> cpu_counters = new HashSet<Counter>();
//		cpu_perform.setGroup("CPU");
//		
//		Counter cpu_conter1 = new Counter();
//		cpu_conter1.setIndex(1);
//		cpu_conter1.setName("Utilization");
//		cpu_conter1.setAlias("CPU利用率");
//		cpu_conter1.setDesc("CPU利用率");
//		cpu_conter1.setType(0);
//		cpu_conter1.setUnit("%");
//		cpu_counters.add(cpu_conter1);
//		cpu_perform.setCounters(cpu_counters);
//		cpu_model.setPerf(cpu_perform);
//		models.add(cpu_model);
//		
//		//MEM
//		PerformanceModelData mem_model = new PerformanceModelData();
//		mem_model.setType("内存利用率");
//		
//		PerformanceVo mem_perform = new PerformanceVo();
//		Set<Counter> mem_counters = new HashSet<Counter>();
//		mem_perform.setGroup("Memory");
//		
//		Counter mem_conter1 = new Counter();
//		mem_conter1.setIndex(1);
//		mem_conter1.setName("Utilization");
//		mem_conter1.setAlias("内存利用率");
//		mem_conter1.setDesc("内存利用率");
//		mem_conter1.setType(0);
//		mem_conter1.setUnit("%");
//		mem_counters.add(mem_conter1);
//		mem_perform.setCounters(mem_counters);
//		mem_model.setPerf(mem_perform);
//		models.add(mem_model);
		
		sendData.setBody(models);
		String jsonString = JSONObject.fromObject(sendData).toString();
		saveFileByString(jsonString);
	}
	
	public void saveFileByString(String jsonString) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(ResourceCenter.getInstance().getSysPath() + 
					File.separator + "middle_file" + File.separator + "perform_model.log");
			fileWriter.write(jsonString);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileWriter != null)fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fileWriter = null;
			}
		}
	}

}

