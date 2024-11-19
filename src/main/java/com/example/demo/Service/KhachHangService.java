package com.example.demo.Service;

import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.entity.khachhang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhachHangService {
    // Get all customers
    List<khachhang> findAll();

    // Get all customers as DTOs
    List<KhachHangResponseDTO> getAllKhachHang();

    // Add a new customer
    KhachHangResponseDTO addKhachHang(KhachHangRequestDTO khachHangRequestDTO);

    // Update an existing customer
    KhachHangResponseDTO updateKhachHang(Long id, KhachHangRequestDTO khachHangRequestDTO);

    // Get all customers with pagination
    Page<KhachHangResponseDTO> getAllKhachHangPaged(Pageable pageable);
}
