/*
 * @(#) $Id: SendMailQueueDto.java,v 1.1 2016/11/10 00:55:26 dev99 Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

/**
 * SendMailQueueDto - 메일발송 요청
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class SendMailQueueDto {

	private String user_id;           // 수신자 아이디
	private String template;          // 메일템플릿
	private String status;            // 발송상태 {N-미발송, I-발송처리중, E-발송오류, Y-발송완료, Z-수신거부}
	private String mail_args;         // 템플릿 적용 변수
	private Date   reg_date;          // 발송신청일
	private Date   send_date;         // 발송일
	private String mail_title;        // 메일 제목
	private String email;             // 메일주소
	private String coupon_number;     // 쿠폰일련번호
	private Long   seq;               // 일련번호
	private String email_use_yn;      // 이메일사용구분
	private String reserve_type;      // 예약구분
	private String reserve_datetime;  // 예약시간
	private String receipt_yn;        // 수신여부
	private Date   receipt_date;      // 수신확인시간
	
	private String str_reg_date;      // 발송신청일(문자열)
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("  user_id="         ).append(this.user_id);
		sb.append(", template="        ).append(this.template);
		sb.append(", status="          ).append(this.status);
		sb.append(", mail_args="       ).append(this.mail_args);
		sb.append(", reg_date="        ).append(this.reg_date);
		sb.append(", send_date="       ).append(this.send_date);
		sb.append(", mail_title="      ).append(this.mail_title);
		sb.append(", email="           ).append(this.email);
		sb.append(", coupon_number="   ).append(this.coupon_number);
		sb.append(", seq="             ).append(this.seq);
		sb.append(", email_use_yn="    ).append(this.email_use_yn);
		sb.append(", reserve_type="    ).append(this.reserve_type);
		sb.append(", reserve_datetime=").append(this.reserve_datetime);
		sb.append(", receipt_yn="      ).append(this.receipt_yn);
		sb.append(", receipt_date="    ).append(this.receipt_date);
		sb.append(", str_reg_date="    ).append(this.str_reg_date);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 수신자 아이디
	 * @return user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * 수신자 아이디
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * 메일템플릿
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * 메일템플릿
	 * @param template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * 발송상태 {N-미발송, I-발송처리중, E-발송오류, Y-발송완료, Z-수신거부}
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 발송상태 {N-미발송, I-발송처리중, E-발송오류, Y-발송완료, Z-수신거부}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 템플릿 적용 변수
	 * @return mail_args
	 */
	public String getMail_args() {
		return mail_args;
	}

	/**
	 * 템플릿 적용 변수
	 * @param mail_args
	 */
	public void setMail_args(String mail_args) {
		this.mail_args = mail_args;
	}
	
	/**
	 * 발송신청일
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 발송신청일
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 발송일
	 * @return send_date
	 */
	public Date getSend_date() {
		return send_date;
	}

	/**
	 * 발송일
	 * @param send_date
	 */
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	
	/**
	 * 메일 제목
	 * @return mail_title
	 */
	public String getMail_title() {
		return mail_title;
	}

	/**
	 * 메일 제목
	 * @param mail_title
	 */
	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}
	
	/**
	 * 메일주소
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 메일주소
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 쿠폰일련번호
	 * @return coupon_number
	 */
	public String getCoupon_number() {
		return coupon_number;
	}

	/**
	 * 쿠폰일련번호
	 * @param coupon_number
	 */
	public void setCoupon_number(String coupon_number) {
		this.coupon_number = coupon_number;
	}
	
	/**
	 * 일련번호
	 * @return seq
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * 일련번호
	 * @param seq
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	/**
	 * 이메일사용구분
	 * @return email_use_yn
	 */
	public String getEmail_use_yn() {
		return email_use_yn;
	}

	/**
	 * 이메일사용구분
	 * @param email_use_yn
	 */
	public void setEmail_use_yn(String email_use_yn) {
		this.email_use_yn = email_use_yn;
	}
	
	/**
	 * 예약구분
	 * @return reserve_type
	 */
	public String getReserve_type() {
		return reserve_type;
	}

	/**
	 * 예약구분
	 * @param reserve_type
	 */
	public void setReserve_type(String reserve_type) {
		this.reserve_type = reserve_type;
	}
	
	/**
	 * 예약시간
	 * @return reserve_datetime
	 */
	public String getReserve_datetime() {
		return reserve_datetime;
	}

	/**
	 * 예약시간
	 * @param reserve_datetime
	 */
	public void setReserve_datetime(String reserve_datetime) {
		this.reserve_datetime = reserve_datetime;
	}
	
	/**
	 * 수신여부
	 * @return receipt_yn
	 */
	public String getReceipt_yn() {
		return receipt_yn;
	}

	/**
	 * 수신여부
	 * @param receipt_yn
	 */
	public void setReceipt_yn(String receipt_yn) {
		this.receipt_yn = receipt_yn;
	}
	
	/**
	 * 수신확인시간
	 * @return receipt_date
	 */
	public Date getReceipt_date() {
		return receipt_date;
	}

	/**
	 * 수신확인시간
	 * @param receipt_date
	 */
	public void setReceipt_date(Date receipt_date) {
		this.receipt_date = receipt_date;
	}
	
	/**
	 * 발송신청일 (문자열)
	 * @return str_reg_date
	 */
	public String getStr_reg_date() {
		return str_reg_date;
	}

	/**
	 * 발송신청일 (문자열)
	 * @param reg_date
	 */
	public void setStr_reg_date(String str_reg_date) {
		this.str_reg_date = str_reg_date;
	}
	
}
