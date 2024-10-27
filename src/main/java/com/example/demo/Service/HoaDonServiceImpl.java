package com.example.demo.Service;

import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class HoaDonServiceImpl implements HoaDonService{
    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    khachhangRePo khachhangRePo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    PhuongThucThanhToanRepo phuongThucThanhToanRepo;

    @Override
    public List<HoaDon> getAllHoaDons() {
        return hoaDonRepo.findAll();  // Retrieve all invoices
    }

    @Override
    public HoaDon createHoaDon(HoaDonResquestDTO hoaDon, String username) {
        return null;
    }

    @Override
    public String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }



}