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
 * 홀케익 예약에 대한 쿠폰 발행 - WholecakeOrderCoupon. 
 * 홀케익 예약 취소 불가 시점에서 홀케익 예약 감사 쿠폰 발행하는 프로세스
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
	private final XoWholecakeOrderMgr xoWholecakeOrderMgr;  // 홀케익 정보
	private final CouponMgr           couponMgr;            // MSR 쿠폰 발행
	private final MailMgr             mailMgr;	          // MSR 메일 발송 요청
	private final MMSMgr              mmsMgr;               // MMS 발송 요청
	private final CreateImage         cImage;               // 이미지 생성

	public WholecakeOrderCoupon() {
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mailMgr             = new MailMgr();
		this.couponMgr           = new CouponMgr();
		this.cImage              = new CreateImage(logger);
		this.mmsMgr              = new MMSMgr();
		this.logSb               = new StringBuffer(); // log용 StringBuffer
		this.loggerTitle         = " [wholecake][coupon] ";
	}

	/**
	 * 홀케익 예약에 대한 쿠폰 발행 프로세스.
	 * @param salesOrderDate 발주확정일(예약 취소 불가능한 시점)
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
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "발주확정일 \t\t\t\t\t: ", StringUtils.defaultIfEmpty(salesOrderDate, DateUtil.getToday()), "\n" ));
		}

		// 쿠폰 발행 대상 홀케익 예약 조회 ====================================================
		List<WholecakeOrderDto> wholecakeOrderList = getWholeCakeOrderList(salesOrderDate);
		if(wholecakeOrderList == null){
			wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		}

		// 쿠폰 마스터 정보 조회 =========================================================
		CouponMasterDto couponMasterDto = new CouponMasterDto();
		if (wholecakeOrderList.size() > 0) {
			couponMasterDto = this.getCouponMaster();
		}

		List<String> orderNoList     = new ArrayList<String>(); // 쿠폰이 발행된 홀케익 예약
		List<String> failOrderNoList = new ArrayList<String>(); // 쿠폰이 발행을 실패한 홀케익 예약
		List<CouponPublicationListDto> couponList = new ArrayList<CouponPublicationListDto>(); // 발행된 쿠폰 번호

		
		int     exceptCnt = 0;		// 쿠폰 발행 제외 수
		boolean isExcept  = false;	// 쿠폰 발행 제외 처리 여부
		if (wholecakeOrderList.size() > 0 && couponMasterDto != null && StringUtils.isNotEmpty(couponMasterDto.getCoupon_name())) {
			
			// 테스트 기간 예외처리 (특정 정책 코드의 홀케익 쿠폰 발송 제외) 기간 확인
			String  today           = DateUtil.getToday();												// 오늘 날짜 YYYYMMDD
			String  exceptPolicyCd  = XOUtil.getPropertiesString("coupon.wholecake.except.policy");		// 쿠폰 발행 제외되는 정책 코드
			String  exceptStartDate = XOUtil.getPropertiesString("coupon.wholecake.except.startdate");	// 쿠폰 발행 제외되는 시작일
			String  exceptEndDate   = XOUtil.getPropertiesString("coupon.wholecake.except.enddate");	// 쿠폰 발행 제외되는 종료일
			String  testIds         = XOUtil.getPropertiesString("coupon.wholecake.test.id");	// 쿠폰 발행 제외기간에도 허용되는 아이디
			try {
				if((Integer.parseInt(exceptStartDate) <= Integer.parseInt(today)) && (Integer.parseInt(today) <= Integer.parseInt(exceptEndDate))){
					isExcept = true;
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 제외 정보 - 제외 정책 코드 : ", exceptPolicyCd, ", 예외처리기간: ", exceptStartDate," ~ ", exceptEndDate, ", 허용ID: ", testIds, "\n"));
					}
				}
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 제외 처리 실패 "), e);
				}
			}
				
			// 쿠폰 발행 처리 ==============================================================
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 시작 ==================\n"));
			}
			int cnt = 1;
			for (WholecakeOrderDto dto : wholecakeOrderList) {
				// 쿠폰 발행 수가 없는 경우(홀케익 쿠폰 사용한 경우 -처리로 발행요청건수가 0일 수 있음)
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
				// 테스트 기간 예외처리 (특정 정책 코드의 홀케익 쿠폰 발송 제외)
				if(isExcept && StringUtils.equals(exceptPolicyCd, dto.getPolicy_cd())){
					if(!StringUtils.contains(testIds, oUtil.concatString("|", dto.getUser_id(), "|"))){
						if(logger.isInfoEnabled()){
							logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 제외 처리 - 해당 주문 정책 : ", dto.getPolicy_cd(), ", 예약 명 : ", dto.getOrder_name(), "\n" ));
						}
						exceptCnt += dto.getTotal_coupon_count();
						continue;
					}
				}
				
				List<CouponPublicationListDto> thisCouponList = new ArrayList<CouponPublicationListDto>();
				if (StringUtils.equals(dto.getMsr_user_flag(), "Y") && StringUtils.isNotEmpty(dto.getUser_id())) {
					// MSR 회원은 E-쿠폰으로 발행
					thisCouponList = this.publishECoupon(dto, couponMasterDto);
				} else {
					// MSR 비회원은 MMS 쿠폰으로 발행
					thisCouponList = this.publishMMSCoupon(dto, couponMasterDto);
				}
				if (thisCouponList == null || thisCouponList.size() == 0) {
					failOrderNoList.add(dto.getOrder_no());
					dto.setPublication_coupon_count(0);
					continue;
				}
				dto.setPublication_coupon_count(thisCouponList.size());

				orderNoList.add(dto.getOrder_no());						// 쿠폰 발행 여부 업데이트 하기 위한 예약 번호
				couponList.addAll(couponList.size(), thisCouponList);	// 영업 정보에 쿠폰 등록
				
				if(logger.isInfoEnabled()){
					if (dto.getFail_coupon_list() != null && dto.getFail_coupon_list().size() != 0 ) {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "홀케익(", dto.getPolicy_cd(), ") : ", dto.getTotal_qty(), "개, 발행대상쿠폰 : ", dto.getTotal_coupon_count(),"건, 발행 : ", dto.getPublication_coupon_count(),"건, MMS 실패 : ", dto.getFail_coupon_list().size(), "건(" , dto.getFail_coupon_list(),")\n"));
					} else {
						logger.info(oUtil.concatString(logSb, this.loggerTitle, "홀케익(", dto.getPolicy_cd(), ") : ", dto.getTotal_qty(), "개, 발행대상쿠폰 : ", dto.getTotal_coupon_count(),"건, 발행 : ", dto.getPublication_coupon_count(),"건\n"));
					}
				}
			} // end For
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 종료 ==================\n"));
			}

			// 홀케익 예약 쿠폰 발행 후 처리 (쿠폰발행여부 업데이트, 영업정보 등록) ==========================
			if(orderNoList.size() > 0){
				publichCouponAfterProcess(orderNoList, couponList);
			}
		}

		int orderCnt            = 0;	// 발주 예약 건수
    	int wholeCakeCnt        = 0;	// 발주 홀케익 건수
    	int usedCoupon          = 0;    // 예약 시 사용한 홀케익 쿠폰
    	int couponOrderCnt      = 0;	// 쿠폰 발행 대상 예약 건수
    	int couponWholecakeCnt  = 0;	// 쿠폰 발행 대상 홀케익 건수
    	int totalCouponCnt      = 0;    // 총 쿠폰 발행 대상 건수
    	int successECouponCnt   = 0;	// 발행된  e-Coupon
    	int successMMSCouponCnt = 0;	// 발행된 MMS Coupon
    	int failCouponCnt       = 0;	// 실패 건수
    	for(WholecakeOrderDto dto : wholecakeOrderList){
    		orderCnt            += 1;	                                    // 발주 예약 건수
        	wholeCakeCnt        += dto.getTotal_qty();	                    // 발주 홀케익 건수
        	if(dto.getTotal_coupon_count() > 0){
            	couponOrderCnt      += 1;	                                // 쿠폰 발행 대상 예약 건수
            	couponWholecakeCnt  += (dto.getTotal_coupon_count()/dto.getCoupon_count());			// 쿠폰 발행 대상 홀케익 건수
        	}
        	usedCoupon          += dto.getTotal_qty() - (dto.getTotal_coupon_count()/dto.getCoupon_count());
        	totalCouponCnt      += dto.getTotal_coupon_count();			// 쿠폰 발행 대상 홀케익 건수
        	if (StringUtils.equals(dto.getMsr_user_flag(), "Y") && StringUtils.isNotEmpty(dto.getUser_id())) {
        		successECouponCnt   += dto.getSuccess_coupon_list().size();	// 발행된  e-Coupon
        	} else {
        		successMMSCouponCnt += dto.getSuccess_coupon_list().size();	// 발행된  MMS Coupon
        	}
        	failCouponCnt       += dto.getFail_coupon_list().size();
    	}
    	
    	// 결과 SMS 발송
        String[] contentArgs = {String.valueOf(orderCnt),                   // 발주 : 예약건수
                				String.valueOf(wholeCakeCnt),               // 발주 : 상세건수
                				String.valueOf(couponOrderCnt),             // 쿠폰발행 : 예약건수 
                				String.valueOf(couponWholecakeCnt),         // 쿠폰발행 : 상세건수
                				String.valueOf(totalCouponCnt),             // 쿠폰발행대상
                				String.valueOf(successECouponCnt),          // e-쿠폰 발행 건수
                				String.valueOf(successMMSCouponCnt)         // MMS쿠폰 발행 건수
                				};
        
        this.smsSend(contentArgs);
    	
        // 결과 로그 출력
        if (logger.isInfoEnabled()) {
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "발주확정된 예약/홀케익 건수\t: ", orderCnt, "건 / ", wholeCakeCnt,"건"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰발행대상 예약/홀케익 건수\t: ", couponOrderCnt, "건 / ", couponWholecakeCnt,"건 (홀케익쿠폰 사용한 케익 - ", usedCoupon, "건 제외)"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "총 쿠폰 발행  대상 \t\t\t: ", totalCouponCnt, "건"));
        	if(isExcept){
        		logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 제외 \t\t\t\t: ", exceptCnt, "건"));
        	}
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "e-Coupon 발행\t\t\t\t\t: ", successECouponCnt, "건"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "MMS Coupon 발행 \t\t\t\t: ", successMMSCouponCnt, "건"));
        	logger.info(oUtil.concatString(logSb, this.loggerTitle, "쿠폰 발행 실패 \t\t\t\t: ", failCouponCnt,    "건\n"));
        	
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
	 * 쿠폰 발행 대상 홀케익 예약 조회
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
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " 홀케익 예약 쿠폰 발행 대상 조회 Exception : PARAMS-", dbMap.toString()), e);
			}
		} finally {
			dbMap.clear();
		}

		return wholecakeOrderList;
	}
	
	/**
	 * 쿠폰 마스터 정보 조회
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
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " 쿠폰 마스터 정보 조회   Exception : PARAMS-", dbMap.toString()), e);
			}
		}

		return cmDto;
	}

	/**
	 * e-쿠폰 발행 (MSR 회원)
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
			// 트랜젝션 시작
			msrSqlMap.startTransaction();

			// MSR 회원 정보 조회
			processStep = "MSR 회원 정보 조회";
			dbMap.put("userId", wholecakeDto.getUser_id());
			memberDto = this.couponMgr.getCardRegMember(msrSqlMap, dbMap);
			if (memberDto == null || StringUtils.isEmpty(memberDto.getUser_number())) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "MSR 회원정보 실패 : PARAMS-", dbMap.toString()));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 쿠폰 번호 채번
			processStep = "쿠폰 번호 채번";
			dbMap.clear();
			dbMap.put("couponCode", cmDto.getCoupon_code());
			dbMap.put("couponCnt", wholecakeDto.getTotal_coupon_count()); // 발행해야 하는 쿠폰 개수 (상품수*정책에 정의된 케익당 쿠폰 발행 수)
			couponList = this.couponMgr.getCouponNumberList(msrSqlMap, dbMap);
			if (couponList.size() != wholecakeDto.getTotal_coupon_count()) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "쿠폰번호 채번 건 수 오류 : 요청한 채번 건수-[", wholecakeDto.getTotal_coupon_count(), "], 채번된 건수-[", couponList.size(), "]"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 쿠폰 발행 내역에 등록 (insert all)
			processStep = "쿠폰 발행 내역에 등록 (insert all)";
			for (CouponPublicationListDto dto : couponList) {
				dto.setUser_number           (memberDto.getUser_number());
				dto.setIpin_dup_key          (memberDto.getIpinDupKey());
				dto.setStr_reg_date          (DateTime.getCurrentDate(13)); // 현재일시 - yyyyMMddHHmmss
				dto.setCoupon_policy_code    (cmDto.getCoupon_policy_code());
				dto.setCoupon_publication_seq(cmDto.getCoupon_publication_seq());
				dto.setValid_day             (cmDto.getValid_day());
				dto.setExpire_start_date     (DateUtil.getDate(cmDto.getCoupon_start_date()));
				dto.setExpire_end_date       (DateUtil.getDate(cmDto.getCoupon_end_date()));
				dto.setGift_send_cnt         (cmDto.getGift_send_max_cnt());
				dto.setGift_mms_retry_cnt    (cmDto.getGift_mms_retry_max_cnt());
				dto.setGift_discard_cnt      (cmDto.getGift_discard_max_cnt());
				dto.setGift_status           ("0"); // 쿠폰선물상태{0-선물가능, 1-쿠폰전송, 2-쿠폰등록, 9-선물불가}
				wholecakeDto.getSuccess_coupon_list().add(dto.getCoupon_number());
			}
			dbMap.clear();
			dbMap.put("list_size", couponList.size());
			int publishCnt = this.couponMgr.insertCouponPublicationListMulti(msrSqlMap, couponList);
			if (publishCnt != couponList.size()) {
				throw new XOException(oUtil.concatString(logSb, this.loggerTitle, "쿠폰번호 발행 오류 : 요청한 발행 수-[", couponList.size(), "], 발행된 쿠폰 수-[", publishCnt, "]"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 트랜젝션 종료
			msrSqlMap.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] 발행 Exception : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] 발행 Exception : PARAMS-", dbMap.toString()), e);
				}
			}
			// 오류 발생시 종료
			wholecakeDto.setSuccess_coupon_list(new ArrayList<String>());
			return new ArrayList<CouponPublicationListDto>();
		} finally {
			try {
				msrSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " 발행  트랜젝션 종료 실패 : ", ee.getMessage()), ee);
				}
			}
		}

		// 메일 발송 요청 등록 - 메일 발송 실패하여도 전체로직은 성공.
		SqlMapClient msrMailSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		try {
			msrMailSqlMap.startTransaction();

			String mail = StringUtils.defaultIfEmpty(wholecakeDto.getUser_email(), memberDto.getEmail()); // 1. 홀케익 메일, 2. MSR 계정 메일
			if (StringUtils.isEmpty(mail)) {
				throw new XOException("email 값 없음.");
			}

			// 메일 본문 생성
			Calendar startCal  = DateUtil.getCalendar(cmDto.getCoupon_start_date());
			Calendar endCal    = DateUtil.getCalendar(cmDto.getCoupon_end_date());
			String   startDate = DateTime.getFormatString(startCal.getTime(), "yyyy-MM-dd");
			String   endDate   = DateTime.getFormatString(endCal.getTime(),   "yyyy-MM-dd");

			StringBuffer mailArgs = new StringBuffer();
			mailArgs.append(memberDto.getUser_id());	// 사용자 아이디
			mailArgs.append("|");
			mailArgs.append(startDate);					// 발급일
			mailArgs.append("|");
			mailArgs.append(startDate);					// 유효기간 시작일
			mailArgs.append("|");
			mailArgs.append(endDate);					// 유효기간 종료일
			mailArgs.append("|");
			mailArgs.append(couponList.size());			// 발급 쿠폰 갯수

			// 메일 제목
			String mailTitle = XOUtil.getPropertiesString("coupon.wholecake.mail.title",  new String[]{wholecakeDto.getUser_id()});

			// 메일 발송
			SendMailQueueDto mailDto = new SendMailQueueDto();
			mailDto.setUser_id     (memberDto.getUser_id());
			mailDto.setTemplate    (cmDto.getCoupon_policy_code());
			mailDto.setMail_args   (mailArgs.toString());
			mailDto.setMail_title  (mailTitle);
			mailDto.setEmail       (mail);
			mailDto.setStr_reg_date(DateTime.getCurrentDate(13)); // yyyyMMddHHmmss
			this.mailMgr.insertSendMailQueue(msrMailSqlMap, mailDto);
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 트랜젝션 종료
			msrMailSqlMap.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "이메일발송 실패 : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " 이메일발송  Exception "), e);
				}
			}
		} finally {
			try {
				msrMailSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " 이메일발송  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
				}
			}
		}

		return couponList;
	}

	/**
	 * MMS 쿠폰 발행 (MSR 비회원)
	 * 
	 * @param wholecakeDto
	 * @return
	 */
	private List<CouponPublicationListDto> publishMMSCoupon(WholecakeOrderDto wholecakeDto, CouponMasterDto cmDto) {
		List<CouponPublicationListDto> couponList = new ArrayList<CouponPublicationListDto>(); // 쿠폰 발행 목록
		Map<String, Object> dbMap = new HashMap<String, Object>();
		String processStep = "";

		SqlMapClient msrSqlMap1 = IBatisSqlConfig.getMsrSqlMapInstance();
		try {
			msrSqlMap1.startTransaction();

			// 쿠폰번호/선물번호 채번
			processStep = "쿠폰번호/선물번호 채번";
			dbMap.clear();
			dbMap.put("couponCode", cmDto.getCoupon_code());
			dbMap.put("couponCnt", wholecakeDto.getTotal_coupon_count()); // 발행해야 하는 쿠폰 개수 (상품수*정책에 정의된 케익당 쿠폰 발행 수)
			couponList = this.couponMgr.getCouponNumberListForGift(msrSqlMap1, dbMap);
			if (couponList.size() != wholecakeDto.getTotal_coupon_count()) {
				throw new XOException(oUtil.concatString(logSb, "쿠폰 번호 채번 오류 "));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 쿠폰 발행 내역에 등록 (insert all)
			processStep = "쿠폰 발행 내역에 등록 (insert all)";
			for (CouponPublicationListDto dto : couponList) {
				dto.setUser_number           (cmDto.getSck_user_number());	// msr비회원이므로 관리자 계정 번호
				dto.setStr_reg_date          (DateTime.getCurrentDate(13)); // 현재일시 - yyyyMMddHHmmss
				dto.setCoupon_policy_code    (cmDto.getCoupon_policy_code());
				dto.setCoupon_publication_seq(cmDto.getCoupon_publication_seq());
				dto.setValid_day             (cmDto.getValid_day());
				dto.setExpire_start_date     (DateUtil.getDate(cmDto.getCoupon_start_date()));
				dto.setExpire_end_date       (DateUtil.getDate(cmDto.getCoupon_end_date()));
				dto.setGift_send_cnt         (cmDto.getGift_send_max_cnt() - 1); //
				dto.setGift_mms_retry_cnt    (cmDto.getGift_mms_retry_max_cnt());
				dto.setGift_discard_cnt      (cmDto.getGift_discard_max_cnt());
				dto.setGift_status           ("1"); // 쿠폰선물상태{0-선물가능, 1-쿠폰전송, 2-쿠폰등록, 9-선물불가}
				wholecakeDto.getSuccess_coupon_list().add(dto.getCoupon_number());
			}
			dbMap.clear();
			dbMap.put("list_size", couponList.size());
			int publishCnt = this.couponMgr.insertCouponPublicationListMulti(msrSqlMap1, couponList);
			if (publishCnt != couponList.size()) {
				throw new XOException(oUtil.concatString(logSb, "ㅇㅇㅇㅇ"));
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

			// 트랜젝션 종료
			msrSqlMap1.commitTransaction();
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] 발행 실패 : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", processStep, "] 발행 Exception : PARAMS-", dbMap.toString()), e);
				}
			}

			// 오류 발생시 종료
			return new ArrayList<CouponPublicationListDto>();
		} finally {
			try {
				msrSqlMap1.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " 발행  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
				}
			}
		}

		// MMS 발송
		// 이미지 생성
		String   imgTitle        = XOUtil.getPropertiesString("coupon.wholecake.img.title");
		String   rootPath        = XOUtil.getPropertiesString("coupon.root.path");
		String   backImgPath     = XOUtil.getPropertiesString("coupon.wholecake.back.img.path",    new String[]{rootPath});
		String   defaultImgPath  = XOUtil.getPropertiesString("coupon.wholecake.default.img.path", new String[]{rootPath});
		String   imgSavePath     = XOUtil.getPropertiesString("coupon.wholecake.img.save.path",    new String[]{rootPath});
		// MMS 문구 생성
		String   subject         = XOUtil.getPropertiesString("mms.wholecake.coupon.title");	// MMS 제목
		String   callback        = XOUtil.getPropertiesString("mms.sck.callback");				// MMS 발신자
		for (CouponPublicationListDto couponDto : couponList) {
			String giftCpnNumber = couponDto.getGift_send_coupon_num();
			
			SqlMapClient xoSqlMap  = IBatisSqlConfig.getXoSqlMapInstance();
			SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			try {
				xoSqlMap.startTransaction();
				msrSqlMap.startTransaction();
				
				processStep = "MMS 이미지 생성";
				String   couponStartDate = DateUtil.getDateString(couponDto.getExpire_start_date(), "yyyy-MM-dd");
				String   couponEndDate   = DateUtil.getDateString(couponDto.getExpire_end_date(),   "yyyy-MM-dd");
				String   propContents    = XOUtil.getPropertiesString("coupon.wholecake.img.contents", new String[]{couponStartDate, couponEndDate});
				String[] contents        = StringUtils.split(propContents, ";");
				String   imgName         = XOUtil.getPropertiesString("coupon.wholecake.img.name",     new String[]{wholecakeDto.getOrder_no(), giftCpnNumber});
				// VO 담기
				MMSImageVO imgVO = new MMSImageVO();
				imgVO.setImgName         (imgName);									// 이미지 파일 명 (예약번호_쿠폰번호.jpg)
				imgVO.setImgTitle        (imgTitle);								// 이미지 제목
				imgVO.setImgSubTitle     (cmDto.getUser_coupon_name());				// 쿠폰명
				imgVO.setImgContents     (contents);								// 이미지 항목 내용
				imgVO.setBackImgPath     (backImgPath); 							// 배경 이미지 경로
				imgVO.setImgBasePath     (imgSavePath);  							// MMS 이미지 저장 경로
				imgVO.setContentsImgPath (defaultImgPath);           	  			// 쿠폰 이미지 경로
				imgVO.setBarcode         (couponDto.getGift_send_coupon_num());		// 쿠폰선물전송번호
				MMSImageVO mmsImgVO = this.cImage.getImageForMMS(imgVO, this.loggerTitle);
				if(!mmsImgVO.getIsSuccess()){
					throw new XOException("MMS 이미지 생성 실패");
				}
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

				processStep = "MMS 이미지 파일 정보 등록";
				EmMmtFileDto mmsFileDto   = new EmMmtFileDto();
				mmsFileDto.setAttach_file_subpath(StringUtils.replace(mmsImgVO.getImgPath(), File.separator, "/"));		// 이미지 경로
				mmsFileDto.setAttach_file_name   (mmsImgVO.getImgName());		// 이미지 파일 명
				long attachFileGroupKey   = this.mmsMgr.insertEmMmtFile(xoSqlMap, mmsFileDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}

				processStep = "MMS 발송 요청 등록";
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
				String[] contentArgs      = {"",                          // 쿠폰 내용
				                            contentProd,                  // 이용 가능 상품
				                            contentNoti,                  // 사용시 주의사항
				                            contentCpnName,               // e-쿠폰명			
				                            contentCpnDate,               // 쿠폰 유효 기간
				                            contentCpnNumber,             // 쿠폰 번호
				                            couponRegLink                 // e-쿠폰 등록 바로가기
				                            };
				String   content          = XOUtil.getPropertiesString("mms.wholecake.coupon.contents", contentArgs);
				SmtTranDto mmsDto         = new SmtTranDto();
				mmsDto.setSubject              (subject);
				mmsDto.setContent              (content);
				mmsDto.setAttach_file_group_key(attachFileGroupKey);
				mmsDto.setCallback             (callback);
				mmsDto.setRecipient_num        (wholecakeDto.getUser_mobile());
				mmsDto.setService_type         ("2");									// 서비스 메시지 전송 타입 {2-MMS MT, 3-LMS}
				mmsDto.setXo_order_no          (wholecakeDto.getOrder_no()); 
				mmsDto.setUser_id(wholecakeDto.getUser_id());	// 사용자아이디
				long mtPr = this.mmsMgr.insertEmMmtTran(xoSqlMap, mmsDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				processStep = "쿠폰 선물 이력 등록";
				CouponGiftHistoryDto cghDto = new CouponGiftHistoryDto();
				cghDto.setCoupon_number       (couponDto.getCoupon_number());
				cghDto.setGift_send_coupon_num(couponDto.getGift_send_coupon_num());
				cghDto.setHistory_code        ("1");									// 구분코드{1-선물,2-재전송,3-회수,4-재선물,5-재회수,6-등록,7-등록취소}
				cghDto.setSend_user_number    (cmDto.getSck_user_number());				// 보낸 사용자 고유번호 : msr비회원이므로 관리자 계정 번호
				cghDto.setGift_send_mobile_num(wholecakeDto.getUser_mobile());			// 선물전송휴대전화번호
				cghDto.setMms_title           (subject);
				cghDto.setMms_message         ("");
				cghDto.setMms_img_1           (imgVO.getImgPath());
				cghDto.setMms_seq             (mtPr);
				cghDto.setGift_method         ("S");									// 선물방식{W-WEB, P-POS, A-APP, S-ADMIN, M-MOBILE}
				long giftHistNo = this.couponMgr.inserMsrCouponGiftHistory(msrSqlMap, cghDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				processStep = "쿠폰 발행 정보 업데이트";
				couponDto.setGift_hist_no(giftHistNo);
				this.couponMgr.updateCouponPublicationList(msrSqlMap, couponDto);
				if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, this.loggerTitle, processStep));}
				
				// 트랜잭션 종료
				xoSqlMap.commitTransaction();
				msrSqlMap.commitTransaction();
			} catch (Exception e) {
				wholecakeDto.getFail_coupon_list().add(couponDto.getCoupon_number());	// MMS 발송 실패
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "[", couponDto.getCoupon_number(), "] MMS 발송 Exception : ", e.getMessage()), e);
				}
			} finally {
				try {
					xoSqlMap.endTransaction();
					msrSqlMap.endTransaction();
				} catch (Exception ee) {
					if (logger.isErrorEnabled()) {
						logger.error(oUtil.concatString(logSb, this.loggerTitle, " MMS 발송  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
					}
				}
			}
		} // for 

		return couponList;
	}

	/**
	 * 쿠폰 발행 후 처리
	 * @param orderNoList 쿠폰이 발행된 예약 번호
	 * @param couponList  발행된 쿠폰 목록
	 */
	private void publichCouponAfterProcess(List<String> orderNoList, List<CouponPublicationListDto> couponList) {
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		// 홀케익 예약의 쿠폰 발행 여부 업데이트
	    SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			xoSqlMap.startTransaction();
			List<String> updateList  = new ArrayList<String>(); // 100건 별 담을 예약 목록
			long allUpdateCnt = 0;
			
			// 100건 당 예약 업데이트
			for (int i = 0; i < orderNoList.size() ; i++){
				updateList.add(orderNoList.get(i));
				
				if(updateList.size() >= 100 || i == (orderNoList.size()-1)){
					dbMap.put("orderNoList", updateList);
					long orderCnt = this.xoWholecakeOrderMgr.updateWholeCakeOrderForCouponPubFlag(xoSqlMap, dbMap);
					if(logger.isDebugEnabled()){
						logger.debug(oUtil.concatString(logSb, this.loggerTitle, "홀케익 예약 정보 업데이트 - ", orderCnt, "건"));
					}
					allUpdateCnt += orderCnt;
					updateList.clear();
				}
			}
			
			// 총 예약 건수
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "전체 홀케익 예약 정보 업데이트 - ", allUpdateCnt, "건"));
			}

			// 트랜젝션 종료
			xoSqlMap.commitTransaction();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "홀케익예약 정보 업데이트 Exception :", e.getMessage()), e);
			}
		} finally {
			try {
				xoSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "홀케익예약 정보 업데이트  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
				}
			}
		}
	
		// 영업정보에 쿠폰 등록
	    SqlMapClient scksaSqlMap = IBatisSqlConfig.getScksasqlMapInstance();
		try {
			scksaSqlMap.startTransaction();
			List<CouponPublicationListDto> insertList  = new ArrayList<CouponPublicationListDto>(); // 100건 별 담을 쿠폰 정보
			long allInsertCnt = 0;
			
			// 100건 당 쿠폰 등록
			for (int i = 0; i < couponList.size() ; i++){
				insertList.add(couponList.get(i));
				
				if(insertList.size() >= 100 || i == (couponList.size()-1)){
					long couponCnt = this.couponMgr.insertSmkCoupSeqMulti(scksaSqlMap, insertList);
					if(logger.isDebugEnabled()){
						logger.debug(oUtil.concatString(logSb, this.loggerTitle, "영업정보에 쿠폰 등록 - ", couponCnt, "건"));
					}
					allInsertCnt += couponCnt;
					insertList.clear();
				}
			}
			
			// 총 쿠폰 건수
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "전체 영업정보에 쿠폰 등록 - ", allInsertCnt, "건\n"));
			}

			// 트랜젝션 종료
			scksaSqlMap.commitTransaction();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "영업정보에 쿠폰 등록  Exception", e.getMessage()), e);
			}
		} finally {
			try {
				scksaSqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, "영업정보에 쿠폰 등록  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
				}
			}
		}		
	}
	
	// 홀케익 예약 감사 무료음료 쿠폰 발송 종료 결과 SMS 발송
	private void smsSend(Object[] params) {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // 사이렌오더 트렌젝션
		
		try {
			sqlMap.startTransaction();
			
			Configuration conf = CommPropertiesConfiguration.getConfiguration("sms.properties");
			String content = conf.getString("wholecake.sms.coupon.msg");
			content = MessageFormat.format(content, params);
			
			String callback = conf.getString("wholecake.sms.callback");
			String recipientNum = conf.getString("wholecake.sms.receive.info");
			String recipientArr[] = recipientNum.split("\\|");
			
	        // SMS 발송 요청 
			for(int i = 0; i < recipientArr.length; i++) {
		        SmtTranDto smtTranDto = new SmtTranDto ();
		        smtTranDto.setPriority ("S"); //전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
		        smtTranDto.setContent (content);
		        smtTranDto.setCallback (callback);
		        smtTranDto.setRecipient_num (recipientArr[i]);

		        this.mmsMgr.insertSmtTran (sqlMap, smtTranDto);
			}
			sqlMap.commitTransaction (); // commit
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, " 쿠폰발행 결과 SMS 발송  Exception "), e);
			}
		} finally {
			try {
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, this.loggerTitle, " 쿠폰발행 결과 SMS 발송  트랜젝션 종료 Exception : ", ee.getMessage()), ee);
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
