/*
 * @(#)URLData.java     v1.01, Apr 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.urlmonitor;

import java.util.Calendar;

/**
 * ClassName:   URLData.java
 * <p>
 *
 * @author      дТаж
 * @version     v1.01
 * @since       v1.01
 * @Date        Apr 24, 2013 4:54:25 PM
 */
public class URLData {

    private int id;
    
    private int url_id;

    private int is_canconnected;

    private int is_valid;

    private int is_refresh;

    private String reason;

    private String page_context;

    private Calendar mon_time;

    private int sms_sign;
        
    private int condelay;
    
    private String pagesize;
    
    private String key_exist;
    
    private String change_rate;
}

