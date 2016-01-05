/*
 * @(#)HpunixLogFileKeywordConstant.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

/**
 * 
 * ClassName: HpunixLogFileKeywordConstant.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:27:49 PM
 */

public class HpunixLogFileKeywordConstant {

	public static final String HPUNIX_UPTIME_BEGIN_KEYWORD = "cmdbegin:uptime";

	public static final String HPUNIX_UPTIME_END_KEYWORD = "cmdbegin:netstat";

	public static final String HPUNIX_DATE_BEGIN_KEYWORD = "cmdbegin:date";

	public static final String HPUNIX_DATE_END_KEYWORD = "cmdbegin:uptime";

	public static final String HPUNIX_NETSTAT_BEGIN_KEYWORD = "cmdbegin:netstat";

	public static final String HPUNIX_NETSTAT_END_KEYWORD = "cmdbegin:end";
	
	public static final String HPUNIX_USER_BEGIN_KEYWORD = "cmdbegin:memoryend";

	public static final String HPUNIX_USER_END_KEYWORD = "cmdbegin:user";
	
	public static final String HPUNIX_UNAME_BEGIN_KEYWORD = "cmdbegin:uname";

	public static final String HPUNIX_UNAME_END_KEYWORD = "cmdbegin:date";
	
	public static final String HPUNIX_PROC_BEGIN_KEYWORD = "cmdbegin:proc";

	public static final String HPUNIX_PROC_END_KEYWORD = "cmdbegin:vmstat";
	
	public static final String HPUNIX_FREEMEMORY_BEGIN_KEYWORD = "cmdbegin:vmstat";

	public static final String HPUNIX_FREEMEMORY_END_KEYWORD = "cmdbegin:swapinfo";
	
	public static final String HPUNIX_MEMORY_BEGIN_KEYWORD = "cmdbegin:end";

	public static final String HPUNIX_MEMORY_END_KEYWORD = "cmdbegin:memory";
	
	public static final String HPUNIX_DISK_BEGIN_KEYWORD = "cmdbegin:disk";

	public static final String HPUNIX_DISK_END_KEYWORD = "cmdbegin:cpu";
	
	public static final String HPUNIX_CPU_BEGIN_KEYWORD = "cmdbegin:cpu";

	public static final String HPUNIX_CPU_END_KEYWORD = "cmdbegin:proc";

}
