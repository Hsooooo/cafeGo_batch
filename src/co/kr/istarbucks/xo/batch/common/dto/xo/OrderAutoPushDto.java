package co.kr.istarbucks.xo.batch.common.dto.xo;

import org.apache.commons.lang.StringUtils;

public class OrderAutoPushDto {
	private String order_no;           //�ֹ���ȣ
	private String bds_no;             //BDS no
	private String user_id;            //����� ID
	private String push_id;            //PUSH ID
	private String os_type;            //�ܸ�����
	private String status;             //����{31-�ֹ��Ϸ�, 22-�ֹ����}
	private String push_cnt;           //Ǫ�� �߼� Ƚ��
	private String receipt_order_no;   //������ǥ�� �ֹ���ȣ
	private String reg_date;           //��û����
	private String msg_type;           //�޽��� Ÿ��{A:�Ϲݻ�ǰ, B:Ư����ǰ}
	private String bds_name;           //BDS �̸�
	
	/**
	 * �ֹ���ȣ
	 * @return
	 */
	public String getOrder_no() {
		return order_no;
	}
	/**
	 * �ֹ���ȣ
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	/**
	 * ����� ID
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * ����� ID
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * PUSH ID
	 * @return
	 */
	public String getPush_id() {
		return push_id;
	}
	/**
	 * PUSH ID
	 * @param push_id
	 */
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
	/**
	 * �ܸ�����
	 * @return
	 */
	public String getOs_type() {
		return os_type;
	}
	/**
	 * �ܸ�����
	 * @param os_type
	 */
	public void setOs_type(String os_type) {
		this.os_type = os_type;
	}
	/**
	 * ����{31-�ֹ��Ϸ�, 22-�ֹ����}
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * ����{31-�ֹ��Ϸ�, 22-�ֹ����}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * Ǫ�� �߼� Ƚ��
	 * @return
	 */
	public String getPush_cnt() {
		return push_cnt;
	}
	/**
	 * Ǫ�� �߼� Ƚ��
	 * @param push_cnt
	 */
	public void setPush_cnt(String push_cnt) {
		this.push_cnt = push_cnt;
	}
	/**
	 * ������ǥ�� �ֹ���ȣ
	 * @return
	 */
	public String getReceipt_order_no() {
		return receipt_order_no;
	}
	/**
	 * ������ǥ�� �ֹ���ȣ
	 * @param receipt_order_no
	 */
	public void setReceipt_order_no(String receipt_order_no) {
		this.receipt_order_no = receipt_order_no;
	}
	/**
	 * ��û����
	 * @return
	 */
	public String getReg_date() {
		return reg_date;
	}
	/**
	 * ��û����
	 * @param reg_date
	 */
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	/**
	 * �޽��� Ÿ��{A-�Ϲ�, B-Ư����ǰ}
	 * @return
	 */
	public String getMsg_type() {
		return StringUtils.defaultIfEmpty(msg_type, "");
	}
	/**
	 * �޽��� Ÿ��{A-�Ϲ�, B-Ư����ǰ}
	 * @param msg_type
	 */
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	/**
	 * BDS �̸�
	 * @return
	 */
	public String getBds_name() {
		return StringUtils.defaultIfEmpty(bds_name, "");
	}
	/**
	 * BDS �̸�
	 * @param bds_name
	 */
	public void setBds_name(String bds_name) {
		this.bds_name = bds_name;
	}
	/**
	 * BDS_NO
	 * @return bds_no
	 */
	public String getBds_no() {
		return bds_no;
	}
	/**
	 * BDS_NO
	 * @param bds_no
	 */
	public void setBds_no(String bds_no) {
		this.bds_no = bds_no;
	}
	
}
