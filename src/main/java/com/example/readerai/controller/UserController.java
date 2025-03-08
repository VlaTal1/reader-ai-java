package com.example.readerai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/public/info")
    public ResponseEntity<Map<String, Object>> getPublicInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Это публичный эндпоинт, доступный всем");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("userId", authentication.getPrincipal());
        response.put("authorities", authentication.getAuthorities());
        response.put("message", "Вы успешно авторизованы через Supabase!");
        return ResponseEntity.ok(response);
    }
}
