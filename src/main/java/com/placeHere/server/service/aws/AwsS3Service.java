package com.placeHere.server.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    // Field
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // Method
    // 파일 업로드 (MultipartFile)
    public Map<String,String> uploadFile(MultipartFile file, String path) throws IOException {

        System.out.println("AWS S3 File Upload - MultipartFile");

        Map<String, String> map = new HashMap<String, String>();

        String filePath = null;

        if (file != null && !file.isEmpty()) {

            // 파일이름 암호화
            String now = LocalDate.now().toString().replace("-", "");
            String uuid = UUID.randomUUID().toString().split("-")[0];
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            // S3 경로 설정 (폴더 경로 + 파일 이름)
            filePath = path + now + uuid + extension;
            System.out.println(filePath);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucket, filePath, file.getInputStream(), metadata);

            System.out.println(amazonS3.getUrl(bucket, filePath).toString());
        }

        map.put("filePath", filePath);
        map.put("url", amazonS3.getUrl(bucket, filePath).toString());

        return map;
    }


    // 파일 업로드 (File)
    public Map<String,String> uploadFile(File file, String path) throws IOException {

        System.out.println("AWS S3 File Upload - File");

        Map<String, String> map = new HashMap<String, String>();

        String filePath = null;

        if (file != null && file.exists()) {

            // 파일이름 암호화
            String now = LocalDate.now().toString().replace("-", "");
            String uuid = UUID.randomUUID().toString().split("-")[0];
            String extension = file.getName().substring(file.getName().lastIndexOf("."));

            // S3 경로 설정 (폴더 경로 + 파일 이름)
            filePath = path + now + uuid + extension;
            System.out.println(filePath);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            metadata.setContentType(Files.probeContentType(file.toPath()));

            try (InputStream inputStream = new FileInputStream(file)) {
                amazonS3.putObject(bucket, filePath, inputStream, metadata);
            }

            System.out.println(amazonS3.getUrl(bucket, filePath).toString());
        }

        map.put("filePath", filePath);
        map.put("url", amazonS3.getUrl(bucket, filePath).toString());

        return map;
    }


    public void deleteFile(String filePath) {

        System.out.println("AWS S3 File Delete");

        amazonS3.deleteObject(bucket, filePath);
    }


    public Map<String, String> updateFile(String beforeFilePath, MultipartFile newFile, String path) throws IOException {

        System.out.println("AWS S3 File Update");

        deleteFile(beforeFilePath);

        return uploadFile(newFile, path);
    }


}
