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
	 * e-Gift Item LMS ���� ���а� ��ȸ
	 * @param searchDate
	 * @throws Exception 
	 **/
	public void batchStart(String searchDate) throws Exception{
		
		gLogger.info(loggerTitle + "[SEARCH DATE : " + searchDate + "] START");
		List<Map<String, String>>  eGiftItemLmsList;				// e-Gift Item LMS �������� ����Ʈ
		
		/* e-Gift Item LMS ���� ���� �� ��ȸ */
		eGiftItemLmsList  = eGiftItemFailPushSenderMgr.getEGiftItemLmsList(searchDate);
		gLogger.info(loggerTitle + "e-Gift Item LMS ���� ���� : " + eGiftItemLmsList.size() + " ��");
		
		if(eGiftItemLmsList.size() > 0){
			/** e-Gift Item LMS ���� ���� �˸� Push �߼� ���� **/
			this.pushSendProc(eGiftItemLmsList);
		}
		
		gLogger.info(loggerTitle + "[SEARCH DATE : " + searchDate + "] END");
	}
	
	/**
	 * e-Gift Item LMS ���� ���� �˸� Push �߼�ó��
	 * @param eGiftMmsInfoList
	 * @throws Exception 
	 **/
	public void pushSendProc(List<Map<String, String>> eGiftMmsInfoList) throws Exception {
		
		SqlMapClient xoSqlMap = null;				// ���̷����� DB
		SqlMapClient pushSqlMap = null;				// TMS DB
		
		int succCnt = 0;							// e-Gift Item ���� ���� �˸� PUSH �߼� ���� �Ǽ�
		int failCnt = 0;							// e-Gift Item ���� ���� �˸� PUSH �߼� ���� �Ǽ�
		Map<String, Object> dbMap   = new HashMap<String, Object>();
		
		try{
			
			String serverType = pushConf.getString("push.service.type");									// Server Type {SOPOS : ���̷����� POS, SOBAT : ���̷����� Batch, MSRAPP : ��Ÿ���� APP, MSRBAT : MSR Batch, MSRADM	: MSR������������}
			String chGubun = pushConf.getString("push.chGubun");											// PU ����
			String msgTpCd = pushConf.getString("push.msgtpcd");											// �޽��� �ڵ� - �� �� ���� �ڵ� ���
			String priorityFlag = pushConf.getString("push.priority.flag");									// ��޹߼ۿ���
			int appGrpId = "real".equals(pushConf.getString("push.service.mode"))? 127 : 125;				// �߼۾۾��̵�
			String insertTable = pushConf.getString("push.insert.table");									// Ǫ�� ��� ���̺� {TQ-�ǽð�, TPQ-����ȭ��뷮}
			String pushType = pushConf.getString("push.type");												// Ǫ�� Ÿ��(MP : Ǫ�� ���� �� ������ ����, OP : Ǫ�� �߼�, OM : ������ ����)
			String screenId = pushConf.getString("push.screenid");											// Ǫ�� ���� URL(PUSH_VALUE) Screen Id
			
			xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			xoSqlMap.startTransaction();
			
			pushSqlMap = IBatisSqlConfig.getPushMapInstance();
			pushSqlMap.startTransaction();
			
			// e-Gift Item LMS ���� ���� �˸� Push �߼��� ���� Push ������ ����
			dbMap.put("serverType",      serverType);		// Server Type (�޽�����ȣ {������ ����ũ Ű} ��ȸ�� ���� �ʿ�)
			dbMap.put("chGubun",         chGubun);			// PU ����
			dbMap.put("msgTpCd",         msgTpCd);			// �޽��� �ڵ� - �� �� ���� �ڵ� ���
			dbMap.put("priorityFlag",    priorityFlag);		// ��޹߼ۿ���
			dbMap.put("appGrpId",        appGrpId);			// �߼۾۾��̵�
			dbMap.put("pushInsertTable", insertTable);		// Ǫ�� ��� ���̺�
			dbMap.put("pushType",	     pushType);			// Ǫ�� Ÿ��
			
			for(Map<String, String> eGiftItemInfoMap : eGiftMmsInfoList){
				
				try{
					/* e-Gift Item ���� ���� �˸� Ǫ�� �޼��� ���� */
					String pushMsg = pushConf.getString("push.message");
					String resultMsg = pushMsg;
					StringBuffer buf = new StringBuffer();
					
					buf.delete(0, buf.length()).append("\\{").append(0).append("\\}");
					resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(eGiftItemInfoMap.get("LMS_SEND_MONTH")));
					buf.delete(0, buf.length()).append("\\{").append(1).append("\\}");
					resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(eGiftItemInfoMap.get("LMS_SEND_DAY")));
					pushMsg = resultMsg;
					dbMap.put("content", pushMsg);														// PUSH �޽���
					
					/* Ǫ�� ���� URL ���� */ 
					if(StringUtils.isNotBlank(screenId)){
						StringBuffer pushValue = new StringBuffer();
						pushValue.append("screenId=").append(screenId).append("&giftOrderNo=").append(eGiftItemInfoMap.get("GIFT_ORDER_NO"));	
						dbMap.put("pushValue", pushValue.toString());									// PUSH �Ķ����(�� URL �Ǵ� �۰� ��ȣ ��ӵ� ��Ű��)
					}
					
					dbMap.put("orderNo", eGiftItemInfoMap.get("GIFT_ORDER_NO"));						// e-Gitf Item �ֹ�/�����ȣ
					dbMap.put("toId", eGiftItemInfoMap.get("USER_ID"));									// �� �ĺ� ��ȣ(�α��� ���̵�)
					
					// 'XO_TMS_QUEUE' ���̺� PUSH ���� Insert ����
					eGiftItemFailPushSenderMgr.insertXoTmsQueue(xoSqlMap, dbMap);
					
					// 'TMS DB'�� PUSH ���� Insert ����
					pushMgr.insertTmsQueue(pushSqlMap, dbMap);
					
					// 'XO_TMS_QUEUE' ���̺� Ǫ�� ��� ���� Update ����
					eGiftItemFailPushSenderMgr.updateXoTmsQueue(xoSqlMap, dbMap);
					
					// 'MMS.EM_MMT_TRAN' ���̺� LMS ���� ���п� ���� �����ڿ��� Ǫ�� �˸� �߼ۿ���(Y : �߼�) Update ����
					eGiftItemFailPushSenderMgr.updateEmMmtTran(xoSqlMap, dbMap);
					
					xoSqlMap.commitTransaction();
					pushSqlMap.commitTransaction();
					
					succCnt++; 							// PUSH �߼� ���� �Ǽ��� ����
				} catch (SQLException se) {

					/** SQL Exception �߻� �� �Է��� DB �� RollBack�� ���� Ʈ������ ���� ��, ���� ������ ó���� ���� �ٽ� ���� ���� **/
					xoSqlMap.endTransaction();
					xoSqlMap.startTransaction();
					
					pushSqlMap.endTransaction();
					pushSqlMap.startTransaction();
					
					gLogger.info("PUSH FAIL dbMap >>>>> " + dbMap.toString() + " >>>>>>>>>> " + se.getMessage());
					failCnt++; 							// PUSH �߼� ���� �Ǽ��� ����
				}
			}
			
		} finally {
			try {
				gLogger.info(loggerTitle + "e-Gift Item ���� ���� �˸� PUSH �߼� >>>>> ���� : " + succCnt + " �� | ���� : " + failCnt + " ��");
				xoSqlMap.endTransaction();
				pushSqlMap.endTransaction();
			} catch (Exception ee) {
				gLogger.error("Exception ee-Message : " + ee.toString());
				throw ee;
			}
		}		
	}
	
	/**
	 * e-Gift Item LMS ���� ���� �˸� Push �߼� Batch Main
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
				strToDay = argDay[0];							// �Է� ���� Ư������ e-Gift Item LMS ���� ���� ��ȸ���ڷ� ����
			} else {
				Calendar calDate = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
				calDate.setTime(new Date());
				strToDay = fm.format(calDate.getTime());	// �Է� �� ��¥�� ���� ��� Batch �������ڸ� e-Gift Item LMS ���� ���� ��ȸ���ڷ� ����
			}
			
			eGiftPushSend.batchStart(strToDay);
			
		} catch (Exception e) {
			gLogger.error("Exception e-Message : " + e.toString());
		}
	}
}
