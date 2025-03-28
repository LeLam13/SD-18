package com.example.demo.repo;

import com.example.demo.entity.khachhang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface khachhangRePo extends JpaRepository<khachhang,Integer> {
    @Query("SELECT k FROM khachhang k WHERE k.soDienThoai = :soDienThoai")
    List<khachhang> findBySoDienThoai(String soDienThoai);


    @Query("SELECT k FROM khachhang k WHERE k.taikhoan.username = :username")
    khachhang findByUsername(@Param("username") String username);
}
