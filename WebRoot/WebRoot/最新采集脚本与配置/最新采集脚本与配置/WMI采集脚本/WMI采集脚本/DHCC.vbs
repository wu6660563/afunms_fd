On Error Resume Next



'�ű���������==================================================
'---------------���ü��������ip��ַ--------------------
 Hostaddress="10.10.152.57"  '�������ɵ�����xml

 ftpname="nms"  'ftp ���û�
 ftppassowd="webnms" 'ftp������
 ftpServerHost="10.10.152.57"  'ftp �����ip��ַ
 ftppath="/"  'ftp �ϴ���·����Ĭ��Ϊ��Ŀ¼/ 




'=======================================================
'---------------end ���ü��������ip��ַ----------------











'���弸�������������Ƹ����ɼ��ű��Ƿ�����(���Ʋ���1��ʾ������,0��ʾ����)
Dim configflg      '����������Ϣ�Ƿ�ɼ�
Dim infoflg        '���ƻ�����Ϣ�Ƿ�ɼ�
Dim performanceflg '�������������Ƿ�ɼ�
'------------------------------------------------------------------------------
'�ɼ��ű����в���,��Ԥ�輸�����еĹ���,�Ժ���԰ɴӷ�������ȡ�������ļ����ñ�����
'-------------------------------------------------------------------------------

Dim config '������Ϣ�ɼ�ʱ����
Dim info  '������Ϣ�ɼ�ʱ����
Dim performancet'������Ϣ�ɼ�ʱ����


'���������ϴ�����ʱ��
Dim configtime
Dim infotime
Dim performancedtime



'�������÷������ж�ʱ�����Ĳɼ� ��60�����ڣ�
Function equalMtime(Mtime)
  Dim flg
  flg=false
   'Wscript.Echo "�ж��Ƿ�����ʱ��"
  If (Minute(Now) Mod CInt(Mtime) =0 or Minute(Now)= CInt("0")  ) Then
   flg=true
  End If
 equalMtime=flg
End Function


'���������ж���СʱΪ����Ĳɼ� ��24Сʱ�ڣ�
Function equalHtime(Mtime)
  Dim flg
  flg=false
   'Wscript.Echo "�ж��Ƿ�����ʱ��"
  If (Hour(Now) Mod CInt(Mtime) =0 and Minute(Now)= CInt("0")  ) Then
   flg=true
  End If
 equalHtime=flg
End Function



 
first=true '����һ���������ж��Ƿ��ǵ�һ������(true,��ʾ��һ������,false,��ʾ���ж��)
xml="<?xml version=""1.0"" encoding=""GBK""?>"

strComputer = "."


'-------------------------------------------------------
'-------------------------------------------------------

'macaddress=""  '��ַ
'Set objWMIService = GetObject("winmgmts:\\"& strComputer & "\root\cimv2")
'Set colAdapters = objWMIService.ExecQuery _
 ' ("SELECT * FROM Win32_NetworkAdapterConfiguration WHERE IPEnabled = True ")
'n = 1
'For Each objAdapter in colAdapters
    'macaddress=objAdapter.IPAddress(0)
	''macaddress="10.10.152.27"  '��ַ
'Next
'---------------ȡ��mac��ַ--------------------


'**************************************
'*����ʱ�����жϽű��Ƿ�ʱ������
'*runtime �ϴ����е�ʱ��
'*newtime ����ϵͳ��ʱ��
'*runvalue ����Ƶ��(�Է���Ϊ׼)
'**************************************

'Function equaltime(runtime,newtime, runvalue)
  'equaltime=false
   'Wscript.Echo "equaltime="& equaltime
  ' If runvalue =-1 Then '�����-1��ʾ�ýű�����ִ��
  ' equaltime=false
   'End If
   
   '����Ǵ��ڻ�����ϴ�����ʱ��,���ʾ�����дνű�(�����Ӽ���)
  ' If runvalue =>0 Then
  ' If ( Abs(DateDiff("s",runtime,newtime))>=runvalue*60) Then 
  ' equaltime=True
   'Wscript.Echo "equaltime"&equaltime
  ' End If
   'End If
   'Wscript.Echo "equaltime2="& equaltime  
'End Function

'*******************************
'ɾ���ļ�
'*******************************
Function deletefile(filename)
 on error resume next 
  Dim fso, f2
   Set fso = CreateObject("Scripting.FileSystemObject")
   '����ļ���ǰλ�õľ����
   Set f2 = fso.GetFile(filename)
   'ɾ���ļ���
   f2.Delete
End Function


'***************************************
'��������Ĵ�С
'***************************************
'Function arraycount(MyArray())

  ' i=0 
	'for each X in MyArray
	'i=i+1 
	'next
'arraycount=i
'End Function
'*******************************
'*��mac��ַ�ַ����зָ�ϲ�
'*macaddress mac��ַ
'*******************************
Function mac(macaddress)
on error resume next 
Dim a
MyArray = Split(macaddress, ":", -1, 1)
For i = Lbound(MyArray) To Ubound(MyArray)
'pathstring=pathstring+ReturnArray(i)&"\"
a=a+ MyArray(i) 'ͨ��������Ϣ��ѭ����ʾ����ֵ 
Next
'Wscript.Echo a
mac=a
End Function


'**********************************
'createfile ���ɵ��ļ��� 
'filename �ļ�������
'ftptype  ftp�������������������,put �ϴ�,get ����
'filepath �ϴ��ļ��������ļ���Ŀ¼
'username ftp���û�
'password ftp������
'host ftp�������ĵ�ַ
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
'ftp�ϴ����غ���
'createfile ���ɵ��ļ��� 
'filename �ļ�������
'ftptype  ftp�������������������,put �ϴ�,get ����
'filepath �ϴ��ļ��������ļ���Ŀ¼
'username ftp���û�
'password ftp������
'host ftp�������ĵ�ַ
'**********************************


Function ftp(createfile,filename,ftptype,filepath,username,password,host)
dim obj
set obj=createobject("wscript.shell")

 Call ftpfile(createfile,filename,ftptype,filepath,username,password,host)
 obj.run "command /c ftp -s:"&createfile ,0,True
 WScript.sleep 5000'����1��
Call deletefile(createfile)
'��������ز�ɾ�����ص��ļ�
If ftptype="put" Then 
'Wscript.Echo "put"
'Call deletefile(filename)
End If
End Function

'**********************************************
'*  ѭ��ִ�вɼ��ű�
'**********************************************


Do While True 'ʹ����ѭ����ʵ���߳�
  
   '����ϵͳ�ǲɼ����в��������ÿ����еĲ���,
    '--------------------------------------
    ' a=Time
    'WScript.sleep 66000
     'b=Time
    ' c=Abs(DateDiff("s",b,a))'�˷��������ж�����ʱ���ڵ�ʱ���(��)
        
       If first Then     
		 '��һ��������ƿ��Ʋ�����������
		 configflg=true  
		 infoflg=true  
         performanceflg=true 
         
       
         config=5 '������Ϣ3Сʱ�ɼ�һ��
         info =5'������Ϣ�ɼ�1Сʱ
         performancet=5'������Ϣ�ɼ�5���Ӳɼ�һ��

         first=false
       End If
	'--------------------------------------

 
  '��ʼ���������Ĳɼ��ļ�
  Set objFS = CreateObject("Scripting.FileSystemObject")
  hostfile=Hostaddress&".xml"
  Set objNewFile = objFS.CreateTextFile(hostfile)
  objNewFile.WriteLine xml
  objNewFile.WriteLine "<host>"


 '********************************************
 '���нű����������ļ�
 '********************************************



 '============================������Ϣ���ݲɼ�===========================

'---------------------------------
'Ӳ��������Ϣ��Ϣ�ű�
'---------------------------------
If configflg Then 

strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_DiskDrive")

objNewFile.WriteLine "<diskconfigstart>"
WScript.sleep 1000'����1��
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<diskconfiginfo>"
    
     objNewFile.WriteLine "<BytesPerSector>" & objStartupCommand.BytesPerSector &"</BytesPerSector>" 'Ӳ�̶�������
     objNewFile.WriteLine "<Caption>" & objStartupCommand.Caption &"</Caption>" 'Ӳ������
     objNewFile.WriteLine "<Index>" & objStartupCommand.Index &"</Index>"'����
     objNewFile.WriteLine "<InterfaceType>" & objStartupCommand.InterfaceType &"</InterfaceType>"'�ӿ�����
	 objNewFile.WriteLine "<Size>" & objStartupCommand.Size &"</Size>"'Ӳ�̴�С
	
	 objNewFile.WriteLine "</diskconfiginfo>"
Next
objNewFile.WriteLine "</diskconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'����1��





'---------------------------------
'����������Ϣ�ű�
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
     objNewFile.WriteLine "<IPAddress>" & trIPAddress &"</IPAddress>"'ip��ַ
     objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>" '��������
     objNewFile.WriteLine "<Index>" & objStartupCommand.Index&"</Index>" '����
     trIPSubnet = Join(objStartupCommand.IPSubnet, ",")
     objNewFile.WriteLine "<IPSubnet>" & trIPSubnet &"</IPSubnet>"'��������
	 objNewFile.WriteLine "<MACAddress>" & objStartupCommand.MACAddress &"</MACAddress>"'��������
     trDNSServerSearchOrder = Join(objStartupCommand.DNSServerSearchOrder, ",")
	 objNewFile.WriteLine "<DNSServerSearchOrder>" & trDNSServerSearchOrder &"</DNSServerSearchOrder>"'dns
     trDefaultIPGateway = Join(objStartupCommand.DefaultIPGateway, ",")
	 objNewFile.WriteLine "<DefaultIPGateway>" & trDefaultIPGateway &"</DefaultIPGateway >"'����
	 objNewFile.WriteLine "</Networkconfiginfo>"
Next
objNewFile.WriteLine "</Networkconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""


'configtime=Time
End If


'=============================���û�����Ϣ���ݲɼ�======================




'============================������Ϣ���ݲɼ�=======================

  If infoflg  Then   '�ж�������Ϣʱ��Ҫ�ɼ�

  WScript.sleep 1000'����5��
  strComputer="."
  
    objNewFile.WriteLine "<hostinfostart>"  '��ӡ��ʼ���
    objNewFile.WriteLine "<hostinfo>" 
	Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
	Set colItems = objWMIService.ExecQuery("Select Manufacturer,TotalPhysicalMemory,Caption,NumberOfProcessors from Win32_ComputerSystem")
	For Each objItem in colItems
       objNewFile.WriteLine "<TotalPhysicalMemory>" & objItem.TotalPhysicalMemory &"</TotalPhysicalMemory>"
       objNewFile.WriteLine "<Hostname>" & objItem.Caption &"</Hostname>" '�������
       'objNewFile.WriteLine "<Manufacturer>" & objItem.Caption &"</Manufacturer>" '����
       objNewFile.WriteLine "<NumberOfProcessors>" & objItem.NumberOfProcessors &"</NumberOfProcessors>" 'cpu����
	Next

   '����ϵͳ
    WScript.sleep 500'����5��
	
    Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
    Set colOperatingSystems = objWMIService.ExecQuery ("Select * from Win32_OperatingSystem")
     For Each objOperatingSystem in colOperatingSystems
         objNewFile.WriteLine "<Sysname>" & objOperatingSystem.Caption &"</Sysname>" '����ϵͳ����
		 objNewFile.WriteLine "<SerialNumber>" & objOperatingSystem.SerialNumber  &"</SerialNumber>" '����
		 'objNewFile.WriteLine "<LastBootUpTime>" & objOperatingSystem.LastBootUpTime &"</LastBootUpTime>" '�������ʱ��
		 'objNewFile.WriteLine "<LocalDateTime>" & objOperatingSystem.LocalDateTime  &"</LocalDateTime>" '������������ǰʱ��
		 objNewFile.WriteLine "<CSDVersion>" & objOperatingSystem.CSDVersion &"</CSDVersion>" 'ϵͳ�İ汾��
         
       Next

          WScript.sleep 500'����5��
    Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
    Set colOperatingSystems = objWMIService.ExecQuery("Select * from Win32_Processor")
     For Each objOperatingSystem in colOperatingSystems
         objNewFile.WriteLine "<CPUname>" & objOperatingSystem.Name & (objOperatingSystem.CurrentClockSpeed)/1000 &" GHz""</CPUname>" '����ϵͳ����
		 objNewFile.WriteLine "<SerialNumber >" & objOperatingSystem.SerialNumber  &"</SerialNumber >" '����
       Next
       objNewFile.WriteLine "</hostinfo>" 

      objNewFile.WriteLine "</hostinfostart>" 
     objNewFile.WriteLine ""
	 objNewFile.WriteLine ""
	  
	  WScript.sleep 2000'����5��
 


'--------------------------------
'�û���Ϣ��Ϣ�ű�
'--------------------------------

strComputer="."
Set objWMIService = GetObject("winmgmts:"& "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_UserAccount")

objNewFile.WriteLine "<UserAccountinfostart>"
''WScript.sleep 1000'����1��
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<UserAccountinfo>"
    
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>" '����
     objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>" '����
     objNewFile.WriteLine "<Domain>" & objStartupCommand.Domain &"</Domain>"'������
     objNewFile.WriteLine "<LocalAccount>" & objStartupCommand.LocalAccount  &"</LocalAccount>"'�Ƿ񱾵��ʻ�
	 objNewFile.WriteLine "<Status>" & objStartupCommand.Status&"</Status>"'״̬
	 objNewFile.WriteLine "</UserAccountinfo>"
Next
objNewFile.WriteLine "</UserAccountinfostart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""




 
'--------------------------------
'��������Ϣ�ű�
'--------------------------------

'strComputer="."
'Set objWMIService = GetObject("winmgmts:" _
 '   & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
'Set colStartupCommands = objWMIService.ExecQuery _
 '   ("Select * from Win32_StartupCommand")

'objNewFile.WriteLine "<startupinfostart>"
''WScript.sleep 1000'����1��
'For Each objStartupCommand in colStartupCommands
   '  objNewFile.WriteLine "<startupinfo>"
    
     'objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>" '����
     'objNewFile.WriteLine "<Command>" & objStartupCommand.Command &"</Command>" '������
     'objNewFile.WriteLine "<Description>" & objStartupCommand.Description &"</Description>"'����
     'objNewFile.WriteLine "<User>" & objStartupCommand.User &"</User>"'�û�
	 'objNewFile.WriteLine "</startupinfo>"
'Next
'objNewFile.WriteLine "</startupinfostart>"
'objNewFile.WriteLine ""
'objNewFile.WriteLine ""




'infotime=Time '�����������ǰʱ��
WScript.sleep 2000'����1��
End If

'=============================����������Ϣ���ݲɼ�======================



'============================�������ݲɼ�===========================

'----------------------------------
'��ط�����Ϣ״̬
'----------------------------------
If performanceflg Then
  
Set objWMIService = GetObject("winmgmts:" & _
    "{impersonationLevel=Impersonate}!\\" & strComputer & "\root\cimv2")

Set colStoppedServices = objWMIService.ExecQuery _
  ("SELECT * FROM Win32_Service ")
WScript.sleep 1000'����1��

objNewFile.WriteLine("<serviceinfostar>")
WScript.sleep 1000'����5��
For Each objService in colStoppedServices
    objNewfile.WriteLine "<serviceinfo>"
    objNewFile.WriteLine "<DisplayName>"&objService.DisplayName &"</DisplayName>" '��������
    objNewFile.WriteLine "<State>"& objService.State &"</State>"'����״̬
    objNewFile.WriteLine "<ServiceType>"& objService.ServiceType&"</ServiceType>"'��������
    objNewFile.WriteLine "<Description>"& objService.Description&"</Description>"'����
    objNewFile.WriteLine "<PathName>"& objService.PathName&"</PathName>"'·��
    objNewFile.WriteLine "<StartMode>"& objService.StartMode&"</StartMode>"'�����������ֶ�,�Զ�.��
	objNewfile.WriteLine "</serviceinfo>"
Next
objNewFile.WriteLine "</serviceinfostar>"
'�ر�д�ļ�����
objNewFile.WriteLine ""
objNewFile.WriteLine ""
WScript.sleep 2000'����1��

 '----------------------------------------------------------------------------
  '���̼�ز�����ļ���ģ��
  '----------------------------------------------------------------------------

 '����TXT�ļ�
 strComputer="."
  Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
  Set colProcesses = objWMIService.ExecQuery _
    ("Select * from Win32_process")
 
  objNewFile.WriteLine "<processinfostart>"
  For Each objProcess in colProcesses
    'sngProcessTime = ( CSng(objProcess.KernelModeTime) + CSng(objProcess.UserModeTime)) / 10000000'ʹ��cpu�����,Ŀǰ�����Ǻ�׼ȷ
    objNewFile.WriteLine "<processinfo>"
    objNewFile.WriteLine "<Caption>"&objProcess.Caption&"</Caption>" '��������
    objNewFile.WriteLine "<CreationDate>"&objProcess.CreationDate&"</CreationDate>"
    objNewFile.WriteLine "<Description>"&objProcess.Description &"</Description>"    '����
	objNewFile.WriteLine "<ExecutablePath>"&objProcess.ExecutablePath &"</ExecutablePath>"  '�����·��	
	objNewFile.WriteLine "<Name>"&objProcess.Name & "</Name>"'���̵�����(���еĳ�������qq.exe)
	objNewFile.WriteLine "<WorkingSetSize>"&objProcess.WorkingSetSize&"</WorkingSetSize>" '�ڴ�Ĵ�С
	objNewFile.WriteLine "<Priority>"&objProcess.Priority &"</Priority>"
	objNewFile.WriteLine "<ProcessId>"&objProcess.ProcessId &"</ProcessId>"   '���̵�id
	objNewFile.WriteLine "<ThreadCount>"&objProcess.ThreadCount &"</ThreadCount>" '�����ܹ������˼����߳�
	objNewFile.WriteLine "<KernelModeTime>"&objProcess.KernelModeTime  &"</KernelModeTime>" 'cpuʹ����
	objNewFile.WriteLine "<HandleCount>"&objProcess.HandleCount  &"</HandleCount>" '�����
    objNewFile.WriteLine "<UserModeTime>"&objProcess.UserModeTime&"</UserModeTime>" 'cpuʹ����
    'objNewFile.WriteLine "<usecpu>"&sngProcessTime  &"</usecpu>" 'cpuʹ����
	objNewFile.WriteLine "</processinfo>"
   
  Next
  objNewFile.WriteLine "</processinfostart>"

 objNewFile.WriteLine ""
 objNewFile.WriteLine ""


  WScript.sleep 2000'����1��


'---------------------------------------------
'�ű��������Ӳ�̿ռ��ʹ�����
'---------------------------------------------
strComputer="."
objNewFile.WriteLine "<diskinfostart>" '��ӡ��ʼ���

HARD_DISK = 3
Set colDisks = objWMIService.ExecQuery _
    ("Select * from Win32_LogicalDisk Where DriveType = " & HARD_DISK & "")
	WScript.sleep 2000'����1��
  For Each objDisk in colDisks
     objNewFile.WriteLine "<diskinfo>" 
   
     objNewFile.WriteLine "<Caption>" & objDisk.Caption &"</Caption>" '����������
     objNewFile.WriteLine "<VolumeName>" & objDisk.VolumeName &"</VolumeName>" '���̱���
     objNewFile.WriteLine "<FileSystem>" & objDisk.FileSystem & "</FileSystem>"
     objNewFile.WriteLine "<FreeSpace>" & objDisk.FreeSpace &"</FreeSpace>"
     objNewFile.WriteLine "<Size>" & objDisk.Size &"</Size>"
	 objNewFile.WriteLine "</diskinfo>" 

   Next
  objNewFile.WriteLine "</diskinfostart>"  '��ӡ�������
  objNewFile.WriteLine ""
  objNewFile.WriteLine ""

  WScript.sleep 2000'����1��


'------------------------------------
'cpu������Ϣ��Ϣ�ű�
'------------------------------------
strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_Processor")
objNewFile.WriteLine "<cpuconfigstart>"
For Each objStartupCommand in colStartupCommands
     objNewFile.WriteLine "<cpuconfiginfo>"
    
     objNewFile.WriteLine "<DataWidth>" & objStartupCommand.DataWidth &"</DataWidth>" 'cpuλ��32λ64λ
     objNewFile.WriteLine "<LoadPercentage>" & objStartupCommand.LoadPercentage &"</LoadPercentage>" 'cpu ʹ���ʣ�˫������ʹ���ʣ�
     objNewFile.WriteLine "<ProcessorId>" & objStartupCommand.ProcessorId &"</ProcessorId>"'cpuid
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'cpu����
	 objNewFile.WriteLine "<L2CacheSize>" & objStartupCommand.L2CacheSize &"</L2CacheSize>"'���������С
	 objNewFile.WriteLine "<L2CacheSpeed>" & objStartupCommand.L2CacheSpeed &"</L2CacheSpeed>"'cpu��Ƶ
	 objNewFile.WriteLine "</cpuconfiginfo>"
Next
objNewFile.WriteLine "</cpuconfigstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'����1��


'------------------------------------
'cpu����ָ����Ϣ
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
     objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'cpu����
	 objNewFile.WriteLine "</OLDOSProcessorsinfo>"
Next
objNewFile.WriteLine "</OSProcessorstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 1000'����1��


'-----------------------------------
'�ڴ�������Ϣ�ű�
'------------------------------------


strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfFormattedData_PerfOS_Memory")

objNewFile.WriteLine "<PerfOSMemorystart>"
WScript.sleep 1000'����1��
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

WScript.sleep 2000'����1��


'-----------------------------
'Ӳ��������Ϣ�ű�
'-----------------------------

strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfDisk_PhysicalDisk where Name != '_Total'")

objNewFile.WriteLine "<PhysicalDiskstart>"

For Each objStartupCommand in colStartupCommands
    'Wscript.Echo "����"
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

WScript.sleep 2000'����1��


'------------------------------------
'Ӳ�̷���������Ϣ
'------------------------------------
strComputer="."
objNewFile.WriteLine "<LogicalDiskstart>" '��ӡ��ʼ���

HARD_DISK = 3
Set colDisks = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfDisk_LogicalDisk Where Name != '_Total'" )
  For Each objDisk in colDisks
     objNewFile.WriteLine "<LogicalDiskinfo>" 
     objNewFile.WriteLine "<Name>"&objDisk.Name &"</Name>" '�������
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

  WScript.sleep 2000'����1��

'------------------------------
'����������Ϣ�ű�
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
     objNewFile.WriteLine "<BytesReceivedPersec>" & objStartupCommand.BytesReceivedPersec &"</BytesReceivedPersec>" 'ÿ����ܵ��ֽ���
	  'Wscript.Echo "BytesReceivedPersec=" & objStartupCommand.BytesReceivedPersec 
     objNewFile.WriteLine "<BytesSentPersec>" & objStartupCommand.BytesSentPersec &"</BytesSentPersec>" 'ÿ�뷢�͵��ֽ���
	 'Wscript.Echo "BytesSentPersec=" & objStartupCommand.BytesSentPersec 
     objNewFile.WriteLine "<BytesTotalPersec>" & objStartupCommand.BytesTotalPersec &"</BytesTotalPersec>"'ÿ�����ֽ���
	  'Wscript.Echo "BytesTotalPersec=" & objStartupCommand.BytesTotalPersec 
     objNewFile.WriteLine "<CurrentBandwidth>" & objStartupCommand.CurrentBandwidth &"</CurrentBandwidth>"'����
	 objNewFile.WriteLine "<Name>" & objStartupCommand.Name &"</Name>"'��������
	 objNewFile.WriteLine "<OutputQueueLength>" & objStartupCommand.OutputQueueLength &"</OutputQueueLength>"'������г���
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

WScript.sleep 2000'����1��



'---------------------------------
'�ڴ������ʲɼ�
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
     objNewFile.WriteLine "<TotalVisibleMemorySize>" & objStartupCommand.TotalVisibleMemorySize &"</TotalVisibleMemorySize>" '�������ڴ�
     objNewFile.WriteLine "<FreeVirtualMemory>" & objStartupCommand.FreeVirtualMemory &"</FreeVirtualMemory>" '���������ڴ�
     objNewFile.WriteLine "<TotalVirtualMemorySize>" & objStartupCommand.TotalVirtualMemorySize &"</TotalVirtualMemorySize>" '�������ڴ�
	 objNewFile.WriteLine "</SystemMemoryinfo>"
Next
objNewFile.WriteLine "</SystemMemorystart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

WScript.sleep 2000'����1��




'----------------------------------
'ϵͳ������Ϣ�ű�
'----------------------------------


strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_PerfRawData_PerfOS_System")

objNewFile.WriteLine "<OSSystemstart>"

For Each objStartupCommand in colStartupCommands

     objNewFile.WriteLine "<OSSysteminfo>"
     objNewFile.WriteLine "<ContextSwitchesPersec>" & objStartupCommand.ContextSwitchesPersec &"</ContextSwitchesPersec>"'ip��ַ
     objNewFile.WriteLine "<ProcessorQueueLength>" & objStartupCommand.ProcessorQueueLength &"</ProcessorQueueLength>" '��������
     objNewFile.WriteLine "<SystemCallsPersec>" & objStartupCommand.SystemCallsPersec&"</SystemCallsPersec>" '����
	 objNewFile.WriteLine "</OSSysteminfo>"
Next
objNewFile.WriteLine "</OSSystemstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

'----------------------------------
'��������״̬��Ϣ
'----------------------------------
strComputer="."
Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
Set colStartupCommands = objWMIService.ExecQuery _
    ("Select * from Win32_NetworkAdapter where MACAddress!=null and NetConnectionStatus!=null")

objNewFile.WriteLine "<NetworkStatusstart>"

For Each objStartupCommand in colStartupCommands

     objNewFile.WriteLine "<NetworkStatusinfo>"
     objNewFile.WriteLine "<Index>"&objStartupCommand.Index &"</Index>"'��������
     objNewFile.WriteLine "<MACAddress>"&objStartupCommand.MACAddress &"</MACAddress>" '����mac��ַ
     objNewFile.WriteLine "<Name>"&objStartupCommand.Name&"</Name>" '��������
	 objNewFile.WriteLine "<NetConnectionStatus>"&objStartupCommand.NetConnectionStatus&"</NetConnectionStatus>" '��������״̬
	 objNewFile.WriteLine "</NetworkStatusinfo>"
Next
objNewFile.WriteLine "</NetworkStatusstart>"
objNewFile.WriteLine ""
objNewFile.WriteLine ""

'performancedtime=Time

WScript.sleep 2000'����1��
End If






  '=============================�����������ݲɼ�======================

objNewFile.WriteLine "</host>"

objNewFile.Close


'--------------------�ϴ��ļ�---------------------------------
 
'��Ҫ���жϣ����û�вɼ����ð��ļ��ϴ�

 If (performanceflg = True and infoflg= True and configflg = True ) Then 
 Wscript.Echo "�ϴ��ļ�"
 Call ftp("up.txt",hostfile,"put",ftppath,ftpname,ftppassowd,ftpServerHost)
 End If
 
'-------------------end �ϴ��ļ�---------------------------------

configflg=False
infoflg=False
performanceflg=false


Set objNewFile = Nothing
Set objWMIService=Nothing
Set colStartupCommands=Nothing

'---------------------��������ɼ��ű��Ƿ�����---------------------------------
performanceflg = equalMtime(performancet) 
infoflg =  equalMtime(info)
configflg=  equalMtime(config)

 WScript.sleep 30000'����30��
'Wscript.Echo performanceflg
''Wscript.Echo infoflg
''Wscript.Echo configflg
'Wscript.Echo "һ��"
Loop