/*
 * @(#)HpunixLogFileKeywordConstant.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

/**
 * 
 * ClassName: Tru64LogFileKeywordConstant.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:27:49 PM
 */

public class Tru64LogFileKeywordConstant {

	public static final String TRU64_UPTIME_BEGIN_KEYWORD = "cmdbegin:uptime";

	public static final String TRU64_UPTIME_END_KEYWORD = "cmdbegin:ifconfig";

	public static final String TRU64_DATE_BEGIN_KEYWORD = "cmdbegin:date";

	public static final String TRU64_DATE_END_KEYWORD = "cmdbegin:uptime";

	public static final String TRU64_NETSTAT_BEGIN_KEYWORD = "cmdbegin:netstat";

	public static final String TRU64_NETSTAT_END_KEYWORD = "cmdbegin:end";
	
	public static final String TRU64_IPCONFIG_START_KEYWORD = "cmdbegin:ifconfig";
	
	public static final String TRU64_IPCONFIG_END_KEYWORD = "cmdbegin:netstat";
	
	public static final String TRU64_USER_BEGIN_KEYWORD = "cmdbegin:memoryend";

	public static final String TRU64_USER_END_KEYWORD = "cmdbegin:user";
	
	public static final String TRU64_UNAME_BEGIN_KEYWORD = "cmdbegin:uname";

	public static final String TRU64_UNAME_END_KEYWORD = "cmdbegin:date";
	
	public static final String TRU64_PROC_BEGIN_KEYWORD = "cmdbegin:proc";

	public static final String TRU64_PROC_END_KEYWORD = "cmdbegin:vmstat";
	
	public static final String TRU64_MEMORY_BEGIN_KEYWORD = "cmdbegin:vm -P";

	public static final String TRU64_MEMORY_END_KEYWORD = "cmdbegin:uname";
	
	public static final String TRU64_DISK_BEGIN_KEYWORD = "cmdbegin:disk";

	public static final String TRU64_DISK_END_KEYWORD = "cmdbegin:proc";
	
	public static final String TRU64_CPU_BEGIN_KEYWORD = "cmdbegin:vmstat";

	public static final String TRU64_CPU_END_KEYWORD = "cmdbegin:vm -P";

}
