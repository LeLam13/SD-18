package com.example.demo.repo;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.nhanvien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<nhanvien, Integer> {
    MauSac findByIdNhanVien(Integer idNhanVien);
    Optional<nhanvien> findByTaikhoan_Username(String username);
    Optional<nhanvien> findByTaikhoan_Email(String email);
    Page<nhanvien> findByTrangThaiTrue(Pageable pageable);

    Page<nhanvien> findByMaNhanVienContainingOrHoTenContainingOrSoDienThoaiContaining(
            String maNhanVien, String hoTen, String soDienThoai, Pageable pageable);
}
