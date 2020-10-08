/*
 * @(#) $Id: NiceCancelDto.java,v 1.1 2015/11/24 03:59:02 soonwoo Exp $
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
 * TODO Insert type comment for NiceCancelDto.
 *
 * @author sw.Lee
 * @version $Revision: 1.1 $
 */
public class NiceCancelDto {
	private static final String serviceMode = "CL0";		//서비스모드{CL0:취소}
	private String mid;						//상점ID
	private String moid;					//주문번호{ORDER_NO}
	private String tid;						//거래TID
	private String payMethod;				//취소 결제수단
	private String cancelAmt;				//취소금액
	private String cancelMsg;				//취소사유
	private String cancelPwd;				//취소비밀번호
	private String partialCancelCode;		//부분취소구분{0:전체취소, 1:부분취소}
	private String cancelIP;				//취소요청자IP

	
	/**
	 * 서비스모드{CL0:취소}
	 * @return
	 */
	public String getServiceMode() {
		return serviceMode;
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
	 * 거래TID
	 * @return
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * 거래TID
	 * @param tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * 취소 결제수단
	 * @return
	 */
	public String getPayMethod() {
		return payMethod;
	}
	/**
	 * 취소 결제수단
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
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
	 * 취소사유
	 * @return
	 */
	public String getCancelMsg() {
		return cancelMsg;
	}
	/**
	 * 취소사유
	 * @param cancelMsg
	 */
	public void setCancelMsg(String cancelMsg) {
		this.cancelMsg = cancelMsg;
	}
	/**
	 * 취소비밀번호
	 * @return
	 */
	public String getCancelPwd() {
		return cancelPwd;
	}
	/**
	 * 취소비밀번호
	 * @param cancelPwd
	 */
	public void setCancelPwd(String cancelPwd) {
		this.cancelPwd = cancelPwd;
	}
	/**
	 * 부분취소구분{0:전체취소, 1:부분취소}
	 * @return
	 */
	public String getPartialCancelCode() {
		return StringUtils.defaultIfEmpty(partialCancelCode, "0");
	}
	/**
	 * 부분취소구분{0:전체취소, 1:부분취소}
	 * @param partialCancelCode
	 */
	public void setPartialCancelCode(String partialCancelCode) {
		this.partialCancelCode = partialCancelCode;
	}
	/**
	 * 취소요청자IP
	 * @return
	 */
	public String getCancelIP() {
		return StringUtils.defaultIfEmpty(cancelIP, "");
	}
	/**
	 * 취소요청자IP
	 * @param cancelIP
	 */
	public void setCancelIP(String cancelIP) {
		this.cancelIP = cancelIP;
	}
	
	@Override
	public String toString() {
		return "NiceCancelDto [serviceMode=" + serviceMode + ", mid=" + mid
				+ ", moid=" + moid + ", tid=" + tid + ", payMethod="
				+ payMethod + ", cancelAmt=" + cancelAmt + ", cancelMsg="
				+ cancelMsg + ", cancelPwd=" + cancelPwd
				+ ", partialCancelCode=" + partialCancelCode + ", cancelIP="
				+ cancelIP + "]";
	}
}
