/*
 * @(#) $Id: EmMmtFileDto.java,v 1.1 2016/11/10 00:55:27 dev99 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.xo;

import java.util.Date;

/**
 * EmMmtFileDto - MMS MT ÷������
 * 
 * @author eZEN ksy
 * @since 2016. 10. 28.
 * @version $Revision: 1.1 $
 */
public class EmMmtFileDto {

	private long   attach_file_group_key = 0L;		// ���� �׷�Ű
	private long   attach_file_group_seq = 0L;		// ���� �׷�Ű ����
	private long   attach_file_seq = 0L;			// ���� ����(Ű)
	private String attach_file_subpath;				// ���� ���� ���
	private String attach_file_name;				// �����̸�
	private long   attach_file_carrier = 0L;		// ���� ��û �����
	private Date   reg_date;						// ��Ͻð�

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append(", attach_file_group_key=").append(this.attach_file_group_key);
		sb.append(", attach_file_group_seq=").append(this.attach_file_group_seq);
		sb.append(", attach_file_seq="		).append(this.attach_file_seq      );
		sb.append(", attach_file_subpath="	).append(this.attach_file_subpath  );
		sb.append(", attach_file_name="		).append(this.attach_file_name     );
		sb.append(", attach_file_carrier="	).append(this.attach_file_carrier  );
		sb.append(", reg_date="				).append(this.reg_date             );
		sb.append("]");
		return sb.toString();
	}

	/**
	 * ���� �׷�Ű
	 * @return attach_file_group_key
	 */
	public long getAttach_file_group_key() {
		return attach_file_group_key;
	}

	/**
	 * ���� �׷�Ű
	 * @param attach_file_group_key
	 */
	public void setAttach_file_group_key(long attach_file_group_key) {
		this.attach_file_group_key = attach_file_group_key;
	}

	/**
	 * ���� �׷�Ű ����
	 * @return attach_file_group_seq
	 */
	public long getAttach_file_group_seq() {
		return attach_file_group_seq;
	}

	/**
	 * ���� �׷�Ű ����
	 * @param attach_file_group_seq
	 */
	public void setAttach_file_group_seq(long attach_file_group_seq) {
		this.attach_file_group_seq = attach_file_group_seq;
	}

	/**
	 * ���� ����(Ű)
	 * @return attach_file_seq
	 */
	public long getAttach_file_seq() {
		return attach_file_seq;
	}

	/**
	 * ���� ����(Ű)
	 * @param attach_file_seq
	 */
	public void setAttach_file_seq(long attach_file_seq) {
		this.attach_file_seq = attach_file_seq;
	}

	/**
	 * ���� ���� ���
	 * @return attach_file_subpath
	 */
	public String getAttach_file_subpath() {
		return attach_file_subpath;
	}

	/**
	 * ���� ���� ���
	 * @param attach_file_subpath
	 */
	public void setAttach_file_subpath(String attach_file_subpath) {
		this.attach_file_subpath = attach_file_subpath;
	}

	/**
	 * �����̸�
	 * @return attach_file_name
	 */
	public String getAttach_file_name() {
		return attach_file_name;
	}

	/**
	 * �����̸�
	 * @param attach_file_name
	 */
	public void setAttach_file_name(String attach_file_name) {
		this.attach_file_name = attach_file_name;
	}

	/**
	 * ���� ��û �����
	 * @return attach_file_carrier
	 */
	public long getAttach_file_carrier() {
		return attach_file_carrier;
	}

	/**
	 * ���� ��û �����
	 * @param attach_file_carrier
	 */
	public void setAttach_file_carrier(long attach_file_carrier) {
		this.attach_file_carrier = attach_file_carrier;
	}

	/**
	 * ��Ͻð�
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * ��Ͻð�
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

}
