package co.kr.istarbucks.xo.batch.mgr;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownServiceException;
import java.util.Map;

public class EmpMgr {
    public static final String SUCCESS = "E000";
    private final String domain;
    private final Logger infoLogger;
//    private final Logger errorLogger;

//    public EmpMgr(Logger infoLogger, Logger errorLogger) {
    public EmpMgr(Logger infoLogger) {
        Configuration conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
        this.domain= conf.getString("xo.emp.domain");
        this.infoLogger = infoLogger;
//        this.errorLogger = errorLogger;
    }

    private Map<String, String> connect(final String api, final String logHeader) throws IOException {
        infoLogger.info(logHeader + "Connection-url=" + api);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(api);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000 * 2);
            connection.setConnectTimeout(1000 * 2);
            connection.setRequestMethod("POST");
            infoLogger.info(logHeader + "ResponseCode=" + connection.getResponseCode());
            /*  200 OK 가 아닌 경우 에러 응답을 받은 상태 */
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException("ResponseCode=" + connection.getResponseCode());
            }
            final String resultCode = connection.getHeaderField("resultCode");
            final String resultMessage = URLDecoder.decode(connection.getHeaderField("resultMessage"), "UTF-8");
            infoLogger.info(logHeader + "resultCode=" + resultCode + ", resultMessage=" + resultMessage);
            if (!SUCCESS.equals(resultCode)) {
                throw new IllegalStateException("resultCode=" + resultCode + ", resultMessage=" + resultMessage);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    sb.append(line);
                }
            } while (line != null);

            infoLogger.info(logHeader + "ResponseBody=" + sb.toString());
            if (sb.length() == 0) {
                throw new IllegalStateException("ResponseBody is empty.");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String temp;

            /* escape 처리된 문자열 제거    */
            if (sb.toString().startsWith("\"")) {
                /*  \"{\"result\": \"some-value\"}\"    =>      {"result":"some-value"}      */
                temp = sb.toString().substring(1, sb.toString().length() - 1);
                temp = temp.replaceAll("\\\\", "");
            }
            else {
                /* "{\"result\": \"some-value\"}\"    =>      {"result":"some-value"}      */
                temp = sb.toString();
                temp = temp.replaceAll("\\\\", "");
            }
            return objectMapper.readValue(temp, new TypeReference<Map<String, String>>() {});
        }
        catch (UnknownServiceException o_O) {
            infoLogger.error("[Protocol does not support input]", o_O);
            throw o_O;
        }
        catch (ProtocolException o_O) {
            infoLogger.error("URL [" + api + "] does not support POST method", o_O);
            throw o_O;
        }
        catch (IOException o_O) {
            infoLogger.error("[I/O error occurs while creating input/output stream]", o_O);
            throw o_O;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 임직원 할인 취소 요청
     * @param orderNo   주문 번호
     * @param empNo     임직원 번호
     * @param logHeader 로그 헤더
     */
    public Map<String, String> cancel(final String orderNo, final String empNo, final String logHeader) throws Exception {
        final String api = this.domain.concat("/emp/dcLimitUseCancel.do")
                .concat("?orderNo=").concat(orderNo)
                .concat("&empNo=").concat(empNo);
        return connect(api, logHeader);
    }

    /**
     * 임직원 할인 취소 롤백 요청
     * @param orderNo   주문 번호
     * @param empNo     임직원 번호
     * @param logHeader 로그 헤더
     */
    public Map<String, String> rollback(final String orderNo, final String empNo, final String logHeader) throws Exception {
        final String api = this.domain.concat("/emp/dcLimitUse.do")
                .concat("?orderNo=").concat(orderNo)
                .concat("&empNo=").concat(empNo);
        return connect(api, logHeader);
    }
}
