package com.example.demo.repo;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    Page<SanPhamChiTiet> findAll(Specification<SanPhamChiTiet> spec,Pageable pageable);


//    @Query("SELECT p FROM SanPhamChiTiet p WHERE "
//            + "(:giaBan IS NULL OR p.giaBan >= :giaMin) AND "
//            + "(:giaMax IS NULL OR p.giaBan <= :giaMax) AND "
//            + "(:xuatXu IS NULL OR p.idXuatXu = :xuatXu) AND "
//            + "(:mauSac IS NULL OR p.idMauSac = :mauSac) AND "
//            + "(:thuongHieu IS NULL OR p.idThuongHieu = :thuongHieu) AND "
//            + "(:kieuDang IS NULL OR p.idKieuDang = :kieuDang) AND "
//            + "(:chatLieu IS NULL OR p.idChatLieu = :chatLieu)")
//    List<SanPhamChiTiet> filterProducts(
//            @Param("ten") String ten,
//            @Param("giaBan") Float giaMin,
//            @Param("giaMax") Float giaMax,
//            @Param("xuatXu") Integer xuatXu,
//            @Param("mauSac") Integer mauSac,
//            @Param("thuongHieu") Integer thuongHieu,
//            @Param("kieuDang") Integer kieuDang,
//            @Param("chatLieu") Integer chatLieu
//    );


    @Query("SELECT spct.idSanPhamChiTiet, SUM(hdct.soLuong) " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "WHERE spct.idSanPham.idSanPham = :idSanPham " +
            "GROUP BY spct.idSanPhamChiTiet")
    List<Object[]> getDetailedTotalSoldByProduct(@Param("idSanPham") Integer idSanPham);

    @Query("SELECT COALESCE(SUM(spct.soLuong), 0) " +
            "FROM SanPhamChiTiet spct " +
            "WHERE spct.idSanPham.idSanPham = :idSanPham")
    Integer getTotalInventoryByProduct(@Param("idSanPham") Integer idSanPham);

}

