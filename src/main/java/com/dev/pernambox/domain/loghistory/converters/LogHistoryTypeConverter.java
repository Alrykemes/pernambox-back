package com.dev.pernambox.domain.loghistory.converters;

import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LogHistoryTypeConverter implements AttributeConverter<LogHistoryType, String> {
    @Override
    public String convertToDatabaseColumn(LogHistoryType type) {
        return type == null ? null : type.name();
    }

    @Override
    public LogHistoryType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LogHistoryType.valueOf(dbData);
    }
}
