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
 * ReservationPush - 대용량 발송을 위한 저장소
 * 
 * @author eZEN ksy
 * @since 2016. 10. 28.
 * @version $Revision: 1.1 $
 */
public class ReservationPushDto {
	
	private int push_id;		// 푸시 메시지 고유값(자동증가 설정)
	private String device_id;	// 푸시 발송을 위한 고유값
	private String msg;			// 푸시 메시지 
	private Date reg_date;		// 등록일시
	private String event_type;	// 메시지 타입(사용안함, 실시간은 3)
	private String device_type;	// 디바이스 종류{1:아이폰, 2:안드로이드}
	private String event_url;	// Push 구성 > 연결될URL
	private String topic_id;	// Group 메시지 전송시 구분할 ID 정
	private String screen_id;	// Push 구성 > 화면ID
	private String param;		// Push 구성 > 파라미터
	private String topic_yn;	// 그룹화 여부{N:개인화 메시지로 개별 전송, Y:그룹화하여 TOPIC_ID 기준으로 메시지 전송}

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
	 * 푸시 메시지 고유값(자동증가 설정)
	 * @return push_id
	 */
	public int getPush_id() {
		return push_id;
	}

	/**
	 * 푸시 메시지 고유값(자동증가 설정)
	 * @param push_id
	 */
	public void setPush_id(int push_id) {
		this.push_id = push_id;
	}
	
	/**
	 * 푸시 발송을 위한 고유값
	 * @return device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * 푸시 발송을 위한 고유값
	 * @param device_id
	 */
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	/**
	 * 푸시 메시지 
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 푸시 메시지 
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 등록일시
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 등록일시
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 메시지 타입(사용안함, 실시간은 3)
	 * @return event_type
	 */
	public String getEvent_type() {
		return event_type;
	}

	/**
	 * 메시지 타입(사용안함, 실시간은 3)
	 * @param event_type
	 */
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	
	/**
	 * 디바이스 종류{1:아이폰, 2:안드로이드}
	 * @return device_type
	 */
	public String getDevice_type() {
		return device_type;
	}

	/**
	 * 디바이스 종류{1:아이폰, 2:안드로이드}
	 * @param device_type
	 */
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	
	/**
	 * Push 구성 > 연결될URL
	 * @return event_url
	 */
	public String getEvent_url() {
		return event_url;
	}

	/**
	 * Push 구성 > 연결될URL
	 * @param event_url
	 */
	public void setEvent_url(String event_url) {
		this.event_url = event_url;
	}
	
	/**
	 * Group 메시지 전송시 구분할 ID 정
	 * @return topic_id
	 */
	public String getTopic_id() {
		return topic_id;
	}

	/**
	 * Group 메시지 전송시 구분할 ID 정
	 * @param topic_id
	 */
	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}
	
	/**
	 * Push 구성 > 화면ID
	 * @return screen_id
	 */
	public String getScreen_id() {
		return screen_id;
	}

	/**
	 * Push 구성 > 화면ID
	 * @param screen_id
	 */
	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}
	
	/**
	 * Push 구성 > 파라미터
	 * @return param
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Push 구성 > 파라미터
	 * @param param
	 */
	public void setParam(String param) {
		this.param = param;
	}
	
	/**
	 * 그룹화 여부{N:개인화 메시지로 개별 전송, Y:그룹화하여 TOPIC_ID 기준으로 메시지 전송}
	 * @return topic_yn
	 */
	public String getTopic_yn() {
		return topic_yn;
	}

	/**
	 * 그룹화 여부{N:개인화 메시지로 개별 전송, Y:그룹화하여 TOPIC_ID 기준으로 메시지 전송}
	 * @param topic_yn
	 */
	public void setTopic_yn(String topic_yn) {
		this.topic_yn = topic_yn;
	}
}
