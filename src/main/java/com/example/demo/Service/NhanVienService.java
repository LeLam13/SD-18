package com.example.demo.Service;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.nhanvien;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NhanVienService {
    public List<nhanvien> getAll();

    public nhanvien updateNhanVien(NhanVienRequetsDTO nhanVienRequestDTO);

    public nhanvien getNhanVien(Integer idNhanVien);

    public nhanvien deleteNhanVien(Integer idNhanVien);
}
