/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.bus;

import com.tracnghiem.dao.UserDAO;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.utils.PasswordUtil;
import com.tracnghiem.utils.PatternMatcherUtil;
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
        return uDAO.selectAll();
    }

    public UserDTO findOne(int id) {
        return uDAO.selectByID(id + "");
    }

    public UserDTO findOneByUserName(String user) {
        return uDAO.selectByUserName(user);
    }

    public int getIndex(UserDTO u) {
        for (int i = 0; i < this.listUser.size(); i++) {
            if (this.listUser.get(i).getUserID() == u.getUserID()) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(UserDTO u) {
        if (uDAO.insert(u)) {
            this.listUser.add(u);
            return true;
        }
        return false;
    }

    public boolean delete(UserDTO u) {
        if (uDAO.delete(u.getUserID() + "")) {
            this.listUser.remove(getIndex(u));
            return true;
        }
        return false;
    }

    public boolean update(UserDTO u) {
        if (uDAO.update(u)) {
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
                    if (PatternMatcherUtil.patternMatcherEmail(key, i)
                            || PatternMatcherUtil.patternMatcherFullName(key, i)
                            || PatternMatcherUtil.patternMatcherUserName(key, i)) {
                        result.add(i);
                    }
                }
            }
            case "Họ tên" -> {
                for (UserDTO i : this.listUser) {
                    if (PatternMatcherUtil.patternMatcherFullName(key, i)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (UserDTO i : this.listUser) {
                    if (PatternMatcherUtil.patternMatcherEmail(key, i)) {
                        result.add(i);
                    }
                }
            }
            case "Tên đăng nhập" -> {
                for (UserDTO i : this.listUser) {
                    if (PatternMatcherUtil.patternMatcherUserName(key, i)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public UserDTO login(String username, String password) {
        listUser = uDAO.selectAll();
        for (UserDTO user : listUser) {
            if (user.getUserName().equals(username)) {
                if (PasswordUtil.checkPassword(password, user.getUserPassword())) {
                    return user; // Đăng nhập thành công
                }
            }
        }
        return null; // Sai tài khoản hoặc mật khẩu
    }

    public boolean isExistUserName(String user) {
        for (UserDTO u : listUser) {
            if (u.getUserName().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExistEmail(String email) {
        for (UserDTO u : listUser) {
            if (u.getUserEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
