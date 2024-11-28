package com.example.demo.repo;

import com.example.demo.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepo extends JpaRepository<HoaDonChiTiet, Integer> {

    @Query("SELECT d FROM HoaDonChiTiet d WHERE d.hoaDon.idHoaDon = :idHoaDonChiTiet")
    List<HoaDonChiTiet> findById1(Integer idHoaDonChiTiet);


    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd")
    Float getTotalRevenue();

    @Query("SELECT COUNT(hd) FROM HoaDon hd")
    Long getTotalInvoices();

    @Query("SELECT sp.ten, SUM(hdct.soLuong), SUM(hdct.soLuong * hdct.donGia) " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "JOIN spct.idSanPham sp " +
            "GROUP BY sp.ten " +
            "ORDER BY SUM(hdct.soLuong * hdct.donGia) DESC")
    List<Object[]> getTopSellingProducts(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT spct.ma, SUM(hdct.soLuong) AS totalSold " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "WHERE spct.idSanPhamChiTiet = :idSanPhamChiTiet " +
            "GROUP BY spct.ma")
    Object[] findTotalSoldByProductDetail(Integer idSanPhamChiTiet);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.idHoaDon = :idHoaDon")
    List<HoaDonChiTiet> findByHoaDonId(@Param("idHoaDon") Integer idHoaDon);

}
