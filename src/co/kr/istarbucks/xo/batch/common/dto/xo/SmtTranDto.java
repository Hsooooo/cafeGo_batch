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
 * SMS 전송 SmtTranDto.
 * @author eZEN ksy
 * @since 2014. 2. 4.
 * @version $Revision: 1.3 $
 */
public class SmtTranDto {
	
	private Long    mt_pr; 					// 메시지 고유 아이디
	private String  mt_refkey; 				// 부서 코드
	private String  priority; 				// 전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
	private String  date_client_req; 		// 전송 예약 시간
	private String  content; 				// 전송 메시지
	private String  callback; 				// 발신자 전화 번호
	private String  service_type; 			// 서비스 메시지 전송 타입{0-SMS MT, 1-CALLBACK URL, 2-MMS MT, 3-LMS}
	private String  broadcast_yn; 			// 동보 발송 유무{'N' : 단일건, 'Y' :동보건}
	private String  msg_status; 			// 메시지 상태{1-전송대기, 2-결과대기, 3-완료}
	private String  recipient_num; 			// 수신자 전화 번호
	private Date    date_mt_sent; 			// G/W 접수시간
	private Date    date_rslt; 				// 단말기 도착 시간
	private Date    date_mt_report; 		// 결과 수신 시간
	private String  mt_report_code_ib; 		// 전송 결과{1000-성공, 기타-실패}
	private String  mt_report_code_ibtype; 	// 전송 결과 분류
	private Integer carrier; 				// 착신망 정보
	private String  rs_id; 					// G/W 정보
	private String  recipient_net; 			// 전송 요청 통신사
	private String  recipient_npsend; 		// 전송 요청 통신사 값이 있는 경우 번호 결과 수신시 재전송 여부
	private String  country_code; 			// 국가코드{82}
	private String  charset; 				// 메시지 CHARSET
	private Long    msg_type; 				// 메시지 종류
	private String  crypto_yn; 				// 암호화 사용 유무
	private Integer ttl; 					// 전송 유효 시간 설정
	private String  emma_id; 				// EMMA 이중화시 사용되는 EMMA ID
	private Date    reg_date; 				// 데이터 등록일자
	
	private String  subject; 				// 메시지제목
	private String  content_type; 			// 전송메시지 타입{0-일반, 1-IBL 타입}
	
	private String  reservation_time;		// 전송예약시간(HHmm)
	
	// 홀케익 MMS 발송. EM_MMT_TRAN 테이블에서 사용됨. 2016-10-28 
	private long    attach_file_group_key;	// 파일 그룹키  
	private String  xo_order_no;		    // 사이렌오더 주문번호
	private String user_id;	// 사용자아이디
	
	
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
	 * 메시지 고유 아이디
	 * @return mt_pr
	 */
	public Long getMt_pr () {
		return mt_pr;
	}
	
	/**
	 * 메시지 고유 아이디
	 * @param mt_pr
	 */
	public void setMt_pr ( Long mt_pr ) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * 부서 코드
	 * @return mt_refkey
	 */
	public String getMt_refkey () {
		return mt_refkey;
	}
	
	/**
	 * 부서 코드
	 * @param mt_refkey
	 */
	public void setMt_refkey ( String mt_refkey ) {
		this.mt_refkey = mt_refkey;
	}
	
	/**
	 * 전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
	 * @return priority
	 */
	public String getPriority () {
		return priority;
	}
	
	/**
	 * 전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
	 * @param priority
	 */
	public void setPriority ( String priority ) {
		this.priority = priority;
	}
	
	/**
	 * 전송 예약 시간
	 * @return date_client_req
	 */
	public String getDate_client_req () {
		return date_client_req;
	}
	
	/**
	 * 전송 예약 시간
	 * @param date_client_req
	 */
	public void setDate_client_req ( String date_client_req ) {
		this.date_client_req = date_client_req;
	}
	
	/**
	 * 전송 메시지
	 * @return content
	 */
	public String getContent () {
		return content;
	}
	
	/**
	 * 전송 메시지
	 * @param content
	 */
	public void setContent ( String content ) {
		this.content = content;
	}
	
	/**
	 * 발신자 전화 번호
	 * @return callback
	 */
	public String getCallback () {
		return callback;
	}
	
	/**
	 * 발신자 전화 번호
	 * @param callback
	 */
	public void setCallback ( String callback ) {
		this.callback = callback;
	}
	
	/**
	 * 서비스 메시지 전송 타입{0-SMS MT, 1-CALLBACK URL}
	 * @return service_type
	 */
	public String getService_type () {
		return service_type;
	}
	
	/**
	 * 서비스 메시지 전송 타입{0-SMS MT, 1-CALLBACK URL}
	 * @param service_type
	 */
	public void setService_type ( String service_type ) {
		this.service_type = service_type;
	}
	
	/**
	 * 동보 발송 유무{'N' : 단일건, 'Y' :동보건}
	 * @return broadcast_yn
	 */
	public String getBroadcast_yn () {
		return broadcast_yn;
	}
	
	/**
	 * 동보 발송 유무{'N' : 단일건, 'Y' :동보건}
	 * @param broadcast_yn
	 */
	public void setBroadcast_yn ( String broadcast_yn ) {
		this.broadcast_yn = broadcast_yn;
	}
	
	/**
	 * 메시지 상태{1-전송대기, 2-결과대기, 3-완료}
	 * @return msg_status
	 */
	public String getMsg_status () {
		return msg_status;
	}
	
	/**
	 * 메시지 상태{1-전송대기, 2-결과대기, 3-완료}
	 * @param msg_status
	 */
	public void setMsg_status ( String msg_status ) {
		this.msg_status = msg_status;
	}
	
	/**
	 * 수신자 전화 번호
	 * @return recipient_num
	 */
	public String getRecipient_num () {
		return recipient_num;
	}
	
	/**
	 * 수신자 전화 번호
	 * @param recipient_num
	 */
	public void setRecipient_num ( String recipient_num ) {
		this.recipient_num = recipient_num;
	}
	
	/**
	 * G/W 접수시간
	 * @return date_mt_sent
	 */
	public Date getDate_mt_sent () {
		return date_mt_sent;
	}
	
	/**
	 * G/W 접수시간
	 * @param date_mt_sent
	 */
	public void setDate_mt_sent ( Date date_mt_sent ) {
		this.date_mt_sent = date_mt_sent;
	}
	
	/**
	 * 단말기 도착 시간
	 * @return date_rslt
	 */
	public Date getDate_rslt () {
		return date_rslt;
	}
	
	/**
	 * 단말기 도착 시간
	 * @param date_rslt
	 */
	public void setDate_rslt ( Date date_rslt ) {
		this.date_rslt = date_rslt;
	}
	
	/**
	 * 결과 수신 시간
	 * @return date_mt_report
	 */
	public Date getDate_mt_report () {
		return date_mt_report;
	}
	
	/**
	 * 결과 수신 시간
	 * @param date_mt_report
	 */
	public void setDate_mt_report ( Date date_mt_report ) {
		this.date_mt_report = date_mt_report;
	}
	
	/**
	 * 전송 결과{1000-성공, 기타-실패}
	 * @return mt_report_code_ib
	 */
	public String getMt_report_code_ib () {
		return mt_report_code_ib;
	}
	
	/**
	 * 전송 결과{1000-성공, 기타-실패}
	 * @param mt_report_code_ib
	 */
	public void setMt_report_code_ib ( String mt_report_code_ib ) {
		this.mt_report_code_ib = mt_report_code_ib;
	}
	
	/**
	 * 전송 결과 분류
	 * @return mt_report_code_ibtype
	 */
	public String getMt_report_code_ibtype () {
		return mt_report_code_ibtype;
	}
	
	/**
	 * 전송 결과 분류
	 * @param mt_report_code_ibtype
	 */
	public void setMt_report_code_ibtype ( String mt_report_code_ibtype ) {
		this.mt_report_code_ibtype = mt_report_code_ibtype;
	}
	
	/**
	 * 착신망 정보
	 * @return carrier
	 */
	public Integer getCarrier () {
		return carrier == null ? 0 : carrier;
	}
	
	/**
	 * 착신망 정보
	 * @param carrier
	 */
	public void setCarrier ( Integer carrier ) {
		this.carrier = carrier;
	}
	
	/**
	 * G/W 정보
	 * @return rs_id
	 */
	public String getRs_id () {
		return rs_id;
	}
	
	/**
	 * G/W 정보
	 * @param rs_id
	 */
	public void setRs_id ( String rs_id ) {
		this.rs_id = rs_id;
	}
	
	/**
	 * 전송 요청 통신사
	 * @return recipient_net
	 */
	public String getRecipient_net () {
		return recipient_net;
	}
	
	/**
	 * 전송 요청 통신사
	 * @param recipient_net
	 */
	public void setRecipient_net ( String recipient_net ) {
		this.recipient_net = recipient_net;
	}
	
	/**
	 * 전송 요청 통신사 값이 있는 경우 번호 결과 수신시 재전송 여부
	 * @return recipient_npsend
	 */
	public String getRecipient_npsend () {
		return recipient_npsend;
	}
	
	/**
	 * 전송 요청 통신사 값이 있는 경우 번호 결과 수신시 재전송 여부
	 * @param recipient_npsend
	 */
	public void setRecipient_npsend ( String recipient_npsend ) {
		this.recipient_npsend = recipient_npsend;
	}
	
	/**
	 * 국가코드{82}
	 * @return country_code
	 */
	public String getCountry_code () {
		return country_code;
	}
	
	/**
	 * 국가코드{82}
	 * @param country_code
	 */
	public void setCountry_code ( String country_code ) {
		this.country_code = country_code;
	}
	
	/**
	 * 메시지 CHARSET
	 * @return charset
	 */
	public String getCharset () {
		return charset;
	}
	
	/**
	 * 메시지 CHARSET
	 * @param charset
	 */
	public void setCharset ( String charset ) {
		this.charset = charset;
	}
	
	/**
	 * 메시지 종류
	 * @return msg_type
	 */
	public Long getMsg_type () {
		return msg_type;
	}
	
	/**
	 * 메시지 종류
	 * @param msg_type
	 */
	public void setMsg_type ( Long msg_type ) {
		this.msg_type = msg_type;
	}
	
	/**
	 * 암호화 사용 유무
	 * @return crypto_yn
	 */
	public String getCrypto_yn () {
		return crypto_yn;
	}
	
	/**
	 * 암호화 사용 유무
	 * @param crypto_yn
	 */
	public void setCrypto_yn ( String crypto_yn ) {
		this.crypto_yn = crypto_yn;
	}
	
	/**
	 * 전송 유효 시간 설정
	 * @return ttl
	 */
	public Integer getTtl () {
		return ttl == null ? 0 : ttl;
	}
	
	/**
	 * 전송 유효 시간 설정
	 * @param ttl
	 */
	public void setTtl ( Integer ttl ) {
		this.ttl = ttl;
	}
	
	/**
	 * EMMA 이중화시 사용되는 EMMA ID
	 * @return emma_id
	 */
	public String getEmma_id () {
		return emma_id;
	}
	
	/**
	 * EMMA 이중화시 사용되는 EMMA ID
	 * @param emma_id
	 */
	public void setEmma_id ( String emma_id ) {
		this.emma_id = emma_id;
	}
	
	/**
	 * 데이터 등록일자
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * 데이터 등록일자
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 메시지제목
	 * @return subject
	 */
	public String getSubject () {
		return subject;
	}
	
	/**
	 * 메시지제목
	 * @param subject
	 */
	public void setSubject ( String subject ) {
		this.subject = subject;
	}
	
	/**
	 * 전송메시지 타입{0-일반, 1-IBL 타입}
	 * @return content_type
	 */
	public String getContent_type () {
		return content_type;
	}
	
	/**
	 * 전송메시지 타입{0-일반, 1-IBL 타입}
	 * @param content_type
	 */
	public void setContent_type ( String content_type ) {
		this.content_type = content_type;
	}
	
	/**
	 * 전송예약시간(HHmm)
	 * @return reservation_time
	 */
	public String getReservation_time () {
		return reservation_time;
	}
	
	/**
	 * 전송예약시간(HHmm)
	 * @param reservation_time
	 */
	public void setReservation_time ( String reservation_time ) {
		this.reservation_time = reservation_time;
	}

	/**
	 * 파일 그룹키  
	 * @return
	 */
	public long getAttach_file_group_key() {
		return attach_file_group_key;
	}

	/**
	 * 파일 그룹키  
	 * @param attach_file_group_key
	 */
	public void setAttach_file_group_key(long attach_file_group_key) {
		this.attach_file_group_key = attach_file_group_key;
	}
	
	/**
	 * 사이렌오더 주문번호  
	 * @return
	 */
	public String getXo_order_no() {
		return xo_order_no;
	}

	/**
	 * 사이렌오더 주문번호  
	 * @param xo_order_no
	 */
	public void setXo_order_no(String xo_order_no) {
		this.xo_order_no = xo_order_no;
	}

	/**
	 * 사용자아이디
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * 사용자아이디
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}	
}
