package com.example.demo.repo;

import com.example.demo.entity.DonHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangChiTietRepo extends JpaRepository<DonHangChiTiet,Integer> {
    @Query("SELECT d FROM DonHangChiTiet d WHERE d.donHang.idDonHang = :idDonHang")
    List<DonHangChiTiet> findByDonHangId(Integer idDonHang);
}
