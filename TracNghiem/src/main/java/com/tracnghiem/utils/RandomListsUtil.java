/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author X
 */
public class RandomListsUtil {
    
    
     public static ArrayList<String> generateUniqueRandomLists(ArrayList<Integer> originalList, int k) {
        Set<String> uniqueSets = new HashSet<>();
        Random rand = new Random();
        
        int maxAttempts = k * 10;
        int attempts = 0;

        while (uniqueSets.size() < k && attempts < maxAttempts) {
            ArrayList<Integer> shuffledList = new ArrayList<>(originalList);
            Collections.shuffle(shuffledList, rand);

            // Chuyển danh sách số thành chuỗi, phân tách bằng dấu ':'
            String listAsString = shuffledList.stream()
                                              .map(String::valueOf)
                                              .reduce((a, b) -> a + ";" + b)
                                              .orElse("");

            // Chỉ thêm vào tập hợp nếu chưa có danh sách giống hệt
            uniqueSets.add(listAsString);
            attempts++;
        }

        return new ArrayList<>(uniqueSets);
    }
}
