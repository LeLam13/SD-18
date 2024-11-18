package com.example.demo.Service;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import java.util.List;

public interface DonHangCuaKhachService {

    List<DonHang> getAllOrderUsername(String username);

    List<DonHangChiTiet> getAllProductsOrder(Integer id);
}
