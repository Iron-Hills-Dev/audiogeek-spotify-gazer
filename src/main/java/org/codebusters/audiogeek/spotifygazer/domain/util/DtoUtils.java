package org.codebusters.audiogeek.spotifygazer.domain.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoUtils {
    public static String convertToString(Map<String, String> object, List<String> reservedFields) {
        var censoredObject = new HashMap<>(object);
        for (String reservedField : reservedFields) {
            censoredObject.replace(reservedField, "[DELETED]");
        }
        return String.format("%s", censoredObject);
    }

    public static String convertToString(Map<String, String> object) {
        return String.format("%s", object.toString());
    }
}
