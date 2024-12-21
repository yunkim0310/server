package com.placeHere.server.service.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface AwsS3Service {

    // Method
    // 파일 업로드 - MultipartFile
    public Map<String,String> uploadFile(MultipartFile file, String path) throws IOException;

    // 파일 업로드 - File
    public Map<String,String> uploadFile(File file, String path) throws IOException;

    // 파일 삭제
    public void deleteFile(String filePath);

    // 파일 업데이트
    public Map<String, String> updateFile(String beforeFilePath, MultipartFile newFile, String path) throws IOException;

}
