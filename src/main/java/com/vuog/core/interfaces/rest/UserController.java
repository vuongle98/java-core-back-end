package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.validation.RequestValidator;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.application.service.UserService;
import com.vuog.core.module.auth.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAll(
            UserQuery query,
            Pageable pageable
    ) {
        try {
            List<String> validationErrors = requestValidator.validatePageable(pageable);

            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(String.join(", ", validationErrors)));
            }

            Page<User> userPaged = userService.findAll(query, pageable);
            Page<UserDto> convertedUser = userPaged.map(UserDto::new);

            return ResponseEntity.ok(ApiResponse.success(convertedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Has error when fetching data " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(@PathVariable Long id) {
        try {
            List<String> validationErrors = requestValidator.validateId(id);

            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(String.join(", ", validationErrors)));
            }

            User user = userService.getById(id);

            return ResponseEntity.ok(ApiResponse.success(new UserDto(user)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Has error when fetching data " + e.getMessage()));
        }
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

            User createdUser = userService.create(createReq);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Created successfully", new UserDto(createdUser)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create entity: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody CreateUserReq updateReq
    ) {
        try {
            List<String> validationErrors = requestValidator.validateId(id);
            validationErrors.addAll(requestValidator.validateEntity(updateReq));

            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(String.join(", ", validationErrors)));
            }

            User updatedUser = userService.update(id, updateReq);

            return ResponseEntity.ok(ApiResponse.success("Created successfully", new UserDto(updatedUser)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create entity: " + e.getMessage()));
        }
    }

}
