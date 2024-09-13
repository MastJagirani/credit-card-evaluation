package com.company.credit.evaluation.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public class ObjectMappingUtils {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Configure ObjectMapper to ignore unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T mapValues(Object source, Class<T> targetType) {
        return objectMapper.convertValue(source, targetType);
    }
}

