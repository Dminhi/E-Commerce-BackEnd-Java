package com.ra.service.comment;
import com.ra.exception.CustomException;
import com.ra.model.dto.request.BannerRequest;
import com.ra.model.dto.request.CommentRequest;
import com.ra.model.dto.response.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ICommentService {
    ResponseEntity<?> getReviews(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;
    Page<CommentResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    Page<CommentResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);

    ResponseEntity<?> changeStatus(Long id) throws CustomException;
    CommentResponseDTO findById(Long id) throws CustomException;

    Page<CommentResponseDTO> findAllByProductDetailId(Pageable pageable,Long productDetailId) throws CustomException;

    ResponseEntity<?> save(CommentRequest commentRequest) throws CustomException;

}
