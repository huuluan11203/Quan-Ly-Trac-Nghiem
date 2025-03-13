/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author X
 */
public class PasswordUtil {
    // Mã hóa mật khẩu bằng MD5
    public static String hashPassword(String password) {
        try {
            // Tạo một đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            
            // Chuyển byte array thành chuỗi hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Kiểm tra mật khẩu với hash MD5 đã lưu
    public static boolean checkPassword(String inputPassword, String storedHash) {
        return storedHash.equals(hashPassword(inputPassword));
    }
}
