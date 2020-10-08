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
 * ���� ���� PaymentDto.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.4 $
 */
/**
 * @author "hw.Jang"
 *
 */
public class PaymentDto {
	
	private String 	order_no; 				// �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	private Integer payment_order; 			// ��������
	private String 	pay_method; 			// �������{S-��Ÿ����ī��, C-�ſ�ī��, P-�ڵ���, B-������ü, K-KT, U-U+, T-��ǰ�� Gift}
	private Integer amount; 				// �����ݾ�
	private String 	sbc_card_no; 			// ��Ÿ����ī���ȣ{��ȣȭ}
	private Integer sbc_remain_amt; 		// ��Ÿ����ī�� �ܾ�
	private String  tid; 					// PG�ŷ���ȣ
	private Date    app_date; 				// �����Ͻ�
	private String  app_date_str;			// �����Ͻ�{YYYYMMDDHH24MISS}
	private String  result_code; 			// ����ڵ�
	private Date 	cancel_date; 			// ����Ͻ�
	private String 	cancel_result_code;		// ��Ұ��
	private String 	result_msg; 			// �������
	private String 	status; 				// ����{P-����, C-���, R-ȯ��}
	private String  tel_app_date;			// ��Ż����� �����Ͻ�
	private String  auth_num;				// �������
	private String  gift_no;				// ���� ��ȣ
	private String 	prcm_frst_code;			// ���޻� ���ڸ� �ڵ�(prefix)
	private String	mbl_gfcr_no;			// ����ϻ�ǰ�ǹ�ȣ(������ȣ)
	private String	mbl_gfcr_use_cnsnt_no;	// ����ϻ�ǰ�ǻ����ι�ȣ
	private String 	mbl_gfcr_cnclt_cnsnt_no;// ����ϻ�ǰ����ҽ��ι�ȣ
	private String 	pgcm_code;				// PG�� �ڵ�{004: ����Ʈ��}
	
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
	 * �������(YYMMDD)
	 * @return
	 */
	public String getAuth_num() {
		return auth_num;
	}

	/**�������(YYMMDD)
	 * @param auth_num
	 */
	public void setAuth_num(String auth_num) {
		this.auth_num = auth_num;
	}

	/**
	 * �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * ��������
	 * @return payment_order
	 */
	public Integer getPayment_order () {
		return payment_order == null ? 0 : payment_order;
	}
	
	/**
	 * ��������
	 * @param payment_order
	 */
	public void setPayment_order ( Integer payment_order ) {
		this.payment_order = payment_order;
	}
	
	/**
	 * �������{S-��Ÿ����ī��, C-�ſ�ī��, P-�ڵ���, B-������ü, K-KT, U-U+, T-��ǰ�� Gift}
	 * @return pay_method
	 */
	public String getPay_method () {
		return pay_method;
	}
	
	/**
	 * �������{S-��Ÿ����ī��, C-�ſ�ī��, P-�ڵ���, B-������ü, K-KT, U-U+, T-��ǰ�� Gift}
	 * @param pay_method
	 */
	public void setPay_method ( String pay_method ) {
		this.pay_method = pay_method;
	}
	
	/**
	 * �����ݾ�
	 * @return amount
	 */
	public Integer getAmount () {
		return amount == null ? 0 : amount;
	}
	
	/**
	 * �����ݾ�
	 * @param amount
	 */
	public void setAmount ( Integer amount ) {
		this.amount = amount;
	}
	
	/**
	 * ��Ÿ����ī���ȣ{��ȣȭ}
	 * @return sbc_card_no
	 */
	public String getSbc_card_no () {
		return sbc_card_no;
	}
	
	/**
	 * ��Ÿ����ī���ȣ{��ȣȭ}
	 * @param sbc_card_no
	 */
	public void setSbc_card_no ( String sbc_card_no ) {
		this.sbc_card_no = sbc_card_no;
	}
	
	/**
	 * ��Ÿ����ī�� �ܾ�
	 * @return sbc_remain_amt
	 */
	public Integer getSbc_remain_amt () {
		return sbc_remain_amt == null ? 0 : sbc_remain_amt;
	}
	
	/**
	 * ��Ÿ����ī�� �ܾ�
	 * @param sbc_remain_amt
	 */
	public void setSbc_remain_amt ( Integer sbc_remain_amt ) {
		this.sbc_remain_amt = sbc_remain_amt;
	}
	
	/**
	 * PG�ŷ���ȣ
	 * @return tid
	 */
	public String getTid () {
		return tid;
	}
	
	/**
	 * PG�ŷ���ȣ
	 * @param tid
	 */
	public void setTid ( String tid ) {
		this.tid = tid;
	}
	
	/**
	 * �����Ͻ�
	 * @return app_date
	 */
	public Date getApp_date () {
		return app_date;
	}
	
	/**
	 * �����Ͻ�
	 * @param app_date
	 */
	public void setApp_date ( Date app_date ) {
		this.app_date = app_date;
	}
	
	/**
	 * �����Ͻ�{YYYYMMDDHH24MISS}
	 * @return
	 */
	public String getApp_date_str() {
		return app_date_str;
	}

	/**
	 * �����Ͻ�{YYYYMMDDHH24MISS}
	 * @param app_date_str
	 */
	public void setApp_date_str(String app_date_str) {
		this.app_date_str = app_date_str;
	}

	/**
	 * ����ڵ�
	 * @return result_code
	 */
	public String getResult_code () {
		return result_code;
	}
	
	/**
	 * ����ڵ�
	 * @param result_code
	 */
	public void setResult_code ( String result_code ) {
		this.result_code = result_code;
	}
	
	/**
	 * ����Ͻ�
	 * @return cancel_date
	 */
	public Date getCancel_date () {
		return cancel_date;
	}
	
	/**
	 * ����Ͻ�
	 * @param cancel_date
	 */
	public void setCancel_date ( Date cancel_date ) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * ��Ұ��
	 * @return cancel_result
	 */
	public String getCancel_result_code () {
		return cancel_result_code;
	}
	
	/**
	 * ��Ұ��
	 * @param cancel_result_code
	 */
	public void setCancel_result_code ( String cancel_result_code ) {
		this.cancel_result_code = cancel_result_code;
	}
	
	/**
	 * �������
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * �������
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * ����{P-����, C-���, R-ȯ��}
	 * @return status
	 */
	public String getStatus () {
		return status;
	}
	
	/**
	 * ����{P-����, C-���, R-ȯ��}
	 * @param status
	 */
	public void setStatus ( String status ) {
		this.status = status;
	}

	/**
	 * ��Ż����� �����Ͻ�
	 * @return
	 */
	public String getTel_app_date() {
		return tel_app_date;
	}

	/**
	 * ��Ż����� �����Ͻ�
	 * @param tel_app_date
	 */
	public void setTel_app_date(String tel_app_date) {
		this.tel_app_date = tel_app_date;
	}

	/**
	 * ���� ��ȣ
	 * @return
	 */
	public String getGift_no() {
		return gift_no;
	}

	/**
	 * ���� ��ȣ
	 * @param gift_no
	 */
	public void setGift_no(String gift_no) {
		this.gift_no = gift_no;
	}
	
	
	/**
	 * ����ϻ�ǰ�ǹ�ȣ(������ȣ)
	 * @return
	 */
	public String getMbl_gfcr_no() {
		return mbl_gfcr_no;
	}

	/**
	 * ����ϻ�ǰ�ǹ�ȣ(������ȣ)
	 * @param mbl_gfcr_no
	 */
	public void setMbl_gfcr_no(String mbl_gfcr_no) {
		this.mbl_gfcr_no = mbl_gfcr_no;
	}

	/**
	 * ����ϻ�ǰ�ǻ����ι�ȣ
	 * @return
	 */
	public String getMbl_gfcr_use_cnsnt_no() {
		return mbl_gfcr_use_cnsnt_no;
	}

	/**
	 * ����ϻ�ǰ�ǻ����ι�ȣ
	 * @param mbl_gfcr_use_cnsnt_no
	 */
	public void setMbl_gfcr_use_cnsnt_no(String mbl_gfcr_use_cnsnt_no) {
		this.mbl_gfcr_use_cnsnt_no = mbl_gfcr_use_cnsnt_no;
	}

	/**
	 * ����ϻ�ǰ����ҽ��ι�ȣ
	 * @return
	 */
	public String getMbl_gfcr_cnclt_cnsnt_no() {
		return mbl_gfcr_cnclt_cnsnt_no;
	}

	/**
	 * ����ϻ�ǰ����ҽ��ι�ȣ
	 * @param mbl_gfcr_cnclt_cnsnt_no
	 */
	public void setMbl_gfcr_cnclt_cnsnt_no(String mbl_gfcr_cnclt_cnsnt_no) {
		this.mbl_gfcr_cnclt_cnsnt_no = mbl_gfcr_cnclt_cnsnt_no;
	}

	/**
	 * ���޻� ���ڸ��ڵ�(prefix)
	 * @return
	 */
	public String getPrcm_frst_code() {
		return prcm_frst_code;
	}
	
	/**
	 * ���޻� ���ڸ��ڵ�(prefix)
	 * @param prcmFrstCode
	 */
	public void setPrcm_frst_code(String prcm_frst_code) {
		this.prcm_frst_code = prcm_frst_code;
	}

	/**
	 * PG�� �ڵ�{004: ����Ʈ��}
	 * @return
	 */
	public String getPgcm_code() {
		return pgcm_code;
	}

	/**
	 * PG�� �ڵ�{004: ����Ʈ��}
	 * @param pgcm_code
	 */
	public void setPgcm_code(String pgcm_code) {
		this.pgcm_code = pgcm_code;
	}
	
}
