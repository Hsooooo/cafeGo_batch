/*
 * @(#) $Id: PaymentDto.java,v 1.4 2017/08/01 06:43:56 shinepop Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * 결제 정보 PaymentDto.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.4 $
 */
/**
 * @author "hw.Jang"
 *
 */
public class PaymentDto {
	
	private String 	order_no; 				// 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	private Integer payment_order; 			// 결제순번
	private String 	pay_method; 			// 결제방식{S-스타벅스카드, C-신용카드, P-핸드폰, B-계좌이체, K-KT, U-U+, T-물품형 Gift}
	private Integer amount; 				// 결제금액
	private String 	sbc_card_no; 			// 스타벅스카드번호{암호화}
	private Integer sbc_remain_amt; 		// 스타벅스카드 잔액
	private String  tid; 					// PG거래번호
	private Date    app_date; 				// 승인일시
	private String  app_date_str;			// 승인일시{YYYYMMDDHH24MISS}
	private String  result_code; 			// 결과코드
	private Date 	cancel_date; 			// 취소일시
	private String 	cancel_result_code;		// 취소결과
	private String 	result_msg; 			// 결과내용
	private String 	status; 				// 상태{P-결제, C-취소, R-환불}
	private String  tel_app_date;			// 통신사제휴 승인일시
	private String  auth_num;				// 생년월일
	private String  gift_no;				// 선물 번호
	private String 	prcm_frst_code;			// 제휴사 앞자리 코드(prefix)
	private String	mbl_gfcr_no;			// 모바일상품권번호(쿠폰번호)
	private String	mbl_gfcr_use_cnsnt_no;	// 모바일상품권사용승인번호
	private String 	mbl_gfcr_cnclt_cnsnt_no;// 모바일상품권취소승인번호
	private String 	pgcm_code;				// PG사 코드{004: 스마트로}
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("PaymentDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", payment_order=").append (this.payment_order);
		str.append (", pay_method=").append (this.pay_method);
		str.append (", amount=").append (this.amount);
		str.append (", sbc_card_no=").append (this.sbc_card_no);
		str.append (", sbc_remain_amt=").append (this.sbc_remain_amt);
		str.append (", tid=").append (this.tid);
		str.append (", app_date=").append (this.app_date);
		str.append (", app_date_str=").append (this.app_date_str);
		str.append (", result_code=").append (this.result_code);
		str.append (", cancel_date=").append (this.cancel_date);
		str.append (", cancel_result=").append (this.cancel_result_code);
		str.append (", result_msg=").append (this.result_msg);
		str.append (", status=").append (this.status);
		str.append (", tel_app_date=").append (this.tel_app_date);
		str.append (", auth_num=").append (this.auth_num);
		str.append (", gift_no=").append (this.gift_no);
		str.append (", prcm_frst_code=").append (this.prcm_frst_code);
		str.append (", mbl_gfcr_no=").append (this.mbl_gfcr_no);
		str.append (", mbl_gfcr_use_cnsnt_no=").append (this.mbl_gfcr_use_cnsnt_no);
		str.append (", mbl_gfcr_cnclt_cnsnt_no=").append (this.mbl_gfcr_cnclt_cnsnt_no);
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * 생년월일(YYMMDD)
	 * @return
	 */
	public String getAuth_num() {
		return auth_num;
	}

	/**생년월일(YYMMDD)
	 * @param auth_num
	 */
	public void setAuth_num(String auth_num) {
		this.auth_num = auth_num;
	}

	/**
	 * 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * 결제순번
	 * @return payment_order
	 */
	public Integer getPayment_order () {
		return payment_order == null ? 0 : payment_order;
	}
	
	/**
	 * 결제순번
	 * @param payment_order
	 */
	public void setPayment_order ( Integer payment_order ) {
		this.payment_order = payment_order;
	}
	
	/**
	 * 결제방식{S-스타벅스카드, C-신용카드, P-핸드폰, B-계좌이체, K-KT, U-U+, T-물품형 Gift}
	 * @return pay_method
	 */
	public String getPay_method () {
		return pay_method;
	}
	
	/**
	 * 결제방식{S-스타벅스카드, C-신용카드, P-핸드폰, B-계좌이체, K-KT, U-U+, T-물품형 Gift}
	 * @param pay_method
	 */
	public void setPay_method ( String pay_method ) {
		this.pay_method = pay_method;
	}
	
	/**
	 * 결제금액
	 * @return amount
	 */
	public Integer getAmount () {
		return amount == null ? 0 : amount;
	}
	
	/**
	 * 결제금액
	 * @param amount
	 */
	public void setAmount ( Integer amount ) {
		this.amount = amount;
	}
	
	/**
	 * 스타벅스카드번호{암호화}
	 * @return sbc_card_no
	 */
	public String getSbc_card_no () {
		return sbc_card_no;
	}
	
	/**
	 * 스타벅스카드번호{암호화}
	 * @param sbc_card_no
	 */
	public void setSbc_card_no ( String sbc_card_no ) {
		this.sbc_card_no = sbc_card_no;
	}
	
	/**
	 * 스타벅스카드 잔액
	 * @return sbc_remain_amt
	 */
	public Integer getSbc_remain_amt () {
		return sbc_remain_amt == null ? 0 : sbc_remain_amt;
	}
	
	/**
	 * 스타벅스카드 잔액
	 * @param sbc_remain_amt
	 */
	public void setSbc_remain_amt ( Integer sbc_remain_amt ) {
		this.sbc_remain_amt = sbc_remain_amt;
	}
	
	/**
	 * PG거래번호
	 * @return tid
	 */
	public String getTid () {
		return tid;
	}
	
	/**
	 * PG거래번호
	 * @param tid
	 */
	public void setTid ( String tid ) {
		this.tid = tid;
	}
	
	/**
	 * 승인일시
	 * @return app_date
	 */
	public Date getApp_date () {
		return app_date;
	}
	
	/**
	 * 승인일시
	 * @param app_date
	 */
	public void setApp_date ( Date app_date ) {
		this.app_date = app_date;
	}
	
	/**
	 * 승인일시{YYYYMMDDHH24MISS}
	 * @return
	 */
	public String getApp_date_str() {
		return app_date_str;
	}

	/**
	 * 승인일시{YYYYMMDDHH24MISS}
	 * @param app_date_str
	 */
	public void setApp_date_str(String app_date_str) {
		this.app_date_str = app_date_str;
	}

	/**
	 * 결과코드
	 * @return result_code
	 */
	public String getResult_code () {
		return result_code;
	}
	
	/**
	 * 결과코드
	 * @param result_code
	 */
	public void setResult_code ( String result_code ) {
		this.result_code = result_code;
	}
	
	/**
	 * 취소일시
	 * @return cancel_date
	 */
	public Date getCancel_date () {
		return cancel_date;
	}
	
	/**
	 * 취소일시
	 * @param cancel_date
	 */
	public void setCancel_date ( Date cancel_date ) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * 취소결과
	 * @return cancel_result
	 */
	public String getCancel_result_code () {
		return cancel_result_code;
	}
	
	/**
	 * 취소결과
	 * @param cancel_result_code
	 */
	public void setCancel_result_code ( String cancel_result_code ) {
		this.cancel_result_code = cancel_result_code;
	}
	
	/**
	 * 결과내용
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * 결과내용
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * 상태{P-결제, C-취소, R-환불}
	 * @return status
	 */
	public String getStatus () {
		return status;
	}
	
	/**
	 * 상태{P-결제, C-취소, R-환불}
	 * @param status
	 */
	public void setStatus ( String status ) {
		this.status = status;
	}

	/**
	 * 통신사제휴 승인일시
	 * @return
	 */
	public String getTel_app_date() {
		return tel_app_date;
	}

	/**
	 * 통신사제휴 승인일시
	 * @param tel_app_date
	 */
	public void setTel_app_date(String tel_app_date) {
		this.tel_app_date = tel_app_date;
	}

	/**
	 * 선물 번호
	 * @return
	 */
	public String getGift_no() {
		return gift_no;
	}

	/**
	 * 선물 번호
	 * @param gift_no
	 */
	public void setGift_no(String gift_no) {
		this.gift_no = gift_no;
	}
	
	
	/**
	 * 모바일상품권번호(쿠폰번호)
	 * @return
	 */
	public String getMbl_gfcr_no() {
		return mbl_gfcr_no;
	}

	/**
	 * 모바일상품권번호(쿠폰번호)
	 * @param mbl_gfcr_no
	 */
	public void setMbl_gfcr_no(String mbl_gfcr_no) {
		this.mbl_gfcr_no = mbl_gfcr_no;
	}

	/**
	 * 모바일상품권사용승인번호
	 * @return
	 */
	public String getMbl_gfcr_use_cnsnt_no() {
		return mbl_gfcr_use_cnsnt_no;
	}

	/**
	 * 모바일상품권사용승인번호
	 * @param mbl_gfcr_use_cnsnt_no
	 */
	public void setMbl_gfcr_use_cnsnt_no(String mbl_gfcr_use_cnsnt_no) {
		this.mbl_gfcr_use_cnsnt_no = mbl_gfcr_use_cnsnt_no;
	}

	/**
	 * 모바일상품권취소승인번호
	 * @return
	 */
	public String getMbl_gfcr_cnclt_cnsnt_no() {
		return mbl_gfcr_cnclt_cnsnt_no;
	}

	/**
	 * 모바일상품권취소승인번호
	 * @param mbl_gfcr_cnclt_cnsnt_no
	 */
	public void setMbl_gfcr_cnclt_cnsnt_no(String mbl_gfcr_cnclt_cnsnt_no) {
		this.mbl_gfcr_cnclt_cnsnt_no = mbl_gfcr_cnclt_cnsnt_no;
	}

	/**
	 * 제휴사 앞자리코드(prefix)
	 * @return
	 */
	public String getPrcm_frst_code() {
		return prcm_frst_code;
	}
	
	/**
	 * 제휴사 앞자리코드(prefix)
	 * @param prcmFrstCode
	 */
	public void setPrcm_frst_code(String prcm_frst_code) {
		this.prcm_frst_code = prcm_frst_code;
	}

	/**
	 * PG사 코드{004: 스마트로}
	 * @return
	 */
	public String getPgcm_code() {
		return pgcm_code;
	}

	/**
	 * PG사 코드{004: 스마트로}
	 * @param pgcm_code
	 */
	public void setPgcm_code(String pgcm_code) {
		this.pgcm_code = pgcm_code;
	}
	
}
