/*
 * @(#) $Id: WholecakeOrderDetailDto.java,v 1.1 2016/11/10 00:55:27 dev99 Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.xo;

/**
 * WholecakeOrderDetailDto - 홀케익 예약 상세
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class WholecakeOrderDetailDto {
	
	private String order_no;                 	// 예약번호
	private int    item_seq;                 	// 상세순번
	private String sku_no;                   	// SKU코드
	private int    price;                    	// 단가
	private int    qty;                      	// 수량
	private int    item_amount;              	// 품목총액{금액 * 수량}
	private int    discount;                 	// 할인액{품목총액 * {직원할인율 * 0.01}}
	private int    gnd_amount;               	// 순매출액{품목총액 - 할인액 - 쿠폰할인금액}
	private String cate_type;                	// 카테고리구분
	private String coupon_number;            	// 쿠폰일련번호
	private String coupon_name;              	// 쿠폰명
	private int    coupon_dc_amt;            	// 쿠폰할인금액
	private String sck_coupon_no;            	// 영업정보 쿠폰번호{e쿠폰-30+COUPON_CODE}
	
	private String file_path;		// 이미지 파일 경로
	private String file_name;		// 이미지 파일 명
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("order_no=").append(this.order_no);
		sb.append(", item_seq=").append(this.item_seq);
		sb.append(", sku_no=").append(this.sku_no);
		sb.append(", price=").append(this.price);
		sb.append(", qty=").append(this.qty);
		sb.append(", item_amount=").append(this.item_amount);
		sb.append(", discount=").append(this.discount);
		sb.append(", gnd_amount=").append(this.gnd_amount);
		sb.append(", cate_type=").append(this.cate_type);
		sb.append(", coupon_number=").append(this.coupon_number);
		sb.append(", coupon_name=").append(this.coupon_name);
		sb.append(", coupon_dc_amt=").append(this.coupon_dc_amt);
		sb.append(", sck_coupon_no=").append(this.sck_coupon_no);
		sb.append(", file_path=").append(this.file_path);
		sb.append(", file_name=").append(this.file_name);
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
	 * 상세순번
	 * @return item_seq
	 */
	public int    getItem_seq() {
		return item_seq;
	}

	/**
	 * 상세순번
	 * @param item_seq
	 */
	public void setItem_seq(int    item_seq) {
		this.item_seq = item_seq;
	}
	
	/**
	 * SKU코드
	 * @return sku_no
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
	 * @return price
	 */
	public int    getPrice() {
		return price;
	}

	/**
	 * 단가
	 * @param price
	 */
	public void setPrice(int    price) {
		this.price = price;
	}
	
	/**
	 * 수량
	 * @return qty
	 */
	public int    getQty() {
		return qty;
	}

	/**
	 * 수량
	 * @param qty
	 */
	public void setQty(int    qty) {
		this.qty = qty;
	}
	
	/**
	 * 품목총액{금액 * 수량}
	 * @return item_amount
	 */
	public int    getItem_amount() {
		return item_amount;
	}

	/**
	 * 품목총액{금액 * 수량}
	 * @param item_amount
	 */
	public void setItem_amount(int    item_amount) {
		this.item_amount = item_amount;
	}
	
	/**
	 * 할인액{품목총액 * {직원할인율 * 0.01}}
	 * @return discount
	 */
	public int    getDiscount() {
		return discount;
	}

	/**
	 * 할인액{품목총액 * {직원할인율 * 0.01}}
	 * @param discount
	 */
	public void setDiscount(int    discount) {
		this.discount = discount;
	}
	
	/**
	 * 순매출액{품목총액 - 할인액 - 쿠폰할인금액}
	 * @return gnd_amount
	 */
	public int    getGnd_amount() {
		return gnd_amount;
	}

	/**
	 * 순매출액{품목총액 - 할인액 - 쿠폰할인금액}
	 * @param gnd_amount
	 */
	public void setGnd_amount(int    gnd_amount) {
		this.gnd_amount = gnd_amount;
	}
	
	/**
	 * 카테고리구분
	 * @return cate_type
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
	 * 쿠폰할인금액
	 * @return coupon_dc_amt
	 */
	public int    getCoupon_dc_amt() {
		return coupon_dc_amt;
	}

	/**
	 * 쿠폰할인금액
	 * @param coupon_dc_amt
	 */
	public void setCoupon_dc_amt(int    coupon_dc_amt) {
		this.coupon_dc_amt = coupon_dc_amt;
	}
	
	/**
	 * 영업정보 쿠폰번호{e쿠폰-30+COUPON_CODE}
	 * @return sck_coupon_no
	 */
	public String getSck_coupon_no() {
		return sck_coupon_no;
	}

	/**
	 * 영업정보 쿠폰번호{e쿠폰-30+COUPON_CODE}
	 * @param sck_coupon_no
	 */
	public void setSck_coupon_no(String sck_coupon_no) {
		this.sck_coupon_no = sck_coupon_no;
	}
	
	/**
	 * 이미지 파일 경로
	 * @return file_path
	 */
	public String getFile_path() {
		return file_path;
	}

	/**
	 * 이미지 파일 경로
	 * @param file_path
	 */
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	/**
	 * 이미지 파일 명
	 * @return file_name
	 */
	public String getFile_name() {
		return file_name;
	}

	/**
	 * 이미지 파일 명
	 * @param file_name
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}
