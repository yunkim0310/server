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
public class Menu {

    // Field
    // 메뉴 번호 (PK)
    private int menuNo;
    // 가게 ID (FK)
    private int storeId;
    // 메뉴 사진
    private MultipartFile menuImgFile;
    private String menuImg;
    // 메뉴 이름
    private String menuName;
    // 메뉴 가격
    private int menuPrice;
    // 메뉴 소개
    private String menuInfo;

    public void setMenuImgFile(MultipartFile menuImgFile) {

        this.menuImgFile = menuImgFile;

        if (menuImgFile != null) {
            this.menuImg = menuImgFile.getOriginalFilename();
        }
    }
}
