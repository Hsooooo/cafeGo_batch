/*
 * @(#) $Id: WholecakeOrderCoupon.java,v 1.15 2017/04/24 02:25:05 leeminjung Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.main;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponGiftHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponMasterDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponPublicationListDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.SendMailQueueDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.EmMmtFileDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.common.image.CreateImage;
import co.kr.istarbucks.xo.batch.common.image.MMSImageVO;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DateTime;
import co.kr.istarbucks.xo.batch.common.util.DateUtil;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.OnmUtil;
import co.kr.istarbucks.xo.batch.common.util.TripleDesApp;
import co.kr.istarbucks.xo.batch.common.util.XOUtil;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.CouponMgr;
import co.kr.istarbucks.xo.batch.mgr.MMSMgr;
import co.kr.istarbucks.xo.batch.mgr.MailMgr;
import co.kr.istarbucks.xo.batch.mgr.XoWholecakeOrderMgr;
import co.kr.istarbucks.xo.batch.mon.DataHelper;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Ȧ���� ���࿡ ���� ���� ���� - WholecakeOrderCoupon. 
 * Ȧ���� ���� ��� �Ұ� �������� Ȧ���� ���� ���� ���� �����ϴ� ���μ���
 * 
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.15 $
 */
public class WholecakeOrderCoupon {

	private final Log    logger      = LogFactory.getLog("WHOLECAKE.COUPON.INFO");
	private final OnmUtil      oUtil       = new OnmUtil();
	private final String loggerTitle;
	private StringBuffer logSb;
	private final XoWholecakeOrderMgr xoWholecakeOrderMgr;  // Ȧ���� ����
	private final CouponMgr           couponMgr;            // MSR ���� ����
	private final MailMgr             mailMgr;	          // MSR ���� �߼� ��û
	private final MMSMgr              mmsMgr;               // MMS �߼� ��û
	private final CreateImage         cImage;               // �̹��� ����

	public WholecakeOrderCoupon() {
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mailMgr             = new MailMgr();
		this.couponMgr           = new CouponMgr();
		this.cImage              = new CreateImage(logger);
		this.mmsMgr              = new MMSMgr();
		this.logSb               = new StringBuffer(); // log�� StringBuffer
		this.loggerTitle         = " [wholecake][coupon] ";
	}

	/**
	 * Ȧ���� ���࿡ ���� ���� ���� ���μ���.
	 * @param salesOrderDate ����Ȯ����(���� ��� �Ұ����� ����)
	 */
	public void start(String salesOrderDate) {
		
		/********************************************
     	 BATCH MONITORING START
     	 ********************************************/
		
	     String bm_today = DateUtil.getToday("yyyyMMdd");
	     boolean bm_is_insert_fail = false;
	     int bm_start_cnt=0;

     	DataHelper helper = new DataHelper();
     	try {
     		bm_start_cnt = helper.startMonitor(bm_today, 1, "WHOLECAKE_CP_SERVICE", "WHOLECAKE_CP_DETAIL");
     	} catch (XOException e) {
     		bm_is_insert_fail = true;
     		logger.error(e.getMessage(), e);
     	}
     	
		long startTime = System.currentTimeMillis();

		if (logger.isInfoEnabled()) {
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "START"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "����Ȯ���� \t\t\t\t\t: ", StringUtils.defaultIfEmpty(salesOrderDate, DateUtil.getToday()), "\n" ));
		}

		// ���� ���� ��� Ȧ���� ���� ��ȸ ====================================================
		List<WholecakeOrderDto> wholecakeOrderList = getWholeCakeOrderList(salesOrderDate);
		if(wholecakeOrderList == null){
			wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		}

		// ���� ������ ���� ��ȸ =========================================================
		CouponMasterDto couponMasterDto = new CouponMasterDto();
		if (wholecakeOrderList.size() > 0) {
			couponMasterDto = this.getCouponMaster();
		}

		List<String> orderNoList     = new ArrayList<String>(); // ������ ����� Ȧ���� ����
		List<String> failOrderNoList = new ArrayList<String>(); // ������ ������ ������ Ȧ���� ����
		List<CouponPublicationListDto> couponList = new ArrayList<CouponPublicationListDto>(); // ����� ���� ��ȣ

		
		int     exceptCnt = 0;		// ���� ���� ���� ��
		boolean isExcept  = false;	// ���� ���� ���� ó�� ����
		if (wholecakeOrderList.size() > 0 && couponMasterDto != null && StringUtils.isNotEmpty(couponMasterDto.getCoupon_name())) {
			
			// �׽�Ʈ �Ⱓ ����ó�� (Ư�� ��å �ڵ��� Ȧ���� ���� �߼� ����) �Ⱓ Ȯ��
			String  today           = DateUtil.getToday();												// ���� ��¥ YYYYMMDD
			String  exceptPolicyCd  = XOUtil.getPropertiesString("coupon.wholecake.except.policy");		// ���� ���� ���ܵǴ� ��å �ڵ�
			String  exceptStartDate = XOUtil.getPropertiesString("coupon.wholecake.except.startdate");	// ���� ���� ���ܵǴ� ������
			String  exceptEndDate   = XOUtil.getPropertiesString("coupon.wholecake.except.enddate");	// ���� ���� ���ܵǴ� ������
			String  testIds         = XOUtil.getPropertiesString("coupon.wholecake.test.id");	// ���� ���� ���ܱⰣ���� ���Ǵ� ���̵�
			try {
				if((Integer.parseInt(exceptStartDate) <= Integer.parseInt(today)) && (Integer.parseInt(today) <= Integer.parseInt(exceptEndDate))){
					isExcept = true;
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� ���� - ���� ��å �ڵ� : ", exceptPolicyCd, ", ����ó���Ⱓ: ", exceptStartDate," ~ ", exceptEndDate, ", ���ID: ", testIds, "\n"));
					}
				}
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� ó�� ���� "), e);
				}
			}
				
			// ���� ���� ó�� ==============================================================
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� ==================\n"));
			}
			int cnt = 1;
			for (WholecakeOrderDto dto : wholecakeOrderList) {
				// ���� ���� ���� ���� ���(Ȧ���� ���� ����� ��� -ó���� �����û�Ǽ��� 0�� �� ����)
				if(dto.getTotal_coupon_count() < 1){
					continue;
				}
				
				if(logger.isInfoEnabled()){
					if (StringUtils.equals(dto.getMsr_user_flag(), "Y") && StringUtils.isNotEmpty(dto.getUser_id())) {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, cnt++, ". [", dto.getOrder_no(), "][e-Coupon][", dto.getUser_id(), "]"));
					} else {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, cnt++, ". [", dto.getOrder_no(), "][MMS Coupon][", dto.getUser_id(), "]"));
					}
				}
				// �׽�Ʈ �Ⱓ ����ó�� (Ư�� ��å �ڵ��� Ȧ���� ���� �߼� ����)
				if(isExcept && StringUtils.equals(exceptPolicyCd, dto.getPolicy_cd())){
					if(!StringUtils.contains(testIds, oUtil.concatString("|", dto.getUser_id(), "|"))){
						if(logger.isInfoEnabled()){
							logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� ó�� - �ش� �ֹ� ��å : ", dto.getPolicy_cd(), ", ���� �� : ", dto.getOrder_name(), "\n" ));
						}
						exceptCnt += dto.getTotal_coupon_count();
						continue;
					}
				}
				
				List<CouponPublicationListDto> thisCouponList = new ArrayList<CouponPublicationListDto>();
				if (StringUtils.equals(dto.getMsr_user_flag(), "Y") && StringUtils.isNotEmpty(dto.getUser_id())) {
					// MSR ȸ���� E-�������� ����
					thisCouponList = this.publishECoupon(dto, couponMasterDto);
				} else {
					// MSR ��ȸ���� MMS �������� ����
					thisCouponList = this.publishMMSCoupon(dto, couponMasterDto);
				}
				if (thisCouponList == null || thisCouponList.size() == 0) {
					failOrderNoList.add(dto.getOrder_no());
					dto.setPublication_coupon_count(0);
					continue;
				}
				dto.setPublication_coupon_count(thisCouponList.size());

				orderNoList.add(dto.getOrder_no());						// ���� ���� ���� ������Ʈ �ϱ� ���� ���� ��ȣ
				couponList.addAll(couponList.size(), thisCouponList);	// ���� ������ ���� ���
				
				if(logger.isInfoEnabled()){
					if (dto.getFail_coupon_list() != null && dto.getFail_coupon_list().size() != 0 ) {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "Ȧ����(", dto.getPolicy_cd(), ") : ", dto.getTotal_qty(), "��, ���������� : ", dto.getTotal_coupon_count(),"��, ���� : ", dto.getPublication_coupon_count(),"��, MMS ���� : ", dto.getFail_coupon_list().size(), "��(" , dto.getFail_coupon_list(),")\n"));
					} else {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "Ȧ����(", dto.getPolicy_cd(), ") : ", dto.getTotal_qty(), "��, ���������� : ", dto.getTotal_coupon_count(),"��, ���� : ", dto.getPublication_coupon_count(),"��\n"));
					}
				}
			} // end For
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� ==================\n"));
			}

			// Ȧ���� ���� ���� ���� �� ó�� (�������࿩�� ������Ʈ, �������� ���) ==========================
			if(orderNoList.size() > 0){
				publichCouponAfterProcess(orderNoList, couponList);
			}
		}

		int orderCnt            = 0;	// ���� ���� �Ǽ�
    	int wholeCakeCnt        = 0;	// ���� Ȧ���� �Ǽ�
    	int usedCoupon          = 0;    // ���� �� ����� Ȧ���� ����
    	int couponOrderCnt      = 0;	// ���� ���� ��� ���� �Ǽ�
    	int couponWholecakeCnt  = 0;	// ���� ���� ��� Ȧ���� �Ǽ�
    	int totalCouponCnt      = 0;    // �� ���� ���� ��� �Ǽ�
    	int successECouponCnt   = 0;	// �����  e-Coupon
    	int successMMSCouponCnt = 0;	// ����� MMS Coupon
    	int failCouponCnt       = 0;	// ���� �Ǽ�
    	for(WholecakeOrderDto dto : wholecakeOrderList){
    		orderCnt            += 1;	                                    // ���� ���� �Ǽ�
        	wholeCakeCnt        += dto.getTotal_qty();	                    // ���� Ȧ���� �Ǽ�
        	if(dto.getTotal_coupon_count() > 0){
            	couponOrderCnt      += 1;	                                // ���� ���� ��� ���� �Ǽ�
            	couponWholecakeCnt  += (dto.getTotal_coupon_count()/dto.getCoupon_count());			// ���� ���� ��� Ȧ���� �Ǽ�
        	}
        	usedCoupon          += dto.getTotal_qty() - (dto.getTotal_coupon_count()/dto.getCoupon_count());
        	totalCouponCnt      += dto.getTotal_coupon_count();			// ���� ���� ��� Ȧ���� �Ǽ�
        	if (StringUtils.equals(dto.getMsr_user_flag(), "Y") && StringUtils.isNotEmpty(dto.getUser_id())) {
        		successECouponCnt   += dto.getSuccess_coupon_list().size();	// �����  e-Coupon
        	} else {
        		successMMSCouponCnt += dto.getSuccess_coupon_list().size();	// �����  MMS Coupon
        	}
        	failCouponCnt       += dto.getFail_coupon_list().size();
    	}
    	
    	// ��� SMS �߼�
        String[] contentArgs = {String.valueOf(orderCnt),                   // ���� : ����Ǽ�
                				String.valueOf(wholeCakeCnt),               // ���� : �󼼰Ǽ�
                				String.valueOf(couponOrderCnt),             // �������� : ����Ǽ� 
                				String.valueOf(couponWholecakeCnt),         // �������� : �󼼰Ǽ�
                				String.valueOf(totalCouponCnt),             // ����������
                				String.valueOf(successECouponCnt),          // e-���� ���� �Ǽ�
                				String.valueOf(successMMSCouponCnt)         // MMS���� ���� �Ǽ�
                				};
        
        this.smsSend(contentArgs);
    	
        // ��� �α� ���
        if (logger.isInfoEnabled()) {
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "����Ȯ���� ����/Ȧ���� �Ǽ�\t: ", orderCnt, "�� / ", wholeCakeCnt,"��"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "���������� ����/Ȧ���� �Ǽ�\t: ", couponOrderCnt, "�� / ", couponWholecakeCnt,"�� (Ȧ�������� ����� ���� - ", usedCoupon, "�� ����)"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "�� ���� ����  ��� \t\t\t: ", totalCouponCnt, "��"));
        	if(isExcept){
        		logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� \t\t\t\t: ", exceptCnt, "��"));
        	}
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "e-Coupon ����\t\t\t\t\t: ", successECouponCnt, "��"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "MMS Coupon ���� \t\t\t\t: ", successMMSCouponCnt, "��"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "���� ���� ���� \t\t\t\t: ", failCouponCnt,    "��\n"));
        	
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "END : ", (System.currentTimeMillis() - startTime), "ms"));
			
			/********************************************
			 BATCH MONITORING END
			 ********************************************/
			try {
				if (bm_is_insert_fail) {
					bm_is_insert_fail = false;
					
				} else {
					helper.endMonitor(bm_today, bm_start_cnt , "WHOLECAKE_CP_SERVICE", "WHOLECAKE_CP_DETAIL" ,totalCouponCnt, (successECouponCnt+successMMSCouponCnt), failCouponCnt,"");
				}
			} catch (XOException e) {
				logger.error(e.getMessage(), e);
			}
			
			
		}
        
	}

	/**
	 * ���� ���� ��� Ȧ���� ���� ��ȸ
	 * @param salesOrderDate
	 * @return
	 */
	private List<WholecakeOrderDto> getWholeCakeOrderList(String salesOrderDate) {
		List<WholecakeOrderDto> wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		Map<String, Object> dbMap = new HashMap<String, Object>();

		try {
			if (StringUtils.isNotEmpty(salesOrderDate)) {
				dbMap.put("salesOrderDate", salesOrderDate);
			}
			wholecakeOrderList = this.xoWholecakeOrderMgr.getWholeCakeOrderListForCoupon(xoSqlMap, dbMap);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " Ȧ���� ���� ���� ���� ��� ��ȸ Exception : PARAMS-", dbMap.toString()), e);
			}
		} finally {
			dbMap.clear();
		}

		return wholecakeOrderList;
	}
	
	/**
	 * ���� ������ ���� ��ȸ
	 * @return
	 */
	private CouponMasterDto getCouponMaster() {
		CouponMasterDto cmDto = new CouponMasterDto();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();

		try {
			dbMap.put("couponCode",           XOUtil.getPropertiesString("coupon.wholecake.code"));
			dbMap.put("couponPolicyCode",     XOUtil.getPropertiesString("coupon.wholecake.policy.code"));
			dbMap.put("couponPublicationSeq", XOUtil.getPropertiesString("coupon.wholecake.publication.seq"));
			dbMap.put("sckUserId",            XOUtil.getPropertiesString("mms.sck.id"));

			cmDto = this.couponMgr.getCouponMaster(msrSqlMap, dbMap);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " ���� ������ ���� ��ȸ   Exception : PARAMS-", dbMap.toString()), e);
			}
		}

		return cmDto;
	}

	/**
	 * e-���� ���� (MSR ȸ��)
	 * @param wholecakeDto
	 * @param cmDto
	 * @return
	 */
	private List<CouponPublicationListDto> publishECoupon(WholecakeOrderDto wholecakeDto, CouponMasterDto cmDto) {
		List<CouponPublicationListDto> couponList = new ArrayList<CouponPublicationListDto>();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		CardRegMemberDto memberDto = new CardRegMemberDto();
		String processStep = "";

		SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		try {
			// Ʈ������ ����
			msrSqlMap.startTransaction();

			// MSR ȸ�� ���� ��ȸ
			processStep = "MSR ȸ�� ���� ��ȸ";
			dbMap.put("userId", wholecakeDto.getUser_id());
			memberDto = this.couponMgr.getCardRegMember(msrSqlMap, dbMap);
			if (memberDto == null || StringUtils.isEmpty(memberDto.getUser_number())) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "MSR ȸ������ ���� : PARAMS-", dbMap.toString()));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// ���� ��ȣ ä��
			processStep = "���� ��ȣ ä��";
			dbMap.clear();
			dbMap.put("couponCode", cmDto.getCoupon_code());
			dbMap.put("couponCnt", wholecakeDto.getTotal_coupon_count()); // �����ؾ� �ϴ� ���� ���� (��ǰ��*��å�� ���ǵ� ���ʹ� ���� ���� ��)
			couponList = this.couponMgr.getCouponNumberList(msrSqlMap, dbMap);
			if (couponList.size() != wholecakeDto.getTotal_coupon_count()) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "������ȣ ä�� �� �� ���� : ��û�� ä�� �Ǽ�-[", wholecakeDto.getTotal_coupon_count(), "], ä���� �Ǽ�-[", couponList.size(), "]"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// ���� ���� ������ ��� (insert all)
			processStep = "���� ���� ������ ��� (insert all)";
			for (CouponPublicationListDto dto : couponList) {
				dto.setUser_number           (memberDto.getUser_number());
				dto.setIpin_dup_key          (memberDto.getIpinDupKey());
				dto.setStr_reg_date          (DateTime.getCurrentDate(13)); // �����Ͻ� - yyyyMMddHHmmss
				dto.setCoupon_policy_code    (cmDto.getCoupon_policy_code());
				dto.setCoupon_publication_seq(cmDto.getCoupon_publication_seq());
				dto.setValid_day             (cmDto.getValid_day());
				dto.setExpire_start_date     (DateUtil.getDate(cmDto.getCoupon_start_date()));
				dto.setExpire_end_date       (DateUtil.getDate(cmDto.getCoupon_end_date()));
				dto.setGift_send_cnt         (cmDto.getGift_send_max_cnt());
				dto.setGift_mms_retry_cnt    (cmDto.getGift_mms_retry_max_cnt());
				dto.setGift_discard_cnt      (cmDto.getGift_discard_max_cnt());
				dto.setGift_status           ("0"); // ������������{0-��������, 1-��������, 2-�������, 9-�����Ұ�}
				wholecakeDto.getSuccess_coupon_list().add(dto.getCoupon_number());
			}
			dbMap.clear();
			dbMap.put("list_size", couponList.size());
			int publishCnt = this.couponMgr.insertCouponPublicationListMulti(msrSqlMap, couponList);
			if (publishCnt != couponList.size()) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "������ȣ ���� ���� : ��û�� ���� ��-[", couponList.size(), "], ����� ���� ��-[", publishCnt, "]"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// Ʈ������ ����
			msrSqlMap.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] ���� Exception : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] ���� Exception : PARAMS-", dbMap.toString()), e);
				}
			}
			// ���� �߻��� ����
			wholecakeDto.setSuccess_coupon_list(new ArrayList<String>());
			return new ArrayList<CouponPublicationListDto>();
		} finally {
			try {
				msrSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " ����  Ʈ������ ���� ���� : ", ee.getMessage()), ee);
				}
			}
		}

		// ���� �߼� ��û ��� - ���� �߼� �����Ͽ��� ��ü������ ����.
		SqlMapClient msrMailSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		try {
			msrMailSqlMap.startTransaction();

			String mail = StringUtils.defaultIfEmpty(wholecakeDto.getUser_email(), memberDto.getEmail()); // 1. Ȧ���� ����, 2. MSR ���� ����
			if (StringUtils.isEmpty(mail)) {
				throw new XOException("email �� ����.");
			}

			// ���� ���� ����
			Calendar startCal  = DateUtil.getCalendar(cmDto.getCoupon_start_date());
			Calendar endCal    = DateUtil.getCalendar(cmDto.getCoupon_end_date());
			String   startDate = DateTime.getFormatString(startCal.getTime(), "yyyy-MM-dd");
			String   endDate   = DateTime.getFormatString(endCal.getTime(),   "yyyy-MM-dd");

			StringBuffer mailArgs = new StringBuffer();
			mailArgs.append(memberDto.getUser_id());	// ����� ���̵�
			mailArgs.append("|");
			mailArgs.append(startDate);					// �߱���
			mailArgs.append("|");
			mailArgs.append(startDate);					// ��ȿ�Ⱓ ������
			mailArgs.append("|");
			mailArgs.append(endDate);					// ��ȿ�Ⱓ ������
			mailArgs.append("|");
			mailArgs.append(couponList.size());			// �߱� ���� ����

			// ���� ����
			String mailTitle = XOUtil.getPropertiesString("coupon.wholecake.mail.title",  new String[]{wholecakeDto.getUser_id()});

			// ���� �߼�
			SendMailQueueDto mailDto = new SendMailQueueDto();
			mailDto.setUser_id     (memberDto.getUser_id());
			mailDto.setTemplate    (cmDto.getCoupon_policy_code());
			mailDto.setMail_args   (mailArgs.toString());
			mailDto.setMail_title  (mailTitle);
			mailDto.setEmail       (mail);
			mailDto.setStr_reg_date(DateTime.getCurrentDate(13)); // yyyyMMddHHmmss
			this.mailMgr.insertSendMailQueue(msrMailSqlMap, mailDto);
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// Ʈ������ ����
			msrMailSqlMap.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "�̸��Ϲ߼� ���� : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " �̸��Ϲ߼�  Exception "), e);
				}
			}
		} finally {
			try {
				msrMailSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " �̸��Ϲ߼�  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
				}
			}
		}

		return couponList;
	}

	/**
	 * MMS ���� ���� (MSR ��ȸ��)
	 * 
	 * @param wholecakeDto
	 * @return
	 */
	private List<CouponPublicationListDto> publishMMSCoupon(WholecakeOrderDto wholecakeDto, CouponMasterDto cmDto) {
		List<CouponPublicationListDto> couponList = new ArrayList<CouponPublicationListDto>(); // ���� ���� ���
		Map<String, Object> dbMap = new HashMap<String, Object>();
		String processStep = "";

		SqlMapClient msrSqlMap1 = IBatisSqlConfig.getMsrSqlMapInstance();
		try {
			msrSqlMap1.startTransaction();

			// ������ȣ/������ȣ ä��
			processStep = "������ȣ/������ȣ ä��";
			dbMap.clear();
			dbMap.put("couponCode", cmDto.getCoupon_code());
			dbMap.put("couponCnt", wholecakeDto.getTotal_coupon_count()); // �����ؾ� �ϴ� ���� ���� (��ǰ��*��å�� ���ǵ� ���ʹ� ���� ���� ��)
			couponList = this.couponMgr.getCouponNumberListForGift(msrSqlMap1, dbMap);
			if (couponList.size() != wholecakeDto.getTotal_coupon_count()) {
				throw new XOException(oUtil.concatString(logSb, "���� ��ȣ ä�� ���� "));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// ���� ���� ������ ��� (insert all)
			processStep = "���� ���� ������ ��� (insert all)";
			for (CouponPublicationListDto dto : couponList) {
				dto.setUser_number           (cmDto.getSck_user_number());	// msr��ȸ���̹Ƿ� ������ ���� ��ȣ
				dto.setStr_reg_date          (DateTime.getCurrentDate(13)); // �����Ͻ� - yyyyMMddHHmmss
				dto.setCoupon_policy_code    (cmDto.getCoupon_policy_code());
				dto.setCoupon_publication_seq(cmDto.getCoupon_publication_seq());
				dto.setValid_day             (cmDto.getValid_day());
				dto.setExpire_start_date     (DateUtil.getDate(cmDto.getCoupon_start_date()));
				dto.setExpire_end_date       (DateUtil.getDate(cmDto.getCoupon_end_date()));
				dto.setGift_send_cnt         (cmDto.getGift_send_max_cnt() - 1); //
				dto.setGift_mms_retry_cnt    (cmDto.getGift_mms_retry_max_cnt());
				dto.setGift_discard_cnt      (cmDto.getGift_discard_max_cnt());
				dto.setGift_status           ("1"); // ������������{0-��������, 1-��������, 2-�������, 9-�����Ұ�}
				wholecakeDto.getSuccess_coupon_list().add(dto.getCoupon_number());
			}
			dbMap.clear();
			dbMap.put("list_size", couponList.size());
			int publishCnt = this.couponMgr.insertCouponPublicationListMulti(msrSqlMap1, couponList);
			if (publishCnt != couponList.size()) {
				throw new XOException(oUtil.concatString(logSb, "��������"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// Ʈ������ ����
			msrSqlMap1.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] ���� ���� : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] ���� Exception : PARAMS-", dbMap.toString()), e);
				}
			}

			// ���� �߻��� ����
			return new ArrayList<CouponPublicationListDto>();
		} finally {
			try {
				msrSqlMap1.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " ����  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
				}
			}
		}

		// MMS �߼�
		// �̹��� ����
		String   imgTitle        = XOUtil.getPropertiesString("coupon.wholecake.img.title");
		String   rootPath        = XOUtil.getPropertiesString("coupon.root.path");
		String   backImgPath     = XOUtil.getPropertiesString("coupon.wholecake.back.img.path",    new String[]{rootPath});
		String   defaultImgPath  = XOUtil.getPropertiesString("coupon.wholecake.default.img.path", new String[]{rootPath});
		String   imgSavePath     = XOUtil.getPropertiesString("coupon.wholecake.img.save.path",    new String[]{rootPath});
		// MMS ���� ����
		String   subject         = XOUtil.getPropertiesString("mms.wholecake.coupon.title");	// MMS ����
		String   callback        = XOUtil.getPropertiesString("mms.sck.callback");				// MMS �߽���
		for (CouponPublicationListDto couponDto : couponList) {
			String giftCpnNumber = couponDto.getGift_send_coupon_num();
			
			SqlMapClient xoSqlMap  = IBatisSqlConfig.getXoSqlMapInstance();
			SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			try {
				xoSqlMap.startTransaction();
				msrSqlMap.startTransaction();
				
				processStep = "MMS �̹��� ����";
				String   couponStartDate = DateUtil.getDateString(couponDto.getExpire_start_date(), "yyyy-MM-dd");
				String   couponEndDate   = DateUtil.getDateString(couponDto.getExpire_end_date(),   "yyyy-MM-dd");
				String   propContents    = XOUtil.getPropertiesString("coupon.wholecake.img.contents", new String[]{couponStartDate, couponEndDate});
				String[] contents        = StringUtils.split(propContents, ";");
				String   imgName         = XOUtil.getPropertiesString("coupon.wholecake.img.name",     new String[]{wholecakeDto.getOrder_no(), giftCpnNumber});
				// VO ���
				MMSImageVO imgVO = new MMSImageVO();
				imgVO.setImgName         (imgName);									// �̹��� ���� �� (�����ȣ_������ȣ.jpg)
				imgVO.setImgTitle        (imgTitle);								// �̹��� ����
				imgVO.setImgSubTitle     (cmDto.getUser_coupon_name());				// ������
				imgVO.setImgContents     (contents);								// �̹��� �׸� ����
				imgVO.setBackImgPath     (backImgPath); 							// ��� �̹��� ���
				imgVO.setImgBasePath     (imgSavePath);  							// MMS �̹��� ���� ���
				imgVO.setContentsImgPath (defaultImgPath);           	  			// ���� �̹��� ���
				imgVO.setBarcode         (couponDto.getGift_send_coupon_num());		// �����������۹�ȣ
				MMSImageVO mmsImgVO = this.cImage.getImageForMMS(imgVO, this.loggerTitle);
				if(!mmsImgVO.getIsSuccess()){
					throw new XOException("MMS �̹��� ���� ����");
				}
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

				processStep = "MMS �̹��� ���� ���� ���";
				EmMmtFileDto mmsFileDto   = new EmMmtFileDto();
				mmsFileDto.setAttach_file_subpath(StringUtils.replace(mmsImgVO.getImgPath(), File.separator, "/"));		// �̹��� ���
				mmsFileDto.setAttach_file_name   (mmsImgVO.getImgName());		// �̹��� ���� ��
				long attachFileGroupKey   = this.mmsMgr.insertEmMmtFile(xoSqlMap, mmsFileDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

				processStep = "MMS �߼� ��û ���";
				String   contentProd      = "";
				if(StringUtils.isNotEmpty(cmDto.getUse_product())){
					contentProd = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.product", new String[]{cmDto.getUse_product()});
				}
				String   contentNoti      = "";
				if(StringUtils.isNotEmpty(cmDto.getUse_notice())){
					contentNoti = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.notice",  new String[]{cmDto.getUse_notice()});
				}
				String   contentCpnName   = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.cname",  new String[]{cmDto.getCoupon_name()});
				String   contentCpnDate   = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.cdate",  new String[]{couponStartDate, couponEndDate});
				String   contentCpnNumber = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.number", new String[]{StringUtils.left(giftCpnNumber, 4), StringUtils.mid(giftCpnNumber, 4, 5), StringUtils.right(giftCpnNumber, 4)});
				String   couponRegLink    = XOUtil.getPropertiesString("mms.wholecake.coupon.contents.regist", new String[]{TripleDesApp.encrypt(giftCpnNumber)});
				String[] contentArgs      = {"",                          // ���� ����
				                            contentProd,                  // �̿� ���� ��ǰ
				                            contentNoti,                  // ���� ���ǻ���
				                            contentCpnName,               // e-������			
				                            contentCpnDate,               // ���� ��ȿ �Ⱓ
				                            contentCpnNumber,             // ���� ��ȣ
				                            couponRegLink                 // e-���� ��� �ٷΰ���
				                            };
				String   content          = XOUtil.getPropertiesString("mms.wholecake.coupon.contents", contentArgs);
				SmtTranDto mmsDto         = new SmtTranDto();
				mmsDto.setSubject              (subject);
				mmsDto.setContent              (content);
				mmsDto.setAttach_file_group_key(attachFileGroupKey);
				mmsDto.setCallback             (callback);
				mmsDto.setRecipient_num        (wholecakeDto.getUser_mobile());
				mmsDto.setService_type         ("2");									// ���� �޽��� ���� Ÿ�� {2-MMS MT, 3-LMS}
				mmsDto.setXo_order_no          (wholecakeDto.getOrder_no()); 
				mmsDto.setUser_id(wholecakeDto.getUser_id());	// ����ھ��̵�
				long mtPr = this.mmsMgr.insertEmMmtTran(xoSqlMap, mmsDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				processStep = "���� ���� �̷� ���";
				CouponGiftHistoryDto cghDto = new CouponGiftHistoryDto();
				cghDto.setCoupon_number       (couponDto.getCoupon_number());
				cghDto.setGift_send_coupon_num(couponDto.getGift_send_coupon_num());
				cghDto.setHistory_code        ("1");									// �����ڵ�{1-����,2-������,3-ȸ��,4-�缱��,5-��ȸ��,6-���,7-������}
				cghDto.setSend_user_number    (cmDto.getSck_user_number());				// ���� ����� ������ȣ : msr��ȸ���̹Ƿ� ������ ���� ��ȣ
				cghDto.setGift_send_mobile_num(wholecakeDto.getUser_mobile());			// ���������޴���ȭ��ȣ
				cghDto.setMms_title           (subject);
				cghDto.setMms_message         ("");
				cghDto.setMms_img_1           (imgVO.getImgPath());
				cghDto.setMms_seq             (mtPr);
				cghDto.setGift_method         ("S");									// �������{W-WEB, P-POS, A-APP, S-ADMIN, M-MOBILE}
				long giftHistNo = this.couponMgr.inserMsrCouponGiftHistory(msrSqlMap, cghDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				processStep = "���� ���� ���� ������Ʈ";
				couponDto.setGift_hist_no(giftHistNo);
				this.couponMgr.updateCouponPublicationList(msrSqlMap, couponDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				// Ʈ����� ����
				xoSqlMap.commitTransaction();
				msrSqlMap.commitTransaction();
			} catch (Exception e) {
				wholecakeDto.getFail_coupon_list().add(couponDto.getCoupon_number());	// MMS �߼� ����
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", couponDto.getCoupon_number(), "] MMS �߼� Exception : ", e.getMessage()), e);
				}
			} finally {
				try {
					xoSqlMap.endTransaction();
					msrSqlMap.endTransaction();
				} catch (Exception ee) {
					if (logger.isErrorEnabled()) {
						logger.error(oUtil.concatString(logSb, this.loggerTitle, " MMS �߼�  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
					}
				}
			}
		} // for 

		return couponList;
	}

	/**
	 * ���� ���� �� ó��
	 * @param orderNoList ������ ����� ���� ��ȣ
	 * @param couponList  ����� ���� ���
	 */
	private void publichCouponAfterProcess(List<String> orderNoList, List<CouponPublicationListDto> couponList) {
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		// Ȧ���� ������ ���� ���� ���� ������Ʈ
	    SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			xoSqlMap.startTransaction();
			List<String> updateList  = new ArrayList<String>(); // 100�� �� ���� ���� ���
			long allUpdateCnt = 0;
			
			// 100�� �� ���� ������Ʈ
			for (int i = 0; i < orderNoList.size() ; i++){
				updateList.add(orderNoList.get(i));
				
				if(updateList.size() >= 100 || i == (orderNoList.size()-1)){
					dbMap.put("orderNoList", updateList);
					long orderCnt = this.xoWholecakeOrderMgr.updateWholeCakeOrderForCouponPubFlag(xoSqlMap, dbMap);
					if(logger.isDebugEnabled()){
						logger.debug(oUtil.concatString(logSb, this.loggerTitle, "Ȧ���� ���� ���� ������Ʈ - ", orderCnt, "��"));
					}
					allUpdateCnt += orderCnt;
					updateList.clear();
				}
			}
			
			// �� ���� �Ǽ�
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "��ü Ȧ���� ���� ���� ������Ʈ - ", allUpdateCnt, "��"));
			}

			// Ʈ������ ����
			xoSqlMap.commitTransaction();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "Ȧ���Ϳ��� ���� ������Ʈ Exception :", e.getMessage()), e);
			}
		} finally {
			try {
				xoSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "Ȧ���Ϳ��� ���� ������Ʈ  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
				}
			}
		}
	
		// ���������� ���� ���
	    SqlMapClient scksaSqlMap = IBatisSqlConfig.getScksasqlMapInstance();
		try {
			scksaSqlMap.startTransaction();
			List<CouponPublicationListDto> insertList  = new ArrayList<CouponPublicationListDto>(); // 100�� �� ���� ���� ����
			long allInsertCnt = 0;
			
			// 100�� �� ���� ���
			for (int i = 0; i < couponList.size() ; i++){
				insertList.add(couponList.get(i));
				
				if(insertList.size() >= 100 || i == (couponList.size()-1)){
					long couponCnt = this.couponMgr.insertSmkCoupSeqMulti(scksaSqlMap, insertList);
					if(logger.isDebugEnabled()){
						logger.debug(oUtil.concatString(logSb, this.loggerTitle, "���������� ���� ��� - ", couponCnt, "��"));
					}
					allInsertCnt += couponCnt;
					insertList.clear();
				}
			}
			
			// �� ���� �Ǽ�
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "��ü ���������� ���� ��� - ", allInsertCnt, "��\n"));
			}

			// Ʈ������ ����
			scksaSqlMap.commitTransaction();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "���������� ���� ���  Exception", e.getMessage()), e);
			}
		} finally {
			try {
				scksaSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "���������� ���� ���  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
				}
			}
		}		
	}
	
	// Ȧ���� ���� ���� �������� ���� �߼� ���� ��� SMS �߼�
	private void smsSend(Object[] params) {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // ���̷����� Ʈ������
		
		try {
			sqlMap.startTransaction();
			
			Configuration conf = CommPropertiesConfiguration.getConfiguration("sms.properties");
			String content = conf.getString("wholecake.sms.coupon.msg");
			content = MessageFormat.format(content, params);
			
			String callback = conf.getString("wholecake.sms.callback");
			String recipientNum = conf.getString("wholecake.sms.receive.info");
			String recipientArr[] = recipientNum.split("\\|");
			
	        // SMS �߼� ��û 
			for(int i = 0; i < recipientArr.length; i++) {
		        SmtTranDto smtTranDto = new SmtTranDto ();
		        smtTranDto.setPriority ("S"); //���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
		        smtTranDto.setContent (content);
		        smtTranDto.setCallback (callback);
		        smtTranDto.setRecipient_num (recipientArr[i]);

		        this.mmsMgr.insertSmtTran (sqlMap, smtTranDto);
			}
			sqlMap.commitTransaction (); // commit
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " �������� ��� SMS �߼�  Exception "), e);
			}
		} finally {
			try {
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " �������� ��� SMS �߼�  Ʈ������ ���� Exception : ", ee.getMessage()), ee);
				}
			}
		}
	}	

	public static void main(String[] args) {
		
		
		if (args.length > 0) {
			new WholecakeOrderCoupon().start(args[0]);
		} else {
			new WholecakeOrderCoupon().start("");
		}
	}
}
