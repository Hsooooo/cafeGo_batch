package co.kr.istarbucks.xo.batch.common.mgift;

public class MGiftCode {
	
	/** 모바일 상품권 제휴사 연동 성공 */
	public static final String XO_MGIFT_SUCCESS = "00";
	
	/** 모바일 상품권 제휴사 서버 통신 실패 */
	public static final String XO_MGIFT_SERVER_ERROR = "01";
	
	/** 모바일 상품권 제휴사 유효 하지 않는 쿠폰 오류 */
	public static final String XO_MGIFT_VALID_ERROR = "10";
	
	/** 모바일 상품권 제휴사 사용 완료된 쿠폰 오류 */
	public static final String XO_MGIFT_USED_ERROR = "11";
	
	/** 모바일 상품권 제휴사 유효기간 만료된 쿠폰 오류 */
	public static final String XO_MGIFT_PERIOD_ERROR = "12";
	
	/** 모바일 상품권 제휴사 사용 금액 오류 */
	public static final String XO_MGIFT_PRICE_ERROR = "20";
	
	/** 모바일 상품권 제휴사 연동  기타 오류*/
	public static final String XO_MGIFT_ETC_ERROR = "99";
	
	/** 상품권 구분 코드 - 조회 */
	public static final String SEND_INFO = "1";
	
	/** 상품권 구분 코드 - 사용 */
	public static final String SEND_USE = "2";
	
	/** 상품권 구분 코드 - 사용 취소 */
	public static final String SEND_CANCEL = "3";
	
	/** 상품권 구분 코드 - 망거래 취소 */
	public static final String SEND_TRADE_CANCEL = "4";
	
	
	/** 머니콘 제휴사 연동 - 머니콘 */
	public static final String MONEYCON_PRCM = "MONEYCON";
	
	/** 기프티쇼 제휴사 연동 - 기프티쇼 */
	public static final String GIFTISHOW_PRCM = "GIFTISHOW";
	
	/** 쿠프마케팅 제휴사 연동 - 아이넘버, 카카오선물하기, 카카오톡 */
	public static final String COOP_PRCM = "COOP";
	
	/** 기프티콘 제휴사 연동 - 기프티콘 */
	public static final String GIFTICON_PRCM = "GIFTICON";
	
	
	/** 머니콘 Prefix - 모바일 상품권 */
	public static final String MONEYCON_PREFIX_GIFT = "56";
	
	/** 머니콘 Prefix - 카드 교환권 */
	public static final String MONEYCON_PREFIX_CARD = "57";
	
	/** 기프티쇼 Prefix - 모바일 상품권 */
	public static final String GIFTISHOW_PREFIX_GIFT = "90";
	
	/** 기프티쇼 Prefix - 카드 교환권 */
	public static final String GIFTISHOW_PREFIX_CARD = "80";
	
	/** 카카오선물하기 Prefix - 모바일 상품권 */
	public static final String KAKAOGIFT_PREFIX_GIFT = "96";
	
	/** 카카오선물하기 Prefix - 모바일 상품권 */
	public static final String KAKAOGIFT_PREFIX_GIFT_NEW = "95";
	
	/** 카카오선물하기 Prefix - 카드 교환권 */
	public static final String KAKAOGIFT_PREFIX_CARD = "86";
	
	/** 카카오톡 Prefix - 모바일 상품권 */
	public static final String KAKAOTALK_PREFIX_GIFT = "97";
	
	/** 카카오톡 Prefix - 카드 교환권 */
	public static final String KAKAOTALK_PREFIX_CARD = "87";
	
	/** 아이넘버 Prefix - 모바일 상품권 */
	public static final String INUMBER_PREFIX_GIFT = "98";
	
	/** 아이넘버 Prefix - 카드 교환권 */
	public static final String INUMBER_PREFIX_CARD = "88";
	
	/** 기프티콘 Prefix - 모바일 상품권 */
	public static final String GIFTICON_PREFIX_GIFT = "99";
	
	/** 기프티콘 Prefix - 카드 교환권 */
	public static final String GIFTICON_PREFIX_CARD = "89";
	
	
	/** 모바일 상품권 구분 - 물품형 상품권(사이렌오더/홀케이크 결제 시 사용) */
	public static final String MOBILE_GIFT = "M";
	
	/** 모바일 상품권 구분 - 금액형 상품권(카드 교환권으로 사용) */
	public static final String CARD_EXCHANGE = "C";
	
	
	/** 카드 교환권으로 사용 가능한 상품권 Prefix - 제휴사 연동 전 체크하기 위함(현업 요청사항) */
	public static final String[] CARD_EXCHANGE_PREFIX = {MONEYCON_PREFIX_CARD, GIFTISHOW_PREFIX_CARD, KAKAOGIFT_PREFIX_CARD, KAKAOTALK_PREFIX_CARD, INUMBER_PREFIX_CARD, GIFTICON_PREFIX_CARD};
	
	/** 망거래 취소 가능한 상품권 Prefix */
	public static final String[] TRADE_CANCEL_PREFIX = {KAKAOGIFT_PREFIX_GIFT, KAKAOGIFT_PREFIX_GIFT_NEW, KAKAOGIFT_PREFIX_CARD, KAKAOTALK_PREFIX_GIFT, KAKAOTALK_PREFIX_CARD, 
			INUMBER_PREFIX_GIFT, INUMBER_PREFIX_CARD, GIFTICON_PREFIX_GIFT, GIFTICON_PREFIX_CARD};
	
}
