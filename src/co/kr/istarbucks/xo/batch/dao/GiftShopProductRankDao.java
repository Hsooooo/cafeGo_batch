package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class GiftShopProductRankDao {

	/**
	 * ��������� ��ü ��ǰ�� �Ǹ�(����)������ ������ �����Ѵ�.
	 * @return
	 * @throws SQLException
	 */
	public int createProductRanking() throws SQLException {
		
		int insCnt = 0;
		
		SqlMapClient sqlMap = null;
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
			sqlMap.startTransaction();
			
			sqlMap.update("giftshop.updatePreviousRankNewsestFlag");
			
			sqlMap.delete("giftshop.deletePreviousRankData");
			
			insCnt = sqlMap.update("giftshop.createRankData");
			
			sqlMap.commitTransaction();
		} catch (SQLException e) {
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
		
		return insCnt;
	}
	
	/**
	 * ��������� �Ż�ǰ�� �Ǹ�(����)������ �����Ѵ�.
	 * @return
	 * @throws SQLException
	 */
	public int createProductNewest() throws SQLException {
		int insCnt = 0;
		
		SqlMapClient sqlMap = null;
		try {
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
			sqlMap.startTransaction();
			
			sqlMap.update("giftshop.updatePreviousNewNewsestFlag");
			
			sqlMap.delete("giftshop.deletePreviousNewData");
			
			insCnt = sqlMap.update("giftshop.createNewProductData");
			
			sqlMap.commitTransaction();
		} catch (SQLException e) {
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
		
		return insCnt;
	}

}
