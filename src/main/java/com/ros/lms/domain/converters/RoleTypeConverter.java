package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType != null ? roleType.str() : null;
    }

    @Override
    public RoleType convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        for (RoleType role : RoleType.values()) {
            if (role.str().equals(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + dbValue);
    }
}
