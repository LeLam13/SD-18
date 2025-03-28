package com.example.demo.repo;

import com.example.demo.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GioHangRepo extends JpaRepository<GioHang,Integer> {
    @Query("SELECT g FROM GioHang g WHERE g.taiKhoan.username = :username")
    GioHang findByUsername(@Param("username") String username);
}
