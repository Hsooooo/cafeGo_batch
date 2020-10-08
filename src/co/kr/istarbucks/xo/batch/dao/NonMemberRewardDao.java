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
 * ��ȸ��(��/��) ������ ���� / ȸ�� NonMemberRewardDao.
 * @author 
 * @since 2018. 11. 02
 * @version 
 */
public class NonMemberRewardDao {

	private final Logger tLogger = Logger.getLogger ("TRADE");
	private static final String loggerTitle = "[TradeCorrection] ";
	
	public Boolean rewardProcess(Map<String, Object> orderMap, SqlMapClient msrSqlMap)throws Exception {
		
		StringBuffer buf = new StringBuffer ();
		Boolean nonMembRewResult = true;																		// ��ȸ��(��/��) ȸ���� ��� ������ ����/ȸ�� ���
		
		try {
		
			// ��ȸ��(��/��)�� ��� ������ ���� / ȸ�� ������ ���� ���� ����
			String nextStatus		=	(String) orderMap.get ("nextStatus");									// �ֹ� ���� ����(31:�����Ϸ�, 22:�ֹ����)
			String xopOrdNo			=	(String) orderMap.get ("xopOrdNo");										// �ֹ���ȣ
			String saleDate			=	(String) orderMap.get ("saleDate");										// ������
			String storeCd			=	(String) orderMap.get ("storeCd");										// �����ڵ�
			String posNo			=	(String) orderMap.get ("posNo");										// POS��ȣ
			String seqNo			=	(String) orderMap.get ("seqNo");										// �ŷ���ȣ
			String userId			= 	(String) orderMap.get ("userId");										// ����� ���̵�
			int orderTotalPayAmt	=	(Integer) orderMap.get ("totalPayAmt");									// �ֹ� �� �����ݾ� {�� ǰ���Ѿ� - �� ���ξ� - �� ������ - �� �������αݾ�}
			
			Map<String, Object> nonMemSelMap		= new HashMap<String, Object> ();							// ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ�� ���� Parameter Map
			Map<String, Object> nonMemInfoMap		= new HashMap<String, Object> ();							// ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� Result Map
			Map<String, Object> nonMemTrdInfoMap	= new HashMap<String, Object> ();							// ��ȸ��(��/��) �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ��ȸ �� Result Map
			Map<String, Object> withDrawMap			= new HashMap<String, Object> ();							// ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ���� ���� Parm ����
			
			int amount				= 0; 
			int beforAccAmt			= 0;
			int afterAccAmt			= 0;
			int beforAccRewardAmt	= 0;
			int afterAccRewardAmt	= 0;
			
			Configuration conf		= CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
			int rewardBaseAmt		= conf.getInt("nonMember.reward.base.amt", 100000);							// ��ȸ��(��/��) �ֹ� ���� ����� ������ ���رݾ� ��ȸ
			
			boolean withDrawCpSucc	= true;																		// ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� �����
			boolean withDrawRdSucc	= true;																		// ��ȸ��(��/��) �ֹ� ���� ����� ������ �ݾ� ���� �����
			
			/** �ֹ� ���� ���°� '�����Ϸ�(31)'�� ��� ������ ���� **/
			if(StringUtils.equals(nextStatus, "31")){
				
				/* ��ȸ��(��/��) �ֹ� �ŷ� ���ݾ�(MSR_XO_NON_TRD_HIST) ��ȸ */
				amount = getNonMemTrdAmtInfo(xopOrdNo, msrSqlMap);												//	ȸ���ݾ�(��ȸ��(��/��) ������ ���� ������ ���� ��ȸ)
				
				/**
				 * ��ȸ��(��/��) ������ ������ �ߺ����� �������� �ʰ� �ϱ� ���� ���̷� ���� �ֹ���ȣ�� ��ȸ �� ��ȸ��(��/��) �ֹ� �ŷ� ���ݾ� Ȯ��
				**/
				if(amount == 0){
					
					buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) ȸ�� ������ ���� Start");
					tLogger.info (buf.toString ());
					
					// ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ�� ���� Parm ����
					nonMemSelMap.put("userId",	userId);														// ����� ���̵�
					nonMemSelMap.put("status",	"I");															// ���� (I: ����, D:���)
					
					/* ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ */
					nonMemInfoMap = getNonMemberInfo(nonMemSelMap, msrSqlMap);
					
					// ��ȸ��(��/��) �ֹ� ���� ����� ������ Null �� ��� Exception ó��
					if(nonMemInfoMap == null){
						buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ��������� �������� �ʽ��ϴ�.(nonMemSelMap ").append (nonMemSelMap).append (")");
						throw new Exception (buf.toString ());	
					}
					
					// ���� ������ �ݾ׿� �ֹ� �ݾ� �ջ��Ͽ� ������ ���� 
					beforAccAmt			 = (Integer) nonMemInfoMap.get ("accAmt");									// �ֹ����� �� ����Ⱓ ���� ��� �ݾ�
					afterAccAmt			 = beforAccAmt + orderTotalPayAmt;											// ����Ⱓ ���� ��� �ݾ�(����Ⱓ ���� ��� �ݾ� + �ֹ� �� �����ݾ�)
					beforAccRewardAmt	 = (Integer) nonMemInfoMap.get ("accRewardAmt");							// �ֹ����� �� ����Ⱓ ������ ���� ������ ���� ��� �ݾ�
					afterAccRewardAmt	 = beforAccRewardAmt + orderTotalPayAmt;									// ����Ⱓ ������ ���� ��(����Ⱓ ������ ���� ������ ���� ��� �ݾ� + �ֹ� �� �����ݾ�)
					
					// ��ȸ��(��/��) ������ ���� ���� ���� üũ���� Date ��������
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA); 
					Date now = new Date();
					Date today = format.parse(format.format(now));													// ����ð�
					Calendar calDate = new GregorianCalendar(Locale.KOREA);
					calDate.setTime(new Date());
					
					String beforeRewardExpireDate = (String) nonMemInfoMap.get ("rewardExpireDate");				// �ֹ����� �� ������ ���� ���� ����
					String afterRewardExpireDate = "";																// �ֹ����� �� ������ ���� ���� ����
					
					/**
					 * ������ ���� �������� �� ���� �� ���� �⵵�� ���������ڸ� ���� ���ڷ� ����
					**/
					if(StringUtils.isBlank (beforeRewardExpireDate)){
						
						calDate.add(Calendar.YEAR, 1); // 1���� ���Ѵ�.
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
					  * ������ ���� �������� ���� �� ���� ���� üũ�Ͽ� ���� ���ڷ� ����
					 **/
					else{
						Date reward_expire_date = format.parse(beforeRewardExpireDate + "235959");					// ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� ������ ���� ���� ����
						int rewardExpireDateCheck = today.compareTo(reward_expire_date);							// ���糯¥�� ������ ���� ���� ���� ���ĳ�¥�̸� 1�� ��ȯ, �� �ݴ��� ��� -1�� ��ȯ
						
						/**
						 * ������ ���� �������� ���� �� �ش�⵵�� ���������ڸ� ���� ���ڷ� �����ϰ�,
						 * ����Ⱓ ������ ���� ������ ���� ��� �ݾ��� ���� ������ ���� �ݾ��� ������ �ֹ��ݾ����� ����
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
						    
						    tLogger.info("������ ���� �������� ���� >>>>>>>>>>>>>> ������ ���� �������� / ������ ���� ������ ���� ��� �ݾ� ������!!!!!!!!!");
						    tLogger.info("[As Is] ������ ���� �������� :: " + beforeRewardExpireDate + ", ������ ���� ������ ���� ��� �ݾ� :: " + beforAccRewardAmt);
						    tLogger.info("[To Be] ������ ���� �������� :: " + afterRewardExpireDate + ", ������ ���� ������ ���� ��� �ݾ� :: " + afterAccRewardAmt);
						    
						}else{
							afterRewardExpireDate = beforeRewardExpireDate;
						}
					}
					
					// ��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� �̷� ������ ���� Parm ����
					withDrawMap.put("di", 					(String) nonMemInfoMap.get ("di"));						// �������� ������
					withDrawMap.put("userNumber",			(String) nonMemInfoMap.get ("userNumber"));				// ����� ��ȣ
					withDrawMap.put("userId",				(String) nonMemInfoMap.get ("userId"));					// ����� ���̵�
					withDrawMap.put("trdType", 				"U");													// �ŷ�����(U:���)
					withDrawMap.put("amount",				(Integer) orderTotalPayAmt);							// ���ݾ�
					withDrawMap.put("accAmt",				(Integer) afterAccAmt);									// ����Ⱓ ���� ��� �ݾ�(����Ⱓ ���� ��� �ݾ� + �ֹ� �� �����ݾ�)
					withDrawMap.put("accRewardAmt",			(Integer) afterAccRewardAmt);							// ����Ⱓ ������ ���� ������ ���� ��� �ݾ�(����Ⱓ ������ ���� ������ ���� ��� �ݾ� + �ֹ� �� �����ݾ�)
					withDrawMap.put("rewardExpireDate", 	afterRewardExpireDate);									// ������ ���� ���� ����
					withDrawMap.put("orderNo", 				xopOrdNo);												// ���̷����� �ֹ���ȣ
					withDrawMap.put("businessDate", 		saleDate);												// POS�ŷ� ������
					withDrawMap.put("branchCode", 			storeCd);												// ���� �ڵ�
					withDrawMap.put("posNumber", 			posNo);													// POS ��ȣ
					withDrawMap.put("posTrdNumber", 		seqNo);													// POS �ŷ� ��ȣ
					withDrawMap.put("rwdTrdYn", 			"Y");													// ������ ���� ��� ����(Y)
					
					/** ��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� �̷� ���� **/
					withDrawRdSucc = withDrawReward(withDrawMap, loggerTitle, msrSqlMap);
					
					if(!withDrawRdSucc){
						buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� �̷� ���� ����(withDrawMap ").append (withDrawMap).append (")");
						throw new Exception (buf.toString ());	
					}
					
					buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) ȸ�� ������ ���� The End");
					tLogger.info (buf.toString ());
					
				}else{
					buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) �������� �ʾ� ������ �������� ����(xopOrdNo : ").append (xopOrdNo).append (")");
					tLogger.info (buf.toString ());
				}
			}
			
			/** �ֹ� ���� ���°� '�ֹ����(22)'�� ��� ������ ȸ�� **/
			else if(StringUtils.equals(nextStatus, "22")){
				
				/* ��ȸ��(��/��) �ֹ� �ŷ� ���ݾ�(MSR_XO_NON_TRD_HIST) ��ȸ */
				amount = getNonMemTrdAmtInfo(xopOrdNo, msrSqlMap);														//	ȸ���ݾ�(��ȸ��(��/��) ������ ȸ�� ������ ���� ��ȸ)
				
				if(amount > 0){
					
					if(amount == orderTotalPayAmt){
						
						/* ��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ��ȸ */
						nonMemTrdInfoMap = getNonMemTrdHist(xopOrdNo, msrSqlMap);
						
						/** 
						 * ��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷��� ���� �� ��� ������ ���� ȸ�� ����
						**/
						if(nonMemTrdInfoMap != null){
							buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) ȸ�� ������ ȸ�� Start");
							tLogger.info (buf.toString ());
							
							// ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ�� ���� Parm ����
							nonMemSelMap.put("di",			(String) nonMemTrdInfoMap.get("di"));						// �������� ������ DI
							nonMemSelMap.put("userNumber",	(String) nonMemTrdInfoMap.get("userNumber"));				// ����� ��ȣ
							
							/* ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ */
							nonMemInfoMap = getNonMemberInfo(nonMemSelMap, msrSqlMap);
							
							// �̰�����(MSR:��ȸ�� �� MSRȸ��, WEB:��ȸ�� �� ��ȸ��, NOT:�̰�X)�� ���� ������ ȸ�� ����
							String transferGb = (String) nonMemInfoMap.get("transferGb");
							Map<String, Object> webMemInfoMap = null;													// ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� Result Map
							int accAmt = 0;
							int accRewardAmt = 0;
							String rewardExpireDate = "";
							String msrMembGb = "NOT";																	// ��ȸ�� MSR ȸ�� ��� ����
							String webMembNo = "";																		// ��ȸ�� �϶��� ����� ���̵�(MSRȸ�� ��� �� ��� ������ �ݾ� ������ ���� ����)
							String transferUserNumber = (String) nonMemInfoMap.get ("transferUserNumber");				// ������ �̰� ����� ��ȣ�� ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� �̰� ����� ��ȣ ����
							
							/** 
							 * �̰������� 'WEB(��ȸ�� �� ��ȸ��)' ��� �̰� �� ��ȸ���� ����Ⱓ ���� ��� �ݾ�, ����Ⱓ ������ ���� ������ ���� ��� �ݾ�, ������ ���� ���� ���� ��ȸ
							 * �̰����п� ���� ����Ⱓ ���� ��� �ݾ�, ����Ⱓ ������ ���� ������ ���� ��� �ݾ�, ������ ���� ���� ���� ����
							**/
							if(StringUtils.equals(transferGb, "WEB")){
								String webUserNumber = (String) nonMemInfoMap.get ("transferUserNumber");				// �̰� �� ��ȸ���� ����� ��ȣ
								
								/* ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ */
								webMemInfoMap = getWebMemberInfo(webUserNumber, msrSqlMap);
								
								accAmt			 = (Integer) webMemInfoMap.get ("accAmt");								// �̰� �� ��ȸ���� ����Ⱓ ���� ��� �ݾ�
								accRewardAmt	 = (Integer) webMemInfoMap.get ("accRewardAmt");						// �̰� �� ��ȸ���� ����Ⱓ ������ ���� ������ ���� ��� �ݾ�
								rewardExpireDate = (String) webMemInfoMap.get ("rewardExpireDate");						// �̰� �� ��ȸ���� ������ ���� ���� ����
								
							}else{
								
								accAmt			 = (Integer) nonMemInfoMap.get ("accAmt");								// ��ȸ��(��/��)�� ����Ⱓ ���� ��� �ݾ�
								accRewardAmt	 = (Integer) nonMemInfoMap.get ("accRewardAmt");						// ��ȸ��(��/��)�� ����Ⱓ ������ ���� ������ ���� ��� �ݾ�
								rewardExpireDate = (String) nonMemInfoMap.get ("rewardExpireDate");						// ��ȸ��(��/��)�� ������ ���� ���� ����
								
							}
							
							/**
							 * ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� Result Map�� ���� �� ���
							 * ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ �� �̰� ����� ��ȣ ����
							**/
			    			if(webMemInfoMap != null){
			    				msrMembGb = (String) webMemInfoMap.get ("transferGb");									// ��ȸ�� MSR ȸ�� ��� ����(MSRȸ�� ��� �� ��� ������ �ݾ� ������ ���� ����)
			    				webMembNo = (String) webMemInfoMap.get ("userNumber");									// ��ȸ�� �϶��� ����� ���̵�(MSRȸ�� ��� �� ��� ������ �ݾ� ������ ���� ����)
			    				transferUserNumber = (String) webMemInfoMap.get ("transferUserNumber");					// ��ȸ������ �̰� �� ��ȸ���� MSRȸ�� ��� ��
			    			}
							
							// ���� ������ �ݾ׿� �ֹ� �ݾ� �����Ͽ� ���� ���� 
							beforAccAmt				 = accAmt;															// �ֹ����� �� ����Ⱓ ���� ��� �ݾ�
							
							/**
							 * ���� �ݾ�(�ֹ� �� �����ݾ�)�� ������ �ݾ�(����Ⱓ ������ ���� ������ ���� ��� �ݾ�)���Ͽ�
							 * ����Ⱓ ������ ���� ������ ���� ��� �ݾ��� �����Ѵ�.
							**/
							if(orderTotalPayAmt > accRewardAmt){						
								beforAccRewardAmt	 = rewardBaseAmt + accRewardAmt;	
							}else{
								beforAccRewardAmt	 = accRewardAmt;
							}
							
							afterAccAmt			 	= beforAccAmt - orderTotalPayAmt;									// ����Ⱓ ���� ��� �ݾ�(����Ⱓ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							afterAccRewardAmt		= beforAccRewardAmt - orderTotalPayAmt;								// ����Ⱓ ������ ���� ��(����Ⱓ ������ ���� ������ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							
							//  ��ȸ�� �ֹ� ���� ����� ���� ȸ���� ���� Parm ����
							withDrawMap.put("di",					(String) nonMemInfoMap.get ("di"));					// �������� ������ DI
							withDrawMap.put("userNumber",			(String) nonMemInfoMap.get ("userNumber"));			// ����� ��ȣ
							withDrawMap.put("userId",				(String) nonMemInfoMap.get ("userId"));				// ����� ���̵�
							withDrawMap.put("trdType", 				"X");												// �ŷ�����(X:������)
							withDrawMap.put("transferUserNumber",	transferUserNumber);								// �̰� ����� ��ȣ
							withDrawMap.put("rewardExpireDate",		rewardExpireDate);									// ������ ���� ���� ����
							withDrawMap.put("orderNo", 				xopOrdNo);											// ���̷����� �ֹ���ȣ
							withDrawMap.put("amount", 				orderTotalPayAmt);									// ���ݾ�
							withDrawMap.put("rewardBaseAmt", 		rewardBaseAmt);										// ��ȸ�� �ֹ� ���� ����� ������ ���رݾ�
							withDrawMap.put("accAmt", 				afterAccAmt);										// ����Ⱓ ���� ��� �ݾ�(����Ⱓ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							withDrawMap.put("accRewardAmt", 		afterAccRewardAmt);									// ����Ⱓ ������ ���� ������ ���� ��� �ݾ�(����Ⱓ ������ ���� ������ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							withDrawMap.put("businessDate", 		(String) nonMemTrdInfoMap.get ("businessDate"));	// ���ŷ� POS�ŷ� ������
							withDrawMap.put("branchCode", 			(String) nonMemTrdInfoMap.get ("branchCode"));		// ���ŷ� ���� �ڵ�
							withDrawMap.put("posNumber", 			(String) nonMemTrdInfoMap.get ("posNumber"));		// ���ŷ� POS ��ȣ
							withDrawMap.put("posTrdNumber", 		(String) nonMemTrdInfoMap.get ("posTrdNumber"));	// ���ŷ� POS �ŷ� ��ȣ
							withDrawMap.put("transferGb", 			transferGb);										// �̰�����(MSR:��ȸ�� �� MSRȸ��, WEB:��ȸ�� �� ��ȸ��, NOT:�̰�X)
							withDrawMap.put("rwdTrdYn", 			"Y");												// ������ ���� ��� ����(Y)
							withDrawMap.put("msrMembGb", 			msrMembGb);											// ��ȸ�� MSR ȸ�� ��� ����
							withDrawMap.put("webMembNo", 			webMembNo);											// ��ȸ�� �϶��� ����� ���̵�(MSRȸ�� ��� �� ��� ������ �ݾ� ������ ���� ����)
							
							withDrawMap.put("trdBusinessDate", 		saleDate);											// ��������(�ŷ������� �� �ŷ��������� �������� ����)
							withDrawMap.put("trdBranchCode", 		storeCd);											// �����ڵ�(�ŷ������� �� �ŷ��������� �������� ����)
							withDrawMap.put("trdPosNumber", 		posNo);												// POS��ȣ(�ŷ������� �� �ŷ��������� �������� ����)
							withDrawMap.put("trdPosTrdNumber", 		seqNo);												// POS�ŷ���ȣ(�ŷ������� �� �ŷ��������� �������� ����)
							
							/**
							 * ���� �ݾ�(�ֹ� �� �����ݾ�) >  ������ �ݾ�(����Ⱓ ������ ���� ������ ���� ��� �ݾ�) �� ��� ���� ȸ�� ����
							**/
							if(orderTotalPayAmt > accRewardAmt){
								/** ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� ���� **/
								withDrawCpSucc = withDrawCoupon(withDrawMap, loggerTitle, msrSqlMap);
							}
							
							if(!withDrawCpSucc){
								buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� �߱� �� ���� ȸ�� ����(withDrawMap ").append (withDrawMap).append (")");
								throw new Exception (buf.toString ());
							}
							
							/** ��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� �̷� ���� **/
							withDrawRdSucc = withDrawReward(withDrawMap, loggerTitle, msrSqlMap);
							
							if(!withDrawRdSucc){
								buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� ��� �̷� ���� ����(withDrawMap ").append (withDrawMap).append (")");
								throw new Exception (buf.toString ());
							}
							
							buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) ȸ�� ������ ȸ�� The End");
							tLogger.info (buf.toString ());
							
						}else{
							buf.delete (0, buf.length ()).append (loggerTitle).append ("��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) �������� �ʾ� ������ ȸ������ ����(xopOrdNo : ").append (xopOrdNo).append (")");
							tLogger.info (buf.toString ());
						}
						
					}else{
						buf.delete (0, buf.length ()).append ("�ŷ� ��� �ݾ� ����ġ(��ҿ�û �ݾ� : ").append (amount).append (", ȸ���ݾ� : ").append (orderTotalPayAmt).append (")");
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
	 * ��ȸ��(��/��) �ֹ� ���� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� �� �ֹ� �ŷ� �̷� ����
	 * @param withDrawReward
	 * @param lotTitle
	 * @param msrSqlMap
	 * @return
	 * @throws Exception 
	 */
	private boolean withDrawReward(Map<String, Object> withDrawMap, String logTitle, SqlMapClient msrSqlMap) throws Exception {
		StringBuffer buf = new StringBuffer ();
		boolean result = true;
		
		String transferGb = StringUtils.defaultIfEmpty((String) withDrawMap.get("transferGb"), "");		// �̰�����(MSR:��ȸ�� �� MSRȸ��, WEB:��ȸ�� �� ��ȸ��, NOT:�̰�X)
		
		String msrMembGb = StringUtils.defaultIfEmpty((String) withDrawMap.get("msrMembGb"), "");		// ��ȸ�� MSR ȸ�� ��� ����
		String webMembNo = StringUtils.defaultIfEmpty((String) withDrawMap.get("webMembNo"), "");		// ��ȸ�� �϶��� ����� ���̵�
		
		/* ��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ���� */
		int insertTrdHistCnt = insertNonMemberTrdHist(withDrawMap, logTitle, msrSqlMap);
		if ( insertTrdHistCnt < 1 ) {
			buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ���� ����(nonMemTrdInsMap :: ").append (withDrawMap).append (")");	
			tLogger.info(buf.toString());
			result = false;
		}
		
		/** 
		 * �̰������� 'WEB:��ȸ�� �� ��ȸ��' �� ��� �̰� �� ������� ����� ���� ��� �ݾװ�
		 * ������ ��� ���� �ݾ� ������ ���� ����ڹ�ȣ�� �̰� ����� ��ȣ�� ����(�ֹ� ��ҽ� ����� ����) 
		**/
		if(StringUtils.equals(transferGb, "WEB")){
			
			/** 
			 * MSR ȸ������ ��� �� ������� ��� ���� �� ����� ���� ��� �ݾ� ������ ��� ���� �ݾ��� ����� ��ȣ�� ��ȸ���� ���� ��ȣ�� ���� 
			**/
			if(StringUtils.equals(msrMembGb, "MSR")){
				withDrawMap.put("userNumber", webMembNo);	
			}else{
				withDrawMap.put("userNumber", withDrawMap.get("transferUserNumber"));	
			}
		}
		
		/* ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER)�� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� */
		int updateCnt = updateNonMemberInfo(withDrawMap, logTitle, msrSqlMap);
		if ( updateCnt < 1 ) {
			buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER)�� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� ����(nonMemUpdMap :: ").append (withDrawMap).append (")");	
			tLogger.info(buf.toString());
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� ����
	 * @param withDrawMap
	 * @param lotTitle
	 * @param msrSqlMap
	 * @return
	 * @throws Exception 
	 */
	private boolean withDrawCoupon(Map<String, Object> withDrawMap, String logTitle, SqlMapClient msrSqlMap) throws Exception {
		
		StringBuffer buf = new StringBuffer ();
		boolean result = true;
		
		int possessionCouponCnt = 0 ;																			// ��ȸ��(��/��) �ֹ� ���� ����� ��������(����:'A')����
		int issueCouponCnt = 0;																					// ��ȸ��(��/��) �ֹ� ���� ����� ȸ����� ��������
		
		Map<String, Object> nonMemTrdInsMap		= new HashMap<String, Object> ();								// ��ȸ��(��/��) �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ���� ������ ���� Parameter Map
		
		/* ��ȸ��(��/��) �ֹ� ���� ����� ȸ����� �������� ��� */
		int rewardBaseAmt = (Integer) withDrawMap.get ("rewardBaseAmt");											// ��ұݾ�(���ݾ�)
		int amount = (Integer) withDrawMap.get ("amount");															// ��ȸ�� �ֹ� ���� ����� ������ ���رݾ�
		
		if(rewardBaseAmt > amount){
			amount = rewardBaseAmt + amount;
		}
		
		/** 
		 * ��ұݾ�(���ݾ�)�� ��ȸ�� �ֹ� ���� ����� ������ ���رݾ��� ���� ��ȸ��(��/��) �ֹ� ���� ����� ȸ����� �������� ���
		**/
		issueCouponCnt = amount/ rewardBaseAmt;																		// ��ȸ��(��/��) �ֹ� ���� ����� ȸ����� ��������
		
		
		// ��ȸ��(��/��) �ֹ� ���� ����� ȸ����� ������ ���� �� ��� ȸ�� ����
		if(issueCouponCnt > 0){
			
			Map<String, Object> couponSelMap		= new HashMap<String, Object> ();								// ���� ����(MSR_COUPON_PUBLICATION_LIST) ��ȸ�� ���� Parameter Map
			couponSelMap.put("di",					(String) withDrawMap.get ("di"));								// �������� ������ DI
			couponSelMap.put("userNumber",			(String) withDrawMap.get ("userNumber"));						// ����� ��ȣ
			couponSelMap.put("transferUserNumber",	(String) withDrawMap.get ("transferUserNumber"));				// �̰� ����� ��ȣ
			couponSelMap.put("orderNo", 			(String) withDrawMap.get ("orderNo"));							// ���̷����� �ֹ���ȣ
			couponSelMap.put("businessDate", 		(String) withDrawMap.get ("businessDate"));						// POS�ŷ� ������
			couponSelMap.put("branchCode", 			(String) withDrawMap.get ("branchCode"));						// ���� �ڵ�
			couponSelMap.put("posNumber", 			(String) withDrawMap.get ("posNumber"));						// POS ��ȣ
			couponSelMap.put("posTrdNumber", 		(String) withDrawMap.get ("posTrdNumber"));						// POS �ŷ� ��ȣ
			
			
			/* ��ȸ��(��/��) �ֹ� ���� ����� ��������(����:'A')���� ��ȸ */
			List<CouponPublicationDto> couponPubDto = getPosseCouponInfo(couponSelMap, msrSqlMap);
			
			if(couponPubDto != null ){
				possessionCouponCnt = couponPubDto.size();
			}
			
			// ��ȸ��(��/��) �ֹ� ���� ����� ��������(����:'A')���� >= ȸ����� �������� ��� ���� ȸ�� ó��
			if(possessionCouponCnt >= issueCouponCnt){
				
				if (issueCouponCnt > 0) {
					
					for (CouponPublicationDto couponMap : couponPubDto) { 
						
						// ȸ����� �������� üũ�Ͽ� ȸ�� ���� ����
						if(issueCouponCnt == 0){
							break;
						}
						
						String couponNo = couponMap.getCouponNumber();
						/* ���� ȸ��(MSR_COUPON_PUBLICATION_LIST) ó�� */
						int couponUpdateCnt = updateExpireCoupon(couponNo, logTitle, msrSqlMap);
						
						if (couponUpdateCnt < 1) {
							
							buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ��(MSR_COUPON_PUBLICATION_LIST) ó�� (����(couponMap = ").append (couponMap).append (")");	
							tLogger.info(buf.toString());
							result = false;
							
						}else{
							
							// ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� �̷� �� �ֹ� �ŷ� ��� �̷� ������ ���� Parm ����
							nonMemTrdInsMap.put("di", 				(String) withDrawMap.get ("di"));					// �������� ������
							nonMemTrdInsMap.put("userNumber",		(String) withDrawMap.get ("userNumber"));			// ����� ��ȣ
							nonMemTrdInsMap.put("amount",			(Integer) withDrawMap.get ("rewardBaseAmt"));		// ���ݾ�(��ȸ�� �ֹ� ���� ����� ������ ���رݾ�)
							nonMemTrdInsMap.put("accAmt",			(Integer) withDrawMap.get ("afterAccAmt"));			// ����Ⱓ ���� ��� �ݾ�(����Ⱓ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							nonMemTrdInsMap.put("accRewardAmt",		(Integer) withDrawMap.get ("afterAaccRewardAmt"));	// ����Ⱓ ������ ���� ������ ���� ��� �ݾ�(����Ⱓ ������ ���� ������ ���� ��� �ݾ� - �ֹ� �� �����ݾ�)
							nonMemTrdInsMap.put("orderNo", 			(String) withDrawMap.get ("orderNo"));				// ���̷����� �ֹ���ȣ
							nonMemTrdInsMap.put("businessDate", 	(String) withDrawMap.get ("businessDate"));			// POS�ŷ� ������
							nonMemTrdInsMap.put("branchCode", 		(String) withDrawMap.get ("branchCode"));			// ���� �ڵ�
							nonMemTrdInsMap.put("posNumber", 		(String) withDrawMap.get ("posNumber"));			// POS ��ȣ
							nonMemTrdInsMap.put("posTrdNumber", 	(String) withDrawMap.get ("posTrdNumber"));			// POS �ŷ� ��ȣ
							nonMemTrdInsMap.put("rwdTrdYn", 		"Y");												// ������ ���� ��� ����(Y)
							nonMemTrdInsMap.put("couponNumber", 	couponNo);											// ������ȣ
							
							nonMemTrdInsMap.put("trdBusinessDate", 	(String) withDrawMap.get ("trdBusinessDate"));		// ��������(�ŷ������� �� �ŷ��������� �������� ����)
							nonMemTrdInsMap.put("trdBranchCode", 	(String) withDrawMap.get ("trdBranchCode"));		// �����ڵ�(�ŷ������� �� �ŷ��������� �������� ����)
							nonMemTrdInsMap.put("trdPosNumber", 	(String) withDrawMap.get ("trdPosNumber"));			// POS��ȣ(�ŷ������� �� �ŷ��������� �������� ����)
							nonMemTrdInsMap.put("trdPosTrdNumber", 	(String) withDrawMap.get ("trdPosTrdNumber"));		// POS�ŷ���ȣ(�ŷ������� �� �ŷ��������� �������� ����)
							
							/* �ŷ� ������(MSR_TRD_MASTER)�� �ŷ�(X:������)���� ���� */
							int trdSeq = insertTrdMaster(nonMemTrdInsMap, logTitle, msrSqlMap);
							
							if(trdSeq < 1){
								
								buf.delete (0, buf.length ()).append ("�ŷ� ������(MSR_TRD_MASTER)�� �ŷ�(X-������)���� ���� ����(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
								tLogger.info(buf.toString());
								result = false;
								
							}else{
								
								nonMemTrdInsMap.put("trdSeq", 			trdSeq);										// �ŷ��Ϸù�ȣ
								
								/* ���� ȸ�� �̷�(MSR_TRD_COUPON_LIST) ���� */
								int instCouponTrdHistCnt = insertTrdCoupon(nonMemTrdInsMap, logTitle, msrSqlMap);
								
								if (instCouponTrdHistCnt < 1) {
									
									buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� �̷�(MSR_TRD_COUPON_LIST) ���� ����(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
									tLogger.info(buf.toString());
									result = false;
									
								}else{
									
									nonMemTrdInsMap.put("trdType", 		"C");											// �ŷ�����(C:������ȸ��)
									
									/* ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� �̷�(MSR_XO_NON_TRD_HIST) ���� */
									int insertCouponTrdHistCnt = insertNonMemberTrdHist(nonMemTrdInsMap, logTitle, msrSqlMap);
									
									if ( insertCouponTrdHistCnt < 1 ) {
										buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ�� �̷�(MSR_XO_NON_TRD_HIST) ���� ����(nonMemTrdInsMap :: ").append (nonMemTrdInsMap).append (")");	
										tLogger.info(buf.toString());
										result = false;
									}
								}
							}
							
							// ȸ����� ���� ���� ȸ�� �� �������� ����
							issueCouponCnt--;
						}	
					}
				}
			}
			// ȸ����� �������� > ��������(����:'A')���� ��� ���� ȸ�� �Ұ�ó��
			else{
				buf.delete(0, buf.length()).append("���� ȸ�� �Ұ�(ȸ�����:").append (issueCouponCnt).append ("��, ��������:").append (possessionCouponCnt).append(")");
				tLogger.info(buf.toString());
				result = false;				
			}
		}
		
		return result;
	}
	
	/**
	 * ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ
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
	 * ��ȸ�� �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER) ��ȸ
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
	 * ��ȸ��(��/��) �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ����
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
				buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ���� (����(resultCnt = ").append (resultCnt).append (")");
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
	 * ��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER)�� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ����
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
				buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ����(MSR_XO_NON_MEMBER)�� ����� ���� ��� �ݾ� / ������ ��� ���� �ݾ� ���� (����(resultCnt = ").append (resultCnt).append (")");
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
	 * ��ȸ��(��/��) �ֹ� �ŷ� ���ݾ�(MSR_XO_NON_TRD_HIST)
	 * @param	xopOrdNo
	 * @param	msrSqlMap
	 * @return	NonMemTrdHist
	 * @throws	Exception 
	 */
	private int  getNonMemTrdAmtInfo(String xopOrdNo, SqlMapClient msrSqlMap) throws Exception {
		return (Integer) msrSqlMap.queryForObject ("nonMember.getNonMemTrdAmtInfo", xopOrdNo);
	}
	
	/**
	 * ��ȸ��(��/��) �ֹ� �ŷ� �̷�(MSR_XO_NON_TRD_HIST) ��ȸ
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
	 * ��������(MSR_COUPON_PUBLICATION_LIST)���� ��ȸ
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
	 * ��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ��(MSR_COUPON_PUBLICATION_LIST) ó��
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
				buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ��(MSR_COUPON_PUBLICATION_LIST) ó�� (����(resultCnt = ").append (resultCnt).append (")");
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
	 * �ŷ� ������(MSR_TRD_MASTER)�� �ŷ�(X:������)���� ���� �� �ŷ���ȣ ��ȸ
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
				buf.delete (0, buf.length ()).append ("�ŷ� ������(MSR_TRD_MASTER)�� �ŷ�(X:������)���� ���� �� �ŷ���ȣ ��ȸ (����(resultCnt = ").append (resultCnt).append (")");
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
	 * ���� ȸ�� �̷�(MSR_TRD_COUPON_LIST) ����
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
				buf.delete (0, buf.length ()).append ("��ȸ��(��/��) �ֹ� ���� ����� ���� ȸ���̷�(MSR_TRD_COUPON_LIST) ���� (����(resultCnt = ").append (resultCnt).append (")");
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
