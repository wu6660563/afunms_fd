/*
 * @(#)SendDataConstant.java     v1.01, Jul 22, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

/**
 * ClassName:   SendDataConstant.java
 * <p> 发送报文变量，用于中调二次运维接口
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 22, 2014 8:57:05 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendDataConstant {
	
	/**
	 * HEADER_SYSTYPE:
	 * <p>1网管、2安管、3横向业务系统、4地调系统、5站端系统
	 *
	 * @since   v1.01
	 */
	public final static String HEADER_SYSTYPE = "4";
	
	/**
	 * HEADER_SYSNAME:
	 * <p>来源系统名，比如网管系统名netgain、安管系统名venus、调度：ems等
	 *
	 * @since   v1.01
	 */
	public final static String HEADER_SYSNAME = "donghua";
	
	/**
	 * HEADER_SYSDOMAIN:
	 * <p>所属的子控区或主控区：比如广东中调：1，佛山地调:4
	 *
	 * @since   v1.01
	 */
	public final static String HEADER_SYSDOMAIN = "4";
	
	/**
	 * SAFETYZONE_1:
	 * <p> 安全区 --一区
	 *
	 * @since   v1.01
	 */
	public final static String SAFETYZONE_1 = "1";
	
	/**
	 * SAFETYZONE_2:
	 * <p> 安全区 --二区
	 *
	 * @since   v1.01
	 */
	public final static String SAFETYZONE_2 = "2";
	
	/**
	 * SAFETYZONE_3:
	 * <p> 安全区 --三区
	 *
	 * @since   v1.01
	 */
	public final static String SAFETYZONE_3 = "3";
	
	/**
	 * header_msgtype:
	 * <p>消息类型，
	 * 如：0 召唤数据报文、1 模型对象数据报文、2 GID映射表报文、3告警/事件数据报文、
	 * 4性能模型数据报文、5 性能运行数据报文、6指标数据报文、7漏洞记录数据报文、
	 * 8基线核查记录数据报文、9关系数据报文、10 健康模型数据报文、11 SVG图形数据报文、
	 * 12设备信息与状态数据报文、13业务行为规则数据报文、14活跃会话信息报文、
	 * 99 设备数据视图请求报文、100 软件列表、101进程列表、102 端口列表、103 IP列表、
	 * 104 路由表、105 TCP链接表、106 UDP链接表、107 端口映射表、108 VLAN信息表、
	 * 109 配置变更历史、110 radware事件列表、111 brocade事件列表、112 fc事件列表、
	 * 113 syslog信息、114 连接信息、200 安管自定义场景报文、201 平台自用,
	 * 202 syslog日志报文,203 发送给安管时间段。由各系统自定义，例如syslog的消息体可包含如下内容
	 *
	 * @since   v1.01
	 */
	public final static String HEADER_MSGTYPE_0 = "0";
	
	public final static String HEADER_MSGTYPE_1 = "1";
	
	public final static String HEADER_MSGTYPE_2 = "2";
	
	public final static String HEADER_MSGTYPE_3 = "3";
	
	public final static String HEADER_MSGTYPE_4 = "4";
	
	public final static String HEADER_MSGTYPE_5 = "5";
	
	public final static String HEADER_MSGTYPE_6 = "6";
	
	public final static String HEADER_MSGTYPE_7 = "7";
	
	public final static String HEADER_MSGTYPE_8 = "8";
	
	public final static String HEADER_MSGTYPE_9 = "9";
	
	public final static String HEADER_MSGTYPE_10 = "10";
	
	public final static String HEADER_MSGTYPE_11 = "11";
	
	public final static String HEADER_MSGTYPE_12 = "12";
	
	public final static String HEADER_MSGTYPE_13 = "13";
	
	public final static String HEADER_MSGTYPE_14 = "14";
	
	public final static String HEADER_MSGTYPE_99 = "99";
	
	public final static String HEADER_MSGTYPE_100 = "100";
	
	public final static String HEADER_MSGTYPE_101 = "101";
	
	public final static String HEADER_MSGTYPE_102 = "102";
	
	public final static String HEADER_MSGTYPE_103 = "103";
	
	public final static String HEADER_MSGTYPE_104 = "104";
	
	public final static String HEADER_MSGTYPE_105 = "105";
	
	public final static String HEADER_MSGTYPE_106 = "106";
	
	public final static String HEADER_MSGTYPE_107 = "107";
	
	public final static String HEADER_MSGTYPE_108 = "108";
	
	public final static String HEADER_MSGTYPE_109 = "109";
	
	public final static String HEADER_MSGTYPE_110 = "110";
	
	public final static String HEADER_MSGTYPE_111 = "111";
	
	public final static String HEADER_MSGTYPE_112 = "112";
	
	public final static String HEADER_MSGTYPE_113 = "113";
	
	public final static String HEADER_MSGTYPE_114 = "114";
	
	public final static String HEADER_MSGTYPE_200 = "200";
	
	public final static String HEADER_MSGTYPE_201 = "201";
	
	public final static String HEADER_MSGTYPE_202 = "202";
	
	public final static String HEADER_MSGTYPE_203 = "203";
	
	/**
	 * TYPE_SERVER:模型对象设备类型，服务器默认类型
	 * <p>SERVER
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_SERVER = "server";
	
	/**
	 * TYPE_SERVER_LINUX_DEVICE:模型对象设备类型,LINUX
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_SERVER_LINUX_DEVICE = "server_linux_device";
	
	/**
	 * TYPE_SERVER_WINDOWS_DEVICE:模型对象设备类型，Window
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_SERVER_WINDOWS_DEVICE = "server_windows_device";
	
	/**
	 * TYPE_SERVER_AIX_DEVICE:模型对象设备类型，Aix
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_SERVER_AIX_DEVICE = "server_aix_device";
	
	/**
	 * TYPE_NETWORK_DEVICE:默认网络设备
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_NETWORK_DEVICE = "network_device";
	
	/**
	 * TYPE_NETWORK_SWITCH_DEVICE:交换机
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_NETWORK_SWITCH_DEVICE = "network_switch_device";
	
	/**
	 * TYPE_NETWORK_SWITCH_DEVICE:路由器
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_NETWORK_ROUTE_DEVICE = "network_route_device";
	
	/**
	 * TYPE_NETWORK_FIREWALL_DEVICE:防火墙
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_NETWORK_FIREWALL_DEVICE = "network_firewall_device";
	
	/**
	 * TYPE_UNKNOWN:未知类型
	 * <p>
	 *
	 * @since   v1.01
	 */
	public final static String TYPE_UNKNOWN = "unknown";
}

