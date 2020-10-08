package co.kr.istarbucks.xo.batch.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.sms.SMSClient;
import co.kr.istarbucks.xo.batch.mgr.DaemonLogMgr;

public class DaemonLog {
	
	private final DaemonLogMgr dlm;
	private final DaemonLogDefine dlf;
	private final SMSClient smsc;
	
	private final String class_name;
	private String desc;
	private String status;
	private String sh_name;
	
	private static final String start_code 	= "S";
	private static final String end_code	= "E";
	private static final String error_code 	= "F";
	
	private static final String start_desc	= "Process Start";
	private static final String end_desc	= "Process Stop";
	
	private static Log logger = LogFactory.getLog("daemonLog");
	
	public DaemonLog(String name) {
		
		class_name = getClassName(name);
		
		dlm = new DaemonLogMgr();
		dlf = new DaemonLogDefine();
		smsc = new SMSClient();
	}
	
	/**
	 * 각 데몬/배치 시작 로그
	 * @param name
	 */
	public void setStartLog() {		
		sh_name 	= dlf.getSh(class_name);
		desc		= start_desc;
		status		= start_code;		
		
		try {
			dlm.insertLog(sh_name, class_name, status, desc);
			logger.info("DAEMON Start Log Write SUCCESS : script=["+sh_name+"], class=["+class_name+"]");
		} catch (Exception e) {
			logger.error(e, e);
		}
		
	}
	
	/**
	 * 각 데몬/배치 종료 로그
	 * @param name
	 */
	public void setEndLogAndSMS(boolean isSMS) {
		sh_name 	= dlf.getSh(class_name);
		desc		= end_desc;
		status		= end_code;
		
		try {
			dlm.insertEndLog(class_name, sh_name, status, desc);
			logger.info("DAEMON End Log Write SUCCESS : script=["+sh_name+"], class=["+class_name+"]");
		} catch (Exception e) {
			logger.error(e, e);
		}
		
		//true 일 때만, 종료 SMS 전송
		if (isSMS) { smsc.sendInfoSMS(sh_name); }
		
	}
	
	/**
	 * 각 데몬/배치 에러 로그
	 * @param e (Exception)
	 */
	public void setErrorLogAndSMS(Exception e, boolean isSMS) {
		sh_name 	= dlf.getSh(class_name);
		status		= error_code;
		
		if (e.getMessage() != null) {
			desc = e.getMessage();
		} else {
			desc = String.valueOf(e);
		}
		
		try {
			dlm.insertErrorLog(class_name, sh_name, status, desc);
			logger.info("DAEMON Error Log Write SUCCESS : script=["+sh_name+"], class=["+class_name+"]");
		} catch (Exception ex) {
			logger.error(ex, ex);
		}
		
		//true 일 때만, 에러 SMS 전송
		if(isSMS) { smsc.sendErrorSMS(sh_name, e); }
		
	}
	
	private String getClassName(String name) {
		String result	= null;
		String[] ns		= name.split("\\.");
		result			= ns[ns.length -1];
		
		return result;
	}
	
	public static void  main(String[] args) {
		
	}
}
