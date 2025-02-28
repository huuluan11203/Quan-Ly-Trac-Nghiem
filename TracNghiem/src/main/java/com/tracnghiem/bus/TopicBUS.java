/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.TopicDAO;
import com.tracnghiem.dto.TopicDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class TopicBUS {

    private final TopicDAO tpDAO = TopicDAO.getInstance();
    public static ArrayList<TopicDTO> listTp = new ArrayList<>();

    public TopicBUS() {
        listTp = tpDAO.selectAll();
    }

    public ArrayList<TopicDTO> getAllParent() {
        ArrayList<TopicDTO> result = new ArrayList<>();
        for (TopicDTO t : listTp) {
            if (t.getTpParent() == 0) {
                result.add(t);
            }
        }
        return result;
    }

    public ArrayList<TopicDTO> getAll() {
        return tpDAO.selectAll();
    }

    public int getNextID() {
        return tpDAO.getNextID();
    }

    public boolean isExistWithParent(String tpTitle, int tpParent) {
        listTp = tpDAO.selectAll();
        for (TopicDTO topic : listTp) {
            // Kiểm tra trùng cả tiêu đề và chủ đề cha
            if (topic.getTpTitle().equalsIgnoreCase(tpTitle) && topic.getTpParent() == tpParent) {
                return true; // Đã tồn tại
            }
        }
        return false; // Chưa tồn tại
    }
    
    public ArrayList<TopicDTO> getAllParentTopics(int id) {
        ArrayList<TopicDTO> result = new ArrayList<>();

        TopicDTO t = findOne(id);
        result.add(t);
        if (t.getTpParent() == 0) {
            return result;
        }

        TopicDTO t1 = findOne(t.getTpParent());
        result.add(t1);
        if (t1.getTpParent() == 0) {
            return result;
        }
        TopicDTO t2 = findOne(t1.getTpParent());
        result.add(t2);
        if (t2.getTpParent() == 0) {
            return result;
        }

        return result;
    }

    public TopicDTO findOne(int id) {
        return tpDAO.selectByID(id + "");
    }

    public TopicDTO findOneTitle(String title) {
        for (TopicDTO t : listTp) {
            if (t.getTpTitle().equals(title)) {
                return t;
            }
        }
        return null;
    }

    public boolean isExist(String tenmonhoc) {
        for (TopicDTO tpDTO : listTp) {
            if (tpDTO.getTpTitle().equalsIgnoreCase(tenmonhoc)) {
                return true;
            }
        }
        return false;

    }

    public int getIndex(TopicDTO tp) {
        for (int i = 0; i < listTp.size(); i++) {
            if (listTp.get(i).getTpID() == tp.getTpID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(TopicDTO tp) {
        if (tpDAO.insert(tp)) {
            listTp.add(tp);
            return true;
        }
        return false;
    }

    public boolean delete(TopicDTO tp) {
        // Kiểm tra xem topic có topic con không
        if (hasChildTopics(tp.getTpID())) {
            return false;
        }

        // Nếu không có topic con thì xóa
        if (tpDAO.delete(tp.getTpID() + "")) {
            listTp.remove(getIndex(tp));
            return true;
        }
        return false;
    }

    public boolean update(TopicDTO tp) {
        if (tpDAO.update(tp)) {
            listTp.set(getIndex(tp), tp);
            return true;
        }
        return false;
    }

    public ArrayList<TopicDTO> search(String key) {
        ArrayList<TopicDTO> result = new ArrayList<>();
        key = key.toLowerCase();

        for (TopicDTO i : listTp) {
            if (i.getTpTitle().toLowerCase().contains(key) || (i.getTpID() + "").toLowerCase().contains(key)) {
                result.add(i);
            }
        }
        return result;
    }

    // Hàm kiểm tra topic có topic con không
    private boolean hasChildTopics(int parentID) {
        for (TopicDTO topic : listTp) {
            if (topic.getTpParent() == parentID) {
                return true; // Có ít nhất một topic con
            }
        }
        return false; // Không có topic con
    }

    public ArrayList<TopicDTO> getChildTopics(int parentID) {
        ArrayList<TopicDTO> childTopics = new ArrayList<>();

        for (TopicDTO topic : listTp) {
            if (topic.getTpParent() == parentID) {
                childTopics.add(topic);
            }
        }

        return childTopics;
    }

    public ArrayList<TopicDTO> getAllChildTopics(int parentID) {
        ArrayList<TopicDTO> result = new ArrayList<>();

        for (TopicDTO topic : listTp) {
            if (topic.getTpParent() == parentID) {
                result.add(topic);
                result.addAll(getAllChildTopics(topic.getTpID())); // Gọi đệ quy
            }
        }

        return result;
    }
}
