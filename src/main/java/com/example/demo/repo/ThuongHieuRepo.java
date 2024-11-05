package com.example.demo.repo;

import com.example.demo.entity.MauSac;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuongHieuRepo extends JpaRepository<ThuongHieu,Integer> {
    ThuongHieu findByMa(String ma);

    ThuongHieu findByIdThuongHieu(Integer idThuongHieu);
}
