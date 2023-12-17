package com.dnd.reetplace.global.converter;

import com.dnd.reetplace.app.domain.place.PlaceSubCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LikeSubCategoryConverter implements AttributeConverter<List<PlaceSubCategory>, String> {

    public static final String EMPTY_STRING = "";

    @Override
    public String convertToDatabaseColumn(List<PlaceSubCategory> attribute) {
        return attribute.isEmpty() ? EMPTY_STRING : attribute.stream().map(PlaceSubCategory::name).collect(Collectors.joining(","));
    }

    @Override
    public List<PlaceSubCategory> convertToEntityAttribute(String dbData) {
        return dbData.isBlank() ? Collections.emptyList() : Arrays.stream(dbData.split(",")).map(PlaceSubCategory::valueOf).collect(Collectors.toList());
    }
}
