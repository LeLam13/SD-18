package com.example.demo.repo;

import com.example.demo.entity.KichCo;
import com.example.demo.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KichCoRepo extends JpaRepository<KichCo,Integer> {
    KichCo findByMa(String ma);

    KichCo findByIdKichCo(Integer idKichCo);

    @Query("SELECT m FROM KichCo m WHERE " +
            "LOWER(REPLACE(m.ma, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd')) OR " +
            "LOWER(REPLACE(m.ten, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd'))")
    Page<KichCo> searchIgnoreCaseAndDiacritics(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT ms FROM KichCo ms " +
            "JOIN SanPhamChiTiet spct ON ms.idKichCo = spct.idKichCo.idKichCo " +
            "WHERE spct.idSanPham.idSanPham = :idSanPham")
    List<KichCo> getAllByCT(@Param("idSanPham") Integer idSanPham);

    @Query("SELECT DISTINCT kc FROM KichCo kc " +
            "JOIN SanPhamChiTiet spct ON kc.idKichCo = spct.idKichCo.idKichCo " +
            "WHERE spct.idSanPham.idSanPham = :idSanPham " +
            "AND spct.idMauSac.idMauSac = :idMauSac")
    List<KichCo> getAllByCTAndMauSac(@Param("idSanPham") Integer idSanPham, @Param("idMauSac") Integer idMauSac);

    @Query("SELECT ms FROM KichCo ms where ms.trangThai = true")
    List<KichCo> getAllByTT();
}
