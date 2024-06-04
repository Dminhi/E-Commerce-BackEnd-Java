package com.ra.service.review;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.ReviewRequest;
import com.ra.model.entity.Review;
import com.ra.repository.IReviewRepository;
import com.ra.repository.IProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    private IProductDetailRepository productDetailRepository;

    @Autowired
    private IReviewRepository reviewRepository;
    @Override
    public Page<Review> findAll(Pageable pageable) {
            return reviewRepository.findAll(pageable);
    }

    @Override
    public Review save(ReviewRequest reviewRequest) throws CustomException, NotFoundException {
        Review review = new Review();
        review.setComments(reviewRequest.getComments());
        review.setRating(reviewRequest.getRating());
        review.setStatus(false);
        review.setCreatedAt(LocalDate.now());
        review.setProductDetail(productDetailRepository.findById(reviewRequest.getProductDetailId()).orElseThrow(()->new NotFoundException("productDetail not found")));
        return reviewRepository.save(review);
    }

    @Override
    public Review changeReviewStatus(Long id) throws  DataNotFound {
        reviewRepository.changeReviewStatus(id);
        return reviewRepository.findById(id).orElseThrow(()->new DataNotFound("feedback not found"));
    }

    @Override
    public Page<Review> findAllByStatusTrue(Pageable pageable) {
        return reviewRepository.findAllByStatusIsTrue(pageable);
    }
}
