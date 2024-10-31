package com.example.demo.repo;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {
    @Query(value = "SELECT * FROM hoa_don WHERE ma_hoa_don LIKE %:maHoaDon%", nativeQuery = true)
    List<HoaDon> findByMaHoaDonContaining(String maHoaDon);

    HoaDon findByMaHoaDon(String maHoaDon);

}

