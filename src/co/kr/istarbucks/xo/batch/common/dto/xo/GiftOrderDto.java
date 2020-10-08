package co.kr.istarbucks.xo.batch.common.dto.xo;

/**
 * XO_GIFT_ORDER# (선물 주문 정보)
 *
 * @author LeeJiHun
 */
public class GiftOrderDto {

    private String gift_order_no;
    private String user_id;
    private Integer gift_issue_cnt;		// 선물 발행 개수

    public GiftOrderDto() {
    }

    public String getGift_order_no() {
        return gift_order_no;
    }

    public void setGift_order_no(String gift_order_no) {
        this.gift_order_no = gift_order_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

	/**
	 * @return the gift_issue_cnt
	 */
	public Integer getGift_issue_cnt() {
		return gift_issue_cnt;
	}

	/**
	 * @param gift_issue_cnt the gift_issue_cnt to set
	 */
	public void setGift_issue_cnt(Integer gift_issue_cnt) {
		this.gift_issue_cnt = gift_issue_cnt;
	}
    
}
