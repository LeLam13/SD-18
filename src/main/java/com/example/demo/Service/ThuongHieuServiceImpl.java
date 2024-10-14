package com.example.demo.Service;

import com.example.demo.entity.ThuongHieu;
import com.example.demo.repo.ThuongHieuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    @Override
    public List<ThuongHieu> findAll() {
        return thuongHieuRepo.findAll();
    }
}
