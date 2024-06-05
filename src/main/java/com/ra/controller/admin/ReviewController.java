package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.ReviewRequest;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Review;
import com.ra.service.review.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api.myservice.com/v1/admin/review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @GetMapping()
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Review> reviews = reviewService.findAll(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(reviews)), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addFeedBack(@RequestBody ReviewRequest reviewRequest) throws CustomException, NotFoundException {
        Review newReview = reviewService.save(reviewRequest);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.CREATED.name(),
                HttpStatus.CREATED.value(),
                newReview), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeReviewStatus(@PathVariable Long id) throws NotFoundException, DataNotFound {
        Review review = reviewService.changeReviewStatus(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                review), HttpStatus.OK);}
}
