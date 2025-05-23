package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.learnJava.domain.response.ResUploadDTO;
import com.example.learnJava.service.FileService;
import com.example.learnJava.utils.error.IdInvalidException;
import com.example.learnJava.utils.error.UploadException;

// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${hoidanit.upload-file.base-URI}")
    private String baseURI;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public ResponseEntity<ResUploadDTO> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, UploadException {

        // Validate
        if (file.isEmpty()) {
            throw new UploadException("File không được để trống");
        }
        String fileNameCheck = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "jpeg", "pdf", "docx", "xlsx");
        Boolean isValid = allowedExtensions.stream().anyMatch(item -> fileNameCheck.endsWith(item));

        if (!isValid) {
            throw new UploadException("File không hợp lệ, chỉ cho phép các định dạng: " + allowedExtensions.toString());
        }
        // Tạo thư mục nếu chưa tồn tại
        this.fileService.createDirectory(baseURI + folder);
        // Lưu file vào thư mục
        // Nếu không có thư mục thì sẽ tự động tạo thư mục
        String fileName = this.fileService.store(file, folder);
        ResUploadDTO resUploadDTO = new ResUploadDTO(fileName, Instant.now());
        return ResponseEntity.ok().body(resUploadDTO);
    }

    // @GetMapping("files")
    // public ResponseEntity<Resource> downloadFile(
    //         @RequestParam("fileName") String filePath,
    //         @RequestParam("folder") String folder) throws IOException {
    //     // Xử lý path hợp lệ
    //     String cleanPath = filePath.replace("file:///", "").replace("/", "\\");
    //     Path path = Paths.get(cleanPath);

    //     Resource resource = new UrlResource(path.toUri());
    //     if (!resource.exists()) {
    //         throw new FileNotFoundException("File not found: " + path);
    //     }

    //     return ResponseEntity.ok()
    //             .contentType(MediaType.APPLICATION_OCTET_STREAM)
    //             .header(HttpHeaders.CONTENT_DISPOSITION,
    //                     "attachment; filename=\"" + path.getFileName().toString() + "\"")
    //             .body(resource);
    // }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(
        @RequestParam(name = "fileName") String fileName,
        @RequestParam(name = "folder") String folder
    ) throws IOException, IdInvalidException, URISyntaxException {
        if(fileName == null || folder == null) {
            throw new IdInvalidException("Vui lòng nhập tên file và tên folder");
        }

        // check file exist
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if(fileLength == 0) {
            throw new IdInvalidException("File with name = "+ fileName + " not found");
        }
        // ...

        // download a file 
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ fileName+ "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
