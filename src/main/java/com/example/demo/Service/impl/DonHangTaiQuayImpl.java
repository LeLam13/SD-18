package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangTaiQuayService;
import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;

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
        TrangThai status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()).get();
//        if(donHangStatus.getIdTrangThai() < 5){
//            status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()+1).get();
//        }

        oldOrder.setTrangThai(status);
        donHangRepo.save(oldOrder);
        return oldOrder;
    }

    @Override
    public List<DonHang> searchMaDonhang(String maHD) {
        return donHangRepo.searchByMaDonHang(maHD);
    }

    @Override
    public List<DonHang> searchLoaiDonhang(Integer loaiDonHang) {
        return donHangRepo.findDonHangByLoaiDonHangNative(loaiDonHang);
    }

    @Override
    public List<DonHang> searchNgayTao(LocalDate startDate, LocalDate endDate) {
        return donHangRepo.findDonHangByDateRangeNative(startDate,endDate);
    }

    @Override
    public List<TrangThai> getAllStatus() {
        return trangThaiRepo.findAll();
    }

    @Override
    public DonHang cancelOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus,String username) {
        DonHang donHang = donHangRepo.findById(donHangStatus.getIdDonHang()).get();
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
//            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            if(getNV != null){
                donHang.setNhanVien(getNV);
                donHang.setUpdateBy(getNV.getHoTen());
            }
        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
        if(donHang.getKhuyenMai() != null){
            KhuyenMai khuyenMai = null;
            Optional<KhuyenMai> optionalKhuyenMai = khuyenMaiRepo.findById(donHang.getKhuyenMai().getIdKhuyenMai());

            if (optionalKhuyenMai.isPresent()) {
                khuyenMai = optionalKhuyenMai.get();
                System.out.println("check khuyenmaix: " + khuyenMai);

                // Tăng số lượng khuyến mãi và lưu lại
                khuyenMai.setSoLuong(khuyenMai.getSoLuong() + 1);
                khuyenMaiRepo.save(khuyenMai);
            }
        }

        TrangThai trangThai = trangThaiRepo.findById(6).orElse(null);
        donHang.setTrangThai(trangThai);
        LocalDate localDate = LocalDate.now();
        donHang.setUpdateDate(localDate);

        donHang.setGhiChu(donHangStatus.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }
}
