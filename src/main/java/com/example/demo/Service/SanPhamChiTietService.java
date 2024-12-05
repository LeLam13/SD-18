package com.example.demo.Service;

import com.example.demo.dto.request.FilterRequestDTO;
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
    public Page<SanPhamChiTiet> findAll(Pageable pageable);

    Page<SanPhamChiTiet> findBySanPham(Integer idSanPham, Pageable pageable);

    public List<SanPhamChiTiet> getAll();

    public List<SanPhamChiTiet> getAllByIdSanPham(Integer idSanPham);

    public List<SanPhamChiTiet> createSanPhamChiTietList(List<SanPhamChiTietRequestDTO> sanPhamChiTietRequestDTOList);

    public SanPhamChiTiet getSanPhamChiTiet(String ma);

    public SanPhamChiTiet updateSanPhamChiTiet(SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO);

    public SanPhamChiTiet updateTrangThai(Integer idSanPhamChiTiet);

    Page<SanPhamChiTiet> filterProducts(FilterRequestDTO filterRequest,Pageable pageable);

    public SanPhamChiTiet updateNhapHang(SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO);

    public SanPhamChiTiet checkEx(Integer idSanPham, Integer idMauSac, Integer idKichCo);

    public SanPhamChiTiet updateBySize(SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO);
}