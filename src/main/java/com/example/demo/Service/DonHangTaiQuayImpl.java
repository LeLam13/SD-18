package com.example.demo.Service;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.repo.DonHangChiTietRepo;
import com.example.demo.repo.DonHangRepo;
import com.example.demo.repo.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangTaiQuayImpl implements DonHangTaiQuayService{
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

    @Override
    public List<DonHang> getAllOrder() {
        List<DonHang>  donHang = donHangRepo.findAll();
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
}
