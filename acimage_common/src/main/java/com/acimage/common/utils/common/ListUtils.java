package com.acimage.common.utils.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {
    public static <T, V> List<V> extract(Function<T, V> attribute, List<T> list) {
        if (list == null) {
            return null;
        }
        return list.stream().map(attribute).collect(Collectors.toList());
    }

    public static <T, V> List<T> extractKeyFrom(List<Pair<T, V>> pairList) {
        List<T> keyList = new ArrayList<>();
        for (Pair<T, V> item : pairList) {
            keyList.add(item.getKey());
        }
        return keyList;
    }

    public static <T> List<T> getListFrom(Set<T> set) {
        if (set == null) {
            return null;
        }
        List<T> list = new ArrayList<>(set.size());
        return null;
    }

    public static List<Long> differenceSetOf(List<Long> list1, List<Long> list2) {
        if (list2 == null || list2.size() == 0) {
            return list1;
        } else if (list1 == null || list1.size() == 0) {
            return list1;
        }

        List<Long> list1Copy = new ArrayList<>(list1);
        List<Long> list2Copy = new ArrayList<>(list2);

        Comparator<Long> longComparator = Comparator.naturalOrder();

        list1Copy.sort(longComparator);
        list2Copy.sort(longComparator);
        int ptr1 = 0, ptr2 = 0;
        List<Long> differenceList = new ArrayList<>();
        while (ptr1 < list1Copy.size() && ptr2 < list2Copy.size()) {
            if (list1Copy.get(ptr1) < list2Copy.get(ptr2)) {
                differenceList.add(list1Copy.get(ptr1));
                ptr1++;
            } else if (list1Copy.get(ptr1) > list2Copy.get(ptr2)) {
                ptr2++;
            } else if (list1Copy.get(ptr1).equals(list2Copy.get(ptr2))) {
                ptr1++;
                ptr2++;
            }
        }
        if (ptr2 == list2Copy.size()) {
            while (ptr1 < list1Copy.size()) {
                differenceList.add(list1Copy.get(ptr1));
                ptr1++;
            }
        }
        return differenceList;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static List<Long> convertToLongList(List<String> list) {
        List<Long> longList = new ArrayList<>();
        for (String item : list) {
            longList.add(Long.parseLong(item));
        }
        return longList;
    }

    public static List<Pair<Long, Integer>> getListInDescOrderFrom(Set<ZSetOperations.TypedTuple<String>> valueAnsScoreSet) {
        List<Pair<Long, Integer>> valueAnsScoreList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> item : valueAnsScoreSet) {
            Pair<Long, Integer> pair = new Pair<>(Long.parseLong(item.getValue()), item.getScore().intValue());
            valueAnsScoreList.add(pair);
        }

        return valueAnsScoreList;
    }

    public static List<Pair<Long, Double>> convertToLongDoublePairFrom(List<Pair<String, Double>> pairList) {

        List<Pair<Long, Double>> newList = new ArrayList<>();
        for (Pair<String, Double> item : pairList) {
            newList.add(new Pair<>(Long.parseLong(item.getKey()), item.getValue()));
        }

        return newList;
    }

    public static <T, V> List<V> convert(List<T> sourceList, Class<V> targetType) {
        List<V> resultList = new ArrayList<>();
        if (sourceList == null) {
            return null;
        } else if (sourceList.size() == 0) {
            return new ArrayList<>();
        }

        for (T item : sourceList) {
            resultList.add(Convert.convert(targetType, item));
        }
        return resultList;
    }

    public static <T> List<T> removeRepeat(List<T> list){
        Set<T> set=new HashSet<>(list);
        return new ArrayList<>(set);
    }

}
