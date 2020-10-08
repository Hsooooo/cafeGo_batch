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
 * EmMmtFileDto - MMS MT 첨부파일
 * 
 * @author eZEN ksy
 * @since 2016. 10. 28.
 * @version $Revision: 1.1 $
 */
public class EmMmtFileDto {

	private long   attach_file_group_key = 0L;		// 파일 그룹키
	private long   attach_file_group_seq = 0L;		// 파일 그룹키 순번
	private long   attach_file_seq = 0L;			// 파일 순번(키)
	private String attach_file_subpath;				// 파일 하위 경로
	private String attach_file_name;				// 파일이름
	private long   attach_file_carrier = 0L;		// 전송 요청 이통사
	private Date   reg_date;						// 등록시간

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
	 * 파일 그룹키
	 * @return attach_file_group_key
	 */
	public long getAttach_file_group_key() {
		return attach_file_group_key;
	}

	/**
	 * 파일 그룹키
	 * @param attach_file_group_key
	 */
	public void setAttach_file_group_key(long attach_file_group_key) {
		this.attach_file_group_key = attach_file_group_key;
	}

	/**
	 * 파일 그룹키 순번
	 * @return attach_file_group_seq
	 */
	public long getAttach_file_group_seq() {
		return attach_file_group_seq;
	}

	/**
	 * 파일 그룹키 순번
	 * @param attach_file_group_seq
	 */
	public void setAttach_file_group_seq(long attach_file_group_seq) {
		this.attach_file_group_seq = attach_file_group_seq;
	}

	/**
	 * 파일 순번(키)
	 * @return attach_file_seq
	 */
	public long getAttach_file_seq() {
		return attach_file_seq;
	}

	/**
	 * 파일 순번(키)
	 * @param attach_file_seq
	 */
	public void setAttach_file_seq(long attach_file_seq) {
		this.attach_file_seq = attach_file_seq;
	}

	/**
	 * 파일 하위 경로
	 * @return attach_file_subpath
	 */
	public String getAttach_file_subpath() {
		return attach_file_subpath;
	}

	/**
	 * 파일 하위 경로
	 * @param attach_file_subpath
	 */
	public void setAttach_file_subpath(String attach_file_subpath) {
		this.attach_file_subpath = attach_file_subpath;
	}

	/**
	 * 파일이름
	 * @return attach_file_name
	 */
	public String getAttach_file_name() {
		return attach_file_name;
	}

	/**
	 * 파일이름
	 * @param attach_file_name
	 */
	public void setAttach_file_name(String attach_file_name) {
		this.attach_file_name = attach_file_name;
	}

	/**
	 * 전송 요청 이통사
	 * @return attach_file_carrier
	 */
	public long getAttach_file_carrier() {
		return attach_file_carrier;
	}

	/**
	 * 전송 요청 이통사
	 * @param attach_file_carrier
	 */
	public void setAttach_file_carrier(long attach_file_carrier) {
		this.attach_file_carrier = attach_file_carrier;
	}

	/**
	 * 등록시간
	 * @return reg_date
	 */
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * 등록시간
	 * @param reg_date
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

}
