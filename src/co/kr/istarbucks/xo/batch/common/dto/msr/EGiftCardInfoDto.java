/*
 * @(#) $Id: EGiftCardInfoDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * E-Gift ī�� ���� EGiftCardInfoDto.
 * @author eZEN ksy
 * @since 2014. 1. 18.
 * @version $Revision: 1.1 $
 */
public class EGiftCardInfoDto {
	
	private String card_number; //ī���ȣ
	private String pin_number; //PIN��ȣ
	private int balance; //ī�� �ܾ�
	private int card_reg_seq; //ī�� ��� �Ϸ� ��ȣ
	private String amount; //���� �ݾ�
	private String cart_seq; //īƮ ��ȣ
	private int send_history_seqno; //�߼��̷¼���(�ڵ�)
	private String order_dtl_seq; //�ֹ��� ������ȣ
	
	/* FD ó�� ���� üũ */
	private String FDActSuccessYn = ""; //FD Ȱ��ȭ ��������{Y:����, etc:����}
	private String FDRedemSuccessYn = ""; //FD Redemtion ��������{Y:����, etc:����}
	

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
	 * ī���ȣ
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * ī���ȣ
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * PIN��ȣ
	 * @return
	 */
	public String getPin_number () {
		return pin_number;
	}
	
	/**
	 * PIN��ȣ
	 * @param pin_number
	 */
	public void setPin_number ( String pin_number ) {
		this.pin_number = pin_number;
	}
	
	/**
	 * ī�� �ܾ�
	 * @return
	 */
	public int getBalance () {
		return balance;
	}
	
	/**
	 * ī�� �ܾ�
	 * @param balance
	 */
	public void setBalance ( int balance ) {
		this.balance = balance;
	}
	
	/**
	 * ī�� ��� �Ϸ� ��ȣ
	 * @return
	 */
	public int getCard_reg_seq () {
		return card_reg_seq;
	}
	
	/**
	 * ī�� ��� �Ϸ� ��ȣ
	 * @param card_reg_seq
	 */
	public void setCard_reg_seq ( int card_reg_seq ) {
		this.card_reg_seq = card_reg_seq;
	}
	
	/**
	 * ���� �ݾ�
	 * @return
	 */
	public String getAmount () {
		return amount;
	}
	
	/**
	 * ���� �ݾ�
	 * @param amount
	 */
	public void setAmount ( String amount ) {
		this.amount = amount;
	}
	
	/**
	 * īƮ ��ȣ
	 * @return
	 */
	public String getCart_seq () {
		return cart_seq;
	}
	
	/**
	 * īƮ ��ȣ
	 * @param cart_seq
	 */
	public void setCart_seq ( String cart_seq ) {
		this.cart_seq = cart_seq;
	}
	
	/**
	 * �ֹ��� ������ȣ
	 * @return
	 */
	public String getOrder_dtl_seq () {
		return order_dtl_seq;
	}
	
	/**
	 * �ֹ��� ������ȣ
	 * @param order_dtl_seq
	 */
	public void setOrder_dtl_seq ( String order_dtl_seq ) {
		this.order_dtl_seq = order_dtl_seq;
	}
	
	/**
	 * �߼��̷¼���(�ڵ�)
	 * @return
	 */
	public int getSend_history_seqno () {
		return send_history_seqno;
	}
	
	/**
	 * �߼��̷¼���(�ڵ�)
	 * @param send_history_seqno
	 */
	public void setSend_history_seqno ( int send_history_seqno ) {
		this.send_history_seqno = send_history_seqno;
	}
	
	/**
	 * FD Ȱ��ȭ ��������{Y:����, etc:����}
	 * @return
	 */
	public String getFDActSuccessYn () {
		return FDActSuccessYn;
	}
	
	/**
	 * FD Ȱ��ȭ ��������{Y:����, etc:����}
	 * @param fDActSuccessYn
	 */
	public void setFDActSuccessYn ( String fDActSuccessYn ) {
		FDActSuccessYn = fDActSuccessYn;
	}
	
	/**
	 * FD Redemtion ��������{Y:����, etc:����}
	 * @return
	 */
	public String getFDRedemSuccessYn () {
		return FDRedemSuccessYn;
	}
	
	/**
	 * FD Redemtion ��������{Y:����, etc:����}
	 * @param fDRedemSuccessYn
	 */
	public void setFDRedemSuccessYn ( String fDRedemSuccessYn ) {
		FDRedemSuccessYn = fDRedemSuccessYn;
	}
	
}
