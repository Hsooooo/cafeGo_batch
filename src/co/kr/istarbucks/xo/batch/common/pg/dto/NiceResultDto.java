/*
 * @(#) $Id: NiceResultDto.java,v 1.1 2015/11/24 03:59:02 soonwoo Exp $
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
package co.kr.istarbucks.xo.batch.common.pg.dto;

import org.apache.commons.lang.StringUtils;

/**
 * TODO Insert type comment for NiceResultDto.
 *
 * @author sw.Lee
 * @version $Revision: 1.1 $
 */
public class NiceResultDto {
	/* ===== 공통 데이터 관련 ===== */
	private String serviceMode;			//서비스모드{PY0:결제, CL0:취소}
	private String moid;				//주문번호{ORDER_NO}
	private String mid;					//상점ID
	private String tid;					//TID
	private String payMethod;			//결제수단(지불수단)
	private String resultCode;			//결과코드{결제시->3001:카드결제성공, 그 외:에러,  취소시->2001:취소성공, 2002:취소진행중, 그 외:에러}
	private String resultMsg;			//결과메시지
	/* ===== 공통 데이터 관련 ===== */
	
	/* ===== 결제승인 데이터 관련 ===== */
	private String goodsName;			//상품명
	private String amt;					//금액
	private String buyerName;			//구매자 명
	private String buyerTel;			//구매자 연락처
	private String buyerEmail;			//구매자 이메일
	private String authCode;			//승인번호
	private String authDate;			//승인날짜{YYMMDDHH24MISS}
	private String cardNo;				//카드번호
	private String cardCode;			//카드코드
	private String cardName;			//카드사명
	private String orderNo;				//주문번호
	private String userId;				//사용자ID
	private String paymentOrder;		//사이렌오더 결제순번
	/* ===== 결제승인 데이터 관련 ===== */
	
	/* ===== 취소 데이터 관련 ===== */
	private String cancelAmt;			//취소금액
	private String cancelDate;			//취소일
	private String cancelTime;			//취소시간
	private String cancelNum;			//취소번호
	/* ===== 취소 데이터 관련 ===== */
	
	
	/* ===== 공통 데이터 관련 ===== */
	/**
	 * 서비스모드{PY0:결제, CL0:취소}
	 * @return
	 */
	public String getServiceMode() {
		return serviceMode;
	}
	/**
	 * 서비스모드{PY0:결제, CL0:취소}
	 * @param serviceMode
	 */
	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
	}
	/**
	 * 주문번호{ORDER_NO}
	 * @return
	 */
	public String getMoid() {
		return moid;
	}
	/**
	 * 주문번호{ORDER_NO}
	 * @param moid
	 */
	public void setMoid(String moid) {
		this.moid = moid;
	}
	/**
	 * 상점ID
	 * @return
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * 상점ID
	 * @param mid
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * TID
	 * @return
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * TID
	 * @param tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * 결제수단(지불수단)
	 * @return
	 */
	public String getPayMethod() {
		return payMethod;
	}
	/**
	 * 결제수단(지불수단)
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * 결과코드{결제시->3001:카드결제성공, 그 외:에러,  취소시->2001:취소성공, 2002:취소진행중, 그 외:에러}
	 * @return
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * 결과코드{결제시->3001:카드결제성공, 그 외:에러,  취소시->2001:취소성공, 2002:취소진행중, 그 외:에러}
	 * @param resultCode
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * 결과메시지
	 * @return
	 */
	public String getResultMsg() {
		return resultMsg;
	}
	/**
	 * 결과메시지
	 * @param resultMsg
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	/* ===== 공통 데이터 관련 ===== */
	
	
	/* ===== 결제승인 데이터 관련 ===== */
	/**
	 * 상품명
	 * @return
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * 상품명
	 * @param goodsName
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * 금액
	 * @return
	 */
	public String getAmt() {
		return amt;
	}
	/**
	 * 금액
	 * @param amt
	 */
	public void setAmt(String amt) {
		this.amt = amt;
	}
	/**
	 * 구매자 명
	 * @return
	 */
	public String getBuyerName() {
		return buyerName;
	}
	/**
	 * 구매자 명
	 * @param buyerName
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	/**
	 * 구매자 연락처
	 * @return
	 */
	public String getBuyerTel() {
		return buyerTel;
	}
	/**
	 * 구매자 연락처
	 * @param buyerTel
	 */
	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}
	/**
	 * 구매자 이메일
	 * @return
	 */
	public String getBuyerEmail() {
		return buyerEmail;
	}
	/**
	 * 구매자 이메일
	 * @param buyerEmail
	 */
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
	/**
	 * 승인번호
	 * @return
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * 승인번호
	 * @param authCode
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	/**
	 * 승인날짜{YYMMDDHH24MISS}
	 * @return
	 */
	public String getAuthDate() {
		return authDate;
	}
	/**
	 * 승인날짜{YYMMDDHH24MISS}
	 * @param authDate
	 */
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	/**
	 * 카드번호
	 * @return
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * 카드번호
	 * @param cardNo
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * 카드코드
	 * @return
	 */
	public String getCardCode() {
		return cardCode;
	}
	/**
	 * 카드코드
	 * @param cardCode
	 */
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	/**
	 * 카드사명
	 * @return
	 */
	public String getCardName() {
		return cardName;
	}
	/**
	 * 카드사명
	 * @param cardName
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	/**
	 * 주문번호
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 주문번호
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 사용자ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 사용자ID
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 사이렌오더 결제순번
	 * @return
	 */
	public String getPaymentOrder() {
		return paymentOrder;
	}
	/**
	 * 사이렌오더 결제순번
	 * @param paymentOrder
	 */
	public void setPaymentOrder(String paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
	/* ===== 결제승인 데이터 관련 ===== */
	
	
	/* ===== 취소 데이터 관련 ===== */
	/**
	 * 취소금액
	 * @return
	 */
	public String getCancelAmt() {
		return cancelAmt;
	}
	/**
	 * 취소금액
	 * @param cancelAmt
	 */
	public void setCancelAmt(String cancelAmt) {
		this.cancelAmt = cancelAmt;
	}
	/**
	 * 취소일
	 * @return
	 */
	public String getCancelDate() {
		return cancelDate;
	}
	/**
	 * 취소일
	 * @param cancelDate
	 */
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	/**
	 * 취소시간
	 * @return
	 */
	public String getCancelTime() {
		return cancelTime;
	}
	/**
	 * 취소시간
	 * @param cancelTime
	 */
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	/**
	 * 취소번호
	 * @return
	 */
	public String getCancelNum() {
		return cancelNum;
	}
	/**
	 * 취소번호
	 * @param cancelNum
	 */
	public void setCancelNum(String cancelNum) {
		this.cancelNum = cancelNum;
	}
	/* ===== 취소 데이터 관련 ===== */


	/**
	 * 시스템용 결과코드{00:성공, 그외:실패}
	 * @return
	 */
	public String getModResultCode() {
		String modRtCode = this.resultCode;
		
		//결제
		if(StringUtils.equals(this.serviceMode, "PY0")){
			//신용카드
			if(StringUtils.equals(this.payMethod, "CARD") && StringUtils.equals(this.resultCode, "3001")){
				modRtCode = "00";
			//계좌이체
			}else if(StringUtils.equals(this.payMethod, "BANK") && StringUtils.equals(this.resultCode, "4000")){
				modRtCode = "00";
			//휴대폰
			}else if(StringUtils.equals(this.payMethod, "CELLPHONE") && StringUtils.equals(this.resultCode, "A000")){
				modRtCode = "00";
			//가상계좌
			}else if(StringUtils.equals(this.payMethod, "VBANK") && StringUtils.equals(this.resultCode, "4100")){
				modRtCode = "00";
			}
		//취소
		}else if(StringUtils.equals(this.serviceMode, "CL0")){
			if(StringUtils.equals(this.resultCode, "2001")){
				modRtCode = "00";
			}
		}
		
		return modRtCode;
	}
	
	public String toPayString() {
		return "PAY:NiceResultDto [serviceMode=" + serviceMode + ", moid=" + moid + ", mid=" + mid + ", tid=" + tid
				+ ", payMethod=" + payMethod + ", resultCode=" + resultCode+"(" + getModResultCode()+")"
				+ ", resultMsg=" + resultMsg + ", goodsName=" + goodsName
				+ ", amt=" + amt + ", buyerName=" + buyerName + ", buyerTel="
				+ buyerTel + ", buyerEmail=" + buyerEmail + ", authCode="
				+ authCode + ", authDate=" + authDate + ", cardNo=" + cardNo
				+ ", cardCode=" + cardCode + ", cardName=" + cardName
				+ ", orderNo=" + orderNo + ", userId=" + userId + ", paymentOrder=" + paymentOrder + "]";
	}
	
	public String toCancelString() {
		return "CANCEL:NiceResultDto [serviceMode=" + serviceMode + ", moid=" + moid + ", mid=" + mid + ", tid=" + tid
				+ ", payMethod=" + payMethod + ", resultCode=" + resultCode+"(" + getModResultCode()+")"
				+ ", resultMsg=" + resultMsg + ", cancelAmt=" + cancelAmt
				+ ", cancelDate=" + cancelDate + ", cancelTime=" + cancelTime
				+ ", cancelNum=" + cancelNum + "]";
	}
}
