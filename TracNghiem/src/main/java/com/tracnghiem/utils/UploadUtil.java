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
    private static void uploadToResources(File selectedFile, String type) {
        try {
            // Đường dẫn tới thư mục resources trong dự án Maven (src/main/resources/images)
            Path destination = Paths.get("src/main/resources/images", selectedFile.getName());

            // Tạo thư mục 'images' nếu chưa có
            Files.createDirectories(destination.getParent());

            // Sao chép file vào thư mục resources
            Files.copy(selectedFile.toPath(), destination);

            System.out.println("File đã được tải lên thư mục resources: " + destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Đã xảy ra lỗi khi tải file lên.");
        }
    }
}
