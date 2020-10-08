/*
 * @(#) $Id: OrderCouponDto.java,v 1.1 2015/09/11 08:13:37 soonwoo Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2015 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, Digitalro 306, Guro-gu,
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.dto.xo;

/**
 * TODO Insert type comment for OrderCouponDto.
 *
 * @author sw.Lee
 * @version $Revision: 1.1 $
 */
public class OrderCouponDto {
	private String  order_no;			//주문번호
	private String  user_id;			//사용자아이디
	private Integer item_seq;			//상품순번
	private Integer seq_sub_no;			//부모 상품순번
	private String  coupon_number;		//쿠폰 번호
	private int     coupon_dc_amt;		//쿠폰 금액
	private Integer cond_item_seq;		//쿠폰조건상품 상품순번{BOGO쿠폰일 경우만 필수}
	private String  cate_type;			//카테고리구분{01-음료, 02-FOOD, 03-MD}
	private String  cup_type;			//컵구분{0-머그, 1-일회용}
	
	
	/**
	 * 주문번호
	 * @return
	 */
	public String getOrder_no() {
		return order_no;
	}
	/**
	 * 주문번호
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	/**
	 * 사용자아이디
	 * @return
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
	 * 상품순번
	 * @return
	 */
	public Integer getItem_seq() {
		return item_seq;
	}
	/**
	 * 상품순번
	 * @param item_seq
	 */
	public void setItem_seq(Integer item_seq) {
		this.item_seq = item_seq;
	}
	/**
	 * 부모 상품순번
	 * @return
	 */
	public Integer getSeq_sub_no() {
		return seq_sub_no;
	}
	/**
	 * 부모 상품순번
	 * @param seq_sub_no
	 */
	public void setSeq_sub_no(Integer seq_sub_no) {
		this.seq_sub_no = seq_sub_no;
	}
	/**
	 * 쿠폰 번호
	 * @return
	 */
	public String getCoupon_number() {
		return coupon_number;
	}
	/**
	 * 쿠폰 번호
	 * @param coupon_number
	 */
	public void setCoupon_number(String coupon_number) {
		this.coupon_number = coupon_number;
	}
	/**
	 * 쿠폰 금액
	 * @return
	 */
	public int getCoupon_dc_amt() {
		return coupon_dc_amt;
	}
	/**
	 * 쿠폰 금액
	 * @param coupon_dc_amt
	 */
	public void setCoupon_dc_amt(int coupon_dc_amt) {
		this.coupon_dc_amt = coupon_dc_amt;
	}
	/**
	 * 쿠폰조건상품 상품순번{BOGO쿠폰일 경우만 필수}
	 * @return
	 */
	public Integer getCond_item_seq() {
		return cond_item_seq;
	}
	/**
	 * 쿠폰조건상품 상품순번{BOGO쿠폰일 경우만 필수}
	 * @param cond_item_seq
	 */
	public void setCond_item_seq(Integer cond_item_seq) {
		this.cond_item_seq = cond_item_seq;
	}
	/**
	 * 카테고리구분{01-음료, 02-FOOD, 03-MD}
	 * @return
	 */
	public String getCate_type() {
		return cate_type;
	}
	/**
	 * 카테고리구분{01-음료, 02-FOOD, 03-MD}
	 * @param cate_type
	 */
	public void setCate_type(String cate_type) {
		this.cate_type = cate_type;
	}
	/**
	 * 컵구분{0-머그, 1-일회용}
	 * @return
	 */
	public String getCup_type() {
		return cup_type;
	}
	/**
	 * 컵구분{0-머그, 1-일회용}
	 * @param cup_type
	 */
	public void setCup_type(String cup_type) {
		this.cup_type = cup_type;
	}
	
	@Override
	public String toString() {
		return "OrderCouponDto [order_no=" + order_no + ", user_id=" + user_id
				+ ", item_seq=" + item_seq + ", seq_sub_no=" + seq_sub_no
				+ ", coupon_number=" + coupon_number + ", coupon_dc_amt="
				+ coupon_dc_amt + ", cond_item_seq=" + cond_item_seq
				+ ", cate_type=" + cate_type + ", cup_type=" + cup_type + "]";
	}
}
