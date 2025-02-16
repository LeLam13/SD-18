package com.example.demo.Service;

import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import java.util.List;

public interface DonHangCuaKhachService {

    List<DonHang> getAllOrderUsername(String username);

    List<DonHangChiTiet> getAllProductsOrder(Integer id);

    List<DonHang> search(String maDonHang);
    DonHang cancelStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username);
}
