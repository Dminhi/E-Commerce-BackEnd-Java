package com.ra.service.userService;

import com.ra.exception.*;
import com.ra.model.dto.request.AccountEditPassword;
import com.ra.model.dto.request.AccountEditRequest;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.RoleName;
import com.ra.model.entity.User;

import com.ra.repository.IRoleRepository;
import com.ra.repository.IUserRepository;
import com.ra.security.jwt.JWTProvider;
import com.ra.security.principle.UserDetailsCustom;
import com.ra.service.UploadService;
import com.ra.service.wishlist.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IWishListService wishListService;
    @Override
    public boolean register(FormRegister formRegister) throws CustomException {
        if(userRepository.existsByUsername(formRegister.getUsername())) {
                throw new CustomException("Username has been already existed!", HttpStatus.CONFLICT);
        }
        if(userRepository.existsByEmail(formRegister.getEmail())) {
            throw new CustomException("Email has been already existed!", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .email(formRegister.getEmail())
                .avatar("https://upload.wikimedia.org/wikipedia/commons/1/1e/Default-avatar.jpg?20160314221008")
                .username(formRegister.getUsername())
                .createdAt(LocalDate.now())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .status(true)
                .build();
        if (formRegister.getRoles()!=null && !formRegister.getRoles().isEmpty()){
            Set<Role> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    r->{
                        switch (r){
                            case "ADMIN":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "MANAGER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "USER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            default:
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        }
                    }
            );
            user.setRoleSet(roles);
        }else {
            // mac dinh la user
            Set<Role> roles = new HashSet<>();
            roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
            user.setRoleSet(roles);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) throws NotFoundException, AccountLockedException {
        // xac thực username va password
        Authentication authentication = null;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new NotFoundException("username or password incorrect");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        if (!detailsCustom.isStatus()) {
            throw new AccountLockedException("account is locked");
        }
        String accessToken = jwtProvider.generateAccessToken(detailsCustom);
        return JWTResponse.builder()
                .wishList(wishListService.findProductIdByUserId(detailsCustom.getId()))
                .phone(detailsCustom.getPhone())
                .avatar(detailsCustom.getAvatar())
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.isStatus())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User changeStatus(Long id) throws NotFoundException, RequestErrorException {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("user not found"));
        if(user.getRoleSet().stream().anyMatch(role -> role.getRoleName().name().equals("ROLE_ADMIN"))){
            throw new RequestErrorException("Can't change admin status");
        }
        user.setStatus(!user.isStatus());
        userRepository.save(user);
        return user;
    }

    @Override
    public List<UserResponse> findUserByUsername(String search) throws DataNotFound {
        List<User> allByUsernameContains = userRepository.findAllByUsernameContains(search);

        if(allByUsernameContains.isEmpty()){throw new DataNotFound("user is empty");

        }
        return allByUsernameContains.stream().map(UserServiceImpl::toUserResponse).toList();
    }

    @Override
    public UserResponse getUserInfor() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        return toUserResponse(userRepository.findById(userDetailsCustom.getId()).orElseThrow(() -> new NotFoundException("user not found")));
    }

    @Override
    public UserResponse getUserInforById(Long userId) throws NotFoundException {
        return toUserResponse(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found")));
    }

    @Override
    public UserResponse updateAccount(AccountEditRequest accountEditRequest) throws NotFoundException, RequestErrorException {
        User user = userRepository.findByEmail(accountEditRequest.getEmail());
        user.setFullName(accountEditRequest.getFullName());
        if(!Objects.equals(user.getPhone(), accountEditRequest.getPhone())){
            if(userRepository.existsByPhone(accountEditRequest.getPhone())){
                throw new RequestErrorException("phone number exist");
            }
            else {
                user.setPhone(accountEditRequest.getPhone());}
        }
        user.setPhone(accountEditRequest.getPhone());
        user.setUpdatedAt(LocalDate.now());
        user.setAvatar(uploadService.uploadFileToServer(accountEditRequest.getAvatar()));
        userRepository.save(user);
        return toUserResponse(user);
    }

    @Override
    public UserResponse changePassword(AccountEditPassword accountEditPassword) throws NotFoundException, RequestErrorException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        User user = userRepository.findById(userDetailsCustom.getId()).orElseThrow(() -> new NotFoundException("user not found"));
        if (!passwordEncoder.matches(accountEditPassword.getOldPassword(),user.getPassword())) {
            throw new RequestErrorException("password incorrect");
        }
        if(!accountEditPassword.getNewPassWord().equals(accountEditPassword.getConfirmPassWord())){
            throw new RequestErrorException("confirm password incorrect");
        }
        if(accountEditPassword.getOldPassword().equals(accountEditPassword.getNewPassWord())){
            throw new RequestErrorException("new password must be not same old password");        }
        user.setPassword(passwordEncoder.encode(accountEditPassword.getNewPassWord()));
        userRepository.save(user);
        return toUserResponse(user);
    }

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null; // Nếu user là null, trả về null để tránh lỗi
        }
        // Sử dụng Builder của UserResponse để tạo một đối tượng mới
        return UserResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .build();
    }
}
