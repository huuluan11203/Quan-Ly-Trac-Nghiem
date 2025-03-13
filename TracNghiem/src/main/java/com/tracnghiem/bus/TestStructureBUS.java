/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.TestStructureDAO;
import com.tracnghiem.dto.TestStructureDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class TestStructureBUS {
    private final TestStructureDAO testStructureDAO = TestStructureDAO.getInstance();
    public static ArrayList<TestStructureDTO> listTestStructure = new ArrayList<>();

    public TestStructureBUS() {
        listTestStructure = testStructureDAO.selectAll();
    }

    public ArrayList<TestStructureDTO> getAll() {
        return testStructureDAO.selectAll();
    }
    
    public ArrayList<TestStructureDTO> getByTestCode(String testCode) {
        ArrayList<TestStructureDTO> result = new ArrayList<>();

        for (TestStructureDTO t : listTestStructure) {
            if (t.getTestCode().equals(testCode))
               result.add(t);
            
        }
        return result;
    }
    
    public int getTotalQuesByTestCode(String testCode) {
        int rs = 0;
        for (TestStructureDTO t : listTestStructure) {
            if (t.getTestCode().equals(testCode))
                rs += t.getNumEasy() + t.getNumMedium() + t.getNumDifficult();
        }
        return rs;
    }
    
    public boolean add(TestStructureDTO test) {
        if (testStructureDAO.insert(test)) {
            listTestStructure.add(test);
            return true;
        }
        return false;
    }
}
