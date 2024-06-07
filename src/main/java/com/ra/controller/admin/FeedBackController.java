package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.FeedBackRequest;
import com.ra.model.dto.request.ReviewRequest;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.FeedBack;
import com.ra.model.entity.Review;
import com.ra.service.feedback.IFeedBackService;
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
@RequestMapping("/api.myservice.com/v1/admin/feedback")
public class FeedBackController {

    @Autowired
    private IFeedBackService feedBackService;

    @GetMapping()
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FeedBack> feedBacks = feedBackService.findAll(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(feedBacks)), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addFeedBack(@RequestBody FeedBackRequest feedBackRequest) throws CustomException, NotFoundException {
        FeedBack newFeedback = feedBackService.save(feedBackRequest);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.CREATED.name(),
                HttpStatus.CREATED.value(),
                newFeedback), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeFeedbackStatus(@PathVariable Long id) throws NotFoundException, DataNotFound {
        FeedBack feedBack = feedBackService.changeFeedbackStatus(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                feedBack), HttpStatus.OK);}
}
