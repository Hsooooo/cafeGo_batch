/*
 * @(#) $Id: ReservationPushDto.java,v 1.1 2016/11/14 01:15:25 dev99 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.push;

import java.util.Date;

/**
 * ReservationPush - ��뷮 �߼��� ���� �����
 * 
 * @author eZEN ksy
 * @since 2016. 10. 28.
 * @version $Revision: 1.1 $
 */
public class ReservationPushDto {
	
	private int push_id;		// Ǫ�� �޽��� ������(�ڵ����� ����)
	private String device_id;	// Ǫ�� �߼��� ���� ������
	private String msg;			// Ǫ�� �޽��� 
	private Date reg_date;		// ����Ͻ�
	private String event_type;	// �޽��� Ÿ��(������, �ǽð��� 3)
	private String device_type;	// ����̽� ����{1:������, 2:�ȵ���̵�}
	private String event_url;	// Push ���� > �����URL
	private String topic_id;	// Group �޽��� ���۽� ������ ID ��
	private String screen_id;	// Push ���� > ȭ��ID
	private String param;		// Push ���� > �Ķ����
	private String topic_yn;	// �׷�ȭ ����{N:����ȭ �޽����� ���� ����, Y:�׷�ȭ�Ͽ� TOPIC_ID �������� �޽��� ����}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("push_id=").append(this.push_id);
		sb.append(", device_id=").append(this.device_id);
		sb.append(", msg=").append(this.msg);
		sb.append(", reg_date=").append(this.reg_date);
		sb.append(", event_type=").append(this.event_type);
		sb.append(", device_type=").append(this.device_type);
		sb.append(", event_url=").append(this.event_url);
		sb.append(", topic_id=").append(this.topic_id);
		sb.append(", screen_id=").append(this.screen_id);
		sb.append(", param=").append(this.param);
		sb.append(", topic_yn=").append(this.topic_yn);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Ǫ�� �޽��� ������(�ڵ����� ����)
	 * @return push_id
	 */
	public int getPush_id() {
		return push_id;
	}

	/**
	 * Ǫ�� �޽��� ������(�ڵ����� ����)
	 * @param push_id
	 */
	public void setPush_id(int push_id) {
		this.push_id = push_id;
	}
	
	/**
	 * Ǫ�� �߼��� ���� ������
	 * @return device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * Ǫ�� �߼��� ���� ������
	 * @param device_id
	 */
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	/**
	 * Ǫ�� �޽��� 
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Ǫ�� �޽��� 
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * ����Ͻ�
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * ����Ͻ�
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �޽��� Ÿ��(������, �ǽð��� 3)
	 * @return event_type
	 */
	public String getEvent_type() {
		return event_type;
	}

	/**
	 * �޽��� Ÿ��(������, �ǽð��� 3)
	 * @param event_type
	 */
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	
	/**
	 * ����̽� ����{1:������, 2:�ȵ���̵�}
	 * @return device_type
	 */
	public String getDevice_type() {
		return device_type;
	}

	/**
	 * ����̽� ����{1:������, 2:�ȵ���̵�}
	 * @param device_type
	 */
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	
	/**
	 * Push ���� > �����URL
	 * @return event_url
	 */
	public String getEvent_url() {
		return event_url;
	}

	/**
	 * Push ���� > �����URL
	 * @param event_url
	 */
	public void setEvent_url(String event_url) {
		this.event_url = event_url;
	}
	
	/**
	 * Group �޽��� ���۽� ������ ID ��
	 * @return topic_id
	 */
	public String getTopic_id() {
		return topic_id;
	}

	/**
	 * Group �޽��� ���۽� ������ ID ��
	 * @param topic_id
	 */
	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}
	
	/**
	 * Push ���� > ȭ��ID
	 * @return screen_id
	 */
	public String getScreen_id() {
		return screen_id;
	}

	/**
	 * Push ���� > ȭ��ID
	 * @param screen_id
	 */
	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}
	
	/**
	 * Push ���� > �Ķ����
	 * @return param
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Push ���� > �Ķ����
	 * @param param
	 */
	public void setParam(String param) {
		this.param = param;
	}
	
	/**
	 * �׷�ȭ ����{N:����ȭ �޽����� ���� ����, Y:�׷�ȭ�Ͽ� TOPIC_ID �������� �޽��� ����}
	 * @return topic_yn
	 */
	public String getTopic_yn() {
		return topic_yn;
	}

	/**
	 * �׷�ȭ ����{N:����ȭ �޽����� ���� ����, Y:�׷�ȭ�Ͽ� TOPIC_ID �������� �޽��� ����}
	 * @param topic_yn
	 */
	public void setTopic_yn(String topic_yn) {
		this.topic_yn = topic_yn;
	}
}
