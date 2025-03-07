/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;

import com.tracnghiem.bus.UserBUS;
import com.tracnghiem.dto.UserDTO;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ADMIN
 */
public class PatternMatcherUtil {
    private static UserBUS userBUS = new UserBUS();
    public static boolean patternMatcherFullName(String key, UserDTO user){
        String normalized = Normalizer.normalize(key, Normalizer.Form.NFD);
        String keyword = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(user.getUserFullName());
        if(matcher.find())
            return true;
        return false;
    }
    
    public static boolean patternMatcherUserName(String key, UserDTO user){
        String normalized = Normalizer.normalize(key, Normalizer.Form.NFD);
        String keyword = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(user.getUserName());
        if(matcher.find())
            return true;
        return false;
    }
    
    public static boolean patternMatcherEmail(String key, UserDTO user){
        String normalized = Normalizer.normalize(key, Normalizer.Form.NFD);
        String keyword = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(user.getUserEmail());
        if(matcher.find())
            return true;
        return false;
    }
}
