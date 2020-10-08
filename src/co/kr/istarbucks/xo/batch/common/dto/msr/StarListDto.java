package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * MSR_STAR_LIST# (�� �̷�)
 *
 * @author LeeJiHun
 */
public class StarListDto {

    /**
     * �� �̷� ��ȣ (�������� + SEQ)
     */
    private String star_seq;

    /**
     * ���� (I - ����, G - �±޻��, F - �������, C - ȸ��)
     */
    private String star_flag;

    /**
     * ����� ������ȣ
     */
    private String user_number;

    /**
     * ��� ��ȣ
     */
    private String event_no;

    /**
     * �� ��
     */
    private int star_count;

    /**
     * �� �߰��� ����
     */
    private int extra_star_all_cnt;

    /**
     * �� �ο� ����
     */
    private String grant_desc;
    
    /**
     * ���� �ֹ���ȣ
     */
    private String gift_order_no;
    
    /**
     * ���� ���� ����
     */
    private Integer gift_issue_cnt;
    
    /** 
     * �� �̷� �׷� ���� 
     */
    private String star_grp_seq;

    public StarListDto() {
    }

    public String getStar_seq() {
        return star_seq;
    }

    public void setStar_seq(String star_seq) {
        this.star_seq = star_seq;
    }

    public String getStar_flag() {
        return star_flag;
    }

    public void setStar_flag(String star_flag) {
        this.star_flag = star_flag;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getEvent_no() {
        return event_no;
    }

    public void setEvent_no(String event_no) {
        this.event_no = event_no;
    }

    public int getStar_count() {
        return star_count;
    }

    public void setStar_count(int star_count) {
        this.star_count = star_count;
    }

    public int getExtra_star_all_cnt() {
        return extra_star_all_cnt;
    }

    public void setExtra_star_all_cnt(int extra_star_all_cnt) {
        this.extra_star_all_cnt = extra_star_all_cnt;
    }

    public String getGrant_desc() {
        return grant_desc;
    }

    public void setGrant_desc(String grant_desc) {
        this.grant_desc = grant_desc;
    }

	/**
	 * @return the gift_order_no
	 */
	public String getGift_order_no() {
		return gift_order_no;
	}

	/**
	 * @param gift_order_no the gift_order_no to set
	 */
	public void setGift_order_no(String gift_order_no) {
		this.gift_order_no = gift_order_no;
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

	public String getStar_grp_seq() {
		return star_grp_seq;
	}

	public void setStar_grp_seq(String star_grp_seq) {
		this.star_grp_seq = star_grp_seq;
	}
}
