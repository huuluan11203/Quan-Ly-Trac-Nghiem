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
public class LogDTO {
        private int logID;
        private String logContent;
        private int logUserID;
        private String logExCode;
        private LocalDateTime logDate;

    public LogDTO() {
    }

    public LogDTO(String logContent, int logUserID, String logExCode, LocalDateTime logDate) {
        this.logContent = logContent;
        this.logUserID = logUserID;
        this.logExCode = logExCode;
        this.logDate = logDate;
    }

    public LogDTO(int logID, String logContent, int logUserID, String logExcode, LocalDateTime logDate) {
        this.logID = logID;
        this.logContent = logContent;
        this.logUserID = logUserID;
        this.logExCode = logExcode;
        this.logDate = logDate;
    }

    public LocalDateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }

  
    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public int getLogUserID() {
        return logUserID;
    }

    public void setLogUserID(int logUserID) {
        this.logUserID = logUserID;
    } 
    

    public String getLogExCode() {
        return logExCode;
    }

    public void setLogExCode(String logExCode) {
        this.logExCode = logExCode;
    }

    @Override
    public String toString() {
        return "LogDTO{" + "logID=" + logID + ", logContent=" + logContent + ", logUserID=" + logUserID + ", logExCode=" + logExCode + ", logDate=" + logDate + '}';
    }

        
}
