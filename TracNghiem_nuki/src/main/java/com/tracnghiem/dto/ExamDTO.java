/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

/**
 *
 * @author X
 */
public class ExamDTO {
    private String testCode;
    private String exOrder;
    private String exCode;
    private String exQuesIDs; // mảng câu hỏi

    public ExamDTO() {
    }

    public ExamDTO(String testCode, String exOrder, String exCode, String exQuesIDs) {
        this.testCode = testCode;
        this.exOrder = exOrder;
        this.exCode = exCode;
        this.exQuesIDs = exQuesIDs;
    }

    public String getExQuesIDs() {
        return exQuesIDs;
    }

    public void setExQuesIDs(String exQuesIDs) {
        this.exQuesIDs = exQuesIDs;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getExOrder() {
        return exOrder;
    }

    public void setExOrder(String exOrder) {
        this.exOrder = exOrder;
    }

    public String getExCode() {
        return exCode;
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    @Override
    public String toString() {
        return "ExamDTO{" + "testCode=" + testCode + ", exOrder=" + exOrder + ", exCode=" + exCode + ", exQuesIDs=" + exQuesIDs + '}';
    }
    
}
