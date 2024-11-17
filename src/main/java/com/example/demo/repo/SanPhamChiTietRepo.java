package com.example.demo.repo;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;

//import org.hibernate.mapping.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query("SELECT spct FROM SanPhamChiTiet spct JOIN spct.idSanPham sp WHERE LOWER(sp.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<SanPhamChiTiet> findBySanPhamTenContainingIgnoreCase(String ten);

    @Query("SELECT spc FROM SanPhamChiTiet spc JOIN spc.dotGiamGiaList dgg " +
            "WHERE dgg.idGiamGia = :idGG AND spc.idSanPham.idSanPham = :idSP")
    List<SanPhamChiTiet> findByDotGiamGiaAndSanPham(@Param("idGG") Integer idGG, @Param("idSP") Integer idSP);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM giam_gia_san_pham_chi_tiet " +
            "WHERE id_giam_gia = :idGG AND id_san_pham_chi_tiet = :idChiTiet",
            nativeQuery = true)
    void deleteByDotGiamGiaAndSanPhamChiTiet(@Param("idGG") Integer idGG, @Param("idChiTiet") Integer idChiTiet);

    @Query(value = """
            SELECT spct.*
            FROM san_pham_chi_tiet spct
            WHERE spct.id_san_pham = :idSanPham
              AND spct.id_san_pham_chi_tiet NOT IN (
                  SELECT ggspct.id_san_pham_chi_tiet
                  FROM giam_gia_san_pham_chi_tiet ggspct
                  WHERE ggspct.id_giam_gia = :idDotGiamGia
              )
            """,
            countQuery = """
                     SELECT COUNT(*)
                     FROM san_pham_chi_tiet spct
                     WHERE spct.id_san_pham = :idSanPham
                       AND spct.id_san_pham_chi_tiet NOT IN (
                           SELECT ggspct.id_san_pham_chi_tiet
                           FROM giam_gia_san_pham_chi_tiet ggspct
                           WHERE ggspct.id_giam_gia = :idDotGiamGia
                       )
                    """,
            nativeQuery = true)
    Page<SanPhamChiTiet> findSanPhamChiTietNotInDotGiamGia(
            @Param("idSanPham") Integer idSanPham,
            @Param("idDotGiamGia") Integer idDotGiamGia,
            Pageable pageable);

//    Page<SanPhamChiTiet> findByIdSanPham_IdSanPham(Integer idSanPham, Pageable pageable);
//    @Query("SELECT s FROM SanPhamChiTiet s WHERE s.idSanPham.idSanPham = :idSanPham")
//    List<SanPhamChiTiet> findCustomBySanPhamId(@Param("idSanPham") Integer idSanPham);

    @Query("SELECT s FROM SanPhamChiTiet s WHERE s.idSanPham.idSanPham = :idSanPham")
    Page<SanPhamChiTiet> getByID(@Param("idSanPham") Integer idSanPham, Pageable pageable);

    Page<SanPhamChiTiet> findByIdSanPham_IdSanPham(Integer idSanPham, Pageable pageable);

    List<SanPhamChiTiet> findAllByIdSanPhamChiTietIn(List<Integer> idSanPhamChiTietList);


    SanPhamChiTiet findByMa(String ma);

    SanPhamChiTiet findByIdSanPhamChiTiet(Integer idSanPhamChiTiet);
}



