package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.GenreType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenreTypeConverter implements AttributeConverter<GenreType, String> {
    @Override
    public String convertToDatabaseColumn(GenreType genreType) {
        return genreType != null ? genreType.getLabel() : null;
    }

    @Override
    public GenreType convertToEntityAttribute(String dbValue) {
        return dbValue != null ? GenreType.fromLabel(dbValue) : null;
    }
}
