/*
 * @(#) $Id: WholecakeOrderDto.java,v 1.4 2016/11/15 04:38:51 dev99 Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 홀케익예약 WholecakeOrderDto.
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.4 $
 */
public class WholecakeOrderDto {
	String order_no;                  // 예약번호
	String cart_no;                   // 주문서번호
	String policy_cd;                 // 정책 코드
	String order_name;                // 예약명
	String user_id;                   // 사용자아이디
	String user_name;                 // 사용자명
	String user_mobile;               // 사용자 휴대폰번호{암호화}
	String user_email;                // 사용자 이메일{암호화}
	String user_nickname;             // 사용자 닉네임
	String msr_user_flag;             // MSR회원여부{Y-회원, N-비회원}
	String msr_user_grade;            // MSR회원등급
	String status;                    // 상태{코드표참조}
	String gstdgr;                    // 객층코드
	String os_type;                   // 단말구분{1-iOS, 2-Android, 9-기타}
	String push_id;                   // PUSH ID
	String guide_code;                // 예약안내 코드
	int    guide_version;             // 예약안내 버전
	String cancel_policy_code;        // 취소/환불 정책 코드
	int    cancel_policy_version;     // 취소/환불 정책 버전
	String receive_store_cd;          // 수령 매장코드
	String receive_date;              // 수령예정일{YYYYMMDD}
	String sales_order_date;          // 발주확정일{YYYYMMDD}
	String revocable_date;            // 취소가능일{YYYYMMDD}
	int    total_qty;                 // 전체 수량
	int    total_pay_amt;             // 총 결제금액{총 품목총액 - 총 할인액}
	int    total_discount;            // 총 할인액
	int    total_coupon;              // 총 쿠폰액
	int    total_item_amount;         // 총 품목총액
	String receipt_type;              // 현금영수증 발행구분
	String receipt_number;            // 현금영수증 발행번호{암호화}
	int    receipt_amount;            // 현금영수증 대상금액
	String emp_no;                    // 직원번호
	String present_no;                // 선물번호
	String receiver_name;             // 수신자명
	String receiver_mobile;           // 수신자 휴대폰번호{암호화}
	String present_message;           // 선물 메세지
	String present_notice_code;       // 선물 유의사항 코드
	int    present_notice_version;    // 선물 유의사항 버전
	String present_status;            // 선물 상태{코드표참조}
	String receive_type;              // 수령구분{O-주문번호, P-선물번호}
	String cashier_id;                // 파트너ID
	String cashier_name;              // 파트너명
	String sale_date;                 // 영업일{YYYYMMDD}
	String store_cd;                  // 점포코드
	String pos_no;                    // POS번호
	String seq_no;                    // 거래번호
	String pos_comp_flag;             // 매출확정여부{Y-확정}
	String sales_order_comp_flag;     // 발주확정여부{Y-확정}
	String trade_flag;                // 대사완료여부{Y-완료}
	String critical_flag;             // 오류구분
	String use_flag;                  // 사용여부{Y-사용, N-미사용}
	Date   reg_date;                  // 등록일
	Date   pay_date;                  // 결제 완료일자
	Date   mod_date;                  // 예약 변경일자
	Date   receive_comp_date;         // 수령/미수령 완료일자
	Date   cancel_date;               // 예약 취소일자
	Date   receiver_mobile_del_date;  // 수신자 휴대폰번호 삭제일자
	String coupon_pub_flag;           // 쿠폰발행여부{Y-발행}
	
	String present_resend_yn;         // 선물재발송여부_YN
	String order_date; 				  // 발주일자{YYYYMMDD}
	
	// 쿠폰 발행을 위한 추가 항목
	int           coupon_count;              // 홀케익 예약 정책의 쿠폰 발행 개수 
	int           total_coupon_count;        // 홀케익 예약의 총 쿠폰 발행 개수 ((전체 수량-홀케익쿠폰사용수)*홀케익정책의 쿠폰 발행 개수)
	int           publication_coupon_count;  // 밸행 성공한 쿠폰 수 
	List<String>  success_coupon_list = new ArrayList<String>();       // 쿠폰 발행 및 선물 발송 성공한 쿠폰 목록
	List<String>  fail_coupon_list    = new ArrayList<String>();	     // MMS 선물 발송 실패한 쿠폰 목록 
	
	// XO_WHOLECAKE_ORDER_DETAIL에서 사용
	int    item_seq;				  // 상세순번
	String sku_no;					  // SKU코드
	int    price;					  // 단가
	int    qty;						  // 수량
	int    item_amount;				  // 품목총액(금액*수량)
	int    discount;				  // 할인액(품목총액 *(직원할인율 *0.1))
	int    gnd_amount;				  // 순매출액
	String cate_type;				  // 카테고리구분
	String coupon_number;			  // 쿠폰일련번호
	int    coupon_dc_amt;			  // 쿠폰할인금액
	String sck_coupon_no;			  // 영업정보쿠폰번호
	
	// 예약 확인 LMS 발송을 위한 항목
	String receive_store_name;        // 수령 매장 명
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append(", order_no=").append(this.order_no);
		sb.append(", cart_no=").append(this.cart_no);
		sb.append(", policy_cd=").append(this.policy_cd);
		sb.append(", order_name=").append(this.order_name);
		sb.append(", user_id=").append(this.user_id);
		sb.append(", user_name=").append(this.user_name);
		sb.append(", user_mobile=").append(this.user_mobile);
		sb.append(", user_email=").append(this.user_email);
		sb.append(", user_nickname=").append(this.user_nickname);
		sb.append(", msr_user_flag=").append(this.msr_user_flag);
		sb.append(", msr_user_grade=").append(this.msr_user_grade);
		sb.append(", status=").append(this.status);
		sb.append(", gstdgr=").append(this.gstdgr);
		sb.append(", os_type=").append(this.os_type);
		sb.append(", push_id=").append(this.push_id);
		sb.append(", guide_code=").append(this.guide_code);
		sb.append(", guide_version=").append(this.guide_version);
		sb.append(", cancel_policy_code=").append(this.cancel_policy_code);
		sb.append(", cancel_policy_version=").append(this.cancel_policy_version);
		sb.append(", receive_store_cd=").append(this.receive_store_cd);
		sb.append(", receive_date=").append(this.receive_date);
		sb.append(", sales_order_date=").append(this.sales_order_date);
		sb.append(", revocable_date=").append(this.revocable_date);
		sb.append(", total_qty=").append(this.total_qty);
		sb.append(", total_pay_amt=").append(this.total_pay_amt);
		sb.append(", total_discount=").append(this.total_discount);
		sb.append(", total_coupon=").append(this.total_coupon);
		sb.append(", total_item_amount=").append(this.total_item_amount);
		sb.append(", receipt_type=").append(this.receipt_type);
		sb.append(", receipt_number=").append(this.receipt_number);
		sb.append(", receipt_amount=").append(this.receipt_amount);
		sb.append(", emp_no=").append(this.emp_no);
		sb.append(", present_no=").append(this.present_no);
		sb.append(", receiver_name=").append(this.receiver_name);
		sb.append(", receiver_mobile=").append(this.receiver_mobile);
		sb.append(", present_message=").append(this.present_message);
		sb.append(", present_notice_code=").append(this.present_notice_code);
		sb.append(", present_notice_version=").append(this.present_notice_version);
		sb.append(", present_status=").append(this.present_status);
		sb.append(", receive_type=").append(this.receive_type);
		sb.append(", cashier_id=").append(this.cashier_id);
		sb.append(", cashier_name=").append(this.cashier_name);
		sb.append(", sale_date=").append(this.sale_date);
		sb.append(", store_cd=").append(this.store_cd);
		sb.append(", pos_no=").append(this.pos_no);
		sb.append(", seq_no=").append(this.seq_no);
		sb.append(", pos_comp_flag=").append(this.pos_comp_flag);
		sb.append(", sales_order_comp_flag=").append(this.sales_order_comp_flag);
		sb.append(", trade_flag=").append(this.trade_flag);
		sb.append(", critical_flag=").append(this.critical_flag);
		sb.append(", use_flag=").append(this.use_flag);
		sb.append(", reg_date=").append(this.reg_date);
		sb.append(", pay_date=").append(this.pay_date);
		sb.append(", mod_date=").append(this.mod_date);
		sb.append(", receive_comp_date=").append(this.receive_comp_date);
		sb.append(", cancel_date=").append(this.cancel_date);
		sb.append(", receiver_mobile_del_date=").append(this.receiver_mobile_del_date);
		sb.append(", coupon_pub_flag=").append(this.coupon_pub_flag);
		sb.append(", coupon_count=").append(this.coupon_count);
		sb.append(", total_coupon_count=").append(this.total_coupon_count);
		sb.append(", publication_coupon_count=").append(this.publication_coupon_count);
		sb.append(", order_date=").append(this.order_date);
		sb.append(", item_seq=").append(this.item_seq);
		sb.append(", sku_no=").append(this.sku_no);
		sb.append(", price=").append(this.price);
		sb.append(", qty=").append(this.qty);
		sb.append(", item_amount=").append(this.item_amount);
		sb.append(", discount=").append(this.discount);
		sb.append(", gnd_amount=").append(this.gnd_amount);
		sb.append(", cate_type=").append(this.cate_type);
		sb.append(", coupon_number=").append(this.coupon_number);
		sb.append(", coupon_dc_amt=").append(this.coupon_dc_amt);
		sb.append(", sck_coupon_no=").append(this.sck_coupon_no);
		sb.append(", present_resend_yn=").append(this.present_resend_yn);
		sb.append(", success_coupon_list=").append(this.success_coupon_list);
		sb.append(", fail_coupon_list=").append(this.fail_coupon_list);
		sb.append(", receive_store_name=").append(this.receive_store_name);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 예약번호
	 * @return order_no
	 */
	public String getOrder_no() {
		return order_no;
	}

	/**
	 * 예약번호
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	/**
	 * 주문서번호
	 * @return cart_no
	 */
	public String getCart_no() {
		return cart_no;
	}

	/**
	 * 주문서번호
	 * @param cart_no
	 */
	public void setCart_no(String cart_no) {
		this.cart_no = cart_no;
	}
	
	/**
	 * 정책 코드
	 * @return policy_cd
	 */
	public String getPolicy_cd() {
		return policy_cd;
	}

	/**
	 * 정책 코드
	 * @param policy_cd
	 */
	public void setPolicy_cd(String policy_cd) {
		this.policy_cd = policy_cd;
	}
	
	/**
	 * 예약명
	 * @return order_name
	 */
	public String getOrder_name() {
		return order_name;
	}

	/**
	 * 예약명
	 * @param order_name
	 */
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	
	/**
	 * 사용자아이디
	 * @return user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * 사용자아이디
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * 사용자명
	 * @return user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * 사용자명
	 * @param user_name
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	/**
	 * 사용자 휴대폰번호{암호화}
	 * @return user_mobile
	 */
	public String getUser_mobile() {
		return user_mobile;
	}

	/**
	 * 사용자 휴대폰번호{암호화}
	 * @param user_mobile
	 */
	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}
	
	/**
	 * 사용자 이메일{암호화}
	 * @return user_email
	 */
	public String getUser_email() {
		return user_email;
	}

	/**
	 * 사용자 이메일{암호화}
	 * @param user_email
	 */
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	
	/**
	 * 사용자 닉네임
	 * @return user_nickname
	 */
	public String getUser_nickname() {
		return user_nickname;
	}

	/**
	 * 사용자 닉네임
	 * @param user_nickname
	 */
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	
	/**
	 * MSR회원여부{Y-회원, N-비회원}
	 * @return msr_user_flag
	 */
	public String getMsr_user_flag() {
		return msr_user_flag;
	}

	/**
	 * MSR회원여부{Y-회원, N-비회원}
	 * @param msr_user_flag
	 */
	public void setMsr_user_flag(String msr_user_flag) {
		this.msr_user_flag = msr_user_flag;
	}
	
	/**
	 * MSR회원등급
	 * @return msr_user_grade
	 */
	public String getMsr_user_grade() {
		return msr_user_grade;
	}

	/**
	 * MSR회원등급
	 * @param msr_user_grade
	 */
	public void setMsr_user_grade(String msr_user_grade) {
		this.msr_user_grade = msr_user_grade;
	}
	
	/**
	 * 상태{코드표참조}
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 상태{코드표참조}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 객층코드
	 * @return gstdgr
	 */
	public String getGstdgr() {
		return gstdgr;
	}

	/**
	 * 객층코드
	 * @param gstdgr
	 */
	public void setGstdgr(String gstdgr) {
		this.gstdgr = gstdgr;
	}
	
	/**
	 * 단말구분{1-iOS, 2-Android, 9-기타}
	 * @return os_type
	 */
	public String getOs_type() {
		return os_type;
	}

	/**
	 * 단말구분{1-iOS, 2-Android, 9-기타}
	 * @param os_type
	 */
	public void setOs_type(String os_type) {
		this.os_type = os_type;
	}
	
	/**
	 * PUSH ID
	 * @return push_id
	 */
	public String getPush_id() {
		return push_id;
	}

	/**
	 * PUSH ID
	 * @param push_id
	 */
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
	
	/**
	 * 예약안내 코드
	 * @return guide_code
	 */
	public String getGuide_code() {
		return guide_code;
	}

	/**
	 * 예약안내 코드
	 * @param guide_code
	 */
	public void setGuide_code(String guide_code) {
		this.guide_code = guide_code;
	}
	
	/**
	 * 예약안내 버전
	 * @return guide_version
	 */
	public int getGuide_version() {
		return guide_version;
	}

	/**
	 * 예약안내 버전
	 * @param guide_version
	 */
	public void setGuide_version(int guide_version) {
		this.guide_version = guide_version;
	}
	
	/**
	 * 취소/환불 정책 코드
	 * @return cancel_policy_code
	 */
	public String getCancel_policy_code() {
		return cancel_policy_code;
	}

	/**
	 * 취소/환불 정책 코드
	 * @param cancel_policy_code
	 */
	public void setCancel_policy_code(String cancel_policy_code) {
		this.cancel_policy_code = cancel_policy_code;
	}
	
	/**
	 * 취소/환불 정책 버전
	 * @return cancel_policy_version
	 */
	public int getCancel_policy_version() {
		return cancel_policy_version;
	}

	/**
	 * 취소/환불 정책 버전
	 * @param cancel_policy_version
	 */
	public void setCancel_policy_version(int cancel_policy_version) {
		this.cancel_policy_version = cancel_policy_version;
	}
	
	/**
	 * 수령 매장코드
	 * @return receive_store_cd
	 */
	public String getReceive_store_cd() {
		return receive_store_cd;
	}

	/**
	 * 수령 매장코드
	 * @param receive_store_cd
	 */
	public void setReceive_store_cd(String receive_store_cd) {
		this.receive_store_cd = receive_store_cd;
	}
	
	/**
	 * 수령예정일{YYYYMMDD}
	 * @return receive_date
	 */
	public String getReceive_date() {
		return receive_date;
	}

	/**
	 * 수령예정일{YYYYMMDD}
	 * @param receive_date
	 */
	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
	}
	
	/**
	 * 발주확정일{YYYYMMDD}
	 * @return sales_order_date
	 */
	public String getSales_order_date() {
		return sales_order_date;
	}

	/**
	 * 발주확정일{YYYYMMDD}
	 * @param sales_order_date
	 */
	public void setSales_order_date(String sales_order_date) {
		this.sales_order_date = sales_order_date;
	}
	
	/**
	 * 취소가능일{YYYYMMDD}
	 * @return revocable_date
	 */
	public String getRevocable_date() {
		return revocable_date;
	}

	/**
	 * 취소가능일{YYYYMMDD}
	 * @param revocable_date
	 */
	public void setRevocable_date(String revocable_date) {
		this.revocable_date = revocable_date;
	}
	
	/**
	 * 전체 수량
	 * @return total_qty
	 */
	public int getTotal_qty() {
		return total_qty;
	}

	/**
	 * 전체 수량
	 * @param total_qty
	 */
	public void setTotal_qty(int total_qty) {
		this.total_qty = total_qty;
	}
	
	/**
	 * 총 결제금액{총 품목총액 - 총 할인액}
	 * @return total_pay_amt
	 */
	public int getTotal_pay_amt() {
		return total_pay_amt;
	}

	/**
	 * 총 결제금액{총 품목총액 - 총 할인액}
	 * @param total_pay_amt
	 */
	public void setTotal_pay_amt(int total_pay_amt) {
		this.total_pay_amt = total_pay_amt;
	}
	
	/**
	 * 총 할인액
	 * @return total_discount
	 */
	public int getTotal_discount() {
		return total_discount;
	}

	/**
	 * 총 할인액
	 * @param total_discount
	 */
	public void setTotal_discount(int total_discount) {
		this.total_discount = total_discount;
	}
	
	/**
	 * 총 쿠폰액
	 * @return total_coupon
	 */
	public int getTotal_coupon() {
		return total_coupon;
	}

	/**
	 * 총 쿠폰액
	 * @param total_coupon
	 */
	public void setTotal_coupon(int total_coupon) {
		this.total_coupon = total_coupon;
	}
	
	/**
	 * 총 품목총액
	 * @return total_item_amount
	 */
	public int getTotal_item_amount() {
		return total_item_amount;
	}

	/**
	 * 총 품목총액
	 * @param total_item_amount
	 */
	public void setTotal_item_amount(int total_item_amount) {
		this.total_item_amount = total_item_amount;
	}
	
	/**
	 * 현금영수증 발행구분
	 * @return receipt_type
	 */
	public String getReceipt_type() {
		return receipt_type;
	}

	/**
	 * 현금영수증 발행구분
	 * @param receipt_type
	 */
	public void setReceipt_type(String receipt_type) {
		this.receipt_type = receipt_type;
	}
	
	/**
	 * 현금영수증 발행번호{암호화}
	 * @return receipt_number
	 */
	public String getReceipt_number() {
		return receipt_number;
	}

	/**
	 * 현금영수증 발행번호{암호화}
	 * @param receipt_number
	 */
	public void setReceipt_number(String receipt_number) {
		this.receipt_number = receipt_number;
	}
	
	/**
	 * 현금영수증 대상금액
	 * @return receipt_amount
	 */
	public int getReceipt_amount() {
		return receipt_amount;
	}

	/**
	 * 현금영수증 대상금액
	 * @param receipt_amount
	 */
	public void setReceipt_amount(int receipt_amount) {
		this.receipt_amount = receipt_amount;
	}
	
	/**
	 * 직원번호
	 * @return emp_no
	 */
	public String getEmp_no() {
		return emp_no;
	}

	/**
	 * 직원번호
	 * @param emp_no
	 */
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	
	/**
	 * 선물번호
	 * @return present_no
	 */
	public String getPresent_no() {
		return present_no;
	}

	/**
	 * 선물번호
	 * @param present_no
	 */
	public void setPresent_no(String present_no) {
		this.present_no = present_no;
	}
	
	/**
	 * 수신자명
	 * @return receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * 수신자명
	 * @param receiver_name
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	
	/**
	 * 수신자 휴대폰번호{암호화}
	 * @return receiver_mobile
	 */
	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	/**
	 * 수신자 휴대폰번호{암호화}
	 * @param receiver_mobile
	 */
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	
	/**
	 * 선물 메세지
	 * @return present_message
	 */
	public String getPresent_message() {
		return present_message;
	}

	/**
	 * 선물 메세지
	 * @param present_message
	 */
	public void setPresent_message(String present_message) {
		this.present_message = present_message;
	}
	
	/**
	 * 선물 유의사항 코드
	 * @return present_notice_code
	 */
	public String getPresent_notice_code() {
		return present_notice_code;
	}

	/**
	 * 선물 유의사항 코드
	 * @param present_notice_code
	 */
	public void setPresent_notice_code(String present_notice_code) {
		this.present_notice_code = present_notice_code;
	}
	
	/**
	 * 선물 유의사항 버전
	 * @return present_notice_version
	 */
	public int getPresent_notice_version() {
		return present_notice_version;
	}

	/**
	 * 선물 유의사항 버전
	 * @param present_notice_version
	 */
	public void setPresent_notice_version(int present_notice_version) {
		this.present_notice_version = present_notice_version;
	}
	
	/**
	 * 선물 상태{코드표참조}
	 * @return present_status
	 */
	public String getPresent_status() {
		return present_status;
	}

	/**
	 * 선물 상태{코드표참조}
	 * @param present_status
	 */
	public void setPresent_status(String present_status) {
		this.present_status = present_status;
	}
	
	/**
	 * 수령구분{O-주문번호, P-선물번호}
	 * @return receive_type
	 */
	public String getReceive_type() {
		return receive_type;
	}

	/**
	 * 수령구분{O-주문번호, P-선물번호}
	 * @param receive_type
	 */
	public void setReceive_type(String receive_type) {
		this.receive_type = receive_type;
	}
	
	/**
	 * 파트너ID
	 * @return cashier_id
	 */
	public String getCashier_id() {
		return cashier_id;
	}

	/**
	 * 파트너ID
	 * @param cashier_id
	 */
	public void setCashier_id(String cashier_id) {
		this.cashier_id = cashier_id;
	}
	
	/**
	 * 파트너명
	 * @return cashier_name
	 */
	public String getCashier_name() {
		return cashier_name;
	}

	/**
	 * 파트너명
	 * @param cashier_name
	 */
	public void setCashier_name(String cashier_name) {
		this.cashier_name = cashier_name;
	}
	
	/**
	 * 영업일{YYYYMMDD}
	 * @return sale_date
	 */
	public String getSale_date() {
		return sale_date;
	}

	/**
	 * 영업일{YYYYMMDD}
	 * @param sale_date
	 */
	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}
	
	/**
	 * 점포코드
	 * @return store_cd
	 */
	public String getStore_cd() {
		return store_cd;
	}

	/**
	 * 점포코드
	 * @param store_cd
	 */
	public void setStore_cd(String store_cd) {
		this.store_cd = store_cd;
	}
	
	/**
	 * POS번호
	 * @return pos_no
	 */
	public String getPos_no() {
		return pos_no;
	}

	/**
	 * POS번호
	 * @param pos_no
	 */
	public void setPos_no(String pos_no) {
		this.pos_no = pos_no;
	}
	
	/**
	 * 거래번호
	 * @return seq_no
	 */
	public String getSeq_no() {
		return seq_no;
	}

	/**
	 * 거래번호
	 * @param seq_no
	 */
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	
	/**
	 * 매출확정여부{Y-확정}
	 * @return pos_comp_flag
	 */
	public String getPos_comp_flag() {
		return pos_comp_flag;
	}

	/**
	 * 매출확정여부{Y-확정}
	 * @param pos_comp_flag
	 */
	public void setPos_comp_flag(String pos_comp_flag) {
		this.pos_comp_flag = pos_comp_flag;
	}
	
	/**
	 * 발주확정여부{Y-확정}
	 * @return sales_order_comp_flag
	 */
	public String getSales_order_comp_flag() {
		return sales_order_comp_flag;
	}

	/**
	 * 발주확정여부{Y-확정}
	 * @param sales_order_comp_flag
	 */
	public void setSales_order_comp_flag(String sales_order_comp_flag) {
		this.sales_order_comp_flag = sales_order_comp_flag;
	}
	
	/**
	 * 대사완료여부{Y-완료}
	 * @return trade_flag
	 */
	public String getTrade_flag() {
		return trade_flag;
	}

	/**
	 * 대사완료여부{Y-완료}
	 * @param trade_flag
	 */
	public void setTrade_flag(String trade_flag) {
		this.trade_flag = trade_flag;
	}
	
	/**
	 * 오류구분
	 * @return critical_flag
	 */
	public String getCritical_flag() {
		return critical_flag;
	}

	/**
	 * 오류구분
	 * @param critical_flag
	 */
	public void setCritical_flag(String critical_flag) {
		this.critical_flag = critical_flag;
	}
	
	/**
	 * 사용여부{Y-사용, N-미사용}
	 * @return use_flag
	 */
	public String getUse_flag() {
		return use_flag;
	}

	/**
	 * 사용여부{Y-사용, N-미사용}
	 * @param use_flag
	 */
	public void setUse_flag(String use_flag) {
		this.use_flag = use_flag;
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
	 * 결제 완료일자
	 * @return pay_date
	 */
	public Date getPay_date() {
		return pay_date;
	}

	/**
	 * 결제 완료일자
	 * @param pay_date
	 */
	public void setPay_date(Date pay_date) {
		this.pay_date = pay_date;
	}
	
	/**
	 * 예약 변경일자
	 * @return mod_date
	 */
	public Date getMod_date() {
		return mod_date;
	}

	/**
	 * 예약 변경일자
	 * @param mod_date
	 */
	public void setMod_date(Date mod_date) {
		this.mod_date = mod_date;
	}
	
	/**
	 * 수령/미수령 완료일자
	 * @return receive_comp_date
	 */
	public Date getReceive_comp_date() {
		return receive_comp_date;
	}

	/**
	 * 수령/미수령 완료일자
	 * @param receive_comp_date
	 */
	public void setReceive_comp_date(Date receive_comp_date) {
		this.receive_comp_date = receive_comp_date;
	}
	
	/**
	 * 예약 취소일자
	 * @return cancel_date
	 */
	public Date getCancel_date() {
		return cancel_date;
	}

	/**
	 * 예약 취소일자
	 * @param cancel_date
	 */
	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * 수신자 휴대폰번호 삭제일자
	 * @return receiver_mobile_del_date
	 */
	public Date getReceiver_mobile_del_date() {
		return receiver_mobile_del_date;
	}

	/**
	 * 수신자 휴대폰번호 삭제일자
	 * @param receiver_mobile_del_date
	 */
	public void setReceiver_mobile_del_date(Date receiver_mobile_del_date) {
		this.receiver_mobile_del_date = receiver_mobile_del_date;
	}
	
	/**
	 * 쿠폰발행여부{Y-발행}
	 * @return coupon_pub_flag
	 */
	public String getCoupon_pub_flag() {
		return coupon_pub_flag;
	}

	/**
	 * 쿠폰발행여부{Y-발행}
	 * @param coupon_pub_flag
	 */
	public void setCoupon_pub_flag(String coupon_pub_flag) {
		this.coupon_pub_flag = coupon_pub_flag;
	}
	
	/**
	 * 홀케익 예약 정책의 쿠폰 발행 개수 
	 * @return
	 */
	public int getCoupon_count() {
		return coupon_count;
	}

	/**
	 * 홀케익 예약 정책의 쿠폰 발행 개수 
	 * @param coupon_count
	 */
	public void setCoupon_count(int coupon_count) {
		this.coupon_count = coupon_count;
	}

	/**
	 * 홀케익 예약의 총 쿠폰 발행 개수 ((전체 수량-홀케익쿠폰사용수)*홀케익정책의 쿠폰 발행 개수)
	 * @return
	 */
	public int getTotal_coupon_count() {
		return total_coupon_count;
	}

	/**
	 * 홀케익 예약의 총 쿠폰 발행 개수 ((전체 수량-홀케익쿠폰사용수)*홀케익정책의 쿠폰 발행 개수)
	 * @param total_coupon_count
	 */
	public void setTotal_coupon_count(int total_coupon_count) {
		this.total_coupon_count = total_coupon_count;
	}
	
	/**
	 * 발행한 쿠폰 개수
	 * @return
	 */
	public int getPublication_coupon_count() {
		return publication_coupon_count;
	}

	/**
	 * 발행한 쿠폰 개수
	 * @param publication_coupon_count
	 */
	public void setPublication_coupon_count(int publication_coupon_count) {
		this.publication_coupon_count = publication_coupon_count;
	}	
	
	/**
	 * 발주일자
	 * @return
	 */
	public String getOrder_date() {
		return order_date;
	}

	/**
	 * 발주일자
	 * @param order_date
	 */
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	/**
	 * 상세순번
	 * @return
	 */
	public int getItem_seq() {
		return item_seq;
	}

	/**
	 * 상세순번
	 * @param item_seq
	 */
	public void setItem_seq(int item_seq) {
		this.item_seq = item_seq;
	}

	/**
	 * SKU코드
	 * @return
	 */
	public String getSku_no() {
		return sku_no;
	}

	/**
	 * SKU코드
	 * @param sku_no
	 */
	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}

	/**
	 * 단가
	 * @return
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * 단가
	 * @param price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * 수량
	 * @return
	 */
	public int getQty() {
		return qty;
	}

	/**
	 * 수량
	 * @param qty
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * 품목총액(금액*수량)
	 * @return
	 */
	public int getItem_amount() {
		return item_amount;
	}

	/**
	 * 품목총액(금액*수량)
	 * @param item_amount
	 */
	public void setItem_amount(int item_amount) {
		this.item_amount = item_amount;
	}

	/**
	 * 할인액(품목총액 *(직원할인율 *0.1))
	 * @return
	 */
	public int getDiscount() {
		return discount;
	}
	
	/**
	 * 할인액(품목총액 *(직원할인율 *0.1))
	 * @param discount
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}

	/**
	 * 순매출액
	 * @return
	 */
	public int getGnd_amount() {
		return gnd_amount;
	}

	/**
	 * 순매출액
	 * @param gnd_amount
	 */
	public void setGnd_amount(int gnd_amount) {
		this.gnd_amount = gnd_amount;
	}

	/**
	 * 카테고리구분
	 * @return
	 */
	public String getCate_type() {
		return cate_type;
	}

	/**
	 * 카테고리구분
	 * @param cate_type
	 */
	public void setCate_type(String cate_type) {
		this.cate_type = cate_type;
	}

	/**
	 * 쿠폰일련번호
	 * @return
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
	 * 쿠폰할인금액
	 * @return
	 */
	public int getCoupon_dc_amt() {
		return coupon_dc_amt;
	}

	/**
	 * 쿠폰할인금액
	 * @param coupon_dc_amt
	 */
	public void setCoupon_dc_amt(int coupon_dc_amt) {
		this.coupon_dc_amt = coupon_dc_amt;
	}

	/**
	 * 영업정보쿠폰번호
	 * @return
	 */
	public String getSck_coupon_no() {
		return sck_coupon_no;
	}

	
	/**
	 * 영업정보쿠폰번호
	 * @param sck_coupon_no
	 */
	public void setSck_coupon_no(String sck_coupon_no) {
		this.sck_coupon_no = sck_coupon_no;
	}

	/**
	 * 선물재발송여부_YN
	 * @return
	 */
	public String getPresent_resend_yn() {
		return present_resend_yn;
	}

	/**
	 * 선물재발송여부_YN
	 * @param present_resend_yn
	 */
	public void setPresent_resend_yn(String present_resend_yn) {
		this.present_resend_yn = present_resend_yn;
	}

	/**
	 * 쿠폰 발행 및 선물 발송 성공한 쿠폰 목록
	 * @return
	 */
	public List<String> getSuccess_coupon_list() {
		return success_coupon_list;
	}

	/**
	 * 쿠폰 발행 및 선물 발송 성공한 쿠폰 목록
	 * @param success_coupon_list
	 */
	public void setSuccess_coupon_list(List<String> success_coupon_list) {
		this.success_coupon_list = success_coupon_list;
	}

	/**
	 * MMS 선물 발송 실패한 쿠폰 목록 
	 * @return
	 */
	public List<String> getFail_coupon_list() {
		return fail_coupon_list;
	}

	/**
	 * MMS 선물 발송 실패한 쿠폰 목록 
	 * @param fail_coupon_list
	 */
	public void setFail_coupon_list(List<String> fail_coupon_list) {
		this.fail_coupon_list = fail_coupon_list;
	}
	
	
	/**
	 * 수령 매장 명
	 * @return receive_store_name
	 */
	public String getReceive_store_name() {
		return receive_store_name;
	}

	/**
	 * 수령 매장코드
	 * @param receive_store_cd
	 */
	public void setReceive_store_name(String receive_store_name) {
		this.receive_store_name = receive_store_name;
	}
}
