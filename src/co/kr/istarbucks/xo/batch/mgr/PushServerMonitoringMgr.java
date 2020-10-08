package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushServerMonitoringDto;
import co.kr.istarbucks.xo.batch.dao.OrderAutoPushDao;
import co.kr.istarbucks.xo.batch.dao.PushServerMonitoringDao;

public class PushServerMonitoringMgr {
	private final PushServerMonitoringDao PushServerMonitoringDao;
	
	public PushServerMonitoringMgr() {
		PushServerMonitoringDao  = new PushServerMonitoringDao();
	}

	public List<PushServerMonitoringDto> getRepinfo() throws SQLException {
		return PushServerMonitoringDao.getRepinfo();
	}

	public PushHistoryDto sendPush(PushServerMonitoringDto recvInfo) {
		return PushServerMonitoringDao.sendPush(recvInfo);
	}

}
