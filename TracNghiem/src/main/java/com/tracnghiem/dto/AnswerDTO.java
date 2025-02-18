/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

/**
 *
 * @author X
 */
public class AnswerDTO {
    private int awID;
    private int qID;
    private String awContent;
    private String awPicture;
    private boolean isRight;
    private int awStatus;

    public AnswerDTO() {
    }

    public AnswerDTO(int qID, String awContent, String awPicture, boolean isRight, int awStatus) {
        this.qID = qID;
        this.awContent = awContent;
        this.awPicture = awPicture;
        this.isRight = isRight;
        this.awStatus = awStatus;
    }

    public AnswerDTO(int awID, int qID, String awContent, String awPicture, boolean isRight, int awStatus) {
        this.awID = awID;
        this.qID = qID;
        this.awContent = awContent;
        this.awPicture = awPicture;
        this.isRight = isRight;
        this.awStatus = awStatus;
    }

    public int getAwStatus() {
        return awStatus;
    }

    public void setAwStatus(int awStatus) {
        this.awStatus = awStatus;
    }

    public int getAwID() {
        return awID;
    }

    public void setAwID(int awID) {
        this.awID = awID;
    }

    public int getQID() {
        return qID;
    }

    public void setQID(int qID) {
        this.qID = qID;
    }

    public String getAwContent() {
        return awContent;
    }

    public void setAwContent(String awContent) {
        this.awContent = awContent;
    }

    public String getAwPicture() {
        return awPicture;
    }

    public void setAwPicture(String awPicture) {
        this.awPicture = awPicture;
    }

    public boolean isIsRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    @Override
    public String toString() {
        return "AnswerDTO{" + "awID=" + awID + ", qID=" + qID + ", awContent=" + awContent + ", awPicture=" + awPicture + ", isRight=" + isRight + ", awStatus=" + awStatus + '}';
    }
    
    
    
}
