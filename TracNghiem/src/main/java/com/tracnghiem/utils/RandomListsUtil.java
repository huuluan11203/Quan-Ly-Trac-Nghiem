/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author X
 */
public class RandomListsUtil {
    
    public static ArrayList<String> generateUniqueRandomLists(ArrayList<Integer> originalList, int k) {
       Set<String> uniqueSets = new HashSet<>();
        Random random = new Random();

        while (uniqueSets.size() < k) {
            List<Integer> shuffledList = new ArrayList<>(originalList);
            Collections.shuffle(shuffledList, random); // Xáo trộn ngẫu nhiên
            String listAsString = String.join(";", shuffledList.stream()
                                      .map(String::valueOf)
                                      .toArray(String[]::new)); // Chuyển thành chuỗi "1;3;4;5"
            uniqueSets.add(listAsString);
        }

        return new ArrayList<>(uniqueSets);
    }

    // Hàm đệ quy để tạo hoán vị
    private static void permute(List<Integer> nums, int l, List<List<Integer>> result) {
        if (l == nums.size()) {
            result.add(new ArrayList<>(nums));
            return;
        }
        for (int i = l; i < nums.size(); i++) {
            Collections.swap(nums, l, i);
            permute(nums, l + 1, result);
            Collections.swap(nums, l, i); // Hoàn tác để thử trường hợp khác
        }
    }
}
