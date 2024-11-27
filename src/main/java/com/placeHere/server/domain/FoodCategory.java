package com.placeHere.server.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FoodCategory {

    // Field
    private final List<String> mainCategory;
    private final List<String> subCategory;
    private final Map<String, List<String>> detailCategory;


    // Constructor
    public FoodCategory() {

        mainCategory = new ArrayList<String>(List.of("한식", "중식", "일식", "양식", "퓨전", "기타"));
        subCategory = new ArrayList<String>(List.of("전체", "밥", "빵-과자", "면-만두", "죽-스프", "국-탕", "찌개", "찜", "구이", "전-부침", "볶음", "조림", "튀김", "나물-무침", "회", "기타"));
        detailCategory = new HashMap<String, List<String>>();
        detailCategory.put("전체", List.of("전체"));
        detailCategory.put("기타", List.of("기타"));
        detailCategory.put("밥", List.of("전체", "솥밥", "비빔밥", "볶음밥", "덮밥", "국밥", "김밥", "기타"));
        detailCategory.put("빵-과자", List.of("전체", "빵", "과자", "기타"));
        detailCategory.put("면-만두", List.of("전체", "면", "만두", "기타"));
        detailCategory.put("죽-스프", List.of("전체", "죽", "스프", "기타"));
        detailCategory.put("국-탕", List.of("전체", "맑은국", "된장국", "곰국", "탕", "냉국", "기타"));
        detailCategory.put("찌개", List.of("전체", "어패류찌개", "육류찌개", "된장찌개", "전골", "기타"));
        detailCategory.put("찜", List.of("전체", "어패류찜", "육류찜", "채소찜", "기타"));
        detailCategory.put("구이", List.of("전체", "어패류구이", "육류구이", "기타"));
        detailCategory.put("전-부침", List.of("전체", "어패류전", "어류전", "채소류전", "부침류", "기타"));
        detailCategory.put("볶음", List.of("전체", "어패류볶음", "육류, 난류볶음", "채소류볶음", "곡류, 두류볶음", "기타"));
        detailCategory.put("조림", List.of("전체", "어패류조림", "육류, 난류조림", "채소류조림", "두류조림", "기타"));
        detailCategory.put("튀김", List.of("전체", "어패류튀김", "육류튀김", "채소, 해조류튀김", "기타"));
        detailCategory.put("나물-무침", List.of("전체", "생채류", "샐러드류", "숙채류", "어패류무침", "육류무침", "기타"));
        detailCategory.put("회", List.of("전체", "어패류회", "육류회", "스시, 초밥", "기타"));

    }
}
