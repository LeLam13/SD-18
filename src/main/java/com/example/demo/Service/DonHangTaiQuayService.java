package com.example.demo.Service;

import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import java.util.List;

public interface DonHangTaiQuayService {
    List<DonHang> getAllOrder();

    List<DonHangChiTiet> getOrderDetailById(Integer id);

    DonHang updateOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus);
}
