/*
 * Value object used to store event data for a device.
 */

package com.afunms.flex.networkTopology.vo;

public class DeviceEvent {
	private long date;          // Milliseconds since the Epoch.
	private String description; // Description of the event.

	public void setDate(long d) {
		date = d;
	}

	public long getDate() {
		return date;
	}

	public void setDescription(String d) {
		description = d;
	}

	public String getDescription() {
		return description;
	}
}