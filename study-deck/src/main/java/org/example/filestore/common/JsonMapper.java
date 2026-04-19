package org.example.filestore.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonMapper {
    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();
        // 1. Java 8 날짜/시간 모듈 등록 (Instant 처리용)
        INSTANCE.registerModule(new JavaTimeModule());

        // 2. 타임스탬프 숫자 형식이 아닌 ISO-8601 문자열로 저장 (가독성 향상)
        INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 3. JSON을 예쁘게 들여쓰기해서 저장
        INSTANCE.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
