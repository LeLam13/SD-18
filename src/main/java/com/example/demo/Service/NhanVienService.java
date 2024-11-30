package com.example.demo.Service;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.nhanvien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NhanVienService {
    Page<nhanvien> getAll(Pageable pageable);

    public nhanvien updateNhanVien(NhanVienRequetsDTO nhanVienRequestDTO);

    public nhanvien getNhanVien(Integer idNhanVien);
    Page<nhanvien> getActiveNhanVien(Pageable pageable);
    nhanvien softDeleteNhanVien(Integer idNhanVien);
    Page<nhanvien> searchNhanVien(String keyword, Pageable pageable);
}
