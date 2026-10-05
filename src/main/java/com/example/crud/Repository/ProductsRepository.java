package com.example.crud.Repository;


import com.example.crud.Domain.Products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface ProductsRepository extends JpaRepository<Products, String> {}
