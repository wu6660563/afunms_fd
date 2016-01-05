/*
 * @(#)IndicatorValueActionBufferRunnable.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.service;

import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   IndicatorValueActionBufferRunnable.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 16:08:03
 */
public class IndicatorValueActionBufferRunnable implements Runnable {

    private IndicatorValueActionService indicatorValueActionService;

    private IndicatorValueBuffer indicatorValueBuffer = null;

    /**
     * thread:
     * <p>�����߳�
     *
     * @since   v1.01
     */
    private Thread thread;

    /**
     * isTerminate:
     * <p>�Ƿ��ж�
     *
     * @since   v1.01
     */
    protected boolean isTerminate;
    
    /**
     * indicatorValue:
     * <p>��ǰ����ִ�� {@link IndicatorValue}
     *
     * @since   v1.01
     */
    protected IndicatorValue indicatorValue = null;

    /**
     * IndicatorValueActionRunnable.java:
     * ���췽��
     * @param   indicatorValueActionService
     *          - �ɼ�ֵ�������
     *
     * @since   v1.01
     */
    public IndicatorValueActionBufferRunnable(IndicatorValueActionService indicatorValueActionService) {
        setIndicatorValueActionService(indicatorValueActionService);
        setIndicatorValueBuffer(indicatorValueActionService.getBuffer());
    }

    /**
     * start:
     * <p>����
     *
     *
     * @since   v1.01
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.start();
    }
    /**
     * run:
     *
     *
     * @since   v1.01
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    while (indicatorValueBuffer == null || indicatorValueBuffer.size() == 0) {
                        this.wait();
                    }
                }
                indicatorValue = indicatorValueBuffer.popIndicatorValue();
                indicatorValueActionService.execute(indicatorValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * stop:
     * <p>ֹͣ����̣߳��� {@link #terminate()} Ч����ͬ
     *
     * @see #terminate()
     * @since   v1.01
     */
    public void stop() {
        this.terminate();
    }

    /**
     * runIt:
     * <p>ִ�л����߳�
     *
     *
     * @since   v1.01
     */
    public synchronized void runIt() {
        this.isTerminate = false;
        this.notify();
    }

    /**
     * terminate:
     * <p>�жϻ����߳�
     *
     *
     * @since   v1.01
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }
    /**
     * getIndicatorValueActionService:
     * <p>
     *
     * @return  IndicatorValueActionService
     *          -
     * @since   v1.01
     */
    public IndicatorValueActionService getIndicatorValueActionService() {
        return indicatorValueActionService;
    }
    /**
     * setIndicatorValueActionService:
     * <p>
     *
     * @param   indicatorValueActionService
     *          -
     * @since   v1.01
     */
    public void setIndicatorValueActionService(
                    IndicatorValueActionService indicatorValueActionService) {
        this.indicatorValueActionService = indicatorValueActionService;
    }

    /**
     * getIndicatorValueBuffer:
     * <p>
     *
     * @return  IndicatorValueBuffer
     *          -
     * @since   v1.01
     */
    public IndicatorValueBuffer getIndicatorValueBuffer() {
        return indicatorValueBuffer;
    }

    /**
     * setIndicatorValueBuffer:
     * <p>
     *
     * @param   indicatorValueBuffer
     *          -
     * @since   v1.01
     */
    public void setIndicatorValueBuffer(IndicatorValueBuffer indicatorValueBuffer) {
        this.indicatorValueBuffer = indicatorValueBuffer;
    }

}

