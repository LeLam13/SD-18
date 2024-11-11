package com.example.demo.Service;

import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamService {
    public List<SanPham> getAll();

    public Page<SanPham> findAll(Pageable pageable);

    public SanPham createSanPham(SanPhamRequestDTO sanPhamRequestDTO);

    public SanPham getSanPham(String ma);

    public SanPham updateSanPham(SanPhamRequestDTO sanPhamRequestDTO);

    public SanPham updateTrangThai(Integer idSanPham);

    public SanPham getByIdSanPham(Integer idSanPham);
}
