package org.home.geolugares.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kelvin on 15/11/2017.
 */

public final class Constants {

    public static final String GEONAME_USER_NAME = "kelcervan";
    public static final List<String> TIPOS_LUGARES = Arrays.asList("A", "H", "L", "P", "R", "S", "T", "U", "V");
    public static final String LANG = "es";
    public static final String DEFAULT_RADIO = "10";
    public static final String DEFAULT_ROW = "10";
    public static final String DEFAULT_GEONAME_ROW = "100";
    public static final String GEOGNOS_API = "http://www.geognos.com/api/en/countries/flag/%s.png";
    public static final String GEONAME_KEY = "Geoname";
    public static String DIRECTIONS_KEY = "AIzaSyAMni--tCJGoCbW-RsdcfhWBDEEC0uOhDQ"; // Google Api directions

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
