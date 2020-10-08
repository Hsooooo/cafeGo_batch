/*
 * @(#) $Id: SmtTranDto.java,v 1.3 2017/04/24 02:25:05 leeminjung Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * SMS ���� SmtTranDto.
 * @author eZEN ksy
 * @since 2014. 2. 4.
 * @version $Revision: 1.3 $
 */
public class SmtTranDto {
	
	private Long    mt_pr; 					// �޽��� ���� ���̵�
	private String  mt_refkey; 				// �μ� �ڵ�
	private String  priority; 				// ���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
	private String  date_client_req; 		// ���� ���� �ð�
	private String  content; 				// ���� �޽���
	private String  callback; 				// �߽��� ��ȭ ��ȣ
	private String  service_type; 			// ���� �޽��� ���� Ÿ��{0-SMS MT, 1-CALLBACK URL, 2-MMS MT, 3-LMS}
	private String  broadcast_yn; 			// ���� �߼� ����{'N' : ���ϰ�, 'Y' :������}
	private String  msg_status; 			// �޽��� ����{1-���۴��, 2-������, 3-�Ϸ�}
	private String  recipient_num; 			// ������ ��ȭ ��ȣ
	private Date    date_mt_sent; 			// G/W �����ð�
	private Date    date_rslt; 				// �ܸ��� ���� �ð�
	private Date    date_mt_report; 		// ��� ���� �ð�
	private String  mt_report_code_ib; 		// ���� ���{1000-����, ��Ÿ-����}
	private String  mt_report_code_ibtype; 	// ���� ��� �з�
	private Integer carrier; 				// ���Ÿ� ����
	private String  rs_id; 					// G/W ����
	private String  recipient_net; 			// ���� ��û ��Ż�
	private String  recipient_npsend; 		// ���� ��û ��Ż� ���� �ִ� ��� ��ȣ ��� ���Ž� ������ ����
	private String  country_code; 			// �����ڵ�{82}
	private String  charset; 				// �޽��� CHARSET
	private Long    msg_type; 				// �޽��� ����
	private String  crypto_yn; 				// ��ȣȭ ��� ����
	private Integer ttl; 					// ���� ��ȿ �ð� ����
	private String  emma_id; 				// EMMA ����ȭ�� ���Ǵ� EMMA ID
	private Date    reg_date; 				// ������ �������
	
	private String  subject; 				// �޽�������
	private String  content_type; 			// ���۸޽��� Ÿ��{0-�Ϲ�, 1-IBL Ÿ��}
	
	private String  reservation_time;		// ���ۿ���ð�(HHmm)
	
	// Ȧ���� MMS �߼�. EM_MMT_TRAN ���̺��� ����. 2016-10-28 
	private long    attach_file_group_key;	// ���� �׷�Ű  
	private String  xo_order_no;		    // ���̷����� �ֹ���ȣ
	private String user_id;	// ����ھ��̵�
	
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName());
		str.append(" [");
		str.append ("mt_pr=").append (this.mt_pr);
		str.append (", mt_refkey=").append (this.mt_refkey);
		str.append (", priority=").append (this.priority);
		str.append (", date_client_req=").append (this.date_client_req);
		str.append (", content=").append (this.content);
		str.append (", callback=").append (this.callback);
		str.append (", service_type=").append (this.service_type);
		str.append (", broadcast_yn=").append (this.broadcast_yn);
		str.append (", msg_status=").append (this.msg_status);
		str.append (", recipient_num=").append (this.recipient_num);
		str.append (", date_mt_sent=").append (this.date_mt_sent);
		str.append (", date_rslt=").append (this.date_rslt);
		str.append (", date_mt_report=").append (this.date_mt_report);
		str.append (", mt_report_code_ib=").append (this.mt_report_code_ib);
		str.append (", mt_report_code_ibtype=").append (this.mt_report_code_ibtype);
		str.append (", carrier=").append (this.carrier);
		str.append (", rs_id=").append (this.rs_id);
		str.append (", recipient_net=").append (this.recipient_net);
		str.append (", recipient_npsend=").append (this.recipient_npsend);
		str.append (", country_code=").append (this.country_code);
		str.append (", charset=").append (this.charset);
		str.append (", msg_type=").append (this.msg_type);
		str.append (", crypto_yn=").append (this.crypto_yn);
		str.append (", ttl=").append (this.ttl);
		str.append (", emma_id=").append (this.emma_id);
		str.append (", reg_date=").append (this.reg_date);
		str.append (", subject=").append (this.subject);
		str.append (", content_type=").append (this.content_type);
		str.append (", reservation_time=").append (this.reservation_time);
		str.append (", attach_file_group_key=").append (this.attach_file_group_key);
		str.append (", xo_order_no=").append (this.xo_order_no);
		str.append (", user_id=").append (this.user_id);
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * �޽��� ���� ���̵�
	 * @return mt_pr
	 */
	public Long getMt_pr () {
		return mt_pr;
	}
	
	/**
	 * �޽��� ���� ���̵�
	 * @param mt_pr
	 */
	public void setMt_pr ( Long mt_pr ) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * �μ� �ڵ�
	 * @return mt_refkey
	 */
	public String getMt_refkey () {
		return mt_refkey;
	}
	
	/**
	 * �μ� �ڵ�
	 * @param mt_refkey
	 */
	public void setMt_refkey ( String mt_refkey ) {
		this.mt_refkey = mt_refkey;
	}
	
	/**
	 * ���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
	 * @return priority
	 */
	public String getPriority () {
		return priority;
	}
	
	/**
	 * ���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
	 * @param priority
	 */
	public void setPriority ( String priority ) {
		this.priority = priority;
	}
	
	/**
	 * ���� ���� �ð�
	 * @return date_client_req
	 */
	public String getDate_client_req () {
		return date_client_req;
	}
	
	/**
	 * ���� ���� �ð�
	 * @param date_client_req
	 */
	public void setDate_client_req ( String date_client_req ) {
		this.date_client_req = date_client_req;
	}
	
	/**
	 * ���� �޽���
	 * @return content
	 */
	public String getContent () {
		return content;
	}
	
	/**
	 * ���� �޽���
	 * @param content
	 */
	public void setContent ( String content ) {
		this.content = content;
	}
	
	/**
	 * �߽��� ��ȭ ��ȣ
	 * @return callback
	 */
	public String getCallback () {
		return callback;
	}
	
	/**
	 * �߽��� ��ȭ ��ȣ
	 * @param callback
	 */
	public void setCallback ( String callback ) {
		this.callback = callback;
	}
	
	/**
	 * ���� �޽��� ���� Ÿ��{0-SMS MT, 1-CALLBACK URL}
	 * @return service_type
	 */
	public String getService_type () {
		return service_type;
	}
	
	/**
	 * ���� �޽��� ���� Ÿ��{0-SMS MT, 1-CALLBACK URL}
	 * @param service_type
	 */
	public void setService_type ( String service_type ) {
		this.service_type = service_type;
	}
	
	/**
	 * ���� �߼� ����{'N' : ���ϰ�, 'Y' :������}
	 * @return broadcast_yn
	 */
	public String getBroadcast_yn () {
		return broadcast_yn;
	}
	
	/**
	 * ���� �߼� ����{'N' : ���ϰ�, 'Y' :������}
	 * @param broadcast_yn
	 */
	public void setBroadcast_yn ( String broadcast_yn ) {
		this.broadcast_yn = broadcast_yn;
	}
	
	/**
	 * �޽��� ����{1-���۴��, 2-������, 3-�Ϸ�}
	 * @return msg_status
	 */
	public String getMsg_status () {
		return msg_status;
	}
	
	/**
	 * �޽��� ����{1-���۴��, 2-������, 3-�Ϸ�}
	 * @param msg_status
	 */
	public void setMsg_status ( String msg_status ) {
		this.msg_status = msg_status;
	}
	
	/**
	 * ������ ��ȭ ��ȣ
	 * @return recipient_num
	 */
	public String getRecipient_num () {
		return recipient_num;
	}
	
	/**
	 * ������ ��ȭ ��ȣ
	 * @param recipient_num
	 */
	public void setRecipient_num ( String recipient_num ) {
		this.recipient_num = recipient_num;
	}
	
	/**
	 * G/W �����ð�
	 * @return date_mt_sent
	 */
	public Date getDate_mt_sent () {
		return date_mt_sent;
	}
	
	/**
	 * G/W �����ð�
	 * @param date_mt_sent
	 */
	public void setDate_mt_sent ( Date date_mt_sent ) {
		this.date_mt_sent = date_mt_sent;
	}
	
	/**
	 * �ܸ��� ���� �ð�
	 * @return date_rslt
	 */
	public Date getDate_rslt () {
		return date_rslt;
	}
	
	/**
	 * �ܸ��� ���� �ð�
	 * @param date_rslt
	 */
	public void setDate_rslt ( Date date_rslt ) {
		this.date_rslt = date_rslt;
	}
	
	/**
	 * ��� ���� �ð�
	 * @return date_mt_report
	 */
	public Date getDate_mt_report () {
		return date_mt_report;
	}
	
	/**
	 * ��� ���� �ð�
	 * @param date_mt_report
	 */
	public void setDate_mt_report ( Date date_mt_report ) {
		this.date_mt_report = date_mt_report;
	}
	
	/**
	 * ���� ���{1000-����, ��Ÿ-����}
	 * @return mt_report_code_ib
	 */
	public String getMt_report_code_ib () {
		return mt_report_code_ib;
	}
	
	/**
	 * ���� ���{1000-����, ��Ÿ-����}
	 * @param mt_report_code_ib
	 */
	public void setMt_report_code_ib ( String mt_report_code_ib ) {
		this.mt_report_code_ib = mt_report_code_ib;
	}
	
	/**
	 * ���� ��� �з�
	 * @return mt_report_code_ibtype
	 */
	public String getMt_report_code_ibtype () {
		return mt_report_code_ibtype;
	}
	
	/**
	 * ���� ��� �з�
	 * @param mt_report_code_ibtype
	 */
	public void setMt_report_code_ibtype ( String mt_report_code_ibtype ) {
		this.mt_report_code_ibtype = mt_report_code_ibtype;
	}
	
	/**
	 * ���Ÿ� ����
	 * @return carrier
	 */
	public Integer getCarrier () {
		return carrier == null ? 0 : carrier;
	}
	
	/**
	 * ���Ÿ� ����
	 * @param carrier
	 */
	public void setCarrier ( Integer carrier ) {
		this.carrier = carrier;
	}
	
	/**
	 * G/W ����
	 * @return rs_id
	 */
	public String getRs_id () {
		return rs_id;
	}
	
	/**
	 * G/W ����
	 * @param rs_id
	 */
	public void setRs_id ( String rs_id ) {
		this.rs_id = rs_id;
	}
	
	/**
	 * ���� ��û ��Ż�
	 * @return recipient_net
	 */
	public String getRecipient_net () {
		return recipient_net;
	}
	
	/**
	 * ���� ��û ��Ż�
	 * @param recipient_net
	 */
	public void setRecipient_net ( String recipient_net ) {
		this.recipient_net = recipient_net;
	}
	
	/**
	 * ���� ��û ��Ż� ���� �ִ� ��� ��ȣ ��� ���Ž� ������ ����
	 * @return recipient_npsend
	 */
	public String getRecipient_npsend () {
		return recipient_npsend;
	}
	
	/**
	 * ���� ��û ��Ż� ���� �ִ� ��� ��ȣ ��� ���Ž� ������ ����
	 * @param recipient_npsend
	 */
	public void setRecipient_npsend ( String recipient_npsend ) {
		this.recipient_npsend = recipient_npsend;
	}
	
	/**
	 * �����ڵ�{82}
	 * @return country_code
	 */
	public String getCountry_code () {
		return country_code;
	}
	
	/**
	 * �����ڵ�{82}
	 * @param country_code
	 */
	public void setCountry_code ( String country_code ) {
		this.country_code = country_code;
	}
	
	/**
	 * �޽��� CHARSET
	 * @return charset
	 */
	public String getCharset () {
		return charset;
	}
	
	/**
	 * �޽��� CHARSET
	 * @param charset
	 */
	public void setCharset ( String charset ) {
		this.charset = charset;
	}
	
	/**
	 * �޽��� ����
	 * @return msg_type
	 */
	public Long getMsg_type () {
		return msg_type;
	}
	
	/**
	 * �޽��� ����
	 * @param msg_type
	 */
	public void setMsg_type ( Long msg_type ) {
		this.msg_type = msg_type;
	}
	
	/**
	 * ��ȣȭ ��� ����
	 * @return crypto_yn
	 */
	public String getCrypto_yn () {
		return crypto_yn;
	}
	
	/**
	 * ��ȣȭ ��� ����
	 * @param crypto_yn
	 */
	public void setCrypto_yn ( String crypto_yn ) {
		this.crypto_yn = crypto_yn;
	}
	
	/**
	 * ���� ��ȿ �ð� ����
	 * @return ttl
	 */
	public Integer getTtl () {
		return ttl == null ? 0 : ttl;
	}
	
	/**
	 * ���� ��ȿ �ð� ����
	 * @param ttl
	 */
	public void setTtl ( Integer ttl ) {
		this.ttl = ttl;
	}
	
	/**
	 * EMMA ����ȭ�� ���Ǵ� EMMA ID
	 * @return emma_id
	 */
	public String getEmma_id () {
		return emma_id;
	}
	
	/**
	 * EMMA ����ȭ�� ���Ǵ� EMMA ID
	 * @param emma_id
	 */
	public void setEmma_id ( String emma_id ) {
		this.emma_id = emma_id;
	}
	
	/**
	 * ������ �������
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * ������ �������
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �޽�������
	 * @return subject
	 */
	public String getSubject () {
		return subject;
	}
	
	/**
	 * �޽�������
	 * @param subject
	 */
	public void setSubject ( String subject ) {
		this.subject = subject;
	}
	
	/**
	 * ���۸޽��� Ÿ��{0-�Ϲ�, 1-IBL Ÿ��}
	 * @return content_type
	 */
	public String getContent_type () {
		return content_type;
	}
	
	/**
	 * ���۸޽��� Ÿ��{0-�Ϲ�, 1-IBL Ÿ��}
	 * @param content_type
	 */
	public void setContent_type ( String content_type ) {
		this.content_type = content_type;
	}
	
	/**
	 * ���ۿ���ð�(HHmm)
	 * @return reservation_time
	 */
	public String getReservation_time () {
		return reservation_time;
	}
	
	/**
	 * ���ۿ���ð�(HHmm)
	 * @param reservation_time
	 */
	public void setReservation_time ( String reservation_time ) {
		this.reservation_time = reservation_time;
	}

	/**
	 * ���� �׷�Ű  
	 * @return
	 */
	public long getAttach_file_group_key() {
		return attach_file_group_key;
	}

	/**
	 * ���� �׷�Ű  
	 * @param attach_file_group_key
	 */
	public void setAttach_file_group_key(long attach_file_group_key) {
		this.attach_file_group_key = attach_file_group_key;
	}
	
	/**
	 * ���̷����� �ֹ���ȣ  
	 * @return
	 */
	public String getXo_order_no() {
		return xo_order_no;
	}

	/**
	 * ���̷����� �ֹ���ȣ  
	 * @param xo_order_no
	 */
	public void setXo_order_no(String xo_order_no) {
		this.xo_order_no = xo_order_no;
	}

	/**
	 * ����ھ��̵�
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * ����ھ��̵�
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}	
}
