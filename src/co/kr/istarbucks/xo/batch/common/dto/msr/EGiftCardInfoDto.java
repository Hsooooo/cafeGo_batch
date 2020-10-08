/*
 * @(#) $Id: EGiftCardInfoDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * E-Gift 카드 정보 EGiftCardInfoDto.
 * @author eZEN ksy
 * @since 2014. 1. 18.
 * @version $Revision: 1.1 $
 */
public class EGiftCardInfoDto {
	
	private String card_number; //카드번호
	private String pin_number; //PIN번호
	private int balance; //카드 잔액
	private int card_reg_seq; //카드 등록 일련 번호
	private String amount; //결제 금액
	private String cart_seq; //카트 번호
	private int send_history_seqno; //발송이력순번(코드)
	private String order_dtl_seq; //주문상세 고유번호
	
	/* FD 처리 관련 체크 */
	private String FDActSuccessYn = ""; //FD 활성화 성공여부{Y:성공, etc:실패}
	private String FDRedemSuccessYn = ""; //FD Redemtion 성공여부{Y:성공, etc:실패}
	

	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("EGiftCardInfoDto [");
		str.append ("card_number=").append (this.card_number);
		str.append (", pin_number=").append (this.pin_number);
		str.append (", balance=").append (this.balance);
		str.append (", card_reg_seq=").append (this.card_reg_seq);
		str.append (", amount=").append (this.amount);
		str.append (", cart_seq=").append (this.cart_seq);
		str.append (", send_history_seqno=").append (this.send_history_seqno);
		str.append (", order_dtl_seq=").append (this.order_dtl_seq);
		str.append (", FDActSuccessYn=").append (this.FDActSuccessYn);
		str.append (", FDRedemSuccessYn=").append (this.FDRedemSuccessYn);
		str.append ("]");
		
		return str.toString ();
	}
	
	
	/**
	 * 카드번호
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * 카드번호
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * PIN번호
	 * @return
	 */
	public String getPin_number () {
		return pin_number;
	}
	
	/**
	 * PIN번호
	 * @param pin_number
	 */
	public void setPin_number ( String pin_number ) {
		this.pin_number = pin_number;
	}
	
	/**
	 * 카드 잔액
	 * @return
	 */
	public int getBalance () {
		return balance;
	}
	
	/**
	 * 카드 잔액
	 * @param balance
	 */
	public void setBalance ( int balance ) {
		this.balance = balance;
	}
	
	/**
	 * 카드 등록 일련 번호
	 * @return
	 */
	public int getCard_reg_seq () {
		return card_reg_seq;
	}
	
	/**
	 * 카드 등록 일련 번호
	 * @param card_reg_seq
	 */
	public void setCard_reg_seq ( int card_reg_seq ) {
		this.card_reg_seq = card_reg_seq;
	}
	
	/**
	 * 결제 금액
	 * @return
	 */
	public String getAmount () {
		return amount;
	}
	
	/**
	 * 결제 금액
	 * @param amount
	 */
	public void setAmount ( String amount ) {
		this.amount = amount;
	}
	
	/**
	 * 카트 번호
	 * @return
	 */
	public String getCart_seq () {
		return cart_seq;
	}
	
	/**
	 * 카트 번호
	 * @param cart_seq
	 */
	public void setCart_seq ( String cart_seq ) {
		this.cart_seq = cart_seq;
	}
	
	/**
	 * 주문상세 고유번호
	 * @return
	 */
	public String getOrder_dtl_seq () {
		return order_dtl_seq;
	}
	
	/**
	 * 주문상세 고유번호
	 * @param order_dtl_seq
	 */
	public void setOrder_dtl_seq ( String order_dtl_seq ) {
		this.order_dtl_seq = order_dtl_seq;
	}
	
	/**
	 * 발송이력순번(코드)
	 * @return
	 */
	public int getSend_history_seqno () {
		return send_history_seqno;
	}
	
	/**
	 * 발송이력순번(코드)
	 * @param send_history_seqno
	 */
	public void setSend_history_seqno ( int send_history_seqno ) {
		this.send_history_seqno = send_history_seqno;
	}
	
	/**
	 * FD 활성화 성공여부{Y:성공, etc:실패}
	 * @return
	 */
	public String getFDActSuccessYn () {
		return FDActSuccessYn;
	}
	
	/**
	 * FD 활성화 성공여부{Y:성공, etc:실패}
	 * @param fDActSuccessYn
	 */
	public void setFDActSuccessYn ( String fDActSuccessYn ) {
		FDActSuccessYn = fDActSuccessYn;
	}
	
	/**
	 * FD Redemtion 성공여부{Y:성공, etc:실패}
	 * @return
	 */
	public String getFDRedemSuccessYn () {
		return FDRedemSuccessYn;
	}
	
	/**
	 * FD Redemtion 성공여부{Y:성공, etc:실패}
	 * @param fDRedemSuccessYn
	 */
	public void setFDRedemSuccessYn ( String fDRedemSuccessYn ) {
		FDRedemSuccessYn = fDRedemSuccessYn;
	}
	
}
