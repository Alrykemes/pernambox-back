package com.dev.pernambox.domain.user.enums;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreRoleEnum implements UserType<Role> {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<Role> returnedClass() {
        return Role.class;
    }

    @Override
    public boolean equals(Role x, Role y) {
        return x == y;
    }

    @Override
    public int hashCode(Role x) {
        return x.hashCode();
    }

    @Override
    public Role nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String name = rs.getString(position);
        return name == null ? null : Role.valueOf(name);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Role value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.name(), Types.OTHER);
        }
    }

    @Override
    public Role deepCopy(Role value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Role value) {
        return value.name();
    }

    @Override
    public Role assemble(Serializable cached, Object owner) {
        return Role.valueOf((String) cached);
    }

    @Override
    public Role replace(Role original, Role target, Object owner) {
        return original;
    }
}
