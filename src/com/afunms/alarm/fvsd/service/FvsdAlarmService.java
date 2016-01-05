package com.afunms.alarm.fvsd.service;

import java.io.Serializable;

import com.afunms.alarm.dao.SendFvsdAlarmDao;



public class FvsdAlarmService implements Serializable{

    private static final long serialVersionUID = 9216797171187476902L;

    public boolean deleteByFvsdCode(String fvsdCode){
        boolean result = false;
        SendFvsdAlarmDao sendFvsdAlarmDao = new SendFvsdAlarmDao();
        try {
            result = sendFvsdAlarmDao.delete(fvsdCode);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            sendFvsdAlarmDao.close();
        }
        return result;
    }

}
