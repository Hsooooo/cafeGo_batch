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
 * PG ���� ��� ��� ��ġ PgTradeCancel
 * �����������ڸ��ִ� �ǿ� ���ؼ��� ����Ѵ�.
 * ����������ڰ� �ִ� �ǿ��� ���ؼ��� ��ȸ���� �ʴ´�.
 * @author FIC04749
 *
 */
public class PgTradeCancel {
   
    private final Logger ptLogger = Logger.getLogger("PgTradeCancel");

    private Configuration conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
    
    private String resultCode = "";
    private String resultMsg = "";
    private String logTitle = "";

    // �̴Ͻý� HOME DIR
    private final String iniHomeDir           = conf.getString("inipay.homeDirectory");             // ���̷�����, ���� Api PG HOME DIR
    private final String chargeIniHomeDir     = conf.getString("charge.inipay.homeDirectory");      // �Ϲ����� PG HOME DIR
    private final String autoChargeIniHomeDir = conf.getString("auto.charge.inipay.homeDirectory"); // �ڵ����� PG HOME DIR
    private final String giftCardIniHomeDir   = conf.getString("eGift.card.inipay.homeDirectory");  // e-Gift Card PG HOME DIR
    private final String giftItemIniHomeDir   = conf.getString("eGift.item.inipay.homeDirectory");  // e-Gift Item PG HOME DIR
    
    // �̴Ͻý� ���� Ű
    private final String chargeIniMid     = conf.getString("charge.inipay.mid");              // �Ϲ����� PG ����Ű
    private final String chargeKeyPwd     = conf.getString("charge.inipay.keyPassWord");      // �Ϲ����� PG ���� ��й�ȣ
    private final String autoChargeIniMid = conf.getString("auto.charge.inipay.mid");         // �ڵ����� PG ����Ű
    private final String autoChargeKeyPwd = conf.getString("auto.charge.inipay.keyPassWord"); // �ڵ����� PG ���� ��й�ȣ
    private final String giftCardIniMid   = conf.getString("eGift.card.inipay.mid");          // e-Gift Card PG ����Ű
    private final String giftCardKeyPwd   = conf.getString("eGift.card.inipay.keyPassWord");  // e-Gift Card PG ���� ��й�ȣ
    private final String sirenOrderIniMid = conf.getString("inipay.mid");                     // ���̷����� PG ����Ű
    private final String sirenOrderKeyPwd = conf.getString("inipay.keyPassWord");             // ���̷����� PG ���� ��й�ȣ
    private final String openApiIniMid    = conf.getString("sam.inipay.mid");                 // ���� Api PG ����Ű
    private final String openApiKeyPwd    = conf.getString("sam.inipay.keyPassWord");         // ���� Api PG ���� ��й�ȣ
    private final String giftItemMid      = conf.getString("eGift.item.inipay.mid");          // e-Gift Item PG ����Ű
    private final String giftItemKeyPwd   = conf.getString("eGift.item.inipay.keyPassWord");  // e-Gift Item PG ���� ��й�ȣ
    
    // ���̽� ���� Ű
    private final String niceChargeIniMid     = conf.getString("charge.nice.mid");             // ���̽� �Ϲ����� PG ����Ű
    private final String niceChargeKeyPwd     = conf.getString("charge.nice.keyPassWord");     // ���̽� �Ϲ����� PG ���� ��й�ȣ
    private final String niceGiftCardIniMid   = conf.getString("eGift.card.nice.mid");         // ���̽� e-Gift Card PG ����Ű
    private final String niceGiftCardKeyPwd   = conf.getString("eGift.card.nice.keyPassWord"); // ���̽� e-Gift Card PG ���� ��й�ȣ
    private final String niceSirenOrderIniMid = conf.getString("xo.ssg.nice.mid");             // ���̽� ���̷����� PG ����Ű
    private final String niceSirenOrderKeyPwd = conf.getString("xo.ssg.nice.cancelPassWord");  // ���̽� ���̷����� PG ���� ��й�ȣ
    private final String niceGiftItemMid      = conf.getString("eGift.item.nice.mid");         // ���̽� e-Gift Item PG ����Ű
    private final String niceGiftItemKeyPwd   = conf.getString("eGift.item.nice.keyPassWord"); // ���̽� e-Gift Item PG ���� ��й�ȣ
    
    private String failReason = "";	// ���� ����
    
    private final String loggerTitle;
    private final PaymentCancelXoMgr paymentCancelXoMgr;
    private List<Map<String, Object>> pgTradeCancelList;
    private List<Map<String, Object>> pgInisisTradeCancelList = new ArrayList<Map<String,Object>>();
    private List<Map<String, Object>> pgNiceTradeCancelList = new ArrayList<Map<String,Object>>();
    private List<Map<String, Object>> pgSmartroTradeCancelList = new ArrayList<Map<String,Object>>();
    private final SmartroPgService smartroPgService;
    
    // ����Ʈ�� PG ����
    private SmartroPgInfoDto pgChargeInfoDto;     // ����Ʈ�� �Ϲ����� PG����
    private SmartroPgInfoDto pgAutoChargeInfoDto; // ����Ʈ�� �ڵ����� PG����
    private SmartroPgInfoDto pgEgiftCardInfoDto;  // ����Ʈ�� e-Gift Card PG����
    private SmartroPgInfoDto pgSirenorderInfoDto; // ����Ʈ�� ���̷����� PG����
    private SmartroPgInfoDto pgWholecakeInfoDto;  // ����Ʈ�� Ȧ����ũ PG����
    private SmartroPgInfoDto pgEgiftItemInfoDto;  // ����Ʈ�� e-Gift Item PG����
    
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
     * PG ���� ��� ��ġ ��� ��ġ
     * @param cancelDate
     */
    public void start (String stlmnCnsntDate) {
    	long startTimeTotal = System.currentTimeMillis ();
        
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "START");

        // 1. PG ���� ��� ��ġ ��� ��� ��ȸ
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. PG ���� ��� ��ġ ��� ��� ��ȸ");
        this.getPgTradeCancelList(stlmnCnsntDate);
        
        // 2. ���� ��� ��� �з�(�̴Ͻý�, ���̽�, ����Ʈ��)
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. ���� ��� ��� �з�(�̴Ͻý�, ���̽�, ����Ʈ��)");
        this.divisionPgTradeCancelList();
        
        // 3. �̴Ͻý� ��� ���
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "3. �̴Ͻý� ��� ���");        
        this.pgInisisTradeCancel();
        
        // 4. ���̽� ��� ���
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "4. ���̽� ��� ���");
        this.pgNiceTradeCancel();
        
        // 5. ����Ʈ�� ��� ���
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "5. ����Ʈ�� ��� ���");
        this.pgSmartroTradeCancel();
        
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "END :" + ( System.currentTimeMillis () - startTimeTotal ) + "ms");

    }

	/**
     * ���̽� ��� ���
     */
    private void pgNiceTradeCancel() { 
    	
    	String order;
    	String mid;
    	String tid;    	
    	String pCancelAmt;
    	String cancelPwd;
    	String pMid;
    	
    	for(Map<String, Object> pgNiceTradeCancel : pgNiceTradeCancelList) {

    		order      = String.valueOf(pgNiceTradeCancel.get("pgcmOrderId")); // �ֹ���ȣ 
    		mid        = String.valueOf(pgNiceTradeCancel.get("pgcmShopId"));  // MID
    		tid        = String.valueOf(pgNiceTradeCancel.get("pgDlngNo"));    // TID
    		pCancelAmt = String.valueOf(pgNiceTradeCancel.get("pgStlmnAmnt")); // ��ұݾ�
    		cancelPwd  = ""; // �ʱ�ȭ
    		pMid       = ""; // �ʱ�ȭ
    		
    		// �Ϲ� ����
    		if(StringUtils.equals(this.niceChargeIniMid, mid)) {
    			pMid      = this.niceChargeIniMid;
    			cancelPwd = this.niceChargeKeyPwd;
    	
    		// e-Gift Card
    		} else if(StringUtils.equals(this.niceGiftCardIniMid, mid)) {
    			pMid      = this.niceGiftCardIniMid;
    			cancelPwd = this.niceGiftCardKeyPwd;
    			
    		// ���̷����� 
    		} else if(StringUtils.equals(this.niceSirenOrderIniMid, mid)) {
    			pMid      = this.niceSirenOrderIniMid;
    			cancelPwd = this.niceSirenOrderKeyPwd;
    			
    		// e-Gift Item
    		} else if(StringUtils.equals(this.niceGiftItemMid, mid)) {
    			pMid      = this.niceGiftItemMid;
    			cancelPwd = this.niceGiftItemKeyPwd; 
    		}
    		
    		// NICE ��ҿ�û(PG ���δ�� ��ġ)
			NiceResultDto niceDto = new NicePgService().niceTradeCancelProcess(order, pMid, cancelPwd, tid, pCancelAmt);
			
			this.resultCode = niceDto.getModResultCode();
			this.resultMsg = niceDto.getResultMsg();
			
			if(StringUtils.equals(niceDto.getModResultCode(), DataCode.NICE_CANCEL_SUCCESS)) {
				// ���ó����� ������Ʈ
				try {
					pgNiceTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : ����, 02 :  ������, 03 : ����
					pgNiceTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);

					this.paymentCancelXoMgr.updatePgTradeCancel(pgNiceTradeCancel);
					this.ptLogger.info("���̽� ��� TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("���̽� ��� ��� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
				} catch (SQLException e) {
					pgNiceTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : ����, 02 :  ������, 03 : ����
					this.ptLogger.error("���̽� ��� TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
				}
			} else {
				pgNiceTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : ����, 02 :  ������, 03 : ����
				pgNiceTradeCancel.put("cncltPrcsgRsn", this.resultMsg);
				
				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgNiceTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("���̽� ��� TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("���̽� ��� TID : " +  pgNiceTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("���̽� ��� ��� ���� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
			}			
    	}
	}

	/**
     * �̴Ͻý� ��� ���
     */
    private void pgInisisTradeCancel() {		

    	boolean cancelSuccess = false;

    	for(Map<String, Object> pgInisisTradeCancel : pgInisisTradeCancelList) {
    		
    		// �̴Ͻý� ��� ó��
    		cancelSuccess = this.pgInisisCancel(pgInisisTradeCancel);

    		if(cancelSuccess) {
    			// ���ó����� ������Ʈ
    			try {
    				pgInisisTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : ����, 02 :  ������, 03 : ����					
    				pgInisisTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);
    				this.paymentCancelXoMgr.updatePgTradeCancel(pgInisisTradeCancel);
					this.ptLogger.info("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("�̴Ͻý� ��� ��� �޽��� : �������");

    			} catch (SQLException e) {
    				pgInisisTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : ����, 02 :  ������, 03 : ����
					this.ptLogger.error("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
    			}
    		} else {
    			pgInisisTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : ����, 02 :  ������, 03 : ����
    			pgInisisTradeCancel.put("cncltPrcsgRsn", this.resultMsg);

				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgInisisTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("�̴Ͻý� ��� ��� ���� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
    		}
    	}
	}

    /**
     * �̴Ͻý� ��� ó��
     * @param pgInisisTradeCancel
     * @return
     */
	private boolean pgInisisCancel(Map<String, Object> pgInisisTradeCancel) {
		
        INIpay50 inipay = new INIpay50 ();
        
        inipay.SetField ("type", "cancel");
        inipay.SetField ("debug", "true");
        
        String mid = String.valueOf(pgInisisTradeCancel.get("pgcmShopId"));

        // �Ϲ� ����
		if(StringUtils.equals(this.chargeIniMid, mid)) {
			inipay.SetField ("inipayhome", this.chargeIniHomeDir);
        	inipay.SetField ("admin",      this.chargeKeyPwd);
        	inipay.SetField ("mid",        this.chargeIniMid);
        	
        // �ڵ�����
		} else if(StringUtils.equals(this.autoChargeIniMid, mid)) {
			inipay.SetField ("inipayhome", this.autoChargeIniHomeDir);
        	inipay.SetField ("admin",      this.autoChargeKeyPwd);
        	inipay.SetField ("mid",        this.autoChargeIniMid);
        	
        // e-Gift Card
		} else if(StringUtils.equals(this.giftCardIniMid, mid)) {
			inipay.SetField ("inipayhome", this.giftCardIniHomeDir);
        	inipay.SetField ("admin",      this.giftCardKeyPwd);
        	inipay.SetField ("mid",        this.giftCardIniMid);				
		
        // ���̷�����
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
        inipay.SetField ("cancelmsg", "PG ���� ��� ��ġ ���� ���"); // ��һ���
        
        inipay.startAction ();
        
        this.resultCode = StringUtils.defaultString (inipay.GetResult ("ResultCode"));
        this.resultMsg = StringUtils.defaultString (inipay.GetResult ("ResultMsg"));
        
        if(StringUtils.equals(this.resultCode, DataCode.RESULT_SUCCESS_CODE)) {
			this.ptLogger.info("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
			this.ptLogger.info("�̴Ͻý� ��� ��� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
			return true;
        } else {
        	this.ptLogger.info("�̴Ͻý� ��� TID : " +  pgInisisTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
        	this.ptLogger.info("�̴Ͻý� ��� ���� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
            return false;
        }
    }

	/**
     * ���� ��� ��� �з�(�̴Ͻý�, ���̽�, ����Ʈ��)
     */
    private void divisionPgTradeCancelList() {
    	String pgcmCode; 
    	int    etcPGCnt = 0; // �̺з� PG ī��Ʈ
    	
        for(Map<String, Object> pgTradeCancel : pgTradeCancelList) {
        	pgcmCode = String.valueOf(pgTradeCancel.get("pgcmCode"));
        	
        	// �̴Ͻý� 
        	if(StringUtils.equals(DataCode.INISYS_PGCM_CODE, pgcmCode)) {
        		pgInisisTradeCancelList.add(pgTradeCancel);
        		
        	// ���̽�
        	} else if(StringUtils.equals(DataCode.NICE_PGCM_CODE, pgcmCode)) {
        		pgNiceTradeCancelList.add(pgTradeCancel);

        	// ����Ʈ��
        	} else if(StringUtils.equals(DataCode.SMARTRO_PGCM_CODE, pgcmCode)) {        		
        		String mid = String.valueOf(pgTradeCancel.get("pgcmShopId"));
        		
        		// �Ϲ�����
        		if(StringUtils.equals(this.pgChargeInfoDto.getPgcmShopId(), mid)) { 
        			pgTradeCancel.put("pgInfoDto", this.pgChargeInfoDto);
        		// �ڵ�����
        		} else if(StringUtils.equals(this.pgAutoChargeInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgAutoChargeInfoDto);
        		// E-Gift Card
        		} else if(StringUtils.equals(this.pgEgiftCardInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgEgiftCardInfoDto);
        		// ���̷�����
        		} else if(StringUtils.equals(this.pgSirenorderInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgSirenorderInfoDto);
        		// Ȧ����ũ
        		} else if(StringUtils.equals(this.pgWholecakeInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgWholecakeInfoDto);        			
        		// e-Gift Item
        		} else if(StringUtils.equals(this.pgEgiftItemInfoDto.getPgcmShopId(), mid)) {
        			pgTradeCancel.put("pgInfoDto", this.pgEgiftItemInfoDto);
        		}
        		
        		pgSmartroTradeCancelList.add(pgTradeCancel);

       		// PG�� �ڵ尡 �з� ���� �ʴ� �׸� ��ȸ��  �α� ó��
        	} else {
        		
        		etcPGCnt++;
        		this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2. �̺з� PG�� �ڵ� ���� ���� (" 
        				+ "PG�� �ڵ� : " + pgTradeCancel.get("pgcmCode").toString().replaceAll("\n|\r", "") 
        				+ ", PG �ŷ���ȣ : " + pgTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", "")
        				+ ", ���� ���� : " + pgTradeCancel.get("stlmnTypeName").toString().replaceAll("\n|\r", "")
        				+ ", ���� ���� �� : " + pgTradeCancel.get("stlmnMeanName").toString().replaceAll("\n|\r", "")
        				+ ", ���� �ݾ� : " + pgTradeCancel.get("pgStlmnAmnt").toString().replaceAll("\n|\r", "")
        		+ ")");
        		
        	}
        }
        
    	this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.1 ���� ��� ���(�̴Ͻý�) : " + this.pgInisisTradeCancelList.size());
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.2 ���� ��� ���(���̽�) : " + this.pgNiceTradeCancelList.size());
        this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.3 ���� ��� ���(����Ʈ��) : " + this.pgSmartroTradeCancelList.size());

        // PG�� �ڵ尡 �з� ���� �ʴ� �׸� ����� �α� ó��
        if(etcPGCnt != 0) {
        	this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "2.4 ���� ��� ���(�̺з� PG��) : " + etcPGCnt);
        }

	}

	/**
     * PG ���� ��� ��ġ ��� ��� ��ȸ
     * @param cancelDate
     */
    private void getPgTradeCancelList(String stlmnCnsntDate) {
    	
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("stlmnCnsntDate", stlmnCnsntDate);
            
            // PG ���� ��� ��ġ ��� ��� ��ȸ
            this.pgTradeCancelList = this.paymentCancelXoMgr.getPgTradeCancelList(dbMap);
            this.ptLogger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1.1 PG ���� ���  �� ��� ��� : " + this.pgTradeCancelList.size () + "��");

        } catch (SQLException e ) {
            this.ptLogger.error (this.loggerTitle.replaceAll("\n|\r", ""), e);
        }

	}

    /**
     * ����Ʈ�� ��� ���
     */
    private void pgSmartroTradeCancel() {

    	boolean cancelSuccess = false;

    	for(Map<String, Object> pgSmartroTradeCancel : pgSmartroTradeCancelList) {

    		// ����Ʈ�� ��� ó��
    		cancelSuccess = this.pgSmartroCancel(pgSmartroTradeCancel);

    		if(cancelSuccess) {
    			// ���ó����� ������Ʈ
    			try {
    				pgSmartroTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_SUCCESS); // 01 : ����, 02 :  ������, 03 : ����					
    				pgSmartroTradeCancel.put("cncltPrcsgRsn", DataCode.PG_TRAD_CANCEL_REASONS);
    				this.paymentCancelXoMgr.updatePgTradeCancel(pgSmartroTradeCancel);
					this.ptLogger.info("����Ʈ�� ��� TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.info("����Ʈ�� ��� ��� �޽��� : �������");

    			} catch (SQLException e) {
    				pgSmartroTradeCancel.put("prcsgStCode", DataCode.PRCSG_ST_FAIL); // 01 : ����, 02 :  ������, 03 : ����
					this.ptLogger.error("����Ʈ�� ��� TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
    			}
    		} else {
    			pgSmartroTradeCancel.put("prcsgStCode",   DataCode.PRCSG_ST_ETC); // 01 : ����, 02 :  ������, 03 : ����
    			pgSmartroTradeCancel.put("cncltPrcsgRsn", this.resultMsg);

				try {
					this.paymentCancelXoMgr.updatePgTradeCancel(pgSmartroTradeCancel);
				} catch (SQLException e) {
					this.ptLogger.error("����Ʈ�� ��� TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
					this.ptLogger.error("DB ���� : " +  e.getMessage().replaceAll("\n|\r", ""));
				}

				this.ptLogger.info("����Ʈ�� ��� TID : " +  pgSmartroTradeCancel.get("pgDlngNo").toString().replaceAll("\n|\r", ""));
				this.ptLogger.info("����Ʈ�� ��� ��� ���� �޽��� : " +  this.resultMsg.replaceAll("\n|\r", ""));
    		}
    	}
	}
    
    /**
     * ����Ʈ�� ��� ó��
     * @param pgSmartroTradeCancel
     * @return
     */
	private boolean pgSmartroCancel(Map<String, Object> pgSmartroTradeCancel) {

		boolean cancelSuccess = false;
		
    	String order        = String.valueOf(pgSmartroTradeCancel.get("pgcmOrderId")); // �ֹ���ȣ
    	String tid          = String.valueOf(pgSmartroTradeCancel.get("pgDlngNo"));    // TID 	
    	String pCancelAmt   = String.valueOf(pgSmartroTradeCancel.get("pgStlmnAmnt")); // ��ұݾ�
		String cancelReason = DataCode.PG_TRAD_CANCEL_REASONS;

		SmartroPgInfoDto pgInfoDto = (SmartroPgInfoDto) pgSmartroTradeCancel.get("pgInfoDto");

		Map<String, String> result = smartroPgService.pgCancelProcessSmartro(tid, order, cancelReason, this.loggerTitle, pCancelAmt, pgInfoDto);

		// ����Ʈ�� PG ���� ����� ���� �ƴ� ��� ó�� 
		if(MapUtils.isNotEmpty(result)) {
			this.resultCode = result.get("resultCode");
			this.resultMsg  = result.get("resultMsg");
	
			if(StringUtils.equals(result.get("resultCode"), DataCode.RESULT_SUCCESS_CODE)) {
				cancelSuccess = true;
			} else {
				cancelSuccess = false;
			}

		// ����Ʈ�� PG���� ���� ���� ���� ���� ��� �޽��� ó��
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
