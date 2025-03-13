/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dto;

import java.util.ArrayList;

/**
 *
 * @author X
 */
public class TestStructureDTO {
    private String testCode;
    private int tpID;
    private int numEasy;
    private int numMedium;
    private int numDifficult;
    private ArrayList<TestStructureDTO> testStructures;

    public TestStructureDTO() {
    }

    public TestStructureDTO(String testCode, int tpID, int numEasy, int numMedium, int numDifficult) {
        this.testCode = testCode;
        this.tpID = tpID;
        this.numEasy = numEasy;
        this.numMedium = numMedium;
        this.numDifficult = numDifficult;
        this.testStructures = new ArrayList<>();
    }

    public ArrayList<TestStructureDTO> getTestStructures() {
        return testStructures;
    }

    public void setTestStructures(ArrayList<TestStructureDTO> testStructures) {
        this.testStructures = testStructures;
    }
    
    

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
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

    @Override
    public String toString() {
        return "TestStructureDTO{" + "testCode=" + testCode + ", tpID=" + tpID + ", numEasy=" + numEasy + ", numMedium=" + numMedium + ", numDifficult=" + numDifficult + ", testStructures=" + testStructures + '}';
    }
    
    
    
}
