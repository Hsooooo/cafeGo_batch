/*
 * @(#) $Id: UserRegCardDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 회원이 등록한 스타벅스 카드
 * @author eZEN ksy
 * @since 2014. 1. 18.
 * @version $Revision: 1.1 $
 */

public class UserRegCardDto implements Serializable {
	
	private static final long serialVersionUID = 5840938990672743283L;
	
	private String card_number;
	private String user_number;
	private String card_nickname;
	private Date reg_date;
	private Integer card_reg_number;
	private Integer balance;
	private Date balance_confirm_date;
	
	private String card_name_k;
	private String card_img_code;
	private String card_img;
	private String card_thumbnail_img;
	private String card_status;
	private long card_reg_seq;
	
	private String card_type_code;
	
	private Date change_date; // 카드 마스터 상태 변경일 추가(2011-07-28, tytolee)
	private String gold_card_reg_status;
	
	private String mobile_view_flag; // 모바일 view 여부 추가(2011-09-08, ksy)
	
	private String change_actor_group_code; // 상태 변경 주체
	
	private String return_code; // 환불 상태코드
	
	private String gold_card_req_seq; // 골드카드 신청 그룹 번호
	private String store_name; // 골드카드 신청시 등록한 수령매장
	private String store_code; // 골드카드 신청시 등록한 수령매장
	private String receive_type; // 골드카드 신청시 수령지 유형(S:매장, A:주소(null))
	private String destruct_date; // 골드카드 매장도착시 보관일자
	private String in_store_nm; // 골드카드 이관 매장명
	private String out_store_nm; // 골드카드 신청 매장명
	private String email; // 골드카드 신청 이메일
	private String mobile_num; // 골드카드 신청 휴대폰번호
	private String from_dt; // 골드카드 도착 예정 일자 from_date 
	private String to_dt; // 골드카드 도착 예정 일자 to_date
	
	private String auto_reload_type; //자동충전방식
	private String auto_reload_type_str; //자동충전방식
	private String auto_reload_amount; //자동충전금액
	private String lowest_amount; //기준하한금액
	private String auto_reload_day; //자동충전일
	private String auto_reload_pay_method; //자동충전결제수당
	private String auto_reload_bill_key; //PG빌키
	private String auto_reload_reg_date; //자동충전설정일자
	private String auto_reload_cancel_date;//자동충전취소일자
	private String billing_mobile_num; //빌링휴대폰번호
	private String auto_reload_fail_count; //자동충전실패횟수
	
	private String user_name; //회원명
	
	private String taxsave; //현금영수증 발행 동의여부
	private String phone_number; //현금영수증 휴대전화번호
	private String receipt_type; //현금영수증 타입
	private String user_id; //사용자 id
	
	private String egift_yn; // 기프트카드 여부 
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("UserRegCardDto [");
		str.append ("card_number=").append (this.card_number);
		str.append (", ").append ("user_number=").append (this.user_number);
		str.append (", ").append ("card_nickname=").append (this.card_nickname);
		if ( this.reg_date != null ) {
			str.append (", ").append ("reg_date=").append (DateFormatUtils.format (this.reg_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("card_reg_number=").append (this.card_reg_number);
		str.append (", ").append ("balance=").append (this.balance);
		if ( this.balance_confirm_date != null ) {
			str.append (", ").append ("balance_confirm_date=").append (DateFormatUtils.format (this.balance_confirm_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("card_name_k=").append (this.card_name_k);
		str.append (", ").append ("card_img_code=").append (this.card_img_code);
		str.append (", ").append ("card_img=").append (this.card_img);
		str.append (", ").append ("card_thumbnail_img=").append (this.card_thumbnail_img);
		str.append (", ").append ("card_status=").append (this.card_status);
		str.append (", ").append ("card_reg_seq=").append (this.card_reg_seq);
		str.append (", ").append ("card_type_code=").append (this.card_type_code);
		if ( this.change_date != null ) {
			str.append (", ").append ("change_date=").append (DateFormatUtils.format (this.change_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("gold_card_reg_status=").append (this.gold_card_reg_status);
		str.append (", ").append ("store_name=").append (this.store_name);
		str.append (", ").append ("store_code=").append (this.store_code);
		str.append (", ").append ("mobile_view_flag=").append (this.mobile_view_flag);
		str.append (", ").append ("change_actor_group_code=").append (this.change_actor_group_code);
		str.append (", ").append ("gold_card_req_seq=").append (this.gold_card_req_seq);
		str.append (", ").append ("user_name=").append (this.user_name);
		str.append (", ").append ("egift_yn=").append (this.egift_yn);
		str.append ("]");
		
		return str.toString ();
	}
	
	
	public String getUser_name () {
		return user_name;
	}
	
	
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	
	public Date getChange_date () {
		return change_date;
	}
	
	public void setChange_date ( Date change_date ) {
		this.change_date = change_date;
	}
	
	/**
	 * 카드일련번호 (암호화)
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * 카드일련번호 (암호화)
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
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
	 * 카드 닉네임
	 * @return
	 */
	public String getCard_nickname () {
		return card_nickname;
	}
	
	/**
	 * 카드 닉네임
	 * @param card_nickname
	 */
	public void setCard_nickname ( String card_nickname ) {
		this.card_nickname = card_nickname;
	}
	
	/**
	 * 카드 등록일
	 * @return
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 카드 등록일
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	public String getCard_name_k () {
		return card_name_k;
	}
	
	public void setCard_name_k ( String card_name_k ) {
		this.card_name_k = card_name_k;
	}
	
	/**
	 * 카드 등록 번호
	 * @return
	 */
	public int getCard_reg_number () {
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
	 * 카드 잔액
	 * @return Integer
	 * @since -
	 * @modify 2011-07-28(my 스타벅스 카드 관리_충전 카드 상세 정보 페이지에서 사용하기 위해 주석처리, tytolee)
	 */
	public Integer getBalance () {
		return balance;
	}
	
	/**
	 * 카드 잔액
	 * @param balance
	 */
	public void setBalance ( Integer balance ) {
		this.balance = balance;
	}
	
	/**
	 * 잔액 확정 일자
	 * @return
	 */
	public Date getBalance_confirm_date () {
		return balance_confirm_date;
	}
	
	/**
	 * 잔액 확정 일자
	 * @param balance_confirm_date
	 */
	public void setBalance_confirm_date ( Date balance_confirm_date ) {
		this.balance_confirm_date = balance_confirm_date;
	}
	
	/**
	 * 이미지 코드
	 * @return
	 */
	public String getCard_img_code () {
		return card_img_code;
	}
	
	/**
	 * 이미지 코드
	 * @param card_img_code
	 */
	public void setCard_img_code ( String card_img_code ) {
		this.card_img_code = card_img_code;
	}
	
	/**
	 * 이미지 경로
	 * @return
	 */
	public String getCard_img () {
		return card_img;
	}
	
	/**
	 * 이미지 경로
	 * @param card_img
	 */
	public void setCard_img ( String card_img ) {
		this.card_img = card_img;
	}
	
	/**
	 * 썸네일 이미지 경로
	 * @return
	 */
	public String getCard_thumbnail_img () {
		return card_thumbnail_img;
	}
	
	/**
	 * 썸네일 이미지 경로
	 * @param card_thumbnail_img
	 */
	public void setCard_thumbnail_img ( String card_thumbnail_img ) {
		this.card_thumbnail_img = card_thumbnail_img;
	}
	
	/**
	 * 카드 상태
	 * R : 등록, S : 중지, X : 미등록
	 * @return
	 */
	public String getCard_status () {
		return card_status;
	}
	
	/**
	 * 카드 상태
	 * R : 등록, S : 중지, X : 미등록
	 * @param card_status
	 */
	public void setCard_status ( String card_status ) {
		this.card_status = card_status;
	}
	
	/**
	 * 카드 구분 일련 코드
	 * @return
	 */
	public long getCard_reg_seq () {
		return card_reg_seq;
	}
	
	
	/**
	 * 카드 구분 일련 코드
	 * @param card_reg_seq
	 */
	public void setCard_reg_seq ( long card_reg_seq ) {
		this.card_reg_seq = card_reg_seq;
	}
	
	
	public void setAuto_reload_type_str ( String auto_reload_type_str ) {
		this.auto_reload_type_str = auto_reload_type_str;
	}
	
	
	/**
	 * 카드 구분 코드
	 * N : 일반, G : 골드
	 * @return
	 */
	public String getCard_type_code () {
		return card_type_code;
	}
	
	/**
	 * 카드 구분 코드
	 * N : 일반, G : 골드
	 * @param card_type_code
	 */
	public void setCard_type_code ( String card_type_code ) {
		this.card_type_code = card_type_code;
	}
	
	public String getGold_card_reg_status () {
		return gold_card_reg_status;
	}
	
	public void setGold_card_reg_status ( String gold_card_reg_status ) {
		this.gold_card_reg_status = gold_card_reg_status;
	}
	
	
	/**
	 * 모바일 표시 여부
	 * Y : 표시 , N : 미표시
	 * @return
	 */
	public String getMobile_view_flag () {
		return mobile_view_flag;
	}
	
	/**
	 * 모바일 표시 여부
	 * Y : 표시 , N : 미표시
	 * @param card_type_code
	 */
	public void setMobile_view_flag ( String mobile_view_flag ) {
		this.mobile_view_flag = mobile_view_flag;
	}
	
	/**
	 * 카드 상태 변경 주체
	 * U : 사용자, A : 관리자. P : POS
	 * @return
	 */
	public String getChange_actor_group_code () {
		return change_actor_group_code;
	}
	
	/**
	 * 카드 상태 변경 주체
	 * U : 사용자, A : 관리자. P : POS
	 * @param change_actor_group_code
	 */
	public void setChange_actor_group_code ( String change_actor_group_code ) {
		this.change_actor_group_code = change_actor_group_code;
	}
	
	/**
	 * 카드 상태 변경 주체
	 * U : 사용자, A : 관리자. P : POS
	 * @return
	 */
	public String getReturn_code () {
		return return_code;
	}
	
	/**
	 * 카드 상태 변경 주체
	 * U : 사용자, A : 관리자. P : POS
	 * @param change_actor_group_code
	 */
	public void setReturn_code ( String return_code ) {
		this.return_code = return_code;
	}
	
	/**
	 * 골드카드 신청 그룹 번호
	 * @return
	 */
	public String getGold_card_req_seq () {
		return gold_card_req_seq;
	}
	
	/**
	 * 골드카드 신청 그룹 번호
	 * @param gold_card_req_seq
	 */
	public void setGold_card_req_seq ( String gold_card_req_seq ) {
		this.gold_card_req_seq = gold_card_req_seq;
	}
	
	/**
	 * 골드카드 신청 수령매장
	 * @return
	 */
	public String getStore_name () {
		return store_name;
	}
	
	/**
	 * 골드카드 신청 수령매장
	 * @param store_name
	 */
	public void setStore_name ( String store_name ) {
		this.store_name = store_name;
	}
	
	/**
	 * 골드카드 신청 수령매장코드
	 * @return
	 */
	public String getStore_code () {
		return store_code;
	}
	
	/**
	 * 골드카드 신청 수령매장코드
	 * @param store_code
	 */
	public void setStore_code ( String store_code ) {
		this.store_code = store_code;
	}
	
	public String getReceive_type () {
		return receive_type;
	}
	
	public void setReceive_type ( String receive_type ) {
		this.receive_type = receive_type;
	}
	
	/**
	 * 매장보관일자
	 * @return
	 */
	public String getDestruct_date () {
		return destruct_date;
	}
	
	/**
	 * 매장보관일자
	 * @param destruct_date
	 */
	public void setDestruct_date ( String destruct_date ) {
		this.destruct_date = destruct_date;
	}
	
	/**
	 * 골드카드 이관 매장
	 * @return
	 */
	public String getIn_store_nm () {
		return in_store_nm;
	}
	
	/**
	 * 골드카드 이관 매장
	 * @param in_store_nm
	 */
	public void setIn_store_nm ( String in_store_nm ) {
		this.in_store_nm = in_store_nm;
	}
	
	/**
	 * 골드카드 신청 매장
	 * @return
	 */
	public String getOut_store_nm () {
		return out_store_nm;
	}
	
	/**
	 * 골드카드 신청 매장
	 * @param out_store_nm
	 */
	public void setOut_store_nm ( String out_store_nm ) {
		this.out_store_nm = out_store_nm;
	}
	
	/**
	 * 골드카드 신청 이메일
	 * @return
	 */
	public String getEmail () {
		return email;
	}
	
	/**
	 * 골드카드 신청 이메일
	 * @param email
	 */
	public void setEmail ( String email ) {
		this.email = email;
	}
	
	/**
	 * 골드카드 신청 휴대폰 번호
	 * @return
	 */
	public String getMobile_num () {
		return mobile_num;
	}
	
	/**
	 * 골드카드 신청 휴대폰 번호
	 * @param mobile_num
	 */
	public void setMobile_num ( String mobile_num ) {
		this.mobile_num = mobile_num;
	}
	
	/**
	 * 골드카드 도착 예정일 from_date
	 * @return
	 */
	public String getFrom_dt () {
		return from_dt;
	}
	
	/**
	 * 골드카드 도착 예정일 from_date
	 * @param from_dt
	 */
	public void setFrom_dt ( String from_dt ) {
		this.from_dt = from_dt;
	}
	
	/**
	 * 골드카드 도착 예정일 to_date
	 * @return
	 */
	public String getTo_dt () {
		return to_dt;
	}
	
	/**
	 * 골드카드 도착 예정일 to_date
	 * @param to_dt
	 */
	public void setTo_dt ( String to_dt ) {
		this.to_dt = to_dt;
	}
	
	/**
	 * 자동충전방식
	 * 1:월정액기준, 2:하한잔액기준, 9:자동충전대상아님
	 * @return
	 */
	public String getAuto_reload_type () {
		return auto_reload_type;
	}
	
	/**
	 * 자동충전방식
	 * 1:월정액기준, 2:하한잔액기준, 9:자동충전대상아님
	 * @param auto_reload_type
	 */
	public void setAuto_reload_type ( String auto_reload_type ) {
		this.auto_reload_type = auto_reload_type;
	}
	
	
	public String getAuto_reload_type_str () {
		if ( StringUtils.equals (getAuto_reload_type (), "1") ) {
			auto_reload_type_str = "월 정액 자동 충전";
		} else if ( StringUtils.equals (getAuto_reload_type (), "2") ) {
			auto_reload_type_str = "기준 하한 자동 충전";
		} else if ( StringUtils.equals (getAuto_reload_type (), "9") ) {
			auto_reload_type_str = "";
		}
		return auto_reload_type_str;
	}
	
	/**
	 * 자동충전금액
	 * @return
	 */
	public String getAuto_reload_amount () {
		return auto_reload_amount;
	}
	
	/**
	 * 자동충전금액
	 * @param auto_reload_amount
	 */
	public void setAuto_reload_amount ( String auto_reload_amount ) {
		this.auto_reload_amount = auto_reload_amount;
	}
	
	/**
	 * 기준하한금액
	 * @return
	 */
	public String getLowest_amount () {
		return lowest_amount;
	}
	
	/**
	 * 기준하한금액
	 * @param lowest_amount
	 */
	public void setLowest_amount ( String lowest_amount ) {
		this.lowest_amount = lowest_amount;
	}
	
	/**
	 * 자동충전일
	 * @return
	 */
	public String getAuto_reload_day () {
		return auto_reload_day;
	}
	
	/**
	 * 자동충전일
	 * @param auto_reload_day
	 */
	public void setAuto_reload_day ( String auto_reload_day ) {
		this.auto_reload_day = auto_reload_day;
	}
	
	/**
	 * 자동충전결제수단
	 * @return
	 */
	public String getAuto_reload_pay_method () {
		return auto_reload_pay_method;
	}
	
	/**
	 * 자동충전결제수단
	 * @param auto_reload_pay_method
	 */
	public void setAuto_reload_pay_method ( String auto_reload_pay_method ) {
		this.auto_reload_pay_method = auto_reload_pay_method;
	}
	
	/**
	 * PG빌키
	 * @return
	 */
	public String getAuto_reload_bill_key () {
		return auto_reload_bill_key;
	}
	
	/**
	 * PG빌키
	 * @param auto_reload_bill_key
	 */
	public void setAuto_reload_bill_key ( String auto_reload_bill_key ) {
		this.auto_reload_bill_key = auto_reload_bill_key;
	}
	
	/**
	 * 자동충전설정일자
	 * @return
	 */
	public String getAuto_reload_reg_date () {
		return auto_reload_reg_date;
	}
	
	/**
	 * 자동충전설정일자
	 * @param auto_reload_reg_date
	 */
	public void setAuto_reload_reg_date ( String auto_reload_reg_date ) {
		this.auto_reload_reg_date = auto_reload_reg_date;
	}
	
	/**
	 * 자동충전취소일자
	 * @return
	 */
	public String getAuto_reload_cancel_date () {
		return auto_reload_cancel_date;
	}
	
	/**
	 * 자동충전취소일자
	 * @param auto_reload_cancel_date
	 */
	public void setAuto_reload_cancel_date ( String auto_reload_cancel_date ) {
		this.auto_reload_cancel_date = auto_reload_cancel_date;
	}
	
	/**
	 * 빌링휴대전화번호
	 * @return
	 */
	public String getBilling_mobile_num () {
		return billing_mobile_num;
	}
	
	/**
	 * 빌링휴대전화번호
	 * @param billing_mobile_num
	 */
	public void setBilling_mobile_num ( String billing_mobile_num ) {
		this.billing_mobile_num = billing_mobile_num;
	}
	
	/**
	 * 자동충전실패횟수
	 * @return
	 */
	public String getAuto_reload_fail_count () {
		return auto_reload_fail_count;
	}
	
	/**
	 * 자동충전실패횟수
	 * @param auto_reload_fail_count
	 */
	public void setAuto_reload_fail_count ( String auto_reload_fail_count ) {
		this.auto_reload_fail_count = auto_reload_fail_count;
	}
	
	public String getTaxsave () {
		return taxsave;
	}
	
	
	public void setTaxsave ( String taxsave ) {
		this.taxsave = taxsave;
	}
	
	
	public String getPhone_number () {
		return phone_number;
	}
	
	
	public void setPhone_number ( String phone_number ) {
		this.phone_number = phone_number;
	}
	
	
	public String getReceipt_type () {
		return receipt_type;
	}
	
	
	public void setReceipt_type ( String receipt_type ) {
		this.receipt_type = receipt_type;
	}
	
	
	public String getUser_id () {
		return user_id;
	}
	
	
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * e-기프트 카드 여부
	 * Y: 기프트카드
	 * N: 기프트카드아님
	 * @return
	 */
	public String getEgift_yn () {
		return egift_yn;
	}
	
	/**
	 * e-기프트 카드 여부
	 * Y: 기프트카드
	 * N: 기프트카드아님
	 * @param egift_yn
	 */
	public void setEgift_yn ( String egift_yn ) {
		this.egift_yn = egift_yn;
	}
	

}
