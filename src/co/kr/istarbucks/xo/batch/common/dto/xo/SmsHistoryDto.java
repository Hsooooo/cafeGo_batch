/*
 * @(#) $Id: SmsHistoryDto.java,v 1.1 2014/03/03 04:50:54 alcyone Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * SMS �߼� �̷� SmsHistory.
 * @author eZEN ksy
 * @since 2014. 2. 5.
 * @version $Revision: 1.1 $
 */
public class SmsHistoryDto {
	
	private Long mt_pr; // �޼��� ���� ���̵�
	private String phone; // �޴�����ȣ{��ȣȭ}
	private String user_id; // ����ھ��̵�
	private String content; // �޼���
	private String order_no; // �ֹ���ȣ
	private Date reg_date; // �����
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("SmsHistory [");
		str.append (", mt_pr=").append (this.mt_pr);
		str.append (", phone=").append (this.phone);
		str.append (", user_id=").append (this.user_id);
		str.append (", content=").append (this.content);
		str.append (", order_no=").append (this.order_no);
		str.append (", reg_date=").append (this.reg_date);
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * �޼��� ���� ���̵�
	 * @return mt_pr
	 */
	public Long getMt_pr () {
		return mt_pr;
	}
	
	/**
	 * �޼��� ���� ���̵�
	 * @param mt_pr
	 */
	public void setMt_pr ( Long mt_pr ) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * �޴�����ȣ{��ȣȭ}
	 * @return phone
	 */
	public String getPhone () {
		return phone;
	}
	
	/**
	 * �޴�����ȣ{��ȣȭ}
	 * @param phone
	 */
	public void setPhone ( String phone ) {
		this.phone = phone;
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
	 * �޼���
	 * @return content
	 */
	public String getContent () {
		return content;
	}
	
	/**
	 * �޼���
	 * @param content
	 */
	public void setContent ( String content ) {
		this.content = content;
	}
	
	/**
	 * �ֹ���ȣ
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * �����
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * �����
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
}
