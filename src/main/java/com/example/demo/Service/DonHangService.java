package com.example.demo.Service;

import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangRequestDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.khachhang;

import java.util.List;

public interface DonHangService {
    List<SanPhamChiTiet> getAllProducts();

    DonHang createDonHAng(DonHangRequestDTO donHangDTO, String username);

    List<DonHangTongSoLuongResponseDTO> getTongSoLuongDonHang();

    List<DonHangChiTiet> getAllProductsOrder(Integer id);

    DonHangChiTiet updateDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO);
    DonHangChiTiet createDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO);


    boolean deleDonHangChiTiet(Integer id);

    boolean deleDonHang(Integer id);


    List<khachhang> getAllKhachHang();

    KhachHangResponseDTO getKhachHangById(Integer id);

    khachhang addKhachHang(KhachHangRequestDTO khachHangRequestDTO,String username);

    DonHang updateDonHangKH(Integer idDH, Integer id);

    DonHang getDonHangByID(Integer id);

    List<khachhang> searchKhachHang(String sdt);
    List<SanPhamChiTiet> searchSanPhamChiTiet(String tenSP);
}
