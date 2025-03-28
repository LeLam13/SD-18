package com.example.demo.Service.impl;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.entity.khachhang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.repo.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    khachhangRePo khachHangRepo; // Use the repository for data access


    @Override
    public List<khachhang> findAll() {
        return khachHangRepo.findAll();
    }

    @Override
    public List<KhachHangResponseDTO> getAllKhachHang() {
        List<khachhang> khachhangList = khachHangRepo.findAll();
        return khachhangList.stream().map(kh -> {
            KhachHangResponseDTO dto = new KhachHangResponseDTO();
            dto.setId_khach_hang(kh.getIdKhachHang());
            dto.setMaKhachHang(kh.getMaKhachHang());
            dto.setHoTen(kh.getHoTen());
            dto.setNgaySinh(kh.getNgaySinh().toString()); // Convert LocalDate to String
            dto.setSoDienThoai(kh.getSoDienThoai());
            dto.setGioiTinh(kh.isGioiTinh()); // Convert boolean to String
            dto.setDiaChi(kh.getDiaChi());
            dto.setUsernameTaiKhoan(kh.getTaikhoan().getUsername()); // Set username from taikhoan
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public KhachHangResponseDTO addKhachHang(KhachHangResponseDTO khachHangRequestDTO) {
        return null;
    }

    @Override
    public Page<KhachHangResponseDTO> getAllKhachHangPaged(Pageable pageable) {
        return null;
    }

}
