package com.example.demo.repo;

import com.example.demo.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GioHangchiTietRepo extends JpaRepository<GioHangChiTiet, Integer> {
    @Query("SELECT g FROM GioHangChiTiet g WHERE g.gioHang.idGioHang = :idGioHang")
    List<GioHangChiTiet> findByGioHangId(@Param("idGioHang") Integer idGioHang);

    @Query("SELECT g FROM GioHangChiTiet g WHERE g.sanPhamChiTiet.idSanPhamChiTiet = :idSanPhamChiTiet AND g.gioHang.idGioHang = :idGioHang")
    GioHangChiTiet findBySanPhamIdAndGioHangId(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("idGioHang") Integer idGioHang);

    GioHangChiTiet findBySanPhamChiTiet_IdSanPhamChiTiet(Integer idSanPhamChiTiet);

    void deleteBySanPhamChiTiet_IdSanPhamChiTiet(Integer idSanPhamChiTiet);
}
