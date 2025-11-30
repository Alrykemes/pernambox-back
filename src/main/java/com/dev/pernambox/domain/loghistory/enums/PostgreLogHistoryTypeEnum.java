package com.dev.pernambox.domain.loghistory.enums;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreLogHistoryTypeEnum implements UserType<LogHistoryType> {
    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<LogHistoryType> returnedClass() {
        return LogHistoryType.class;
    }

    @Override
    public boolean equals(LogHistoryType x, LogHistoryType y) {
        return x == y;
    }

    @Override
    public int hashCode(LogHistoryType x) {
        return x.hashCode();
    }

    @Override
    public LogHistoryType nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String name = rs.getString(position);
        return name == null ? null : LogHistoryType.valueOf(name);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, LogHistoryType value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.name(), Types.OTHER);
        }
    }

    @Override
    public LogHistoryType deepCopy(LogHistoryType value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(LogHistoryType value) {
        return value.name();
    }

    @Override
    public LogHistoryType assemble(Serializable cached, Object owner) {
        return LogHistoryType.valueOf((String) cached);
    }

    @Override
    public LogHistoryType replace(LogHistoryType original, LogHistoryType target, Object owner) {
        return original;
    }
}
