package com.example.demo.Service;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    Date date = new Date();

    @Override
    public Page<SanPhamChiTiet> findBySanPham(Integer idSanPham, Pageable pageable) {
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

    @Override
    public SanPhamChiTiet createSanPhamChiTiet(SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO) {
        SanPhamChiTiet ms = new SanPhamChiTiet();
        ms.setMa(sanPhamChiTietRequestDTO.getMa());
        ms.setIdMauSac(sanPhamChiTietRequestDTO.getIdMauSac());
        ms.setIdKichCo(sanPhamChiTietRequestDTO.getIdKichCo());
        ms.setCreateDate(date);
        ms.setTrangThai(true);
        return sanPhamChiTietRepo.save(ms);
    }
}
