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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    @Value("${app.jwt.expiration.ms}")
    private Long tokenExpireTime;
    @Value("${app.jwt.refersh.expiration.ms}")
    private Long refreshTokenExpireTime;

    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           TokenService tokenService) {
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

        Optional<Token> accessTokenOpt = tokenService.findByUserAndType(user, Token.TokenType.ACCESS);
        Optional<Token> refreshTokenOpt = tokenService.findByUserAndType(user, Token.TokenType.REFRESH);

        if (accessTokenOpt.isPresent() && jwtUtils.validateJwtToken(accessTokenOpt.get().getToken())) {
            String refreshToken = refreshTokenOpt
                    .map(Token::getToken)
                    .orElseGet(() -> createAndSaveRefreshToken(authentication, user, accessTokenOpt.get()));

            return buildJwtResponse(accessTokenOpt.get().getToken(), refreshToken, user);
        }

        logout(); // Invalidate old tokens if any

        String accessToken = createAndSaveAccessToken(authentication, user);
        String refreshToken = createAndSaveRefreshToken(authentication, user, null);

        return buildJwtResponse(accessToken, refreshToken, user);
    }

    @Override
    public JwtResponseDto refreshToken(String refreshToken) {
        String tokenValue = refreshToken.replace("Bearer ", "").trim();

        String username = jwtUtils.getUserNameFromJwtToken(tokenValue);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Token storedRefreshToken = tokenService.findValidByTokenAndUser(tokenValue, user)
                .filter(t -> t.getToken().equals(tokenValue))
                .orElseThrow(() -> new UserNotFoundException("Invalid refresh token"));

        String newAccessToken = jwtUtils.generateToken(user);
        tokenService.create(
                newAccessToken,
                Token.TokenType.ACCESS,
                Instant.now().plus(tokenExpireTime, ChronoUnit.MILLIS),
                user,
                storedRefreshToken
        );

        return buildJwtResponse(newAccessToken, tokenValue, user);
    }

    @Override
    public User register(CreateUserReq command) {
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }

        User user = new User(
                command.getUsername(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                Set.of() // TODO: Assign default roles here
        );

        return userRepository.save(user);
    }

    @Override
    public User verify() {
        User user = Context.getUser();

        if (user == null || !user.isEnabled()) {
            throw new UserNotFoundException("User not found or disabled");
        }

        return userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Not found user " + user.getUsername()));
    }

    @Override
    public void logout() {
        User user = Context.getUser();
        List<Token> tokens = tokenService.findAllByUser(user);
        tokens.forEach(token -> tokenService.blacklistTokenAndRelated(token.getToken()));
    }

    @Override
    public void revoke(String token) {
        token = token.replace("Bearer ", "").trim();
        Token storedToken = tokenService.findValidByToken(token)
                .orElseThrow(() -> new UserNotFoundException("Invalid token"));

        tokenService.blacklistToken(storedToken.getToken());
    }

    // ================== PRIVATE METHODS ===================

    private String createAndSaveAccessToken(Authentication authentication, User user) {
        String jwt = jwtUtils.generateJwtToken(new HashMap<>(), authentication);
        tokenService.create(jwt, Token.TokenType.ACCESS,
                Instant.now().plus(tokenExpireTime, ChronoUnit.MILLIS), user, null);
        return jwt;
    }

    private String createAndSaveRefreshToken(Authentication authentication, User user, Token accessToken) {
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), authentication);
        tokenService.create(refreshToken, Token.TokenType.REFRESH,
                Instant.now().plus(refreshTokenExpireTime, ChronoUnit.MILLIS), user, accessToken);
        return refreshToken;
    }

    private JwtResponseDto buildJwtResponse(String accessToken, String refreshToken, User user) {
        return JwtResponseDto.builder()
                .type("Bearer")
                .token(accessToken)
                .refresh(refreshToken)
                .user(new UserDto(user))
                .build();
    }
}
