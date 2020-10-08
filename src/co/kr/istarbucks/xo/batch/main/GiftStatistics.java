package co.kr.istarbucks.xo.batch.main;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.GiftOrderHistoryDto;
import co.kr.istarbucks.xo.batch.common.util.DateUtil;
import co.kr.istarbucks.xo.batch.dao.XoGiftDao;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mon.DataHelper;

/**
 * e-Gift Item 통계 배치
 * @author LeeJiHun
 */
public class GiftStatistics {

	
    private final static Logger logger = Logger.getLogger("EGIFT.STATISTICS");
    private final XoGiftDao xoGiftDao;

    private static String bm_today = DateUtil.getToday("yyyyMMdd");
    private static boolean bm_is_insert_fail = false;
    private static int bm_start_cnt=0;
    private static int bm_error_cnt=0;
    private static int bm_success_cnt=0;
    private static String bm_error_target="";
  	 
    public GiftStatistics() {
        this.xoGiftDao = new XoGiftDao();
    }

    public void start(String value) {
        logger.info("'e-Gift Item' 통계 시작");
        logger.info("통계일={" + value + "}");
        
        xoStatGiftPayMethodDay(value);
        xoStatGiftSSGPayDay(value);
        xoStatGiftSaleSum(value);
        xoStatGiftUseSum(value);
        
        xoStatGiftSkuSaleSum(value);
        xoStatGiftSetSkuSaleSum(value);
        xoStatGiftCompSkuSaleSum(value);

        logger.info("'e-Gift Item' 통계 종료");
        
    }

    /**
     * 사이렌기프트 일별 SKU별 판매사용집계
     * XO_STAT_GIFT_SKU_SALE_SUM
     */
    private void xoStatGiftSkuSaleSum(String value) {
        try {
            logger.info("'XO_STAT_GIFT_SKU_SALE_SUM' 시작");

            int count = xoGiftDao.countXoStatGiftSkuSaleSum(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }

            xoGiftDao.saveXoStatGiftSkuSaleSum(value);
            bm_success_cnt++;
            logger.info("'XO_STAT_GIFT_SKU_SALE_SUM' 종료");
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_SKU_SALE_SUM'." +
                    "e={" + e + "}, cause={" + e.getCause() + "}, msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_SKU_SALE_SUM ";
        }
    }

    /**
     * 사이렌기프트 결제수단별 매출 일통계
     * XO_STAT_GIFT_PAY_METHOD_DAY
     */
    private void xoStatGiftPayMethodDay(String value) {
    	

      	
        try {
            logger.info("'XO_STAT_GIFT_PAY_METHOD_DAY' 시작");

            int count = xoGiftDao.countXoStatGiftPayMethodDay(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }
            xoGiftDao.saveXoStatGiftPayMethodDay(value);
            bm_success_cnt++;
            logger.info("'XO_STAT_GIFT_PAY_METHOD_DAY' 종료");
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_PAY_METHOD'. " +
                    "e={" + e + "}, cause={" + e.getCause() + "},msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_PAY_METHOD ";
        }
        
        
    }

    /**
     * 사이렌기프트 SSG PAY 결제 일통계
     * XO_STAT_GIFT_SSGPAY_DAY
     */
    private void xoStatGiftSSGPayDay(String value) {
        try {
            logger.info("'XO_STAT_GIFT_SSGPAY_DAY' 시작");

            int count = xoGiftDao.countXoStatGiftSSGPayDay(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }
            xoGiftDao.saveXoStatGiftSSGPayDay(value);
            logger.info("'XO_STAT_GIFT_SSGPAY_DAY' 종료");
            bm_success_cnt++;
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_PAY_METHOD'. " +
                    "e={" + e + "}, cause={" + e.getCause() + "},msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_SSGPAY_DAY ";
        }
    }

    /**
     * 사이렌기프트 일별 판매집계
     * XO_STAT_GIFT_SALE_SUM
     */
    private void xoStatGiftSaleSum(String value) {
        try {
            logger.info("'XO_STAT_GIFT_SALE_SUM' 시작");
            int count = xoGiftDao.countXoStatGiftSaleSum(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }
            xoGiftDao.saveXoStatGiftSaleSum(value);
            bm_success_cnt++;
            logger.info("'XO_STAT_GIFT_SALE_SUM' 종료");
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_SALE_SUM'. " +
                    "e={" + e + "}, cause={" + e.getCause() + "},msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_SALE_SUM "; 
        }
    }

    /**
     * 사이렌기프트 일별 사용집계
     * XO_STAT_GIFT_USE_SUM
     */
    private void xoStatGiftUseSum(String value) {

        try {
            logger.info("'XO_STAT_GIFT_USE_SUM' 시작");
            int count = xoGiftDao.countXoStatGiftUseSum(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }

            List<GiftOrderHistoryDto> histories = xoGiftDao.findStoreTradeHistoryFromXoGiftOrderHistory(value);
            xoGiftDao.findFromMsrTrdXoGiftListAndSaveIntoXoGiftMsrTrdList(histories);

            List<GiftOrderHistoryDto> notExists = xoGiftDao.findExistInXoGiftOrderHistoryButNotInMsrTrdXoGiftList(value);
            xoGiftDao.findFromMsrDupTrdXoGiftListAndSaveIntoXoGiftMsrTrdList(notExists);

            xoGiftDao.saveXoStatGiftUseSum(value);
            bm_success_cnt++;
            logger.info("'XO_STAT_GIFT_USE_SUM' 종료");
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_SALE_SUM'. " +
                    "e={" + e + "}, cause={" + e.getCause() + "},msg={" + e.getMessage() + "}, trace={" + Arrays.toString(e.getStackTrace()) + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_USE_SUM "; 
        }
    }
    

    /**
     * e-Gift Item 일별 세트상품별 판매사용집계 테이블
     * XO_STAT_GIFT_SKU_SALE_SUM
     */
    private void xoStatGiftSetSkuSaleSum(String value) {
        try {
            logger.info("'XO_STAT_GIFT_SET_SKU_SALE_SUM' 시작");

            int count = xoGiftDao.countXoStatGiftSetSkuSaleSum(value);
            if (count > 0) {
                logger.info("STAT_DATE=" + value + " already batched.");
                return;
            }

            xoGiftDao.saveXoStatGiftSetSkuSaleSum(value);
            bm_success_cnt++;
            logger.info("'XO_STAT_GIFT_SET_SKU_SALE_SUM' 종료");
        }
        catch (Exception e) {
            logger.error("[Exception] 'XO_STAT_GIFT_SKU_SALE_SUM'." +
                    "e={" + e + "}, cause={" + e.getCause() + "}, msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_SET_SKU_SALE_SUM "; 
        }
    }
    
    /**
     * e-Gift Item 일별 세트구성 품목 판매집계 테이블 
     * XO_STAT_GIFT_SKU_SALE_SUM
     */
    private void xoStatGiftCompSkuSaleSum(String value) {
    	try {
    		logger.info("'XO_STAT_GIFT_COMP_SKU_SALE_SUM' 시작");
    		
    		int count = xoGiftDao.countXoStatGiftCompSkuSaleSum(value);
    		if (count > 0) {
    			logger.info("STAT_DATE=" + value + " already batched.");
    			return;
    		}
    		
    		xoGiftDao.saveXoStatGiftCompSkuSaleSum(value);
    		bm_success_cnt++;
    		logger.info("'XO_STAT_GIFT_COMP_SKU_SALE_SUM' 종료");
    	}
    	catch (Exception e) {
    		logger.error("[Exception] 'XO_STAT_GIFT_SKU_SALE_SUM'." +
    				"e={" + e + "}, cause={" + e.getCause() + "}, msg={" + e.getMessage() + "}");
            bm_error_cnt++;
            bm_error_target+="XO_STAT_GIFT_COMP_SKU_SALE_SUM "; 
    	}
    }

    public static void main(String[] args) {
    	

        /********************************************
      	 BATCH MONITORING START
      	 ********************************************/
      	DataHelper helper = new DataHelper();
      	try {
      		bm_start_cnt = helper.startMonitor(bm_today, 1, "GIFT_STATISTICS_SERVICE", "GIFT_STATISTICS_DETAIL");
      	} catch (XOException e) {
      		bm_is_insert_fail = true;
      		logger.error(e.getMessage(), e);
      	}
      	
        String date;
        if (args.length == 0) {
            Date yesterday = DateUtils.addDays(new Date(), -1);
            date = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(yesterday);
        }
        else {
            date = args[0];
        }

        new GiftStatistics().start(date);
        
        
		/********************************************
		 BATCH MONITORING END
		 ********************************************/
		try {
			if (bm_is_insert_fail) {
				bm_is_insert_fail = false;
				
			} else {
				helper.endMonitor(bm_today, bm_start_cnt , "GIFT_STATISTICS_SERVICE", "GIFT_STATISTICS_DETAIL" ,7, bm_success_cnt, bm_error_cnt,bm_error_target);
			}
		} catch (XOException e) {
			logger.error(e.getMessage(), e);
		}
		
    }
}
