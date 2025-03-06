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
        
        // Tạo tất cả các hoán vị của danh sách câu hỏi
        List<List<Integer>> allPermutations = new ArrayList<>();
        permute(originalList, 0, allPermutations);

        // Chuyển danh sách các hoán vị thành chuỗi
        for (List<Integer> perm : allPermutations) {
            String listAsString = perm.stream()
                                      .map(String::valueOf)
                                      .reduce((a, b) -> a + ";" + b)
                                      .orElse("");
            uniqueSets.add(listAsString);
        }

        // Nếu k > số hoán vị có thể có, chỉ lấy tối đa số hoán vị có thể
        int limit = Math.min(k, uniqueSets.size());
        
        return new ArrayList<>(new ArrayList<>(uniqueSets).subList(0, limit));
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
