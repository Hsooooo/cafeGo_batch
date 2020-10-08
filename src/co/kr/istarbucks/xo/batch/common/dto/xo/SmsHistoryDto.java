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
 * SMS 발송 이력 SmsHistory.
 * @author eZEN ksy
 * @since 2014. 2. 5.
 * @version $Revision: 1.1 $
 */
public class SmsHistoryDto {
	
	private Long mt_pr; // 메세지 고유 아이디
	private String phone; // 휴대폰번호{암호화}
	private String user_id; // 사용자아이디
	private String content; // 메세지
	private String order_no; // 주문번호
	private Date reg_date; // 등록일
	
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
	 * 메세지 고유 아이디
	 * @return mt_pr
	 */
	public Long getMt_pr () {
		return mt_pr;
	}
	
	/**
	 * 메세지 고유 아이디
	 * @param mt_pr
	 */
	public void setMt_pr ( Long mt_pr ) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * 휴대폰번호{암호화}
	 * @return phone
	 */
	public String getPhone () {
		return phone;
	}
	
	/**
	 * 휴대폰번호{암호화}
	 * @param phone
	 */
	public void setPhone ( String phone ) {
		this.phone = phone;
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
	 * 메세지
	 * @return content
	 */
	public String getContent () {
		return content;
	}
	
	/**
	 * 메세지
	 * @param content
	 */
	public void setContent ( String content ) {
		this.content = content;
	}
	
	/**
	 * 주문번호
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * 등록일
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 등록일
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
}
