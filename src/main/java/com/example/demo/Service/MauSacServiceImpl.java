package com.example.demo.Service;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.repo.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {
    @Autowired
    private MauSacRepo mauSacRepo;

    Date date = new Date();

    @Override
    public List<MauSac> getAll() {
        return mauSacRepo.findAll();
    }

    @Override
    public MauSac createMauSac(MauSacRequestDTO mauSacRequestDTO) {
        MauSac ms = new MauSac();
        ms.setMa(mauSacRequestDTO.getMa());
        ms.setTen(mauSacRequestDTO.getTen());
        ms.setCreateDate(date);
        ms.setUpdateDate(date);
        ms.setTrangThai(true);
        return mauSacRepo.save(ms);
    }

    @Override
    public MauSac updateMauSac(MauSacRequestDTO mauSacRequestDTO) {
        MauSac ms = mauSacRepo.findByMa(mauSacRequestDTO.getMa());
        ms.setTen(mauSacRequestDTO.getTen());
        ms.setUpdateDate(date);
        return mauSacRepo.save(ms);
    }

    @Override
    public MauSac getMauSac(String ma) {
        return mauSacRepo.findByMa(ma);
    }

    @Override
    public MauSac updateTrangThai(Integer idMauSac) {
        MauSac ms = mauSacRepo.findByIdMauSac(idMauSac);
        if(ms.getTrangThai()==true){
            ms.setTrangThai(false);
        }
        else{
            ms.setTrangThai(true);
        }
        return mauSacRepo.save(ms);
    }


    @Override
    public MauSac deleteMauSac(Integer idMauSac) {
        mauSacRepo.deleteById(idMauSac);
        return null;
    }


}
