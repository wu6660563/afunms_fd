﻿<!--
SPT="--请选择类型--";
SCT="--请选择类别--";
SAT="--请选择指标--";
ShowT=1;		//提示文字 0:不显示 1:显示
PCAD="host$Windows Professional/XP,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,diskperc,hardware,interface,ipmac,physicalmemory,ping,process,service,software,storage,systemgroup,utilhdx,virtualmemory|IBM AIX 服务器,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,avque,avserv,avwait,cpu,disk,diskbusy,diskinc,interface,physicalmemory,ping,process,responsetime,swapmemory,utilhdx|HP SunOS Sparc agent original,无|Sun Microsystems SunOS,无|Linux,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,disk,hardware,ipmac,physicalmemory,ping,process,service,software,storage,systemgroup,utilhdx,virtualmemory|AS400 服务器,cpu,diskperc,jobnumber,ping#net$bdcom,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|cisco,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|dlink,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|enterasys,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|h3c,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|harbour,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|huawei,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|maipu,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|nortel,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|radware,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|redgiant,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage|zte,AllInBandwidthUtilHdx,AllOutBandwidthUtilHdx,cpu,discardsperc,errorsperc,fan,fdb,flash,inpacks,interface,ipmac,memory,outpacks,packs,ping,power,router,systemgroup,temperature,utilhdx,voltage#db$Oracle,ping,tablespace,buffercache,dictionarycache,pctmemorysorts,pctbufgets,tablespaceinc,opencursor|sqlserver,ping";if(ShowT)PCAD=SPT+"$"+SCT+","+SAT+"#"+PCAD;PCAArea=[];PCAP=[];PCAC=[];PCAA=[];PCAN=PCAD.split("#");for(i=0;i<PCAN.length;i++){PCAA[i]=[];TArea=PCAN[i].split("$")[1].split("|");for(j=0;j<TArea.length;j++){PCAA[i][j]=TArea[j].split(",");if(PCAA[i][j].length==1)PCAA[i][j][1]=SAT;TArea[j]=TArea[j].split(",")[0]}PCAArea[i]=PCAN[i].split("$")[0]+","+TArea.join(",");PCAP[i]=PCAArea[i].split(",")[0];PCAC[i]=PCAArea[i].split(',')}function PCAS(){this.SelP=document.getElementsByName(arguments[0])[0];this.SelC=document.getElementsByName(arguments[1])[0];this.SelA=document.getElementsByName(arguments[2])[0];this.DefP=this.SelA?arguments[3]:arguments[2];this.DefC=this.SelA?arguments[4]:arguments[3];this.DefA=this.SelA?arguments[5]:arguments[4];this.SelP.PCA=this;this.SelC.PCA=this;this.SelP.onchange=function(){PCAS.SetC(this.PCA)};if(this.SelA)this.SelC.onchange=function(){PCAS.SetA(this.PCA)};PCAS.SetP(this)};PCAS.SetP=function(PCA){for(i=0;i<PCAP.length;i++){PCAPT=PCAPV=PCAP[i];if(PCAPT==SPT)PCAPV="";PCA.SelP.options.add(new Option(PCAPT,PCAPV));if(PCA.DefP==PCAPV)PCA.SelP[i].selected=true}PCAS.SetC(PCA)};PCAS.SetC=function(PCA){PI=PCA.SelP.selectedIndex;PCA.SelC.length=0;for(i=1;i<PCAC[PI].length;i++){PCACT=PCACV=PCAC[PI][i];if(PCACT==SCT)PCACV="";PCA.SelC.options.add(new Option(PCACT,PCACV));if(PCA.DefC==PCACV)PCA.SelC[i-1].selected=true}if(PCA.SelA)PCAS.SetA(PCA)};PCAS.SetA=function(PCA){PI=PCA.SelP.selectedIndex;CI=PCA.SelC.selectedIndex;PCA.SelA.length=0;for(i=1;i<PCAA[PI][CI].length;i++){PCAAT=PCAAV=PCAA[PI][CI][i];if(PCAAT==SAT)PCAAV="";PCA.SelA.options.add(new Option(PCAAT,PCAAV));if(PCA.DefA==PCAAV)PCA.SelA[i-1].selected=true}}
//-->