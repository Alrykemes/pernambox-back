package com.dev.pernambox.domain.loghistory.converters;

import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LogHistoryTargetConverter implements AttributeConverter<LogHistoryTarget, String> {
    @Override
    public String convertToDatabaseColumn(LogHistoryTarget target) {
        return target == null ? null : target.name();
    }

    @Override
    public LogHistoryTarget convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LogHistoryTarget.valueOf(dbData);
    }
}
