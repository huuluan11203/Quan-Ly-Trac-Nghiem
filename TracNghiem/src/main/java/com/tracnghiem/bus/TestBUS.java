package com.tracnghiem.bus;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.tracnghiem.dao.TestDAO;
import com.tracnghiem.dto.TestDTO;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class TestBUS {
     private final TestDAO testDAO = TestDAO.getInstance();
    public static ArrayList<TestDTO> listTest = new ArrayList<>();

    public TestBUS() {
        listTest = testDAO.selectAll();
    }

    public ArrayList<TestDTO> getAll() {
        return testDAO.selectAll();
    }

    public TestDTO findOne(int id) {
        return testDAO.selectByID(id + "");
    }
    
    public boolean isExistTestCode(String testCode) {
        for (int i = 0; i < listTest.size(); i++) {
            if (listTest.get(i).getTestCode().equalsIgnoreCase(testCode)) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(TestDTO test) {
        for (int i = 0; i < listTest.size(); i++) {
            if (listTest.get(i).getTestID() == test.getTestID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(TestDTO test) {
        if (testDAO.insert(test)) {
            listTest.add(test);
            return true;
        }
        return false;
    }

    public boolean delete(TestDTO test) {
        if (testDAO.delete(test.getTestID() + "")) {
            listTest.remove(getIndex(test));
            return true;
        }
        return false;
    }

    public boolean update(TestDTO test) {
        if (testDAO.update(test)) {
            listTest.set(getIndex(test), test);
            return true;
        } 
        return false;
    }

    public ArrayList<TestDTO> search(String key) {
        ArrayList<TestDTO> result = new ArrayList<>();
        key = key.toLowerCase();
        TopicBUS tpBUS = new TopicBUS();

        for (TestDTO test : listTest) {
            if (test.getTestCode().toLowerCase().contains(key) ||
                test.getTestTitle().toLowerCase().contains(key) ||
                String.valueOf(test.getTestID()).equals(key) ||
                tpBUS.findOne(test.getTpID()).getTpTitle().toLowerCase().contains(key)
                    ) {
                result.add(test);
            }
        }
        return result;
    }

    public ArrayList<TestDTO> search(String key, LocalDate date) {
        ArrayList<TestDTO> result = new ArrayList<>();
        key = key.toLowerCase();
        TopicBUS tpBUS = new TopicBUS();

        for (TestDTO test : listTest) {
            if (test.getTestCode().toLowerCase().contains(key) ||
                test.getTestTitle().toLowerCase().contains(key) ||
                String.valueOf(test.getTestID()).equals(key) ||
                tpBUS.findOne(test.getTpID()).getTpTitle().toLowerCase().contains(key)
                    ) {
                result.add(test);
            }
        }
        
        if (result.isEmpty()) {
            return result;
        }
        
        for (int i = result.size() - 1; i >= 0; i--) {
            if (!result.get(i).getTestDate().isEqual(date)) {
                result.remove(i);
            }
        }
        
        return result;
    }
//
//    

    public int getMaxID() {
        return testDAO.getMaxID();
    }
}
