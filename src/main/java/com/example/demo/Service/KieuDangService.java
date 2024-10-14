package com.example.demo.Service;

import com.example.demo.entity.KieuDang;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KieuDangService {
   public List<KieuDang> getAll();
}
