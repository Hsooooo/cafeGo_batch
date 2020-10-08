package co.kr.istarbucks.xo.batch.common.mgift;

public class MGiftCode {
	
	/** ����� ��ǰ�� ���޻� ���� ���� */
	public static final String XO_MGIFT_SUCCESS = "00";
	
	/** ����� ��ǰ�� ���޻� ���� ��� ���� */
	public static final String XO_MGIFT_SERVER_ERROR = "01";
	
	/** ����� ��ǰ�� ���޻� ��ȿ ���� �ʴ� ���� ���� */
	public static final String XO_MGIFT_VALID_ERROR = "10";
	
	/** ����� ��ǰ�� ���޻� ��� �Ϸ�� ���� ���� */
	public static final String XO_MGIFT_USED_ERROR = "11";
	
	/** ����� ��ǰ�� ���޻� ��ȿ�Ⱓ ����� ���� ���� */
	public static final String XO_MGIFT_PERIOD_ERROR = "12";
	
	/** ����� ��ǰ�� ���޻� ��� �ݾ� ���� */
	public static final String XO_MGIFT_PRICE_ERROR = "20";
	
	/** ����� ��ǰ�� ���޻� ����  ��Ÿ ����*/
	public static final String XO_MGIFT_ETC_ERROR = "99";
	
	/** ��ǰ�� ���� �ڵ� - ��ȸ */
	public static final String SEND_INFO = "1";
	
	/** ��ǰ�� ���� �ڵ� - ��� */
	public static final String SEND_USE = "2";
	
	/** ��ǰ�� ���� �ڵ� - ��� ��� */
	public static final String SEND_CANCEL = "3";
	
	/** ��ǰ�� ���� �ڵ� - ���ŷ� ��� */
	public static final String SEND_TRADE_CANCEL = "4";
	
	
	/** �Ӵ��� ���޻� ���� - �Ӵ��� */
	public static final String MONEYCON_PRCM = "MONEYCON";
	
	/** ����Ƽ�� ���޻� ���� - ����Ƽ�� */
	public static final String GIFTISHOW_PRCM = "GIFTISHOW";
	
	/** ���������� ���޻� ���� - ���̳ѹ�, īī�������ϱ�, īī���� */
	public static final String COOP_PRCM = "COOP";
	
	/** ����Ƽ�� ���޻� ���� - ����Ƽ�� */
	public static final String GIFTICON_PRCM = "GIFTICON";
	
	
	/** �Ӵ��� Prefix - ����� ��ǰ�� */
	public static final String MONEYCON_PREFIX_GIFT = "56";
	
	/** �Ӵ��� Prefix - ī�� ��ȯ�� */
	public static final String MONEYCON_PREFIX_CARD = "57";
	
	/** ����Ƽ�� Prefix - ����� ��ǰ�� */
	public static final String GIFTISHOW_PREFIX_GIFT = "90";
	
	/** ����Ƽ�� Prefix - ī�� ��ȯ�� */
	public static final String GIFTISHOW_PREFIX_CARD = "80";
	
	/** īī�������ϱ� Prefix - ����� ��ǰ�� */
	public static final String KAKAOGIFT_PREFIX_GIFT = "96";
	
	/** īī�������ϱ� Prefix - ����� ��ǰ�� */
	public static final String KAKAOGIFT_PREFIX_GIFT_NEW = "95";
	
	/** īī�������ϱ� Prefix - ī�� ��ȯ�� */
	public static final String KAKAOGIFT_PREFIX_CARD = "86";
	
	/** īī���� Prefix - ����� ��ǰ�� */
	public static final String KAKAOTALK_PREFIX_GIFT = "97";
	
	/** īī���� Prefix - ī�� ��ȯ�� */
	public static final String KAKAOTALK_PREFIX_CARD = "87";
	
	/** ���̳ѹ� Prefix - ����� ��ǰ�� */
	public static final String INUMBER_PREFIX_GIFT = "98";
	
	/** ���̳ѹ� Prefix - ī�� ��ȯ�� */
	public static final String INUMBER_PREFIX_CARD = "88";
	
	/** ����Ƽ�� Prefix - ����� ��ǰ�� */
	public static final String GIFTICON_PREFIX_GIFT = "99";
	
	/** ����Ƽ�� Prefix - ī�� ��ȯ�� */
	public static final String GIFTICON_PREFIX_CARD = "89";
	
	
	/** ����� ��ǰ�� ���� - ��ǰ�� ��ǰ��(���̷�����/Ȧ����ũ ���� �� ���) */
	public static final String MOBILE_GIFT = "M";
	
	/** ����� ��ǰ�� ���� - �ݾ��� ��ǰ��(ī�� ��ȯ������ ���) */
	public static final String CARD_EXCHANGE = "C";
	
	
	/** ī�� ��ȯ������ ��� ������ ��ǰ�� Prefix - ���޻� ���� �� üũ�ϱ� ����(���� ��û����) */
	public static final String[] CARD_EXCHANGE_PREFIX = {MONEYCON_PREFIX_CARD, GIFTISHOW_PREFIX_CARD, KAKAOGIFT_PREFIX_CARD, KAKAOTALK_PREFIX_CARD, INUMBER_PREFIX_CARD, GIFTICON_PREFIX_CARD};
	
	/** ���ŷ� ��� ������ ��ǰ�� Prefix */
	public static final String[] TRADE_CANCEL_PREFIX = {KAKAOGIFT_PREFIX_GIFT, KAKAOGIFT_PREFIX_GIFT_NEW, KAKAOGIFT_PREFIX_CARD, KAKAOTALK_PREFIX_GIFT, KAKAOTALK_PREFIX_CARD, 
			INUMBER_PREFIX_GIFT, INUMBER_PREFIX_CARD, GIFTICON_PREFIX_GIFT, GIFTICON_PREFIX_CARD};
	
}
