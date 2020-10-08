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
	
	private String order_no; // �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	private String user_id; // ����ھ��̵�
	private String target_card_number; // ����ī���ȣ{��ȣȭ}
	private String result; // ���{1-����, 0-����}
	private String result_msg; // ����޼���
	private Date reg_date; // ó���Ͻ�
	private Long amount; // �ݾ�
	private String sbc_type; // ����{1-���ī��, 2-eGift}
	private String sbc_nickname; // ��Ÿ����ī�� �г���
	
	private String pin_number; // �ɹ�ȣ
	
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
	 * �ֹ���ȣ{""3""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ{""3""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * ����ھ��̵�
	 * @return user_id
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * ����ھ��̵�
	 * @param user_id
	 */
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * ����ī���ȣ{��ȣȭ}
	 * @return target_card_number
	 */
	public String getTarget_card_number () {
		return target_card_number;
	}
	
	/**
	 * ����ī���ȣ{��ȣȭ}
	 * @param target_card_number
	 */
	public void setTarget_card_number ( String target_card_number ) {
		this.target_card_number = target_card_number;
	}
	
	/**
	 * ���{1-����, 0-����}
	 * @return result
	 */
	public String getResult () {
		return result;
	}
	
	/**
	 * ���{1-����, 0-����}
	 * @param result
	 */
	public void setResult ( String result ) {
		this.result = result;
	}
	
	/**
	 * ����޼���
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * ����޼���
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * ó���Ͻ�
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * ó���Ͻ�
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �ݾ�
	 * @return amount
	 */
	public Long getAmount () {
		return amount;
	}
	
	/**
	 * �ݾ�
	 * @param amount
	 */
	public void setAmount ( Long amount ) {
		this.amount = amount;
	}
	
	/**
	 * ����{1-���ī��, 2-eGift}
	 * @return sbc_type
	 */
	public String getSbc_type () {
		return sbc_type;
	}
	
	/**
	 * ����{1-���ī��, 2-eGift}
	 * @param sbc_type
	 */
	public void setSbc_type ( String sbc_type ) {
		this.sbc_type = sbc_type;
	}
	
	/**
	 * ��Ÿ����ī�� �г���
	 * @return sbc_nickname
	 */
	public String getSbc_nickname () {
		return sbc_nickname;
	}
	
	/**
	 * ��Ÿ����ī�� �г���
	 * @param sbc_nickname
	 */
	public void setSbc_nickname ( String sbc_nickname ) {
		this.sbc_nickname = sbc_nickname;
	}
	
	/**
	 * �ɹ�ȣ
	 * @return pin_number
	 */
	public String getPin_number () {
		return pin_number;
	}
	
	/**
	 * �ɹ�ȣ
	 * @param pin_number
	 */
	public void setPin_number ( String pin_number ) {
		this.pin_number = pin_number;
	}
}
