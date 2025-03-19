/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.LogDAO;
import com.tracnghiem.dto.LogDTO;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class LogBUS {
    private final LogDAO lDAO = LogDAO.getInstance();
    private ArrayList<LogDTO> listLogs;

    public LogBUS() {
        listLogs = lDAO.selectAll();
    }

    public ArrayList<LogDTO> getAll() {
        return lDAO.selectAll();
    }

    public LogDTO findOne(String id) {
        return lDAO.selectByID(id);
    }

    public int getIndex(int id) {
        for (int i = 0; i < listLogs.size(); i++) {
            if (listLogs.get(i).getLogID() == id) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(LogDTO log) {
        if (lDAO.insert(log)) {
            listLogs.add(log);
            return true;
        }
        return false;
    }
    
    
}
