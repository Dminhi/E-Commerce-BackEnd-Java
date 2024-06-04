package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.service.review.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;


    @GetMapping("")
    public ResponseEntity<?> getReview(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return reviewService.getReviews(keyword, page, limit, sort, order);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return reviewService.changeStatus(id);
    }
}

