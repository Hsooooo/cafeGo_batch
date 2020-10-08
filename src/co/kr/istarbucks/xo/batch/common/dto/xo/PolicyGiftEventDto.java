package co.kr.istarbucks.xo.batch.common.dto.xo;

/**
 * XO_POLICY_GIFT_EVENT
 *
 * @author LeeJiHun
 */
public class PolicyGiftEventDto {

    private String event_no;			// 이벤트 번호
    private String event_name;			// 이벤트 명
    private String start_date;			// 이벤트 시작일
    private String end_date;			// 이벤트 종료일
    private String event_gb;			// 이벤트 구분 [1:상품, 2:금액, 3:답례하기]
    private Integer add_star_cnt;		// 이벤트 리워드 별 적립 개수
    private Integer save_reward_amt; 	// 이벤트 리워드 별 적립 금액 (리워드 적립을 받기 위한 최소금액)

    /**
	 * @return the event_no
	 */
	public String getEvent_no() {
		return event_no;
	}

	/**
	 * @param event_no the event_no to set
	 */
	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}

	/**
	 * @return the event_name
	 */
	public String getEvent_name() {
		return event_name;
	}

	/**
	 * @param event_name the event_name to set
	 */
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the event_gb
	 */
	public String getEvent_gb() {
		return event_gb;
	}

	/**
	 * @param event_gb the event_gb to set
	 */
	public void setEvent_gb(String event_gb) {
		this.event_gb = event_gb;
	}

	/**
	 * @return the add_star_cnt
	 */
	public Integer getAdd_star_cnt() {
		return add_star_cnt;
	}

	/**
	 * @param add_star_cnt the add_star_cnt to set
	 */
	public void setAdd_star_cnt(Integer add_star_cnt) {
		this.add_star_cnt = add_star_cnt;
	}

	/**
	 * @return the save_reward_amt
	 */
	public Integer getSave_reward_amt() {
		return save_reward_amt;
	}

	/**
	 * @param save_reward_amt the save_reward_amt to set
	 */
	public void setSave_reward_amt(Integer save_reward_amt) {
		this.save_reward_amt = save_reward_amt;
	}

	@Override
	public String toString() {
		return "PolicyGiftEventDto [event_no=" + event_no + ", event_name="
				+ event_name + ", start_date=" + start_date + ", end_date="
				+ end_date + ", event_gb=" + event_gb + ", add_star_cnt="
				+ add_star_cnt + ", save_reward_amt=" + save_reward_amt + "]";
	}
}
