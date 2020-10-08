/*
 * @(#) $Id: ScksaWholecakeOrderDao.java,v 1.1 2016/11/03 08:51:18 namgu1 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016�⵵ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;

import com.ibatis.sqlmap.client.SqlMapClient;

public class ScksaWholecakeOrderDao {

	/**
	 * �������� ���(���̷����� ���� -> ��������)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	public boolean insertScksaWholecakeOrder(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		String sqlId = "wholecakeOrderScksa.insertScksaWholecakeOrder";
		return scksaSqlMap.update (sqlId, dto) == 1 ? true : false;
	}

	/**
	 * �������� ������ ���(���̷����� ���� -> ��������)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 */
	public boolean insertScksaWholecakeOrderDetail(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		String sqlId = "wholecakeOrderScksa.insertScksaWholecakeOrderDetail";
		return scksaSqlMap.update (sqlId, dto) == 1 ? true : false;
	}
	
	
}
