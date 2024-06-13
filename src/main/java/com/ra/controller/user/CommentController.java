package com.ra.controller.user;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.BannerRequest;
import com.ra.model.dto.request.CommentRequest;
import com.ra.model.dto.response.CommentResponseDTO;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Banner;
import com.ra.service.comment.ICommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user/comment")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findCommentByProductDetailId(@PathVariable Long id,@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) throws  CustomException {
        Page<CommentResponseDTO> commentResponseDTO = commentService.findAllByProductDetailId(pageable,id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                commentResponseDTO), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commentRequest) throws CustomException {
        return commentService.save(commentRequest);
    }
}
