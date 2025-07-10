package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.Sex;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexConverter implements AttributeConverter<Sex, Character> {

    @Override
    public Character convertToDatabaseColumn(Sex sex) {
        return (sex != null) ? sex.getCode() : null;
    }

    @Override
    public Sex convertToEntityAttribute(Character dbCode) {
        return (dbCode != null) ? Sex.fromCode(dbCode) : null;
    }
}
