package com.example.crud.Repository;

import com.example.crud.Domain.Products.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, String> {
}
