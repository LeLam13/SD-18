package com.example.demo.Service;

import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import java.util.List;

public interface QuanLyDonHangOnlineService {
    List<DonHang> getAllOrderByOrderType();

    List<DonHangChiTiet>  getOrderByIdOrderType(Integer id);

    DonHang updateStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO,String username);
}
