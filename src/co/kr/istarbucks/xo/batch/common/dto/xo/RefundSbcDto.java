/*
 * @(#) $Id: RefundSbcDto.java,v 1.1 2014/03/03 04:50:54 alcyone Exp $
 *
 * Starbucks XO
 *
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * XO_REFUND_SBC RefundSbcDto.
 * 
 * @author eZEN ksy
 * @since 2014. 1. 2.
 * @version $Revision: 1.1 $
 */
public class RefundSbcDto {
	
	private String order_no; // 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	private String user_id; // 사용자아이디
	private String target_card_number; // 충전카드번호{암호화}
	private String result; // 결과{1-성공, 0-실패}
	private String result_msg; // 결과메세지
	private Date reg_date; // 처리일시
	private Long amount; // 금액
	private String sbc_type; // 구분{1-등록카드, 2-eGift}
	private String sbc_nickname; // 스타벅스카드 닉네임
	
	private String pin_number; // 핀번호
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("RefundSbcDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", target_card_number=").append (this.target_card_number);
		str.append (", result=").append (this.result);
		str.append (", result_msg=").append (this.result_msg);
		str.append (", reg_date=").append (this.reg_date);
		str.append (", amount=").append (this.amount);
		str.append (", sbc_type=").append (this.sbc_type);
		str.append (", sbc_nickname=").append (this.sbc_nickname);
		str.append (", pin_number=").append (this.pin_number);
		str.append ("]");
		
		return str.toString ();
	}
//	
	/**
	 * 주문번호{""3""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호{""3""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * 사용자아이디
	 * @return user_id
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * 사용자아이디
	 * @param user_id
	 */
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * 충전카드번호{암호화}
	 * @return target_card_number
	 */
	public String getTarget_card_number () {
		return target_card_number;
	}
	
	/**
	 * 충전카드번호{암호화}
	 * @param target_card_number
	 */
	public void setTarget_card_number ( String target_card_number ) {
		this.target_card_number = target_card_number;
	}
	
	/**
	 * 결과{1-성공, 0-실패}
	 * @return result
	 */
	public String getResult () {
		return result;
	}
	
	/**
	 * 결과{1-성공, 0-실패}
	 * @param result
	 */
	public void setResult ( String result ) {
		this.result = result;
	}
	
	/**
	 * 결과메세지
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * 결과메세지
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * 처리일시
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 처리일시
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 금액
	 * @return amount
	 */
	public Long getAmount () {
		return amount;
	}
	
	/**
	 * 금액
	 * @param amount
	 */
	public void setAmount ( Long amount ) {
		this.amount = amount;
	}
	
	/**
	 * 구분{1-등록카드, 2-eGift}
	 * @return sbc_type
	 */
	public String getSbc_type () {
		return sbc_type;
	}
	
	/**
	 * 구분{1-등록카드, 2-eGift}
	 * @param sbc_type
	 */
	public void setSbc_type ( String sbc_type ) {
		this.sbc_type = sbc_type;
	}
	
	/**
	 * 스타벅스카드 닉네임
	 * @return sbc_nickname
	 */
	public String getSbc_nickname () {
		return sbc_nickname;
	}
	
	/**
	 * 스타벅스카드 닉네임
	 * @param sbc_nickname
	 */
	public void setSbc_nickname ( String sbc_nickname ) {
		this.sbc_nickname = sbc_nickname;
	}
	
	/**
	 * 핀번호
	 * @return pin_number
	 */
	public String getPin_number () {
		return pin_number;
	}
	
	/**
	 * 핀번호
	 * @param pin_number
	 */
	public void setPin_number ( String pin_number ) {
		this.pin_number = pin_number;
	}
}
