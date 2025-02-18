/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.AnswerDAO;
import com.tracnghiem.dto.AnswerDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class AnswerBUS {

    private final AnswerDAO aDAO = AnswerDAO.getInstance();
    public ArrayList<AnswerDTO> listAns = new ArrayList<>();

    public AnswerBUS() {
        this.listAns = aDAO.selectAll();
    }

    public ArrayList<AnswerDTO> getAll() {
        return listAns;
    }

    public AnswerDTO findOne(int id) {
        return aDAO.selectByID(id + "");
    }

    public int getIndex(AnswerDTO a) {
        for (int i = 0; i < this.listAns.size(); i++) {
            if (this.listAns.get(i).getAwID() == a.getAwID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(AnswerDTO a) {
        if (aDAO.insert(a)) {
            this.listAns.add(a);
            return true;
        }
        return false;
    }

    public boolean delete(AnswerDTO a) {
        if (aDAO.delete(a.getAwID() + "")) {
            this.listAns.remove(getIndex(a));
            return true;
        }
        return false;
    }

    public boolean update(AnswerDTO a) {
        if (aDAO.update(a)) {
            this.listAns.set(getIndex(a), a);
            return true;
        }
        return false;
    }

    public ArrayList<AnswerDTO> search(String key) {
        ArrayList<AnswerDTO> result = new ArrayList<>();
        key = key.toLowerCase();

        for (AnswerDTO i : this.listAns) {
            if (i.getAwContent().toLowerCase().contains(key)) {
                result.add(i);
            }
        }

        return result;
    }
    
    public ArrayList<AnswerDTO> getByQuesID(int qID) {
        ArrayList<AnswerDTO> result = new ArrayList<>();

        for (AnswerDTO i : this.listAns) {
            if (i.getQID() == qID) {
                result.add(i);
            }
        }

        return result;
    }
    

}
