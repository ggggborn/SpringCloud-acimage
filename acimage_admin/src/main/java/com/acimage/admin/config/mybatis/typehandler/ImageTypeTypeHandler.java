package com.acimage.admin.config.mybatis.typehandler;

import com.acimage.common.global.enums.ImageType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedJdbcTypes(JdbcType.VARCHAR)
public class ImageTypeTypeHandler extends BaseTypeHandler<ImageType> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ImageType imageType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, imageType.toString());
    }

    @Override
    public ImageType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return ImageType.getImageType(resultSet.getString(s));
    }

    @Override
    public ImageType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return ImageType.getImageType(resultSet.getString(i));
    }

    @Override
    public ImageType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return ImageType.getImageType(callableStatement.getString(i));
    }
}
