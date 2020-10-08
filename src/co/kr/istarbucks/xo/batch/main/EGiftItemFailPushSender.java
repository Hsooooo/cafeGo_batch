package co.kr.istarbucks.xo.batch.main;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.mgr.EGiftItemFailPushSenderMgr;
import co.kr.istarbucks.xo.batch.mgr.PushMgr;

public class EGiftItemFailPushSender {
	
	private final String loggerTitle;
	private final static Logger gLogger = Logger.getLogger ("GIFT_LMS_PUSH");
	
	private final EGiftItemFailPushSenderMgr eGiftItemFailPushSenderMgr;
	private final PushMgr pushMgr;
	private final Configuration pushConf;

	public EGiftItemFailPushSender(){
		
		 this.loggerTitle = "[EGiftItemFailPushSender] ";
		 this.eGiftItemFailPushSenderMgr = new EGiftItemFailPushSenderMgr();
		 this.pushMgr = new PushMgr();
		 this.pushConf = CommPropertiesConfiguration.getConfiguration ("push.properties");
		 
	}
	
	/**
	 * e-Gift Item LMS 수신 실패건 조회
	 * @param searchDate
	 * @throws Exception 
	 **/
	public void batchStart(String searchDate) throws Exception{
		
		gLogger.info(loggerTitle + "[SEARCH DATE : " + searchDate + "] START");
		List<Map<String, String>>  eGiftItemLmsList;				// e-Gift Item LMS 수신정보 리스트
		
		/* e-Gift Item LMS 수신 실패 건 조회 */
		eGiftItemLmsList  = eGiftItemFailPushSenderMgr.getEGiftItemLmsList(searchDate);
		gLogger.info(loggerTitle + "e-Gift Item LMS 수신 실패 : " + eGiftItemLmsList.size() + " 건");
		
		if(eGiftItemLmsList.size() > 0){
			/** e-Gift Item LMS 수신 실패 알림 Push 발송 진행 **/
			this.pushSendProc(eGiftItemLmsList);
		}
		
		gLogger.info(loggerTitle + "[SEARCH DATE : " + searchDate + "] END");
	}
	
	/**
	 * e-Gift Item LMS 수신 실패 알림 Push 발송처리
	 * @param eGiftMmsInfoList
	 * @throws Exception 
	 **/
	public void pushSendProc(List<Map<String, String>> eGiftMmsInfoList) throws Exception {
		
		SqlMapClient xoSqlMap = null;				// 사이렌오더 DB
		SqlMapClient pushSqlMap = null;				// TMS DB
		
		int succCnt = 0;							// e-Gift Item 수신 실패 알림 PUSH 발송 정상 건수
		int failCnt = 0;							// e-Gift Item 수신 실패 알림 PUSH 발송 실패 건수
		Map<String, Object> dbMap   = new HashMap<String, Object>();
		
		try{
			
			String serverType = pushConf.getString("push.service.type");									// Server Type {SOPOS : 사이렌오더 POS, SOBAT : 사이렌오더 Batch, MSRAPP : 스타벅스 APP, MSRBAT : MSR Batch, MSRADM	: MSR관리자페이지}
			String chGubun = pushConf.getString("push.chGubun");											// PU 고정
			String msgTpCd = pushConf.getString("push.msgtpcd");											// 메시지 코드 - 할 당 받은 코드 사용
			String priorityFlag = pushConf.getString("push.priority.flag");									// 긴급발송여부
			int appGrpId = "real".equals(pushConf.getString("push.service.mode"))? 127 : 125;				// 발송앱아이디
			String insertTable = pushConf.getString("push.insert.table");									// 푸시 등록 테이블 {TQ-실시간, TPQ-개인화대용량}
			String pushType = pushConf.getString("push.type");												// 푸시 타입(MP : 푸시 전송 및 수신함 적재, OP : 푸시 발송, OM : 수신함 적재)
			String screenId = pushConf.getString("push.screenid");											// 푸시 랜딩 URL(PUSH_VALUE) Screen Id
			
			xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			xoSqlMap.startTransaction();
			
			pushSqlMap = IBatisSqlConfig.getPushMapInstance();
			pushSqlMap.startTransaction();
			
			// e-Gift Item LMS 수신 실패 알림 Push 발송을 위해 Push 데이터 셋팅
			dbMap.put("serverType",      serverType);		// Server Type (메시지번호 {연동시 유니크 키} 조회를 위해 필요)
			dbMap.put("chGubun",         chGubun);			// PU 고정
			dbMap.put("msgTpCd",         msgTpCd);			// 메시지 코드 - 할 당 받은 코드 사용
			dbMap.put("priorityFlag",    priorityFlag);		// 긴급발송여부
			dbMap.put("appGrpId",        appGrpId);			// 발송앱아이디
			dbMap.put("pushInsertTable", insertTable);		// 푸시 등록 테이블
			dbMap.put("pushType",	     pushType);			// 푸시 타입
			
			for(Map<String, String> eGiftItemInfoMap : eGiftMmsInfoList){
				
				try{
					/* e-Gift Item 수신 실패 알림 푸쉬 메세지 지정 */
					String pushMsg = pushConf.getString("push.message");
					String resultMsg = pushMsg;
					StringBuffer buf = new StringBuffer();
					
					buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
					resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(eGiftItemInfoMap.get("LMS_SEND_MONTH")));
					buf.delete(0, buf.length()).append("\\{").append(1).append("\\}");
					resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(eGiftItemInfoMap.get("LMS_SEND_DAY")));
					pushMsg = resultMsg;
					dbMap.put("content", pushMsg);														// PUSH 메시지
					
					/* 푸시 랜딩 URL 지정 */ 
					if(StringUtils.isNotBlank(screenId)){
						StringBuffer pushValue = new StringBuffer();
						pushValue.append("screenId=").append(screenId).append("&giftOrderNo=").append(eGiftItemInfoMap.get("GIFT_ORDER_NO"));	
						dbMap.put("pushValue", pushValue.toString());									// PUSH 파라미터(웹 URL 또는 앱과 상호 약속된 스키마)
					}
					
					dbMap.put("orderNo", eGiftItemInfoMap.get("GIFT_ORDER_NO"));						// e-Gitf Item 주문/예약번호
					dbMap.put("toId", eGiftItemInfoMap.get("USER_ID"));									// 고객 식별 번호(로그인 아이디)
					
					// 'XO_TMS_QUEUE' 테이블에 PUSH 정보 Insert 진행
					eGiftItemFailPushSenderMgr.insertXoTmsQueue(xoSqlMap, dbMap);
					
					// 'TMS DB'에 PUSH 정보 Insert 진행
					pushMgr.insertTmsQueue(pushSqlMap, dbMap);
					
					// 'XO_TMS_QUEUE' 테이블에 푸시 등록 여부 Update 진행
					eGiftItemFailPushSenderMgr.updateXoTmsQueue(xoSqlMap, dbMap);
					
					// 'MMS.EM_MMT_TRAN' 테이블에 LMS 수신 실패에 대한 구매자에게 푸시 알림 발송여부(Y : 발송) Update 진행
					eGiftItemFailPushSenderMgr.updateEmMmtTran(xoSqlMap, dbMap);
					
					xoSqlMap.commitTransaction();
					pushSqlMap.commitTransaction();
					
					succCnt++; 							// PUSH 발송 정상 건수를 더함
				} catch (SQLException se) {

					/** SQL Exception 발생 시 입력한 DB 값 RollBack을 위해 트랜젝션 종료 후, 이후 데이터 처리를 위해 다시 시작 진행 **/
					xoSqlMap.endTransaction();
					xoSqlMap.startTransaction();
					
					pushSqlMap.endTransaction();
					pushSqlMap.startTransaction();
					
					gLogger.info("PUSH FAIL dbMap >>>>> " + dbMap.toString() + " >>>>>>>>>> " + se.getMessage());
					failCnt++; 							// PUSH 발송 실패 건수를 더함
				}
			}
			
		} finally {
			try {
				gLogger.info(loggerTitle + "e-Gift Item 수신 실패 알림 PUSH 발송 >>>>> 정상 : " + succCnt + " 건 | 실패 : " + failCnt + " 건");
				xoSqlMap.endTransaction();
				pushSqlMap.endTransaction();
			} catch (Exception ee) {
				gLogger.error("Exception ee-Message : " + ee.toString());
				throw ee;
			}
		}		
	}
	
	/**
	 * e-Gift Item LMS 수신 실패 알림 Push 발송 Batch Main
	 * @param args
	 * @throws Exception 
	 **/
	public static void main(String[] args) throws Exception {
		EGiftItemFailPushSender eGiftPushSend = new EGiftItemFailPushSender();
		
		String[] argDay = args;
		String strToDay = "";
		
		try{
			
			// argDay = "20190222".split(",");
			if(argDay != null && argDay.length > 0){	
				strToDay = argDay[0];							// 입력 받은 특정일을 e-Gift Item LMS 수신 실패 조회일자로 지정
			} else {
				Calendar calDate = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
				calDate.setTime(new Date());
				strToDay = fm.format(calDate.getTime());	// 입력 한 날짜가 없을 경우 Batch 실행일자를 e-Gift Item LMS 수신 실패 조회일자로 지정
			}
			
			eGiftPushSend.batchStart(strToDay);
			
		} catch (Exception e) {
			gLogger.error("Exception e-Message : " + e.toString());
		}
	}
}
