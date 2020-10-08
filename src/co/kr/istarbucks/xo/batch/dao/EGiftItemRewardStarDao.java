package co.kr.istarbucks.xo.batch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;

import co.kr.istarbucks.xo.batch.common.dto.msr.CardRegMemberDto;
import co.kr.istarbucks.xo.batch.common.dto.msr.StarListDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.GiftOrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PolicyGiftEventDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

/**
 * @author LeeJiHun
 */
public class EGiftItemRewardStarDao {

    private final static Logger logger = Logger.getLogger("EGIFT_ITEM_REWARD_STAR");

    /**
     * 'e-Gift Item 상시혜택' 이벤트 정보 조회
     * @return 'e-Gift Item 상시혜택' 이벤트 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PolicyGiftEventDto> findPolicyGiftEventByEventTypeIsB() throws Exception {
        
    	String sqlID = "xoGift.findPolicyGiftEventByEventTypeIsB";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	
		return (List<PolicyGiftEventDto>) sqlMap.queryForList(sqlID);
    }

    /**
     * 'e-Gift Item 상시혜택' 이벤트의 대상이 될 '선물 주문 정보(xo_gift_order#)' 목록 조회.
     *
     * @param event 'e-Gift Item 상시혜택' 이벤트 정보
     * @return 'e-Gift Item 상시혜택' 이벤트의 대상인 '선물 주문 정보(xo_gift_order#)' 목록
     * @throws Exception
     */
    public Queue<GiftOrderDto> findAllIrrevocableGiftOrder(String date, PolicyGiftEventDto event) throws Exception {
        logger.info(String.format("Enter with argument [%s]", event));
        String sqlID = "xoGift.findAllIrrevocableGiftOrder";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();

        final Queue<GiftOrderDto> orders = new LinkedList<GiftOrderDto>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        params.put("event", event);

        sqlMap.queryWithRowHandler(sqlID, params, new RowHandler() {
            @Override
            public void handleRow(Object o) {
                orders.add((GiftOrderDto)o);
            }
        });
        logger.info(String.format("Exit with results [order count: %d]", orders.size()));
        return orders;
    }
    
    /**
     * 'e-Gift Shop 전용 특정상품' 기준 별 적립 이벤트이거나, 'e-Gift Shop 전용 특정상품 및 사이렌오더(상품)' 기준 별 적립 이벤트 경우
     * '선물 발행 원장(XO_GIFT_ISSUE#)'에서 'e-Gift Item 상시혜택' 이벤트의 대상이 될 사용자정보 조회.
     *
     * @param event 'e-Gift Item 상시혜택' 이벤트 정보
     * @return 'e-Gift Item 상시혜택' 이벤트의 대상인 '선물 발행 원장(XO_GIFT_ISSUE#)' 목록
     * @throws Exception
     */
    public Queue<GiftOrderDto> findAllIrrevocableGiftOrderV2(String date, PolicyGiftEventDto event) throws Exception {
        logger.info(String.format("Enter with argument [%s]", event));
        String sqlID = "xoGift.findAllIrrevocableGiftOrderV2";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();

        final Queue<GiftOrderDto> orders = new LinkedList<GiftOrderDto>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        params.put("event", event);

        sqlMap.queryWithRowHandler(sqlID, params, new RowHandler() {
            @Override
            public void handleRow(Object o) {
                orders.add((GiftOrderDto)o);
            }
        });
        logger.info(String.format("Exit with results [order count: %d]", orders.size()));
        return orders;
    }
    
    /**
     * 'e-Gift Shop 전용 특정상품' 기준 별 적립 이벤트이거나, 'e-Gift Shop 전용 특정상품 및 사이렌오더(상품)' 기준 별 적립 이벤트 경우
     * '선물 발행 원장(XO_GIFT_ISSUE#)'에서 'e-Gift Item 상시혜택' 이벤트의 대상이 될 사용자정보 조회.
     *
     * @param event 'e-Gift Item 상시혜택' 이벤트 정보
     * @return 'e-Gift Item 상시혜택' 이벤트의 대상인 '선물 발행 원장(XO_GIFT_ISSUE#)' 목록
     * @throws Exception
     */
    public Queue<GiftOrderDto> findAllIrrevocableGiftOrderV3(String date, PolicyGiftEventDto event) throws Exception {
        logger.info(String.format("Enter with argument [%s]", event));
        String sqlID = "xoGift.findAllIrrevocableGiftOrderV3";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();

        final Queue<GiftOrderDto> orders = new LinkedList<GiftOrderDto>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        params.put("event", event);

        sqlMap.queryWithRowHandler(sqlID, params, new RowHandler() {
            @Override
            public void handleRow(Object o) {
                orders.add((GiftOrderDto)o);
            }
        });
        logger.info(String.format("Exit with results [order count: %d]", orders.size()));
        return orders;
    }
    
    /**
     * 'e-Gift Item 상시혜택' 이벤트의 대상이 될 '선물 주문 정보(xo_gift_order#)' 목록 조회.
     *
     * @param event 'e-Gift Item 상시혜택' 이벤트 정보
     * @return 'e-Gift Item 상시혜택' 이벤트의 대상인 '선물 주문 정보(xo_gift_order#)' 목록
     * @throws Exception
     */
    public Queue<GiftOrderDto> findAllIrrevocableGiftOrderV4(String date, PolicyGiftEventDto event) throws Exception {
        logger.info(String.format("Enter with argument [%s]", event));
        String sqlID = "xoGift.findAllIrrevocableGiftOrderV4";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();

        final Queue<GiftOrderDto> orders = new LinkedList<GiftOrderDto>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        params.put("event", event);

        sqlMap.queryWithRowHandler(sqlID, params, new RowHandler() {
            @Override
            public void handleRow(Object o) {
                orders.add((GiftOrderDto)o);
            }
        });
        logger.info(String.format("Exit with results [order count: %d]", orders.size()));
        return orders;
    }    

    /**
     * 'e-Gift Item 상시혜택'에 해당하는 선물 주문에 대해 이벤트 별 발행
     *
     * @param event 'e-Gift Item 상시혜택' 이벤트 정보
     * @param orders 선물 주문 목록
     * @throws Exception
     */
    public void irrevocableGiftOrderToStarList(final PolicyGiftEventDto event,
                                               Queue<GiftOrderDto> orders) throws Exception {

        logger.info(String.format(
                "Enter with arguments [%s, order count: %d]",
                event, orders.size()));

        final int BATCH_SIZE = 100;
        List<GiftOrderDto> insertedOrders = new ArrayList<GiftOrderDto>();
        List<StarListDto> promiseStars = new ArrayList<StarListDto>();
        String evnetGb = event.getEvent_gb();		// 이벤트 구분

        for (GiftOrderDto aOrder : orders) {
        	
        	// '카드 등록 사용자(msr_card_reg_member#)' 조회
            CardRegMemberDto member = findCardRegMember(aOrder.getUser_id());
            
            if (member == null) {
                logger.error(String.format("user_id [%s] does not have user_number. gift_order_no:[%s]",
                        aOrder.getUser_id(), aOrder.getGift_order_no()));
                continue;
            }

            StarListDto star = new StarListDto();
            star.setUser_number(member.getUser_number());		// 회원번호
            star.setEvent_no(event.getEvent_no());						// 이벤트 번호
            star.setStar_count(event.getAdd_star_cnt());				// 이벤트 리워드 별 적립 개수
            star.setGift_order_no(aOrder.getGift_order_no());		// 선물 구매 번호
            star.setGift_issue_cnt(aOrder.getGift_issue_cnt());		// 선물 구매 번호

            insertedOrders.add(aOrder);
            promiseStars.add(star);
            
            // 100건 씩 분할 처리
            if (promiseStars.size() == BATCH_SIZE) {
            	// 이벤트 별 발행 진행
                provideRevocableDateExpirationStar(promiseStars, insertedOrders, evnetGb);
                promiseStars.clear();
                insertedOrders.clear();
            }
        }

        if (!promiseStars.isEmpty()) {
        	// 이벤트 별 발행 진행
        	provideRevocableDateExpirationStar(promiseStars, insertedOrders, evnetGb);
            promiseStars.clear();
            insertedOrders.clear();
        }
    }

    
    /**
     * 'e-Gift Item 상시혜택'에 해당하는 선물 주문에 대해 이벤트 별 발행
     *
     * @param event		'e-Gift Item 상시혜택' 이벤트 정보
     * @param orders 	선물 주문 목록
     * @param evnetGb	이벤트 구분
     * @throws Exception
     */
    private void provideRevocableDateExpirationStar(List<StarListDto> stars, List<GiftOrderDto> orders, String evnetGb) throws Exception {
        String sqlID = "xoGiftMsr.saveIrrevocableGiftOrderEventStar";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();

        try {            
        	int addStarCnt      = 0;  // 이벤트 리워드 별 적립 개수
        	int provideStarCnt  = 0;  // 이미 적립 된 리워드 별 개수
        	int extraStarCnt    = 0;  // 추가 적립 해야 할 리워드 별 개수
        	String starGrpSeq   = "";
        	
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	// 한 프로세스에 별발행(MSR), 선물 주문 정보 업데이트(XO) 2개를 논리적 트렌젝션으로 묶음
            sqlMap.startTransaction();
            sqlMap.startBatch();
            
            for (StarListDto aStar : stars) {
            	
            	addStarCnt = aStar.getStar_count();	// 이벤트 리워드 별 적립 개수
            			
            	params.clear();
                params.put("event_no",      aStar.getEvent_no());
                params.put("user_number",   aStar.getUser_number());
                params.put("gift_order_no", aStar.getGift_order_no());
                params.put("status",        "1"); // 상태(1:발급, 2:회수)
                
                // 회원 Gift Order 별 이미 적립 된 이벤트 리워드 별 개수 조회
                provideStarCnt = getProvideStarCnt(params);
            	
                int gifrIssueCnt = aStar.getGift_issue_cnt();			// 선물 발행 개수
                
                // 실제 적립해야 할 별 개수 ((이벤트 리워드 별 적립 개수 * 선물 발행 개수) - 이미 적립 된 리워드 별 개수)
                extraStarCnt = ((addStarCnt * gifrIssueCnt) - provideStarCnt);
                
                logger.info(String.format(
                        "추가 적립 해야 할 리워드 별 개수 >>> GiftOrderNo: [%-20s] userNumber: [%-10s] extraStarCnt: [%-1s]",
                        aStar.getGift_order_no(),
                        aStar.getUser_number(),
                        extraStarCnt)
                );
               
                /* 추가 적립 해야 할 리워드 별 개수만큼 처리 진행 */
                if(extraStarCnt > 0){
                	
            		for (int i = 1; i <= extraStarCnt; i++) {
            			
            			// 별 이력번호(영업일자+SEQ) 조회
            			aStar.setStar_seq(getStarSeqNo());
            			
            			// 별 이력 그룹번호 설정
            			if (i == 1) {
            				starGrpSeq = aStar.getStar_seq();
            			}            			
            			aStar.setStar_grp_seq(starGrpSeq);
            			aStar.setExtra_star_all_cnt(extraStarCnt);
            			
            			// 리워드 별 지급
            			sqlMap.insert(sqlID, aStar);
            			
            			// 이벤트 참여 이력(XO_GIFT_EVENT_HIST) 테이블에 주문별 이벤트 리워드 별 지급 내역 Insert
            			params.put("star_seq", aStar.getStar_seq());
            			insertGiftEventHist(params);
            		
            			/* 'e-Gift Item' 이벤트 리워드 별 발행이 완료된 주문에 대해 Log 작성 */
                		logger.info(String.format(
                                "GiftOrderNo: [%-20s] userNumber: [%-10s] starSEQ: [%-14s]",
                                aStar.getGift_order_no(),
                                aStar.getUser_number(),
                                aStar.getStar_seq())
                        );
            		}
            	}else {
            		 logger.info("No Event Stars To Add.");
            	}
            }
            
            sqlMap.executeBatch();
            
            /* 이벤트 구분이 기존 이벤트('1') 일 경우에만 선물 구매정보(XO_GIFT_ORDER#) 테이블의 상시 혜택 별 제공여부(BENEFIT_YN) 업데이트 진행 */
            if("1".equals(evnetGb)){
            	updateGiftOrderBenefitYn(orders, stars);
            }
            
            sqlMap.commitTransaction();
        }
        catch (Exception ex) {
            List<String> failedOrders = new ArrayList<String>();
            for (GiftOrderDto aOrder : orders) {
                failedOrders.add(aOrder.getGift_order_no());
            }
            logger.error(String.format("failed orders: %s", failedOrders));
            throw ex;
        }
        finally {
            sqlMap.endTransaction();
        }
    }

	/**
     * '카드 등록 사용자(msr_card_reg_member#)' 조회
     *
     * @param userID 사용자 아이디
     * @return msr_card_reg_member# 사용자 데이터
     * @throws Exception
     */
    private CardRegMemberDto findCardRegMember(String userID) throws Exception {
        String sqlID = "xoGiftMsr.findCardRegMemberByUserIDAndUserStatusIsJ";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
        return (CardRegMemberDto) sqlMap.queryForObject(sqlID, userID);
    }

    /**
     * 'e-Gift Item 상시혜택' 별 발행이 완료된 주문에 대해 'xo_gift_order#.benefit_yn' 업데이트
     *
     * @param orders 별 발행이 완료된 'e-Gift Item' 주문 목록
     * @param insertedStars 발행이 완료된 별 목록
     * @throws Exception
     */
    private void updateGiftOrderBenefitYn(List<GiftOrderDto> orders, List<StarListDto> insertedStars) throws Exception {
        if (orders.isEmpty()) {
            return;
        }

        String sqlID = "xoGift.updateGiftOrderBenefitYnToYContainsGiftOrderNo";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        try {
            sqlMap.startTransaction();
            int rows = sqlMap.update(sqlID, orders);

            if (rows != orders.size()) {
                throw new IllegalStateException("some orders didn't updated.");
            }
            /*
            for (int i = 0 ; i < orders.size() ; i++) {
                GiftOrderDto aOrder = orders.get(i);
                StarListDto aStar = insertedStars.get(i);
                logger.info(String.format(
                        "GiftOrderNo: [%-20s] userID: [%-20s] userNumber: [%-10s] starSEQ: [%-14s]",
                        aOrder.getGift_order_no(),
                        aOrder.getUser_id(),
                        aStar.getUser_number(),
                        aStar.getStar_seq())
                );
            }
 			*/
            sqlMap.commitTransaction();
        }
        catch (Exception ex) {
        	logger.error("exception occurred during update XO_GIFT_ORDER#.benefit_yn");
            throw ex;
        }
        finally {
            sqlMap.endTransaction();
        }
    }
    
    /**
     * 회원 Gift Order 별 이미 적립 된 이벤트 리워드 별 개수 조회
     * @param params
     * @throws Exception
     */
    private int getProvideStarCnt(Map<String, Object> params) throws Exception {
        String sqlID = "xoGift.getProvideStarCnt";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, params);
    }
    
    /**
     * 별 이력번호(영업일자+SEQ) 조회
     * @param 
     * @throws Exception
     */
    private String getStarSeqNo() throws Exception {
        String sqlID = "xoGiftMsr.getStarSeqNo";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
        return (String) sqlMap.queryForObject(sqlID);
    }
    
    /**
     * 이벤트 참여 이력(XO_GIFT_EVENT_HIST) 테이블에 주문별 이벤트 리워드 별 지급 내역 Insert
     * @param params
     * @throws Exception
     */
    private void insertGiftEventHist(Map<String, Object> params) throws Exception {
    	
    	String sqlID = "xoGift.insertGiftEventHist";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        
        try {
        	
        	 sqlMap.startTransaction();
        	 sqlMap.insert(sqlID, params);
             sqlMap.commitTransaction();
        	
        } catch (Exception ex) {
            logger.error("exception occurred during insert XO_GIFT_EVENT_HIST");
            throw ex;
        } finally {
            sqlMap.endTransaction();
        }
    
    }
    
}

