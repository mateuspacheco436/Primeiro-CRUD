package com.example.crud.Repository;

import com.example.crud.Domain.User.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository <Usuario, String> {

    UserDetails findByNome(String nome);
}
