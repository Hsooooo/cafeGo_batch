/*
 * @(#) $Id: OrderDto.java,v 1.2 2018/10/01 06:55:14 iamjihun Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * �ֹ����� OrderDto.
 * @author eZEN ksy
 * @since 2014. 1. 17.
 * @version $Revision: 1.2 $
 */
public class OrderDto {
	
	private String order_no; // �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	private String cart_no; // �ֹ����Ϸù�ȣ{"2"+YYYYMMDDHH24MISS+������5�ڸ�}
	private String user_id; // ����ھ��̵�
	private String user_name; // ����ڸ�
	private String status; // ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}
	private String payment_kind; // ��������{1-APP, 2-POS}
	private String order_kind; // �ֹ�����{1-APP, 2-POS}
	private Date reg_date; // �����
	private String order_name; // �ֹ���
	private String msr_user_flag; // MSRȸ������{Y-ȸ��, N-��ȸ��}
	private String push_id; // PUSH ID
	private Long total_pay_amt; // �� �����ݾ�
	private Integer total_discount; // �� ���ξ�
	private Long total_item_amount; // �� ǰ���Ѿ�
	private String take_out_flag; // �ֹ�����{Y-TAKE OUT, N-HERE IN}
	private String packing_flag; // ���忩��{Y-����, N-������}
	private String freq_barcode; // �������� ���ڵ�{��ȣȭ}
	private Date mod_date; // ������
	private Date pay_date; // �����Ϸ�����
	private Date order_date; // �ֹ��Ϸ�����
	private String sale_date; // ������{YYYYMMDD}
	private String store_cd; // �����ڵ�
	private String pos_no; // POS��ȣ
	private String seq_no; // �ŷ���ȣ
	private String receipt_type; // ���ݿ����� ���౸��{H-�޴��� ��ȣ, C-���α���ûī��, K-�ٸ�����޴�����ȣ, V-����ڵ�Ϲ�ȣ, D-���α���ûī��, A-���๮�Ǿ���, E-������������}
	private String receipt_number; // ���ݿ����� �����ȣ{��ȣȭ}
	private Integer receipt_amount; // ���ݿ����� ���ݾ�
	private String msr_user_grade; // MSRȸ�����
	private String tran_seq_no; // POS ���ι�ȣ
	private String cashier_id; // ��Ʈ��ID
	private String cancel_reason; // �ֹ� ��� ����
	private String phone; // �޴�����ȣ{��ȣȭ}
	private Integer total_qty; // ��ü����
	private String gstdgr; // �����ڵ�
	private String bankcd; // POS����
	private String os_type; // �ܸ�����{1-iOS, 2-Android}
	private String pos_comp_flag; // ����Ȯ������{Y-Ȯ��, N-��Ȯ��}
	
	private String store_name; // �����
	private Integer payment_count; // ������ �ֹ� �Ǽ�
	private String check_status_flag; // �ش� �ֹ� �������� ���� {Y/N}
    private String emp_no;  // ������ ��ȣ
    private String emp_auth_app_no; // ������ ���� ���ι�ȣ

    @Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("OrderDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", cart_no=").append (this.cart_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", user_name=").append (this.user_name);
		str.append (", status=").append (this.status);
		str.append (", payment_kind=").append (this.payment_kind);
		str.append (", order_kind=").append (this.order_kind);
		str.append (", reg_date=").append (this.reg_date);
		str.append (", order_name=").append (this.order_name);
		str.append (", msr_user_flag=").append (this.msr_user_flag);
		str.append (", push_id=").append (this.push_id);
		str.append (", total_pay_amt=").append (this.total_pay_amt);
		str.append (", total_discount=").append (this.total_discount);
		str.append (", total_item_amount=").append (this.total_item_amount);
		str.append (", take_out_flag=").append (this.take_out_flag);
		str.append (", packing_flag=").append (this.packing_flag);
		str.append (", freq_barcode=").append (this.freq_barcode);
		str.append (", mod_date=").append (this.mod_date);
		str.append (", pay_date=").append (this.pay_date);
		str.append (", order_date=").append (this.order_date);
		str.append (", sale_date=").append (this.sale_date);
		str.append (", store_cd=").append (this.store_cd);
		str.append (", pos_no=").append (this.pos_no);
		str.append (", seq_no=").append (this.seq_no);
		str.append (", receipt_type=").append (this.receipt_type);
		str.append (", receipt_number=").append (this.receipt_number);
		str.append (", receipt_amount=").append (this.receipt_amount);
		str.append (", msr_user_grade=").append (this.msr_user_grade);
		str.append (", tran_seq_no=").append (this.tran_seq_no);
		str.append (", cashier_id=").append (this.cashier_id);
		str.append (", cancel_reason=").append (this.cancel_reason);
		str.append (", phone=").append (this.phone);
		str.append (", total_qty=").append (this.total_qty);
		str.append (", gstdgr=").append (this.gstdgr);
		str.append (", bankcd=").append (this.bankcd);
		str.append (", os_type=").append (this.os_type);
		str.append (", pos_comp_flag=").append (this.pos_comp_flag);
		
		str.append (", store_name=").append (this.store_name);
		str.append (", payment_count=").append (this.payment_count);
		str.append (", check_status_flag=").append (this.check_status_flag);
		
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * �ֹ���ȣ{""3""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ{""3""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}
	
	/**
	 * �ֹ����Ϸù�ȣ{""2""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @return cart_no
	 */
	public String getCart_no () {
		return cart_no;
	}
	
	/**
	 * �ֹ����Ϸù�ȣ{""2""+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @param cart_no
	 */
	public void setCart_no ( String cart_no ) {
		this.cart_no = cart_no;
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
	 * ����ڸ�
	 * @return user_name
	 */
	public String getUser_name () {
		return user_name;
	}
	
	/**
	 * ����ڸ�
	 * @param user_name
	 */
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	/**
	 * ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}
	 * @return status
	 */
	public String getStatus () {
		return status;
	}
	
	/**
	 * ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}
	 * @param status
	 */
	public void setStatus ( String status ) {
		this.status = status;
	}
	
	/**
	 * ��������{1-APP, 2-POS}
	 * @return payment_kind
	 */
	public String getPayment_kind () {
		return payment_kind;
	}
	
	/**
	 * ��������{1-APP, 2-POS}
	 * @param payment_kind
	 */
	public void setPayment_kind ( String payment_kind ) {
		this.payment_kind = payment_kind;
	}
	
	/**
	 * �ֹ�����{1-APP, 2-POS}
	 * @return order_kind
	 */
	public String getOrder_kind () {
		return order_kind;
	}
	
	/**
	 * �ֹ�����{1-APP, 2-POS}
	 * @param order_kind
	 */
	public void setOrder_kind ( String order_kind ) {
		this.order_kind = order_kind;
	}
	
	/**
	 * �����
	 * @return reg_date
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * �����
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * �ֹ���
	 * @return order_name
	 */
	public String getOrder_name () {
		return order_name;
	}
	
	/**
	 * �ֹ���
	 * @param order_name
	 */
	public void setOrder_name ( String order_name ) {
		this.order_name = order_name;
	}
	
	/**
	 * MSRȸ������{Y-ȸ��, N-��ȸ��}
	 * @return msr_user_flag
	 */
	public String getMsr_user_flag () {
		return msr_user_flag;
	}
	
	/**
	 * MSRȸ������{Y-ȸ��, N-��ȸ��}
	 * @param msr_user_flag
	 */
	public void setMsr_user_flag ( String msr_user_flag ) {
		this.msr_user_flag = msr_user_flag;
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
	 * �� �����ݾ�
	 * @return total_pay_amt
	 */
	public Long getTotal_pay_amt () {
		return total_pay_amt;
	}
	
	/**
	 * �� �����ݾ�
	 * @param total_pay_amt
	 */
	public void setTotal_pay_amt ( Long total_pay_amt ) {
		this.total_pay_amt = total_pay_amt;
	}
	
	/**
	 * �� ���ξ�
	 * @return total_discount
	 */
	public Integer getTotal_discount () {
		return total_discount == null ? 0 : total_discount;
	}
	
	/**
	 * �� ���ξ�
	 * @param total_discount
	 */
	public void setTotal_discount ( Integer total_discount ) {
		this.total_discount = total_discount;
	}
	
	/**
	 * �� ǰ���Ѿ�
	 * @return total_item_amount
	 */
	public Long getTotal_item_amount () {
		return total_item_amount;
	}
	
	/**
	 * �� ǰ���Ѿ�
	 * @param total_item_amount
	 */
	public void setTotal_item_amount ( Long total_item_amount ) {
		this.total_item_amount = total_item_amount;
	}
	
	/**
	 * �ֹ�����{Y-TAKE OUT, N-HERE IN}
	 * @return take_out_flag
	 */
	public String getTake_out_flag () {
		return take_out_flag;
	}
	
	/**
	 * �ֹ�����{Y-TAKE OUT, N-HERE IN}
	 * @param take_out_flag
	 */
	public void setTake_out_flag ( String take_out_flag ) {
		this.take_out_flag = take_out_flag;
	}
	
	/**
	 * ���忩��{Y-����, N-������}
	 * @return packing_flag
	 */
	public String getPacking_flag () {
		return packing_flag;
	}
	
	/**
	 * ���忩��{Y-����, N-������}
	 * @param packing_flag
	 */
	public void setPacking_flag ( String packing_flag ) {
		this.packing_flag = packing_flag;
	}
	
	/**
	 * �������� ���ڵ�{��ȣȭ}
	 * @return freq_barcode
	 */
	public String getFreq_barcode () {
		return freq_barcode;
	}
	
	/**
	 * �������� ���ڵ�{��ȣȭ}
	 * @param freq_barcode
	 */
	public void setFreq_barcode ( String freq_barcode ) {
		this.freq_barcode = freq_barcode;
	}
	
	/**
	 * ������
	 * @return mod_date
	 */
	public Date getMod_date () {
		return mod_date;
	}
	
	/**
	 * ������
	 * @param mod_date
	 */
	public void setMod_date ( Date mod_date ) {
		this.mod_date = mod_date;
	}
	
	/**
	 * �����Ϸ�����
	 * @return pay_date
	 */
	public Date getPay_date () {
		return pay_date;
	}
	
	/**
	 * �����Ϸ�����
	 * @param pay_date
	 */
	public void setPay_date ( Date pay_date ) {
		this.pay_date = pay_date;
	}
	
	/**
	 * �ֹ��Ϸ�����
	 * @return order_date
	 */
	public Date getOrder_date () {
		return order_date;
	}
	
	/**
	 * �ֹ��Ϸ�����
	 * @param order_date
	 */
	public void setOrder_date ( Date order_date ) {
		this.order_date = order_date;
	}
	
	/**
	 * ������{YYYYMMDD}
	 * @return sale_date
	 */
	public String getSale_date () {
		return sale_date;
	}
	
	/**
	 * ������{YYYYMMDD}
	 * @param sale_date
	 */
	public void setSale_date ( String sale_date ) {
		this.sale_date = sale_date;
	}
	
	/**
	 * �����ڵ�
	 * @return store_cd
	 */
	public String getStore_cd () {
		return store_cd;
	}
	
	/**
	 * �����ڵ�
	 * @param store_cd
	 */
	public void setStore_cd ( String store_cd ) {
		this.store_cd = store_cd;
	}
	
	/**
	 * POS��ȣ
	 * @return pos_no
	 */
	public String getPos_no () {
		return pos_no;
	}
	
	/**
	 * POS��ȣ
	 * @param pos_no
	 */
	public void setPos_no ( String pos_no ) {
		this.pos_no = pos_no;
	}
	
	/**
	 * �ŷ���ȣ
	 * @return seq_no
	 */
	public String getSeq_no () {
		return seq_no;
	}
	
	/**
	 * �ŷ���ȣ
	 * @param seq_no
	 */
	public void setSeq_no ( String seq_no ) {
		this.seq_no = seq_no;
	}
	
	/**
	 * ���ݿ����� ���౸��{H-�޴��� ��ȣ, C-���α���ûī��, K-�ٸ�����޴�����ȣ, V-����ڵ�Ϲ�ȣ, D-���α���ûī��, A-���๮�Ǿ���, E-������������}
	 * @return receipt_type
	 */
	public String getReceipt_type () {
		return receipt_type;
	}
	
	/**
	 * ���ݿ����� ���౸��{H-�޴��� ��ȣ, C-���α���ûī��, K-�ٸ�����޴�����ȣ, V-����ڵ�Ϲ�ȣ, D-���α���ûī��, A-���๮�Ǿ���, E-������������}
	 * @param receipt_type
	 */
	public void setReceipt_type ( String receipt_type ) {
		this.receipt_type = receipt_type;
	}
	
	/**
	 * ���ݿ����� �����ȣ{��ȣȭ}
	 * @return receipt_number
	 */
	public String getReceipt_number () {
		return receipt_number;
	}
	
	/**
	 * ���ݿ����� �����ȣ{��ȣȭ}
	 * @param receipt_number
	 */
	public void setReceipt_number ( String receipt_number ) {
		this.receipt_number = receipt_number;
	}
	
	/**
	 * ���ݿ����� ���ݾ�
	 * @return receipt_amount
	 */
	public Integer getReceipt_amount () {
		return receipt_amount == null ? 0 : receipt_amount;
	}
	
	/**
	 * ���ݿ����� ���ݾ�
	 * @param receipt_amount
	 */
	public void setReceipt_amount ( Integer receipt_amount ) {
		this.receipt_amount = receipt_amount;
	}
	
	/**
	 * MSRȸ�����
	 * @return msr_user_grade
	 */
	public String getMsr_user_grade () {
		return msr_user_grade;
	}
	
	/**
	 * MSRȸ�����
	 * @param msr_user_grade
	 */
	public void setMsr_user_grade ( String msr_user_grade ) {
		this.msr_user_grade = msr_user_grade;
	}
	
	/**
	 * POS ���ι�ȣ
	 * @return tran_seq_no
	 */
	public String getTran_seq_no () {
		return tran_seq_no;
	}
	
	/**
	 * POS ���ι�ȣ
	 * @param tran_seq_no
	 */
	public void setTran_seq_no ( String tran_seq_no ) {
		this.tran_seq_no = tran_seq_no;
	}
	
	/**
	 * ��Ʈ��ID
	 * @return cashier_id
	 */
	public String getCashier_id () {
		return cashier_id;
	}
	
	/**
	 * ��Ʈ��ID
	 * @param cashier_id
	 */
	public void setCashier_id ( String cashier_id ) {
		this.cashier_id = cashier_id;
	}
	
	/**
	 * �ֹ� ��� ����
	 * @return cancel_reason
	 */
	public String getCancel_reason () {
		return cancel_reason;
	}
	
	/**
	 * �ֹ� ��� ����
	 * @param cancel_reason
	 */
	public void setCancel_reason ( String cancel_reason ) {
		this.cancel_reason = cancel_reason;
	}
	
	/**
	 * �޴�����ȣ{��ȣȭ}
	 * @return phone
	 */
	public String getPhone () {
		return phone;
	}
	
	/**
	 * �޴�����ȣ{��ȣȭ}
	 * @param phone
	 */
	public void setPhone ( String phone ) {
		this.phone = phone;
	}
	
	/**
	 * ��ü����
	 * @return total_qty
	 */
	public Integer getTotal_qty () {
		return total_qty == null ? 0 : total_qty;
	}
	
	/**
	 * ��ü����
	 * @param total_qty
	 */
	public void setTotal_qty ( Integer total_qty ) {
		this.total_qty = total_qty;
	}
	
	/**
	 * �����ڵ�
	 * @return gstdgr
	 */
	public String getGstdgr () {
		return gstdgr;
	}
	
	/**
	 * �����ڵ�
	 * @param gstdgr
	 */
	public void setGstdgr ( String gstdgr ) {
		this.gstdgr = gstdgr;
	}
	
	/**
	 * POS����
	 * @return bankcd
	 */
	public String getBankcd () {
		return bankcd;
	}
	
	/**
	 * POS����
	 * @param bankcd
	 */
	public void setBankcd ( String bankcd ) {
		this.bankcd = bankcd;
	}
	
	/**
	 * �ܸ�����{1-iOS, 2-Android}
	 * @return os_type
	 */
	public String getOs_type () {
		return os_type;
	}
	
	/**
	 * �ܸ�����{1-iOS, 2-Android}
	 * @param os_type
	 */
	public void setOs_type ( String os_type ) {
		this.os_type = os_type;
	}
	
	/**
	 * ����Ȯ������{Y-Ȯ��, N-��Ȯ��}
	 * @return pos_comp_flag
	 */
	public String getPos_comp_flag () {
		return pos_comp_flag;
	}
	
	/**
	 * ����Ȯ������{Y-Ȯ��, N-��Ȯ��}
	 * @param pos_comp_flag
	 */
	public void setPos_comp_flag ( String pos_comp_flag ) {
		this.pos_comp_flag = pos_comp_flag;
	}
	
	
	/**
	 * �����
	 * @return store_name
	 */
	public String getStore_name () {
		return store_name;
	}
	
	/**
	 * �����
	 * @param store_name
	 */
	public void setStore_name ( String store_name ) {
		this.store_name = store_name;
	}
	
	/**
	 * ������ �ֹ� �Ǽ�
	 * @return payment_count
	 */
	public Integer getPayment_count () {
		return payment_count == null ? 0 : payment_count;
	}
	
	/**
	 * ������ �ֹ� �Ǽ�
	 * @param payment_count
	 */
	public void setPayment_count ( Integer payment_count ) {
		this.payment_count = payment_count;
	}
	
	/**
	 * �ش� �ֹ� �������� ���� {Y/N}
	 * @return check_status_flag
	 */
	public String getCheck_status_flag () {
		return check_status_flag;
	}
	
	/**
	 * �ش� �ֹ� �������� ���� {Y/N}
	 * @param check_status_flag
	 */
	public void setCheck_status_flag ( String check_status_flag ) {
		this.check_status_flag = check_status_flag;
	}

    /**
     * ������ ��ȣ
     */
    public String getEmp_no() {
        return emp_no;
    }

    /**
     * ������ ��ȣ
     */
    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    /**
     * ������ ���� ���� ��ȣ
     */
    public String getEmp_auth_app_no() {
        return emp_auth_app_no;
    }

    /**
     * ������ ���� ���� ��ȣ
     */
    public void setEmp_auth_app_no(String emp_auth_app_no) {
        this.emp_auth_app_no = emp_auth_app_no;
    }
}
