/*
 * @(#)TimeGraphFlexChartService.java     v1.01, 2013 12 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.common.util.SysUtil;
import com.afunms.flex.chart.FlexChartParameter;
import com.afunms.flex.chart.data.TimeGraphFlexChart;
import com.afunms.flex.chart.data.TimeSeries;
import com.afunms.flex.chart.data.TimeSeriesDataItem;
import com.afunms.node.model.PerformanceInfo;
import com.afunms.node.service.PerformanceInfoService;

/**
 * ClassName:   TimeGraphFlexChartService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 6 16:58:25
 */
public class TimeGraphFlexChartService {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Hashtable<String, String> SERIES_TITLE_HASHTABLE = new Hashtable<String, String>();

    static {
        SERIES_TITLE_HASHTABLE.put("AllInBandwidthUtilHdx", "综合入口流速");
        SERIES_TITLE_HASHTABLE.put("AllOutBandwidthUtilHdx", "综合出口流速");
        SERIES_TITLE_HASHTABLE.put("AllBandwidthUtilHdx", "综合总流速");
        SERIES_TITLE_HASHTABLE.put("PhysicalMemory", "物理内存");
        SERIES_TITLE_HASHTABLE.put("VirtualMemory", "虚拟内存");
        SERIES_TITLE_HASHTABLE.put("SwapMemory", "交换内存");
        SERIES_TITLE_HASHTABLE.put("InBandwidthUtilHdx", "入口流速");
        SERIES_TITLE_HASHTABLE.put("OutBandwidthUtilHdx", "出口流速");
    }

    /**
     * execute:
     * <p>
     *
     * @param action
     * @param parameters
     * @return
     *
     * @since   v1.01
     */
    public TimeGraphFlexChart execute(FlexChartParameter parameter) {
        String action = (String) parameter.getParameter("action");
        if ("flow".equals(action)) {
            return getFlow(parameter);
        } else if ("memory".equals(action)) {
            return getMemory(parameter);
        } else if ("InBandwidthUtilHdx".equals(action)) {
            return getInBandwidthUtilHdx(parameter);
        }
        return null;
    }

    /**
     * getFlow:
     * <p>获取流速信息
     *
     * @param   parameters
     * @return
     *
     * @since   v1.01
     */
    @SuppressWarnings("unchecked")
    public TimeGraphFlexChart getFlow(FlexChartParameter parameter) {
        String ipAddress = (String) parameter.getParameter("ipAddress");
        String startTime = (String) parameter.getParameter("startDate") + " 00:00:00";
        String endTime = (String) parameter.getParameter("endDate") + " 23:59:59";

        PerformanceInfoService service = new PerformanceInfoService();
        Hashtable<String, List<PerformanceInfo>> hashtable = service.getPerformanceGroupBySubentity(PerformanceInfoService.TABLE_NAME_ALLUTILHDX + SysUtil.doip(ipAddress), startTime, endTime);

        TimeGraphFlexChart chart = new TimeGraphFlexChart();
        Iterator<String> iterator = hashtable.keySet().iterator();
        while (iterator.hasNext()) {
            String subentity = iterator.next();
            chart.addSeries(create(subentity, hashtable.get(subentity)));
        }
        chart.setTitle("综合流速");
        chart.setOrdinateUnit("kb/s");
        return chart;
    }

    /**
     * getFlow:
     * <p>获取端口流速信息
     *
     * @param   parameters
     * @return
     *
     * @since   v1.01
     */
    @SuppressWarnings("unchecked")
    public TimeGraphFlexChart getInBandwidthUtilHdx(FlexChartParameter parameter) {
        String ipAddress = (String) parameter.getParameter("ipAddress");
        String ifindex = (String) parameter.getParameter("ifindex");
        String startTime = (String) parameter.getParameter("startDate") + " 00:00:00";
        String endTime = (String) parameter.getParameter("endDate") + " 23:59:59";

        PerformanceInfoService service = new PerformanceInfoService();
        Hashtable<String, List<PerformanceInfo>> hashtable = service.getPerformanceGroupBySubentity(PerformanceInfoService.TABLE_NAME_UTILHDX + SysUtil.doip(ipAddress), ifindex, startTime, endTime);

        TimeGraphFlexChart chart = new TimeGraphFlexChart();
        Iterator<String> iterator = hashtable.keySet().iterator();
        while (iterator.hasNext()) {
            String subentity = iterator.next();
            chart.addSeries(create(subentity, hashtable.get(subentity)));
        }
        chart.setTitle("端口流速");
        chart.setOrdinateUnit("kb/s");
        return chart;
    }

    public TimeGraphFlexChart getMemory(FlexChartParameter parameter) {
        String ipAddress = (String) parameter.getParameter("ipAddress");
        String startTime = (String) parameter.getParameter("startDate") + " 00:00:00";
        String endTime = (String) parameter.getParameter("endDate") + " 23:59:59";

        PerformanceInfoService service = new PerformanceInfoService();
        Hashtable<String, List<PerformanceInfo>> hashtable = service.getPerformanceGroupBySubentity(PerformanceInfoService.TABLE_NAME_MEMORY + SysUtil.doip(ipAddress), startTime, endTime);

        TimeGraphFlexChart chart = new TimeGraphFlexChart();
        Iterator<String> iterator = hashtable.keySet().iterator();
        while (iterator.hasNext()) {
            String subentity = iterator.next();
            chart.addSeries(create(subentity, hashtable.get(subentity)));
        }
        chart.setTitle("内存利用率");
        chart.setOrdinateUnit("%");
        return chart;
    }

    public TimeSeries create(String subentity, List<PerformanceInfo> list) {
        Double allValue = 0D;
        TimeSeries timeSeries = new TimeSeries();
        try {
            for (PerformanceInfo performanceInfo : list) {
                Double value = Double.valueOf(performanceInfo.getThevalue());
                String time = performanceInfo.getCollecttime();

                allValue += value;
                Date date = simpleDateFormat.parse(time);
                Date curDate = date;
                if (timeSeries.getCurValueTime() != null) {
                    curDate = simpleDateFormat.parse(timeSeries.getCurValueTime());
                }
                
                TimeSeriesDataItem dataItem = new TimeSeriesDataItem();
                dataItem.setTime(time);
                dataItem.setValue(value);
                if (value >= timeSeries.getMaxValue()) {
                    timeSeries.setMaxValue(value);
                    timeSeries.setMaxValueTime(time);
                } else if (value <= timeSeries.getMinValue()) {
                    timeSeries.setMinValue(value);
                    timeSeries.setMinValueTime(time);
                }
                if (date.getTime() >= curDate.getTime()) {
                    timeSeries.setCurValue(value);
                    timeSeries.setCurValueTime(time);
                }
                timeSeries.addOrUpdate(dataItem);
                timeSeries.setName(subentity);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            Double avgValue = allValue / list.size();
            timeSeries.setAvgValue(avgValue.intValue());
        }
        timeSeries.setTitle(SERIES_TITLE_HASHTABLE.get(subentity));
        return timeSeries;
    }

}

