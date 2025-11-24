package com.dev.pernambox.domain.loghistory.enums;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreLogHistoryTargetEnum implements UserType<LogHistoryTarget> {
    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<LogHistoryTarget> returnedClass() {
        return LogHistoryTarget.class;
    }

    @Override
    public boolean equals(LogHistoryTarget x, LogHistoryTarget y) {
        return x == y;
    }

    @Override
    public int hashCode(LogHistoryTarget x) {
        return x.hashCode();
    }

    @Override
    public LogHistoryTarget nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String name = rs.getString(position);
        return name == null ? null : LogHistoryTarget.valueOf(name);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, LogHistoryTarget value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.name(), Types.OTHER);
        }
    }

    @Override
    public LogHistoryTarget deepCopy(LogHistoryTarget value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(LogHistoryTarget value) {
        return value.name();
    }

    @Override
    public LogHistoryTarget assemble(Serializable cached, Object owner) {
        return LogHistoryTarget.valueOf((String) cached);
    }

    @Override
    public LogHistoryTarget replace(LogHistoryTarget original, LogHistoryTarget target, Object owner) {
        return original;
    }
}
