package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.msr.CouponPublicationDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 비회원(준/웹) 리워드 적립 / 회수 NonMemberRewardDao.
 * @author 
 * @since 2018. 11. 02
 * @version 
 */
public class NonMemberRewardDao {

	private final Logger tLogger = Logger.getLogger ("TRADE");
	private static final String loggerTitle = "[TradeCorrection] ";
	
	public Boolean rewardProcess(Map<String, Object> orderMap, SqlMapClient msrSqlMap)throws Exception {
		
		StringBuffer buf = new StringBuffer ();
		Boolean nonMembRewResult = true;																		// 비회원(준/웹) 회원일 경우 리워드 적립/회수 결과
		
		try {
		
			// 비회원(준/웹)일 경우 리워드 적립 / 회수 진행을 위해 변수 지정
			String nextStatus		=	(String) orderMap.get ("nextStatus");									// 주문 다음 상태(31:제조완료, 22:주문취소)
			String xopOrdNo			=	(String) orderMap.get ("xopOrdNo");										// 주문번호
			String saleDate			=	(String) orderMap.get ("saleDate");										// 영업일
			String storeCd			=	(String) orderMap.get ("storeCd");										// 매장코드
			String posNo			=	(String) orderMap.get ("posNo");										// POS번호
			String seqNo			=	(String) orderMap.get ("seqNo");										// 거래번호
			String userId			= 	(String) orderMap.get ("userId");										// 사용자 아이디
			int orderTotalPayAmt	=	(Integer) orderMap.get ("totalPayAmt");									// 주문 총 결제금액 {총 품목총액 - 총 할인액 - 총 쿠폰액 - 총 쿠폰할인금액}
			
			Map<String, Object> nonMemSelMap		= new HashMap<String, Object> ();							// 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회를 위한 Parameter Map
			Map<String, Object> nonMemInfoMap		= new HashMap<String, Object> ();							// 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 Result Map
			Map<String, Object> nonMemTrdInfoMap	= new HashMap<String, Object> ();							// 비회원(준/웹) 주문 거래 이력(MSR_XO_NON_TRD_HIST) 조회 된 Result Map
			Map<String, Object> withDrawMap			= new HashMap<String, Object> ();							// 비회원(준/웹) 주문 가상 사용자 쿠폰 회수를 위한 Parm 지정
			
			int amount				= 0; 
			int beforAccAmt			= 0;
			int afterAccAmt			= 0;
			int beforAccRewardAmt	= 0;
			int afterAccRewardAmt	= 0;
			
			Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
			int rewardBaseAmt		= conf.getInt("nonMember.reward.base.amt", 100000);							// 비회원(준/웹) 주문 가상 사용자 리워드 기준금액 조회
			
			boolean withDrawCpSucc	= true;																		// 비회원(준/웹) 주문 가상 사용자 쿠폰 회수 결과값
			boolean withDrawRdSucc	= true;																		// 비회원(준/웹) 주문 가상 사용자 리워드 금액 변경 결과값
			
			/** 주문 다음 상태가 '제조완료(31)'일 경우 리워드 적립 **/
			if(StringUtils.equals(nextStatus, "31")){
				
				/* 비회원(준/웹) 주문 거래 사용금액(MSR_XO_NON_TRD_HIST) 조회 */
				amount = getNonMemTrdAmtInfo(xopOrdNo, msrSqlMap);												//	회수금액(비회원(준/웹) 리워드 적립 진행을 위해 조회)
				
				/**
				 * 비회원(준/웹) 리워드 실적이 중복으로 적립되지 않게 하기 위해 싸이렌 오더 주문번호로 조회 된 비회원(준/웹) 주문 거래 사용금액 확인
				**/
				if(amount == 0){
					
					buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 회원 리워드 적립 Start");
					tLogger.info (buf.toString ());
					
					// 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회을 위한 Parm 지정
					nonMemSelMap.put("userId",	userId);														// 사용자 아이디
					nonMemSelMap.put("status",	"I");															// 상태 (I: 정상, D:폐기)
					
					/* 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 */
					nonMemInfoMap = getNonMemberInfo(nonMemSelMap, msrSqlMap);
					
					// 비회원(준/웹) 주문 가상 사용자 정보가 Null 일 경우 Exception 처리
					if(nonMemInfoMap == null){
						buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자정보 존재하지 않습니다.(nonMemSelMap ").append (nonMemSelMap).append (")");
						throw new Exception (buf.toString ());	
					}
					
					// 이전 리워드 금액에 주문 금액 합산하여 변수네 지정 
					beforAccAmt			 = (Integer) nonMemInfoMap.get ("accAmt");									// 주문승인 전 집계기간 누적 사용 금액
					afterAccAmt			 = beforAccAmt + orderTotalPayAmt;											// 집계기간 누적 사용 금액(집계기간 누적 사용 금액 + 주문 총 결제금액)
					beforAccRewardAmt	 = (Integer) nonMemInfoMap.get ("accRewardAmt");							// 주문승인 전 집계기간 리워드 지급 차감된 누적 사용 금액
					afterAccRewardAmt	 = beforAccRewardAmt + orderTotalPayAmt;									// 집계기간 리워드 지급 차(집계기간 리워드 지급 차감된 누적 사용 금액 + 주문 총 결제금액)
					
					// 비회원(준/웹) 리워드 집계 만료 일자 체크위해 Date 포멧지정
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA); 
					Date now = new Date();
					Date today = format.parse(format.format(now));													// 현재시간
					Calendar calDate = new GregorianCalendar(Locale.KOREA);
					calDate.setTime(new Date());
					
					String beforeRewardExpireDate = (String) nonMemInfoMap.get ("rewardExpireDate");				// 주문승인 전 리워드 집계 만료 일자
					String afterRewardExpireDate = "";																// 주문승인 후 리워드 집계 만료 일자
					
					/**
					 * 리워드 집계 만료일자 미 존재 시 다음 년도의 마지막일자를 만료 일자로 지정
					**/
					if(StringUtils.isBlank (beforeRewardExpireDate)){
						
						calDate.add(Calendar.YEAR, 1); // 1년을 더한다.
					    SimpleDateFormat fm = new SimpleDateFormat("yyyy", Locale.KOREA);
					    
						String strYear = fm.format(calDate.getTime());
					    Calendar calendar = Calendar.getInstance();
					    
					    int intYear = Integer.parseInt(strYear);
					    int intMonth = Integer.parseInt(conf.getString("nonMember.reward.month", "12"));
					    
					    calendar.set(intYear, intMonth-1, 1);
					    String strDay = String.valueOf(calendar.getActualMaximum(Calendar.DATE));
					    
					    afterRewardExpireDate = strYear + conf.getString("nonMember.reward.month", "12") + strDay;
					}

					/**
					  * 리워드 집계 만료일자 존재 시 만료 여부 체크하여 만료 일자로 지정
					 **/
					else{
						Date reward_expire_date = format.parse(beforeRewardExpireDate + "235959");					// 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 리워드 집계 만료 일자
						int rewardExpireDateCheck = today.compareTo(reward_expire_date);							// 현재날짜가 리워드 집계 만료 일자 이후날짜이면 1을 반환, 그 반대의 경우 -1을 반환
						
						/**
						 * 리워드 집계 만료일자 만료 시 해당년도의 마지막일자를 만료 일자로 지정하고,
						 * 집계기간 리워드 지급 차감된 누적 사용 금액은 이전 리워드 누적 금액을 제외한 주문금액으로 지정
						**/
						if(rewardExpireDateCheck > 0){
							
							calDate.setTime(new Date());
							SimpleDateFormat fm = new SimpleDateFormat("yyyy", Locale.KOREA);
						    
						    String strYear = fm.format(calDate.getTime());
						    
						    Calendar calendar = Calendar.getInstance();
						    int intYear = Integer.parseInt(strYear);
						    int intMonth = Integer.parseInt(conf.getString("nonMember.reward.month", "12"));
						    
						    calendar.set(intYear, intMonth-1, 1);
						    
						    String strDay = String.valueOf(calendar.getActualMaximum(Calendar.DATE));
						    
						    afterRewardExpireDate 	= strYear + conf.getString("nonMember.reward.month", "12") + strDay;
						    afterAccRewardAmt	 	= orderTotalPayAmt;
						    
						    tLogger.info("리워드 집계 만료일자 만료 >>>>>>>>>>>>>> 리워드 집계 만료일자 / 리워드 지급 차감된 누적 사용 금액 재지정!!!!!!!!!");
						    tLogger.info("[As Is] 리워드 집계 만료일자 :: " + beforeRewardExpireDate + ", 리워드 지급 차감된 누적 사용 금액 :: " + beforAccRewardAmt);
						    tLogger.info("[To Be] 리워드 집계 만료일자 :: " + afterRewardExpireDate + ", 리워드 지급 차감된 누적 사용 금액 :: " + afterAccRewardAmt);
						    
						}else{
							afterRewardExpireDate = beforeRewardExpireDate;
						}
					}
					
					// 비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 이력 생성을 위한 Parm 지정
					withDrawMap.put("di", 					(String) nonMemInfoMap.get ("di"));						// 본인인증 고유값
					withDrawMap.put("userNumber",			(String) nonMemInfoMap.get ("userNumber"));				// 사용자 번호
					withDrawMap.put("userId",				(String) nonMemInfoMap.get ("userId"));					// 사용자 아이디
					withDrawMap.put("trdType", 				"U");													// 거래유형(U:사용)
					withDrawMap.put("amount",				(Integer) orderTotalPayAmt);							// 사용금액
					withDrawMap.put("accAmt",				(Integer) afterAccAmt);									// 집계기간 누적 사용 금액(집계기간 누적 사용 금액 + 주문 총 결제금액)
					withDrawMap.put("accRewardAmt",			(Integer) afterAccRewardAmt);							// 집계기간 리워드 지급 차감된 누적 사용 금액(집계기간 리워드 지급 차감된 누적 사용 금액 + 주문 총 결제금액)
					withDrawMap.put("rewardExpireDate", 	afterRewardExpireDate);									// 리워드 집계 만료 일자
					withDrawMap.put("orderNo", 				xopOrdNo);												// 사이렌오더 주문번호
					withDrawMap.put("businessDate", 		saleDate);												// POS거래 영업일
					withDrawMap.put("branchCode", 			storeCd);												// 매장 코드
					withDrawMap.put("posNumber", 			posNo);													// POS 번호
					withDrawMap.put("posTrdNumber", 		seqNo);													// POS 거래 번호
					withDrawMap.put("rwdTrdYn", 			"Y");													// 리워드 집계 대상 여부(Y)
					
					/** 비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 이력 생성 **/
					withDrawRdSucc = withDrawReward(withDrawMap, loggerTitle, msrSqlMap);
					
					if(!withDrawRdSucc){
						buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 이력 생성 실패(withDrawMap ").append (withDrawMap).append (")");
						throw new Exception (buf.toString ());	
					}
					
					buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 회원 리워드 적립 The End");
					tLogger.info (buf.toString ());
					
				}else{
					buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 주문 가상 사용자 주문 거래 이력(MSR_XO_NON_TRD_HIST) 존재하지 않아 리워드 적립하지 않음(xopOrdNo : ").append (xopOrdNo).append (")");
					tLogger.info (buf.toString ());
				}
			}
			
			/** 주문 다음 상태가 '주문취소(22)'일 경우 리워드 회수 **/
			else if(StringUtils.equals(nextStatus, "22")){
				
				/* 비회원(준/웹) 주문 거래 사용금액(MSR_XO_NON_TRD_HIST) 조회 */
				amount = getNonMemTrdAmtInfo(xopOrdNo, msrSqlMap);														//	회수금액(비회원(준/웹) 리워드 회수 진행을 위해 조회)
				
				if(amount > 0){
					
					if(amount == orderTotalPayAmt){
						
						/* 비회원(준/웹) 주문 가상 사용자 주문 거래 이력(MSR_XO_NON_TRD_HIST) 조회 */
						nonMemTrdInfoMap = getNonMemTrdHist(xopOrdNo, msrSqlMap);
						
						/** 
						 * 비회원(준/웹) 주문 가상 사용자 주문 거래 이력이 존재 할 경우 리원드 실적 회수 진행
						**/
						if(nonMemTrdInfoMap != null){
							buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 회원 리워드 회수 Start");
							tLogger.info (buf.toString ());
							
							// 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회을 위한 Parm 지정
							nonMemSelMap.put("di",			(String) nonMemTrdInfoMap.get("di"));						// 본인인증 고유값 DI
							nonMemSelMap.put("userNumber",	(String) nonMemTrdInfoMap.get("userNumber"));				// 사용자 번호
							
							/* 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 */
							nonMemInfoMap = getNonMemberInfo(nonMemSelMap, msrSqlMap);
							
							// 이관구분(MSR:웹회원 → MSR회원, WEB:준회원 → 웹회원, NOT:이관X)에 따라서 리워드 회수 진행
							String transferGb = (String) nonMemInfoMap.get("transferGb");
							Map<String, Object> webMemInfoMap = null;													// 웹회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 Result Map
							int accAmt = 0;
							int accRewardAmt = 0;
							String rewardExpireDate = "";
							String msrMembGb = "NOT";																	// 웹회원 MSR 회원 등록 구분
							String webMembNo = "";																		// 웹회원 일때의 사용자 아이디(MSR회원 등록 일 경우 리워드 금액 수정을 위해 지정)
							String transferUserNumber = (String) nonMemInfoMap.get ("transferUserNumber");				// 리워드 이관 사용자 번호에 비회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 이관 사용자 번호 지정
							
							/** 
							 * 이관구분이 'WEB(준회원 → 웹회원)' 경우 이관 된 웹회원의 집계기간 누적 사용 금액, 집계기간 리워드 지급 차감된 누적 사용 금액, 리워드 집계 만료 일자 조회
							 * 이관구분에 따라 집계기간 누적 사용 금액, 집계기간 리워드 지급 차감된 누적 사용 금액, 리워드 집계 만료 일자 지정
							**/
							if(StringUtils.equals(transferGb, "WEB")){
								String webUserNumber = (String) nonMemInfoMap.get ("transferUserNumber");				// 이관 된 웹회원의 사용자 번호
								
								/* 웹회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 */
								webMemInfoMap = getWebMemberInfo(webUserNumber, msrSqlMap);
								
								accAmt			 = (Integer) webMemInfoMap.get ("accAmt");								// 이관 된 웹회원의 집계기간 누적 사용 금액
								accRewardAmt	 = (Integer) webMemInfoMap.get ("accRewardAmt");						// 이관 된 웹회원의 집계기간 리워드 지급 차감된 누적 사용 금액
								rewardExpireDate = (String) webMemInfoMap.get ("rewardExpireDate");						// 이관 된 웹회원의 리워드 집계 만료 일자
								
							}else{
								
								accAmt			 = (Integer) nonMemInfoMap.get ("accAmt");								// 비회원(준/웹)의 집계기간 누적 사용 금액
								accRewardAmt	 = (Integer) nonMemInfoMap.get ("accRewardAmt");						// 비회원(준/웹)의 집계기간 리워드 지급 차감된 누적 사용 금액
								rewardExpireDate = (String) nonMemInfoMap.get ("rewardExpireDate");						// 비회원(준/웹)의 리워드 집계 만료 일자
								
							}
							
							/**
							 * 웹회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 Result Map이 존재 할 경우
							 * 웹회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회 된 이관 사용자 번호 지정
							**/
			    			if(webMemInfoMap != null){
			    				msrMembGb = (String) webMemInfoMap.get ("transferGb");									// 웹회원 MSR 회원 등록 구분(MSR회원 등록 일 경우 리워드 금액 수정을 위해 지정)
			    				webMembNo = (String) webMemInfoMap.get ("userNumber");									// 웹회원 일때의 사용자 아이디(MSR회원 등록 일 경우 리워드 금액 수정을 위해 지정)
			    				transferUserNumber = (String) webMemInfoMap.get ("transferUserNumber");					// 준회원에서 이관 된 웹회원이 MSR회원 등록 시
			    			}
							
							// 이전 리워드 금액에 주문 금액 빼기하여 변수 지정 
							beforAccAmt				 = accAmt;															// 주문승인 전 집계기간 누적 사용 금액
							
							/**
							 * 차감 금액(주문 총 결제금액)과 리워드 금액(집계기간 리워드 지급 차감된 누적 사용 금액)비교하여
							 * 집계기간 리워드 지급 차감된 누적 사용 금액을 지정한다.
							**/
							if(orderTotalPayAmt > accRewardAmt){						
								beforAccRewardAmt	 = rewardBaseAmt + accRewardAmt;	
							}else{
								beforAccRewardAmt	 = accRewardAmt;
							}
							
							afterAccAmt			 	= beforAccAmt - orderTotalPayAmt;									// 집계기간 누적 사용 금액(집계기간 누적 사용 금액 - 주문 총 결제금액)
							afterAccRewardAmt		= beforAccRewardAmt - orderTotalPayAmt;								// 집계기간 리워드 지급 차(집계기간 리워드 지급 차감된 누적 사용 금액 - 주문 총 결제금액)
							
							//  비회원 주문 가상 사용자 쿠폰 회수를 위한 Parm 지정
							withDrawMap.put("di",					(String) nonMemInfoMap.get ("di"));					// 본인인증 고유값 DI
							withDrawMap.put("userNumber",			(String) nonMemInfoMap.get ("userNumber"));			// 사용자 번호
							withDrawMap.put("userId",				(String) nonMemInfoMap.get ("userId"));				// 사용자 아이디
							withDrawMap.put("trdType", 				"X");												// 거래유형(X:사용취소)
							withDrawMap.put("transferUserNumber",	transferUserNumber);								// 이관 사용자 번호
							withDrawMap.put("rewardExpireDate",		rewardExpireDate);									// 리워드 집계 만료 일자
							withDrawMap.put("orderNo", 				xopOrdNo);											// 사이렌오더 주문번호
							withDrawMap.put("amount", 				orderTotalPayAmt);									// 사용금액
							withDrawMap.put("rewardBaseAmt", 		rewardBaseAmt);										// 비회원 주문 가상 사용자 리워드 기준금액
							withDrawMap.put("accAmt", 				afterAccAmt);										// 집계기간 누적 사용 금액(집계기간 누적 사용 금액 - 주문 총 결제금액)
							withDrawMap.put("accRewardAmt", 		afterAccRewardAmt);									// 집계기간 리워드 지급 차감된 누적 사용 금액(집계기간 리워드 지급 차감된 누적 사용 금액 - 주문 총 결제금액)
							withDrawMap.put("businessDate", 		(String) nonMemTrdInfoMap.get ("businessDate"));	// 원거래 POS거래 영업일
							withDrawMap.put("branchCode", 			(String) nonMemTrdInfoMap.get ("branchCode"));		// 원거래 매장 코드
							withDrawMap.put("posNumber", 			(String) nonMemTrdInfoMap.get ("posNumber"));		// 원거래 POS 번호
							withDrawMap.put("posTrdNumber", 		(String) nonMemTrdInfoMap.get ("posTrdNumber"));	// 원거래 POS 거래 번호
							withDrawMap.put("transferGb", 			transferGb);										// 이관구분(MSR:웹회원 → MSR회원, WEB:준회원 → 웹회원, NOT:이관X)
							withDrawMap.put("rwdTrdYn", 			"Y");												// 리워드 집계 대상 여부(Y)
							withDrawMap.put("msrMembGb", 			msrMembGb);											// 웹회원 MSR 회원 등록 구분
							withDrawMap.put("webMembNo", 			webMembNo);											// 웹회원 일때의 사용자 아이디(MSR회원 등록 일 경우 리워드 금액 수정을 위해 지정)
							
							withDrawMap.put("trdBusinessDate", 		saleDate);											// 영업일자(거래마스터 및 거래쿠폰내역 생성위해 지정)
							withDrawMap.put("trdBranchCode", 		storeCd);											// 매장코드(거래마스터 및 거래쿠폰내역 생성위해 지정)
							withDrawMap.put("trdPosNumber", 		posNo);												// POS번호(거래마스터 및 거래쿠폰내역 생성위해 지정)
							withDrawMap.put("trdPosTrdNumber", 		seqNo);												// POS거래번호(거래마스터 및 거래쿠폰내역 생성위해 지정)
							
							/**
							 * 차감 금액(주문 총 결제금액) >  리워드 금액(집계기간 리워드 지급 차감된 누적 사용 금액) 일 경우 쿠폰 회수 진행
							**/
							if(orderTotalPayAmt > accRewardAmt){
								/** 비회원(준/웹) 주문 가상 사용자 쿠폰 회수 진행 **/
								withDrawCpSucc = withDrawCoupon(withDrawMap, loggerTitle, msrSqlMap);
							}
							
							if(!withDrawCpSucc){
								buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 발급 된 쿠폰 회수 실패(withDrawMap ").append (withDrawMap).append (")");
								throw new Exception (buf.toString ());
							}
							
							/** 비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 이력 생성 **/
							withDrawRdSucc = withDrawReward(withDrawMap, loggerTitle, msrSqlMap);
							
							if(!withDrawRdSucc){
								buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 취소 이력 생성 실패(withDrawMap ").append (withDrawMap).append (")");
								throw new Exception (buf.toString ());
							}
							
							buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 회원 리워드 회수 The End");
							tLogger.info (buf.toString ());
							
						}else{
							buf.delete (0, buf.length ()).append (loggerTitle).append ("비회원(준/웹) 주문 가상 사용자 주문 거래 이력(MSR_XO_NON_TRD_HIST) 존재하지 않아 리워드 회수하지 않음(xopOrdNo : ").append (xopOrdNo).append (")");
							tLogger.info (buf.toString ());
						}
						
					}else{
						buf.delete (0, buf.length ()).append ("거래 취소 금액 불일치(취소요청 금액 : ").append (amount).append (", 회수금액 : ").append (orderTotalPayAmt).append (")");
						throw new Exception (buf.toString ());
					}
				}
			}
		
		} catch ( Exception e ) {
			nonMembRewResult = false;
			buf.delete (0, buf.length ()).append (loggerTitle).append (e.getMessage ());
			tLogger.error (buf.toString (), e);
		}
		
		return nonMembRewResult;
	}
	
	
	/**
	 * 비회원(준/웹) 주문 가상 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 및 주문 거래 이력 생성
	 * @param withDrawReward
	 * @param lotTitle
	 * @param msrSqlMap
	 * @return
	 * @throws Exception 
	 */
	private boolean withDrawReward(Map<String, Object> withDrawMap, String logTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		boolean result = true;
		
		String transferGb = StringUtils.defaultIfEmpty((String) withDrawMap.get("transferGb"), "");		// 이관구분(MSR:웹회원 → MSR회원, WEB:준회원 → 웹회원, NOT:이관X)
		
		String msrMembGb = StringUtils.defaultIfEmpty((String) withDrawMap.get("msrMembGb"), "");		// 웹회원 MSR 회원 등록 구분
		String webMembNo = StringUtils.defaultIfEmpty((String) withDrawMap.get("webMembNo"), "");		// 웹회원 일때의 사용자 아이디
		
		/* 비회원(준/웹) 주문 가상 사용자 주문 거래 이력(MSR_XO_NON_TRD_HIST) 생성 */
		int insertTrdHistCnt = insertNonMemberTrdHist(withDrawMap, logTitle, msrSqlMap);
		if ( insertTrdHistCnt < 1 ) {
			buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 주문 거래 이력(MSR_XO_NON_TRD_HIST) 생성 실패(nonMemTrdInsMap :: ").append (withDrawMap).append (")");	
			tLogger.info(buf.toString());
			result = false;
		}
		
		/** 
		 * 이관구분이 'WEB:준회원 → 웹회원' 일 경우 이관 된 사용자의 사용자 누적 사용 금액과
		 * 리워드 대상 누적 금액 변경을 위해 사용자번호에 이관 사용자 번호를 지정(주문 취소시 사용을 위해) 
		**/
		if(StringUtils.equals(transferGb, "WEB")){
			
			/** 
			 * MSR 회원으로 등록 된 사용자인 경우 변경 될 사용자 누적 사용 금액 리워드 대상 누적 금액의 사용자 번호는 웹회원일 때의 번호로 지정 
			**/
			if(StringUtils.equals(msrMembGb, "MSR")){
				withDrawMap.put("userNumber", webMembNo);	
			}else{
				withDrawMap.put("userNumber", withDrawMap.get("transferUserNumber"));	
			}
		}
		
		/* 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER)에 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 */
		int updateCnt = updateNonMemberInfo(withDrawMap, logTitle, msrSqlMap);
		if ( updateCnt < 1 ) {
			buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER)에 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 실패(nonMemUpdMap :: ").append (withDrawMap).append (")");	
			tLogger.info(buf.toString());
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * 비회원(준/웹) 주문 가상 사용자 쿠폰 회수 진행
	 * @param withDrawMap
	 * @param lotTitle
	 * @param msrSqlMap
	 * @return
	 * @throws Exception 
	 */
	private boolean withDrawCoupon(Map<String, Object> withDrawMap, String logTitle, SqlMapClient msrSqlMap) throws Exception {
		
		StringBuffer buf = new StringBuffer ();
		boolean result = true;
		
		int possessionCouponCnt = 0 ;																			// 비회원(준/웹) 주문 가상 사용자 보유쿠폰(상태:'A')개수
		int issueCouponCnt = 0;																					// 비회원(준/웹) 주문 가상 사용자 회수대상 쿠폰개수
		
		Map<String, Object> nonMemTrdInsMap		= new HashMap<String, Object> ();								// 비회원(준/웹) 주문 거래 이력(MSR_XO_NON_TRD_HIST) 저장 저장을 위한 Parameter Map
		
		/* 비회원(준/웹) 주문 가상 사용자 회수대상 쿠폰개수 계산 */
		int rewardBaseAmt = (Integer) withDrawMap.get ("rewardBaseAmt");											// 취소금액(사용금액)
		int amount = (Integer) withDrawMap.get ("amount");															// 비회원 주문 가상 사용자 리워드 기준금액
		
		if(rewardBaseAmt > amount){
			amount = rewardBaseAmt + amount;
		}
		
		/** 
		 * 취소금액(사용금액)에 비회원 주문 가상 사용자 리워드 기준금액을 나눠 비회원(준/웹) 주문 가상 사용자 회수대상 쿠폰개수 계산
		**/
		issueCouponCnt = amount/ rewardBaseAmt;																		// 비회원(준/웹) 주문 가상 사용자 회수대상 쿠폰개수
		
		
		// 비회원(준/웹) 주문 가상 사용자 회수대상 쿠폰이 존재 할 경우 회수 진행
		if(issueCouponCnt > 0){
			
			Map<String, Object> couponSelMap		= new HashMap<String, Object> ();								// 쿠폰 정보(MSR_COUPON_PUBLICATION_LIST) 조회를 위한 Parameter Map
			couponSelMap.put("di",					(String) withDrawMap.get ("di"));								// 본인인증 고유값 DI
			couponSelMap.put("userNumber",			(String) withDrawMap.get ("userNumber"));						// 사용자 번호
			couponSelMap.put("transferUserNumber",	(String) withDrawMap.get ("transferUserNumber"));				// 이관 사용자 번호
			couponSelMap.put("orderNo", 			(String) withDrawMap.get ("orderNo"));							// 사이렌오더 주문번호
			couponSelMap.put("businessDate", 		(String) withDrawMap.get ("businessDate"));						// POS거래 영업일
			couponSelMap.put("branchCode", 			(String) withDrawMap.get ("branchCode"));						// 매장 코드
			couponSelMap.put("posNumber", 			(String) withDrawMap.get ("posNumber"));						// POS 번호
			couponSelMap.put("posTrdNumber", 		(String) withDrawMap.get ("posTrdNumber"));						// POS 거래 번호
			
			
			/* 비회원(준/웹) 주문 가상 사용자 보유쿠폰(상태:'A')개수 조회 */
			List<CouponPublicationDto> couponPubDto = getPosseCouponInfo(couponSelMap, msrSqlMap);
			
			if(couponPubDto != null ){
				possessionCouponCnt = couponPubDto.size();
			}
			
			// 비회원(준/웹) 주문 가상 사용자 보유쿠폰(상태:'A')개수 >= 회수대상 쿠폰개수 경우 쿠폰 회수 처리
			if(possessionCouponCnt >= issueCouponCnt){
				
				if (issueCouponCnt > 0) {
					
					for (CouponPublicationDto couponMap : couponPubDto) { 
						
						// 회수대상 쿠폰개수 체크하여 회수 진행 중지
						if(issueCouponCnt == 0){
							break;
						}
						
						String couponNo = couponMap.getCouponNumber();
						/* 쿠폰 회수(MSR_COUPON_PUBLICATION_LIST) 처리 */
						int couponUpdateCnt = updateExpireCoupon(couponNo, logTitle, msrSqlMap);
						
						if (couponUpdateCnt < 1) {
							
							buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 쿠폰 회수(MSR_COUPON_PUBLICATION_LIST) 처리 (실패(couponMap = ").append (couponMap).append (")");	
							tLogger.info(buf.toString());
							result = false;
							
						}else{
							
							// 비회원(준/웹) 주문 가상 사용자 쿠폰 회수 이력 및 주문 거래 취소 이력 저장을 위한 Parm 지정
							nonMemTrdInsMap.put("di", 				(String) withDrawMap.get ("di"));					// 본인인증 고유값
							nonMemTrdInsMap.put("userNumber",		(String) withDrawMap.get ("userNumber"));			// 사용자 번호
							nonMemTrdInsMap.put("amount",			(Integer) withDrawMap.get ("rewardBaseAmt"));		// 사용금액(비회원 주문 가상 사용자 리워드 기준금액)
							nonMemTrdInsMap.put("accAmt",			(Integer) withDrawMap.get ("afterAccAmt"));			// 집계기간 누적 사용 금액(집계기간 누적 사용 금액 - 주문 총 결제금액)
							nonMemTrdInsMap.put("accRewardAmt",		(Integer) withDrawMap.get ("afterAaccRewardAmt"));	// 집계기간 리워드 지급 차감된 누적 사용 금액(집계기간 리워드 지급 차감된 누적 사용 금액 - 주문 총 결제금액)
							nonMemTrdInsMap.put("orderNo", 			(String) withDrawMap.get ("orderNo"));				// 사이렌오더 주문번호
							nonMemTrdInsMap.put("businessDate", 	(String) withDrawMap.get ("businessDate"));			// POS거래 영업일
							nonMemTrdInsMap.put("branchCode", 		(String) withDrawMap.get ("branchCode"));			// 매장 코드
							nonMemTrdInsMap.put("posNumber", 		(String) withDrawMap.get ("posNumber"));			// POS 번호
							nonMemTrdInsMap.put("posTrdNumber", 	(String) withDrawMap.get ("posTrdNumber"));			// POS 거래 번호
							nonMemTrdInsMap.put("rwdTrdYn", 		"Y");												// 리워드 집계 대상 여부(Y)
							nonMemTrdInsMap.put("couponNumber", 	couponNo);											// 쿠폰번호
							
							nonMemTrdInsMap.put("trdBusinessDate", 	(String) withDrawMap.get ("trdBusinessDate"));		// 영업일자(거래마스터 및 거래쿠폰내역 생성위해 지정)
							nonMemTrdInsMap.put("trdBranchCode", 	(String) withDrawMap.get ("trdBranchCode"));		// 매장코드(거래마스터 및 거래쿠폰내역 생성위해 지정)
							nonMemTrdInsMap.put("trdPosNumber", 	(String) withDrawMap.get ("trdPosNumber"));			// POS번호(거래마스터 및 거래쿠폰내역 생성위해 지정)
							nonMemTrdInsMap.put("trdPosTrdNumber", 	(String) withDrawMap.get ("trdPosTrdNumber"));		// POS거래번호(거래마스터 및 거래쿠폰내역 생성위해 지정)
							
							/* 거래 마스터(MSR_TRD_MASTER)에 거래(X:사용취소)내역 생성 */
							int trdSeq = insertTrdMaster(nonMemTrdInsMap, logTitle, msrSqlMap);
							
							if(trdSeq < 1){
								
								buf.delete (0, buf.length ()).append ("거래 마스터(MSR_TRD_MASTER)에 거래(X-사용취소)내역 생성 실패(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
								tLogger.info(buf.toString());
								result = false;
								
							}else{
								
								nonMemTrdInsMap.put("trdSeq", 			trdSeq);										// 거래일련번호
								
								/* 쿠폰 회수 이력(MSR_TRD_COUPON_LIST) 생성 */
								int instCouponTrdHistCnt = insertTrdCoupon(nonMemTrdInsMap, logTitle, msrSqlMap);
								
								if (instCouponTrdHistCnt < 1) {
									
									buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 쿠폰 회수 이력(MSR_TRD_COUPON_LIST) 생성 실패(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
									tLogger.info(buf.toString());
									result = false;
									
								}else{
									
									nonMemTrdInsMap.put("trdType", 		"C");											// 거래유형(C:리워드회수)
									
									/* 비회원(준/웹) 주문 가상 사용자 쿠폰 회수 이력(MSR_XO_NON_TRD_HIST) 생성 */
									int insertCouponTrdHistCnt = insertNonMemberTrdHist(nonMemTrdInsMap, logTitle, msrSqlMap);
									
									if ( insertCouponTrdHistCnt < 1 ) {
										buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 쿠폰 회수 이력(MSR_XO_NON_TRD_HIST) 생성 실패(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
										tLogger.info(buf.toString());
										result = false;
									}
								}
							}
							
							// 회수대상 쿠폰 정상 회수 시 쿠폰개수 차감
							issueCouponCnt--;
						}	
					}
				}
			}
			// 회수대상 쿠폰개수 > 보유쿠폰(상태:'A')개수 경우 쿠폰 회수 불가처리
			else{
				buf.delete(0, buf.length()).append("쿠폰 회수 불가(회수대상:").append (issueCouponCnt).append ("개, 보유쿠폰:").append (possessionCouponCnt).append(")");
				tLogger.info(buf.toString());
				result = false;				
			}
		}
		
		return result;
	}
	
	/**
	 * 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회
	 * @param	nonMemSelMap
	 * @param	msrSqlMap
	 * @return	NonMemberInfo
	 * @throws	Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNonMemberInfo(Map<String, Object> nonMemSelMap, SqlMapClient msrSqlMap) throws Exception {
		return (Map<String, Object>) msrSqlMap.queryForObject ("nonMember.getNonMemberInfo", nonMemSelMap);
	}	
	
	/**
	 * 웹회원 주문 가상 사용자 정보(MSR_XO_NON_MEMBER) 조회
	 * 
	 * @param	webUserNumber
	 * @param	msrSqlMap
	 * @return	WebMemberInfo
	 * @throws	Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getWebMemberInfo(String webUserNumber, SqlMapClient msrSqlMap) throws Exception {
		return (Map<String, Object>) msrSqlMap.queryForObject ("nonMember.getWebMemberInfo", webUserNumber);
	}	

	/**
	 * 비회원(준/웹) 주문 거래 이력(MSR_XO_NON_TRD_HIST) 저장
	 * @param	nonMemTrdInsMap
	 * @param	lotTitle
	 * @param	msrSqlMap
	 * @return	resultCnt
	 * @throws	Exception 
	 */
	private int insertNonMemberTrdHist(Map<String, Object> nonMemTrdInsMap, String lotTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		int resultCnt = -1;
		
		try {
			resultCnt = msrSqlMap.update("nonMember.insertNonMemberTrdHist", nonMemTrdInsMap);
			if ( resultCnt < 1 ) {
				buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 거래 이력(MSR_XO_NON_TRD_HIST) 저장 (실패(resultCnt = ").append (resultCnt).append (")");
				tLogger.info(buf.toString());
			}
		} catch (SQLException se) {
			buf.delete (0, buf.length ()).append (lotTitle).append ("[").append (nonMemTrdInsMap.get("userNumber")).append ("] DB FAIL : parameter=").append (nonMemTrdInsMap.toString ());
			tLogger.info (buf.toString ());
			tLogger.info(se.toString());
			resultCnt = -1;
		} catch (Exception e) {
			tLogger.info("Exception :: " + e.toString());
			resultCnt = -1;
		}
		
		return resultCnt;
	}
	
	/**
	 * 비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER)에 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경
	 * @param	nonMemUpdMap
	 * @param	lotTitle
	 * @param	msrSqlMap
	 * @return	resultCnt
	 * @throws	Exception 
	 */
	private int updateNonMemberInfo(Map<String, Object> nonMemUpdMap, String lotTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		int resultCnt = -1;
		
		try {
			resultCnt = msrSqlMap.update("nonMember.updateNonMemberInfo", nonMemUpdMap);
			if ( resultCnt < 1 ) {
				buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 정보(MSR_XO_NON_MEMBER)에 사용자 누적 사용 금액 / 리워드 대상 누적 금액 변경 (실패(resultCnt = ").append (resultCnt).append (")");
				tLogger.info(buf.toString());
			}
		} catch (SQLException se) {
			buf.delete (0, buf.length ()).append (lotTitle).append ("[").append (nonMemUpdMap.get("userNumber")).append ("] DB FAIL : parameter=").append (nonMemUpdMap.toString ());
			tLogger.info (buf.toString ());
			tLogger.info(se.toString());
			resultCnt = -1;
		} catch (Exception e) {
			resultCnt = -1;
		}
		
		return resultCnt;
	}
	
	/**
	 * 비회원(준/웹) 주문 거래 사용금액(MSR_XO_NON_TRD_HIST)
	 * @param	xopOrdNo
	 * @param	msrSqlMap
	 * @return	NonMemTrdHist
	 * @throws	Exception 
	 */
	private int  getNonMemTrdAmtInfo(String xopOrdNo, SqlMapClient msrSqlMap) throws Exception {
		return (Integer) msrSqlMap.queryForObject ("nonMember.getNonMemTrdAmtInfo", xopOrdNo);
	}
	
	/**
	 * 비회원(준/웹) 주문 거래 이력(MSR_XO_NON_TRD_HIST) 조회
	 * @param	xopOrdNo
	 * @param	msrSqlMap
	 * @return	NonMemTrdHist
	 * @throws	Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNonMemTrdHist(String xopOrdNo, SqlMapClient msrSqlMap) throws Exception {
		return (Map<String, Object>) msrSqlMap.queryForObject ("nonMember.getNonMemTrdHist", xopOrdNo);
	}
	
	/**
	 * 보유쿠폰(MSR_COUPON_PUBLICATION_LIST)개수 조회
	 * @param	couponSelMap
	 * @param	msrSqlMap
	 * @return	couponList
	 * @throws	Exception 
	 */
	@SuppressWarnings("unchecked")
	private List<CouponPublicationDto> getPosseCouponInfo(Map<String, Object> couponSelMap, SqlMapClient msrSqlMap) throws Exception {
		try{
		List<CouponPublicationDto> couponList = (List<CouponPublicationDto>) msrSqlMap.queryForList("nonMember.getPosseCouponInfo", couponSelMap);
		return couponList;
		} catch (Exception e) {
			tLogger.error (e.toString());
			throw e;
		}
	}
	
	/**
	 * 비회원(준/웹) 주문 가상 사용자 쿠폰 회수(MSR_COUPON_PUBLICATION_LIST) 처리
	 * @param	couponNumber
	 * @param	lotTitle
	 * @param	msrSqlMap
	 * @return	resultCnt
	 * @throws	Exception 
	 */
	private int updateExpireCoupon(String couponNumber, String logTitle, SqlMapClient msrSqlMap) {
		StringBuffer buf = new StringBuffer ();
		int resultCnt = -1;
		
		try {
			resultCnt = msrSqlMap.update("nonMember.updateExpireCoupon", couponNumber);

			if ( resultCnt < 1 ) {
				buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 쿠폰 회수(MSR_COUPON_PUBLICATION_LIST) 처리 (실패(resultCnt = ").append (resultCnt).append (")");
				tLogger.info(buf.toString());
			}
		} catch (SQLException se) {
			buf.delete (0, buf.length ()).append (logTitle).append ("[").append (couponNumber).append ("] DB FAIL : parameter=").append (couponNumber);
			tLogger.info (buf.toString ());
			tLogger.info(se.toString());
			resultCnt = -1;
		} catch (Exception e) {
			resultCnt = -1;
		}
		
		return resultCnt;
	}
	
	/**
	 * 거래 마스터(MSR_TRD_MASTER)에 거래(X:사용취소)내역 생성 및 거래번호 조회
	 * @param	nonMemTrdInsMap
	 * @param	lotTitle
	 * @param	msrSqlMap
	 * @return	resultCnt
	 * @throws	Exception 
	 */
	private int insertTrdMaster(Map<String, Object> nonMemTrdInsMap, String lotTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		int resultCnt = -1;
		
		try {

			resultCnt = (Integer) msrSqlMap.insert("nonMember.insertTrdMaster", nonMemTrdInsMap);

			if ( resultCnt < 1 ) {
				buf.delete (0, buf.length ()).append ("거래 마스터(MSR_TRD_MASTER)에 거래(X:사용취소)내역 생성 및 거래번호 조회 (실패(resultCnt = ").append (resultCnt).append (")");
				tLogger.info(buf.toString());
			}
		} catch (SQLException se) {
			buf.delete (0, buf.length ()).append (lotTitle).append ("[orderNo : ").append (nonMemTrdInsMap.get("orderNo")).append ("] DB FAIL : parameter=").append (nonMemTrdInsMap.toString ());
			tLogger.info (buf.toString ());
			tLogger.info(se.toString());
			resultCnt = -1;
		} catch (Exception e) {
			tLogger.info("Exception :: " + e.toString());
			resultCnt = -1;
		}
		
		return resultCnt;
	}
	
	/**
	 * 쿠폰 회수 이력(MSR_TRD_COUPON_LIST) 생성
	 * @param	nonMemTrdInsMap
	 * @param	lotTitle
	 * @param	msrSqlMap
	 * @return	resultCnt
	 * @throws	Exception 
	 */
	private int insertTrdCoupon(Map<String, Object> nonMemTrdInsMap, String lotTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		int resultCnt = -1;
		
		try {

			resultCnt = msrSqlMap.update("nonMember.insertTrdCoupon", nonMemTrdInsMap);

			if ( resultCnt < 1 ) {
				buf.delete (0, buf.length ()).append ("비회원(준/웹) 주문 가상 사용자 쿠폰 회수이력(MSR_TRD_COUPON_LIST) 생성 (실패(resultCnt = ").append (resultCnt).append (")");
				tLogger.info(buf.toString());
			}
		} catch (SQLException se) {
			buf.delete (0, buf.length ()).append (lotTitle).append ("[couponNumber : ").append (nonMemTrdInsMap.get("couponNumber")).append ("] DB FAIL : parameter=").append (nonMemTrdInsMap.toString ());
			tLogger.info (buf.toString ());
			tLogger.info(se.toString());
			resultCnt = -1;
		} catch (Exception e) {
			tLogger.info("Exception :: " + e.toString());
			resultCnt = -1;
		}
		
		return resultCnt;
	}

}
