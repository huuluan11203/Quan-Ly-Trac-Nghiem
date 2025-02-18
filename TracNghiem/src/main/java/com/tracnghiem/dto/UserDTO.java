/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

/**
 *
 * @author X
 */
public class UserDTO {
    private int userID;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userFullName;
    private int isAdmin;

    public UserDTO(int userID, String userName, String userEmail, String userPassword, String userFullName, int isAdmin) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFullName = userFullName;
        this.isAdmin = isAdmin;
    }

    public UserDTO(String userPassword, String userName, String userEmail, String userFullName, int isAdmin) {
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.isAdmin = isAdmin;
    }

    public UserDTO() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "userID=" + userID + ", userPassword=" + userPassword + ", userName=" + userName + ", userEmail=" + userEmail + ", userFullName=" + userFullName + ", isAdmin=" + isAdmin + '}';
    }
    
    
    
    
}
