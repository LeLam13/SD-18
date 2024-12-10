package com.example.demo.Service;

import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import java.time.LocalDate;
import java.util.List;

public interface DonHangTaiQuayService {
    List<DonHang> getAllOrder();

    List<DonHangChiTiet> getOrderDetailById(Integer id);

    DonHang updateOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus);

    List<DonHang> searchMaDonhang(String maHD);

    List<DonHang> searchLoaiDonhang(Integer loaiDonHang);

    List<DonHang> searchNgayTao(LocalDate startDate, LocalDate endDate);
}
