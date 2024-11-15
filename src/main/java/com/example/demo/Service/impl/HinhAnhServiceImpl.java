package com.example.demo.Service.impl;

import com.example.demo.Service.HinhAnhService;
import com.example.demo.dto.request.HinhAnhRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.MauSac;
import com.example.demo.repo.HinhAnhRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HinhAnhServiceImpl implements HinhAnhService {
    @Autowired
    private HinhAnhRepo hinhAnhRepo;

    Date date=new Date();

    @Override
    public HinhAnh findbyTen(String ten){
        return hinhAnhRepo.findByTen(ten);
    }

    @Override
    public HinhAnh createHinhAnh(HinhAnhRequestDTO hinhAnhRequestDTO) {
        HinhAnh ms = new HinhAnh();
        ms.setMa(hinhAnhRequestDTO.getMa());
        ms.setTen(hinhAnhRequestDTO.getTen());
        ms.setCreateDate(date);
        ms.setTrangThai(true);
        return hinhAnhRepo.save(ms);
    }
}
