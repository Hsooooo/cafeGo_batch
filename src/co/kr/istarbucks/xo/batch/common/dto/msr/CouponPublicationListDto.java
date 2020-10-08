/*
 * @(#) $Id: CouponPublicationListDto.java,v 1.1 2016/11/10 00:55:26 dev99 Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

/**
 * CouponPublicationListDto - ���� ���� ����
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class CouponPublicationListDto {

	private String coupon_number;            // �����Ϸù�ȣ
	private String user_number;              // ����� ������ȣ
	private String coupon_policy_code;       // ������å�ڵ�
	private String coupon_code;              // �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15}
	private String coupon_status;            // ��������{A-����,U-���, C-ȸ��}
	private Date   reg_date;                 // ���� ��������(���)
	private String grant_admin_id;           // ���� ���� ������ ���̵�
	private String grant_desc;               // �������� ����
	private String birthday;                 // ���� ����� ����
	private Date   use_date;                 // �����������
	private Date   cancel_date;              // ����ȸ������
	private Date   expire_start_date;        // ��ȿ�Ⱓ ��������
	private Date   expire_end_date;          // ��ȿ�Ⱓ ��������
	private long   coupon_publication_seq;   // ���� ���� ��ȣ
	private Date   reg_date_l;               // ���� ���� ����(����)
	private String birth_coupon_year;        // ���� ���� ����⵵
	private int    gift_send_cnt;            // ����ȸ��
	private int    gift_mms_retry_cnt;       // ����������ȸ��
	private String gift_recv_user_number;    // ���� ����� ������ȣ
	private String gift_send_coupon_num;     // �����������۹�ȣ
	private int    gift_discard_cnt;         // ������� ȸ��
	private String gift_status;              // ������������{0-��������, 1-��������, 2-�������, 9-�����Ұ�}
	private long   gift_hist_no;             // �����̷��Ϸù�ȣ
	private int    promotion_seq;            // ���θ���Ϸù�ȣ
	private String gift_sender_name;         // �������� ����� ��Ī
	private String anniversary_coupon_year;  // ����� ���� ����⵵
	private String ipin_dup_key;             // ������ Ű
	private String coupon_option_seq;        // �������� ����{���� 0���� ä��}
	private int    coupon_used_amount;       // ���� �ǻ��ݾ�
	private String event_no;                 // ����ȣ
	private String coupon_send_type;         // ��������Ÿ��{D-���, R-��������, NULL-DEFAULT}
	 
	private String str_reg_date;             // ���� ���� ����(���ڿ�) - ���/������ ���� �ʿ�
	private int    valid_day;                // ��ȿ�ϼ� - ����������

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("coupon_number="            ).append(this.coupon_number);
		sb.append(", user_number="            ).append(this.user_number);
		sb.append(", coupon_policy_code="     ).append(this.coupon_policy_code);
		sb.append(", coupon_code="            ).append(this.coupon_code);
		sb.append(", coupon_status="          ).append(this.coupon_status);
		sb.append(", reg_date="               ).append(this.reg_date);
		sb.append(", grant_admin_id="         ).append(this.grant_admin_id);
		sb.append(", grant_desc="             ).append(this.grant_desc);
		sb.append(", birthday="               ).append(this.birthday);
		sb.append(", use_date="               ).append(this.use_date);
		sb.append(", cancel_date="            ).append(this.cancel_date);
		sb.append(", expire_start_date="      ).append(this.expire_start_date);
		sb.append(", expire_end_date="        ).append(this.expire_end_date);
		sb.append(", coupon_publication_seq=" ).append(this.coupon_publication_seq);
		sb.append(", reg_date_l="             ).append(this.reg_date_l);
		sb.append(", birth_coupon_year="      ).append(this.birth_coupon_year);
		sb.append(", gift_send_cnt="          ).append(this.gift_send_cnt);
		sb.append(", gift_mms_retry_cnt="     ).append(this.gift_mms_retry_cnt);
		sb.append(", gift_recv_user_number="  ).append(this.gift_recv_user_number);
		sb.append(", gift_send_coupon_num="   ).append(this.gift_send_coupon_num);
		sb.append(", gift_discard_cnt="       ).append(this.gift_discard_cnt);
		sb.append(", gift_status="            ).append(this.gift_status);
		sb.append(", gift_hist_no="           ).append(this.gift_hist_no);
		sb.append(", promotion_seq="          ).append(this.promotion_seq);
		sb.append(", gift_sender_name="       ).append(this.gift_sender_name);
		sb.append(", anniversary_coupon_year=").append(this.anniversary_coupon_year);
		sb.append(", ipin_dup_key="           ).append(this.ipin_dup_key);
		sb.append(", coupon_option_seq="      ).append(this.coupon_option_seq);
		sb.append(", coupon_used_amount="     ).append(this.coupon_used_amount);
		sb.append(", event_no="               ).append(this.event_no);
		sb.append(", coupon_send_type="       ).append(this.coupon_send_type);
		
		sb.append(", str_reg_date="           ).append(this.str_reg_date);
		sb.append(", valid_day="              ).append(this.valid_day);
		
		sb.append("]");
		return sb.toString();
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
	 * ����� ������ȣ
	 * @return user_number
	 */
	public String getUser_number() {
		return user_number;
	}

	/**
	 * ����� ������ȣ
	 * @param user_number
	 */
	public void setUser_number(String user_number) {
		this.user_number = user_number;
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
	 * �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15}
	 * @return coupon_code
	 */
	public String getCoupon_code() {
		return coupon_code;
	}

	/**
	 * �����ڵ� {001-����, 002-1+1, 003-����, 004-PO, 005-��15}
	 * @param coupon_code
	 */
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	
	/**
	 * ��������{A-����,U-���, C-ȸ��}
	 * @return coupon_status
	 */
	public String getCoupon_status() {
		return coupon_status;
	}

	/**
	 * ��������{A-����,U-���, C-ȸ��}
	 * @param coupon_status
	 */
	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	
	/**
	 * ���� ��������(���)
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * ���� ��������(���)
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * ���� ���� ������ ���̵�
	 * @return grant_admin_id
	 */
	public String getGrant_admin_id() {
		return grant_admin_id;
	}

	/**
	 * ���� ���� ������ ���̵�
	 * @param grant_admin_id
	 */
	public void setGrant_admin_id(String grant_admin_id) {
		this.grant_admin_id = grant_admin_id;
	}
	
	/**
	 * �������� ����
	 * @return grant_desc
	 */
	public String getGrant_desc() {
		return grant_desc;
	}

	/**
	 * �������� ����
	 * @param grant_desc
	 */
	public void setGrant_desc(String grant_desc) {
		this.grant_desc = grant_desc;
	}
	
	/**
	 * ���� ����� ����
	 * @return birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * ���� ����� ����
	 * @param birthday
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * �����������
	 * @return use_date
	 */
	public Date getUse_date() {
		return use_date;
	}

	/**
	 * �����������
	 * @param use_date
	 */
	public void setUse_date(Date use_date) {
		this.use_date = use_date;
	}
	
	/**
	 * ����ȸ������
	 * @return cancel_date
	 */
	public Date getCancel_date() {
		return cancel_date;
	}

	/**
	 * ����ȸ������
	 * @param cancel_date
	 */
	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * ��ȿ�Ⱓ ��������
	 * @return expire_start_date
	 */
	public Date getExpire_start_date() {
		return expire_start_date;
	}

	/**
	 * ��ȿ�Ⱓ ��������
	 * @param expire_start_date
	 */
	public void setExpire_start_date(Date expire_start_date) {
		this.expire_start_date = expire_start_date;
	}
	
	/**
	 * ��ȿ�Ⱓ ��������
	 * @return expire_end_date
	 */
	public Date getExpire_end_date() {
		return expire_end_date;
	}

	/**
	 * ��ȿ�Ⱓ ��������
	 * @param expire_end_date
	 */
	public void setExpire_end_date(Date expire_end_date) {
		this.expire_end_date = expire_end_date;
	}
	
	/**
	 * ���� ���� ��ȣ
	 * @return coupon_publication_seq
	 */
	public long getCoupon_publication_seq() {
		return coupon_publication_seq;
	}

	/**
	 * ���� ���� ��ȣ
	 * @param coupon_publication_seq
	 */
	public void setCoupon_publication_seq(long coupon_publication_seq) {
		this.coupon_publication_seq = coupon_publication_seq;
	}
	
	/**
	 * ���� ���� ����(����)
	 * @return reg_date_l
	 */
	public Date getReg_date_l() {
		return reg_date_l;
	}

	/**
	 * ���� ���� ����(����)
	 * @param reg_date_l
	 */
	public void setReg_date_l(Date reg_date_l) {
		this.reg_date_l = reg_date_l;
	}
	
	/**
	 * ���� ���� ����⵵
	 * @return birth_coupon_year
	 */
	public String getBirth_coupon_year() {
		return birth_coupon_year;
	}

	/**
	 * ���� ���� ����⵵
	 * @param birth_coupon_year
	 */
	public void setBirth_coupon_year(String birth_coupon_year) {
		this.birth_coupon_year = birth_coupon_year;
	}
	
	/**
	 * ����ȸ��
	 * @return gift_send_cnt
	 */
	public int getGift_send_cnt() {
		return gift_send_cnt;
	}

	/**
	 * ����ȸ��
	 * @param gift_send_cnt
	 */
	public void setGift_send_cnt(int gift_send_cnt) {
		this.gift_send_cnt = gift_send_cnt;
	}
	
	/**
	 * ����������ȸ��
	 * @return gift_mms_retry_cnt
	 */
	public int getGift_mms_retry_cnt() {
		return gift_mms_retry_cnt;
	}

	/**
	 * ����������ȸ��
	 * @param gift_mms_retry_cnt
	 */
	public void setGift_mms_retry_cnt(int gift_mms_retry_cnt) {
		this.gift_mms_retry_cnt = gift_mms_retry_cnt;
	}
	
	/**
	 * ���� ����� ������ȣ
	 * @return gift_recv_user_number
	 */
	public String getGift_recv_user_number() {
		return gift_recv_user_number;
	}

	/**
	 * ���� ����� ������ȣ
	 * @param gift_recv_user_number
	 */
	public void setGift_recv_user_number(String gift_recv_user_number) {
		this.gift_recv_user_number = gift_recv_user_number;
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
	 * ������� ȸ��
	 * @return gift_discard_cnt
	 */
	public int getGift_discard_cnt() {
		return gift_discard_cnt;
	}

	/**
	 * ������� ȸ��
	 * @param gift_discard_cnt
	 */
	public void setGift_discard_cnt(int gift_discard_cnt) {
		this.gift_discard_cnt = gift_discard_cnt;
	}
	
	/**
	 * ������������{0-��������, 1-��������, 2-�������, 9-�����Ұ�}
	 * @return gift_status
	 */
	public String getGift_status() {
		return gift_status;
	}

	/**
	 * ������������{0-��������, 1-��������, 2-�������, 9-�����Ұ�}
	 * @param gift_status
	 */
	public void setGift_status(String gift_status) {
		this.gift_status = gift_status;
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
	 * ���θ���Ϸù�ȣ
	 * @return promotion_seq
	 */
	public int getPromotion_seq() {
		return promotion_seq;
	}

	/**
	 * ���θ���Ϸù�ȣ
	 * @param promotion_seq
	 */
	public void setPromotion_seq(int promotion_seq) {
		this.promotion_seq = promotion_seq;
	}
	
	/**
	 * �������� ����� ��Ī
	 * @return gift_sender_name
	 */
	public String getGift_sender_name() {
		return gift_sender_name;
	}

	/**
	 * �������� ����� ��Ī
	 * @param gift_sender_name
	 */
	public void setGift_sender_name(String gift_sender_name) {
		this.gift_sender_name = gift_sender_name;
	}
	
	/**
	 * ����� ���� ����⵵
	 * @return anniversary_coupon_year
	 */
	public String getAnniversary_coupon_year() {
		return anniversary_coupon_year;
	}

	/**
	 * ����� ���� ����⵵
	 * @param anniversary_coupon_year
	 */
	public void setAnniversary_coupon_year(String anniversary_coupon_year) {
		this.anniversary_coupon_year = anniversary_coupon_year;
	}
	
	/**
	 * ������ Ű
	 * @return ipin_dup_key
	 */
	public String getIpin_dup_key() {
		return ipin_dup_key;
	}

	/**
	 * ������ Ű
	 * @param ipin_dup_key
	 */
	public void setIpin_dup_key(String ipin_dup_key) {
		this.ipin_dup_key = ipin_dup_key;
	}
	
	/**
	 * �������� ����{���� 0���� ä��}
	 * @return coupon_option_seq
	 */
	public String getCoupon_option_seq() {
		return coupon_option_seq;
	}

	/**
	 * �������� ����{���� 0���� ä��}
	 * @param coupon_option_seq
	 */
	public void setCoupon_option_seq(String coupon_option_seq) {
		this.coupon_option_seq = coupon_option_seq;
	}
	
	/**
	 * ���� �ǻ��ݾ�
	 * @return coupon_used_amount
	 */
	public int getCoupon_used_amount() {
		return coupon_used_amount;
	}

	/**
	 * ���� �ǻ��ݾ�
	 * @param coupon_used_amount
	 */
	public void setCoupon_used_amount(int coupon_used_amount) {
		this.coupon_used_amount = coupon_used_amount;
	}
	
	/**
	 * ����ȣ
	 * @return event_no
	 */
	public String getEvent_no() {
		return event_no;
	}

	/**
	 * ����ȣ
	 * @param event_no
	 */
	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}
	
	/**
	 * ��������Ÿ��{D-���, R-��������, NULL-DEFAULT}
	 * @return coupon_send_type
	 */
	public String getCoupon_send_type() {
		return coupon_send_type;
	}

	/**
	 * ��������Ÿ��{D-���, R-��������, NULL-DEFAULT}
	 * @param coupon_send_type
	 */
	public void setCoupon_send_type(String coupon_send_type) {
		this.coupon_send_type = coupon_send_type;
	}
	
	/**
	 * ���� ���� ���� (���ڿ�)
	 * @return str_reg_date
	 */
	public String getStr_reg_date() {
		return str_reg_date;
	}

	/**
	 * ���� ���� ���� (���ڿ�)
	 * @param str_reg_date
	 */
	public void setStr_reg_date(String str_reg_date) {
		this.str_reg_date = str_reg_date;
	}
	
	/**
	 * ��ȿ�ϼ� - ���� ������
	 * @return valid_day
	 */
	public int getValid_day() {
		return valid_day;
	}

	/**
	 * ��ȿ�ϼ� - ���� ������
	 * @param valid_day
	 */
	public void setValid_day(int valid_day) {
		this.valid_day = valid_day;
	}	
}
