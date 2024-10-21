package com.example.demo.repo;

import com.example.demo.entity.KieuDang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KieuDangRepo extends JpaRepository<KieuDang,Integer> {
    KieuDang findByMa(String ma);

    KieuDang findByIdKieuDang(Integer idKieuDang);
}
