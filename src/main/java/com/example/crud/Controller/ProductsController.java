package com.example.crud.Controller;

import com.example.crud.Domain.Products.Products;
import com.example.crud.Domain.Products.RequestProduct;
import com.example.crud.Domain.Products.RequestUpdateProduct;
import com.example.crud.Repository.ProductsRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/V1/products")
public class ProductsController {

    private final ProductsRepository repository;

    public ProductsController(ProductsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getById(@PathVariable String id) {
        return ResponseEntity.ok(findOrThrow(id));
    }

    @PostMapping
    public ResponseEntity<Products> create(@RequestBody @Valid RequestProduct data) {
        Products saved = repository.save(new Products(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<Products> update(@RequestBody @Valid RequestUpdateProduct data) {
        Products product = findOrThrow(data.id());
        product.setNome(data.nome());
        product.setPreco(data.preco());
        product.setQuantidade(data.quantidade());
        return ResponseEntity.ok(repository.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Products findOrThrow(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
    }
}
