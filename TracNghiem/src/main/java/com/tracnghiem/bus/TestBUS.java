package com.tracnghiem.bus;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.tracnghiem.dao.TestDAO;
import com.tracnghiem.dto.TestDTO;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class TestBUS {
     private final TestDAO testDAO = TestDAO.getInstance();
    public ArrayList<TestDTO> listTest = new ArrayList<>();

    public TestBUS() {
        this.listTest = testDAO.selectAll();
    }

    public ArrayList<TestDTO> getAll() {
        return testDAO.selectAll();
    }

    public TestDTO findOne(int id) {
        return testDAO.selectByID(id + "");
    }

    public int getIndex(TestDTO test) {
        for (int i = 0; i < this.listTest.size(); i++) {
            if (this.listTest.get(i).getTestID() == test.getTestID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(TestDTO test) {
        if (testDAO.insert(test)) {
            this.listTest.add(test);
            return true;
        }
        return false;
    }

    public boolean delete(TestDTO test) {
        if (testDAO.delete(test.getTestID() + "")) {
            this.listTest.remove(getIndex(test));
            return true;
        }
        return false;
    }

    public boolean update(TestDTO test) {
        if (testDAO.update(test)) {
            this.listTest.set(getIndex(test), test);
            return true;
        }
        return false;
    }

//    public ArrayList<TestDTO> search(String key) {
//        ArrayList<TestDTO> result = new ArrayList<>();
//        key = key.toLowerCase();
//
//        for (TestDTO test : this.listTest) {
//            if (test.getTestCode().toLowerCase().contains(key) ||
//                test.getTestTitle().toLowerCase().contains(key) ||
//                String.valueOf(test.getTestID()).equals(key)) {
//                result.add(test);
//            }
//        }
//        return result;
//    }
//
//    

    public int getMaxID() {
        return testDAO.getMaxID();
    }
}
