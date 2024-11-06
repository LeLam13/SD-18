package com.example.demo.Service.impl;

import com.example.demo.Service.SanPhamChiTietService;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Override
    public Page<SanPhamChiTiet> findBySanPham(Integer idSanPham,Pageable pageable) {
//        Pageable pageable = PageRequest.of(0, 1);
//        Page<SanPhamChiTiet> spct = sanPhamChiTietRepo.findByIdSanPham_IdSanPham(2,pageable);
//        System.out.println("check log page1: "+spct);
//        System.out.println("Total elements: " + spct.getTotalElements());
//        System.out.println("Total pages: " + spct.getTotalPages());
//        System.out.println("Content: " + spct.getContent());
        return sanPhamChiTietRepo.getByID(idSanPham, pageable);
    }

    @Override
    public List<SanPhamChiTiet> getAll() {
        return sanPhamChiTietRepo.findAll();
    }


}
