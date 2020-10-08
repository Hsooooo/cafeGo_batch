/*
 * @(#) $Id: XoWholecakeOrderMgr.java,v 1.5 2017/05/10 02:13:54 namgu1 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016년도 eZENsolution Co., Ltd. All rights reserved.
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
	 * 발주 확정일자가 오늘일 홀케익 예약정보 조회
	 * @return
	 * @throws Exception 
	 */
	public List<WholecakeOrderDto> getWholecakeOrderList(Map<String, Object> dbMap) throws Exception {
		return this.xoWholecakeOrderDao.getWholecakeOrderList (dbMap);
	}
	
	/**
	 * 사이렌오더 XO_ORDER_WHOLECAKE의 STATUS값 O20으로 업데이트
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean updateWholecakeOrder(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		return this.xoWholecakeOrderDao.updateWholecakeOrder (sqlMap, dto);
	}

	/**
	 * 사이렌오더 XO_ORDER_HISTORY_WHOLECAKE의 STATUS값 O20으로 히스토리 등록
	 * @param sqlMap
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean insertWholecakeOrderHistory(SqlMapClient sqlMap, WholecakeOrderDto dto) throws Exception {
		return this.xoWholecakeOrderDao.insertWholecakeOrderHistory (sqlMap, dto);
	}
	
	/**
	 * 홀케익 예약 쿠폰 발행 대상 예약 추출
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDto> getWholeCakeOrderListForCoupon(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeOrderListForCoupon(sqlMap, paramsMap);
	}
	
	/**
	 *홀케익 쿠폰 MMS 이미지 조회
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDetailDto> getWholeCakeCouponMMSImageList(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeCouponMMSImageList(sqlMap, paramsMap);
	}	
	
	/**
	 * 홀케익 예약 쿠폰 발행 완료 처리
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public int updateWholeCakeOrderForCouponPubFlag(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.updateWholeCakeOrderForCouponPubFlag(sqlMap, paramsMap);
	}
	
	
	/**
	 * 홀케익 예약 확정 LMS 발송 대상 예약 추출
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public List<WholecakeOrderDto> getWholeCakeOrderListForLms(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return this.xoWholecakeOrderDao.getWholeCakeOrderListForLms(sqlMap, paramsMap);
	}
	
	/**
	 * 홀케익 예약 상태 알림 등록
	 * @param sqlMap
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertWholecakeOrderNoti(SqlMapClient sqlMap, WholecakeOrderNotiDto params) throws SQLException {
		return this.xoWholecakeOrderDao.insertWholecakeOrderNoti(sqlMap, params);
	}

	/**
	 * 발주 확정일자의 전일 발주 확정 카운터 조회
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getYesterdayWholecakeOrderConfirm(Map<String, Object> dbMap) throws Exception {
		return this.xoWholecakeOrderDao.getYesterdayWholecakeOrderConfirm(dbMap);
	}

	/**
	 * 홀케익 예약 상태 알림 등록(XO PUSH 발송 요청 테이블에 등록)
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
