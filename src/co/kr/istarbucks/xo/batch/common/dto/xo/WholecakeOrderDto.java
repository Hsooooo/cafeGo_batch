/*
 * @(#) $Id: WholecakeOrderDto.java,v 1.4 2016/11/15 04:38:51 dev99 Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ȧ���Ϳ��� WholecakeOrderDto.
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.4 $
 */
public class WholecakeOrderDto {
	String order_no;                  // �����ȣ
	String cart_no;                   // �ֹ�����ȣ
	String policy_cd;                 // ��å �ڵ�
	String order_name;                // �����
	String user_id;                   // ����ھ��̵�
	String user_name;                 // ����ڸ�
	String user_mobile;               // ����� �޴�����ȣ{��ȣȭ}
	String user_email;                // ����� �̸���{��ȣȭ}
	String user_nickname;             // ����� �г���
	String msr_user_flag;             // MSRȸ������{Y-ȸ��, N-��ȸ��}
	String msr_user_grade;            // MSRȸ�����
	String status;                    // ����{�ڵ�ǥ����}
	String gstdgr;                    // �����ڵ�
	String os_type;                   // �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	String push_id;                   // PUSH ID
	String guide_code;                // ����ȳ� �ڵ�
	int    guide_version;             // ����ȳ� ����
	String cancel_policy_code;        // ���/ȯ�� ��å �ڵ�
	int    cancel_policy_version;     // ���/ȯ�� ��å ����
	String receive_store_cd;          // ���� �����ڵ�
	String receive_date;              // ���ɿ�����{YYYYMMDD}
	String sales_order_date;          // ����Ȯ����{YYYYMMDD}
	String revocable_date;            // ��Ұ�����{YYYYMMDD}
	int    total_qty;                 // ��ü ����
	int    total_pay_amt;             // �� �����ݾ�{�� ǰ���Ѿ� - �� ���ξ�}
	int    total_discount;            // �� ���ξ�
	int    total_coupon;              // �� ������
	int    total_item_amount;         // �� ǰ���Ѿ�
	String receipt_type;              // ���ݿ����� ���౸��
	String receipt_number;            // ���ݿ����� �����ȣ{��ȣȭ}
	int    receipt_amount;            // ���ݿ����� ���ݾ�
	String emp_no;                    // ������ȣ
	String present_no;                // ������ȣ
	String receiver_name;             // �����ڸ�
	String receiver_mobile;           // ������ �޴�����ȣ{��ȣȭ}
	String present_message;           // ���� �޼���
	String present_notice_code;       // ���� ���ǻ��� �ڵ�
	int    present_notice_version;    // ���� ���ǻ��� ����
	String present_status;            // ���� ����{�ڵ�ǥ����}
	String receive_type;              // ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	String cashier_id;                // ��Ʈ��ID
	String cashier_name;              // ��Ʈ�ʸ�
	String sale_date;                 // ������{YYYYMMDD}
	String store_cd;                  // �����ڵ�
	String pos_no;                    // POS��ȣ
	String seq_no;                    // �ŷ���ȣ
	String pos_comp_flag;             // ����Ȯ������{Y-Ȯ��}
	String sales_order_comp_flag;     // ����Ȯ������{Y-Ȯ��}
	String trade_flag;                // ���ϷῩ��{Y-�Ϸ�}
	String critical_flag;             // ��������
	String use_flag;                  // ��뿩��{Y-���, N-�̻��}
	Date   reg_date;                  // �����
	Date   pay_date;                  // ���� �Ϸ�����
	Date   mod_date;                  // ���� ��������
	Date   receive_comp_date;         // ����/�̼��� �Ϸ�����
	Date   cancel_date;               // ���� �������
	Date   receiver_mobile_del_date;  // ������ �޴�����ȣ ��������
	String coupon_pub_flag;           // �������࿩��{Y-����}
	
	String present_resend_yn;         // ������߼ۿ���_YN
	String order_date; 				  // ��������{YYYYMMDD}
	
	// ���� ������ ���� �߰� �׸�
	int           coupon_count;              // Ȧ���� ���� ��å�� ���� ���� ���� 
	int           total_coupon_count;        // Ȧ���� ������ �� ���� ���� ���� ((��ü ����-Ȧ������������)*Ȧ������å�� ���� ���� ����)
	int           publication_coupon_count;  // ���� ������ ���� �� 
	List<String>  success_coupon_list = new ArrayList<String>();       // ���� ���� �� ���� �߼� ������ ���� ���
	List<String>  fail_coupon_list    = new ArrayList<String>();	     // MMS ���� �߼� ������ ���� ��� 
	
	// XO_WHOLECAKE_ORDER_DETAIL���� ���
	int    item_seq;				  // �󼼼���
	String sku_no;					  // SKU�ڵ�
	int    price;					  // �ܰ�
	int    qty;						  // ����
	int    item_amount;				  // ǰ���Ѿ�(�ݾ�*����)
	int    discount;				  // ���ξ�(ǰ���Ѿ� *(���������� *0.1))
	int    gnd_amount;				  // �������
	String cate_type;				  // ī�װ�����
	String coupon_number;			  // �����Ϸù�ȣ
	int    coupon_dc_amt;			  // �������αݾ�
	String sck_coupon_no;			  // ��������������ȣ
	
	// ���� Ȯ�� LMS �߼��� ���� �׸�
	String receive_store_name;        // ���� ���� ��
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append(", order_no=").append(this.order_no);
		sb.append(", cart_no=").append(this.cart_no);
		sb.append(", policy_cd=").append(this.policy_cd);
		sb.append(", order_name=").append(this.order_name);
		sb.append(", user_id=").append(this.user_id);
		sb.append(", user_name=").append(this.user_name);
		sb.append(", user_mobile=").append(this.user_mobile);
		sb.append(", user_email=").append(this.user_email);
		sb.append(", user_nickname=").append(this.user_nickname);
		sb.append(", msr_user_flag=").append(this.msr_user_flag);
		sb.append(", msr_user_grade=").append(this.msr_user_grade);
		sb.append(", status=").append(this.status);
		sb.append(", gstdgr=").append(this.gstdgr);
		sb.append(", os_type=").append(this.os_type);
		sb.append(", push_id=").append(this.push_id);
		sb.append(", guide_code=").append(this.guide_code);
		sb.append(", guide_version=").append(this.guide_version);
		sb.append(", cancel_policy_code=").append(this.cancel_policy_code);
		sb.append(", cancel_policy_version=").append(this.cancel_policy_version);
		sb.append(", receive_store_cd=").append(this.receive_store_cd);
		sb.append(", receive_date=").append(this.receive_date);
		sb.append(", sales_order_date=").append(this.sales_order_date);
		sb.append(", revocable_date=").append(this.revocable_date);
		sb.append(", total_qty=").append(this.total_qty);
		sb.append(", total_pay_amt=").append(this.total_pay_amt);
		sb.append(", total_discount=").append(this.total_discount);
		sb.append(", total_coupon=").append(this.total_coupon);
		sb.append(", total_item_amount=").append(this.total_item_amount);
		sb.append(", receipt_type=").append(this.receipt_type);
		sb.append(", receipt_number=").append(this.receipt_number);
		sb.append(", receipt_amount=").append(this.receipt_amount);
		sb.append(", emp_no=").append(this.emp_no);
		sb.append(", present_no=").append(this.present_no);
		sb.append(", receiver_name=").append(this.receiver_name);
		sb.append(", receiver_mobile=").append(this.receiver_mobile);
		sb.append(", present_message=").append(this.present_message);
		sb.append(", present_notice_code=").append(this.present_notice_code);
		sb.append(", present_notice_version=").append(this.present_notice_version);
		sb.append(", present_status=").append(this.present_status);
		sb.append(", receive_type=").append(this.receive_type);
		sb.append(", cashier_id=").append(this.cashier_id);
		sb.append(", cashier_name=").append(this.cashier_name);
		sb.append(", sale_date=").append(this.sale_date);
		sb.append(", store_cd=").append(this.store_cd);
		sb.append(", pos_no=").append(this.pos_no);
		sb.append(", seq_no=").append(this.seq_no);
		sb.append(", pos_comp_flag=").append(this.pos_comp_flag);
		sb.append(", sales_order_comp_flag=").append(this.sales_order_comp_flag);
		sb.append(", trade_flag=").append(this.trade_flag);
		sb.append(", critical_flag=").append(this.critical_flag);
		sb.append(", use_flag=").append(this.use_flag);
		sb.append(", reg_date=").append(this.reg_date);
		sb.append(", pay_date=").append(this.pay_date);
		sb.append(", mod_date=").append(this.mod_date);
		sb.append(", receive_comp_date=").append(this.receive_comp_date);
		sb.append(", cancel_date=").append(this.cancel_date);
		sb.append(", receiver_mobile_del_date=").append(this.receiver_mobile_del_date);
		sb.append(", coupon_pub_flag=").append(this.coupon_pub_flag);
		sb.append(", coupon_count=").append(this.coupon_count);
		sb.append(", total_coupon_count=").append(this.total_coupon_count);
		sb.append(", publication_coupon_count=").append(this.publication_coupon_count);
		sb.append(", order_date=").append(this.order_date);
		sb.append(", item_seq=").append(this.item_seq);
		sb.append(", sku_no=").append(this.sku_no);
		sb.append(", price=").append(this.price);
		sb.append(", qty=").append(this.qty);
		sb.append(", item_amount=").append(this.item_amount);
		sb.append(", discount=").append(this.discount);
		sb.append(", gnd_amount=").append(this.gnd_amount);
		sb.append(", cate_type=").append(this.cate_type);
		sb.append(", coupon_number=").append(this.coupon_number);
		sb.append(", coupon_dc_amt=").append(this.coupon_dc_amt);
		sb.append(", sck_coupon_no=").append(this.sck_coupon_no);
		sb.append(", present_resend_yn=").append(this.present_resend_yn);
		sb.append(", success_coupon_list=").append(this.success_coupon_list);
		sb.append(", fail_coupon_list=").append(this.fail_coupon_list);
		sb.append(", receive_store_name=").append(this.receive_store_name);
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
	 * �ֹ�����ȣ
	 * @return cart_no
	 */
	public String getCart_no() {
		return cart_no;
	}

	/**
	 * �ֹ�����ȣ
	 * @param cart_no
	 */
	public void setCart_no(String cart_no) {
		this.cart_no = cart_no;
	}
	
	/**
	 * ��å �ڵ�
	 * @return policy_cd
	 */
	public String getPolicy_cd() {
		return policy_cd;
	}

	/**
	 * ��å �ڵ�
	 * @param policy_cd
	 */
	public void setPolicy_cd(String policy_cd) {
		this.policy_cd = policy_cd;
	}
	
	/**
	 * �����
	 * @return order_name
	 */
	public String getOrder_name() {
		return order_name;
	}

	/**
	 * �����
	 * @param order_name
	 */
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	
	/**
	 * ����ھ��̵�
	 * @return user_id
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
	
	/**
	 * ����ڸ�
	 * @return user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * ����ڸ�
	 * @param user_name
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	/**
	 * ����� �޴�����ȣ{��ȣȭ}
	 * @return user_mobile
	 */
	public String getUser_mobile() {
		return user_mobile;
	}

	/**
	 * ����� �޴�����ȣ{��ȣȭ}
	 * @param user_mobile
	 */
	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}
	
	/**
	 * ����� �̸���{��ȣȭ}
	 * @return user_email
	 */
	public String getUser_email() {
		return user_email;
	}

	/**
	 * ����� �̸���{��ȣȭ}
	 * @param user_email
	 */
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	
	/**
	 * ����� �г���
	 * @return user_nickname
	 */
	public String getUser_nickname() {
		return user_nickname;
	}

	/**
	 * ����� �г���
	 * @param user_nickname
	 */
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	
	/**
	 * MSRȸ������{Y-ȸ��, N-��ȸ��}
	 * @return msr_user_flag
	 */
	public String getMsr_user_flag() {
		return msr_user_flag;
	}

	/**
	 * MSRȸ������{Y-ȸ��, N-��ȸ��}
	 * @param msr_user_flag
	 */
	public void setMsr_user_flag(String msr_user_flag) {
		this.msr_user_flag = msr_user_flag;
	}
	
	/**
	 * MSRȸ�����
	 * @return msr_user_grade
	 */
	public String getMsr_user_grade() {
		return msr_user_grade;
	}

	/**
	 * MSRȸ�����
	 * @param msr_user_grade
	 */
	public void setMsr_user_grade(String msr_user_grade) {
		this.msr_user_grade = msr_user_grade;
	}
	
	/**
	 * ����{�ڵ�ǥ����}
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * ����{�ڵ�ǥ����}
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * �����ڵ�
	 * @return gstdgr
	 */
	public String getGstdgr() {
		return gstdgr;
	}

	/**
	 * �����ڵ�
	 * @param gstdgr
	 */
	public void setGstdgr(String gstdgr) {
		this.gstdgr = gstdgr;
	}
	
	/**
	 * �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	 * @return os_type
	 */
	public String getOs_type() {
		return os_type;
	}

	/**
	 * �ܸ�����{1-iOS, 2-Android, 9-��Ÿ}
	 * @param os_type
	 */
	public void setOs_type(String os_type) {
		this.os_type = os_type;
	}
	
	/**
	 * PUSH ID
	 * @return push_id
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
	 * ����ȳ� �ڵ�
	 * @return guide_code
	 */
	public String getGuide_code() {
		return guide_code;
	}

	/**
	 * ����ȳ� �ڵ�
	 * @param guide_code
	 */
	public void setGuide_code(String guide_code) {
		this.guide_code = guide_code;
	}
	
	/**
	 * ����ȳ� ����
	 * @return guide_version
	 */
	public int getGuide_version() {
		return guide_version;
	}

	/**
	 * ����ȳ� ����
	 * @param guide_version
	 */
	public void setGuide_version(int guide_version) {
		this.guide_version = guide_version;
	}
	
	/**
	 * ���/ȯ�� ��å �ڵ�
	 * @return cancel_policy_code
	 */
	public String getCancel_policy_code() {
		return cancel_policy_code;
	}

	/**
	 * ���/ȯ�� ��å �ڵ�
	 * @param cancel_policy_code
	 */
	public void setCancel_policy_code(String cancel_policy_code) {
		this.cancel_policy_code = cancel_policy_code;
	}
	
	/**
	 * ���/ȯ�� ��å ����
	 * @return cancel_policy_version
	 */
	public int getCancel_policy_version() {
		return cancel_policy_version;
	}

	/**
	 * ���/ȯ�� ��å ����
	 * @param cancel_policy_version
	 */
	public void setCancel_policy_version(int cancel_policy_version) {
		this.cancel_policy_version = cancel_policy_version;
	}
	
	/**
	 * ���� �����ڵ�
	 * @return receive_store_cd
	 */
	public String getReceive_store_cd() {
		return receive_store_cd;
	}

	/**
	 * ���� �����ڵ�
	 * @param receive_store_cd
	 */
	public void setReceive_store_cd(String receive_store_cd) {
		this.receive_store_cd = receive_store_cd;
	}
	
	/**
	 * ���ɿ�����{YYYYMMDD}
	 * @return receive_date
	 */
	public String getReceive_date() {
		return receive_date;
	}

	/**
	 * ���ɿ�����{YYYYMMDD}
	 * @param receive_date
	 */
	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
	}
	
	/**
	 * ����Ȯ����{YYYYMMDD}
	 * @return sales_order_date
	 */
	public String getSales_order_date() {
		return sales_order_date;
	}

	/**
	 * ����Ȯ����{YYYYMMDD}
	 * @param sales_order_date
	 */
	public void setSales_order_date(String sales_order_date) {
		this.sales_order_date = sales_order_date;
	}
	
	/**
	 * ��Ұ�����{YYYYMMDD}
	 * @return revocable_date
	 */
	public String getRevocable_date() {
		return revocable_date;
	}

	/**
	 * ��Ұ�����{YYYYMMDD}
	 * @param revocable_date
	 */
	public void setRevocable_date(String revocable_date) {
		this.revocable_date = revocable_date;
	}
	
	/**
	 * ��ü ����
	 * @return total_qty
	 */
	public int getTotal_qty() {
		return total_qty;
	}

	/**
	 * ��ü ����
	 * @param total_qty
	 */
	public void setTotal_qty(int total_qty) {
		this.total_qty = total_qty;
	}
	
	/**
	 * �� �����ݾ�{�� ǰ���Ѿ� - �� ���ξ�}
	 * @return total_pay_amt
	 */
	public int getTotal_pay_amt() {
		return total_pay_amt;
	}

	/**
	 * �� �����ݾ�{�� ǰ���Ѿ� - �� ���ξ�}
	 * @param total_pay_amt
	 */
	public void setTotal_pay_amt(int total_pay_amt) {
		this.total_pay_amt = total_pay_amt;
	}
	
	/**
	 * �� ���ξ�
	 * @return total_discount
	 */
	public int getTotal_discount() {
		return total_discount;
	}

	/**
	 * �� ���ξ�
	 * @param total_discount
	 */
	public void setTotal_discount(int total_discount) {
		this.total_discount = total_discount;
	}
	
	/**
	 * �� ������
	 * @return total_coupon
	 */
	public int getTotal_coupon() {
		return total_coupon;
	}

	/**
	 * �� ������
	 * @param total_coupon
	 */
	public void setTotal_coupon(int total_coupon) {
		this.total_coupon = total_coupon;
	}
	
	/**
	 * �� ǰ���Ѿ�
	 * @return total_item_amount
	 */
	public int getTotal_item_amount() {
		return total_item_amount;
	}

	/**
	 * �� ǰ���Ѿ�
	 * @param total_item_amount
	 */
	public void setTotal_item_amount(int total_item_amount) {
		this.total_item_amount = total_item_amount;
	}
	
	/**
	 * ���ݿ����� ���౸��
	 * @return receipt_type
	 */
	public String getReceipt_type() {
		return receipt_type;
	}

	/**
	 * ���ݿ����� ���౸��
	 * @param receipt_type
	 */
	public void setReceipt_type(String receipt_type) {
		this.receipt_type = receipt_type;
	}
	
	/**
	 * ���ݿ����� �����ȣ{��ȣȭ}
	 * @return receipt_number
	 */
	public String getReceipt_number() {
		return receipt_number;
	}

	/**
	 * ���ݿ����� �����ȣ{��ȣȭ}
	 * @param receipt_number
	 */
	public void setReceipt_number(String receipt_number) {
		this.receipt_number = receipt_number;
	}
	
	/**
	 * ���ݿ����� ���ݾ�
	 * @return receipt_amount
	 */
	public int getReceipt_amount() {
		return receipt_amount;
	}

	/**
	 * ���ݿ����� ���ݾ�
	 * @param receipt_amount
	 */
	public void setReceipt_amount(int receipt_amount) {
		this.receipt_amount = receipt_amount;
	}
	
	/**
	 * ������ȣ
	 * @return emp_no
	 */
	public String getEmp_no() {
		return emp_no;
	}

	/**
	 * ������ȣ
	 * @param emp_no
	 */
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	
	/**
	 * ������ȣ
	 * @return present_no
	 */
	public String getPresent_no() {
		return present_no;
	}

	/**
	 * ������ȣ
	 * @param present_no
	 */
	public void setPresent_no(String present_no) {
		this.present_no = present_no;
	}
	
	/**
	 * �����ڸ�
	 * @return receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * �����ڸ�
	 * @param receiver_name
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	
	/**
	 * ������ �޴�����ȣ{��ȣȭ}
	 * @return receiver_mobile
	 */
	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	/**
	 * ������ �޴�����ȣ{��ȣȭ}
	 * @param receiver_mobile
	 */
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	
	/**
	 * ���� �޼���
	 * @return present_message
	 */
	public String getPresent_message() {
		return present_message;
	}

	/**
	 * ���� �޼���
	 * @param present_message
	 */
	public void setPresent_message(String present_message) {
		this.present_message = present_message;
	}
	
	/**
	 * ���� ���ǻ��� �ڵ�
	 * @return present_notice_code
	 */
	public String getPresent_notice_code() {
		return present_notice_code;
	}

	/**
	 * ���� ���ǻ��� �ڵ�
	 * @param present_notice_code
	 */
	public void setPresent_notice_code(String present_notice_code) {
		this.present_notice_code = present_notice_code;
	}
	
	/**
	 * ���� ���ǻ��� ����
	 * @return present_notice_version
	 */
	public int getPresent_notice_version() {
		return present_notice_version;
	}

	/**
	 * ���� ���ǻ��� ����
	 * @param present_notice_version
	 */
	public void setPresent_notice_version(int present_notice_version) {
		this.present_notice_version = present_notice_version;
	}
	
	/**
	 * ���� ����{�ڵ�ǥ����}
	 * @return present_status
	 */
	public String getPresent_status() {
		return present_status;
	}

	/**
	 * ���� ����{�ڵ�ǥ����}
	 * @param present_status
	 */
	public void setPresent_status(String present_status) {
		this.present_status = present_status;
	}
	
	/**
	 * ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	 * @return receive_type
	 */
	public String getReceive_type() {
		return receive_type;
	}

	/**
	 * ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	 * @param receive_type
	 */
	public void setReceive_type(String receive_type) {
		this.receive_type = receive_type;
	}
	
	/**
	 * ��Ʈ��ID
	 * @return cashier_id
	 */
	public String getCashier_id() {
		return cashier_id;
	}

	/**
	 * ��Ʈ��ID
	 * @param cashier_id
	 */
	public void setCashier_id(String cashier_id) {
		this.cashier_id = cashier_id;
	}
	
	/**
	 * ��Ʈ�ʸ�
	 * @return cashier_name
	 */
	public String getCashier_name() {
		return cashier_name;
	}

	/**
	 * ��Ʈ�ʸ�
	 * @param cashier_name
	 */
	public void setCashier_name(String cashier_name) {
		this.cashier_name = cashier_name;
	}
	
	/**
	 * ������{YYYYMMDD}
	 * @return sale_date
	 */
	public String getSale_date() {
		return sale_date;
	}

	/**
	 * ������{YYYYMMDD}
	 * @param sale_date
	 */
	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}
	
	/**
	 * �����ڵ�
	 * @return store_cd
	 */
	public String getStore_cd() {
		return store_cd;
	}

	/**
	 * �����ڵ�
	 * @param store_cd
	 */
	public void setStore_cd(String store_cd) {
		this.store_cd = store_cd;
	}
	
	/**
	 * POS��ȣ
	 * @return pos_no
	 */
	public String getPos_no() {
		return pos_no;
	}

	/**
	 * POS��ȣ
	 * @param pos_no
	 */
	public void setPos_no(String pos_no) {
		this.pos_no = pos_no;
	}
	
	/**
	 * �ŷ���ȣ
	 * @return seq_no
	 */
	public String getSeq_no() {
		return seq_no;
	}

	/**
	 * �ŷ���ȣ
	 * @param seq_no
	 */
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	
	/**
	 * ����Ȯ������{Y-Ȯ��}
	 * @return pos_comp_flag
	 */
	public String getPos_comp_flag() {
		return pos_comp_flag;
	}

	/**
	 * ����Ȯ������{Y-Ȯ��}
	 * @param pos_comp_flag
	 */
	public void setPos_comp_flag(String pos_comp_flag) {
		this.pos_comp_flag = pos_comp_flag;
	}
	
	/**
	 * ����Ȯ������{Y-Ȯ��}
	 * @return sales_order_comp_flag
	 */
	public String getSales_order_comp_flag() {
		return sales_order_comp_flag;
	}

	/**
	 * ����Ȯ������{Y-Ȯ��}
	 * @param sales_order_comp_flag
	 */
	public void setSales_order_comp_flag(String sales_order_comp_flag) {
		this.sales_order_comp_flag = sales_order_comp_flag;
	}
	
	/**
	 * ���ϷῩ��{Y-�Ϸ�}
	 * @return trade_flag
	 */
	public String getTrade_flag() {
		return trade_flag;
	}

	/**
	 * ���ϷῩ��{Y-�Ϸ�}
	 * @param trade_flag
	 */
	public void setTrade_flag(String trade_flag) {
		this.trade_flag = trade_flag;
	}
	
	/**
	 * ��������
	 * @return critical_flag
	 */
	public String getCritical_flag() {
		return critical_flag;
	}

	/**
	 * ��������
	 * @param critical_flag
	 */
	public void setCritical_flag(String critical_flag) {
		this.critical_flag = critical_flag;
	}
	
	/**
	 * ��뿩��{Y-���, N-�̻��}
	 * @return use_flag
	 */
	public String getUse_flag() {
		return use_flag;
	}

	/**
	 * ��뿩��{Y-���, N-�̻��}
	 * @param use_flag
	 */
	public void setUse_flag(String use_flag) {
		this.use_flag = use_flag;
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
	
	/**
	 * ���� �Ϸ�����
	 * @return pay_date
	 */
	public Date getPay_date() {
		return pay_date;
	}

	/**
	 * ���� �Ϸ�����
	 * @param pay_date
	 */
	public void setPay_date(Date pay_date) {
		this.pay_date = pay_date;
	}
	
	/**
	 * ���� ��������
	 * @return mod_date
	 */
	public Date getMod_date() {
		return mod_date;
	}

	/**
	 * ���� ��������
	 * @param mod_date
	 */
	public void setMod_date(Date mod_date) {
		this.mod_date = mod_date;
	}
	
	/**
	 * ����/�̼��� �Ϸ�����
	 * @return receive_comp_date
	 */
	public Date getReceive_comp_date() {
		return receive_comp_date;
	}

	/**
	 * ����/�̼��� �Ϸ�����
	 * @param receive_comp_date
	 */
	public void setReceive_comp_date(Date receive_comp_date) {
		this.receive_comp_date = receive_comp_date;
	}
	
	/**
	 * ���� �������
	 * @return cancel_date
	 */
	public Date getCancel_date() {
		return cancel_date;
	}

	/**
	 * ���� �������
	 * @param cancel_date
	 */
	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}
	
	/**
	 * ������ �޴�����ȣ ��������
	 * @return receiver_mobile_del_date
	 */
	public Date getReceiver_mobile_del_date() {
		return receiver_mobile_del_date;
	}

	/**
	 * ������ �޴�����ȣ ��������
	 * @param receiver_mobile_del_date
	 */
	public void setReceiver_mobile_del_date(Date receiver_mobile_del_date) {
		this.receiver_mobile_del_date = receiver_mobile_del_date;
	}
	
	/**
	 * �������࿩��{Y-����}
	 * @return coupon_pub_flag
	 */
	public String getCoupon_pub_flag() {
		return coupon_pub_flag;
	}

	/**
	 * �������࿩��{Y-����}
	 * @param coupon_pub_flag
	 */
	public void setCoupon_pub_flag(String coupon_pub_flag) {
		this.coupon_pub_flag = coupon_pub_flag;
	}
	
	/**
	 * Ȧ���� ���� ��å�� ���� ���� ���� 
	 * @return
	 */
	public int getCoupon_count() {
		return coupon_count;
	}

	/**
	 * Ȧ���� ���� ��å�� ���� ���� ���� 
	 * @param coupon_count
	 */
	public void setCoupon_count(int coupon_count) {
		this.coupon_count = coupon_count;
	}

	/**
	 * Ȧ���� ������ �� ���� ���� ���� ((��ü ����-Ȧ������������)*Ȧ������å�� ���� ���� ����)
	 * @return
	 */
	public int getTotal_coupon_count() {
		return total_coupon_count;
	}

	/**
	 * Ȧ���� ������ �� ���� ���� ���� ((��ü ����-Ȧ������������)*Ȧ������å�� ���� ���� ����)
	 * @param total_coupon_count
	 */
	public void setTotal_coupon_count(int total_coupon_count) {
		this.total_coupon_count = total_coupon_count;
	}
	
	/**
	 * ������ ���� ����
	 * @return
	 */
	public int getPublication_coupon_count() {
		return publication_coupon_count;
	}

	/**
	 * ������ ���� ����
	 * @param publication_coupon_count
	 */
	public void setPublication_coupon_count(int publication_coupon_count) {
		this.publication_coupon_count = publication_coupon_count;
	}	
	
	/**
	 * ��������
	 * @return
	 */
	public String getOrder_date() {
		return order_date;
	}

	/**
	 * ��������
	 * @param order_date
	 */
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	/**
	 * �󼼼���
	 * @return
	 */
	public int getItem_seq() {
		return item_seq;
	}

	/**
	 * �󼼼���
	 * @param item_seq
	 */
	public void setItem_seq(int item_seq) {
		this.item_seq = item_seq;
	}

	/**
	 * SKU�ڵ�
	 * @return
	 */
	public String getSku_no() {
		return sku_no;
	}

	/**
	 * SKU�ڵ�
	 * @param sku_no
	 */
	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}

	/**
	 * �ܰ�
	 * @return
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * �ܰ�
	 * @param price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * ����
	 * @return
	 */
	public int getQty() {
		return qty;
	}

	/**
	 * ����
	 * @param qty
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * ǰ���Ѿ�(�ݾ�*����)
	 * @return
	 */
	public int getItem_amount() {
		return item_amount;
	}

	/**
	 * ǰ���Ѿ�(�ݾ�*����)
	 * @param item_amount
	 */
	public void setItem_amount(int item_amount) {
		this.item_amount = item_amount;
	}

	/**
	 * ���ξ�(ǰ���Ѿ� *(���������� *0.1))
	 * @return
	 */
	public int getDiscount() {
		return discount;
	}
	
	/**
	 * ���ξ�(ǰ���Ѿ� *(���������� *0.1))
	 * @param discount
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}

	/**
	 * �������
	 * @return
	 */
	public int getGnd_amount() {
		return gnd_amount;
	}

	/**
	 * �������
	 * @param gnd_amount
	 */
	public void setGnd_amount(int gnd_amount) {
		this.gnd_amount = gnd_amount;
	}

	/**
	 * ī�װ�����
	 * @return
	 */
	public String getCate_type() {
		return cate_type;
	}

	/**
	 * ī�װ�����
	 * @param cate_type
	 */
	public void setCate_type(String cate_type) {
		this.cate_type = cate_type;
	}

	/**
	 * �����Ϸù�ȣ
	 * @return
	 */
	public String getCoupon_number() {
		return coupon_number;
	}

	/**
	 * �����Ϸù�ȣ
	 * @param coupon_number
	 */
	public void setCoupon_number(String coupon_number) {
		this.coupon_number = coupon_number;
	}

	/**
	 * �������αݾ�
	 * @return
	 */
	public int getCoupon_dc_amt() {
		return coupon_dc_amt;
	}

	/**
	 * �������αݾ�
	 * @param coupon_dc_amt
	 */
	public void setCoupon_dc_amt(int coupon_dc_amt) {
		this.coupon_dc_amt = coupon_dc_amt;
	}

	/**
	 * ��������������ȣ
	 * @return
	 */
	public String getSck_coupon_no() {
		return sck_coupon_no;
	}

	
	/**
	 * ��������������ȣ
	 * @param sck_coupon_no
	 */
	public void setSck_coupon_no(String sck_coupon_no) {
		this.sck_coupon_no = sck_coupon_no;
	}

	/**
	 * ������߼ۿ���_YN
	 * @return
	 */
	public String getPresent_resend_yn() {
		return present_resend_yn;
	}

	/**
	 * ������߼ۿ���_YN
	 * @param present_resend_yn
	 */
	public void setPresent_resend_yn(String present_resend_yn) {
		this.present_resend_yn = present_resend_yn;
	}

	/**
	 * ���� ���� �� ���� �߼� ������ ���� ���
	 * @return
	 */
	public List<String> getSuccess_coupon_list() {
		return success_coupon_list;
	}

	/**
	 * ���� ���� �� ���� �߼� ������ ���� ���
	 * @param success_coupon_list
	 */
	public void setSuccess_coupon_list(List<String> success_coupon_list) {
		this.success_coupon_list = success_coupon_list;
	}

	/**
	 * MMS ���� �߼� ������ ���� ��� 
	 * @return
	 */
	public List<String> getFail_coupon_list() {
		return fail_coupon_list;
	}

	/**
	 * MMS ���� �߼� ������ ���� ��� 
	 * @param fail_coupon_list
	 */
	public void setFail_coupon_list(List<String> fail_coupon_list) {
		this.fail_coupon_list = fail_coupon_list;
	}
	
	
	/**
	 * ���� ���� ��
	 * @return receive_store_name
	 */
	public String getReceive_store_name() {
		return receive_store_name;
	}

	/**
	 * ���� �����ڵ�
	 * @param receive_store_cd
	 */
	public void setReceive_store_name(String receive_store_name) {
		this.receive_store_name = receive_store_name;
	}
}
