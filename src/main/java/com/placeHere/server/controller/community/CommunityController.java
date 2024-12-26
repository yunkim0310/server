package com.placeHere.server.controller.community;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.community.FriendService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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

    @Autowired
    @Qualifier("FriendServiceImpl")
    private FriendService friendService;

    @Autowired
    private LikeService likeService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;


    // Constructor
    public CommunityController() {
        System.out.println(this.getClass());
    }


    // Method
    //  @RequestMapping (value = "/addReview.do" , method = RequestMethod.GET)
    @GetMapping("/addReview")
    public String addReview(HttpSession session, Model model) throws Exception {

        System.out.println("/addReview : Get");

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        } else {

            if (user.getRole().equals("ROLE_USER")) {
                Search search = new Search(pageSize, listSize);
                search.setSearchKeyword("이용 완료");
                search.setOrder("desc");

                List<Reservation> reservations = reservationService.getRsrvUserList(user.getUsername(), search);

                Review review = new Review();

                model.addAttribute("url", bucketUrl);
                model.addAttribute("reservations", reservations);
                model.addAttribute("review", review);
                model.addAttribute("currentUser", user);

                return "community/addReview";

            } else {
                return "redirect:/";
            }
        }

    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam("rsrvNo") int rsrvNo, @ModelAttribute("review") Review review) throws Exception {

        System.out.println("/community/addReview : Post");
        System.out.println(review);

        // B/L
        boolean result = communityService.addReview(review);

        if (result) {
            reservationService.updateRsrvStatus(rsrvNo, "리뷰 완료");
        } else {
            System.out.println("리뷰 등록 중 오류 발생");
        }

        return "redirect:/review/getReviewList?type=feed";
    }


    // getReview 리뷰 상세 조회
    @GetMapping("/getReview")
    public String getReview(@RequestParam("reviewNo") int reviewNo,
                            @ModelAttribute Search search,
                            Model model,
                            HttpSession session) throws Exception { //todo 방어코딩

        System.out.println("/review/getReview : GET");

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        search.setPageSize(pageSize);
        search.setListSize(listSize);

        Review review = communityService.getReview(reviewNo, search);

        if (review == null) {
            throw new Exception("리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
        } else {

            // 좋아요 판별
            Like chkLike = new Like();
            chkLike.setRelationNo(review.getReviewNo());
            chkLike.setTarget("review");
            chkLike.setUserName(user.getUsername());

            Like chkedLike = likeService.chkLike(chkLike);

            review.setLike(chkedLike);
        }

        // 댓글 리스트 불러오는 거
        List<Comment> commentList = communityService.getCommentList(reviewNo, search);
        int commentTotalCnt = (commentList.isEmpty()) ? 0 : commentList.get(0).getCommentTotalCnt();

        Paging paging = new Paging(commentTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());

        // TODO 변경 해야할 코드
        List<Review> reviewList = new ArrayList<Review>();
        reviewList.add(review);
        model.addAttribute("reviewList", reviewList);

        model.addAttribute("url", bucketUrl);
        model.addAttribute("paging", paging);
        model.addAttribute("review", review);
        model.addAttribute("commentList", commentList);
        model.addAttribute("user", user);

        System.out.println("getReview컨트롤러 user == " + user);
        System.out.println("cont" + review);

        return "community/getReview";
    }

    //updateReview 리뷰 수정
    @GetMapping("/updateReview")
    public String updateReview(@RequestParam("reviewNo") int reviewNo,HttpSession session ,Model model) throws Exception {

        System.out.println("/review/updateReview : GET");
        System.out.println("updateReview 페이지로 이동: reviewNo = " + reviewNo);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("updateReview 방어 코딩");
            return "redirect:/user/login";
        }


        Search search = new Search(pageSize, listSize);
        Review review = communityService.getReview(reviewNo, search);

        model.addAttribute("review", review);
        model.addAttribute("url", bucketUrl);

        return "community/updateReview";
//        return "test/community/updateTest";
    }
    
    // TODO user == null 상황확인
    @PostMapping("/updateReview")
    public String updateReview(@ModelAttribute("review") Review review) throws Exception {
        System.out.println("/review/updateReview : POST");

        // 리뷰 업데이트
        communityService.updateReview(review);

        // 업데이트 후 MyFeed로 리다이렉트, 현재 사용자 이름을 사용
        return "redirect:/review/getReviewList?type=feed";
    }


    // 리뷰 삭제
    @PostMapping("/removeReview")
    public String removeReivew(@ModelAttribute Review review) throws Exception {

        communityService.removeReview(review);

        return "redirect:/review/getReviewList?type=feed";
    }

    // TODO 2
    //getReviewList ( 리뷰 전체 목록 보기(ok) + 친구 리뷰 불러오기 + 다른사람의 리뷰리스트 , My피드 리뷰리스트(ok) ) ==> todo 현재 user정보가 없어 하드코딩 함 )
    // getReviewList.html +  getMyReviewList.html + getFriendReviewList.html + getOtherFeedView.html
    @GetMapping("/getReviewList")
    public String getReviewList(
            @ModelAttribute Search search,
            //friendUsername = friendReq + friendRes / 즉 나와 친구인 username
            @RequestParam(value = "friendUsername", required = false) String friendUsername,
            HttpSession session,
            //type의 따라 조회하는 데이터 형태를 결정 ex) type=my : 나 / type=friend : 친구
            @RequestParam(value = "type", required = false, defaultValue = "allList") String type,
            Model model) {

        System.out.println("/review/getReviewList : GET");

        User user = (User) session.getAttribute("user");

        try {

            String currentUser ="";
            if(user != null) {

                currentUser = user.getUsername();
                System.out.println("currentUser : " + currentUser);
            }

            System.out.println("요청된 페이지: " + search.getPage());
            System.out.println("친구 사용자 이름: " + friendUsername);
            System.out.println("요청 타입: " + type);

            if (type.equals("feed") && (friendUsername == null || friendUsername.isEmpty())) {
                friendUsername = user.getUsername();
            }

            // Search 객체를 생성하고 페이지 번호 및 리스트 사이즈를 설정
            search.setListSize(listSize); // 리스트 사이즈 설정
            search.setPageSize(pageSize);

            // 리뷰 리스트 초기화
            List<Review> reviewList = new ArrayList<Review>();

            String result = "";
            Paging paging = new Paging();
            int totalCnt = 0;

            switch (type) {

                case "allList":
                    // 전체 리뷰 리스트 가져오기
                    reviewList = communityService.getReviewList(search);

                    //페이징을 위한거
                    totalCnt = (reviewList.isEmpty()) ? 0 : reviewList.get(0).getReviewTotalCnt();
                    paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

                    result = "community/getReviewList";

                    break;

                // 피드
                case "feed":

                    // 로그인한 경우
                    if (user != null) {

                        // 내 피드? 남의 피드?
                        boolean isMyFeed = user.getUsername().equals(friendUsername);
                        model.addAttribute("isMyFeed", isMyFeed);

                        if (!isMyFeed) {

                            Friend friend = new Friend(user.getUsername(), friendUsername);
                            Friend chkFriend = friendService.chkFriend(friend);

                            model.addAttribute("chkFriend", chkFriend);

                            System.out.println("chkFriend = " + chkFriend);

                        }

                        model.addAttribute("feedUser", friendUsername);

                        reviewList = communityService.getReviewList(List.of(friendUsername), search);
                        totalCnt = (reviewList.isEmpty()) ? 0 : reviewList.get(0).getReviewTotalCnt();
                        paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());


                        result = "community/getFeed";
                    }

                    // 로그인 안 한 경우
                    else {
                        result = "redirect:/user/login";
                    }

                    break;

                case "friend":

                    if (user != null) {

                        // 친구 아이디 리스트
                        List<String> friendNameList = friendService.getFriendList(user.getUsername());
                        model.addAttribute("friendNameList", friendNameList);

                        if (friendNameList != null && !friendNameList.isEmpty()) {
                            // 친구 리뷰 리스트 가져오기
                            reviewList = communityService.getReviewList(friendNameList, search);
                        }

                        //페이징을 위한거
                        totalCnt = (reviewList.isEmpty()) ? 0 : reviewList.get(0).getReviewTotalCnt();
                        paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

                        result = "community/getReviewList";
                    }

                    else {
                        result = "redirect:/user/login";
                    }

                    break;

            }

            for (int i = 0; i < reviewList.size(); i++) {

                Review review = reviewList.get(i);
                if(user != null) {
                    Like chkLike = new Like();
                    chkLike.setRelationNo(review.getReviewNo());
                    chkLike.setTarget("review");
                    chkLike.setUserName(user.getUsername());

                    Like chkedLike = likeService.chkLike(chkLike);

                    review.setLike(chkedLike);

                }
                reviewList.set(i, review);

            }

            System.out.println(reviewList);

            model.addAttribute("url", bucketUrl);
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("paging", paging);
            model.addAttribute("user", user);
            model.addAttribute("type", type);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }


    // 댓글 삭제
    @PostMapping("/removeComment")
    public String removeComment(@RequestParam("commentNo") int commentNo,
                                @RequestParam("reviewNo") int reviewNo) throws Exception {

        Comment comment = new Comment();
        comment.setCommentNo(commentNo);

        System.out.println("reviewNo : " + reviewNo);

        communityService.removeComment(comment);

        return "redirect:/review/getReview?reviewNo=" + reviewNo;

    }


    // 친구신청
    @PostMapping("/sendFriendReq")
    public String sendFriendReq(@RequestParam(value = "friendRes") String friendRes,
                                @SessionAttribute("user") User user) throws Exception {

        System.out.println("/review/sendFriendReq : POST 친구 신청");
        System.out.println("friendRes : " + friendRes);
        System.out.println("친구 신청 요청: " + user.getUsername() + " -> " + friendRes);

        Friend friend = new Friend();
        friend.setFriendReq(user.getUsername());
        friend.setFriendRes(friendRes);

        // 친구 상태 확인
        Friend existingFriend = friendService.chkFriend(friend);
        if (existingFriend != null) {
            System.out.println("이미 친구 상태입니다: " + user.getUsername() + "와 " + friendRes + "는 이미 친구입니다.");
            return "redirect:/review/alreadyFriends"; // 친구인 경우 다른 페이지로 리다이렉트
        }

        friendService.sendFriendReq(friend);

        return "redirect:/review/getFriendReqStatus";
    }


    //친구삭제
    @PostMapping("/remove")
    public String removeFriend(@ModelAttribute Friend friend) {

        System.out.println("/review/remove : POST 친구 삭제");
        System.out.println("삭제할 친구 : " + friend);

        try {
            // 친구 삭제 로직 (서비스 호출)
            friendService.removeFriend(friend);

            return "redirect:/review/getFriendList";
        } catch (Exception e) {

            e.printStackTrace();

            return "error";
        }
    }

    // 친구 신청 취소
    @PostMapping("/removeFriendRequest")
    public String removeFriendRequest(@SessionAttribute("user") User user, @RequestParam int friendNo) throws Exception {
        System.out.println("친구 신청 취소 : POST ::  friendNo=" + friendNo);


        String currentUsername = user.getUsername();

        Friend friend = friendService.chkFriendByFriendNo(friendNo);

        if (friend != null) {
            friendService.removeFriendRequest(friendNo);
            System.out.println("친구 신청 취소됨.");
        } else {
            System.out.println("친구 신청 취소 중 오류.");
        }

        return "redirect:/review/getFriendReqStatus";
    }



    //친구 요청 목록을 확인하다 (getFriendReq 합침)
    @GetMapping(value = "/getFriendReqStatus")
    public String getFriendReqList(@SessionAttribute("user") User user, Model model) throws Exception {

        System.out.println("/review/getFriendReqStatus : GET");

        String userName = user.getUsername();
        System.out.println("userName : " + userName);

        Search search = new Search();
        search.setListSize(10);
        search.setStartRowNum(0);

        model.addAttribute("url", bucketUrl);

        try {

            System.out.println("친구 요청 목록을 가져옴. 사용자: " + userName);

            // 친구 요청 목록
            List<Friend> friendRequests = friendService.getFriendReqList(userName, search);
            // 수신된 친구 요청 목록
            List<Friend> receivedRequests = friendService.getFriendResList(userName, search);

            model.addAttribute("friendRequests", friendRequests);
            model.addAttribute("receivedRequests", receivedRequests);
            model.addAttribute("user", user);
            model.addAttribute("type", "friendReqStatus");

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("message", "친구 요청을 가져오는 데 오류가 발생했습니다.");
        }

        return "community/getFriendReqStatus";
    }


    //친구 신청을 수락하다
    @PostMapping("/addFriend")
    public String addFriend(@RequestParam int friendNo, RedirectAttributes redirectAttributes) {

        System.out.println("/review/addFriend : POST 친구 신청 수락");

        try {
            // 친구 추가 메서드 호출
            boolean success = friendService.addFriend(friendNo);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "친구 요청이 수락되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "친구 요청 수락에 실패했습니다.");
            }

            return "redirect:/review/getFriendList";

        } catch (Exception e) {

            e.printStackTrace();

            // 오류 발생 시 에러 페이지로 이동
            return "error";
        }
    }


    // 친구 요청 거절
    @PostMapping("/removeFriendReq")
    public String removeFriendReq(@RequestParam int friendNo, RedirectAttributes redirectAttributes) {

        System.out.println("/review/removeFriendReq : POST 친구 요청 거절");

        try {

            Friend friend = new Friend();
            friend.setFriendNo(friendNo);

            boolean success = friendService.removeFriendReq(friend);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "친구 요청이 거절되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "친구 요청 거절에 실패했습니다.");
            }

            return "redirect:/review/getFriendReqStatus";

        } catch (Exception e) {

            e.printStackTrace();

            return "error";
        }

    }


    // 친구 목록 조회
    @GetMapping("/getFriendList")
    public String getFriendList(@SessionAttribute("user") User user,
                                @ModelAttribute Search search,
                                @RequestParam(value = "keyword", required = false) String keyword, Model model) {

        System.out.println("/review/getFriendList : GET");

        model.addAttribute("url", bucketUrl);

        try {
            String username = user.getUsername();
            System.out.println("username : " + username);

            // Search 객체를 생성하고 페이지 번호 및 리스트 사이즈를 설정
            search.setPageSize(pageSize);
            search.setListSize(5);

            // 친구 목록 가져오기
            List<Friend> friends = friendService.getFriendList(username, search, keyword);

            System.out.println(friends);

            int totalCnt = (friends.isEmpty()) ? 0 : friends.get(0).getFriendTotalCnt();
            Paging paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());
            model.addAttribute("paging", paging);

            model.addAttribute("friends", friends);
            model.addAttribute("paging", paging);
            model.addAttribute("keyword", keyword);
            model.addAttribute("user", user);
            model.addAttribute("type", "friendList");

            return "community/getFriendList";

        } catch (Exception e) {

            e.printStackTrace();

            return "error"; // 오류 발생 시 에러 페이지로 이동
        }
    }


}






