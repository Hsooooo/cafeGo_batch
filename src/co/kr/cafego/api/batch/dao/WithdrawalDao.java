package co.kr.cafego.api.batch.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.cafego.api.batch.common.dto.WithdrawalMemberDto;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

public class WithdrawalDao {
	
	/**
	 * Ż�� �����Ⱓ �ʰ� ȸ������ ��ȸ
	 * @param date
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<WithdrawalMemberDto> getWithdrawalMemberList(String date, Map<String, Object> dbMap) throws SQLException{
		String sqlId = "withdrawal.getWithdrawalMemberList";
		SqlMapClient sqlMap = IBatisSqlConfig.getSqlMapInstance();
		return (List<WithdrawalMemberDto>)sqlMap.queryForList(sqlId, dbMap);
	}
}
