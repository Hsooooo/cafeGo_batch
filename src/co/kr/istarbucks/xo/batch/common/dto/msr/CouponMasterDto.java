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
 * CouponMasterDto - 쿠폰 마스터
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class CouponMasterDto {

	private String coupon_policy_code;          // 쿠폰정책코드
	private Date   reg_date;                    // 등록일
	private String coupon_code;                 // 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15, 006-충전, 007-CoffeeFood, 008-WholeCake, 009-자동충전, 010-e프리퀀시, 011-관리자MMS, 012-리저브원두, 013-1년 무료 음료 쿠폰, 014-그린레벨진입 쿠폰, 015-골드레벨진입 쿠폰, 016-선택 쿠폰, 017-별소진 쿠폰}
	private String coupon_name;                 // 쿠폰명
	private String coupon_start_date;           // 쿠폰 유효기간 시작일
	private String coupon_end_date;             // 쿠폰 유효기간 종료일
	private String coupon_desc;                 // 쿠폰설명
	private String coupon_grade;                // 쿠폰 발행 등급{00-WEBCOM이상, 10-GREEN이상, 20-GOLD이상, 99-대상지정}
	private String pos_cond_text;               // POS 연동 정책 조건 전문
	private String pos_target_text;             // POS 연동 정책 전용 전문
	private String coupon_type;                 // 쿠폰종류{1-일반, 2-1+1, 3-2+1, 4-커피푸드, 5-N+1, 6-금액, 7-선택}
	private int    coupon_publication_seq;      // 쿠폰 발행 번호
	private String coupon_publication_date;     // 쿠폰발행 예정일
	private String coupon_group;                // 쿠폰구분{11-생일쿠폰, 12-원두쿠폰, 13-FS, 14-1+1쿠폰, 15-PO쿠폰, 16-PO쿠폰, 17-CoffeeFood쿠폰, 18-WholeCake쿠폰, 19-자동충전쿠폰, 20-e-프리퀀시쿠폰, 21-관리자MMS쿠폰, 22-리저브원두쿠폰, 23-1년 무료 음료 쿠폰, 24-N+1 쿠폰, 26-금액 쿠폰, 27-toGreen 쿠폰, 28-toGold 쿠폰, 29-선택 쿠폰, 30-별소진 쿠폰}
	private int    publication_gap_date;        // 생일쿠폰의 발행대상자의 범위를 가져옴 sysdate + 일자
	private String publication_status;          // 발행상태{N-미발행, Y-발행}
	private int    valid_day;                   // 유효일수(1. 생일쿠폰:생일로 부터 유효 일 수 2. 생일쿠폰 : 발행대상자의 이전범위 sysdate - 일자)
	private String user_coupon_name;            // 사용자 쿠폰명
	private String use_product;                 // 이용 가능 상품
	private String use_notice;                  // 사용시 주의 사항
	private String email_img;                   // 이 메일 이미지
	private String pos_target_text2;            // POS연동 정책 적용 전문2
	private String gift_reg_coupon_title;       // 선물등록쿠폰명
	private int    discount_amount;             // 할인금액
	private String pos_cond_text2;              // POS연동 정책 조건 전문2
	private String gift_able_yn;                // 선물가능여부{Y-선물가능, N-선물불가}
	private int    pos_cond_cnt;                // N+1쿠폰 조건대상 개수{데이터 범위 2~10}
	private String event_use_yn;                // 쿠폰 이벤트 사용여부 {Y : 사용, N : 미사용}
	
	// MSR 정책
	private int    gift_send_max_cnt;           // 쿠폰 선물 최대 회 수
	private int    gift_discard_max_cnt;        // 쿠폰 선물취소 최대 회 수
	private int    gift_mms_retry_max_cnt;      // 쿠폰 MMS 재전송 최대 회 수
	
	// 관리지 정보
	private String sck_user_number;				// 카드등록회원 테이블에 등록된 관리자 번호			   


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
	 * 등록일
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 등록일
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15, 006-충전, 007-CoffeeFood, 008-WholeCake, 009-자동충전, 010-e프리퀀시, 011-관리자MMS, 012-리저브원두, 013-1년 무료 음료 쿠폰, 014-그린레벨진입 쿠폰, 015-골드레벨진입 쿠폰, 016-선택 쿠폰, 017-별소진 쿠폰}
	 * @return coupon_code
	 */
	public String getCoupon_code() {
		return coupon_code;
	}

	/**
	 * 쿠폰코드 {001-생일, 002-1+1, 003-원두, 004-PO, 005-별15, 006-충전, 007-CoffeeFood, 008-WholeCake, 009-자동충전, 010-e프리퀀시, 011-관리자MMS, 012-리저브원두, 013-1년 무료 음료 쿠폰, 014-그린레벨진입 쿠폰, 015-골드레벨진입 쿠폰, 016-선택 쿠폰, 017-별소진 쿠폰}
	 * @param coupon_code
	 */
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	
	/**
	 * 쿠폰명
	 * @return coupon_name
	 */
	public String getCoupon_name() {
		return coupon_name;
	}

	/**
	 * 쿠폰명
	 * @param coupon_name
	 */
	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}
	
	/**
	 * 쿠폰 유효기간 시작일
	 * @return coupon_start_date
	 */
	public String getCoupon_start_date() {
		return coupon_start_date;
	}

	/**
	 * 쿠폰 유효기간 시작일
	 * @param coupon_start_date
	 */
	public void setCoupon_start_date(String coupon_start_date) {
		this.coupon_start_date = coupon_start_date;
	}
	
	/**
	 * 쿠폰 유효기간 종료일
	 * @return coupon_end_date
	 */
	public String getCoupon_end_date() {
		return coupon_end_date;
	}

	/**
	 * 쿠폰 유효기간 종료일
	 * @param coupon_end_date
	 */
	public void setCoupon_end_date(String coupon_end_date) {
		this.coupon_end_date = coupon_end_date;
	}
	
	/**
	 * 쿠폰설명
	 * @return coupon_desc
	 */
	public String getCoupon_desc() {
		return coupon_desc;
	}

	/**
	 * 쿠폰설명
	 * @param coupon_desc
	 */
	public void setCoupon_desc(String coupon_desc) {
		this.coupon_desc = coupon_desc;
	}
	
	/**
	 * 쿠폰 발행 등급{00-WEBCOM이상, 10-GREEN이상, 20-GOLD이상, 99-대상지정}
	 * @return coupon_grade
	 */
	public String getCoupon_grade() {
		return coupon_grade;
	}

	/**
	 * 쿠폰 발행 등급{00-WEBCOM이상, 10-GREEN이상, 20-GOLD이상, 99-대상지정}
	 * @param coupon_grade
	 */
	public void setCoupon_grade(String coupon_grade) {
		this.coupon_grade = coupon_grade;
	}
	
	/**
	 * POS 연동 정책 조건 전문
	 * @return pos_cond_text
	 */
	public String getPos_cond_text() {
		return pos_cond_text;
	}

	/**
	 * POS 연동 정책 조건 전문
	 * @param pos_cond_text
	 */
	public void setPos_cond_text(String pos_cond_text) {
		this.pos_cond_text = pos_cond_text;
	}
	
	/**
	 * POS 연동 정책 전용 전문
	 * @return pos_target_text
	 */
	public String getPos_target_text() {
		return pos_target_text;
	}

	/**
	 * POS 연동 정책 전용 전문
	 * @param pos_target_text
	 */
	public void setPos_target_text(String pos_target_text) {
		this.pos_target_text = pos_target_text;
	}
	
	/**
	 * 쿠폰종류{1-일반, 2-1+1, 3-2+1, 4-커피푸드, 5-N+1, 6-금액, 7-선택}
	 * @return coupon_type
	 */
	public String getCoupon_type() {
		return coupon_type;
	}

	/**
	 * 쿠폰종류{1-일반, 2-1+1, 3-2+1, 4-커피푸드, 5-N+1, 6-금액, 7-선택}
	 * @param coupon_type
	 */
	public void setCoupon_type(String coupon_type) {
		this.coupon_type = coupon_type;
	}
	
	/**
	 * 쿠폰 발행 번호
	 * @return coupon_publication_seq
	 */
	public int getCoupon_publication_seq() {
		return coupon_publication_seq;
	}

	/**
	 * 쿠폰 발행 번호
	 * @param coupon_publication_seq
	 */
	public void setCoupon_publication_seq(int coupon_publication_seq) {
		this.coupon_publication_seq = coupon_publication_seq;
	}
	
	/**
	 * 쿠폰발행 예정일
	 * @return coupon_publication_date
	 */
	public String getCoupon_publication_date() {
		return coupon_publication_date;
	}

	/**
	 * 쿠폰발행 예정일
	 * @param coupon_publication_date
	 */
	public void setCoupon_publication_date(String coupon_publication_date) {
		this.coupon_publication_date = coupon_publication_date;
	}
	
	/**
	 * 쿠폰구분{11-생일쿠폰, 12-원두쿠폰, 13-FS, 14-1+1쿠폰, 15-PO쿠폰, 16-PO쿠폰, 17-CoffeeFood쿠폰, 18-WholeCake쿠폰, 19-자동충전쿠폰, 20-e-프리퀀시쿠폰, 21-관리자MMS쿠폰, 22-리저브원두쿠폰, 23-1년 무료 음료 쿠폰, 24-N+1 쿠폰, 26-금액 쿠폰, 27-toGreen 쿠폰, 28-toGold 쿠폰, 29-선택 쿠폰, 30-별소진 쿠폰}
	 * @return coupon_group
	 */
	public String getCoupon_group() {
		return coupon_group;
	}

	/**
	 * 쿠폰구분{11-생일쿠폰, 12-원두쿠폰, 13-FS, 14-1+1쿠폰, 15-PO쿠폰, 16-PO쿠폰, 17-CoffeeFood쿠폰, 18-WholeCake쿠폰, 19-자동충전쿠폰, 20-e-프리퀀시쿠폰, 21-관리자MMS쿠폰, 22-리저브원두쿠폰, 23-1년 무료 음료 쿠폰, 24-N+1 쿠폰, 26-금액 쿠폰, 27-toGreen 쿠폰, 28-toGold 쿠폰, 29-선택 쿠폰, 30-별소진 쿠폰}
	 * @param coupon_group
	 */
	public void setCoupon_group(String coupon_group) {
		this.coupon_group = coupon_group;
	}
	
	/**
	 * 생일쿠폰의 발행대상자의 범위를 가져옴 sysdate + 일자
	 * @return publication_gap_date
	 */
	public int getPublication_gap_date() {
		return publication_gap_date;
	}

	/**
	 * 생일쿠폰의 발행대상자의 범위를 가져옴 sysdate + 일자
	 * @param publication_gap_date
	 */
	public void setPublication_gap_date(int publication_gap_date) {
		this.publication_gap_date = publication_gap_date;
	}
	
	/**
	 * 발행상태{N-미발행, Y-발행}
	 * @return publication_status
	 */
	public String getPublication_status() {
		return publication_status;
	}

	/**
	 * 발행상태{N-미발행, Y-발행}
	 * @param publication_status
	 */
	public void setPublication_status(String publication_status) {
		this.publication_status = publication_status;
	}
	
	/**
	 * 유효일수(1. 생일쿠폰:생일로 부터 유효 일 수 2. 생일쿠폰 : 발행대상자의 이전범위 sysdate - 일자)
	 * @return valid_day
	 */
	public int getValid_day() {
		return valid_day;
	}

	/**
	 * 유효일수(1. 생일쿠폰:생일로 부터 유효 일 수 2. 생일쿠폰 : 발행대상자의 이전범위 sysdate - 일자)
	 * @param valid_day
	 */
	public void setValid_day(int valid_day) {
		this.valid_day = valid_day;
	}
	
	/**
	 * 사용자 쿠폰명
	 * @return user_coupon_name
	 */
	public String getUser_coupon_name() {
		return user_coupon_name;
	}

	/**
	 * 사용자 쿠폰명
	 * @param user_coupon_name
	 */
	public void setUser_coupon_name(String user_coupon_name) {
		this.user_coupon_name = user_coupon_name;
	}
	
	/**
	 * 이용 가능 상품
	 * @return use_product
	 */
	public String getUse_product() {
		return use_product;
	}

	/**
	 * 이용 가능 상품
	 * @param use_product
	 */
	public void setUse_product(String use_product) {
		this.use_product = use_product;
	}
	
	/**
	 * 사용시 주의 사항
	 * @return use_notice
	 */
	public String getUse_notice() {
		return use_notice;
	}

	/**
	 * 사용시 주의 사항
	 * @param use_notice
	 */
	public void setUse_notice(String use_notice) {
		this.use_notice = use_notice;
	}
	
	/**
	 * 이 메일 이미지
	 * @return email_img
	 */
	public String getEmail_img() {
		return email_img;
	}

	/**
	 * 이 메일 이미지
	 * @param email_img
	 */
	public void setEmail_img(String email_img) {
		this.email_img = email_img;
	}
	
	/**
	 * POS연동 정책 적용 전문2
	 * @return pos_target_text2
	 */
	public String getPos_target_text2() {
		return pos_target_text2;
	}

	/**
	 * POS연동 정책 적용 전문2
	 * @param pos_target_text2
	 */
	public void setPos_target_text2(String pos_target_text2) {
		this.pos_target_text2 = pos_target_text2;
	}
	
	/**
	 * 선물등록쿠폰명
	 * @return gift_reg_coupon_title
	 */
	public String getGift_reg_coupon_title() {
		return gift_reg_coupon_title;
	}

	/**
	 * 선물등록쿠폰명
	 * @param gift_reg_coupon_title
	 */
	public void setGift_reg_coupon_title(String gift_reg_coupon_title) {
		this.gift_reg_coupon_title = gift_reg_coupon_title;
	}
	
	/**
	 * 할인금액
	 * @return discount_amount
	 */
	public int getDiscount_amount() {
		return discount_amount;
	}

	/**
	 * 할인금액
	 * @param discount_amount
	 */
	public void setDiscount_amount(int discount_amount) {
		this.discount_amount = discount_amount;
	}
	
	/**
	 * POS연동 정책 조건 전문2
	 * @return pos_cond_text2
	 */
	public String getPos_cond_text2() {
		return pos_cond_text2;
	}

	/**
	 * POS연동 정책 조건 전문2
	 * @param pos_cond_text2
	 */
	public void setPos_cond_text2(String pos_cond_text2) {
		this.pos_cond_text2 = pos_cond_text2;
	}
	
	/**
	 * 선물가능여부{Y-선물가능, N-선물불가}
	 * @return gift_able_yn
	 */
	public String getGift_able_yn() {
		return gift_able_yn;
	}

	/**
	 * 선물가능여부{Y-선물가능, N-선물불가}
	 * @param gift_able_yn
	 */
	public void setGift_able_yn(String gift_able_yn) {
		this.gift_able_yn = gift_able_yn;
	}
	
	/**
	 * N+1쿠폰 조건대상 개수{데이터 범위 2~10}
	 * @return pos_cond_cnt
	 */
	public int getPos_cond_cnt() {
		return pos_cond_cnt;
	}

	/**
	 * N+1쿠폰 조건대상 개수{데이터 범위 2~10}
	 * @param pos_cond_cnt
	 */
	public void setPos_cond_cnt(int pos_cond_cnt) {
		this.pos_cond_cnt = pos_cond_cnt;
	}
	
	/**
	 * 쿠폰 이벤트 사용여부 {Y : 사용, N : 미사용}
	 * @return event_use_yn
	 */
	public String getEvent_use_yn() {
		return event_use_yn;
	}

	/**
	 * 쿠폰 이벤트 사용여부 {Y : 사용, N : 미사용}
	 * @param event_use_yn
	 */
	public void setEvent_use_yn(String event_use_yn) {
		this.event_use_yn = event_use_yn;
	}
	
	/**
	 * 쿠폰 선물 최대 회 수
	 * @return gift_send_max_cnt
	 */
	public int getGift_send_max_cnt() {
		return gift_send_max_cnt;
	}

	/**
	 * 쿠폰 선물 최대 회 수
	 * @param gift_send_max_cnt
	 */
	public void setGift_send_max_cnt(int gift_send_max_cnt) {
		this.gift_send_max_cnt = gift_send_max_cnt;
	}
	
	/**
	 * 쿠폰 선물취소 최대 회 수
	 * @return gift_discard_max_cnt
	 */
	public int getGift_discard_max_cnt() {
		return gift_discard_max_cnt;
	}

	/**
	 * 쿠폰 선물취소 최대 회 수
	 * @param gift_discard_max_cnt
	 */
	public void setGift_discard_max_cnt(int gift_discard_max_cnt) {
		this.gift_discard_max_cnt = gift_discard_max_cnt;
	}
	
	/**
	 * 쿠폰 MMS 재전송 최대 회 수
	 * @return gift_mms_retry_max_cnt
	 */
	public int getGift_mms_retry_max_cnt() {
		return gift_mms_retry_max_cnt;
	}

	/**
	 * 쿠폰 MMS 재전송 최대 회 수
	 * @param gift_mms_retry_max_cnt
	 */
	public void setGift_mms_retry_max_cnt(int gift_mms_retry_max_cnt) {
		this.gift_mms_retry_max_cnt = gift_mms_retry_max_cnt;
	}
	
	
	/**
	 * 카드등록회원 테이블에 등록된 관리자 번호	
	 * @return sck_user_number
	 */
	public String getSck_user_number() {
		return sck_user_number;
	}

	/**
	 * 카드등록회원 테이블에 등록된 관리자 번호	
	 * @param sck_user_number
	 */
	public void setSck_user_number(String sck_user_number) {
		this.sck_user_number = sck_user_number;
	}
	
	

	
}
