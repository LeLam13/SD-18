package com.example.demo.Service.impl;

import com.example.demo.Service.QuanLyDonHangOnlineService;
import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanLyDonHangOnlineServiceImpl implements QuanLyDonHangOnlineService {
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;

    @Override
    public List<DonHang> getAllOrderByOrderType() {
        List<DonHang> listOrder = donHangRepo.findDonHangByLoaiDonHang(2);
        return listOrder;
    }

    @Override
    public List<DonHangChiTiet>  getOrderByIdOrderType(Integer id) {
        List<DonHangChiTiet>  donHangChiTiet = donHangChiTietRepo.findByDonHangId(id);
        return donHangChiTiet;
    }

    @Override
    public DonHang updateStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username) {
        DonHang donHang = donHangRepo.findById(donHangOnlineStatusRequestDTO.getIdDonHang()).get();
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            donHang.setNhanVien(getNV);

        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }

        int currentTrangThaiId = donHang.getTrangThai().getIdTrangThai();

        if (currentTrangThaiId == 1) {
            TrangThai trangThai = trangThaiRepo.findById(7).orElse(null);
            donHang.setTrangThai(trangThai);
        } else if (currentTrangThaiId == 7) {
            TrangThai trangThai = trangThaiRepo.findById(2).orElse(null);
            donHang.setTrangThai(trangThai);
        } else if (currentTrangThaiId == 2) {
            TrangThai trangThai = trangThaiRepo.findById(3).orElse(null);
            donHang.setTrangThai(trangThai);
        } else if (currentTrangThaiId == 3) {
            TrangThai trangThai = trangThaiRepo.findById(5).orElse(null);
            donHang.setTrangThai(trangThai);
        }

//        if(donHang.getTrangThai().getIdTrangThai() ==3){
//            TrangThai trangThai = trangThaiRepo.findById(4).get();
//            donHang.setTrangThai(trangThai);
//        }

        donHang.setGhiChu(donHangOnlineStatusRequestDTO.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }
}
