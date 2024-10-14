package com.example.demo.repo;

import com.example.demo.entity.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrangThaiRepo extends JpaRepository<TrangThai,Integer> {
}
