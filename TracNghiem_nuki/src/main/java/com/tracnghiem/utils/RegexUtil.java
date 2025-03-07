/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ADMIN
 */
public class RegexUtil {
    //Regex Email
    public static boolean isValidEmail(String email){
        String regex = "^[a-zA-Z0-9.-_+%]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    public static boolean isValidUserName(String user){
        String regex = "^(?=.{3,16}$)(?![!@#$%^&*_\\-.])(?!.*[!@#$%^&*\\-_]{2,})[a-zA-Z0-9._]+(?<![!@#$%^&*_\\-.])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }
}
