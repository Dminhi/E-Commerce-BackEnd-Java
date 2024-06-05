package com.ra.service.commentDetail;

import com.ra.exception.CustomException;
import com.ra.model.dto.response.CommentDetailResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ICommentDetailService {
    ResponseEntity<?> getCommentDetail(String keyword, int page, int limit, String sort, String order) throws CustomException;
    Page<CommentDetailResponseDTO> findAllWithPaginationAndSort(Pageable pageable);

//    Page<CommentDetailResponseDTO> searchByNameWithPaginationAndSort( String name,Pageable pageable);
    CommentDetailResponseDTO findById(Long id) throws CustomException;

    ResponseEntity<?> changeStatus(Long id) throws CustomException;
}
