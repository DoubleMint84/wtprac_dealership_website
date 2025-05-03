package ru.ispras.wtprac.dealership.utility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        String value;
        try {
            value = objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Map<String, Object> mapValue;
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        try {
            mapValue = objectMapper.readValue(dbData, typeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mapValue;
    }
}
