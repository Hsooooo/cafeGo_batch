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
 * Ȧ���� ���� Ȯ�� �˸� - WholecakeOrderConfirmNoti. 
 * Ȧ���� ���� ��� ������ ���Ͽ� ���� Ȯ�� �˸� LMS �߼�
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

	private final XoWholecakeOrderMgr xoWholecakeOrderMgr;  // Ȧ���� ����
	private final MMSMgr              mmsMgr;               // MMS �߼� ��û
	private final PushMgr             pushMgr;			  // PUSH �߼� ��û

	private static final int BATCH_DELAY_TIME = 1000; // 1 sec.
	
	public WholecakeOrderConfirmNoti() {
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mmsMgr              = new MMSMgr();
		this.pushMgr             = new PushMgr();
		this.logSb               = new StringBuffer();				 // log�� StringBuffer
		this.loggerTitle         = " [wholecake][confirmNoti] ";
	}

	/**
	 * Ȧ���� ���࿡ ���� ���� ���� ���μ���.
	 * @param revocableDate
	 */
	public void start(String revocableDate) {
		long startTime = System.currentTimeMillis();
		
		if (logger.isInfoEnabled()) {
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "START"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "��Ұ�����\t\t\t: ", StringUtils.defaultIfEmpty(revocableDate, DateUtil.getToday()) ));
		}
		
		// ���� Ȯ�� LMS �߼� ��� Ȧ���� ���� ��ȸ ====================================================
		List<WholecakeOrderDto> wholecakeOrderList = getWholeCakeOrderList(revocableDate);
		if(wholecakeOrderList == null){
			wholecakeOrderList = new ArrayList<WholecakeOrderDto>();
		}
		
		List<String> successList   = new ArrayList<String>(); // LMS �߼� ������ ���� ��ȣ
		List<String> failList      = new ArrayList<String>(); // LMS �߼� ������ ���� ��ȣ
		boolean      isSuccess     = false;
		int          cnt           = 1;
		
		// LMS �߼� ó�� ==============================================================
		for(WholecakeOrderDto dto : wholecakeOrderList){
			// LMS �߼�
			isSuccess = this.sendOrderConfirmLms(dto);
			if(isSuccess){
				successList.add(dto.getOrder_no());
			} else {
				failList.add(dto.getOrder_no());
			}
			
			if(logger.isInfoEnabled()){
				logger.info(oUtil.concatString(logSb, this.loggerTitle, "[LMS] ", cnt++, ". ��������: ", dto.getOrder_no(), "|01*****", StringUtils.right(dto.getUser_mobile(), 4), "|", dto.getUser_id(), ", \t result - ", isSuccess));
			}
		}
		
		// PUSH �߼� ó�� ============================================================
		if(logger.isInfoEnabled()){
			logger.info(oUtil.concatString(logSb, this.loggerTitle));
		}
		boolean isPushSuccess = sendPush(wholecakeOrderList);
		
		// ��� �α� ���
		if (logger.isInfoEnabled()) {
			logger.info(oUtil.concatString(logSb, this.loggerTitle));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "��� ���� �Ǽ� \t\t: ", wholecakeOrderList.size(), "��"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "LMS �߼� ���� \t\t: "  , successList.size(), "��"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "LMS �߼� ���� \t\t: "  , failList.size(), "��"));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "PUSH ���� ���� \t\t: " , isPushSuccess));
			logger.info(oUtil.concatString(logSb, this.loggerTitle, "END : ", (System.currentTimeMillis() - startTime), "ms"));
		}
	}
	
	/**
	 * Ȧ���� ���� Ȯ�� LMS �߼� ��� ��ȸ
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
				logger.error(oUtil.concatString(logSb, this.loggerTitle, "Ȧ���� ���� Ȯ�� LMS �߼� ��� ��ȸ ���� : PARAMS-", dbMap.toString()), e);
			}
		} finally {
			dbMap.clear();
		}
		
		return wholecakeOrderList;
	}
	
	/**
	 * LMS �߼�
	 * @param wholecakeDto
	 * @return
	 */
	private boolean sendOrderConfirmLms(WholecakeOrderDto wholecakeDto) {
		boolean      returnBoo      = false;
		String       processStep    = "";
		String       methodLogTitle = oUtil.concatString(logSb, this.loggerTitle, "[", wholecakeDto.getOrder_no(), "]");
		SqlMapClient sqlMap         = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			// Ʈ������ ����
			sqlMap.startTransaction();
			
			// LMS �߼� ��û
			processStep = "LMS �߼� ��û";
			String   subject              = XOUtil.getPropertiesString("mms.wholecake.confirm.title");
			String   contentSku           = "";
			if(wholecakeDto.getTotal_qty() > 1){
				contentSku = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.sku", new String[]{wholecakeDto.getOrder_name(), String.valueOf((wholecakeDto.getTotal_qty() - 1)), "��"});
			} else {
				contentSku = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.sku", new String[]{wholecakeDto.getOrder_name(), "", ""});
			}
			String   contentReceiveDate   = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.receivedate",   new String[]{wholecakeDto.getReceive_date()});
			String   contentReceiveStore  = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.receivestore",  new String[]{wholecakeDto.getReceive_store_name(), wholecakeDto.getReceive_store_cd()});
			String   contentRevocableDate = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.revocabledate", new String[]{wholecakeDto.getRevocable_date()});
			String   encOrderNo           = AES128.encryptUrlEncode(wholecakeDto.getOrder_no());
			String   contentGoOrderInfo   = XOUtil.getPropertiesString("mms.wholecake.confirm.contents.orderinfo",     new String[]{encOrderNo});
			String[] contentArgs          = {wholecakeDto.getUser_name(),  // ����
					                         contentSku,                   // ǰ������
					                         contentReceiveDate,           // ������
					                         contentReceiveStore,          // ���ɸ���			
					                         contentRevocableDate,         // ��Ұ�����
					                         contentGoOrderInfo,           // ���� ���� ��ũ
			                                };
			String   content          = XOUtil.getPropertiesString("mms.wholecake.confirm.contents", contentArgs);
			String   callback         = XOUtil.getPropertiesString("mms.sck.callback");
			SmtTranDto mmsDto         = new SmtTranDto();
			mmsDto.setSubject      (subject);
			mmsDto.setContent      (content);
			mmsDto.setCallback     (callback);
			mmsDto.setRecipient_num(wholecakeDto.getUser_mobile());
			mmsDto.setService_type ("3");									// ���� �޽��� ���� Ÿ�� {2-MMS MT, 3-LMS}
			mmsDto.setXo_order_no  (wholecakeDto.getOrder_no()); 
			long mtPr = this.mmsMgr.insertEmMmtTran(sqlMap, mmsDto);
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, methodLogTitle, processStep));}
			
			// Ȧ���� ���� ���� �˸� ���
			processStep = "Ȧ���� ���� ���� �˸� ���";
			WholecakeOrderNotiDto notiDto = new WholecakeOrderNotiDto();
			notiDto.setOrder_no (wholecakeDto.getOrder_no());
			notiDto.setStatus   (wholecakeDto.getStatus());
			notiDto.setNoti_type("L");							// �˸� Ÿ��{M-MMS, L-LMS, S-SMS, E-EMAIL}	
			notiDto.setNoti_user("O");							// �˸� ���{O-������, P-����������}
			notiDto.setMt_pr    (mtPr);
			int insertCnt = this.xoWholecakeOrderMgr.insertWholecakeOrderNoti(sqlMap, notiDto);
			if (insertCnt != 1) {
				if(logger.isInfoEnabled()){
					logger.info(oUtil.concatString(logSb, methodLogTitle, " Ȧ���� ���� ���� �˸� ��� ����!!"));
				}
			}
			if(logger.isDebugEnabled()){logger.debug(oUtil.concatString(logSb, methodLogTitle, processStep));}
			
			// Ʈ������ ����
			sqlMap.commitTransaction();
			returnBoo = true;
		} catch (Exception e) {
			if (e.getClass().equals(XOException.class)) {
				if (logger.isInfoEnabled()) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, "[", processStep, "] ���� ���� : ", e.getMessage()));
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, "[", processStep, "] ���� ���� "), e);
				}
			}
			returnBoo = false;
		} finally {
			try {
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, " ����  Ʈ������ ���� ���� : ", ee.getMessage()), ee);
				}
			}
		}
		return returnBoo;
	}
	
	/**
	 * PUSH �߼�
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
			// Ʈ������ ����
			xoSqlMap.startTransaction();
			xoSqlMap.startBatch();
			
			sqlMap.startTransaction();
			sqlMap.startBatch();
			
			// PUSH �߼� ��û
			String pushMsg      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.msg");
			String screeId      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.screenid");
			String chGubun      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.chgubun");       // ä�� ����(PU�� ����)
			int    appGrpId     = Integer.parseInt(XOUtil.getPropertiesString("push.appgrpid"));           // TMS���� �߱��� �� �׷� ID ������ 125����, ����� 127
			String priorityFlag = XOUtil.getPropertiesString("push.wholecake.confirm.noti.priorityflag");  // ��޹߼ۿ���
			String msgTpCd      = XOUtil.getPropertiesString("push.wholecake.confirm.noti.msgtpcd");       // �޽�������
			String pushType    =  XOUtil.getPropertiesString("push.type.mp");       // �޽��� Ÿ��
			
			for(WholecakeOrderDto dto : wholecakeList){
				// ��ȿ�� Ȯ��
				if(!StringUtils.equals(dto.getOs_type(), "1") && !StringUtils.equals(dto.getOs_type(), "2")){
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, methodLogTitle, " �߼� ���� - ORDER_NO: ", dto.getOrder_no(), ", USER_ID : ", dto.getUser_id(), ", OS_TYPE [", dto.getOs_type(), "] "));
					}
					continue;
				}
				if(StringUtils.isEmpty(dto.getPush_id()) || StringUtils.equals(dto.getPush_id(), "0")){
					if(logger.isInfoEnabled()){
						logger.info(oUtil.concatString(logSb, methodLogTitle, " �߼����� - ORDER_NO: ", dto.getOrder_no(), ", USER_ID : ", dto.getUser_id(), ", PUSH_ID [", dto.getPush_id(),"]"));
					}
					continue;
				}
				
				// ����Ÿ ����
				String pushValue = ""; // Push ���� URL
				String orderNo = dto.getOrder_no();
				
				if(StringUtils.isNotEmpty(screeId)) {
					pushValue = "screenId=" + screeId;
				}

				if(StringUtils.isEmpty(pushValue) && StringUtils.isNotEmpty(orderNo) ) {
					pushValue = "orderNo=" + orderNo;
				} else if(StringUtils.isNotEmpty(pushValue) && StringUtils.isNotEmpty(orderNo) ) {
					pushValue = pushValue + "&orderNo=" + orderNo;
				}
				
				boolean insertXoTmsQueueOk = false;    // ���̷����� DB�� PUSH ������ ���� ��� ���� 
				boolean insertTmsQueueOk   = false;    // TMS DB�� PUSH ������ ���� ��� ����
				
				dbMap.clear();
				dbMap.put("chGubun",         chGubun);          // PU ����
				dbMap.put("msgTpCd",         msgTpCd);          // �޽��� �ڵ�
				dbMap.put("priorityFlag",    priorityFlag);     // ��޹߼ۿ���
				dbMap.put("toId",            dto.getUser_id()); // ����� ���̵�
				dbMap.put("content",         pushMsg);          // push �޽���
				dbMap.put("appGrpId",        appGrpId);         // TMS ���� �߱��� �� �׷� ���̵� 
				dbMap.put("pushInsertTable", "TPQ");            // TPQ-����ȭ��뷮
				dbMap.put("serverType",      "SOBAT");          // serverType {SOPOS : ���̷����� POS, SOBAT : ���̷����� Batch, MSRAPP : ��Ÿ���� APP, MSRBAT : MSR Batch, MSRADM : MSR������������}
				dbMap.put("pushValue",       pushValue);
				dbMap.put("pushType",        pushType);         // MP:Ǫ�� �߼� �� �޽����� ����, OP:Ǫ�� �߼�, OM:�޽����� ����
				
				// Ȧ���� ���� ���� �˸� ���(XO PUSH �߼� ��û ���̺� ���)
				try {
					this.xoWholecakeOrderMgr.insertWholecakeOrderPush(xoSqlMap, dbMap);
					insertXoTmsQueueOk = true;
					
					// XO_TMS_QUEUE ��� �� ����
					// 2017-07-26 ��û���� ���� ����
					Thread.sleep(50);
				} catch (Exception e) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt + ". ��������: ", dto.getOrder_no(), "|", dto.getUser_id(), "|XO_TMS_QUEUE Ǫ�� ��� ����"));
				}
				
				String reqUid = (String) dbMap.get("reqUid"); // �޽�����ȣ
				
				try {
					this.pushMgr.insertTmsPersonQueue(sqlMap, dbMap);
					insertTmsQueueOk = true;
				} catch (Exception e) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt + ". ��������: ", dto.getOrder_no(), "|", dto.getUser_id(), "|TMS_PERSON_QUEUE Ǫ�� ��� ����"));
				}
				
				// �ַ�� DB ���̺� ��� Ȯ�� ������Ʈ 
				if(insertXoTmsQueueOk && insertTmsQueueOk) {
					this.xoWholecakeOrderMgr.updateWholecakeOrderPush(xoSqlMap, dbMap);
				}
				
				workCnt++;
				
				// 100�Ǵ� Ŀ�� ó��
				if (workCnt % 100 == 0) {
					xoSqlMap.executeBatch();
					sqlMap.executeBatch();

					workCnt = 0;
					
					xoSqlMap.commitTransaction();
					sqlMap.commitTransaction();
					Thread.sleep(BATCH_DELAY_TIME);
				}
				
				if(insertXoTmsQueueOk && insertTmsQueueOk) {
					logger.info(oUtil.concatString(logSb, methodLogTitle, " ", cnt++, ". ��������: ", dto.getOrder_no(), "|", dto.getUser_id(), "|", dto.getOs_type(), "|", dto.getPush_id(), ", \t REQ_UID - ", reqUid));
				} else {
					cnt++;
				}
			}

			// Ʈ������ ����
			xoSqlMap.commitTransaction();
			sqlMap.commitTransaction();
			
			xoSqlMap.commitTransaction();
			sqlMap.commitTransaction();
			returnBoo = true;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(oUtil.concatString(logSb, methodLogTitle, " ���� "), e);
			}
			returnBoo = false;
		} finally {
			try {
				xoSqlMap.endTransaction();
				sqlMap.endTransaction();
			} catch (Exception ee) {
				if (logger.isErrorEnabled()) {
					logger.error(oUtil.concatString(logSb, methodLogTitle, " Ʈ������ ���� ���� : ", ee.getMessage()), ee);
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
