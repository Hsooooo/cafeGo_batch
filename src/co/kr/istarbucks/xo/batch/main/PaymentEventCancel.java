
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
import com.inicis.inipay.INIpay50;

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
import co.kr.istarbucks.xo.batch.common.pg.NicePgService;
import co.kr.istarbucks.xo.batch.common.pg.SmartroPgService;
import co.kr.istarbucks.xo.batch.common.pg.dto.NiceResultDto;
import co.kr.istarbucks.xo.batch.common.pg.dto.SmartroPgInfoDto;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommService;
import co.kr.istarbucks.xo.batch.common.telecomm.TelecommUtil;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DataCode;
import co.kr.istarbucks.xo.batch.common.util.DateTime;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.TripleDesEGift;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.EmpMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelMsrMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;

/**
 * �� �ڵ� ���� ��� PaymentEventCancel.
 * �� �ֹ��� ���� 3�� �������� �ֹ��� ��� ���������� ��� ó��
 * @author namgu1
 * @since 2016. 6. 08.
 * @version $Revision: 1.8 $
 */
public class PaymentEventCancel {
    
    private final Logger pcLogger = Logger.getLogger ("PEC_INFO");
    private final Logger eLogger = Logger.getLogger ("PEC_ERROR");
    private final Logger fLogger = Logger.getLogger ("PEC_FD");
    private final Logger pLogger = Logger.getLogger ("PEC_PG");
    private final Logger tLogger = Logger.getLogger ("PEC_TEL");
    private final Logger cLogger = Logger.getLogger ("PEC_CRITICAL");
    private final Logger mLogger = Logger.getLogger("PC_MGIFT");
    
    private Configuration conf = null;
    private final Configuration telecomConf;
    private int sbcMazBalance = 0; // ��Ÿ���� ī�� �ܾ� ���Ѽ�
    private String iniHomeDir = ""; // PG HOME DIR
    private String iniMid = ""; // PG ����Ű
    private String keyPwd = ""; // PG ���� ��й�ȣ
    
    // ����� : true, ���߸�� : false
    public boolean isRealMode = true;
    public String resultCode = "";
    public String resultMsg = "";
    public String logTitle = "";
    public int remainAmount = 0;
    
    public int procCnt = 0;
    public String failReason = "";
    public int refundCnt = 0;
    public int compCnt = 0;	// �����Ϸ� ��� �Ǽ�
    public int waitCnt = 0;	// ������� ��� �Ǽ�
    
    StringBuffer refundInfo = new StringBuffer ();
    StringBuffer buf = new StringBuffer ();
    
    private final PaymentCancelXoMgr paymentCancelXoMgr;
    private final PaymentCancelMsrMgr paymentCancelMsrMgr;
    private final EmpMgr empMgr;
    private final String loggerTitle;
    
    private String samIniMid = "";
    private String samKeyPwd = "";
    private String openApiModel = ""; 
    
    private List<Map<String, String>> paymentCancelList;
    
    private final SmartroPgService smartroPgService;
    private SmartroPgInfoDto smartroPgInfoDto;
    
    public PaymentEventCancel () {
        this.paymentCancelXoMgr = new PaymentCancelXoMgr ();
        this.paymentCancelMsrMgr = new PaymentCancelMsrMgr ();
        this.loggerTitle = "[PaymentEventCancel] ";
        this.telecomConf = CommPropertiesConfiguration.getConfiguration ("telecom.properties");
//        this.empMgr = new EmpMgr(pcLogger, eLogger);
        this.empMgr = new EmpMgr(pcLogger);
        this.smartroPgService = new SmartroPgService();
        this.smartroPgInfoDto = smartroPgService.getPgInfoDto(DataCode.PG_SERVICE_SIRENORDER);
    }
    
    public void start () {
        long startTimeTotal = System.currentTimeMillis ();
        long startTime = System.currentTimeMillis ();
        
        buf.delete (0, buf.length ()).append (this.loggerTitle).append ("START");
        this.pcLogger.info (buf.toString ());
        
        // ���� ��� ��� ��ȸ
        // �� ���� ������ ����ִ� ���� ��ϸ� ��ȸ
        this.getPaymentCancelInfo();
        
        // ���� ��� ó��
        int successCnt = 0;
        int failCnt = 0;
        StringBuffer failInfo = new StringBuffer ();
        if ( paymentCancelList != null && paymentCancelList.size () > 0 ) {
            
            this.conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
            this.sbcMazBalance = conf.getInt ("sbc.balance.maximum"); // ��Ÿ���� ī�� �ܾ� ���Ѽ�
            
            this.iniHomeDir = conf.getString ("inipay.homeDirectory"); // PG HOME DIR
            this.iniMid = conf.getString ("inipay.mid"); // PG ����Ű
            this.keyPwd = conf.getString ("inipay.keyPassWord"); // PG ���� ��й�ȣ
            this.samIniMid = conf.getString ("sam.inipay.mid");         // �Ｚ���� PG ����Ű
            this.samKeyPwd = conf.getString ("sam.inipay.keyPassWord"); // �Ｚ����  PG ���� ��й�ȣ
            this.openApiModel = conf.getString("open.api.model","81");
            
            for ( Map<String, String> map : paymentCancelList ) {
                buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (this.procCnt).append (" : ").append (map.get ("user_id")).append (" : ").append (map.get ("order_no")).append (") ");
                String logTitle = buf.toString ();
                
                buf.delete (0, buf.length ()).append (logTitle).append ("START");
                this.pcLogger.info (buf.toString ());
                
                startTime = System.currentTimeMillis ();
                boolean isPayment = false;
                this.failReason = "";
                try {
                	// ��Ÿ���� ���հ������� �����Ͽ� ó��
                	if(StringUtils.indexOf(map.get("pay_method"),"S") > -1 ) {                	
                		isPayment = this.processPaymentCancel (map, logTitle);
                	} else {
                		isPayment = this.processPaymentPgCancel (map, logTitle);
                	}
                    
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
        
        buf.delete (0, buf.length ()).insert (0, "=================================================")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("���� ��� ��� \t     : ").append (procCnt).append ("��")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("���� ��� ���� \t     : ").append (successCnt).append ("�� (ȯ�� : ").append (refundCnt).append ("��)")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("���� ��� ���� \t     : ").append (failCnt).append ("��")
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("ȯ�� �ֹ� ���� \t     : ").append (new String (refundInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t ").append ("��� ���� �ֹ� ���� : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t\t\t\t\t\t =================================================");
        this.pcLogger.info (buf.toString ());
        
        buf.delete (0, buf.length ()).append (this.loggerTitle).append ("END : ").append ( ( System.currentTimeMillis () - startTimeTotal )).append ("ms");
        this.pcLogger.info (buf.toString ());
    }
    
    /**
     * ���� ��� ��� ��ȸ
     */
    private void getPaymentCancelInfo() {
        
        try {        	 
        	 // ���� ��� ��� ��ȸ
             this.paymentCancelList = this.paymentCancelXoMgr.getPaymentEventCancel ();            
             buf.delete (0, buf.length ()).append (this.loggerTitle).append ("Get PaymentCancel Info : ").append (this.paymentCancelList.size ()).append ("��");
             this.pcLogger.info (buf.toString ());
            
        } catch ( Exception e ) {
            buf.delete (0, buf.length ()).append (this.loggerTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
        }
    }
    
    /**
     * ���� ��� ó��(��Ÿ����ī��)
     * @param orderDto
     * @param paymentList
     * @param logTitle
     * @return
     */
    public boolean processPaymentCancel ( Map<String, String> map, String logTitle ) {
        Map<String, Object> dbMap = new HashMap<String, Object> ();
        long startTime;
        int step = 1;
        
        boolean telCancelSuccess = false;	// ��Ż����� ��� �Ϸ� ����
        boolean isSCKCardCancel  = false; 	// SCK CARD ���� ��� �Ϸ� ����
        boolean isRefundCard 	 = false; 	// Ÿ ī�� ���� ȯ�� ����
        boolean empSuccess       = false;   // ������ ���� ��� ��û ���� ����
        boolean mGiftCancelSuccess = false; // ����� ��ǰ�� ���� ��� ��������
        int criticalCount	 	 = 0;		// ũ��Ƽ�� flag{C:ũ��Ƽ�� ���� �߻�}
        
        StringBuffer logBuf = new StringBuffer ();
        
        List<PaymentDto> sckCardPaymentList = new ArrayList<PaymentDto> (); 	// SCK ī�� ���� ����
        List<PaymentDto> newPaymentList     = new ArrayList<PaymentDto> (); 	// ��Ż����� �ű� ���� ó����
        List<PaymentDto> paymentList = new ArrayList<PaymentDto>();			// ���� ���� ��ȸ
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
            if ( !StringUtils.equals (orderDto.getStatus (), "11") && !StringUtils.equals (orderDto.getStatus (), "22") ) {
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
            
            if ( paymentList == null || paymentList.size () == 0 ) {
                buf.delete (0, buf.length ()).append ("paymentList is null - ").append (orderNo);
                throw new XOException (buf.toString ());
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : ���� ���� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. XO : ���� ���� ��ȸ END   ==============================================================
            
            
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
            // ���� �� �� �̻� ���� ���� �ʰ� ���� ī��Ʈ �� ������, DB ����, ���� ī��Ʈ ����, ���� �̷� ���
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
        		return isSCKCardCancel;
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
                    return isSCKCardCancel;
                }
                else {
                    empSuccess = true;
                    empNo = orderDto.getEmp_no();
                    
                    if ( this.pcLogger.isDebugEnabled () ) {
            			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (".������ ���� ���: ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            			this.pcLogger.debug (buf.toString ());
            		}
                }
            }
            // step 5. ������ ���� ��� ��û END =================================================================
            
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
            	return isSCKCardCancel;
            }else {
            	if ( this.pcLogger.isDebugEnabled () ) {
        			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (".����� ��ǰ�� ���� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        			this.pcLogger.debug (buf.toString ());
        		}
            }
            // ����� ��ǰ�� ���� ��� ============================================================
            
            
            // step 5. ��Ż� : ���� ��� START ===============================================================
            startTime = System.currentTimeMillis ();
            telCancelSuccess = this.setTelecomOrderCancel(paymentList, logTitle, logBuf);
            
            if(!telCancelSuccess) {
				// ��Ż� : ���� ��� ���н� ���� ���� ó��
            	telCancelSuccess = true;
            	if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete(0, buf.length()).append(logTitle).append("step ").append(step).append(". ��Ż� : ������� ���� (��������) ").append(System.currentTimeMillis() - startTime).append("ms");
					this.pcLogger.debug (buf.toString ());
				}
            }
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete(0, buf.length()).append(logTitle).append("step ").append(step++).append(". ��Ż� : ���� ��� END : ").append(System.currentTimeMillis() - startTime).append("ms");
				this.pcLogger.debug (buf.toString ());
			}
            // step 5. ��Ż� : ���� ��� END =================================================================
            
            
            // step 6. MSR : ȸ���� ��ϵ� ��Ÿ���� ī�� ��ȸ START =============================================
            RefundSbcDto refundSbcDto = new RefundSbcDto ();
            if(telCancelSuccess){
	            startTime = System.currentTimeMillis ();
	            SqlMapClient msrSqlMap1 = IBatisSqlConfig.getMsrSqlMapInstance ();
	            try {
	                msrSqlMap1.startTransaction ();
	                
	                dbMap.put ("user_id", orderDto.getUser_id ());
	                dbMap.put ("msr_user_reg_card_table", "MSR_USER_REG_CARD# URC");
	                dbMap.put ("msr_card_master_table", "MSR_CARD_MASTER# CM");
	                dbMap.put ("card_state", "R"); // ��ϵ� ī�常�� ��ȸ
	                
	                // ��� ���� ��� ī�� ���
	                List<UserRegCardDto> regCardlist = this.paymentCancelMsrMgr.getUserRegCardStateList (msrSqlMap1, dbMap);
	                dbMap.clear ();
	                
	                // �����ߴ� ��Ÿ���� ī�尡 ���� ��� ���¿� ���� 
	                if ( paymentList != null ) {
	                    payList:
	                    for ( PaymentDto dto : paymentList ) {
	                        // SCK ī�� ������ �ƴϸ� continue
	                        if ( !StringUtils.equals (dto.getPay_method (), "S") ) {
	                            continue payList;
	                        }
	                        if ( regCardlist == null || regCardlist.size () == 0 ) {
	                            // ȸ������ ��ϵ� SCK ī�� 0���̸� ���� ��� �ڵ� 98
	                            dto.setCancel_result_code ("98");
	                            sckCardPaymentList.add (dto);
	                        } else {
	                            cardList:
	                            for ( UserRegCardDto cardDto : regCardlist ) {
	                                if ( StringUtils.equals (dto.getSbc_card_no (), cardDto.getCard_number ()) ) {
	                                    // ���� MSR_CARD_MASTER �ܾװ� ���� ��� �ݾ��� ���� �ܾ׻����� ������ ���� ��� �ڵ� 97
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
	                            dto.setCancel_result_code ("98"); // ����ī�尡 ȸ������ ��ϵǾ� ���� ������  ���� ��� �ڵ� 98
	                            sckCardPaymentList.add (dto);
	                        }
	                    } // end payList
	                }
	            } catch ( Exception e ) {
	                throw e;
	            } finally {
	                msrSqlMap1.commitTransaction (); // (��ȸ) Ʈ������ Ŀ��
	                try {
	                    msrSqlMap1.endTransaction ();
	                } catch ( Exception ee ) {
	                    pcLogger.error(ee.getMessage(), ee);
	                }
	            }
	            if ( this.pcLogger.isDebugEnabled () ) {
	                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : ȸ���� ��ϵ� ��Ÿ���� ī�� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                this.pcLogger.debug (buf.toString ());
	            }
	            // step 6. MSR : ȸ���� ��ϵ� ��Ÿ���� ī�� ��ȸ END ===========================================
	            
	            
	            // step 7. FD : ��� ���(reload) ó�� START ==================================================
	            startTime = System.currentTimeMillis ();
	            try {
	                boolean isOk = false;
	                if ( sckCardPaymentList != null ) {
	                    for ( PaymentDto dto : sckCardPaymentList ) {
	                        if ( StringUtils.isNotEmpty (dto.getCancel_result_code ()) ) continue;
	                        
	                        // FD reload ȣ��
	                        if(isRealMode){
	                        	isOk = this.setFdBalance(dto.getSbc_card_no (), String.valueOf (dto.getAmount ()), "X", FDCode.ORDER_CODE.USE_CANCEL, dto.getOrder_no(), orderDto.getUser_id ());
	                        }else{
	                        	isOk = true;
	                        	this.resultCode = "00";
	                        	this.resultMsg = "FD Success";
	                        	this.remainAmount = 50000;
	                        }
	                        
	                        // FD ���� - ���� ��� ��� �α� ===================
	                        logBuf.delete(0, buf.length()).append (logTitle).append ("FD Reload")
	                            .append ("|").append ("orderNo=").append (orderNo)
	                            .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
	                            .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
	                            .append ("|").append (this.resultCode)
	                            .append ("|").append (this.resultMsg);
	                        this.fLogger.info (logBuf); // FD �α�
	                        this.pcLogger.info (logBuf); // �⺻ �α�
	                        logBuf.delete (0, logBuf.length ());
	                        // FD ���� - ���� ��� ��� �α� ===================
	                        
	                        dto.setCancel_result_code (this.resultCode);
	                        if ( isOk ) {
	                            dto.setSbc_remain_amt (this.remainAmount);
	                        }
	                    }
	                }
	            } catch ( Exception e ) {
	                buf.delete (0, buf.length ()).append (this.loggerTitle).append (e.getMessage ());
	                this.pcLogger.error (buf.toString (), e);
	                this.eLogger.error (buf.toString (), e);
	            }
	            
	            // ������ ���ī�带  FD ó�� �� Exception �߻��� ��� �� ���� ���� �� ���� ��Ͽ� �߰� 
	            try {
	                for ( PaymentDto dto : sckCardPaymentList ) {
	                    if ( StringUtils.equals (dto.getCancel_result_code (), "00") ) continue;
	                    
	                    // ��� ��� �ڵ� ���� ���
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
	                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". FD : ��� ���(reload) ó�� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
	                this.pcLogger.debug (buf.toString ());
	            }
	            // step 7. FD : ��� ���(reload) ó�� START ==================================================
	            
	            
	            // step 8. MSR : e-Gift ī�� ���� START ======================================================
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
	                } else {
	                    refundSbcDto.setOrder_no (orderDto.getOrder_no ()); // �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	                    refundSbcDto.setUser_id (orderDto.getUser_id ()); // ����ھ��̵�
	                    refundSbcDto.setAmount (paymentAmount); // �ݾ�
	                    refundSbcDto.setSbc_type ("2"); // ����{1-���ī��, 2-eGift}
	                    
	                    // e-Gift ī�� ���� ��û
	                    buf.delete (0, buf.length ()).append (logTitle).append ("egiftCardPublish : ");
	                    refundSbcDto = this.egiftCardPublish (refundSbcDto, buf.toString ());
	                    
	                    if ( StringUtils.isNotEmpty (refundSbcDto.getTarget_card_number ()) && StringUtils.equals (refundSbcDto.getResult (), "00") ) { // ���{00-����, �� �� ����}
	                        isSCKCardCancel = true;
	                        isRefundCard = true;
	                    } else {
	                        isSCKCardCancel = false;
	                    }
	                    
	                    if ( this.pcLogger.isDebugEnabled () ) {
	                        buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : e-Gift ī�� ���� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
            
            // e-Gift ī�� ���� ������ ��� FD ��� ���(reload) ó�� ����
            if ( !isSCKCardCancel ) {
                if ( sckCardPaymentList != null && sckCardPaymentList.size () > 0 ) {
                    boolean isOk = false;
                    for ( PaymentDto dto : sckCardPaymentList ) {
                        if ( StringUtils.contains ("|00|97|98|99|", dto.getCancel_result_code ()) ) continue;
                        
                        // FD voidOfReload ȣ��
                        isOk = this.setFdBalance (dto.getSbc_card_no (), String.valueOf (dto.getAmount ()), "C", FDCode.ORDER_CODE.REFUND, dto.getOrder_no(), orderDto.getUser_id ());
                        
                        // FD ���� - ���� ��� ���� ��� �α� ===================
                        logBuf.append (logTitle).append ("FD voidOfReload")
                            .append ("|").append ("orderNo=").append (orderNo)
                            .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
                            .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
                            .append ("|").append (this.resultCode)
                            .append ("|").append (this.resultMsg);
                        this.fLogger.info (logBuf); // FD �α�
                        this.pcLogger.info (logBuf); // �⺻ �α�
                        if ( !isOk ) {
                        	criticalCount++;
                            this.cLogger.info (logBuf); // ũ��Ƽ�� �α�
                        }
                        logBuf.delete (0, logBuf.length ());
                        // FD ���� - ���� ��� ���� ��� �α� ===================
                    }
                }
                
                //���� ��Ұ� ������ ��� �̹� ��� ó���� ��Ż� ���޸� �ٽ� ����ó����
                criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
                //����� ��ǰ�� �ѹ� ó��
                if(mGiftCancelSuccess) {
                	criticalCount  += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
                }
            } // isSCKCardCancel
            // step 8. MSR : e-Gift ī�� ���� END   ==========================================================
            

            // step 9. MSR : SBC ī�� �ܾ� ���� START =========================================================
            if ( telCancelSuccess &&  isSCKCardCancel ) {
                startTime = System.currentTimeMillis ();
                SqlMapClient msrSqlMap2 = IBatisSqlConfig.getMsrSqlMapInstance ();
                try {
                    // MSR Ʈ����� ����
                    msrSqlMap2.startTransaction ();
                    
                    for ( PaymentDto dto : sckCardPaymentList ) {
                        if ( !StringUtils.equals (dto.getCancel_result_code (), "00") ) continue;
                        
                        // ���� �߻��ϴ��� ���� �� ���� 
                        try {
                            dbMap.put ("balance", String.valueOf (dto.getSbc_remain_amt ()));
                            dbMap.put ("cardNumber", dto.getSbc_card_no ());
                            // ����� ���ī�� �ܾ� ����
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
                            
                            // ī�� ��� �̷� ��ȸ
                            CardUseHistoryDto cardUseDto = this.paymentCancelMsrMgr.getMsrCardUseHistoryForSmartOrder (msrSqlMap2, dbMap);
                            
                            dbMap.put ("branch",cardUseDto.getBranch());
                            dbMap.put ("businessDate",cardUseDto.getBusiness_date());
                            dbMap.put ("posNumber",cardUseDto.getPos_number());
                            dbMap.put ("posTrdNumber",cardUseDto.getPos_trd_number());
                            dbMap.put ("sendDate",cardUseDto.getSend_date());
                            dbMap.put ("sendTime",cardUseDto.getSend_time());
                            
                            
                            // ����� ���ī�� �ܾ� ����� �����丮 ���
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
                            
                            // MSR DB Exception ũ��Ƽ�� �α� ===================
                            criticalCount++;
                            logBuf.append (logTitle).append ("MSR ī�� ���� ��� �ܾ� ���� ����")
                                .append ("|").append ("orderNo=").append (orderNo)
                                .append ("|").append ("paymentOrder=").append (dto.getPayment_order ())
                                .append ("|").append ("cardNumber=").append (dto.getSbc_card_no ())
                                .append ("|").append ("balance=").append (dto.getSbc_remain_amt ())
                                .append ("|").append ("Exception=").append (e.getMessage ());
                            this.pcLogger.info (logBuf); // �⺻ �α�
                            this.cLogger.info (logBuf); // ũ��Ƽ�� �α�
                            logBuf.delete (0, logBuf.length ());
                            // MSR DB Exception ũ��Ƽ�� �α� ===================
                        } // try
                    } //for
                    
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
                    buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : SBC ī�� �ܾ� ���� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                    this.pcLogger.debug (buf.toString ());
                }
            }
            // step 9. MSR : SBC ī�� �ܾ� ���� END   =========================================================

            // step 10. XO : �ֹ����� ���� ��� ���·� ���� START ================================================
            startTime = System.currentTimeMillis ();
            boolean isOk = false;
            
            String status = "";
            if ( isSCKCardCancel ) {
                status = "12"; // ���� ��� ����
            } else if ( !isSCKCardCancel ) {
                status = "13"; // ���� ��� ����
            } else {
                status = "14"; // ���� ��� �κ� ����
            }
            
            // ���� ��� ������ ��� �ֹ����� ������Ʈ ���� ����.
            if ( !StringUtils.equals (status, "13") ) {
                // �ֹ� ���� ������Ʈ
                isOk = false;
                startTime = System.currentTimeMillis ();
                try {
                    dbMap.put ("orderNo", 	   orderNo);
                    dbMap.put ("status", 	   status); 		// ����
                    if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//ũ��Ƽ�� ���� ����
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
            // ���� ��� ������ ��� critical_log�� �ִ� ��� ������Ʈ
            }else{
            	try {
                    dbMap.put ("orderNo", 	   orderNo);
                    if(criticalCount > 0){
                    	dbMap.put ("criticalFlag", "C");	//ũ��Ƽ�� ���� ����
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
            

            // �ֹ� ���� ���� �̷� ������Ʈ
            startTime = System.currentTimeMillis ();
            isOk = false;
            try {
                dbMap.put ("orderNo", orderNo); // �ֹ���ȣ
                dbMap.put ("status",  status); 	// ����
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
            
            // ���� ���� ������Ʈ ó��
            List<PaymentDto> paymentDBList = new ArrayList<PaymentDto> ();
            // ��Ż� ��� ó���� ���
            for(PaymentDto payDto : paymentList){
            	if( (StringUtils.equals(payDto.getPay_method(), "K") || StringUtils.equals(payDto.getPay_method(), "U")
            			|| StringUtils.equals(payDto.getPay_method(), "T")|| StringUtils.isNotEmpty(payDto.getPrcm_frst_code())) 
            		  && StringUtils.equals(payDto.getCancel_result_code(), "00")){
            		paymentDBList.add(payDto);
            	}
            }
            // ��Ÿ����ī�� ó���� ���
            if ( isSCKCardCancel ) {
                paymentDBList.addAll (sckCardPaymentList);
            }
            
            if ( paymentDBList != null && paymentDBList.size () != 0 ) {
                for ( PaymentDto dto : paymentDBList ) {
                    if ( StringUtils.isEmpty (dto.getCancel_result_code ()) ) {
                        dto.setCancel_result_code ("99");
                    }
                    
                    // ���� ���� ������Ʈ
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
            
            if ( isRefundCard ) {
                // Ÿ ī�� ���� ȯ�� ���
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
                
                // eGift ī�� ��� SMS �߼�
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
                        
                        // SMS �߼� ��û 
                        SmtTranDto smtTranDto = new SmtTranDto ();
                        smtTranDto.setPriority ("S"); //���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
                        smtTranDto.setSubject (subject);
                        smtTranDto.setContent (content);
                        smtTranDto.setCallback (callback);
                        smtTranDto.setRecipient_num (orderDto.getPhone ());
                        smtTranDto.setReservation_time (reservationTime);
                        
                        Long mtPr = this.paymentCancelXoMgr.insertSmtTran (xoSqlMap, smtTranDto);
                        
                        // SMS �̷� ��� 
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
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". XO : �ֹ����� ���� ��� ���·� ���� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 10. XO : �ֹ����� ���� ��� ���·� ���� END   ==============================================
            
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
          //����� ��ǰ�� �ѹ� ó��
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
        
        return isSCKCardCancel;
    }

    private Map<String, String> cancelEmpOrder(String logTitle, OrderDto orderDto) throws Exception {
        try {
            return this.empMgr.cancel(orderDto.getOrder_no(), orderDto.getEmp_no(), logTitle);
        }
        catch (Exception e) {
            pcLogger.error(e.getMessage(), e);
            return new HashMap<String, String>();
        }
    }

    private void rollbackEmp(SqlMapClient xoSqlMap, String orderNo, String empNo, String logTitle) throws Exception {
        Map<String, String> rollback = this.empMgr.rollback(orderNo, empNo, logTitle);
        rollback.put("orderNo", orderNo);
        rollback.put("empNo", empNo);
        this.paymentCancelXoMgr.updateEmpOrder(xoSqlMap, rollback);
    }

    /**
     * e-Gift Card ����
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
            
            // step 1. MSR : �����ڿ��� �߼� ������ ī�� ��ȸ START ===========================================
            startTime = System.currentTimeMillis ();
            
            // ��밡���� ī�� ä�� (procedure - UPDATE:MSR_EGIFT_CARD_INFO.USE_YN='N') - ä���� ī��� ���ó�� USE_YN (Y:��밡���� ī��, N:���Ұ����� ī��)
            int publicationCnt = 1;
            String cardNumber = this.paymentCancelMsrMgr.getPaymentCardListProc (msrSqlMap1, publicationCnt);
            
            // ī�� ä���� ������ �ִ� ��� '-1'�� ����
            if ( StringUtils.isEmpty (cardNumber) || StringUtils.equals (cardNumber, "-1") ) { throw new XOException ("e-Gift �߱� ������ ī�尡 ����"); }
            
            String[] cardNumberArr = cardNumber.split ("\\,");
            if ( cardNumberArr.length != publicationCnt ) { throw new XOException ("e-Gift �߱� ������ ������ ī�尡 ����"); }
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : �����ڿ��� �߼۰����� ī�� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 1. MSR : �����ڿ��� �߼� ������ ī�� ��ȸ END =============================================
            
            // step 2. MSR : eGiftCard ���� ��ȸ START ========================================================
            // eGiftCard ���� ��ȸ
            List<String> eGiftCardList = Arrays.asList (cardNumberArr);
            eGiftCardInfoList = this.paymentCancelMsrMgr.getEGiftCardInfoFront (msrSqlMap1, eGiftCardList);
            
            if ( eGiftCardInfoList == null || eGiftCardInfoList.size () == 0 ) { throw new XOException ("�ֹ� ������ ī�尡 ����"); }
            
            egiftCardInfoDto = eGiftCardInfoList.get (0);
            refundDto.setTarget_card_number (egiftCardInfoDto.getCard_number ());
            refundDto.setPin_number (egiftCardInfoDto.getPin_number ());
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : eGiftCard ���� ��ȸ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 2. MSR : eGiftCard ���� ��ȸ END ==========================================================
            
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
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' ������Ʈ
            if ( eGiftCardInfoList != null && eGiftCardInfoList.size() > 0) {
                boolean result = this.setRequestEGiftCardInfoAbort (eGiftCardInfoList);
                if ( this.pcLogger.isDebugEnabled () ) {
                    buf.delete (0, buf.length ()).append (logTitle).append ("setRequestEGiftCardInfoAbort Result => ").append (result);
                    this.pcLogger.debug (buf.toString ());
                }
            }
            throw e;
        }
        
        // step 3. FD Activation START ====================================================================
        startTime = System.currentTimeMillis ();
        try {
            // FD Activation ȣ��
            boolean isOk = this.setFdBalance (refundDto.getTarget_card_number (), String.valueOf (refundDto.getAmount ()), "A", FDCode.ORDER_CODE.REFUND, refundDto.getOrder_no(), refundDto.getUser_id());
            
            // FD ���� - ���� ��� ���� ��� �α� ===================
            logBuf.append (logTitle).append ("FD Activation")
                .append ("|").append ("orderNo=").append (refundDto.getOrder_no ())
                .append ("|").append ("cardNumber=").append (refundDto.getTarget_card_number ())
                .append ("|").append (this.resultCode)
                .append ("|").append (this.resultMsg);
            this.fLogger.info (logBuf); // FD �α�
            this.pcLogger.info (logBuf); // �⺻ �α�
            logBuf.delete (0, logBuf.length ());
            // FD ���� - ���� ��� ���� ��� �α� ===================
            
            if ( !isOk ) {
                buf.delete (0, buf.length ()).append ("FD Activation Fail. ").append (this.resultMsg);
                throw new XOException (buf.toString ());
            }
            
            refundDto.setAmount ((long) this.remainAmount);
        } catch ( Exception e ) {
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' ������Ʈ
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
            
            // step 4. MSR : MSR ȸ�� ���� Ȯ�� START =========================================================
            startTime = System.currentTimeMillis ();
            CardRegMemberDto cardRegMemberDto = this.paymentCancelMsrMgr.getRegMember (msrSqlMap2, refundDto.getUser_id ());
            
            if ( cardRegMemberDto == null ) {
                buf.delete (0, buf.length ()).append ("cardRegMemberDto is null(userId : ").append (refundDto.getUser_id ()).append (")");
                throw new XOException (buf.toString ());
            }
            
            String userNumber = cardRegMemberDto.getUser_number ();
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : MSR ȸ������ Ȯ�� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 4. MSR : MSR ȸ�� ���� Ȯ�� END   =========================================================
            
            // step 5. MSR : ī�� ��� START ==================================================================
            startTime = System.currentTimeMillis ();
            
            // ī�� �г���
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
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : ī�� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 5. MSR : ī�� ��� END ==================================================================
            
            // step 6. MSR : ī�� ���� ������Ʈ START =========================================================
            startTime = System.currentTimeMillis ();
            boolean cardInsertFlag = this.paymentCancelMsrMgr.updateCardInfoForRegister (msrSqlMap2, refundDto.getTarget_card_number (), "R", String.valueOf (currBalance));
            
            if ( !cardInsertFlag ) { // ������Ʈ ���� �� �ѹ�
                buf.delete (0, buf.length ()).append ("Update Card Status Failed(cardNumber : ").append (refundDto.getTarget_card_number ()).append (")");
                throw new XOException ();
            }
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : ī�� ���� ������Ʈ END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 6. MSR : ī�� ���� ������Ʈ END =========================================================
            
            // step 7. MSR : ī�� ��� �̷� ���� START ========================================================
            startTime = System.currentTimeMillis ();
            
            // �̵�� ī�� ��� �̷¿� ���� �̷� ���.
            Map<String, Object> etcMap = new HashMap<String, Object> ();
            etcMap.put ("cardNumber", refundDto.getTarget_card_number ()); // ī���ȣ
            etcMap.put ("historyCode", "T"); // �̷±���(R-����, U-���, C-�������, X-������, M-�ܾ�����, A-����, T-��������, Z-�����������, V-��ǰ����, W-��ǰ�������, I-���, D-����)
            etcMap.put ("amount", currBalance); // ó���ݾ�
            etcMap.put ("balance", currBalance); // �ܾ�
            etcMap.put ("formulaCode", "O"); // ���/���� ��� (O:���̷�����)
            etcMap.put ("paymentType", "���̷�����ȯ��"); // 
            etcMap.put ("orderNo", refundDto.getOrder_no ()); 
            
            this.paymentCancelMsrMgr.insertCardUseHistoryEtc (msrSqlMap2, etcMap);
            
            // ī�� ���� ���� �̷� ���
            CardStatusHistDto histDto = new CardStatusHistDto ();
            histDto.setCard_number (refundDto.getTarget_card_number ());
            histDto.setChange_actor_group_code ("B");
            histDto.setChange_actor_id (refundDto.getUser_id ());
            histDto.setUser_number (userNumber);
            histDto.setChange_status ("R");
            histDto.setCard_reg_number (cardRegNumber);
            histDto.setNo_use_desc ("SmartOrder e-Gift");
            
            this.paymentCancelMsrMgr.insertCardStatusHist (msrSqlMap2, histDto);
            
            // ī�� ��� �̷� ���
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
                buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". MSR : ī�� ���� ���� �̷� ���� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
                this.pcLogger.debug (buf.toString ());
            }
            // step 7. MSR : ī�� ��� �̷� ���� END ========================================================
            
            if ( this.pcLogger.isDebugEnabled () ) {
                buf.delete (0, buf.length ()).append (logTitle).append (" statusMsr2 commit!!");
                this.pcLogger.debug (buf.toString ());
            }// Ʈ������ Ŀ��
            msrSqlMap2.commitTransaction ();
            refundDto.setResult ("00");
        } catch ( Exception e ) {
            //MSR_EGIFT_CARD_INFO.USE_YN = 'X' ������Ʈ
            if ( eGiftCardInfoList != null && eGiftCardInfoList.size() > 0) {
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
                pcLogger.error(ee.getMessage());
            }
        }
        return refundDto;
    }
    
    /**
     * eGift ī�� USE_YN ���°� ����
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
            
            if ( eGiftCardInfoList == null || eGiftCardInfoList.size() == 0 ) {
                buf.delete (0, buf.length ()).append (logTitle).append ("eGiftCardInfoList is Null");
                this.pcLogger.error (buf.toString ());
                return false;
            }
            
            // ī�� ��ȣ ����
            for ( EGiftCardInfoDto dto : eGiftCardInfoList ) {
                if ( dto.getCard_number ().length () != 16 ) { return false; }
                eGiftCardList.add (dto.getCard_number ());
            }
            
            // eGift ī�� ��밡�� ���θ� X(�̻��)���� ����
            updateCnt = this.paymentCancelMsrMgr.updateEGiftCardInfoUseYnX (msrSqlMap, eGiftCardList);
            
            // Ʈ������ Ŀ��
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
                pcLogger.error(ee.getMessage(), ee);
            }
            buf.delete (0, buf.length ()).append (logTitle).append ("step 1. MSR : eGift ī�� ��밡�� ���θ� X(�̻��)���� ���� (update Count : ").append (updateCnt).append (") END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            this.pcLogger.info (buf.toString ());
        }
        return true;
    }
    
    /**
     * FD�� ī�� ���/��� ��û�ϰ�, �ܾ� Ȯ��
     * @param paramMap
     * @return
     */
    protected boolean setFdBalance ( String cardNumber, String amount, String fdType, String orderCode, String orderNo, String userId ) {
        try {
            
            FDClient fdc = new FDClient (FORMULA_CODE.XO_BATCH, orderCode);
            fdc.setLogInsertUser(userId);
            
            Map<String, String> fdProcessResult = new HashMap<String, String> ();
            String functionName = "";
            if ( StringUtils.equals (fdType, "U") ) {
                fdProcessResult = fdc.redemption (cardNumber, amount, orderNo); // FD�� ī�� �ݾ� ��� - SV.0200 (ī���ȣ, ���ݾ�)
                functionName = "redemption";
            } else if ( StringUtils.equals (fdType, "X") ) {
                fdProcessResult = fdc.voidOfCashOut (cardNumber, amount, orderNo); // FD�� ī�� �ݾ� ��� ��� - SV.0800 (ī���ȣ, ���ݾ�)
                functionName = "voidOfCashOut";
            } else if ( StringUtils.equals (fdType, "R") ) {
                fdProcessResult = fdc.reload (cardNumber, amount, orderNo); // FD�� ī�� �ݾ� ���� - SV.0300 (ī���ȣ, ���ݾ�)
                functionName = "reload";
            } else if ( StringUtils.equals (fdType, "C") ) {
                fdProcessResult = fdc.voidOfReload (cardNumber, amount, orderNo); // FD�� ī�� �ݾ� ���� ��� - SV.0801(ī���ȣ, ���ݾ�)
                functionName = "voidOfReload";
            } else if ( StringUtils.equals (fdType, "A") ) {
                fdProcessResult = fdc.activation (cardNumber, amount, orderNo); // FD�� ī�� �ݾ� activation - SV.0100 (ī���ȣ, ���ݾ�)
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
                 || !StringUtils.equals (trCode, codeF6) // �� ���� ���� ������ ����
                 || StringUtils.equals (trCode, TRCODE.TIMEOUT_REVERSAL) // TRCODE�� 0704�̸� ����
            )
            {
                String fdFailMsg = "";
                if ( StringUtils.equals (code39, RESPONSE_CODE.ACCOUNT_CLOSED) || StringUtils.equals (code39, RESPONSE_CODE.UNKNOWN_ACCOUNT) ) {
                    fdFailMsg = "��ȿ�� ī�尡 �ƴմϴ�.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INACTIVE_ACCOUNT) ) {
                    fdFailMsg = "���� ������ ���� ���� ī���Դϴ�. ���忡�� ���� ���� �� ����� �ٶ��ϴ�.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.MAX_BALANCE_EXCEEDED) ) {
                    fdFailMsg = "�ִ� ���� �ݾ��� �ʰ��Ͽ����ϴ�.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INVALID_AMOUNT) ) {
                    fdFailMsg = "��� �ݾ��� �ùٸ��� �ʽ��ϴ�.";
                } else if ( StringUtils.equals (code39, RESPONSE_CODE.INSUFFICIENT_FUNDS) ) {
                    fdFailMsg = "�ܾ��� �����մϴ�.";
                } else {
                    fdFailMsg = "ī�� ��뿡 �����Ͽ����ϴ�.";
                }
                this.resultMsg = buf.delete (0, buf.length ()).append (fdFailMsg).append ("(").append (code39).append (":").append (code76).append (")").toString ();
                return false;
            }
            
            this.remainAmount = Integer.parseInt (code76); // �ܾ�
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
    
    /**
     * ���� ��� ó��(�ſ�ī��, SSG PAY, SSG MONEY)
     * @param orderDto
     * @param paymentList
     * @param logTitle
     * @return
     */
    public boolean processPaymentPgCancel ( Map<String, String> map, String logTitle ) {
        Map<String, Object> dbMap = new HashMap<String, Object> ();
        long startTime;
        int step = 1;
        
        StringBuffer logBuf = new StringBuffer ();
        
        boolean telCancelSuccess = false;	// ��Ż� ��� ���� ����
        boolean cancelSuccess 	 = false;	// ��� ���� ����
        boolean empSuccess       = false;   // ������ ���� ��� ��û ���� ����
        int criticalCount	 	 = 0;		// ũ��Ƽ�� flag{C:ũ��Ƽ�� ���� �߻�}
        boolean mGiftCancelSuccess = false; // ����� ��ǰ�� ���� ��� ��������
        List<PaymentDto> newPaymentList = new ArrayList<PaymentDto> (); 	// ��Ż����� �ű� ���� ó����
        List<PaymentDto> paymentList = new ArrayList<PaymentDto> ();
        String orderStatus = "";	// xo_order ���̺��� ���� status
        String cancelReason = "�ڵ����";
        String orderNo = map.get ("order_no");
        String empNo = "";
        
        String modelType = StringUtils.defaultIfEmpty(map.get ("model_type"), "");
        
        // SSGPAY ���� ������ ���  [G,M]
        // SSGPAY �ſ�ī�� ������ ��� [G]
        // SSGPAY �Ӵ� ������ ��� [M]
        // �̴Ͻý� �ſ�ī�� ������ ��� [C]
        String main_pay_method = map.get ("pay_method");        
        
        SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
        
        MGiftHttpService mGiftHttpService = new MGiftHttpService();	// ����� ��ǰ��
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
            
            if ( paymentList == null || paymentList.size () == 0 ) {
                buf.delete (0, buf.length ()).append ("paymentList is null - ").append (orderNo);
                throw new XOException (buf.toString ());
            }
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
            // ���� �� �� �̻� ���� ���� �ʰ� ���� ī��Ʈ �� ������, DB ����, ���� ī��Ʈ ����, ���� �̷� ���
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
        		// F-1. ���� �� ��ǰ�� GIFT �ѹ�
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
        	
        	
        	// step 5. ������ ���� ��� ��û  ====================================================================
            if (StringUtils.isNotEmpty(orderDto.getEmp_no()) && StringUtils.isNotEmpty(orderDto.getEmp_auth_app_no())) {
                Map<String, String> empResult = cancelEmpOrder(logTitle, orderDto);
                if (empResult.isEmpty()) {
                    empSuccess = false;
                    try {
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
                    
                    if ( this.pcLogger.isDebugEnabled () ) {
            			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ������ ���� ��� : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
            			this.pcLogger.debug (buf.toString ());
            		}
                }
            }
            // step 5. ������ ���� ��� ��û END =================================================================
            
            // step 6. ����� ��ǰ�� ���� ��� START ===========================================================
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
        			buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (".����� ��ǰ�� ���� ��� END: ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
        			this.pcLogger.debug (buf.toString ());
        		}
            }
            // step 6. ����� ��ǰ�� ���� ��� ============================================================
            
            
            // step 7. ��Ż� : ���� ��� START ===============================================================
            startTime = System.currentTimeMillis ();
            telCancelSuccess = this.setTelecomOrderCancel(paymentList, logTitle, logBuf);
            
            if(!telCancelSuccess) {
				// ��Ż� : ���� ��� ���н� ���� ���� ó��
            	telCancelSuccess = true;
            	if ( this.pcLogger.isDebugEnabled () ) {
					buf.delete (0, buf.length ()).append (logTitle).append ("step ").append (  step ).append (". ��Ż� : ������� ���� (��������) ").append (  System.currentTimeMillis () - startTime ).append ("ms");
					this.pcLogger.debug (buf.toString ());
				}
			}
            
            if ( this.pcLogger.isDebugEnabled () ) {
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ��Ż� : ���� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				this.pcLogger.debug (buf.toString ());
			}
            // step 7. ��Ż� : ���� ��� END ==================================================================
            
            
            // step 8. PG(SSG PAY ����) : ���� ��� START ===================================================================
        	if(telCancelSuccess){
        		for(PaymentDto paymentDto : paymentList){
        			if(StringUtils.equals(paymentDto.getPay_method(), "C") || StringUtils.equals(paymentDto.getPay_method(), "G") || StringUtils.equals(paymentDto.getPay_method(), "M")){
        				// PG ���� �Ϸ�� ��� ó��

    					startTime = System.currentTimeMillis ();
    					
    					//SSG PAY(�ſ�ī��)�� ���
    					if(org.apache.commons.lang.StringUtils.equals(paymentDto.getPay_method(), "G")){
    						NiceResultDto niceDto = new NicePgService().niceCancelProcess(paymentDto.getOrder_no(), paymentDto.getTid(), paymentDto.getAmount().intValue()+"", logTitle);
    						
    						this.resultCode = niceDto.getModResultCode();
    						this.resultMsg = niceDto.getResultMsg();
    						if(org.apache.commons.lang.StringUtils.equals(niceDto.getModResultCode(), "00")){
    							cancelSuccess = true;
    						}else{
    							cancelSuccess = false;
    						}
    						
       					//SSG PAY(�Ӵ�)�� ���        						
    					} else if(org.apache.commons.lang.StringUtils.equals(paymentDto.getPay_method(), "M")){
    						NiceResultDto niceDto = new NicePgService().niceCancelProcess(paymentDto.getOrder_no(), paymentDto.getTid(), paymentDto.getAmount().intValue()+"", logTitle);
    						
    						this.resultCode = niceDto.getModResultCode();
    						this.resultMsg = niceDto.getResultMsg();
    						if(org.apache.commons.lang.StringUtils.equals(niceDto.getModResultCode(), "00")){
    							cancelSuccess = true;
    						}else{
    							cancelSuccess = false;
    						}
    					} else {
    						if(StringUtils.equals(paymentDto.getPgcm_code(), DataCode.SMARTRO_PGCM_CODE)) {
    							
    							Map<String, String> result = null;
    							// PG�� ������ ���� ���� �ʴ� ��� ��� ���� ���θ� ���з� ó��
    							if(smartroPgInfoDto != null) {
    								result = smartroPgService.pgCancelProcessSmartro(paymentDto.getTid(), paymentDto.getOrder_no(), cancelReason, logTitle, String.valueOf(paymentDto.getAmount()), smartroPgInfoDto);
    								this.resultCode = result.get("resultCode");
    								this.resultMsg = result.get("resultMsg");
    							} else {
    								this.resultCode = DataCode.RESULT_ETC_ERROR_CODE;
    								this.resultMsg = DataCode.PG_INFO_SEARCH_ERR;
    							}
    							
    							if(result != null) {
    								if(StringUtils.equals(result.get("resultCode"), DataCode.RESULT_SUCCESS_CODE)) {
    									cancelSuccess = true;
    								} else {
    									cancelSuccess = false;
    								}
    							} else {
    								cancelSuccess = false;
    							}
    							
    						} else {
    							cancelSuccess = setPgOrderCancel (paymentDto.getTid(), paymentDto.getOrder_no(), cancelReason, logTitle, modelType);
    						}
    					}
    					
    					// PG ���� ���� ��� ��� �α� ===================
    					logBuf.delete(0, buf.length()).append (logTitle).append ("PG cancel")
        					.append ("|").append ("orderNo=").append (orderNo)
        					.append ("|").append ("paymentOrder=").append (paymentDto.getPayment_order ())
        					.append ("|").append ("TID=").append (paymentDto.getTid ())
        					.append ("|").append (this.resultCode)
        					.append ("|").append (this.resultMsg);
    					this.pLogger.info (logBuf.toString()); 		// PG �α�
    					this.pcLogger.info (logBuf.toString()); 	// �⺻ �α�
    					logBuf.delete (0, logBuf.length ());
    					
    					if ( cancelSuccess ) {
							orderStatus = "12";	// ���� ��� �Ϸ�
    						paymentDto.setCancel_result_code(this.resultCode);	// ��� ��� ����
    						paymentDto.setStatus("C");	// ���� ���� ����
    						this.compCnt++;	// �����Ϸ� ��Ұ� ī��Ʈ
    					} else {
    						// SSGPAY ���հ������ �κ� �����ΰ��
    						if(org.apache.commons.lang.StringUtils.equals(main_pay_method, "G,M")){
    							orderStatus = "14";	// ���� ��� �κ� ����
    							criticalCount++;
    							
            					logBuf.delete(0, buf.length()).append (logTitle).append ("SSGPAY MULTI PG ������� �κн���")
	        					.append ("|").append ("orderNo=").append (orderNo)
	        					.append ("|").append ("paymentOrder=").append (paymentDto.getPayment_order ())
	        					.append ("|").append ("paymentMethod=").append (paymentDto.getPay_method ())
	        					.append ("|").append ("TID=").append (paymentDto.getTid ())
	        					.append ("|").append (this.resultCode)
	        					.append ("|").append (this.resultMsg);
            					
            					this.cLogger.info (logBuf.toString()); // ũ��Ƽ�� �α�
        						this.failReason = this.resultMsg;	// ���� ����
        						logBuf.delete (0, logBuf.length ());
        						
        						//SSGPAY ���հ������ �κн��н� ��Ż����޴� �ٽ� ����ó�� ����
        						break;
    						} else {
    							orderStatus = "13";	// ���� ��� ���� [�̷����̺� ����]
    							
            					logBuf.delete(0, buf.length()).append (logTitle).append ("PG ������� ����")
	        					.append ("|").append ("orderNo=").append (orderNo)
	        					.append ("|").append ("paymentOrder=").append (paymentDto.getPayment_order ())
	        					.append ("|").append ("paymentMethod=").append (paymentDto.getPay_method ())
	        					.append ("|").append ("TID=").append (paymentDto.getTid ())
	        					.append ("|").append (this.resultCode)
	        					.append ("|").append (this.resultMsg);
            					
            					this.cLogger.info (logBuf.toString()); // ũ��Ƽ�� �α�
        						this.failReason = this.resultMsg;	// ���� ����
        						logBuf.delete (0, logBuf.length ());
        						
        						//PG���� ��� ���н� ���ó���� ��Ż����޸� �ٽ� ����ó����
        						criticalCount += this.setTelecomOrderPayment(paymentList, newPaymentList, logTitle, logBuf);
        						//����� ��ǰ�� �ѹ� ó��
        						criticalCount += mGiftHttpService.setMGiftOrderCancelRollback(xoSqlMap, paymentList, logTitle, logBuf);
        		                
    						}
    					}
    					// PG ���� ���� ��� ��� �α� ===================
    					
    					if ( this.pcLogger.isDebugEnabled () ) {
    						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". PG : ���� ��� END : ").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
    						this.pcLogger.debug (buf.toString ());
    					}
        			}
        		}
        	}
        	// step 8. PG(SSG PAY ����) : ���� ��� END =====================================================================

        	
        	// step 9. MSR : ���� ��� ��� START =============================================================
        	if(telCancelSuccess && cancelSuccess){
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
        	// step 9. ���� ��� ��� END =====================================================================
        	
        	
            boolean dbSuccess = false;	// DB ó�� ���
            dbMap.put ("orderNo", orderNo);
            dbMap.put ("status",  orderStatus); // ����
            
            // step 10. �ֹ� ���� ������Ʈ START   ==============================================================
            if(telCancelSuccess && cancelSuccess) {	// ��� ������ �����͸� ������Ʈ
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
            		// �����ڵ� 13�ΰ�쿡�� ������������ ���º��� ����(���������� ����)
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
            // step 10. �ֹ� ���� ������Ʈ END   ================================================================

            // step 11. �ֹ� ���� ���� �̷� ���� START   =========================================================
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
            // step 11. �ֹ� ���� ���� �̷� ���� END   ===========================================================
            
            
            // step 12. ���� ���� ������Ʈ START   ==============================================================
        	startTime = System.currentTimeMillis ();
        	for(PaymentDto paymentDto : paymentList){
        		//�ſ�ī��� ��Ż����� ��� ������Ʈ
        		if(StringUtils.equals(paymentDto.getPay_method(), "C") || StringUtils.equals(paymentDto.getPay_method(), "G") || StringUtils.equals(paymentDto.getPay_method(), "M") || StringUtils.equals(paymentDto.getPay_method(), "K") 
        				|| StringUtils.equals(paymentDto.getPay_method(), "U") || StringUtils.equals(paymentDto.getPay_method(), "T")
        				|| StringUtils.isNotEmpty(paymentDto.getPrcm_frst_code())){
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
            // step 12. ���� ���� ������Ʈ END   =================================================================
            
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
          //����� ��ǰ�� �ѹ� ó��
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
    
    /**
     * PG�� �ֹ� ��� ��û �� ��� ���� �����ϴ� �޼ҵ�
     * @param tid
     * @param orderNo
     * @param cancelReason
     * @param logTitle
     * @return
     */
    public boolean setPgOrderCancel ( String tid, String orderNo, String cancelReason, String logTitle, String modelType) {
        try {
            INIpay50 inipay = new INIpay50 ();
            
            inipay.SetField ("inipayhome", iniHomeDir); // �̴����� Ȩ���͸�(�������� �ʿ�)
            inipay.SetField ("type", "cancel");
            inipay.SetField ("debug", "true");
            
            // modelType �𵨱��� {81-Bixb} ���� 81�� ���  �Ｚ���̿� PG ��� MID, PASSWORD ����
            if(StringUtils.isNotEmpty(modelType) && StringUtils.indexOf(this.openApiModel, modelType) >= 0) {
            	inipay.SetField ("admin", samKeyPwd);
            	inipay.SetField ("mid",   samIniMid);
            }
            else
            {
            	inipay.SetField ("admin", keyPwd);
	            inipay.SetField ("mid",   iniMid);
            }
            
            inipay.SetField ("tid", tid);
            inipay.SetField ("cancelmsg", cancelReason); // ��һ���
            
            inipay.startAction ();
            
            this.resultCode = StringUtils.defaultString (inipay.GetResult ("ResultCode"));
            this.resultMsg = StringUtils.defaultString (inipay.GetResult ("ResultMsg"));
            
            if ( !StringUtils.equals (this.resultCode, "00") ) { return false; }
        } catch ( Exception e ) {
            this.resultCode = "07";
            this.resultMsg = e.getMessage ();
            buf.delete (0, buf.length ()).append (logTitle).append (e.getMessage ());
            this.pcLogger.error (buf.toString (), e);
            this.eLogger.error (buf.toString (), e);
            return false;
        }
        return true;
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
    
    public static void main ( String[] args ) {
        PaymentEventCancel paymentCancel = new PaymentEventCancel ();
        paymentCancel.start ();        
    }
    
}
