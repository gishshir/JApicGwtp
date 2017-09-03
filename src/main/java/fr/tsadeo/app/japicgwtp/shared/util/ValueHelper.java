package fr.tsadeo.app.japicgwtp.shared.util;

import java.util.HashMap;
import java.util.Map;

public class ValueHelper {



	public static String getStringValue(String value, String defaultValue) {
		return isStringEmptyOrNull(value) ? defaultValue : value;
	}
	
	public static  boolean isPair(int value) {
		return value % 2 == 0;
	}
	
    public static <T> Map<String, T> buildMapIdWithOneItem(String key, T value) {

        final Map<String, T> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

	public static int getIntValue(String value, int defaultValue) {

		int intValue = defaultValue;
		try {
			intValue = Integer.parseInt(value);
		} catch (NumberFormatException ignored) {
			// do nothing
		}

		return intValue;
	}

         public static long getLongValue(Object value, long defaultValue) {
             if (value == null) {
                 return defaultValue;
             }
             return getLongValue(value.toString(), defaultValue);
         }
        public static long getLongValue(String value, long defaultValue) {

		long longValue = defaultValue;
		try {
			longValue = Long.parseLong(value);
		} catch (NumberFormatException ignored) {
			// do nothing
		}

		return longValue;
	}
	public static boolean isStringEmptyOrNull(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	public static boolean isBetween (int value, int inf, int sup, boolean exclusif) {
		
		if (exclusif) {
			return value > inf && value < sup;
		} else {
			return value >= inf && value <= sup;
		}
	}

}
