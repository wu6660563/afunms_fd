// --------------------------------------------------
// E2CS  Extjs-Event-Calendar-Solution  alpha(0.0.11) 
// @Version E2CS-0.0.11  May 20 2009
// Chinese
// @Author:   Feng Gao - (AKA) sniperdiablo  
// @contact:  sniperdiablo@gmail.com, for MSN chat:sniperdiablo@hotmail.com
// --------------------------------------------------
Ext.namespace('e2cs.cal_locale');
e2cs.cal_locale={
	loadmaskText: '��������....<br>���Ժ�...!',  //0.0.12 
	addTaskMsg:  '������־!',
	// Calendar locale	
	dateSelectorText:    'ѡ��..',
	dateSelectorTooltip: 'e2cs_cn.js..',
	// Why i have my own locale for days and months you will ask ? i didn't want to mess with ext locale and prefer the widget will have his own locale	
	monthtitles:["һ��","����","����","����","����","����","����","����","����","ʮ��","ʮһ��","ʮ����"],
	daytitles:  ["��","һ","��","��","��","��","��"],
	todayLabel: '����',
	todayToolTip: '���ý�������...',
	// toolbar buttons tooltips 
	tooltipMonthView:	'�鿴����ͼ...',
	tooltipWeekView:	'�鿴����ͼ...',	
	tooltipDayView:		'�鿴����ͼ...',	
	tooltipSchedView:   '�鿴������ͼ...',		//0.0.10 new view 
	tooltipListView:    '�鿴�б���ͼ...',    // 20091208 add
	// tpl for zoom tasks general labels 
	win_tasks_format:  'm-d-Y',
	win_tasks_loading: '������...',
	win_tasks_empty:   'û���¼�...', 	 				//0.0.7  tasks changed to events
	// Month view locale
	win_month_zoomlabel:'��־', 				//0.0.7  tasks changed to events
	headerTooltipsMonth: { 
		prev: '����.', 
		next: '����.' 
	}, 
	contextMenuLabelsMonth: { 
		task: "�������־", 
		chgwview: "�л�������ͼ...", 
		chgdview: "�л�������ͼ...",
		chgsview: "�л���������ͼ...",			// 0.0.11
		gonextmonth: "�鿴�¸���",     			// 0.0.4
		goprevmonth: "�鿴�ϸ���"     			// 0.0.4
	},
	labelforTasksinMonth: '��־:',				//0.0.7  tasks changed to events
	//-----------------------------------------------------------------
	// Dayview and daytask  locale 	
	//-----------------------------------------------------------------	
	task_MoreDaysFromTask: '<br>(+)',
	task_LessDaysFromTask: '(-)<br>',
	task_qtip_starts: 	'��ʼ: ', 
	task_qtip_ends: 	'����: ',
	task_qtip_username: '�û� : ',
	task_qtip_date:     '���� : ',
	headerTooltipsDay: { 
			prev: '��һ��', 
			next: '��һ��' 
	}, 
	contextMenuLabelsDay: { 
		taskAdd: "����־", 							//0.0.7  tasks changed to events
		taskDelete: "ɾ����־", 					//0.0.7  tasks changed to events
		taskEdit: "�༭��־:",						//0.0.7  tasks changed to events
		NextDay: "��һ��", 
		PreviousDay: "��һ��",
		chgwview: "�л�������ͼ...", 
		chgmview: "�л�������ͼ...",
		chgsview: "�л���������ͼ..."			//0.0.11 
	},
	//-----------------------------------------------------------------
	//Week view  and weektask locale  // 0.0.4
	contextMenuLabelsWeek: { 
		prev: "��һ��.", 
		next: "��һ��.", 
		chgdview: "�л�������ͼ...", 
		chgmview: "�л�������ͼ...",
		chgsview: "�л���������ͼ..."			//0.0.11 
	},	
	//0.0.4
	headerTooltipsWeek: { 
		prev: '��һ��.', 
		next: '��һ��.' 
	},
	labelforTasksinWeek: '��������־:',   		//0.0.7  tasks changed to events
	win_week_zoomlabel:'������־...',				//0.0.7  tasks changed to events
	weekheaderlabel:'�� #',
	weekheaderlabel_from:'��:',
	weekheaderlabel_to:' ��:',	
	alldayTasksMaxLabel:'�鿴ȫ�����¼�..', //0.0.7  tasks changed to events
	//-----------------------------------------------------------------
	//Scheduler view strings//0.0.10 & 0.0.11 
	Scheduler_selector:'ѡ��ʱ���', 
	scheduler_noeventsonview:'<p>��ʱ��������¼�,������</p>',
	scheduler_period_from_to:{
		starts:'��',
		ends:'��'
	},
	headerTooltipsScheduler: { 
		prev: '��һʱ���.', 
		next: '��һʱ���.' 
	},
	scheduler_headerListStrings: {
		Day: "��",
		week: "��",
		period:"ʱ���",
		start: "��",
		end: "��"
	},
	contextMenuLabelsSchedule: { 
		taskAdd: 	"���¼�", 							
		taskDelete: "ɾ���¼�", 					
		taskEdit: 	"�༭�¼�:",						
		NextPeriod: 	"��һʱ���", 
		PreviousPeriod: "��һʱ���",
		chgwview: "�л�������ͼ...", 
		chgmview: "�л�������ͼ...",
		chgdview: "�л�������ͼ..."
}
}