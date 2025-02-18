/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.UserDAO;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.utils.PasswordUtil;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class UserBUS {
    private final UserDAO uDAO = UserDAO.getInstance();
    public ArrayList<UserDTO> listUser = new ArrayList<>();

    public UserBUS() {
        this.listUser = uDAO.selectAll();
    }
    
     public ArrayList<UserDTO> getAll() {
        return this.listUser;
    }
    
    public UserDTO findOne(int id) {
        return uDAO.selectByID(id+"");
    }
    
    public int getIndex(UserDTO u) {
        for (int i = 0; i < this.listUser.size(); i++) {
            if (this.listUser.get(i).getUserID()== u.getUserID()) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean add(UserDTO u) {
        // Mã hóa mật khẩu bằng MD5 trước khi lưu
        u.setUserPassword(PasswordUtil.hashPassword(u.getUserPassword()));

        if (uDAO.insert(u)) {
            this.listUser.add(u);
            return true;
        }
        return false;
    }


    public boolean delete(UserDTO u) {
        if (uDAO.delete(u.getUserID()+ "")) {
            this.listUser.remove(getIndex(u));
            return true;
        }
        return false;
    }
    public boolean update(UserDTO u) {
        // Kiểm tra nếu mật khẩu đã được cập nhật thì mã hóa lại
        UserDTO existingUser = findOne(u.getUserID());
        if (!u.getUserPassword().equals(existingUser.getUserPassword())) {
            u.setUserPassword(PasswordUtil.hashPassword(u.getUserPassword()));
        }

        if (uDAO.update(u)) {
            this.listUser.set(getIndex(u), u);
            return true;
        }
        return false;
    }
    
    public ArrayList<UserDTO> search(String key, String type) {
        ArrayList<UserDTO> result = new ArrayList<>();
        key = key.toLowerCase();

        switch (type) {
            case "Tất cả" -> {
                for (UserDTO i : this.listUser) {
                    if (i.getUserEmail().toLowerCase().contains(key) 
                            || i.getUserFullName().toLowerCase().contains(key)
                            || i.getUserName().toLowerCase().contains(key)
                    ) {
                        result.add(i);
                    }
                }
            }
            case "Họ tên" -> {
                for (UserDTO i : this.listUser) {
                    if (i.getUserFullName().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (UserDTO i : this.listUser) {
                    if (i.getUserEmail().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            case "Username" -> {
                for (UserDTO i : this.listUser) {
                    if (i.getUserName().toLowerCase().contains(key)) {
                        result.add(i);
                    }
                }
            }
            
        }

        return result;
    }
    
    public UserDTO login(String username, String password) {
    for (UserDTO user : listUser) {
        if (user.getUserName().equals(username)) {
            if (PasswordUtil.checkPassword(password, user.getUserPassword())) {
                return user; // Đăng nhập thành công
            }
        }
    }
    return null; // Sai tài khoản hoặc mật khẩu
}
    
}
