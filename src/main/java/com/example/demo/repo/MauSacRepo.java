package com.example.demo.repo;

import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepo extends JpaRepository<MauSac,Integer> {
    MauSac findByMa(String ma);

    MauSac findByIdMauSac(Integer idMauSac);
}
