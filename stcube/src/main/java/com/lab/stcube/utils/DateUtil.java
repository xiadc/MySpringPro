package com.lab.stcube.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bson.Document;

public class DateUtil {
	
	/*
	 * 根据时间戳，计算包含年、月、日、小时的document
	 */
	public static Document getDateDocutment(long timestamp){
		Document date = new Document();
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = format.format(timestamp);  
	    Date dateTime = null;
		try {
			dateTime = format.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Calendar c = Calendar.getInstance();
		c.setTime(dateTime);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
	    date.append("year", year);
	    date.append("month", month);
	    date.append("day", day);
	    date.append("hour", hour);
		return date;
	}

}
