/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

/**
 *
 * @author X
 */
public class QuestionDTO {
    private int qID;
    private String qContent;
    private String qPictures;
    private int qTopic;
    private String qLevel;
    private int qStatus;

    public QuestionDTO() {
    }

    public QuestionDTO(String qContent, String qPictures, int qTopic, String qLevel, int qStatus) {
        this.qContent = qContent;
        this.qPictures = qPictures;
        this.qTopic = qTopic;
        this.qLevel = qLevel;
        this.qStatus = qStatus;
    }

    public QuestionDTO(int qID, String qContent, String qPictures, int qTopic, String qLevel, int qStatus) {
        this.qID = qID;
        this.qContent = qContent;
        this.qPictures = qPictures;
        this.qTopic = qTopic;
        this.qLevel = qLevel;
        this.qStatus = qStatus;
    }

    public int getQStatus() {
        return qStatus;
    }

    public void setQStatus(int qStatus) {
        this.qStatus = qStatus;
    }

    public int getQID() {
        return qID;
    }

    public void setQID(int qID) {
        this.qID = qID;
    }

    public String getQContent() {
        return qContent;
    }

    public void setQContent(String qContent) {
        this.qContent = qContent;
    }

    public String getQPictures() {
        return qPictures;
    }

    public void setQPictures(String qPictures) {
        this.qPictures = qPictures;
    }

    public int getQTopic() {
        return qTopic;
    }

    public void setQTopic(int qTopic) {
        this.qTopic = qTopic;
    }

    public String getQLevel() {
        return qLevel;
    }

    public void setQLevel(String qLevel) {
        this.qLevel = qLevel;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" + "qID=" + qID + ", qContent=" + qContent + ", qPictures=" + qPictures + ", qTopic=" + qTopic + ", qLevel=" + qLevel + ", qStatus=" + qStatus + '}';
    }
    
    
    
}
