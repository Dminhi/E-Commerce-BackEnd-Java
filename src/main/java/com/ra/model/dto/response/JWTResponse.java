package com.ra.model.dto.response;

import com.ra.model.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class JWTResponse {
    private final String type = "Bearer";
    private String accessToken ;
    private String fullName;
    private String email;
    private String avatar;
    private Long userId;
    private Collection<? extends GrantedAuthority> roleSet;
    private boolean status;
    private String phone;
    private List<Long> wishList;
}

