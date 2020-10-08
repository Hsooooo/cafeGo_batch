 package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.mgr.EzOrderMgr;

 
public class EzOrder { 
	
	private static Log sLogger = LogFactory.getLog ("EZORDER");
	
	private final EzOrderMgr ezorderMgr;
	private final String loggerTitle;
//	private boolean isFirstDay = false;
	private final StringBuffer logSb; 
	
	public EzOrder () {
		this.ezorderMgr = new EzOrderMgr ();
		this.loggerTitle = "[ezorder] ";
		this.logSb = new StringBuffer (); // log용 StringBuffer
	}
	
	public void start () {
		long startTime = System.currentTimeMillis ();
		
		logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("START");
		sLogger.info (logSb.toString ());
		
		// 오늘 DB 날짜 조회
		getToday ();
		
		Map<String, String> paramMap = new HashMap<String, String> ();
		  
		// 이지오더 일통계
		this.startEzOrder();
		
		logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("END : ").append (System.currentTimeMillis () - startTime).append ("ms");
		sLogger.info (logSb.toString ());
	}  
	
	/**
	 * DB의 오늘 날짜 가져오기 
	 */
	private void getToday () {
		try {
			Map<String, String> map = this.ezorderMgr.getToday ();
			
			String today = "";
			if ( map != null ) {
//				isFirstDay = StringUtils.equals (map.get ("firstDayFlag"), "Y");
				today = map.get ("dbToday");
			}
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("ezorder TODAY : ").append (today);
			sLogger.info (logSb.toString ());
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("ezorder TODAY ").append (e);
			sLogger.error (logSb.toString (), e);
		}
	}
	
	 
	/**
	 * 이지오더 일통계
	 */
	private void startEzOrder() {
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.ezorderMgr.ezOrder ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("EzOrder ezOrder (").append ("D").append (") : ").append (resutCnt).append ("건 END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("EzOrder ezOrder (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
		}
	}	
	
	public static void main ( String[] args ) {
		EzOrder ezorder = new EzOrder ();
		ezorder.start ();
	}
	
}
