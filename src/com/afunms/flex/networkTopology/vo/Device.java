/*
*  Value object used to store device data.
*/

package com.afunms.flex.networkTopology.vo;

public class Device
{
	private String id;					// Device id.
	private String ip;				
	private String name;				// Device name.
	private String type;				// Device type, if type equals "icon" only an icon will show without data.
										// Valid values are icon, switch, server and router.
	private String typeName;			// Name for the type of device, ex: a VOIP monitor is the type name for a type of server.
	private String iconUrl;				// Used only when type equals "icon"
	private int cpu;					// CPU consumption as a percent, not used if type equals "icon".
	private int memory;					// Memory consumption as a percent, not used if type equals "icon".
	private int ping;
	private float incoming;				// Incoming traffic in GBs, not used if type equals "icon".
	private float outgoing;				// Incoming traffic in GBs, not used if type equals "icon".
	private int x;						// Used only for the network topology view.
	private int y;						// Used only for the network topology view.
	private String[] connections;		// Array of id strings.
	private String location;			// Physical city location of the device.
	private String responseTime;		// In milliseconds.
	private String admin;				// Name of the administrator.
	private DeviceEvent[] deviceEvents;	// Array of DeviceEvent objects.
	private AlertTotal[] alertTotals;	// Array of AlertTotal objects.
	private int[] averageMemoryByMonth;	// Array of memory averages.
	private int[] averageCpuByMonth;	// Array of memory averages.
	private int[] averagePingByMonth;	// Array of memory averages.

	private int alarmCpu;
	private int alarmMemory;
	private float alarmIncoming;
	private float alarmOutgoing;
	private String alarmFlag;
	private String[] alarmDesc;
	
	private String parentTitle;
	
	public void setId(String s)
	{
		id = s;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}

	public void setType(String t)
	{
		type = t;
	}
	
	public String getType()
	{
		return type;
	}

	public void setTypeName(String t)
	{
		typeName = t;
	}
	
	public String getTypeName()
	{
		return typeName;
	}

	public void setIconUrl(String i)
	{
		iconUrl = i;
	}
	
	public String getIconUrl()
	{
		return iconUrl;
	}

	public void setCpu(int c)
	{
		cpu = c;
	}

	public int getCpu()
	{
		return cpu;
	}

	public void setMemory(int m)
	{
		memory = m;
	}

	public int getMemory()
	{
		return memory;
	}

	public void setIncoming(float i)
	{
		incoming = i;
	}

	public float getIncoming()
	{
		return incoming;
	}

	public void setOutgoing(float o)
	{
		outgoing = o;
	}

	public float getOutgoing()
	{
		return outgoing;
	}

	public void setX(int i)
	{
		x = i;
	}

	public int getX()
	{
		return x;
	}

	public void setY(int i)
	{
		y = i;
	}

	public int getY()
	{
		return y;
	}
	
	public void setConnections(String[] c)
	{
		connections = c;
	}
	
	public String[] getConnections()
	{
		return connections;
	}
	
	public void setLocation(String l)
	{
		location = l;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setResponseTime(String r)
	{
		responseTime = r;
	}
	
	public String getResponseTime()
	{
		return responseTime;
	}
	
	public void setAdmin(String a)
	{
		admin = a;
	}
	
	public String getAdmin()
	{
		return admin;
	}
	
	public void setDeviceEvents(DeviceEvent[] e)
	{
		deviceEvents = e;
	}
	
	public DeviceEvent[] getDeviceEvents()
	{
		return deviceEvents;
	}
	
	public void setAlertTotals(AlertTotal[] a)
	{
		alertTotals = a;
	}
	
	public AlertTotal[] getAlertTotals()
	{
		return alertTotals;
	}
	
	public void setAverageMemoryByMonth(int[] a)
	{
		averageMemoryByMonth = a;
	}
	
	public int[] getAverageMemoryByMonth()
	{
		return averageMemoryByMonth;
	}

	public int getAlarmCpu() {
		return alarmCpu;
	}

	public void setAlarmCpu(int alarmCpu) {
		this.alarmCpu = alarmCpu;
	}

	public int getAlarmMemory() {
		return alarmMemory;
	}

	public void setAlarmMemory(int alarmMemory) {
		this.alarmMemory = alarmMemory;
	}

	public float getAlarmIncoming() {
		return alarmIncoming;
	}

	public void setAlarmIncoming(float alarmIncoming) {
		this.alarmIncoming = alarmIncoming;
	}

	public float getAlarmOutgoing() {
		return alarmOutgoing;
	}

	public void setAlarmOutgoing(float alarmOutgoing) {
		this.alarmOutgoing = alarmOutgoing;
	}

	public String getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public String[] getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String[] alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	public String getParentTitle() {
		return parentTitle;
	}

	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int[] getAverageCpuByMonth() {
		return averageCpuByMonth;
	}

	public void setAverageCpuByMonth(int[] averageCpuByMonth) {
		this.averageCpuByMonth = averageCpuByMonth;
	}

	public int[] getAveragePingByMonth() {
		return averagePingByMonth;
	}

	public void setAveragePingByMonth(int[] averagePingByMonth) {
		this.averagePingByMonth = averagePingByMonth;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}