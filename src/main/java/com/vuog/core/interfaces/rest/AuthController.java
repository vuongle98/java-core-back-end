package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.LoginCommand;
import com.vuog.core.module.auth.application.dto.JwtResponseDto;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.service.AuthService;
import com.vuog.core.module.auth.domain.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(
            @RequestBody LoginCommand command
    ) {
        JwtResponseDto response = authService.getToken(command);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refresh(
            @RequestParam String token
    ) {
        try {
            Context.setSystemUser();
            JwtResponseDto response = authService.refreshToken(token);
            Context.clear();
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
    ) {
        Context.setSystemUser();
        authService.logout();
        Context.clear();

        return ResponseEntity.ok(ApiResponse.success("Logout successfully"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @RequestBody CreateUserReq request
    ) {
        Context.setSystemUser();
        User response = authService.register(request);
        Context.clear();
        UserDto userInfo = new UserDto(response);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<UserDto>> verify() {
        User response = authService.verify();

        UserDto userInfo = new UserDto(response);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }


}
