package com.example.demo.repo;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {
    @Query(value = "SELECT * FROM hoa_don WHERE ma_hoa_don = :maHoaDon", nativeQuery = true)
    HoaDon findByMaHoaDon(String maHoaDon);
}
