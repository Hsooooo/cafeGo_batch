/*
 * @(#) $Id: CommonLogAppender.java,v 1.1 2014/03/03 04:50:52 alcyone Exp $
 * 
 * Starbucks XO
 * 
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * CommonLogAppender.
 *
 * @author leeminjung
 * @version $Revision: 1.1 $
 */
public class CommonLogAppender extends FileAppender{
    private String m_directory;
    private String m_prefix;
    private String m_suffix;
    private String logTerm;
    private String logPeriod;
    private String m_partition;
    private File m_path;
    private File m_date_path;
    private Calendar m_calendar;
    private long m_next_term;
    
    public CommonLogAppender() {
    	setLogTerm(".", "TLOLog_", ".log", "D", "1", "N");
    }
    
	
	public CommonLogAppender(String logDir, String logPrefix, String logSuffix, String logTerm, String logPeriod, String logPartition) {
		setLogTerm(logDir, logPrefix, logSuffix, logTerm, logPeriod, logPartition);
		activateOptions();// NOPMD
		
	}
	
	private void setLogTerm(String logDir, String logPrefix, String logSuffix, String logTerm, String logPeriod, String logPartition) {
		this.logTerm = logTerm != null ? logTerm : "D";
		this.logPeriod = logPeriod;
		this.m_directory = logDir != null ? logDir : ".";
		this.m_prefix = logPrefix != null ? logPrefix : "TLOLog_";
		this.m_suffix = logSuffix != null ? logSuffix : ".log";
		this.m_next_term = 0L;
		this.m_partition = logPartition != null ? logPartition : "N";
	}
	
    public String getDirectory() {
        return m_directory;
    }

    public void setDirectory(String s) {
        m_directory = s;
    }

    public String getPrefix() {
        return m_prefix;
    }

    public void setPrefix(String s) {
        m_prefix = s;
    }

    public String getSuffix() {
        return m_suffix;
    }

    public void setSuffix(String s) {
        m_suffix = s;
    }
    
    public void setLogTerm(String logTerm) {
    	this.logTerm = logTerm;
    }
    
    public String getLogTerm() {
    	return logTerm;
    }
    
    public void setLogPeriod(String logPeriod) {
    	this.logPeriod = logPeriod;
    }
    
    public String getLogPeriod() {
    	return logPeriod;
    }
	
    public String getPartition() {
		return m_partition;
	}

	public void setPartition(String mPartition) {
		m_partition = mPartition;
	}


	@Override
    public void activateOptions() {
    	if(m_directory == null || m_directory.length() == 0) {
            m_directory = ".";
    	}
		//m_path = new File(m_directory);
    	 this.m_path = FileUtils.getFile(new String[]{this.m_directory});
		if (!m_path.isAbsolute()) {
			String s = System.getProperty("catalina.base");
			if (s != null) {
				m_path = FileUtils.getFile(s, m_directory);
			}
		}
		m_path.mkdirs();
		if (m_path.canWrite()) {
			makeDailyFolder();
			m_calendar = Calendar.getInstance();
			
			if ("M".equals(logTerm)) {
	        	int min = m_calendar.get(Calendar.MINUTE);
	        	int period = Integer.parseInt(this.logPeriod);
	        	if( min % period != 0){
	        		m_calendar.set(Calendar.MINUTE, period * (min / period));
	        	}
	        }
			
			long l = System.currentTimeMillis();
			makeLogFile(l, false);
		}
	}
	
	@Override
    public void append(LoggingEvent loggingevent) {
        if(layout == null) {
            errorHandler.error("No layout set for the appender named [" + name + "].");
            return;
        }
        if(m_calendar == null) {
            errorHandler.error("Improper initialization for the appender named [" + name + "].");
            return;
        }
        makeDailyFolder();
        
        long l = System.currentTimeMillis();
        if(l >= m_next_term) {
        	makeLogFile(l);
        }
        if(qw == null) {
            errorHandler.error("No output stream or file set for the appender named [" + name + "].");
            return;
        } else {
            subAppend(loggingevent);
            return;
        }
    }
    
    public void makeDailyFolder() {
        if (this.m_partition != null && this.m_partition.trim().toUpperCase().equals("Y")) {
            String date = DateTime.getFormatString(new Date(), "yyyyMMdd");
            this.m_date_path = FileUtils.getFile(new String[]{this.m_directory + "/" + date});
            if (!this.m_date_path.exists()) {
                this.m_date_path.mkdirs();
            }
            this.m_path = this.m_date_path;
        }
    }

    
    public void makeLogFile(long l, boolean isSetTime) {
    	if(isSetTime){
    		m_calendar.setTime(new Date(l));
			if ("M".equals(logTerm)) {
	        	int min = m_calendar.get(Calendar.MINUTE);
	        	int period = Integer.parseInt(this.logPeriod);
	        	if( min % period != 0){
	        		m_calendar.set(Calendar.MINUTE, period * (min / period));
	        	}
	        }    		
    	}
    	
        String s = datestamp(m_calendar, logTerm);
        nextTerm(m_calendar, logTerm, Integer.parseInt(this.logPeriod));
        m_next_term = m_calendar.getTime().getTime();
        File file = FileUtils.getFile(m_path, m_prefix + s + m_suffix);
        fileName = file.getAbsolutePath();
        super.activateOptions();
    }
    
    public void makeLogFile(long l) {
    	makeLogFile(l, true);
    }
    
    public static String datestamp(Calendar calendar, String logTerm)  {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONDAY) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        
        
        StringBuffer stringbuffer = new StringBuffer();
        if(year < 1000) {
            stringbuffer.append('0');
            if(year < 100) {
                stringbuffer.append('0');
                if(year < 10)
                    stringbuffer.append('0');
            }
        }
        stringbuffer.append(Integer.toString(year));
        //stringbuffer.append('-');
        if(month < 10)
            stringbuffer.append('0');
        stringbuffer.append(Integer.toString(month));
        //stringbuffer.append('-');
        if(day < 10)
            stringbuffer.append('0');
        stringbuffer.append(Integer.toString(day));
        if (!"D".equals(logTerm)) {
        	if(hour < 10)
        		//stringbuffer.append('-');
                stringbuffer.append('0');
            stringbuffer.append(Integer.toString(hour));
            if ("M".equals(logTerm)) {
            	if(min < 10)
            		//stringbuffer.append('-');
                    stringbuffer.append('0');
                stringbuffer.append(Integer.toString(min));
            }
        }
        return stringbuffer.toString();
    }	
    
    public static void nextTerm(Calendar calendar, String logTerm, int logPeriod) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if ("D".equals(logTerm)) {
        	day = calendar.get(Calendar.DAY_OF_MONTH) + logPeriod;
        	calendar.clear();
        	calendar.set(year, month, day);
        } else if ("H".equals(logTerm)) {
        	hour = calendar.get(Calendar.HOUR_OF_DAY) + logPeriod;
        	calendar.clear();
        	calendar.set(year, month, day, hour, 00);
        } else if ("M".equals(logTerm)) {
        	min = calendar.get(Calendar.MINUTE) + logPeriod;
        	calendar.clear();
        	calendar.set(year, month, day, hour, min);
        }
    }
}
