package com.example.demo.repo;

import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {
//    List<SanPham> findByIdSanPham(Integer idSanPham);
    SanPham findByMa(String ma);

    SanPham findByIdSanPham(Integer idSanPham);

    @Query("SELECT s FROM SanPham s WHERE REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(s.ten, 'á', 'a'), 'à', 'a'), 'ả', 'a'), 'ã', 'a'), 'ạ', 'a'), 'ă', 'a'), 'â', 'a') LIKE %:ten%")
    List<SanPham> findByName(@Param("ten") String ten);
}
