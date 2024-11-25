package com.placeHere.server.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    // Field
    // 메뉴 ID (PK, AI)
    private int menuId;
    // 가게 ID (FK)
    private int storeId;
    // 메뉴 번호
    private int menuNo;
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
