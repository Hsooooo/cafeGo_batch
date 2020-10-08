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
	/* ===== ���� ������ ���� ===== */
	private String serviceMode;			//���񽺸��{PY0:����, CL0:���}
	private String moid;				//�ֹ���ȣ{ORDER_NO}
	private String mid;					//����ID
	private String tid;					//TID
	private String payMethod;			//��������(���Ҽ���)
	private String resultCode;			//����ڵ�{������->3001:ī���������, �� ��:����,  ��ҽ�->2001:��Ҽ���, 2002:���������, �� ��:����}
	private String resultMsg;			//����޽���
	/* ===== ���� ������ ���� ===== */
	
	/* ===== �������� ������ ���� ===== */
	private String goodsName;			//��ǰ��
	private String amt;					//�ݾ�
	private String buyerName;			//������ ��
	private String buyerTel;			//������ ����ó
	private String buyerEmail;			//������ �̸���
	private String authCode;			//���ι�ȣ
	private String authDate;			//���γ�¥{YYMMDDHH24MISS}
	private String cardNo;				//ī���ȣ
	private String cardCode;			//ī���ڵ�
	private String cardName;			//ī����
	private String orderNo;				//�ֹ���ȣ
	private String userId;				//�����ID
	private String paymentOrder;		//���̷����� ��������
	/* ===== �������� ������ ���� ===== */
	
	/* ===== ��� ������ ���� ===== */
	private String cancelAmt;			//��ұݾ�
	private String cancelDate;			//�����
	private String cancelTime;			//��ҽð�
	private String cancelNum;			//��ҹ�ȣ
	/* ===== ��� ������ ���� ===== */
	
	
	/* ===== ���� ������ ���� ===== */
	/**
	 * ���񽺸��{PY0:����, CL0:���}
	 * @return
	 */
	public String getServiceMode() {
		return serviceMode;
	}
	/**
	 * ���񽺸��{PY0:����, CL0:���}
	 * @param serviceMode
	 */
	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
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
	 * ��������(���Ҽ���)
	 * @return
	 */
	public String getPayMethod() {
		return payMethod;
	}
	/**
	 * ��������(���Ҽ���)
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * ����ڵ�{������->3001:ī���������, �� ��:����,  ��ҽ�->2001:��Ҽ���, 2002:���������, �� ��:����}
	 * @return
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * ����ڵ�{������->3001:ī���������, �� ��:����,  ��ҽ�->2001:��Ҽ���, 2002:���������, �� ��:����}
	 * @param resultCode
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * ����޽���
	 * @return
	 */
	public String getResultMsg() {
		return resultMsg;
	}
	/**
	 * ����޽���
	 * @param resultMsg
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	/* ===== ���� ������ ���� ===== */
	
	
	/* ===== �������� ������ ���� ===== */
	/**
	 * ��ǰ��
	 * @return
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * ��ǰ��
	 * @param goodsName
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * �ݾ�
	 * @return
	 */
	public String getAmt() {
		return amt;
	}
	/**
	 * �ݾ�
	 * @param amt
	 */
	public void setAmt(String amt) {
		this.amt = amt;
	}
	/**
	 * ������ ��
	 * @return
	 */
	public String getBuyerName() {
		return buyerName;
	}
	/**
	 * ������ ��
	 * @param buyerName
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	/**
	 * ������ ����ó
	 * @return
	 */
	public String getBuyerTel() {
		return buyerTel;
	}
	/**
	 * ������ ����ó
	 * @param buyerTel
	 */
	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}
	/**
	 * ������ �̸���
	 * @return
	 */
	public String getBuyerEmail() {
		return buyerEmail;
	}
	/**
	 * ������ �̸���
	 * @param buyerEmail
	 */
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
	/**
	 * ���ι�ȣ
	 * @return
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * ���ι�ȣ
	 * @param authCode
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	/**
	 * ���γ�¥{YYMMDDHH24MISS}
	 * @return
	 */
	public String getAuthDate() {
		return authDate;
	}
	/**
	 * ���γ�¥{YYMMDDHH24MISS}
	 * @param authDate
	 */
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	/**
	 * ī���ȣ
	 * @return
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * ī���ȣ
	 * @param cardNo
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * ī���ڵ�
	 * @return
	 */
	public String getCardCode() {
		return cardCode;
	}
	/**
	 * ī���ڵ�
	 * @param cardCode
	 */
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	/**
	 * ī����
	 * @return
	 */
	public String getCardName() {
		return cardName;
	}
	/**
	 * ī����
	 * @param cardName
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	/**
	 * �ֹ���ȣ
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * �ֹ���ȣ
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * �����ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * �����ID
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * ���̷����� ��������
	 * @return
	 */
	public String getPaymentOrder() {
		return paymentOrder;
	}
	/**
	 * ���̷����� ��������
	 * @param paymentOrder
	 */
	public void setPaymentOrder(String paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
	/* ===== �������� ������ ���� ===== */
	
	
	/* ===== ��� ������ ���� ===== */
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
	 * �����
	 * @return
	 */
	public String getCancelDate() {
		return cancelDate;
	}
	/**
	 * �����
	 * @param cancelDate
	 */
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	/**
	 * ��ҽð�
	 * @return
	 */
	public String getCancelTime() {
		return cancelTime;
	}
	/**
	 * ��ҽð�
	 * @param cancelTime
	 */
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	/**
	 * ��ҹ�ȣ
	 * @return
	 */
	public String getCancelNum() {
		return cancelNum;
	}
	/**
	 * ��ҹ�ȣ
	 * @param cancelNum
	 */
	public void setCancelNum(String cancelNum) {
		this.cancelNum = cancelNum;
	}
	/* ===== ��� ������ ���� ===== */


	/**
	 * �ý��ۿ� ����ڵ�{00:����, �׿�:����}
	 * @return
	 */
	public String getModResultCode() {
		String modRtCode = this.resultCode;
		
		//����
		if(StringUtils.equals(this.serviceMode, "PY0")){
			//�ſ�ī��
			if(StringUtils.equals(this.payMethod, "CARD") && StringUtils.equals(this.resultCode, "3001")){
				modRtCode = "00";
			//������ü
			}else if(StringUtils.equals(this.payMethod, "BANK") && StringUtils.equals(this.resultCode, "4000")){
				modRtCode = "00";
			//�޴���
			}else if(StringUtils.equals(this.payMethod, "CELLPHONE") && StringUtils.equals(this.resultCode, "A000")){
				modRtCode = "00";
			//�������
			}else if(StringUtils.equals(this.payMethod, "VBANK") && StringUtils.equals(this.resultCode, "4100")){
				modRtCode = "00";
			}
		//���
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
