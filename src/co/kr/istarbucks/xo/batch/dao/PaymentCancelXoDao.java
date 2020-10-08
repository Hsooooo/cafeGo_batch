/*
 * @(#) $Id: PaymentCancelXoDao.java,v 1.7 2018/10/01 06:55:14 iamjihun Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderCouponDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PaymentDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.RefundSbcDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmsHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

/**
 * 자동 결제 취소 PaymentCancelXoDao.
 * @author eZEN ksy
 * @since 2014. 1. 15.
 * @version $Revision: 1.7 $
 */
public class PaymentCancelXoDao {
	
	/**
	 * 결제 수단별 정책 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public Map<String, String> getPolicyPayment (Map<String, String> map) throws Exception {
		String sqlId = "paymentcancelXo.getPolicyPayment";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (Map<String, String>) xoSqlMap.queryForObject (sqlId, map);
	}
	
	/**
	 * 결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getPaymentCancel ( Map<String, String> map) throws Exception {
		String sqlId = "paymentcancelXo.getPaymentCancel";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, map);
	}
	
	/**
	 * 특정 날짜 결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getPaymentCancelTargetDays ( Map<String, String> map ) throws Exception {
		String sqlId = "paymentcancelXo.getPaymentCancelTargetDays";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, map);
	}
	
	/**
	 * 특정 날짜 Pg결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getPaymentPgCancelTargetDays ( Map<String, String> map ) throws Exception {
		String sqlId = "paymentcancelXo.getPaymentPgCancelTargetDays";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, map);
	}
	
	/**
	 * 결제 취소 대상 조회(스타벅스카드 및 PG를 제외한 대상-쿠폰(통신사)만 사용한 경우)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getPaymentEtcCancel ( Map<String, String> map) throws Exception {
		String sqlId = "paymentcancelXo.getPaymentEtcCancel";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, map);
	}
	
	/**
	 * 주문 정보 조회
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public OrderDto getOrder ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		String sqlId = "paymentcancelXo.getOrder";
		return (OrderDto) xoSqlMap.queryForObject (sqlId, orderNo);
	}
	
	/**
	 * 결제 정보 조회
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<PaymentDto> getPaymentList ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		String sqlId = "paymentcancelXo.getPaymentList";
		return xoSqlMap.queryForList (sqlId, orderNo);
	}
	
	/**
	 * 주문 정보 업데이트
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrder ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		String sqlId = "paymentCancelXo.updateOrder";
		return xoSqlMap.update (sqlId, dbMap) == 1 ? true : false;
	}
	
	/**
	 * 주문 정보 변경 이력 업데이트
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertOrderHistory ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		String sqlId = "paymentCancelXo.insertOrderHistory";
		return xoSqlMap.update (sqlId, dbMap) == 1 ? true : false;
	}
	
	/**
	 * 결제 정보 업데이트
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updatePayment ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		String sqlId = "paymentCancelXo.updatePayment";
		return xoSqlMap.update (sqlId, dto) == 1 ? true : false;
	}
	
	/**
	 * 타 카드 충전 환불 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertRefundSbc ( SqlMapClient xoSqlMap, RefundSbcDto dto ) throws Exception {
		String sqlId = "paymentcancelXo.insertRefundSbc";
		return xoSqlMap.update (sqlId, dto) == 1 ? true : false;
	}
	
	/**
	 * SMS 발송 요청(계정 : MMS)
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Long insertSmtTran ( SqlMapClient xoSqlMap, SmtTranDto dto ) throws Exception {
		String sqlId = "paymentcancelXo.insertSmtTran";
		return (Long) xoSqlMap.insert (sqlId, dto);
	}
	
	/**
	 * SMS 발송 이력 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertSmsHistory ( SqlMapClient xoSqlMap, SmsHistoryDto dto ) throws Exception {
		String sqlId = "paymentcancelXo.insertSmsHistory";
		return xoSqlMap.update (sqlId, dto) == 1 ? true : false;
	}
	
	/**
	 * 주문상세의 쿠폰정보 조회
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<OrderCouponDto> getOrderCouponInfo ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		String sqlId = "paymentcancelXo.getOrderCouponInfo";
		return xoSqlMap.queryForList (sqlId, orderNo);
	}

	/**
	 * 결제 취소시 에러 발생으로 인한 결제정보 변경이력 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertPaymentOfPaymentHist ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		String sqlId = "paymentcancelXo.insertPaymentOfPaymentHist";
		return xoSqlMap.update (sqlId, dto) == 1 ? true : false;
	}

	/**
	 * 결제 취소 대상 조회(스타벅스카드, 신용카드, SSG PAY, SSG MONEY 결제 및 페어링 이벤트 결제 건)
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getPaymentEventCancel() throws Exception {
		String sqlId = "paymentcancelXo.getPaymentEventCancel";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId);
	}

	/**
	 * 취소 가능 여부 확인
	 * @param xoGiftSqlMap
	 * @param giftNo
	 * @return
	 * @throws Exception
	 */
	public String getGiftUseCancel(SqlMapClient xoGiftSqlMap, String giftNo) throws Exception {
		String sqlId = "paymentcancelXo.getGiftUseCancel";
		return (String) xoGiftSqlMap.queryForObject(sqlId, giftNo);
	}
	
	/**
	 * 선물 사용 후 사용 이력 등록
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int insertGiftUseHistory( SqlMapClient xoGiftSqlMap, Map<String, Object> dbMap ) throws Exception {
		String sqlId = "paymentcancelXo.insertGiftUseHistory";
		return xoGiftSqlMap.update (sqlId, dbMap);
	}
	
	/**
	 * 선물 상태 수정
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int updateGiftStatus( SqlMapClient xoGiftSqlMap, Map<String, Object> dbMap ) throws Exception {
		String sqlId = "paymentcancelXo.updateGiftStatus";
		return xoGiftSqlMap.update (sqlId, dbMap);
	}

    public void updateEmpOrder(SqlMapClient xoGiftSqlMap, Map<String, String> dbMap) throws SQLException {
        String sqlId = "paymentcancelXo.updateEmpOrder";
        xoGiftSqlMap.update(sqlId, dbMap);
    }
    /**
	 * 제휴사 정보 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public Map<String, String> getMblGfcrPrcmInfo (Map<String, String> map) throws Exception {
		String sqlId = "paymentcancelXo.getMblGfcrPrcmInfo";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (Map<String, String>) xoSqlMap.queryForObject (sqlId, map);
	}

	/**
	 * PG 승인 대사 배치 취소 대상 조회
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPgTradeCancelList (Map<String, Object> dbMap) throws SQLException {
		String sqlId = "paymentcancelXo.getPgTradeCancelList";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (List<Map<String, Object>>) xoSqlMap.queryForList(sqlId, dbMap);
	}

	/**
	 * 취소처리결과 업데이트
	 * @param dbMap
	 * @throws SQLException
	 */
	public void updatePgTradeCancel(Map<String, Object> dbMap) throws SQLException {
        String sqlId = "paymentcancelXo.updatePgTradeCancel";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		xoSqlMap.update(sqlId, dbMap);
	}
    
    /**
	 * e-Gift Item LMS 수신 실패 건 조회
	 * @param xoSqlMap
	 * @param date
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public List<Map<String, String>> getEGiftItemLmsList(String date) throws SQLException {
		// TODO Auto-generated method stub
		String sqlId = "paymentcancelXo.getEGiftItemLmsList";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, date);
	}
    
    /**
	 * e-Gift Item LMS 수신 실패에 대한 구매자에게 푸시 알림 발송여부(Y : 발송) Update
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public void updateEmMmtTran(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		// TODO Auto-generated method stub
		String sqlId = "paymentcancelXo.updateEmMmtTran";
		xoSqlMap.update(sqlId, dbMap);
	}
	
	/**
	 * 결제상세방식 조회 (신한 페이판 전용)
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public String getDtlPayMethod(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws Exception {
		String sqlId = "paymentcancelXo.getDtlPayMethod";
		return (String) xoSqlMap.queryForObject(sqlId, dbMap);
	}
}
