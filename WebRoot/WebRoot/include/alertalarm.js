var allAlertAlarm = null;
    var aa ;
	function openWin(ad) 
	{
		aa = window.open(ad,'theUniqueName');
	}
	
	function openGraphWindow(theURL,winName,width,height,parms)
	{
	    var left = Math.floor( (screen.width - width) / 2);
	    var top = Math.floor( (screen.height - height) / 2);
	    var winParms = "top=" + top + ",left=" + left + ",height=" + height + ",width=" + width +",scrollbars=yes";
	    if (parms) { winParms += "," + parms; }
	    window.open(theURL, winName, winParms);   
	}
	
/*  
 *    消息构造  
 */  
function CLASS_MSN_MESSAGE(id,width,height,caption,title,message,target,action)  
{  
    this.id     = id;  
    this.title  = title;  
    this.caption= caption;  
    this.message= message;  
    this.target = target;  
    this.action = action;  
    this.width    = width?width:250;  
    this.height = height?height:350;  
    this.timeout= 900000000;  
    this.speed    = 20; 
    this.step    = 1; 
    this.right    = screen.width -1;  
    this.bottom = screen.height; 
    this.left    = this.right - this.width; 
    this.top    = this.bottom - this.height; 
    this.timer    = 0; 
}  
  
/*  
 *    隐藏消息方法  
 */  
CLASS_MSN_MESSAGE.prototype.hide = function()  
{  
    if(this.onunload())      
    {  
        var offset  = this.height>this.bottom-this.top?this.height:this.bottom-this.top; 
        var me  = this;  
 
        if(this.timer>0)  
        {   
            window.clearInterval(me.timer);  
        }  
 
        var fun = function()  
        {  
            var x  = me.left; 
            var y  = 0; 
            var width = me.width; 
            var height = 0; 
            if(me.offset>0) 
            { 
                height = me.offset; 
            } 
 
            y  = me.bottom - height; 
 
            if(y>=me.bottom) 
            { 
                window.clearInterval(me.timer);  
                me.Pop.hide();  
            } 
            else 
            { 
                me.offset = me.offset - me.step;  
            } 
            alert(Height);
            me.Pop.show(x,y,width,height);    
 
             
 
        }  
 
        //this.timer = window.setInterval(fun,this.speed)      
    }  
}  

/*  
 *    隐藏消息方法  
 */  
CLASS_MSN_MESSAGE.prototype.stopSound = function()  
{  
    var me  = this;  
    me.Pop.document.embeds[0].stop();  
} 
  
/*  
 *    消息卸载事件，可以重写  
 */  
CLASS_MSN_MESSAGE.prototype.onunload = function()  
{  
    return true;  
}  
/*  
 *    消息命令事件，要实现自己的连接，请重写它  
 *  
 */  
CLASS_MSN_MESSAGE.prototype.oncommand = function()  
{  
    //this.hide();  
    var hostStr = document.location.host;
    var urlStr = "http://127.0.0.1:788/afunms/AlarmInfoMgr.do?operate=list";
    openWin(urlStr);
}  
  
/*  
 *    消息显示方法  
 */  
CLASS_MSN_MESSAGE.prototype.show = function()  
{  
    var oPopup = window.createPopup(); //IE5.5+  
      
    this.Pop = oPopup;  
  
    var w = this.width;  
    var h = this.height;  
  
    var str = "<DIV style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX: 99999; LEFT: 0px; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: " + w + "px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: " + 450 + "px; BACKGROUND-COLOR: #c9d3f3'>"  
        str += "<TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0>"  
        str += "<TR>"  
        str += "<TD style='FONT-SIZE: 12px;COLOR: #0f2c8c' width=30 height=24></TD>"  
        str += "<TD style='PADDING-LEFT: 4px; FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px' vAlign=center width='100%'>" + this.caption + "</TD>"  
        str += "<TD style='PADDING-RIGHT: 2px; PADDING-TOP: 2px' vAlign=center align=right width=19>"  
        str += "<SPAN title=关闭 style='FONT-WEIGHT: bold; FONT-SIZE: 12px; CURSOR: hand; COLOR: red; MARGIN-RIGHT: 4px' id='btSysClose' >×</SPAN></TD>"  
        str += "</TR>"  
        str += "<TR>"  
        str += "<TD style='PADDING-RIGHT: 1px;PADDING-BOTTOM: 1px' colSpan=3 height=" + (400) + ">"  
        str += "<DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 8px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 8px; FONT-SIZE: 12px; PADDING-BOTTOM: 8px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 8px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%'>" + this.title + "<BR><BR>"  
        
        if(this.message!=null){
	        for(var i=0;i<this.message.length;i++){	 
	          if(i<10){       //限制显示的个数，如果超过2个，则只显示前面2个告警信息
	            str += "<DIV style='WORD-BREAK: break-all' align=left>"
	            str += "<A href='openWin('alarm.do');' hidefocus=false id='btCommand'><FONT color=#ff0000>" + (i+1) +","+this.message[i] + "</FONT></A>"  
	            str += "</DIV>"
	          }
	        }
        }
        str += "</DIV>"  
        str += "</TD>"  
        str += "</TR>"  
        str += "</TABLE>"  
        str += "</DIV>"  
        str += "<DIV>"
        str += "<embed src='http://127.0.0.1:788/afunms/Sound/msg.wav' hidden='true' loop='true' name='midi' width='0' height='0'></embed>" 
        str += "</DIV>"
  
    oPopup.document.body.innerHTML = str;  
  
    this.offset  = 0; 
    var me  = this;  
    var fun = function()  
    {  
        var x  = me.left; 
        var y  = 0; 
        var width    = me.width; 
        var height    = me.height; 
 
 
            if(me.offset>me.height) 
            { 
                height = me.height; 
            } 
            else 
            { 
                height = me.offset; 
            } 
 
        y  = me.bottom - me.offset; 
        if(y<=me.top) 
        { 
            me.timeout--; 
            if(me.timeout==0) 
            { 
                window.clearInterval(me.timer);  
                me.hide(); 
            } 
        } 
        else 
        { 
            me.offset = me.offset + me.step; 
        } 
        me.Pop.show(x,y,width,height);    
 
          
 
    }  
  
    this.timer = window.setInterval(fun,this.speed)      
  
     
  
    var btClose = oPopup.document.getElementById("btSysClose");  
  
    btClose.onclick = function()  
    {  
    	oPopup.document.embeds[0].stop();
        oPopup.hide();
        me.hide();  
    }  
  
    var btCommand = oPopup.document.getElementById("btCommand");  
    btCommand.onclick = function()  
    {  
        me.oncommand();  
    }  
  
  
}  
/* 
** 设置速度方法 
**/ 
CLASS_MSN_MESSAGE.prototype.speed = function(s) 
{ 
    var t = 20; 
    try 
    { 
        t = praseInt(s); 
    } 
    catch(e){} 
    this.speed = t; 
} 
/* 
** 设置步长方法 
**/ 
CLASS_MSN_MESSAGE.prototype.step = function(s) 
{ 
    var t = 1; 
    try 
    { 
        t = praseInt(s); 
    } 
    catch(e){} 
    this.step = t; 
} 
  
CLASS_MSN_MESSAGE.prototype.rect = function(left,right,top,bottom) 
{ 
    try 
    { 
        this.left        = left    !=null?left:this.right-this.width; 
        this.right        = right    !=null?right:this.left +this.width; 
        this.bottom        = bottom!=null?(bottom>screen.height?screen.height:bottom):screen.height; 
        this.top        = top    !=null?top:this.bottom - this.height; 
 
    } 
    catch(e) 
    {} 
} 

function alertAlarm(){ 
    buffalo.remoteCall("alertAlarm.readAlertAlarm",[], function(reply1){  
    	 
		allAlertAlarm = reply1.getResult();
	});
    var alarmArray = new Array();
    if((allAlertAlarm==null)||(allAlertAlarm=="")){
    }else{
        for (var i=0; i<allAlertAlarm.length; i++)
	   	{   
	   	  alarmArray[i] = allAlertAlarm[i][0]+","+allAlertAlarm[i][2];
	   	}   
    //var MSG1 = new CLASS_MSN_MESSAGE("aa",200,120,"报警提示：","有1个新的警报产生","10.55.1.17,cpu使用率超过90%");
        if(MSG1!=null){
          //MSG1.document.embeds[0].stop();
          MSG1.hide();
          MSG1.stopSound();
        }
	    MSG1 = new CLASS_MSN_MESSAGE("aa",250,350,"报警提示：","有"+allAlertAlarm.length+"个新的警报产生",alarmArray);  
	    MSG1.rect(null,null,null,screen.height-50); 
	    MSG1.speed = 20; 
	    MSG1.step = 5; 
	    MSG1.show(); 
    }
}
 
