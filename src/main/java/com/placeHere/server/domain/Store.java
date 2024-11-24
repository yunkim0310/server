package com.placeHere.server.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    // Field
    // 가게 ID (PK)
    private int storeId;
    // 회원 아이디
    private String userName;
    // 사업자 번호 (숫자 10글자)
    private String businessNo;
    // 매장명
    private String storeName;
    // 매장 주소
    private String storeAddr;
    // 매장 전화번호
    private String storePhone;
    // 매장 사진 목록 (매장 대표 사진은 첫번째꺼)
    private MultipartFile[] storeImgFiles;
    private List<String> storeImgList;
    private String storeImg1;
    private String storeImg2;
    private String storeImg3;
    private String storeImg4;
    private String storeImg5;
    // 매장 소개
    private String storeInfo;
    // 해시태그 목록
    private List<String> hashtagList;
    private String hashtag;
    // 편의시설 목록
    /* ①주차 가능, ②발렛 가능, ③콜키지 프리, ④콜키지 가능,
        ⑤키즈존, ⑥노키즈존, ⑦반려동물 동반, ⑧유아시설,
        ⑨장애인 편의시설 ⑩대관 가능, ⑪이벤트 가능, ⑫단체 이용가능
        단, ③콜키지 프리와 ④콜키지 가능은 동시 선택불가, ⑤키즈존과 ⑥노키즈존 동시 선택 불가 */
    private List<Integer> amenitiesNoList;
    // 음식 카테고리 ID (숫자6글자 + 세부분류) (숫자 2글자 단위로 1,2,3차 분류) ex: 010101/장어솥밥
    private String foodCategoryId;
    private String foodCategory;
    // 메뉴 목록
    private List<Menu> menuList;
    // 대표 메뉴 번호 (메뉴 번호 순서중 하나)
    private int specialMenuNo;
    // 포트원 가게 ID
    private String portOneStoreId;
    // 가게 운영 Class (오픈시간, 마감시간, 브레이크타임, 정기 휴무요일 목록, 예약 보증금, 예약 최대인수, 적용일, 휴무일 목록)
    private StoreOperation storeOperation;
    // 매장 소식 목록 (소식 ID, 가게 ID, 소식 사진, 소식 내용, 작성일시)
    private List<StoreNews> storeNewsList;
    // 리뷰 평균 별점
    private double reviewScoreAvg;
    // 리뷰수
    private int reviewCnt;
    // 가게 상태(0이면 점주회원, 1이면 탈퇴회원)
    private int storeStatus;


    // Method
    public void setStoreImgFiles(MultipartFile[] storeImgFiles) {

        this.storeImgFiles = storeImgFiles;

        List<String> fileNameList = new ArrayList<>();

        if (storeImgFiles != null) {
            for(MultipartFile storeImgFile : storeImgFiles) {
                fileNameList.add(storeImgFile.getOriginalFilename());
            }
        }

        this.storeImgList = fileNameList;
    }

    public void setHashtagList(List<String> hashtagList) {
        this.hashtagList = hashtagList;

        String fullHashtag = "";

        if (hashtagList != null) {

            for (String hashtag : hashtagList) {
                fullHashtag += "#"+hashtag;
            }

        }

        this.hashtag = fullHashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;

        List<String> hashtagList = new ArrayList<>();

        if (hashtag != null && !hashtag.isEmpty()) {

            for (String hash : hashtag.split("#")) {
                if (!hash.isEmpty()) {
                    hashtagList.add(hash.trim());
                }
            }

            this.hashtagList = hashtagList;

        }


    }

}
