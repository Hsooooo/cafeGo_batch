package co.kr.cafego.batch.withdrawal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.batch.common.dto.WithdrawalMemberDto;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.DateTime;
import co.kr.cafego.core.support.ApiSupport;

@Service
public class WithdrawalService extends ApiSupport {
	
	private final String TODAY = DateTime.getCurrentDate(1);		//yyyyMMddHHmm 
	
	@Autowired
	private WithdrawalMapper withdrawalMapper;
	
	
	//cron - 매일 새벽 1시 0분에 실행
	@Scheduled(cron="* * * * * *")
	@Transactional(value="transactionManager", rollbackFor= {ApiException.class, Exception.class})
	public void withdrawalBatch() {
		logger.info("##################[Withdrawal Batch Start!!!!]["+TODAY+"]#####################################");
		try {
			int targetCnt 		 = 0;		// 배치 대상 회원수
			int memberInfoDelCnt = 0;		// (MEMBER_INFO) 삭제 건수
			int pointInfoDelCnt  = 0;		// (MEMBER_POINT) 삭제 건수
			
			String expireDate = DateTime.getBeforeDate(7);
			logger.info(expireDate);
			List<WithdrawalMemberDto> expireWdList = withdrawalMapper.getExpireWdMemberList(expireDate);
			expireWdList = expireWdList == null ? new ArrayList<WithdrawalMemberDto>() : expireWdList;
			
			targetCnt = expireWdList.size();
			logger.info("[삭제 대상 회원 수] : " + targetCnt);
			
			if(targetCnt != 0) {
				int delMemInfoCnt = withdrawalMapper.delMemberInfo(expireWdList);
				logger.info("[회원정보 삭제 수 ] : " + delMemInfoCnt);
				
				int delPointInfoCnt = withdrawalMapper.delPointInfo(expireWdList);
				logger.info("[포인트 정보 삭제 수] : " + delPointInfoCnt);
			}
		}catch(ApiException ae) {
			
		}catch(Exception e) {
			logger.error("Error", e);
		}
	}
}
