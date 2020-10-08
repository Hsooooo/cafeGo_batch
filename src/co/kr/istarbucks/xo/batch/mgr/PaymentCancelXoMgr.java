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
 * �ڵ� ���� ��� PaymentCancelXoMgr.
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
	 * ���� ���ܺ� ��å ��ȸ
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPolicyPayment (Map<String, String> map) throws Exception {
		return this.paymentCancelXoDao.getPolicyPayment (map);
	}
	
	/**
	 * ���� ��� ��� ��ȸ
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentCancel ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentCancel (map);
	}
	
	/**
	 * Ư�� ��¥ ���� ��� ��� ��ȸ
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentCancelTargetDays ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentCancelTargetDays (map);
	}
	
	/**
	 * Ư�� ��¥ Pg���� ��� ��� ��ȸ
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentPgCancelTargetDays ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentPgCancelTargetDays (map);
	}
	
	/**
	 * ���� ��� ��� ��ȸ(��Ÿ����ī�� �� PG�� ������ ���-����(��Ż�)�� ����� ���)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getPaymentEtcCancel ( Map<String, String> map ) throws Exception {
		return this.paymentCancelXoDao.getPaymentEtcCancel (map);
	}
	
	/**
	 * �ֹ� ���� ��ȸ
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public OrderDto getOrder ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getOrder (xoSqlMap, orderNo);
	}
	
	/**
	 * ���� ���� ��ȸ
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<PaymentDto> getPaymentList ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getPaymentList (xoSqlMap, orderNo);
	}
	
	/**
	 * �ֹ� ���� ������Ʈ
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrder ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.updateOrder (xoSqlMap, dbMap);
	}
	
	/**
	 * �ֹ� ���� ���� �̷� ������Ʈ
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertOrderHistory ( SqlMapClient xoSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.insertOrderHistory (xoSqlMap, dbMap);
	}
	
	/**
	 * ���� ���� ������Ʈ
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updatePayment ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		return this.paymentCancelXoDao.updatePayment (xoSqlMap, dto);
	}
	
	/**
	 * Ÿ ī�� ���� ȯ�� ���
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertRefundSbc ( SqlMapClient xoSqlMap, RefundSbcDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertRefundSbc (xoSqlMap, dto);
	}
	
	/**
	 * SMS �߼� ��û(���� : MMS)
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Long insertSmtTran ( SqlMapClient xoSqlMap, SmtTranDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertSmtTran (xoSqlMap, dto);
	}
	
	/**
	 * SMS �߼� �̷� ���
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertSmsHistory ( SqlMapClient xoSqlMap, SmsHistoryDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertSmsHistory (xoSqlMap, dto);
	}
	
	/**
	 * �ֹ����� �������� ��ȸ
	 * @param xoSqlMap
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<OrderCouponDto> getOrderCouponInfo ( SqlMapClient xoSqlMap, String orderNo ) throws Exception {
		return this.paymentCancelXoDao.getOrderCouponInfo(xoSqlMap, orderNo);
	}
	
	/**
	 * ���� ��ҽ� ���� �߻����� ���� �������� �����̷� ���
	 * @param xoSqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertPaymentOfPaymentHist ( SqlMapClient xoSqlMap, PaymentDto dto ) throws Exception {
		return this.paymentCancelXoDao.insertPaymentOfPaymentHist (xoSqlMap, dto);
	}

	/**
	 * ���� ��� ��� ��ȸ(��Ÿ����ī��, �ſ�ī��, SSG PAY, SSG MONEY ���� �� �� �̺�Ʈ ���� ��)
	 * @param dbMap
	 * @return
	 */
	public List<Map<String, String>> getPaymentEventCancel() throws Exception {
		return this.paymentCancelXoDao.getPaymentEventCancel();
	}
	
	/**
	 * ��� ���� ���� Ȯ��
	 * @param xoGiftSqlMap
	 * @param giftNo
	 * @return
	 * @throws Exception
	 */
	public String getGiftUseCancel( SqlMapClient xoGiftSqlMap, String giftNo ) throws Exception {
		return this.paymentCancelXoDao.getGiftUseCancel (xoGiftSqlMap, giftNo);
	}
	
	/**
	 * ���� ��� �� ��� �̷� ���
	 * @param xoGiftSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int insertGiftUseHistory( SqlMapClient xoGiftSqlMap, Map<String, Object> dbMap ) throws Exception {
		return this.paymentCancelXoDao.insertGiftUseHistory (xoGiftSqlMap, dbMap);
	}
	
	/**
	 * ���� ���� ����
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
	 * ���޻� ���� ��ȸ
	 * @param xoSqlMap
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getMblGfcrPrcmInfo ( Map<String, String> dbMap) throws Exception {
		return this.paymentCancelXoDao.getMblGfcrPrcmInfo (dbMap);
	}

	/**
	 * PG ���� ��� ��ġ ��� ��� ��ȸ
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getPgTradeCancelList(Map<String, Object> dbMap) throws SQLException {		
		return (List<Map<String, Object>>) this.paymentCancelXoDao.getPgTradeCancelList(dbMap);
	}

	/**
	 * ���ó����� ������Ʈ
	 * @param dbMap
	 * @throws SQLException
	 */
	public void updatePgTradeCancel(Map<String, Object> dbMap) throws SQLException {
		this.paymentCancelXoDao.updatePgTradeCancel(dbMap);
	}
	
	/**
	 * �����󼼹�� ��ȸ (���� ������ ����)
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public String getDtlPayMethod(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws Exception {
		return this.paymentCancelXoDao.getDtlPayMethod(xoSqlMap, dbMap);
	}
}
