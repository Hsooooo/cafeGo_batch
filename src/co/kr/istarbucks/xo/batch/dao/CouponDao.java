/*
 * @(#) $Id: CouponDao.java,v 1.1 2016/11/10 00:55:25 dev99 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponGiftHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponMasterDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CouponPublicationListDto;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ���� ���� CouponDao.
 * 
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.1 $
 */
public class CouponDao {
	
	/**
	 * ���� ������ ���� ��ȸ
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public CouponMasterDto getCouponMaster(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return (CouponMasterDto) sqlMap.queryForObject("coupon.getCouponMaster", paramsMap);
	}
	
	/**
	 * ���� ������ ���� MSR ȸ�� ���� ��ȸ
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public CardRegMemberDto getCardRegMember(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return (CardRegMemberDto) sqlMap.queryForObject("coupon.getCardRegMember", paramsMap);
	}
	
	/**
	 * ���� ��ȣ ä�� (�ٰ�)
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<CouponPublicationListDto> getCouponNumberList(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.queryForList("coupon.getCouponNumberList", paramsMap);
	}
	
	/**
	 * ���� ��ȣ/������ȣ ��ȣ ä�� (�ٰ�)
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<CouponPublicationListDto> getCouponNumberListForGift(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return sqlMap.queryForList("coupon.getCouponNumberListForGift", paramsMap);
	}
	
	/**
	 * ���� ���� ���� �ٰ� ���
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertCouponPublicationListMulti(SqlMapClient sqlMap, List<CouponPublicationListDto> paramList) throws SQLException {
		return sqlMap.update("coupon.insertCouponPublicationListMulti", paramList);
	}
	
	/**
	 * ���� ���� �̷� ���
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public long inserMsrCouponGiftHistory(SqlMapClient sqlMap, CouponGiftHistoryDto params) throws SQLException {
		return (Long) sqlMap.insert("coupon.inserMsrCouponGiftHistory", params);
	}
	
	/**
	 * ���� ���� ���� ������Ʈ
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int updateCouponPublicationList(SqlMapClient sqlMap, CouponPublicationListDto params) throws SQLException {
		return sqlMap.update("coupon.updateCouponPublicationList", params);
	}
	
	/**
	 * ���������� ���� ���
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertSmkCoupSeqMulti(SqlMapClient sqlMap, List<CouponPublicationListDto> paramList) throws SQLException {
		return sqlMap.update("couponScksa.insertSmkCoupSeqMulti", paramList);
	}		

}
