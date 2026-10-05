package com.example.crud.Domain.Products;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "produtos")
@Entity(name= "produtos")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    private Double preco;

    private Integer quantidade;

    public Products(RequestProduct data) {
        this.nome = data.nome();
        this.preco = data.preco();
        this.quantidade = data.quantidade();
    }

}
