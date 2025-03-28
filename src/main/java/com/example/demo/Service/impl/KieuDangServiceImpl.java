package com.example.demo.Service.impl;

import com.example.demo.Service.KieuDangService;
import com.example.demo.dto.request.KieuDangRequestDTO;
import com.example.demo.entity.KieuDang;
import com.example.demo.repo.KieuDangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KieuDangServiceImpl implements KieuDangService {
    @Autowired
    private KieuDangRepo kieuDangRepo;

    Date date = new Date();

    @Override
    public List<KieuDang> getAll() {
        return kieuDangRepo.findAll();
    }

    @Override
    public Page<KieuDang> findAll(Pageable pageable) {
        return kieuDangRepo.findAll(pageable);
    }


    @Override
    public KieuDang createKieuDang(KieuDangRequestDTO kieuDangRequestDTO) {
        KieuDang kd = new KieuDang();
        kd.setMa(kieuDangRequestDTO.getMa());
        kd.setTen(kieuDangRequestDTO.getTen());
        kd.setCreateDate(date);
        kd.setUpdateDate(date);
        kd.setTrangThai(true);
        return kieuDangRepo.save(kd);
    }

    @Override
    public KieuDang updateKieuDang(KieuDangRequestDTO kieuDangRequestDTO) {
        KieuDang kd = kieuDangRepo.findByMa(kieuDangRequestDTO.getMa());
        kd.setTen(kieuDangRequestDTO.getTen());
        kd.setUpdateDate(date);
        return kieuDangRepo.save(kd);
    }

    @Override
    public KieuDang getKieuDang(String ma) {
        return kieuDangRepo.findByMa(ma);
    }

    @Override
    public KieuDang updateTrangThai(Integer idKieuDang) {
        KieuDang kd = kieuDangRepo.findByIdKieuDang(idKieuDang);
        if(kd.getTrangThai()==true){
            kd.setTrangThai(false);
        }
        else{
            kd.setTrangThai(true);
        }
        return kieuDangRepo.save(kd);
    }


    @Override
    public KieuDang deleteKieuDang(Integer idKieuDang) {
        kieuDangRepo.deleteById(idKieuDang);
        return null;
    }


}
