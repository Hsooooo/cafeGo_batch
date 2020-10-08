/*
 * @(#) $Id: PaymentCancel.java,v 1.30 2019/04/11 00:02:49 resin Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.fd.FDClient;
import co.kr.istarbucks.fd.FDCode;
import co.kr.istarbucks.fd.FDCode.FILED;
import co.kr.istarbucks.fd.FDCode.FORMULA_CODE;
import co.kr.istarbucks.fd.FDCode.RESPONSE_CODE;
import co.kr.istarbucks.fd.FDCode.TRCODE;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardStatusHistDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardUseHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.EGiftCardInfoDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.UserRegCardDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderCouponDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PaymentDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.RefundSbcDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmsHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.mgift.MGiftHttpService;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommService;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommUtil;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DateTime;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.TripleDesEGift;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.EmpMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelMsrMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;

/**
 * 자동 결제 취소 PaymentCancel.
 * @author eZEN ksy
 * @since 2014. 1. 15.
 * @version $Revision: 1.30 $
 */
public class PaymentCancel {
    
    private final Logger pcLogger = Logger.getLogger ("PC_INFO");
    private final Logger eLogger = Logger.getLogger ("PC_ERROR");
    private final Logger fLogger = Logger.getLogger ("PC_FD");
    private final Logger tLogger = Logger.getLogger ("PC_TEL");
    private final Logger cLogger = Logger.getLogger ("PC_CRITICAL");
    private final Logger mLogger = Logger.getLogger("PC_MGIFT");
    
    private Configuration conf = null;
    private final Configuration telecomConf;
    private int sbcMazBalance = 0; // 스타벅스 카드 잔액 상한선
    
    // 상용모드 : true, 개발모드 : false
    public boolean isRealMode = true;
    public String resultCode = "";
    public String resultMsg = "";
    public String logTitle = "";
    public int remainAmount = 0;
    
    public int procCnt = 0;
    public String failReason = "";
    public int refundCnt = 0;
    StringBuffer refundInfo = new StringBuffer ();
    StringBuffer buf = new StringBuffer ();
    

    private final PaymentCancelXoMgr paymentCancelXoMgr;
    private final PaymentCancelMsrMgr paymentCancelMsrMgr;
    private final EmpMgr empMgr;
    private final String loggerTitle;
    
    private List<Map<String, String>> paymentCancelList;
    
    public PaymentCancel () {
        this.paymentCancelXoMgr = new PaymentCancelXoMgr ();
        this.paymentCancelMsrMgr = new PaymentCancelMsrMgr ();
        this.loggerTitle = "[PaymentCancel] ";
        this.telecomConf = CommPropertiesConfiguration.getConfiguration ("telecom.properties");
//        this.empMgr = new EmpMgr(pcLogger, eLogger);
        this.empMgr = new EmpMgr(pcLogger);
    }
    
    public void start ( String cancelStartDate, String cancelEndDate ) {
        long startTimeTotal = System.currentTimeMillis ();
        long startTime = System.currentTimeMillis ();
        
        buf.delete (0, buf.length ()).append (this.loggerTitle).append ("START");
        this.pcLogger.info (buf.toString ());
        
        // 결제 취소 대상 조회
        this.getPaymentCancelInfo (cancelStartDate, cancelEndDate);
        
        // 결제 취소 처리
        int successCnt = 0;
        int failCnt = 0;
        StringBuffer failInfo = new StringBuffer ();
        if ( paymentCancelList != null && paymentCancelList.size () > 0 ) {
            
            this.conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
            this.sbcMazBalance = conf.getInt ("sbc.balance.maximum"); // 스타벅스 카드 잔액 상한선

            for ( Map<String, String> map : paymentCancelList ) {
                buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (this.procCnt).append (" : ").append (map.get ("user_id")).append (" : ").append (map.get ("order_no")).append (") ");
                String logTitle = buf.toString ();
                
                buf.delete (0, buf.length ()).append (logTitle).append ("START");
                this.pcLogger.info (buf.toString ());
                
                startTime = System.currentTimeMillis ();
                boolean isPayment = false;
                this.failReason = "";
                try {
                    isPayment = this.processPaymentCancel (map, logTitle);
                    
                    buf.delete (0, buf.length ()).append (logTitle).append ("RESULT : ").append (isPayment);
                    this.pcLogger.info (buf.toString ());
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                }
                
                buf.delete (0, buf.length ()).append (logTitle).append ("END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.info (buf.toString ());
                if ( isPayment ) {
                    successCnt++;
                } else {
                    failCnt++;
                    if ( failInfo.length () > 0 ) {
                        failInfo.append ("\n\t\t\t\t\t\t\t\t\t\t\t   ");
                    }
                    failInfo.append (this.procCnt)
                            .append ("|").append (map.get ("user_id"))
                            .append ("|").append (map.get ("order_no"))
                            .append ("|").append (failReason)
                            .append (";");
                }
                this.procCnt++;
            }
        }
        
        buf.delete (0, buf.length ());
        if ( StringUtils.isNotEmpty (cancelStartDate) ) {
            buf.append ("\n\t\t\t\t\t\t     ").append ("결제일 \t\t\t : ").append (cancelStartDate).append ("~").append (cancelEndDate);
        }
        buf.delete (0, buf.length ()).insert (0, "=================================================")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("결제 취소 대상 \t     : ").append (procCnt).append ("건")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("결제 취소 성공 \t     : ").append (successCnt).append ("건 (환불 : ").append (refundCnt).append ("건)")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("결제 취소 실패 \t     : ").append (failCnt).append ("건")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("환불 주문 정보 \t     : ").append (new String (refundInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("취소 실패 주문 정보 : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t =================================================");
        this.pcLogger.info (buf.toString ());
        
        buf.delete (0, buf.length ()).append (this.loggerTitle).append ("END : ").append ( ( System.currentTimeMillis () - startTimeTotal )).append ("ms");
        this.pcLogger.info (buf.toString ());
    }
    
    /**
     * 결제 취소 대상 조회
     */
    private void getPaymentCancelInfo ( String cancelStartDate, String cancelEndDate ) {
        
        try {
            Map<String, String> dbMap = new HashMap<String, String> ();
            if ( StringUtils.isNotEmpty (cancelStartDate) && StringUtils.isNotEmpty (cancelEndDate) ) {
                dbMap.put ("startDate", cancelStartDate);
                dbMap.put ("endDate", cancelEndDate);
                this.paymentCancelList = this.paymentCancelXoMgr.getPaymentCancelTargetDays (dbMap);
            } else if ( StringUtils.isNotEmpty (cancelStartDate) && StringUtils.isEmpty (cancelEndDate) ) {
                dbMap.put ("startDate", cancelStartDate);
                dbMap.put ("endDate", cancelStartDate);
                this.paymentCancelList = this.paymentCancelXoMgr.getPaymentCancelTargetDays (dbMap);
            } else {
            	// 스타벅스 카드 결제 정책 조회
            	dbMap.put("payMethod", "S");
            	Map<String, String> map = this.paymentCancelXoMgr.getPolicyPayment (dbMap);
            	
            	// 결제 취소 대상 조회
                this.paymentCancelList = this.paymentCancelXoMgr.getPaymentCancel (map);
            }
            
            buf.delete (0, buf.length ()).append (this.loggerTitle).append ("Get PaymentCancel Info : ").append (this.paymentCancelList.size ()).append ("건");
            this.pcLogger.info (buf.toString ());
            
        } catch ( Exception e ) {
            buf.delete (0, buf.length ()).append (this.loggerTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
        }
    }
    
    /**
     * 결제 취소 처리
     * @param logTitle
     * @return
     */
    public boolean processPaymentCancel ( Map<String, String> map, String logTitle ) {
        Map<String, Object> dbMap = new HashMap<String, Object> ();
        long startTime;
        int step = 1;
        
        boolean telCancelSuccess				= false;	// 통신사제휴 취소 완료 여부
        boolean isSCKCardCancel				= false; 	// SCK CARD 결제 취소 완료 여부
        boolean isRefundCard					= false; 	// 타 카드 충전 환불 여부
        boolean empSuccess					= false;  // 임직원 할인 취소 요청 여부
        boolean mGiftCancelSuccess			= false;	//모바일상품권 결제 취소 완료 여부
        int criticalCount							= 0;		// 크리티컬 flag{C:크리티컬 에러 발생}
        StringBuffer logBuf = new StringBuffer ();
        
        List<PaymentDto> sckCardPaymentList = new ArrayList<PaymentDto> (); 	// SCK 카드 결제 정보
        List<PaymentDto> newPaymentList     = new ArrayList<PaymentDto> (); 	// 통신사제휴 신규 결제 처리건
        List<PaymentDto> paymentList				= new ArrayList<PaymentDto>();	//결제정보조회
        String orderNo = map.get ("order_no");
        String empNo = "";
        
        SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
        
        MGiftHttpService mGiftHttpService = new MGiftHttpService();		//모바일상품권 제휴사 연동 서비스
        try {
            // XO 트랜잭션 시작
            xoSqlMap.startTransaction ();
            
            // step 1. XO : 주문 정보 조회 START ==============================================================
            startTime = System.currentTimeMillis ();
            OrderDto orderDto = this.paymentCancelXoMgr.getOrder (xoSqlMap, orderNo);
            
            if ( orderDto == null || StringUtils.isEmpty (orderDto.getOrder_no ()) ) {
                buf.delete (0, buf.length ()).append ("orderDto is null - ").append (orderNo);
                throw new XOException (buf.toString ());
            }
            if ( !StringUtils.equals (orderDto.getStatus (), "11") && !StringUtils.equals (orderDto.getStatus (), "22") ) {
                buf.delete (0, buf.length ()).append ("order status - ").append (orderDto.getStatus ());
                throw new XOException (buf.toString ());
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : 주문 정보 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 1. XO : 주문 정보 조회 END   ==============================================================
            
            
            // step 2. XO : 결제 정보 조회 START ==============================================================
            startTime = System.currentTimeMillis ();
            paymentList = this.paymentCancelXoMgr.getPaymentList (xoSqlMap, orderNo);
            
            if ( paymentList == null || paymentList.size () == 0 ) {
                buf.delete (0, buf.length ()).append ("paymentList is null - ").append (orderNo);
                throw new XOException (buf.toString ());
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : 결제 정보 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. XO : 결제 정보 조회 END   ==============================================================
            
            // step 3. XO : 쿠폰 정보 조회 START ==============================================================
            startTime = System.currentTimeMillis ();
            List<OrderCouponDto> couponList = this.paymentCancelXoMgr.getOrderCouponInfo (xoSqlMap, orderNo);

            if(couponList != null){
            	this.pcLogger.info(logTitle + "쿠폰정보 :: " + couponList);
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : 쿠폰 정보 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 3. XO : 쿠폰 정보 조회 END   ==============================================================
            
            // step 4. 물품형 GIFT : 결제 취소 START ===============================================================
            // 실패 시 더 이상 진행 하지 않고 실패 카운트 만 증가함, DB 원복, 실패 카운트 증가, 실패 이력 등록
            startTime = System.currentTimeMillis ();
            
            // 물품형 GIFT 취소 완료 여부
            boolean giftCancelSuccess = this.eGiftItemCencelProcess(xoSqlMap, paymentList);
        	
        	if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". e-Gift Item : 결제 취소 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
        	
        	// 물품형 GIFT 실패 시 더 이상 진행 안함
        	if(!giftCancelSuccess)
        	{
        		// F-1. 실패 시 물품형 GIFT 롤백(commitTransaction() 없이 종료하여 이전 로직에서 처리한 내용들 RollBack)
        		try {
                    xoSqlMap.endTransaction ();
                } catch ( Exception ee ) {
                    pcLogger.error(ee.getMessage(), ee);
                }
        		
        		// F-2. XO 트랜잭션 다시 시작
                xoSqlMap.startTransaction ();
        		
        		// F-3. 결제 취소 실패 이력 등록
                startTime = System.currentTimeMillis ();
                boolean giftIsOk = false;
                Map<String, Object> giftDBMap = new HashMap<String, Object> ();
                try {
                	giftDBMap.put ("orderNo", orderNo); // 주문번호
                	giftDBMap.put ("status",  "13"); 	// 상태
                	
                	// 이력 등록
                    giftIsOk = this.paymentCancelXoMgr.insertOrderHistory (xoSqlMap, giftDBMap);
                    xoSqlMap.commitTransaction (); // commit
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    if ( giftIsOk ) {
                        if ( this.pcLogger.isDebugEnabled () ) {
                            buf.delete (0, buf.length ()).append (logTitle).append ("STATUS = 13 OF insertOrderHistory OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.debug (buf.toString ());
                        }
                    } else {
                        buf.delete (0, buf.length ()).append (logTitle).append ("STATUS = 13 OF insertOrderHistory FAIL (").append (giftDBMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.info (buf.toString ());
                    }
                }
                
        		// 진행 안 함 
        		return isSCKCardCancel;
        	}
            
            // step 4. 물품형 GIFT : 결제 취소 END =================================================================

            // step 5. 임직원 할인 한도 취소 요청 START =================================================================
            if (StringUtils.isNotEmpty(orderDto.getEmp_no()) && StringUtils.isNotEmpty(orderDto.getEmp_auth_app_no())) {
                buf.delete(0, buf.length()).append(logTitle).append("stop ").append(step)
                        .append(".임직원 할인 거래 empNo=").append(orderDto.getEmp_no());
                Map<String, String> cancelResult = cancelEmpOrder(logTitle, orderDto);
                if (cancelResult.isEmpty()) {
                    empSuccess = false;
                    try {
                    	//XO 트랜잭션 종료(commitTransaction() 없이 종료하여 이전 로직에서 처리한 내용들 RollBack)
                        xoSqlMap.endTransaction ();
                    } catch ( Exception ee ) {
                        pcLogger.error(ee.getMessage(), ee);
                    }

                    // F-2. XO 트랜잭션 다시 시작
                    xoSqlMap.startTransaction ();

                    // F-3. 결제 취소 실패 이력 등록
                    startTime = System.currentTimeMillis ();
                    boolean historySuccess = false;
                    Map<String, Object> giftDBMap = new HashMap<String, Object> ();
                    try {
                        giftDBMap.put ("orderNo", orderNo); // 주문번호
                        giftDBMap.put ("status",  "13"); 	// 상태

                        // 이력 등록
                        historySuccess = this.paymentCancelXoMgr.insertOrderHistory (xoSqlMap, giftDBMap);
                        xoSqlMap.commitTransaction (); // commit
                    } catch ( Exception e ) {
                        buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                        this.pcLogger.error (buf.toString (), e);
                        this.eLogger.error (buf.toString (), e);
                    } finally {
                        if (historySuccess) {
                            if (this.pcLogger.isDebugEnabled()) {
                                buf.delete(0, buf.length()).append(logTitle).append("STATUS = 13 OF insertOrderHistory OK (orderNo=").append(orderNo).append(") END : ").append((System.currentTimeMillis() - startTime)).append("ms");
                                this.pcLogger.debug(buf.toString());
                            }
                        }
                        else {
                            buf.delete(0, buf.length()).append(logTitle).append("STATUS = 13 OF insertOrderHistory FAIL (").append(giftDBMap.toString()).append(") END : ").append((System.currentTimeMillis() - startTime)).append("ms");
                            this.pcLogger.info(buf.toString());
                        }
                    }

                    // 진행 안 함
                    return isSCKCardCancel;
                }
                else {
                    empNo = orderDto.getEmp_no();
                    empSuccess = true;
                }
            }
            // step 5. 임직원 할인 한도 취소 요청 END =================================================================

            // step 6. 모바일 상품권 결제 취소 START ===========================================================
            mGiftCancelSuccess = mGiftHttpService.setMGiftOrderCancelProcess(paymentList, logTitle, logBuf);
            if(!mGiftCancelSuccess) {
            	// XO 트랜잭션 종료(commitTransaction() 없이 종료하여 이전 로직에서 처리한 내용들 RollBack)
            	xoSqlMap.endTransaction();
            	buf.delete(0, buf.length()).append(logTitle).append("step ").append(step++)
            	.append(". 모바일 상품권 : 결제취소 실패")
            	.append(System.currentTimeMillis() - startTime).append("ms");
            	mLogger.error(buf.toString());
            	
            	//결제 취소 실패 이력 등록
                startTime = System.currentTimeMillis ();
                boolean historySuccess = false;
                Map<String, Object> mGiftDBMap = new HashMap<String, Object> ();
                try {
                	
                	//XO 트랜잭션 다시 시작
                    xoSqlMap.startTransaction ();
                	
                    mGiftDBMap.put ("orderNo", orderNo); // 주문번호
                    mGiftDBMap.put ("status",  "13"); 	// 상태

                    // 이력 등록
                    historySuccess = this.paymentCancelXoMgr.insertOrderHistory (xoSqlMap, mGiftDBMap);
                    xoSqlMap.commitTransaction (); // commit
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    if (historySuccess) {
                        if (this.pcLogger.isDebugEnabled()) {
                            buf.delete(0, buf.length()).append(logTitle).append("STATUS = 13 OF insertOrderHistory OK (orderNo=").append(orderNo).append(") END : ").append((System.currentTimeMillis() - startTime)).append("ms");
                            this.pcLogger.debug(buf.toString());
                        }
                    }
                    else {
                        buf.delete(0, buf.length()).append(logTitle).append("STATUS = 13 OF insertOrderHistory FAIL (").append(mGiftDBMap.toString()).append(") END : ").append((System.currentTimeMillis() - startTime)).append("ms");
                        this.pcLogger.info(buf.toString());
                    }
                }
            	//mGift rollBack
            	int mGiftRollback  = mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
            	if(mGiftRollback != 0) {
            		throw new Exception("Mobile Gift Rollback Error!");
            	}
            	
            	if(empSuccess) {
            		//임직원 할인 롤백
                    // 임직원 승인 번호를 재요청한 승인번호로 업데이트 해야 하기 때문에
                    // 새로운 트렌젝션을 시작함
                    xoSqlMap.endTransaction();
                    xoSqlMap.startTransaction();
                    rollbackEmp(xoSqlMap, orderNo, empNo, logTitle);
                    xoSqlMap.commitTransaction();
            	}
            	return isSCKCardCancel;
            }else {
            	if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 모바일 상품권 결제 취소 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                    this.pcLogger.debug (buf.toString ());
                }
            }
            
            // step 6. 모바일 상품권 결제 취소 END ============================================================
            
            // step 7. 통신사 : 결제 취소 START ===============================================================
            startTime = System.currentTimeMillis ();
            telCancelSuccess = this.setTelecomOrderCancel(paymentList, logTitle, logBuf);
            
            if(!telCancelSuccess) {
				// 통신사 : 결제 취소 실패시 강제 성공 처리
            	telCancelSuccess = true;
            	if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete(0, buf.length()).append(logTitle).append("step ").append(step)
                            .append(". 통신사 : 결제취소 실패 (강제성공) ")
                            .append(System.currentTimeMillis() - startTime).append("ms");
					this.pcLogger.debug (buf.toString ());
				}
            }
            
            if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 통신사 : 결제 취소 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
            // step 7. 통신사 : 결제 취소 END =================================================================
            
            
            // step 8. MSR : 회원의 등록된 스타벅스 카드 조회 START =============================================
            RefundSbcDto refundSbcDto = new RefundSbcDto ();
            if(telCancelSuccess){
	            startTime = System.currentTimeMillis ();
	            SqlMapClient msrSqlMap1 = IBatisSqlConfig.getMsrSqlMapInstance ();
	            try {
	                msrSqlMap1.startTransaction ();
	                
	                dbMap.put ("user_id", orderDto.getUser_id ());
	                dbMap.put ("msr_user_reg_card_table", "MSR_USER_REG_CARD# URC");
	                dbMap.put ("msr_card_master_table", "MSR_CARD_MASTER# CM");
	                dbMap.put ("card_state", "R"); // 등록된 카드만을 조회
	                
	                // 등록 상태 모든 카드 목록
	                List<UserRegCardDto> regCardlist = this.paymentCancelMsrMgr.getUserRegCardStateList (msrSqlMap1, dbMap);
	                dbMap.clear ();
	                
	                // 결제했던 스타벅스 카드가 현재 등록 상태에 따라 
	                if ( paymentList != null ) {
	                    payList:
	                    for ( PaymentDto dto : paymentList ) {
	                        // SCK 카드 결제가 아니면 continue
	                        if ( !StringUtils.equals (dto.getPay_method (), "S") ) {
	                            continue payList;
	                        }
	                        if ( regCardlist == null || regCardlist.size () == 0 ) {
	                            // 회원에게 등록된 SCK 카드 0건이면 결제 취소 코드 98
	                            dto.setCancel_result_code ("98");
	                            sckCardPaymentList.add (dto);
	                        } else {
	                            cardList:
	                            for ( UserRegCardDto cardDto : regCardlist ) {
	                                if ( StringUtils.equals (dto.getSbc_card_no (), cardDto.getCard_number ()) ) {
	                                    // 현재 MSR_CARD_MASTER 잔액과 결제 취소 금액의 합이 잔액상한이 넘으면 결제 취소 코드 97
	                                    int preBalance = cardDto.getBalance () + dto.getAmount ();
	                                    if ( preBalance > this.sbcMazBalance ) {
	                                        dto.setCancel_result_code ("97");
	                                    }
	                                    
	                                    sckCardPaymentList.add (dto);
	                                    continue payList;
	                                } else {
	                                    continue cardList;
	                                }
	                            } // end cardList
	                            dto.setCancel_result_code ("98"); // 결제카드가 회원에게 등록되어 있지 않으면  결제 취소 코드 98
	                            sckCardPaymentList.add (dto);
	                        }
	                    } // end payList
	                }
	            } catch ( Exception e ) {
	                throw e;
	            } finally {
	                msrSqlMap1.commitTransaction (); // (조회) 트랜젝션 커밋
	                try {
	                    msrSqlMap1.endTransaction ();
	                } catch ( Exception ee ) {
	                    pcLogger.error(ee.getMessage(), ee);
	                }
	            }
	            if ( this.pcLogger.isDebugEnabled () ) {
	                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 회원의 등록된 스타벅스 카드 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                this.pcLogger.debug (buf.toString ());
	            }
	            // step 8. MSR : 회원의 등록된 스타벅스 카드 조회 END ===========================================
	            
	            
	            // step 9. FD : 사용 취소(reload) 처리 START ==================================================
	            startTime = System.currentTimeMillis ();
	            try {
	                boolean isOk = false;
	                if ( sckCardPaymentList != null ) {
	                    for ( PaymentDto dto : sckCardPaymentList ) {
	                        if ( StringUtils.isNotEmpty (dto.getCancel_result_code ()) ) continue;
	                        
	                        // FD reload 호출
	                        if(isRealMode){
	                        	isOk = this.setFdBalance(dto.getSbc_card_no (), String.valueOf (dto.getAmount ()), "X", FDCode.ORDER_CODE.USE_CANCEL, dto.getOrder_no(), orderDto.getUser_id ());
	                        }else{
	                        	isOk = true;
	                        	this.resultCode = "00";
	                        	this.resultMsg = "FD Success";
	                        	this.remainAmount = 50000;
	                        }
	                        
	                        // FD 연동 - 결제 취소 결과 로그 ===================
	                        logBuf.delete(0, buf.length()).append (logTitle).append ("FD Reload")
	                            .append ("|").append ("orderNo=").append (orderNo)
	                            .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
	                            .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
	                            .append ("|").append (this.resultCode)
	                            .append ("|").append (this.resultMsg);
	                        this.fLogger.info (logBuf); // FD 로그
	                        this.pcLogger.info (logBuf); // 기본 로그
	                        logBuf.delete (0, logBuf.length ());
	                        // FD 연동 - 결제 취소 결과 로그 ===================
	                        
	                        dto.setCancel_result_code (this.resultCode);
	                        if ( isOk ) {
	                            dto.setSbc_remain_amt (this.remainAmount);
	                        } else {
                            	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
	                        }
	                    }
	                }
	            } catch ( Exception e ) {
	            	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
	                buf.delete (0, buf.length ()).append (this.loggerTitle).append (e.getMessage ());
	                this.pcLogger.error (buf.toString (), e);
	                this.eLogger.error (buf.toString (), e);
	            }
	            
	            // 결제된 등록카드를  FD 처리 중 Exception 발생한 경우 그 이후 결제 건 실패 목록에 추가 
	            try {
	                for ( PaymentDto dto : sckCardPaymentList ) {
	                    if ( StringUtils.equals (dto.getCancel_result_code (), "00") ) continue;
	                    
	                    // 취소 결과 코드 없는 경우
	                    if ( StringUtils.isEmpty (dto.getCancel_result_code ()) ) {
	                        dto.setCancel_result_code ("99");
	                    }
	                }
	            } catch ( Exception e ) {
	                buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
	                this.pcLogger.error (buf.toString (), e);
	                this.eLogger.error (buf.toString (), e);
	            }
	            
	            if ( this.pcLogger.isDebugEnabled () ) {
	                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". FD : 사용 취소(reload) 처리 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                this.pcLogger.debug (buf.toString ());
	            }
	            // step 9. FD : 사용 취소(reload) 처리 START ==================================================

	            // step 10. MSR : e-Gift 카드 발행 START ======================================================
	            startTime = System.currentTimeMillis ();
	            try {
	                Long paymentAmount = 0L;
	                for ( PaymentDto dto : sckCardPaymentList ) {
	                    if ( StringUtils.equals (dto.getCancel_result_code (), "00") ) continue;
	                    paymentAmount += dto.getAmount ();
	                }
	                
	                if ( paymentAmount == 0 ) {
	                    isSCKCardCancel = true;
	                    isRefundCard = false;
	                    
	                    if ( this.pcLogger.isDebugEnabled () ) {
	                        buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : e-Gift 카드 미발행 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                        this.pcLogger.debug (buf.toString ());
	                    }
	                } else {
	                    refundSbcDto.setOrder_no (orderDto.getOrder_no ()); // 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	                    refundSbcDto.setUser_id (orderDto.getUser_id ()); // 사용자아이디
	                    refundSbcDto.setAmount (paymentAmount); // 금액
	                    refundSbcDto.setSbc_type ("2"); // 구분{1-등록카드, 2-eGift}
	                    
	                    // e-Gift 카드 발행 요청
	                    buf.delete (0, buf.length ()).append (logTitle).append ("egiftCardPublish : ");
	                    refundSbcDto = this.egiftCardPublish (refundSbcDto, buf.toString ());
	                    
	                    if ( StringUtils.isNotEmpty (refundSbcDto.getTarget_card_number ()) && StringUtils.equals (refundSbcDto.getResult (), "00") ) { // 결과{00-성공, 그 외 실패}
	                        isSCKCardCancel = true;
	                        isRefundCard = true;
	                    } else {
	                        isSCKCardCancel = false;
	                    }
	                    
	                    if ( this.pcLogger.isDebugEnabled () ) {
	                        buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : e-Gift 카드 발행 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                        this.pcLogger.debug (buf.toString ());
	                    }
	                }
	            } catch ( Exception e ) {
	                buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
	                this.pcLogger.error (buf.toString (), e);
	                this.eLogger.error (buf.toString (), e);
	                isSCKCardCancel = false;
	                isRefundCard = false;
	            }
            }
            
            // e-Gift 카드 발행 실패한 경우 FD 사용 취소(reload) 처리 원복
            if ( !isSCKCardCancel ) {
                if ( sckCardPaymentList != null && sckCardPaymentList.size () > 0 ) {
                    boolean isOk = false;
                    for ( PaymentDto dto : sckCardPaymentList ) {
                        if ( StringUtils.contains ("|00|97|98|99|", dto.getCancel_result_code ()) ) continue;
                        
                        // FD voidOfReload 호출
                        isOk = this.setFdBalance (dto.getSbc_card_no (), String.valueOf (dto.getAmount ()), "C", FDCode.ORDER_CODE.REFUND, dto.getOrder_no(), orderDto.getUser_id ());
                        
                        // FD 연동 - 결제 취소 원복 결과 로그 ===================
                        logBuf.append (logTitle).append ("FD voidOfReload")
                            .append ("|").append ("orderNo=").append (orderNo)
                            .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
                            .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
                            .append ("|").append (this.resultCode)
                            .append ("|").append (this.resultMsg);
                        this.fLogger.info (logBuf); // FD 로그
                        this.pcLogger.info (logBuf); // 기본 로그
                        if ( !isOk ) {
                        	criticalCount++;
                            this.cLogger.info (logBuf); // 크리티컬 로그
                        }
                        logBuf.delete (0, logBuf.length ());
                        // FD 연동 - 결제 취소 원복 결과 로그 ===================
                    }
                }
                
                //결제 취소가 실패한 경우 이미 취소 처리한 통신사 제휴를 다시 결제처리함
                criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
                if(mGiftCancelSuccess) {
                	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                }
            } // isSCKCardCancel
            // step 10. MSR : e-Gift 카드 발행 END   ==========================================================
            

            // step 11. MSR : SBC 카드 잔액 변경 START =========================================================
            if ( telCancelSuccess &&  isSCKCardCancel ) {
                startTime = System.currentTimeMillis ();
                SqlMapClient msrSqlMap2 = IBatisSqlConfig.getMsrSqlMapInstance ();
                try {
                    // MSR 트랜잭션 시작
                    msrSqlMap2.startTransaction ();
                    
                    for ( PaymentDto dto : sckCardPaymentList ) {
                    	
                        if ( !StringUtils.equals (dto.getCancel_result_code (), "00") ) continue;
                        
                        // 오류 발생하더라도 다음 건 진행 
                        try {
                            dbMap.put ("balance", String.valueOf (dto.getSbc_remain_amt ()));
                            dbMap.put ("cardNumber", dto.getSbc_card_no ());
                            // 사용자 등록카드 잔액 변경
                            this.paymentCancelMsrMgr.setMsrUserRegCardBalanceUpd (msrSqlMap2, dbMap);
                            if ( this.pcLogger.isDebugEnabled () ) {
                                buf.delete (0, buf.length ()).append (logTitle).append ("setMsrUserRegCardBalanceUpd(cardNumber=").append (dto.getSbc_card_no ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                                this.pcLogger.debug (buf.toString ());
                            }
                            
                            dbMap.put ("historyCode", "X");
                            dbMap.put ("amount", String.valueOf (dto.getAmount ()));
                            dbMap.put ("balanceConfirmFlag", "Y");
                            dbMap.put ("formulaCode", "A");
                            dbMap.put ("orderNo", orderNo);
                            
                            // 카드 사용 이력 조회
                            CardUseHistoryDto cardUseDto = this.paymentCancelMsrMgr.getMsrCardUseHistoryForSmartOrder (msrSqlMap2, dbMap);
                            
                            dbMap.put ("branch",cardUseDto.getBranch());
                            dbMap.put ("businessDate",cardUseDto.getBusiness_date());
                            dbMap.put ("posNumber",cardUseDto.getPos_number());
                            dbMap.put ("posTrdNumber",cardUseDto.getPos_trd_number());
                            dbMap.put ("sendDate",cardUseDto.getSend_date());
                            dbMap.put ("sendTime",cardUseDto.getSend_time());
                            
                            
                            // 사용자 등록카드 잔액 변경시 히스토리 등록
                            this.paymentCancelMsrMgr.setMsrCardUseHistoryForSmartOrder (msrSqlMap2, dbMap);
                            if ( this.pcLogger.isDebugEnabled () ) {
                                buf.delete (0, buf.length ()).append (logTitle).append ("setMsrCardUseHistoryForSmartOrder(cardNumber=").append (dto.getSbc_card_no ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                                this.pcLogger.debug (buf.toString ());
                            }
                            dbMap.clear ();
                        } catch ( Exception e ) {
                            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                            this.pcLogger.error (buf.toString (), e);
                            this.eLogger.error (buf.toString (), e);
                            
                            // MSR DB Exception 크리티컬 로그 ===================
                            criticalCount++;
                            logBuf.append (logTitle).append ("MSR 카드 결제 취소 잔액 변경 실패")
                                .append ("|").append ("orderNo=").append (orderNo)
                                .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
                                .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
                                .append ("|").append ("balance=").append (dto.getSbc_remain_amt ())
                                .append ("|").append ("Exception=").append (e.getMessage ());
                            this.pcLogger.info (logBuf); // 기본 로그
                            this.cLogger.info (logBuf); // 크리티컬 로그
                            logBuf.delete (0, logBuf.length ());
                            // MSR DB Exception 크리티컬 로그 ===================
                        } // try
                    } //for
                    
                    //사용 쿠폰 취소 처리
                    if(couponList != null && couponList.size() > 0){
                    	Map<String, String> couponMap = new HashMap<String, String>();
        				String[] couponArr = null;
        				String   coupons = "";
        				
                    	for(OrderCouponDto couDto : couponList){
		                    try{
		                    	boolean isCheck = true;
		                    	
        						// 중복된 쿠폰 번호가 있는지 체크
        						if(StringUtils.isBlank(coupons)) {
        							coupons = couDto.getCoupon_number();		                    	
        						} else {
        							couponArr = coupons.split("\\,");
        							for(int i = 0; i < couponArr.length; i++) {
        								// 중복된 쿠폰 번호가 있는지 확인
        								if(couponArr[i].equals(String.valueOf(couDto.getCoupon_number()))) {
        									isCheck = false;
        								}
        							}
        							coupons = coupons + "," + couDto.getCoupon_number();
        						}
        						
        						// 중복된 쿠폰 번호가 없는 경우 업데이트
        						if(isCheck) {
    	                    		couponMap.clear();
    	                    		couponMap.put("couponNumber", 	   couDto.getCoupon_number());
    	                    		couponMap.put("whereCouponStatus", "U");
    	                    		couponMap.put("couponStatus", 	   "A");
    	                    		this.paymentCancelMsrMgr.updateCouponPublicationList(msrSqlMap2, couponMap);	
        						}
		                    }catch(Exception ex){
		                    	buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
		                        this.pcLogger.error (buf.toString (), ex);
		                        this.eLogger.error (buf.toString (), ex);
		                        
		                        // MSR DB Exception 크리티컬 로그 ===================
		                        criticalCount++;
		                        logBuf.append (logTitle).append ("MSR 쿠폰 취소 실패")
		                            .append ("|").append ("orderNo=").append (orderNo)
		                            .append ("|").append ("couponNumber=").append (couDto.getCoupon_number())
		                            .append ("|").append ("couponAmount=").append (couDto.getCoupon_dc_amt())
		                            .append ("|").append ("Exception=").append (ex.getMessage ());
		                        this.pcLogger.info (logBuf); 	// 기본 로그
		                        this.cLogger.info (logBuf); 	// 크리티컬 로그
		                        logBuf.delete (0, logBuf.length ());
		                        // MSR DB Exception 크리티컬 로그 ===================
		                    }
                    	}
                    }
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    // commit
                    msrSqlMap2.commitTransaction ();
                    try {
                        msrSqlMap2.endTransaction ();
                    } catch ( Exception ee ) {
                        pcLogger.error(ee.getMessage(), ee);
                    }
                }
                if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : SBC 카드 잔액 변경 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                    this.pcLogger.debug (buf.toString ());
                }
            }
            // step 11. MSR : SBC 카드 잔액 변경 END   =========================================================
            
            // step 12. XO : 주문정보 결제 취소 상태로 변경 START ================================================
            startTime = System.currentTimeMillis ();
            boolean isOk = false;
            
            String status = "";
            if ( isSCKCardCancel ) {
                status = "12"; // 결제 취소 성공
            } else if ( !isSCKCardCancel ) {
                status = "13"; // 결제 취소 실패
            } else {
                status = "14"; // 결제 취소 부분 실패
            }
            
            // 결제 취소 실패인 경우 주문상태 업데이트 하지 않음.
            if ( !StringUtils.equals (status, "13") ) {
                // 주문 정보 업데이트
                isOk = false;
                startTime = System.currentTimeMillis ();
                try {
                    dbMap.put ("orderNo", 	   orderNo);
                    dbMap.put ("status", 	   status); 		// 상태
                    if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//크리티컬 에러 여부
                    }
                    isOk = this.paymentCancelXoMgr.updateOrder (xoSqlMap, dbMap);
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    if ( isOk ) {
                        if ( this.pcLogger.isDebugEnabled () ) {
                            buf.delete (0, buf.length ()).append (logTitle).append ("updateOrder OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.debug (buf.toString ());
                        }
                    } else {
                        buf.delete (0, buf.length ()).append (logTitle).append ("updateOrder FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.info (buf.toString ());
                    }
                }
            // 결제 취소 실패인 경우 critical_log가 있는 경우 업데이트
            }else{
            	try {
                    dbMap.put ("orderNo", 	   orderNo);
                    if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//크리티컬 에러 여부
                    }
                    isOk = this.paymentCancelXoMgr.updateOrder (xoSqlMap, dbMap);
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    if ( isOk ) {
                        if ( this.pcLogger.isDebugEnabled () ) {
                            buf.delete (0, buf.length ()).append (logTitle).append ("updateOrder OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.debug (buf.toString ());
                        }
                    } else {
                        buf.delete (0, buf.length ()).append (logTitle).append ("updateOrder FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.info (buf.toString ());
                    }
                }
            }
            

            // 주문 정보 변경 이력 업데이트
            startTime = System.currentTimeMillis ();
            isOk = false;
            try {
                dbMap.put ("orderNo", orderNo); // 주문번호
                dbMap.put ("status",  status); 	// 상태
                isOk = this.paymentCancelXoMgr.insertOrderHistory (xoSqlMap, dbMap);
            } catch ( Exception e ) {
                buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                this.pcLogger.error (buf.toString (), e);
                this.eLogger.error (buf.toString (), e);
            } finally {
                if ( isOk ) {
                    if ( this.pcLogger.isDebugEnabled () ) {
                        buf.delete (0, buf.length ()).append (logTitle).append ("insertOrderHistory OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.debug (buf.toString ());
                    }
                } else {
                    buf.delete (0, buf.length ()).append (logTitle).append ("insertOrderHistory FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                    this.pcLogger.info (buf.toString ());
                }
            }
            
            // 결제 정보 업데이트 처리
            List<PaymentDto> paymentDBList = new ArrayList<PaymentDto> ();
            // 통신사 취소 처리건 등록
            for(PaymentDto payDto : paymentList){
            	if( (StringUtils.equals(payDto.getPay_method(), "K") || StringUtils.equals(payDto.getPay_method(), "U") 
            			|| StringUtils.equals(payDto.getPay_method(), "T")|| StringUtils.isNotEmpty(payDto.getPrcm_frst_code())) 
            		  && StringUtils.equals(payDto.getCancel_result_code(), "00")){
            		paymentDBList.add(payDto);
            	}
            }
            // 스타벅스카드 처리건 등록
            if ( isSCKCardCancel ) {
                paymentDBList.addAll (sckCardPaymentList);
            }
            
            if ( paymentDBList != null && paymentDBList.size () != 0 ) {
                for ( PaymentDto dto : paymentDBList ) {
                    if ( StringUtils.isEmpty (dto.getCancel_result_code ()) ) {
                        dto.setCancel_result_code ("99");
                    }
                    
                    // 결제 정보 업데이트
                    startTime = System.currentTimeMillis ();
                    isOk = false;
                    try {
                        isOk = this.paymentCancelXoMgr.updatePayment (xoSqlMap, dto);
                    } catch ( Exception e ) {
                        buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                        this.pcLogger.error (buf.toString (), e);
                        this.eLogger.error (buf.toString (), e);
                    } finally {
                        if ( isOk ) {
                            if ( this.pcLogger.isDebugEnabled () ) {
                                buf.delete (0, buf.length ()).append (logTitle).append ("updatePayment OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                                this.pcLogger.debug (buf.toString ());
                            }
                        } else {
                            buf.delete (0, buf.length ()).append (logTitle).append ("updatePayment FAIL (").append (dto.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.info (buf.toString ());
                        }
                    }
                }
            }
            
            //결제 취소시 에러 발생건으로 인해 롤백(통신사)된 데이터 등록
            if(newPaymentList != null && newPaymentList.size() > 0){
            	try{
	            	for(PaymentDto payDto : newPaymentList){
	            		this.paymentCancelXoMgr.insertPaymentOfPaymentHist(xoSqlMap, payDto);
	            		//모바일 상품권 승인번호 업데이트
	            		
	            	}
            	}catch(Exception ex){
            		buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
                    this.pcLogger.error (buf.toString (), ex);
                    this.eLogger.error (buf.toString (), ex);
                    this.cLogger.info (buf.toString (), ex); 	// 크리티컬 로그
            	}
            }
            
            if ( isRefundCard ) {
                // 타 카드 충전 환불 등록
                startTime = System.currentTimeMillis ();
                isOk = false;
                try {
                    refundCnt++;
                    if ( refundInfo.length () > 0 ) {
                        refundInfo.append ("\n\t\t\t\t\t\t\t\t\t\t\t   ");
                    }
                    refundInfo.append (this.procCnt)
                            .append ("|").append (refundSbcDto.getUser_id ())
                            .append ("|").append (refundSbcDto.getOrder_no ())
                            .append ("|").append (refundSbcDto.getTarget_card_number ())
                            .append (";");
                    
                    isOk = this.paymentCancelXoMgr.insertRefundSbc (xoSqlMap, refundSbcDto);
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                } finally {
                    if ( isOk ) {
                        if ( this.pcLogger.isDebugEnabled () ) {
                            buf.delete (0, buf.length ()).append (logTitle).append ("insertRefundSbc OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.debug (buf.toString ());
                        }
                    } else {
                        buf.delete (0, buf.length ()).append (logTitle).append ("insertRefundSbc FAIL (").append (refundSbcDto.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.info (buf.toString ());
                    }
                }
                
                // eGift 카드 등록 SMS 발송
                if ( StringUtils.defaultString (orderDto.getPhone ()).length () > 10 ) {
                    startTime = System.currentTimeMillis ();
                    isOk = false;
                    try {
                        String callback = this.conf.getString ("refund.sms.callback");
                        String subject = this.conf.getString ("refund.sms.subject");
                        String content = this.conf.getString ("refund.sms.content");
                        String reservationTime = this.conf.getString ("refund.sms.reservation.time");
                        String cardNumber = StringUtils.defaultString (refundSbcDto.getTarget_card_number ());
                        String pinNumber = StringUtils.defaultString (TripleDesEGift.decrypt (refundSbcDto.getPin_number ()));
                        
                        if ( StringUtils.length (cardNumber) == 16 ) {
                            String cardNumber1 = StringUtils.substring (cardNumber, 0, 4);
                            String cardNumber2 = StringUtils.substring (cardNumber, 4, 8);
                            String cardNumber3 = StringUtils.substring (cardNumber, 8, 12);
                            String cardNumber4 = StringUtils.substring (cardNumber, 12, 16);
                            
                            buf.delete (0, buf.length ()).append (cardNumber1).append ("-").append (cardNumber2).append ("-").append (cardNumber3).append ("-").append (cardNumber4);
                            cardNumber = buf.toString ();
                        }
                        
                        content = content.replaceAll ("\\{0\\}", cardNumber);
                        content = content.replaceAll ("\\{1\\}", pinNumber);
                        
                        // SMS 발송 요청 
                        SmtTranDto smtTranDto = new SmtTranDto ();
                        smtTranDto.setPriority ("S"); //전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
                        smtTranDto.setSubject (subject);
                        smtTranDto.setContent (content);
                        smtTranDto.setCallback (callback);
                        smtTranDto.setRecipient_num (orderDto.getPhone ());
                        smtTranDto.setReservation_time (reservationTime);
                        
                        Long mtPr = this.paymentCancelXoMgr.insertSmtTran (xoSqlMap, smtTranDto);
                        
                        // SMS 이력 등록 
                        SmsHistoryDto smsHistoryDto = new SmsHistoryDto ();
                        smsHistoryDto.setMt_pr (mtPr);
                        smsHistoryDto.setPhone (orderDto.getPhone ());
                        smsHistoryDto.setUser_id (orderDto.getUser_id ());
                        smsHistoryDto.setContent (content);
                        smsHistoryDto.setOrder_no (orderNo);
                        
                        isOk = this.paymentCancelXoMgr.insertSmsHistory (xoSqlMap, smsHistoryDto);
                        
                    } catch ( Exception e ) {
                        buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                        this.pcLogger.error (buf.toString (), e);
                        this.eLogger.error (buf.toString (), e);
                    } finally {
                        if ( isOk ) {
                            if ( this.pcLogger.isDebugEnabled () ) {
                                buf.delete (0, buf.length ()).append (logTitle).append ("insertSmsHistory OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                                this.pcLogger.debug (buf.toString ());
                            }
                        } else {
                            buf.delete (0, buf.length ()).append (logTitle).append ("insertSmsHistory FAIL (").append (refundSbcDto.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                            this.pcLogger.info (buf.toString ());
                        }
                    }
                }
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : 주문정보 결제 취소 상태로 변경 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 12. XO : 주문정보 결제 취소 상태로 변경 END   ==============================================
            
            xoSqlMap.commitTransaction (); // commit
        } catch ( Exception e ) {
            this.failReason = e.getMessage ();
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            
            if (empSuccess) {
                try {
                    // 임직원 할인 취소 요청이 성공했고 롤백을 해야할 경우
                    // 임직원 승인 번호를 재요청한 승인번호로 업데이트 해야 하기 때문에
                    // 새로운 트렌젝션을 시작함
                    xoSqlMap.endTransaction();
                    xoSqlMap.startTransaction();
                    rollbackEmp(xoSqlMap, orderNo, empNo, logTitle);
                    xoSqlMap.commitTransaction();
                }
                catch (Exception e1) {
                    buf.delete(0, buf.length()).append(logTitle)
                            .append("임직원 할인 취소 롤백 실패.")
                            .append("orderNo=").append(orderNo)
                            .append(", empNo=").append(empNo)
                            .append(", message=").append(e1.getMessage());
                    this.pcLogger.error(buf.toString(), e1);
                }
            }
            if(mGiftCancelSuccess) {
            	try {
            		xoSqlMap.endTransaction();
            		xoSqlMap.startTransaction();
            		
            		int mGiftRollbackErr  = mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                	if(mGiftRollbackErr != 0) {
                		throw new Exception("Mobile Gift Rollback Error!");
                	}
                	xoSqlMap.commitTransaction();
            	}catch(Exception e1) {
            		buf.delete(0, buf.length()).append(logTitle)
                    .append("모바일 상품권 취소 롤백 실패.")
                    .append("orderNo=").append(orderNo)
                    .append(", message=").append(e1.getMessage());
            		mLogger.error(buf.toString(), e1);
            	}
            }

        } finally {
            try {
                xoSqlMap.endTransaction ();
            } catch ( Exception ee ) {
                pcLogger.error(ee.getMessage(), ee);
            }
        }
        
        return isSCKCardCancel;
    }

    private Map<String, String> cancelEmpOrder(String logTitle, OrderDto orderDto) {
        try {
            return this.empMgr.cancel(orderDto.getOrder_no(), orderDto.getEmp_no(), logTitle);
        }
        catch (Exception e) {
            pcLogger.error(e.getMessage(), e);
            return new HashMap<String, String>();
        }
    }

    /**
     * 임직원 할인 취소요청 롤백처리.
     * @param xoSqlMap SqlMapClient
     * @param orderNo 주문번호
     * @param empNo 임직원 번호
     * @param logTitle logTitle
     * @throws Exception
     */
    private void rollbackEmp(SqlMapClient xoSqlMap, String orderNo, String empNo, String logTitle) throws Exception {
        Map<String, String> rollback = this.empMgr.rollback(orderNo, empNo, logTitle);
        rollback.put("orderNo", orderNo);
        rollback.put("empNo", empNo);
        this.paymentCancelXoMgr.updateEmpOrder(xoSqlMap, rollback);
    }

    /**
     * e-Gift Card 발행
     * @param refundDto
     * @param logTitle
     * @return
     * @throws Exception
     */
    private RefundSbcDto egiftCardPublish ( RefundSbcDto refundDto, String logTitle ) throws Exception {
        long startTime;
        int step = 1;
        StringBuffer logBuf = new StringBuffer ();
        
        List<EGiftCardInfoDto> eGiftCardInfoList = new ArrayList<EGiftCardInfoDto> ();
        SqlMapClient msrSqlMap1 = IBatisSqlConfig.getMsrSqlMapInstance ();
        try {
            msrSqlMap1.startTransaction ();
            
            EGiftCardInfoDto egiftCardInfoDto = new EGiftCardInfoDto ();
            
            // step 1. MSR : 수신자에게 발송 가능한 카드 조회 START ===========================================
            startTime = System.currentTimeMillis ();
            
            // 사용가능한 카드 채번 (procedure - UPDATE:MSR_EGIFT_CARD_INFO.USE_YN='N') - 채번한 카드는 사용처리 USE_YN (Y:사용가능한 카드, N:사용불가능한 카드)
            int publicationCnt = 1;
            String cardNumber = this.paymentCancelMsrMgr.getPaymentCardListProc (msrSqlMap1, publicationCnt);
            
            // 카드 채번에 오류가 있는 경우 '-1'을 리턴
            if ( StringUtils.isEmpty (cardNumber) || StringUtils.equals (cardNumber, "-1") ) {
                throw new XOException("e-Gift 발급 가능한 카드가 부족");
            }


            
            String[] cardNumberArr = cardNumber.split ("\\,");
            if ( cardNumberArr.length != publicationCnt ) { throw new XOException ("e-Gift 발급 가능한 가능한 카드가 부족"); }
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 수신자에게 발송가능한 카드 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 1. MSR : 수신자에게 발송 가능한 카드 조회 END =============================================
            
            // step 2. MSR : eGiftCard 정보 조회 START ========================================================
            // eGiftCard 정보 조회
            List<String> eGiftCardList = Arrays.asList (cardNumberArr);
            eGiftCardInfoList = this.paymentCancelMsrMgr.getEGiftCardInfoFront (msrSqlMap1, eGiftCardList);
            
            if ( eGiftCardInfoList == null || eGiftCardInfoList.size () == 0 ) { throw new XOException ("주문 가능한 카드가 부족"); }
            
            egiftCardInfoDto = eGiftCardInfoList.get (0);
            refundDto.setTarget_card_number (egiftCardInfoDto.getCard_number ());
            refundDto.setPin_number (egiftCardInfoDto.getPin_number ());
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : eGiftCard 정보 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. MSR : eGiftCard 정보 조회 END ==========================================================
            
            msrSqlMap1.commitTransaction (); // commit
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append (" msrSqlMap1 commit!!");
                this.pcLogger.debug (buf.toString ());
            }
        } catch ( Exception e ) {
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
            try {
                msrSqlMap1.endTransaction ();
            } catch ( Exception ee ) {
                pcLogger.error(ee.getMessage(), ee);
            }
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' 업데이트
            if ( eGiftCardInfoList != null && eGiftCardInfoList.size() > 0) {
                boolean result = this.setRequestEGiftCardInfoAbort (eGiftCardInfoList);
                if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("setRequestEGiftCardInfoAbort Result => ").append (result);
                    this.pcLogger.debug (buf.toString ());
                }
            }
            throw e;
        }finally {
            try {
                msrSqlMap1.endTransaction ();
            } catch ( Exception ee ) {
                pcLogger.error(ee.getMessage(), ee);
            }
        }
        
        // step 3. FD Activation START ====================================================================
        startTime = System.currentTimeMillis ();
        try {
            // FD Activation 호출
            boolean isOk = this.setFdBalance (refundDto.getTarget_card_number (), String.valueOf (refundDto.getAmount ()), "A", FDCode.ORDER_CODE.REFUND, refundDto.getOrder_no(), refundDto.getUser_id());
            
            // FD 연동 - 결제 취소 원복 결과 로그 ===================
            logBuf.append (logTitle).append ("FD Activation")
                .append ("|").append ("orderNo=").append (refundDto.getOrder_no ())
                .append ("|").append ("cardNumber=").append (refundDto.getTarget_card_number ())
                .append ("|").append (this.resultCode)
                .append ("|").append (this.resultMsg);
            this.fLogger.info (logBuf); // FD 로그
            this.pcLogger.info (logBuf); // 기본 로그
            logBuf.delete (0, logBuf.length ());
            // FD 연동 - 결제 취소 원복 결과 로그 ===================
            
            if ( !isOk ) {
                buf.delete (0, buf.length ()).append ("FD Activation Fail. ").append (this.resultMsg);
                throw new XOException (buf.toString ());
            }
            
            refundDto.setAmount ((long) this.remainAmount);
        } catch ( Exception e ) {
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' 업데이트
            if ( eGiftCardInfoList != null && eGiftCardInfoList.size() > 0) {
                boolean result = this.setRequestEGiftCardInfoAbort (eGiftCardInfoList);
                if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("setRequestEGiftCardInfoAbort Result => ").append (result);
                    this.pcLogger.debug (buf.toString ());
                }
            }
            throw e;
        }
        if ( this.pcLogger.isDebugEnabled () ) {
            buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". FD : Activation END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            this.pcLogger.debug (buf.toString ());
        }
        // step 3. FD Activation END   ====================================================================
        
        SqlMapClient msrSqlMap2 = IBatisSqlConfig.getMsrSqlMapInstance ();
        try {
            msrSqlMap2.startTransaction ();
            
            int currBalance = Integer.parseInt (String.valueOf (refundDto.getAmount ()));
            
            // step 4. MSR : MSR 회원 여부 확인 START =========================================================
            startTime = System.currentTimeMillis ();
            CardRegMemberDto cardRegMemberDto = this.paymentCancelMsrMgr.getRegMember (msrSqlMap2, refundDto.getUser_id ());
            
            if ( cardRegMemberDto == null ) {
                buf.delete (0, buf.length ()).append ("cardRegMemberDto is null(userId : ").append (refundDto.getUser_id ()).append (")");
                throw new XOException (buf.toString ());
            }
            
            String userNumber = cardRegMemberDto.getUser_number ();
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : MSR 회원인지 확인 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 4. MSR : MSR 회원 여부 확인 END   =========================================================
            
            // step 5. MSR : 카드 등록 START ==================================================================
            startTime = System.currentTimeMillis ();
            
            // 카드 닉네임
            String nickname = this.conf.getString ("refund.card.nickname");
            buf.delete (0, buf.length ()).append (nickname).append ("_").append (DateTime.getShortDateString ());
            nickname = buf.toString ();
            refundDto.setSbc_nickname (nickname);
            
            UserRegCardDto cardDto = new UserRegCardDto ();
            cardDto.setCard_number (refundDto.getTarget_card_number ());
            cardDto.setUser_number (cardRegMemberDto.getUser_number ());
            cardDto.setCard_nickname (refundDto.getSbc_nickname ());
            cardDto.setBalance (currBalance);
            cardDto.setCard_reg_number (0);
            
            int cardRegNumber = this.paymentCancelMsrMgr.insertUserRegCard (msrSqlMap2, cardDto);
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 카드 등록 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 5. MSR : 카드 등록 END ==================================================================
            
            // step 6. MSR : 카드 상태 업데이트 START =========================================================
            startTime = System.currentTimeMillis ();
            boolean cardInsertFlag = this.paymentCancelMsrMgr.updateCardInfoForRegister (msrSqlMap2, refundDto.getTarget_card_number (), "R", String.valueOf (currBalance));
            
            if ( !cardInsertFlag ) { // 업데이트 실패 시 롤백
                buf.delete (0, buf.length ()).append ("Update Card Status Failed(cardNumber : ").append (refundDto.getTarget_card_number ()).append (")");
                throw new XOException ();
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 카드 상태 업데이트 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 6. MSR : 카드 상태 업데이트 END =========================================================
            
            // step 7. MSR : 카드 등록 이력 저장 START ========================================================
            startTime = System.currentTimeMillis ();
            
            // 미등록 카드 사용 이력에 충전 이력 등록.
            Map<String, Object> etcMap = new HashMap<String, Object> ();
            etcMap.put ("cardNumber", refundDto.getTarget_card_number ()); // 카드번호
            etcMap.put ("historyCode", "T"); // 이력구분(R-충전, U-사용, C-충전취소, X-사용취소, M-잔액이전, A-보정, T-최초충전, Z-최초충전취소, V-반품충전, W-반품충전취소, I-등록, D-삭제)
            etcMap.put ("amount", currBalance); // 처리금액
            etcMap.put ("balance", currBalance); // 잔액
            etcMap.put ("formulaCode", "O"); // 사용/충전 방식 (O:사이렌오더)
            etcMap.put ("paymentType", "사이렌오더환불"); // 
            etcMap.put ("orderNo", refundDto.getOrder_no ()); 
            
            this.paymentCancelMsrMgr.insertCardUseHistoryEtc (msrSqlMap2, etcMap);
            
            // 카드 상태 변경 이력 등록
            CardStatusHistDto histDto = new CardStatusHistDto ();
            histDto.setCard_number (refundDto.getTarget_card_number ());
            histDto.setChange_actor_group_code ("B");
            histDto.setChange_actor_id (refundDto.getUser_id ());
            histDto.setUser_number (userNumber);
            histDto.setChange_status ("R");
            histDto.setCard_reg_number (cardRegNumber);
            histDto.setNo_use_desc ("SmartOrder e-Gift");
            
            this.paymentCancelMsrMgr.insertCardStatusHist (msrSqlMap2, histDto);
            
            // 카드 사용 이력 등록
            CardUseHistoryDto useHistoryDto = new CardUseHistoryDto ();
            useHistoryDto.setCard_number (refundDto.getTarget_card_number ());
            useHistoryDto.setCard_reg_number (cardRegNumber);
            useHistoryDto.setUser_number (userNumber);
            useHistoryDto.setFormula_code ("A");
            useHistoryDto.setBalance_confirm_flag ("Y");
            useHistoryDto.setBalance (currBalance);
            useHistoryDto.setHistory_code ("I");
            useHistoryDto.setOrder_no (refundDto.getOrder_no ());
            
            this.paymentCancelMsrMgr.insertCardUseHistory (msrSqlMap2, useHistoryDto);
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 카드 상태 변경 이력 저장 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 7. MSR : 카드 등록 이력 저장 END ========================================================
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append (" statusMsr2 commit!!");
                this.pcLogger.debug (buf.toString ());
            }// 트랜젝션 커밋
            msrSqlMap2.commitTransaction ();
            refundDto.setResult ("00");
        } catch ( Exception e ) {
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' 업데이트
            if ( eGiftCardInfoList != null && eGiftCardInfoList.size() > 0 ) {
                boolean result = this.setRequestEGiftCardInfoAbort (eGiftCardInfoList);
                if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("setRequestEGiftCardInfoAbort Result => ").append (result);
                    this.pcLogger.debug (buf.toString ());
                }
            }
            
            throw e;
        } finally {
            try {
                msrSqlMap2.endTransaction ();
            } catch ( Exception ee ) {
                pcLogger.error(ee.getMessage(), ee);
            }
        }
        return refundDto;
    }
    
    /**
     * eGift 카드 USE_YN 상태값 변경
     * @param eGiftCardInfoList
     * @return
     */
    public boolean setRequestEGiftCardInfoAbort ( List<EGiftCardInfoDto> eGiftCardInfoList ) {
        if ( this.pcLogger.isDebugEnabled () ) {
            buf.delete (0, buf.length ()).append (logTitle).append ("START ");
            this.pcLogger.debug (buf.toString ());
        }
        
        List<String> eGiftCardList = new ArrayList<String> ();
        Long startTime = System.currentTimeMillis ();
        int updateCnt = 0;
        
        SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance ();
        try {
            msrSqlMap.startTransaction ();
            
            if ( eGiftCardInfoList == null ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("eGiftCardInfoList is Null");
                this.pcLogger.error (buf.toString ());
                return false;
            }
            
            // 카드 번호 추출
            for ( EGiftCardInfoDto dto : eGiftCardInfoList ) {
                if ( dto.getCard_number ().length () != 16 ) { return false; }
                eGiftCardList.add (dto.getCard_number ());
            }
            
            // eGift 카드 사용가능 여부를 X(미사용)으로 수정
            updateCnt = this.paymentCancelMsrMgr.updateEGiftCardInfoUseYnX (msrSqlMap, eGiftCardList);
            
            // 트랜젝션 커밋
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append (" msrSqlMap commit!!");
                this.pcLogger.debug (buf.toString ());
            }
            msrSqlMap.commitTransaction ();
        } catch ( Exception e ) {
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
            return false;
        } finally {
            try {
                msrSqlMap.endTransaction ();
            } catch ( Exception ee ) {
                this.pcLogger.error(ee.getMessage(), ee);
                this.eLogger.error(ee.getMessage(), ee);
            }
            buf.delete (0, buf.length ()).append (logTitle).append ("step 1. MSR : eGift 카드 사용가능 여부를 X(미사용)으로 수정 (update Count : ").append (updateCnt).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            this.pcLogger.info (buf.toString ());
        }
        return true;
    }
    
    /**
     * FD에 카드 사용/취소 요청하고, 잔액 확인
     * @return
     */
    protected boolean setFdBalance ( String cardNumber, String amount, String fdType, String orderCode, String orderNo, String userId ) {
        try {
            
            FDClient fdc = new FDClient (FORMULA_CODE.XO_BATCH, orderCode);
            fdc.setLogInsertUser(userId);
            
            Map<String, String> fdProcessResult = new HashMap<String, String> ();
            String functionName = "";
            if ( StringUtils.equals (fdType, "U") ) {
                fdProcessResult = fdc.redemption (cardNumber, amount, orderNo); // FD에 카드 금액 사용 - SV.0200 (카드번호, 사용금액)
                functionName = "redemption";
            } else if ( StringUtils.equals (fdType, "X") ) {
                fdProcessResult = fdc.voidOfCashOut (cardNumber, amount, orderNo); // FD에 카드 금액 사용 취소 - SV.0800 (카드번호, 사용금액)
                functionName = "voidOfCashOut";
            } else if ( StringUtils.equals (fdType, "R") ) {
                fdProcessResult = fdc.reload (cardNumber, amount, orderNo); // FD에 카드 금액 충전 - SV.0300 (카드번호, 사용금액)
                functionName = "reload";
            } else if ( StringUtils.equals (fdType, "C") ) {
                fdProcessResult = fdc.voidOfReload (cardNumber, amount, orderNo); // FD에 카드 금액 충전 취소 - SV.0801(카드번호, 사용금액)
                functionName = "voidOfReload";
            } else if ( StringUtils.equals (fdType, "A") ) {
                fdProcessResult = fdc.activation (cardNumber, amount, orderNo); // FD에 카드 금액 activation - SV.0100 (카드번호, 사용금액)
                functionName = "reload";
            }
            
            if ( fdProcessResult == null || fdProcessResult.size () <= 0 ) {
                this.resultCode = "FF";
                this.resultMsg = buf.delete (0, buf.length ()).append ("FD ").append (functionName).append (" - fdProcessResult is null").toString ();
                return false;
            }
            
            String trCode = fdProcessResult.get ("TRCODE");
            String codeF6 = fdProcessResult.get (FILED.ORIGINAL_TRANSACTION_REQUEST);
            String code39 = fdProcessResult.get (FILED.RESPONSE_CODE);
            String code76 = fdProcessResult.get (FILED.NEW_BALANCE);
            this.resultCode = code39;
            this.resultMsg = buf.delete (0, buf.length ()).append ("Fd Balance=").append (code76).toString ();
            
            if ( !StringUtils.equals (code39, RESPONSE_CODE.COMPLETED_OK)
                 || !StringUtils.equals (trCode, codeF6) // 두 값이 같지 않으면 실패
                 || StringUtils.equals (trCode, TRCODE.TIMEOUT_REVERSAL) // TRCODE가 0704이면 실패
            )
            {
                String fdFailMsg = "";
                if ( StringUtils.equals (code39, RESPONSE_CODE.ACCOUNT_CLOSED) || StringUtils.equals (code39, RESPONSE_CODE.UNKNOWN_ACCOUNT) ) {
                    fdFailMsg = "유효한 카드가 아닙니다.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INACTIVE_ACCOUNT) ) {
                    fdFailMsg = "최초 충전이 되지 않은 카드입니다. 매장에서 최초 충전 후 등록을 바랍니다.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.MAX_BALANCE_EXCEEDED) ) {
                    fdFailMsg = "최대 충전 금액을 초과하였습니다.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INVALID_AMOUNT) ) {
                    fdFailMsg = "사용 금액이 올바르지 않습니다.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INSUFFICIENT_FUNDS) ) {
                    fdFailMsg = "잔액이 부족합니다.";
                } else {
                    fdFailMsg = "카드 사용에 실패하였습니다.";
                }
                this.resultMsg = buf.delete (0, buf.length ()).append (fdFailMsg).append ("(").append (code39).append (":").append (code76).append (")").toString ();
                return false;
            }
            
            this.remainAmount = Integer.parseInt (code76); // 잔액
            this.resultMsg = buf.delete (0, buf.length ()).append ("batch : FD SUCCESS").toString ();
            return true;
        } catch ( Exception e ) {
            this.resultMsg = e.getMessage ();
            
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            return false;
        }
    }
  
    
    /**
     * 통신사제휴 취소 처리
     */
    public boolean setTelecomOrderCancel(List<PaymentDto> paymentList, String logTitle, StringBuffer logBuf){
    	int telCount = 0;
    	
		//생년월일 인증 여부
    	String telecommType = telecomConf.getString("telecomm.birthYmd.check");
		TelecommUtil telecommUtil = new TelecommUtil(telecommType);
    	
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			//통신사제휴일 경우만 처리
    			if(StringUtils.equals(payDto.getPay_method(), "K") || StringUtils.equals(payDto.getPay_method(), "U")){
    				telCount++;
    				
    				try{
    					//승인날짜를 통신사 취소용 승인날짜로 변환
    					String orgSaleDate = DateTime.getDbApplDateToTelApplDate(payDto.getApp_date_str());
    					
    					Map<String, String> rtnMap = null;
    					if( ("K".equals(payDto.getPay_method()) && telecommUtil.isKt_birth() && StringUtils.isNotBlank(payDto.getAuth_num()) ) || ("U".equals(payDto.getPay_method()) && telecommUtil.isLg_birth() && StringUtils.isNotBlank(payDto.getAuth_num()) ) ) {
    						rtnMap = TelecommService.requestMembershipSaleCancel(payDto.getPay_method(), payDto.getSbc_card_no(), payDto.getAmount().intValue(), payDto.getTid(), orgSaleDate, payDto.getAuth_num());
    					} else {
    						rtnMap = TelecommService.requestMembershipSaleCancel(payDto.getPay_method(), payDto.getSbc_card_no(), payDto.getAmount().intValue(), payDto.getTid(), orgSaleDate);
    					}
    					
    					String RespCd 	  = rtnMap.get("RespCd");
    					String ScreenExpr = rtnMap.get("ScreenExpr");
    					
    					logBuf.delete(0, buf.length()).append (logTitle).append ("TELECOM cancel")
	    					.append("|").append("METHOD=").append(payDto.getPay_method())
	    					.append("|").append("CARD_NUMBER=").append(payDto.getSbc_card_no())
	    					.append("|").append("APP_DATE=").append(orgSaleDate)
	    					.append("|").append("RESULT=").append(RespCd)
	    					.append("|").append("RESULT_MSG=").append(ScreenExpr);
    					tLogger.info(logBuf.toString());		//통신사 로그
    					pcLogger.info(logBuf.toString());		//INFO 로그
    					logBuf.delete(0, logBuf.length ());
    					
    					//통신사 취소 성공
    					if(StringUtils.equals(RespCd, "0000")){
    						payDto.setCancel_result_code("00");
    						payDto.setResult_msg(ScreenExpr);
    						payDto.setStatus("C");
    						
    					//통신사 취소 실패
    					}else{
    						this.failReason = RespCd+"|"+ScreenExpr;	// 실패 사유
    						return false;
    					}
    				}catch(Exception e){
    					this.resultCode = "07";
    					this.resultMsg = e.getMessage ();
    					logBuf.delete (0, logBuf.length ()).append (logTitle).append (e.getMessage ());
    					this.pcLogger.error (logBuf.toString (), e);
    					this.eLogger.error (logBuf.toString (), e);
    					
    					return false;
    				}
    			}
    		}
    	}
    	
    	//통신사제휴가 없는 경우 무조건 true 리턴
    	if(telCount == 0){
    		return true;
    	}
    	
    	return true;
    }
    
    
    /**
     * 통신사제휴 결제 처리
     */
    public int setTelecomOrderPayment(List<PaymentDto> paymentList, List<PaymentDto> newPaymentList, String logTitle, StringBuffer logBuf){
    	int inCriticalCnt = 0;
    	
		String telecommType = this.telecomConf.getString("telecomm.birthYmd.check");
		TelecommUtil telecommUtil = new TelecommUtil(telecommType);
    	
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			//통신사 이면서 취소가 성공한 건에 대해서는 다시 결제처리
    			if( (StringUtils.equals(payDto.getPay_method(), "K") || StringUtils.equals(payDto.getPay_method(), "U")) && StringUtils.equals(payDto.getCancel_result_code(), "00") ){
    				try{
    					Map<String, String> rtnMap = null;
    					if( ("K".equals(payDto.getPay_method()) && telecommUtil.isKt_birth()) || ("U".equals(payDto.getPay_method()) && telecommUtil.isLg_birth()) ) {
    						if(StringUtils.isNotBlank(payDto.getAuth_num())) {
    							rtnMap = TelecommService.requestMembershipSale(payDto.getPay_method(), payDto.getSbc_card_no(), payDto.getAmount(), payDto.getAuth_num());
    						} else {
    							rtnMap = TelecommService.requestMembershipSale(payDto.getPay_method(), payDto.getSbc_card_no(), payDto.getAmount() );
    						}
    					} else {
    						rtnMap = TelecommService.requestMembershipSale(payDto.getPay_method(), payDto.getSbc_card_no(), payDto.getAmount() );
    					}
    					
    					String RespCd 	  = rtnMap.get("RespCd");
    					String ScreenExpr = rtnMap.get("ScreenExpr");
    					String ApprovDT   = rtnMap.get("ApprovDT");
    					String ApprovNo   = rtnMap.get("ApprovNo");
    					
    					logBuf.delete(0, buf.length()).append (logTitle).append ("TELECOM cancel->Rollback(repay)")
	    					.append("|").append("METHOD=").append(payDto.getPay_method())
	    					.append("|").append("CARD_NUMBER=").append(payDto.getSbc_card_no())
	    					.append("|").append("APPROV_NO=").append(ApprovNo)
	    					.append("|").append("RESULT=").append(RespCd)
	    					.append("|").append("RESULT_MSG=").append(ScreenExpr)
	    					.append("|").append("APP_DATE=").append(ApprovDT);
    					tLogger.info(logBuf.toString());		//통신사 로그
    					pcLogger.info(logBuf.toString());		//INFO 로그
    					
    					//통신사 취소 롤백(재결제) 성공
    					if(StringUtils.equals(RespCd, "0000")){
    						PaymentDto rePayDto = new PaymentDto();
    						rePayDto.setOrder_no(payDto.getOrder_no());
    						rePayDto.setPay_method(payDto.getPay_method());
    						rePayDto.setAmount(payDto.getAmount());
    						rePayDto.setSbc_card_no(payDto.getSbc_card_no());
    						rePayDto.setTid(ApprovNo);
    						rePayDto.setResult_code("00");
    						rePayDto.setResult_msg(ScreenExpr);
    						rePayDto.setTel_app_date(DateTime.getTelApplDateToDbApplDate(ApprovDT));	//통신사 승인일시(DB 저장용)
    						rePayDto.setCancel_result_code("");
    						rePayDto.setStatus("P");
    						newPaymentList.add(rePayDto);	//정상적으로 취소된건 처리
    						
    					//통신사 취소 실패
    					}else{
    						inCriticalCnt++;
    						this.cLogger.info(logBuf.toString());		//크리티컬 로그
    						this.failReason = RespCd+"|"+ScreenExpr;	// 실패 사유
    					}
    				}catch(Exception e){
    					inCriticalCnt++;
    					this.resultCode = "07";
    					this.resultMsg = e.getMessage ();
    					logBuf.delete (0, logBuf.length ()).append (logTitle).append (e.getMessage ());
    					this.pcLogger.error (logBuf.toString (), e);
    					this.eLogger.error (logBuf.toString (), e);
    					this.cLogger.info (logBuf.toString (), e);	//크리티컬 로그
    				}
    			}
    		}
    	}
    	
    	return inCriticalCnt;
    }
    
    /*
     * 물품형 GIFT 취소
     */
    public boolean eGiftItemCencelProcess(SqlMapClient xoGiftSqlMap, List<PaymentDto> paymentList){
    	boolean returnBoolean = true;
    	
    	try
    	{
    		for(PaymentDto forDto :paymentList)
    		{
    			if("T".equals(forDto.getPay_method()))
    			{
    				String giftNo = forDto.getGift_no();
    				String giftCancelYn = this.paymentCancelXoMgr.getGiftUseCancel(xoGiftSqlMap, giftNo);
    				
    				if("Y".equals(giftCancelYn))
    				{
    					Map<String, Object> dbMap = new HashMap<String, Object>();
    					
    					dbMap.put("giftNo", giftNo);
						dbMap.put("historyChannel", "4");
						dbMap.put("orderNo", forDto.getOrder_no());
						dbMap.put("regId", "BATCH");
						dbMap.put("regName", "BATCH");
	
						// 사용 취소 (H11)
						dbMap.put("status", "H11");
						
						// 선물 사용 후 사용 이력 등록
						this.paymentCancelXoMgr.insertGiftUseHistory(xoGiftSqlMap, dbMap);
						
						// 발행 (G00)
						dbMap.put("status", "G00");
						
						// 물품형 GIFT 상태 수정
						this.paymentCancelXoMgr.updateGiftStatus(xoGiftSqlMap, dbMap);
						// 물품형 GIFT 취소 성공 시
						
    					forDto.setCancel_result_code("00");
    					forDto.setResult_msg("e-Gift Item Batch Cancel Success;");
    					forDto.setStatus("C");
    				}
    				else
    				{
    					this.pcLogger.info("Gift Cancel YN : "+giftCancelYn);
    					returnBoolean = false;
    				}
    			}
    		}
    	}catch (Exception e) {
    		this.pcLogger.info(e.getMessage(), e);
    		this.pcLogger.error(e.getMessage(), e);
    		returnBoolean = false;
		}
    	
    	return returnBoolean;
    }

    public static void main ( String[] args ) {
        PaymentCancel paymentCancel = new PaymentCancel ();
        if ( args.length > 2 ) {
            paymentCancel.start (args[1], args[2]);
        } else if ( args.length > 1 ) {
            paymentCancel.start (args[1], "");
        } else {
            paymentCancel.start ("", "");
        }
        
    }
    
}
