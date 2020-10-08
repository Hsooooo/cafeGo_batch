package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * @author LeeJiHun
 */
public class TrdXoGiftList {
    private String business_date;
    private String branch_code;
    private String pos_number;
    private String pos_trd_number;
    private String gift_no;
    private Integer total_trd_amount;
    private Integer trd_amount;

    public String getBusiness_date() {
        return business_date;
    }

    public void setBusiness_date(String business_date) {
        this.business_date = business_date;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getPos_number() {
        return pos_number;
    }

    public void setPos_number(String pos_number) {
        this.pos_number = pos_number;
    }

    public String getPos_trd_number() {
        return pos_trd_number;
    }

    public void setPos_trd_number(String pos_trd_number) {
        this.pos_trd_number = pos_trd_number;
    }

    public String getGift_no() {
        return gift_no;
    }

    public void setGift_no(String gift_no) {
        this.gift_no = gift_no;
    }

    public Integer getTotal_trd_amount() {
        return total_trd_amount;
    }

    public void setTotal_trd_amount(Integer total_trd_amount) {
        this.total_trd_amount = total_trd_amount;
    }

    public Integer getTrd_amount() {
        return trd_amount;
    }

    public void setTrd_amount(Integer trd_amount) {
        this.trd_amount = trd_amount;
    }

    @Override
    public String toString() {
        return "TrdXoGiftList{" +
                "business_date='" + business_date + '\'' +
                ", branch_code='" + branch_code + '\'' +
                ", pos_number='" + pos_number + '\'' +
                ", pos_trd_number='" + pos_trd_number + '\'' +
                ", gift_no='" + gift_no + '\'' +
                ", total_trd_amount=" + total_trd_amount +
                ", trd_amount=" + trd_amount +
                '}';
    }
}
