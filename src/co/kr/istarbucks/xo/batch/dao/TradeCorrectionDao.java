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
 * 영업정보 거래 대사 TradeCorrectionDao.
 * @author eZEN
 * @since 2014. 2. 6.
 * @version $Revision: 1.16 $
 */
public class TradeCorrectionDao {
	
	private final Logger tLogger = Logger.getLogger ("TRADE");
	
	private static final String loggerTitle = "[TradeCorrection] "; 				// PMD 적용( PMD 에러 : final field는 static final로 변경 )

	
	/** 수령 완료 **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_COMP   = "O30"; // 수령 완료
	/** 미수령 폐기 **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE = "O31"; // 미수령 폐기
	
	/**
	 * 거래 대사 대상 조회
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
	 * 거래 대사 대상 조회 - 거래 날짜 입력
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
	 * XO_PAYMENT# STATUS 조회
	 * @param orderno
	 * @return string
	 * @throws Exception
	 */
	
	@SuppressWarnings ( "unchecked" )
	public String getXoPaymentStatusInfo(String orderno) throws SQLException {
		// 2020.09.10 정종모 추가
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("orderNo", orderno);
		return (String) sqlMap.queryForObject("trade.getXoPaymentStatusInfo", paramMap);
	}
	
	// XO대사 상이건 결과 SMS 발송
		private void smsSend(String orderNo,String trdKey, int Cnt) {
			
			SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // 사이렌오더 트렌젝션
			MMSMgr mmsMgr;
			StringBuffer logSb = new StringBuffer ();
			mmsMgr = new MMSMgr();
			try {
				
				sqlMap.startTransaction();

				Configuration config = null;
				config = CommPropertiesConfiguration.getConfiguration("sms.properties");

				String content="[XO대사 DIFF]\n";
				if(Cnt==0){
					content += "차이 : 0건";
				}else if(Cnt==1){
                	content += "거래키 : "+trdKey;
                }else if(Cnt > 1){
                	content += "거래키 : "+trdKey+" 외 "+(Cnt-1)+"건";
                }
              
				String callback = config.getString("wholecake.sms.callback");
				String recipientNum = config.getString("xotrade.diff.sms.receive.info");
				String recipientArr[] = recipientNum.split("\\|");

		        // SMS 발송 요청 
				for(int i = 0; i < recipientArr.length; i++) {
			        SmtTranDto smtTranDto = new SmtTranDto ();
			        smtTranDto.setPriority ("S"); //전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
			        smtTranDto.setContent (content);
			        smtTranDto.setCallback (callback);
			        smtTranDto.setRecipient_num (recipientArr[i]);
			        mmsMgr.insertSmtTran (sqlMap, smtTranDto);
			        sqlMap.commitTransaction (); // commit
				}
		        
			} catch (Exception e) {
				logSb.delete(0, logSb.length ()).append ("[XO대사 MSR/통영 주문상태 DIFF] SMS 발송 실패 : ").append (e.getMessage());
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
	 * 거래 대사 완료 처리
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
		
		NonMemberRewardDao nonMemberRewardDao = new NonMemberRewardDao();		// 비회원(준/웹) 리워드 적립 / 회수 NonMemberRewardDao.
		
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			
			Map<String, String> paramMap = new HashMap<String, String> ();
			Map<String, String> dbMap = new HashMap<String, String> ();
			int resultCnt = 0;
			Boolean nonMembRewResult = true;									// 비회원 리워드 실적 적립 / 회수 결과값
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
						
						// 비회원(준/웹) 리워드 실적 저장 및 회수를 위한 Parm 지정
						Map<String, Object> nonMembOrderMap = new HashMap<String, Object> ();
						
						// 영업정보 상태 : 11-일반매출 / MSR 상태 : 30-주문 완료,31-제조 완료가 아닌 경우 MSR 상태 31-제조완료 대사처리  
						if ( tradeMap.getSale_flag ().equals ("11") && ( !tradeMap.getStatus ().equals ("30") && !tradeMap.getStatus ().equals ("31") ) ) {
							
							// 2020.09.10 정종모 추가
							// XO_ORDER#(결제취소, 주문취소), XO_PAYMENT#(결제취소) 경우 플래그 체크
							if(tradeMap.getSale_flag ().equals ("11") && (tradeMap.getStatus ().equals ("12") || tradeMap.getStatus ().equals ("22")) ){
								
								try{
								
								String xoPaymentStatus = StringUtils.defaultIfEmpty(getXoPaymentStatusInfo(tradeMap.getOrder_no() ), "");
								
								// XO_ORDER#(결제취소, 주문취소), XO_PAYMENT#(결제취소) 경우 대사 미처리
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
							
							// 2020.09.10 정종모 추가
							// XO_ORDER#(결제취소, 주문취소), XO_PAYMENT#(결제취소) 경우 대사 미처리
							if(isMatched){
								
							/********** MSR회원여부가 '준회원(J)'이거나 '웹회원(N)'일 경우 비회원(준/웹) 리워드 실적 적립 진행 Start *****/
							Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
							String nonMemberRewardPracYn = conf.getString("nonMember.reward.practice.yn","N");
							
							if(StringUtils.equals (nonMemberRewardPracYn, "Y")){
								if("J".equals(tradeMap.getMsr_user_flag()) || "N".equals(tradeMap.getMsr_user_flag())){
									buf.delete (0, buf.length ()).append (logTitle).append ("비회원(준/웹) 리워드 실적 적립 진행 Start !!!!!").append (" (xopOrdNo : ").append ((String) tradeMap.getOrder_no()).append (")");
									tLogger.info (buf.toString ());
									
									nonMembOrderMap.put("nextStatus", 		"31");											// 변경 될 주문상태(31:제조완료)
									nonMembOrderMap.put("userId", 			(String) tradeMap.getUser_id());				// 사용자 아이디
									nonMembOrderMap.put("totalPayAmt", 		(Integer) tradeMap.getTotal_pay_amt());			// 주문 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액} 
									nonMembOrderMap.put("xopOrdNo", 		(String) tradeMap.getOrder_no());				// 사이렌오더 주문번호
									nonMembOrderMap.put("saleDate", 		(String) tradeMap.getSale_date());				// POS거래 영업일
									nonMembOrderMap.put("storeCd", 			(String) tradeMap.getStore_no());				// 매장 코드
									nonMembOrderMap.put("posNo", 			(String) tradeMap.getPos_no());					// POS 번호
									nonMembOrderMap.put("seqNo", 			(String) tradeMap.getSeq_no());					// POS 거래 번호
									
									/* 비회원(준/웹) 리워드 실적 적립 */
									nonMembRewResult = nonMemberRewardDao.rewardProcess(nonMembOrderMap, msrSqlMap);
									nonMembOrderMap.clear();
									
									buf.delete (0, buf.length ()).append (logTitle).append ("비회원(준/웹) 리워드 실적 적립 진행 The End !!!!!");
									tLogger.info (buf.toString ());
								}
							}
							/********** MSR회원여부가 '준회원(J)'이거나 '웹회원(N)'일 경우 비회원(준/웹) 리워드 실적 적립 진행 The End *****/
							
							if(nonMembRewResult){
								String sqlId = "trade.tradeComplete";
								String sqlId2 = "trade.orderHistoryRegister";							
								//String sqlId3 = "survey.getSurveyJoinInfo";			// 마스리 관련 삭제(미사용)
								//String sqlId4 = "survey.insertUserInBoxMsg";			// 마스리 관련 삭제(미사용)
								//String sqlId5 = "trade.getOrderMakeComplateDate";		// 마스리 관련 삭제(미사용)
								//String sqlId6 = "paymentcancelMsr.getRegMember";		// 마스리 관련 삭제(미사용)
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
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								paramMap.clear ();
								
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("userId", tradeMap.getUser_id ());
								paramMap.put ("userName", tradeMap.getUser_name ());
								paramMap.put ("status", "31"); // 31-제조완료
								paramMap.put ("historyChannel", "5"); // 5-대사
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("tranSeqNo", tradeMap.getTran_seq_no ());
								
								sqlMap.update (sqlId2, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 이력 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								// 설문조사 안내 - Start
								try {
									// 설문 대상자 선정 및 INBOX 발송(BATCH의 경우 PUSH 발송은 하지않음)
									this.sendSurveyInfo(sqlMap, msrSqlMap, homeSqlMap, tradeMap, logTitle, step, startTime);
									step++;
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
								// 설문조사 안내 - End
								
								// 사이렌오더 쿠폰사용시  사용매장 등록 - Start
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
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 거래 쿠폰 내역에  쿠폰사용 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
								// 사이렌오더 쿠폰사용시  사용매장 등록 - End
							}
							
							
						}//isMatched
						isMatched = true;	
						
						} // 영업정보 상태 : 19-일반매출취소 / MSR 상태 : 20-주문 요청, 21-주문 승인, 30-주문 완료,31-제조 완료인 경우 MSR 상태 22-주문취소 대사처리
						else if ( tradeMap.getSale_flag ().equals ("19") && ( tradeMap.getStatus ().equals ("20") || tradeMap.getStatus ().equals ("21") || tradeMap.getStatus ().equals ("30") || tradeMap.getStatus ().equals ("31") ) ) {
							
							/********** MSR회원여부가 '준회원(J)'이거나 '웹회원(N)'일 경우 비회원(준/웹) 리워드 회수 진행 Start *****/
							
							Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
							String nonMemberRewardPracYn = conf.getString("nonMember.reward.practice.yn","N");
							
							if(StringUtils.equals (nonMemberRewardPracYn, "Y")){
								
								if("J".equals(tradeMap.getMsr_user_flag()) || "N".equals(tradeMap.getMsr_user_flag())){
									buf.delete (0, buf.length ()).append (logTitle).append ("비회원(준/웹) 리워드 실적 회수 진행 Start !!!!!").append (" (xopOrdNo : ").append ((String) tradeMap.getOrder_no()).append (")");
									tLogger.info (buf.toString ());
									
									nonMembOrderMap.put("nextStatus", 		"22");											// 변경 될 주문상태(22:승인 취소)
									nonMembOrderMap.put("userId", 			(String) tradeMap.getUser_id());				// 사용자 아이디
									nonMembOrderMap.put("totalPayAmt", 		(Integer) tradeMap.getTotal_pay_amt());			// 주문 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액} 
									nonMembOrderMap.put("xopOrdNo", 		(String) tradeMap.getOrder_no());				// 사이렌오더 주문번호
									nonMembOrderMap.put("saleDate", 		(String) tradeMap.getSale_date());				// POS거래 영업일
									nonMembOrderMap.put("storeCd", 			(String) tradeMap.getStore_no());				// 매장 코드
									nonMembOrderMap.put("posNo", 			(String) tradeMap.getPos_no());					// POS 번호
									nonMembOrderMap.put("seqNo", 			(String) tradeMap.getSeq_no());					// POS 거래 번호
									
									/* 비회원 리워드 실적 적립 */
									nonMembRewResult = nonMemberRewardDao.rewardProcess(nonMembOrderMap, msrSqlMap);
									nonMembOrderMap.clear();
									
									buf.delete (0, buf.length ()).append (logTitle).append ("비회원(준/웹) 리워드 실적 회수 진행 The End !!!!!");
									tLogger.info (buf.toString ());
								}
								
							}
							/********** MSR회원여부가 '준회원(J)'이거나 '웹회원(N)'일 경우 비회원(준/웹) 리워드 회수 진행 The End *****/
							
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
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								paramMap.clear ();
								
								paramMap.put ("orderNo", tradeMap.getOrder_no ());
								paramMap.put ("userId", tradeMap.getUser_id ());
								paramMap.put ("userName", tradeMap.getUser_name ());
								paramMap.put ("status", "22"); // 22-주문취소
								paramMap.put ("historyChannel", "5"); // 5-대사
								paramMap.put ("storeCd", tradeMap.getStore_no ());
								paramMap.put ("posNo", tradeMap.getPos_no ());
								paramMap.put ("saleDate", tradeMap.getSale_date ());
								paramMap.put ("seqNo", tradeMap.getSeq_no ());
								paramMap.put ("tranSeqNo", tradeMap.getTran_seq_no ());
								
								sqlMap.update (sqlId2, paramMap);
								
								buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 이력 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
								tLogger.info (buf.toString ());
								
								// 사이렌오더 주문취소시  쿠폰사용 취소 등록 - Start
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
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 거래 쿠폰 내역에  쿠폰취소 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
												tLogger.info (buf.toString ());
											}
										}
									}
								} catch (Exception e) {
									buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
									tLogger.error (buf.toString (), e);
								}
							}
							
							// 사이렌오더 주문취소시  쿠폰사용 취소 등록 - End
							
						} else {
							String sqlId = "trade.tradeComplete";
							
							paramMap.put ("orderNo", tradeMap.getOrder_no ());
							
							sqlMap.update (sqlId, paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
							this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
						}
					}
					
					procCnt++;
				}

				//대사 종료 후 DIFF SMS 발송
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
				this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
			}
		}
		
		return commitCnt;
	}
	
	/**
	 * 주문 취소 대상 조회
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
	 * 주문 취소 대상 조회 - 거래 날짜 입력
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
	 * 주문 취소 처리
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
						
						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
						tLogger.info (buf.toString ());
						
						paramMap.clear ();
						
						paramMap.put ("orderNo", tradeMap.getOrder_no ());
						paramMap.put ("userId", tradeMap.getUser_id ());
						paramMap.put ("userName", tradeMap.getUser_name ());
						paramMap.put ("status", "22"); // 22-주문 취소
						paramMap.put ("historyChannel", "5"); // 5-대사
						
						sqlMap.update (sqlId2, paramMap);
						
						buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 주문 정보 변경 이력 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
							this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
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
				this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
			}
		}
		
		return commitCnt;
	}

	/**
	 * 홀케익 (수령완료 : O30, 미수령 폐기 : O31) 대사 대상 조회
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
	 * 홀케익 수령완료 대사 완료 처리
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
	
						// 영업정보 상태 : 11 - 일반매출 / 홀케익 상태 : O30 - 수령 완료 상태가 아닌 경우 대사 처리
						// 1. 수령완료처리 확인(영업정보 POS의 상태값과 사이렌오더의 상태값이 같은지 확인
						if(tradeWholecake.getSale_flag().equals("11") && !tradeWholecake.getStatus().equals(WHOLECAKE_ORDER_STATUS_RECEIVED_COMP)) {
							
							paramMap.clear();
							paramMap.put ("status",      	 WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);  // 상태(O30 : 수령완료상태)
							paramMap.put ("orderNo",     	 tradeWholecake.getOrder_no ());  	     // 예약번호
							paramMap.put ("saleDate",    	 tradeWholecake.getSale_date ());        // 영업일
							paramMap.put ("storeCd",     	 tradeWholecake.getStore_no ());  	     // 점포코드
							paramMap.put ("posNo",       	 tradeWholecake.getPos_no ());    	     // POS번호							
							paramMap.put ("seqNo",           tradeWholecake.getSeq_no ());           // 거래번호
							paramMap.put ("receiveCompDate", tradeWholecake.getReceive_comp_date()); // 수령/미수령 완료일자
							
							// 수령구분 값이 없다면 SAL_PAY_XO에서 확인한 수령구분{O-주문번호, P-선물번호}값으로 업데이트
							if(StringUtils.isBlank(tradeWholecake.getReceive_type())) {
								paramMap.put ("receiveType", tradeWholecake.getSales_tbl_receive_type());
							}

							// 2. 홀케익 상태값 변경							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 상태 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());

							// 3. 히스토리 등록 처리
							paramMap.clear();
							paramMap.put ("orderNo",  tradeWholecake.getOrder_no ());  	     // 예약번호
							paramMap.put ("status",   WHOLECAKE_ORDER_STATUS_RECEIVED_COMP); // 상태(O30 : 수령완료상태)							
							paramMap.put ("saleDate", tradeWholecake.getSale_date ());       // 영업일
							paramMap.put ("storeCd",  tradeWholecake.getStore_no ());  	     // 점포코드
							paramMap.put ("posNo",    tradeWholecake.getPos_no ());    	     // POS번호							
							paramMap.put ("seqNo",    tradeWholecake.getSeq_no ());          // 거래번호

							sqlMap.update ("trade.wholecakeOrderHistoryRegister", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 변경 이력 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
							// 4. 홀케익 쿠폰사용시  사용매장 등록 - Start
							try {
								// 홀케익 예약시 사용된 쿠폰조회
								List<Map<String, Object>> couponInfoMapList = (List<Map<String, Object>>) sqlMap.queryForList("paymentcancelXo.getWholecakeOrderCouponNumber", tradeWholecake.getOrder_no());
								if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
									dbMap.clear();
									dbMap.put ("storeCd",  tradeWholecake.getStore_no ());
									dbMap.put ("posNo",    tradeWholecake.getPos_no ());
									dbMap.put ("saleDate", tradeWholecake.getSale_date ());
									dbMap.put ("seqNo",    tradeWholecake.getSeq_no ());
									
									for (Map<String, Object> couponInfoMap : couponInfoMapList) {
										dbMap.putAll(couponInfoMap);
										
										// msr_trd_coupon_list에 등록된 쿠폰 번호가 있는지 확인
										int checkCount = (Integer) msrSqlMap.queryForObject("paymentcancelMsr.getTrdCouponCount", dbMap);

										// 등록된 쿠폰 번호가 없다면 등록
										if(checkCount == 0) {
											resultCnt = msrSqlMap.update ("paymentcancelMsr.insertTrdCouponList", dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 거래 쿠폰 내역에  쿠폰사용 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
						    // 5. 동일하다면 홀케익 예약정보 대사 완료처리(trade_flag) Y 처리
							paramMap.clear();
							paramMap.put ("orderNo", tradeWholecake.getOrder_no ()); // 예약번호
							
							// 6. 홀케익 상태값 변경							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 상태 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
							this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
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
				this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
			}
		}
		
		return commitCnt;
	}

	/**
	 * 홀케익 미수령 폐기 대사 완료 처리
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
	
						// 영업정보 상태 : 11 - 일반매출 / 홀케익 상태 : O31 - 미수령 폐기 상태가 아닌 경우 대사 처리
						// 1. 미수령 폐기처리 확인(영업정보 POS의 상태값과 사이렌오더의 상태값이 같은지 확인
						if(tradeWholecake.getSale_flag().equals("11") && !tradeWholecake.getStatus().equals(WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE)) {
							
							paramMap.clear();
							paramMap.put ("status",      	 WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE); // 상태(O31 : 미수령 폐기상태)
							paramMap.put ("orderNo",     	 tradeWholecake.getOrder_no ());  	      // 예약번호
							paramMap.put ("saleDate",    	 tradeWholecake.getSale_date ());         // 영업일
							paramMap.put ("storeCd",     	 tradeWholecake.getStore_no ());  	      // 점포코드
							paramMap.put ("posNo",       	 tradeWholecake.getPos_no ());    	      // POS번호							
							paramMap.put ("seqNo",           tradeWholecake.getSeq_no ());            // 거래번호
							paramMap.put ("receiveCompDate", tradeWholecake.getReceive_comp_date());  // 수령/미수령 완료일자

							// 수령구분 값이 없다면 SAL_PAY_XO에서 확인한 수령구분{O-주문번호, P-선물번호}값으로 업데이트
							if(StringUtils.isBlank(tradeWholecake.getReceive_type())) {
								paramMap.put ("receiveType", tradeWholecake.getSales_tbl_receive_type());
							}
							
							// 2. 홀케익 상태값 변경							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 상태 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());

							// 3. 히스토리 등록 처리
							paramMap.clear();
							paramMap.put ("orderNo",  tradeWholecake.getOrder_no ());  	       // 예약번호
							paramMap.put ("status",   WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE); // 상태(O31 : 미수령 폐기상태)							
							paramMap.put ("saleDate", tradeWholecake.getSale_date ());         // 영업일
							paramMap.put ("storeCd",  tradeWholecake.getStore_no ());  	       // 점포코드
							paramMap.put ("posNo",    tradeWholecake.getPos_no ());    	       // POS번호							
							paramMap.put ("seqNo",    tradeWholecake.getSeq_no ());            // 거래번호

							sqlMap.update ("trade.wholecakeOrderHistoryRegister", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 변경 이력 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
							tLogger.info (buf.toString ());
							
							// 4. 홀케익 쿠폰사용시  사용매장 등록 - Start
							try {
								// 홀케익 예약시 사용된 쿠폰조회
								List<Map<String, Object>> couponInfoMapList = (List<Map<String, Object>>) sqlMap.queryForList("paymentcancelXo.getWholecakeOrderCouponNumber", tradeWholecake.getOrder_no());
								if (couponInfoMapList != null && couponInfoMapList.size() > 0) {
									dbMap.clear();
									dbMap.put ("storeCd",  tradeWholecake.getStore_no ());
									dbMap.put ("posNo",    tradeWholecake.getPos_no ());
									dbMap.put ("saleDate", tradeWholecake.getSale_date ());
									dbMap.put ("seqNo",    tradeWholecake.getSeq_no ());
									
									for (Map<String, Object> couponInfoMap : couponInfoMapList) {
										dbMap.putAll(couponInfoMap);
										
										// msr_trd_coupon_list에 등록된 쿠폰 번호가 있는지 확인
										int checkCount = (Integer) msrSqlMap.queryForObject("paymentcancelMsr.getTrdCouponCount", dbMap);
										
										// 등록된 쿠폰 번호가 없다면 등록
										if(checkCount == 0) {
											resultCnt = msrSqlMap.update ("paymentcancelMsr.insertTrdCouponList", dbMap);
											if ( resultCnt > 0 ) {
												buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 거래 쿠폰 내역에  쿠폰사용 등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
						    // 5. 동일하다면 홀케익 예약정보 대사 완료처리(trade_flag) Y 처리
							paramMap.clear();
							paramMap.put ("orderNo", tradeWholecake.getOrder_no ()); // 예약번호
							
							// 6. 홀케익 상태값 변경							
							sqlMap.update ("trade.tradeWholecakeComplete", paramMap);
							buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 예약 정보 상태 변경 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
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
							this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
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
				this.tLogger.error (ee);		// PMD 적용( PMD 에러 : printStackTrace()사용 )
			}
		}
		
		return commitCnt;
	}
	
	
	/**
	 * 설문 대상자 선정 및 INBOX 발송
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
			
			boolean isInsPushHist = false;	// PUSH 이력등록 가능여부 체크
    		boolean isSurveyInbox = false;	// PUSH 발송 가능여부 체크
    		String mbbrDvsnCode   = "N"; 	// 회원구분코드{N:일반대상자, F:파일럿_타겟팅X, P:파일럿_타겟팅O}
    		String pushOrderNo    = null;	// PUSH 발송시 사용할 사이렌오더 주문번호
			Map<String, String> dbMap = new HashMap<String, String>();
			
			// 마이스타벅스리뷰 마스터 정보 조회
			dbMap.clear();
			dbMap.put("order_no", xopOrdNo);
			dbMap.put("user_id",  userId);
			msrvMasterDto = (MsrvInfrmMasterDto) sqlMap.queryForObject("msReview.getMsrvInfrmMasterInfo", dbMap);
			if (msrvMasterDto == null || msrvMasterDto.getMsrvSrnm() <= 0) {
    			throw new XOException(msrvLogStr+":유효한설문없음");
    		} else {
    			tLogger.info(logTitle+msrvLogStr+":"+msrvMasterDto.toString());
    		}
			
			// MSR 회원여부 조회
			CardRegMemberDto msrMemberDto = (CardRegMemberDto) msrSqlMap.queryForObject("paymentcancelMsr.getRegMember", userId);
			if (msrMemberDto == null || StringUtils.isBlank(msrMemberDto.getUser_number())) {
				throw new XOException(msrvLogStr+":MSR회원아님");
    		}
			
			// 현재 주문이 주문완료(30), 제조완료(31) 상태인지 체크
   			if (StringUtils.isBlank(msrvMasterDto.getCurrOrderDate())) {
   				throw new XOException(msrvLogStr+":현재주문이{30,31}상태아님/당일주문건아님");
   			}
			
   			// 참여대상자제한여부인 경우 제한인원 초과시 설문 참여 불가
   			if (StringUtils.equals(msrvMasterDto.getPrtcnTrprRstrcYn(), "Y")) {
   				dbMap.clear();
   				dbMap.put("msrv_srnm", msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);
   				Integer prtcnPrcnt = (Integer)sqlMap.queryForObject("msReview.getMsrvPrtcnPrcnt", dbMap);
   				
   				if (prtcnPrcnt != null && (msrvMasterDto.getPrtcnTrprRstrcPrcnt() <= prtcnPrcnt)) {
   					throw new XOException(msrvLogStr+":설문참여인원_OVER{"+msrvMasterDto.getPrtcnTrprRstrcPrcnt()+"/"+prtcnPrcnt+"}");
   				}
   			}
			
   			// 설문 참여여부 조회 (설문 완료자는 대상자 제외)
   			dbMap.clear();
   			dbMap.put("scs_idx", msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);		
   			dbMap.put("user_id", userId);
   			SurveyDto surveyDto = (SurveyDto) homeSqlMap.queryForObject("survey.getSurveyJoinInfo", dbMap);
   			if (surveyDto == null) {
   				throw new XOException(msrvLogStr+":회원정보없음{"+userId+"}");
   			} else if (!StringUtils.equals(surveyDto.getSurvey_yn(), "Y")) {
   				throw new XOException(msrvLogStr+":설문참여완료");
   			}
   			tLogger.info(logTitle+msrvLogStr+":"+surveyDto.toMsrvString());
   			
   			/*** 실시간 타겟팅 조건 체크 ***/
   			pushOrderNo = this.msrvRealTimeChk(sqlMap, msrvMasterDto, surveyDto, pLogTitle);
   			
   			// INBOX용 주문번호와 현재 주문번호가 동일하면 현재 주문이 타겟팅 조건을 만족
			// 현재 주문이 타겟팅 조건을 만족하는 경우에만 대상자 이력 등록
   			if (StringUtils.equals(pushOrderNo, xopOrdNo)) {
   				isInsPushHist = true;
   				isSurveyInbox = true;
   			} else {
   				isInsPushHist = false;
   				isSurveyInbox = false;
   			}
   			
   			// 마이스타벅스리뷰 참여대상자 이력등록 및 현재 설문 참여인원수 업데이트
   			if (isInsPushHist) {
   				dbMap.clear();
   				dbMap.put("msrv_srnm",  		 msrvMasterDto.getMsrvSrnm()+StringUtils.EMPTY);
   				dbMap.put("msrmb_id",  			 userId);
   				dbMap.put("msrv_mbbr_dvsn_code", mbbrDvsnCode);
   				dbMap.put("sror_order_no", 		 xopOrdNo);
   				
   				// 마이스타벅스리뷰 참여대상자 이력등록
   				sqlMap.insert("msReview.insertMsrvPrtcnTrprH", dbMap);
   				
   				/*
   				// 실시간 조회로 수정 (마스터 테이블 업데이트 이슈)
   				// 설문참여대상자 제한인 경우 현재 참여인원수 업데이트
   				if (StringUtils.equals(msrvMasterDto.getPrtcnTrprRstrcYn(), "Y")) {
   					sqlMap.update("msReview.updateMsrvPrtcnPrcnt", dbMap);
   	   			}
   				*/
   				
   				sqlMap.commitTransaction();
   			} else {
   				// 실시간타겟팅 조건이 존재하는 설문인 경우
   				if (StringUtils.equals(msrvMasterDto.getMsrvDvsnCode(), "S") || StringUtils.equals(msrvMasterDto.getMsrvDvsnCode(), "T")) {
   					// 현재 주문이 타겟팅 조건을 만족하지 않더라도 이전 주문에서 타겟팅 조건을 만족하는 경우 PUSH 발송
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
   			tLogger.info(logTitle+msrvLogStr+":xoOrdNo->"+xopOrdNo+",pushOrdNo->"+pushOrderNo+",이력등록->"+isInsPushHist+",INBOX등록->"+isSurveyInbox);
   			
   			if (isSurveyInbox) {
   				//음료 제조완료시 설문안내 인박스 등록
				UserInboxDto dto = new UserInboxDto();
				dto.setSui_app_yn("Y");
				dto.setSui_web_yn("Y");
				dto.setSui_link_yn("Y");
				dto.setSui_user_id(userId);
				dto.setSui_title("[마이 스타벅스 리뷰] 지금 참여하시고 추가 별★ 혜택도 함께 받으세요!");
				dto.setSui_url("starbucks://callCvPage");
				homeSqlMap.insert("survey.insertUserInBoxMsg", dto);
				
				homeSqlMap.commitTransaction();
				
				buf.delete (0, buf.length ()).append (logTitle).append ("step ").append ( ( step++ )).append (". 설문조사 안내 inbox등록 END :").append ( ( System.currentTimeMillis () - startTime )).append ("ms");
				tLogger.info (buf.toString ());
   			} else {
   				tLogger.info(logTitle+msrvLogStr+":설문대상자아님");
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
	 * 실시간 타겟팅 조건 체크(분기)
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
		
		// 구분코드 {X:사전+실기간없음, B:사전만존재, S:실시간만존재, T:사전+실시간둘다존재}
		String msrvDvsnCode = StringUtils.defaultIfEmpty(msrvDto.getMsrvDvsnCode(), "X");
		
		// 사전대상자+실시간조건 없음
		if (msrvDvsnCode.equals("X")) {
			pushOrderNo = msrvDto.getOrderNo();
			
		// 사전대상자 조건만 존재
		} else if (msrvDvsnCode.equals("B")) {
			// 마이스타벅스리뷰 사전대상자 유무 조회
			dbMap.clear();
			dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
			dbMap.put("msrmb_id",  msrvDto.getUserId());
			Integer preMemberCnt = (Integer)sqlMap.queryForObject("msReview.getMsrvBfrhnTrprCount", dbMap);
			// 사전대상자로 선정된 경우
			if (preMemberCnt != null && preMemberCnt > 0) {
				pushOrderNo = msrvDto.getOrderNo();
			}
			
		// 실시간 조건만 존재
		} else if (msrvDvsnCode.equals("S")) {
			pushOrderNo = this.msrvRealTimeProc(sqlMap, msrvDto, surveyDto, pLogTitle);
			
		// 사전대상자 + 실시간 조건 둘다 존재
		} else if (msrvDvsnCode.equals("T")) {
			// 마이스타벅스리뷰 사전대상자 유무 조회
			dbMap.clear();
			dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
			dbMap.put("msrmb_id",  msrvDto.getUserId());
			Integer preMemberCnt = (Integer)sqlMap.queryForObject("msReview.getMsrvBfrhnTrprCount", dbMap);
			// 사전대상자로 선정된 경우
			if (preMemberCnt != null && preMemberCnt > 0) {
				pushOrderNo = this.msrvRealTimeProc(sqlMap, msrvDto, surveyDto, pLogTitle);	
			}
		}
		
		return pushOrderNo;
	}
	
	/**
	 * 실시간 타겟팅 조건 체크(처리)
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
			// 1. 성별체크
			if (!StringUtils.equals(msrvDto.getGnrCode(), "A")) {
				if (!StringUtils.equals(msrvDto.getGnrCode(), surveyDto.getGender())) {
					throw new XOException("성별체크Fail{설정:"+msrvDto.getGnrCode()+"/계정:"+surveyDto.getGender()+"}");
				}
			}
			
			// MSR등급 체크유무 판단
			String grdChkYn = "N";
			if (StringUtils.isNotBlank(msrvDto.getMsrGradeCodeDtls())) {
				grdChkYn = "Y";
			}
			// 시간정보의 등록순번 조회
			String tmeRgsttSrnm = "";
			if (StringUtils.equals(msrvDto.getOrderTmeAplctYn(), "Y")) {
				dbMap.clear();
				dbMap.put("msrv_srnm", msrvDto.getMsrvSrnm()+StringUtils.EMPTY);
				tmeRgsttSrnm = (String)sqlMap.queryForObject("msReview.getTmeRgsttSrnm", dbMap);
			}
			
			// 2. 실시간조건 체크를 위한 param 설정
			dbMap.clear();
			dbMap.put("msrv_srnm", 	    msrvDto.getMsrvSrnm()+StringUtils.EMPTY);	// 마이스타벅스리뷰순번
			dbMap.put("birth_yrwn",     surveyDto.getBirth_year());					// 출생년도
			dbMap.put("user_id", 	    msrvDto.getUserId());						// 사용자ID
			dbMap.put("curr_order_no",  msrvDto.getOrderNo());						// 사이렌오더주문번호
			dbMap.put("bir_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getBirthYrwnAplctYn(),  "N"));	// 출생년도적용여부
			dbMap.put("grd_chk_yn",     grdChkYn);									// MSR등급적용여부
			dbMap.put("str_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getStoreAtrbtAplctYn(), "N")); 	// 매장속성적용여부
			dbMap.put("tme_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getOrderTmeAplctYn(),   "N"));	// 주문시간적용여부
			dbMap.put("tme_rgstt_srnm", tmeRgsttSrnm);								// 시간정보등록순번
			dbMap.put("sku_chk_yn",     StringUtils.defaultIfEmpty(msrvDto.getGoodsInfrmAplctYn(), "N"));	// 상품정보적용여부
			dbMap.put("sku_prn_yn",     StringUtils.defaultIfEmpty(msrvDto.getGoodsInfrmPrngYn(),  "N"));	// 상품정보페어링여부
			
			// 3. 마이스타벅스리뷰 실시간체크 성공건 주문번호 조회
			List<String> orderNoList = (List<String>)sqlMap.queryForList("msReview.getMsrvOrderNoChkList", dbMap);
			
			if (orderNoList != null && orderNoList.size() > 0) {
				finalOrderNo = orderNoList.get(0);	// 첫번째 사이렌오더 주문번호 설정(가장 최근 주문번호)
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
