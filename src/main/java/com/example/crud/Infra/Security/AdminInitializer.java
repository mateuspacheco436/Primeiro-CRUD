package com.example.crud.Infra.Security;

import com.example.crud.Domain.User.UserRoles;
import com.example.crud.Domain.User.Usuario;
import com.example.crud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro usuario ADMIN a partir de variaveis de ambiente
 * (ADMIN_NOME, ADMIN_EMAIL, ADMIN_SENHA). Se nao estiverem definidas, nao faz nada.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${api.security.admin.nome:}")
    private String nome;
    @Value("${api.security.admin.email:}")
    private String email;
    @Value("${api.security.admin.senha:}")
    private String senha;

    public AdminInitializer(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (nome.isBlank() || senha.isBlank()) return;
        if (repository.findByNome(nome) != null) return;

        repository.save(new Usuario(nome, email, passwordEncoder.encode(senha), UserRoles.Admin));
    }
}
