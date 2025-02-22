/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.QuestionDAO;
import com.tracnghiem.dao.TopicDAO;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TopicDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class QuestionBUS {
    private final QuestionDAO qDAO = QuestionDAO.getInstance();
    public ArrayList<QuestionDTO> listQ = new ArrayList<>();
    
    public QuestionBUS() {
        this.listQ = qDAO.selectAll();
    }
    
    public ArrayList<QuestionDTO> getAll() {
        return qDAO.selectAll();
    }
    
    public QuestionDTO findOne(int id) {
        return qDAO.selectByID(id+"");
    }
    
    public int getIndex(QuestionDTO q) {
        for (int i = 0; i < this.listQ.size(); i++) {
            if (this.listQ.get(i).getQID()== q.getQID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(QuestionDTO q) {
        if (qDAO.insert(q)) {
            this.listQ.add(q);
            return true;
        }
        return false;
    }
    public boolean delete(QuestionDTO q) {
        if (qDAO.delete(q.getQID()+ "")) {
            this.listQ.remove(getIndex(q));
            return true;
        }
        return false;
    }
    public boolean update(QuestionDTO q) {
        if (qDAO.update(q)) {
            this.listQ.set(getIndex(q), q);
            return true;
        }
        return false;
    }
    public ArrayList<QuestionDTO> search(String key, String type) {
        ArrayList<QuestionDTO> result = new ArrayList<>();
        key = key.toLowerCase();

        switch (type) {
            case "Tất cả" -> {
                for (QuestionDTO i : this.listQ) {
                    if (i.getQContent().toLowerCase().contains(key) || i.getQLevel().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            case "Mức độ" -> {
                for (QuestionDTO i : this.listQ) {
                    if (i.getQLevel().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            case "Nội dung" -> {
                for (QuestionDTO i : this.listQ) {
                    if (i.getQContent().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            
        }

        return result;
    }
    public ArrayList<QuestionDTO> getByTopic(int tpID) {
        ArrayList<QuestionDTO> result = new ArrayList<>();
        for (QuestionDTO i : this.listQ) {
            if (i.getQTopic() == tpID)
                result.add(i);
        }
        return  result;
    }
    public ArrayList<QuestionDTO> getByTopicParent(int tpIDParent) {
        ArrayList<QuestionDTO> result = new ArrayList<>();
        TopicBUS tBUS = new TopicBUS();
        
        ArrayList<TopicDTO> lTp = tBUS.getAllChildTopics(tpIDParent);
        
        ArrayList<Integer> topicIds = new ArrayList<>();
        topicIds.add(tpIDParent);
        for (TopicDTO topic : lTp) {
            topicIds.add(topic.getTpID()); // Giả sử TopicDTO có phương thức getId()
        }
        
        for (QuestionDTO i : this.listQ) {
             if (topicIds.contains(i.getQTopic())) 
                 result.add(i);
        }
        return  result;
    }
    public ArrayList<QuestionDTO> getByTopicParentAndChild(int tpIDParent ,int tpIDChild) {
        ArrayList<QuestionDTO> result = new ArrayList<>();
        TopicBUS tBUS = new TopicBUS();
        
        ArrayList<TopicDTO> lTp = tBUS.getAllChildTopics(tpIDParent);
        
        ArrayList<Integer> topicIds = new ArrayList<>();
        topicIds.add(tpIDParent);
        for (TopicDTO topic : lTp) {
            topicIds.add(topic.getTpID()); // Giả sử TopicDTO có phương thức getId()
        }
        
        for (QuestionDTO i : this.listQ) {
             if (topicIds.contains(i.getQTopic())) 
                 result.add(i);
        }
        return  result;
    }
    public ArrayList<QuestionDTO> getByLevel(String level) {
        ArrayList<QuestionDTO> result = new ArrayList<>();
        for (QuestionDTO i : this.listQ) {
            if (i.getQLevel().equalsIgnoreCase(level))
                result.add(i);
        }
        return  result;
    }
    public int getMaxID() {
        return qDAO.getMaxID();
    }
}

