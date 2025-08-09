package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.api.dtos.auth.LoginRequestDTO;
import dev.dcarminatti.resolva_ja_api.api.dtos.auth.LoginResponseDTO;
import dev.dcarminatti.resolva_ja_api.api.dtos.auth.RegisterRequestDTO;
import dev.dcarminatti.resolva_ja_api.models.repositories.UserRepository;
import dev.dcarminatti.resolva_ja_api.security.JwtService;
import dev.dcarminatti.resolva_ja_api.services.UserService;
import dev.dcarminatti.resolva_ja_api.models.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());
        try {
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        UserDetails user = userService.loadUserByUsername(loginRequestDTO.email());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(loginRequestDTO.email(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        if(userRepository.findByEmail(request.email()) != null) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

