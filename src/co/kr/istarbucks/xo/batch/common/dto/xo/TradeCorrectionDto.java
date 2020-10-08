/*
 * @(#) $Id: TradeCorrectionDto.java,v 1.4 2018/12/04 01:35:41 yoyo962 Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;


/**
 * �������� �ŷ� ��� TradeCorrectionDto.
 * @author eZEN
 * @since 2014. 2. 6.
 * @version $Revision: 1.4 $
 */
public class TradeCorrectionDto {
	
	private String order_no;    	       // �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}, Ȧ���� : �����ȣ{"6"+YYYYMMDDHH24MISS+������5�ڸ�}
	private String sale_flag;   	       // �������� �ŷ�����{11-�Ϲݸ���,19-�Ϲݸ������}
	private String store_no;    	       // �����ȣ
	private String pos_no;      	       // ������ȣ
	private String sale_date;   	       // ��������
	private String appro_tm;    	       // ���νð�
	private String seq_no;      	       // �ŷ���ȣ
	private String tran_seq_no; 	       // �ŷ�������ȣ
	private String user_id;     	       // ����ھ��̵�
	private String user_name;   	       // ����ڸ�
	private String status;      	  	   // MSR ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}, Ȧ���� (���ɿϷ� : O30, �̼��� ��� : O31)
	private String receive_type;	  	   // ���ɱ���{O-�ֹ���ȣ, P-������ȣ} 
	private Date   receive_comp_date;      // ����/�̼��� �Ϸ����� 
	private String sales_tbl_receive_type; // SAL_PAY_XO���� Ȯ���� ���ɱ���{O-�ֹ���ȣ, P-������ȣ} 
	private String msr_user_flag;			// MSRȸ������{B-��ȸ��, Y-ȸ��, N-��ȸ��, J-��ȸ��}
	private int total_pay_amt;				// �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�}
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("PaymentDto [");
		str.append ("order_no=").append (this.order_no);
		str.append (", sale_flag=").append (this.sale_flag);
		str.append (", store_no=").append (this.store_no);
		str.append (", pos_no=").append (this.pos_no);
		str.append (", sale_date=").append (this.sale_date);
		str.append (", appro_tm=").append (this.appro_tm);
		str.append (", seq_no=").append (this.seq_no);
		str.append (", tran_seq_no=").append (this.tran_seq_no);
		str.append (", user_id=").append (this.user_id);
		str.append (", user_name=").append (this.user_name);
		str.append (", status=").append (this.status);
		str.append (", receive_type=").append (this.receive_type);
		str.append (", receive_comp_date=").append (this.receive_comp_date);
		str.append (", sales_tbl_receive_type=").append (this.sales_tbl_receive_type);
		str.append (", msr_user_flag=").append (this.msr_user_flag);
		str.append (", total_pay_amt=").append (String.valueOf(this.total_pay_amt));
		str.append ("]");
		
		return str.toString ();
	}
	
	/**
	 * �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * Ȧ���� : �����ȣ{"6"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @return order_no
	 */
	public String getOrder_no () {
		return order_no;
	}
	
	/**
	 * �ֹ���ȣ{"3"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * Ȧ���� : �����ȣ{"6"+YYYYMMDDHH24MISS+������5�ڸ�}
	 * @param order_no
	 */
	public void setOrder_no ( String order_no ) {
		this.order_no = order_no;
	}

	/**
	 * �������� �ŷ�����{11-�Ϲݸ���,19-�Ϲݸ������}
	 * @return sale_flag
	 */
	public String getSale_flag() {
		return sale_flag;
	}

	/**
	 * �������� �ŷ�����{11-�Ϲݸ���,19-�Ϲݸ������}
	 * @param sale_flag
	 */
	public void setSale_flag(String sale_flag) {
		this.sale_flag = sale_flag;
	}

	/**
	 * �����ȣ
	 * @return store_no
	 */
	public String getStore_no() {
		return store_no;
	}

	/**
	 * �����ȣ
	 * @param store_no
	 */
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}

	/**
	 * ������ȣ
	 * @return pos_no
	 */
	public String getPos_no() {
		return pos_no;
	}

	/**
	 * ������ȣ
	 * @param pos_no
	 */
	public void setPos_no(String pos_no) {
		this.pos_no = pos_no;
	}

	/**
	 * ��������
	 * @return sale_date
	 */
	public String getSale_date() {
		return sale_date;
	}

	/**
	 * ��������
	 * @param sale_date
	 */
	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}

	/**
	 * ���νð�
	 * @return appro_tm
	 */
	public String getAppro_tm() {
		return appro_tm;
	}

	/**
	 * ���νð�
	 * @param appro_tm
	 */
	public void setAppro_tm(String appro_tm) {
		this.appro_tm = appro_tm;
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
	 * �ŷ�������ȣ
	 * @return tran_seq_no
	 */
	public String getTran_seq_no() {
		return tran_seq_no;
	}

	/**
	 * �ŷ�������ȣ
	 * @param tran_seq_no
	 */
	public void setTran_seq_no(String tran_seq_no) {
		this.tran_seq_no = tran_seq_no;
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
	 * MSR ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}
	 * Ȧ���� (���ɿϷ� : O30, �̼��� ��� : O31)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * MSR ����{10-���� ��û, 11-���� �Ϸ�, 12-���� ���, 13-���� ��� ����, 14-���� ��� �κ� ����, 15-���� ����, 20-�ֹ� ��û, 21-�ֹ� ����, 22-�ֹ� ���, 23-�ֹ� ��� ����, 24-�ֹ� ���� Ȯ��(�̷�), 25-��� ���� Ȯ��(�̷�), 30-�ֹ��Ϸ�(nonBDS), 31-�����Ϸ�(BDS)}
	 * Ȧ���� (���ɿϷ� : O30, �̼��� ��� : O31)
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	 * @return
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
	 * ����/�̼��� �Ϸ�����
	 * @return
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
	 * SAL_PAY_XO���� Ȯ���� ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	 * @return
	 */
	public String getSales_tbl_receive_type() {
		return sales_tbl_receive_type;
	}

	/**
	 * SAL_PAY_XO���� Ȯ���� ���ɱ���{O-�ֹ���ȣ, P-������ȣ}
	 * @param sales_tbl_receive_type
	 */
	public void setSales_tbl_receive_type(String sales_tbl_receive_type) {
		this.sales_tbl_receive_type = sales_tbl_receive_type;
	}

	/**
	 * MSRȸ������{B-��ȸ��, Y-ȸ��, N-��ȸ��, J-��ȸ��}
	 * @return the msr_user_flag
	 */
	public String getMsr_user_flag() {
		return msr_user_flag;
	}

	/**
	 * MSRȸ������{B-��ȸ��, Y-ȸ��, N-��ȸ��, J-��ȸ��}
	 * @param msr_user_flag the msr_user_flag to set
	 */
	public void setMsr_user_flag(String msr_user_flag) {
		this.msr_user_flag = msr_user_flag;
	}

	/**
	 * �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�}
	 * @return the total_pay_amt
	 */
	public int getTotal_pay_amt() {
		return total_pay_amt;
	}

	/**
	 * �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�}
	 * @param total_pay_amt the total_pay_amt to set
	 */
	public void setTotal_pay_amt(int total_pay_amt) {
		this.total_pay_amt = total_pay_amt;
	}
	
	
}
