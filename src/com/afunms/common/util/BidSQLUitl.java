/*
 * @(#)BidSQLUitl.java     v1.01, Jan 6, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.common.util;

import java.util.List;

import com.afunms.config.model.Business;

/**
 * ClassName:   BidSQLUitl.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 6, 2013 4:24:13 PM
 */
public class BidSQLUitl {

    public static String getBidSQL(String businessId, String fieldName) {
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (businessId != null) {
            if (businessId != "-1") {
                String[] bids = businessId.split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( " + fieldName + " like '%,"
                                        + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                s.append(" or " + fieldName + " like '%,"
                                        + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }
            }
        }
        String sql = s.toString();
        return sql;
    }

    public static String getBidNames(String businessId, List<Business> allbuss) {
        String names = "";
        if (businessId != null) {
            if (businessId != "-1") {
                String[] bids = businessId.split(",");
                for (String bid : bids) {
                    for (Business business : allbuss) {
                        if (bid.equals(business.getId())) {
                            names += business.getName() + ",";
                        }
                    }
                }
            }
        }
        return names;
    }
}

