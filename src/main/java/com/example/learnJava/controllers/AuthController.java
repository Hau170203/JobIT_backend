package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.User;
import com.example.learnJava.domain.request.ReqLoginDTO;
import com.example.learnJava.domain.response.ResLoginDTO;
import com.example.learnJava.service.UserService;
import com.example.learnJava.utils.SecurityUtils;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long accessRefreshExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            @Lazy SecurityUtils securityUtils,
            @Lazy UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody ReqLoginDTO loginDTO) {
        // Nạp input username password vào security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUserName(), loginDTO.getPassWord());

        // hàm xác thực người dùng => Viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Lấy thông tin user
        User user = this.userService.handleByUser(loginDTO.getUserName());

        // Trả về thông tin user và token
        ResLoginDTO res = new ResLoginDTO();

        ResLoginDTO.LoginUser userLogin = new ResLoginDTO.LoginUser(user.getId(), user.getUsername(), user.getEmail());
        res.setUser(userLogin);

        ResLoginDTO.RoleUser1 roleLogin1 = new ResLoginDTO.RoleUser1(user.getRole().getId(), user.getRole().getName());
        res.setRole(roleLogin1);
        // Tạo access_token
        String access_token = this.securityUtils.createAccessToken(user.getEmail(), res.getUser(), res.getRole() );

        res.setAccessToken(access_token);

        // tạo refresh token
        String refresh_token = this.securityUtils.createRefreshToken(user.getEmail(), res);

        this.userService.handleRefreshToken(refresh_token, user.getEmail());

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessRefreshExpiration)
                .build();

        // Lưu trữ trạng thái xác thực
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("get account user")
    public ResponseEntity<ResLoginDTO.LoginUser> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User currentUserDB = this.userService.handleByUser(email);
        ResLoginDTO.LoginUser userLogin = new ResLoginDTO.LoginUser();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setName(currentUserDB.getUsername());
            userLogin.setEmail(currentUserDB.getEmail());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Refresh-token thành công")
    public ResponseEntity<Object> getMethodName(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken) throws IdInvalidException {

        if (refreshToken.equals("abc")) {
            throw new IdInvalidException("Không có cookies ở headers");
        }
        String email = this.securityUtils.checkValidRefreshToken(refreshToken).getSubject();

        // Lấy thông tin user
        User user = this.userService.handleByUser(email);

        // Trả về thông tin user và token
        ResLoginDTO res = new ResLoginDTO();

        ResLoginDTO.LoginUser userLogin = new ResLoginDTO.LoginUser(user.getId(), user.getUsername(), user.getEmail());
        res.setUser(userLogin);

        ResLoginDTO.RoleUser1 roleLogin1 = new ResLoginDTO.RoleUser1(user.getRole().getId(), user.getRole().getName());
        res.setRole(roleLogin1);
        // Tạo access_token
        String access_token = this.securityUtils.createAccessToken(email, res.getUser(), res.getRole());

        res.setAccessToken(access_token);

        // tạo refresh token
        String refresh_token = this.securityUtils.createRefreshToken(email, userLogin);

        this.userService.handleRefreshToken(refresh_token, user.getEmail());

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessRefreshExpiration)
                .build();

        // Lưu trữ trạng thái xác thực
        // SecurityContextHolder.getContext().setAuthentication(userLogin);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout user ")
    public ResponseEntity<Void> Logout() throws IdInvalidException {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Token không hợp lệ");
        }
        this.userService.handleRefreshToken(null, email);

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(null);
    }

}
