package co.kr.cafego.core.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class YesNoBooleanTypeHandler extends BaseTypeHandler<Boolean>{
	
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
			throws SQLException{
		ps.setString(i, convert(parameter));
	}

	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return convert(rs.getString(columnName));
	}

	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return convert(rs.getString(columnIndex));
	}

	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return convert(cs.getString(columnIndex));
	}

	private String convert(Boolean b) {
		if (b == null) {
			return null;
		}

		return b.booleanValue() ? "Y" : "N";
	}

	private Boolean convert(String s) {
		if ((!StringUtils.equals(s, "Y")) && (!StringUtils.equals(s, "1")) && 
				(!StringUtils.equalsIgnoreCase(s, "true")) && (!StringUtils.equals(s, "T")))
			return Boolean.valueOf(false);
		return Boolean.valueOf(true);
	}
}
