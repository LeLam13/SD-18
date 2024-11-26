package com.example.demo.repo;

import com.example.demo.entity.DonHangChiTiet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangChiTietRepo extends JpaRepository<DonHangChiTiet,Integer> {
    @Query("SELECT d FROM DonHangChiTiet d WHERE d.donHang.idDonHang = :idDonHang")
    List<DonHangChiTiet> findByDonHangId(Integer idDonHang);

    @Query("SELECT d FROM DonHangChiTiet d WHERE d.sanPhamChiTiet.idSanPhamChiTiet = :idSanPhamCT AND d.donHang.idDonHang = :idDonHang")
    DonHangChiTiet findBySanPhamID(Integer idSanPhamCT, Integer idDonHang);

    @Modifying
    @Query("DELETE FROM DonHangChiTiet d WHERE d.donHang.idDonHang = :donHangId")
    void deleteByDonHangId(Integer donHangId);

<<<<<<< HEAD

    @Query("SELECT SUM(dh.tongTienSauKhuyenMai) FROM DonHang dh")
    Float getTotalRevenue();


    @Query("SELECT COUNT(dh) FROM DonHang dh")
    Long getTotalOrders();


    @Query("SELECT spct.idSanPham.ten, SUM(dhct.soLuong), SUM(dhct.soLuong * dhct.donGia) " +
            "FROM DonHangChiTiet dhct " +
            "JOIN dhct.sanPhamChiTiet spct " +
            "GROUP BY spct.idSanPham.ten " +
            "ORDER BY SUM(dhct.soLuong) DESC")
    List<Object[]> getTopSellingProducts(Pageable pageable);
=======
    @Query("SELECT d FROM DonHangChiTiet d WHERE d.sanPhamChiTiet.idSanPhamChiTiet = :idSanPhamChiTiet AND d.donHang.idDonHang = :idDonHang")
    DonHangChiTiet findBySanPhamChiTietIdAndDonHangId(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet,
                                                      @Param("idDonHang") Integer idDonHang);
>>>>>>> feature/banhang
}
