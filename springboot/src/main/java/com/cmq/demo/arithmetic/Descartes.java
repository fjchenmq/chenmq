package com.cmq.demo.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/5/20.
 *
 * 笛卡尔积
 */
public class Descartes {
    public static void main(String[] args) {
        List<List<String>> listData = new ArrayList<>();
        listData.add(Arrays.asList("note", "pro"));
        listData.add(Arrays.asList("4G", "8G"));
        listData.add(Arrays.asList("64", "128"));
        listData.add(Arrays.asList("标配", "低配", "高配"));
        List<List<String>> lisReturn = getDescartes(listData);
        System.out.println(lisReturn.size());
        System.out.println(lisReturn);
    }

    private static <T> List<List<T>> getDescartes(List<List<T>> list) {
        List<List<T>> returnList = new ArrayList<>();
        descartesRecursive(list, 0, returnList, new ArrayList<T>());
        return returnList;
    }

    /**
     * 递归实现
     * 原理：从原始list的0开始依次遍历到最后
     *
     * @param originalList 原始list
     * @param position     当前递归在原始list的position
     * @param returnList   返回结果
     * @param cacheList    临时保存的list
     */
    private static <T> void descartesRecursive(List<List<T>> originalList, int position, List<List<T>> returnList, List<T> cacheList) {
        List<T> originalItemList = originalList.get(position);
        for (int i = 0; i < originalItemList.size(); i++) {
            //最后一个复用cacheList，节省内存
            List<T> childCacheList = (i == originalItemList.size() - 1) ? cacheList : new ArrayList<>(cacheList);
            childCacheList.add(originalItemList.get(i));
            if (position == originalList.size() - 1) {//遍历到最后退出递归
                returnList.add(childCacheList);
                continue;
            }
            descartesRecursive(originalList, position + 1, returnList, childCacheList);
        }
    }

}
