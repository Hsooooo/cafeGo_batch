package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.msr.TrdXoGiftList;
import co.kr.istarbucks.xo.batch.common.dto.xo.GiftOrderHistoryDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author LeeJiHun
 */
public class XoGiftDao {

    private final static Logger logger = Logger.getLogger("EGIFT.STATISTICS");

    public void saveXoStatGiftSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public int countXoStatGiftSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }

    public void saveXoStatGiftSSGPayDay(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftSSGPayDay";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public int countXoStatGiftSSGPayDay(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftSSGPayDay";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }

    public void saveXoStatGiftPayMethodDay(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftPayMethodDay";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public Integer countXoStatGiftPayMethodDay(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftPayMethodDay";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }

    public void saveXoStatGiftUseSum(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftUseSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public int countXoStatGiftUseSum(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftUseSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }

    public void saveXoStatGiftSkuSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftSkuSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public int countXoStatGiftSkuSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftSkuSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findExtinction(String value) throws SQLException {
        String sqlID = "xoGift.findExtinction";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return sqlMap.queryForList(sqlID, value);
    }


    public void saveIcsXoGiftExpList(List<Map<String, Object>> data) throws SQLException {
        String sqlID = "xoGiftScksa.saveIcsXoGiftExpList";
        SqlMapClient sqlMap = IBatisSqlConfig.getScksasqlMapInstance();
        sqlMap.insert(sqlID, data);
    }
    
    public void saveXoStatGiftSetSkuSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.saveXoStatGiftSetSkuSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        sqlMap.insert(sqlID, value);
    }

    public int countXoStatGiftSetSkuSaleSum(String value) throws SQLException {
        String sqlID = "xoGift.countXoStatGiftSetSkuSaleSum";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, value);
    }
    
    public void saveXoStatGiftCompSkuSaleSum(String value) throws SQLException {
    	String sqlID = "xoGift.saveXoStatGiftCompSkuSaleSum";
    	SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	sqlMap.insert(sqlID, value);
    }
    
    public int countXoStatGiftCompSkuSaleSum(String value) throws SQLException {
    	String sqlID = "xoGift.countXoStatGiftCompSkuSaleSum";
    	SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	return (Integer) sqlMap.queryForObject(sqlID, value);
    }
    

    /**
     * 매장에서 거래한 GIFT 히스토리 조회
     * @param value 조회할 날짜(yyyyMMdd)
     * @return 매장에서 거래한 히스토리 리스트
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<GiftOrderHistoryDto> findStoreTradeHistoryFromXoGiftOrderHistory(String value) throws SQLException {
        String sqlID = "xoGift.findStoreTradeHistory";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return sqlMap.queryForList(sqlID, value);
    }

    public void saveAllXoGiftMsrTrdList(List<TrdXoGiftList> values) throws SQLException {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values must not be null");
        }
        else {
            SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
            sqlMap.insert("xoGift.saveAllXoGiftMsrTrdList", values);
        }
    }

    /**
     * 'XO_GIFT_ORDER_HISTORY'에서 조회한 매장거래건은 '총 결제금액'을 가지고 있지 않으므로
     * 'MSR_TRD_XO_GIFT_LIST'의 총 결제금액을 조회한 뒤, 'XO_GIFT_MSR_TRD_LIST'에 저장.
     * @param values 거래키(business_date, branch_code, pos_number, pos_trd_number)와 'gift_no'를 포함한 'XO_ORDER_HISTORY' 리스트
     * @throws SQLException
     */
    public void findFromMsrTrdXoGiftListAndSaveIntoXoGiftMsrTrdList(List<GiftOrderHistoryDto> values) throws SQLException {
        List<TrdXoGiftList> results = new ArrayList<TrdXoGiftList>();

        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
        String sqlID = "xoGiftMsr.findMsrTrdXoGiftList";

        logger.info("BEFORE SIZE : " + values.size());

        Set<GiftOrderHistoryDto> distinct = new HashSet<GiftOrderHistoryDto>(values);

        logger.info("AFTER SIZE : " + distinct.size());

        for (GiftOrderHistoryDto each : distinct) {
            TrdXoGiftList aTrdXoGiftList = (TrdXoGiftList) sqlMap.queryForObject(sqlID, each);  // select   msr_trd_xo_gift_list
            if (aTrdXoGiftList != null) {
                results.add(aTrdXoGiftList);
            }

            if ((results.size() + 1) % 50 == 0) {
                this.saveAllXoGiftMsrTrdList(results);  // insert       xo_gift_msr_trd_list
                results.clear();
            }
        }

        if (!results.isEmpty()) {
            this.saveAllXoGiftMsrTrdList(results);
        }
    }

    /**
     * 'XO_GIFT_ORDER_HISTORY' 에는 존재하지만, 'MSR_TRD_XO_GIFT_LIST'에 존재하지 않는 데이터 조회
     * @param value 조회할 날짜 (yyyyMMdd)
     * @return 'MSR_TRD_XO_GFIT_LIST'에 존재하지 않는 'XO_GIFT_ORDER_HISTORY' 데이터 목록
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<GiftOrderHistoryDto> findExistInXoGiftOrderHistoryButNotInMsrTrdXoGiftList(String value) throws SQLException {
        String sqlID = "xoGift.findExistInXoGiftOrderHistoryButNotInMsrTrdXoGiftList";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return sqlMap.queryForList(sqlID, value);
    }

    /**
     * 'MSR_DUP_TRD_XO_GIFT_LIST'에서 조회한 데이터를 'XO_GIFT_MSR_TRD_LIST'에 저장
     * @param histories 'MSR_DUP_TRD_XO_GIFT_LIST'를 조회할 거래키, GIFT 번호를 갖는 'XO_GIFT_ORDER_HISTORY' 목록
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public void findFromMsrDupTrdXoGiftListAndSaveIntoXoGiftMsrTrdList(List<GiftOrderHistoryDto> histories) throws SQLException {
        String sqlID = "xoGiftMsr.findMsrDupTrdXoGiftList";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();

        List<TrdXoGiftList> lists = new ArrayList<TrdXoGiftList>();
        for (GiftOrderHistoryDto aHistory : histories) {
            TrdXoGiftList msrDupTrdXoGiftList = (TrdXoGiftList) sqlMap.queryForObject(sqlID, aHistory);
            if (msrDupTrdXoGiftList != null) {
                lists.add(msrDupTrdXoGiftList);
            }
        }
        if (!lists.isEmpty()) {
            for (TrdXoGiftList each : lists) {
                logger.info("each=" + each.toString());
            }
            this.saveAllXoGiftMsrTrdList(lists);
        }
    }
}

