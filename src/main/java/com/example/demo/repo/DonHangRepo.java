package com.example.demo.repo;

import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonHangRepo extends JpaRepository<DonHang, Integer> {
//    @Query("SELECT new com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO(dh.idDonHang, SUM(dhc.soLuong)) " +
//            "FROM DonHang dh LEFT JOIN DonHangChiTiet dhc ON dh.idDonHang = dhc.donHang.idDonHang " +
//            "GROUP BY dh.idDonHang")
//    List<DonHangTongSoLuongResponseDTO> findTongSoLuongDonHang();

    @Query("SELECT new com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO(dh.idDonHang, SUM(dhc.soLuong)) " +
            "FROM DonHang dh LEFT JOIN DonHangChiTiet dhc ON dh.idDonHang = dhc.donHang.idDonHang " +
            "WHERE dh.trangThai.idTrangThai = 1 " +
            "GROUP BY dh.idDonHang")
    List<DonHangTongSoLuongResponseDTO> findTongSoLuongDonHang();

    @Query("SELECT d FROM DonHang d WHERE d.maDonHang = :maDonHang")
    DonHang findByMaDonHang(@Param("maDonHang") String maDonHang);
}
