package com.example.demo.repo;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.nhanvien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<nhanvien, Integer> {
    MauSac findByIdNhanVien(Integer idNhanVien);
    Optional<nhanvien> findByTaikhoan_Username(String username);
    Optional<nhanvien> findByTaikhoan_Email(String email);
}
