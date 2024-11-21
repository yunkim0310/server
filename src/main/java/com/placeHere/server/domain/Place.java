package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Place {

    // Field
    // 장소 유형 (공원, 카페, 술집)
    private String placeType;
    // 장소 이름
    private String placeName;
    // 장소 사진
    private MultipartFile placeImgFile;
    private String placeImg;
    // 장소 주소
    private String placeAddr;
    // 가게와의 거리
    private int distance;


    // Method
    public void setPlaceImgFile(MultipartFile placeImgFile) {
        this.placeImgFile = placeImgFile;

        if (placeImgFile != null) {
            this.placeImg = placeImgFile.getOriginalFilename();
        }
    }

}
