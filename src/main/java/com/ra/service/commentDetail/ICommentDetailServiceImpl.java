package com.ra.service.commentDetail;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.response.CommentDetailResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.CommentDetail;
import com.ra.model.entity.Product;
import com.ra.repository.ICommentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ICommentDetailServiceImpl implements ICommentDetailService{
    @Autowired
    private ICommentDetailRepository commentDetailRepository;
    @Override
    public ResponseEntity<?> getCommentDetail(String keyword, int page, int limit, String sort, String order) throws CustomException {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<CommentDetailResponseDTO> commentDetailPage;
//        if (keyword != null) {
//            commentDetailPage = searchByNameWithPaginationAndSort( keyword, pageable);
//        } else {
            commentDetailPage = findAllWithPaginationAndSort(pageable);

//        }
        if (commentDetailPage == null || commentDetailPage.isEmpty()) {
            throw new CustomException("commentDetailPage is not found", HttpStatus.NOT_FOUND);
        }
        PageDataDTO<CommentDetailResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(commentDetailPage.getNumber());
        pageDataDTO.setTotalPage(commentDetailPage.getTotalPages());
        pageDataDTO.setLimit(commentDetailPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(commentDetailPage.getContent());
        pageDataDTO.setTotalElement(commentDetailPage.getTotalElements());
        ;
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<CommentDetailResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<CommentDetail> list = commentDetailRepository.findAll(pageable);
        return list.map(CommentDetailResponseDTO::new);
    }


    @Override
    public CommentDetailResponseDTO findById(Long id) throws CustomException {
        CommentDetail commentDetail = commentDetailRepository.findById(id).orElseThrow(() -> new CustomException("CommentDetail is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new CommentDetailResponseDTO(commentDetail);
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) throws CustomException {
        CommentDetailResponseDTO commentDetailResponseDTO = findById(id);
        if (commentDetailResponseDTO != null) {
            commentDetailRepository.changeStatus(id);
            return new ResponseEntity<>(new ResponseMapper<>(
                    HttpResponse.SUCCESS,
                    HttpStatus.OK.value(),
                    HttpStatus.OK.name(),
                    "CommentDetail change successfully !!\""
            ), HttpStatus.OK);
        } else {
            throw  new CustomException("CommentDetail is not found with this id " + id, HttpStatus.NOT_FOUND);
        }
    }
}
