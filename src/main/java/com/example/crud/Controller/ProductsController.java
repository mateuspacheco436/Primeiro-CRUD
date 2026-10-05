package com.example.crud.Controller;

import com.example.crud.Domain.Products.Products;
import com.example.crud.Repository.ProductsRepository;
import com.example.crud.Domain.Products.RequestProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/V1/products")

public class ProductsController {

    @Autowired
    private ProductsRepository repository;

    @GetMapping
    public ResponseEntity getallproducts() {
        var allproducts = repository.findAll();
        return ResponseEntity.ok(allproducts);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid RequestProduct data) {
        Products newProduct = new Products(data);
        repository.save(newProduct);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody @Valid Products data) {
        var product = repository.getReferenceById(data.getId());
        product.setNome(data.getNome());
        product.setPreco(data.getPreco());
        product.setQuantidade(data.getQuantidade());
        repository.save(product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
