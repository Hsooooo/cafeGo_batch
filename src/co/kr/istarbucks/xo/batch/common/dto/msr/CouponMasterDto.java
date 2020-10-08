/*
 * @(#) $Id: CouponMasterDto.java,v 1.1 2016/11/10 00:55:26 dev99 Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

/**
 * CouponMasterDto - ���� ������
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class CouponMasterDto {

	private String coupon_policy_code;          // ������å�ڵ�
	private Date   reg_date;                    // �����
	private String coupon_code;                 // �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15, 006-����, 007-CoffeeFood, 008-WholeCake, 009-�ڵ�����, 010-e��������, 011-������MMS, 012-���������, 013-1�� ���� ���� ����, 014-�׸��������� ����, 015-��巹������ ����, 016-���� ����, 017-������ ����}
	private String coupon_name;                 // ������
	private String coupon_start_date;           // ���� ��ȿ�Ⱓ ������
	private String coupon_end_date;             // ���� ��ȿ�Ⱓ ������
	private String coupon_desc;                 // ��������
	private String coupon_grade;                // ���� ���� ���{00-WEBCOM�̻�, 10-GREEN�̻�, 20-GOLD�̻�, 99-�������}
	private String pos_cond_text;               // POS ���� ��å ���� ����
	private String pos_target_text;             // POS ���� ��å ���� ����
	private String coupon_type;                 // ��������{1-�Ϲ�, 2-1+1, 3-2+1, 4-Ŀ��Ǫ��, 5-N+1, 6-�ݾ�, 7-����}
	private int    coupon_publication_seq;      // ���� ���� ��ȣ
	private String coupon_publication_date;     // �������� ������
	private String coupon_group;                // ��������{11-��������, 12-��������, 13-FS, 14-1+1����, 15-PO����, 16-PO����, 17-CoffeeFood����, 18-WholeCake����, 19-�ڵ���������, 20-e-������������, 21-������MMS����, 22-�������������, 23-1�� ���� ���� ����, 24-N+1 ����, 26-�ݾ� ����, 27-toGreen ����, 28-toGold ����, 29-���� ����, 30-������ ����}
	private int    publication_gap_date;        // ���������� ���������� ������ ������ sysdate + ����
	private String publication_status;          // �������{N-�̹���, Y-����}
	private int    valid_day;                   // ��ȿ�ϼ�(1. ��������:���Ϸ� ���� ��ȿ �� �� 2. �������� : ���������� �������� sysdate - ����)
	private String user_coupon_name;            // ����� ������
	private String use_product;                 // �̿� ���� ��ǰ
	private String use_notice;                  // ���� ���� ����
	private String email_img;                   // �� ���� �̹���
	private String pos_target_text2;            // POS���� ��å ���� ����2
	private String gift_reg_coupon_title;       // �������������
	private int    discount_amount;             // ���αݾ�
	private String pos_cond_text2;              // POS���� ��å ���� ����2
	private String gift_able_yn;                // �������ɿ���{Y-��������, N-�����Ұ�}
	private int    pos_cond_cnt;                // N+1���� ���Ǵ�� ����{������ ���� 2~10}
	private String event_use_yn;                // ���� �̺�Ʈ ��뿩�� {Y : ���, N : �̻��}
	
	// MSR ��å
	private int    gift_send_max_cnt;           // ���� ���� �ִ� ȸ ��
	private int    gift_discard_max_cnt;        // ���� ������� �ִ� ȸ ��
	private int    gift_mms_retry_max_cnt;      // ���� MMS ������ �ִ� ȸ ��
	
	// ������ ����
	private String sck_user_number;				// ī����ȸ�� ���̺� ��ϵ� ������ ��ȣ			   


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("  coupon_policy_code="     ).append(this.coupon_policy_code);
		sb.append(", reg_date="               ).append(this.reg_date);
		sb.append(", coupon_code="            ).append(this.coupon_code);
		sb.append(", coupon_name="            ).append(this.coupon_name);
		sb.append(", coupon_start_date="      ).append(this.coupon_start_date);
		sb.append(", coupon_end_date="        ).append(this.coupon_end_date);
		sb.append(", coupon_desc="            ).append(this.coupon_desc);
		sb.append(", coupon_grade="           ).append(this.coupon_grade);
		sb.append(", pos_cond_text="          ).append(this.pos_cond_text);
		sb.append(", pos_target_text="        ).append(this.pos_target_text);
		sb.append(", coupon_type="            ).append(this.coupon_type);
		sb.append(", coupon_publication_seq=" ).append(this.coupon_publication_seq);
		sb.append(", coupon_publication_date=").append(this.coupon_publication_date);
		sb.append(", coupon_group="           ).append(this.coupon_group);
		sb.append(", publication_gap_date="   ).append(this.publication_gap_date);
		sb.append(", publication_status="     ).append(this.publication_status);
		sb.append(", valid_day="              ).append(this.valid_day);
		sb.append(", user_coupon_name="       ).append(this.user_coupon_name);
		sb.append(", use_product="            ).append(this.use_product);
		sb.append(", use_notice="             ).append(this.use_notice);
		sb.append(", email_img="              ).append(this.email_img);
		sb.append(", pos_target_text2="       ).append(this.pos_target_text2);
		sb.append(", gift_reg_coupon_title="  ).append(this.gift_reg_coupon_title);
		sb.append(", discount_amount="        ).append(this.discount_amount);
		sb.append(", pos_cond_text2="         ).append(this.pos_cond_text2);
		sb.append(", gift_able_yn="           ).append(this.gift_able_yn);
		sb.append(", pos_cond_cnt="           ).append(this.pos_cond_cnt);
		sb.append(", event_use_yn="           ).append(this.event_use_yn);
		sb.append(", gift_send_max_cnt="      ).append(this.gift_send_max_cnt);
		sb.append(", gift_discard_max_cnt="   ).append(this.gift_discard_max_cnt);
		sb.append(", gift_mms_retry_max_cnt=" ).append(this.gift_mms_retry_max_cnt);
		sb.append(", sck_user_number="        ).append(this.sck_user_number);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * ������å�ڵ�
	 * @return coupon_policy_code
	 */
	public String getCoupon_policy_code() {
		return coupon_policy_code;
	}

	/**
	 * ������å�ڵ�
	 * @param coupon_policy_code
	 */
	public void setCoupon_policy_code(String coupon_policy_code) {
		this.coupon_policy_code = coupon_policy_code;
	}
	
	/**
	 * �����
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * �����
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15, 006-����, 007-CoffeeFood, 008-WholeCake, 009-�ڵ�����, 010-e��������, 011-������MMS, 012-���������, 013-1�� ���� ���� ����, 014-�׸��������� ����, 015-��巹������ ����, 016-���� ����, 017-������ ����}
	 * @return coupon_code
	 */
	public String getCoupon_code() {
		return coupon_code;
	}

	/**
	 * �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15, 006-����, 007-CoffeeFood, 008-WholeCake, 009-�ڵ�����, 010-e��������, 011-������MMS, 012-���������, 013-1�� ���� ���� ����, 014-�׸��������� ����, 015-��巹������ ����, 016-���� ����, 017-������ ����}
	 * @param coupon_code
	 */
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	
	/**
	 * ������
	 * @return coupon_name
	 */
	public String getCoupon_name() {
		return coupon_name;
	}

	/**
	 * ������
	 * @param coupon_name
	 */
	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}
	
	/**
	 * ���� ��ȿ�Ⱓ ������
	 * @return coupon_start_date
	 */
	public String getCoupon_start_date() {
		return coupon_start_date;
	}

	/**
	 * ���� ��ȿ�Ⱓ ������
	 * @param coupon_start_date
	 */
	public void setCoupon_start_date(String coupon_start_date) {
		this.coupon_start_date = coupon_start_date;
	}
	
	/**
	 * ���� ��ȿ�Ⱓ ������
	 * @return coupon_end_date
	 */
	public String getCoupon_end_date() {
		return coupon_end_date;
	}

	/**
	 * ���� ��ȿ�Ⱓ ������
	 * @param coupon_end_date
	 */
	public void setCoupon_end_date(String coupon_end_date) {
		this.coupon_end_date = coupon_end_date;
	}
	
	/**
	 * ��������
	 * @return coupon_desc
	 */
	public String getCoupon_desc() {
		return coupon_desc;
	}

	/**
	 * ��������
	 * @param coupon_desc
	 */
	public void setCoupon_desc(String coupon_desc) {
		this.coupon_desc = coupon_desc;
	}
	
	/**
	 * ���� ���� ���{00-WEBCOM�̻�, 10-GREEN�̻�, 20-GOLD�̻�, 99-�������}
	 * @return coupon_grade
	 */
	public String getCoupon_grade() {
		return coupon_grade;
	}

	/**
	 * ���� ���� ���{00-WEBCOM�̻�, 10-GREEN�̻�, 20-GOLD�̻�, 99-�������}
	 * @param coupon_grade
	 */
	public void setCoupon_grade(String coupon_grade) {
		this.coupon_grade = coupon_grade;
	}
	
	/**
	 * POS ���� ��å ���� ����
	 * @return pos_cond_text
	 */
	public String getPos_cond_text() {
		return pos_cond_text;
	}

	/**
	 * POS ���� ��å ���� ����
	 * @param pos_cond_text
	 */
	public void setPos_cond_text(String pos_cond_text) {
		this.pos_cond_text = pos_cond_text;
	}
	
	/**
	 * POS ���� ��å ���� ����
	 * @return pos_target_text
	 */
	public String getPos_target_text() {
		return pos_target_text;
	}

	/**
	 * POS ���� ��å ���� ����
	 * @param pos_target_text
	 */
	public void setPos_target_text(String pos_target_text) {
		this.pos_target_text = pos_target_text;
	}
	
	/**
	 * ��������{1-�Ϲ�, 2-1+1, 3-2+1, 4-Ŀ��Ǫ��, 5-N+1, 6-�ݾ�, 7-����}
	 * @return coupon_type
	 */
	public String getCoupon_type() {
		return coupon_type;
	}

	/**
	 * ��������{1-�Ϲ�, 2-1+1, 3-2+1, 4-Ŀ��Ǫ��, 5-N+1, 6-�ݾ�, 7-����}
	 * @param coupon_type
	 */
	public void setCoupon_type(String coupon_type) {
		this.coupon_type = coupon_type;
	}
	
	/**
	 * ���� ���� ��ȣ
	 * @return coupon_publication_seq
	 */
	public int getCoupon_publication_seq() {
		return coupon_publication_seq;
	}

	/**
	 * ���� ���� ��ȣ
	 * @param coupon_publication_seq
	 */
	public void setCoupon_publication_seq(int coupon_publication_seq) {
		this.coupon_publication_seq = coupon_publication_seq;
	}
	
	/**
	 * �������� ������
	 * @return coupon_publication_date
	 */
	public String getCoupon_publication_date() {
		return coupon_publication_date;
	}

	/**
	 * �������� ������
	 * @param coupon_publication_date
	 */
	public void setCoupon_publication_date(String coupon_publication_date) {
		this.coupon_publication_date = coupon_publication_date;
	}
	
	/**
	 * ��������{11-��������, 12-��������, 13-FS, 14-1+1����, 15-PO����, 16-PO����, 17-CoffeeFood����, 18-WholeCake����, 19-�ڵ���������, 20-e-������������, 21-������MMS����, 22-�������������, 23-1�� ���� ���� ����, 24-N+1 ����, 26-�ݾ� ����, 27-toGreen ����, 28-toGold ����, 29-���� ����, 30-������ ����}
	 * @return coupon_group
	 */
	public String getCoupon_group() {
		return coupon_group;
	}

	/**
	 * ��������{11-��������, 12-��������, 13-FS, 14-1+1����, 15-PO����, 16-PO����, 17-CoffeeFood����, 18-WholeCake����, 19-�ڵ���������, 20-e-������������, 21-������MMS����, 22-�������������, 23-1�� ���� ���� ����, 24-N+1 ����, 26-�ݾ� ����, 27-toGreen ����, 28-toGold ����, 29-���� ����, 30-������ ����}
	 * @param coupon_group
	 */
	public void setCoupon_group(String coupon_group) {
		this.coupon_group = coupon_group;
	}
	
	/**
	 * ���������� ���������� ������ ������ sysdate + ����
	 * @return publication_gap_date
	 */
	public int getPublication_gap_date() {
		return publication_gap_date;
	}

	/**
	 * ���������� ���������� ������ ������ sysdate + ����
	 * @param publication_gap_date
	 */
	public void setPublication_gap_date(int publication_gap_date) {
		this.publication_gap_date = publication_gap_date;
	}
	
	/**
	 * �������{N-�̹���, Y-����}
	 * @return publication_status
	 */
	public String getPublication_status() {
		return publication_status;
	}

	/**
	 * �������{N-�̹���, Y-����}
	 * @param publication_status
	 */
	public void setPublication_status(String publication_status) {
		this.publication_status = publication_status;
	}
	
	/**
	 * ��ȿ�ϼ�(1. ��������:���Ϸ� ���� ��ȿ �� �� 2. �������� : ���������� �������� sysdate - ����)
	 * @return valid_day
	 */
	public int getValid_day() {
		return valid_day;
	}

	/**
	 * ��ȿ�ϼ�(1. ��������:���Ϸ� ���� ��ȿ �� �� 2. �������� : ���������� �������� sysdate - ����)
	 * @param valid_day
	 */
	public void setValid_day(int valid_day) {
		this.valid_day = valid_day;
	}
	
	/**
	 * ����� ������
	 * @return user_coupon_name
	 */
	public String getUser_coupon_name() {
		return user_coupon_name;
	}

	/**
	 * ����� ������
	 * @param user_coupon_name
	 */
	public void setUser_coupon_name(String user_coupon_name) {
		this.user_coupon_name = user_coupon_name;
	}
	
	/**
	 * �̿� ���� ��ǰ
	 * @return use_product
	 */
	public String getUse_product() {
		return use_product;
	}

	/**
	 * �̿� ���� ��ǰ
	 * @param use_product
	 */
	public void setUse_product(String use_product) {
		this.use_product = use_product;
	}
	
	/**
	 * ���� ���� ����
	 * @return use_notice
	 */
	public String getUse_notice() {
		return use_notice;
	}

	/**
	 * ���� ���� ����
	 * @param use_notice
	 */
	public void setUse_notice(String use_notice) {
		this.use_notice = use_notice;
	}
	
	/**
	 * �� ���� �̹���
	 * @return email_img
	 */
	public String getEmail_img() {
		return email_img;
	}

	/**
	 * �� ���� �̹���
	 * @param email_img
	 */
	public void setEmail_img(String email_img) {
		this.email_img = email_img;
	}
	
	/**
	 * POS���� ��å ���� ����2
	 * @return pos_target_text2
	 */
	public String getPos_target_text2() {
		return pos_target_text2;
	}

	/**
	 * POS���� ��å ���� ����2
	 * @param pos_target_text2
	 */
	public void setPos_target_text2(String pos_target_text2) {
		this.pos_target_text2 = pos_target_text2;
	}
	
	/**
	 * �������������
	 * @return gift_reg_coupon_title
	 */
	public String getGift_reg_coupon_title() {
		return gift_reg_coupon_title;
	}

	/**
	 * �������������
	 * @param gift_reg_coupon_title
	 */
	public void setGift_reg_coupon_title(String gift_reg_coupon_title) {
		this.gift_reg_coupon_title = gift_reg_coupon_title;
	}
	
	/**
	 * ���αݾ�
	 * @return discount_amount
	 */
	public int getDiscount_amount() {
		return discount_amount;
	}

	/**
	 * ���αݾ�
	 * @param discount_amount
	 */
	public void setDiscount_amount(int discount_amount) {
		this.discount_amount = discount_amount;
	}
	
	/**
	 * POS���� ��å ���� ����2
	 * @return pos_cond_text2
	 */
	public String getPos_cond_text2() {
		return pos_cond_text2;
	}

	/**
	 * POS���� ��å ���� ����2
	 * @param pos_cond_text2
	 */
	public void setPos_cond_text2(String pos_cond_text2) {
		this.pos_cond_text2 = pos_cond_text2;
	}
	
	/**
	 * �������ɿ���{Y-��������, N-�����Ұ�}
	 * @return gift_able_yn
	 */
	public String getGift_able_yn() {
		return gift_able_yn;
	}

	/**
	 * �������ɿ���{Y-��������, N-�����Ұ�}
	 * @param gift_able_yn
	 */
	public void setGift_able_yn(String gift_able_yn) {
		this.gift_able_yn = gift_able_yn;
	}
	
	/**
	 * N+1���� ���Ǵ�� ����{������ ���� 2~10}
	 * @return pos_cond_cnt
	 */
	public int getPos_cond_cnt() {
		return pos_cond_cnt;
	}

	/**
	 * N+1���� ���Ǵ�� ����{������ ���� 2~10}
	 * @param pos_cond_cnt
	 */
	public void setPos_cond_cnt(int pos_cond_cnt) {
		this.pos_cond_cnt = pos_cond_cnt;
	}
	
	/**
	 * ���� �̺�Ʈ ��뿩�� {Y : ���, N : �̻��}
	 * @return event_use_yn
	 */
	public String getEvent_use_yn() {
		return event_use_yn;
	}

	/**
	 * ���� �̺�Ʈ ��뿩�� {Y : ���, N : �̻��}
	 * @param event_use_yn
	 */
	public void setEvent_use_yn(String event_use_yn) {
		this.event_use_yn = event_use_yn;
	}
	
	/**
	 * ���� ���� �ִ� ȸ ��
	 * @return gift_send_max_cnt
	 */
	public int getGift_send_max_cnt() {
		return gift_send_max_cnt;
	}

	/**
	 * ���� ���� �ִ� ȸ ��
	 * @param gift_send_max_cnt
	 */
	public void setGift_send_max_cnt(int gift_send_max_cnt) {
		this.gift_send_max_cnt = gift_send_max_cnt;
	}
	
	/**
	 * ���� ������� �ִ� ȸ ��
	 * @return gift_discard_max_cnt
	 */
	public int getGift_discard_max_cnt() {
		return gift_discard_max_cnt;
	}

	/**
	 * ���� ������� �ִ� ȸ ��
	 * @param gift_discard_max_cnt
	 */
	public void setGift_discard_max_cnt(int gift_discard_max_cnt) {
		this.gift_discard_max_cnt = gift_discard_max_cnt;
	}
	
	/**
	 * ���� MMS ������ �ִ� ȸ ��
	 * @return gift_mms_retry_max_cnt
	 */
	public int getGift_mms_retry_max_cnt() {
		return gift_mms_retry_max_cnt;
	}

	/**
	 * ���� MMS ������ �ִ� ȸ ��
	 * @param gift_mms_retry_max_cnt
	 */
	public void setGift_mms_retry_max_cnt(int gift_mms_retry_max_cnt) {
		this.gift_mms_retry_max_cnt = gift_mms_retry_max_cnt;
	}
	
	
	/**
	 * ī����ȸ�� ���̺� ��ϵ� ������ ��ȣ	
	 * @return sck_user_number
	 */
	public String getSck_user_number() {
		return sck_user_number;
	}

	/**
	 * ī����ȸ�� ���̺� ��ϵ� ������ ��ȣ	
	 * @param sck_user_number
	 */
	public void setSck_user_number(String sck_user_number) {
		this.sck_user_number = sck_user_number;
	}
	
	

	
}
