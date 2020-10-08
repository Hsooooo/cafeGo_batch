package co.kr.istarbucks.xo.batch.common.mgift;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MGiftHttpParser{
	private final Logger mLogger = Logger.getLogger("PC_MGIFT");
	
	
	public Logger getMLogger() {
		return mLogger;
	}

	/**
	 * result -> Map
	 * @param result
	 * @return
	 */
	public Map<String, String> getResultMapMoneyCon(String result, String couponNo) {
		Map<String, String> resultValue = null;
		String[] resultArray = null;
		String[] resultArrayValue = null;
		resultValue = new HashMap<String, String>();
		resultArray = result.split("&");
		for(int i = 0; i < resultArray.length; i++) {
			resultArrayValue = resultArray[i].split("=");
			if(resultArrayValue.length == 1) {
				resultValue.put(resultArrayValue[0].trim(), null);
			} else {
				if(StringUtils.equals("StatusCode", resultArrayValue[0])) {
					if(StringUtils.equals("000", resultArrayValue[1])) {
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_SUCCESS);
					} 
				} else if(StringUtils.equals("ErrorCode", resultArrayValue[0])) {
					if(StringUtils.equals("E0002", resultArrayValue[1])||StringUtils.equals("E0003", resultArrayValue[1])
							|| StringUtils.equals("E0004", resultArrayValue[1]) || StringUtils.equals("E0005", resultArrayValue[1])) {		
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
					} else if(StringUtils.equals("E0006", resultArrayValue[1])) {		
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_USED_ERROR);
					} else if(StringUtils.equals("E0007", resultArrayValue[1])) { 		
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_PERIOD_ERROR);
					} else if(StringUtils.equals("E0026", resultArrayValue[1]) || StringUtils.equals("E0027", resultArrayValue[1])
							|| StringUtils.equals("E0028", resultArrayValue[1]) || StringUtils.equals("E0029", resultArrayValue[1])
							|| StringUtils.equals("E0030", resultArrayValue[1])) { 		
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_PRICE_ERROR);
					} else if(!StringUtils.equals("E0000", resultArrayValue[1])) {		
						resultValue.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
					}
				} else if(StringUtils.equals("ErrorMessage", resultArrayValue[0])) {
					resultValue.put("RESULT_MESSAGE", resultArrayValue[1].trim());
				} else if(StringUtils.equals("ProductInfo", resultArrayValue[0])) {
					resultValue.put("COUPON_TYPE", resultArrayValue[1].trim());
				} else if(StringUtils.equals("AdmitNum", resultArrayValue[0])) {
					resultValue.put("APPROV_NO", resultArrayValue[1].trim());
				} else if(StringUtils.equals("ProductCode", resultArrayValue[0])) {
					if(NumberUtils.isNumber(resultArrayValue[1]) && StringUtils.isNotEmpty(resultArrayValue[1])) {
						resultValue.put("SKU_CODE", Long.toString(Long.parseLong(resultArrayValue[1])));
					} else {
						resultValue.put("SKU_CODE", resultArrayValue[1].trim());
					}
				} else if(StringUtils.equals("Balance", resultArrayValue[0])) {
					resultValue.put("BALANCE", resultArrayValue[1].trim());
				} else if(StringUtils.equals("ProductPrice", resultArrayValue[0])) {
					resultValue.put("PRICE", resultArrayValue[1].trim());
				} else if(StringUtils.equals("ProductCnt", resultArrayValue[0])) {
					resultValue.put("PRODUCT_CNT", resultArrayValue[1].trim());
				} else {
					resultValue.put(resultArrayValue[0].trim(), resultArrayValue[1].trim());
				}
				resultValue.put("COUPON_NUMBER", couponNo);
			}
		}
		return resultValue;
	}
	
	/**
	 * Json -> Map
	 * @param data
	 * @param type
	 * @return
	 */
	public Map<String, String> getResultMapGiftiShow(String data, String type, String couponNo) {
		Map<String, String> resultMap = new HashMap<String, String>();
		JsonParser jsonParser = new JsonParser();
		try {
			JsonObject jsonObject = (JsonObject)jsonParser.parse(data);
			String resCode = jsonObject.get("resCode").toString().replace("\"", "");
			String resMsg = jsonObject.get("resMsg").toString().replace("\"", "");
			
			if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
				if(StringUtils.equals("0000", resCode)) {
					String subData = jsonObject.get("posNetResVo").toString();
					JsonObject resultObject = (JsonObject) jsonParser.parse(subData);
					Set<Map.Entry<String, JsonElement>> entries = resultObject.entrySet();
					
					for(Map.Entry<String, JsonElement> entry : entries) {
						String key = entry.getKey();
						String value = entry.getValue().toString();
					
						if(StringUtils.equals("productId", key)) {
							String skuCode = value.replace("\"", "");
							if(NumberUtils.isNumber(skuCode) && StringUtils.isNotEmpty(skuCode)) {
								resultMap.put("SKU_CODE", Long.toString(Long.parseLong(skuCode)));
							} else {
								resultMap.put("SKU_CODE", skuCode);
							}
						} else if(StringUtils.equals("prcost",key)) {
							resultMap.put("PRICE",value.replace("\"", ""));
						}
						resultMap.put(key, value.replace("\"", ""));
					}
					resultMap.put("COUPON_NUMBER", couponNo);
					resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SUCCESS);
				} else {
					if(StringUtils.equals(resCode, "COUPONS.9037")) {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_USED_ERROR);
					} else if (StringUtils.equals(resCode, "COUPONS.0016")){
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_PERIOD_ERROR);
					} else if(StringUtils.equals(resCode, "COUPONS.9041")) {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
					} else {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
					}
					resultMap.put("RESULT_MESSAGE", resMsg);
				}
			} else {
				if("0000".equals(resCode)) {
					resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SUCCESS);
					resultMap.put("APPROV_NO", jsonObject.get("apprvNo").toString().replace("\"", ""));
					resultMap.put("COUPON_NUMBER", couponNo);
				} else {
					if(StringUtils.equals(resCode, "COUPONS.9037")) {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_USED_ERROR);
					} else if (StringUtils.equals(resCode, "COUPONS.0016")){
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_PERIOD_ERROR);
					} else if(StringUtils.equals(resCode, "COUPONS.9041")) {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
					} else {
						resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
					}
				}
			}
		} catch (Exception e) {
			mLogger.info("[Exception-getResultMapGiftiShow]["+couponNo.replaceAll("\r\n", "")+"]"+e.toString().replaceAll("\r\n", ""));
		}
		return resultMap;
	}
	
	public Map<String, String> getResultMapGiftiCon(String data, String type, String couponNo) {

		Map<String, String> resultMap = new HashMap<String, String>();
		JsonParser jsonParser = new JsonParser();
		try {
			JsonObject jsonObject = (JsonObject)jsonParser.parse(data);
			
			String resCode = jsonObject.get("ERROR_CODE").toString().replace("\"", "");
			
			if(StringUtils.equals(resCode, "0000")) {
				if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
					String subData = jsonObject.get("Sub1").toString().replace("[", "").replace("]", "");
					JsonObject resultObject = (JsonObject) jsonParser.parse(subData);
					Set<Map.Entry<String, JsonElement>> entries = resultObject.entrySet();
					int price = 0;
					
					for(Map.Entry<String, JsonElement> entry : entries) {
						String key = entry.getKey();
						String value = entry.getValue().toString();
						
						if(StringUtils.equals("OSIDE_GOODS_ID", key)) {
							String skuCode = value.replace("\"", "");
							if(NumberUtils.isNumber(skuCode)&&StringUtils.isNotEmpty(skuCode)) {
								resultMap.put("SKU_CODE", Long.toString(Long.parseLong(skuCode)));
							} else {
								resultMap.put("SKU_CODE", skuCode);
							}
						} else if(StringUtils.equals("COST", key)) {
							price += Integer.parseInt(value.replace("\"", ""));
						} else if(StringUtils.equals("EXCHANGE_CHARGE", key)) {
							price += Integer.parseInt(value.replace("\"", ""));
						} else if(StringUtils.equals("PRODUCT_KIND", key)) {
							resultMap.put("COUPON_TYPE", value.replace("\"", ""));
						}
					}
					resultMap.put("COUPON_NUMBER", couponNo);
					resultMap.put("PRICE", Integer.toString(price));
				} else if(StringUtils.equals(MGiftCode.SEND_USE, type) || StringUtils.equals(MGiftCode.SEND_CANCEL, type)) {
					resultMap.put("APPROV_NO", jsonObject.get("APPROVAL_NUMBER").toString().replace("\"", ""));
					resultMap.put("COUPON_NUMBER", couponNo);
				} 
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SUCCESS);
			} else if(StringUtils.equals(resCode,"3111") || StringUtils.equals(resCode,"3112") || StringUtils.equals(resCode,"3113") || StringUtils.equals(resCode,"3114")) {
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
			} else if(StringUtils.equals(resCode,"3115") || StringUtils.equals(resCode,"3121") || StringUtils.equals(resCode,"3132")) {
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_USED_ERROR);
			} else if(StringUtils.equals(resCode,"3128") || StringUtils.equals(resCode,"3131")) {
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_PRICE_ERROR);
			} else if(StringUtils.equals(resCode, "3116")) {
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_PERIOD_ERROR);
			} else {
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
			}
		} catch (Exception e) {
			mLogger.info("[Exception-getResultMapGiftiCon]["+couponNo.replaceAll("\r\n", "")+"]"+e.toString().replaceAll("\r\n", ""));
		}
		return resultMap;
	}
	
	/**
	 * XML -> Map
	 * @param data
	 * @return
	 */
	public Map<String, String> getResultMapCoup(String data, String couponNo) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
			Document doc = documentBuilder.parse(is);
			Element element = doc.getDocumentElement();
			NodeList nodeList = element.getChildNodes();
			Node node;
			mLogger.info("data : "+data);
			for(int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					if(node.getFirstChild() != null) {
						if(StringUtils.equals("RESULTCODE",node.getNodeName().trim())) {
							if(StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "00")) {
								resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SUCCESS);
							} else if(StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "01") || StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "02")
									|| StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "06")) {
								resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
							} else if(StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "05")) {
								resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_USED_ERROR);
							} else if(StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "04")) {
								resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_PERIOD_ERROR);
							} else if(!StringUtils.equals(node.getFirstChild().getNodeValue().trim(), "00")) {
								resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
							} 
						} else if(StringUtils.equals("RESULTMSG",node.getNodeName().trim())) {
							resultMap.put("RESULT_MESSAGE", node.getFirstChild().getNodeValue());
						} else if(StringUtils.equals("PRODUCT_CODE",node.getNodeName().trim())) {
							if(NumberUtils.isNumber(node.getFirstChild().getNodeValue()) && StringUtils.isNotEmpty(node.getFirstChild().getNodeValue())) {
								resultMap.put("SKU_CODE", Long.toString(Long.parseLong(node.getFirstChild().getNodeValue())));
							} else {
								resultMap.put("SKU_CODE", node.getFirstChild().getNodeValue());
							}
						} else if(StringUtils.equals("END_DAY", node.getNodeName().trim())) {
							resultMap.put("EXPIRE_DATE", node.getFirstChild().getNodeValue());
						} else if(StringUtils.equals("BAL_PRICE", node.getNodeName().trim())) {
							resultMap.put("PRICE", node.getFirstChild().getNodeValue());
						} else if(StringUtils.equals("AUTH_CODE", node.getNodeName().trim())) {
							resultMap.put("APPROV_NO", node.getFirstChild().getNodeValue());
						} else {
							resultMap.put(node.getNodeName().trim(), node.getFirstChild().getNodeValue());
						}
					} else {
						resultMap.put(node.getNodeName().trim(), "");
					}
				}
			}
			resultMap.put("COUPON_NUMBER", couponNo);
		} catch (Exception e) {
			mLogger.info("[Exception-getResultMapCoop]["+couponNo.replaceAll("\r\n", "")+"]"+e.toString().replaceAll("\r\n", ""));
		} 	  
		return resultMap;
	}
}
