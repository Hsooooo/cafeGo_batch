/*
 * @(#) $Id: PaymentCancelXoMgr.java,v 1.7 2018/10/01 06:55:15 iamjihun Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderCouponDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PaymentDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.RefundSbcDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmsHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.dao.PaymentCancelXoDao;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 자동 결제 취소 PaymentCancelXoMgr.
 * @author eZEN ksy
 * @since 2014. 1. 15.
 * @version $Revision: 1.7 $
 */
public class PaymentCancelXoMgr {
	
	private final PaymentCancelXoDao paymentCancelXoDao;
	
	public PaymentCancelXoMgr () {
		this.paymentCancelXoDao = new PaymentCancelXoDao ();
	}
	
	/**
	 * 결제 수단별 정책 조회
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPolicyPayment (Map<String, String> map) throws Exception {
		return this.paymentCancelXoDao.getPolicyPayment (map);
	}
	
	/**
	 * 결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentCancel ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentCancel (map);
	}
	
	/**
	 * 특정 날짜 결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentCancelTargetDays ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentCancelTargetDays (map);
	}
	
	/**
	 * 특정 날짜 Pg결제 취소 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentPgCancelTargetDays ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentPgCancelTargetDays (map);
	}
	
	/**
	 * 결제 취소 대상 조회(스타벅스카드 및 PG를 제외한 대상-쿠폰(통신사)만 사용한 경우)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentEtcCancel ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentEtcCancel (map);
	}
	
	/**
	 * 주문 정보 조회
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public OrderDto getOrder ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getOrder (xoSqlMap, orderNo);
	}
	
	/**
	 * 결제 정보 조회
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<PaymentDto> getPaymentList ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getPaymentList (xoSqlMap, orderNo);
	}
	
	/**
	 * 주문 정보 업데이트
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrder ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.updateOrder (xoSqlMap, dbMap);
	}
	
	/**
	 * 주문 정보 변경 이력 업데이트
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertOrderHistory ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.insertOrderHistory (xoSqlMap, dbMap);
	}
	
	/**
	 * 결제 정보 업데이트
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updatePayment ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		return this.paymentCancelXoDao.updatePayment (xoSqlMap, dto);
	}
	
	/**
	 * 타 카드 충전 환불 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertRefundSbc ( SqlMapClient xoSqlMap, RefundSbcDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertRefundSbc (xoSqlMap, dto);
	}
	
	/**
	 * SMS 발송 요청(계정 : MMS)
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Long insertSmtTran ( SqlMapClient xoSqlMap, SmtTranDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertSmtTran (xoSqlMap, dto);
	}
	
	/**
	 * SMS 발송 이력 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertSmsHistory ( SqlMapClient xoSqlMap, SmsHistoryDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertSmsHistory (xoSqlMap, dto);
	}
	
	/**
	 * 주문상세의 쿠폰정보 조회
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<OrderCouponDto> getOrderCouponInfo ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getOrderCouponInfo(xoSqlMap, orderNo);
	}
	
	/**
	 * 결제 취소시 에러 발생으로 인한 결제정보 변경이력 등록
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertPaymentOfPaymentHist ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertPaymentOfPaymentHist (xoSqlMap, dto);
	}

	/**
	 * 결제 취소 대상 조회(스타벅스카드, 신용카드, SSG PAY, SSG MONEY 결제 및 페어링 이벤트 결제 건)
	 * @param dbMap
	 * @return
	 */
	public List<Map<String, String>> getPaymentEventCancel() throws Exception {
		return this.paymentCancelXoDao.getPaymentEventCancel();
	}
	
	/**
	 * 취소 가능 여부 확인
	 * @param xoGiftSqlMap
	 * @param giftNo
	 * @return
	 * @throws Exception
	 */
	public String getGiftUseCancel( SqlMapClient xoGiftSqlMap, String giftNo ) throws Exception {
		return this.paymentCancelXoDao.getGiftUseCancel (xoGiftSqlMap, giftNo);
	}
	
	/**
	 * 선물 사용 후 사용 이력 등록
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int insertGiftUseHistory( SqlMapClient xoGiftSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.insertGiftUseHistory (xoGiftSqlMap, dbMap);
	}
	
	/**
	 * 선물 상태 수정
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int updateGiftStatus( SqlMapClient xoGiftSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.updateGiftStatus (xoGiftSqlMap, dbMap);
	}

    public void updateEmpOrder(SqlMapClient xoGiftSqlMap, Map<String, String> dbMap) throws SQLException {
        this.paymentCancelXoDao.updateEmpOrder(xoGiftSqlMap, dbMap);
    }
    
    /**
	 * 제휴사 정보 조회
	 * @param xoSqlMap
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getMblGfcrPrcmInfo ( Map<String, String> dbMap) throws Exception {
		return this.paymentCancelXoDao.getMblGfcrPrcmInfo (dbMap);
	}

	/**
	 * PG 승인 대사 배치 취소 대상 조회
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getPgTradeCancelList(Map<String, Object> dbMap) throws SQLException {		
		return (List<Map<String, Object>>) this.paymentCancelXoDao.getPgTradeCancelList(dbMap);
	}

	/**
	 * 취소처리결과 업데이트
	 * @param dbMap
	 * @throws SQLException
	 */
	public void updatePgTradeCancel(Map<String, Object> dbMap) throws SQLException {
		this.paymentCancelXoDao.updatePgTradeCancel(dbMap);
	}
	
	/**
	 * 결제상세방식 조회 (신한 페이판 전용)
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public String getDtlPayMethod(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws Exception {
		return this.paymentCancelXoDao.getDtlPayMethod(xoSqlMap, dbMap);
	}
}
