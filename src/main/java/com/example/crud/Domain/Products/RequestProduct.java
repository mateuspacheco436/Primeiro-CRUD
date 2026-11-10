package com.example.crud.Domain.Products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record RequestProduct(
        @NotBlank(message = "O nome e obrigatorio") String nome,
        @NotNull(message = "O preco e obrigatorio")
        @Positive(message = "O preco deve ser maior que zero") Double preco,
        @NotNull(message = "A quantidade e obrigatoria")
        @PositiveOrZero(message = "A quantidade nao pode ser negativa") Integer quantidade) {
}
