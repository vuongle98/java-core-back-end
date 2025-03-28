package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.validation.RequestValidator;
import com.vuog.core.module.user.application.command.CreateUserReq;
import com.vuog.core.module.user.application.dto.UserDto;
import com.vuog.core.module.user.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final RequestValidator requestValidator;

    public UserController(UserService userService, RequestValidator requestValidator) {
        this.userService = userService;
        this.requestValidator = requestValidator;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @RequestBody CreateUserReq createReq
    ) {
        try {
            List<String> validationErrors = requestValidator.validateEntity(createReq);
            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(String.join(", ", validationErrors)));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Created successfully", userService.create(createReq)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create entity: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @RequestParam(value = "id") Long id,
            @RequestBody CreateUserReq updateReq
    ) {
        try {
            List<String> validationErrors = requestValidator.validateId(id);
            validationErrors.addAll(requestValidator.validateEntity(updateReq));

            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(String.join(", ", validationErrors)));
            }

            return ResponseEntity.ok(ApiResponse.success("Created successfully", userService.update(id, updateReq)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create entity: " + e.getMessage()));
        }
    }

}
