/*
 * @(#) $Id: WholecakeOrderConfirmNoti.java,v 1.9 2017/08/16 07:06:40 jylee Exp $
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderNotiDto;
import co.kr.istarbucks.xo.batch.common.util.AES128;
import co.kr.istarbucks.xo.batch.common.util.DateUtil;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.OnmUtil;
import co.kr.istarbucks.xo.batch.common.util.XOUtil;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.MMSMgr;
import co.kr.istarbucks.xo.batch.mgr.PushMgr;
import co.kr.istarbucks.xo.batch.mgr.XoWholecakeOrderMgr;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 홀케익 예약 확인 알림 - WholecakeOrderConfirmNoti. 
 * 홀케익 예약 취소 가능일 달일에 예약 확인 알림 LMS 발송
 * 
 * @author eZEN ksy
 * @since 2016. 11. 09.
 * @version $Revision: 1.9 $
 */
public class WholecakeOrderConfirmNoti {

	private final Log    logger      = LogFactory.getLog("WHOLECAKE_ORDER_CONFIRM_NOTI");
	private final OnmUtil      oUtil       = new OnmUtil();
	private final String loggerTitle;
	private final StringBuffer logSb;

	private final XoWholecakeOrderMgr xoWholecakeOrderMgr;  // 홀케익 정보
	private final MMSMgr              mmsMgr;               // MMS 발송 요청
	private final PushMgr             pushMgr;			  // PUSH 발송 요청

	private static final int BATCH_DELAY_TIME = 1000; // 1 sec.
	
	public WholecakeOrderConfirmNoti() {
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mmsMgr              = new MMSMgr();
		this.pushMgr             = new PushMgr();
		this.logSb               = new StringBuffer();				 // log용 StringBuffer
		this.loggerTitle         = " [wholecake][confirmNoti] ";
	}

	/**
	 * 홀케익 예약에 대한 쿠폰 발행 프로세스.
	 * @param revocableDate
	 */
	public void start(String revocableDate) {
		long startTime = System.currentTimeMillis();
		
		if (logger.isInfoEnabled()) {
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "START"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "취소가능일\t\t\t: ", StringUtils.defaultIfEmpty(revocableDate, DateUtil.getToday()) ));
		}
		
		// 예약 확인 LMS 발송 대상 홀케익 예약 조회 ====================================================
		List<WholecakeOrderDto> wholecakeOrderList = getWholeCakeOrderList(revocableDate);
		if(wholecakeOrderList == null){
			wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		}
		
		List<String> successList   = new ArrayList<String>(); // LMS 발송 성공한 예약 번호
		List<String> failList      = new ArrayList<String>(); // LMS 발송 실패한 예약 번호
		boolean      isSuccess     = false;
		int          cnt           = 1;
		
		// LMS 발송 처리 ==============================================================
		for(WholecakeOrderDto dto : wholecakeOrderList){
			// LMS 발송
			isSuccess = this.sendOrderConfirmLms(dto);
			if(isSuccess){
				successList.add(dto.getOrder_no());
			} else {
				failList.add(dto.getOrder_no());
			}
			
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "[LMS] ", cnt++, ". 예약정보: ", dto.getOrder_no(), "|01*****", StringUtils.right(dto.getUser_mobile(), 4), "|", dto.getUser_id(), ", \t result - ", isSuccess));
			}
		}
		
		// PUSH 발송 처리 ============================================================
		if(logger.isInfoEnabled()){
			logger.info(oUtil.concatString(logSb, this.loggerTitle));
		}
		boolean isPushSuccess = sendPush(wholecakeOrderList);
		
		// 결과 로그 출력
		if (logger.isInfoEnabled()) {
			logger.info(oUtil.concatString(logSb, this.loggerTitle));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "대상 예약 건수 \t\t: ", wholecakeOrderList.size(), "건"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "LMS 발송 성공 \t\t: "  , successList.size(), "건"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "LMS 발송 실패 \t\t: "  , failList.size(), "건"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "PUSH 성공 여부 \t\t: " , isPushSuccess));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "END : ", (System.currentTimeMillis() - startTime), "ms"));
		}
	}
	
	/**
	 * 홀케익 예약 확인 LMS 발송 대상 조회
	 * @param revocableDate
	 * @return
	 */
	private List<WholecakeOrderDto> getWholeCakeOrderList(String revocableDate) {
		List<WholecakeOrderDto> wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		try {
			if (StringUtils.isNotEmpty(revocableDate)) {
				dbMap.put("revocableDate", revocableDate);
			}
			wholecakeOrderList = this.xoWholecakeOrderMgr.getWholeCakeOrderListForLms(xoSqlMap, dbMap);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "홀케익 예약 확인 LMS 발송 대상 조회 실패 : PARAMS-", dbMap.toString()), e);
			}
		} finally {
			dbMap.clear();
		}
		
		return wholecakeOrderList;
	}
	
	/**
	 * LMS 발송
	 * @param wholecakeDto
	 * @return
	 */
	private boolean sendOrderConfirmLms(WholecakeOrderDto wholecakeDto) {
		boolean      returnBoo      = false;
		String       processStep    = "";
		String       methodLogTitle = oUtil.concatString(logSb, this.loggerTitle, "[", wholecakeDto.getOrder_no(), "]");
		SqlMapClient sqlMap         = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			// 트랜젝션 시작
			sqlMap.startTransaction();
			
			// LMS 발송 요청
			processStep = "LMS 발송 요청";
			String   subject              = XOUtil.getPropertiesString("mms.wholecake.confirm.title");
			String   contentSku           = "";
			if(wholecakeDto.getTotal_qty() > 1){
				contentSku = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.sku", new String[]{wholecakeDto.getOrder_name(), String.valueOf((wholecakeDto.getTotal_qty() - 1)), "개"});
			} else {
				contentSku = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.sku", new String[]{wholecakeDto.getOrder_name(), "", ""});
			}
			String   contentReceiveDate   = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.receivedate",   new String[]{wholecakeDto.getReceive_date()});
			String   contentReceiveStore  = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.receivestore",  new String[]{wholecakeDto.getReceive_store_name(), wholecakeDto.getReceive_store_cd()});
			String   contentRevocableDate = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.revocabledate", new String[]{wholecakeDto.getRevocable_date()});
			String   encOrderNo           = AES128.encryptUrlEncode(wholecakeDto.getOrder_no());
			String   contentGoOrderInfo   = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.orderinfo",     new String[]{encOrderNo});
			String[] contentArgs          = {wholecakeDto.getUser_name(),  // 고객명
					                         contentSku,                   // 품목정보
					                         contentReceiveDate,           // 수령일
					                         contentReceiveStore,          // 수령매장			
					                         contentRevocableDate,         // 취소가능일
					                         contentGoOrderInfo,           // 예약 수정 링크
			                                };
			String   content          = XOUtil.getPropertiesString("mms.wholecake.confirm.contents", contentArgs);
			String   callback         = XOUtil.getPropertiesString("mms.sck.callback");
			SmtTranDto mmsDto         = new SmtTranDto();
			mmsDto.setSubject      (subject);
			mmsDto.setContent      (content);
			mmsDto.setCallback     (callback);
			mmsDto.setRecipient_num(wholecakeDto.getUser_mobile());
			mmsDto.setService_type ("3");									// 서비스 메시지 전송 타입 {2-MMS MT, 3-LMS}
			mmsDto.setXo_order_no  (wholecakeDto.getOrder_no()); 
			long mtPr = this.mmsMgr.insertEmMmtTran(sqlMap, mmsDto);
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, methodLogTitle, processStep));}
			
			// 홀케익 예약 상태 알림 등록
			processStep = "홀케익 예약 상태 알림 등록";
			WholecakeOrderNotiDto notiDto = new WholecakeOrderNotiDto();
			notiDto.setOrder_no (wholecakeDto.getOrder_no());
			notiDto.setStatus   (wholecakeDto.getStatus());
			notiDto.setNoti_type("L");							// 알림 타입{M-MMS, L-LMS, S-SMS, E-EMAIL}	
			notiDto.setNoti_user("O");							// 알림 대상{O-예약자, P-선물수신자}
			notiDto.setMt_pr    (mtPr);
			int insertCnt = this.xoWholecakeOrderMgr.insertWholecakeOrderNoti(sqlMap, notiDto);
			if (insertCnt != 1) {
				if(logger.isInfoEnabled()){
					logger.info(oUtil.concatString(logSb, methodLogTitle, " 홀케익 예약 상태 알림 등록 실패!!"));
				}
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, methodLogTitle, processStep));}
			
			// 트랜젝션 종료
			sqlMap.commitTransaction();
			returnBoo = true;
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, "[", processStep, "] 발행 실패 : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, "[", processStep, "] 발행 실패 "), e);
				}
			}
			returnBoo = false;
		} finally {
			try {
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, " 발행  트랜젝션 종료 실패 : ", ee.getMessage()), ee);
				}
			}
		}
		return returnBoo;
	}
	
	/**
	 * PUSH 발송
	 * @param wholecakeList
	 * @return
	 */
	private boolean sendPush(List<WholecakeOrderDto> wholecakeList) {
		boolean      returnBoo      = false;
		String       methodLogTitle = oUtil.concatString(logSb, this.loggerTitle, "[PUSH]");
		int          cnt            = 1;
		SqlMapClient xoSqlMap       = IBatisSqlConfig.getXoSqlMapInstance();
		SqlMapClient sqlMap         = IBatisSqlConfig.getPushMapInstance();	
		Map<String, Object> dbMap   = new HashMap<String, Object>();
		int          workCnt        = 0;
		
		try {
			// 트랜젝션 시작
			xoSqlMap.startTransaction();
			xoSqlMap.startBatch();
			
			sqlMap.startTransaction();
			sqlMap.startBatch();
			
			// PUSH 발송 요청
			String pushMsg      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.msg");
			String screeId      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.screenid");
			String chGubun      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.chgubun");       // 채널 구분(PU값 고정)
			int    appGrpId     = Integer.parseInt(XOUtil.getPropertiesString("push.appgrpid"));           // TMS에서 발급한 앱 그룹 ID 개발은 125고정, 상용은 127
			String priorityFlag = XOUtil.getPropertiesString("push.wholecake.confirm.noti.priorityflag");  // 긴급발송여부
			String msgTpCd      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.msgtpcd");       // 메시지유형
			String pushType    =  XOUtil.getPropertiesString("push.type.mp");       // 메시지 타입
			
			for(WholecakeOrderDto dto : wholecakeList){
				// 유효성 확인
				if(!StringUtils.equals(dto.getOs_type(), "1") && !StringUtils.equals(dto.getOs_type(), "2")){
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, methodLogTitle, " 발송 제외 - ORDER_NO: ", dto.getOrder_no(), ", USER_ID : ", dto.getUser_id(), ", OS_TYPE [", dto.getOs_type(), "] "));
					}
					continue;
				}
				if(StringUtils.isEmpty(dto.getPush_id()) || StringUtils.equals(dto.getPush_id(), "0")){
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, methodLogTitle, " 발송제외 - ORDER_NO: ", dto.getOrder_no(), ", USER_ID : ", dto.getUser_id(), ", PUSH_ID [", dto.getPush_id(),"]"));
					}
					continue;
				}
				
				// 데이타 셋팅
				String pushValue = ""; // Push 랜딩 URL
				String orderNo = dto.getOrder_no();
				
				if(StringUtils.isNotEmpty(screeId)) {
					pushValue = "screenId=" + screeId;
				}

				if(StringUtils.isEmpty(pushValue) && StringUtils.isNotEmpty(orderNo) ) {
					pushValue = "orderNo=" + orderNo;
				} else if(StringUtils.isNotEmpty(pushValue) && StringUtils.isNotEmpty(orderNo) ) {
					pushValue = pushValue + "&orderNo=" + orderNo;
				}
				
				boolean insertXoTmsQueueOk = false;    // 사이렌오더 DB에 PUSH 데이터 정상 등록 여부 
				boolean insertTmsQueueOk   = false;    // TMS DB에 PUSH 데이터 정상 등록 여부
				
				dbMap.clear();
				dbMap.put("chGubun",         chGubun);          // PU 고정
				dbMap.put("msgTpCd",         msgTpCd);          // 메시지 코드
				dbMap.put("priorityFlag",    priorityFlag);     // 긴급발송여부
				dbMap.put("toId",            dto.getUser_id()); // 사용자 아이디
				dbMap.put("content",         pushMsg);          // push 메시지
				dbMap.put("appGrpId",        appGrpId);         // TMS 에서 발급한 앱 그룹 아이디 
				dbMap.put("pushInsertTable", "TPQ");            // TPQ-개인화대용량
				dbMap.put("serverType",      "SOBAT");          // serverType {SOPOS : 사이렌오더 POS, SOBAT : 사이렌오더 Batch, MSRAPP : 스타벅스 APP, MSRBAT : MSR Batch, MSRADM : MSR관리자페이지}
				dbMap.put("pushValue",       pushValue);
				dbMap.put("pushType",        pushType);         // MP:푸시 발송 및 메시지함 적재, OP:푸시 발송, OM:메시지함 적재
				
				// 홀케익 예약 상태 알림 등록(XO PUSH 발송 요청 테이블에 등록)
				try {
					this.xoWholecakeOrderMgr.insertWholecakeOrderPush(xoSqlMap, dbMap);
					insertXoTmsQueueOk = true;
					
					// XO_TMS_QUEUE 등록 후 설정
					// 2017-07-26 요청으로 인해 수정
					Thread.sleep(50);
				} catch (Exception e) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt + ". 예약정보: ", dto.getOrder_no(), "|", dto.getUser_id(), "|XO_TMS_QUEUE 푸쉬 등록 실패"));
				}
				
				String reqUid = (String) dbMap.get("reqUid"); // 메시지번호
				
				try {
					this.pushMgr.insertTmsPersonQueue(sqlMap, dbMap);
					insertTmsQueueOk = true;
				} catch (Exception e) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt + ". 예약정보: ", dto.getOrder_no(), "|", dto.getUser_id(), "|TMS_PERSON_QUEUE 푸쉬 등록 실패"));
				}
				
				// 솔루션 DB 테이블 등록 확인 업데이트 
				if(insertXoTmsQueueOk && insertTmsQueueOk) {
					this.xoWholecakeOrderMgr.updateWholecakeOrderPush(xoSqlMap, dbMap);
				}
				
				workCnt++;
				
				// 100건당 커밋 처리
				if (workCnt % 100 == 0) {
					xoSqlMap.executeBatch();
					sqlMap.executeBatch();

					workCnt = 0;
					
					xoSqlMap.commitTransaction();
					sqlMap.commitTransaction();
					Thread.sleep(BATCH_DELAY_TIME);
				}
				
				if(insertXoTmsQueueOk && insertTmsQueueOk) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt++, ". 예약정보: ", dto.getOrder_no(), "|", dto.getUser_id(), "|", dto.getOs_type(), "|", dto.getPush_id(), ", \t REQ_UID - ", reqUid));
				} else {
					cnt++;
				}
			}

			// 트랜젝션 종료
			xoSqlMap.commitTransaction();
			sqlMap.commitTransaction();
			
			xoSqlMap.commitTransaction();
			sqlMap.commitTransaction();
			returnBoo = true;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, methodLogTitle, " 실패 "), e);
			}
			returnBoo = false;
		} finally {
			try {
				xoSqlMap.endTransaction();
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, " 트랜젝션 종료 실패 : ", ee.getMessage()), ee);
				}
			}
		}
		return returnBoo;
	}	
	
	public static void main(String[] args) {
		if (args.length > 0) {
			new WholecakeOrderConfirmNoti().start(args[0]);
		} else {
			new WholecakeOrderConfirmNoti().start("");
		}
	}
}
