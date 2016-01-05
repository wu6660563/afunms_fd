On Error Resume Next



'脚本参数配置==================================================
'---------------配置监控主机的ip地址--------------------
 Hostaddress="10.10.152.57"  '配置生成的主机xml

 ftpname="nms"  'ftp 的用户
 ftppassowd="webnms" 'ftp的密码
 ftpServerHost="10.10.152.57"  'ftp 服务的ip地址
 ftppath="/"  'ftp 上传的路径，默认为根目录/ 




'=======================================================
'---------------end 配置监控主机的ip地址----------------











'定义几个参数用来控制各个采集脚本是否运行(控制参数1表示不运行,0表示运行)
Dim configflg      '控制配置信息是否采集
Dim infoflg        '控制基本信息是否采集
Dim performanceflg '控制性能数据是否采集
'------------------------------------------------------------------------------
'采集脚本运行参数,先预设几个运行的规则,以后可以吧从服务器上取得配置文件设置本参数
'-------------------------------------------------------------------------------

Dim config '配置信息采集时间间隔
Dim info  '基本信息采集时间间隔
Dim performancet'性能信息采集时间间隔


'用来保留上次运行时间
Dim configtime
Dim infotime
Dim performancedtime



'函数是用分钟来判断时间间隔的采集 （60分钟内）
Function equalMtime(Mtime)
  Dim flg
  flg=false
   'Wscript.Echo "判断是否到运行时间"
  If (Minute(Now) Mod CInt(Mtime) =0 or Minute(Now)= CInt("0")  ) Then
   flg=true
  End If
 equalMtime=flg
End Function


'函数用来判断以小时为间隔的采集 （24小时内）
Function equalHtime(Mtime)
  Dim flg
  flg=false
   'Wscript.Echo "判断是否到运行时间"
  If (Hour(Now) Mod CInt(Mtime) =0 and Minute(Now)= CInt("0")  ) Then
   flg=true
  End If
 equalHtime=flg
End Function



 
first=true '定义一个参数来判断是否是第一次运行(true,表示第一次运行,false,表示运行多次)
xml="<?xml version=""1.0"" encoding=""GBK""?>"

strComputer = "."


'-------------------------------------------------------
'-------------------------------------------------------

'macaddress=""  '地址
'Set objWMIService = GetObject("winmgmts:\\"& strComputer & "\root\cimv2")
'Set colAdapters = objWMIService.ExecQuery _
 ' ("SELECT * FROM Win32_NetworkAdapterConfiguration WHERE IPEnabled = True ")
'n = 1
'For Each objAdapter in colAdapters
    'macaddress=objAdapter.IPAddress(0)
	''macaddress="10.10.152.27"  '地址
'Next
'---------------取得mac地址--------------------


'**************************************
'*根据时间来判断脚本是否到时间运行
'*runtime 上次运行的时间
'*newtime 现在系统的时间
'*runvalue 运行频率(以分钟为准)
'**************************************

'Function equaltime(runtime,newtime, runvalue)
  'equaltime=false
   'Wscript.Echo "equaltime="& equaltime
  ' If runvalue =-1 Then '如果是-1表示该脚本不再执行
  ' equaltime=false
   'End If
   
   '如果是大于或等于上次运行时间,则表示需运行次脚本(安分钟计算)
  ' If runvalue =>0 Then
  ' If ( Abs(DateDiff("s",runtime,newtime))>=runvalue*60) Then 
  ' equaltime=True
   'Wscript.Echo "equaltime"&equaltime
  ' End If
   'End If
   'Wscript.Echo "equaltime2="& equaltime  
'End Function

'*******************************
'删除文件
'*******************************
Function deletefile(filename)
 on error resume next 
  Dim fso, f2
   Set fso = CreateObject("Scripting.FileSystemObject")
   '获得文件当前位置的句柄。
   Set f2 = fso.GetFile(filename)
   '删除文件。
   f2.Delete
End Function


'***************************************
'计算数组的大小
'***************************************
'Function arraycount(MyArray())

  ' i=0 
	'for each X in MyArray
	'i=i+1 
	'next
'arraycount=i
'End Function
'*******************************
'*把mac地址字符进行分割合并
'*macaddress mac地址
'*******************************
Function mac(macaddress)
on error resume next 
Dim a
MyArray = Split(macaddress, ":", -1, 1)
For i = Lbound(MyArray) To Ubound(MyArray)
'pathstring=pathstring+ReturnArray(i)&"\"
a=a+ MyArray(i) '通过弹出消息框循环显示数组值 
Next
'Wscript.Echo a
mac=a
End Function


'**********************************
'createfile 生成的文件名 
'filename 文件得名称
'ftptype  ftp的类型用来又两种情况,put 上传,get 下载
'filepath 上传文件或下载文件的目录
'username ftp的用户
'password ftp的密码
'host ftp服务器的地址
'**********************************

Function ftpfile(createfile,filename,ftptype,filepath,username,password,host)
Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.CreateTextFile(createfile,1)
objFile.WriteLine "open " & host
objFile.WriteLine username
objFile.WriteLine password 
objFile.WriteLine "cd " & filepath
objFile.WriteLine ftptype&" " & filename
objFile.WriteLine "bye"
objFile.Close
Set objFile = Nothing
Set objFSO = Nothing
End Function


'**********************************
'ftp上传下载函数
'createfile 生成的文件名 
'filename 文件得名称
'ftptype  ftp的类型用来又两种情况,put 上传,get 下载
'filepath 上传文件或下载文件的目录
'username ftp的用户
'password ftp的密码
'host ftp服务器的地址
'**********************************


Function ftp(createfile,filename,ftptype,filepath,username,password,host)
dim obj
set obj=createobject("wscript.shell")

 Call ftpfile(createfile,filename,ftptype,filepath,username,password,host)
 obj.run "command /c ftp -s:"&createfile ,0,True
 WScript.sleep 5000'休眠1秒
Call deletefile(createfile)
'如果是下载不删除下载的文件
If ftptype="put" Then 
'Wscript.Echo "put"
'Call deletefile(filename)
End If
End Function

'**********************************************
'*  循环执行采集脚本
'**********************************************


Do While True '使用死循环来实现线程
  
   '根据系统是采集运行参数来设置控制中的参数,
    '--------------------------------------
    ' a=Time
    'WScript.sleep 66000
     'b=Time
    ' c=Abs(DateDiff("s",b,a))'此方法用来判断两个时间内得时间差(秒)
        
       If first Then     
		 '第一次运行设计控制参数都可运行
		 configflg=true  
		 infoflg=true  
         performanceflg=true 
         
       
         config=5 '配置信息3小时采集一次
         info =5'基本信息采集1小时
         performancet=5'性能信息采集5分钟采集一次

         first=false
       End If
	'--------------------------------------

 
  '开始生成主机的采集文件
  Set objFS = CreateObject("Scripting.FileSystemObject")
  hostfile=Hostaddress&".xml"
  Set objNewFile = objFS.CreateTextFile(hostfile)
  objNewFile.WriteLine xml
  objNewFile.WriteLine "<host>"


 '********************************************
 '运行脚本并且生成文件
 '********************************************



 '============================配置信息数据采集===========================

'---------------------------------
'硬盘配置信息信息脚本
'---------------------------------
If configflg Then 

strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_DiskDrive")

objNewFile.WriteLine "<diskconfigstart>"
WScript.sleep 1000'休眠1秒
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<diskconfiginfo>"
    
     objNewFile.WriteLine "<BytesPerSector>" & objStartupCommand.BytesPerSector &"</BytesPerSector>" '硬盘二级缓存
     objNewFile.WriteLine "<Caption>" & objStartupCommand.Caption &"</Caption>" '硬盘名称
     objNewFile.WriteLine "<Index>" & objStartupCommand.Index &"</Index>"'索引
     objNewFile.WriteLine "<InterfaceType>" & objStartupCommand.InterfaceType &"</InterfaceType>"'接口类型
	 objNewFile.WriteLine "<Size>" & objStartupCommand.Size &"</Size>"'硬盘大小
	
	 objNewFile.WriteLine "</diskconfiginfo>"
Next
objNewFile.WriteLine "</diskconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒





'---------------------------------
'网卡配置信息脚本
'---------------------------------


strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_NetworkAdapterConfiguration where IPEnabled = TRUE and MACAddress !=null ")

objNewFile.WriteLine "<Networkconfigstart>"

For Each objStartupCommand in colStartupCommands

    
     objNewFile.WriteLine "<Networkconfiginfo>"
	 trIPAddress = Join(objStartupCommand.IPAddress, ",")
     objNewFile.WriteLine "<IPAddress>" & trIPAddress &"</IPAddress>"'ip地址
     objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>" '网卡名称
     objNewFile.WriteLine "<Index>" & objStartupCommand.Index&"</Index>" '索引
     trIPSubnet = Join(objStartupCommand.IPSubnet, ",")
     objNewFile.WriteLine "<IPSubnet>" & trIPSubnet &"</IPSubnet>"'子网掩码
	 objNewFile.WriteLine "<MACAddress>" & objStartupCommand.MACAddress &"</MACAddress>"'网卡名称
     trDNSServerSearchOrder = Join(objStartupCommand.DNSServerSearchOrder, ",")
	 objNewFile.WriteLine "<DNSServerSearchOrder>" & trDNSServerSearchOrder &"</DNSServerSearchOrder>"'dns
     trDefaultIPGateway = Join(objStartupCommand.DefaultIPGateway, ",")
	 objNewFile.WriteLine "<DefaultIPGateway>" & trDefaultIPGateway &"</DefaultIPGateway >"'网关
	 objNewFile.WriteLine "</Networkconfiginfo>"
Next
objNewFile.WriteLine "</Networkconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""


'configtime=Time
End If


'=============================配置基本信息数据采集======================




'============================基本信息数据采集=======================

  If infoflg  Then   '判断主机信息时候要采集

  WScript.sleep 1000'休眠5秒
  strComputer="."
  
    objNewFile.WriteLine "<hostinfostart>"  '打印开始标记
    objNewFile.WriteLine "<hostinfo>" 
	Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
	Set colItems = objWMIService.ExecQuery("Select Manufacturer,TotalPhysicalMemory,Caption,NumberOfProcessors from Win32_ComputerSystem")
	For Each objItem in colItems
       objNewFile.WriteLine "<TotalPhysicalMemory>" & objItem.TotalPhysicalMemory &"</TotalPhysicalMemory>"
       objNewFile.WriteLine "<Hostname>" & objItem.Caption &"</Hostname>" '计算机名
       'objNewFile.WriteLine "<Manufacturer>" & objItem.Caption &"</Manufacturer>" '厂商
       objNewFile.WriteLine "<NumberOfProcessors>" & objItem.NumberOfProcessors &"</NumberOfProcessors>" 'cpu个数
	Next

   '操作系统
    WScript.sleep 500'休眠5秒
	
    Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
    Set colOperatingSystems = objWMIService.ExecQuery ("Select * from Win32_OperatingSystem")
     For Each objOperatingSystem in colOperatingSystems
         objNewFile.WriteLine "<Sysname>" & objOperatingSystem.Caption &"</Sysname>" '操作系统名称
		 objNewFile.WriteLine "<SerialNumber>" & objOperatingSystem.SerialNumber  &"</SerialNumber>" '编码
		 'objNewFile.WriteLine "<LastBootUpTime>" & objOperatingSystem.LastBootUpTime &"</LastBootUpTime>" '最近启动时间
		 'objNewFile.WriteLine "<LocalDateTime>" & objOperatingSystem.LocalDateTime  &"</LocalDateTime>" '主机服务器当前时间
		 objNewFile.WriteLine "<CSDVersion>" & objOperatingSystem.CSDVersion &"</CSDVersion>" '系统的版本号
         
       Next

          WScript.sleep 500'休眠5秒
    Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
    Set colOperatingSystems = objWMIService.ExecQuery("Select * from Win32_Processor")
     For Each objOperatingSystem in colOperatingSystems
         objNewFile.WriteLine "<CPUname>" & objOperatingSystem.Name & (objOperatingSystem.CurrentClockSpeed)/1000 &" GHz""</CPUname>" '操作系统名称
		 objNewFile.WriteLine "<SerialNumber >" & objOperatingSystem.SerialNumber  &"</SerialNumber >" '编码
       Next
       objNewFile.WriteLine "</hostinfo>" 

      objNewFile.WriteLine "</hostinfostart>" 
     objNewFile.WriteLine ""
	 objNewFile.WriteLine ""
	  
	  WScript.sleep 2000'休眠5秒
 


'--------------------------------
'用户信息信息脚本
'--------------------------------

strComputer="."
Set objWMIService = GetObject("winmgmts:"& "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_UserAccount")

objNewFile.WriteLine "<UserAccountinfostart>"
''WScript.sleep 1000'休眠1秒
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<UserAccountinfo>"
    
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>" '名称
     objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>" '描述
     objNewFile.WriteLine "<Domain>" & objStartupCommand.Domain &"</Domain>"'所在域
     objNewFile.WriteLine "<LocalAccount>" & objStartupCommand.LocalAccount  &"</LocalAccount>"'是否本地帐户
	 objNewFile.WriteLine "<Status>" & objStartupCommand.Status&"</Status>"'状态
	 objNewFile.WriteLine "</UserAccountinfo>"
Next
objNewFile.WriteLine "</UserAccountinfostart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""




 
'--------------------------------
'启动项信息脚本
'--------------------------------

'strComputer="."
'Set objWMIService = GetObject("winmgmts:" _
 '   & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
'Set colStartupCommands = objWMIService.ExecQuery _
 '   ("Select * from Win32_StartupCommand")

'objNewFile.WriteLine "<startupinfostart>"
''WScript.sleep 1000'休眠1秒
'For Each objStartupCommand in colStartupCommands
   '  objNewFile.WriteLine "<startupinfo>"
    
     'objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>" '名称
     'objNewFile.WriteLine "<Command>" & objStartupCommand.Command &"</Command>" '命令行
     'objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>"'描述
     'objNewFile.WriteLine "<User>" & objStartupCommand.User &"</User>"'用户
	 'objNewFile.WriteLine "</startupinfo>"
'Next
'objNewFile.WriteLine "</startupinfostart>"
'objNewFile.WriteLine ""
'objNewFile.WriteLine ""




'infotime=Time '运行玩后保留当前时间
WScript.sleep 2000'休眠1秒
End If

'=============================结束基本信息数据采集======================



'============================性能数据采集===========================

'----------------------------------
'监控服务信息状态
'----------------------------------
If performanceflg Then
  
Set objWMIService = GetObject("winmgmts:" & _
    "{impersonationLevel=Impersonate}!\\" & strComputer & "\root\cimv2")

Set colStoppedServices = objWMIService.ExecQuery _
  ("SELECT * FROM Win32_Service ")
WScript.sleep 1000'休眠1秒

objNewFile.WriteLine("<serviceinfostar>")
WScript.sleep 1000'休眠5秒
For Each objService in colStoppedServices
    objNewfile.WriteLine "<serviceinfo>"
    objNewFile.WriteLine "<DisplayName>"&objService.DisplayName &"</DisplayName>" '服务名称
    objNewFile.WriteLine "<State>"& objService.State &"</State>"'服务状态
    objNewFile.WriteLine "<ServiceType>"& objService.ServiceType&"</ServiceType>"'服务类型
    objNewFile.WriteLine "<Description>"& objService.Description&"</Description>"'描述
    objNewFile.WriteLine "<PathName>"& objService.PathName&"</PathName>"'路径
    objNewFile.WriteLine "<StartMode>"& objService.StartMode&"</StartMode>"'启动方法分手动,自动.等
	objNewfile.WriteLine "</serviceinfo>"
Next
objNewFile.WriteLine "</serviceinfostar>"
'关闭写文件对象
objNewFile.WriteLine ""
objNewFile.WriteLine ""
WScript.sleep 2000'休眠1秒

 '----------------------------------------------------------------------------
  '进程监控并身成文件的模块
  '----------------------------------------------------------------------------

 '创建TXT文件
 strComputer="."
  Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
  Set colProcesses = objWMIService.ExecQuery _
    ("Select * from Win32_process")
 
  objNewFile.WriteLine "<processinfostart>"
  For Each objProcess in colProcesses
    'sngProcessTime = ( CSng(objProcess.KernelModeTime) + CSng(objProcess.UserModeTime)) / 10000000'使用cpu的情况,目前还不是很准确
    objNewFile.WriteLine "<processinfo>"
    objNewFile.WriteLine "<Caption>"&objProcess.Caption&"</Caption>" '进程名称
    objNewFile.WriteLine "<CreationDate>"&objProcess.CreationDate&"</CreationDate>"
    objNewFile.WriteLine "<Description>"&objProcess.Description &"</Description>"    '描述
	objNewFile.WriteLine "<ExecutablePath>"&objProcess.ExecutablePath &"</ExecutablePath>"  '程序的路径	
	objNewFile.WriteLine "<Name>"&objProcess.Name & "</Name>"'进程的名称(运行的程序名如qq.exe)
	objNewFile.WriteLine "<WorkingSetSize>"&objProcess.WorkingSetSize&"</WorkingSetSize>" '内存的大小
	objNewFile.WriteLine "<Priority>"&objProcess.Priority &"</Priority>"
	objNewFile.WriteLine "<ProcessId>"&objProcess.ProcessId &"</ProcessId>"   '进程的id
	objNewFile.WriteLine "<ThreadCount>"&objProcess.ThreadCount &"</ThreadCount>" '进程总共启用了几个线程
	objNewFile.WriteLine "<KernelModeTime>"&objProcess.KernelModeTime  &"</KernelModeTime>" 'cpu使用率
	objNewFile.WriteLine "<HandleCount>"&objProcess.HandleCount  &"</HandleCount>" '句柄数
    objNewFile.WriteLine "<UserModeTime>"&objProcess.UserModeTime&"</UserModeTime>" 'cpu使用率
    'objNewFile.WriteLine "<usecpu>"&sngProcessTime  &"</usecpu>" 'cpu使用率
	objNewFile.WriteLine "</processinfo>"
   
  Next
  objNewFile.WriteLine "</processinfostart>"

 objNewFile.WriteLine ""
 objNewFile.WriteLine ""


  WScript.sleep 2000'休眠1秒


'---------------------------------------------
'脚本用来监控硬盘空间的使用情况
'---------------------------------------------
strComputer="."
objNewFile.WriteLine "<diskinfostart>" '打印开始标记

HARD_DISK = 3
Set colDisks = objWMIService.ExecQuery _
    ("Select * from Win32_LogicalDisk Where DriveType = " & HARD_DISK & "")
	WScript.sleep 2000'休眠1秒
  For Each objDisk in colDisks
     objNewFile.WriteLine "<diskinfo>" 
   
     objNewFile.WriteLine "<Caption>" & objDisk.Caption &"</Caption>" '驱动器名称
     objNewFile.WriteLine "<VolumeName>" & objDisk.VolumeName &"</VolumeName>" '磁盘别名
     objNewFile.WriteLine "<FileSystem>" & objDisk.FileSystem & "</FileSystem>"
     objNewFile.WriteLine "<FreeSpace>" & objDisk.FreeSpace &"</FreeSpace>"
     objNewFile.WriteLine "<Size>" & objDisk.Size &"</Size>"
	 objNewFile.WriteLine "</diskinfo>" 

   Next
  objNewFile.WriteLine "</diskinfostart>"  '打印结束标记
  objNewFile.WriteLine ""
  objNewFile.WriteLine ""

  WScript.sleep 2000'休眠1秒


'------------------------------------
'cpu配置信息信息脚本
'------------------------------------
strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_Processor")
objNewFile.WriteLine "<cpuconfigstart>"
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<cpuconfiginfo>"
    
     objNewFile.WriteLine "<DataWidth>" & objStartupCommand.DataWidth &"</DataWidth>" 'cpu位数32位64位
     objNewFile.WriteLine "<LoadPercentage>" & objStartupCommand.LoadPercentage &"</LoadPercentage>" 'cpu 使用率（双核评价使用率）
     objNewFile.WriteLine "<ProcessorId>" & objStartupCommand.ProcessorId &"</ProcessorId>"'cpuid
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'cpu名称
	 objNewFile.WriteLine "<L2CacheSize>" & objStartupCommand.L2CacheSize &"</L2CacheSize>"'二级缓存大小
	 objNewFile.WriteLine "<L2CacheSpeed>" & objStartupCommand.L2CacheSpeed &"</L2CacheSpeed>"'cpu主频
	 objNewFile.WriteLine "</cpuconfiginfo>"
Next
objNewFile.WriteLine "</cpuconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒


'------------------------------------
'cpu性能指标信息
'------------------------------------
strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfOS_Processor where Name != '_Total'")

objNewFile.WriteLine "<OSProcessorstart>"
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<OLDOSProcessorsinfo>"
     objNewFile.WriteLine "<InterruptsPersec>" & objStartupCommand.InterruptsPersec &"</InterruptsPersec>" 
     objNewFile.WriteLine "<PercentProcessorTime>" & objStartupCommand.PercentProcessorTime &"</PercentProcessorTime>"
     objNewFile.WriteLine "<Timestamp_Sys100NS>" & objStartupCommand.Timestamp_Sys100NS &"</Timestamp_Sys100NS>"
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'cpu名称
	 objNewFile.WriteLine "</OLDOSProcessorsinfo>"
Next
objNewFile.WriteLine "</OSProcessorstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 1000'休眠1秒


'-----------------------------------
'内存性能信息脚本
'------------------------------------


strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfFormattedData_PerfOS_Memory")

objNewFile.WriteLine "<PerfOSMemorystart>"
WScript.sleep 1000'休眠1秒
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<OSMemorystartinfo>"
    
     objNewFile.WriteLine "<AvailableBytes>" & objStartupCommand.AvailableBytes &"</AvailableBytes>" '
     objNewFile.WriteLine "<CommittedBytes>" & objStartupCommand.CommittedBytes &"</CommittedBytes>" '
     objNewFile.WriteLine "<PageFaultsPersec>" & objStartupCommand.PageFaultsPersec &"</PageFaultsPersec>"'
     objNewFile.WriteLine "<PagesInputPersec>" & objStartupCommand.PagesInputPersec &"</PagesInputPersec>"'
	 objNewFile.WriteLine "<PagesOutputPersec>" & objStartupCommand.PagesOutputPersec &"</PagesOutputPersec>"'
	 objNewFile.WriteLine "<PagesPersec>" & objStartupCommand.PagesPersec &"</PagesPersec>"'
	 objNewFile.WriteLine "</OSMemorystartinfo>"
Next
objNewFile.WriteLine "</PerfOSMemorystart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒


'-----------------------------
'硬盘性能信息脚本
'-----------------------------

strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfDisk_PhysicalDisk where Name != '_Total'")

objNewFile.WriteLine "<PhysicalDiskstart>"

For Each objStartupCommand in colStartupCommands
    'Wscript.Echo "测试"
     objNewFile.WriteLine "<PhysicalDiskinfo>"
     objNewFile.WriteLine "<AvgDiskQueueLength>" & objStartupCommand.AvgDiskQueueLength &"</AvgDiskQueueLength>" '
     objNewFile.WriteLine "<AvgDiskReadQueueLength>" & objStartupCommand.AvgDiskReadQueueLength &"</AvgDiskReadQueueLength>" '
     objNewFile.WriteLine "<AvgDiskWriteQueueLength>" & objStartupCommand.AvgDiskWriteQueueLength &"</AvgDiskWriteQueueLength>"'
     objNewFile.WriteLine "<CurrentDiskQueueLength>" & objStartupCommand.CurrentDiskQueueLength &"</CurrentDiskQueueLength>"'
	 objNewFile.WriteLine "<DiskWriteBytesPersec>" & objStartupCommand.DiskWriteBytesPersec &"</DiskWriteBytesPersec>"'
     objNewFile.WriteLine "<DiskReadBytesPersec>" & objStartupCommand.DiskReadBytesPersec &"</DiskReadBytesPersec>"'
     'objNewFile.WriteLine "<TimeStamp_Sys100NS>" & objStartupCommand.TimeStamp_Sys100NS &"</TimeStamp_Sys100NS>"'
     objNewFile.WriteLine "<PercentDiskTime_Base>" & objStartupCommand.PercentDiskTime_Base &"</PercentDiskTime_Base>"'
	 objNewFile.WriteLine "<PercentDiskTime>" & objStartupCommand.PercentDiskTime &"</PercentDiskTime>"'
	 objNewFile.WriteLine "</PhysicalDiskinfo>"
Next
objNewFile.WriteLine "</PhysicalDiskstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒


'------------------------------------
'硬盘分区性能信息
'------------------------------------
strComputer="."
objNewFile.WriteLine "<LogicalDiskstart>" '打印开始标记

HARD_DISK = 3
Set colDisks = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfDisk_LogicalDisk Where Name != '_Total'" )
  For Each objDisk in colDisks
     objNewFile.WriteLine "<LogicalDiskinfo>" 
     objNewFile.WriteLine "<Name>"&objDisk.Name &"</Name>" '分区编号
     objNewFile.WriteLine "<AvgDiskQueueLength>" & objDisk.AvgDiskQueueLength &"</AvgDiskQueueLength>" 
     objNewFile.WriteLine "<AvgDiskReadQueueLength>" & objDisk.AvgDiskReadQueueLength & "</AvgDiskReadQueueLength>"
     objNewFile.WriteLine "<AvgDiskWriteQueueLength>" & objDisk.AvgDiskWriteQueueLength &"</AvgDiskWriteQueueLength>"
     objNewFile.WriteLine "<DiskWriteBytesPersec>" & objDisk.DiskWriteBytesPersec &"</DiskWriteBytesPersec>"
     objNewFile.WriteLine "<AvgDisksecPerWrite>" & objDisk.AvgDisksecPerWrite &"</AvgDisksecPerWrite>"
     objNewFile.WriteLine "<AvgDisksecPerRead>" & objDisk.AvgDisksecPerRead &"</AvgDisksecPerRead>"
	 objNewFile.WriteLine "<DiskReadBytesPersec>" & objDisk.DiskReadBytesPersec &"</DiskReadBytesPersec>"'
	 objNewFile.WriteLine "<PercentDiskTime>" & objDisk.PercentDiskTime &"</PercentDiskTime>"
     objNewFile.WriteLine "<TimeStamp_Sys100NS>" & objDisk.TimeStamp_Sys100NS &"</TimeStamp_Sys100NS>"'
	 objNewFile.WriteLine "<PercentDiskTime_Base>" & objDisk.PercentDiskTime_Base &"</PercentDiskTime_Base>"'
	 objNewFile.WriteLine "</LogicalDiskinfo>" 
   Next
  objNewFile.WriteLine "</LogicalDiskstart>" 
  objNewFile.WriteLine ""
  objNewFile.WriteLine ""

  WScript.sleep 2000'休眠1秒

'------------------------------
'网卡性能信息脚本
'------------------------------

strComputer = "."
Set objArgs = wscript.Arguments
strComputer = objArgs.item(0)
Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
wqlQuery = "select * from Win32_PerfRawData_Tcpip_NetworkInterface" 
Set colItems = objWMIService.ExecQuery(wqlQuery ,,48)

objNewFile.WriteLine "<NetworkInterfacestart>"
For Each objStartupCommand in colItems
     objNewFile.WriteLine "<NetworkInterfaceinfo>"
     objNewFile.WriteLine "<BytesReceivedPersec>" & objStartupCommand.BytesReceivedPersec &"</BytesReceivedPersec>" '每秒接受的字节数
	  'Wscript.Echo "BytesReceivedPersec=" & objStartupCommand.BytesReceivedPersec 
     objNewFile.WriteLine "<BytesSentPersec>" & objStartupCommand.BytesSentPersec &"</BytesSentPersec>" '每秒发送的字节数
	 'Wscript.Echo "BytesSentPersec=" & objStartupCommand.BytesSentPersec 
     objNewFile.WriteLine "<BytesTotalPersec>" & objStartupCommand.BytesTotalPersec &"</BytesTotalPersec>"'每秒总字节数
	  'Wscript.Echo "BytesTotalPersec=" & objStartupCommand.BytesTotalPersec 
     objNewFile.WriteLine "<CurrentBandwidth>" & objStartupCommand.CurrentBandwidth &"</CurrentBandwidth>"'带宽
	 objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'网卡名称
	 objNewFile.WriteLine "<OutputQueueLength>" & objStartupCommand.OutputQueueLength &"</OutputQueueLength>"'输出队列长度
	 objNewFile.WriteLine "<PacketsOutboundErrors>" & objStartupCommand.PacketsOutboundErrors &"</PacketsOutboundErrors>"'
	 objNewFile.WriteLine "<PacketsPersec>" & objStartupCommand.PacketsPersec &"</PacketsPersec>"'
	 objNewFile.WriteLine "<PacketsReceivedDiscarded>" & objStartupCommand.PacketsReceivedDiscarded &"</PacketsReceivedDiscarded>"'
	 objNewFile.WriteLine "<PacketsReceivedErrors>" & objStartupCommand.PacketsReceivedErrors &"</PacketsReceivedErrors>"'
	 objNewFile.WriteLine "<PacketsReceivedNonUnicastPersec>" & objStartupCommand.PacketsReceivedNonUnicastPersec &"</PacketsReceivedNonUnicastPersec>"'
	 objNewFile.WriteLine "<PacketsReceivedPersec>" & objStartupCommand.PacketsReceivedPersec &"</PacketsReceivedPersec>"'
	 objNewFile.WriteLine "<PacketsReceivedUnicastPersec>" & objStartupCommand.PacketsReceivedUnicastPersec &"</PacketsReceivedUnicastPersec>"'
	 objNewFile.WriteLine "<PacketsReceivedUnknown>" & objStartupCommand.PacketsReceivedUnknown &"</PacketsReceivedUnknown>"'
	 objNewFile.WriteLine "<PacketsSentNonUnicastPersec>" & objStartupCommand.PacketsSentNonUnicastPersec &"</PacketsSentNonUnicastPersec>"'
	 objNewFile.WriteLine "<PacketsSentPersec>" & objStartupCommand.PacketsSentPersec &"</PacketsSentPersec>"'
	 objNewFile.WriteLine "<PacketsSentUnicastPersec>" & objStartupCommand.PacketsSentUnicastPersec &"</PacketsSentUnicastPersec>"'
	 objNewFile.WriteLine "</NetworkInterfaceinfo>"
Next
objNewFile.WriteLine "</NetworkInterfacestart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒



'---------------------------------
'内存利用率采集
'---------------------------------
 

strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_OperatingSystem")

objNewFile.WriteLine "<SystemMemorystart>"

For Each objStartupCommand in colStartupCommands

    
     objNewFile.WriteLine "<SystemMemoryinfo>"
     objNewFile.WriteLine "<FreePhysicalMemory>" & objStartupCommand.FreePhysicalMemory &"</FreePhysicalMemory>"'
     objNewFile.WriteLine "<TotalVisibleMemorySize>" & objStartupCommand.TotalVisibleMemorySize &"</TotalVisibleMemorySize>" '物理总内存
     objNewFile.WriteLine "<FreeVirtualMemory>" & objStartupCommand.FreeVirtualMemory &"</FreeVirtualMemory>" '空闲虚拟内存
     objNewFile.WriteLine "<TotalVirtualMemorySize>" & objStartupCommand.TotalVirtualMemorySize &"</TotalVirtualMemorySize>" '虚拟总内存
	 objNewFile.WriteLine "</SystemMemoryinfo>"
Next
objNewFile.WriteLine "</SystemMemorystart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'休眠1秒




'----------------------------------
'系统性能信息脚本
'----------------------------------


strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfOS_System")

objNewFile.WriteLine "<OSSystemstart>"

For Each objStartupCommand in colStartupCommands

     objNewFile.WriteLine "<OSSysteminfo>"
     objNewFile.WriteLine "<ContextSwitchesPersec>" & objStartupCommand.ContextSwitchesPersec &"</ContextSwitchesPersec>"'ip地址
     objNewFile.WriteLine "<ProcessorQueueLength>" & objStartupCommand.ProcessorQueueLength &"</ProcessorQueueLength>" '网卡名称
     objNewFile.WriteLine "<SystemCallsPersec>" & objStartupCommand.SystemCallsPersec&"</SystemCallsPersec>" '索引
	 objNewFile.WriteLine "</OSSysteminfo>"
Next
objNewFile.WriteLine "</OSSystemstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

'----------------------------------
'网卡连接状态信息
'----------------------------------
strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_NetworkAdapter where MACAddress!=null and NetConnectionStatus!=null")

objNewFile.WriteLine "<NetworkStatusstart>"

For Each objStartupCommand in colStartupCommands

     objNewFile.WriteLine "<NetworkStatusinfo>"
     objNewFile.WriteLine "<Index>"&objStartupCommand.Index &"</Index>"'网卡索引
     objNewFile.WriteLine "<MACAddress>"&objStartupCommand.MACAddress &"</MACAddress>" '网卡mac地址
     objNewFile.WriteLine "<Name>"&objStartupCommand.Name&"</Name>" '网卡名称
	 objNewFile.WriteLine "<NetConnectionStatus>"&objStartupCommand.NetConnectionStatus&"</NetConnectionStatus>" '网卡连接状态
	 objNewFile.WriteLine "</NetworkStatusinfo>"
Next
objNewFile.WriteLine "</NetworkStatusstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

'performancedtime=Time

WScript.sleep 2000'休眠1秒
End If






  '=============================结束性能数据采集======================

objNewFile.WriteLine "</host>"

objNewFile.Close


'--------------------上传文件---------------------------------
 
'需要加判断，如果没有采集不用把文件上传

 If (performanceflg = True and infoflg= True and configflg = True ) Then 
 Wscript.Echo "上传文件"
 Call ftp("up.txt",hostfile,"put",ftppath,ftpname,ftppassowd,ftpServerHost)
 End If
 
'-------------------end 上传文件---------------------------------

configflg=False
infoflg=False
performanceflg=false


Set objNewFile = Nothing
Set objWMIService=Nothing
Set colStartupCommands=Nothing

'---------------------计算各个采集脚本是否运行---------------------------------
performanceflg = equalMtime(performancet) 
infoflg =  equalMtime(info)
configflg=  equalMtime(config)

 WScript.sleep 30000'休眠30秒
'Wscript.Echo performanceflg
''Wscript.Echo infoflg
''Wscript.Echo configflg
'Wscript.Echo "一次"
Loop