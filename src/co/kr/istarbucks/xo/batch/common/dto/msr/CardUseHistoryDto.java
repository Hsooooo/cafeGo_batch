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
 * 카드 사용/충전 이력
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
//	private String formular_code; //변수명 변경(2011-08-10, tytolee)
	private String formula_code;
	private String balance_confirm_flag;
	private Date reg_date;
	private String regDate;
	private String cancel; // 충전 취소버튼 사용여부를 위해 추가(2011-08-09, tytolee)
	private String order_no; // 주문 번호 취소를 처리하기 위해 추가(2011-08-09, tytolee)
	private String pg_fail_flag; // 자동 충전일 경우 pg 실패 여부 확인 
	private Integer balance;
	private String business_date;
	private String pos_number;
	private String pos_trd_number;
	private String tid; // 이용조회 페이지 영수증 조회를 위해 추가(2012-09-12)
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
//		str.append(", ").append("formular_code=").append(this.formular_code);  //변수명 변경(2011-08-10, tytolee)
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
	 * 카드 일련번호 (암호화)
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * 카드 일련번호 (암호화)
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * 카드 등록 번호
	 * @return
	 */
	public Integer getCard_reg_number () {
		return card_reg_number;
	}
	
	/**
	 * 카드 등록 번호
	 * @param card_reg_number
	 */
	public void setCard_reg_number ( Integer card_reg_number ) {
		this.card_reg_number = card_reg_number;
	}
	
	/**
	 * 사용자 고유번호
	 * @return
	 */
	public String getUser_number () {
		return user_number;
	}
	
	/**
	 * 사용자 고유번호
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * 이력구분
	 * I : 카드등록, R : 충전, U : 사용, C : 충전취소, X : 사용취소, M : 잔액이전, A : 보정, B : 잔액확인
	 * @return
	 */
	public String getHistory_code () {
		return history_code;
	}
	
	/**
	 * 이력구분
	 * I : 카드등록, R : 충전, U : 사용, C : 충전취소, X : 사용취소, M : 잔액이전, A : 보정, B : 잔액확인
	 * @param history_code
	 */
	public void setHistory_code ( String history_code ) {
		this.history_code = history_code;
	}
	
	/**
	 * 처리금액
	 * @return
	 */
	public Long getAmount () {
		return amount;
	}
	
	/**
	 * 처리금액
	 * @param amount
	 */
	public void setAmount ( Long amount ) {
		this.amount = amount;
	}
	
	/**
	 * 처리매장
	 * @return
	 */
	public String getBranch () {
		return branch;
	}
	
	/**
	 * 처리매장
	 * @param branch
	 */
	public void setBranch ( String branch ) {
		this.branch = branch;
	}
	
	/**
	 * 처리매장명
	 * @return
	 */
	public String getBranch_name () {
		return branch_name;
	}
	
	/**
	 * 처리매장명
	 * @param branch_name
	 */
	public void setBranch_name ( String branch_name ) {
		this.branch_name = branch_name;
	}
	
	/**
	 * 결제수단
	 * @return
	 */
	public String getPayment_type () {
		return payment_type;
	}
	
	/**
	 * 결제수단
	 * @param payment_type
	 */
	public void setPayment_type ( String payment_type ) {
		this.payment_type = payment_type;
	}
	
	/**
	 * 잔액이전카드 (암호화)
	 * @return
	 */
	public String getMerge_card () {
		return merge_card;
	}
	
	/**
	 * 잔액이전카드 (암호화)
	 * @param merge_card
	 */
	public void setMerge_card ( String merge_card ) {
		this.merge_card = merge_card;
	}
	
	// 변수명 변경 start!(2011-08-10, tytolee)
//	/**
//	 * 사용/충전 방식
//	 * W : WEB, P : POS
//	 * 
//	 * @return
//	 */
//	public String getFormular_code() {
//		return formular_code;
//	}
//
//	/**
//	 * 사용/충전 방식
//	 * W : WEB, P : POS
//	 * 
//	 * @param formular_code
//	 */
//	public void setFormular_code(String formular_code) {
//		this.formular_code = formular_code;
//	}
	// 변수명 변경 end!(2011-08-10, tytolee)
	
	/**
	 * 사용/충전 방식
	 * W : WEB, P : POS
	 * @return
	 */
	public String getFormula_code () {
		return formula_code;
	}
	
	/**
	 * 사용/충전 방식
	 * W : WEB, P : POS
	 * @param formular_code
	 */
	public void setFormula_code ( String formula_code ) {
		this.formula_code = formula_code;
	}
	
	/**
	 * 잔액 확정 여부
	 * Y : 확정, N : 미확정
	 * @return
	 */
	public String getBalance_confirm_flag () {
		return balance_confirm_flag;
	}
	
	/**
	 * 잔액 확정 여부
	 * Y : 확정, N : 미확정
	 * @param balance_confirm_flag
	 */
	public void setBalance_confirm_flag ( String balance_confirm_flag ) {
		this.balance_confirm_flag = balance_confirm_flag;
	}
	
	/**
	 * 처리일자
	 * @return
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 처리일자
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
	 * 거래 후 잔액
	 * @return
	 */
	public Integer getBalance () {
		return balance;
	}
	
	/**
	 * 거래 후 잔액
	 * @param balance
	 */
	public void setBalance ( Integer balance ) {
		if ( balance != null ) this.balance = balance;
		else this.balance = 0;
	}
	
	
	/**
	 * 영업일자
	 * @return
	 */
	public String getBusiness_date () {
		return business_date;
	}
	
	/**
	 * 영업일자
	 * @param business_date
	 */
	public void setBusiness_date ( String business_date ) {
		this.business_date = business_date;
	}
	
	/**
	 * pos 번호
	 * @return
	 */
	public String getPos_number () {
		return pos_number;
	}
	
	/**
	 * pos 번호
	 * @param pos_number
	 */
	public void setPos_number ( String pos_number ) {
		this.pos_number = pos_number;
	}
	
	/**
	 * pos 거래번호
	 * @return
	 */
	public String getPos_trd_number () {
		return pos_trd_number;
	}
	
	/**
	 * pos 거래번호
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
	 * 거래 승인/취소 여부
	 * @return
	 */
	public String getCancel_flag () {
		return cancel_flag;
	}
	
	/**
	 * 거래 승인/취소 여부
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
