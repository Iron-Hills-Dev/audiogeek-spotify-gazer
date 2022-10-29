package org.codebusters.audiogeek.spotifygazer.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class DtoUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);;

    /**
     * Converts object to string
     *
     * @param object         object to convert
     * @param reservedFields fields to be censored
     * @return object data in string (with reservedFields censored)
     */
    @SuppressWarnings("unchecked")
    public static String convertToString(Object object, List<String> reservedFields) {
        var map = MAPPER.convertValue(object, Map.class);
        for (String reservedField : reservedFields) {
            map.replace(reservedField, "***");
        }
        return String.format("%s", map);

    }

    /**
     * Converts object to string
     *
     * @param object object to convert
     * @return object data in string
     */
    public static String convertToString(Object object) {
        var map = MAPPER.convertValue(object, Map.class);
        return String.format("%s", map);
    }
}
