package com.example.demo.repo;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {


}

