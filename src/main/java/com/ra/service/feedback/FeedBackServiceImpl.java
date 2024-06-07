package com.ra.service.feedback;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.FeedBackRequest;
import com.ra.model.entity.FeedBack;
import com.ra.model.entity.Review;
import com.ra.repository.IFeedBackRepository;
import com.ra.repository.IOrderRepository;
import com.ra.repository.IProductDetailRepository;
import com.ra.service.review.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FeedBackServiceImpl implements IFeedBackService {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IFeedBackRepository feedBackRepository;
    @Override
    public Page<FeedBack> findAll(Pageable pageable) {
            return feedBackRepository.findAll(pageable);
    }

    @Override
    public FeedBack save(FeedBackRequest reviewRequest) throws CustomException, NotFoundException {
        FeedBack feedBack = new FeedBack();
        feedBack.setFeedback(reviewRequest.getFeedback());
        feedBack.setRating(reviewRequest.getRating());
        feedBack.setStatus(false);
        feedBack.setCreatedAt(LocalDate.now());
        feedBack.setOrders(orderRepository.findById(reviewRequest.getOrderId()).orElseThrow(()->new NotFoundException("order not found")));
        return feedBackRepository.save(feedBack);
    }

    @Override
    public FeedBack changeFeedbackStatus(Long id) throws  DataNotFound {
        feedBackRepository.changeFeedbackStatus(id);
        return feedBackRepository.findById(id).orElseThrow(()->new DataNotFound("feedback not found"));
    }

    @Override
    public Page<FeedBack> findAllByStatusTrue(Pageable pageable) {
        return feedBackRepository.findAllByStatusIsTrue(pageable);
    }
}
