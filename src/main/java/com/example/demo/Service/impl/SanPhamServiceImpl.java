package com.example.demo.Service.impl;

import com.example.demo.Service.SanPhamService;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.SanPham;
import com.example.demo.repo.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SanPhamServiceImpl implements SanPhamService {
    @Autowired
    private SanPhamRepo sanPhamRepo;

    Date date = new Date();

    @Override
    public List<SanPham> getAll() {
        return sanPhamRepo.findAll();
    }

    @Override
    public Page<SanPham> findAll(Pageable pageable) {
        return sanPhamRepo.findAll(pageable);
    }

    @Override
    public SanPham createSanPham(SanPhamRequestDTO sanPhamRequestDTO) {
        SanPham sp = new SanPham();
        sp.setMa(sanPhamRequestDTO.getMa());
        sp.setTen(sanPhamRequestDTO.getTen());
        sp.setCreateDate(date);
        sp.setTrangThai(true);
        return sanPhamRepo.save(sp);
    }

    @Override
    public SanPham updateSanPham(SanPhamRequestDTO sanPhamRequestDTO) {
        SanPham ms = sanPhamRepo.findByMa(sanPhamRequestDTO.getMa());
        ms.setTen(sanPhamRequestDTO.getTen());
        ms.setUpdateDate(date);
        return sanPhamRepo.save(ms);
    }

    @Override
    public SanPham getSanPham(String ma) {
        return sanPhamRepo.findByMa(ma);
    }

    @Override
    public SanPham getByIdSanPham(Integer idSanPham) {
        return sanPhamRepo.findByIdSanPham(idSanPham);
    }


    @Override
    public SanPham updateTrangThai(Integer idSanPham) {
        SanPham ms = sanPhamRepo.findByIdSanPham(idSanPham);
        if(ms.getTrangThai()==true){
            ms.setTrangThai(false);
        }
        else{
            ms.setTrangThai(true);
        }
        return sanPhamRepo.save(ms);
    }

}
