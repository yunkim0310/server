package com.placeHere.server.controller.community;


import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Like;
import com.placeHere.server.domain.Review;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.community.FriendService;
import com.placeHere.server.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")


public class CommunityRestController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FriendService friendService;

    //댓글 작성
    @PostMapping("/addComment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {

        System.out.println("comment chk 11 :: " + comment);

        comment.setUserName("user01");

        System.out.println("comment chk 22 ::" + comment);


        try {
            communityService.addComment(comment);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    //좋아요 추가
    @PostMapping("/addLike")
    public ResponseEntity<String> addLike(@RequestBody Like like) {
        try {
            // 현재 사용자의 좋아요 상태 확인
            if (likeService.chkLike(like) != null) {
                // 좋아요가 존재하면 취소
                likeService.removeLike(like);
                return ResponseEntity.ok("좋아요가 취소 되었습니다");
            } else {
                //좋아요가 없으면 추가
                likeService.addLike(like.getUserName(), like.getRelationNo(), like.getTarget());
                return ResponseEntity.ok("좋아요가 추가되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("좋아요 오류 ");
        }
    }

    //좋아요 체크
    @PostMapping("/chkLike")
    public ResponseEntity<Boolean> chkLike(@RequestBody Like like) {
        try {
            // 입력값 유효성 검사
            if (like == null || like.getUserName() == null || like.getUserName().isEmpty() ||
                    like.getRelationNo() <= 0 || like.getTarget() == null || like.getTarget().isEmpty()) {
                return ResponseEntity.badRequest().body(false); // 잘못된 입력
            }
            // target 값 유효성 검사 (필요에 따라 추가)

            Like result = likeService.chkLike(like);
            return ResponseEntity.ok(result != null); // 결과가 있으면 true, 없으면 false

        } catch (Exception e) {
            // 예외 발생 시 에러 로그를 기록하고 적절한 응답을 반환
            System.err.println("Error checking like: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // 서버 에러
        }
    }

    //좋아요 취소
    @PostMapping("/removeLike")
    public ResponseEntity<String> removeLike(@RequestBody Like like) {
        try {
            // 입력값 유효성 검사 (필요에 따라 추가)
            if (like == null || like.getLikeId() == 0) {
                return ResponseEntity.badRequest().body("잘못된 입력입니다.");
            }

            if (likeService.removeLike(like)) {
                return ResponseEntity.ok("좋아요가 취소되었습니다.");
            } else {
                return ResponseEntity.ok("좋아요 취소 실패"); // 데이터베이스 에러 등
            }
        } catch (Exception e) {
            System.err.println("Error removing like: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }


//친구 삭제 POST방법 => 실패
//    @PostMapping("/removeFriend")
//    public ResponseEntity<String> removeFriend(@RequestParam("friendNo") int friendNo) {
//        try {
//            // 친구 삭제 로직
//            boolean success = friendService.removeFriendReq(friendNo);
//            if (success) {
//                return ResponseEntity.ok("친구가 삭제되었습니다.");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 삭제에 실패했습니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
//        }
//    }

    // 친구 삭제 실패 버전
//    @GetMapping("/removeFriend")
//    public ResponseEntity<String> removeFriend(@RequestParam("friendNo") int friendNo, Model model) {
//        try {
//            // 친구 삭제 로직
//            boolean success = friendService.removeFriend(friendNo);
//            model.addAttribute("friendNo", friendNo);
//
//            if (success) {
//                return ResponseEntity.ok("친구가 삭제되었습니다.");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 삭제에 실패했습니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
//        }
//    }

    @DeleteMapping("/removeFriend")
    public ResponseEntity<String> removeFriend(@RequestParam("friendNo") int friendNo) {
        try {
            // 친구 삭제 로직
            boolean success = friendService.removeFriendReq(friendNo);
            if (success) {
                return ResponseEntity.ok("친구가 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("친구 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시 로그를 기록
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류가 발생했습니다.");
        }
    }


    //댓글 수정
    @PutMapping("/updateComment")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        try {
            communityService.updateComment(comment);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // 댓글 삭제 -> 노출여부 T => F 로 변경
    @DeleteMapping("/removeComment/{commentNo}")
    public ResponseEntity<Void> removeComment(@RequestBody int commentNo) {
        try {
            // 댓글 객체 생성
            Comment comment = new Comment();
            comment.setCommentNo(commentNo);


            // 삭제 처리
            communityService.removeComment(comment);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // 서버 오류 발생 시 500 상태 코드 반환
        }
    }
}



//    //addLike
//    @PostMapping("/addLike")
//    public ResponseEntity<Like> addLike(@RequestBody String userName, String target) {
//
//


//    @PostMapping("/removeReview")
//    public ResponseEntity<String> removeReview(@RequestParam("reviewNo") int reviewNo) {
//        try {
//            // 리뷰의 상태값을 0으로 변경
//            communityService.removeReview(new Review(reviewNo));
//            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("리뷰 삭제 중 오류가 발생.");
//        }
//    }
//}

//    //    //리뷰 삭제
//    @PostMapping("/removeReview")
//    public String removeReview(@RequestParam("reviewNo") int reviewNo) throws Exception {
//        Review review = new Review();
//        review.setReviewNo(reviewNo);
//
//        //리뷰 삭제 메서드 => 리뷰 상테 값 변경
//        communityService.removeReview(review);
//
//        return "redirect:/review/getReviewList";
//    }


