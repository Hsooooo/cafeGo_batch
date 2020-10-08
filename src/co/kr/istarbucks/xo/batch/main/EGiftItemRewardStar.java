package co.kr.istarbucks.xo.batch.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.GiftOrderDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PolicyGiftEventDto;
import co.kr.istarbucks.xo.batch.dao.EGiftItemRewardStarDao;

/**
 * "e-Gift Item" 취소기한 만료 별 발행 배치 (2017/11/08 ~)
 * <p>
 * 구매자 혜택제공 (별 1개 추가증정) 시점은 <strong>결제 취소기한 만료 익일 일괄 증정</strong>
 * <ul>
 * <li>구매 영수증 당 혜택 지급</li>
 * <li>수신자 등록 사용 시, 현행 금액당 별 혜택만 제공 (필요 시, 등록 사용 시 혜택지급 추가 이벤트 진행)</li>
 * <li>선물 거절기간은 거절 시, 즉시 취소 전제로 결제 취소기한과 동일하게 운영</li>
 * <li>상시 혜택은 연단위 고지를 통한 장기 이벤트로 운영/관리하여, 상시 종료 가능성 존재함</li>
 * <li>결제취소 기한이 상당기간 축소됨에 따라 결제 전 및 보낸 선물 상세 페이지 고지 필수</li>
 * </ul>
 *
 * @author LeeJiHun
 */
public class EGiftItemRewardStar {
    private static final Logger logger = Logger.getLogger("EGIFT_ITEM_REWARD_STAR");
    private final EGiftItemRewardStarDao dao;

    EGiftItemRewardStar() {
        this.dao = new EGiftItemRewardStarDao();
    }

    void run(String date) {
        try {
            logger.info("\n\n" + "========================================\n" +
                    "EGiftItemRewardStar! date:" + date + "\n" +
                    "========================================\n" );
          
            List<PolicyGiftEventDto> eventInfoList = new ArrayList<PolicyGiftEventDto>();
            
            // 이벤트 정보 조회
            eventInfoList = this.dao.findPolicyGiftEventByEventTypeIsB();
            
            logger.info("[e-Gift Item Reward Star] Event Info List >>> " + eventInfoList.toString());
            
            if (eventInfoList.size() > 0) {
                
            	for(PolicyGiftEventDto eventInfo : eventInfoList){
            		
            		Queue<GiftOrderDto> orders = null;
            		
            		/** 
            		 * 이벤트 구분에 따라 이벤트 대상정보 조회 진행
            		 * 1. 기존 별 적립 이벤트 (결제금액)
            		 * 2. 특정상품 별 적립 이벤트 (Gift Shop)
            		 * 3. 특정상품 + 사이렌오더 별 적립 이벤트 (Gift Shop + Siren Order)
            		 * 4. 답례하기 별 적립 이벤트
            		**/
            		
            		if (StringUtils.isBlank(eventInfo.getEvent_gb())) {
            			eventInfo.setEvent_gb("1");
            		}
            		
            		// 1, 4번 이벤트 일 경우 'e-Gift Item 상시혜택' 이벤트의 대상을 '선물 주문 정보(XO_GIFT_ORDER#)'에서 조회
            		if("1".equals(eventInfo.getEvent_gb())){
                		orders = this.dao.findAllIrrevocableGiftOrder(date, eventInfo);
            		}
            		else if ("4".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV4(date, eventInfo);
            		}
            		// 2, 3번 이벤트 일 경우 'e-Gift Item 상시혜택' 이벤트의 대상을 '선물 발행 원장(XO_GIFT_ISSUE#)'에서 조회
            		else if ("2".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV2(date, eventInfo);
            		}
            		else if ("3".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV3(date, eventInfo);
            		}
            		
            		if (!orders.isEmpty()) {
            			// 'e-Gift Item 상시혜택'에 해당하는 선물 주문에 대해 이벤트 별 발행
            			this.dao.irrevocableGiftOrderToStarList(eventInfo, orders);
            		} else {
            			logger.info("No irrevocable order exist.");
            		}
            	}
            	
            } else {
                logger.info("No Event Exist.");
            }
        }
        catch (Exception e) {
            logger.info("[e-Gift Item Revocable date expiration star] finish with exceptions", e);
        }
        finally {
            logger.info("\n\n" + "========================================\n" +
                    "EGiftItemRewardStar finished!" + "\n" +
                    "========================================\n\n");
        }
    }

    public static void main(String[] args) {
        String date;
        
        if (args != null && args.length > 0) {
            date = args[0];
        }
        else {
        	date = new SimpleDateFormat("yyyyMMdd", Locale.KOREA)
                    .format(DateUtils.addDays(new Date(), -1))
            ;
        }
        
        new EGiftItemRewardStar()
                .run(date)
                ;
    }
}
