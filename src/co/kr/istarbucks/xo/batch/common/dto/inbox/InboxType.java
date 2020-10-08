package co.kr.istarbucks.xo.batch.common.dto.inbox;

@SuppressWarnings("PMD.AvoidConstantsInterface")
public interface InboxType {

	/**
	 * 구매관련
	 * @author sinjw
	 *
	 */
	public interface PURCHASE {
		
		/**
		 *  스타벅스 매장 방문 및 구매 이력
		 */
//		public Object[] VISIT = {"A-1", false, false };
		
		/**
		 * 일반 카드 등록 완료
		 */
//		public Object[] CARD_REGISTER = {"A-2", false, false };
		
		/**
		 * 카드 자동충전(월정액)
		 */
		public Object[] CARD_AUTOCHARGE_DAY = {"A-3", true, true };

		/**
		 * 카드 자동충전(기준하한)
		 */
		public Object[] CARD_AUTOCHARGE_LOWEST_AMT = {"A-4", true, true };
		
		/**
		 * 스타벅스 카드 환불 완료
		 */
		public Object[] CARD_REFUND = {"A-5", true, true };
		
		/**
		 * eGift 선물
		 */
		public Object[] EGIFT_SEND = {"A-6", true, true };
		
		/**
		 * eGift 취소
		 */
		public Object[] EGIFT_CANCEL = {"A-7", true, true };		
	}
	
	/**
	 * 우수회원
	 * @author sinjw
	 *
	 */
	public interface VIP_MEMBER {
		
		/**
		 * 스페셜 기프트 신청 안내
		 */
		public Object[] GIFT_APPLICATION  = {"B-1", true, true };
		
		/**
		 * 스페셜 기프트 도착
		 */
		public Object[] GIFT_ARRIVAL  = {"B-2", true, true };
	}
	
	/**
	 * 이벤트
	 * @author sinjw
	 *
	 */
	public interface EVENT {
		
		/**
		 * 전사 프로모션 시작 알림
		 */
		public Object[] PROMOTION_START = {"C-1", true, true };
	}
	
	/**
	 * 개인정보 카테고리
	 */
	public interface MEMBER_INFO {
		
		/**
		 * MSR 최초 등록일
		 */
		public Object[] FIRST_CARD_REGISTER = {"E-2", true, true };
	}
	
	/**
	 * 홀케익
	 * @author sinjw
	 *
	 */
	public interface WHOLE_CAKE  {
		
		/**
		 * 홀 케익 주문 확인 
		 */
		public Object[] ORDER_CONFIRM = { "G-1", true, true };

		/**
		 * 홀 케익 도착 예정
		 */
		public Object[] ARRIVAL = { "G-2", true, true };
	}
	
	/**
	 * 골드카드
	 * @author sinjw
	 *
	 */
	public interface GOLD_CARD {
		
		/**
		 * 골드카드 등록 완료
		 */
		public Object[] REGISTER = {"H-1", false, false };
		
		/**
		 * 골드카드 도착
		 */
		public Object[] ARRIVAL = {"H-2", true, true };
	}
	
	/**
	 * 레벨 변경 알림
	 * @author sinjw
	 *
	 */
	public interface LEVEL {
		/**
		 * 웰컴->그린<br>
		 * 승급
		 */
		public Object[] WelcomeToGreen_MODIFY = {"J-1", true, true };
		/**
		 * 웰컴->골드<br>
		 * 그린->골드<br>
		 * 승급
		 */
		public Object[] WGToGold_MODIFY = {"J-2", true, true };
		/**
		 * 골드->그린<br>
		 * 하향
		 */
		public Object[] GoldToGreen_MODIFY = {"J-3", true, true };
		/**
		 * 골드->골드<br>
		 * 연장
		 */
		public Object[] GoldToGold_MODIFY = {"J-4", true, true };
	}
	
	/**
	 * 쿠폰 
	 * @author sinjw
	 *
	 */
	public interface COUPON {
		
		/**
		 * e-쿠폰 만기 안내 
		 */
		public Object[] EXPIRE = {"K-1", true, true };
	}

	/**
	 * 물품형 GIFT
	 * @author sinjw
	 *
	 */
	public interface E_GIFT_ITEM {
		
		/**
		 * 수신자 유효기간만료
		 */
		public Object[] RECEIVE_NOTICE = {"L-20", true, true };
		
		/**
		 * 구매자 유효기간만료
		 */
		public Object[] SEND_NOTICE = {"L-30", true, true };
		
		/**
		 * 선물 예약전송(전송)
		 */
		public Object[] RESERVE_NOTICE = {"L-6", true, true };
	}
}
