package com.example.demo.repo;

import com.example.demo.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhAnhRepo extends JpaRepository<HinhAnh,Integer> {
    HinhAnh findByTen(String ten);

    HinhAnh findByIdHinhAnh(Integer idHinhAnh);
}
