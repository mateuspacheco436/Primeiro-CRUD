package com.example.crud.Domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResgisterDTO(
        @NotBlank(message = "O nome e obrigatorio") String nome,
        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres") String senha,
        @NotBlank(message = "O e-mail e obrigatorio")
        @Email(message = "E-mail invalido") String email) {
}
