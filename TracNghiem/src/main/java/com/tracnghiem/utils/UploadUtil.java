/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author X
 */
public class UploadUtil {
    public static boolean uploadToResources(File selectedFile) {
        boolean rs = true;
        try {
            // Đường dẫn tới thư mục resources trong dự án Maven (src/main/resources/images)
            Path destination = Paths.get("src/main/resources/images", selectedFile.getName());

            // Tạo thư mục 'images' nếu chưa có
            Files.createDirectories(destination.getParent());

            // Sao chép file vào thư mục resources
            Files.copy(selectedFile.toPath(), destination);
            
        } catch (IOException e) {
            e.printStackTrace();
            rs = false;
            System.out.println("Đã xảy ra lỗi khi tải file lên.");
        }
        return rs;
    }
}
