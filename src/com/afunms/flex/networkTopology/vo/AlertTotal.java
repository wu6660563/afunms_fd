/*
 * Value object used to store alert total data for a device.
 */

package com.afunms.flex.networkTopology.vo;

public class AlertTotal {
	private String type;
	private int total;

	public void setType(String t) {
		type = t;
	}

	public String getType() {
		return type;
	}

	public void setTotal(int d) {
		total = d;
	}

	public int getTotal() {
		return total;
	}
}