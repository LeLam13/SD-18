package com.example.demo.repo;


import com.example.demo.entity.vaitro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface vaitroRepo extends JpaRepository<vaitro,String> {
    Optional<vaitro> findByTen(String ten);
}
