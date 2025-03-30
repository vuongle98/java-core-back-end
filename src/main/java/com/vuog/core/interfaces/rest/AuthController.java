package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.LoginCommand;
import com.vuog.core.module.auth.application.dto.JwtResponseDto;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.service.AuthService;
import com.vuog.core.module.auth.domain.model.User;
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

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @RequestBody CreateUserReq request
    ) {
        User response = authService.register(request);

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
