package com.example.crud.Controller;

import com.example.crud.Domain.User.AuthenticaionDTO;
import com.example.crud.Domain.User.LoginResponseDTO;
import com.example.crud.Domain.User.ResgisterDTO;
import com.example.crud.Domain.User.UserRoles;
import com.example.crud.Domain.User.Usuario;
import com.example.crud.Infra.Security.TokenService;
import com.example.crud.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticaionController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticaionController(AuthenticationManager authenticationManager,
                                   UserRepository repository,
                                   TokenService tokenService,
                                   PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticaionDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.nome(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid ResgisterDTO data) {
        if (this.repository.findByNome(data.nome()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());
        // O papel NUNCA vem do cliente: todo cadastro publico e USER.
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, UserRoles.User);

        this.repository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }
}
