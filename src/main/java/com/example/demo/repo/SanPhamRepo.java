package com.example.demo.repo;

import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {
//    List<SanPham> findByIdSanPham(Integer idSanPham);
    SanPham findByMa(String ma);

    SanPham findByIdSanPham(Integer idSanPham);
}
