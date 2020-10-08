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
 * 스타벅스 카드를 등록한 회원
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
	
	private String taxsave; // 현금영수증 (Y/N)
	private String anniversary; // 기념일
	private String anniversary_dt; // 기념일자
	private String anniversary_flag; // 기념일 양/음력 구분
	
	private String ipinDupKey; //사용자 고유 key
	

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
	 * 사용자 고유번호
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
	 * 사용자 고유번호
	 * @param user_number
	 */
	public void setUser_number ( String user_number ) {
		this.user_number = user_number;
	}
	
	/**
	 * 사용자 아이디
	 * @return
	 */
	public String getUser_id () {
		return user_id;
	}
	
	/**
	 * 사용자 아이디
	 * @param user_id
	 */
	public void setUser_id ( String user_id ) {
		this.user_id = user_id;
	}
	
	/**
	 * 사용자 상태
	 * J : 가입, X : 해지
	 * @return
	 */
	public String getUser_status () {
		return user_status;
	}
	
	/**
	 * 사용자 상태
	 * J : 가입, X : 해지
	 * @param user_status
	 */
	public void setUser_status ( String user_status ) {
		this.user_status = user_status;
	}
	
	/**
	 * 등록 카드 리스트
	 * @return
	 */
	public List<UserRegCardDto> getReg_card () {
		return reg_card;
	}
	
	/**
	 * 등록 카드 리스트
	 * @param reg_card
	 */
	public void setReg_card ( List<UserRegCardDto> reg_card ) {
		this.reg_card = reg_card;
	}
	
	/**
	 * 회원 등급
	 * 00 : Welcome, 10 : Green, 11 : GGreen, 20 : Gold, 21 : GGold
	 * @return
	 */
	public String getUser_grade () {
		return user_grade;
	}
	
	/**
	 * 회원 등급
	 * 00 : Welcome, 10 : Green, 11 : GGreen, 20 : Gold, 21 : GGold
	 * @param user_grade
	 */
	public void setUser_grade ( String user_grade ) {
		this.user_grade = user_grade;
	}
	
	/**
	 * 등급 적용 일자
	 * @return
	 */
	public Date getGrade_date () {
		return grade_date;
	}
	
	/**
	 * 등급 적용 일자
	 * @param grade_date
	 */
	public void setGrade_date ( Date grade_date ) {
		this.grade_date = grade_date;
	}
	
	/**
	 * 그린 회원 승급일
	 * @return
	 */
	public Date getGreen_grade_date () {
		return green_grade_date;
	}
	
	/**
	 * 그린 회원 승급일
	 * @param green_grade_date
	 */
	public void setGreen_grade_date ( Date green_grade_date ) {
		this.green_grade_date = green_grade_date;
	}
	
	/**
	 * 골드 회원 승급일
	 * @return
	 */
	public Date getGold_grade_date () {
		return gold_grade_date;
	}
	
	/**
	 * 골드 회원 승급일
	 * @param gold_grade_date
	 */
	public void setGold_grade_date ( Date gold_grade_date ) {
		this.gold_grade_date = gold_grade_date;
	}
	
	/**
	 * 관리자 지정 문구
	 * @return
	 */
	public String getAdmin_announce () {
		return admin_announce;
	}
	
	/**
	 * 관리자 지정 문구
	 * @param admin_announce
	 */
	public void setAdmin_announce ( String admin_announce ) {
		this.admin_announce = admin_announce;
	}
	
	/**
	 * 사용자 이름
	 * @return
	 */
	public String getUser_name () {
		return user_name;
	}
	
	/**
	 * 사용자 이름
	 * @param user_name
	 */
	public void setUser_name ( String user_name ) {
		this.user_name = user_name;
	}
	
	/**
	 * 사용자 성별
	 * @return
	 */
	public String getSex () {
		return sex;
	}
	
	/**
	 * 사용자 성별
	 * @param sex
	 */
	public void setSex ( String sex ) {
		this.sex = sex;
	}
	
	/**
	 * 사용자 전화번호
	 * @return
	 */
	public String getPhone_num () {
		return phone_num;
	}
	
	/**
	 * 사용자 전화번호
	 * @param phone_num
	 */
	public void setPhone_num ( String phone_num ) {
		this.phone_num = phone_num;
	}
	
	/**
	 * 사용자 모바일 번호
	 * @return
	 */
	public String getMobile_num () {
		return mobile_num;
	}
	
	/**
	 * 사용자 모바일 번호
	 * @param mobile_num
	 */
	public void setMobile_num ( String mobile_num ) {
		this.mobile_num = mobile_num;
	}
	
	/**
	 * 사용자 카드 개수
	 * @return
	 */
	public Integer getReg_card_cnt () {
		return reg_card_cnt;
	}
	
	/**
	 * 사용자 카드 개수
	 * @param reg_card_cnt
	 */
	public void setReg_card_cnt ( Integer reg_card_cnt ) {
		this.reg_card_cnt = reg_card_cnt;
	}
	
	/**
	 * 사용자 카드 번호
	 * @return
	 */
	public String getCard_number () {
		return card_number;
	}
	
	/**
	 * 사용자 카드 번호
	 * @param card_number
	 */
	public void setCard_number ( String card_number ) {
		this.card_number = card_number;
	}
	
	/**
	 * 등록일
	 * @return
	 */
	public String getReg_date () {
		return reg_date;
	}
	
	/**
	 * 등록일
	 * @param reg_date
	 */
	public void setReg_date ( String reg_date ) {
		this.reg_date = reg_date;
	}
	
	/**
	 * 생일
	 * @return
	 */
	public String getBirth () {
		return birth;
	}
	
	/**
	 * 생일
	 * @param birth
	 */
	public void setBirth ( String birth ) {
		this.birth = birth;
	}
	
	/**
	 * 생일 구분 yyyy-MM-dd
	 * @return
	 */
	public String getBirth_flg () {
		return birth_flg;
	}
	
	/**
	 * 생일 구분 1:양력
	 * @param birth_flg
	 */
	public void setBirth_flg ( String birth_flg ) {
		this.birth_flg = birth_flg;
	}
	
	/**
	 * 사용기기 고유번호
	 * @return
	 */
	public String getDevice_number () {
		return device_number;
	}
	
	/**
	 * 사용기기 고유번호
	 * @param device_number
	 */
	public void setDevice_number ( String device_number ) {
		this.device_number = device_number;
	}
	
	/**
	 * 카드 구분 코드(N - 일반, G - 골드)
	 * @return
	 */
	public String getCard_type_code () {
		return card_type_code;
	}
	
	/**
	 * 카드 구분 코드(N-일반, G-골드)
	 * @param card_type_code
	 */
	public void setCard_type_code ( String card_type_code ) {
		this.card_type_code = card_type_code;
	}
	
	/**
	 * 골드카드 신청 가능 횟수
	 * @return
	 */
	public Integer getGoldcard_request_possible_cnt () {
		return goldcard_request_possible_cnt;
	}
	
	/**
	 * 골드카드 신청 가능 횟수
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
	 * Star멤버년도(YYYY)
	 * @param star_year
	 */
	public String getStar_year () {
		return star_year;
	}
	
	/**
	 * Star멤버년도(YYYY)
	 * @return star_year
	 */
	public void setStar_year ( String star_year ) {
		this.star_year = star_year;
	}
	
	/**
	 * 현금영수증 (Y/N)
	 * @return taxsave
	 */
	public String getTaxsave () {
		return taxsave;
	}
	
	/**
	 * 현금영수증 (Y/N)
	 * @param taxsave
	 */
	public void setTaxsave ( String taxsave ) {
		this.taxsave = taxsave;
	}
	
	/**
	 * 기념일
	 * @return anniversary
	 */
	public String getAnniversary () {
		return anniversary;
	}
	
	/**
	 * 기념일
	 * @param anniversary
	 */
	public void setAnniversary ( String anniversary ) {
		this.anniversary = anniversary;
	}
	
	/**
	 * 기념일자
	 * @return anniversary_dt
	 */
	public String getAnniversary_dt () {
		return anniversary_dt;
	}
	
	/**
	 * 기념일자
	 * @param anniversary_dt
	 */
	public void setAnniversary_dt ( String anniversary_dt ) {
		this.anniversary_dt = anniversary_dt;
	}
	
	/**
	 * 기념일 양/음력 구분
	 * @return anniversary_flag
	 */
	public String getAnniversary_flag () {
		return anniversary_flag;
	}
	
	/**
	 * 기념일 양/음력 구분
	 * @param anniversary_flag
	 */
	public void setAnniversary_flag ( String anniversary_flag ) {
		this.anniversary_flag = anniversary_flag;
	}
	
	/**
	 * SMS수신여부(Y:수신 N:수신거부)
	 * @return
	 */
	public String getMobile_yn () {
		return mobile_yn;
	}
	
	/**
	 * SMS수신여부(Y:수신 N:수신거부)
	 * @param mobile_yn
	 */
	public void setMobile_yn ( String mobile_yn ) {
		this.mobile_yn = mobile_yn;
	}
	
	/**
	 * 이메일수신여부(Y:수신 N:수신거부)
	 * @return
	 */
	public String getEmail_yn () {
		return email_yn;
	}
	
	/**
	 * 이메일수신여부(Y:수신 N:수신거부)
	 * @param email_yn
	 */
	public void setEmail_yn ( String email_yn ) {
		this.email_yn = email_yn;
	}
	
}
