package com.placeHere.server.controller.common;

import com.placeHere.server.service.aws.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        // 경로 마지막에 / 필수
        // 경로는 예시 : user/
        // store/store/
        // point/product/

        // TODO 오류 처리

        return ResponseEntity.ok(awsS3Service.uploadFile(file, path));
    }


    // 파일 수정
    @PostMapping("/updateFile")
    public ResponseEntity<Map<String, String>> updateFile(@RequestPart("beforeFile") String beforeFile,
                                                          @RequestPart("newFile") MultipartFile newFile,
                                                          @RequestPart("path") String path) throws IOException {

        System.out.println("/api-common/updateFile : POST");

        return ResponseEntity.ok(awsS3Service.updateFile(beforeFile, newFile, path));
    }


    // 파일 삭제
    @PostMapping("/removeFile")
    public ResponseEntity<Void> removeFile(@RequestParam("filePath") String filePath) {

        System.out.println("/api-common/removeFile : POST");

        try {
            awsS3Service.deleteFile(filePath);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
