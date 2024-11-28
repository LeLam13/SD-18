package com.example.demo.Service;

import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamService {
    List<SanPham> getAll();

    Page<SanPham> findAll(Pageable pageable);

    SanPham createSanPham(SanPhamRequestDTO sanPhamRequestDTO);

    SanPham getSanPham(String ma);

    SanPham updateSanPham(SanPhamRequestDTO sanPhamRequestDTO);

    SanPham updateTrangThai(Integer idSanPham);

    SanPham getByIdSanPham(Integer idSanPham);

    // Hàm bổ sung
    Page<SanPham> findAllWithStatistics(Pageable pageable); // Tìm tất cả sản phẩm kèm số lượng tồn và đã bán
}
