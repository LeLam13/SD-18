package com.example.demo.Service;

import com.example.demo.entity.ThuongHieu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThuongHieuService {

    public List<ThuongHieu> findAll();
}
