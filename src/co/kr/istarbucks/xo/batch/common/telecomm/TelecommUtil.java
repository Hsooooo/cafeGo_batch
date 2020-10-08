package co.kr.istarbucks.xo.batch.common.telecomm;

import org.apache.commons.lang.StringUtils;

public class TelecommUtil {

	//생년월일 인증 여부
	public boolean kt_birth = false;
	public boolean lg_birth = false;
	
	public TelecommUtil(String telecommType) {
		
		if(StringUtils.isNotBlank(telecommType)) {
			String telecommTypeArray[] = telecommType.split("\\|");
			for (int i = 0; i < telecommTypeArray.length; i++) {
				String telecommArray[] = telecommTypeArray[i].split(":");
				if("K".equals(telecommArray[0])) {
					if("Y".equals(telecommArray[1])) {
						this.kt_birth = true;
					} else {
						this.kt_birth = false;
					}
				}					
				else if("U".equals(telecommArray[0])) {
					if("Y".equals(telecommArray[1])) {
						this.lg_birth = true;
					} else {
						this.lg_birth = false;
					}
				}
			}
		}
	}

	public boolean isKt_birth() {
		return kt_birth;
	}

	public boolean isLg_birth() {
		return lg_birth;
	}

	public String getDiscountType() {
		if(kt_birth) {
			return "KT2";
		} else {
			return "KT0002";
		}
	}
}
