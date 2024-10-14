package com.example.demo.Service;

import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;

import java.util.List;

public interface DonHangService {
    List<SanPhamChiTiet> getAllProducts();

    DonHang createDonHAng(DonHangRequestDTO donHangDTO, String username);

    List<DonHangTongSoLuongResponseDTO> getTongSoLuongDonHang();

    List<DonHangChiTiet> getAllProductsOrder(Integer id);

    DonHangChiTiet updateDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO);
    DonHangChiTiet createDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO);
}
