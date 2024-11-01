package com.example.demo.repo;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet,Integer> {
    @Query("SELECT spct FROM SanPhamChiTiet spct JOIN spct.idSanPham sp WHERE LOWER(sp.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<SanPhamChiTiet> findBySanPhamTenContainingIgnoreCase(String ten);
}
