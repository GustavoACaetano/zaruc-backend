package com.zaruc.backend.controllers;

import com.zaruc.backend.domain.AuthenticationDTO;
import com.zaruc.backend.domain.LoginResponseDTO;
import com.zaruc.backend.domain.RegisterDTO;
import com.zaruc.backend.domain.user.User;
import com.zaruc.backend.infra.security.TokenService;
import com.zaruc.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        if (data.login() == null) {
            return ResponseEntity.badRequest().body("Login não pode ser nulo.");
        }
        
        if (data.password() == null ) {
            return ResponseEntity.badRequest().body("Senha não pode ser nula.");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
        
        if (data.login() == null) {
            return ResponseEntity.badRequest().body("Login não pode ser nulo.");
        }

        if (data.name() == null) {
            return ResponseEntity.badRequest().body("Nome não pode ser nulo.");
        }

        if (data.password() == null ) {
            return ResponseEntity.badRequest().body("Senha não pode ser nula.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), data.name(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}