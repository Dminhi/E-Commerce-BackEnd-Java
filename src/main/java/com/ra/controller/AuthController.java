package com.ra.controller;

import com.ra.exception.AccountLockedException;
import com.ra.exception.CustomException;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.service.userService.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.example.com/v1/auth")
public class AuthController {
    @Autowired
    private IUserService userService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> doLogin(@Valid @RequestBody FormLogin formLogin) throws AccountLockedException, NotFoundException, AccountLockedException, NotFoundException {
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                userService.login(formLogin)), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<?> doRegister(@RequestBody FormRegister formRegister) throws CustomException {
        boolean check = userService.register(formRegister);
        if (check){
            Map<String,String> map = new HashMap<>();
            map.put("message","Account create successfully");
            return new ResponseEntity<>(map,HttpStatus.CREATED);
        }else {
            throw new RuntimeException("something is error");
        }
    }
}
