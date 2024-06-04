package com.ra.service.review;
import com.ra.exception.CustomException;
import com.ra.model.dto.response.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IReviewService {
    ResponseEntity<?> getReviews(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;
    Page<CommentResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    Page<CommentResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);

    ResponseEntity<?> changeStatus(Long id) throws CustomException;
    CommentResponseDTO findById(Long id) throws CustomException;
}
