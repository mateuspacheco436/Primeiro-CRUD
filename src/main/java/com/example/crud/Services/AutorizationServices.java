package com.example.crud.Services;

import com.example.crud.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutorizationServices implements UserDetailsService {

    private final UserRepository repository;

    public AutorizationServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByNome(username);
        if (user == null) throw new UsernameNotFoundException("Usuario nao encontrado");
        return user;
    }
}
