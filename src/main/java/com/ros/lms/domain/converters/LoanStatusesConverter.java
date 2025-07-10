package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.LoanStatuses;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class LoanStatusesConverter implements AttributeConverter<LoanStatuses, String> {

    @Override
    public String convertToDatabaseColumn(LoanStatuses status) {
        return (status != null) ? status.getVal() : null;
    }

    @Override
    public LoanStatuses convertToEntityAttribute(String dbValue) {
        for (LoanStatuses status : LoanStatuses.values()) {
            if (status.getVal().equalsIgnoreCase(dbValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown loan status value: " + dbValue);
    }
}
