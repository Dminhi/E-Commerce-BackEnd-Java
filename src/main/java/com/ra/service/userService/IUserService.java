package com.ra.service.userService;

import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.request.AccountEditPassword;
import com.ra.model.dto.request.AccountEditRequest;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
      boolean register(FormRegister formRegister);
        JWTResponse login(FormLogin formLogin);

    Page<User> findAll(Pageable pageable);

    User changeStatus(Long id) throws NotFoundException, RequestErrorException;

    List<UserResponse> findUserByUsername(String search) throws DataNotFound;

    UserResponse getUserInfor() throws NotFoundException;

    UserResponse updateAccount(AccountEditRequest accountEditRequest) throws NotFoundException, RequestErrorException;

    UserResponse changePassword(AccountEditPassword accountEditPassword) throws NotFoundException, RequestErrorException;

    UserResponse getUserInforById(Long userId) throws NotFoundException;
}
