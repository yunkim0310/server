package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {

    // Field 댓글
    // 리뷰 번호
    private int reviewNo;
    // 댓글 번호
    private int commentNo;
    // 댓글 내용
    private String commentsContent;

    //댓글 등록일
    private Date commentsDt;
    //최종댓글 수정일
    private Date currCommentsUpdate;
    //댓글 노출 여부
    private boolean commentViewStatus;
    //댓글 총 개수
    private int commentTotalCnt;


    //좋아요 개수
    private int commentLikeCnt;


    //회원프로필
    private String profileImg;
    //회원아이디
    private String userName;



}

