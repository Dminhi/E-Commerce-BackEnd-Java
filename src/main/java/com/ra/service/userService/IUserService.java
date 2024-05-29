package com.ra.service.userService;

import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;

public interface IUserService {
      boolean register(FormRegister formRegister);
        JWTResponse login(FormLogin formLogin);
}
