package com.liren.lottery_system.common.handler;

import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.common.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@MappedTypes(Encrypt.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {
    /**
     * 将Encrypt对象插入到PreparedStatement中去
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null || parameter.getValue() == null) {
            // 为空则插入null
            ps.setString(i, null);
            return;
        }
        log.info("加密内容：" + SecurityUtil.aesEncrypt(parameter.getValue()));
        // 插入加密后的Encrypt对象
        ps.setString(i, SecurityUtil.aesEncrypt(parameter.getValue()));
    }

    /**
     * 下面三个方法都是获取PreparedStatement中的Encrypt对象
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.info("解密后的内容：" + SecurityUtil.aesDecrypt(rs.getString(columnName)));
        return decrypt(rs.getString(columnName));
    }

    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.info("解密后的内容：" + SecurityUtil.aesDecrypt(rs.getString(columnIndex)));
        return decrypt(rs.getString(columnIndex));
    }

    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        log.info("解密后的内容：" + SecurityUtil.aesDecrypt(cs.getString(columnIndex)));
        return decrypt(cs.getString(columnIndex));
    }

    private Encrypt decrypt(String value) {
        if(!StringUtils.hasText(value)) {
            return null;
        }
        return new Encrypt(SecurityUtil.aesDecrypt(value));
    }
}
