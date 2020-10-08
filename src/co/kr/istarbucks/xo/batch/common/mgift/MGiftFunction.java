package co.kr.istarbucks.xo.batch.common.mgift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class MGiftFunction {

	/**
	 * ��ǰ�� prefix�� ����ϻ�ǰ�� �Ǵ� ī�屳ȯ������ ��� ������ ������ üũ
	 * @param prefix ��ǰ�� prefix 2�ڸ�
	 * @param type ��ǰ�� Ÿ�� - M : ����ϻ�ǰ��, C : ī�屳ȯ��
	 * @return
	 */
	public static boolean checkValidGfcrPrefix(String prefix, String type) {
		boolean isValid = false;
		
		if(StringUtils.equals(MGiftCode.MOBILE_GIFT, type)) {
			// ī�� ��ȯ�� prefix�� ���Ե��� ������ ����� ��ǰ������ �Ǵ�
			isValid = !ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		} else if(StringUtils.equals(MGiftCode.CARD_EXCHANGE, type)) {
			// ī�� ��ȯ�� prefix�� ���Ե� ��츸 ī�� ��ȯ������ ��� ����
			isValid = ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		}
		
		return isValid;
	}

	/**
	 * ��ǰ�� prefix�� ���ŷ� ��Ұ� ������ ������ üũ
	 * @param prefix ��ǰ�� prefix 2�ڸ�
	 * @return
	 */
	public static boolean checkTradeCancel(String prefix) {
		boolean isValid = false;
		
		isValid = ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		
		return isValid;
	}

	/**
	 * �ŷ� ������ Ű ���� - �ִ� 20��
	 * @param id ȸ�����̵� �Ǵ� ����̽���ȣ
	 * @return
	 */
	public static String getTraceKey(String id) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss", Locale.KOREA);
		
		StringBuffer traceKey = new StringBuffer();
		traceKey.append(sdf.format(date)).append(sdf2.format(date));
		
		if(StringUtils.isNotBlank(id)) {
			if(id.length() > 6) {
				traceKey.append(id.substring(0, 6));
			} else {
				traceKey.append(id);
			}
		}
		
		return traceKey.toString();
	}

	/**
	 * ��ǰ�� prefix�� ������ ���޻� Ÿ�� ��ȸ
	 * @param prefix ��ǰ�� prefix 2�ڸ�
	 * @return
	 */
	public static String getPrcmType(String prefix) {
		if(StringUtils.equals(prefix, MGiftCode.GIFTICON_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.GIFTICON_PREFIX_CARD)) {
			return MGiftCode.GIFTICON_PRCM;
		} else if(StringUtils.equals(prefix, MGiftCode.GIFTISHOW_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.GIFTISHOW_PREFIX_CARD)) {
			return MGiftCode.GIFTISHOW_PRCM;
		} else if(StringUtils.equals(prefix, MGiftCode.MONEYCON_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.MONEYCON_PREFIX_CARD)) {
			return MGiftCode.MONEYCON_PRCM;
		} else if(StringUtils.equals(prefix, MGiftCode.INUMBER_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.INUMBER_PREFIX_CARD)) {
			return MGiftCode.COOP_PRCM;
		} else if(StringUtils.equals(prefix, MGiftCode.KAKAOGIFT_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.KAKAOGIFT_PREFIX_CARD) || StringUtils.equals(prefix, MGiftCode.KAKAOGIFT_PREFIX_GIFT_NEW)) {
			return MGiftCode.COOP_PRCM;
		} else if(StringUtils.equals(prefix, MGiftCode.KAKAOTALK_PREFIX_GIFT) || StringUtils.equals(prefix, MGiftCode.KAKAOTALK_PREFIX_CARD)) {
			return MGiftCode.COOP_PRCM;
		}
		
		return "";
	}
}
