package co.kr.istarbucks.xo.batch.common.dto.xo;

import org.apache.commons.lang.StringUtils;

public class OrderAutoPushDto {
	private String order_no;           //주문번호
	private String bds_no;             //BDS no
	private String user_id;            //사용자 ID
	private String push_id;            //PUSH ID
	private String os_type;            //단말구분
	private String status;             //상태{31-주문완료, 22-주문취소}
	private String push_cnt;           //푸시 발송 횟수
	private String receipt_order_no;   //영수증표시 주문번호
	private String reg_date;           //요청일자
	private String msg_type;           //메시지 타입{A:일반상품, B:특정상품}
	private String bds_name;           //BDS 이름
	
	/**
	 * 주문번호
	 * @return
	 */
	public String getOrder_no() {
		return order_no;
	}
	/**
	 * 주문번호
	 * @param order_no
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	/**
	 * 사용자 ID
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * 사용자 ID
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
	 * 단말구분
	 * @return
	 */
	public String getOs_type() {
		return os_type;
	}
	/**
	 * 단말구분
	 * @param os_type
	 */
	public void setOs_type(String os_type) {
		this.os_type = os_type;
	}
	/**
	 * 상태{31-주문완료, 22-주문취소}
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 상태{31-주문완료, 22-주문취소}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 푸시 발송 횟수
	 * @return
	 */
	public String getPush_cnt() {
		return push_cnt;
	}
	/**
	 * 푸시 발송 횟수
	 * @param push_cnt
	 */
	public void setPush_cnt(String push_cnt) {
		this.push_cnt = push_cnt;
	}
	/**
	 * 영수증표시 주문번호
	 * @return
	 */
	public String getReceipt_order_no() {
		return receipt_order_no;
	}
	/**
	 * 영수증표시 주문번호
	 * @param receipt_order_no
	 */
	public void setReceipt_order_no(String receipt_order_no) {
		this.receipt_order_no = receipt_order_no;
	}
	/**
	 * 요청일자
	 * @return
	 */
	public String getReg_date() {
		return reg_date;
	}
	/**
	 * 요청일자
	 * @param reg_date
	 */
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	/**
	 * 메시지 타입{A-일반, B-특정상품}
	 * @return
	 */
	public String getMsg_type() {
		return StringUtils.defaultIfEmpty(msg_type, "");
	}
	/**
	 * 메시지 타입{A-일반, B-특정상품}
	 * @param msg_type
	 */
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	/**
	 * BDS 이름
	 * @return
	 */
	public String getBds_name() {
		return StringUtils.defaultIfEmpty(bds_name, "");
	}
	/**
	 * BDS 이름
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
