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
 * "e-Gift Item" ��ұ��� ���� �� ���� ��ġ (2017/11/08 ~)
 * <p>
 * ������ �������� (�� 1�� �߰�����) ������ <strong>���� ��ұ��� ���� ���� �ϰ� ����</strong>
 * <ul>
 * <li>���� ������ �� ���� ����</li>
 * <li>������ ��� ��� ��, ���� �ݾ״� �� ���ø� ���� (�ʿ� ��, ��� ��� �� �������� �߰� �̺�Ʈ ����)</li>
 * <li>���� �����Ⱓ�� ���� ��, ��� ��� ������ ���� ��ұ��Ѱ� �����ϰ� �</li>
 * <li>��� ������ ������ ������ ���� ��� �̺�Ʈ�� �/�����Ͽ�, ��� ���� ���ɼ� ������</li>
 * <li>������� ������ ���Ⱓ ��ҵʿ� ���� ���� �� �� ���� ���� �� ������ ���� �ʼ�</li>
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
            
            // �̺�Ʈ ���� ��ȸ
            eventInfoList = this.dao.findPolicyGiftEventByEventTypeIsB();
            
            logger.info("[e-Gift Item Reward Star] Event Info List >>> " + eventInfoList.toString());
            
            if (eventInfoList.size() > 0) {
                
            	for(PolicyGiftEventDto eventInfo : eventInfoList){
            		
            		Queue<GiftOrderDto> orders = null;
            		
            		/** 
            		 * �̺�Ʈ ���п� ���� �̺�Ʈ ������� ��ȸ ����
            		 * 1. ���� �� ���� �̺�Ʈ (�����ݾ�)
            		 * 2. Ư����ǰ �� ���� �̺�Ʈ (Gift Shop)
            		 * 3. Ư����ǰ + ���̷����� �� ���� �̺�Ʈ (Gift Shop + Siren Order)
            		 * 4. ����ϱ� �� ���� �̺�Ʈ
            		**/
            		
            		if (StringUtils.isBlank(eventInfo.getEvent_gb())) {
            			eventInfo.setEvent_gb("1");
            		}
            		
            		// 1, 4�� �̺�Ʈ �� ��� 'e-Gift Item �������' �̺�Ʈ�� ����� '���� �ֹ� ����(XO_GIFT_ORDER#)'���� ��ȸ
            		if("1".equals(eventInfo.getEvent_gb())){
                		orders = this.dao.findAllIrrevocableGiftOrder(date, eventInfo);
            		}
            		else if ("4".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV4(date, eventInfo);
            		}
            		// 2, 3�� �̺�Ʈ �� ��� 'e-Gift Item �������' �̺�Ʈ�� ����� '���� ���� ����(XO_GIFT_ISSUE#)'���� ��ȸ
            		else if ("2".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV2(date, eventInfo);
            		}
            		else if ("3".equals(eventInfo.getEvent_gb())){
            			orders = this.dao.findAllIrrevocableGiftOrderV3(date, eventInfo);
            		}
            		
            		if (!orders.isEmpty()) {
            			// 'e-Gift Item �������'�� �ش��ϴ� ���� �ֹ��� ���� �̺�Ʈ �� ����
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
