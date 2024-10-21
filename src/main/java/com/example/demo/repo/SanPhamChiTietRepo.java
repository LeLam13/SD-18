package com.example.demo.repo;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet,Integer> {
}
