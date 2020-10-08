/*
 * @(#) $Id: EGiftItemExpirationNotice.java,v 1.4 2018/10/04 01:21:27 resin Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017년도 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.inbox.InboxType;
import co.kr.istarbucks.xo.batch.common.dto.inbox.UserInboxDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.EGiftItemExpirationNoticeXoMgr;
import co.kr.istarbucks.xo.batch.mgr.InboxMgr;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;
import co.kr.istarbucks.xo.batch.mgr.PushMgr;

public class EGiftItemExpirationNotice {

	private final EGiftItemExpirationNoticeXoMgr eGiftItemExpirationNoticeXoMgr;
	private final InboxMgr inboxMgr;
	private final PushMgr pushMgr;
	private final PaymentCancelXoMgr paymentCancelXoMgr;
	private final String loggerTitle;
	private final Configuration noticeConf;
	private final Configuration inboxConf;
	private final Logger gLogger = Logger.getLogger ("GIFT_NOTICE");
	
    public EGiftItemExpirationNotice () {
        this.eGiftItemExpirationNoticeXoMgr = new EGiftItemExpirationNoticeXoMgr ();
        this.inboxMgr = new InboxMgr();
        this.pushMgr = new PushMgr();
        this.paymentCancelXoMgr = new PaymentCancelXoMgr ();
        this.loggerTitle = "[EGiftItemExpirationNotice] ";
        this.noticeConf = CommPropertiesConfiguration.getConfiguration ("notice.properties");
        this.inboxConf =  CommPropertiesConfiguration.getConfiguration("inbox.properties");
    }
	
	public void batchStart(String thisDate){
		gLogger.info(loggerTitle + " START");
		
		gLogger.info("[TYPE A] 수신자 선물 등록 : O, 알림 대상 : 수신자, 알림 수단 : PUSH/INBOX");
		gLogger.info("[TYPE B] 수신자 선물 등록 : X, 선물 전송 수단 : LMS, 알림 대상 : 수신자, 알림 수단 : LMS");
		gLogger.info("[TYPE C] 수신자 선물 등록 : X, 선물 전송 수단 : 카톡/라인, 알림 대상 : 구매자, 알림 수단 : PUSH/INBOX");
		
		// 대상 조회
		if("".equals(thisDate))
		{
			// 기본 Batch
			String date = noticeConf.getString("notice.dates");		
			String [] noticeDates = date.split("\\|");
			
			for(String forDate : noticeDates)
			{
				try{
					gLogger.info("START NOTICE DATE ["+forDate+"]");
					this.noticeProcess(thisDate, forDate);
					gLogger.info("");
				}catch (Exception e) {
					gLogger.info(e);
				}
			}
			gLogger.info("END NOTICE");
			gLogger.info("End");
		}
		else
		{
			// 특정일
			try{
				gLogger.info("START DATE ["+thisDate+"]");
				this.noticeProcess(thisDate, "");
				gLogger.info("END NOTICE");
			}catch (Exception e) {
				gLogger.info(e);
			}
		}
	}
	
	public void noticeProcess(String thisDate, String noticeDate) throws Exception {
		
		Map<String, Object> dbMap = new HashMap<String, Object>();
		dbMap.put("thisDate", thisDate);
		dbMap.put("noticeDate", noticeDate);
		
		/**
		 * A : 수신자가 선물 등록 완료 후 만기 알림  PUSH와 IBNOX를 수신자에게 보냄
		 * B : 수신자 미 등록 선물로 LMS를 통해 발송 한 경우 LMS를 수신자에게 보냄
		 * C : 수신자 미 등록 선물로 카톡 or 라인으로 발송 한 경우  PUSH와 IBNOX를 구매자에게 보냄
		 */
		// 1. 수신자 기준 inBox와 push 발행 로직 수행
		dbMap.put("noticeType", "A");
		List<Map<String, String>> targetList = eGiftItemExpirationNoticeXoMgr.getEGiftItemExpirationNoticeTarget(dbMap);
		
		gLogger.info("NOTICE TYPE A SIZE : "+targetList.size());		
		gLogger.info(targetList.toString());
		if(targetList.size() > 0)
		{
			inboxProcess(targetList, noticeDate);
			pushProcess(targetList, noticeDate);
		}
		
		// 2. 수신자 기준 LMS 발행 로직 수행
		dbMap.put("noticeType", "B");
		targetList = eGiftItemExpirationNoticeXoMgr.getEGiftItemExpirationNoticeTarget(dbMap);
		
		gLogger.info("NOTICE TYPE B SIZE : "+targetList.size());		
		gLogger.info(targetList.toString());
		if(targetList.size() > 0)
		{
			lmsProcess(targetList, noticeDate);
		}
		
		
		// 3. 구매자 기준 inBox와 push 발행 로직 수행
		dbMap.put("noticeType", "C");
		targetList = eGiftItemExpirationNoticeXoMgr.getEGiftItemExpirationNoticeTarget(dbMap);
		
		gLogger.info("NOTICE TYPE C SIZE : "+targetList.size());		
		gLogger.info(targetList.toString());
		if(targetList.size() > 0)
		{
			inboxProcess(targetList, noticeDate);
			pushProcess(targetList, noticeDate);
		}
	}
	
	/**
	 * inbox 등록
	 * @param targetList
	 * @throws Exception
	 */
	public void inboxProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {
		
		SqlMapClient homeSqlMap = null;		
		
		try{
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			UserInboxDto dto = null;
			
			int forCnt = 0; 			// 1000번째 Commit 하기
			int insertCnt = 0;			// log 용
			String indoxInserType = "";	// log 용
			StringBuilder inboxTarger = new StringBuilder(); // log 용
			
			homeSqlMap.startTransaction();
			
			for(Map<String, String> forMap : targetList)
			{
				try{
					String noticeType = forMap.get("NOTICE_TYPE");
					indoxInserType = noticeType;
					String targetId = "A".equals(noticeType) ? forMap.get("REG_USER_ID") : forMap.get("USER_ID");
					String giftNo = forMap.get("GIFT_NO");
					String expireDate = forMap.get("EXPIRE_DATE");
					
					inboxTarger.append("[").append(targetId).append("]");
					
					if(StringUtils.isNotEmpty(noticeDate) && forCnt == 0)
					{
						gLogger.info("EXPIRE_DATE ["+expireDate+"]");
					}
					
					Object[] inboxType = "A".equals(noticeType) ? InboxType.E_GIFT_ITEM.RECEIVE_NOTICE : InboxType.E_GIFT_ITEM.SEND_NOTICE;
					
					if(inboxType == null) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{inboxType}");
					}
					
					String type = (String) inboxType[0];
					Boolean isApp = (Boolean) inboxType[1];
					Boolean isWeb = (Boolean) inboxType[2];
					
					dto = new UserInboxDto();
					
					if(StringUtils.isBlank(targetId)) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{userId}");
					}
					
					if(inboxType.length != 3) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{length}");
					}
					
					if(inboxConf == null){
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{inbox.properties}");
					}
					
					String title = StringUtils.defaultIfEmpty(inboxConf.getString("inbox."+type), "");
					
					if(StringUtils.isNotEmpty(noticeDate))
					{
						String resultMsg = title;
						StringBuffer buf = new StringBuffer();
										
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(noticeDate));
						
						title = resultMsg;
					}
					else
					{
						// 특정 날(args []의 파라미터 값) 수행 시 메시지 셋팅
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						title = resultMsg;
					}
					
					if(StringUtils.isBlank(title)) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러 {title}");
					}
					
					dto.setSui_user_id(targetId);
					dto.setSui_title(title.trim());
					
					if(isWeb.booleanValue()) {
						dto.setSui_web_yn("Y");
					} else {
						dto.setSui_web_yn("N");
					}
					
					if(isApp.booleanValue()) {
						dto.setSui_app_yn("Y");
					} else {
						dto.setSui_app_yn("N");
					}
					
					dto.setSui_link_yn("N");
					
					gLogger.info("INBOX INSERT DTO :" + dto.toString());
					
					inboxMgr.insertUserInBoxMsg(homeSqlMap, dto);
					
				/*	if(forCnt % 1000 == 0)
					{
						// 1000 단위로 커밋
						homeSqlMap.commitTransaction();
						
						homeSqlMap.startTransaction();
					}*/
					insertCnt++;
				} catch (Exception e) {
					// 실패 시 이전까지 등록 한 inbox를 커밋 하고, 실패 이력 로그 생성 후 다음 진행
					homeSqlMap.commitTransaction();
					
					gLogger.info("INBOX FAIL -> "+dto.toString()+":"+e.getMessage());
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					PrintStream pinrtStream = new PrintStream(out);
					e.printStackTrace(pinrtStream);
					gLogger.info(out.toString());
					
					homeSqlMap.startTransaction();					
				}
				
				forCnt++;
			} // End Of For List
			
			// 1000단위로 커밋 후 남어지 커밋
			homeSqlMap.commitTransaction();
			
			gLogger.info("Inbox "+indoxInserType+" Type Try Count : "+forCnt+", Insert Count : "+insertCnt);
			gLogger.info("Inbox "+indoxInserType+" Target ID -> "+inboxTarger.toString());
		}catch (Exception e){
			throw e;
		} finally {
			try {				
				homeSqlMap.endTransaction();
			} catch ( Exception ee ) {
				gLogger.error(ee.getMessage(), ee);
			}
		}
	}
	
	/**
	 * push 등록 및 tms 수신함 적재
	 * @param targetList
	 * @throws Exception
	 */
	public void pushProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {

		SqlMapClient pushSqlMap = null;
		SqlMapClient xoSqlMap = null;
		
		try{
			pushSqlMap = IBatisSqlConfig.getPushMapInstance();
			xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			
			int forCnt = 0; 			// 1000번째 Commit 하기
			int insertCnt = 0;			// log 용
			String indoxInserType = "";	// log 용
			StringBuilder inboxTarger = new StringBuilder(); // log 용
			
			pushSqlMap.startTransaction();
			xoSqlMap.startTransaction();
			
			for(Map<String, String> forMap : targetList)
			{
				Map<String, Object> dbMap   = new HashMap<String, Object>();
				
				try{
					String noticeType = forMap.get("NOTICE_TYPE");
					indoxInserType = noticeType;
					String targetId = "A".equals(noticeType) ? forMap.get("REG_USER_ID") : forMap.get("USER_ID");
					String giftNo = forMap.get("GIFT_NO");
					String giftOrderNo = forMap.get("GIFT_ORDER_NO");
					String msgTpCd = "A".equals(noticeType) ? StringUtils.defaultIfEmpty(noticeConf.getString("receive.push.msg.tp.cd"), "GFT03")
															  : StringUtils.defaultIfEmpty(noticeConf.getString("send.push.msg.tp.cd"), "GFT04");
					
					inboxTarger.append("[").append(targetId).append("]");
					
					gLogger.info("NOTICE DATE : " + forMap.get("EXPIRE_DATE") + ", PUSH TARGET ID : " + targetId);
					
					if(StringUtils.isNotEmpty(noticeDate) && forCnt == 0)
					{
						gLogger.info("EXPIRE_DATE ["+forMap.get("EXPIRE_DATE")+"]");
					}
					
					dbMap.put("chGubun",         "PU");					// PU 고정
					dbMap.put("msgTpCd",         msgTpCd);				// 메시지 코드 - 할 당 받은 코드 사용
					dbMap.put("priorityFlag",    "N");					// 긴급발송여부
					dbMap.put("toId",            targetId);				// 고객 식별 번호(로그인 아이디)
					//125 : TMS 에서 발급한 앱 그룹 아이디(개발), 127 : TMS 에서 발급한 앱 그룹 아이디(상용)
					dbMap.put("appGrpId",        "real".equals(noticeConf.getString("notice.service.mode"))? 127 : 125);
					dbMap.put("pushInsertTable", "TPQ");				// TPQ-개인화대용량
					// serverType {SOPOS : 사이렌오더 POS, SOBAT : 사이렌오더 Batch, MSRAPP : 스타벅스 APP, MSRBAT : MSR Batch, MSRADM : MSR관리자페이지}
					dbMap.put("serverType",      "SOBAT");
					// 푸시 타입(MP : 푸시 전송 및 수신함 적재, OP : 푸시 발송, OM : 수신함 적재)
					dbMap.put("pushType",	     "MP");
							
					// push 메시지
					String pushMsg = "A".equals(noticeType) ? noticeConf.getString("typeA.notice.push.msg") : noticeConf.getString("typeC.notice.push.msg");
					
					if(StringUtils.isNotEmpty(noticeDate))
					{
						String resultMsg = pushMsg;
						StringBuffer buf = new StringBuffer();
										
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(noticeDate));
						
						pushMsg = resultMsg;
					}
					else
					{
						// 특정 날(args []의 파라미터 값) 수행 시 메시지 셋팅
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						pushMsg = resultMsg;
					}
					
					dbMap.put("content", pushMsg);
					
					// 웹 URL 또는 앱과 상호 약속된 스키마
					dbMap.put("pushValue", "A".equals(noticeType) ? "screenId=EG-01&giftNo="+giftNo 
							  										: "screenId=EG-02&giftOrderNo="+giftOrderNo);
					
					gLogger.info("PUSH INSERT MAP :" + dbMap.toString());
					
					// 1. XO_TMS_QUEUE 등록
					eGiftItemExpirationNoticeXoMgr.insertXoTmsQueue(xoSqlMap, dbMap);
					
					gLogger.info("PUSH REG_UID : " + dbMap.get("reqUid"));
					
					// 2. TMS_PERSON_QUEUE 등록
					pushMgr.insertTmsPersonQueue(pushSqlMap, dbMap);
					
					// 3. TMS_PERSON_QUEUE 테이블에 정상 등록 되었다면 XO_TMS_QUEUE테이블에 push_insert_yn 컬럼의 값을 'Y' 업데이트
					eGiftItemExpirationNoticeXoMgr.updateXoTmsQueue(xoSqlMap, dbMap);
					/*
					if(forCnt % 1000 == 0)
					{
						// 1000 단위로 커밋
						pushSqlMap.commitTransaction();
						xoSqlMap.commitTransaction();
						
						pushSqlMap.startTransaction();
						xoSqlMap.startTransaction();
					}*/
					insertCnt++;
				} catch (Exception e) {
					// 실패 시 이전까지 등록 한 push를 커밋 하고, 실패 이력 로그 생성 후 다음 진행
					pushSqlMap.commitTransaction();
					xoSqlMap.commitTransaction();
					
					gLogger.info("PUSH FAIL -> "+dbMap.toString()+":"+e.getMessage());
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					PrintStream pinrtStream = new PrintStream(out);
					e.printStackTrace(pinrtStream);
					gLogger.info(out.toString());
					
					pushSqlMap.startTransaction();
					xoSqlMap.startTransaction();
				}
				
				forCnt++;
			} // End Of For List
			
			// 1000단위로 커밋 후 남어지 커밋
			pushSqlMap.commitTransaction();
			xoSqlMap.commitTransaction();
			
			gLogger.info("Push "+indoxInserType+" Type Try Count : "+forCnt+", Insert Count : "+insertCnt);
			gLogger.info("Push "+indoxInserType+" Target ID -> "+inboxTarger.toString());
		}catch (Exception e){
			throw e;
		} finally {
			try {				
				pushSqlMap.endTransaction();
				xoSqlMap.endTransaction();
			} catch ( Exception ee ) {
				gLogger.error(ee.getMessage(), ee);
			}
		}	
	}
	
	/**
	 * LMS 등록
	 * @param targetList
	 * @throws Exception
	 */
	public void lmsProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {
		
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		
		try{
			SmtTranDto smtTranDto = null;
			int forCnt = 0; 			// 1000번째 Commit 하기
			int insertCnt = 0;			// log 용
			StringBuilder inboxTarger = new StringBuilder(); // log 용
			
			String callback = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.callback"), "02-3015-1100");
			String subject = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.subject"), "e-Gift Item의 유효기간 만료 알림");
			String content = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.content"), "e-Gift Item의 유효기간 만료 알림");
			
			if(StringUtils.isNotEmpty(noticeDate))
			{
				String resultMsg = content;
				StringBuffer buf = new StringBuffer();
								
				buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
				resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(noticeDate));
				
				content = resultMsg;
			}
			
			xoSqlMap.startTransaction();
			
			for(Map<String, String> forMap : targetList)
			{
				try{
					smtTranDto = new SmtTranDto();
					
					if(StringUtils.isNotEmpty(noticeDate) && forCnt == 0)
					{
						gLogger.info("EXPIRE_DATE ["+forMap.get("EXPIRE_DATE")+"]");
					}
					
					// 특정 날(args []의 파라미터 값) 수행 시 메시지 셋팅
					if(StringUtils.isEmpty(noticeDate))
					{
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						content = resultMsg;
					}
					
					smtTranDto.setPriority ("S"); //전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
					smtTranDto.setSubject (subject);
					smtTranDto.setContent (content);
					smtTranDto.setCallback (callback);
					smtTranDto.setRecipient_num (forMap.get("RECEIVER_MOBILE"));
					
					inboxTarger.append("[").append(forMap.get("RECEIVER_MOBILE")).append("]");
					
					gLogger.info(smtTranDto.toString());
					
					// LMS 등록
					Long mtPr = this.paymentCancelXoMgr.insertSmtTran(xoSqlMap, smtTranDto);
					
					Map<String, Object> dbMap = new HashMap<String, Object>();
					dbMap.put("giftOrderNo", forMap.get("GIFT_ORDER_NO"));
					dbMap.put("userId", "BATCH");
					dbMap.put("sendType", "E");								// S:즉시, R:재전송, E:만기 알림, C:취소
					dbMap.put("mt_pr", mtPr);
					dbMap.put("channel", "3");								// 1:USER, 2:Admin, 3:Batch
		
					gLogger.info(dbMap.toString());
					
					// LMS 히스토리 등록
					eGiftItemExpirationNoticeXoMgr.setSendHistory(xoSqlMap, dbMap);
					/*
					if(!(forCnt % 1000 == 999))
					{
						xoSqlMap.commitTransaction();
						
						xoSqlMap.startTransaction();
					}
					*/
					insertCnt++;
				} catch (Exception e) {
					// 실패 시 이전까지 등록 한 push를 커밋 하고, 실패 이력 로그 생성 후 다음 진행
					xoSqlMap.commitTransaction();
					
					gLogger.info("LMS FAIL -> "+smtTranDto.toString()+":"+e.getMessage());
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					PrintStream pinrtStream = new PrintStream(out);
					e.printStackTrace(pinrtStream);
					gLogger.info(out.toString());
					
					xoSqlMap.startTransaction();
				}
					
				forCnt++;
			} // End Of For List
			
			// 1000단위로 커밋 후 남어지 커밋
			xoSqlMap.commitTransaction();
			
			gLogger.info("LMS Try Count : "+forCnt+", Insert Count : "+insertCnt);
			gLogger.info("LMS Target CTN -> "+inboxTarger.toString());
		}catch (Exception e){
			throw e;
		} finally {
			try {				
				xoSqlMap.endTransaction();
			} catch ( Exception ee ) {
				gLogger.error(ee.getMessage(), ee);
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EGiftItemExpirationNotice batch = new EGiftItemExpirationNotice();
		//args = "20170627".split(",");
		if(args != null && args.length > 0)
		{
			// 특정일
			batch.batchStart(args[0]);
		}
		else
		{
			// 기본 Batch
			batch.batchStart("");
		}
	}

}
