/*
 * @(#) $Id: CouponGiftHistoryDto.java,v 1.1 2016/11/10 00:55:26 dev99 Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

/**
 * CouponGiftHistoryDto - ���� ���� �̷�
 * @author ksy
 * @since 2016. 11. 07.
 * @version $Revision: 1.1 $
 */
public class CouponGiftHistoryDto {

	private long   gift_hist_no;            // �����̷��Ϸù�ȣ
	private String coupon_number;           // �����Ϸù�ȣ
	private String gift_send_coupon_num;    // �����������۹�ȣ
	private String history_code;            // �����ڵ�{1-����,2-������,3-ȸ��,4-�缱��,5-��ȸ��,6-���,7-������}
	private Date   gift_date;               // ��������
	private String send_user_number;        // ���� ����� ������ȣ
	private String gift_send_mobile_num;    // ���������޴���ȭ��ȣ
	private int    mms_card_seqno;          // ī�����{ī�װ��� ����}
	private String mms_card_name;           // ����MMSī�� ��
	private String gift_mms_card_category;  // ����MMSī�� ī�װ�
	private String mms_card_img_url;        // ����MMSī�� �̹��� URL
	private String mms_title;               // MMS����
	private String mms_message;             // MMS�޽���
	private String mms_img_1;               // MMS�̹���1
	private String mms_img_2;               // MMS�̹���2
	private String mms_img_3;               // MMS�̹���3
	private long   mms_seq;                 // MMS�߼ۿ�û�Ϸù�ȣ
	private String gift_method;             // �������{W-WEB, P-POS, A-APP, S-ADMIN, M-MOBILE}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append(", gift_hist_no="          ).append(this.gift_hist_no);
		sb.append(", coupon_number="         ).append(this.coupon_number);
		sb.append(", gift_send_coupon_num="  ).append(this.gift_send_coupon_num);
		sb.append(", history_code="          ).append(this.history_code);
		sb.append(", gift_date="             ).append(this.gift_date);
		sb.append(", send_user_number="      ).append(this.send_user_number);
		sb.append(", gift_send_mobile_num="  ).append(this.gift_send_mobile_num);
		sb.append(", mms_card_seqno="        ).append(this.mms_card_seqno);
		sb.append(", mms_card_name="         ).append(this.mms_card_name);
		sb.append(", gift_mms_card_category=").append(this.gift_mms_card_category);
		sb.append(", mms_card_img_url="      ).append(this.mms_card_img_url);
		sb.append(", mms_title="             ).append(this.mms_title);
		sb.append(", mms_message="           ).append(this.mms_message);
		sb.append(", mms_img_1="             ).append(this.mms_img_1);
		sb.append(", mms_img_2="             ).append(this.mms_img_2);
		sb.append(", mms_img_3="             ).append(this.mms_img_3);
		sb.append(", mms_seq="               ).append(this.mms_seq);
		sb.append(", gift_method="           ).append(this.gift_method);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * �����̷��Ϸù�ȣ
	 * @return gift_hist_no
	 */
	public long getGift_hist_no() {
		return gift_hist_no;
	}

	/**
	 * �����̷��Ϸù�ȣ
	 * @param gift_hist_no
	 */
	public void setGift_hist_no(long gift_hist_no) {
		this.gift_hist_no = gift_hist_no;
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
	 * �����������۹�ȣ
	 * @return gift_send_coupon_num
	 */
	public String getGift_send_coupon_num() {
		return gift_send_coupon_num;
	}

	/**
	 * �����������۹�ȣ
	 * @param gift_send_coupon_num
	 */
	public void setGift_send_coupon_num(String gift_send_coupon_num) {
		this.gift_send_coupon_num = gift_send_coupon_num;
	}
	
	/**
	 * �����ڵ�{1-����,2-������,3-ȸ��,4-�缱��,5-��ȸ��,6-���,7-������}
	 * @return history_code
	 */
	public String getHistory_code() {
		return history_code;
	}

	/**
	 * �����ڵ�{1-����,2-������,3-ȸ��,4-�缱��,5-��ȸ��,6-���,7-������}
	 * @param history_code
	 */
	public void setHistory_code(String history_code) {
		this.history_code = history_code;
	}
	
	/**
	 * ��������
	 * @return gift_date
	 */
	public Date getGift_date() {
		return gift_date;
	}

	/**
	 * ��������
	 * @param gift_date
	 */
	public void setGift_date(Date gift_date) {
		this.gift_date = gift_date;
	}
	
	/**
	 * ���� ����� ������ȣ
	 * @return send_user_number
	 */
	public String getSend_user_number() {
		return send_user_number;
	}

	/**
	 * ���� ����� ������ȣ
	 * @param send_user_number
	 */
	public void setSend_user_number(String send_user_number) {
		this.send_user_number = send_user_number;
	}
	
	/**
	 * ���������޴���ȭ��ȣ
	 * @return gift_send_mobile_num
	 */
	public String getGift_send_mobile_num() {
		return gift_send_mobile_num;
	}

	/**
	 * ���������޴���ȭ��ȣ
	 * @param gift_send_mobile_num
	 */
	public void setGift_send_mobile_num(String gift_send_mobile_num) {
		this.gift_send_mobile_num = gift_send_mobile_num;
	}
	
	/**
	 * ī�����{ī�װ��� ����}
	 * @return mms_card_seqno
	 */
	public int getMms_card_seqno() {
		return mms_card_seqno;
	}

	/**
	 * ī�����{ī�װ��� ����}
	 * @param mms_card_seqno
	 */
	public void setMms_card_seqno(int mms_card_seqno) {
		this.mms_card_seqno = mms_card_seqno;
	}
	
	/**
	 * ����MMSī�� ��
	 * @return mms_card_name
	 */
	public String getMms_card_name() {
		return mms_card_name;
	}

	/**
	 * ����MMSī�� ��
	 * @param mms_card_name
	 */
	public void setMms_card_name(String mms_card_name) {
		this.mms_card_name = mms_card_name;
	}
	
	/**
	 * ����MMSī�� ī�װ�
	 * @return gift_mms_card_category
	 */
	public String getGift_mms_card_category() {
		return gift_mms_card_category;
	}

	/**
	 * ����MMSī�� ī�װ�
	 * @param gift_mms_card_category
	 */
	public void setGift_mms_card_category(String gift_mms_card_category) {
		this.gift_mms_card_category = gift_mms_card_category;
	}
	
	/**
	 * ����MMSī�� �̹��� URL
	 * @return mms_card_img_url
	 */
	public String getMms_card_img_url() {
		return mms_card_img_url;
	}

	/**
	 * ����MMSī�� �̹��� URL
	 * @param mms_card_img_url
	 */
	public void setMms_card_img_url(String mms_card_img_url) {
		this.mms_card_img_url = mms_card_img_url;
	}
	
	/**
	 * MMS����
	 * @return mms_title
	 */
	public String getMms_title() {
		return mms_title;
	}

	/**
	 * MMS����
	 * @param mms_title
	 */
	public void setMms_title(String mms_title) {
		this.mms_title = mms_title;
	}
	
	/**
	 * MMS�޽���
	 * @return mms_message
	 */
	public String getMms_message() {
		return mms_message;
	}

	/**
	 * MMS�޽���
	 * @param mms_message
	 */
	public void setMms_message(String mms_message) {
		this.mms_message = mms_message;
	}
	
	/**
	 * MMS�̹���1
	 * @return mms_img_1
	 */
	public String getMms_img_1() {
		return mms_img_1;
	}

	/**
	 * MMS�̹���1
	 * @param mms_img_1
	 */
	public void setMms_img_1(String mms_img_1) {
		this.mms_img_1 = mms_img_1;
	}
	
	/**
	 * MMS�̹���2
	 * @return mms_img_2
	 */
	public String getMms_img_2() {
		return mms_img_2;
	}

	/**
	 * MMS�̹���2
	 * @param mms_img_2
	 */
	public void setMms_img_2(String mms_img_2) {
		this.mms_img_2 = mms_img_2;
	}
	
	/**
	 * MMS�̹���3
	 * @return mms_img_3
	 */
	public String getMms_img_3() {
		return mms_img_3;
	}

	/**
	 * MMS�̹���3
	 * @param mms_img_3
	 */
	public void setMms_img_3(String mms_img_3) {
		this.mms_img_3 = mms_img_3;
	}
	
	/**
	 * MMS�߼ۿ�û�Ϸù�ȣ
	 * @return mms_seq
	 */
	public long getMms_seq() {
		return mms_seq;
	}

	/**
	 * MMS�߼ۿ�û�Ϸù�ȣ
	 * @param mms_seq
	 */
	public void setMms_seq(long mms_seq) {
		this.mms_seq = mms_seq;
	}
	
	/**
	 * �������{W-WEB, P-POS, A-APP, S-ADMIN, M-MOBILE}
	 * @return gift_method
	 */
	public String getGift_method() {
		return gift_method;
	}

	/**
	 * �������{W-WEB, P-POS, A-APP, S-ADMIN, M-MOBILE}
	 * @param gift_method
	 */
	public void setGift_method(String gift_method) {
		this.gift_method = gift_method;
	}
}
