package com.example.demo.Service;

import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.entity.khachhang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<khachhang> findAll();
    List<KhachHangResponseDTO> getAllKhachHang();
    // Add a new customer
    KhachHangResponseDTO addKhachHang(KhachHangResponseDTO khachHangRequestDTO);

    // Get all customers with pagination
    Page<KhachHangResponseDTO> getAllKhachHangPaged(Pageable pageable);
}
