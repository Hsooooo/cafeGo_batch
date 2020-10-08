/*
 * @(#) $Id: CardUseHistoryDto.java,v 1.2 2015/03/03 04:55:01 ragi Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * ī�� ���/���� �̷�
 * @author leeminjung
 * @version $Revision: 1.2 $
 */

public class CardUseHistoryDto {
	
	private String card_number;
	private Integer card_reg_number;
	private String user_number;
	private String history_code;
	private Long amount;
	private String branch;
	private String branch_name;
	private String payment_type;
	private String merge_card;
//	private String formular_code; //������ ����(2011-08-10, tytolee)
	private String formula_code;
	private String balance_confirm_flag;
	private Date reg_date;
	private String regDate;
	private String cancel; // ���� ��ҹ�ư ��뿩�θ� ���� �߰�(2011-08-09, tytolee)
	private String order_no; // �ֹ� ��ȣ ��Ҹ� ó���ϱ� ���� �߰�(2011-08-09, tytolee)
	private String pg_fail_flag; // �ڵ� ������ ��� pg ���� ���� Ȯ�� 
	private Integer balance;
	private String business_date;
	private String pos_number;
	private String pos_trd_number;
	private String tid; // �̿���ȸ ������ ������ ��ȸ�� ���� �߰�(2012-09-12)
	private String cancel_flag;
	private String send_date;
	private String send_time;
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("CardUseHistoryDto [");
		str.append ("card_number=").append (this.card_number);
		str.append (", ").append ("card_reg_number=").append (this.card_reg_number);
		str.append (", ").append ("user_number=").append (this.user_number);
		str.append (", ").append ("history_code=").append (this.history_code);
		str.append (", ").append ("amount=").append (this.amount);
		str.append (", ").append ("branch=").append (this.branch);
		str.append (", ").append ("branch_name=").append (this.branch_name);
		str.append (", ").append ("payment_type=").append (this.payment_type);
		str.append (", ").append ("merge_card=").append (this.merge_card);
//		str.append(", ").append("formular_code=").append(this.formular_code);  //������ ����(2011-08-10, tytolee)
		str.append (", ").append ("formular_code=").append (this.formula_code);
		str.append (", ").append ("balance_confirm_flag=").append (this.balance_confirm_flag);
		str.append (", ").append ("cancel=").append (this.cancel);
		str.append (", ").append ("order_no=").append (this.order_no);
		str.append (", ").append ("pg_fail_flag=").append (this.pg_fail_flag);
		str.append (", ").append ("balance=").append (this.balance);
		str.append (", ").append ("business_date=").append (this.business_date);
		str.append (", ").append ("pos_number=").append (this.pos_number);
		str.append (", ").append ("pos_trd_number=").append (this.pos_trd_number);
		str.append (", ").append ("tid=").append (this.tid);
		str.append (", ").append ("cancel_flag=").append (this.cancel_flag);
		if ( this.reg_date != null ) {
			str.append (", ").append ("reg_date=").append (DateFormatUtils.format (this.reg_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append(", ").append("send_date=").append(this.send_date);
		str.append(", ").append("send_time=").append(this.send_time);
		str.append ("]");
		
		return str.toString ();
	}
	
	public String getOrder_no () {
		return order_no;
	}
	
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	public String getPg_fail_flag () {
		return pg_fail_flag;
	}
	
	public void setPg_fail_flag ( String pg_fail_flag ) {
		this.pg_fail_flag = pg_fail_flag;
	}
	
	public String getCancel () {
		return cancel;
	}
	
	public void setCancel ( String cancel ) {
		this.cancel = cancel;
	}
	
	/**
	 * ī�� �Ϸù�ȣ (��ȣȭ)
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * ī�� �Ϸù�ȣ (��ȣȭ)
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * ī�� ��� ��ȣ
	 * @return
	 */
	public Integer getCard_reg_number () {
		return card_reg_number;
	}
	
	/**
	 * ī�� ��� ��ȣ
	 * @param card_reg_number
	 */
	public void setCard_reg_number ( Integer card_reg_number ) {
		this.card_reg_number = card_reg_number;
	}
	
	/**
	 * ����� ������ȣ
	 * @return
	 */
	public String getUser_number () {
		return user_number;
	}
	
	/**
	 * ����� ������ȣ
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * �̷±���
	 * I : ī����, R : ����, U : ���, C : �������, X : ������, M : �ܾ�����, A : ����, B : �ܾ�Ȯ��
	 * @return
	 */
	public String getHistory_code () {
		return history_code;
	}
	
	/**
	 * �̷±���
	 * I : ī����, R : ����, U : ���, C : �������, X : ������, M : �ܾ�����, A : ����, B : �ܾ�Ȯ��
	 * @param history_code
	 */
	public void setHistory_code ( String history_code ) {
		this.history_code = history_code;
	}
	
	/**
	 * ó���ݾ�
	 * @return
	 */
	public Long getAmount () {
		return amount;
	}
	
	/**
	 * ó���ݾ�
	 * @param amount
	 */
	public void setAmount ( Long amount ) {
		this.amount = amount;
	}
	
	/**
	 * ó������
	 * @return
	 */
	public String getBranch () {
		return branch;
	}
	
	/**
	 * ó������
	 * @param branch
	 */
	public void setBranch ( String branch ) {
		this.branch = branch;
	}
	
	/**
	 * ó�������
	 * @return
	 */
	public String getBranch_name () {
		return branch_name;
	}
	
	/**
	 * ó�������
	 * @param branch_name
	 */
	public void setBranch_name ( String branch_name ) {
		this.branch_name = branch_name;
	}
	
	/**
	 * ��������
	 * @return
	 */
	public String getPayment_type () {
		return payment_type;
	}
	
	/**
	 * ��������
	 * @param payment_type
	 */
	public void setPayment_type ( String payment_type ) {
		this.payment_type = payment_type;
	}
	
	/**
	 * �ܾ�����ī�� (��ȣȭ)
	 * @return
	 */
	public String getMerge_card () {
		return merge_card;
	}
	
	/**
	 * �ܾ�����ī�� (��ȣȭ)
	 * @param merge_card
	 */
	public void setMerge_card ( String merge_card ) {
		this.merge_card = merge_card;
	}
	
	// ������ ���� start!(2011-08-10, tytolee)
//	/**
//	 * ���/���� ���
//	 * W : WEB, P : POS
//	 * 
//	 * @return
//	 */
//	public String getFormular_code() {
//		return formular_code;
//	}
//
//	/**
//	 * ���/���� ���
//	 * W : WEB, P : POS
//	 * 
//	 * @param formular_code
//	 */
//	public void setFormular_code(String formular_code) {
//		this.formular_code = formular_code;
//	}
	// ������ ���� end!(2011-08-10, tytolee)
	
	/**
	 * ���/���� ���
	 * W : WEB, P : POS
	 * @return
	 */
	public String getFormula_code () {
		return formula_code;
	}
	
	/**
	 * ���/���� ���
	 * W : WEB, P : POS
	 * @param formular_code
	 */
	public void setFormula_code ( String formula_code ) {
		this.formula_code = formula_code;
	}
	
	/**
	 * �ܾ� Ȯ�� ����
	 * Y : Ȯ��, N : ��Ȯ��
	 * @return
	 */
	public String getBalance_confirm_flag () {
		return balance_confirm_flag;
	}
	
	/**
	 * �ܾ� Ȯ�� ����
	 * Y : Ȯ��, N : ��Ȯ��
	 * @param balance_confirm_flag
	 */
	public void setBalance_confirm_flag ( String balance_confirm_flag ) {
		this.balance_confirm_flag = balance_confirm_flag;
	}
	
	/**
	 * ó������
	 * @return
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * ó������
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	public String getRegDate () {
		return regDate;
	}
	
	public void setRegDate ( String regDate ) {
		this.regDate = regDate;
	}
	
	/**
	 * �ŷ� �� �ܾ�
	 * @return
	 */
	public Integer getBalance () {
		return balance;
	}
	
	/**
	 * �ŷ� �� �ܾ�
	 * @param balance
	 */
	public void setBalance ( Integer balance ) {
		if ( balance != null ) this.balance = balance;
		else this.balance = 0;
	}
	
	
	/**
	 * ��������
	 * @return
	 */
	public String getBusiness_date () {
		return business_date;
	}
	
	/**
	 * ��������
	 * @param business_date
	 */
	public void setBusiness_date ( String business_date ) {
		this.business_date = business_date;
	}
	
	/**
	 * pos ��ȣ
	 * @return
	 */
	public String getPos_number () {
		return pos_number;
	}
	
	/**
	 * pos ��ȣ
	 * @param pos_number
	 */
	public void setPos_number ( String pos_number ) {
		this.pos_number = pos_number;
	}
	
	/**
	 * pos �ŷ���ȣ
	 * @return
	 */
	public String getPos_trd_number () {
		return pos_trd_number;
	}
	
	/**
	 * pos �ŷ���ȣ
	 * @param pos_trd_number
	 */
	public void setPos_trd_number ( String pos_trd_number ) {
		this.pos_trd_number = pos_trd_number;
	}
	
	/**
	 * TID
	 * @return
	 */
	public String getTid () {
		return tid;
	}
	
	/**
	 * TID
	 * @param tid
	 */
	public void setTid ( String tid ) {
		this.tid = tid;
	}
	
	/**
	 * �ŷ� ����/��� ����
	 * @return
	 */
	public String getCancel_flag () {
		return cancel_flag;
	}
	
	/**
	 * �ŷ� ����/��� ����
	 * @param cancel_flag
	 */
	public void setCancel_flag ( String cancel_flag ) {
		this.cancel_flag = cancel_flag;
	}

	public String getSend_date() {
		return send_date;
	}

	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
}
