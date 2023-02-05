package com.acimage.admin.config.mybatis.typehandler;


import com.acimage.common.global.enums.MatchRule;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//@MappedTypes(MatchRule.class)
public class MatchRuleTypeHandler extends BaseTypeHandler<MatchRule> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MatchRule matchRule, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, matchRule.getKey());
    }

    @Override
    public MatchRule getNullableResult(ResultSet resultSet, String column) throws SQLException {
        return MatchRule.from(resultSet.getInt(column));
    }

    @Override
    public MatchRule getNullableResult(ResultSet resultSet, int i) throws SQLException {
       return MatchRule.from(Integer.parseInt(resultSet.getString(i)));
    }

    @Override
    public MatchRule getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return MatchRule.from(Integer.parseInt(callableStatement.getString(i)));
    }

}
