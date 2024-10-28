package com.example.demo.repo;

import com.example.demo.entity.khachhang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface khachhangRePo extends JpaRepository<khachhang, Integer> {
    @Query("SELECT k FROM khachhang k WHERE k.soDienThoai = :soDienThoai")
    List<khachhang> findBySoDienThoai( String soDienThoai);
}
