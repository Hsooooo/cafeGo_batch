package co.kr.istarbucks.xo.batch.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushServerMonitoringDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.OnmUtil;
import co.kr.istarbucks.xo.batch.common.util.XOUtil;
import co.kr.istarbucks.xo.batch.main.PushServerMonitoring;

import com.google.gson.Gson;
import com.ibatis.sqlmap.client.SqlMapClient;

public class PushServerMonitoringDao {
	private static Logger logger      = Logger.getLogger("PushServerMonitoring");
	private final Configuration conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
	private final OnmUtil ou      = new OnmUtil();
	
	//MSR push ?????? ???????? ????
	@SuppressWarnings({ "unchecked", "null" })
	public static List<PushServerMonitoringDto> getRepinfo() {
		//
		List pushUsers = new ArrayList();
		PushServerMonitoringDto dto = new PushServerMonitoringDto();
		Configuration conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
		
		String idList = conf.getString("push.servermonitoring.receive.id");
		String idListArr[] = idList.split("\\|");

		for(int i = 0; i < idListArr.length; i++) {
			dto.setUser_id(idListArr[i]);
			pushUsers.add(i, idListArr[i]);
		}
				
		return pushUsers;		
	}


	//???Send
	@SuppressWarnings ( {"unused", "unchecked"} )
	public PushHistoryDto sendPush(PushServerMonitoringDto recvInfo) {
		StringBuffer buf = new StringBuffer ();
		PushHistoryDto pushDto = new PushHistoryDto();
		
		String apiUrl      = conf.getString("auto.push.api.url");
		int pushTimeOut    = conf.getInt("auto.push.timeout", 5);
		String pushCharset = conf.getString("auto.push.charset");

		String pushMsg = "";
		String serverType = "";
		String msgTpCd      = "";
		String priorityFlag = "";	
		String viewType = conf.getString("auto.push.viewtype");
		String screenId = conf.getString("auto.push.screenid");
		
		msgTpCd      = conf.getString("push.servermonitoring.receive.msgTpCd");
		priorityFlag = conf.getString("push.servermonitoring.priorityflag");
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		pushMsg = XOUtil.getPropertiesString("push.servermonitoring.receive.msg", new String[]{Integer.toString(hour)});
		pushDto.setPush_message (pushMsg); // PUSH ?????
		pushDto.setUser_id(recvInfo.getUser_id());

		String msg = "";
		String userId = "";
		try {
			msg = URLEncoder.encode (pushMsg, pushCharset);
			userId = URLEncoder.encode (pushDto.getUser_id(), pushCharset);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		
		String postParam = ou.concatString(
				  "userId="        , userId
				, "&serverType="   , "SOBAT"
				, "&msgTpCd="      , msgTpCd
              , "&message="      , msg
             // , "&screenId="     , screenId
              , "&priorityFlag=" , priorityFlag
             );
		logger.info("========postParam" + postParam);
		// step 1. Http URL Connection START =================================================
		HttpURLConnection connection = null;
		URL url = null;
		try {
			if ( StringUtils.isEmpty (apiUrl) ) {
				pushDto.setResult_code ("999");
				pushDto.setResult_msg ("url is null");
				return pushDto;
			}
			url = new URL (apiUrl);
			connection = (HttpURLConnection) url.openConnection ();
		} catch ( Exception e ) {
			buf.delete (0, buf.length ()).append ("Connection Error : ").append (e.getMessage ());
			
			pushDto.setResult_code ("999");
			pushDto.setResult_msg (buf.toString ());
			return pushDto;
		}
		// step 1. Http URL Connection END   =================================================
		if ( connection != null ) {
			try {
				// step 2. HttpURL Setting START ===========================================================
				connection.setDoOutput (true);
				connection.setInstanceFollowRedirects (false);
				connection.setConnectTimeout (pushTimeOut);
				connection.setReadTimeout (pushTimeOut);
				connection.setRequestMethod ("POST");
				
				DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
				dos.write(postParam.getBytes());
				dos.flush();
				dos.close();
				// step 2. HttpURL Setting END   ===========================================================
				
				// step 3. RESPONSE START ==================================================================
				int code = connection.getResponseCode ();

				String resultCode = "";
				String resultMessage = "";
				
				if ( code == 200 || code == 201 ) {
					resultCode = connection.getHeaderField("resultCode");    // ?????
					resultMessage = connection.getHeaderField("resultMessage"); // ????????
				} else {
					resultCode = connection.getHeaderField("resultCode");    // ?????
					resultMessage = connection.getHeaderField("resultMessage"); // ????????
				}
				
				buf.delete (0, buf.length ()).append (" PUSH RESPONSE (").append (code).append (") :[").append (resultCode).append ("]");
                logger.info (buf.toString ());
                
				// RESPONSE Header
				if(StringUtils.isEmpty(resultCode) && StringUtils.isEmpty(resultMessage)) {
					pushDto.setResult_code ("997");
					pushDto.setResult_msg ("resultMap is null");
				} else {
					if(StringUtils.isEmpty(resultCode)) {
						buf.delete (0, buf.length ()).append ("resultCode is null");
						pushDto.setResult_code ("996");
						pushDto.setResult_msg(buf.toString ());
					} else {
						pushDto.setResult_code(resultCode);
						pushDto.setResult_msg(resultMessage);
					}
				}
				
				// step 3. RESPONSE END   ==================================================================
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
				
				buf.delete (0, buf.length ()).append ("Connection Error : ").append (e.getMessage ());
				
				pushDto.setResult_code ("998");
				pushDto.setResult_msg (buf.toString ());
				return pushDto;
			} finally {
				if ( connection != null ) {
					connection.disconnect ();
				}
			}
		}

		logger.info("user_id : " + recvInfo.getUser_id() + ", result_code : " + pushDto.getResult_code());
		return pushDto;
	}
}
