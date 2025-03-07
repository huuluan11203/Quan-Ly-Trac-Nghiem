/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tracnghiem.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author X
 */
public interface InterfaceDAO <T> {
    boolean insert(T t) ;
    boolean update(T t) ;
    ArrayList<T> selectAll() ;
    T selectByID(String id) ; 
    boolean delete(String id);
}
