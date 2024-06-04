package com.ra.service.review;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.ReviewRequest;
import com.ra.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewService {
    Page<Review> findAll(Pageable pageable);
    Review save(ReviewRequest reviewRequest) throws CustomException, NotFoundException;

    Review changeReviewStatus(Long id) throws NotFoundException, DataNotFound;
    Page<Review> findAllByStatusTrue(Pageable pageable);
}
