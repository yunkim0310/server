package com.placeHere.server.controller.common;

import com.placeHere.server.service.aws.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api-common/*")
public class CommonRestController {

    // Field
    @Autowired
    private AwsS3Service awsS3Service;


    // Constructor
    public CommonRestController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    // 파일 업로드
    @PostMapping("/uploadFile")
    public ResponseEntity<Map<String,String>> uploadFile(@RequestPart("file") MultipartFile file,
                                                         @RequestPart("path") String path) throws IOException {

        System.out.println("/api-common/uploadFile : POST");

        if (path.equals("store")) {
            path = "store/store/";
        } else if (path.equals("menu")) {
            path = "store/menu/";
        } else if (path.equals("news")) {
            path = "store/news/";
        } else {
            path = path + "/";
        }

        return ResponseEntity.ok(awsS3Service.uploadFile(file, path));
    }


    // 파일 수정
    @PostMapping("/updateFile")
    public ResponseEntity<Map<String, String>> updateFile(@RequestPart("beforeFile") String beforeFile,
                                                          @RequestPart("newFile") MultipartFile newFile,
                                                          @RequestPart("path") String path) throws IOException {

        System.out.println("/api-common/updateFile : POST");

        if (path.equals("store")) {
            path = "store/store/";
        } else if (path.equals("menu")) {
            path = "store/menu/";
        } else if (path.equals("news")) {
            path = "store/news/";
        } else {
            path = path + "/";
        }

        return ResponseEntity.ok(awsS3Service.updateFile(beforeFile, newFile, path));
    }

}
