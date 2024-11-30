package com.example.demo.repo;

import com.example.demo.dto.request.SanPhamWithImageDto;
import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {
    //    List<SanPham> findByIdSanPham(Integer idSanPham);


    SanPham findByMa(String ma);

    SanPham findByIdSanPham(Integer idSanPham);

    @Query(nativeQuery = true, value = """
            SELECT DISTINCT sp.*
            FROM giam_gia_san_pham_chi_tiet ggspct
            JOIN san_pham_chi_tiet spct ON ggspct.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
            JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
            WHERE ggspct.id_giam_gia = :id
            """)
    List<SanPham> findAllByDotGiamGia(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            DELETE FROM giam_gia_san_pham_chi_tiet
            WHERE id_giam_gia = :idGiamGia 
              AND id_san_pham_chi_tiet IN (
                  SELECT spct.id_san_pham_chi_tiet
                  FROM san_pham_chi_tiet spct
                  WHERE spct.id_san_pham = :idSanPham
              )
            """)
    void deleteByDotGiamGiaAndSanPham(@Param("idGiamGia") Integer idGiamGia,
                                      @Param("idSanPham") Integer idSanPham);

    @Query(nativeQuery = true, value = """
    SELECT DISTINCT sp.id_san_pham, sp.ma, sp.ten, sp.trang_thai, sp.create_date, sp.create_by, 
                    sp.update_date, sp.update_by, sp.mo_ta, sp.id_thuong_hieu, sp.id_kieu_dang, 
                    sp.id_chat_lieu, sp.id_xuat_xu,sp.id_hinh_anh
    FROM san_pham sp
    JOIN san_pham_chi_tiet spct ON sp.id_san_pham = spct.id_san_pham
    WHERE NOT EXISTS (
        SELECT 1
        FROM giam_gia_san_pham_chi_tiet ggspct
        WHERE ggspct.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
          AND ggspct.id_giam_gia = :idGiamGia
    )
    ORDER BY sp.id_san_pham DESC
    """)
    Page<SanPham> findAllNotInDotGiamGia(@Param("idGiamGia") Integer idGiamGia, Pageable pageable);


    // Thống kê số lượng tồn kho theo sản phẩm
    @Query("SELECT sp.ten, SUM(spct.soLuong) AS soLuongTon " +
            "FROM SanPhamChiTiet spct " +
            "JOIN spct.idSanPham sp " +
            "GROUP BY sp.ten")
    List<Object[]> getInventoryByProduct();


    @Query("SELECT s FROM SanPham s WHERE LOWER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(s.ten, 'á', 'a'), 'à', 'a'), 'ả', 'a'), 'ã', 'a'), 'ạ', 'a'), 'ă', 'a'), 'â', 'a')) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<SanPham> findByName(@Param("ten") String ten);


//    @Query("SELECT new com.example.demo.dto.request.SanPhamWithImageDto(sp.idSanPham, sp.tenSanPham, h.tenHinhAnh) " +
//            "FROM SanPham sp " +
//            "JOIN SanPhamChiTiet ct ON sp.idSanPham = ct.idSanPham" +
//            "JOIN HinhAnh h ON ct.idHinhAnh = h.idHinhAnh")
//    Page<SanPhamWithImageDto> findAllWithImages(Pageable pageable);

}

