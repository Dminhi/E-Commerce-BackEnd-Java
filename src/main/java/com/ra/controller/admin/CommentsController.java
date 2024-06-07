package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.service.commentDetail.ICommentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/commentDetail")
public class CommentsController {
    @Autowired
    private ICommentDetailService commentDetailService;
    @GetMapping("")
    public ResponseEntity<?> getComment(@RequestParam(name = "keyword", required = false) String keyword,
                                        @RequestParam(defaultValue = "5", name = "limit") int limit,
                                        @RequestParam(defaultValue = "0", name = "page") int page,
                                        @RequestParam(defaultValue = "status", name = "sort") String sort,
                                        @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return commentDetailService.getCommentDetail(keyword, page, limit, sort, order);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return commentDetailService.changeStatus(id);
    }


}
