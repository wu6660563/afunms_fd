package com.database;

/**
 * ���ݿ⹤��ʵ����
 * @author itims
 *
 */


public class DatabaseAccessFactory {
	
	
	
    private static DatabaseAccessInterface databaseai = null;
    
    public static DatabaseAccessInterface getDataAccessInstance() {
        if (databaseai == null) {
            databaseai = new DatabaseAccessImpl();
        }
        return databaseai;
    }
    
    
}

