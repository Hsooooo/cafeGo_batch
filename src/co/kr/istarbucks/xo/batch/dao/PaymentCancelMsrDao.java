/*
 * @(#) $Id: PaymentCancelMsrDao.java,v 1.4 2015/09/11 08:15:12 soonwoo Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardStatusHistDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardUseHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.EGiftCardInfoDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.UserRegCardDto;
import co.kr.istarbucks.xo.batch.common.pg.dto.SmartroPgInfoDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

/**
 * �ڵ� ���� ��� paymentcancelMsrDao.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.4 $
 */
public class PaymentCancelMsrDao {
	
	/**
	 * ȸ���� ��ϵ� ī�� ��ȸ
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<UserRegCardDto> getUserRegCardStateList ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		String sqlId = "paymentcancelMsr.getUserRegCardStateList";
		
		return msrSqlMap.queryForList (sqlId, paramMap);
	}
	
	/**
	 * ����� ���ī�� �ܾ� ����
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrUserRegCardBalanceUpd ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		String sqlId = "paymentcancelMsr.setMsrUserRegCardBalanceUpd";
		return msrSqlMap.update (sqlId, paramMap) == 1 ? true : false;
	}
	
	/**
	 * ī�� ��� �̷� ��ȸ (����Ʈ������)
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public CardUseHistoryDto getMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		String sqlId = "paymentcancelMsr.getMsrCardUseHistoryForSmartOrder";
		return (CardUseHistoryDto) msrSqlMap.queryForObject (sqlId, paramMap);
	}
	
	/**
	 * ����� ���ī�� �ܾ� ����� �����丮 ���
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		String sqlId = "paymentcancelMsr.setMsrCardUseHistoryForSmartOrder";
		return msrSqlMap.update (sqlId, paramMap) == 1 ? true : false;
	}
	
	/**
	 * ��� ������ e-Gift ī�� ä��
	 * @param msrSqlMap
	 * @param publicationCnt
	 * @return
	 * @throws Exception
	 */
	public String getPaymentCardListProc ( SqlMapClient msrSqlMap, int publicationCnt ) throws Exception {
		String sqlId = "paymentcancelMsr.getPaymentCardListProc";
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put ("p_card_cnt", publicationCnt);
		paramMap.put ("r_card_number", "");
		
		msrSqlMap.queryForObject (sqlId, paramMap);
		String cardList = (String) paramMap.get ("r_card_number");
		
		return cardList;
	}
	
	/**
	 * eGiftCard ���� ��ȸ
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<EGiftCardInfoDto> getEGiftCardInfoFront ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		String sqlId = "paymentcancelMsr.getEGiftCardInfoFront";
		return msrSqlMap.queryForList (sqlId, eGiftCardList);
	}
	
	/**
	 * MSR ȸ�� ����
	 * @param msrSqlMap
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CardRegMemberDto getRegMember ( SqlMapClient msrSqlMap, String userId ) throws Exception {
		String sqlId = "paymentcancelMsr.getRegMember";
		return (CardRegMemberDto) msrSqlMap.queryForObject (sqlId, userId);
	}
	
	/**
	 * MSR ī�� ���
	 * @param msrSqlMap
	 * @param cardDto
	 * @return
	 * @throws Exception
	 */
	public int insertUserRegCard ( SqlMapClient msrSqlMap, UserRegCardDto cardDto ) throws Exception {
		String sqlId = "paymentcancelMsr.insertUserRegCard";
		return (Integer) msrSqlMap.insert (sqlId, cardDto);
	}
	
	/**
	 * MSR : ī�� ���� ������Ʈ
	 * @param msrSqlMap
	 * @param card_number
	 * @param card_status
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public boolean updateCardInfoForRegister ( SqlMapClient msrSqlMap, String card_number, String card_status, String balance ) throws Exception {
		String sqlId = "paymentcancelMsr.updateCardInfoForRegister";
		
		Map<String, String> map = new HashMap<String, String> ();
		map.put ("card_number", card_number);
		map.put ("card_status", card_status);
		map.put ("balance", balance);
		
		return msrSqlMap.update (sqlId, map) == 1 ? true : false;
	}
	
	/**
	 * ī�� ���� ���� �̷� ���
	 * @param msrSqlMap
	 * @param histDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardStatusHist ( SqlMapClient msrSqlMap, CardStatusHistDto histDto ) throws Exception {
		String sqlId = "paymentcancelMsr.insertCardStatusHist";
		return msrSqlMap.update (sqlId, histDto) == 1 ? true : false;
	}
	
	/**
	 * ī�� ��� �̷� ���
	 * @param msrSqlMap
	 * @param useHistoryDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardUseHistory ( SqlMapClient msrSqlMap, CardUseHistoryDto useHistoryDto ) throws Exception {
		String sqlId = "paymentcancelMsr.insertCardUseHistory";
		return msrSqlMap.update (sqlId, useHistoryDto) == 1 ? true : false;
	}
	
	
	/**
     * �����/�̵�� ī�� ���� �̷� ���
     * @param msrSqlMap
     * @param etcMap
     * @return
     * @throws Exception
     */
    public boolean insertCardUseHistoryEtc ( SqlMapClient msrSqlMap, Map<String, Object> etcMap ) throws Exception {
        String sqlId = "paymentcancelMsr.insertCardUseHistoryEtc";
        return msrSqlMap.update (sqlId, etcMap) == 1 ? true : false;
    }
	
	/**
	 * eGift ī�� ��밡�� ���θ� X(�̻��)���� ����
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	public int updateEGiftCardInfoUseYnX ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		String sqlId = "paymentcancelMsr.updateEGiftCardInfoUseYnX";
		return msrSqlMap.update (sqlId, eGiftCardList);
	}
	
	/**
	 * ���� ���� ���� ������Ʈ
	 * @param msrSqlMap
	 * @param couponMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCouponPublicationList ( SqlMapClient msrSqlMap, Map<String, String> couponMap ) throws Exception {
		String sqlId = "paymentcancelMsr.updateCouponPublicationList";
		return msrSqlMap.update (sqlId, couponMap) == 1 ? true : false;
	}
	
	public SmartroPgInfoDto getSmartroPgInfo ( Map<String, Object> dbMap ) throws Exception {
		String sqlId = "paymentcancelMsr.getSmartroPgInfo";
		SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance ();
		return (SmartroPgInfoDto) msrSqlMap.queryForObject (sqlId, dbMap);
	}
}
