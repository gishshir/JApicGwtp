package fr.tsadeo.app.japicgwtp.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utilitaires de manipulation de collections
 * @author sylvie
 */
public class CollectUtils {

    public static long[] reverseArray(long[] items) {

        if (items == null) {
            return null;
        }
       List<Long> list = Arrays.stream(items)
               .mapToObj(item -> item)
               .collect(Collectors.toList());
                
        Collections.reverse(list);
        return list.stream()
                .mapToLong(item -> item)
                .toArray();
    }
    
    public static boolean isNullOrEmpty(Collection<?> collection){
    	return Objects.isNull(collection) || collection.isEmpty();
    }

    public static Map<String, Object> buildMapWithFirsItem(int size, String key, Object value) {

        final Map<String, Object> map = new HashMap<>(size);
        map.put(key, value);
        return map;
    }

    public static Map<String, Object> buildMapWithOneItem(String key, Object value) {
        return buildMapWithFirsItem(1, key, value);
    }
    
    public static <T> List<T> buildListWithOneItem(T item) {
    	final List<T> list = new ArrayList<>(0);
    	list.add(item);
    	return list;
    }

    public static <T> Map<Long, T> buildMapIdWithOneItem(Long key, T value) {

        final Map<Long, T> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

    public static String tabToString(String[] items, char separator) {

        if (items == null) {
            return null;
        }
        return Stream.of(items)
                .collect(Collectors.joining(separator + " "));

    }

    public static String buildStringFromList(List<String> list, char separator) {

        if (list == null) {
            return null;
        }
        return list.stream()
                .collect(Collectors.joining(separator + " "));
    }

    public static List<String> buildListItems(String items, String separator) {
        if (items == null) {
            return new ArrayList<>(0);
        }
        String[] tabItems = items.split(separator);

        return Arrays.asList(tabItems);
    }

    /**
     * Creation d'une liste de lignes en s'appuyant sur les saut de lignes \n
     *
     * @param text
     * @return
     */
    public static List<String> createListLines(String text) {

        final List<String> listLines = new ArrayList<>();
        if (text == null) {
            return listLines;
        }

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);
            if (c == '\n') {
                listLines.add(line.toString());
                line = new StringBuilder();
            } else {
                line.append(c);
            }

        }

        return listLines;

    }

    public static String[] listToTab(List<String> list) {

        if (list == null) {
            return null;
        }
        String[] tab = new String[list.size()];
        IntStream.range(0, list.size())
                .forEach(i -> tab[i] = list.get(i));
                
        return tab;
    }

}
