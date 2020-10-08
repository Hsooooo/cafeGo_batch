package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * WholecakeOrderNotiDto - 홀케익 예약 상태 알림
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class WholecakeOrderNotiDto {

	private String order_no;                 	// 예약번호
	private String status;                   	// 예약/선물상태
	private String noti_type;                	// 알림 타입{M-MMS, L-LMS, S-SMS, E-EMAIL}
	private String noti_user;                	// 알림 대상{O-예약자, P-선물수신자}
	private long   mt_pr;                    	// 메세지 고유 아이디
	private long   email_seq;                	// 이메일 발송 일련번호
	private Date   reg_date;                 	// 등록일

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("  order_no=" ).append(this.order_no);
		sb.append(", status="   ).append(this.status);
		sb.append(", noti_type=").append(this.noti_type);
		sb.append(", noti_user=").append(this.noti_user);
		sb.append(", mt_pr="    ).append(this.mt_pr);
		sb.append(", email_seq=").append(this.email_seq);
		sb.append(", reg_date=" ).append(this.reg_date);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 예약번호
	 * @return order_no
	 */
	public String getOrder_no() {
		return order_no;
	}

	/**
	 * 예약번호
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	/**
	 * 예약/선물상태
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 예약/선물상태
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 알림 타입{M-MMS, L-LMS, S-SMS, E-EMAIL}
	 * @return noti_type
	 */
	public String getNoti_type() {
		return noti_type;
	}

	/**
	 * 알림 타입{M-MMS, L-LMS, S-SMS, E-EMAIL}
	 * @param noti_type
	 */
	public void setNoti_type(String noti_type) {
		this.noti_type = noti_type;
	}
	
	/**
	 * 알림 대상{O-예약자, P-선물수신자}
	 * @return noti_user
	 */
	public String getNoti_user() {
		return noti_user;
	}

	/**
	 * 알림 대상{O-예약자, P-선물수신자}
	 * @param noti_user
	 */
	public void setNoti_user(String noti_user) {
		this.noti_user = noti_user;
	}
	
	/**
	 * 메세지 고유 아이디
	 * @return mt_pr
	 */
	public long getMt_pr() {
		return mt_pr;
	}

	/**
	 * 메세지 고유 아이디
	 * @param mt_pr
	 */
	public void setMt_pr(long mt_pr) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * 이메일 발송 일련번호
	 * @return email_seq
	 */
	public long getEmail_seq() {
		return email_seq;
	}

	/**
	 * 이메일 발송 일련번호
	 * @param email_seq
	 */
	public void setEmail_seq(long email_seq) {
		this.email_seq = email_seq;
	}
	
	/**
	 * 등록일
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 등록일
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
}
