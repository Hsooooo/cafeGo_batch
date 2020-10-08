package co.kr.istarbucks.xo.batch.ss;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ExternalGatewayDao {
	private final static Logger LOGGER = Logger.getLogger("samsungAutoPushSender");
	
	/**
     * 전용망 PUSH 전송
     * @param orderNo (사이렌오더 주문번호)
     * @param contents (PUSH 메시지)
     * @param msgTpCd (PUSH 메시지유형)
     * @return
     */
    public Map<String, Object> sendExternalPush(String orderNo, String contents, String msgTpCd) {
        Map<String, Object> resultMap = null;
        
        // 1. 전문용 TRANSACTION_ID 생성
        String transactionId = this.nextTransactionId();
		
		if (StringUtils.isBlank(transactionId)) {
			resultMap = new HashMap<String, Object>();
        	resultMap.put("result_code", "998");
        	resultMap.put("result_msg",  "TRANSACTION_ID 생성오류");
        	
        	return resultMap;
		}
        
		// 2. 전용망 PUSH 전문 생성
   		final byte[] message  = this.createS600(transactionId, orderNo, contents, msgTpCd);
   		
   		// 3. 전용망 PUSH 전문 전송
   		final byte[] response = this.sendToPrivate(message);
   		
   		if (response == null) {
   			resultMap = new HashMap<String, Object>();
   			resultMap.put("transaction_id", transactionId);
        	resultMap.put("result_code", 	"997");
        	resultMap.put("result_msg",  	"PUSH 전송_응답 오류");
   		} else {
   			// 4. PUSH 응답 전문 파싱
   			resultMap = this.parseS600(orderNo, response);
   		}
        
        return resultMap;
    }
    

    /**
     * 삼성전용망 PUSH 전문 생성 	<br>
     *    -전문번호 : S600			<br>
     *    -요청전문 DB 저장 포함 (SQLException 발생 무시) 
     * @param transactionId (트랜잭션 ID)
     * @param orderNo (사이렌오더 주문번호)
     * @param contents (PUSH 내용)
     * @param msgTpCd (PUSH 메시지유형)
     * @return
     */
    public byte[] createS600(String transactionId, String orderNo, String contents, String msgTpCd) {
        final StringBuilder sb = new StringBuilder(2048);
        final Date now = new Date();
        final String msgId = "S600";
        byte[] message;

        // msg_id
        message = msgId.getBytes();
        this.mergeLog(sb, "MSR_ID="+msgId+"("+msgId.length()+")|");
        
        // send_date
        final String sendDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(now);
        message = XOUtilSS.addArray(message, sendDate.getBytes());
        this.mergeLog(sb, "SEND_DATE="+sendDate+"("+sendDate.length()+")|");
        
        // send_time
        final String sendTime = new SimpleDateFormat("HHmmss", Locale.KOREA).format(now);
        message = XOUtilSS.addArray(message, sendTime.getBytes());
        this.mergeLog(sb, "SEND_TIME="+sendTime+"("+sendTime.length()+")|");
        
        // transaction_id
        message = XOUtilSS.addArray(message, transactionId.getBytes());
        this.mergeLog(sb, "TRANSACTION_ID="+transactionId+"("+transactionId.length()+")|");
        
        // order_no
        message = XOUtilSS.addArray(message, orderNo.getBytes());
        this.mergeLog(sb, "ORDER_NO="+orderNo+"("+orderNo.length()+")|");
        
        // contents
        final String encContents = StringUtils.rightPad(this.encode(contents), 1350, StringUtils.EMPTY);
        message = XOUtilSS.addArray(message, encContents.getBytes());
        this.mergeLog(sb, "CONTENTS="+contents+"/"+encContents+"("+encContents.length()+")|");
        
        // msgTpCd
        final String pushMsgTyCd = StringUtils.rightPad(msgTpCd, 5, StringUtils.EMPTY);
        message = XOUtilSS.addArray(message, pushMsgTyCd.getBytes());
        this.mergeLog(sb, "MSG_TP_CD="+pushMsgTyCd+"("+pushMsgTyCd.length()+")|");

        // etc
        final String etc = StringUtils.leftPad(StringUtils.EMPTY, 200, StringUtils.EMPTY);
        message = XOUtilSS.addArray(message, etc.getBytes());
        this.mergeLog(sb, "ETC="+etc+"("+etc.length()+")");

        LOGGER.info("[S600/REQ/"+orderNo.replaceAll("\n|\r","")+"]:" + sb.toString().replaceAll("\n|\r",""));
        
        // 요청전문 DB 저장
        try {
        	Map<String, Object> dbMap = new HashMap<String, Object>();
        	dbMap.put("xtrnl_trnsc_id",			transactionId);
        	dbMap.put("sror_order_no",  		orderNo);
        	dbMap.put("trnss_date", 			sendDate);
        	dbMap.put("trnss_hms", 				sendTime);
        	dbMap.put("cntnt",  				contents);
        	dbMap.put("xtrnl_push_type_code",	msgTpCd);
        	
        	this.insertExternalPushInfo(dbMap);
        } catch(SQLException sqlEx) {
        	LOGGER.error("[S600/REQ/"+orderNo.replaceAll("\n|\r","")+"] Insert_Ex => " + sqlEx.getMessage().replaceAll("\n|\r",""));
        }
        
        return this.appendMessageLength(message);
    }
    
    
    /**
     * 삼성전용망 PUSH 전문 응답 	<br>
     *    -전문번호 : S600			<br>
     *    -응답전문 DB 업데이트 포함 (SQLException 발생 무시) 
     * @param orderNo
     * @param response
     * @return
     */
    private Map<String, Object> parseS600(final String orderNo, final byte[] response) {
    	final Map<String, Object> result = new HashMap<String, Object>();
        int offset 	 = 0;
        String value = "";
        
        try {
        	value = this.bytesToString(response);
        } catch(UnsupportedEncodingException UnsEx) {
        	result.put("result_code", "996");
            result.put("result_msg",  "응답전문 파싱 오류");
        }
        
        if (StringUtils.isNotBlank(value)) {
        	String msgLen 		 = StringUtils.substring(value, offset, offset += 4);
        	String msgId 		 = StringUtils.substring(value, offset, offset += 4);
        	String sendDate 	 = StringUtils.substring(value, offset, offset += 8);
        	String sendTime 	 = StringUtils.substring(value, offset, offset += 6);
        	String transactionId = StringUtils.substring(value, offset, offset += 12);
        	String resultCode 	 = StringUtils.substring(value, offset, offset += 4);
        	String resultMsg 	 = StringUtils.substring(value, offset, offset += 700);
        	resultMsg = StringUtils.trim(this.decode(resultMsg));
        	
        	result.put("msg_len",           msgLen);
        	result.put("msg_id",            msgId);
        	result.put("send_date",         sendDate);
        	result.put("send_time",         sendTime);
        	result.put("transaction_id",	transactionId);
        	result.put("result_code",       resultCode);
        	result.put("result_msg",        resultMsg);
        	
        	LOGGER.info("[S600/RES/"+orderNo.replaceAll("\n|\r","")+"]:" + result.toString().replaceAll("\n|\r",""));
        	
        	// 응답전문 DB 업데이트
        	try {
        		Map<String, Object> dbMap = new HashMap<String, Object>();
        		dbMap.put("xtrnl_trnsc_id",	result.get("transaction_id"));
        		dbMap.put("rspns_code",     result.get("result_code"));
        		dbMap.put("rspns_cntnt",  	result.get("result_msg"));
        		
        		this.updateExternalPushInfo(dbMap);
        	} catch(SQLException sqlEx) {
        		LOGGER.error("[S600/RES/"+orderNo.replaceAll("\n|\r","")+"] Update_Ex => " + sqlEx.getMessage().replaceAll("\n|\r",""));
        	}
        }
        
        return result;
    }
    
    
    /**
     * 트랜잭션 ID 생성
     * @return
     */
    private String nextTransactionId() {
    	String transactionId = "";
    	
    	try {
    		SqlMapClient msrSqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
    		transactionId = (String)msrSqlMap.queryForObject ("openPush.getTransactionId");
    	} catch(SQLException sqlEx) {
    		LOGGER.error("sqlEx => " + sqlEx.getMessage().replaceAll("\n|\r",""));
    	}
    	
    	return transactionId;
    }
    
    /**
     * 전용망 PUSH 이력 등록
     * @param dbMap
     * @throws SQLException
     */
    private void insertExternalPushInfo(Map<String, Object> dbMap) throws SQLException {
    	SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	
		try{
			xoSqlMap.startTransaction ();
			xoSqlMap.insert("autoPushSS.insertExternalPushHistory", dbMap);
			xoSqlMap.commitTransaction();
		} catch (SQLException sqlEx){
			throw sqlEx;
		} finally {
			xoSqlMap.endTransaction();
		}
    }
    
    /**
     * 전용망 PUSH 이력 수정
     * @param dbMap
     * @throws SQLException
     */
    private void updateExternalPushInfo(Map<String, Object> dbMap) throws SQLException {
    	SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance();
    	
		try{
			xoSqlMap.startTransaction ();
			xoSqlMap.update("autoPushSS.updateExternalPushHistory", dbMap);
			xoSqlMap.commitTransaction();
		} catch (SQLException sqlEx){
			throw sqlEx;
		} finally {
			xoSqlMap.endTransaction();
		}
    }
    
    /**
     * 전용망 전문 전송
     * @param message (전문 메시지)
     * @return
     */
    private byte[] sendToPrivate(final byte[] message) {
        final String host = XOUtilSS.getPropertiesString("samsung.private.host");
        final int port 	  = XOUtilSS.getPropertiesInt("samsung.private.port", 19729);
        return this.send(host, port, message);
    }
    
    /**
     * 최종 전문 생성 (총전문길이 + S600)
     * @param message
     * @return
     */
    private byte[] appendMessageLength(final byte[] message) {
        final int length = message.length + 4;
        final String msg_len = StringUtils.leftPad(String.valueOf(length), 4, '0');
        byte[] result = msg_len.getBytes();
        result = XOUtilSS.addArray(result, message);
        
        return result;
    }
    
    /**
     * byte -> string 변환
     * @param bytes
     * @return
     */
    private String bytesToString(final byte[] bytes) throws UnsupportedEncodingException {
        try {
            return new String(bytes, "UTF-8");
        } catch(UnsupportedEncodingException ex) {
        	LOGGER.error("bytesToStringEx => " + ex.getMessage().replaceAll("\n|\r",""));
            throw ex;
        }
    }
    
    /**
     * 문자열을 UTF-8로 Encoding 처리
     * @param txt
     * @return URLEncoder.encode(txt, "UTF-8")
     */
    private String encode(final String txt) {
    	String encStr = "";
    	
    	try {
    		encStr = URLEncoder.encode(txt, "UTF-8");
    	} catch(UnsupportedEncodingException ex) {
    		LOGGER.error("EncodingEx => " + ex.getMessage().replaceAll("\n|\r",""));
    	}
    	
    	return encStr;
    }
    
    /**
     * 문자열을 UTF-8로 Decoding 처리
     * @param txt
     * @return URLDecoder.decode(txt, "UTF-8")
     */
    private String decode(final String txt) {
    	String decStr = "";
    	
    	try {
    		decStr = URLDecoder.decode(txt, "UTF-8");
    	} catch(UnsupportedEncodingException ex) {
    		LOGGER.error("DecodingEx => " + ex.getMessage().replaceAll("\n|\r",""));
    	}
    	
    	return decStr;
    }

    
    /**
     * 전용망 데이터 전송 처리
     * @param host
     * @param port
     * @param message
     * @return
     */
    private byte[] send(final String host, final int port, final byte[] message) {
        SocketChannel socketChannel = null;
        byte[] result = new byte[]{};
        InputStream inStream = null;
		ReadableByteChannel wrappedChannel = null;
        
		final int bufSize 	= 1024 * 32;
		ByteBuffer writeBuf = ByteBuffer.allocate(message.length);
		ByteBuffer readBuf 	= ByteBuffer.allocate(bufSize);
		
		final int connTimeout = XOUtilSS.getPropertiesInt("samsung.private.conn.timeout", 3000);
        final int readTimeout = XOUtilSS.getPropertiesInt("samsung.private.read.timeout", 10000);
        
        try {
            socketChannel = SocketChannel.open();
            socketChannel.socket().connect(new InetSocketAddress(host, port), connTimeout);
            socketChannel.configureBlocking(true);

            //***** write *****//
            writeBuf.put(message);
            writeBuf.flip();
            while (writeBuf.hasRemaining()) {
            	socketChannel.write(writeBuf);
            }

            //***** read *****//
            socketChannel.socket().setSoTimeout(readTimeout); 
            inStream = socketChannel.socket().getInputStream();
			wrappedChannel = Channels.newChannel(inStream);
            
            int read;
            while (true) {
            	read = wrappedChannel.read(readBuf);
                
                if (read == 0) {
                    continue;
                }

                /* Connection closed. end-of-stream */
                if (read == -1) {
                    break;
                } else {
                    byte[] temp = new byte[read];
                    System.arraycopy(readBuf.array(), 0, temp, 0, read);
                    result = temp;
                    if (read < bufSize) {
                        break;
                    }
                }
            }
        } catch(SocketTimeoutException socTimeEx) {
        	LOGGER.error("ExternalGateWay SocketTimeoutException => " + socTimeEx.getMessage().replaceAll("\n|\r",""));
        	result = null;
        } catch(SocketException socEx) {
        	LOGGER.error("ExternalGateWay SocketException => " + socEx.getMessage().replaceAll("\n|\r",""));
        	result = null;
        } catch(IOException ioEx) {
        	LOGGER.error("ExternalGateWay IOException => " + ioEx.getMessage().replaceAll("\n|\r",""));
        	result = null;
        } finally {
        	this.socketClose(socketChannel, inStream, wrappedChannel, writeBuf, readBuf);
        }
        
        return result;
    }
    
    
    private void socketClose(SocketChannel socketChannel, InputStream inStream, ReadableByteChannel wrappedChannel, ByteBuffer writeBuf, ByteBuffer readBuf) {
    	// socketChannel close
    	if (socketChannel != null) {
        	try {
                if (socketChannel.socket().isConnected()) {
                    socketChannel.close();
                }
                if (socketChannel.isConnected()) {
                    socketChannel.close();
                }
            } catch(IOException ioEx) {
            	LOGGER.error("socketChannel.close IOException => " + ioEx.getMessage().replaceAll("\n|\r",""));
            }
    	}
        
    	// writeBuf clear
        if (writeBuf != null) {
        	writeBuf.clear();
        }
        // readBuf clear
        if (readBuf != null) {
        	readBuf.clear();
        }
         
        // inStream close
        if (inStream != null) {
        	try {
            	inStream.close();
        	} catch(IOException ioEx) {
        		LOGGER.error("inStream.close IOException => " + ioEx.getMessage().replaceAll("\n|\r",""));
        	}
        }
        
        // wrappedChannel close
        if (wrappedChannel != null) {
        	try {
        		wrappedChannel.close();
        	} catch(IOException ioEx) {
        		LOGGER.error("wrappedChannel.close IOException => " + ioEx.getMessage().replaceAll("\n|\r",""));
        	}
        }
    }
    
    /* non-business logic */
    private StringBuilder mergeLog(StringBuilder sb, Object obj) {
    	if (obj != null) {
    		sb.append(obj.toString());
    	}
    	return sb;
    }
	/* non-business logic */
}
