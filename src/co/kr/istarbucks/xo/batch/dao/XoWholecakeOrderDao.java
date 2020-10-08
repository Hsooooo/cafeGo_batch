/*
 * @(#) $Id: XoWholecakeOrderDao.java,v 1.5 2017/05/10 02:13:55 namgu1 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016�⵵ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDetailDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderNotiDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class XoWholecakeOrderDao {

	/**
	 * ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings ( "unchecked" )
	public List<WholecakeOrderDto> getWholecakeOrderList(Map<String, Object> dbMap) throws Exception {
		String sqlId = "wholecakeOrderXo.getWholecakeOrderList";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (List<WholecakeOrderDto>) sqlMap.queryForList(sqlId, dbMap);
	}

	/**
	 * ���̷����� XO_ORDER_WHOLECAKE�� STATUS�� O20���� ������Ʈ
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updateWholecakeOrder(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		String sqlId = "wholecakeOrderXo.updateWholecakeOrder";
		return sqlMap.update (sqlId, dto) == 1 ? true : false;
	}

	/**
	 * ���̷����� XO_ORDER_HISTORY_WHOLECAKE�� STATUS�� O20���� �����丮 ���
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertWholecakeOrderHistory(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		String sqlId = "wholecakeOrderXo.insertWholecakeOrderHistory";
		return sqlMap.update (sqlId, dto) == 1 ? true : false;
	}
	
	/**
	 * Ȧ���� ���� ���� ���� ��� ���� ����
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<WholecakeOrderDto> getWholeCakeOrderListForCoupon(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.queryForList ("wholecakeOrderXo.getWholeCakeOrderListForCoupon", paramsMap);
	}
	
	/**
	 *Ȧ���� ���� MMS �̹��� ��ȸ
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<WholecakeOrderDetailDto> getWholeCakeCouponMMSImageList(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.queryForList("wholecakeOrderXo.getWholeCakeCouponMMSImageList", paramsMap);
	}
	
	/**
	 * Ȧ���� ���� ���� ���� �Ϸ� ó��
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public int updateWholeCakeOrderForCouponPubFlag(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.update("wholecakeOrderXo.updateWholeCakeOrderForCouponPubFlag", paramsMap);
	}
	
	/**
	 * Ȧ���� ���� Ȯ�� LMS �߼� ��� ���� ����
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<WholecakeOrderDto> getWholeCakeOrderListForLms(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.queryForList ("wholecakeOrderXo.getWholeCakeOrderListForLms", paramsMap);
	}
	
	/**
	 * Ȧ���� ���� ���� �˸� ���
	 * @param sqlMap
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertWholecakeOrderNoti(SqlMapClient sqlMap, WholecakeOrderNotiDto params) throws SQLException {
		return sqlMap.update("wholecakeOrderXo.insertWholecakeOrderNoti", params);
	}

	/**
	 * ���� Ȯ�������� ���� ���� Ȯ�� ī���� ��ȸ
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getYesterdayWholecakeOrderConfirm(Map<String, Object> dbMap) throws Exception {
		String sqlId = "wholecakeOrderXo.getYesterdayWholecakeOrderConfirm";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Map<String, Object>) sqlMap.queryForObject(sqlId, dbMap);
	}

	/**
	 * Ȧ���� ���� ���� �˸� ���(XO PUSH �߼� ��û ���̺� ���)
	 * @param xoSqlMap
	 * @param dbMap
	 * @throws SQLException
	 */
	public void insertWholecakeOrderPush(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		 xoSqlMap.insert("wholecakeOrderXo.insertWholecakeOrderPush", dbMap);
	}

	public void updateWholecakeOrderPush(SqlMapClient xoSqlMap,	Map<String, Object> dbMap) throws SQLException {
		 xoSqlMap.update("wholecakeOrderXo.updateWholecakeOrderPush", dbMap);
	}
}
