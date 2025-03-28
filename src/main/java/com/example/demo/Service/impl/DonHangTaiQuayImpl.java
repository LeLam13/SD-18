package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangTaiQuayService;
import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.TrangThai;
import com.example.demo.repo.DonHangChiTietRepo;
import com.example.demo.repo.DonHangRepo;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.TrangThaiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangTaiQuayImpl implements DonHangTaiQuayService {
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;

    @Override
    public List<DonHang> getAllOrder() {
        //List<DonHang>  donHang = donHangRepo.findAll();
        List<DonHang> donHang = donHangRepo.findAll(Sort.by(Sort.Direction.DESC, "idDonHang"));
        return donHang;
    }

    @Override
    public List<DonHangChiTiet> getOrderDetailById(Integer id) {
        DonHang donHang = donHangRepo.findById(id).get();
        if(donHang == null){
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        List<DonHangChiTiet> donHangChiTiet = donHangChiTietRepo.findByDonHangId(id);
        if(donHangChiTiet == null){
            throw new RuntimeException("Đơn hàng không có đơn hàng chi tiết!");
        }
        return donHangChiTiet;
    }

    @Override
    public DonHang updateOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus) {
        DonHang oldOrder = donHangRepo.findById(donHangStatus.getIdDonHang()).get();
        if(oldOrder == null){
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        TrangThai status = new TrangThai();
        status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()).get();
        if(donHangStatus.getIdTrangThai() < 5){
            status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()+1).get();
        }

        oldOrder.setTrangThai(status);
        donHangRepo.save(oldOrder);
        return oldOrder;
    }
}
