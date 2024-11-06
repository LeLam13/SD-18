package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangOnlineServiceImpl implements DonHangOnlineService {
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

    @Override
    public List<SanPhamChiTiet> getAllProducts() {
        List<SanPhamChiTiet> listSanPham = sanPhamChiTietRepo.findAll();
//
        return listSanPham;
    }

    @Override
    public SanPhamChiTiet getProductsByID(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(id).get();
        return sanPhamChiTiet;
    }

//    @Override
//    public Page<SanPhamChiTiet> findByIdSanPham(Integer idSanPham, Pageable pageable) {
//        Pageable pageable1 = PageRequest.of(0, 1);
//        Page<SanPhamChiTiet> spct = sanPhamChiTietRepo.findByIdSanPham_IdSanPham(2,pageable1);
//        List<SanPhamChiTiet> listSanPham1 = sanPhamChiTietRepo.findCustomBySanPhamId(1);
//        System.out.println("check log page: "+listSanPham1);
//        System.out.println("check log page1: "+spct);
//        System.out.println("Total elements: " + spct.getTotalElements());
//        System.out.println("Total pages: " + spct.getTotalPages());
//        System.out.println("Content: " + spct.getContent());
//        return spct;
//    }
}
