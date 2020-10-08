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
 * �ڵ� ���� ��� PaymentPgCancel.
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
    
    public int procCnt = 0;	// ��� ��� �Ǽ�
    public int compCnt = 0;	// �����Ϸ� ��� �Ǽ�
    public int waitCnt = 0;	// ������� ��� �Ǽ�
    public String failReason = "";	// ���� ����
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
    	//5�� ����
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
        
        // ���� ��� ��� ��ȸ
        this.getPaymentCancelInfo (cancelStartDate, cancelEndDate);
        
        // ���� ��� ó��
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
            buf.append ("\n\t\t\t\t\t\t     ").append ("������ \t\t\t : ").append (cancelStartDate).append ("~").append (cancelEndDate);
        }
        buf.delete (0, buf.length ()).insert (0, "=================================================")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("���� ��� ��� \t     : ").append (this.procCnt).append ("��")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("���� ��� ���� \t     : ").append (successCnt).append ("�� (�����Ϸ� : ").append(this.compCnt).append("��, ������� : ").append(this.waitCnt).append("��)")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("���� ��� ���� \t     : ").append (failCnt).append ("��")
                                     .append ("\r\n\t\t\t\t\t\t\t\t    ").append ("��� ���� �ֹ� ���� : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t    =================================================");
        this.pcLogger.info (buf.toString ());
        
        buf.delete (0, buf.length ()).append (this.loggerTitle).append ("END : ").append ( ( System.currentTimeMillis () - startTimeTotal )).append ("ms");
        this.pcLogger.info (buf.toString ());
    }
    
    /**
     * ���� ��� ��� ��ȸ
     */
    private void getPaymentCancelInfo ( String cancelStartDate, String cancelEndDate ) {
        
        try {
            Map<String, String> dbMap = new HashMap<String, String> ();
            if ( StringUtils.isNotEmpty (cancelStartDate) && StringUtils.isNotEmpty (cancelEndDate) ) {
                dbMap.put ("startDate", cancelStartDate);
                dbMap.put ("endDate", cancelEndDate);
            } 
            
            // ���� ��� ��� ��ȸ
            this.paymentCancelList = this.paymentCancelXoMgr.getPaymentEtcCancel (dbMap);
            
            buf.delete (0, buf.length ()).append (this.loggerTitle).append ("Get PaymentEtcCancel Info : ").append (this.paymentCancelList.size ()).append ("��");
            this.pcLogger.info (buf.toString ());
            
        } catch ( Exception e ) {
            buf.delete (0, buf.length ()).append (this.loggerTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
        }
    }
    
    /**
     * ���� ��� ó��
     * @param logTitle
     * @return
     */
    public boolean processPaymentCancel ( Map<String, String> map, String logTitle ) {
        Map<String, Object> dbMap = new HashMap<String, Object> ();
        long startTime;
        int step = 1;
        
        StringBuffer logBuf = new StringBuffer ();
        
        boolean cancelSuccess = false;	// ��� ���� ����
        boolean empSuccess = false;     // ������ ���� ��� ��û ���� ����
        boolean mGiftCancelSuccess = false; //���� ��ǰ�� ���� ��� ��������
        int criticalCount	  = 0;		// ũ��Ƽ�� flag{C:ũ��Ƽ�� ���� �߻�}
        
        List<PaymentDto> newPaymentList = new ArrayList<PaymentDto> (); 	// ��Ż����� �ű� ���� ó����
        List<PaymentDto> paymentList = new ArrayList<PaymentDto>();			// ���� ���� ��ȸ
        
        String orderStatus = "";	// xo_order ���̺��� ���� status
        String orderNo = map.get ("order_no");
        String empNo = "";
        
        SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
        
        MGiftHttpService mGiftHttpService = new MGiftHttpService();		//����ϻ�ǰ�� ���޻� ���� ����
        
        try {
            // XO Ʈ����� ����
            xoSqlMap.startTransaction ();
            
            // step 1. XO : �ֹ� ���� ��ȸ START ==============================================================
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
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : �ֹ� ���� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 1. XO : �ֹ� ���� ��ȸ END   ==============================================================
            
            
            // step 2. XO : ���� ���� ��ȸ START ==============================================================
            startTime = System.currentTimeMillis ();
            paymentList = this.paymentCancelXoMgr.getPaymentList (xoSqlMap, orderNo);
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : ���� ���� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. XO : ���� ���� ��ȸ END ================================================================

            
            // step 3. XO : ���� ���� ��ȸ START ==============================================================
            startTime = System.currentTimeMillis ();
            List<OrderCouponDto> couponList = this.paymentCancelXoMgr.getOrderCouponInfo (xoSqlMap, orderNo);

            if(couponList != null){
            	this.pcLogger.info(logTitle + "�������� :: " + couponList);
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : ���� ���� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 3. XO : ���� ���� ��ȸ END   ==============================================================
            
            // step 4. ��ǰ�� GIFT : ���� ��� START ===============================================================
            // ���� �� �� �̻� ���� ���� �ʰ� ���� ī��Ʈ �� ������, telCancelSuccess, isSCKCardCancel, DB ����, ���� ī��Ʈ ����, ���� �̷� ���
            startTime = System.currentTimeMillis ();
            
            // ��ǰ�� GIFT ��� �Ϸ� ����
            boolean giftCancelSuccess = this.eGiftItemCencelProcess(xoSqlMap, paymentList);
        	
        	if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". e-Gift Item : ���� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
        	
        	// ��ǰ�� GIFT ���� �� �� �̻� ���� ����
        	if(!giftCancelSuccess)
        	{
        		// F-1. ���� �� ��ǰ�� GIFT �ѹ�(commitTransaction() ���� �����Ͽ� ���� �������� ó���� ����� RollBack)
        		try {
                    xoSqlMap.endTransaction ();
                } catch ( Exception ee ) {
                    pcLogger.error(ee.getMessage(), ee);
                }
        		
        		// F-2. XO Ʈ����� �ٽ� ����
                xoSqlMap.startTransaction ();
        		
        		// F-3. ���� ��� ���� �̷� ���
                startTime = System.currentTimeMillis ();
                boolean giftIsOk = false;
                Map<String, Object> giftDBMap = new HashMap<String, Object> ();
                try {
                	giftDBMap.put ("orderNo", orderNo); // �ֹ���ȣ
                	giftDBMap.put ("status",  "13"); 	// ����
                	
                	// �̷� ���
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
                
        		// ���� �� �� 
        		return cancelSuccess;
        	}
            
            // step 4. ��ǰ�� GIFT : ���� ��� END =================================================================

            // step 5. ������ ���� ��� ��û START =================================================================
            if (StringUtils.isNotEmpty(orderDto.getEmp_no()) && StringUtils.isNotEmpty(orderDto.getEmp_auth_app_no())) {
                Map<String, String> empResult = cancelEmpOrder(logTitle, orderDto);
                if (empResult.isEmpty()) {
                    empSuccess = false;
                    try {
                    	//XO Ʈ����� ����(commitTransaction() ���� �����Ͽ� ���� �������� ó���� ����� RollBack)
                        xoSqlMap.endTransaction ();
                    } catch ( Exception ee ) {
                        pcLogger.error(ee.getMessage(), ee);
                    }

                    // F-2. XO Ʈ����� �ٽ� ����
                    xoSqlMap.startTransaction ();

                    // F-3. ���� ��� ���� �̷� ���
                    startTime = System.currentTimeMillis ();
                    boolean historySuccess = false;
                    Map<String, Object> giftDBMap = new HashMap<String, Object> ();
                    try {
                        giftDBMap.put ("orderNo", orderNo); // �ֹ���ȣ
                        giftDBMap.put ("status",  "13"); 	// ����

                        // �̷� ���
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
                    // ���� �� ��
                    return cancelSuccess;
                }
                else {
                    empNo = orderDto.getEmp_no();
                    empSuccess = true;
                }
            }
            // step 5. ������ ���� ��� ��û END =================================================================
            buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". ����� ��ǰ�� ��� Start ")
            .append(System.currentTimeMillis() - startTime).append("ms");
            
         // ����� ��ǰ�� ���� ��� START ===========================================================
            
            mGiftCancelSuccess = mGiftHttpService.setMGiftOrderCancelProcess(paymentList, logTitle, logBuf);
            if(!mGiftCancelSuccess) {
            	// XO Ʈ����� ����(commitTransaction() ���� �����Ͽ� ���� �������� ó���� ����� RollBack)
            	xoSqlMap.endTransaction();
            	
            	buf.delete(0, buf.length()).append(logTitle).append("step ").append(step++)
            	.append(". ����� ��ǰ�� : ������� ����")
            	.append(System.currentTimeMillis() - startTime).append("ms");
            	mLogger.error(buf.toString());
            	
            	//���� ��� ���� �̷� ���
                startTime = System.currentTimeMillis ();
                boolean historySuccess = false;
                Map<String, Object> mGiftDBMap = new HashMap<String, Object> ();
                try {
                	
                	//XO Ʈ����� �ٽ� ����
                    xoSqlMap.startTransaction ();
                    
                    mGiftDBMap.put ("orderNo", orderNo); // �ֹ���ȣ
                    mGiftDBMap.put ("status",  "13"); 	// ����

                    // �̷� ���
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
            		//������ ���� �ѹ�
                    // ������ ���� ��ȣ�� ���û�� ���ι�ȣ�� ������Ʈ �ؾ� �ϱ� ������
                    // ���ο� Ʈ�������� ������
                    xoSqlMap.endTransaction();
                    xoSqlMap.startTransaction();
                    rollbackEmp(xoSqlMap, orderNo, empNo, logTitle);
                    xoSqlMap.commitTransaction();
            	}
            	return cancelSuccess;
            }else {
            	if ( this.pcLogger.isDebugEnabled () ) {
                	buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". ����� ��ǰ�� ��� End :")
                    .append(System.currentTimeMillis() - startTime).append("ms");
                }
            }
            
            
            // ����� ��ǰ�� ���� ��� ============================================================
            
            // step 5. ��Ż� : ���� ��� START ===============================================================
            startTime = System.currentTimeMillis ();
            cancelSuccess = this.setTelecomOrderCancel(paymentList, logTitle, logBuf);
            
            if ( cancelSuccess ) {
				orderStatus = "12";		// ���� ���
			}else {
				// ��Ż� : ���� ��� ���н� ���� ���� ó��
				orderStatus = "12";		// ���� ���
				cancelSuccess = true;
				
				if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". ��Ż� : ������� ���� (��������) ")
                            .append(System.currentTimeMillis() - startTime).append("ms");
					this.pcLogger.debug (buf.toString ());
				}
				
				criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
				if(mGiftCancelSuccess) {
                	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                }
				
				///////////////////////////////////////////////
				
//				orderStatus = "13";		// ���� ��� ����
				//PG���� ��� ���н� ���ó���� ��Ż����޸� �ٽ� ����ó����
//				criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
			}
            
            if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ��Ż� : ���� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
            // step 5. ��Ż� : ���� ��� END ==================================================================
            
        	
        	// step 6. MSR : ���� ��� ��� START =============================================================
        	if(cancelSuccess){
        		startTime = System.currentTimeMillis ();
        		SqlMapClient msrSqlMap2 = IBatisSqlConfig.getMsrSqlMapInstance ();
        		try{
        			// MSR Ʈ����� ����
                    msrSqlMap2.startTransaction ();
                    
        			//��� ���� ��� ó��
        			if(couponList != null && couponList.size() > 0){
        				Map<String, String> couponMap = new HashMap<String, String>();
        				String[] couponArr = null;
        				String   coupons = "";
        				
        				for(OrderCouponDto couDto : couponList){
        					try{
        						boolean isCheck = true;
        						
        						// �ߺ��� ���� ��ȣ�� �ִ��� üũ
        						if(StringUtils.isBlank(coupons)) {
        							coupons = couDto.getCoupon_number();		                    	
        						} else {
        							couponArr = coupons.split("\\,");
        							for(int i = 0; i < couponArr.length; i++) {
        								// �ߺ��� ���� ��ȣ�� �ִ��� Ȯ��
        								if(couponArr[i].equals(String.valueOf(couDto.getCoupon_number()))) {
        									isCheck = false;
        								}
        							}
        							coupons = coupons + "," + couDto.getCoupon_number();
        						}
        						
        						// �ߺ��� ���� ��ȣ�� ���� ��� ������Ʈ
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
        						
        						// MSR DB Exception ũ��Ƽ�� �α� ===================
        						criticalCount++;
        						logBuf.append (logTitle).append ("MSR ���� ��� ����")
	        						.append ("|").append ("orderNo=").append (orderNo)
	        						.append ("|").append ("couponNumber=").append (couDto.getCoupon_number())
	        						.append ("|").append ("couponAmount=").append (couDto.getCoupon_dc_amt())
	        						.append ("|").append ("Exception=").append (ex.getMessage ());
        						this.pcLogger.info (logBuf); 	// �⺻ �α�
        						this.cLogger.info (logBuf); 	// ũ��Ƽ�� �α�
        						logBuf.delete (0, logBuf.length ());
        						// MSR DB Exception ũ��Ƽ�� �α� ===================
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
        			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : ���� ��� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        			this.pcLogger.debug (buf.toString ());
        		}
        	}
        	// step 6. ���� ��� ��� END =====================================================================
        	
        	
            boolean dbSuccess = false;	// DB ó�� ���
            dbMap.put ("orderNo", orderNo);
            dbMap.put ("status",  orderStatus); // ����
            
            // step 7. �ֹ� ���� ������Ʈ START   ==============================================================
            if(cancelSuccess) {	// ��� ������ �����͸� ������Ʈ
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
            // ���� ��� ������ ��� critical_log�� �ִ� ��� ������Ʈ	
            }else{
            	startTime = System.currentTimeMillis ();
            	try {
            		if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//ũ��Ƽ�� ���� ����
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
            // step 7. �ֹ� ���� ������Ʈ END   ================================================================

            // step 8. �ֹ� ���� ���� �̷� ���� START   =========================================================
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
            // step 8. �ֹ� ���� ���� �̷� ���� END   ===========================================================
            
            
            // step 9. ���� ���� ������Ʈ START   ==============================================================
        	startTime = System.currentTimeMillis ();
        	for(PaymentDto paymentDto : paymentList){
        		//��Ż����� ��� ������Ʈ
        		if(StringUtils.equals(paymentDto.getPay_method(), "K") || StringUtils.equals(paymentDto.getPay_method(), "U")
        				|| StringUtils.equals(paymentDto.getPay_method(), "T")|| StringUtils.isNotEmpty(paymentDto.getPrcm_frst_code())){
        			//��� ������ �����͸� ������Ʈ
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
        	
            //���� ��ҽ� ���� �߻������� ���� �ѹ�(��Ż�)�� ������ ���
            if(newPaymentList != null && newPaymentList.size() > 0){
            	try{
	            	for(PaymentDto payDto : newPaymentList){
	            		this.paymentCancelXoMgr.insertPaymentOfPaymentHist(xoSqlMap, payDto);
	            	}
            	}catch(Exception ex){
            		buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
                    this.pcLogger.error (buf.toString (), ex);
                    this.eLogger.error (buf.toString (), ex);
                    this.cLogger.info (buf.toString (), ex); 	// ũ��Ƽ�� �α�
            	}
            }
            // step 9. ���� ���� ������Ʈ END   =================================================================
            
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
                            .append("������ ���� ��� �ѹ� ����.")
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
                    .append("����� ��ǰ�� ��� �ѹ� ����.")
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
     * ������ ���� ��ҿ�û �ѹ�ó��.
     * @param xoSqlMap SqlMapClient
     * @param orderNo �ֹ���ȣ
     * @param empNo ������ ��ȣ
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
     * ��Ż����� ��� ó��
     */
    public boolean setTelecomOrderCancel(List<PaymentDto> paymentList, String logTitle, StringBuffer logBuf){
    	int telCount = 0;

		//������� ���� ����
    	String telecommType = telecomConf.getString("telecomm.birthYmd.check");
		TelecommUtil telecommUtil = new TelecommUtil(telecommType);
    	
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			//��Ż������� ��츸 ó��
    			if(StringUtils.equals(payDto.getPay_method(), "K") || StringUtils.equals(payDto.getPay_method(), "U")){
    				telCount++;
    				
    				try{
    					//���γ�¥�� ��Ż� ��ҿ� ���γ�¥�� ��ȯ
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
    					tLogger.info(logBuf.toString());		//��Ż� �α�
    					pcLogger.info(logBuf.toString());		//INFO �α�
    					logBuf.delete(0, logBuf.length ());
    					
    					//��Ż� ��� ����
    					if(StringUtils.equals(RespCd, "0000")){
    						payDto.setCancel_result_code("00");
    						payDto.setResult_msg(ScreenExpr);
    						payDto.setStatus("C");
    						
    					//��Ż� ��� ����
    					}else{
    						this.failReason = RespCd+"|"+ScreenExpr;	// ���� ����
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
    	
    	//��Ż����ް� ���� ��� ������ true ����
    	if(telCount == 0){
    		return true;
    	}
    	
    	return true;
    }
    
    /**
     * ��Ż����� ���� ó��
     */
    public int setTelecomOrderPayment(List<PaymentDto> paymentList, List<PaymentDto> newPaymentList, String logTitle, StringBuffer logBuf){
    	int inCriticalCnt = 0;
    	
		String telecommType = this.telecomConf.getString("telecomm.birthYmd.check");
		TelecommUtil telecommUtil = new TelecommUtil(telecommType);
    	
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			//��Ż� �̸鼭 ��Ұ� ������ �ǿ� ���ؼ��� �ٽ� ����ó��
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
    					tLogger.info(logBuf.toString());		//��Ż� �α�
    					pcLogger.info(logBuf.toString());		//INFO �α�
    					logBuf.delete (0, logBuf.length ());
    					
    					//��Ż� ��� �ѹ�(�����) ����
    					if(StringUtils.equals(RespCd, "0000")){
    						PaymentDto rePayDto = new PaymentDto();
    						rePayDto.setOrder_no(payDto.getOrder_no());
    						rePayDto.setPay_method(payDto.getPay_method());
    						rePayDto.setAmount(payDto.getAmount());
    						rePayDto.setSbc_card_no(payDto.getSbc_card_no());
    						rePayDto.setTid(ApprovNo);
    						rePayDto.setResult_code("00");
    						rePayDto.setResult_msg(ScreenExpr);
    						rePayDto.setTel_app_date(DateTime.getTelApplDateToDbApplDate(ApprovDT));	//��Ż� �����Ͻ�(DB �����)
    						rePayDto.setCancel_result_code("");
    						rePayDto.setStatus("P");
    						newPaymentList.add(rePayDto);	//���������� ��ҵȰ� ó��
    						
    					//��Ż� ��� ����
    					}else{
    						inCriticalCnt++;
    						this.cLogger.info(logBuf.toString());		//ũ��Ƽ�� �α�
    						this.failReason = RespCd+"|"+ScreenExpr;	// ���� ����
    					}
    				}catch(Exception e){
    					inCriticalCnt++;
    					this.resultCode = "07";
    					this.resultMsg = e.getMessage ();
    					logBuf.delete (0, logBuf.length ()).append (logTitle).append (e.getMessage ());
    					this.pcLogger.error (logBuf.toString (), e);
    					this.eLogger.error (logBuf.toString (), e);
    					this.cLogger.info (logBuf.toString (), e);	//ũ��Ƽ�� �α�
    				}
    			}
    		}
    	}
    	
    	return inCriticalCnt;
    }
    
    /*
     * ��ǰ�� GIFT ���
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
	
						// ��� ��� (H11)
						dbMap.put("status", "H11");
						
						// ���� ��� �� ��� �̷� ���
						this.paymentCancelXoMgr.insertGiftUseHistory(xoGiftSqlMap, dbMap);
						
						// ���� (G00)
						dbMap.put("status", "G00");
						
						// ��ǰ�� GIFT ���� ����
						this.paymentCancelXoMgr.updateGiftStatus(xoGiftSqlMap, dbMap);
						
						// ��ǰ�� GIFT ��� ���� ��
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
