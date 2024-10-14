package com.example.demo.Service;

import com.example.demo.entity.MauSac;
import com.example.demo.repo.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {
    @Autowired
    private MauSacRepo mauSacRepo;

    @Override
    public List<MauSac> getAll(){
        return mauSacRepo.findAll();
    }


}
