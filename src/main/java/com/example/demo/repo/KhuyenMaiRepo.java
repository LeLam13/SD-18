package com.example.demo.repo;

import com.example.demo.entity.KhuyenMai;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai, Integer> {
        Page<KhuyenMai> findByTenKhuyenMaiContaining(String search, Pageable pageable);

        Page<KhuyenMai> findByTenKhuyenMaiContainingAndTrangThai(String search, boolean trangThai, Pageable pageable);

        Optional<KhuyenMai> findByMaKhuyenMai(String maKhuyenMai);
}
