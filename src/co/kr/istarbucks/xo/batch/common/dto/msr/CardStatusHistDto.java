/*
 * @(#) $Id: CardStatusHistDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * ī�� ���� ���� �̷�
 * @author leeminjung
 * @version $Revision: 1.1 $
 */

public class CardStatusHistDto {
	private String card_number;
	private String change_actor_group_code;
	private String change_actor_id;
	private String user_number;
	private Integer card_reg_number;
	private String change_status;
	private String no_use_desc;
	private Date change_date;
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("CardStatusHistoryDto [");
		str.append ("card_number=").append (this.card_number);
		str.append (", ").append ("change_actor_group_code=").append (this.change_actor_group_code);
		str.append (", ").append ("change_actor_id=").append (this.change_actor_id);
		str.append (", ").append ("user_number=").append (this.user_number);
		str.append (", ").append ("card_reg_number=").append (this.card_reg_number);
		str.append (", ").append ("change_status=").append (this.change_status);
		str.append (", ").append ("no_use_desc=").append (this.no_use_desc);
		if ( this.change_date != null ) {
			str.append (", ").append ("change_date=").append (DateFormatUtils.format (this.change_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append ("]");
		
		return str.toString ();
		
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
	 * ���� ���� ��ü ����
	 * U : �����, A : ������, P : POS
	 * @return
	 */
	public String getChange_actor_group_code () {
		return change_actor_group_code;
	}
	
	/**
	 * ���� ���� ��ü ����
	 * U : �����, A : ������, P : POS
	 * @param change_actor_group_code
	 */
	public void setChange_actor_group_code ( String change_actor_group_code ) {
		this.change_actor_group_code = change_actor_group_code;
	}
	
	/**
	 * ���� ������ ���̵�
	 * @return
	 */
	public String getChange_actor_id () {
		return change_actor_id;
	}
	
	/**
	 * ���� ������ ���̵�
	 * @param change_actor_id
	 */
	public void setChange_actor_id ( String change_actor_id ) {
		this.change_actor_id = change_actor_id;
	}
	
	/**
	 * ����� ������ȣ
	 * @return
	 */
	public String getUser_number () {
		return user_number;
	}
	
	
	/**
	 * ī�� ��� ��ȣ
	 * @return
	 */
	public Integer getCard_reg_number () {
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
	 * ����� ������ȣ
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * ���� ����
	 * R : ���, L : �н�, X : �̵��/����
	 * @return
	 */
	public String getChange_status () {
		return change_status;
	}
	
	/**
	 * ���� ����
	 * R : ���, L : �н�, X : �̵��/����
	 * @param change_status
	 */
	public void setChange_status ( String change_status ) {
		this.change_status = change_status;
	}
	
	
	/**
	 * ���� ��û ����
	 * @return
	 */
	public String getNo_use_desc () {
		return no_use_desc;
	}
	
	/**
	 * ���� ��û ����
	 * @param no_use_desc
	 */
	public void setNo_use_desc ( String no_use_desc ) {
		this.no_use_desc = no_use_desc;
	}
	
	/**
	 * ���� ���� ����
	 * @return
	 */
	public Date getChange_date () {
		return change_date;
	}
	
	/**
	 * ���� ���� ����
	 * @param change_date
	 */
	public void setChange_date ( Date change_date ) {
		this.change_date = change_date;
	}
}
