package com.placeHere.server.controller.community;

import com.placeHere.server.dao.community.CommunityDao;
import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Review;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review/*")
public class CommunityController {

    //Field
    @Autowired
    @Qualifier("communityServiceImpl")
    private CommunityService communityService;

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

    public CommunityController() {
        System.out.println(this.getClass());
    }

    //  @RequestMapping (value = "/addReview.do" , method = RequestMethod.GET)
    @GetMapping("/addReview")
    public String addReview(@RequestParam("rsrvNo") int rsrvNo,
                            @RequestParam("userName") String userName,
                            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                            @RequestParam(value = "order", required = false) String order,
                            Model model) throws Exception {

        System.out.println("/addReview.do");

        Search search = new Search();

        // 조건 설정: 값이 있을 때만 설정
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            search.setSearchKeyword(searchKeyword);
        }
        if (order != null && !order.isEmpty()) {
            search.setOrder(order);
        }


        Reservation reservation = reservationService.getRsrv(rsrvNo);
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        Review review = new Review();
        model.addAttribute("reservations", reservations);
        model.addAttribute("reservation", reservation);
        model.addAttribute("review", review);

        return "test/community/addReview";
    }

    @RequestMapping(value = "/addReview", method = RequestMethod.POST)
    public String addReview(@ModelAttribute("review") Review review) throws Exception {

        System.out.println("/addReview.do" + review.toString());
        // B/L
        communityService.addReview(review);

        return "test/community/listreview";
    }

    // getReview
    @GetMapping("/getReview")
    public String getReview(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {
        System.out.println("community/getReview : GET");

        Review review = communityService.getReview(reviewNo);

        if (review == null) {
            throw new Exception("리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
        }

        model.addAttribute("review", review);
        List<Comment> comments = communityService.getCommentList(reviewNo);
        model.addAttribute("commentList", comments);

        return "test/community/getReview";
    }

    //add Comment
    @PostMapping("/review/addComment")
    public String addComment(@ModelAttribute Comment comment, Model model) {
        try {
            // 댓글 추가
            communityService.addComment(comment);

            // 댓글이 추가된 리뷰의 상세 정보를 다시 얻음
            Review review = communityService.getReview(comment.getReviewNo());
            model.addAttribute("review", review);

            // 새로운 댓글 목록 추가
            List<Comment> comments = communityService.getCommentList(comment.getReviewNo());
            model.addAttribute("commentList", comments);

        } catch (Exception e) {
            // 오류 처리 로직
            e.printStackTrace();
            model.addAttribute("errorMessage", "댓글 추가 중 오류가 발생했습니다.");
        }

        // 리뷰 상세 페이지로 리턴
        return "redirect: test/community/getReview";
    }

    //getComment
    @PostMapping("/getComment")
    public String getComment(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception{
        System.out.println("getComment" + reviewNo);

        List<Comment> comments = communityService.getCommentList(reviewNo);

        model.addAttribute("CommentList" , comments);

        Review review = communityService.getReview(reviewNo);
        model.addAttribute("review", review);

        return "test/community/getReview";
    }

//updateReview
//@GetMapping("/updateReview")
//public String updateReview(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {
//    System.out.println("updateReview 페이지로 이동: reviewNo = " + reviewNo);
//
//    Review review = communityService.getReview(reviewNo);
//    model.addAttribute("review", review);
//    return "test/community/updateReview";
//}
//
//@PostMapping("/updateReview") // POST 방식으로 처리
//public String updateReview(@ModelAttribute("review") Review review) throws Exception {
//    System.out.println("/community/updateReview : POST");
//
//    communityService.updateReview(review);
//    return "redirect:/review/getReview?reviewNo=" + review.getReviewNo();
//}
//
//
////    // 리뷰 삭제
//    @PostMapping("/removeReview")
//    public String removeReview(@RequestParam("reviewNo") int reviewNo) throws Exception {
//
//        Review review = new Review();
//        review.setReviewNo(reviewNo);
//
//        communityService.removeReview(review);
//        return "redirect:/review/getReviewList"; // 리뷰 목록으로 리다이렉션
//
//    }
//
//    //getReviewList
//    @GetMapping("/getReviewList")
//    public String getReviewList(Model model) throws Exception {
//        System.out.println("/community/getReviewList : GET");
//
//        List<Review> reviewList = communityService.getReviewList(new Search());
//
//        model.addAttribute("community", reviewList);
//        return "test/community/getReviewList";
//    }
//




}

