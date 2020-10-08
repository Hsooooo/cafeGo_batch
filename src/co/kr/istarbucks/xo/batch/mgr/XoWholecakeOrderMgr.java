/*
 * @(#) $Id: XoWholecakeOrderMgr.java,v 1.5 2017/05/10 02:13:54 namgu1 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016�⵵ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDetailDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderNotiDto;
import co.kr.istarbucks.xo.batch.dao.XoWholecakeOrderDao;

import com.ibatis.sqlmap.client.SqlMapClient;

public class XoWholecakeOrderMgr {
	private final XoWholecakeOrderDao xoWholecakeOrderDao;
	
	public XoWholecakeOrderMgr() {
		xoWholecakeOrderDao  = new XoWholecakeOrderDao();
	}

	/**
	 * ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
	 * @return
	 * @throws Exception 
	 */
	public List<WholecakeOrderDto> getWholecakeOrderList(Map<String, Object> dbMap) throws Exception {
		return this.xoWholecakeOrderDao.getWholecakeOrderList (dbMap);
	}
	
	/**
	 * ���̷����� XO_ORDER_WHOLECAKE�� STATUS�� O20���� ������Ʈ
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updateWholecakeOrder(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		return this.xoWholecakeOrderDao.updateWholecakeOrder (sqlMap, dto);
	}

	/**
	 * ���̷����� XO_ORDER_HISTORY_WHOLECAKE�� STATUS�� O20���� �����丮 ���
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertWholecakeOrderHistory(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		return this.xoWholecakeOrderDao.insertWholecakeOrderHistory (sqlMap, dto);
	}
	
	/**
	 * Ȧ���� ���� ���� ���� ��� ���� ����
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDto> getWholeCakeOrderListForCoupon(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeOrderListForCoupon(sqlMap, paramsMap);
	}
	
	/**
	 *Ȧ���� ���� MMS �̹��� ��ȸ
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDetailDto> getWholeCakeCouponMMSImageList(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeCouponMMSImageList(sqlMap, paramsMap);
	}	
	
	/**
	 * Ȧ���� ���� ���� ���� �Ϸ� ó��
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public int updateWholeCakeOrderForCouponPubFlag(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.updateWholeCakeOrderForCouponPubFlag(sqlMap, paramsMap);
	}
	
	
	/**
	 * Ȧ���� ���� Ȯ�� LMS �߼� ��� ���� ����
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDto> getWholeCakeOrderListForLms(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeOrderListForLms(sqlMap, paramsMap);
	}
	
	/**
	 * Ȧ���� ���� ���� �˸� ���
	 * @param sqlMap
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertWholecakeOrderNoti(SqlMapClient sqlMap, WholecakeOrderNotiDto params) throws SQLException {
		return this.xoWholecakeOrderDao.insertWholecakeOrderNoti(sqlMap, params);
	}

	/**
	 * ���� Ȯ�������� ���� ���� Ȯ�� ī���� ��ȸ
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getYesterdayWholecakeOrderConfirm(Map<String, Object> dbMap) throws Exception {
		return this.xoWholecakeOrderDao.getYesterdayWholecakeOrderConfirm(dbMap);
	}

	/**
	 * Ȧ���� ���� ���� �˸� ���(XO PUSH �߼� ��û ���̺� ���)
	 * @param xoSqlMap
	 * @param dbMap
	 * @throws SQLException
	 */
	public void insertWholecakeOrderPush(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		this.xoWholecakeOrderDao.insertWholecakeOrderPush(xoSqlMap, dbMap);
	}

	public void updateWholecakeOrderPush(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		this.xoWholecakeOrderDao.updateWholecakeOrderPush(xoSqlMap, dbMap);		
	}	
}
