package com.example.demo.repo;

import com.example.demo.entity.khachhang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepo extends JpaRepository<khachhang, Integer> {

}
