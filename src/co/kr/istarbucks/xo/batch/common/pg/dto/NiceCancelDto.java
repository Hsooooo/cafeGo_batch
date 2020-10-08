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
	private static final String serviceMode = "CL0";		//���񽺸��{CL0:���}
	private String mid;						//����ID
	private String moid;					//�ֹ���ȣ{ORDER_NO}
	private String tid;						//�ŷ�TID
	private String payMethod;				//��� ��������
	private String cancelAmt;				//��ұݾ�
	private String cancelMsg;				//��һ���
	private String cancelPwd;				//��Һ�й�ȣ
	private String partialCancelCode;		//�κ���ұ���{0:��ü���, 1:�κ����}
	private String cancelIP;				//��ҿ�û��IP

	
	/**
	 * ���񽺸��{CL0:���}
	 * @return
	 */
	public String getServiceMode() {
		return serviceMode;
	}
	/**
	 * ����ID
	 * @return
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * ����ID
	 * @param mid
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * �ֹ���ȣ{ORDER_NO}
	 * @return
	 */
	public String getMoid() {
		return moid;
	}
	/**
	 * �ֹ���ȣ{ORDER_NO}
	 * @param moid
	 */
	public void setMoid(String moid) {
		this.moid = moid;
	}
	/**
	 * �ŷ�TID
	 * @return
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * �ŷ�TID
	 * @param tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * ��� ��������
	 * @return
	 */
	public String getPayMethod() {
		return payMethod;
	}
	/**
	 * ��� ��������
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * ��ұݾ�
	 * @return
	 */
	public String getCancelAmt() {
		return cancelAmt;
	}
	/**
	 * ��ұݾ�
	 * @param cancelAmt
	 */
	public void setCancelAmt(String cancelAmt) {
		this.cancelAmt = cancelAmt;
	}
	/**
	 * ��һ���
	 * @return
	 */
	public String getCancelMsg() {
		return cancelMsg;
	}
	/**
	 * ��һ���
	 * @param cancelMsg
	 */
	public void setCancelMsg(String cancelMsg) {
		this.cancelMsg = cancelMsg;
	}
	/**
	 * ��Һ�й�ȣ
	 * @return
	 */
	public String getCancelPwd() {
		return cancelPwd;
	}
	/**
	 * ��Һ�й�ȣ
	 * @param cancelPwd
	 */
	public void setCancelPwd(String cancelPwd) {
		this.cancelPwd = cancelPwd;
	}
	/**
	 * �κ���ұ���{0:��ü���, 1:�κ����}
	 * @return
	 */
	public String getPartialCancelCode() {
		return StringUtils.defaultIfEmpty(partialCancelCode, "0");
	}
	/**
	 * �κ���ұ���{0:��ü���, 1:�κ����}
	 * @param partialCancelCode
	 */
	public void setPartialCancelCode(String partialCancelCode) {
		this.partialCancelCode = partialCancelCode;
	}
	/**
	 * ��ҿ�û��IP
	 * @return
	 */
	public String getCancelIP() {
		return StringUtils.defaultIfEmpty(cancelIP, "");
	}
	/**
	 * ��ҿ�û��IP
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
