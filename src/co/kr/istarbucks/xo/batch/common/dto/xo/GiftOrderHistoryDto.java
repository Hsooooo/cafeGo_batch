package co.kr.istarbucks.xo.batch.common.dto.xo;

/**
 * @author LeeJiHun
 */
public class GiftOrderHistoryDto {
    private String sale_date;
    private String store_cd;
    private String pos_no;
    private String seq_no;
    private String gift_no;

    public String getSale_date() {
        return sale_date;
    }

    public void setSale_date(String sale_date) {
        this.sale_date = sale_date;
    }

    public String getStore_cd() {
        return store_cd;
    }

    public void setStore_cd(String store_cd) {
        this.store_cd = store_cd;
    }

    public String getPos_no() {
        return pos_no;
    }

    public void setPos_no(String pos_no) {
        this.pos_no = pos_no;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getGift_no() {
        return gift_no;
    }

    public void setGift_no(String gift_no) {
        this.gift_no = gift_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftOrderHistoryDto that = (GiftOrderHistoryDto) o;

        if (sale_date != null ? !sale_date.equals(that.sale_date) : that.sale_date != null)
            return false;
        if (store_cd != null ? !store_cd.equals(that.store_cd) : that.store_cd != null)
            return false;
        if (pos_no != null ? !pos_no.equals(that.pos_no) : that.pos_no != null)
            return false;
        if (seq_no != null ? !seq_no.equals(that.seq_no) : that.seq_no != null)
            return false;
        return gift_no != null ? gift_no.equals(that.gift_no) : that.gift_no == null;
    }

    @Override
    public int hashCode() {
        int result = sale_date != null ? sale_date.hashCode() : 0;
        result = 31 * result + (store_cd != null ? store_cd.hashCode() : 0);
        result = 31 * result + (pos_no != null ? pos_no.hashCode() : 0);
        result = 31 * result + (seq_no != null ? seq_no.hashCode() : 0);
        result = 31 * result + (gift_no != null ? gift_no.hashCode() : 0);
        return result;
    }
}
