package com.spo.auth.jwt.service;

import com.spo.auth.jwt.dto.AuthUserDto;
import com.spo.auth.jwt.dto.NewUserDto;
import com.spo.auth.jwt.dto.RequestDto;
import com.spo.auth.jwt.dto.TokenDto;
import com.spo.auth.jwt.entity.AuthUser;
import com.spo.auth.jwt.repository.AuthUserRepository;
import com.spo.auth.jwt.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser save(NewUserDto dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if(user.isPresent()) {
            return null;
        }

        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser
                .builder()
                .userName(dto.getUserName())
                .password(password)
                .role(dto.getRole())
                .build();

        return authUserRepository.save(authUser);
    }

    public TokenDto loginAuth(AuthUserDto authUserDto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        if(!user.isPresent()){
            return null;
        }

        if(passwordEncoder.matches(authUserDto.getPassword(), user.get().getPassword())) {
            return new TokenDto(jwtProvider.createToken(user.get()));
        }

        return  null;
    }

    public TokenDto validateToken(String token, RequestDto requestDto) {
        if(!jwtProvider.validateToken(token, requestDto)) {
            return null;
        }

        String userName = jwtProvider.getUserNameFromToken(token);
        if(!authUserRepository.findByUserName(userName).isPresent()){
            return null;
        }

        return new TokenDto(token);
    }
}
