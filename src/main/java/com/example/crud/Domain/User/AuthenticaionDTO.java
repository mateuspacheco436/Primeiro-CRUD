package com.example.crud.Domain.User;

import jakarta.validation.constraints.NotBlank;

public record AuthenticaionDTO(
        @NotBlank(message = "O nome e obrigatorio") String nome,
        @NotBlank(message = "A senha e obrigatoria") String senha) {
}
