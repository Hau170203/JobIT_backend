package com.example.learnJava.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${hoidanit.upload-file.base-URI}")
    private String baseURI;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDIr = new File(path.toString());
        if (!tmpDIr.isDirectory()) {
           try {
            Files.createDirectory(tmpDIr.toPath());
            System.out.println("Directory created: " + tmpDIr.toPath());
           } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
           }
        } else {
            System.out.println("Directory already exists: " + tmpDIr.getAbsolutePath());
        }
    }

    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
       
        String fileName = file.getOriginalFilename();
        String safeFileName = fileName.replaceAll(" ", "_"); // hoặc dùng URLEncoder
        
        // Tiếp tục lưu file như bình thường
        // Tạo tên file mới với định dạng thời gian hiện tại + tên file gốc
        // Ví dụ: 1678901234567_filename.jpg
        String finalName =System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Tạo đường dẫn đầy đủ đến thư mục và tên file
        // Ví dụ: /path/to/storage/folder/1678901234567_filename.jpg
        URI uri = new URI(baseURI + folder+ "/" +safeFileName);

        // Tạo đường dẫn đến thư mục
        // Ví dụ: /path/to/storage/folder
        Path path = Paths.get(uri);
        
        // lưu file vào thư mục
        try (InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    // public void downloadFile(String fileName, String folder) throws URISyntaxException, IOException {
    //     // Tạo đường dẫn đầy đủ đến thư mục và tên file
    //     // Ví dụ: /path/to/storage/folder/1678901234567_filename.jpg
    //     URI uri = new URI(baseURI + folder+ "/" +fileName);

    //     // Tạo đường dẫn đến thư mục
    //     // Ví dụ: /path/to/storage/folder
    //     Path path = Paths.get(uri);
        
    //     // lưu file vào thư mục
    //     try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)){
    //         Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    //     }
    // }

    public long getFileLength(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(baseURI + folder+ "/" +fileName);
        Path path = Paths.get(uri);
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            return 0;
        }
        return file.length();
    }

    public InputStreamResource getResource(String fileName, String folder) throws URISyntaxException, FileNotFoundException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File file = new File(path.toString());
        return new InputStreamResource(new FileInputStream(file));
    }

}
