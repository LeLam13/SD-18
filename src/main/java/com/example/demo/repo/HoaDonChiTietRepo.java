package com.example.demo.repo;

import com.example.demo.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepo extends JpaRepository<HoaDonChiTiet, Integer> {
    //    Optional<HoaDonChiTiet> findById(Integer id);
    @Query("SELECT d FROM HoaDonChiTiet d WHERE d.hoaDon.idHoaDon= :idHoaDonChiTiet")
    List<HoaDonChiTiet> findById1(Integer idHoaDonChiTiet);
}
