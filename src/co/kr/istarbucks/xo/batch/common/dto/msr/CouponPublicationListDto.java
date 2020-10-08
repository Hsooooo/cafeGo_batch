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
 * CouponPublicationListDto - 쿠폰 발행 내역
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class CouponPublicationListDto {

	private String coupon_number;            // 쿠폰일련번호
	private String user_number;              // 사용자 고유번호
	private String coupon_policy_code;       // 쿠폰정책코드
	private String coupon_code;              // 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15}
	private String coupon_status;            // 쿠폰상태{A-발행,U-사용, C-회수}
	private Date   reg_date;                 // 쿠폰 발행일자(양력)
	private String grant_admin_id;           // 쿠폰 발행 관리자 아이디
	private String grant_desc;               // 쿠폰발행 사유
	private String birthday;                 // 쿠폰 대상자 생일
	private Date   use_date;                 // 쿠폰사용일자
	private Date   cancel_date;              // 쿠폰회수일자
	private Date   expire_start_date;        // 유효기간 시작일자
	private Date   expire_end_date;          // 유효기간 종료일자
	private long   coupon_publication_seq;   // 쿠폰 발행 번호
	private Date   reg_date_l;               // 쿠폰 발행 일자(음력)
	private String birth_coupon_year;        // 생일 쿠폰 발행년도
	private int    gift_send_cnt;            // 선물회수
	private int    gift_mms_retry_cnt;       // 선물재전송회수
	private String gift_recv_user_number;    // 받은 사용자 고유번호
	private String gift_send_coupon_num;     // 쿠폰선물전송번호
	private int    gift_discard_cnt;         // 선물취소 회수
	private String gift_status;              // 쿠폰선물상태{0-선물가능, 1-쿠폰전송, 2-쿠폰등록, 9-선물불가}
	private long   gift_hist_no;             // 선물이력일련번호
	private int    promotion_seq;            // 프로모션일련번호
	private String gift_sender_name;         // 선물보낸 사용자 별칭
	private String anniversary_coupon_year;  // 기념일 쿠폰 발행년도
	private String ipin_dup_key;             // 아이핀 키
	private String coupon_option_seq;        // 선택쿠폰 순번{좌측 0으로 채움}
	private int    coupon_used_amount;       // 쿠폰 실사용금액
	private String event_no;                 // 행사번호
	private String coupon_send_type;         // 쿠폰전송타입{D-기부, R-랜덤선물, NULL-DEFAULT}
	 
	private String str_reg_date;             // 쿠폰 발행 일자(문자열) - 등록/수정을 위해 필요
	private int    valid_day;                // 유효일수 - 쿠폰마스터

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
	 * 사용자 고유번호
	 * @return user_number
	 */
	public String getUser_number() {
		return user_number;
	}

	/**
	 * 사용자 고유번호
	 * @param user_number
	 */
	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}
	
	/**
	 * 쿠폰정책코드
	 * @return coupon_policy_code
	 */
	public String getCoupon_policy_code() {
		return coupon_policy_code;
	}

	/**
	 * 쿠폰정책코드
	 * @param coupon_policy_code
	 */
	public void setCoupon_policy_code(String coupon_policy_code) {
		this.coupon_policy_code = coupon_policy_code;
	}
	
	/**
	 * 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15}
	 * @return coupon_code
	 */
	public String getCoupon_code() {
		return coupon_code;
	}

	/**
	 * 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15}
	 * @param coupon_code
	 */
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	
	/**
	 * 쿠폰상태{A-발행,U-사용, C-회수}
	 * @return coupon_status
	 */
	public String getCoupon_status() {
		return coupon_status;
	}

	/**
	 * 쿠폰상태{A-발행,U-사용, C-회수}
	 * @param coupon_status
	 */
	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	
	/**
	 * 쿠폰 발행일자(양력)
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 쿠폰 발행일자(양력)
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 쿠폰 발행 관리자 아이디
	 * @return grant_admin_id
	 */
	public String getGrant_admin_id() {
		return grant_admin_id;
	}

	/**
	 * 쿠폰 발행 관리자 아이디
	 * @param grant_admin_id
	 */
	public void setGrant_admin_id(String grant_admin_id) {
		this.grant_admin_id = grant_admin_id;
	}
	
	/**
	 * 쿠폰발행 사유
	 * @return grant_desc
	 */
	public String getGrant_desc() {
		return grant_desc;
	}

	/**
	 * 쿠폰발행 사유
	 * @param grant_desc
	 */
	public void setGrant_desc(String grant_desc) {
		this.grant_desc = grant_desc;
	}
	
	/**
	 * 쿠폰 대상자 생일
	 * @return birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * 쿠폰 대상자 생일
	 * @param birthday
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * 쿠폰사용일자
	 * @return use_date
	 */
	public Date getUse_date() {
		return use_date;
	}

	/**
	 * 쿠폰사용일자
	 * @param use_date
	 */
	public void setUse_date(Date use_date) {
		this.use_date = use_date;
	}
	
	/**
	 * 쿠폰회수일자
	 * @return cancel_date
	 */
	public Date getCancel_date() {
		return cancel_date;
	}

	/**
	 * 쿠폰회수일자
	 * @param cancel_date
	 */
	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * 유효기간 시작일자
	 * @return expire_start_date
	 */
	public Date getExpire_start_date() {
		return expire_start_date;
	}

	/**
	 * 유효기간 시작일자
	 * @param expire_start_date
	 */
	public void setExpire_start_date(Date expire_start_date) {
		this.expire_start_date = expire_start_date;
	}
	
	/**
	 * 유효기간 종료일자
	 * @return expire_end_date
	 */
	public Date getExpire_end_date() {
		return expire_end_date;
	}

	/**
	 * 유효기간 종료일자
	 * @param expire_end_date
	 */
	public void setExpire_end_date(Date expire_end_date) {
		this.expire_end_date = expire_end_date;
	}
	
	/**
	 * 쿠폰 발행 번호
	 * @return coupon_publication_seq
	 */
	public long getCoupon_publication_seq() {
		return coupon_publication_seq;
	}

	/**
	 * 쿠폰 발행 번호
	 * @param coupon_publication_seq
	 */
	public void setCoupon_publication_seq(long coupon_publication_seq) {
		this.coupon_publication_seq = coupon_publication_seq;
	}
	
	/**
	 * 쿠폰 발행 일자(음력)
	 * @return reg_date_l
	 */
	public Date getReg_date_l() {
		return reg_date_l;
	}

	/**
	 * 쿠폰 발행 일자(음력)
	 * @param reg_date_l
	 */
	public void setReg_date_l(Date reg_date_l) {
		this.reg_date_l = reg_date_l;
	}
	
	/**
	 * 생일 쿠폰 발행년도
	 * @return birth_coupon_year
	 */
	public String getBirth_coupon_year() {
		return birth_coupon_year;
	}

	/**
	 * 생일 쿠폰 발행년도
	 * @param birth_coupon_year
	 */
	public void setBirth_coupon_year(String birth_coupon_year) {
		this.birth_coupon_year = birth_coupon_year;
	}
	
	/**
	 * 선물회수
	 * @return gift_send_cnt
	 */
	public int getGift_send_cnt() {
		return gift_send_cnt;
	}

	/**
	 * 선물회수
	 * @param gift_send_cnt
	 */
	public void setGift_send_cnt(int gift_send_cnt) {
		this.gift_send_cnt = gift_send_cnt;
	}
	
	/**
	 * 선물재전송회수
	 * @return gift_mms_retry_cnt
	 */
	public int getGift_mms_retry_cnt() {
		return gift_mms_retry_cnt;
	}

	/**
	 * 선물재전송회수
	 * @param gift_mms_retry_cnt
	 */
	public void setGift_mms_retry_cnt(int gift_mms_retry_cnt) {
		this.gift_mms_retry_cnt = gift_mms_retry_cnt;
	}
	
	/**
	 * 받은 사용자 고유번호
	 * @return gift_recv_user_number
	 */
	public String getGift_recv_user_number() {
		return gift_recv_user_number;
	}

	/**
	 * 받은 사용자 고유번호
	 * @param gift_recv_user_number
	 */
	public void setGift_recv_user_number(String gift_recv_user_number) {
		this.gift_recv_user_number = gift_recv_user_number;
	}
	
	/**
	 * 쿠폰선물전송번호
	 * @return gift_send_coupon_num
	 */
	public String getGift_send_coupon_num() {
		return gift_send_coupon_num;
	}

	/**
	 * 쿠폰선물전송번호
	 * @param gift_send_coupon_num
	 */
	public void setGift_send_coupon_num(String gift_send_coupon_num) {
		this.gift_send_coupon_num = gift_send_coupon_num;
	}
	
	/**
	 * 선물취소 회수
	 * @return gift_discard_cnt
	 */
	public int getGift_discard_cnt() {
		return gift_discard_cnt;
	}

	/**
	 * 선물취소 회수
	 * @param gift_discard_cnt
	 */
	public void setGift_discard_cnt(int gift_discard_cnt) {
		this.gift_discard_cnt = gift_discard_cnt;
	}
	
	/**
	 * 쿠폰선물상태{0-선물가능, 1-쿠폰전송, 2-쿠폰등록, 9-선물불가}
	 * @return gift_status
	 */
	public String getGift_status() {
		return gift_status;
	}

	/**
	 * 쿠폰선물상태{0-선물가능, 1-쿠폰전송, 2-쿠폰등록, 9-선물불가}
	 * @param gift_status
	 */
	public void setGift_status(String gift_status) {
		this.gift_status = gift_status;
	}
	
	/**
	 * 선물이력일련번호
	 * @return gift_hist_no
	 */
	public long getGift_hist_no() {
		return gift_hist_no;
	}

	/**
	 * 선물이력일련번호
	 * @param gift_hist_no
	 */
	public void setGift_hist_no(long gift_hist_no) {
		this.gift_hist_no = gift_hist_no;
	}
	
	/**
	 * 프로모션일련번호
	 * @return promotion_seq
	 */
	public int getPromotion_seq() {
		return promotion_seq;
	}

	/**
	 * 프로모션일련번호
	 * @param promotion_seq
	 */
	public void setPromotion_seq(int promotion_seq) {
		this.promotion_seq = promotion_seq;
	}
	
	/**
	 * 선물보낸 사용자 별칭
	 * @return gift_sender_name
	 */
	public String getGift_sender_name() {
		return gift_sender_name;
	}

	/**
	 * 선물보낸 사용자 별칭
	 * @param gift_sender_name
	 */
	public void setGift_sender_name(String gift_sender_name) {
		this.gift_sender_name = gift_sender_name;
	}
	
	/**
	 * 기념일 쿠폰 발행년도
	 * @return anniversary_coupon_year
	 */
	public String getAnniversary_coupon_year() {
		return anniversary_coupon_year;
	}

	/**
	 * 기념일 쿠폰 발행년도
	 * @param anniversary_coupon_year
	 */
	public void setAnniversary_coupon_year(String anniversary_coupon_year) {
		this.anniversary_coupon_year = anniversary_coupon_year;
	}
	
	/**
	 * 아이핀 키
	 * @return ipin_dup_key
	 */
	public String getIpin_dup_key() {
		return ipin_dup_key;
	}

	/**
	 * 아이핀 키
	 * @param ipin_dup_key
	 */
	public void setIpin_dup_key(String ipin_dup_key) {
		this.ipin_dup_key = ipin_dup_key;
	}
	
	/**
	 * 선택쿠폰 순번{좌측 0으로 채움}
	 * @return coupon_option_seq
	 */
	public String getCoupon_option_seq() {
		return coupon_option_seq;
	}

	/**
	 * 선택쿠폰 순번{좌측 0으로 채움}
	 * @param coupon_option_seq
	 */
	public void setCoupon_option_seq(String coupon_option_seq) {
		this.coupon_option_seq = coupon_option_seq;
	}
	
	/**
	 * 쿠폰 실사용금액
	 * @return coupon_used_amount
	 */
	public int getCoupon_used_amount() {
		return coupon_used_amount;
	}

	/**
	 * 쿠폰 실사용금액
	 * @param coupon_used_amount
	 */
	public void setCoupon_used_amount(int coupon_used_amount) {
		this.coupon_used_amount = coupon_used_amount;
	}
	
	/**
	 * 행사번호
	 * @return event_no
	 */
	public String getEvent_no() {
		return event_no;
	}

	/**
	 * 행사번호
	 * @param event_no
	 */
	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}
	
	/**
	 * 쿠폰전송타입{D-기부, R-랜덤선물, NULL-DEFAULT}
	 * @return coupon_send_type
	 */
	public String getCoupon_send_type() {
		return coupon_send_type;
	}

	/**
	 * 쿠폰전송타입{D-기부, R-랜덤선물, NULL-DEFAULT}
	 * @param coupon_send_type
	 */
	public void setCoupon_send_type(String coupon_send_type) {
		this.coupon_send_type = coupon_send_type;
	}
	
	/**
	 * 쿠폰 발행 일자 (문자열)
	 * @return str_reg_date
	 */
	public String getStr_reg_date() {
		return str_reg_date;
	}

	/**
	 * 쿠폰 발행 일자 (문자열)
	 * @param str_reg_date
	 */
	public void setStr_reg_date(String str_reg_date) {
		this.str_reg_date = str_reg_date;
	}
	
	/**
	 * 유효일수 - 쿠폰 마스터
	 * @return valid_day
	 */
	public int getValid_day() {
		return valid_day;
	}

	/**
	 * 유효일수 - 쿠폰 마스터
	 * @param valid_day
	 */
	public void setValid_day(int valid_day) {
		this.valid_day = valid_day;
	}	
}
