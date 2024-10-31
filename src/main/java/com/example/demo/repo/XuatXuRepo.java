package com.example.demo.repo;

import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuatXuRepo extends JpaRepository<XuatXu,Integer> {
    XuatXu findByMa(String ma);

    XuatXu findByIdXuatXu(Integer idXuatXu);
}
