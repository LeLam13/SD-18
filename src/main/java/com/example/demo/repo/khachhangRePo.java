package com.example.demo.repo;

import com.example.demo.entity.khachhang;
import com.example.demo.entity.nhanvien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface khachhangRePo extends JpaRepository<khachhang, Integer> {
    @Query("SELECT k FROM khachhang k WHERE k.soDienThoai = :soDienThoai")
    List<khachhang> findBySoDienThoai(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT k FROM khachhang k WHERE k.taikhoan.username = :username")
    khachhang findByUsername(@Param("username") String username);

    @Query("SELECT k FROM khachhang k WHERE k.idKhachHang = :idKhachHang")
    khachhang findByIdKhachHang(@Param("idKhachHang") Long idKhachHang);

    @Query("SELECT k FROM khachhang k WHERE k.idKhachHang = :id")
    Optional<khachhang> findById(@Param("id") Long id);

    // Phương thức tìm kiếm với từ khóa và phân trang
    @Query("SELECT k FROM khachhang k WHERE " +
            "LOWER(k.maKhachHang) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(k.soDienThoai) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<khachhang> findBySearchQuery(@Param("searchQuery") String searchQuery, Pageable pageable);

    khachhang findByTaikhoanUsername(String username);

    boolean existsByMaKhachHang(String maKhachHang);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

}
