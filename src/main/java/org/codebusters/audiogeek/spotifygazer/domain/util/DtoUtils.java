package org.codebusters.audiogeek.spotifygazer.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class DtoUtils {


    @SuppressWarnings("unchecked")
    public static String convertToString(Object object, List<String> reservedFields) {
        var mapper = new ObjectMapper();
        var map = mapper.convertValue(object, Map.class);
        for (String reservedField : reservedFields) {
            map.replace(reservedField, "██████████████");
        }
        return String.format("%s", map);

    }

    public static String convertToString(Object object) {
        var oMapper = new ObjectMapper();
        var map = oMapper.convertValue(object, Map.class);
        return String.format("%s", map);

    }
}
