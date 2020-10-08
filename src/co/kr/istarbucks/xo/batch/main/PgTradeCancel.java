package co.kr.istarbucks.xo.batch.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.inicis.inipay.INIpay50;

import co.kr.istarbucks.xo.batch.common.pg.NicePgService;
import co.kr.istarbucks.xo.batch.common.pg.SmartroPgService;
import co.kr.istarbucks.xo.batch.common.pg.dto.NiceResultDto;
import co.kr.istarbucks.xo.batch.common.pg.dto.SmartroPgInfoDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DataCode;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;

/**
 * PG 승인 대사 취소 배치 PgTradeCancel
 * 결제승인일자만있는 건에 대해서만 취소한다.
 * 결제취소일자가 있는 건에서 대해서는 조회하지 않는다.
 * @author FIC04749
 *
 */
public class PgTradeCancel {
   
    private final Logger ptLogger = Logger.getLogger("PgTradeCancel");

    private Configuration conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
    
    private String resultCode = "";
    private String resultMsg = "";
    private String logTitle = "";

    // 이니시스 HOME DIR
    private final String iniHomeDir           = conf.getString("inipay.homeDirectory");             // 사이렌오더, 오픈 Api PG HOME DIR
    private final String chargeIniHomeDir     = conf.getString("charge.inipay.homeDirectory");      // 일반충전 PG HOME DIR
    private final String autoChargeIniHomeDir = conf.getString("auto.charge.inipay.homeDirectory"); // 자동충전 PG HOME DIR
    private final String giftCardIniHomeDir   = conf.getString("eGift.card.inipay.homeDirectory");  // e-Gift Card PG HOME DIR
    private final String giftItemIniHomeDir   = conf.getString("eGift.item.inipay.homeDirectory");  // e-Gift Item PG HOME DIR
    
    // 이니시스 상점 키
    private final String chargeIniMid     = conf.getString("charge.inipay.mid");              // 일반충전 PG 상점키
    private final String chargeKeyPwd     = conf.getString("charge.inipay.keyPassWord");      // 일반충전 PG 접속 비밀번호
    private final String autoChargeIniMid = conf.getString("auto.charge.inipay.mid");         // 자동충전 PG 상점키
    private final String autoChargeKeyPwd = conf.getString("auto.charge.inipay.keyPassWord"); // 자동충전 PG 접속 비밀번호
    private final String giftCardIniMid   = conf.getString("eGift.card.inipay.mid");          // e-Gift Card PG 상점키
    private final String giftCardKeyPwd   = conf.getString("eGift.card.inipay.keyPassWord");  // e-Gift Card PG 접속 비밀번호
    private final String sirenOrderIniMid = conf.getString("inipay.mid");                     // 사이렌오더 PG 상점키
    private final String sirenOrderKeyPwd = conf.getString("inipay.keyPassWord");             // 사이렌오더 PG 접속 비밀번호
    private final String openApiIniMid    = conf.getString("sam.inipay.mid");                 // 오픈 Api PG 상점키
    private final String openApiKeyPwd    = conf.getString("sam.inipay.keyPassWord");         // 오픈 Api PG 접속 비밀번호
    private final String giftItemMid      = conf.getString("eGift.item.inipay.mid");          // e-Gift Item PG 상점키
    private final String giftItemKeyPwd   = conf.getString("eGift.item.inipay.keyPassWord");  // e-Gift Item PG 접속 비밀번호
    
    // 나이스 상점 키
    private final String niceChargeIniMid     = conf.getString("charge.nice.mid");             // 나이스 일반충전 PG 상점키
    private final String niceChargeKeyPwd     = conf.getString("charge.nice.keyPassWord");     // 나이스 일반충전 PG 접속 비밀번호
    private final String niceGiftCardIniMid   = conf.getString("eGift.card.nice.mid");         // 나이스 e-Gift Card PG 상점키
    private final String niceGiftCardKeyPwd   = conf.getString("eGift.card.nice.keyPassWord"); // 나이스 e-Gift Card PG 접속 비밀번호
    private final String niceSirenOrderIniMid = conf.getString("xo.ssg.nice.mid");             // 나이스 사이렌오더 PG 상점키
    private final String niceSirenOrderKeyPwd = conf.getString("xo.ssg.nice.cancelPassWord");  // 나이스 사이렌오더 PG 접속 비밀번호
    private final String niceGiftItemMid      = conf.getString("eGift.item.nice.mid");         // 나이스 e-Gift Item PG 상점키
    private final String niceGiftItemKeyPwd   = conf.getString("eGift.item.nice.keyPassWord"); // 나이스 e-Gift Item PG 접속 비밀번호
    
    private String failReason = "";	// 실패 사유
    
    private final String loggerTitle;
    private final PaymentCancelXoMgr paymentCancelXoMgr;
    private List<Map<String, Object>> pgTradeCancelList;
    private List<Map<String, Object>> pgInisisTradeCancelList = new ArrayList<Map<String,Object>>();
    private List<Map<String, Object>> pgNiceTradeCancelList = new ArrayList<Map<String,Object>>();
    private List<Map<String, Object>> pgSmartroTradeCancelList = new ArrayList<Map<String,Object>>();
    private final SmartroPgService smartroPgService;
    
    // 스마트로 PG 정보
    private SmartroPgInfoDto pgChargeInfoDto;     // 스마트로 일반충전 PG정보
    private SmartroPgInfoDto pgAutoChargeInfoDto; // 스마트로 자동충전 PG정보
    private SmartroPgInfoDto pgEgiftCardInfoDto;  // 스마트로 e-Gift Card PG정보
    private SmartroPgInfoDto pgSirenorderInfoDto; // 스마트로 사이렌오더 PG정보
    private SmartroPgInfoDto pgWholecakeInfoDto;  // 스마트로 홀케이크 PG정보
    private SmartroPgInfoDto pgEgiftItemInfoDto;  // 스마트로 e-Gift Item PG정보
    
    public PgTradeCancel () {
        this.paymentCancelXoMgr = new PaymentCancelXoMgr ();
        this.smartroPgService = new SmartroPgService();

        this.pgChargeInfoDto     = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_CHARGE);
        this.pgAutoChargeInfoDto = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_AUTOCHARGE);
        this.pgEgiftCardInfoDto  = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_EGIFT_CARD);
        this.pgSirenorderInfoDto = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_SIRENORDER);
        this.pgWholecakeInfoDto  = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_WHOLECAKE);
        this.pgEgiftItemInfoDto  = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_EGIFT_ITEM);

        this.loggerTitle = "[PgTradeCancel] ";
    }
    
    /**
     * PG 승인 대사 배치 취소 배치
     * @param cancelDate
     */
    public void start (String stlmnCnsntDate) {
    	long startTimeTotal = System.currentTimeMillis ();
        
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "START");

        // 1. PG 승인 대사 배치 취소 대상 조회
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. PG 승인 대사 배치 취소 대상 조회");
        this.getPgTradeCancelList(stlmnCnsntDate);
        
        // 2. 결제 취소 대상 분류(이니시스, 나이스, 스마트로)
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. 결제 취소 대상 분류(이니시스, 나이스, 스마트로)");
        this.divisionPgTradeCancelList();
        
        // 3. 이니시스 대상 취소
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "3. 이니시스 대상 취소");        
        this.pgInisisTradeCancel();
        
        // 4. 나이스 대상 취소
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "4. 나이스 대상 취소");
        this.pgNiceTradeCancel();
        
        // 5. 스마트로 대상 취소
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "5. 스마트로 대상 취소");
        this.pgSmartroTradeCancel();
        
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "END :" + ( System.currentTimeMillis () - startTimeTotal ) + "ms");

    }

	/**
     * 나이스 대상 취소
     */
    private void pgNiceTradeCancel() { 
    	
    	String order;
    	String mid;
    	String tid;    	
    	String pCancelAmt;
    	String cancelPwd;
    	String pMid;
    	
    	for(Map<String, Object> pgNiceTradeCancel : pgNiceTradeCancelList) {

    		order      = String.valueOf(pgNiceTradeCancel.get("pgcmOrderId")); // 주문번호 
    		mid        = String.valueOf(pgNiceTradeCancel.get("pgcmShopId"));  // MID
    		tid        = String.valueOf(pgNiceTradeCancel.get("pgDlngNo"));    // TID
    		pCancelAmt = String.valueOf(pgNiceTradeCancel.get("pgStlmnAmnt")); // 취소금액
    		cancelPwd  = ""; // 초기화
    		pMid       = ""; // 초기화
    		
    		// 일반 충전
    		if(StringUtils.equals(this.niceChargeIniMid, mid)) {
    			pMid      = this.niceChargeIniMid;
    			cancelPwd = this.niceChargeKeyPwd;
    	
    		// e-Gift Card
    		} else if(StringUtils.equals(this.niceGiftCardIniMid, mid)) {
    			pMid      = this.niceGiftCardIniMid;
    			cancelPwd = this.niceGiftCardKeyPwd;
    			
    		// 사이렌오더 
    		} else if(StringUtils.equals(this.niceSirenOrderIniMid, mid)) {
    			pMid      = this.niceSirenOrderIniMid;
    			cancelPwd = this.niceSirenOrderKeyPwd;
    			
    		// e-Gift Item
    		} else if(StringUtils.equals(this.niceGiftItemMid, mid)) {
    			pMid      = this.niceGiftItemMid;
    			cancelPwd = this.niceGiftItemKeyPwd; 
    		}
    		
    		// NICE 취소요청(PG 승인대사 배치)
			NiceResultDto niceDto = new NicePgService().niceTradeCancelProcess(order, pMid, cancelPwd, tid, pCancelAmt);
			
			this.resultCode = niceDto.getModResultCode();
			this.resultMsg = niceDto.getResultMsg();
			
			if(StringUtils.equals(niceDto.getModResultCode(), DataCode.NICE_CANCEL_SUCCESS)) {
				// 취소처리결과 업데이트
				try {
					pgNiceTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : 정상, 02 :  비정상, 03 : 예외
					pgNiceTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);

					this.paymentCancelXoMgr.updatePgTradeCancel(pgNiceTradeCancel);
					this.ptLogger.info("나이스 대상 TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("나이스 대상 취소 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
				} catch (SQLException e) {
					pgNiceTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : 정상, 02 :  비정상, 03 : 예외
					this.ptLogger.error("나이스 대상 TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
				}
			} else {
				pgNiceTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : 정상, 02 :  비정상, 03 : 예외
				pgNiceTradeCancel.put("cncltPrcsgRsn", this.resultMsg);
				
				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgNiceTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("나이스 대상 TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("나이스 대상 TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("나이스 대상 취소 예외 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
			}			
    	}
	}

	/**
     * 이니시스 대상 취소
     */
    private void pgInisisTradeCancel() {		

    	boolean cancelSuccess = false;

    	for(Map<String, Object> pgInisisTradeCancel : pgInisisTradeCancelList) {
    		
    		// 이니시스 취소 처리
    		cancelSuccess = this.pgInisisCancel(pgInisisTradeCancel);

    		if(cancelSuccess) {
    			// 취소처리결과 업데이트
    			try {
    				pgInisisTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : 정상, 02 :  비정상, 03 : 예외					
    				pgInisisTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);
    				this.paymentCancelXoMgr.updatePgTradeCancel(pgInisisTradeCancel);
					this.ptLogger.info("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("이니시스 대상 취소 메시지 : 정상취소");

    			} catch (SQLException e) {
    				pgInisisTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : 정상, 02 :  비정상, 03 : 예외
					this.ptLogger.error("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
    			}
    		} else {
    			pgInisisTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : 정상, 02 :  비정상, 03 : 예외
    			pgInisisTradeCancel.put("cncltPrcsgRsn", this.resultMsg);

				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgInisisTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("이니시스 대상 취소 예외 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
    		}
    	}
	}

    /**
     * 이니시스 취소 처리
     * @param pgInisisTradeCancel
     * @return
     */
	private boolean pgInisisCancel(Map<String, Object> pgInisisTradeCancel) {
		
        INIpay50 inipay = new INIpay50 ();
        
        inipay.SetField ("type", "cancel");
        inipay.SetField ("debug", "true");
        
        String mid = String.valueOf(pgInisisTradeCancel.get("pgcmShopId"));

        // 일반 충전
		if(StringUtils.equals(this.chargeIniMid, mid)) {
			inipay.SetField ("inipayhome", this.chargeIniHomeDir);
        	inipay.SetField ("admin",      this.chargeKeyPwd);
        	inipay.SetField ("mid",        this.chargeIniMid);
        	
        // 자동충전
		} else if(StringUtils.equals(this.autoChargeIniMid, mid)) {
			inipay.SetField ("inipayhome", this.autoChargeIniHomeDir);
        	inipay.SetField ("admin",      this.autoChargeKeyPwd);
        	inipay.SetField ("mid",        this.autoChargeIniMid);
        	
        // e-Gift Card
		} else if(StringUtils.equals(this.giftCardIniMid, mid)) {
			inipay.SetField ("inipayhome", this.giftCardIniHomeDir);
        	inipay.SetField ("admin",      this.giftCardKeyPwd);
        	inipay.SetField ("mid",        this.giftCardIniMid);				
		
        // 사이렌오더
		} else if(StringUtils.equals(this.sirenOrderIniMid, mid)) {
			inipay.SetField ("inipayhome", this.iniHomeDir);
        	inipay.SetField ("admin",      this.sirenOrderKeyPwd);
        	inipay.SetField ("mid",        this.sirenOrderIniMid);
        
        // openApi
		} else if(StringUtils.equals(this.openApiIniMid, mid)) {
			inipay.SetField ("inipayhome", this.iniHomeDir);
        	inipay.SetField ("admin",      this.openApiKeyPwd);
        	inipay.SetField ("mid",        this.openApiIniMid);
        	
        // e-Gift Item
		} else if(StringUtils.equals(this.giftItemMid, mid)) {
			inipay.SetField ("inipayhome", this.giftItemIniHomeDir);
        	inipay.SetField ("admin",      this.giftItemKeyPwd);
        	inipay.SetField ("mid",        this.giftItemMid);
		}

        inipay.SetField ("tid", pgInisisTradeCancel.get("pgDlngNo"));
        inipay.SetField ("cancelmsg", "PG 승인 대사 배치 결제 취소"); // 취소사유
        
        inipay.startAction ();
        
        this.resultCode = StringUtils.defaultString (inipay.GetResult ("ResultCode"));
        this.resultMsg = StringUtils.defaultString (inipay.GetResult ("ResultMsg"));
        
        if(StringUtils.equals(this.resultCode, DataCode.RESULT_SUCCESS_CODE)) {
			this.ptLogger.info("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
			this.ptLogger.info("이니시스 대상 취소 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
			return true;
        } else {
        	this.ptLogger.info("이니시스 대상 TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
        	this.ptLogger.info("이니시스 취소 예외 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
            return false;
        }
    }

	/**
     * 결제 취소 대상 분류(이니시스, 나이스, 스마트로)
     */
    private void divisionPgTradeCancelList() {
    	String pgcmCode; 
    	int    etcPGCnt = 0; // 미분류 PG 카운트
    	
        for(Map<String, Object> pgTradeCancel : pgTradeCancelList) {
        	pgcmCode = String.valueOf(pgTradeCancel.get("pgcmCode"));
        	
        	// 이니시스 
        	if(StringUtils.equals(DataCode.INISYS_PGCM_CODE, pgcmCode)) {
        		pgInisisTradeCancelList.add(pgTradeCancel);
        		
        	// 나이스
        	} else if(StringUtils.equals(DataCode.NICE_PGCM_CODE, pgcmCode)) {
        		pgNiceTradeCancelList.add(pgTradeCancel);

        	// 스마트로
        	} else if(StringUtils.equals(DataCode.SMARTRO_PGCM_CODE, pgcmCode)) {        		
        		String mid = String.valueOf(pgTradeCancel.get("pgcmShopId"));
        		
        		// 일반충전
        		if(StringUtils.equals(this.pgChargeInfoDto.getPgcmShopId(), mid)) { 
        			pgTradeCancel.put("pgInfoDto", this.pgChargeInfoDto);
        		// 자동충전
        		} else if(StringUtils.equals(this.pgAutoChargeInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgAutoChargeInfoDto);
        		// E-Gift Card
        		} else if(StringUtils.equals(this.pgEgiftCardInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgEgiftCardInfoDto);
        		// 사이렌오더
        		} else if(StringUtils.equals(this.pgSirenorderInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgSirenorderInfoDto);
        		// 홀케이크
        		} else if(StringUtils.equals(this.pgWholecakeInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgWholecakeInfoDto);        			
        		// e-Gift Item
        		} else if(StringUtils.equals(this.pgEgiftItemInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgEgiftItemInfoDto);
        		}
        		
        		pgSmartroTradeCancelList.add(pgTradeCancel);

       		// PG사 코드가 분류 되지 않는 항목 조회시  로깅 처리
        	} else {
        		
        		etcPGCnt++;
        		this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. 미분류 PG사 코드 결제 정보 (" 
        				+ "PG사 코드 : " + pgTradeCancel.get("pgcmCode").toString().replaceAll("\n|\r", "") 
        				+ ", PG 거래번호 : " + pgTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", "")
        				+ ", 결제 유형 : " + pgTradeCancel.get("stlmnTypeName").toString().replaceAll("\n|\r", "")
        				+ ", 결제 수단 명 : " + pgTradeCancel.get("stlmnMeanName").toString().replaceAll("\n|\r", "")
        				+ ", 결제 금액 : " + pgTradeCancel.get("pgStlmnAmnt").toString().replaceAll("\n|\r", "")
        		+ ")");
        		
        	}
        }
        
    	this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.1 결제 취소 대상(이니시스) : " + this.pgInisisTradeCancelList.size());
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.2 결제 취소 대상(나이스) : " + this.pgNiceTradeCancelList.size());
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.3 결제 취소 대상(스마트로) : " + this.pgSmartroTradeCancelList.size());

        // PG사 코드가 분류 되지 않는 항목 존재시 로깅 처리
        if(etcPGCnt != 0) {
        	this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.4 결제 취소 대상(미분류 PG사) : " + etcPGCnt);
        }

	}

	/**
     * PG 승인 대사 배치 취소 대상 조회
     * @param cancelDate
     */
    private void getPgTradeCancelList(String stlmnCnsntDate) {
    	
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("stlmnCnsntDate", stlmnCnsntDate);
            
            // PG 승인 대사 배치 취소 대상 조회
            this.pgTradeCancelList = this.paymentCancelXoMgr.getPgTradeCancelList(dbMap);
            this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1.1 PG 승인 대사  총 취소 대상 : " + this.pgTradeCancelList.size () + "건");

        } catch (SQLException e ) {
            this.ptLogger.error (this.loggerTitle.replaceAll("\n|\r", ""), e);
        }

	}

    /**
     * 스마트로 대상 취소
     */
    private void pgSmartroTradeCancel() {

    	boolean cancelSuccess = false;

    	for(Map<String, Object> pgSmartroTradeCancel : pgSmartroTradeCancelList) {

    		// 스마트로 취소 처리
    		cancelSuccess = this.pgSmartroCancel(pgSmartroTradeCancel);

    		if(cancelSuccess) {
    			// 취소처리결과 업데이트
    			try {
    				pgSmartroTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : 정상, 02 :  비정상, 03 : 예외					
    				pgSmartroTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);
    				this.paymentCancelXoMgr.updatePgTradeCancel(pgSmartroTradeCancel);
					this.ptLogger.info("스마트로 대상 TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("스마트로 대상 취소 메시지 : 정상취소");

    			} catch (SQLException e) {
    				pgSmartroTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : 정상, 02 :  비정상, 03 : 예외
					this.ptLogger.error("스마트로 대상 TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
    			}
    		} else {
    			pgSmartroTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : 정상, 02 :  비정상, 03 : 예외
    			pgSmartroTradeCancel.put("cncltPrcsgRsn", this.resultMsg);

				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgSmartroTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("스마트로 대상 TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB 에러 : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("스마트로 대상 TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("스마트로 대상 취소 예외 메시지 : " +  this.resultMsg.replaceAll("\n|\r", ""));
    		}
    	}
	}
    
    /**
     * 스마트로 취소 처리
     * @param pgSmartroTradeCancel
     * @return
     */
	private boolean pgSmartroCancel(Map<String, Object> pgSmartroTradeCancel) {

		boolean cancelSuccess = false;
		
    	String order        = String.valueOf(pgSmartroTradeCancel.get("pgcmOrderId")); // 주문번호
    	String tid          = String.valueOf(pgSmartroTradeCancel.get("pgDlngNo"));    // TID 	
    	String pCancelAmt   = String.valueOf(pgSmartroTradeCancel.get("pgStlmnAmnt")); // 취소금액
		String cancelReason = DataCode.PG_TRAD_CANCEL_REASONS;

		SmartroPgInfoDto pgInfoDto = (SmartroPgInfoDto) pgSmartroTradeCancel.get("pgInfoDto");

		Map<String, String> result = smartroPgService.pgCancelProcessSmartro(tid, order, cancelReason, this.loggerTitle, pCancelAmt, pgInfoDto);

		// 스마트로 PG 전달 결과기 빈값이 아닌 경우 처리 
		if(MapUtils.isNotEmpty(result)) {
			this.resultCode = result.get("resultCode");
			this.resultMsg  = result.get("resultMsg");
	
			if(StringUtils.equals(result.get("resultCode"), DataCode.RESULT_SUCCESS_CODE)) {
				cancelSuccess = true;
			} else {
				cancelSuccess = false;
			}

		// 스마트로 PG에서 전달 받은 값이 없는 경우 메시지 처리
		} else {
			this.resultMsg = DataCode.PG_TRAD_SMARTRO_CANCEL_ERR;
			cancelSuccess = false;
		}
		
		return cancelSuccess;
	}

	public static void main( String[] args) {
        PgTradeCancel pgTradeCancel = new PgTradeCancel();        
        
        if(args.length > 1) {
        	pgTradeCancel.start(args[1]);
        } else {
			pgTradeCancel.start("");
        }        
    }

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public List<Map<String, Object>> getPgTradeCancelList() {
		return pgTradeCancelList;
	}

	public void setPgTradeCancelList(List<Map<String, Object>> pgTradeCancelList) {
		this.pgTradeCancelList = pgTradeCancelList;
	}

	public List<Map<String, Object>> getPgInisisTradeCancelList() {
		return pgInisisTradeCancelList;
	}

	public void setPgInisisTradeCancelList(List<Map<String, Object>> pgInisisTradeCancelList) {
		this.pgInisisTradeCancelList = pgInisisTradeCancelList;
	}

	public List<Map<String, Object>> getPgNiceTradeCancelList() {
		return pgNiceTradeCancelList;
	}

	public void setPgNiceTradeCancelList(List<Map<String, Object>> pgNiceTradeCancelList) {
		this.pgNiceTradeCancelList = pgNiceTradeCancelList;
	}

	public Logger getPtLogger() {
		return ptLogger;
	}

	public String getIniHomeDir() {
		return iniHomeDir;
	}

	public String getChargeIniHomeDir() {
		return chargeIniHomeDir;
	}

	public String getAutoChargeIniHomeDir() {
		return autoChargeIniHomeDir;
	}

	public String getGiftCardIniHomeDir() {
		return giftCardIniHomeDir;
	}

	public String getGiftItemIniHomeDir() {
		return giftItemIniHomeDir;
	}

	public String getChargeIniMid() {
		return chargeIniMid;
	}

	public String getChargeKeyPwd() {
		return chargeKeyPwd;
	}

	public String getAutoChargeIniMid() {
		return autoChargeIniMid;
	}

	public String getAutoChargeKeyPwd() {
		return autoChargeKeyPwd;
	}

	public String getGiftCardIniMid() {
		return giftCardIniMid;
	}

	public String getGiftCardKeyPwd() {
		return giftCardKeyPwd;
	}

	public String getSirenOrderIniMid() {
		return sirenOrderIniMid;
	}

	public String getSirenOrderKeyPwd() {
		return sirenOrderKeyPwd;
	}

	public String getOpenApiIniMid() {
		return openApiIniMid;
	}

	public String getOpenApiKeyPwd() {
		return openApiKeyPwd;
	}

	public String getGiftItemMid() {
		return giftItemMid;
	}

	public String getGiftItemKeyPwd() {
		return giftItemKeyPwd;
	}

	public String getNiceChargeIniMid() {
		return niceChargeIniMid;
	}

	public String getNiceChargeKeyPwd() {
		return niceChargeKeyPwd;
	}

	public String getNiceGiftCardIniMid() {
		return niceGiftCardIniMid;
	}

	public String getNiceGiftCardKeyPwd() {
		return niceGiftCardKeyPwd;
	}

	public String getNiceSirenOrderIniMid() {
		return niceSirenOrderIniMid;
	}

	public String getNiceSirenOrderKeyPwd() {
		return niceSirenOrderKeyPwd;
	}

	public String getNiceGiftItemMid() {
		return niceGiftItemMid;
	}

	public String getNiceGiftItemKeyPwd() {
		return niceGiftItemKeyPwd;
	}

	public String getLoggerTitle() {
		return loggerTitle;
	}

	public PaymentCancelXoMgr getPaymentCancelXoMgr() {
		return paymentCancelXoMgr;
	}

	public List<Map<String, Object>> getPgSmartroTradeCancelList() {
		return pgSmartroTradeCancelList;
	}

	public void setPgSmartroTradeCancelList(List<Map<String, Object>> pgSmartroTradeCancelList) {
		this.pgSmartroTradeCancelList = pgSmartroTradeCancelList;
	}

	public SmartroPgInfoDto getPgChargeInfoDto() {
		return pgChargeInfoDto;
	}

	public void setPgChargeInfoDto(SmartroPgInfoDto pgChargeInfoDto) {
		this.pgChargeInfoDto = pgChargeInfoDto;
	}

	public SmartroPgInfoDto getPgAutoChargeInfoDto() {
		return pgAutoChargeInfoDto;
	}

	public void setPgAutoChargeInfoDto(SmartroPgInfoDto pgAutoChargeInfoDto) {
		this.pgAutoChargeInfoDto = pgAutoChargeInfoDto;
	}

	public SmartroPgInfoDto getPgEgiftCardInfoDto() {
		return pgEgiftCardInfoDto;
	}

	public void setPgEgiftCardInfoDto(SmartroPgInfoDto pgEgiftCardInfoDto) {
		this.pgEgiftCardInfoDto = pgEgiftCardInfoDto;
	}

	public SmartroPgInfoDto getPgSirenorderInfoDto() {
		return pgSirenorderInfoDto;
	}

	public void setPgSirenorderInfoDto(SmartroPgInfoDto pgSirenorderInfoDto) {
		this.pgSirenorderInfoDto = pgSirenorderInfoDto;
	}

	public SmartroPgInfoDto getPgWholecakeInfoDto() {
		return pgWholecakeInfoDto;
	}

	public void setPgWholecakeInfoDto(SmartroPgInfoDto pgWholecakeInfoDto) {
		this.pgWholecakeInfoDto = pgWholecakeInfoDto;
	}

	public SmartroPgInfoDto getPgEgiftItemInfoDto() {
		return pgEgiftItemInfoDto;
	}

	public void setPgEgiftItemInfoDto(SmartroPgInfoDto pgEgiftItemInfoDto) {
		this.pgEgiftItemInfoDto = pgEgiftItemInfoDto;
	}	
}
