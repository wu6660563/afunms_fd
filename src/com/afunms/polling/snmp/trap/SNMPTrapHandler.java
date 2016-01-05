/*
 * @(#)TrapHandler.java     v1.01, Dec 3, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.trap;

import java.io.Serializable;

import org.snmp4j.CommandResponder;

public interface SNMPTrapHandler extends CommandResponder , Serializable {

}

