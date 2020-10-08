package co.kr.istarbucks.xo.batch.dao;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.msr.DvsnMsgDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DataCode;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.common.util.OnmUtil;
import co.kr.istarbucks.xo.batch.common.util.XOUtil;

public class OrderAutoPushDao {
	private static Logger logger      = Logger.getLogger("orderAutoPushSender");
	private final Configuration conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
	private final OnmUtil ou      = new OnmUtil();
	
	//자동 푸시 리스트 조회
	@SuppressWarnings("unchecked")
	public List<OrderAutoPushDto> getOrderAutoPush(Map<String, String> paramMap) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (List<OrderAutoPushDto>) sqlMap.queryForList("autoPush.getOrderAutoPush",paramMap);
	}

	//주문 상태 재조회
	public String getOrderStatus(OrderAutoPushDto recvInfo) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_no", recvInfo.getOrder_no());
		paramMap.put("bds_no"  , recvInfo.getBds_no());
		
		return (String) sqlMap.queryForObject("autoPush.getOrderStatus",paramMap);
	}

	//푸시Send
	@SuppressWarnings({"unused"})
	public PushHistoryDto sendPush(OrderAutoPushDto recvInfo) {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		List<DvsnMsgDto> dvsnMsgPushMap = new ArrayList<DvsnMsgDto>();
		List<DvsnMsgDto> dvsnMsgMap = new ArrayList<DvsnMsgDto>();
		try {
			dvsnMsgMap = this.getDvsnMsgInfoByLrcl("XOORD");
			dvsnMsgPushMap = this.getDvsnMsgInfoByLrcl("XOPSH");
		} catch (SQLException e1) {
			logger.error("공통 메시지 테이블 조회 실패!!");
			logger.error(e1.toString());
		}
		StringBuffer buf = new StringBuffer ();
		PushHistoryDto pushDto = new PushHistoryDto();
		String apiUrl      = conf.getString("auto.push.api.url");
		int pushTimeOut    = conf.getInt("auto.push.timeout", 5);
		String pushCharset = conf.getString("auto.push.charset");

		String pushMsg = "";
		String pushCnt = recvInfo.getPush_cnt();
		String receiptOrderNo = recvInfo.getReceipt_order_no();

		String screenId = conf.getString("auto.push.screenid");
		
		String msgType = recvInfo.getMsg_type();
		String bdsName = recvInfo.getBds_name();
		

		if(StringUtils.isNotBlank(bdsName)) {
			bdsName += " ";
		}
		
		// Push API 연동방식 변경으로 추가됨(2017-04-07)
		String msgTpCd      = "";
		String priorityFlag = "";
		
		if(pushCnt.equals("1")) {
			if(StringUtils.equals("B", msgType)) {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtil.getPropertiesString ("auto.push.messageB"), bdsName, bdsName)).toString();
			} else {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtil.getPropertiesString ("auto.push.message1"), bdsName)).toString();
			}
			
			// Push API 연동방식 변경으로 추가됨(2017-04-07)
			msgTpCd      = conf.getString("auto.push.msgtpcd1");
			priorityFlag = conf.getString("auto.push.priorityflag1");
			
		} else if (pushCnt.equals("2")) {
			if(StringUtils.equals("B", msgType)) {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtil.getPropertiesString ("auto.push.messageB"), bdsName, bdsName)).toString();
			} else {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtil.getPropertiesString ("auto.push.message2"), bdsName)).toString();
			}
			
			// Push API 연동방식 변경으로 추가됨(2017-04-07)
			msgTpCd      = conf.getString("auto.push.msgtpcd2");
			priorityFlag = conf.getString("auto.push.priorityflag2");
		}
		
		// UX 리뉴얼 - 공통 코드 테이블의 문구 사용
		if(CollectionUtils.isNotEmpty(dvsnMsgPushMap)) {
			logger.info("PUSH - 공통 메시지 테이블 데이터 사용");
			for(DvsnMsgDto dvsnMsg : dvsnMsgPushMap) {
				if(StringUtils.equals("B", msgType)) {
					if(dvsnMsg.getMsgId().equals(DataCode.getDvsnMsgId(msgType))){
						if(StringUtils.isNotBlank(dvsnMsg.getSbMsgCntnt())) {
							pushMsg = dvsnMsg.getMsgCntnt() + "\n" + dvsnMsg.getSbMsgCntnt();
						}else {
							pushMsg = dvsnMsg.getMsgCntnt();
						}
						pushMsg = this.replaceDvsnMsg(pushMsg, pushDto.getWaitingCup(), "", bdsName, "");
						break;
					}
				}else {
					if(dvsnMsg.getMsgId().equals(DataCode.getDvsnMsgId(msgTpCd))) {
						if(StringUtils.isNotBlank(dvsnMsg.getSbMsgCntnt())) {
							pushMsg = dvsnMsg.getMsgCntnt() + "\n" + dvsnMsg.getSbMsgCntnt();
						}else {
							pushMsg = dvsnMsg.getMsgCntnt();
						}
						pushMsg = this.replaceDvsnMsg(pushMsg, pushDto.getWaitingCup(), "", bdsName, "");
						break;
					}
				}
			}
		}else {
			logger.info("PUSH - 공통 메시지 테이블 데이터 미사용");
		}
		
		String pushType = conf.getString("push.type.mp");
		
		pushDto.setPush_message(pushMsg); // PUSH 메세지
		pushDto.setScreen_id(screenId);
		pushDto.setUser_id(recvInfo.getUser_id());
		pushDto.setPush_id(recvInfo.getPush_id());
		pushDto.setOrder_no(recvInfo.getOrder_no());
		pushDto.setOs_type(recvInfo.getOs_type());
		pushDto.setParam(recvInfo.getOrder_no());
		pushDto.setPush_type("3");
		pushDto.setUrl("");
		
		String msg = "";
		String userId = "";
		String nickname = "";
		try {
			msg = URLEncoder.encode (pushMsg, pushCharset);
			userId = URLEncoder.encode (pushDto.getUser_id(), pushCharset);
			nickname = StringUtils.defaultIfEmpty(this.getuserNickname(userId), receiptOrderNo);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		
		
		
		// 주문번호 조회
		String orderNo = StringUtils.defaultIfEmpty(pushDto.getParam(), "");
		
		// UX 리뉴얼 - 사이렌오더 주문 상태 메시지 저장
		Map<String, String> map = new HashMap<String, String>();
		String orderMsgCntnt = "";
		String orderSbMsgCntnt = "";
		String storeAdclWordsCntnt = "";
		if(CollectionUtils.isNotEmpty(dvsnMsgMap)) {
			logger.info("주문 메시지 - 공통 메시지 테이블 데이터 사용");
			for(DvsnMsgDto dvsnMsg : dvsnMsgMap) {
				// 특정 음료일 경우
				if(StringUtils.equals("B", msgType)) {
					if("XO12_0005".equals(dvsnMsg.getMsgId())) {
						orderMsgCntnt = dvsnMsg.getMsgCntnt();
						orderSbMsgCntnt = dvsnMsg.getSbMsgCntnt();
						orderMsgCntnt = this.replaceDvsnMsg(orderMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
						orderSbMsgCntnt = this.replaceDvsnMsg(orderSbMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
						storeAdclWordsCntnt = dvsnMsg.getStoreAdclWordsCntnt();
						break;
					}
				}else {
					if(pushCnt.equals("1")) {
						if("XO12_0003".equals(dvsnMsg.getMsgId())) {
							orderMsgCntnt = dvsnMsg.getMsgCntnt();
							orderSbMsgCntnt = dvsnMsg.getSbMsgCntnt();
							orderMsgCntnt = this.replaceDvsnMsg(orderMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
							orderSbMsgCntnt = this.replaceDvsnMsg(orderSbMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
							storeAdclWordsCntnt = dvsnMsg.getStoreAdclWordsCntnt();
							break;
						}
					}else if(pushCnt.equals("2")) {
						if("XO12_0004".equals(dvsnMsg.getMsgId())) {
							orderMsgCntnt = dvsnMsg.getMsgCntnt();
							orderSbMsgCntnt = dvsnMsg.getSbMsgCntnt();
							orderMsgCntnt = this.replaceDvsnMsg(orderMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
							orderSbMsgCntnt = this.replaceDvsnMsg(orderSbMsgCntnt, pushDto.getWaitingCup(), "", bdsName, nickname);
							storeAdclWordsCntnt = dvsnMsg.getStoreAdclWordsCntnt();
							break;
						}
					}
				}
			}
			try {
				this.deleteOrderMsgM(orderNo);
				map.put("orderNo", orderNo);
				map.put("orderMsgRgsttSrnm", "1");
				map.put("orederMsgTypeCode", "main");
				map.put("orderMsgCntnt", orderMsgCntnt);
				map.put("storeAdclWordsCntnt", storeAdclWordsCntnt);
				map.put("status", "31");
				this.insertOrderMsgM(map);
				map.clear();
				map.put("orderNo", orderNo);
				map.put("orderMsgRgsttSrnm", "2");
				map.put("orederMsgTypeCode", "sub");
				map.put("orderMsgCntnt", orderSbMsgCntnt);
				map.put("storeAdclWordsCntnt", storeAdclWordsCntnt);
				map.put("status", "31");
				this.insertOrderMsgM(map);
				
				pushType = "OP";
				
			} catch (SQLException e) {
				logger.error("사이렌오더 상태 메시지 저장 실패");
				logger.error(e.getMessage(), e);
			}
		}else {
			logger.info("주문 메시지 - 공통 메시지 테이블 데이터 미사용");
		}
		// POS 주문 PUSH인 경우, 주문번호가 1(나만의메뉴코드) 또는 2(카트코드)으로 시작할 경우 CALL PUSH 발송 시 PUSH_VALUE 없음
		if( (StringUtils.isNotBlank(orderNo) && StringUtils.equals("P", orderNo.substring(0, 1)))
			|| (StringUtils.isNotBlank(orderNo) && StringUtils.equals("1", orderNo.substring(0, 1)))
			|| (StringUtils.isNotBlank(orderNo) && StringUtils.equals("2", orderNo.substring(0, 1))) ) {
			pushDto.setScreen_id("");
			pushDto.setUrl("");
			pushDto.setParam("");
		}
		logger.info("========query string========");
		logger.info(  "userId="+userId + "&serverType="+"SOBAT" + "&msgTpCd="+msgTpCd + "&message="+msg
                   + "&screenId="+pushDto.getScreen_id() + "&priorityFlag="+priorityFlag
                   + "&orderNo="+StringUtils.defaultIfEmpty(pushDto.getParam(), "")
                   + "&pushType="+pushType + "&decodeYn="+"Y");
		logger.info("========query string========");
		String postParam = ou.concatString(
				  "userId="        , userId
				, "&serverType="   , "SOBAT"
				, "&msgTpCd="      , msgTpCd
                , "&message="      , msg
                , "&screenId="     , pushDto.getScreen_id()
                , "&priorityFlag=" , priorityFlag
                , "&orderNo="      , StringUtils.defaultIfEmpty(pushDto.getParam(), "")
                , "&pushType="     , pushType
                , "&decodeYn="     , "Y"
               );
	
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
					resultCode = connection.getHeaderField("resultCode");    // 결과값
					resultMessage = connection.getHeaderField("resultMessage"); // 결과메시지
				} else {
					resultCode = connection.getHeaderField("resultCode");    // 결과값
					resultMessage = connection.getHeaderField("resultMessage"); // 결과메시지
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
		
		int push = Integer.parseInt(pushCnt);
		push = push + 1;
		
		logger.info("order_no : " + recvInfo.getOrder_no() + ", push_cnt : " + push + ", user_id : " + recvInfo.getUser_id() + ", result_code : " + pushDto.getResult_code());
		return pushDto;
	}

	//자동푸시 발송횟수 update
	public void updateOrderAutoPush(OrderAutoPushDto recvInfo) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			sqlMap.startTransaction ();
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_no", recvInfo.getOrder_no());
			paramMap.put("bds_no"  , recvInfo.getBds_no());
			
			sqlMap.update("autoPush.updateOrderAutoPush",paramMap);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}	

	//푸시 히스토리 등록
	public void insertPushHistory(PushHistoryDto pushDto) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			sqlMap.startTransaction ();
			sqlMap.insert("autoPush.insertPushHistory",pushDto);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}

	//스레드에 들어가고 빠져나올때 상태값 변경
	public void updateThreadInputYn(OrderAutoPushDto recvInfo, String input_yn) throws Exception{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_no", recvInfo.getOrder_no());
			paramMap.put("bds_no"  , recvInfo.getBds_no());
			paramMap.put("input_yn", input_yn);
			
			sqlMap.startTransaction ();
			sqlMap.insert("autoPush.updateThreadInputYn",paramMap);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}
	
	/**
	 * SMS 발송
	 * @param orderNo
	 * @param receiptOrderNo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void sendSms(OrderAutoPushDto recvInfo) throws Exception{
		SqlMapClient sqlMap = null;
		SqlMapClient homeSqlMap = null;
		
		try{
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
			homeSqlMap = IBatisSqlConfig.getHomeSqlMapInstance();
			
			sqlMap.startTransaction();
			homeSqlMap.startTransaction();
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_no", recvInfo.getOrder_no());
			paramMap.put("bds_no"  , recvInfo.getBds_no());
			
			//주문번호로 주문매장, 휴대폰번호 조회
			Map<String, String> orderMap = (Map<String, String>) sqlMap.queryForObject("autoPush.getOrderInfo", paramMap);
			String msrFlag = orderMap.get("msrFlag");
			
			//비회원일 경우 sms전송 안함.
			if(StringUtils.equals(msrFlag, "J") || StringUtils.equals(msrFlag, "B")){
				logger.info(" Push SMS 전송 안함 (비회원) ");
			}else{
				String callBack = this.conf.getString ("autopush.sms.callback");	//대표번호
				String message = this.conf.getString("autopush.sms.message");		//SMS내용
				StringBuffer buf = new StringBuffer ();
				
				String storeName = StringUtils.defaultIfEmpty(orderMap.get("storeName"), "");
				String phone = StringUtils.defaultIfEmpty(orderMap.get("phone"), "");
				String userId = StringUtils.defaultIfEmpty(orderMap.get("userId"), "");
				String bdsName = StringUtils.defaultIfEmpty(orderMap.get("bdsName"), "");
				
				if(StringUtils.isNotBlank(bdsName)) {
					bdsName += " ";
				}
				
				// 휴대폰 번호가 없으면 홈페이지 회원 정보에서 조회
				if(StringUtils.isBlank(phone) && StringUtils.isNotBlank(userId)) {
					phone = (String) homeSqlMap.queryForObject("stbMember.getMemberPhone", userId);
				}
				
				// SMS 발송 문구
				String smsMsg = buf.delete (0, buf.length ()).append(String.format(message, storeName, bdsName, recvInfo.getReceipt_order_no())).toString();
				
				// 수신자 휴대폰 번호가 있을때만 SMS 발송 처리
				if(StringUtils.isNotBlank(phone)) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("priority", "S");
					param.put("content", smsMsg);
					param.put("callback", callBack);
					param.put("recipient_num", phone);
					
					//SMS 발송
					sqlMap.insert ("autoPush.insertSmtTran", param);
					
					logger.info("SMS 발송 완료. (order_no : " + recvInfo.getOrder_no() + ", user_id : " + userId + ", sms_msg : " + smsMsg + ")");
				} else {
					logger.info("SMS 발송 실패. phone is null (order_no : " + recvInfo.getOrder_no() + ", user_id : " + userId + ", sms_msg : " + smsMsg + ")");
				}
				
				sqlMap.commitTransaction();
				homeSqlMap.commitTransaction();
			}
			
		} catch (Exception e){
			throw e;
		} finally {
			try {
				if(sqlMap != null){
					sqlMap.endTransaction ();
				}
				if(homeSqlMap != null){
					homeSqlMap.endTransaction();
				}
			} catch ( Exception ee ) {
				logger.error(ee.getMessage(), ee);
			}
		}
	}

	// 대분류 그룹코드로 메시지 상세 테이블 리스트 조회
	@SuppressWarnings("unchecked")
	public List<DvsnMsgDto> getDvsnMsgInfoByLrcl (String msgLrclGroupCode) throws SQLException {
		SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance ();
		List<DvsnMsgDto> dvsnMsgMap = new ArrayList<DvsnMsgDto>();
		
		dvsnMsgMap = (List<DvsnMsgDto>) msrSqlMap.queryForList("dvsnMsgInfo.getDvsnMsgInfoByLrcl", msgLrclGroupCode);

		return dvsnMsgMap;
	}
	
	/**
	 * 사이렌오더 주문 상태 삭제
	 * @param orderNo
	 * @return
	 * @throws SQLException
	 */
	public void deleteOrderMsgM (String orderNo) throws SQLException {
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			xoSqlMap.startTransaction ();
			xoSqlMap.delete("orderMsg.deleteOrderMsgM", orderNo);
			xoSqlMap.commitTransaction();
		} catch ( SQLException e ) {
			throw e;
		} finally {
			try{
				xoSqlMap.endTransaction ();
			} catch (SQLException e){
				throw e;
			}
		}
	}
	
	/**
	 * 사이렌오더 주문 상태 저장
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public void insertOrderMsgM (Map<String, String> map) throws SQLException {
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try {
			xoSqlMap.startTransaction ();
			xoSqlMap.insert("orderMsg.insertOrderMsgM", map);
			xoSqlMap.commitTransaction();
		} catch ( SQLException e ) {
			throw e;
		} finally {
			try{
				xoSqlMap.endTransaction ();
			} catch (SQLException e){
				throw e;
			}
		}
	}
	
	// 사용자 nickname 조회
	public String getuserNickname(String userId) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		return (String) sqlMap.queryForObject("member.getUserNickname", userId);
	}
	
	/**
	 * 공통 메시지 문자 치환
	 * @param pushMsg
	 * @param waitCnt
	 * @param receiptOrderNo
	 * @return
	 */
	public String replaceDvsnMsg(String pushMsg, String waitCnt, String receiptOrderNo, String bdsName, String userNickname) {
		String dvsnMsg = pushMsg;
		// waitCnt 설정
		if(StringUtils.isNotBlank(waitCnt)) {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{waitCnt}", waitCnt);
		}else {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{waitCnt}", "");
		}
		// receiptOrderNo 설정
		if(StringUtils.isNotBlank(receiptOrderNo)) {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{receiptOrderNo}", receiptOrderNo);
		}else {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{receiptOrderNo}", "");
		}
		// bdsName 설정
		if(StringUtils.isNotBlank(bdsName)) {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{bdsName}", bdsName);
		}else {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{bdsName}", "");
		}
		// nickOrOrdNo 설정
		if(StringUtils.isNotBlank(userNickname)) {
			dvsnMsg = StringUtils.replace(dvsnMsg, "{nickOrOrdNo}", userNickname);
		}else {
			if(StringUtils.isNotBlank(receiptOrderNo)) {
				dvsnMsg = StringUtils.replace(dvsnMsg, "{nickOrOrdNo}", receiptOrderNo);
			}else {
				dvsnMsg = StringUtils.replace(dvsnMsg, "{nickOrOrdNo}", "");
			}
		}
		
		return dvsnMsg;
	}
}
