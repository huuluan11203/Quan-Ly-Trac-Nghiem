/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

/**
 *
 * @author X
 */
public class TopicDTO {
    private int tpID;
    private String tpTitle;
    private int tpParent;
    private int tpStatus;

    public TopicDTO(int tpID, String tpTitle, int tpParent, int tpStatus) {
        this.tpID = tpID;
        this.tpTitle = tpTitle;
        this.tpParent = tpParent;
        this.tpStatus = tpStatus;
    }

    public TopicDTO() {
    }

    public TopicDTO(String tpTitle, int tpParent, int tpStatus) {
        this.tpTitle = tpTitle;
        this.tpParent = tpParent;
        this.tpStatus = tpStatus;
    }

    public int getTpID() {
        return tpID;
    }

    public void setTpID(int tpID) {
        this.tpID = tpID;
    }

    public String getTpTitle() {
        return tpTitle;
    }

    public void setTpTitle(String tpTitle) {
        this.tpTitle = tpTitle;
    }

    public int getTpParent() {
        return tpParent;
    }

    public void setTpParent(int tpParent) {
        this.tpParent = tpParent;
    }

    public int getTpStatus() {
        return tpStatus;
    }

    public void setTpStatus(int tpStatus) {
        this.tpStatus = tpStatus;
    }

    @Override
    public String toString() {
        return "TopicDTO{" + "tpID=" + tpID + ", tpTitle=" + tpTitle + ", tpParent=" + tpParent + ", tpStatus=" + tpStatus + '}';
    }
    
    
    
}
