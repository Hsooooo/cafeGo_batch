package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * WholecakeOrderNotiDto - Ȧ���� ���� ���� �˸�
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class WholecakeOrderNotiDto {

	private String order_no;                 	// �����ȣ
	private String status;                   	// ����/��������
	private String noti_type;                	// �˸� Ÿ��{M-MMS, L-LMS, S-SMS, E-EMAIL}
	private String noti_user;                	// �˸� ���{O-������, P-����������}
	private long   mt_pr;                    	// �޼��� ���� ���̵�
	private long   email_seq;                	// �̸��� �߼� �Ϸù�ȣ
	private Date   reg_date;                 	// �����

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
	 * �����ȣ
	 * @return order_no
	 */
	public String getOrder_no() {
		return order_no;
	}

	/**
	 * �����ȣ
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	/**
	 * ����/��������
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * ����/��������
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * �˸� Ÿ��{M-MMS, L-LMS, S-SMS, E-EMAIL}
	 * @return noti_type
	 */
	public String getNoti_type() {
		return noti_type;
	}

	/**
	 * �˸� Ÿ��{M-MMS, L-LMS, S-SMS, E-EMAIL}
	 * @param noti_type
	 */
	public void setNoti_type(String noti_type) {
		this.noti_type = noti_type;
	}
	
	/**
	 * �˸� ���{O-������, P-����������}
	 * @return noti_user
	 */
	public String getNoti_user() {
		return noti_user;
	}

	/**
	 * �˸� ���{O-������, P-����������}
	 * @param noti_user
	 */
	public void setNoti_user(String noti_user) {
		this.noti_user = noti_user;
	}
	
	/**
	 * �޼��� ���� ���̵�
	 * @return mt_pr
	 */
	public long getMt_pr() {
		return mt_pr;
	}

	/**
	 * �޼��� ���� ���̵�
	 * @param mt_pr
	 */
	public void setMt_pr(long mt_pr) {
		this.mt_pr = mt_pr;
	}
	
	/**
	 * �̸��� �߼� �Ϸù�ȣ
	 * @return email_seq
	 */
	public long getEmail_seq() {
		return email_seq;
	}

	/**
	 * �̸��� �߼� �Ϸù�ȣ
	 * @param email_seq
	 */
	public void setEmail_seq(long email_seq) {
		this.email_seq = email_seq;
	}
	
	/**
	 * �����
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * �����
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
}
