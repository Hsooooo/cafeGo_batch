package co.kr.cafego.batch.withdrawal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.kr.cafego.batch.common.dto.WithdrawalMemberDto;
import co.kr.cafego.common.util.DateTime;
import co.kr.cafego.core.support.ApiSupport;

@Service
public class WithdrawalService extends ApiSupport {
	
	private final Logger logger = LoggerFactory.getLogger("BATCH");
	
	private final String TODAY = DateTime.getCurrentDate(1);		//yyyyMMddHHmm 
	
	@Autowired
	private WithdrawalMapper withdrawalMapper;
	
	@Scheduled(cron="*/1 * * * * *")
	public void withdrawalBatch() {
		logger.info("##################[Withdrawal Batch Start!!!!]["+TODAY+"]#####################################");
		
		int targetCnt 		 = 0;		// 배치 대상 회원수
		int memberInfoDelCnt = 0;		// (MEMBER_INFO) 삭제 건수
		int pointInfoDelCnt  = 0;		// (MEMBER_POINT) 삭제 건수
		
		String expireDate = DateTime.getBeforeDate(7);
		logger.info(expireDate);
		List<WithdrawalMemberDto> expireWdList = withdrawalMapper.getExpireWdMemberList(expireDate);
		targetCnt = expireWdList.size();
		
		logger.info("[삭제 대상 회원 수] : " + targetCnt);
		
		
	}
}
