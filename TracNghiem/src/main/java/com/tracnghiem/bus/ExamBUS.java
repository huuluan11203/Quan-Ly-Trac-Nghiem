/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.ExamDAO;
import com.tracnghiem.dto.ExamDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class ExamBUS {
    private final ExamDAO eDAO = ExamDAO.getInstance();
    private ArrayList<ExamDTO> listExams;

    public ExamBUS() {
        listExams = eDAO.selectAll();
    }

    public ArrayList<ExamDTO> getAll() {
        return eDAO.selectAll();
    }
    
    public ArrayList<ExamDTO> getAll(String testCode) {
        return eDAO.selectAll(testCode);
    }
    
    public ExamDTO findOne(String exCode) {
        return eDAO.selectByID(exCode);
    }

    public int getIndex(String exCode) {
        for (int i = 0; i < listExams.size(); i++) {
            if (listExams.get(i).getExCode().equals(exCode)) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(ExamDTO exam) {
        if (eDAO.insert(exam)) {
            listExams.add(exam);
            return true;
        }
        return false;
    }

    public boolean delete(String exCode) {
        int index = getIndex(exCode);
        if (index != -1 && eDAO.delete(exCode)) {
            listExams.remove(index);
            return true;
        }
        return false;
    }

    public boolean update(ExamDTO exam) {
        int index = getIndex(exam.getExCode());
        if (index != -1 && eDAO.update(exam)) {
            listExams.set(index, exam);
            return true;
        }
        return false;
    }

    public ArrayList<ExamDTO> search(String key) {
        ArrayList<ExamDTO> result = new ArrayList<>();
        key = key.toLowerCase();

        for (ExamDTO exam : listExams) {
            if (exam.getExCode().toLowerCase().contains(key) ||
                exam.getTestCode().toLowerCase().contains(key)) {
                result.add(exam);
            }
        }

        return result;
    }
    
    public ArrayList<String> getExamCodesByTestCode(String testCode){
        return eDAO.getExamCodesByTestCode(testCode);
    }
    
    
}
