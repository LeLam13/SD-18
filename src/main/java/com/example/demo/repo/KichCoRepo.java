package com.example.demo.repo;

import com.example.demo.entity.KichCo;
import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichCoRepo extends JpaRepository<KichCo,Integer> {
    KichCo findByMa(String ma);

    KichCo findByIdKichCo(Integer idKichCo);
}
