package com.example.demo.Service;

import com.example.demo.dto.request.LichSuNhapHangRequestDTO;
import com.example.demo.entity.LichSuNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LichSuNhapHangService {
    public LichSuNhapHang createLichSuNhapHang(LichSuNhapHangRequestDTO lichSuNhapHangRequestDTO);

    public Page<LichSuNhapHang> findAll(Integer idSanPham, Pageable pageable);
}
