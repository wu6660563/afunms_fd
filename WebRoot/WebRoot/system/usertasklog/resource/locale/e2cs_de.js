// --------------------------------------------------
// E2CS  Extjs-Event-Calendar-Solution  alpha(0.0.1) 
// @Version E2CS-0.0.1  june 27 2008 
// German
// @Author- translation: Sol Neckels -  Ext forum user: RugWarrior
// @contact: mateus@mattsoft.eti.br
// --------------------------------------------------
Ext.namespace('e2cs.cal_locale');
e2cs.cal_locale={
    // Calendar locale
    dateformat:          'd.m.Y',
    dateSelectorText:    'W�hle..',
    dateSelectorTooltip: 'W�hle ein Datum..',
    // Why i have my own locale for days and months you will ask ? 
    // i didn't want to mess with ext locale and prefer the widget will have his own locale    
    monthtitles:["Januar",     "Februar",     "M�rz",
                 "April",     "Mai",             "Juni",
                 "Juli",     "August",         "September",
                 "Oktober",     "November",     "Dezember"],
    daytitles:  ["Sonntag",     "Montag",         "Dienstag",     "Mittwoch","Donnerstag",     "Freitag",     "Samstag"],
    todayLabel: 'Heute',
    todayToolTip: 'Heutiges Datum setzen...',
    // toolbar buttons tooltips 
    tooltipMonthView:   'Monats-Ansicht...',
    tooltipWeekView:    'Wochen-Ansicht...',    
    tooltipDayView:     'Tages-Ansicht..',    
    // tpl for zoom tasks general labels 
    win_tasks_format:  'd.m.Y',
    win_tasks_loading: 'Lade...',
    win_tasks_empty:   'Keine Aufgaben...',
    // Month view locale
    win_month_zoomlabel:'Tagesaufgaben',
    headerTooltipsMonth: { 
        prev: 'Vorheriger Monat..', 
        next: 'N�chster Monat' 
    }, 
    contextMenuLabelsMonth: { 
        task: "Eine neue Aufgabe hinzuf�gen", 
        chgwview: "Zur Wochen-Ansicht wechseln...", 
        chgdview: "Zur Tages-Ansicht wechseln...",
        gonextmonth: "N�chster Monat",     // 0.0.4
        goprevmonth: "Vorheriger Monat"     // 0.0.4
    },
    labelforTasksinMonth: 'Tagesaufgaben:',
    // Dayview and daytask  locale     
    task_MoreDaysFromTask: '<br>(+)',
    task_LessDaysFromTask: '(-)<br>',
    task_qtip_starts:     'Beginnt: ', 
    task_qtip_ends:     'Endet: ',     
    headerTooltipsDay: { 
            prev: 'Vorheriger Tag', 
            next: 'N�chster Tag' 
    }, 
    contextMenuLabelsDay: { 
        taskAdd: "Neue Aufgabe", 
        taskDelete: "Aufgabe l�schen", 
        taskEdit: "Aufgabe bearbeiten:",
        NextDay: "Zum n�chten Tag wechseln", 
        PreviousDay: "Zum vorherigen Tag wechseln"
    },
    //Week view  and weektask locale 
    // 0.0.4
    contextMenuLabelsWeek: { 
        prev: "Vorherige Woche.", 
        next: "N�chste Woche.", 
        chgdview: "Tages-Ansicht...", 
        chgmview: "Monats-Ansicht..."
    },    
    //0.0.4
    headerTooltipsWeek: { 
        prev: 'Vorherige Woche.', 
        next: 'N�chste Woche.' 
    },    
    labelforTasksinWeek: 'Mehr Aufgaben in der Woche:',
    win_week_zoomlabel:'Mehr Aufgaben...',
    weekheaderlabel:'Woche #',
    weekheaderlabel_from:'Von:',
    weekheaderlabel_to:' An:',    
    alldayTasksMaxLabel:'Mehr Ganztages Aufgaben ansehen...'
}