/*
 * @(#) $Id: ReserveSendPostNotice.java,v 1.2 2017/08/07 10:43:10 shinepop Exp $
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
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.EGiftItemExpirationNoticeXoMgr;
import co.kr.istarbucks.xo.batch.mgr.InboxMgr;
import co.kr.istarbucks.xo.batch.mgr.PushMgr;
import co.kr.istarbucks.xo.batch.mgr.ReserveSendPostNoticeXoMgr;

public class ReserveSendPostNotice {
	
	private final ReserveSendPostNoticeXoMgr reserveSendPostNoticeXoMgr;
	private final EGiftItemExpirationNoticeXoMgr eGiftItemExpirationNoticeXoMgr;
	private final InboxMgr inboxMgr;
	private final PushMgr pushMgr;
	private final String loggerTitle;
	private final Configuration inboxConf;
	private final Configuration noticeConf;
	private final Logger gLogger = Logger.getLogger ("RESERVE_NOTICE");
	
	public ReserveSendPostNotice(){
		this.reserveSendPostNoticeXoMgr = new ReserveSendPostNoticeXoMgr();
		this.eGiftItemExpirationNoticeXoMgr = new EGiftItemExpirationNoticeXoMgr();
		this.inboxMgr = new InboxMgr();
        this.pushMgr = new PushMgr();
        this.loggerTitle = "[e-Gift Item ReserveSendPostNotice] ";
        this.inboxConf =  CommPropertiesConfiguration.getConfiguration("inbox.properties");
        this.noticeConf = CommPropertiesConfiguration.getConfiguration ("notice.properties");
	}
	
	public void batchStart(String thisDate){
		// 기본 배치 수행 (매시 00분 예약 발송 or 매시 30분 예약 발송)
		if(StringUtils.isNotEmpty(thisDate))
		{
			// 특정일
			gLogger.info(loggerTitle + " START DATE ["+thisDate+"]");
		}
		else
		{
			gLogger.info(loggerTitle + " START");
		}
		
		Map<String, Object> dbMap = new HashMap<String, Object>();
		dbMap.put("thisDate", thisDate);
		
		try{
			List<Map<String, String>> targetList = reserveSendPostNoticeXoMgr.getReserveSendPostNoticeTarget(dbMap);
			
			gLogger.info(targetList.toString());
			
			inboxProcess(targetList);
			pushProcess(targetList);
			gLogger.info(loggerTitle + " END");
		}catch (Exception e){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream pinrtStream = new PrintStream(out);
			e.printStackTrace(pinrtStream);
			gLogger.info(out.toString());
			
			gLogger.info(e.getMessage());
		}
		
	}
	
	public void inboxProcess(List<Map<String, String>> targetList) throws Exception {
		SqlMapClient homeSqlMap = null;		
		
		try{
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			UserInboxDto dto = null;
			
			int forCnt = 0; 								// 1000번째 Commit 하기
			int insertCnt = 0;								// log 용
			int passCnt = 0;								// log 용
			StringBuilder inMT_PR = new StringBuilder();	// log 용
			StringBuilder passMT_PR = new StringBuilder();	// log 용
			
			homeSqlMap.startTransaction();
			
			for(Map<String, String> forMap : targetList)
			{
				try{
					String targetId = forMap.get("USER_ID");					
					String receiverMobile = forMap.get("RECEIVER_MOBILE");					
					String reserveTime = forMap.get("RESERVE_TIME");
					String userName = forMap.get("USER_NAME");
					String msgStatus = forMap.get("MSG_STATUS");
					
					if(forCnt == 0)
					{
						gLogger.info("INBOX(HP) IN RESERVE_TIME[" + reserveTime + "]");
					}
					
					if("1".equals(msgStatus))
					{
						passMT_PR.append("[").append(targetId).append("]");
						passCnt++;
					}
					else if("2".equals(msgStatus) || "3".equals(msgStatus))
					{
						Object[] inboxType = InboxType.E_GIFT_ITEM.RESERVE_NOTICE;
						
						if(inboxType == null) {						
							throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{inboxType}");
						}
						
						String type = (String) inboxType[0];
						Boolean isApp = (Boolean) inboxType[1];
						Boolean isWeb = (Boolean) inboxType[2];
						
						dto = new UserInboxDto();
						
						if(StringUtils.isBlank(targetId)) {
							throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{userId}");
						}
						
						if(inboxType.length != 3) {
							throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{length}");
						}
						
						if(inboxConf == null){
							throw new XOException("이벤트 고객 인박스 등록 파라마미터 에러{inbox.properties}");
						}
						
						String title = StringUtils.defaultIfEmpty(inboxConf.getString("inbox."+type), "");
						
						String resultMsg = title;
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(userName));
						buf.delete(0, buf.length()).append("\\{").append(1).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(receiverMobile));
						
						title = resultMsg;
						
						if(StringUtils.isBlank(title)) {
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
						
						inMT_PR.append("[").append(targetId).append("]");
						insertCnt++;
					}
					
					if(forCnt % 1000 == 0)
					{
						// 1000 단위로 커밋
						homeSqlMap.commitTransaction();
						homeSqlMap.startTransaction();
					}
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
			
			gLogger.info("INBOX(HP) Try Count : "+forCnt+", Insert Count : "+insertCnt+", Pass Count : "+passCnt);
			gLogger.info("INSERT LMS INFO : "+inMT_PR.toString());
			gLogger.info("PASS LMS INFO : "+passMT_PR.toString());
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
	
	public void pushProcess(List<Map<String, String>> targetList) throws Exception {

		SqlMapClient pushSqlMap = null;
		SqlMapClient xoSqlMap = null;
		
		try{
			pushSqlMap = IBatisSqlConfig.getPushMapInstance();
			xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			
			int forCnt = 0; 								// 1000번째 Commit 하기
			int insertCnt = 0;								// log 용
			int passCnt = 0;								// log 용
			StringBuilder inMT_PR = new StringBuilder();	// log 용
			StringBuilder passMT_PR = new StringBuilder();	// log 용
			
			pushSqlMap.startTransaction();
			xoSqlMap.startTransaction();
			
			for(Map<String, String> forMap : targetList)
			{
				Map<String, Object> dbMap   = new HashMap<String, Object>();
				
				try{
					String targetId = forMap.get("USER_ID");					
					String receiverMobile = forMap.get("RECEIVER_MOBILE");					
					String reserveTime = forMap.get("RESERVE_TIME");
					String userName = forMap.get("USER_NAME");
					String msgStatus = forMap.get("MSG_STATUS");
					String giftOrderNo = forMap.get("GIFT_ORDER_NO");
					
					if(forCnt == 0)
					{
						gLogger.info("INBOX(TMS) IN RESERVE_TIME[" + reserveTime + "]");
					}
					
					if("1".equals(msgStatus))
					{
						passMT_PR.append("[").append(targetId).append("]");
						passCnt++;
					}
					else if("2".equals(msgStatus) || "3".equals(msgStatus))
					{
						dbMap.put("chGubun",         "PU");					// PU 고정
						// 메시지 코드 - 할 당 받은 코드 사용
						dbMap.put("msgTpCd",         StringUtils.defaultIfEmpty(noticeConf.getString("reserve.send.msg.tp.cd"), "GFT09"));
						dbMap.put("priorityFlag",    "N");					// 긴급발송여부
						dbMap.put("toId",            targetId);				// 고객 식별 번호(로그인 아이디)
						//125 : TMS 에서 발급한 앱 그룹 아이디(개발), 127 : TMS 에서 발급한 앱 그룹 아이디(상용)
						dbMap.put("appGrpId",        "real".equals(noticeConf.getString("notice.service.mode"))? 127 : 125);
						dbMap.put("pushInsertTable", "TPQ");				// TPQ-개인화대용량
						// serverType {SOPOS : 사이렌오더 POS, SOBAT : 사이렌오더 Batch, MSRAPP : 스타벅스 APP, MSRBAT : MSR Batch, MSRADM : MSR관리자페이지}
						dbMap.put("serverType",      "SOBAT");
						// 푸시 타입(MP : 푸시 전송 및 수신함 적재, OP : 푸시 발송, OM : 수신함 적재)
						dbMap.put("pushType",	     "OM");
								
						// push 메시지
						String pushMsg = noticeConf.getString("reserve.send.push.msg");
						
						String resultMsg = pushMsg;
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(userName));
						buf.delete(0, buf.length()).append("\\{").append(1).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(receiverMobile));
						
						pushMsg = resultMsg;
						
						dbMap.put("content", pushMsg);
						
						gLogger.info("PUSH TMS INSERT MAP : " + dbMap.toString());
						
						// 1. XO_TMS_QUEUE 등록
						eGiftItemExpirationNoticeXoMgr.insertXoTmsQueue(xoSqlMap, dbMap);
						
						gLogger.info("PUSH TMS REG_UID : " + dbMap.get("reqUid"));
						
						// 2. TMS_PERSON_QUEUE 등록
						pushMgr.insertTmsPersonQueue(pushSqlMap, dbMap);
						
						// 3. TMS_PERSON_QUEUE 테이블에 정상 등록 되었다면 XO_TMS_QUEUE테이블에 push_insert_yn 컬럼의 값을 'Y' 업데이트
						eGiftItemExpirationNoticeXoMgr.updateXoTmsQueue(xoSqlMap, dbMap);
						
						// 4. 예약 발송 후 INBOX 전송 여부 등록
						eGiftItemExpirationNoticeXoMgr.updateReserveInbox(xoSqlMap, giftOrderNo);
						
						inMT_PR.append("[").append(targetId).append("]");
						insertCnt++;
					}
					
					if(forCnt % 1000 == 0)
					{
						// 1000 단위로 커밋
						pushSqlMap.commitTransaction();
						xoSqlMap.commitTransaction();
						
						pushSqlMap.startTransaction();
						xoSqlMap.startTransaction();
					}
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
			
			gLogger.info("INBOX(TMS) Count : "+forCnt+", Insert Count : "+insertCnt+", Pass Count : "+passCnt);
			gLogger.info("INSERT LMS INFO : "+inMT_PR.toString());
			gLogger.info("PASS LMS INFO : "+passMT_PR.toString());
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
	 * @param args
	 */
	public static void main(String[] args) {
		ReserveSendPostNotice batch = new ReserveSendPostNotice();
		//args = "201707251500".split(",");
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
