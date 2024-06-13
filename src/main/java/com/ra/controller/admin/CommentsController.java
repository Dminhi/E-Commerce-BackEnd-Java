package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.CustomException;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Banner;
import com.ra.model.entity.Comment;
import com.ra.repository.ICommentRepository;
import com.ra.service.commentDetail.ICommentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/comment")
public class CommentsController {
    @Autowired
    private ICommentRepository commentRepository;
    @GetMapping()
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(comments)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws NotFoundException, RequestErrorException, CustomException {
        commentRepository.changeStatus(id);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException("Comment is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                comment), HttpStatus.OK);
    }
}
