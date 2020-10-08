package co.kr.istarbucks.xo.batch.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.mgr.PreOrderDeleteMgr;

/**
 * 선계산 주문 데이터 삭제
 *
 */
public class PreOrderDelete {

   	private static Log logger = LogFactory.getLog("PreOrderDelete");

    private Configuration conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");

    private String logTitle = "";
    
    private final String loggerTitle;
    private final PreOrderDeleteMgr preOrderDeleteMgr;
  
    public PreOrderDelete () {
        this.preOrderDeleteMgr = new PreOrderDeleteMgr ();
        this.loggerTitle = "[PreOrderDelete] ";
    }
    
    /**
     * 선계산 주문 데이터 삭제
     * @param deleteDay
     */
    public void start (String thatDay) {
    	long startTimeTotal = System.currentTimeMillis ();
    	
    	boolean paramDaysCheck = NumberUtils.isNumber(thatDay) && Integer.parseInt(thatDay) > 0;
    	
    	String deleteDay = paramDaysCheck ? thatDay : "7";
    	
        
    	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "START");
        
    	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "오늘부터 " + deleteDay + "일 전 기준으로 삭제");

        // 1. XO_PRE_PAYMENT 테이블 데이터 삭제
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. XO_PRE_PAYMENT 테이블 데이터 삭제 START");
        boolean isPrePaymentSuccess = this.deletePrePayment(deleteDay);
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. XO_PRE_PAYMENT 테이블 데이터 삭제 END");

        // 2. XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. XO_PRE_ORDER_DETAIL 테이블 데이터 삭제 START");
        boolean isPreOrderDetailSuccess = this.deletePreOrderDetail(deleteDay);
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. XO_PRE_ORDER_DETAIL 테이블 데이터 삭제 END");

        if(isPrePaymentSuccess && isPreOrderDetailSuccess) {
        	 // 3. XO_PRE_ORDER# 테이블 데이터 삭제
        	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "3. XO_PRE_ORDER# 테이블 데이터 삭제 START");
            this.deletePreOrder(deleteDay);
        	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "3. XO_PRE_ORDER# 테이블 데이터 삭제 END");

            logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "END :" + ( System.currentTimeMillis () - startTimeTotal ) + "ms");
        }else {
        	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "데이터 삭제 실패로 인한 프로세스 종료");
        }
       
        
    }

	/**
     * XO_PRE_ORDER# 테이블 데이터 삭제
     * @param deleteDay
     */
    private void deletePreOrder(String deleteDay) {
    	
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("deleteDay", deleteDay);
            
            // XO_PRE_ORDER# 테이블 데이터 삭제
            int deleteDataCnt = this.preOrderDeleteMgr.deletePreOrder(dbMap);
            logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "XO_PRE_ORDER# 테이블 데이터 삭제 : " + deleteDataCnt + "건");

        } catch (SQLException e ) {
            logger.error (e.toString().replaceAll("\n|\r", ""), e);
        }

	}

	/**
     * XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
     * @param deleteDay
     */
    private boolean deletePreOrderDetail(String deleteDay) {
    	boolean seccess = false;
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("deleteDay", deleteDay);
            
            // XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
            int deleteDataCnt = this.preOrderDeleteMgr.deletePreOrderDetail(dbMap);
            logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "XO_PRE_ORDER_DETAIL 테이블 데이터 삭제 : " + deleteDataCnt + "건");
            seccess = true;
        } catch (SQLException e ) {
            logger.error (e.toString().replaceAll("\n|\r", ""), e);
            seccess = false;
        }
        return seccess;
	}

	/**
     * XO_PRE_PAYMENT 테이블 데이터 삭제
     * @param deleteDay
     */
    private boolean deletePrePayment(String deleteDay) {
    	boolean seccess = false;
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("deleteDay", deleteDay);
            
            // XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
            int deleteDataCnt = this.preOrderDeleteMgr.deletePrePayment(dbMap);
            logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "XO_PRE_ORDER_DETAIL 테이블 데이터 삭제 : " + deleteDataCnt + "건");
            seccess = true;
        } catch (SQLException e ) {
            logger.error (e.toString().replaceAll("\n|\r", ""), e);
            seccess = false;
        }
        return seccess;

	}


	public static void main( String[] args) {
        PreOrderDelete preOrderDelete = new PreOrderDelete();        
        
        if(args.length > 0) {
        	preOrderDelete.start(args[0]);
        } else {
        	preOrderDelete.start("");
        }        
    }

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLoggerTitle() {
		return loggerTitle;
	}

	public PreOrderDeleteMgr getPreOrderDeleteMgr() {
		return preOrderDeleteMgr;
	}
	

}
