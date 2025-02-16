package com.example.demo.repo;

import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XuatXuRepo extends JpaRepository<XuatXu,Integer> {
    XuatXu findByMa(String ma);

    XuatXu findByIdXuatXu(Integer idXuatXu);

    @Query("SELECT m FROM XuatXu m WHERE " +
            "LOWER(REPLACE(m.ma, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd')) OR " +
            "LOWER(REPLACE(m.ten, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd'))")
    Page<XuatXu> searchIgnoreCaseAndDiacritics(@Param("query") String query, Pageable pageable);

    @Query("SELECT ms FROM XuatXu ms where ms.trangThai = true")
    List<XuatXu> getAllByTT();
}
