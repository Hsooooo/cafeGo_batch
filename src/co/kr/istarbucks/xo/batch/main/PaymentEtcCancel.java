/*
 * @(#) $Id: PaymentEtcCancel.java,v 1.12 2018/10/29 05:01:21 iamjihun Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.mgr.EmpMgr;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderCouponDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PaymentDto;
import co.kr.istarbucks.xo.batch.common.mgift.MGiftHttpService;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommService;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommUtil;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DateTime;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelMsrMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 자동 결제 취소 PaymentPgCancel.
 * @author eZEN ksy
 * @since 2014. 1. 15.
 * @version $Revision: 1.12 $
 */
public class PaymentEtcCancel {
    
    private final Logger pcLogger = Logger.getLogger ("PPC_INFO");
    private final Logger eLogger = Logger.getLogger ("PPC_ERROR");
    private final Logger tLogger = Logger.getLogger ("PPC_TEL");
    private final Logger cLogger = Logger.getLogger ("PPC_CRITICAL");
    private final Logger mLogger = Logger.getLogger("PC_MGIFT");
    
    public String resultCode = "";
    public String resultMsg = "";
    public String logTitle = "";
    public int remainAmount = 0;
    
    public int procCnt = 0;	// 취소 대상 건수
    public int compCnt = 0;	// 결제완료 취소 건수
    public int waitCnt = 0;	// 결제대기 취소 건수
    public String failReason = "";	// 실패 사유
    StringBuffer buf = new StringBuffer ();
    

    private final PaymentCancelXoMgr paymentCancelXoMgr;
    private final PaymentCancelMsrMgr paymentCancelMsrMgr;
    private final EmpMgr empMgr;
    private final String loggerTitle;
    
    private List<Map<String, String>> paymentCancelList;
    
    private final Configuration telecomConf;
    
    public PaymentEtcCancel () {
        this.paymentCancelXoMgr = new PaymentCancelXoMgr ();
        this.paymentCancelMsrMgr = new PaymentCancelMsrMgr ();
        this.loggerTitle = "[PaymentEtcCancel] ";
        this.telecomConf = CommPropertiesConfiguration.getConfiguration ("telecom.properties");
//        this.empMgr = new EmpMgr(pcLogger, eLogger);
        this.empMgr = new EmpMgr(pcLogger);
    }
    
    public void start ( String cancelStartDate, String cancelEndDate ) {
    	//5초 지연
    	try{
    		Thread.sleep(5000);
    	}catch(Exception e){
    	    pcLogger.error(e.getMessage(), e);
        }
    	pcLogger.info("\n\n\n============================================================ ETC_CANCEL ============================================================");
    	
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
            for ( Map<String, String> map : paymentCancelList ) {
                buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (this.procCnt).append (" : ").append (map.get ("user_id")).append (" : ").append (map.get ("order_no")).append (") ");
                String logTitle = buf.toString ();
                
                buf.delete (0, buf.length ()).append (logTitle).append ("START");
                this.pcLogger.info (buf.toString ());
                
                startTime = System.currentTimeMillis ();
                boolean cancelSuccess = false;
                this.failReason = "";
                try {
                	cancelSuccess = this.processPaymentCancel (map, logTitle);
                    
                    buf.delete (0, buf.length ()).append (logTitle).append ("RESULT : ").append (cancelSuccess);
                    this.pcLogger.info (buf.toString ());
                } catch ( Exception e ) {
                    buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                    this.pcLogger.error (buf.toString (), e);
                    this.eLogger.error (buf.toString (), e);
                }
                
                buf.delete (0, buf.length ()).append (logTitle).append ("END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.info (buf.toString ());
                if ( cancelSuccess ) {
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
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("결제 취소 대상 \t     : ").append (this.procCnt).append ("건")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("결제 취소 성공 \t     : ").append (successCnt).append ("건 (결제완료 : ").append(this.compCnt).append("건, 결제대기 : ").append(this.waitCnt).append("건)")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("결제 취소 실패 \t     : ").append (failCnt).append ("건")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("취소 실패 주문 정보 : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t    =================================================");
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
            } 
            
            // 결제 취소 대상 조회
            this.paymentCancelList = this.paymentCancelXoMgr.getPaymentEtcCancel (dbMap);
            
            buf.delete (0, buf.length ()).append (this.loggerTitle).append ("Get PaymentEtcCancel Info : ").append (this.paymentCancelList.size ()).append ("건");
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
        
        StringBuffer logBuf = new StringBuffer ();
        
        boolean cancelSuccess = false;	// 취소 성공 여부
        boolean empSuccess = false;     // 임직원 할인 취쇼 요청 성공 여부
        boolean mGiftCancelSuccess = false; //뫄일 상품권 결제 취소 성공여부
        int criticalCount	  = 0;		// 크리티컬 flag{C:크리티컬 에러 발생}
        
        List<PaymentDto> newPaymentList = new ArrayList<PaymentDto> (); 	// 통신사제휴 신규 결제 처리건
        List<PaymentDto> paymentList = new ArrayList<PaymentDto>();			// 결제 정보 조회
        
        String orderStatus = "";	// xo_order 테이블의 최종 status
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
            if( !StringUtils.equals(orderDto.getCheck_status_flag(), "Y") ) {
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
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : 결제 정보 조회 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. XO : 결제 정보 조회 END ================================================================

            
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
            // 실패 시 더 이상 진행 하지 않고 실패 카운트 만 증가함, telCancelSuccess, isSCKCardCancel, DB 원복, 실패 카운트 증가, 실패 이력 등록
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
        		return cancelSuccess;
        	}
            
            // step 4. 물품형 GIFT : 결제 취소 END =================================================================

            // step 5. 임직원 할인 취소 요청 START =================================================================
            if (StringUtils.isNotEmpty(orderDto.getEmp_no()) && StringUtils.isNotEmpty(orderDto.getEmp_auth_app_no())) {
                Map<String, String> empResult = cancelEmpOrder(logTitle, orderDto);
                if (empResult.isEmpty()) {
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
                    return cancelSuccess;
                }
                else {
                    empNo = orderDto.getEmp_no();
                    empSuccess = true;
                }
            }
            // step 5. 임직원 할인 취소 요청 END =================================================================
            buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". 모바일 상품권 취소 Start ")
            .append(System.currentTimeMillis() - startTime).append("ms");
            
         // 모바일 상품권 결제 취소 START ===========================================================
            
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
            	return cancelSuccess;
            }else {
            	if ( this.pcLogger.isDebugEnabled () ) {
                	buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". 모바일 상품권 취소 End :")
                    .append(System.currentTimeMillis() - startTime).append("ms");
                }
            }
            
            
            // 모바일 상품권 결제 취소 ============================================================
            
            // step 5. 통신사 : 결제 취소 START ===============================================================
            startTime = System.currentTimeMillis ();
            cancelSuccess = this.setTelecomOrderCancel(paymentList, logTitle, logBuf);
            
            if ( cancelSuccess ) {
				orderStatus = "12";		// 결제 취소
			}else {
				// 통신사 : 결제 취소 실패시 강제 성공 처리
				orderStatus = "12";		// 결제 취소
				cancelSuccess = true;
				
				if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". 통신사 : 결제취소 실패 (강제성공) ")
                            .append(System.currentTimeMillis() - startTime).append("ms");
					this.pcLogger.debug (buf.toString ());
				}
				
				criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
				if(mGiftCancelSuccess) {
                	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                }
				
				///////////////////////////////////////////////
				
//				orderStatus = "13";		// 결제 취소 실패
				//PG결제 취소 실패시 취소처리한 통신사제휴를 다시 결제처리함
//				criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
			}
            
            if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 통신사 : 결제 취소 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
            // step 5. 통신사 : 결제 취소 END ==================================================================
            
        	
        	// step 6. MSR : 쿠폰 사용 취소 START =============================================================
        	if(cancelSuccess){
        		startTime = System.currentTimeMillis ();
        		SqlMapClient msrSqlMap2 = IBatisSqlConfig.getMsrSqlMapInstance ();
        		try{
        			// MSR 트랜잭션 시작
                    msrSqlMap2.startTransaction ();
                    
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
        			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : 쿠폰 사용 취소 END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        			this.pcLogger.debug (buf.toString ());
        		}
        	}
        	// step 6. 쿠폰 사용 취소 END =====================================================================
        	
        	
            boolean dbSuccess = false;	// DB 처리 결과
            dbMap.put ("orderNo", orderNo);
            dbMap.put ("status",  orderStatus); // 상태
            
            // step 7. 주문 정보 업데이트 START   ==============================================================
            if(cancelSuccess) {	// 취소 성공인 데이터만 업데이트
            	startTime = System.currentTimeMillis ();
            	try {
            		dbSuccess = this.paymentCancelXoMgr.updateOrder (xoSqlMap, dbMap);
            	} catch ( Exception e ) {
            		buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            		this.pcLogger.error (buf.toString (), e);
            		this.eLogger.error (buf.toString (), e);
            	} finally {
            		if ( dbSuccess ) {
            			if ( this.pcLogger.isDebugEnabled () ) {
            				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updateOrder OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            				this.pcLogger.debug (buf.toString ());
            			}
            		} else {
            			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updateOrder FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            			this.pcLogger.info (buf.toString ());
            		}
            	}
            // 결제 취소 실패인 경우 critical_log가 있는 경우 업데이트	
            }else{
            	startTime = System.currentTimeMillis ();
            	try {
            		if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//크리티컬 에러 여부
                    }
            		dbSuccess = this.paymentCancelXoMgr.updateOrder (xoSqlMap, dbMap);
            	} catch ( Exception e ) {
            		buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            		this.pcLogger.error (buf.toString (), e);
            		this.eLogger.error (buf.toString (), e);
            	} finally {
            		if ( dbSuccess ) {
            			if ( this.pcLogger.isDebugEnabled () ) {
            				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updateOrder OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            				this.pcLogger.debug (buf.toString ());
            			}
            		} else {
            			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updateOrder FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            			this.pcLogger.info (buf.toString ());
            		}
            	}
            }
            // step 7. 주문 정보 업데이트 END   ================================================================

            // step 8. 주문 정보 변경 이력 저장 START   =========================================================
            startTime = System.currentTimeMillis ();
            dbSuccess = false;
            try {
                dbSuccess = this.paymentCancelXoMgr.insertOrderHistory (xoSqlMap, dbMap);
            } catch ( Exception e ) {
                buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
                this.pcLogger.error (buf.toString (), e);
                this.eLogger.error (buf.toString (), e);
            } finally {
                if ( dbSuccess ) {
                    if ( this.pcLogger.isDebugEnabled () ) {
                        buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". insertOrderHistory OK (orderNo=").append (orderNo).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                        this.pcLogger.debug (buf.toString ());
                    }
                } else {
                    buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". insertOrderHistory FAIL (").append (dbMap.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                    this.pcLogger.info (buf.toString ());
                }
            }
            // step 8. 주문 정보 변경 이력 저장 END   ===========================================================
            
            
            // step 9. 결제 정보 업데이트 START   ==============================================================
        	startTime = System.currentTimeMillis ();
        	for(PaymentDto paymentDto : paymentList){
        		//통신사제휴 결과 업데이트
        		if(StringUtils.equals(paymentDto.getPay_method(), "K") || StringUtils.equals(paymentDto.getPay_method(), "U")
        				|| StringUtils.equals(paymentDto.getPay_method(), "T")|| StringUtils.isNotEmpty(paymentDto.getPrcm_frst_code())){
        			//취소 성공인 데이터만 업데이트
        			if(StringUtils.equals(paymentDto.getCancel_result_code(), "00")){
        				dbSuccess = false;
        				
        				try {
        					dbSuccess = this.paymentCancelXoMgr.updatePayment (xoSqlMap, paymentDto);
        				} catch ( Exception e ) {
        					buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
        					this.pcLogger.error (buf.toString (), e);
        					this.eLogger.error (buf.toString (), e);
        				} finally {
        					if ( dbSuccess ) {
        						if ( this.pcLogger.isDebugEnabled () ) {
        							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updatePayment OK (orderNo=").append (orderNo).append(", paymentOrder=").append(paymentDto.getPayment_order()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        							this.pcLogger.debug (buf.toString ());
        						}
        					} else {
        						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". updatePayment FAIL (").append (paymentDto.toString ()).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        						this.pcLogger.info (buf.toString ());
        					}
        				} //[try~catch]
        			}
        		}
        	} //[for]
        	
            //결제 취소시 에러 발생건으로 인해 롤백(통신사)된 데이터 등록
            if(newPaymentList != null && newPaymentList.size() > 0){
            	try{
	            	for(PaymentDto payDto : newPaymentList){
	            		this.paymentCancelXoMgr.insertPaymentOfPaymentHist(xoSqlMap, payDto);
	            	}
            	}catch(Exception ex){
            		buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
                    this.pcLogger.error (buf.toString (), ex);
                    this.eLogger.error (buf.toString (), ex);
                    this.cLogger.info (buf.toString (), ex); 	// 크리티컬 로그
            	}
            }
            // step 9. 결제 정보 업데이트 END   =================================================================
            
            xoSqlMap.commitTransaction (); // commit
        } catch ( Exception e ) {
            this.failReason = e.getMessage ();
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);

            if (empSuccess) {
                try {
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
                    pcLogger.error(buf.toString(), e1);
                }
            }
            
            if(mGiftCancelSuccess) {
            	try {
            		xoSqlMap.endTransaction();
            		xoSqlMap.startTransaction();
            		
            		int mGiftRollback  = mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                	if(mGiftRollback != 0) {
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
        
        return cancelSuccess;
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
    private void rollbackEmp(SqlMapClient xoSqlMap, String orderNo, String empNo, String logTitle) throws Exception{
        Map<String, String> rollback = this.empMgr.rollback(orderNo, empNo, logTitle);
        rollback.put("orderNo", orderNo);
        rollback.put("empNo", empNo);
        this.paymentCancelXoMgr.updateEmpOrder(xoSqlMap, rollback);
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
    					logBuf.delete (0, logBuf.length ());
    					
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
}
