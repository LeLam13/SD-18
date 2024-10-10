package com.example.demo.repo;


import com.example.demo.entity.taikhoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface taikhoanRepo extends JpaRepository<taikhoan, String> {
    taikhoan findByEmail(String email);
    taikhoan findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
