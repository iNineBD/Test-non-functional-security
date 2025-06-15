package com.javabycode.controller;

import com.javabycode.model.User;
import com.javabycode.repository.UserRepository;
import com.javabycode.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MS = TimeUnit.MINUTES.toMillis(1);

    private static class Attempt {
        int count;
        long lastAttempt;
        long blockedUntil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");
        String ip = request.getRemoteAddr();
        Attempt attempt = attempts.computeIfAbsent(ip, k -> new Attempt());
        long now = System.currentTimeMillis();
        synchronized (attempt) {
            if (attempt.blockedUntil > now) {
                return ResponseEntity.status(429).body("Muitas tentativas. Tente novamente em alguns instantes.");
            }
            if (attempt.lastAttempt + BLOCK_TIME_MS < now) {
                attempt.count = 0;
            }
            attempt.lastAttempt = now;
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                attempt.count = 0;
                String token = JwtUtil.generateToken(username);
                return ResponseEntity.ok(Map.of("token", token));
            }
            attempt.count++;
            if (attempt.count >= MAX_ATTEMPTS) {
                attempt.blockedUntil = now + BLOCK_TIME_MS;
                return ResponseEntity.status(429).body("Muitas tentativas. Tente novamente em alguns instantes.");
            }
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username e password são obrigatórios");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(409).body("Usuário já existe");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

    @GetMapping("/users")
    public ResponseEntity<?> listUsernames() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(User::getUsername).toList());
    }
}
