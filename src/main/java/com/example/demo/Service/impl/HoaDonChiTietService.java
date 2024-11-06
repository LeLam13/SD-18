package com.example.demo.Service.impl;

import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repo.HoaDonChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietService {
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;

    public List<HoaDonChiTiet> findById(Integer id) {
        List<HoaDonChiTiet> listHDCT= hoaDonChiTietRepo.findById1(id);
        System.out.println("123"+ listHDCT);
        return hoaDonChiTietRepo.findById1(id);
    }

}
