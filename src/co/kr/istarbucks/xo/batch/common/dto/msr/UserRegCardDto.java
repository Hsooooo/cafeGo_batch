/*
 * @(#) $Id: UserRegCardDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * ȸ���� ����� ��Ÿ���� ī��
 * @author eZEN ksy
 * @since 2014. 1. 18.
 * @version $Revision: 1.1 $
 */

public class UserRegCardDto implements Serializable {
	
	private static final long serialVersionUID = 5840938990672743283L;
	
	private String card_number;
	private String user_number;
	private String card_nickname;
	private Date reg_date;
	private Integer card_reg_number;
	private Integer balance;
	private Date balance_confirm_date;
	
	private String card_name_k;
	private String card_img_code;
	private String card_img;
	private String card_thumbnail_img;
	private String card_status;
	private long card_reg_seq;
	
	private String card_type_code;
	
	private Date change_date; // ī�� ������ ���� ������ �߰�(2011-07-28, tytolee)
	private String gold_card_reg_status;
	
	private String mobile_view_flag; // ����� view ���� �߰�(2011-09-08, ksy)
	
	private String change_actor_group_code; // ���� ���� ��ü
	
	private String return_code; // ȯ�� �����ڵ�
	
	private String gold_card_req_seq; // ���ī�� ��û �׷� ��ȣ
	private String store_name; // ���ī�� ��û�� ����� ���ɸ���
	private String store_code; // ���ī�� ��û�� ����� ���ɸ���
	private String receive_type; // ���ī�� ��û�� ������ ����(S:����, A:�ּ�(null))
	private String destruct_date; // ���ī�� ���嵵���� ��������
	private String in_store_nm; // ���ī�� �̰� �����
	private String out_store_nm; // ���ī�� ��û �����
	private String email; // ���ī�� ��û �̸���
	private String mobile_num; // ���ī�� ��û �޴�����ȣ
	private String from_dt; // ���ī�� ���� ���� ���� from_date 
	private String to_dt; // ���ī�� ���� ���� ���� to_date
	
	private String auto_reload_type; //�ڵ��������
	private String auto_reload_type_str; //�ڵ��������
	private String auto_reload_amount; //�ڵ������ݾ�
	private String lowest_amount; //�������ѱݾ�
	private String auto_reload_day; //�ڵ�������
	private String auto_reload_pay_method; //�ڵ�������������
	private String auto_reload_bill_key; //PG��Ű
	private String auto_reload_reg_date; //�ڵ�������������
	private String auto_reload_cancel_date;//�ڵ������������
	private String billing_mobile_num; //�����޴�����ȣ
	private String auto_reload_fail_count; //�ڵ���������Ƚ��
	
	private String user_name; //ȸ����
	
	private String taxsave; //���ݿ����� ���� ���ǿ���
	private String phone_number; //���ݿ����� �޴���ȭ��ȣ
	private String receipt_type; //���ݿ����� Ÿ��
	private String user_id; //����� id
	
	private String egift_yn; // ����Ʈī�� ���� 
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("UserRegCardDto [");
		str.append ("card_number=").append (this.card_number);
		str.append (", ").append ("user_number=").append (this.user_number);
		str.append (", ").append ("card_nickname=").append (this.card_nickname);
		if ( this.reg_date != null ) {
			str.append (", ").append ("reg_date=").append (DateFormatUtils.format (this.reg_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("card_reg_number=").append (this.card_reg_number);
		str.append (", ").append ("balance=").append (this.balance);
		if ( this.balance_confirm_date != null ) {
			str.append (", ").append ("balance_confirm_date=").append (DateFormatUtils.format (this.balance_confirm_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("card_name_k=").append (this.card_name_k);
		str.append (", ").append ("card_img_code=").append (this.card_img_code);
		str.append (", ").append ("card_img=").append (this.card_img);
		str.append (", ").append ("card_thumbnail_img=").append (this.card_thumbnail_img);
		str.append (", ").append ("card_status=").append (this.card_status);
		str.append (", ").append ("card_reg_seq=").append (this.card_reg_seq);
		str.append (", ").append ("card_type_code=").append (this.card_type_code);
		if ( this.change_date != null ) {
			str.append (", ").append ("change_date=").append (DateFormatUtils.format (this.change_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("gold_card_reg_status=").append (this.gold_card_reg_status);
		str.append (", ").append ("store_name=").append (this.store_name);
		str.append (", ").append ("store_code=").append (this.store_code);
		str.append (", ").append ("mobile_view_flag=").append (this.mobile_view_flag);
		str.append (", ").append ("change_actor_group_code=").append (this.change_actor_group_code);
		str.append (", ").append ("gold_card_req_seq=").append (this.gold_card_req_seq);
		str.append (", ").append ("user_name=").append (this.user_name);
		str.append (", ").append ("egift_yn=").append (this.egift_yn);
		str.append ("]");
		
		return str.toString ();
	}
	
	
	public String getUser_name () {
		return user_name;
	}
	
	
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	
	public Date getChange_date () {
		return change_date;
	}
	
	public void setChange_date ( Date change_date ) {
		this.change_date = change_date;
	}
	
	/**
	 * ī���Ϸù�ȣ (��ȣȭ)
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * ī���Ϸù�ȣ (��ȣȭ)
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * ����� ������ȣ
	 * @return
	 */
	public String getUser_number () {
		return user_number;
	}
	
	/**
	 * ����� ������ȣ
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * ī�� �г���
	 * @return
	 */
	public String getCard_nickname () {
		return card_nickname;
	}
	
	/**
	 * ī�� �г���
	 * @param card_nickname
	 */
	public void setCard_nickname ( String card_nickname ) {
		this.card_nickname = card_nickname;
	}
	
	/**
	 * ī�� �����
	 * @return
	 */
	public Date getReg_date () {
		return reg_date;
	}
	
	/**
	 * ī�� �����
	 * @param reg_date
	 */
	public void setReg_date ( Date reg_date ) {
		this.reg_date = reg_date;
	}
	
	public String getCard_name_k () {
		return card_name_k;
	}
	
	public void setCard_name_k ( String card_name_k ) {
		this.card_name_k = card_name_k;
	}
	
	/**
	 * ī�� ��� ��ȣ
	 * @return
	 */
	public int getCard_reg_number () {
		return card_reg_number;
	}
	
	/**
	 * ī�� ��� ��ȣ
	 * @param card_reg_number
	 */
	public void setCard_reg_number ( Integer card_reg_number ) {
		this.card_reg_number = card_reg_number;
	}
	
	/**
	 * ī�� �ܾ�
	 * @return Integer
	 * @since -
	 * @modify 2011-07-28(my ��Ÿ���� ī�� ����_���� ī�� �� ���� ���������� ����ϱ� ���� �ּ�ó��, tytolee)
	 */
	public Integer getBalance () {
		return balance;
	}
	
	/**
	 * ī�� �ܾ�
	 * @param balance
	 */
	public void setBalance ( Integer balance ) {
		this.balance = balance;
	}
	
	/**
	 * �ܾ� Ȯ�� ����
	 * @return
	 */
	public Date getBalance_confirm_date () {
		return balance_confirm_date;
	}
	
	/**
	 * �ܾ� Ȯ�� ����
	 * @param balance_confirm_date
	 */
	public void setBalance_confirm_date ( Date balance_confirm_date ) {
		this.balance_confirm_date = balance_confirm_date;
	}
	
	/**
	 * �̹��� �ڵ�
	 * @return
	 */
	public String getCard_img_code () {
		return card_img_code;
	}
	
	/**
	 * �̹��� �ڵ�
	 * @param card_img_code
	 */
	public void setCard_img_code ( String card_img_code ) {
		this.card_img_code = card_img_code;
	}
	
	/**
	 * �̹��� ���
	 * @return
	 */
	public String getCard_img () {
		return card_img;
	}
	
	/**
	 * �̹��� ���
	 * @param card_img
	 */
	public void setCard_img ( String card_img ) {
		this.card_img = card_img;
	}
	
	/**
	 * ����� �̹��� ���
	 * @return
	 */
	public String getCard_thumbnail_img () {
		return card_thumbnail_img;
	}
	
	/**
	 * ����� �̹��� ���
	 * @param card_thumbnail_img
	 */
	public void setCard_thumbnail_img ( String card_thumbnail_img ) {
		this.card_thumbnail_img = card_thumbnail_img;
	}
	
	/**
	 * ī�� ����
	 * R : ���, S : ����, X : �̵��
	 * @return
	 */
	public String getCard_status () {
		return card_status;
	}
	
	/**
	 * ī�� ����
	 * R : ���, S : ����, X : �̵��
	 * @param card_status
	 */
	public void setCard_status ( String card_status ) {
		this.card_status = card_status;
	}
	
	/**
	 * ī�� ���� �Ϸ� �ڵ�
	 * @return
	 */
	public long getCard_reg_seq () {
		return card_reg_seq;
	}
	
	
	/**
	 * ī�� ���� �Ϸ� �ڵ�
	 * @param card_reg_seq
	 */
	public void setCard_reg_seq ( long card_reg_seq ) {
		this.card_reg_seq = card_reg_seq;
	}
	
	
	public void setAuto_reload_type_str ( String auto_reload_type_str ) {
		this.auto_reload_type_str = auto_reload_type_str;
	}
	
	
	/**
	 * ī�� ���� �ڵ�
	 * N : �Ϲ�, G : ���
	 * @return
	 */
	public String getCard_type_code () {
		return card_type_code;
	}
	
	/**
	 * ī�� ���� �ڵ�
	 * N : �Ϲ�, G : ���
	 * @param card_type_code
	 */
	public void setCard_type_code ( String card_type_code ) {
		this.card_type_code = card_type_code;
	}
	
	public String getGold_card_reg_status () {
		return gold_card_reg_status;
	}
	
	public void setGold_card_reg_status ( String gold_card_reg_status ) {
		this.gold_card_reg_status = gold_card_reg_status;
	}
	
	
	/**
	 * ����� ǥ�� ����
	 * Y : ǥ�� , N : ��ǥ��
	 * @return
	 */
	public String getMobile_view_flag () {
		return mobile_view_flag;
	}
	
	/**
	 * ����� ǥ�� ����
	 * Y : ǥ�� , N : ��ǥ��
	 * @param card_type_code
	 */
	public void setMobile_view_flag ( String mobile_view_flag ) {
		this.mobile_view_flag = mobile_view_flag;
	}
	
	/**
	 * ī�� ���� ���� ��ü
	 * U : �����, A : ������. P : POS
	 * @return
	 */
	public String getChange_actor_group_code () {
		return change_actor_group_code;
	}
	
	/**
	 * ī�� ���� ���� ��ü
	 * U : �����, A : ������. P : POS
	 * @param change_actor_group_code
	 */
	public void setChange_actor_group_code ( String change_actor_group_code ) {
		this.change_actor_group_code = change_actor_group_code;
	}
	
	/**
	 * ī�� ���� ���� ��ü
	 * U : �����, A : ������. P : POS
	 * @return
	 */
	public String getReturn_code () {
		return return_code;
	}
	
	/**
	 * ī�� ���� ���� ��ü
	 * U : �����, A : ������. P : POS
	 * @param change_actor_group_code
	 */
	public void setReturn_code ( String return_code ) {
		this.return_code = return_code;
	}
	
	/**
	 * ���ī�� ��û �׷� ��ȣ
	 * @return
	 */
	public String getGold_card_req_seq () {
		return gold_card_req_seq;
	}
	
	/**
	 * ���ī�� ��û �׷� ��ȣ
	 * @param gold_card_req_seq
	 */
	public void setGold_card_req_seq ( String gold_card_req_seq ) {
		this.gold_card_req_seq = gold_card_req_seq;
	}
	
	/**
	 * ���ī�� ��û ���ɸ���
	 * @return
	 */
	public String getStore_name () {
		return store_name;
	}
	
	/**
	 * ���ī�� ��û ���ɸ���
	 * @param store_name
	 */
	public void setStore_name ( String store_name ) {
		this.store_name = store_name;
	}
	
	/**
	 * ���ī�� ��û ���ɸ����ڵ�
	 * @return
	 */
	public String getStore_code () {
		return store_code;
	}
	
	/**
	 * ���ī�� ��û ���ɸ����ڵ�
	 * @param store_code
	 */
	public void setStore_code ( String store_code ) {
		this.store_code = store_code;
	}
	
	public String getReceive_type () {
		return receive_type;
	}
	
	public void setReceive_type ( String receive_type ) {
		this.receive_type = receive_type;
	}
	
	/**
	 * ���庸������
	 * @return
	 */
	public String getDestruct_date () {
		return destruct_date;
	}
	
	/**
	 * ���庸������
	 * @param destruct_date
	 */
	public void setDestruct_date ( String destruct_date ) {
		this.destruct_date = destruct_date;
	}
	
	/**
	 * ���ī�� �̰� ����
	 * @return
	 */
	public String getIn_store_nm () {
		return in_store_nm;
	}
	
	/**
	 * ���ī�� �̰� ����
	 * @param in_store_nm
	 */
	public void setIn_store_nm ( String in_store_nm ) {
		this.in_store_nm = in_store_nm;
	}
	
	/**
	 * ���ī�� ��û ����
	 * @return
	 */
	public String getOut_store_nm () {
		return out_store_nm;
	}
	
	/**
	 * ���ī�� ��û ����
	 * @param out_store_nm
	 */
	public void setOut_store_nm ( String out_store_nm ) {
		this.out_store_nm = out_store_nm;
	}
	
	/**
	 * ���ī�� ��û �̸���
	 * @return
	 */
	public String getEmail () {
		return email;
	}
	
	/**
	 * ���ī�� ��û �̸���
	 * @param email
	 */
	public void setEmail ( String email ) {
		this.email = email;
	}
	
	/**
	 * ���ī�� ��û �޴��� ��ȣ
	 * @return
	 */
	public String getMobile_num () {
		return mobile_num;
	}
	
	/**
	 * ���ī�� ��û �޴��� ��ȣ
	 * @param mobile_num
	 */
	public void setMobile_num ( String mobile_num ) {
		this.mobile_num = mobile_num;
	}
	
	/**
	 * ���ī�� ���� ������ from_date
	 * @return
	 */
	public String getFrom_dt () {
		return from_dt;
	}
	
	/**
	 * ���ī�� ���� ������ from_date
	 * @param from_dt
	 */
	public void setFrom_dt ( String from_dt ) {
		this.from_dt = from_dt;
	}
	
	/**
	 * ���ī�� ���� ������ to_date
	 * @return
	 */
	public String getTo_dt () {
		return to_dt;
	}
	
	/**
	 * ���ī�� ���� ������ to_date
	 * @param to_dt
	 */
	public void setTo_dt ( String to_dt ) {
		this.to_dt = to_dt;
	}
	
	/**
	 * �ڵ��������
	 * 1:�����ױ���, 2:�����ܾױ���, 9:�ڵ��������ƴ�
	 * @return
	 */
	public String getAuto_reload_type () {
		return auto_reload_type;
	}
	
	/**
	 * �ڵ��������
	 * 1:�����ױ���, 2:�����ܾױ���, 9:�ڵ��������ƴ�
	 * @param auto_reload_type
	 */
	public void setAuto_reload_type ( String auto_reload_type ) {
		this.auto_reload_type = auto_reload_type;
	}
	
	
	public String getAuto_reload_type_str () {
		if ( StringUtils.equals (getAuto_reload_type (), "1") ) {
			auto_reload_type_str = "�� ���� �ڵ� ����";
		} else if ( StringUtils.equals (getAuto_reload_type (), "2") ) {
			auto_reload_type_str = "���� ���� �ڵ� ����";
		} else if ( StringUtils.equals (getAuto_reload_type (), "9") ) {
			auto_reload_type_str = "";
		}
		return auto_reload_type_str;
	}
	
	/**
	 * �ڵ������ݾ�
	 * @return
	 */
	public String getAuto_reload_amount () {
		return auto_reload_amount;
	}
	
	/**
	 * �ڵ������ݾ�
	 * @param auto_reload_amount
	 */
	public void setAuto_reload_amount ( String auto_reload_amount ) {
		this.auto_reload_amount = auto_reload_amount;
	}
	
	/**
	 * �������ѱݾ�
	 * @return
	 */
	public String getLowest_amount () {
		return lowest_amount;
	}
	
	/**
	 * �������ѱݾ�
	 * @param lowest_amount
	 */
	public void setLowest_amount ( String lowest_amount ) {
		this.lowest_amount = lowest_amount;
	}
	
	/**
	 * �ڵ�������
	 * @return
	 */
	public String getAuto_reload_day () {
		return auto_reload_day;
	}
	
	/**
	 * �ڵ�������
	 * @param auto_reload_day
	 */
	public void setAuto_reload_day ( String auto_reload_day ) {
		this.auto_reload_day = auto_reload_day;
	}
	
	/**
	 * �ڵ�������������
	 * @return
	 */
	public String getAuto_reload_pay_method () {
		return auto_reload_pay_method;
	}
	
	/**
	 * �ڵ�������������
	 * @param auto_reload_pay_method
	 */
	public void setAuto_reload_pay_method ( String auto_reload_pay_method ) {
		this.auto_reload_pay_method = auto_reload_pay_method;
	}
	
	/**
	 * PG��Ű
	 * @return
	 */
	public String getAuto_reload_bill_key () {
		return auto_reload_bill_key;
	}
	
	/**
	 * PG��Ű
	 * @param auto_reload_bill_key
	 */
	public void setAuto_reload_bill_key ( String auto_reload_bill_key ) {
		this.auto_reload_bill_key = auto_reload_bill_key;
	}
	
	/**
	 * �ڵ�������������
	 * @return
	 */
	public String getAuto_reload_reg_date () {
		return auto_reload_reg_date;
	}
	
	/**
	 * �ڵ�������������
	 * @param auto_reload_reg_date
	 */
	public void setAuto_reload_reg_date ( String auto_reload_reg_date ) {
		this.auto_reload_reg_date = auto_reload_reg_date;
	}
	
	/**
	 * �ڵ������������
	 * @return
	 */
	public String getAuto_reload_cancel_date () {
		return auto_reload_cancel_date;
	}
	
	/**
	 * �ڵ������������
	 * @param auto_reload_cancel_date
	 */
	public void setAuto_reload_cancel_date ( String auto_reload_cancel_date ) {
		this.auto_reload_cancel_date = auto_reload_cancel_date;
	}
	
	/**
	 * �����޴���ȭ��ȣ
	 * @return
	 */
	public String getBilling_mobile_num () {
		return billing_mobile_num;
	}
	
	/**
	 * �����޴���ȭ��ȣ
	 * @param billing_mobile_num
	 */
	public void setBilling_mobile_num ( String billing_mobile_num ) {
		this.billing_mobile_num = billing_mobile_num;
	}
	
	/**
	 * �ڵ���������Ƚ��
	 * @return
	 */
	public String getAuto_reload_fail_count () {
		return auto_reload_fail_count;
	}
	
	/**
	 * �ڵ���������Ƚ��
	 * @param auto_reload_fail_count
	 */
	public void setAuto_reload_fail_count ( String auto_reload_fail_count ) {
		this.auto_reload_fail_count = auto_reload_fail_count;
	}
	
	public String getTaxsave () {
		return taxsave;
	}
	
	
	public void setTaxsave ( String taxsave ) {
		this.taxsave = taxsave;
	}
	
	
	public String getPhone_number () {
		return phone_number;
	}
	
	
	public void setPhone_number ( String phone_number ) {
		this.phone_number = phone_number;
	}
	
	
	public String getReceipt_type () {
		return receipt_type;
	}
	
	
	public void setReceipt_type ( String receipt_type ) {
		this.receipt_type = receipt_type;
	}
	
	
	public String getUser_id () {
		return user_id;
	}
	
	
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * e-����Ʈ ī�� ����
	 * Y: ����Ʈī��
	 * N: ����Ʈī��ƴ�
	 * @return
	 */
	public String getEgift_yn () {
		return egift_yn;
	}
	
	/**
	 * e-����Ʈ ī�� ����
	 * Y: ����Ʈī��
	 * N: ����Ʈī��ƴ�
	 * @param egift_yn
	 */
	public void setEgift_yn ( String egift_yn ) {
		this.egift_yn = egift_yn;
	}
	

}
