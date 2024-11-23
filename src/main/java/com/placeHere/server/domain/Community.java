package com. placeHere. server. domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Community {

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
    private String username;

    // Field 댓글
    // 댓글 번호
    private int commentNo;
    // 댓글 내용
    private String commentContent;
    //댓글 등록일
    private Date commentDt;
    //최종댓글 수정일
    private Date currCommentUpdate;
    //댓글 노출 여부
    private boolean comment_ViewStatus;

}
