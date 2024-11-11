package com.example.demo.Service;

import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamChiTietService {
    Page<SanPhamChiTiet> findBySanPham(Integer idSanPham, Pageable pageable);

    public List<SanPhamChiTiet> getAll();

    public List<SanPhamChiTiet> createSanPhamChiTietList(List<SanPhamChiTietRequestDTO> sanPhamChiTietRequestDTOList);
}