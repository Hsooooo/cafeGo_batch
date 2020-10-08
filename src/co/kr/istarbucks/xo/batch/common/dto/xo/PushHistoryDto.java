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
 * PUSH �߼��̷� PushHistoryDto.
 * @author eZEN ksy
 * @since 2014. 1. 27.
 * @version $Revision: 1.2 $
 */
public class PushHistoryDto {
	
	private String push_no; // PUSH �Ϸù�ȣ{"9" + YYYYMMDDHH24MISS + ������ 5�ڸ�}
	private String user_id; // ����ھ��̵�
	private String push_id; // PUSH ID
	private String push_type; // PUSHŸ��{1-�ֹ�����, 2-�����Ϸ�, 3-�����Ұ�, 4-��ȣ��, 5-��Ÿ}
	private String push_message; // PUSH �޼���
	private String waitingCup;	// ��� �ܼ�
	private String order_no; // �ֹ���ȣ
	private String os_type; // �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	private String view_type; // ���ȭ�� Ÿ��{W-����, N-����Ƽ���, X-ȣ�����}
	private String url; // ���� ȣ�� �� �̵��� URL
	private String screen_id; // ����Ƽ��� ȣ�� �� �̵��� ȭ����̵�
	private String param; // �Ķ����
	private Date reg_date; // ��û����
	private String result_push_id; // Push �߼� ���� ��ȣ 
	private String result_code; // ����ڵ�
	private String result_msg; // �����{�ڵ�99�Ͻ� GCM���� Ȥ�� APNS���� ���ϵǴ� �ѱ��� ��ȯ��}
	private String process_date; // �߼�����
	
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
	 * PUSH �Ϸù�ȣ{""9"" + YYYYMMDDHH24MISS + ������ 5�ڸ�}
	 * @return push_no
	 */
	public String getPush_no () {
		return push_no;
	}
	
	/**
	 * PUSH �Ϸù�ȣ{""9"" + YYYYMMDDHH24MISS + ������ 5�ڸ�}
	 * @param push_no
	 */
	public void setPush_no ( String push_no ) {
		this.push_no = push_no;
	}
	
	/**
	 * ����ھ��̵�
	 * @return user_id
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * ����ھ��̵�
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
	 * PUSHŸ��{1-�ֹ�����, 2-�����Ϸ�, 3-�����Ұ�, 4-��ȣ��, 5-��Ÿ}
	 * @return push_type
	 */
	public String getPush_type () {
		return push_type;
	}
	
	/**
	 * PUSHŸ��{1-�ֹ�����, 2-�����Ϸ�, 3-�����Ұ�, 4-��ȣ��, 5-��Ÿ}
	 * @param push_type
	 */
	public void setPush_type ( String push_type ) {
		this.push_type = push_type;
	}
	
	/**
	 * PUSH �޼���
	 * @return push_message
	 */
	public String getPush_message () {
		return push_message;
	}
	
	/**
	 * PUSH �޼���
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
	 * �ֹ���ȣ
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	 * @return os_type
	 */
	public String getOs_type () {
		return os_type;
	}
	
	/**
	 * �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	 * @param os_type
	 */
	public void setOs_type ( String os_type ) {
		this.os_type = os_type;
	}
	
	/**
	 * ���ȭ�� Ÿ��{W-����, N-����Ƽ���, X-ȣ�����}
	 * @return view_type
	 */
	public String getView_type () {
		return view_type;
	}
	
	/**
	 * ���ȭ�� Ÿ��{W-����, N-����Ƽ���, X-ȣ�����}
	 * @param view_type
	 */
	public void setView_type ( String view_type ) {
		this.view_type = view_type;
	}
	
	/**
	 * ���� ȣ�� �� �̵��� URL
	 * @return url
	 */
	public String getUrl () {
		return url;
	}
	
	/**
	 * ���� ȣ�� �� �̵��� URL
	 * @param url
	 */
	public void setUrl ( String url ) {
		this.url = url;
	}
	
	/**
	 * ����Ƽ��� ȣ�� �� �̵��� ȭ����̵�
	 * @return screen_id
	 */
	public String getScreen_id () {
		return screen_id;
	}
	
	/**
	 * ����Ƽ��� ȣ�� �� �̵��� ȭ����̵�
	 * @param screen_id
	 */
	public void setScreen_id ( String screen_id ) {
		this.screen_id = screen_id;
	}
	
	/**
	 * �Ķ����
	 * @return param
	 */
	public String getParam () {
		return param;
	}
	
	/**
	 * �Ķ����
	 * @param param
	 */
	public void setParam ( String param ) {
		this.param = param;
	}
	
	/**
	 * ��û����
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * ��û����
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * Push �߼� ���� ��ȣ
	 * @return result_code
	 */
	public String getResult_push_id () {
		return result_push_id;
	}
	
	/**
	 * Push �߼� ���� ��ȣ
	 * @param result_push_id
	 */
	public void setResult_push_id ( String result_push_id ) {
		this.result_push_id = result_push_id;
	}
	
	/**
	 * ����ڵ�
	 * @return result_code
	 */
	public String getResult_code () {
		return result_code;
	}
	
	/**
	 * ����ڵ�
	 * @param result_code
	 */
	public void setResult_code ( String result_code ) {
		this.result_code = result_code;
	}
	
	/**
	 * �����{�ڵ�99�Ͻ� GCM���� Ȥ�� APNS���� ���ϵǴ� �ѱ��� ��ȯ��}
	 * @return result_msg
	 */
	public String getResult_msg () {
		return result_msg;
	}
	
	/**
	 * �����{�ڵ�99�Ͻ� GCM���� Ȥ�� APNS���� ���ϵǴ� �ѱ��� ��ȯ��}
	 * @param result_msg
	 */
	public void setResult_msg ( String result_msg ) {
		this.result_msg = result_msg;
	}
	
	/**
	 * �߼�����
	 * @return process_date
	 */
	public String getProcess_date () {
		return process_date;
	}
	
	/**
	 * �߼�����
	 * @param process_date
	 */
	public void setProcess_date ( String process_date ) {
		this.process_date = process_date;
	}
	
}
