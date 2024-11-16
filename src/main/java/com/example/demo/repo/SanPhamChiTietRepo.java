package com.example.demo.repo;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;

//import org.hibernate.mapping.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query("SELECT spct FROM SanPhamChiTiet spct JOIN spct.idSanPham sp WHERE LOWER(sp.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<SanPhamChiTiet> findBySanPhamTenContainingIgnoreCase(String ten);


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

