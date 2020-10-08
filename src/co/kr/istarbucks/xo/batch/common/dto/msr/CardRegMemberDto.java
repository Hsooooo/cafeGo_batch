/*
 * @(#) $Id: CardRegMemberDto.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Starbucks Service
 * Copyright 2011 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.dto.msr;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * ��Ÿ���� ī�带 ����� ȸ��
 * @author leeminjung
 * @version $Revision: 1.1 $
 */

public class CardRegMemberDto {
	
	private String user_number;
	private String user_id;
	private String user_status;
	private String reg_date;
	
	private String user_grade;
	private Date grade_date;
	private Date green_grade_date;
	private Date gold_grade_date;
	private String admin_announce;
	
	private String user_name;
	private String sex;
	private String phone_num;
	private String mobile_num;
	private String mobile_yn;
	private String email;
	private String email_yn;
	private Integer reg_card_cnt;
	private String card_number;
	private Date card_reg_date;
	private String userGradeName;
	private String regDate;
	private String card_status;
	private String cardStatus;
	private int balance;
	private String card_reg_number;
	
	private String prevGrade;
	private String gradeDate;
	private String changeDesc;
	private String changeDate;
	
	private String birth;
	private String birth_flg;
	
	private String device_number;
	
	private String card_type_code;
	private Integer goldcard_request_possible_cnt;
	private String org_user_name;
	private String star_year;
	
	private String taxsave; // ���ݿ����� (Y/N)
	private String anniversary; // �����
	private String anniversary_dt; // �������
	private String anniversary_flag; // ����� ��/���� ����
	
	private String ipinDupKey; //����� ���� key
	

	public String getIpinDupKey () {
		return ipinDupKey;
	}
	
	public void setIpinDupKey ( String ipinDupKey ) {
		this.ipinDupKey = ipinDupKey;
	}
	
	public String getChangeDate () {
		return changeDate;
	}
	
	public void setChangeDate ( String changeDate ) {
		this.changeDate = changeDate;
	}
	
	public String getEmail () {
		return email;
	}
	
	public String getPrevGrade () {
		return prevGrade;
	}
	
	public void setPrevGrade ( String prevGrade ) {
		this.prevGrade = prevGrade;
	}
	
	public int getBalance () {
		return balance;
	}
	
	public String getCard_reg_number () {
		return card_reg_number;
	}
	
	public void setCard_reg_number ( String card_reg_number ) {
		this.card_reg_number = card_reg_number;
	}
	
	public void setBalance ( int balance ) {
		this.balance = balance;
	}
	
	public String getGradeDate () {
		return gradeDate;
	}
	
	public String getCardStatus () {
		return cardStatus;
	}
	
	public void setCardStatus ( String cardStatus ) {
		this.cardStatus = cardStatus;
	}
	
	public void setGradeDate ( String gradeDate ) {
		this.gradeDate = gradeDate;
	}
	
	public String getChangeDesc () {
		return changeDesc;
	}
	
	public String getCard_status () {
		return card_status;
	}
	
	public void setCard_status ( String card_status ) {
		this.card_status = card_status;
	}
	
	public void setChangeDesc ( String changeDesc ) {
		this.changeDesc = changeDesc;
	}
	
	public void setEmail ( String email ) {
		this.email = email;
	}
	
	public Date getCard_reg_date () {
		return card_reg_date;
	}
	
	public void setCard_reg_date ( Date card_reg_date ) {
		this.card_reg_date = card_reg_date;
	}
	
	private List<UserRegCardDto> reg_card;
	
	
	@Override
	public String toString () {
		StringBuffer str = new StringBuffer ();
		
		str.append ("CardRegisterMemberDto [");
		str.append ("user_number=").append (this.user_number);
		str.append (", ").append ("user_id=").append (this.user_id);
		str.append (", ").append ("user_status=").append (this.user_status);
		str.append (", ").append ("user_grade=").append (this.user_grade);
		if ( this.grade_date != null ) {
			str.append (", ").append ("grade_date=").append (DateFormatUtils.format (this.grade_date, "yyyy-MM-dd HH:mm:ss"));
		}
		if ( this.green_grade_date != null ) {
			str.append (", ").append ("green_grade_date=").append (DateFormatUtils.format (this.green_grade_date, "yyyy-MM-dd HH:mm:ss"));
		}
		if ( this.gold_grade_date != null ) {
			str.append (", ").append ("gold_grade_date=").append (DateFormatUtils.format (this.gold_grade_date, "yyyy-MM-dd HH:mm:ss"));
		}
		str.append (", ").append ("admin_announce=").append (this.admin_announce);
		str.append (", ").append ("taxsave=").append (this.taxsave);
		str.append (", ").append ("anniversary=").append (this.anniversary);
		str.append (", ").append ("anniversary_dt=").append (this.anniversary_dt);
		str.append (", ").append ("anniversary_flag=").append (this.anniversary_flag);
		str.append ("]");
		
		return str.toString ();
		
	}
	
	public String getRegDate () {
		return regDate;
	}
	
	public void setRegDate ( String regDate ) {
		this.regDate = regDate;
	}
	
	/**
	 * ����� ������ȣ
	 * @return
	 */
	public String getUser_number () {
		return user_number;
	}
	
	public String getUserGradeName () {
		return userGradeName;
	}
	
	public void setUserGradeName ( String userGradeName ) {
		this.userGradeName = userGradeName;
	}
	
	/**
	 * ����� ������ȣ
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * ����� ���̵�
	 * @return
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * ����� ���̵�
	 * @param user_id
	 */
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * ����� ����
	 * J : ����, X : ����
	 * @return
	 */
	public String getUser_status () {
		return user_status;
	}
	
	/**
	 * ����� ����
	 * J : ����, X : ����
	 * @param user_status
	 */
	public void setUser_status ( String user_status ) {
		this.user_status = user_status;
	}
	
	/**
	 * ��� ī�� ����Ʈ
	 * @return
	 */
	public List<UserRegCardDto> getReg_card () {
		return reg_card;
	}
	
	/**
	 * ��� ī�� ����Ʈ
	 * @param reg_card
	 */
	public void setReg_card ( List<UserRegCardDto> reg_card ) {
		this.reg_card = reg_card;
	}
	
	/**
	 * ȸ�� ���
	 * 00 : Welcome, 10 : Green, 11 : GGreen, 20 : Gold, 21 : GGold
	 * @return
	 */
	public String getUser_grade () {
		return user_grade;
	}
	
	/**
	 * ȸ�� ���
	 * 00 : Welcome, 10 : Green, 11 : GGreen, 20 : Gold, 21 : GGold
	 * @param user_grade
	 */
	public void setUser_grade ( String user_grade ) {
		this.user_grade = user_grade;
	}
	
	/**
	 * ��� ���� ����
	 * @return
	 */
	public Date getGrade_date () {
		return grade_date;
	}
	
	/**
	 * ��� ���� ����
	 * @param grade_date
	 */
	public void setGrade_date ( Date grade_date ) {
		this.grade_date = grade_date;
	}
	
	/**
	 * �׸� ȸ�� �±���
	 * @return
	 */
	public Date getGreen_grade_date () {
		return green_grade_date;
	}
	
	/**
	 * �׸� ȸ�� �±���
	 * @param green_grade_date
	 */
	public void setGreen_grade_date ( Date green_grade_date ) {
		this.green_grade_date = green_grade_date;
	}
	
	/**
	 * ��� ȸ�� �±���
	 * @return
	 */
	public Date getGold_grade_date () {
		return gold_grade_date;
	}
	
	/**
	 * ��� ȸ�� �±���
	 * @param gold_grade_date
	 */
	public void setGold_grade_date ( Date gold_grade_date ) {
		this.gold_grade_date = gold_grade_date;
	}
	
	/**
	 * ������ ���� ����
	 * @return
	 */
	public String getAdmin_announce () {
		return admin_announce;
	}
	
	/**
	 * ������ ���� ����
	 * @param admin_announce
	 */
	public void setAdmin_announce ( String admin_announce ) {
		this.admin_announce = admin_announce;
	}
	
	/**
	 * ����� �̸�
	 * @return
	 */
	public String getUser_name () {
		return user_name;
	}
	
	/**
	 * ����� �̸�
	 * @param user_name
	 */
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	/**
	 * ����� ����
	 * @return
	 */
	public String getSex () {
		return sex;
	}
	
	/**
	 * ����� ����
	 * @param sex
	 */
	public void setSex ( String sex ) {
		this.sex = sex;
	}
	
	/**
	 * ����� ��ȭ��ȣ
	 * @return
	 */
	public String getPhone_num () {
		return phone_num;
	}
	
	/**
	 * ����� ��ȭ��ȣ
	 * @param phone_num
	 */
	public void setPhone_num ( String phone_num ) {
		this.phone_num = phone_num;
	}
	
	/**
	 * ����� ����� ��ȣ
	 * @return
	 */
	public String getMobile_num () {
		return mobile_num;
	}
	
	/**
	 * ����� ����� ��ȣ
	 * @param mobile_num
	 */
	public void setMobile_num ( String mobile_num ) {
		this.mobile_num = mobile_num;
	}
	
	/**
	 * ����� ī�� ����
	 * @return
	 */
	public Integer getReg_card_cnt () {
		return reg_card_cnt;
	}
	
	/**
	 * ����� ī�� ����
	 * @param reg_card_cnt
	 */
	public void setReg_card_cnt ( Integer reg_card_cnt ) {
		this.reg_card_cnt = reg_card_cnt;
	}
	
	/**
	 * ����� ī�� ��ȣ
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * ����� ī�� ��ȣ
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * �����
	 * @return
	 */
	public String getReg_date () {
		return reg_date;
	}
	
	/**
	 * �����
	 * @param reg_date
	 */
	public void setReg_date ( String reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * ����
	 * @return
	 */
	public String getBirth () {
		return birth;
	}
	
	/**
	 * ����
	 * @param birth
	 */
	public void setBirth ( String birth ) {
		this.birth = birth;
	}
	
	/**
	 * ���� ���� yyyy-MM-dd
	 * @return
	 */
	public String getBirth_flg () {
		return birth_flg;
	}
	
	/**
	 * ���� ���� 1:���
	 * @param birth_flg
	 */
	public void setBirth_flg ( String birth_flg ) {
		this.birth_flg = birth_flg;
	}
	
	/**
	 * ����� ������ȣ
	 * @return
	 */
	public String getDevice_number () {
		return device_number;
	}
	
	/**
	 * ����� ������ȣ
	 * @param device_number
	 */
	public void setDevice_number ( String device_number ) {
		this.device_number = device_number;
	}
	
	/**
	 * ī�� ���� �ڵ�(N - �Ϲ�, G - ���)
	 * @return
	 */
	public String getCard_type_code () {
		return card_type_code;
	}
	
	/**
	 * ī�� ���� �ڵ�(N-�Ϲ�, G-���)
	 * @param card_type_code
	 */
	public void setCard_type_code ( String card_type_code ) {
		this.card_type_code = card_type_code;
	}
	
	/**
	 * ���ī�� ��û ���� Ƚ��
	 * @return
	 */
	public Integer getGoldcard_request_possible_cnt () {
		return goldcard_request_possible_cnt;
	}
	
	/**
	 * ���ī�� ��û ���� Ƚ��
	 * @param goldcard_request_possible_cnt
	 */
	public void setGoldcard_request_possible_cnt ( Integer goldcard_request_possible_cnt ) {
		this.goldcard_request_possible_cnt = goldcard_request_possible_cnt;
	}
	
	
	public String getOrg_user_name () {
		return org_user_name;
	}
	
	public void setOrg_user_name ( String org_user_name ) {
		this.org_user_name = org_user_name;
	}
	
	
	/**
	 * Star����⵵(YYYY)
	 * @param star_year
	 */
	public String getStar_year () {
		return star_year;
	}
	
	/**
	 * Star����⵵(YYYY)
	 * @return star_year
	 */
	public void setStar_year ( String star_year ) {
		this.star_year = star_year;
	}
	
	/**
	 * ���ݿ����� (Y/N)
	 * @return taxsave
	 */
	public String getTaxsave () {
		return taxsave;
	}
	
	/**
	 * ���ݿ����� (Y/N)
	 * @param taxsave
	 */
	public void setTaxsave ( String taxsave ) {
		this.taxsave = taxsave;
	}
	
	/**
	 * �����
	 * @return anniversary
	 */
	public String getAnniversary () {
		return anniversary;
	}
	
	/**
	 * �����
	 * @param anniversary
	 */
	public void setAnniversary ( String anniversary ) {
		this.anniversary = anniversary;
	}
	
	/**
	 * �������
	 * @return anniversary_dt
	 */
	public String getAnniversary_dt () {
		return anniversary_dt;
	}
	
	/**
	 * �������
	 * @param anniversary_dt
	 */
	public void setAnniversary_dt ( String anniversary_dt ) {
		this.anniversary_dt = anniversary_dt;
	}
	
	/**
	 * ����� ��/���� ����
	 * @return anniversary_flag
	 */
	public String getAnniversary_flag () {
		return anniversary_flag;
	}
	
	/**
	 * ����� ��/���� ����
	 * @param anniversary_flag
	 */
	public void setAnniversary_flag ( String anniversary_flag ) {
		this.anniversary_flag = anniversary_flag;
	}
	
	/**
	 * SMS���ſ���(Y:���� N:���Űź�)
	 * @return
	 */
	public String getMobile_yn () {
		return mobile_yn;
	}
	
	/**
	 * SMS���ſ���(Y:���� N:���Űź�)
	 * @param mobile_yn
	 */
	public void setMobile_yn ( String mobile_yn ) {
		this.mobile_yn = mobile_yn;
	}
	
	/**
	 * �̸��ϼ��ſ���(Y:���� N:���Űź�)
	 * @return
	 */
	public String getEmail_yn () {
		return email_yn;
	}
	
	/**
	 * �̸��ϼ��ſ���(Y:���� N:���Űź�)
	 * @param email_yn
	 */
	public void setEmail_yn ( String email_yn ) {
		this.email_yn = email_yn;
	}
	
}
