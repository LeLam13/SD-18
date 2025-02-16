package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangCuaKhachService;
import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;

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

    @Override
    public List<DonHang> search(String maDonHang) {
        List<DonHang> donHangList = donHangRepo.searchByMaDonHang(maDonHang);
        return donHangList;
    }

    @Override
    public DonHang cancelStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username) {
        DonHang donHang = donHangRepo.findById(donHangOnlineStatusRequestDTO.getIdDonHang()).get();
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
//            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            khachhang getKH = khachhangRePo.findByIdKhachHang(oldTaiKoan.getKhachHang().getIdKhachHang());
            if(getKH != null){
                donHang.setKhachHang(getKH);
                donHang.setUpdateBy(getKH.getHoTen());
            }
        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
//        KhuyenMai khuyenMai = khuyenMaiRepo.findById(donHang.getKhuyenMai().getIdKhuyenMai()).get();
//        if(khuyenMai != null){
//            khuyenMai.setSoLuong(khuyenMai.getSoLuong()+1);
//            khuyenMaiRepo.save(khuyenMai);
//        }
        if (donHang.getKhuyenMai() != null && donHang.getKhuyenMai().getIdKhuyenMai() != null) {
            Optional<KhuyenMai> optionalKhuyenMai = khuyenMaiRepo.findById(donHang.getKhuyenMai().getIdKhuyenMai());
            if (optionalKhuyenMai.isPresent()) {
                KhuyenMai khuyenMai = optionalKhuyenMai.get();
                khuyenMai.setSoLuong(khuyenMai.getSoLuong() + 1);
                khuyenMaiRepo.save(khuyenMai);
            } else {
                // Xử lý nếu khuyến mãi không tồn tại
                System.out.println("Khuyến mãi không tồn tại!");
            }
        } else {
            // Xử lý nếu `donHang.getKhuyenMai()` hoặc `getIdKhuyenMai()` bị null
            System.out.println("Khuyến mãi hoặc ID khuyến mãi không hợp lệ!");
        }
        List<DonHangChiTiet> chiTietDonHangList = donHangChiTietRepo.findByDonHangId(donHang.getIdDonHang());

        // Cập nhật lại số lượng sản phẩm
        for (DonHangChiTiet chiTiet : chiTietDonHangList) {
            SanPhamChiTiet sanPham = chiTiet.getSanPhamChiTiet();
            sanPham.setSoLuong(sanPham.getSoLuong() + chiTiet.getSoLuong());
            sanPhamChiTietRepo.save(sanPham);
        }

        TrangThai trangThai = trangThaiRepo.findById(6).orElse(null);
        donHang.setTrangThai(trangThai);
        LocalDate localDate = LocalDate.now();
        donHang.setUpdateDate(localDate);

        donHang.setGhiChu(donHangOnlineStatusRequestDTO.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }
}
