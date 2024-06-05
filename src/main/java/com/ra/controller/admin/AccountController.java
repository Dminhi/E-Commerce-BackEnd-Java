package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.User;
import com.ra.service.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/account")
public class AccountController {
    @Autowired
    private IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> doAdd(@RequestBody FormRegister formRegister) throws CustomException {
        boolean check = userService.register(formRegister);
        if (check){
            Map<String,String> map = new HashMap<>();
            map.put("message","Account create successfully");
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        }else {
            throw new RuntimeException("something is error");
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(users)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) throws NotFoundException, DataNotFound {
        UserResponse user = userService.getUserInforById(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws NotFoundException, RequestErrorException, RequestErrorException {
        User user = userService.changeStatus(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                user), HttpStatus.OK);
    }
}
