package com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ProcessTest {

    public static void main(String[] args) {
    	String dateStr = "1991-10-18 00:00:00";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
    	Date today = new Date();
    	try {
			Date date = sdf.parse(dateStr);
			
			Long time = today.getTime()- date.getTime();
			Long day = time / (1000 * 60 * 60 * 24);
			System.out.println(day+"Ìì");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
    }

}
