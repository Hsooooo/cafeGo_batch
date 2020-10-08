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
     * 'e-Gift Item �������' �̺�Ʈ ���� ��ȸ
     * @return 'e-Gift Item �������' �̺�Ʈ ����
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PolicyGiftEventDto> findPolicyGiftEventByEventTypeIsB() throws Exception {
        
    	String sqlID = "xoGift.findPolicyGiftEventByEventTypeIsB";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	
		return (List<PolicyGiftEventDto>) sqlMap.queryForList(sqlID);
    }

    /**
     * 'e-Gift Item �������' �̺�Ʈ�� ����� �� '���� �ֹ� ����(xo_gift_order#)' ��� ��ȸ.
     *
     * @param event 'e-Gift Item �������' �̺�Ʈ ����
     * @return 'e-Gift Item �������' �̺�Ʈ�� ����� '���� �ֹ� ����(xo_gift_order#)' ���
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
     * 'e-Gift Shop ���� Ư����ǰ' ���� �� ���� �̺�Ʈ�̰ų�, 'e-Gift Shop ���� Ư����ǰ �� ���̷�����(��ǰ)' ���� �� ���� �̺�Ʈ ���
     * '���� ���� ����(XO_GIFT_ISSUE#)'���� 'e-Gift Item �������' �̺�Ʈ�� ����� �� ��������� ��ȸ.
     *
     * @param event 'e-Gift Item �������' �̺�Ʈ ����
     * @return 'e-Gift Item �������' �̺�Ʈ�� ����� '���� ���� ����(XO_GIFT_ISSUE#)' ���
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
     * 'e-Gift Shop ���� Ư����ǰ' ���� �� ���� �̺�Ʈ�̰ų�, 'e-Gift Shop ���� Ư����ǰ �� ���̷�����(��ǰ)' ���� �� ���� �̺�Ʈ ���
     * '���� ���� ����(XO_GIFT_ISSUE#)'���� 'e-Gift Item �������' �̺�Ʈ�� ����� �� ��������� ��ȸ.
     *
     * @param event 'e-Gift Item �������' �̺�Ʈ ����
     * @return 'e-Gift Item �������' �̺�Ʈ�� ����� '���� ���� ����(XO_GIFT_ISSUE#)' ���
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
     * 'e-Gift Item �������' �̺�Ʈ�� ����� �� '���� �ֹ� ����(xo_gift_order#)' ��� ��ȸ.
     *
     * @param event 'e-Gift Item �������' �̺�Ʈ ����
     * @return 'e-Gift Item �������' �̺�Ʈ�� ����� '���� �ֹ� ����(xo_gift_order#)' ���
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
     * 'e-Gift Item �������'�� �ش��ϴ� ���� �ֹ��� ���� �̺�Ʈ �� ����
     *
     * @param event 'e-Gift Item �������' �̺�Ʈ ����
     * @param orders ���� �ֹ� ���
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
        String evnetGb = event.getEvent_gb();		// �̺�Ʈ ����

        for (GiftOrderDto aOrder : orders) {
        	
        	// 'ī�� ��� �����(msr_card_reg_member#)' ��ȸ
            CardRegMemberDto member = findCardRegMember(aOrder.getUser_id());
            
            if (member == null) {
                logger.error(String.format("user_id [%s] does not have user_number. gift_order_no:[%s]",
                        aOrder.getUser_id(), aOrder.getGift_order_no()));
                continue;
            }

            StarListDto star = new StarListDto();
            star.setUser_number(member.getUser_number());		// ȸ����ȣ
            star.setEvent_no(event.getEvent_no());						// �̺�Ʈ ��ȣ
            star.setStar_count(event.getAdd_star_cnt());				// �̺�Ʈ ������ �� ���� ����
            star.setGift_order_no(aOrder.getGift_order_no());		// ���� ���� ��ȣ
            star.setGift_issue_cnt(aOrder.getGift_issue_cnt());		// ���� ���� ��ȣ

            insertedOrders.add(aOrder);
            promiseStars.add(star);
            
            // 100�� �� ���� ó��
            if (promiseStars.size() == BATCH_SIZE) {
            	// �̺�Ʈ �� ���� ����
                provideRevocableDateExpirationStar(promiseStars, insertedOrders, evnetGb);
                promiseStars.clear();
                insertedOrders.clear();
            }
        }

        if (!promiseStars.isEmpty()) {
        	// �̺�Ʈ �� ���� ����
        	provideRevocableDateExpirationStar(promiseStars, insertedOrders, evnetGb);
            promiseStars.clear();
            insertedOrders.clear();
        }
    }

    
    /**
     * 'e-Gift Item �������'�� �ش��ϴ� ���� �ֹ��� ���� �̺�Ʈ �� ����
     *
     * @param event		'e-Gift Item �������' �̺�Ʈ ����
     * @param orders 	���� �ֹ� ���
     * @param evnetGb	�̺�Ʈ ����
     * @throws Exception
     */
    private void provideRevocableDateExpirationStar(List<StarListDto> stars, List<GiftOrderDto> orders, String evnetGb) throws Exception {
        String sqlID = "xoGiftMsr.saveIrrevocableGiftOrderEventStar";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();

        try {            
        	int addStarCnt      = 0;  // �̺�Ʈ ������ �� ���� ����
        	int provideStarCnt  = 0;  // �̹� ���� �� ������ �� ����
        	int extraStarCnt    = 0;  // �߰� ���� �ؾ� �� ������ �� ����
        	String starGrpSeq   = "";
        	
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	// �� ���μ����� ������(MSR), ���� �ֹ� ���� ������Ʈ(XO) 2���� ���� Ʈ���������� ����
            sqlMap.startTransaction();
            sqlMap.startBatch();
            
            for (StarListDto aStar : stars) {
            	
            	addStarCnt = aStar.getStar_count();	// �̺�Ʈ ������ �� ���� ����
            			
            	params.clear();
                params.put("event_no",      aStar.getEvent_no());
                params.put("user_number",   aStar.getUser_number());
                params.put("gift_order_no", aStar.getGift_order_no());
                params.put("status",        "1"); // ����(1:�߱�, 2:ȸ��)
                
                // ȸ�� Gift Order �� �̹� ���� �� �̺�Ʈ ������ �� ���� ��ȸ
                provideStarCnt = getProvideStarCnt(params);
            	
                int gifrIssueCnt = aStar.getGift_issue_cnt();			// ���� ���� ����
                
                // ���� �����ؾ� �� �� ���� ((�̺�Ʈ ������ �� ���� ���� * ���� ���� ����) - �̹� ���� �� ������ �� ����)
                extraStarCnt = ((addStarCnt * gifrIssueCnt) - provideStarCnt);
                
                logger.info(String.format(
                        "�߰� ���� �ؾ� �� ������ �� ���� >>> GiftOrderNo: [%-20s] userNumber: [%-10s] extraStarCnt: [%-1s]",
                        aStar.getGift_order_no(),
                        aStar.getUser_number(),
                        extraStarCnt)
                );
               
                /* �߰� ���� �ؾ� �� ������ �� ������ŭ ó�� ���� */
                if(extraStarCnt > 0){
                	
            		for (int i = 1; i <= extraStarCnt; i++) {
            			
            			// �� �̷¹�ȣ(��������+SEQ) ��ȸ
            			aStar.setStar_seq(getStarSeqNo());
            			
            			// �� �̷� �׷��ȣ ����
            			if (i == 1) {
            				starGrpSeq = aStar.getStar_seq();
            			}            			
            			aStar.setStar_grp_seq(starGrpSeq);
            			aStar.setExtra_star_all_cnt(extraStarCnt);
            			
            			// ������ �� ����
            			sqlMap.insert(sqlID, aStar);
            			
            			// �̺�Ʈ ���� �̷�(XO_GIFT_EVENT_HIST) ���̺� �ֹ��� �̺�Ʈ ������ �� ���� ���� Insert
            			params.put("star_seq", aStar.getStar_seq());
            			insertGiftEventHist(params);
            		
            			/* 'e-Gift Item' �̺�Ʈ ������ �� ������ �Ϸ�� �ֹ��� ���� Log �ۼ� */
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
            
            /* �̺�Ʈ ������ ���� �̺�Ʈ('1') �� ��쿡�� ���� ��������(XO_GIFT_ORDER#) ���̺��� ��� ���� �� ��������(BENEFIT_YN) ������Ʈ ���� */
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
     * 'ī�� ��� �����(msr_card_reg_member#)' ��ȸ
     *
     * @param userID ����� ���̵�
     * @return msr_card_reg_member# ����� ������
     * @throws Exception
     */
    private CardRegMemberDto findCardRegMember(String userID) throws Exception {
        String sqlID = "xoGiftMsr.findCardRegMemberByUserIDAndUserStatusIsJ";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
        return (CardRegMemberDto) sqlMap.queryForObject(sqlID, userID);
    }

    /**
     * 'e-Gift Item �������' �� ������ �Ϸ�� �ֹ��� ���� 'xo_gift_order#.benefit_yn' ������Ʈ
     *
     * @param orders �� ������ �Ϸ�� 'e-Gift Item' �ֹ� ���
     * @param insertedStars ������ �Ϸ�� �� ���
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
     * ȸ�� Gift Order �� �̹� ���� �� �̺�Ʈ ������ �� ���� ��ȸ
     * @param params
     * @throws Exception
     */
    private int getProvideStarCnt(Map<String, Object> params) throws Exception {
        String sqlID = "xoGift.getProvideStarCnt";
        SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
        return (Integer) sqlMap.queryForObject(sqlID, params);
    }
    
    /**
     * �� �̷¹�ȣ(��������+SEQ) ��ȸ
     * @param 
     * @throws Exception
     */
    private String getStarSeqNo() throws Exception {
        String sqlID = "xoGiftMsr.getStarSeqNo";
        SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
        return (String) sqlMap.queryForObject(sqlID);
    }
    
    /**
     * �̺�Ʈ ���� �̷�(XO_GIFT_EVENT_HIST) ���̺� �ֹ��� �̺�Ʈ ������ �� ���� ���� Insert
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

