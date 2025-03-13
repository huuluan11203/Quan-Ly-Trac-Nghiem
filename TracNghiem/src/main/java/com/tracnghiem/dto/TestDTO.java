package com.tracnghiem.dto;

import java.time.LocalDate;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author X
 */
public class TestDTO {
    private int testID;
    private String testCode;
    private String testTittle;
    private int testTime;
    private int testLimit;
    private LocalDate testDate;
    private int testStatus;

    public TestDTO() {
    }

    public TestDTO(int testID, String testCode, String testTittle, int testTime, int testLimit, LocalDate testDate, int testStatus) {
        this.testID = testID;
        this.testCode = testCode;
        this.testTittle = testTittle;
        this.testTime = testTime;
        this.testLimit = testLimit;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestTittle() {
        return testTittle;
    }

    public void setTestTittle(String testTittle) {
        this.testTittle = testTittle;
    }

    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    public int getTestLimit() {
        return testLimit;
    }

    public void setTestLimit(int testLimit) {
        this.testLimit = testLimit;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public int getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(int testStatus) {
        this.testStatus = testStatus;
    }

    @Override
    public String toString() {
        return "TestDTO{" + "testID=" + testID + ", testCode=" + testCode + ", testTittle=" + testTittle + ", testTime=" + testTime + ", testLimit=" + testLimit + ", testDate=" + testDate + ", testStatus=" + testStatus + '}';
    }
    
    

}
