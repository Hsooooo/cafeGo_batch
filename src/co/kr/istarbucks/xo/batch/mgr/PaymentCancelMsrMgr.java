/*
 * @(#) $Id: PaymentCancelMsrMgr.java,v 1.4 2015/09/11 08:16:26 soonwoo Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardStatusHistDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.CardUseHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.EGiftCardInfoDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.UserRegCardDto;
import co.kr.istarbucks.xo.batch.common.pg.dto.SmartroPgInfoDto;
import co.kr.istarbucks.xo.batch.dao.PaymentCancelMsrDao;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 자동 결제 취소 PaymentCancelMsrMgr.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.4 $
 */
public class PaymentCancelMsrMgr {
	
	private final PaymentCancelMsrDao paymentCancelMsrDao;
	
	public PaymentCancelMsrMgr () {
		paymentCancelMsrDao = new PaymentCancelMsrDao ();
	}
	
	/**
	 * 회원에 등록된 카드 조회
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<UserRegCardDto> getUserRegCardStateList ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.getUserRegCardStateList (msrSqlMap, paramMap);
	}
	
	/**
	 * 사용자 등록카드 잔액 변경
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrUserRegCardBalanceUpd ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.setMsrUserRegCardBalanceUpd (msrSqlMap, paramMap);
	}

	/**
	 * 카드 사용 이력 조회 (스마트오더용)
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public CardUseHistoryDto getMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.getMsrCardUseHistoryForSmartOrder (msrSqlMap, paramMap);
	}
	
	/**
	 * 사용자 등록카드 잔액 변경시 히스토리 등록
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.setMsrCardUseHistoryForSmartOrder (msrSqlMap, paramMap);
	}
	
	/**
	 * 사용 가능한 e-Gift 카드 채번
	 * @param msrSqlMap
	 * @param publicationCnt
	 * @return
	 * @throws Exception
	 */
	public String getPaymentCardListProc ( SqlMapClient msrSqlMap, int publicationCnt ) throws Exception {
		return this.paymentCancelMsrDao.getPaymentCardListProc (msrSqlMap, publicationCnt);
	}
	
	/**
	 * eGiftCard 정보 조회
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	public List<EGiftCardInfoDto> getEGiftCardInfoFront ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		return this.paymentCancelMsrDao.getEGiftCardInfoFront (msrSqlMap, eGiftCardList);
	}
	
	/**
	 * MSR 회원 정보
	 * @param msrSqlMap
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CardRegMemberDto getRegMember ( SqlMapClient msrSqlMap, String userId ) throws Exception {
		return this.paymentCancelMsrDao.getRegMember (msrSqlMap, userId);
	}
	
	/**
	 * MSR 카드 등록
	 * @param msrSqlMap
	 * @param cardDto
	 * @return
	 * @throws Exception
	 */
	public int insertUserRegCard ( SqlMapClient msrSqlMap, UserRegCardDto cardDto ) throws Exception {
		return this.paymentCancelMsrDao.insertUserRegCard (msrSqlMap, cardDto);
	}
	
	/**
	 * MSR : 카드 상태 업데이트
	 * @param msrSqlMap
	 * @param card_number
	 * @param card_status
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public boolean updateCardInfoForRegister ( SqlMapClient msrSqlMap, String card_number, String card_status, String balance ) throws Exception {
		return this.paymentCancelMsrDao.updateCardInfoForRegister (msrSqlMap, card_number, card_status, balance);
	}
	
	/**
	 * 카드 상태 변경 이력 등록
	 * @param msrSqlMap
	 * @param histDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardStatusHist ( SqlMapClient msrSqlMap, CardStatusHistDto histDto ) throws Exception {
		return this.paymentCancelMsrDao.insertCardStatusHist (msrSqlMap, histDto);
	}
	
	/**
	 * 카드 사용 이력 등록
	 * @param msrSqlMap
	 * @param useHistoryDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardUseHistory ( SqlMapClient msrSqlMap, CardUseHistoryDto useHistoryDto ) throws Exception {
		return this.paymentCancelMsrDao.insertCardUseHistory (msrSqlMap, useHistoryDto);
	}
	
	/**
	 * 무기명/미등록 카드 충전 이력 등록
	 * @param msrSqlMap
	 * @param etcMap
	 * @return
	 * @throws Exception
	 */
    public boolean insertCardUseHistoryEtc ( SqlMapClient msrSqlMap, Map<String, Object> etcMap ) throws Exception {
        return this.paymentCancelMsrDao.insertCardUseHistoryEtc (msrSqlMap, etcMap);
    }
	
	
	/**
	 * eGift 카드 사용가능 여부를 X(미사용)으로 수정
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	public int updateEGiftCardInfoUseYnX ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		return this.paymentCancelMsrDao.updateEGiftCardInfoUseYnX (msrSqlMap, eGiftCardList);
	}
	
	/**
	 * 쿠폰 발행 내역 업데이트
	 * @param msrSqlMap
	 * @param couponMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCouponPublicationList ( SqlMapClient msrSqlMap, Map<String, String> couponMap ) throws Exception {
		return this.paymentCancelMsrDao.updateCouponPublicationList (msrSqlMap, couponMap);
	}
	
	
	/**
	 * 스마트로 연동 정보 조회
	 * @param msrSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public SmartroPgInfoDto getSmartroPgInfo ( Map<String, Object> dbMap) throws Exception {
		return this.paymentCancelMsrDao.getSmartroPgInfo (dbMap);
	}
}
