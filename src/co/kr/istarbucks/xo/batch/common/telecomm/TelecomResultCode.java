package co.kr.istarbucks.xo.batch.common.telecomm;

public class TelecomResultCode {

	/** 통신사 결제/취소 성공 */
	public static final String SUCCESS = "0000";
	
	/** 통신사 취소 실패 */
	public static final String FAIL = "99";
	
	public class KT {
		/** 통신사 KT 1일 사용횟수 한도 초과 */
		public static final String ONE_DAY_USE_CNT_OVER_LIMIT = "1024";
		
		/** 미등록 카드 */
		public static final String NON_REG_CARD = "1013";
		
		/** KT통신사 생년월일 인증 실패인경우		 */
		public static final String FAIL_AUTH_BIRTH = "1009";
	}
	
	public class UPLUS {
		/** 통신사 UPLUS 1일 사용횟수 한도 초과 */
		public static final String ONE_DAY_USE_CNT_OVER_LIMIT = "0043";
		
		/** 미등록 카드 */
		public static final String NON_REG_CARD = "0027";
	}	
}
