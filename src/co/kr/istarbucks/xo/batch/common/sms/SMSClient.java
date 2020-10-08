package co.kr.istarbucks.xo.batch.common.sms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.mgr.SMSClientMgr;

/**
 * SMS 발송 처리
 * @author JUNO
 *
 */
public class SMSClient {
	
	private static Log logger = LogFactory.getLog("smsClient");
	private final SMSClientMgr scm;
	
	private final Configuration conf;
	private final String server_inet;
	private final String info_list;
	private final String error_list;
	private String message		= null;
	private String send_name	= null;
	private final String send_id;
	private final String memberid;
//	private String callback		= null;
	private final String errCallback;
	private final String infoCallback;
	private final String statCallback;
	private final String info_msg;
	private final String error_msg;
	private final String message_header;
	private final String stat_list;
	private String code			= null;	
	private final String rTime;
	private String regularStat	= "N";
	private String result		= "0";
	private final boolean send_all;
	private final boolean send_error;
	private final boolean send_info;
	private final boolean send_stat;
//	private String send_ip	= "";
	
	//2014.01.07 CSH 추가 - 정합성 체크 관련
	private final boolean send_valid;
	private final String valid_list;
	private final String validCallback;
	private final String valid_msg;
	
	
	public SMSClient() {
		scm			= new SMSClientMgr();
		
		conf 			= CommPropertiesConfiguration.getConfiguration("sms.properties");
		
		send_all 		= conf.getBoolean("sms.send.all");
		send_error 	= conf.getBoolean("sms.send.error");
		send_info	 	= conf.getBoolean("sms.send.info");
		send_stat	 	= conf.getBoolean("sms.send.stat");
		//2014.01.07 CSH 추가 - 정합성 체크
		send_valid	 	= conf.getBoolean("sms.send.valid");
		
		server_inet   = conf.getString("sms.server.inet");
		info_list 		= conf.getString("sms.receive.info");
		error_list 		= conf.getString("sms.receive.error");
		stat_list		= conf.getString("sms.receive.stat");
		//2014.01.07 CSH 추가 - 정합성 체크
		valid_list	 	= conf.getString("sms.receive.valid");
		
		send_id		= conf.getString("sms.send.id");
		send_name	= conf.getString("sms.send.name");
		memberid	= conf.getString("sms.send.memberid");
		infoCallback	= conf.getString("sms.callback.info");
		errCallback	= conf.getString("sms.callback.error");
		statCallback	= conf.getString("sms.callback.stat");
		//2014.01.07 CSH 추가 - 정합성 체크
		validCallback	 	= conf.getString("sms.callback.valid");
		
		info_msg		= conf.getString("sms.message.end");
		error_msg	= conf.getString("sms.message.error");
		message_header = conf.getString("sms.message.header");
		//2014.01.07 CSH 추가 - 정합성 체크
		valid_msg	 	= conf.getString("sms.message.valid");
		
		rTime			= conf.getString("sms.reserve.time");
		
//		send_ip 		= conf.getString("sms.server.inet");
	}
	
//	public void sendSMS (String type, String desc, String sh_name) {
//		if ("info".equals(type)) {
//			if (getSMSStatus(type)) {
//				message = SMSUtil.replaceString(this.info_msg, "$DAEMON$", sh_name);
//				sendInfoSMS();
//			}
//		} else if ("error".equals(type)) {
//			if (getSMSStatus(type)) {
//				message = SMSUtil.replaceString(this.error_msg, "$DAEMON$", sh_name);
//				message = SMSUtil.replaceString(this.error_msg, "$DESC$", desc);
//				sendErrorSMS();
//			}
//		}		
//	}
	
	/**
	 * 일반 SMS (데몬/배치 종료) 
	 */
	public void sendInfoSMS(String sh_name) {
		if (getSMSStatus("info")) {
			
			String[] info_ctn = info_list.split("\\|");
			message = SMSUtil.replaceString(this.info_msg, "$DAEMON$", sh_name);
			
			logger.info("info SMS message=["+this.message+"]");
			
			for (int i=0; i<info_ctn.length; i++) {
				insertSMS(info_ctn[i], "info");
			}
		}
	}
	
	/**
	 * 에러 SMS (데몬/배치 에러)
	 */
	public void sendErrorSMS(String sh_name, Exception e ) {
		if (getSMSStatus("error")) {
			
			String[] err_ctn = error_list.split("\\|");
			String msg = SMSUtil.getErrorMsg(e);
			
			message = this.message_header;
			if("60.196.0.84".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " devS") ;
			}else if("60.196.0.199".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S1") ; 
			}else if("60.196.0.200".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S2") ;
			}else if("10.21.21.123".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S1") ;
			}
			
			message = message + SMSUtil.replaceString(this.error_msg, "$DAEMON$", sh_name);
			message = SMSUtil.replaceString(message, "$DESC$", msg);
			//message = message + SMSUtil.replaceString(this.error_msg, "$DAEMON$", sh_name);
			
			if (message.getBytes().length > 80) {
				message = message.substring(0, 80);
			}
			
			logger.info("error SMS message=["+this.message+"]");
			
			for (int i=0; i<err_ctn.length; i++) {
				insertSMS(err_ctn[i], "error");
			}
		}
	}
	
	public void sendErrorSMS(String sh_name, String desc ) {
		if (getSMSStatus("error")) {
			
			String[] err_ctn = error_list.split("\\|");
			
			message = this.message_header;
			if("60.196.0.84".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " devS") ;
			}else if("60.196.0.199".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S1") ; 
			}else if("60.196.0.200".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S2") ;
			}else if("10.21.21.123".equals(this.server_inet)){
				message = SMSUtil.replaceString(message, "$SERVER$", " S1") ;
			}
			
			message = message + SMSUtil.replaceString(this.error_msg, "$DAEMON$", sh_name);
			message = SMSUtil.replaceString(message, "$DESC$", desc);
			
			if (message.getBytes().length > 80) {
				message = message.substring(0, 80);
			}
			
			logger.info("error SMS message=["+this.message+"]");
			
			for (int i=0; i<err_ctn.length; i++) {
				insertSMS(err_ctn[i], "error");
			}
		}
	}
	
	/**
	 * 대용량 쿠폰 통계 SMS
	 * @param code
	 * @param grade
	 * @param count
	 */
	public void sendStatSMS(String code, String grade, int count) {
		if (getSMSStatus("stat")) {
			
			String message		= conf.getString("sms.message.stat");
			String[] stat_ctn 	= stat_list.split("\\|");
			
			if("002".equals(code)) {
				this.code = "1+1";
			} else if("004".equals(code)) {
				this.code = "PO";
			} else {
				this.code = "";
			}
			
			message = SMSUtil.replaceString(message, "$CODE$", this.code);
			message = SMSUtil.replaceString(message, "$GRADE$", grade.toUpperCase());
			message = SMSUtil.replaceString(message, "$COUNT$", String.valueOf(count));
			
			this.message = message;
			
			logger.info("stat SMS message="+this.message);
			
			for(int i=0; i<stat_ctn.length; i++) {
				insertSMS(stat_ctn[i], "stat");
			}
		}
	}
	
	/**
	 * 일반 통계 SMS
	 * @param code
	 * @param grade
	 * @param count
	 */
	public void sendStatSMS(String couponCode, int count) {
		if (getSMSStatus("stat")) {
			
			String[] stat_ctn	= stat_list.split("\\|");
			String message	= conf.getString("sms.message.stat.regular");
			
			String statName = SMSUtil.getCouponName(couponCode);
			
			if(statName == null || "null".equals(statName)) {
				statName = couponCode;
			}
			
			message = SMSUtil.replaceString(message, "$NAME$", statName);
			message = SMSUtil.replaceString(message, "$COUNT$", String.valueOf(count));
			
			this.message = message;
			
			logger.info("stat SMS message="+this.message);
			
			for(int i=0; i<stat_ctn.length; i++) {
				insertSMS(stat_ctn[i], "stat");
			}
		}
	}
	
	/**
	 * 정기 발행 쿠폰 통계 SMS 발송
	 * @param lastParam
	 * @param sysParam
	 * @param code
	 */
	public void sendCouponStatSMS(Map<String, String> lastParam, Map<String, String> sysParam, String code) throws Exception{
		logger.info("sendCouponStatSMS START!!" );
		
		FileOutputStream fos	= null;
	    FileLock fl 				= null;
		
		try {
			//서로다른 VM 에서 동시에 동일 메소드를 사용하기 때문에 파일 락을 걸어둔다.
			
			fos = new FileOutputStream(FileUtils.getFile(new String[]{this.conf.getString("sms.lock.path")}));
		    fl = fos.getChannel().tryLock();
		    		
			if (getSMSStatus("stat")) {	//통계 SMS 전달이 true 일때만 발송
				String message		= null;			
				String[] stat_ctn	= stat_list.split("\\|");
				
				//발신자 이름 가져오기
				//통계 문자 하나에 어떤 통계를 넣을 것인가를 결정하기 위해 그룹으로 묶는다.
				this.send_name = SMSUtil.getSendName(code);
				
				//발송 메세지 만들어오기
				message = getStatSMSMessage(lastParam, sysParam, code);
				
				logger.info("sendCouponStatSMS Message=" + this.message);
				
				SimpleDateFormat sdf	= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);			
				String sysdate 			= sdf.format(System.currentTimeMillis());
				
				Map<String, String> paramMap = new HashMap<String, String>();
				
				paramMap.put("inDate",		sysdate);
				paramMap.put("sendName",	this.send_name);
				paramMap.put("rDate",		sysdate);
				paramMap.put("rTime",		this.rTime);
				
				List<Map<String, Object>> infoList = null;
				
				try {
					//금일 발행 쿠폰에 대한 통계 sms 데이터가 들어가 있는지 확인
					//snedname 으로 문자 그룹을 확인한다.
					infoList = scm.getCouponStatSMSInfo(paramMap);
				} catch (Exception e) {
					logger.error(e, e);
				}
					
				if (infoList != null && infoList.size() > 0) {
					// 동일한 sendname 그룹에 발송될 통계 데이터가 존재한다면 발송 메세지를 update 
					for (Map<String, Object> info : infoList) {
						int seq			= (Integer) info.get("seqNo");
						String msg		= (String) info.get("msg") + message;
						String result		= (String) info.get("result");
						
						if("C".equals(result)) {
							this.result = "D";
						} else if ("D".equals(result)) {
							this.result = "0";
						}
						
						logger.info("seq="+seq+ ", message==" + msg);
						try {
							scm.updateCouponStatSMSInfo(seq, msg, this.result);
						} catch (Exception ex) {
							logger.error(ex, ex);
						}
					}
				} else {
					// 통계 데이터가 없다면 새로 insert
					this.message		= "[MSR STAT]" + message;
					this.regularStat	= "Y";
					
//					if (!"009".equals(code)) {
						this.result = "C";
//					}
					
					for (int i=0; i<stat_ctn.length; i++) {
						insertSMS(stat_ctn[i], "stat");
					}
				}
				
			} else {
				logger.info("NO Recieve CTN");
			}
			
			logger.info("sendThreeStatSMS END!!");
		
		} catch (IOException ioe) {
			logger.error(ioe, ioe);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		} finally {
			try {
				fl.release();
				fos.close();
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}
	
	/**
	 * 자동충전 쿠폰 통계 SMS 발송
	 * @param lastParam
	 * @param sysParam
	 * @param code
	 */
	public void sendCouponAutoReloadSMS(Map<String, String> lastParam, Map<String, String> sysParam, String code) throws Exception{
		logger.info("sendCouponAutoReloadSMS START!!" );
		
		FileOutputStream fos	= null;
	    FileLock fl 				= null;
	    		
		try {
		   fos = new FileOutputStream(FileUtils.getFile(new String[]{this.conf.getString("sms.lock.path")}));
		    fl = fos.getChannel().tryLock();
		    		
			if (getSMSStatus("stat")) {	//통계 SMS 전달이 true 일때만 발송
				String message	= null;			
				String[] stat_ctn	= stat_list.split("\\|");
				
				this.send_name = "couponStat2";
				
				//발송 메세지 만들어오기
				message = getStatSMSMessage(lastParam, sysParam, code);
				
				logger.info("ThreeStatSMS Message=" + this.message);
				
				SimpleDateFormat sdf	= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);			
				String sysdate 			= sdf.format(System.currentTimeMillis());
				
				Map<String, String> paramMap = new HashMap<String, String>();
				
				paramMap.put("inDate",		sysdate);
				paramMap.put("sendName",	this.send_name);
				paramMap.put("rDate",		sysdate);
				paramMap.put("rTime",		this.rTime);
				
				// 통계 데이터가 없다면 새로 insert
				this.message		= "[MSR STAT]" + message;
				this.result		= "0";
				
				for (int i=0; i<stat_ctn.length; i++) {
					insertSMS(stat_ctn[i], "stat");
				}
				
			} else {
				logger.info("NO Recieve CTN");
			}
			
			logger.info("sendThreeStatSMS END!!");
		
		} catch (IOException ioe) {
			logger.error(ioe, ioe);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		} finally {
			try {
				fl.release();
				fos.close();
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}
	
	/**
	 * 전송될 SMS 구성
	 * @param ctn
	 */
	public void insertSMS(String ctn, String type) {
		try {
			Map<String, String> ctnMap 	= new HashMap<String, String>();
			Map<String, Object> param 	= new HashMap<String, Object>();
			
			SimpleDateFormat sdf 		= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
			SimpleDateFormat sdf2 		= new SimpleDateFormat("HHmmss", Locale.KOREAN);
			
			String rDate 	= null;
			String rTime 	= null;	
			String inDate 	= sdf.format(System.currentTimeMillis());
			String inTime 	= sdf2.format(System.currentTimeMillis());
			
			//회신번호 자르기, type 에 따라 각기 다른 회신번로를 사용한다.
			String[] sphone	= null;
			
			if("info".equals(type)) {
				sphone = this.infoCallback.split("\\-");
			} else if ("stat".equals(type)) {
				sphone = this.statCallback.split("\\-");
			} else if ("error".equals(type)) {
				sphone = this.errCallback.split("\\-");
			} else if ("valid".equals(type)) {	//2014.01.07 추가 - 정합성 체크
				sphone = this.validCallback.split("\\-");
			} else {
				sphone = this.infoCallback.split("\\-");
			}
			 
			//수신자 ctn 자르기
			ctnMap = SMSUtil.getDivCtn(ctn);
			
			//정기 쿠폰 발행 SMS 경우에는 예약 시간을 걸어둔다.
			if ("Y".equals(this.regularStat)) {
				rDate = inDate;
				rTime = this.rTime;
			} else {
				rDate = "00000000";
				rTime = "000000";
			}
			param.put("sendName", send_name);
			param.put("sendid", send_id);
			param.put("member", memberid);
			param.put("msg", message);
			param.put("Sphone1", 	sphone[0]);
			param.put("Sphone2", 	sphone[1]);
			param.put("Sphone3", 	sphone[2]);
			param.put("inDate",		inDate);
			param.put("inTime",	inTime);
			//정기 발행 쿠폰은 당일 06시로 예약 전송 세팅
			param.put("rdate", 		rDate);
			param.put("rtime", 		rTime);
			param.put("result", 	this.result);
			param.put("kind", 		"S");
			param.put("Rphone1", 	ctnMap.get("Rphone1"));
			param.put("Rphone2", 	ctnMap.get("Rphone2"));
			param.put("Rphone3", 	ctnMap.get("Rphone3"));
			
			logger.info("===================== SEND SMS DATA ===================");
			logger.info("Sphone = " + sphone[0] + sphone[1] + sphone[2]);
			logger.info("inDate = " + inDate);
			logger.info("inTime = " + inTime);
			logger.info("member = " + send_name);
			logger.info("sendid = " + send_id);
			logger.info("sendName = " + send_name);
			logger.info("msg = " + message);
			logger.info("rDate = " + rDate);
			logger.info("rTime = " + rTime);
			logger.info("result = " + "0");
			logger.info("kind = " + "S");
			logger.info("Rphone = " + ctnMap.get("Rphone1")+ctnMap.get("Rphone2")+ctnMap.get("Rphone3"));
			
			try {
				scm.insertSMS(param);
			} catch (Exception e) {
				logger.error(e, e);
			}
		}catch (Exception ex) {
			logger.error(ex, ex);
		}
		
	}
	
	/**
	 * 월 자동충전 통계 SMS 발송
	 * @param lastParam
	 * @param sysParam
	 * @param code
	 */
	public void sendAutoReloadStatSMS(String msg) throws Exception{
		logger.info("sendAutoReloadStatSMS START!!" );
		FileOutputStream fos = null;
	    FileLock fl 			= null;
		
		try {
			fos = new FileOutputStream(FileUtils.getFile(new String[]{this.conf.getString("sms.lock.path")}));
		    fl = fos.getChannel().tryLock();

			if (getSMSStatus("stat")) {	//통계 SMS 전달이 true 일때만 발송
				String message		= null;			
				String[] stat_ctn	= stat_list.split("\\|");
				
				this.send_name = "autoReloadStat";
				
				StringBuffer sb = new StringBuffer();
				
				sb.append("\n");
				sb.append("- ");
				sb.append("Month AutoReload");
				sb.append(" : ");
				sb.append(msg);
				
				//발송 메세지
				message = sb.toString();
				
				logger.info("autoReloadSMS Message=" + this.message);
				
				SimpleDateFormat sdf	= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);			
				String sysdate 			= sdf.format(System.currentTimeMillis());
				
				Map<String, String> paramMap = new HashMap<String, String>();
				
				paramMap.put("inDate",		sysdate);
				paramMap.put("sendName",	this.send_name);
				paramMap.put("rDate",		sysdate);
				paramMap.put("rTime",		this.rTime);
				
				this.message		= "[MSR STAT]" + message;
				this.result			= "0";
				
				for (int i=0; i<stat_ctn.length; i++) {
					insertSMS(stat_ctn[i],"stat");
				}
				
			} else {
				logger.info("NO Recieve CTN");
			}
			
			logger.info("sendAutoReloadStatSMS END!!");
		
		} catch (IOException ioe) {
			logger.error(ioe, ioe);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		} finally {
			try {
				fl.release();
				fos.close();
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}
	
	/**
	 * 우수회원 쿠폰 통계 SMS 발송
	 * @param lastParam
	 * @param sysParam
	 * @param code
	 */
	public void sendStarMemberStatSMS(String statTitle, String msg) throws Exception{
		logger.info("sendStarMemberStatSMS START!!" );
		
		FileOutputStream fos	= null;
	    FileLock fl 			= null;
	    		
		try {
			fos = new FileOutputStream(FileUtils.getFile(new String[]{this.conf.getString("sms.lock.path")}));
		    fl = fos.getChannel().tryLock();
		    		
			if (getSMSStatus("stat")) {	//통계 SMS 전달이 true 일때만 발송
				String message		= null;			
				String[] stat_ctn	= stat_list.split("\\|");
				
				this.send_name = "starMemberCoupon";
				
				StringBuffer sb = new StringBuffer();
				
				sb.append("\n");
				sb.append("- ");
				sb.append(statTitle);
				sb.append(" : ");
				sb.append(msg);
				
				//발송 메세지
				message = sb.toString();
				
				logger.info("sendStarMemberStatSMS Message=" + this.message);
				
				SimpleDateFormat sdf	= new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);			
				String sysdate 			= sdf.format(System.currentTimeMillis());
				
				Map<String, String> paramMap = new HashMap<String, String>();
				
				paramMap.put("inDate",		sysdate);
				paramMap.put("sendName",	this.send_name);
				paramMap.put("rDate",		sysdate);
				paramMap.put("rTime",		this.rTime);
				
				this.message		= "[MSR STAT]" + message;
				this.result			= "0";
				
				for (int i=0; i<stat_ctn.length; i++) {
					insertSMS(stat_ctn[i],"stat");
				}
				
			} else {
				logger.info("NO Recieve CTN");
			}
			
			logger.info("sendStarMemberStatSMS END!!");
		
		} catch (IOException ioe) {
			logger.error(ioe, ioe);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		} finally {
			try {
				fl.release();
				fos.close();
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}
	
	/**
	 * 정합성 체크 오류 SMS
	 * 2014.01.07 CSH 추가
	 * @param errSeq
	 */
	public void sendValidCheckErrSMS(String check_kind, String desc) {
		if (getSMSStatus("valid")) {
			
			String[] valid_ctn	= valid_list.split("\\|");
			
			this.message = this.valid_msg;
			if("60.196.0.84".equals(this.server_inet)){
				this.message = SMSUtil.replaceString(this.message, "$SERVER$", "devS") ;
			}else if("60.196.0.199".equals(this.server_inet)){
				this.message = SMSUtil.replaceString(this.message, "$SERVER$", "S1") ; 
			}else if("60.196.0.200".equals(this.server_inet)){
				this.message = SMSUtil.replaceString(this.message, "$SERVER$", "S2") ;
			}else if("10.21.21.123".equals(this.server_inet)){
				this.message = SMSUtil.replaceString(this.message, "$SERVER$", "S1") ;
			}

			this.message = SMSUtil.replaceString(this.message, "$CHECK_KIND$", check_kind);
			this.message = SMSUtil.replaceString(this.message, "$DESC$", desc);
			
			if (this.message.getBytes().length > 80) {
				this.message = this.message.substring(0, 80);
			}
			
			logger.info("error SMS message=["+this.message+"]");
			
			for (int i=0; i<valid_ctn.length; i++) {
				insertSMS(valid_ctn[i], "valid");
			}
		}
	}
	
	/**
	 * SMS 수신 유효성 체크
	 * @param type
	 * @return
	 */
	private boolean getSMSStatus(String type) {
		boolean status = false;
		
		if (send_all) {	//전체 문자 수신 여부
			if ("info".equals(type)) {	//일반 문자 수신 여부
				if (send_info) {
					status = true;
				} else {
					status = false;
				}
			} else if ("error".equals(type)) {	//에러 문자 수신여부
				if (send_error) {
					status = true;
				} else {
					status = false;
				}
			} else if ("stat".equals(type)) {	//통계 문자 수신여부
				if (send_stat) {
					status = true;
				} else {
					status = false;
				}
			} else if ("valid".equals(type)) {	//2014.01.07 추가 - 정합성 체크 수신여부
				if (send_valid) {
					status = true;
				} else {
					status = false;
				}
			}
		} else {
			status = false;
		}
		
		return status;
	}
	
	/**
	 * 통계 SMS 메세지 구성
	 * @param obj
	 * @return
	 */
	private String getStatSMSMessage(Map<String, String> lastParam, Map<String, String> sysParam, String code) throws Exception {
		
		String result		= null;
		String lastStat		= null;
		String sysStat		= null;
		String coupon		= null;
		
		// 전일 통계 count 설정
		if (lastParam != null) {
			lastStat = lastParam.get("statCount") == null ? "0" : lastParam.get("statCount");
		}else {
			lastStat = "0";
		}
		// 금일 통계 count 설정
		if (sysParam != null) {
			sysStat	= sysParam.get("statCount") == null ? "0" : sysParam.get("statCount");
		} else {
			sysStat	= "0";
		}
		
		coupon = SMSUtil.getCouponName(code);
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(coupon);
		sb.append(": ");
		sb.append(lastStat);
		sb.append("/");
		sb.append(sysStat);
		
		result = sb.toString();
		
		return result;
		
	}
	
	public static void main(String[] args) {
	}
}
