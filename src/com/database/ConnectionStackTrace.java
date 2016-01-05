package com.database;

import java.util.Date;

public class ConnectionStackTrace {

    private String id;

    private StackTraceElement[] stackTraceElements;

    private Date date;

    private Long time;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the stackTraceElements
     */
    public StackTraceElement[] getStackTraceElements() {
        return stackTraceElements;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param stackTraceElements the stackTraceElements to set
     */
    public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
        this.stackTraceElements = stackTraceElements;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Long time) {
        this.time = time;
    }
    
    
}
