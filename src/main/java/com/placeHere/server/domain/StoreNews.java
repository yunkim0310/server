package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreNews {

    // Field
    // 소식 ID (PK)
    private int newsId;
    // 가게 ID (FK)
    private int storeId;
    // 소식 사진
    private MultipartFile newsImgFile;
    private String newsImg;
    // 소식 내용
    private String newsContents;
    // 작성일시 (now()?, sysdate())
    private LocalDateTime regDt;


    // Method
    public void setNewsImgFile(MultipartFile newsImgFile) {
        this.newsImgFile = newsImgFile;

        if (newsImgFile != null) {
            this.newsImg = newsImgFile.getOriginalFilename();
        }
    }

}
