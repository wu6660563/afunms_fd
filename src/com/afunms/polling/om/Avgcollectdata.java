package com.afunms.polling.om;

import java.io.Serializable;

public class Avgcollectdata implements Serializable {

	/** nullable persistent field */
    private String ipaddress;
    
    /** nullable persistent field */
    private String thevalue;

   

    public java.lang.String getIpaddress() {
        return this.ipaddress;
    }

	public void setIpaddress(java.lang.String ipaddress) {
		this.ipaddress = ipaddress;
	}

  

    public java.lang.String getThevalue() {
        return this.thevalue;
    }

	public void setThevalue(java.lang.String thevalue) {
		this.thevalue = thevalue;
	}
	
}