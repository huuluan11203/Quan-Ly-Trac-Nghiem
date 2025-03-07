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
    private int tpID;
    private int numEasy;
    private int numMedium;
    private int numDifficult;
    private int testLimit;
    private LocalDate testDate;
    private int testStatus;

    public TestDTO(String testCode, String testTitle, int testTime, int tpID, int numEasy, int numMedium, int numDifficult, int testLimit, LocalDate testDate, int testStatus) {
        this.testCode = testCode;
        this.testTittle = testTitle;
        this.testTime = testTime;
        this.tpID = tpID;
        this.numEasy = numEasy;
        this.numMedium = numMedium;
        this.numDifficult = numDifficult;
        this.testLimit = testLimit;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }
    
    public TestDTO() {
    }

    public TestDTO(int testID, String testCode, String testTitle, int testTime, int tpID, int numEasy, int numMedium, int numDifficult, int testLimit, LocalDate testDate, int testStatus) {
        this.testID = testID;
        this.testCode = testCode;
        this.testTittle = testTitle;
        this.testTime = testTime;
        this.tpID = tpID;
        this.numEasy = numEasy;
        this.numMedium = numMedium;
        this.numDifficult = numDifficult;
        this.testLimit = testLimit;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }

    @Override
    public String toString() {
        return "TestDTO{" + "testID=" + testID + ", testCode=" + testCode + ", testTitle=" + testTittle + ", testTime=" + testTime + ", tpID=" + tpID + ", numEasy=" + numEasy + ", numMedium=" + numMedium + ", numDifficult=" + numDifficult + ", testLimit=" + testLimit + ", testDate=" + testDate + ", testStatus=" + testStatus + '}';
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

    public String getTestTitle() {
        return testTittle;
    }

    public void setTestTitle(String testTitle) {
        this.testTittle = testTitle;
    }

    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    public int getTpID() {
        return tpID;
    }

    public void setTpID(int tpID) {
        this.tpID = tpID;
    }

    public int getNumEasy() {
        return numEasy;
    }

    public void setNumEasy(int numEasy) {
        this.numEasy = numEasy;
    }

    public int getNumMedium() {
        return numMedium;
    }

    public void setNumMedium(int numMedium) {
        this.numMedium = numMedium;
    }

    public int getNumDifficult() {
        return numDifficult;
    }

    public void setNumDifficult(int numDifficult) {
        this.numDifficult = numDifficult;
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


    
}
