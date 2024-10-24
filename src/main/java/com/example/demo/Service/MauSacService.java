package com.example.demo.Service;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.MauSac;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MauSacService {

    public List<MauSac> getAll();

    public MauSac createMauSac(MauSacRequestDTO mauSacRequestDTO);

    public MauSac updateMauSac(MauSacRequestDTO mauSacRequestDTO);

    public MauSac getMauSac(String ma);

    public MauSac updateTrangThai(Integer idMauSac);

    public MauSac deleteMauSac(Integer idMauSac);

}
