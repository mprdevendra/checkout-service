package com.demo.util;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class JsonReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> List<T> read(String filePath, Class<T> clazz) {
        try (InputStream is = JsonReader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) throw new IllegalArgumentException("File not found: " + filePath);
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
            return OBJECT_MAPPER.readValue(is, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + filePath, e);
        }
    }
}
