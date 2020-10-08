package co.kr.istarbucks.xo.batch.main;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.mgr.LogFileRemoverMgr;
import co.kr.istarbucks.xo.batch.common.util.DateUtil;

public class LogFileRemover {
	private static Log logger = LogFactory.getLog("logFileRemover");
//	private DaemonLog daemonLog;
	private final LogFileRemoverMgr lfrMgr;
	private final String BASE_DIR_PATH_RESIN       		= File.separator + "xop_app" + File.separator + "resin-pro-4.0.43" + File.separator +"log" + File.separator;
	private final String BASE_DIR_PATH_XO_BATCH    		= File.separator + "xop_app" + File.separator + "xo" + File.separator + "batch" + File.separator +"logs" + File.separator;
	private final String BASE_DIR_PATH_XO_POS      		= File.separator + "xop_app" + File.separator + "xo" + File.separator + "pos" + File.separator +"logs" + File.separator;
	private final String BASE_DIR_PATH_JEUS_WEB			= File.separator + "JEUS" + File.separator + "jeus7" + File.separator +"domains" + File.separator+ "sta-eux-xowas1" + File.separator + "servers" + File.separator + "sta-eux-xowas4-msrweb" + File.separator + "logs" + File.separator ;			//xowas4서버반영
	private final String BASE_DIR_PATH_JEUS_MSRGEAR		= File.separator + "JEUS" + File.separator + "jeus7" + File.separator +"domains" + File.separator+ "sta-eux-xowas1" + File.separator + "servers" + File.separator + "sta-eux-xowas4-msrgear" + File.separator + "logs" + File.separator ;			//xowas4서버반영
	private final String BASE_DIR_PATH_JEUS_APP			= File.separator + "JEUS" + File.separator + "jeus7" + File.separator +"domains" + File.separator+ "sta-eux-xowas1" + File.separator + "servers" + File.separator + "sta-eux-xowas4-msrapp" + File.separator + "logs" + File.separator ;			//xowas4서버반영
	private final String BASE_DIR_PATH_JEUS_MANGO		= File.separator + "JEUS" + File.separator + "jeus7" + File.separator +"domains" + File.separator+ "sta-eux-xowas1" + File.separator + "servers" + File.separator + "mango6" + File.separator + "logs" + File.separator ;							//xowas4서버반영
	private final String BASE_DIR_PATH_JEUS_APPLE		= File.separator + "JEUS" + File.separator + "jeus7" + File.separator +"domains" + File.separator+ "sta-eux-xowas1" + File.separator + "servers" + File.separator + "apple6" + File.separator + "logs" + File.separator ;							//xowas4서버반영
	
	public LogFileRemover() {
		lfrMgr    = new LogFileRemoverMgr();
//		daemonLog = new DaemonLog("LogFileRemover");
	}
	
	public void removeLogFiles() {
		
		logger.info("");
		logger.info("");
		logger.info("");
		logger.info("==========================================================");
		logger.info("[processStart]=" + DateUtil.getTodayLog());
		logger.info("==========================================================");
//		logger.info("[targetPath]=" + BASE_DIR_PATH_RESIN);
//		logger.info("[targetPath]=" + BASE_DIR_PATH_MSR);

		try {
			logger.info("1. delete log file(s)");
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_RESIN);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_XO_BATCH);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_XO_POS);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_JEUS_WEB);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_JEUS_MSRGEAR);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_JEUS_APP);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_JEUS_MANGO);
			lfrMgr.deleteLogFiles(BASE_DIR_PATH_JEUS_APPLE);
			
			logger.info("");
		} catch (Exception e) {
			logger.error("ERROR!================================");
			logger.error(e, e);
		} finally {
			logger.info("==========================================================");
			logger.info("[processEnd]=" + DateUtil.getTodayLog());
			logger.info("==========================================================");
			logger.info("");
			logger.info("");
		}
	}
	
	public static void main(String[] args) {
		if (args.length >= 1) {
			String workType = args[0];
			if ("start".equals(workType)) {
				LogFileRemover logRemover = new LogFileRemover();
				logRemover.removeLogFiles(); 
			}  else {
				logger.info("usage :: logFileRemover.sh [start | legacy]");
			}
		} else {
			logger.info("usage :: logFileRemover.sh [start | legacy]");
		}
	}
}
