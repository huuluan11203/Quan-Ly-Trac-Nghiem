/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

import java.time.LocalDateTime;

/**
 *
 * @author X
 */
public class ResultDTO {
    private int rsID;
    private int userID;
    private String exCode;
    private String rsAnswer;
    private double rsMark;
    private LocalDateTime rsDate;

    public ResultDTO(int userID, String exCode, String rsAnswer, double rsMark, LocalDateTime rsDate) {
        this.userID = userID;
        this.exCode = exCode;
        this.rsAnswer = rsAnswer;
        this.rsMark = rsMark;
        this.rsDate = rsDate;
    }

    public ResultDTO(int rsID, int userID, String exCode, String rsAnswer, double rsMark, LocalDateTime rsDate) {
        this.rsID = rsID;
        this.userID = userID;
        this.exCode = exCode;
        this.rsAnswer = rsAnswer;
        this.rsMark = rsMark;
        this.rsDate = rsDate;
    }

    public ResultDTO() {
    }

    public int getRsID() {
        return rsID;
    }

    public void setRsID(int rsID) {
        this.rsID = rsID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getExCode() {
        return exCode;
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    public String getRsAnswer() {
        return rsAnswer;
    }

    public void setRsAnswer(String rsAnswer) {
        this.rsAnswer = rsAnswer;
    }

    public double getRsMark() {
        return rsMark;
    }

    public void setRsMark(double rsMark) {
        this.rsMark = rsMark;
    }

    public LocalDateTime getRsDate() {
        return rsDate;
    }

    public void setRsDate(LocalDateTime rsDate) {
        this.rsDate = rsDate;
    }

    @Override
    public String toString() {
        return "ResultDTO{" + "rsID=" + rsID + ", userID=" + userID + ", exCode=" + exCode + ", rsAnswer=" + rsAnswer + ", rsMark=" + rsMark + ", rsDate=" + rsDate + '}';
    }

    
    
}
