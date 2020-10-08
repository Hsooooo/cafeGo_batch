/*
 * @(#) $Id: TradeCorrectionDto.java,v 1.4 2018/12/04 01:35:41 yoyo962 Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;


/**
 * 영업정보 거래 대사 TradeCorrectionDto.
 * @author eZEN
 * @since 2014. 2. 6.
 * @version $Revision: 1.4 $
 */
public class TradeCorrectionDto {
	
	private String order_no;    	       // 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}, 홀케익 : 예약번호{"6"+YYYYMMDDHH24MISS+시퀀스5자리}
	private String sale_flag;   	       // 영업정보 거래구분{11-일반매출,19-일반매출취소}
	private String store_no;    	       // 매장번호
	private String pos_no;      	       // 포스번호
	private String sale_date;   	       // 영업일자
	private String appro_tm;    	       // 승인시간
	private String seq_no;      	       // 거래번호
	private String tran_seq_no; 	       // 거래고유번호
	private String user_id;     	       // 사용자아이디
	private String user_name;   	       // 사용자명
	private String status;      	  	   // MSR 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}, 홀케익 (수령완료 : O30, 미수령 폐기 : O31)
	private String receive_type;	  	   // 수령구분{O-주문번호, P-선물번호} 
	private Date   receive_comp_date;      // 수령/미수령 완료일자 
	private String sales_tbl_receive_type; // SAL_PAY_XO에서 확인한 수령구분{O-주문번호, P-선물번호} 
	private String msr_user_flag;			// MSR회원여부{B-비회원, Y-회원, N-웹회원, J-준회원}
	private int total_pay_amt;				// 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액}
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("PaymentDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", sale_flag=").append (this.sale_flag);
		str.append (", store_no=").append (this.store_no);
		str.append (", pos_no=").append (this.pos_no);
		str.append (", sale_date=").append (this.sale_date);
		str.append (", appro_tm=").append (this.appro_tm);
		str.append (", seq_no=").append (this.seq_no);
		str.append (", tran_seq_no=").append (this.tran_seq_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", user_name=").append (this.user_name);
		str.append (", status=").append (this.status);
		str.append (", receive_type=").append (this.receive_type);
		str.append (", receive_comp_date=").append (this.receive_comp_date);
		str.append (", sales_tbl_receive_type=").append (this.sales_tbl_receive_type);
		str.append (", msr_user_flag=").append (this.msr_user_flag);
		str.append (", total_pay_amt=").append (String.valueOf(this.total_pay_amt));
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * 홀케익 : 예약번호{"6"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호{"3"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * 홀케익 : 예약번호{"6"+YYYYMMDDHH24MISS+시퀀스5자리}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}

	/**
	 * 영업정보 거래구분{11-일반매출,19-일반매출취소}
	 * @return sale_flag
	 */
	public String getSale_flag() {
		return sale_flag;
	}

	/**
	 * 영업정보 거래구분{11-일반매출,19-일반매출취소}
	 * @param sale_flag
	 */
	public void setSale_flag(String sale_flag) {
		this.sale_flag = sale_flag;
	}

	/**
	 * 매장번호
	 * @return store_no
	 */
	public String getStore_no() {
		return store_no;
	}

	/**
	 * 매장번호
	 * @param store_no
	 */
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}

	/**
	 * 포스번호
	 * @return pos_no
	 */
	public String getPos_no() {
		return pos_no;
	}

	/**
	 * 포스번호
	 * @param pos_no
	 */
	public void setPos_no(String pos_no) {
		this.pos_no = pos_no;
	}

	/**
	 * 영업일자
	 * @return sale_date
	 */
	public String getSale_date() {
		return sale_date;
	}

	/**
	 * 영업일자
	 * @param sale_date
	 */
	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}

	/**
	 * 승인시간
	 * @return appro_tm
	 */
	public String getAppro_tm() {
		return appro_tm;
	}

	/**
	 * 승인시간
	 * @param appro_tm
	 */
	public void setAppro_tm(String appro_tm) {
		this.appro_tm = appro_tm;
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
	 * 거래고유번호
	 * @return tran_seq_no
	 */
	public String getTran_seq_no() {
		return tran_seq_no;
	}

	/**
	 * 거래고유번호
	 * @param tran_seq_no
	 */
	public void setTran_seq_no(String tran_seq_no) {
		this.tran_seq_no = tran_seq_no;
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
	 * MSR 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}
	 * 홀케익 (수령완료 : O30, 미수령 폐기 : O31)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * MSR 상태{10-결제 요청, 11-결제 완료, 12-결제 취소, 13-결제 취소 실패, 14-결제 취소 부분 실패, 15-결제 실패, 20-주문 요청, 21-주문 승인, 22-주문 취소, 23-주문 취소 실패, 24-주문 매출 확정(이력), 25-취소 매출 확정(이력), 30-주문완료(nonBDS), 31-제조완료(BDS)}
	 * 홀케익 (수령완료 : O30, 미수령 폐기 : O31)
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 수령구분{O-주문번호, P-선물번호}
	 * @return
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
	 * 수령/미수령 완료일자
	 * @return
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
	 * SAL_PAY_XO에서 확인한 수령구분{O-주문번호, P-선물번호}
	 * @return
	 */
	public String getSales_tbl_receive_type() {
		return sales_tbl_receive_type;
	}

	/**
	 * SAL_PAY_XO에서 확인한 수령구분{O-주문번호, P-선물번호}
	 * @param sales_tbl_receive_type
	 */
	public void setSales_tbl_receive_type(String sales_tbl_receive_type) {
		this.sales_tbl_receive_type = sales_tbl_receive_type;
	}

	/**
	 * MSR회원여부{B-비회원, Y-회원, N-웹회원, J-준회원}
	 * @return the msr_user_flag
	 */
	public String getMsr_user_flag() {
		return msr_user_flag;
	}

	/**
	 * MSR회원여부{B-비회원, Y-회원, N-웹회원, J-준회원}
	 * @param msr_user_flag the msr_user_flag to set
	 */
	public void setMsr_user_flag(String msr_user_flag) {
		this.msr_user_flag = msr_user_flag;
	}

	/**
	 * 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액}
	 * @return the total_pay_amt
	 */
	public int getTotal_pay_amt() {
		return total_pay_amt;
	}

	/**
	 * 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액}
	 * @param total_pay_amt the total_pay_amt to set
	 */
	public void setTotal_pay_amt(int total_pay_amt) {
		this.total_pay_amt = total_pay_amt;
	}
	
	
}
