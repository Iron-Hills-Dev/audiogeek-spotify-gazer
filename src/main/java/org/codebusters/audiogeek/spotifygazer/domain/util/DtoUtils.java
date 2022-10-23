package org.codebusters.audiogeek.spotifygazer.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class DtoUtils {


    /**
     * Converts object to string
     * @param object object to convert
     * @param reservedFields fields to be censored
     * @return object data in string (with reservedFields censored)
     */
    @SuppressWarnings("unchecked")
    public static String convertToString(Object object, List<String> reservedFields) {
        var mapper = new ObjectMapper();
        var map = mapper.convertValue(object, Map.class);
        for (String reservedField : reservedFields) {
            map.replace(reservedField, "***");
        }
        return String.format("%s", map);

    }

    /**
     * Converts object to string
     * @param object object to convert
     * @return object data in string
     */
    public static String convertToString(Object object) {
        var oMapper = new ObjectMapper();
        var map = oMapper.convertValue(object, Map.class);
        return String.format("%s", map);

    }
}
