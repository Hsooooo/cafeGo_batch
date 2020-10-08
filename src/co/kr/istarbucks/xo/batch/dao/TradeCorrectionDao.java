/*
 * @(#) $Id: TradeCorrectionDao.java,v 1.16 2018/12/04 01:35:41 yoyo962 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.home.SurveyDto;
import co.kr.istarbucks.xo.batch.common.dto.home.UserInboxDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.MsrvInfrmMasterDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.TradeCorrectionDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.MMSMgr;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * �������� �ŷ� ��� TradeCorrectionDao.
 * @author eZEN
 * @since 2014. 2. 6.
 * @version $Revision: 1.16 $
 */
public class TradeCorrectionDao {
	
	private final Logger tLogger = Logger.getLogger ("TRADE");
	
	private static final String loggerTitle = "[TradeCorrection] "; 				// PMD ����( PMD ���� : final field�� static final�� ���� )

	
	/** ���� �Ϸ� **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_COMP   = "O30"; // ���� �Ϸ�
	/** �̼��� ��� **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE = "O31"; // �̼��� ���
	
	/**
	 * �ŷ� ��� ��� ��ȸ
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<TradeCorrectionDto> getTradeCorrectionInfo () throws Exception {
		String sqlId = "trade.getTradeCorrectionInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.queryForList (sqlId);
	}
	
	
	/**
	 * �ŷ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 * @param tradeDate
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<TradeCorrectionDto> getTradeCorrectionDateInfo ( String tradeDate ) throws Exception {
		String sqlId = "trade.getTradeCorrectionDateInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.queryForList (sqlId, tradeDate);
	}

	/**
	 * XO_PAYMENT# STATUS ��ȸ
	 * @param orderno
	 * @return string
	 * @throws Exception
	 */
	
	@SuppressWarnings ( "unchecked" )
	public String getXoPaymentStatusInfo(String orderno) throws SQLException {
		// 2020.09.10 ������ �߰�
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("orderNo", orderno);
		return (String) sqlMap.queryForObject("trade.getXoPaymentStatusInfo", paramMap);
	}
	
	// XO��� ���̰� ��� SMS �߼�
		private void smsSend(String orderNo,String trdKey, int Cnt) {
			
			SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // ���̷����� Ʈ������
			MMSMgr mmsMgr;
			StringBuffer logSb = new StringBuffer ();
			mmsMgr = new MMSMgr();
			try {
				
				sqlMap.startTransaction();

				Configuration config = null;
				config = CommPropertiesConfiguration.getConfiguration("sms.properties");

				String content="[XO��� DIFF]\n";
				if(Cnt==0){
					content += "���� : 0��";
				}else if(Cnt==1){
                	content += "�ŷ�Ű : "+trdKey;
                }else if(Cnt > 1){
                	content += "�ŷ�Ű : "+trdKey+" �� "+(Cnt-1)+"��";
                }
              
				String callback = config.getString("wholecake.sms.callback");
				String recipientNum = config.getString("xotrade.diff.sms.receive.info");
				String recipientArr[] = recipientNum.split("\\|");

		        // SMS �߼� ��û 
				for(int i = 0; i < recipientArr.length; i++) {
			        SmtTranDto smtTranDto = new SmtTranDto ();
			        smtTranDto.setPriority ("S"); //���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
			        smtTranDto.setContent (content);
			        smtTranDto.setCallback (callback);
			        smtTranDto.setRecipient_num (recipientArr[i]);
			        mmsMgr.insertSmtTran (sqlMap, smtTranDto);
			        sqlMap.commitTransaction (); // commit
				}
		        
			} catch (Exception e) {
				logSb.delete(0, logSb.length ()).append ("[XO��� MSR/�뿵 �ֹ����� DIFF] SMS �߼� ���� : ").append (e.getMessage());
				tLogger.info (logSb.toString ());
				
				if(sqlMap != null) {
					try {
						sqlMap.endTransaction();
					} catch (SQLException e1) {
						tLogger.error(e.getMessage(), e);
					}
				}
			} finally {
	            try {
	            	sqlMap.endTransaction ();
	            } catch ( Exception ee ) {
	            	tLogger.error(ee.getMessage(), ee);
	            }
			}
		}
		
	/**
	 * �ŷ� ��� �Ϸ� ó��
	 * @return int
	 * @throws Exception
	 */
	public int tradeComplete ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		SqlMapClient sqlMap = null;
		SqlMapClient msrSqlMap = null;
		SqlMapClient homeSqlMap = null;
		
		long startTime;
		int commitCnt = 0;
		int procCnt = 1;
		StringBuffer buf = new StringBuffer ();
		
		NonMemberRewardDao nonMemberRewardDao = new NonMemberRewardDao();		// ��ȸ��(��/��) ������ ���� / ȸ�� NonMemberRewardDao.
		
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			
			Map<String, String> paramMap = new HashMap<String, String> ();
			Map<String, String> dbMap = new HashMap<String, String> ();
			int resultCnt = 0;
			Boolean nonMembRewResult = true;									// ��ȸ�� ������ ���� ���� / ȸ�� �����
			boolean isMatched = true;
			int trdDiffCnt=0;
			String trdDiffOrderNo="";
			String trdDiffKey="";
			
			if ( tradeDtoInfo != null ) {
				for ( TradeCorrectionDto tradeMap : tradeDtoInfo ) {
					
					try {
						sqlMap.startTransaction ();
						msrSqlMap.startTransaction();
						
						startTime = System.currentTimeMillis ();
						
						int step = 1;
						
						buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (procCnt).append (" : ").append (tradeMap.getOrder_no ()).append (") ");
						String logTitle = buf.toString ();
						
						// ��ȸ��(��/��) ������ ���� ���� �� ȸ���� ���� Parm ����
						Map<String, Object> nonMembOrderMap = new HashMap<String, Object> ();
						
						// �������� ���� : 11-�Ϲݸ��� / MSR ���� : 30-�ֹ� �Ϸ�,31-���� �Ϸᰡ �ƴ� ��� MSR ���� 31-�����Ϸ� ���ó��  
						if ( tradeMap.getSale_flag ().equals ("11") && ( !tradeMap.getStatus ().equals ("30") && !tradeMap.getStatus ().equals ("31") ) ) {
							
							// 2020.09.10 ������ �߰�
							// XO_ORDER#(�������, �ֹ����), XO_PAYMENT#(�������) ��� �÷��� üũ
							if(tradeMap.getSale_flag ().equals ("11") && (tradeMap.getStatus ().equals ("12") || tradeMap.getStatus ().equals ("22")) ){
								
								try{
								
								String xoPaymentStatus = StringUtils.defaultIfEmpty(getXoPaymentStatusInfo(tradeMap.getOrder_no() ), "");
								
								// XO_ORDER#(�������, �ֹ����), XO_PAYMENT#(�������) ��� ��� ��ó��
								if(StringUtils.equals (xoPaymentStatus, "C")){
									tLogger.info ("[XO_PAYMENT# STATUS=C] ORDER_NO : "+tradeMap.getOrder_no());
									isMatched = false;
									trdDiffCnt++;
									trdDiffOrderNo = tradeMap.getOrder_no();
									trdDiffKey = tradeMap.getSale_date()+"/"+tradeMap.getStore_no()+"/"+tradeMap.getPos_no()+"/"+tradeMap.getSeq_no();
								}
                                }catch(Exception e){
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
								
							}
							
							// 2020.09.10 ������ �߰�
							// XO_ORDER#(�������, �ֹ����), XO_PAYMENT#(�������) ��� ��� ��ó��
							if(isMatched){
								
							/********** MSRȸ�����ΰ� '��ȸ��(J)'�̰ų� '��ȸ��(N)'�� ��� ��ȸ��(��/��) ������ ���� ���� ���� Start *****/
							Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
							String nonMemberRewardPracYn = conf.getString("nonMember.reward.practice.yn","N");
							
							if(StringUtils.equals (nonMemberRewardPracYn, "Y")){
								if("J".equals(tradeMap.getMsr_user_flag()) || "N".equals(tradeMap.getMsr_user_flag())){
									buf.delete (0, buf.length ()).append (logTitle).append ("��ȸ��(��/��) ������ ���� ���� ���� Start !!!!!").append (" (xopOrdNo : ").append ((String) tradeMap.getOrder_no()).append (")");
									tLogger.info (buf.toString ());
									
									nonMembOrderMap.put("nextStatus", 		"31");											// ���� �� �ֹ�����(31:�����Ϸ�)
									nonMembOrderMap.put("userId", 			(String) tradeMap.getUser_id());				// ����� ���̵�
									nonMembOrderMap.put("totalPayAmt", 		(Integer) tradeMap.getTotal_pay_amt());			// �ֹ� �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�} 
									nonMembOrderMap.put("xopOrdNo", 		(String) tradeMap.getOrder_no());				// ���̷����� �ֹ���ȣ
									nonMembOrderMap.put("saleDate", 		(String) tradeMap.getSale_date());				// POS�ŷ� ������
									nonMembOrderMap.put("storeCd", 			(String) tradeMap.getStore_no());				// ���� �ڵ�
									nonMembOrderMap.put("posNo", 			(String) tradeMap.getPos_no());					// POS ��ȣ
									nonMembOrderMap.put("seqNo", 			(String) tradeMap.getSeq_no());					// POS �ŷ� ��ȣ
									
									/* ��ȸ��(��/��) ������ ���� ���� */
									nonMembRewResult = nonMemberRewardDao.rewardProcess(nonMembOrderMap, msrSqlMap);
									nonMembOrderMap.clear();
									
									buf.delete (0, buf.length ()).append (logTitle).append ("��ȸ��(��/��) ������ ���� ���� ���� The End !!!!!");
									tLogger.info (buf.toString ());
								}
							}
							/********** MSRȸ�����ΰ� '��ȸ��(J)'�̰ų� '��ȸ��(N)'�� ��� ��ȸ��(��/��) ������ ���� ���� ���� The End *****/
							
							if(nonMembRewResult){
								String sqlId = "trade.tradeComplete";
								String sqlId2 = "trade.orderHistoryRegister";							
								//String sqlId3 = "survey.getSurveyJoinInfo";			// ������ ���� ����(�̻��)
								//String sqlId4 = "survey.insertUserInBoxMsg";			// ������ ���� ����(�̻��)
								//String sqlId5 = "trade.getOrderMakeComplateDate";		// ������ ���� ����(�̻��)
								//String sqlId6 = "paymentcancelMsr.getRegMember";		// ������ ���� ����(�̻��)
								String sqlId7 = "paymentcancelMsr.insertTrdCouponList";
								String sqlId8 = "paymentcancelXo.getCouponNumber";
								
								paramMap.put ("status", "31");
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("tranSeqNo", tradeMap.getTran_seq_no ());
								paramMap.put ("isCancelComp", "N");
								
								sqlMap.update (sqlId, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								paramMap.clear ();
								
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("userId", tradeMap.getUser_id ());
								paramMap.put ("userName", tradeMap.getUser_name ());
								paramMap.put ("status", "31"); // 31-�����Ϸ�
								paramMap.put ("historyChannel", "5"); // 5-���
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("tranSeqNo", tradeMap.getTran_seq_no ());
								
								sqlMap.update (sqlId2, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� �̷� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								// �������� �ȳ� - Start
								try {
									// ���� ����� ���� �� INBOX �߼�(BATCH�� ��� PUSH �߼��� ��������)
									this.sendSurveyInfo(sqlMap, msrSqlMap, homeSqlMap, tradeMap, logTitle, step, startTime);
									step++;
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
								// �������� �ȳ� - End
								
								// ���̷����� ��������  ������ ��� - Start
								try {
									List<Map<String, String>> couponInfoMapList = (List<Map<String, String>>) sqlMap.queryForList(sqlId8, tradeMap.getOrder_no());
									if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
										dbMap.clear();
										dbMap.put ("storeCd", tradeMap.getStore_no ());
										dbMap.put ("posNo", tradeMap.getPos_no ());
										dbMap.put ("saleDate", tradeMap.getSale_date ());
										dbMap.put ("seqNo", tradeMap.getSeq_no ());
										
										for (Map<String, String> couponInfoMap : couponInfoMapList) {
											dbMap.putAll(couponInfoMap);
											resultCnt = msrSqlMap.update (sqlId7, dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ŷ� ���� ������  ������� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
								// ���̷����� ��������  ������ ��� - End
							}
							
							
						}//isMatched
						isMatched = true;	
						
						} // �������� ���� : 19-�Ϲݸ������ / MSR ���� : 20-�ֹ� ��û, 21-�ֹ� ����, 30-�ֹ� �Ϸ�,31-���� �Ϸ��� ��� MSR ���� 22-�ֹ���� ���ó��
						else if ( tradeMap.getSale_flag ().equals ("19") && ( tradeMap.getStatus ().equals ("20") || tradeMap.getStatus ().equals ("21") || tradeMap.getStatus ().equals ("30") || tradeMap.getStatus ().equals ("31") ) ) {
							
							/********** MSRȸ�����ΰ� '��ȸ��(J)'�̰ų� '��ȸ��(N)'�� ��� ��ȸ��(��/��) ������ ȸ�� ���� Start *****/
							
							Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
							String nonMemberRewardPracYn = conf.getString("nonMember.reward.practice.yn","N");
							
							if(StringUtils.equals (nonMemberRewardPracYn, "Y")){
								
								if("J".equals(tradeMap.getMsr_user_flag()) || "N".equals(tradeMap.getMsr_user_flag())){
									buf.delete (0, buf.length ()).append (logTitle).append ("��ȸ��(��/��) ������ ���� ȸ�� ���� Start !!!!!").append (" (xopOrdNo : ").append ((String) tradeMap.getOrder_no()).append (")");
									tLogger.info (buf.toString ());
									
									nonMembOrderMap.put("nextStatus", 		"22");											// ���� �� �ֹ�����(22:���� ���)
									nonMembOrderMap.put("userId", 			(String) tradeMap.getUser_id());				// ����� ���̵�
									nonMembOrderMap.put("totalPayAmt", 		(Integer) tradeMap.getTotal_pay_amt());			// �ֹ� �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�} 
									nonMembOrderMap.put("xopOrdNo", 		(String) tradeMap.getOrder_no());				// ���̷����� �ֹ���ȣ
									nonMembOrderMap.put("saleDate", 		(String) tradeMap.getSale_date());				// POS�ŷ� ������
									nonMembOrderMap.put("storeCd", 			(String) tradeMap.getStore_no());				// ���� �ڵ�
									nonMembOrderMap.put("posNo", 			(String) tradeMap.getPos_no());					// POS ��ȣ
									nonMembOrderMap.put("seqNo", 			(String) tradeMap.getSeq_no());					// POS �ŷ� ��ȣ
									
									/* ��ȸ�� ������ ���� ���� */
									nonMembRewResult = nonMemberRewardDao.rewardProcess(nonMembOrderMap, msrSqlMap);
									nonMembOrderMap.clear();
									
									buf.delete (0, buf.length ()).append (logTitle).append ("��ȸ��(��/��) ������ ���� ȸ�� ���� The End !!!!!");
									tLogger.info (buf.toString ());
								}
								
							}
							/********** MSRȸ�����ΰ� '��ȸ��(J)'�̰ų� '��ȸ��(N)'�� ��� ��ȸ��(��/��) ������ ȸ�� ���� The End *****/
							
							if(nonMembRewResult){
								String sqlId = "trade.tradeComplete";
								String sqlId2 = "trade.orderHistoryRegister";
								String sqlId3 = "paymentcancelMsr.insertTrdCouponListCancel";
								String sqlId4 = "paymentcancelXo.getCouponNumber";
								
								paramMap.put ("status", "22");
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());							
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("isCancelComp", "Y");
								
								sqlMap.update (sqlId, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								paramMap.clear ();
								
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("userId", tradeMap.getUser_id ());
								paramMap.put ("userName", tradeMap.getUser_name ());
								paramMap.put ("status", "22"); // 22-�ֹ����
								paramMap.put ("historyChannel", "5"); // 5-���
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("tranSeqNo", tradeMap.getTran_seq_no ());
								
								sqlMap.update (sqlId2, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� �̷� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								// ���̷����� �ֹ���ҽ�  ������� ��� ��� - Start
								try {
									List<Map<String, String>> couponInfoMapList = (List<Map<String, String>>) sqlMap.queryForList(sqlId4, tradeMap.getOrder_no());
									if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
										dbMap.clear();
										dbMap.put ("storeCd", tradeMap.getStore_no ());
										dbMap.put ("posNo", tradeMap.getPos_no ());
										dbMap.put ("saleDate", tradeMap.getSale_date ());
										dbMap.put ("seqNo", tradeMap.getSeq_no ());
										
										for (Map<String, String> couponInfoMap : couponInfoMapList) {
											dbMap.putAll(couponInfoMap);
											resultCnt = msrSqlMap.update (sqlId3, dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ŷ� ���� ������  ������� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
							}
							
							// ���̷����� �ֹ���ҽ�  ������� ��� ��� - End
							
						} else {
							String sqlId = "trade.tradeComplete";
							
							paramMap.put ("orderNo", tradeMap.getOrder_no ());
							
							sqlMap.update (sqlId, paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
						}
						
						paramMap.clear ();
						
						msrSqlMap.commitTransaction();
						sqlMap.commitTransaction ();
						
						commitCnt++;
					} catch ( Exception e ) {
						buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
						tLogger.error (buf.toString (), e);
					} finally {
						try {
							msrSqlMap.endTransaction();
							sqlMap.endTransaction ();
						} catch ( Exception ee ) {
							this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
						}
					}
					
					procCnt++;
				}

				//��� ���� �� DIFF SMS �߼�
				try{
					smsSend(trdDiffOrderNo,trdDiffKey,trdDiffCnt);
				}catch(Exception e){
					buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
					tLogger.error (buf.toString (), e);
				}

			}
			
		} catch ( Exception e ) {
			buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
			this.tLogger.error (buf.toString (), e);
			throw e;
		} finally {
			try {
				if (msrSqlMap != null) {
					msrSqlMap.endTransaction();
				}
				if (sqlMap != null) {
					sqlMap.endTransaction();
				}
			} catch ( Exception ee ) {
				this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
			}
		}
		
		return commitCnt;
	}
	
	/**
	 * �ֹ� ��� ��� ��ȸ
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<TradeCorrectionDto> getOrderCancelInfo () throws Exception {
		String sqlId = "trade.getOrderCancelInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.queryForList (sqlId);
	}
	
	/**
	 * �ֹ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 * @param tradeDate
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<TradeCorrectionDto> getOrderCancelDateInfo ( String tradeDate ) throws Exception {
		String sqlId = "trade.getOrderCancelDateInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.queryForList (sqlId, tradeDate);
	}
	
	/**
	 * �ֹ� ��� ó��
	 * @return int
	 * @throws Exception
	 */
	public int orderCancel ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		SqlMapClient sqlMap = null;
		long startTime;
		int commitCnt = 0;
		int procCnt = 1;
		StringBuffer buf = new StringBuffer ();
		
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			
			Map<String, String> paramMap = new HashMap<String, String> ();
			
			if ( tradeDtoInfo != null ) {
				for ( TradeCorrectionDto tradeMap : tradeDtoInfo ) {
					
					try {
						sqlMap.startTransaction ();
						
						startTime = System.currentTimeMillis ();
						
						int step = 1;
						
						buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (procCnt).append (" : ").append (tradeMap.getOrder_no ()).append (") ");
						String logTitle = buf.toString ();
						
						String sqlId = "trade.tradeComplete";
						String sqlId2 = "trade.orderHistoryRegister";
						
						paramMap.put ("status", "22");
						paramMap.put ("orderNo", tradeMap.getOrder_no ());
						paramMap.put ("isCancelComp", "Y");
						
						sqlMap.update (sqlId, paramMap);
						
						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
						tLogger.info (buf.toString ());
						
						paramMap.clear ();
						
						paramMap.put ("orderNo", tradeMap.getOrder_no ());
						paramMap.put ("userId", tradeMap.getUser_id ());
						paramMap.put ("userName", tradeMap.getUser_name ());
						paramMap.put ("status", "22"); // 22-�ֹ� ���
						paramMap.put ("historyChannel", "5"); // 5-���
						
						sqlMap.update (sqlId2, paramMap);
						
						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ֹ� ���� ���� �̷� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
						tLogger.info (buf.toString ());
						
						paramMap.clear ();
						
						sqlMap.commitTransaction ();
						
						commitCnt++;
					} catch ( Exception e ) {
						buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
						tLogger.error (buf.toString (), e);
					} finally {
						try {
							sqlMap.endTransaction ();
						} catch ( Exception ee ) {
							this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
						}
					}
					
					procCnt++;
				}
			}
			
		} catch ( Exception e ) {
			buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
			this.tLogger.error (buf.toString (), e);
			throw e;
		} finally {
			try {
				sqlMap.endTransaction ();
			} catch ( Exception ee ) {
				this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
			}
		}
		
		return commitCnt;
	}

	/**
	 * Ȧ���� (���ɿϷ� : O30, �̼��� ��� : O31) ��� ��� ��ȸ
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<TradeCorrectionDto> getWholecakeInfo(Map<String, Object> dbMap) throws Exception {
		String sqlId = "trade.getWholecakeInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.queryForList (sqlId, dbMap);
	}
	
	/**
	 * Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
	 * @param tradeWholecakeCorrectionList
	 * @return
	 * @throws Exception
	 */
	public int tradeWholecakeComplete(List<TradeCorrectionDto> tradeWholecakeCorrectionList) throws Exception {
		SqlMapClient sqlMap = null;
		SqlMapClient msrSqlMap = null;
		long startTime;
		int commitCnt = 0;
		int procCnt = 1;
		StringBuffer buf = new StringBuffer ();
		
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			Map<String, Object> dbMap = new HashMap<String, Object> (); 
			int resultCnt = 0;
			
			if(tradeWholecakeCorrectionList != null) {
				for(TradeCorrectionDto tradeWholecake : tradeWholecakeCorrectionList) {
					
					try {
						sqlMap.startTransaction ();
						msrSqlMap.startTransaction();
	
						startTime = System.currentTimeMillis ();
						
						int step = 1;
						
						buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (procCnt).append (" : ").append (tradeWholecake.getOrder_no ()).append (") ");
						String logTitle = buf.toString ();
	
						// �������� ���� : 11 - �Ϲݸ��� / Ȧ���� ���� : O30 - ���� �Ϸ� ���°� �ƴ� ��� ��� ó��
						// 1. ���ɿϷ�ó�� Ȯ��(�������� POS�� ���°��� ���̷������� ���°��� ������ Ȯ��
						if(tradeWholecake.getSale_flag().equals("11") && !tradeWholecake.getStatus().equals(WHOLECAKE_ORDER_STATUS_RECEIVED_COMP)) {
							
							paramMap.clear();
							paramMap.put ("status",      	 WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);  // ����(O30 : ���ɿϷ����)
							paramMap.put ("orderNo",     	 tradeWholecake.getOrder_no ());  	     // �����ȣ
							paramMap.put ("saleDate",    	 tradeWholecake.getSale_date ());        // ������
							paramMap.put ("storeCd",     	 tradeWholecake.getStore_no ());  	     // �����ڵ�
							paramMap.put ("posNo",       	 tradeWholecake.getPos_no ());    	     // POS��ȣ							
							paramMap.put ("seqNo",           tradeWholecake.getSeq_no ());           // �ŷ���ȣ
							paramMap.put ("receiveCompDate", tradeWholecake.getReceive_comp_date()); // ����/�̼��� �Ϸ�����
							
							// ���ɱ��� ���� ���ٸ� SAL_PAY_XO���� Ȯ���� ���ɱ���{O-�ֹ���ȣ, P-������ȣ}������ ������Ʈ
							if(StringUtils.isBlank(tradeWholecake.getReceive_type())) {
								paramMap.put ("receiveType", tradeWholecake.getSales_tbl_receive_type());
							}

							// 2. Ȧ���� ���°� ����							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());

							// 3. �����丮 ��� ó��
							paramMap.clear();
							paramMap.put ("orderNo",  tradeWholecake.getOrder_no ());  	     // �����ȣ
							paramMap.put ("status",   WHOLECAKE_ORDER_STATUS_RECEIVED_COMP); // ����(O30 : ���ɿϷ����)							
							paramMap.put ("saleDate", tradeWholecake.getSale_date ());       // ������
							paramMap.put ("storeCd",  tradeWholecake.getStore_no ());  	     // �����ڵ�
							paramMap.put ("posNo",    tradeWholecake.getPos_no ());    	     // POS��ȣ							
							paramMap.put ("seqNo",    tradeWholecake.getSeq_no ());          // �ŷ���ȣ

							sqlMap.update ("trade.wholecakeOrderHistoryRegister", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� �̷� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
							// 4. Ȧ���� ��������  ������ ��� - Start
							try {
								// Ȧ���� ����� ���� ������ȸ
								List<Map<String, Object>> couponInfoMapList = (List<Map<String, Object>>) sqlMap.queryForList("paymentcancelXo.getWholecakeOrderCouponNumber", tradeWholecake.getOrder_no());
								if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
									dbMap.clear();
									dbMap.put ("storeCd",  tradeWholecake.getStore_no ());
									dbMap.put ("posNo",    tradeWholecake.getPos_no ());
									dbMap.put ("saleDate", tradeWholecake.getSale_date ());
									dbMap.put ("seqNo",    tradeWholecake.getSeq_no ());
									
									for (Map<String, Object> couponInfoMap : couponInfoMapList) {
										dbMap.putAll(couponInfoMap);
										
										// msr_trd_coupon_list�� ��ϵ� ���� ��ȣ�� �ִ��� Ȯ��
										int checkCount = (Integer) msrSqlMap.queryForObject("paymentcancelMsr.getTrdCouponCount", dbMap);

										// ��ϵ� ���� ��ȣ�� ���ٸ� ���
										if(checkCount == 0) {
											resultCnt = msrSqlMap.update ("paymentcancelMsr.insertTrdCouponList", dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ŷ� ���� ������  ������� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								}
							} catch (Exception e) {
								buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
								tLogger.error (buf.toString (), e);
							}
							
						} else {
						    // 5. �����ϴٸ� Ȧ���� �������� ��� �Ϸ�ó��(trade_flag) Y ó��
							paramMap.clear();
							paramMap.put ("orderNo", tradeWholecake.getOrder_no ()); // �����ȣ
							
							// 6. Ȧ���� ���°� ����							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
						}
						
						msrSqlMap.commitTransaction();
						sqlMap.commitTransaction ();
						commitCnt++;
						
					} catch ( Exception e ) {
						buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
						tLogger.error (buf.toString (), e);
					} finally {
						try {
							msrSqlMap.endTransaction();
							sqlMap.endTransaction ();
						} catch ( Exception ee ) {
							this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
						}
					}
					
					procCnt++; 
				}
			}

		} catch ( Exception e ) {
			buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
			this.tLogger.error (buf.toString (), e);
			throw e;
		} finally {
			try {
				msrSqlMap.endTransaction();
				sqlMap.endTransaction ();
			} catch ( Exception ee ) {
				this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
			}
		}
		
		return commitCnt;
	}

	/**
	 * Ȧ���� �̼��� ��� ��� �Ϸ� ó��
	 * @param receivedDisuseWholecakeList
	 * @return
	 * @throws Exception
	 */
	public int receivedDisuseWholecakeComplete(List<TradeCorrectionDto> receivedDisuseWholecakeList) throws Exception {
		
		SqlMapClient sqlMap = null;
		SqlMapClient msrSqlMap = null;
		long startTime;
		int commitCnt = 0;
		int procCnt = 1;
		StringBuffer buf = new StringBuffer ();
		
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			Map<String, Object> dbMap = new HashMap<String, Object> (); 
			int resultCnt = 0;
			
			if(receivedDisuseWholecakeList != null) {
				for(TradeCorrectionDto tradeWholecake : receivedDisuseWholecakeList) {
					
					try {
						sqlMap.startTransaction ();
						msrSqlMap.startTransaction();
	
						startTime = System.currentTimeMillis ();
						
						int step = 1;
						
						buf.delete (0, buf.length ()).append (this.loggerTitle).append ("(").append (procCnt).append (" : ").append (tradeWholecake.getOrder_no ()).append (") ");
						String logTitle = buf.toString ();
	
						// �������� ���� : 11 - �Ϲݸ��� / Ȧ���� ���� : O31 - �̼��� ��� ���°� �ƴ� ��� ��� ó��
						// 1. �̼��� ���ó�� Ȯ��(�������� POS�� ���°��� ���̷������� ���°��� ������ Ȯ��
						if(tradeWholecake.getSale_flag().equals("11") && !tradeWholecake.getStatus().equals(WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE)) {
							
							paramMap.clear();
							paramMap.put ("status",      	 WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE); // ����(O31 : �̼��� ������)
							paramMap.put ("orderNo",     	 tradeWholecake.getOrder_no ());  	      // �����ȣ
							paramMap.put ("saleDate",    	 tradeWholecake.getSale_date ());         // ������
							paramMap.put ("storeCd",     	 tradeWholecake.getStore_no ());  	      // �����ڵ�
							paramMap.put ("posNo",       	 tradeWholecake.getPos_no ());    	      // POS��ȣ							
							paramMap.put ("seqNo",           tradeWholecake.getSeq_no ());            // �ŷ���ȣ
							paramMap.put ("receiveCompDate", tradeWholecake.getReceive_comp_date());  // ����/�̼��� �Ϸ�����

							// ���ɱ��� ���� ���ٸ� SAL_PAY_XO���� Ȯ���� ���ɱ���{O-�ֹ���ȣ, P-������ȣ}������ ������Ʈ
							if(StringUtils.isBlank(tradeWholecake.getReceive_type())) {
								paramMap.put ("receiveType", tradeWholecake.getSales_tbl_receive_type());
							}
							
							// 2. Ȧ���� ���°� ����							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());

							// 3. �����丮 ��� ó��
							paramMap.clear();
							paramMap.put ("orderNo",  tradeWholecake.getOrder_no ());  	       // �����ȣ
							paramMap.put ("status",   WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE); // ����(O31 : �̼��� ������)							
							paramMap.put ("saleDate", tradeWholecake.getSale_date ());         // ������
							paramMap.put ("storeCd",  tradeWholecake.getStore_no ());  	       // �����ڵ�
							paramMap.put ("posNo",    tradeWholecake.getPos_no ());    	       // POS��ȣ							
							paramMap.put ("seqNo",    tradeWholecake.getSeq_no ());            // �ŷ���ȣ

							sqlMap.update ("trade.wholecakeOrderHistoryRegister", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� �̷� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
							// 4. Ȧ���� ��������  ������ ��� - Start
							try {
								// Ȧ���� ����� ���� ������ȸ
								List<Map<String, Object>> couponInfoMapList = (List<Map<String, Object>>) sqlMap.queryForList("paymentcancelXo.getWholecakeOrderCouponNumber", tradeWholecake.getOrder_no());
								if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
									dbMap.clear();
									dbMap.put ("storeCd",  tradeWholecake.getStore_no ());
									dbMap.put ("posNo",    tradeWholecake.getPos_no ());
									dbMap.put ("saleDate", tradeWholecake.getSale_date ());
									dbMap.put ("seqNo",    tradeWholecake.getSeq_no ());
									
									for (Map<String, Object> couponInfoMap : couponInfoMapList) {
										dbMap.putAll(couponInfoMap);
										
										// msr_trd_coupon_list�� ��ϵ� ���� ��ȣ�� �ִ��� Ȯ��
										int checkCount = (Integer) msrSqlMap.queryForObject("paymentcancelMsr.getTrdCouponCount", dbMap);
										
										// ��ϵ� ���� ��ȣ�� ���ٸ� ���
										if(checkCount == 0) {
											resultCnt = msrSqlMap.update ("paymentcancelMsr.insertTrdCouponList", dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �ŷ� ���� ������  ������� ��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								}
							} catch (Exception e) {
								buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
								tLogger.error (buf.toString (), e);
							}
							
						} else {
						    // 5. �����ϴٸ� Ȧ���� �������� ��� �Ϸ�ó��(trade_flag) Y ó��
							paramMap.clear();
							paramMap.put ("orderNo", tradeWholecake.getOrder_no ()); // �����ȣ
							
							// 6. Ȧ���� ���°� ����							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". ���� ���� ���� ���� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
						}
						
						msrSqlMap.commitTransaction();
						sqlMap.commitTransaction ();
						commitCnt++;
						
					} catch ( Exception e ) {
						buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
						tLogger.error (buf.toString (), e);
					} finally {
						try {
							msrSqlMap.endTransaction();
							sqlMap.endTransaction ();
						} catch ( Exception ee ) {
							this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
						}
					}
					
					procCnt++; 
				}
			}

		} catch ( Exception e ) {
			buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
			this.tLogger.error (buf.toString (), e);
			throw e;
		} finally {
			try {
				msrSqlMap.endTransaction();
				sqlMap.endTransaction ();
			} catch ( Exception ee ) {
				this.tLogger.error (ee);		// PMD ����( PMD ���� : printStackTrace()��� )
			}
		}
		
		return commitCnt;
	}
	
	
	/**
	 * ���� ����� ���� �� INBOX �߼�
	 * @param sqlMap
	 * @param msrSqlMap
	 * @param homeSqlMap
	 * @param tradeMap
	 * @param pLogTitle
	 * @param pStep
	 * @param pStartTime
	 */
	public void sendSurveyInfo(SqlMapClient sqlMap, SqlMapClient msrSqlMap, SqlMapClient homeSqlMap, TradeCorrectionDto tradeMap, String pLogTitle, int pStep, long pStartTime) {
		String logTitle = pLogTitle;
		int step 		= pStep;
		long startTime  = pStartTime;
		
		MsrvInfrmMasterDto msrvMasterDto = null;
		StringBuffer buf  = new StringBuffer();
		String userId 	  = StringUtils.defaultString(tradeMap.getUser_id());
		String xopOrdNo   = StringUtils.defaultString(tradeMap.getOrder_no());
		String msrvLogStr = "[sendSurveyInfo]["+userId+"]["+xopOrdNo+"]";
		
		try {
			homeSqlMap.startTransaction();
			
			boolean isInsPushHist = false;	// PUSH �̷µ�� ���ɿ��� üũ
    		boolean isSurveyInbox = false;	// PUSH �߼� ���ɿ��� üũ
    		String mbbrDvsnCode   = "N"; 	// ȸ�������ڵ�{N:�Ϲݴ����, F:���Ϸ�_Ÿ����X, P:���Ϸ�_Ÿ����O}
    		String pushOrderNo    = null;	// PUSH �߼۽� ����� ���̷����� �ֹ���ȣ
			Map<String, String> dbMap = new HashMap<String, String>();
			
			// ���̽�Ÿ�������� ������ ���� ��ȸ
			dbMap.clear();
			dbMap.put("order_no", xopOrdNo);
			dbMap.put("user_id",  userId);
			msrvMasterDto = (MsrvInfrmMasterDto) sqlMap.queryForObject("msReview.getMsrvInfrmMasterInfo", dbMap);
			if (msrvMasterDto == null || msrvMasterDto.getMsrvSrnm() <= 0) {
    			throw new XOException(msrvLogStr+":��ȿ�Ѽ�������");
    		} else {
    			tLogger.info(logTitle+msrvLogStr+":"+msrvMasterDto.toString());
    		}
			
			// MSR ȸ������ ��ȸ
			CardRegMemberDto msrMemberDto = (CardRegMemberDto) msrSqlMap.queryForObject("paymentcancelMsr.getRegMember", userId);
			if (msrMemberDto == null || StringUtils.isBlank(msrMemberDto.getUser_number())) {
				throw new XOException(msrvLogStr+":MSRȸ���ƴ�");
    		}
			
			// ���� �ֹ��� �ֹ��Ϸ�(30), �����Ϸ�(31) �������� üũ
   			if (StringUtils.isBlank(msrvMasterDto.getCurrOrderDate())) {
   				throw new XOException(msrvLogStr+":�����ֹ���{30,31}���¾ƴ�/�����ֹ��Ǿƴ�");
   			}
			
   			// ������������ѿ����� ��� �����ο� �ʰ��� ���� ���� �Ұ�
   			if (StringUtils.equals(msrvMasterDto.getPrtcnTrprRstrcYn(), "Y")) {
   				dbMap.clear();
   				dbMap.put("msrv_srnm", msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);
   				Integer prtcnPrcnt = (Integer)sqlMap.queryForObject("msReview.getMsrvPrtcnPrcnt", dbMap);
   				
   				if (prtcnPrcnt != null && (msrvMasterDto.getPrtcnTrprRstrcPrcnt() <= prtcnPrcnt)) {
   					throw new XOException(msrvLogStr+":���������ο�_OVER{"+msrvMasterDto.getPrtcnTrprRstrcPrcnt()+"/"+prtcnPrcnt+"}");
   				}
   			}
			
   			// ���� �������� ��ȸ (���� �Ϸ��ڴ� ����� ����)
   			dbMap.clear();
   			dbMap.put("scs_idx", msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);		
   			dbMap.put("user_id", userId);
   			SurveyDto surveyDto = (SurveyDto) homeSqlMap.queryForObject("survey.getSurveyJoinInfo", dbMap);
   			if (surveyDto == null) {
   				throw new XOException(msrvLogStr+":ȸ����������{"+userId+"}");
   			} else if (!StringUtils.equals(surveyDto.getSurvey_yn(), "Y")) {
   				throw new XOException(msrvLogStr+":���������Ϸ�");
   			}
   			tLogger.info(logTitle+msrvLogStr+":"+surveyDto.toMsrvString());
   			
   			/*** �ǽð� Ÿ���� ���� üũ ***/
   			pushOrderNo = this.msrvRealTimeChk(sqlMap, msrvMasterDto, surveyDto, pLogTitle);
   			
   			// INBOX�� �ֹ���ȣ�� ���� �ֹ���ȣ�� �����ϸ� ���� �ֹ��� Ÿ���� ������ ����
			// ���� �ֹ��� Ÿ���� ������ �����ϴ� ��쿡�� ����� �̷� ���
   			if (StringUtils.equals(pushOrderNo, xopOrdNo)) {
   				isInsPushHist = true;
   				isSurveyInbox = true;
   			} else {
   				isInsPushHist = false;
   				isSurveyInbox = false;
   			}
   			
   			// ���̽�Ÿ�������� ��������� �̷µ�� �� ���� ���� �����ο��� ������Ʈ
   			if (isInsPushHist) {
   				dbMap.clear();
   				dbMap.put("msrv_srnm",  		 msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);
   				dbMap.put("msrmb_id",  			 userId);
   				dbMap.put("msrv_mbbr_dvsn_code", mbbrDvsnCode);
   				dbMap.put("sror_order_no", 		 xopOrdNo);
   				
   				// ���̽�Ÿ�������� ��������� �̷µ��
   				sqlMap.insert("msReview.insertMsrvPrtcnTrprH", dbMap);
   				
   				/*
   				// �ǽð� ��ȸ�� ���� (������ ���̺� ������Ʈ �̽�)
   				// ������������� ������ ��� ���� �����ο��� ������Ʈ
   				if (StringUtils.equals(msrvMasterDto.getPrtcnTrprRstrcYn(), "Y")) {
   					sqlMap.update("msReview.updateMsrvPrtcnPrcnt", dbMap);
   	   			}
   				*/
   				
   				sqlMap.commitTransaction();
   			} else {
   				// �ǽð�Ÿ���� ������ �����ϴ� ������ ���
   				if (StringUtils.equals(msrvMasterDto.getMsrvDvsnCode(), "S") || StringUtils.equals(msrvMasterDto.getMsrvDvsnCode(), "T")) {
   					// ���� �ֹ��� Ÿ���� ������ �������� �ʴ��� ���� �ֹ����� Ÿ���� ������ �����ϴ� ��� PUSH �߼�
   					dbMap.clear();
   	   				dbMap.put("msrv_srnm", msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);
   	   				dbMap.put("msrmb_id",  userId);
   					String preOrderNo = (String)sqlMap.queryForObject("msReview.getOrderNoOfPushHist", dbMap);
   					if (StringUtils.isNotBlank(preOrderNo)) {
   						isSurveyInbox = true;
   						pushOrderNo = preOrderNo;
   					}
   				}
   			}
   			tLogger.info(logTitle+msrvLogStr+":xoOrdNo->"+xopOrdNo+",pushOrdNo->"+pushOrderNo+",�̷µ��->"+isInsPushHist+",INBOX���->"+isSurveyInbox);
   			
   			if (isSurveyInbox) {
   				//���� �����Ϸ�� �����ȳ� �ιڽ� ���
				UserInboxDto dto = new UserInboxDto();
				dto.setSui_app_yn("Y");
				dto.setSui_web_yn("Y");
				dto.setSui_link_yn("Y");
				dto.setSui_user_id(userId);
				dto.setSui_title("[���� ��Ÿ���� ����] ���� �����Ͻð� �߰� ���� ���õ� �Բ� ��������!");
				dto.setSui_url("starbucks://callCvPage");
				homeSqlMap.insert("survey.insertUserInBoxMsg", dto);
				
				homeSqlMap.commitTransaction();
				
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". �������� �ȳ� inbox��� END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				tLogger.info (buf.toString ());
   			} else {
   				tLogger.info(logTitle+msrvLogStr+":��������ھƴ�");
   			}
		} catch(XOException xoEx) {
			buf.delete(0, buf.length()).append(logTitle).append("\t").append("SurveyValidFail:").append(xoEx.getMessage());
			tLogger.info(buf.toString ());
		} catch(Exception ex) {
			buf.delete (0, buf.length ()).append (logTitle).append("\t").append ("SurveyException:").append (msrvLogStr).append (ex.getMessage());
			tLogger.info (buf.toString());
			tLogger.error(buf.toString());
		} finally {
			try {
				if (homeSqlMap != null) {
					homeSqlMap.endTransaction();
				}
			} catch(Exception e) {
				this.tLogger.error (e);
			}
		}
	}
	
	/**
	 * �ǽð� Ÿ���� ���� üũ(�б�)
	 * @param sqlMap
	 * @param msrvDto
	 * @param surveyDto
	 * @param pLogTitle
	 * @return String
	 * @throws Exception
	 */
	public String msrvRealTimeChk(SqlMapClient sqlMap, MsrvInfrmMasterDto msrvDto, SurveyDto surveyDto, String pLogTitle) throws Exception {
		String pushOrderNo = null;
		Map<String, String> dbMap = new HashMap<String, String>();
		
		// �����ڵ� {X:����+�ǱⰣ����, B:����������, S:�ǽð�������, T:����+�ǽð��Ѵ�����}
		String msrvDvsnCode = StringUtils.defaultIfEmpty(msrvDto.getMsrvDvsnCode(), "X");
		
		// ���������+�ǽð����� ����
		if (msrvDvsnCode.equals("X")) {
			pushOrderNo = msrvDto.getOrderNo();
			
		// ��������� ���Ǹ� ����
		} else if (msrvDvsnCode.equals("B")) {
			// ���̽�Ÿ�������� ��������� ���� ��ȸ
			dbMap.clear();
			dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
			dbMap.put("msrmb_id",  msrvDto.getUserId());
			Integer preMemberCnt = (Integer)sqlMap.queryForObject("msReview.getMsrvBfrhnTrprCount", dbMap);
			// ��������ڷ� ������ ���
			if (preMemberCnt != null && preMemberCnt > 0) {
				pushOrderNo = msrvDto.getOrderNo();
			}
			
		// �ǽð� ���Ǹ� ����
		} else if (msrvDvsnCode.equals("S")) {
			pushOrderNo = this.msrvRealTimeProc(sqlMap, msrvDto, surveyDto, pLogTitle);
			
		// ��������� + �ǽð� ���� �Ѵ� ����
		} else if (msrvDvsnCode.equals("T")) {
			// ���̽�Ÿ�������� ��������� ���� ��ȸ
			dbMap.clear();
			dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
			dbMap.put("msrmb_id",  msrvDto.getUserId());
			Integer preMemberCnt = (Integer)sqlMap.queryForObject("msReview.getMsrvBfrhnTrprCount", dbMap);
			// ��������ڷ� ������ ���
			if (preMemberCnt != null && preMemberCnt > 0) {
				pushOrderNo = this.msrvRealTimeProc(sqlMap, msrvDto, surveyDto, pLogTitle);	
			}
		}
		
		return pushOrderNo;
	}
	
	/**
	 * �ǽð� Ÿ���� ���� üũ(ó��)
	 * @param sqlMap
	 * @param msrvDto
	 * @param surveyDto
	 * @param pLogTitle
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String msrvRealTimeProc(SqlMapClient sqlMap, MsrvInfrmMasterDto msrvDto, SurveyDto surveyDto, String pLogTitle) throws Exception {
		String finalOrderNo = null;
		String logTitle 	= pLogTitle;
		Map<String, String> dbMap = new HashMap<String, String>();
		
		
		try {
			// 1. ����üũ
			if (!StringUtils.equals(msrvDto.getGnrCode(), "A")) {
				if (!StringUtils.equals(msrvDto.getGnrCode(), surveyDto.getGender())) {
					throw new XOException("����üũFail{����:"+msrvDto.getGnrCode()+"/����:"+surveyDto.getGender()+"}");
				}
			}
			
			// MSR��� üũ���� �Ǵ�
			String grdChkYn = "N";
			if (StringUtils.isNotBlank(msrvDto.getMsrGradeCodeDtls())) {
				grdChkYn = "Y";
			}
			// �ð������� ��ϼ��� ��ȸ
			String tmeRgsttSrnm = "";
			if (StringUtils.equals(msrvDto.getOrderTmeAplctYn(), "Y")) {
				dbMap.clear();
				dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
				tmeRgsttSrnm = (String)sqlMap.queryForObject("msReview.getTmeRgsttSrnm", dbMap);
			}
			
			// 2. �ǽð����� üũ�� ���� param ����
			dbMap.clear();
			dbMap.put("msrv_srnm", 	    msrvDto.getMsrvSrnm()+StringUtils.EMPTY);	// ���̽�Ÿ�����������
			dbMap.put("birth_yrwn",     surveyDto.getBirth_year());					// ����⵵
			dbMap.put("user_id", 	    msrvDto.getUserId());						// �����ID
			dbMap.put("curr_order_no",  msrvDto.getOrderNo());						// ���̷������ֹ���ȣ
			dbMap.put("bir_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getBirthYrwnAplctYn(),  "N"));	// ����⵵���뿩��
			dbMap.put("grd_chk_yn",     grdChkYn);									// MSR������뿩��
			dbMap.put("str_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getStoreAtrbtAplctYn(), "N")); 	// ����Ӽ����뿩��
			dbMap.put("tme_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getOrderTmeAplctYn(),   "N"));	// �ֹ��ð����뿩��
			dbMap.put("tme_rgstt_srnm", tmeRgsttSrnm);								// �ð�������ϼ���
			dbMap.put("sku_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getGoodsInfrmAplctYn(), "N"));	// ��ǰ�������뿩��
			dbMap.put("sku_prn_yn",     StringUtils.defaultIfEmpty(msrvDto.getGoodsInfrmPrngYn(),  "N"));	// ��ǰ����������
			
			// 3. ���̽�Ÿ�������� �ǽð�üũ ������ �ֹ���ȣ ��ȸ
			List<String> orderNoList = (List<String>)sqlMap.queryForList("msReview.getMsrvOrderNoChkList", dbMap);
			
			if (orderNoList != null && orderNoList.size() > 0) {
				finalOrderNo = orderNoList.get(0);	// ù��° ���̷����� �ֹ���ȣ ����(���� �ֱ� �ֹ���ȣ)
			}
		} catch(XOException xoEx) {
			tLogger.info(logTitle+"\t"+"SurveyValidFail->"+xoEx.getMessage());
			finalOrderNo = null;
		} catch(Exception ex) {
			tLogger.error(logTitle+"\t"+"SurveyException->"+ex.getMessage());
			finalOrderNo = null;
		}
		
		return finalOrderNo;
	}
}
