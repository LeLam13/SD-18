package com.example.demo.Service.impl;

import com.example.demo.Service.KichCoService;
import com.example.demo.dto.request.KichCoRequestDTO;
import com.example.demo.entity.KichCo;
import com.example.demo.repo.KichCoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KichCoServiceImpl implements KichCoService {
    @Autowired
    private KichCoRepo kichCoRepo;

    Date date = new Date();

    @Override
    public List<KichCo> getAll() {
        return kichCoRepo.findAll();
    }

    @Override
    public Page<KichCo> findAll(Pageable pageable) {
        return kichCoRepo.findAll(pageable);
    }

    @Override
    public KichCo createKichCo(KichCoRequestDTO kichCoRequestDTO) {
        KichCo kc = new KichCo();
        kc.setMa(kichCoRequestDTO.getMa());
        kc.setTen(kichCoRequestDTO.getTen());
        kc.setCreateDate(date);
        kc.setTrangThai(true);
        return kichCoRepo.save(kc);
    }

    @Override
    public KichCo updateKichCo(KichCoRequestDTO kichCoRequestDTO) {
        KichCo kc = kichCoRepo.findByMa(kichCoRequestDTO.getMa());
        kc.setTen(kichCoRequestDTO.getTen());
        kc.setUpdateDate(date);
        return kichCoRepo.save(kc);
    }

    @Override
    public KichCo getKichCo(String ma) {
        return kichCoRepo.findByMa(ma);
    }

    @Override
    public KichCo updateTrangThai(Integer idKichCo) {
        KichCo kc = kichCoRepo.findByIdKichCo(idKichCo);
        if(kc.getTrangThai()==true){
            kc.setTrangThai(false);
        }
        else{
            kc.setTrangThai(true);
        }
        return kichCoRepo.save(kc);
    }


    @Override
    public KichCo deleteKichCo(Integer idKichCo) {
        kichCoRepo.deleteById(idKichCo);
        return null;
    }


}
