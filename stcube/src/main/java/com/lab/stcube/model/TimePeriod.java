package com.lab.stcube.model;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class TimePeriod {

    /**
     * Default constructor
     */
    public TimePeriod() {
    }

    public TimePeriod(String bg) {
    	setBegin(bg);
    	setEnd(bg);
    }
    
    public TimePeriod(String bg,String format) {
    	if(format==""){
    		setBegin(bg);
    	}
    	else {
			setBegin(bg, format);
		}
		end=begin;
    }
    
    public TimePeriod(String bg,String ed,String format) {
    	if(format==""){
        	setBegin(bg);
        	setEnd(ed);
    	}else {
        	setBegin(bg,format);
        	setEnd(ed,format);
		}

    }
    
    
    
    /**
     * 
     */
    private long begin=Long.MAX_VALUE;

    /**
     * 
     */
    private long end=0;
    
    
    
    public long getBegin() {	
    	return begin;
	}
    
    public String getBeginAsString(String format) {
		return stampToDate(begin, format);
	}
    
    public String getBeginAsString() {
		return stampToDate(begin, "yyyy-MM-dd HH:mm:ss");
	}
    
    
    public void setBegin(long s){
    	begin=s;
    }
    
    public void setBegin(String s) {
    	begin = dateToStamp(s,"yyyy-MM-dd HH:mm:ss");
	}
    
    public void setBegin(String s,String format) {
    	begin = dateToStamp(s,format);
	}
    
    
    
    public long getEnd() {
		return end;
	}
    
    public String getEndAsString(String format) {
		return stampToDate(end, format);
	}
    
    public String getEndAsString() {
		return stampToDate(end, "yyyy-MM-dd HH:mm:ss");
	}
    
    
    public void setEnd(long s) {
		end=s;
	}
    
    public void setEnd(String s) {
    	end = dateToStamp(s,"yyyy-MM-dd HH:mm:ss");
	}
    
    public void setEnd(String s,String format) {
    	begin = dateToStamp(s,format);
	}
    
    
    public long getInterval() {
		return end-begin;
	}
    
    public int getIntervalInDays() {
    	return (int) ((end - begin)/(1000 * 60 * 60 * 24));
	}
    
    public static long dateToStamp(String str,String format) {
    	//format=format?format:"yyyy-MM-dd HH:mm:ss";
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return sdf.parse(str).getTime();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return 0;  
	}
    
    public static String stampToDate(long stamp,String format){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }
}