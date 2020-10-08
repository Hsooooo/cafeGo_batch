/*
 * @(#) $Id: OrderDto.java,v 1.2 2018/10/01 06:55:14 iamjihun Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * 주문정보 OrderDto.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.2 $
 */
public class OrderDto {
	
	private String order_no; // 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	private String cart_no; // 주문서일련번호{"2"+YYYYMMDDHH24MISS+시퀀스5자리}
	private String user_id; // 사용자아이디
	private String user_name; // 사용자명
	private String status; // 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}
	private String payment_kind; // 결제구분{1-APP, 2-POS}
	private String order_kind; // 주문구분{1-APP, 2-POS}
	private Date reg_date; // 등록일
	private String order_name; // 주문명
	private String msr_user_flag; // MSR회원여부{Y-회원, N-비회원}
	private String push_id; // PUSH ID
	private Long total_pay_amt; // 총 결제금액
	private Integer total_discount; // 총 할인액
	private Long total_item_amount; // 총 품목총액
	private String take_out_flag; // 주문형태{Y-TAKE OUT, N-HERE IN}
	private String packing_flag; // 포장여부{Y-포장, N-미포장}
	private String freq_barcode; // 프리퀀시 바코드{암호화}
	private Date mod_date; // 변경일
	private Date pay_date; // 결제완료일자
	private Date order_date; // 주문완료일자
	private String sale_date; // 영업일{YYYYMMDD}
	private String store_cd; // 점포코드
	private String pos_no; // POS번호
	private String seq_no; // 거래번호
	private String receipt_type; // 현금영수증 발행구분{H-휴대폰 번호, C-개인국세청카드, K-다른사람휴대폰번호, V-사업자등록번호, D-법인국세청카드, A-발행문의안함, E-설정하지않음}
	private String receipt_number; // 현금영수증 발행번호{암호화}
	private Integer receipt_amount; // 현금영수증 대상금액
	private String msr_user_grade; // MSR회원등급
	private String tran_seq_no; // POS 승인번호
	private String cashier_id; // 파트너ID
	private String cancel_reason; // 주문 취소 사유
	private String phone; // 휴대폰번호{암호화}
	private Integer total_qty; // 전체수량
	private String gstdgr; // 객층코드
	private String bankcd; // POS구분
	private String os_type; // 단말구분{1-iOS, 2-Android}
	private String pos_comp_flag; // 매출확정여부{Y-확정, N-미확정}
	
	private String store_name; // 매장명
	private Integer payment_count; // 결제된 주문 건수
	private String check_status_flag; // 해당 주문 상태인지 여부 {Y/N}
    private String emp_no;  // 임직원 번호
    private String emp_auth_app_no; // 임직원 할인 승인번호

    @Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("OrderDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", cart_no=").append (this.cart_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", user_name=").append (this.user_name);
		str.append (", status=").append (this.status);
		str.append (", payment_kind=").append (this.payment_kind);
		str.append (", order_kind=").append (this.order_kind);
		str.append (", reg_date=").append (this.reg_date);
		str.append (", order_name=").append (this.order_name);
		str.append (", msr_user_flag=").append (this.msr_user_flag);
		str.append (", push_id=").append (this.push_id);
		str.append (", total_pay_amt=").append (this.total_pay_amt);
		str.append (", total_discount=").append (this.total_discount);
		str.append (", total_item_amount=").append (this.total_item_amount);
		str.append (", take_out_flag=").append (this.take_out_flag);
		str.append (", packing_flag=").append (this.packing_flag);
		str.append (", freq_barcode=").append (this.freq_barcode);
		str.append (", mod_date=").append (this.mod_date);
		str.append (", pay_date=").append (this.pay_date);
		str.append (", order_date=").append (this.order_date);
		str.append (", sale_date=").append (this.sale_date);
		str.append (", store_cd=").append (this.store_cd);
		str.append (", pos_no=").append (this.pos_no);
		str.append (", seq_no=").append (this.seq_no);
		str.append (", receipt_type=").append (this.receipt_type);
		str.append (", receipt_number=").append (this.receipt_number);
		str.append (", receipt_amount=").append (this.receipt_amount);
		str.append (", msr_user_grade=").append (this.msr_user_grade);
		str.append (", tran_seq_no=").append (this.tran_seq_no);
		str.append (", cashier_id=").append (this.cashier_id);
		str.append (", cancel_reason=").append (this.cancel_reason);
		str.append (", phone=").append (this.phone);
		str.append (", total_qty=").append (this.total_qty);
		str.append (", gstdgr=").append (this.gstdgr);
		str.append (", bankcd=").append (this.bankcd);
		str.append (", os_type=").append (this.os_type);
		str.append (", pos_comp_flag=").append (this.pos_comp_flag);
		
		str.append (", store_name=").append (this.store_name);
		str.append (", payment_count=").append (this.payment_count);
		str.append (", check_status_flag=").append (this.check_status_flag);
		
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * 주문번호{""3""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호{""3""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * 주문서일련번호{""2""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @return cart_no
	 */
	public String getCart_no () {
		return cart_no;
	}
	
	/**
	 * 주문서일련번호{""2""+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @param cart_no
	 */
	public void setCart_no ( String cart_no ) {
		this.cart_no = cart_no;
	}
	
	/**
	 * 사용자아이디
	 * @return user_id
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * 사용자아이디
	 * @param user_id
	 */
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * 사용자명
	 * @return user_name
	 */
	public String getUser_name () {
		return user_name;
	}
	
	/**
	 * 사용자명
	 * @param user_name
	 */
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	/**
	 * 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}
	 * @return status
	 */
	public String getStatus () {
		return status;
	}
	
	/**
	 * 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}
	 * @param status
	 */
	public void setStatus ( String status ) {
		this.status = status;
	}
	
	/**
	 * 결제구분{1-APP, 2-POS}
	 * @return payment_kind
	 */
	public String getPayment_kind () {
		return payment_kind;
	}
	
	/**
	 * 결제구분{1-APP, 2-POS}
	 * @param payment_kind
	 */
	public void setPayment_kind ( String payment_kind ) {
		this.payment_kind = payment_kind;
	}
	
	/**
	 * 주문구분{1-APP, 2-POS}
	 * @return order_kind
	 */
	public String getOrder_kind () {
		return order_kind;
	}
	
	/**
	 * 주문구분{1-APP, 2-POS}
	 * @param order_kind
	 */
	public void setOrder_kind ( String order_kind ) {
		this.order_kind = order_kind;
	}
	
	/**
	 * 등록일
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 등록일
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 주문명
	 * @return order_name
	 */
	public String getOrder_name () {
		return order_name;
	}
	
	/**
	 * 주문명
	 * @param order_name
	 */
	public void setOrder_name ( String order_name ) {
		this.order_name = order_name;
	}
	
	/**
	 * MSR회원여부{Y-회원, N-비회원}
	 * @return msr_user_flag
	 */
	public String getMsr_user_flag () {
		return msr_user_flag;
	}
	
	/**
	 * MSR회원여부{Y-회원, N-비회원}
	 * @param msr_user_flag
	 */
	public void setMsr_user_flag ( String msr_user_flag ) {
		this.msr_user_flag = msr_user_flag;
	}
	
	/**
	 * PUSH ID
	 * @return push_id
	 */
	public String getPush_id () {
		return push_id;
	}
	
	/**
	 * PUSH ID
	 * @param push_id
	 */
	public void setPush_id ( String push_id ) {
		this.push_id = push_id;
	}
	
	/**
	 * 총 결제금액
	 * @return total_pay_amt
	 */
	public Long getTotal_pay_amt () {
		return total_pay_amt;
	}
	
	/**
	 * 총 결제금액
	 * @param total_pay_amt
	 */
	public void setTotal_pay_amt ( Long total_pay_amt ) {
		this.total_pay_amt = total_pay_amt;
	}
	
	/**
	 * 총 할인액
	 * @return total_discount
	 */
	public Integer getTotal_discount () {
		return total_discount == null ? 0 : total_discount;
	}
	
	/**
	 * 총 할인액
	 * @param total_discount
	 */
	public void setTotal_discount ( Integer total_discount ) {
		this.total_discount = total_discount;
	}
	
	/**
	 * 총 품목총액
	 * @return total_item_amount
	 */
	public Long getTotal_item_amount () {
		return total_item_amount;
	}
	
	/**
	 * 총 품목총액
	 * @param total_item_amount
	 */
	public void setTotal_item_amount ( Long total_item_amount ) {
		this.total_item_amount = total_item_amount;
	}
	
	/**
	 * 주문형태{Y-TAKE OUT, N-HERE IN}
	 * @return take_out_flag
	 */
	public String getTake_out_flag () {
		return take_out_flag;
	}
	
	/**
	 * 주문형태{Y-TAKE OUT, N-HERE IN}
	 * @param take_out_flag
	 */
	public void setTake_out_flag ( String take_out_flag ) {
		this.take_out_flag = take_out_flag;
	}
	
	/**
	 * 포장여부{Y-포장, N-미포장}
	 * @return packing_flag
	 */
	public String getPacking_flag () {
		return packing_flag;
	}
	
	/**
	 * 포장여부{Y-포장, N-미포장}
	 * @param packing_flag
	 */
	public void setPacking_flag ( String packing_flag ) {
		this.packing_flag = packing_flag;
	}
	
	/**
	 * 프리퀀시 바코드{암호화}
	 * @return freq_barcode
	 */
	public String getFreq_barcode () {
		return freq_barcode;
	}
	
	/**
	 * 프리퀀시 바코드{암호화}
	 * @param freq_barcode
	 */
	public void setFreq_barcode ( String freq_barcode ) {
		this.freq_barcode = freq_barcode;
	}
	
	/**
	 * 변경일
	 * @return mod_date
	 */
	public Date getMod_date () {
		return mod_date;
	}
	
	/**
	 * 변경일
	 * @param mod_date
	 */
	public void setMod_date ( Date mod_date ) {
		this.mod_date = mod_date;
	}
	
	/**
	 * 결제완료일자
	 * @return pay_date
	 */
	public Date getPay_date () {
		return pay_date;
	}
	
	/**
	 * 결제완료일자
	 * @param pay_date
	 */
	public void setPay_date ( Date pay_date ) {
		this.pay_date = pay_date;
	}
	
	/**
	 * 주문완료일자
	 * @return order_date
	 */
	public Date getOrder_date () {
		return order_date;
	}
	
	/**
	 * 주문완료일자
	 * @param order_date
	 */
	public void setOrder_date ( Date order_date ) {
		this.order_date = order_date;
	}
	
	/**
	 * 영업일{YYYYMMDD}
	 * @return sale_date
	 */
	public String getSale_date () {
		return sale_date;
	}
	
	/**
	 * 영업일{YYYYMMDD}
	 * @param sale_date
	 */
	public void setSale_date ( String sale_date ) {
		this.sale_date = sale_date;
	}
	
	/**
	 * 점포코드
	 * @return store_cd
	 */
	public String getStore_cd () {
		return store_cd;
	}
	
	/**
	 * 점포코드
	 * @param store_cd
	 */
	public void setStore_cd ( String store_cd ) {
		this.store_cd = store_cd;
	}
	
	/**
	 * POS번호
	 * @return pos_no
	 */
	public String getPos_no () {
		return pos_no;
	}
	
	/**
	 * POS번호
	 * @param pos_no
	 */
	public void setPos_no ( String pos_no ) {
		this.pos_no = pos_no;
	}
	
	/**
	 * 거래번호
	 * @return seq_no
	 */
	public String getSeq_no () {
		return seq_no;
	}
	
	/**
	 * 거래번호
	 * @param seq_no
	 */
	public void setSeq_no ( String seq_no ) {
		this.seq_no = seq_no;
	}
	
	/**
	 * 현금영수증 발행구분{H-휴대폰 번호, C-개인국세청카드, K-다른사람휴대폰번호, V-사업자등록번호, D-법인국세청카드, A-발행문의안함, E-설정하지않음}
	 * @return receipt_type
	 */
	public String getReceipt_type () {
		return receipt_type;
	}
	
	/**
	 * 현금영수증 발행구분{H-휴대폰 번호, C-개인국세청카드, K-다른사람휴대폰번호, V-사업자등록번호, D-법인국세청카드, A-발행문의안함, E-설정하지않음}
	 * @param receipt_type
	 */
	public void setReceipt_type ( String receipt_type ) {
		this.receipt_type = receipt_type;
	}
	
	/**
	 * 현금영수증 발행번호{암호화}
	 * @return receipt_number
	 */
	public String getReceipt_number () {
		return receipt_number;
	}
	
	/**
	 * 현금영수증 발행번호{암호화}
	 * @param receipt_number
	 */
	public void setReceipt_number ( String receipt_number ) {
		this.receipt_number = receipt_number;
	}
	
	/**
	 * 현금영수증 대상금액
	 * @return receipt_amount
	 */
	public Integer getReceipt_amount () {
		return receipt_amount == null ? 0 : receipt_amount;
	}
	
	/**
	 * 현금영수증 대상금액
	 * @param receipt_amount
	 */
	public void setReceipt_amount ( Integer receipt_amount ) {
		this.receipt_amount = receipt_amount;
	}
	
	/**
	 * MSR회원등급
	 * @return msr_user_grade
	 */
	public String getMsr_user_grade () {
		return msr_user_grade;
	}
	
	/**
	 * MSR회원등급
	 * @param msr_user_grade
	 */
	public void setMsr_user_grade ( String msr_user_grade ) {
		this.msr_user_grade = msr_user_grade;
	}
	
	/**
	 * POS 승인번호
	 * @return tran_seq_no
	 */
	public String getTran_seq_no () {
		return tran_seq_no;
	}
	
	/**
	 * POS 승인번호
	 * @param tran_seq_no
	 */
	public void setTran_seq_no ( String tran_seq_no ) {
		this.tran_seq_no = tran_seq_no;
	}
	
	/**
	 * 파트너ID
	 * @return cashier_id
	 */
	public String getCashier_id () {
		return cashier_id;
	}
	
	/**
	 * 파트너ID
	 * @param cashier_id
	 */
	public void setCashier_id ( String cashier_id ) {
		this.cashier_id = cashier_id;
	}
	
	/**
	 * 주문 취소 사유
	 * @return cancel_reason
	 */
	public String getCancel_reason () {
		return cancel_reason;
	}
	
	/**
	 * 주문 취소 사유
	 * @param cancel_reason
	 */
	public void setCancel_reason ( String cancel_reason ) {
		this.cancel_reason = cancel_reason;
	}
	
	/**
	 * 휴대폰번호{암호화}
	 * @return phone
	 */
	public String getPhone () {
		return phone;
	}
	
	/**
	 * 휴대폰번호{암호화}
	 * @param phone
	 */
	public void setPhone ( String phone ) {
		this.phone = phone;
	}
	
	/**
	 * 전체수량
	 * @return total_qty
	 */
	public Integer getTotal_qty () {
		return total_qty == null ? 0 : total_qty;
	}
	
	/**
	 * 전체수량
	 * @param total_qty
	 */
	public void setTotal_qty ( Integer total_qty ) {
		this.total_qty = total_qty;
	}
	
	/**
	 * 객층코드
	 * @return gstdgr
	 */
	public String getGstdgr () {
		return gstdgr;
	}
	
	/**
	 * 객층코드
	 * @param gstdgr
	 */
	public void setGstdgr ( String gstdgr ) {
		this.gstdgr = gstdgr;
	}
	
	/**
	 * POS구분
	 * @return bankcd
	 */
	public String getBankcd () {
		return bankcd;
	}
	
	/**
	 * POS구분
	 * @param bankcd
	 */
	public void setBankcd ( String bankcd ) {
		this.bankcd = bankcd;
	}
	
	/**
	 * 단말구분{1-iOS, 2-Android}
	 * @return os_type
	 */
	public String getOs_type () {
		return os_type;
	}
	
	/**
	 * 단말구분{1-iOS, 2-Android}
	 * @param os_type
	 */
	public void setOs_type ( String os_type ) {
		this.os_type = os_type;
	}
	
	/**
	 * 매출확정여부{Y-확정, N-미확정}
	 * @return pos_comp_flag
	 */
	public String getPos_comp_flag () {
		return pos_comp_flag;
	}
	
	/**
	 * 매출확정여부{Y-확정, N-미확정}
	 * @param pos_comp_flag
	 */
	public void setPos_comp_flag ( String pos_comp_flag ) {
		this.pos_comp_flag = pos_comp_flag;
	}
	
	
	/**
	 * 매장명
	 * @return store_name
	 */
	public String getStore_name () {
		return store_name;
	}
	
	/**
	 * 매장명
	 * @param store_name
	 */
	public void setStore_name ( String store_name ) {
		this.store_name = store_name;
	}
	
	/**
	 * 결제된 주문 건수
	 * @return payment_count
	 */
	public Integer getPayment_count () {
		return payment_count == null ? 0 : payment_count;
	}
	
	/**
	 * 결제된 주문 건수
	 * @param payment_count
	 */
	public void setPayment_count ( Integer payment_count ) {
		this.payment_count = payment_count;
	}
	
	/**
	 * 해당 주문 상태인지 여부 {Y/N}
	 * @return check_status_flag
	 */
	public String getCheck_status_flag () {
		return check_status_flag;
	}
	
	/**
	 * 해당 주문 상태인지 여부 {Y/N}
	 * @param check_status_flag
	 */
	public void setCheck_status_flag ( String check_status_flag ) {
		this.check_status_flag = check_status_flag;
	}

    /**
     * 임직원 번호
     */
    public String getEmp_no() {
        return emp_no;
    }

    /**
     * 임직원 번호
     */
    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    /**
     * 임직원 할인 승인 번호
     */
    public String getEmp_auth_app_no() {
        return emp_auth_app_no;
    }

    /**
     * 임직원 할인 승인 번호
     */
    public void setEmp_auth_app_no(String emp_auth_app_no) {
        this.emp_auth_app_no = emp_auth_app_no;
    }
}
