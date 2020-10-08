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
 * �ڵ� ���� ��� PaymentCancelMsrMgr.
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
	 * ȸ���� ��ϵ� ī�� ��ȸ
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<UserRegCardDto> getUserRegCardStateList ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.getUserRegCardStateList (msrSqlMap, paramMap);
	}
	
	/**
	 * ����� ���ī�� �ܾ� ����
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrUserRegCardBalanceUpd ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.setMsrUserRegCardBalanceUpd (msrSqlMap, paramMap);
	}

	/**
	 * ī�� ��� �̷� ��ȸ (����Ʈ������)
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public CardUseHistoryDto getMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.getMsrCardUseHistoryForSmartOrder (msrSqlMap, paramMap);
	}
	
	/**
	 * ����� ���ī�� �ܾ� ����� �����丮 ���
	 * @param msrSqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean setMsrCardUseHistoryForSmartOrder ( SqlMapClient msrSqlMap, Map<String, Object> paramMap ) throws Exception {
		return this.paymentCancelMsrDao.setMsrCardUseHistoryForSmartOrder (msrSqlMap, paramMap);
	}
	
	/**
	 * ��� ������ e-Gift ī�� ä��
	 * @param msrSqlMap
	 * @param publicationCnt
	 * @return
	 * @throws Exception
	 */
	public String getPaymentCardListProc ( SqlMapClient msrSqlMap, int publicationCnt ) throws Exception {
		return this.paymentCancelMsrDao.getPaymentCardListProc (msrSqlMap, publicationCnt);
	}
	
	/**
	 * eGiftCard ���� ��ȸ
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	public List<EGiftCardInfoDto> getEGiftCardInfoFront ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		return this.paymentCancelMsrDao.getEGiftCardInfoFront (msrSqlMap, eGiftCardList);
	}
	
	/**
	 * MSR ȸ�� ����
	 * @param msrSqlMap
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CardRegMemberDto getRegMember ( SqlMapClient msrSqlMap, String userId ) throws Exception {
		return this.paymentCancelMsrDao.getRegMember (msrSqlMap, userId);
	}
	
	/**
	 * MSR ī�� ���
	 * @param msrSqlMap
	 * @param cardDto
	 * @return
	 * @throws Exception
	 */
	public int insertUserRegCard ( SqlMapClient msrSqlMap, UserRegCardDto cardDto ) throws Exception {
		return this.paymentCancelMsrDao.insertUserRegCard (msrSqlMap, cardDto);
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
		return this.paymentCancelMsrDao.updateCardInfoForRegister (msrSqlMap, card_number, card_status, balance);
	}
	
	/**
	 * ī�� ���� ���� �̷� ���
	 * @param msrSqlMap
	 * @param histDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardStatusHist ( SqlMapClient msrSqlMap, CardStatusHistDto histDto ) throws Exception {
		return this.paymentCancelMsrDao.insertCardStatusHist (msrSqlMap, histDto);
	}
	
	/**
	 * ī�� ��� �̷� ���
	 * @param msrSqlMap
	 * @param useHistoryDto
	 * @return
	 * @throws Exception
	 */
	public boolean insertCardUseHistory ( SqlMapClient msrSqlMap, CardUseHistoryDto useHistoryDto ) throws Exception {
		return this.paymentCancelMsrDao.insertCardUseHistory (msrSqlMap, useHistoryDto);
	}
	
	/**
	 * �����/�̵�� ī�� ���� �̷� ���
	 * @param msrSqlMap
	 * @param etcMap
	 * @return
	 * @throws Exception
	 */
    public boolean insertCardUseHistoryEtc ( SqlMapClient msrSqlMap, Map<String, Object> etcMap ) throws Exception {
        return this.paymentCancelMsrDao.insertCardUseHistoryEtc (msrSqlMap, etcMap);
    }
	
	
	/**
	 * eGift ī�� ��밡�� ���θ� X(�̻��)���� ����
	 * @param msrSqlMap
	 * @param eGiftCardList
	 * @return
	 * @throws Exception
	 */
	public int updateEGiftCardInfoUseYnX ( SqlMapClient msrSqlMap, List<String> eGiftCardList ) throws Exception {
		return this.paymentCancelMsrDao.updateEGiftCardInfoUseYnX (msrSqlMap, eGiftCardList);
	}
	
	/**
	 * ���� ���� ���� ������Ʈ
	 * @param msrSqlMap
	 * @param couponMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCouponPublicationList ( SqlMapClient msrSqlMap, Map<String, String> couponMap ) throws Exception {
		return this.paymentCancelMsrDao.updateCouponPublicationList (msrSqlMap, couponMap);
	}
	
	
	/**
	 * ����Ʈ�� ���� ���� ��ȸ
	 * @param msrSqlMap
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public SmartroPgInfoDto getSmartroPgInfo ( Map<String, Object> dbMap) throws Exception {
		return this.paymentCancelMsrDao.getSmartroPgInfo (dbMap);
	}
}
