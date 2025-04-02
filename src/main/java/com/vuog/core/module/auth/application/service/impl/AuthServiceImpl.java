package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.common.util.Context;
import com.vuog.core.common.util.JwtUtils;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.LoginCommand;
import com.vuog.core.module.auth.application.dto.JwtResponseDto;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.service.AuthService;
import com.vuog.core.module.auth.application.service.TokenService;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Value("${app.jwt.expiration.ms}")
    private Long tokenExpireTime;

    @Value("${app.jwt.refersh.expiration.ms}")
    private Long refreshTokenExpireTime;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    @Override
    public JwtResponseDto getToken(LoginCommand command) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.getUsername(), command.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        // find existed token by user before generate new
        Optional<Token> accessTokenOptional = tokenService.findByUserAndType(user, Token.TokenType.ACCESS);
        Optional<Token> refreshTokenOptional = tokenService.findByUserAndType(user, Token.TokenType.REFRESH);

        if (accessTokenOptional.isPresent()) {
            Token token = accessTokenOptional.get();

            String refreshToken;
            if (refreshTokenOptional.isPresent()) {
                refreshToken = refreshTokenOptional.get().getToken();
            } else {
                refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), authentication);
                tokenService.create(
                        refreshToken,
                        Token.TokenType.REFRESH,
                        LocalDateTime.now().plus(refreshTokenExpireTime, ChronoUnit.MILLIS),
                        user, token);
            }

            return JwtResponseDto
                    .builder()
                    .type("Bearer")
                    .token(token.getToken())
                    .refresh(refreshToken)
                    .user(new UserDto(user))
                    .build();
        } else {

            String jwt = jwtUtils.generateJwtToken(new HashMap<>(), authentication);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), authentication);

            Token token  = tokenService.create(jwt,
                    Token.TokenType.ACCESS,
                    LocalDateTime.now().plus(tokenExpireTime, ChronoUnit.MILLIS),
                    user, null);
            tokenService.create(
                    refreshToken,
                    Token.TokenType.REFRESH,
                    LocalDateTime.now().plus(refreshTokenExpireTime, ChronoUnit.MILLIS),
                    user, token);

            return JwtResponseDto
                    .builder()
                    .type("Bearer")
                    .token(jwt)
                    .refresh(refreshToken)
                    .user(new UserDto(user))
                    .build();
        }
    }

    @Override
    public JwtResponseDto refreshToken(String refreshToken) {

        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();

        Optional<Token> refreshTokenDB = tokenService.findValidByTokenAndUser(refreshToken, user);

        if (refreshTokenDB.isEmpty() || !refreshTokenDB.get().getToken().equals(refreshToken)) {
            throw new UserNotFoundException("Invalid refresh token: " + refreshToken);
        }

        String newToken = jwtUtils.generateToken(user);

        tokenService.create(newToken,
                Token.TokenType.ACCESS,
                LocalDateTime.now().plus(tokenExpireTime, ChronoUnit.MILLIS),
                user, refreshTokenDB.get());

        return JwtResponseDto
                .builder()
                .type("Bearer")
                .token(newToken)
                .refresh(refreshToken)
                .user(new UserDto(user))
                .build();

    }

    @Override
    public User register(CreateUserReq command) {
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }

        // find some default roles for user

        User user = new User(
                command.getUsername(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                Set.of()
        );

        return userRepository.save(user);
    }

    @Override
    public User verify() {
        User user = Context.getUser();

        if (Objects.isNull(user) || !user.isEnabled()) {
            throw new UserNotFoundException("User not found");
        }

        Optional<User> userInfo = userRepository.findByUsername(user.getUsername());

        if (userInfo.isPresent()) {
            return userInfo.get();
        }

        throw new UserNotFoundException("Not found user " + user.getUsername());
    }

    @Override
    public void logout() {

        // find all token of user
        User user = Context.getUser();

        List<Token> tokens = tokenService.findAllByUser(user);

        for (Token token : tokens) {
            tokenService.blacklist(token.getToken());
        }
    }
}
