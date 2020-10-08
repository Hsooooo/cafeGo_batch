package co.kr.istarbucks.xo.batch.common.dto.home;

public class SurveyDto {
	
	private String scs_idx;
	private String scs_reward_type;
	private String scs_reward_cnt;
	private String scj_status;
	private String scj_idx;  
	private String survey_yn;
	private String cv_expire_time;
	/** 아이디 */
	private String user_id;
	/** 출생년도 */
	private String birth_year;
	/** 성별{남:M, 여:F, 외국인:R} */
	private String gender;
	
	public String getCv_expire_time() {
		return cv_expire_time;
	}
	public void setCv_expire_time(String cv_expire_time) {
		this.cv_expire_time = cv_expire_time;
	}
	public String getScs_idx() {
		return scs_idx;
	}
	public void setScs_idx(String scs_idx) {
		this.scs_idx = scs_idx;
	}
	public String getScs_reward_type() {
		return scs_reward_type;
	}
	public void setScs_reward_type(String scs_reward_type) {
		this.scs_reward_type = scs_reward_type;
	}
	public String getScs_reward_cnt() {
		return scs_reward_cnt;
	}
	public void setScs_reward_cnt(String scs_reward_cnt) {
		this.scs_reward_cnt = scs_reward_cnt;
	}
	public String getScj_status() {
		return scj_status;
	}
	public void setScj_status(String scj_status) {
		this.scj_status = scj_status;
	}
	public String getScj_idx() {
		return scj_idx;
	}
	public void setScj_idx(String scj_idx) {
		this.scj_idx = scj_idx;
	}
	public String getSurvey_yn() {
		return survey_yn;
	}
	public void setSurvey_yn(String survey_yn) {
		this.survey_yn = survey_yn;
	}

	/**
	 * 아이디
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * 아이디
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * 출생년도
	 * @return
	 */
	public String getBirth_year() {
		return birth_year;
	}
	/**
	 * 출생년도
	 * @param birth_year
	 */
	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}
	
	/**
	 * 성별{남:M, 여:F, 외국인:R}
	 * @return
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * 성별{남:M, 여:F, 외국인:R}
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SurveyDto [scs_idx=").append(scs_idx)
				.append(", scs_reward_type=").append(scs_reward_type)
				.append(", scs_reward_cnt=").append(scs_reward_cnt)
				.append(", scj_status=").append(scj_status)
				.append(", cv_expire_time=").append(cv_expire_time)
				.append(", scj_idx=").append(scj_idx).append(", survey_yn=")
				.append(survey_yn).append("]");
		return builder.toString();
	}
	
	public String toMsrvString() {
		return "SurveyDto [scs_idx=" + scs_idx + ", scj_status=" + scj_status + ", survey_yn=" + survey_yn
				+ ", birth_year=" + birth_year + ", gender=" + gender + "]";
	}
}
