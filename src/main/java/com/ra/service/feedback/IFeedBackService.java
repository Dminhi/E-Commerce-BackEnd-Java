package com.ra.service.feedback;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.FeedBackRequest;
import com.ra.model.entity.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFeedBackService {
    Page<FeedBack> findAll(Pageable pageable);
    FeedBack save(FeedBackRequest reviewRequest) throws CustomException, NotFoundException;

    FeedBack changeFeedbackStatus(Long id) throws NotFoundException, DataNotFound;
    Page<FeedBack> findAllByStatusTrue(Pageable pageable);
}
