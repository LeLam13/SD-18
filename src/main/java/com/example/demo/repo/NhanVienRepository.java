package com.example.demo.repo;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.nhanvien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<nhanvien, Integer> {
    MauSac findByIdNhanVien(Integer idNhanVien);
    Optional<nhanvien> findByTaikhoan_Username(String username);
    Optional<nhanvien> findByTaikhoan_Email(String email);
    @Query("SELECT n FROM nhanvien n ORDER BY n.trangThai DESC")
    Page<nhanvien> findAllWithTrangThaiOrder(Pageable pageable);



    @Query("""
    SELECT n FROM nhanvien n 
    WHERE (n.maNhanVien LIKE %:maNhanVien% OR n.hoTen LIKE %:hoTen% OR n.soDienThoai LIKE %:soDienThoai%) 
    ORDER BY n.trangThai DESC
""")
    Page<nhanvien> findByMaNhanVienContainingOrHoTenContainingOrSoDienThoaiContainingOrderByTrangThai(
            @Param("maNhanVien") String maNhanVien,
            @Param("hoTen") String hoTen,
            @Param("soDienThoai") String soDienThoai,
            Pageable pageable);


}
