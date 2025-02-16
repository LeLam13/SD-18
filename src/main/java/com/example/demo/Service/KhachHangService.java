package com.example.demo.Service;

import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.reponse.LichSuMuaHangResponseDTO;
import com.example.demo.entity.khachhang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhachHangService {

    List<khachhang> findAll();

    List<KhachHangResponseDTO> getAllKhachHang();

    KhachHangResponseDTO addKhachHang(KhachHangRequestDTO khachHangRequestDTO);

    KhachHangResponseDTO updateKhachHang(Long id, KhachHangRequestDTO khachHangRequestDTO);

    Page<KhachHangResponseDTO> getAllKhachHangPaged(String searchQuery, Pageable pageable);

    List<LichSuMuaHangResponseDTO> getLichSuMuaHang(Integer idKhachHang);
}
