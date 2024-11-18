package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangCuaKhachService;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.khachhang;
import com.example.demo.entity.taikhoan;
import com.example.demo.repo.DonHangChiTietRepo;
import com.example.demo.repo.DonHangRepo;
import com.example.demo.repo.khachhangRePo;
import com.example.demo.repo.taikhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangCuaKhacServiceImpl implements DonHangCuaKhachService {
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    khachhangRePo khachhangRePo;

    @Override
    public List<DonHang> getAllOrderUsername(String username) {
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if (oldTaiKoan == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        khachhang khachhang = khachhangRePo.findByUsername(username);
        //System.out.println("check username: " + khachhang);
        List<DonHang> donHangList = donHangRepo.findByKhachHangId(khachhang.getIdKhachHang());

        return donHangList;
    }

    @Override
    public List<DonHangChiTiet> getAllProductsOrder(Integer id) {
        return donHangChiTietRepo.findByDonHangId(id);
    }
}
