// --------------------------------------------------
// E2CS  Extjs-Event-Calendar-Solution  alpha(0.0.1) 
// @Version E2CS-0.0.1  june 27 2008 
//Portuguese - Brazil 
// @Author- translation: Mateus Medeiros
// @contact: mateus@mattsoft.eti.br
// --------------------------------------------------

Ext.namespace('e2cs.cal_locale');
e2cs.cal_locale={
    // Calendar locale    
    dateSelectorText:    'Selecione..',
    dateSelectorTooltip: 'Selecione uma data..',
    // Why i have my own locale for days and months you will ask ? 
    // i didn't want to mess with ext locale and prefer the widget will have his own locale    
    monthtitles:["Janeiro",     "Fevereiro",     "Mar�o",
                 "Abril",     "Maio",         "Junho",
                 "Julho",     "Agosto",         "Setembro",
                 "Outubro",     "Novembro",     "Dezembro"],
    daytitles:  ["Domingo",     "Segunda",         "Ter�a",     "Quarta","Quinta",     "Sexta",     "S�bado"],
    todayLabel: 'Hoje',
    todayToolTip: 'Ir para data de Hoje...',
    
    // toolbar buttons tooltips 
    tooltipMonthView:    'Visualizar M�s...',
    tooltipWeekView:    'Visualizar Semana...',    
    tooltipDayView:        'Visualizar Dia...',    
    
    // tpl for zoom tasks general labels 
    win_tasks_format:  'd/m/Y',
    win_tasks_loading: 'Carregado...',
    win_tasks_empty:   'Nenhuma tarefa...',
    // Month view locale
    win_month_zoomlabel:'Tarefas do dia',
    headerTooltipsMonth: { 
        prev: 'M�s Anterior..', 
        next: 'Pr�ximo M�s' 
    }, 
    contextMenuLabelsMonth: { 
        task: "Adicionar nova tarefa", 
        chgwview: "Mudar para visualiza��o da semana...", 
        chgdview: "Mudar para visualiza��o do dia...",
		gonextmonth: "Go to next month",     // 0.0.4
		goprevmonth: "Go previous month"     // 0.0.4
    },
    labelforTasksinMonth: 'Tarefas do dia:',
    // Dayview and daytask  locale     
    task_MoreDaysFromTask: '<br>(+)',
    task_LessDaysFromTask: '(-)<br>',
    task_qtip_starts:     'Come�a: ', 
    task_qtip_ends:     'Termina: ',     
    headerTooltipsDay: { 
            prev: 'Dia Anterior', 
            next: 'Pr�ximo Dia' 
    }, 
    contextMenuLabelsDay: { 
        taskAdd: "Adicionar Tarefa", 
        taskDelete: "Apagar Tarefa", 
        taskEdit: "Alterar Tarefa:",
        NextDay: "Ir para pr�ximo dia", 
        PreviousDay: "Ir para o dia anterior"
    },
    //Week view  and weektask locale 
	// 0.0.4
	contextMenuLabelsWeek: { 
		prev: "Go to Previous week.", 
		next: "Go Next week.", 
		chgdview: "Change to day view...", 
		chgmview: "Change to month view..."
	},	
	//0.0.4
	headerTooltipsWeek: { 
		prev: 'Previous week.', 
		next: 'Next week.' 
	},	
    labelforTasksinWeek: 'Mais tarefas na Semana:',
    win_week_zoomlabel:'Mais tarefas...',
    weekheaderlabel:'Semana #',
    weekheaderlabel_from:'De:',
    weekheaderlabel_to:' Para:',    
    alldayTasksMaxLabel:'Visualizar lista de tarefas..'
} 
