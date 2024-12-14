package com.example.demo.Service;

import com.example.demo.dto.request.KichCoRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KichCoService {

    public List<KichCo> getAll();

    public Page<KichCo> findAll(Pageable pageable);

    public KichCo createKichCo(KichCoRequestDTO kichCoRequestDTO);

    public KichCo updateKichCo(KichCoRequestDTO kichCoRequestDTO);

    public KichCo getKichCo(String ma);

    public KichCo updateTrangThai(Integer idKichCo);

    public KichCo deleteKichCo(Integer idKichCo);

    public Page<KichCo> search(String query, Pageable pageable);

    public List<KichCo> getAllbyCT(Integer idSanPham);

    public List<KichCo> getAllbyMS(Integer idSanPham, Integer idMauSac);


    public List<KichCo> getAllByTT();


}
