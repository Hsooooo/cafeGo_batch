package co.kr.istarbucks.xo.batch.ss;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SamsungAutoPushDao {
	private final Logger logger      = Logger.getLogger("samsungAutoPushSender");
	private final Configuration conf = CommPropertiesConfiguration.getConfiguration ("xoBatchSS.properties");
	
	//�ڵ� Ǫ�� ����Ʈ ��ȸ 
	@SuppressWarnings("unchecked")
	public List<OrderAutoPushDto> getOrderAutoPush(Map<String, String> paramMap) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (List<OrderAutoPushDto>) sqlMap.queryForList("autoPushSS.getSamsungOrderAutoPush",paramMap);
	}

	//�ֹ� ���� ����ȸ
	public String getOrderStatus(OrderAutoPushDto recvInfo) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_no", recvInfo.getOrder_no());
		paramMap.put("bds_no"  , recvInfo.getBds_no());
		
		return (String) sqlMap.queryForObject("autoPushSS.getOrderStatus",paramMap);
	}
	
	//�ֹ� ���� ��ȸ
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrderInfo(Map<String, String> paramMap) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Map<String, String>) sqlMap.queryForObject("autoPushSS.getOrderInfo", paramMap);
	}

	//Ǫ��Send
	public PushHistoryDto sendPush(OrderAutoPushDto recvInfo) {
		StringBuffer buf = new StringBuffer ();
		PushHistoryDto pushDto = new PushHistoryDto();
		String pushCharset = conf.getString("auto.push.charset");

		String pushMsg = "";
		String pushCnt = recvInfo.getPush_cnt();
		String screenId = conf.getString("auto.push.screenid");
		String msgType = recvInfo.getMsg_type();
		String bdsName = recvInfo.getBds_name();

		if(StringUtils.isNotBlank(bdsName)) {
			bdsName += " ";
		}
		
		// Push API ������� �������� �߰���(2017-04-07)
		String msgTpCd      = "";
		String priorityFlag = "";
		
		if(pushCnt.equals("1")) {
			if(StringUtils.equals("B", msgType)) {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtilSS.getPropertiesString ("auto.push.messageB"), bdsName, bdsName)).toString();
			} else {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtilSS.getPropertiesString ("auto.push.message1"), bdsName)).toString();
			}
			
			// Push API ������� �������� �߰���(2017-04-07)
			msgTpCd      = conf.getString("auto.push.msgtpcd1");
			priorityFlag = conf.getString("auto.push.priorityflag1");
			
		} else if (pushCnt.equals("2")) {
			if(StringUtils.equals("B", msgType)) {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtilSS.getPropertiesString ("auto.push.messageB"), bdsName, bdsName)).toString();
			} else {
				pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtilSS.getPropertiesString ("auto.push.message2"), bdsName)).toString();
			}
			
			// Push API ������� �������� �߰���(2017-04-07)
			msgTpCd      = conf.getString("auto.push.msgtpcd2");
			priorityFlag = conf.getString("auto.push.priorityflag2");
		
		//�Ｚ��ī���� ��� ����° PUSH �޽����� SMS �޽����� �����ϰ� �����Ͽ� �߼�
		} else if (pushCnt.equals("3")) {
			Map<String, String> orderMap = null;
			
			try {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("order_no", recvInfo.getOrder_no());
				paramMap.put("bds_no"  , recvInfo.getBds_no());
				
				orderMap = this.getOrderInfo(paramMap);
			} catch(SQLException sqlEx) {
				logger.error(sqlEx.getMessage());
			}
			
			String smsStoreName = StringUtils.defaultIfEmpty(orderMap.get("storeName"), "");
			String smsBdsName   = StringUtils.defaultIfEmpty(orderMap.get("bdsName"), "");
				
			pushMsg = buf.delete (0, buf.length ()).append(String.format(XOUtilSS.getPropertiesString ("autopush.sms.message"), smsStoreName, smsBdsName, recvInfo.getReceipt_order_no())).toString();
			msgTpCd = conf.getString("auto.push.msgtpcdSms1");
		}
		
		String pushType = conf.getString("push.type.mp");
		
		pushDto.setPush_message(pushMsg); // PUSH �޼���
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
		
		try {
			//msg = URLEncoder.encode (pushMsg, pushCharset);
			msg = pushMsg;
			userId = URLEncoder.encode (pushDto.getUser_id(), pushCharset);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace ();
		}
		
		// �ֹ���ȣ ��ȸ
		String orderNo = StringUtils.defaultIfEmpty(pushDto.getParam(), "");
				
		// POS �ֹ� PUSH�� ���, �ֹ���ȣ�� 1(�����Ǹ޴��ڵ�) �Ǵ� 2(īƮ�ڵ�)���� ������ ��� CALL PUSH �߼� �� PUSH_VALUE ����
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
	

		// ����� PUSH
		pushDto.setPush_message(msg);
		pushDto = this.sendExternalPush(pushDto, msgTpCd);
		
		int push = Integer.parseInt(pushCnt);
		push = push + 1;
		
		logger.info("order_no : " + recvInfo.getOrder_no() + ", push_cnt : " + push + ", user_id : " + recvInfo.getUser_id() + ", result_code : " + pushDto.getResult_code());
		return pushDto;
	}

	
	/**
	 * ����� PUSH ����
	 * @param pushDto
	 * @param msg
	 * @param conf
	 * @return
	 */
	public PushHistoryDto sendExternalPush(PushHistoryDto pushDto, String msgTpCd) {
		Map<String, Object> returnMap = null;
		
		try {
			String contents = pushDto.getPush_message();
			
			returnMap = new ExternalGatewayDao().sendExternalPush(pushDto.getOrder_no(), contents, msgTpCd);
		} catch(Exception ex) {
			logger.error("[�����PUSH/"+pushDto.getOrder_no()+"] ex => " + ex.getMessage());
			
			returnMap = new HashMap<String, Object>();
			returnMap.put("result_code", "999");
			returnMap.put("result_msg",  ex.getMessage());
		}

		// result_code, result_msg ����
		pushDto.setResult_code((String)returnMap.get("result_code"));
		pushDto.setResult_msg((String)returnMap.get("result_msg"));
		
		return pushDto;
	}
	

	//�ڵ�Ǫ�� �߼�Ƚ�� update
	public void updateOrderAutoPush(OrderAutoPushDto recvInfo) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			sqlMap.startTransaction ();
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_no", recvInfo.getOrder_no());
			paramMap.put("bds_no"  , recvInfo.getBds_no());
			
			sqlMap.update("autoPushSS.updateOrderAutoPush",paramMap);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}	

	//Ǫ�� �����丮 ���
	public void insertPushHistory(PushHistoryDto pushDto) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			sqlMap.startTransaction ();
			sqlMap.insert("autoPushSS.insertPushHistory",pushDto);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}

	//�����忡 ���� �������ö� ���°� ����
	public void updateThreadInputYn(OrderAutoPushDto recvInfo, String input_yn) throws Exception{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		try{
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_no", recvInfo.getOrder_no());
			paramMap.put("bds_no"  , recvInfo.getBds_no());
			paramMap.put("input_yn", input_yn);
			
			sqlMap.startTransaction ();
			sqlMap.insert("autoPushSS.updateThreadInputYn",paramMap);
			sqlMap.commitTransaction();
		} catch (Exception e){
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}
}
