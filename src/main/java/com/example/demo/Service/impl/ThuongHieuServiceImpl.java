package com.example.demo.Service.impl;

import com.example.demo.Service.ThuongHieuService;
import com.example.demo.dto.request.ThuongHieuRequestDTO;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.repo.ThuongHieuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    Date date=new Date();

    @Override
    public List<ThuongHieu> getAll() {
        return thuongHieuRepo.findAll();
    }

    @Override
    public Page<ThuongHieu> findAll(Pageable pageable) {
        return thuongHieuRepo.findAll(pageable);
    }

    @Override
    public ThuongHieu createThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO) {
        ThuongHieu th = new ThuongHieu();
        th.setMa(thuongHieuRequestDTO.getMa());
        th.setTen(thuongHieuRequestDTO.getTen());
        th.setCreateDate(date);
        th.setUpdateDate(date);
        th.setTrangThai(true);
        return thuongHieuRepo.save(th);
    }

    @Override
    public ThuongHieu updateThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO) {
        ThuongHieu th = thuongHieuRepo.findByMa(thuongHieuRequestDTO.getMa());
        th.setTen(thuongHieuRequestDTO.getTen());
        th.setUpdateDate(date);
        return thuongHieuRepo.save(th);
    }

    @Override
    public ThuongHieu getThuongHieu(String ma) {
        return thuongHieuRepo.findByMa(ma);
    }

    @Override
    public ThuongHieu updateTrangThai(Integer idThuongHieu) {
        ThuongHieu th = thuongHieuRepo.findByIdThuongHieu(idThuongHieu);
        if(th.getTrangThai()==true){
            th.setTrangThai(false);
        }
        else{
            th.setTrangThai(true);
        }
        return thuongHieuRepo.save(th);
    }


    @Override
    public ThuongHieu deleteThuongHieu(Integer idThuongHieu) {
        thuongHieuRepo.deleteById(idThuongHieu);
        return null;
    }
}
