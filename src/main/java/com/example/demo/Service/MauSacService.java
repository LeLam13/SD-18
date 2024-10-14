package com.example.demo.Service;

import com.example.demo.entity.MauSac;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MauSacService {

    public List<MauSac> getAll();
}
