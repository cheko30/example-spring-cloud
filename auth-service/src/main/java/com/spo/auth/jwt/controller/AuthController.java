package com.spo.auth.jwt.controller;

import com.spo.auth.jwt.dto.AuthUserDto;
import com.spo.auth.jwt.dto.NewUserDto;
import com.spo.auth.jwt.dto.RequestDto;
import com.spo.auth.jwt.dto.TokenDto;
import com.spo.auth.jwt.entity.AuthUser;
import com.spo.auth.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto authUserDto) {
        TokenDto tokenDto = authService.loginAuth(authUserDto);
        if(authUserDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token, @RequestBody RequestDto requestDto) {
        TokenDto tokenDto = authService.validateToken(token, requestDto);
        if(tokenDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody NewUserDto dto) {
        AuthUser authUser = authService.save(dto);
        if(authUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authUser);
    }

}
