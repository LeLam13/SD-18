package com.example.demo.repo;

import com.example.demo.entity.LichSuNhapHang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuNhapHangRepo extends JpaRepository<LichSuNhapHang, Integer> {

    @Query("SELECT ls FROM LichSuNhapHang ls " +
            "JOIN ls.idSanPhamChiTiet sp " +
            "JOIN sp.idSanPham s " +
            "WHERE s.idSanPham = :idSanPham")
    Page<LichSuNhapHang> findByIdSanPham(@Param("idSanPham") Integer idSanPham, Pageable pageable);

}
