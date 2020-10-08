package co.kr.istarbucks.xo.batch.common.mgift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class MGiftFunction {

	/**
	 * 상품권 prefix가 모바일상품권 또는 카드교환권으로 사용 가능한 값인지 체크
	 * @param prefix 상품권 prefix 2자리
	 * @param type 상품권 타입 - M : 모바일상품권, C : 카드교환권
	 * @return
	 */
	public static boolean checkValidGfcrPrefix(String prefix, String type) {
		boolean isValid = false;
		
		if(StringUtils.equals(MGiftCode.MOBILE_GIFT, type)) {
			// 카드 교환권 prefix에 포함되지 않으면 모바일 상품권으로 판단
			isValid = !ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		} else if(StringUtils.equals(MGiftCode.CARD_EXCHANGE, type)) {
			// 카드 교환권 prefix에 포함된 경우만 카드 교환권으로 사용 가능
			isValid = ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		}
		
		return isValid;
	}

	/**
	 * 상품권 prefix가 망거래 취소가 가능한 값인지 체크
	 * @param prefix 상품권 prefix 2자리
	 * @return
	 */
	public static boolean checkTradeCancel(String prefix) {
		boolean isValid = false;
		
		isValid = ArrayUtils.contains(MGiftCode.CARD_EXCHANGE_PREFIX,prefix);
		
		return isValid;
	}

	/**
	 * 거래 추적용 키 생성 - 최대 20자
	 * @param id 회원아이디 또는 디바이스번호
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
	 * 상품권 prefix로 연동할 제휴사 타입 조회
	 * @param prefix 상품권 prefix 2자리
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
