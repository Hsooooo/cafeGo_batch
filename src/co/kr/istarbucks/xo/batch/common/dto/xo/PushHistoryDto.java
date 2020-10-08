/*
 * @(#) $Id: PushHistoryDto.java,v 1.2 2016/06/28 09:31:09 sonagi0219 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * PUSH 발송이력 PushHistoryDto.
 * @author eZEN ksy
 * @since 2014. 1. 27.
 * @version $Revision: 1.2 $
 */
public class PushHistoryDto {
	
	private String push_no; // PUSH 일련번호{"9" + YYYYMMDDHH24MISS + 시퀀스 5자리}
	private String user_id; // 사용자아이디
	private String push_id; // PUSH ID
	private String push_type; // PUSH타입{1-주문접수, 2-제조완료, 3-제조불가, 4-고객호출, 5-기타}
	private String push_message; // PUSH 메세지
	private String waitingCup;	// 대기 잔수
	private String order_no; // 주문번호
	private String os_type; // 단말구분{1-iOS, 2-Android, 9-기타}
	private String view_type; // 결과화면 타입{W-웹뷰, N-네이티브뷰, X-호출안함}
	private String url; // 웹뷰 호출 시 이동할 URL
	private String screen_id; // 네이티브뷰 호출 시 이동할 화면아이디
	private String param; // 파라미터
	private Date reg_date; // 요청일자
	private String result_push_id; // Push 발송 고유 번호 
	private String result_code; // 결과코드
	private String result_msg; // 결과상세{코드99일시 GCM서버 혹은 APNS에서 리턴되는 한글을 반환함}
	private String process_date; // 발송일자
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("pushHistoryDto [");
		str.append ("push_no=").append (this.push_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", push_id=").append (this.push_id);
		str.append (", push_type=").append (this.push_type);
		str.append (", push_message=").append (this.push_message);
		str.append (", waitingCup=").append (this.waitingCup);
		str.append (", order_no=").append (this.order_no);
		str.append (", os_type=").append (this.os_type);
		str.append (", view_type=").append (this.view_type);
		str.append (", url=").append (this.url);
		str.append (", screen_id=").append (this.screen_id);
		str.append (", param=").append (this.param);
		str.append (", reg_date=").append (this.reg_date);
		str.append (", result_push_id=").append (this.result_push_id);
		str.append (", result_code=").append (this.result_code);
		str.append (", result_msg=").append (this.result_msg);
		str.append (", process_date=").append (this.process_date);
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * PUSH 일련번호{""9"" + YYYYMMDDHH24MISS + 시퀀스 5자리}
	 * @return push_no
	 */
	public String getPush_no () {
		return push_no;
	}
	
	/**
	 * PUSH 일련번호{""9"" + YYYYMMDDHH24MISS + 시퀀스 5자리}
	 * @param push_no
	 */
	public void setPush_no ( String push_no ) {
		this.push_no = push_no;
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
	 * PUSH타입{1-주문접수, 2-제조완료, 3-제조불가, 4-고객호출, 5-기타}
	 * @return push_type
	 */
	public String getPush_type () {
		return push_type;
	}
	
	/**
	 * PUSH타입{1-주문접수, 2-제조완료, 3-제조불가, 4-고객호출, 5-기타}
	 * @param push_type
	 */
	public void setPush_type ( String push_type ) {
		this.push_type = push_type;
	}
	
	/**
	 * PUSH 메세지
	 * @return push_message
	 */
	public String getPush_message () {
		return push_message;
	}
	
	/**
	 * PUSH 메세지
	 * @param push_message
	 */
	public void setPush_message ( String push_message ) {
		this.push_message = push_message;
	}
	
	public String getWaitingCup() {
		return waitingCup;
	}

	public void setWaitingCup(String waitingCup) {
		this.waitingCup = waitingCup;
	}

	/**
	 * 주문번호
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * 주문번호
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * 단말구분{1-iOS, 2-Android, 9-기타}
	 * @return os_type
	 */
	public String getOs_type () {
		return os_type;
	}
	
	/**
	 * 단말구분{1-iOS, 2-Android, 9-기타}
	 * @param os_type
	 */
	public void setOs_type ( String os_type ) {
		this.os_type = os_type;
	}
	
	/**
	 * 결과화면 타입{W-웹뷰, N-네이티브뷰, X-호출안함}
	 * @return view_type
	 */
	public String getView_type () {
		return view_type;
	}
	
	/**
	 * 결과화면 타입{W-웹뷰, N-네이티브뷰, X-호출안함}
	 * @param view_type
	 */
	public void setView_type ( String view_type ) {
		this.view_type = view_type;
	}
	
	/**
	 * 웹뷰 호출 시 이동할 URL
	 * @return url
	 */
	public String getUrl () {
		return url;
	}
	
	/**
	 * 웹뷰 호출 시 이동할 URL
	 * @param url
	 */
	public void setUrl ( String url ) {
		this.url = url;
	}
	
	/**
	 * 네이티브뷰 호출 시 이동할 화면아이디
	 * @return screen_id
	 */
	public String getScreen_id () {
		return screen_id;
	}
	
	/**
	 * 네이티브뷰 호출 시 이동할 화면아이디
	 * @param screen_id
	 */
	public void setScreen_id ( String screen_id ) {
		this.screen_id = screen_id;
	}
	
	/**
	 * 파라미터
	 * @return param
	 */
	public String getParam () {
		return param;
	}
	
	/**
	 * 파라미터
	 * @param param
	 */
	public void setParam ( String param ) {
		this.param = param;
	}
	
	/**
	 * 요청일자
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 요청일자
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * Push 발송 고유 번호
	 * @return result_code
	 */
	public String getResult_push_id () {
		return result_push_id;
	}
	
	/**
	 * Push 발송 고유 번호
	 * @param result_push_id
	 */
	public void setResult_push_id ( String result_push_id ) {
		this.result_push_id = result_push_id;
	}
	
	/**
	 * 결과코드
	 * @return result_code
	 */
	public String getResult_code () {
		return result_code;
	}
	
	/**
	 * 결과코드
	 * @param result_code
	 */
	public void setResult_code ( String result_code ) {
		this.result_code = result_code;
	}
	
	/**
	 * 결과상세{코드99일시 GCM서버 혹은 APNS에서 리턴되는 한글을 반환함}
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * 결과상세{코드99일시 GCM서버 혹은 APNS에서 리턴되는 한글을 반환함}
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * 발송일자
	 * @return process_date
	 */
	public String getProcess_date () {
		return process_date;
	}
	
	/**
	 * 발송일자
	 * @param process_date
	 */
	public void setProcess_date ( String process_date ) {
		this.process_date = process_date;
	}
	
}
