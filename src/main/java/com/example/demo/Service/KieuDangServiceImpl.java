package com.example.demo.Service;

import com.example.demo.entity.KieuDang;
import com.example.demo.repo.KieuDangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KieuDangServiceImpl implements KieuDangService {
    @Autowired
    private KieuDangRepo kieuDangRepo;

    @Override
    public List<KieuDang> getAll(){
        return kieuDangRepo.findAll();
    }
}
