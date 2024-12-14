package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Review {

        // Field 리뷰
        // 리뷰번호
        private int reviewNo;
        //예약번호
        private int rsrvNo;
        // 리뷰 별점
        private int reviewScore;
        // 리뷰 사진 1
        private String reviewImg1;
        // 리뷰 사진 2
        private String reviewImg2;
        // 리뷰 사진 3
        private String reviewImg3;
        // 리뷰 내용
        private String reviewContent;
        //리뷰 등록 날짜
        private Date reviewDt;
        //최종 리뷰 수정일
        private Date currReviewUpdate;
        //리뷰 노출 여부
        private boolean reviewViewStatus;
        //회원아이디
        private String userName;

        // 리뷰 총 개수
        private int reviewTotalCnt;

        // 좋아요 개수
        private int reviewLikeCnt;

        // 댓글 개수
        private int reviewCommentCnt;

        // 댓글 리스트
        private List<Comment> commentList;

        //회원프로필
        private String profileImg;

        // 매장명
        private String storeName;
        // 가게 Id
        private int storeId;

        //Like 상태체크
        private Like like;

    }
