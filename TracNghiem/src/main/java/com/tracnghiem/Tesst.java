/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author X
 */
public class Tesst {
    public static void main(String[] args) {
        ArrayList<Integer> l = new ArrayList<>();
       l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(4);
        l.add(2);
        
        String result = l.stream()
                         .map(String::valueOf) // Chuyển Integer thành String
                         .collect(Collectors.joining(";"));
        System.out.println(result);
    }
}
