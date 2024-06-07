package com.ra.service.comment;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.CommentRequest;
import com.ra.model.dto.response.CommentResponseDTO;
import com.ra.model.entity.Comment;
import com.ra.repository.ICommentRepository;
import com.ra.repository.IProductDetailRepository;
import com.ra.repository.IProductRepository;
import com.ra.repository.IUserRepository;
import com.ra.service.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductDetailRepository productDetailRepository;
    @Override
    public ResponseEntity<?> getReviews(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<CommentResponseDTO> commentPage;
        if (keyword != null) {
            commentPage = searchByNameWithPaginationAndSort(  pageable,keyword);
        } else {
            commentPage = findAllWithPaginationAndSort(pageable);

        }
        if (commentPage == null || commentPage.isEmpty()) {
            throw new CustomException("commentPage is not found", HttpStatus.NOT_FOUND);
        }
        PageDataDTO<CommentResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(commentPage.getNumber());
        pageDataDTO.setTotalPage(commentPage.getTotalPages());
        pageDataDTO.setLimit(commentPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(commentPage.getContent());
        pageDataDTO.setTotalElement(commentPage.getTotalElements());
        ;
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<CommentResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Comment> list = commentRepository.findAll(pageable);
        return list.map(CommentResponseDTO::new);

    }

    @Override
    public Page<CommentResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Comment> list = commentRepository.findByCommentContaining( name, pageable);
        return list.map(CommentResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) throws CustomException {
        CommentResponseDTO commentResponseDTO = findById(id);
        if (commentResponseDTO != null) {
            commentRepository.changeStatus(id);
            return new ResponseEntity<>(new ResponseMapper<>(
                    HttpResponse.SUCCESS,
                    HttpStatus.OK.value(),
                    HttpStatus.OK.name(),
                    "Comment change successfully !!\""
            ), HttpStatus.OK);
        } else {
            throw  new CustomException("Comment is not found with this id " + id, HttpStatus.NOT_FOUND);
        }

    }
    @Override
    public CommentResponseDTO findById(Long id) throws CustomException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException("Product is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new CommentResponseDTO(comment);
    }

    @Override
    public Page<CommentResponseDTO> findAllByProductDetailId(Pageable pageable,Long productDetailId) {
        Page<Comment> comments = commentRepository.findAllByProductDetailId(productDetailId,pageable);
        return comments.map(CommentResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> save(CommentRequest commentRequest) throws CustomException {
        Comment comment = commentRepository.save(Comment.builder()
                .id(commentRequest.getId())
                .comment(commentRequest.getComment())
                .createdAt(LocalDateTime.now())
                .user(userRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new CustomException("User is not found with this id " + commentRequest.getUserId(), HttpStatus.NOT_FOUND)))
                .productDetail(productDetailRepository.findById(commentRequest.getProductDetailId()).orElseThrow(() -> new CustomException("ProductDetail is not found with this id " + commentRequest.getProductDetailId(), HttpStatus.NOT_FOUND)))
                .status(true).build());
        return new ResponseEntity<>(
                new ResponseMapper<>(HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        new CommentResponseDTO(comment)), HttpStatus.CREATED);
    }
}
