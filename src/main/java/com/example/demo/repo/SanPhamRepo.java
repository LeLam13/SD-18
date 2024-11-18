package com.example.demo.repo;

import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {

    SanPham findByMa(String ma);

    SanPham findByIdSanPham(Integer idSanPham);

    // Thống kê số lượng tồn kho theo sản phẩm
    @Query("SELECT sp.ten, SUM(spct.soLuong) AS soLuongTon " +
            "FROM SanPhamChiTiet spct " +
            "JOIN spct.idSanPham sp " +
            "GROUP BY sp.ten")
    List<Object[]> getInventoryByProduct();
}
