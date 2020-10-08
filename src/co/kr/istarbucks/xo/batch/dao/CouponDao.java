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
 * 쿠폰 발행 CouponDao.
 * 
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.1 $
 */
public class CouponDao {
	
	/**
	 * 쿠폰 마스터 정보 조회
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public CouponMasterDto getCouponMaster(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return (CouponMasterDto) sqlMap.queryForObject("coupon.getCouponMaster", paramsMap);
	}
	
	/**
	 * 쿠폰 발행을 위한 MSR 회원 정보 조회
	 * @param sqlMap
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public CardRegMemberDto getCardRegMember(SqlMapClient sqlMap, Map<String, Object> paramsMap) throws SQLException {
		return (CardRegMemberDto) sqlMap.queryForObject("coupon.getCardRegMember", paramsMap);
	}
	
	/**
	 * 쿠폰 번호 채번 (다건)
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
	 * 쿠폰 번호/선물번호 번호 채번 (다건)
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
	 * 쿠폰 발행 내역 다건 등록
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertCouponPublicationListMulti(SqlMapClient sqlMap, List<CouponPublicationListDto> paramList) throws SQLException {
		return sqlMap.update("coupon.insertCouponPublicationListMulti", paramList);
	}
	
	/**
	 * 쿠폰 선물 이력 등록
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public long inserMsrCouponGiftHistory(SqlMapClient sqlMap, CouponGiftHistoryDto params) throws SQLException {
		return (Long) sqlMap.insert("coupon.inserMsrCouponGiftHistory", params);
	}
	
	/**
	 * 쿠폰 발행 내역 업데이트
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int updateCouponPublicationList(SqlMapClient sqlMap, CouponPublicationListDto params) throws SQLException {
		return sqlMap.update("coupon.updateCouponPublicationList", params);
	}
	
	/**
	 * 영업정보에 쿠폰 등록
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertSmkCoupSeqMulti(SqlMapClient sqlMap, List<CouponPublicationListDto> paramList) throws SQLException {
		return sqlMap.update("couponScksa.insertSmkCoupSeqMulti", paramList);
	}		

}
