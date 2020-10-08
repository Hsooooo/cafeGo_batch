/*
 * @(#) $Id: EGiftItemExpirationNotice.java,v 1.4 2018/10/04 01:21:27 resin Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017�⵵ eZENsolution Co., Ltd. All rights reserved.
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
		
		gLogger.info("[TYPE A] ������ ���� ��� : O, �˸� ��� : ������, �˸� ���� : PUSH/INBOX");
		gLogger.info("[TYPE B] ������ ���� ��� : X, ���� ���� ���� : LMS, �˸� ��� : ������, �˸� ���� : LMS");
		gLogger.info("[TYPE C] ������ ���� ��� : X, ���� ���� ���� : ī��/����, �˸� ��� : ������, �˸� ���� : PUSH/INBOX");
		
		// ��� ��ȸ
		if("".equals(thisDate))
		{
			// �⺻ Batch
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
			// Ư����
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
		 * A : �����ڰ� ���� ��� �Ϸ� �� ���� �˸�  PUSH�� IBNOX�� �����ڿ��� ����
		 * B : ������ �� ��� ������ LMS�� ���� �߼� �� ��� LMS�� �����ڿ��� ����
		 * C : ������ �� ��� ������ ī�� or �������� �߼� �� ���  PUSH�� IBNOX�� �����ڿ��� ����
		 */
		// 1. ������ ���� inBox�� push ���� ���� ����
		dbMap.put("noticeType", "A");
		List<Map<String, String>> targetList = eGiftItemExpirationNoticeXoMgr.getEGiftItemExpirationNoticeTarget(dbMap);
		
		gLogger.info("NOTICE TYPE A SIZE : "+targetList.size());		
		gLogger.info(targetList.toString());
		if(targetList.size() > 0)
		{
			inboxProcess(targetList, noticeDate);
			pushProcess(targetList, noticeDate);
		}
		
		// 2. ������ ���� LMS ���� ���� ����
		dbMap.put("noticeType", "B");
		targetList = eGiftItemExpirationNoticeXoMgr.getEGiftItemExpirationNoticeTarget(dbMap);
		
		gLogger.info("NOTICE TYPE B SIZE : "+targetList.size());		
		gLogger.info(targetList.toString());
		if(targetList.size() > 0)
		{
			lmsProcess(targetList, noticeDate);
		}
		
		
		// 3. ������ ���� inBox�� push ���� ���� ����
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
	 * inbox ���
	 * @param targetList
	 * @throws Exception
	 */
	public void inboxProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {
		
		SqlMapClient homeSqlMap = null;		
		
		try{
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			UserInboxDto dto = null;
			
			int forCnt = 0; 			// 1000��° Commit �ϱ�
			int insertCnt = 0;			// log ��
			String indoxInserType = "";	// log ��
			StringBuilder inboxTarger = new StringBuilder(); // log ��
			
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
						throw new XOException("�̺�Ʈ �� �ιڽ� ��� �Ķ󸶹��� ����{inboxType}");
					}
					
					String type = (String) inboxType[0];
					Boolean isApp = (Boolean) inboxType[1];
					Boolean isWeb = (Boolean) inboxType[2];
					
					dto = new UserInboxDto();
					
					if(StringUtils.isBlank(targetId)) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("�̺�Ʈ �� �ιڽ� ��� �Ķ󸶹��� ����{userId}");
					}
					
					if(inboxType.length != 3) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("�̺�Ʈ �� �ιڽ� ��� �Ķ󸶹��� ����{length}");
					}
					
					if(inboxConf == null){
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("�̺�Ʈ �� �ιڽ� ��� �Ķ󸶹��� ����{inbox.properties}");
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
						// Ư�� ��(args []�� �Ķ���� ��) ���� �� �޽��� ����
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						title = resultMsg;
					}
					
					if(StringUtils.isBlank(title)) {
						gLogger.info("NOTICE_TYPE : "+noticeType+", GIFT_NO : "+giftNo);
						throw new XOException("�̺�Ʈ �� �ιڽ� ��� �Ķ󸶹��� ���� {title}");
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
						// 1000 ������ Ŀ��
						homeSqlMap.commitTransaction();
						
						homeSqlMap.startTransaction();
					}*/
					insertCnt++;
				} catch (Exception e) {
					// ���� �� �������� ��� �� inbox�� Ŀ�� �ϰ�, ���� �̷� �α� ���� �� ���� ����
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
			
			// 1000������ Ŀ�� �� ������ Ŀ��
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
	 * push ��� �� tms ������ ����
	 * @param targetList
	 * @throws Exception
	 */
	public void pushProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {

		SqlMapClient pushSqlMap = null;
		SqlMapClient xoSqlMap = null;
		
		try{
			pushSqlMap = IBatisSqlConfig.getPushMapInstance();
			xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			
			int forCnt = 0; 			// 1000��° Commit �ϱ�
			int insertCnt = 0;			// log ��
			String indoxInserType = "";	// log ��
			StringBuilder inboxTarger = new StringBuilder(); // log ��
			
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
					
					dbMap.put("chGubun",         "PU");					// PU ����
					dbMap.put("msgTpCd",         msgTpCd);				// �޽��� �ڵ� - �� �� ���� �ڵ� ���
					dbMap.put("priorityFlag",    "N");					// ��޹߼ۿ���
					dbMap.put("toId",            targetId);				// �� �ĺ� ��ȣ(�α��� ���̵�)
					//125 : TMS ���� �߱��� �� �׷� ���̵�(����), 127 : TMS ���� �߱��� �� �׷� ���̵�(���)
					dbMap.put("appGrpId",        "real".equals(noticeConf.getString("notice.service.mode"))? 127 : 125);
					dbMap.put("pushInsertTable", "TPQ");				// TPQ-����ȭ��뷮
					// serverType {SOPOS : ���̷����� POS, SOBAT : ���̷����� Batch, MSRAPP : ��Ÿ���� APP, MSRBAT : MSR Batch, MSRADM : MSR������������}
					dbMap.put("serverType",      "SOBAT");
					// Ǫ�� Ÿ��(MP : Ǫ�� ���� �� ������ ����, OP : Ǫ�� �߼�, OM : ������ ����)
					dbMap.put("pushType",	     "MP");
							
					// push �޽���
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
						// Ư�� ��(args []�� �Ķ���� ��) ���� �� �޽��� ����
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						pushMsg = resultMsg;
					}
					
					dbMap.put("content", pushMsg);
					
					// �� URL �Ǵ� �۰� ��ȣ ��ӵ� ��Ű��
					dbMap.put("pushValue", "A".equals(noticeType) ? "screenId=EG-01&giftNo="+giftNo 
							  										: "screenId=EG-02&giftOrderNo="+giftOrderNo);
					
					gLogger.info("PUSH INSERT MAP :" + dbMap.toString());
					
					// 1. XO_TMS_QUEUE ���
					eGiftItemExpirationNoticeXoMgr.insertXoTmsQueue(xoSqlMap, dbMap);
					
					gLogger.info("PUSH REG_UID : " + dbMap.get("reqUid"));
					
					// 2. TMS_PERSON_QUEUE ���
					pushMgr.insertTmsPersonQueue(pushSqlMap, dbMap);
					
					// 3. TMS_PERSON_QUEUE ���̺� ���� ��� �Ǿ��ٸ� XO_TMS_QUEUE���̺� push_insert_yn �÷��� ���� 'Y' ������Ʈ
					eGiftItemExpirationNoticeXoMgr.updateXoTmsQueue(xoSqlMap, dbMap);
					/*
					if(forCnt % 1000 == 0)
					{
						// 1000 ������ Ŀ��
						pushSqlMap.commitTransaction();
						xoSqlMap.commitTransaction();
						
						pushSqlMap.startTransaction();
						xoSqlMap.startTransaction();
					}*/
					insertCnt++;
				} catch (Exception e) {
					// ���� �� �������� ��� �� push�� Ŀ�� �ϰ�, ���� �̷� �α� ���� �� ���� ����
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
			
			// 1000������ Ŀ�� �� ������ Ŀ��
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
	 * LMS ���
	 * @param targetList
	 * @throws Exception
	 */
	public void lmsProcess(List<Map<String, String>> targetList, String noticeDate) throws Exception {
		
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		
		try{
			SmtTranDto smtTranDto = null;
			int forCnt = 0; 			// 1000��° Commit �ϱ�
			int insertCnt = 0;			// log ��
			StringBuilder inboxTarger = new StringBuilder(); // log ��
			
			String callback = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.callback"), "02-3015-1100");
			String subject = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.subject"), "e-Gift Item�� ��ȿ�Ⱓ ���� �˸�");
			String content = StringUtils.defaultIfEmpty(noticeConf.getString("notice.lms.content"), "e-Gift Item�� ��ȿ�Ⱓ ���� �˸�");
			
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
					
					// Ư�� ��(args []�� �Ķ���� ��) ���� �� �޽��� ����
					if(StringUtils.isEmpty(noticeDate))
					{
						String resultMsg = noticeConf.getString("notice.this.date.msg");
						StringBuffer buf = new StringBuffer();
						
						buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
						resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(forMap.get("EXPIRE_DATE")));
						
						content = resultMsg;
					}
					
					smtTranDto.setPriority ("S"); //���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
					smtTranDto.setSubject (subject);
					smtTranDto.setContent (content);
					smtTranDto.setCallback (callback);
					smtTranDto.setRecipient_num (forMap.get("RECEIVER_MOBILE"));
					
					inboxTarger.append("[").append(forMap.get("RECEIVER_MOBILE")).append("]");
					
					gLogger.info(smtTranDto.toString());
					
					// LMS ���
					Long mtPr = this.paymentCancelXoMgr.insertSmtTran(xoSqlMap, smtTranDto);
					
					Map<String, Object> dbMap = new HashMap<String, Object>();
					dbMap.put("giftOrderNo", forMap.get("GIFT_ORDER_NO"));
					dbMap.put("userId", "BATCH");
					dbMap.put("sendType", "E");								// S:���, R:������, E:���� �˸�, C:���
					dbMap.put("mt_pr", mtPr);
					dbMap.put("channel", "3");								// 1:USER, 2:Admin, 3:Batch
		
					gLogger.info(dbMap.toString());
					
					// LMS �����丮 ���
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
					// ���� �� �������� ��� �� push�� Ŀ�� �ϰ�, ���� �̷� �α� ���� �� ���� ����
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
			
			// 1000������ Ŀ�� �� ������ Ŀ��
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
			// Ư����
			batch.batchStart(args[0]);
		}
		else
		{
			// �⺻ Batch
			batch.batchStart("");
		}
	}

}
