package com.ra.model.dto.request;

import com.ra.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormRegister {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Set<String> roles;
}
