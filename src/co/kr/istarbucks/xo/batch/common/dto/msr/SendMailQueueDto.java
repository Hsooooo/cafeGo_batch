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
 * SendMailQueueDto - ���Ϲ߼� ��û
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class SendMailQueueDto {

	private String user_id;           // ������ ���̵�
	private String template;          // �������ø�
	private String status;            // �߼ۻ��� {N-�̹߼�, I-�߼�ó����, E-�߼ۿ���, Y-�߼ۿϷ�, Z-���Űź�}
	private String mail_args;         // ���ø� ���� ����
	private Date   reg_date;          // �߼۽�û��
	private Date   send_date;         // �߼���
	private String mail_title;        // ���� ����
	private String email;             // �����ּ�
	private String coupon_number;     // �����Ϸù�ȣ
	private Long   seq;               // �Ϸù�ȣ
	private String email_use_yn;      // �̸��ϻ�뱸��
	private String reserve_type;      // ���౸��
	private String reserve_datetime;  // ����ð�
	private String receipt_yn;        // ���ſ���
	private Date   receipt_date;      // ����Ȯ�νð�
	
	private String str_reg_date;      // �߼۽�û��(���ڿ�)
	
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
	 * ������ ���̵�
	 * @return user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * ������ ���̵�
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * �������ø�
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * �������ø�
	 * @param template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * �߼ۻ��� {N-�̹߼�, I-�߼�ó����, E-�߼ۿ���, Y-�߼ۿϷ�, Z-���Űź�}
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * �߼ۻ��� {N-�̹߼�, I-�߼�ó����, E-�߼ۿ���, Y-�߼ۿϷ�, Z-���Űź�}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * ���ø� ���� ����
	 * @return mail_args
	 */
	public String getMail_args() {
		return mail_args;
	}

	/**
	 * ���ø� ���� ����
	 * @param mail_args
	 */
	public void setMail_args(String mail_args) {
		this.mail_args = mail_args;
	}
	
	/**
	 * �߼۽�û��
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * �߼۽�û��
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �߼���
	 * @return send_date
	 */
	public Date getSend_date() {
		return send_date;
	}

	/**
	 * �߼���
	 * @param send_date
	 */
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	
	/**
	 * ���� ����
	 * @return mail_title
	 */
	public String getMail_title() {
		return mail_title;
	}

	/**
	 * ���� ����
	 * @param mail_title
	 */
	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}
	
	/**
	 * �����ּ�
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * �����ּ�
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * �����Ϸù�ȣ
	 * @return coupon_number
	 */
	public String getCoupon_number() {
		return coupon_number;
	}

	/**
	 * �����Ϸù�ȣ
	 * @param coupon_number
	 */
	public void setCoupon_number(String coupon_number) {
		this.coupon_number = coupon_number;
	}
	
	/**
	 * �Ϸù�ȣ
	 * @return seq
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * �Ϸù�ȣ
	 * @param seq
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	/**
	 * �̸��ϻ�뱸��
	 * @return email_use_yn
	 */
	public String getEmail_use_yn() {
		return email_use_yn;
	}

	/**
	 * �̸��ϻ�뱸��
	 * @param email_use_yn
	 */
	public void setEmail_use_yn(String email_use_yn) {
		this.email_use_yn = email_use_yn;
	}
	
	/**
	 * ���౸��
	 * @return reserve_type
	 */
	public String getReserve_type() {
		return reserve_type;
	}

	/**
	 * ���౸��
	 * @param reserve_type
	 */
	public void setReserve_type(String reserve_type) {
		this.reserve_type = reserve_type;
	}
	
	/**
	 * ����ð�
	 * @return reserve_datetime
	 */
	public String getReserve_datetime() {
		return reserve_datetime;
	}

	/**
	 * ����ð�
	 * @param reserve_datetime
	 */
	public void setReserve_datetime(String reserve_datetime) {
		this.reserve_datetime = reserve_datetime;
	}
	
	/**
	 * ���ſ���
	 * @return receipt_yn
	 */
	public String getReceipt_yn() {
		return receipt_yn;
	}

	/**
	 * ���ſ���
	 * @param receipt_yn
	 */
	public void setReceipt_yn(String receipt_yn) {
		this.receipt_yn = receipt_yn;
	}
	
	/**
	 * ����Ȯ�νð�
	 * @return receipt_date
	 */
	public Date getReceipt_date() {
		return receipt_date;
	}

	/**
	 * ����Ȯ�νð�
	 * @param receipt_date
	 */
	public void setReceipt_date(Date receipt_date) {
		this.receipt_date = receipt_date;
	}
	
	/**
	 * �߼۽�û�� (���ڿ�)
	 * @return str_reg_date
	 */
	public String getStr_reg_date() {
		return str_reg_date;
	}

	/**
	 * �߼۽�û�� (���ڿ�)
	 * @param reg_date
	 */
	public void setStr_reg_date(String str_reg_date) {
		this.str_reg_date = str_reg_date;
	}
	
}
