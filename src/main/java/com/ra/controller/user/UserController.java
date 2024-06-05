package com.ra.controller.user;

import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.request.AccountEditPassword;
import com.ra.model.dto.request.AccountEditRequest;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.service.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @GetMapping("/account")
    public ResponseEntity<?> userInfor() throws NotFoundException {
        UserResponse user = userService.getUserInfor();
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                user), HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@ModelAttribute AccountEditRequest accountEditRequest) throws NotFoundException, RequestErrorException {
        UserResponse user = userService.updateAccount(accountEditRequest);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                user), HttpStatus.OK);
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<?> updatePasswordAccount(@RequestBody AccountEditPassword accountEditPassword) throws NotFoundException, RequestErrorException {
       UserResponse user = userService.changePassword(accountEditPassword);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                user), HttpStatus.OK);
    }
}
