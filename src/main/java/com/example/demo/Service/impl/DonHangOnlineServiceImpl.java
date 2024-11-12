package com.example.demo.Service.impl;


import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;

import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangOnlineRequestDTO;
import com.example.demo.dto.request.GioHAngChiTietRequestDTO;
import com.example.demo.dto.request.GioHangRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
public class DonHangOnlineServiceImpl implements DonHangOnlineService {
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    PhuongThucThanhToanRepo phuongThucThanhToanRepo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    khachhangRePo khachhangRePo;

    @Autowired
    GioHangRepo gioHangRepo;
    @Autowired
    GioHangchiTietRepo gioHangchiTietRepo;

    @Override
    public List<SanPhamChiTiet> getAllProducts() {
        List<SanPhamChiTiet> listSanPham = sanPhamChiTietRepo.findAll();
//
        return listSanPham;
    }

    @Override
    public SanPhamChiTiet getProductsByID(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(id).get();
        return sanPhamChiTiet;
    }

    @Override
    public DonHang createOrder(DonHangOnlineRequestDTO donHangOnlineRequestDTO) {
        DonHang donHang = new DonHang();
        donHang.setMaDonHang(donHangOnlineRequestDTO.getMaDonHang());
        donHang.setTenKhachNhan(donHangOnlineRequestDTO.getTenKhachHang());
        donHang.setSoDienThoaiKhachNhan(donHangOnlineRequestDTO.getSoDienThoaiKhachHang());
        donHang.setDiaChiNhan(donHangOnlineRequestDTO.getDiaChiKhachHang());
        //donHang.setEmailChiNhan(donHangOnlineRequestDTO.getEmailKhachHang());
        donHang.setTongTien(donHangOnlineRequestDTO.getTongTien());
        donHang.setTongTienKhuyenMai(donHangOnlineRequestDTO.getTongTienKhuyenMai());
        donHang.setTongTienSauKhuyenMai(donHangOnlineRequestDTO.getTongTienSauKhuyenMai());
        donHang.setGhiChu(donHangOnlineRequestDTO.getGhiChu());
        donHang.setTrangThaiThanhToan(donHangOnlineRequestDTO.getTrangThaiThanhToan());
        donHang.setLoaiDonHang(2);
        donHang.setPhuongThucNhan(2);

        TrangThai trangThai = trangThaiRepo.findById(donHangOnlineRequestDTO.getIdTrangThai()).get();
        donHang.setTrangThai(trangThai);
        PhuongThucThanhToan phuongThucThanhToan= phuongThucThanhToanRepo.findById(donHangOnlineRequestDTO.getIdPhuongThucThanhToan()).get();
        donHang.setPhuongThucThanhToan(phuongThucThanhToan);
        KhuyenMai khuyenMai = khuyenMaiRepo.findById(donHangOnlineRequestDTO.getIdKhuyenMai()).get();
        donHang.setKhuyenMai(khuyenMai);

        //ngày
        LocalDate localDate = LocalDate.now();
        donHang.setCreateDate(localDate);
        donHang.setCreateBy(donHangOnlineRequestDTO.getTenKhachHang());

        donHangRepo.save(donHang);

        //tạo đơn hàng chi tiết
        DonHang donHang1 = donHangRepo.findByMaDonHang(donHangOnlineRequestDTO.getMaDonHang());
        List<DonHangChiTietRequestDTO> donHangChiTietList = donHangOnlineRequestDTO.getOrderDetail();
        for (DonHangChiTietRequestDTO dto : donHangChiTietList) {
            DonHangChiTiet donHangChiTiet = new DonHangChiTiet();

            donHangChiTiet.setMaDonHangChiTiet(generateRandomString(8));
            donHangChiTiet.setSoLuong(dto.getSoLuong());
            donHangChiTiet.setDonGia(dto.getGiaBan());
            donHangChiTiet.setDonHang(donHang1);

            // Giả sử bạn có phương thức để tìm SanPhamChiTiet từ id
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(dto.getIdSanPhamChiTiet()).get();
            donHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);

            donHangChiTietRepo.save(donHangChiTiet);
        }
        return donHang;
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

    @Override
    public khachhang getUserLogin(String username) {
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if (oldTaiKoan == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }

        khachhang kh = khachhangRePo.findByUsername(username);
        return kh;
    }

    @Override
    public GioHang createCart(GioHangRequestDTO gioHangRequestDTO) {
        GioHang gioHang = new GioHang();
        gioHang.setMaGioHang(gioHangRequestDTO.getMaGioHang());
        gioHang.setTrangThai(true);
        taikhoan taikhoan= taikhoanRepo.findByUsername(gioHangRequestDTO.getUserName().getTaikhoan().getUsername());
        gioHang.setTaiKhoan(taikhoan);
        gioHangRepo.save(gioHang);
        return gioHang;
    }

    @Override
    public GioHangChiTiet createDetailCart(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        GioHang gioHang = gioHangRepo.findById(gioHAngChiTietRequestDTO.getIdGioHang()).get();

        gioHangChiTiet.setGioHang(gioHang);
        gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        gioHangChiTiet.setMaGioHangChiTiet(gioHAngChiTietRequestDTO.getMaGioHangChiTiet());
        gioHangChiTiet.setSoLuong(1);
        gioHangChiTiet.setDonGia(sanPhamChiTiet.getGiaBan());
        gioHangChiTiet.setTrangThai(true);

        gioHangchiTietRepo.save(gioHangChiTiet);
        return gioHangChiTiet;
    }

    @Override
    public  GioHang getCart(String username) {
        GioHang gioHang = gioHangRepo.findByUsername(username);
        return gioHang;
    }

    @Override
    public List<GioHangChiTiet> getDetailCart(Integer idGioHang) {
        List<GioHangChiTiet> gioHangChiTiet = gioHangchiTietRepo.findByGioHangId(idGioHang);
        return gioHangChiTiet;
    }

    @Override
    public GioHangChiTiet updateCartDetail(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        //DonHangChiTiet oldDonHangCT = donHangChiTietRepo.findBySanPhamID(chitietRequestDTO.getIdSanPhamChiTiet(),chitietRequestDTO.getIdĐonHang());
        GioHangChiTiet oldGioHAngChiTiet = gioHangchiTietRepo.findBySanPhamIdAndGioHangId(
                gioHAngChiTietRequestDTO.getIdSanPhamChiTiet(), gioHAngChiTietRequestDTO.getIdGioHang());
        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldGioHAngChiTiet.setIdGioHangChiTiet(oldGioHAngChiTiet.getIdGioHangChiTiet());
        Integer soLuong = oldGioHAngChiTiet.getSoLuong() + gioHAngChiTietRequestDTO.getSoLuong();
        oldGioHAngChiTiet.setSoLuong(soLuong);
        gioHangchiTietRepo.save(oldGioHAngChiTiet);
        return oldGioHAngChiTiet;
    }

    @Override
    public GioHangChiTiet deleteAndReturnBySanPhamChiTietId(Integer idSanPhamChiTiet) {
        GioHangChiTiet gioHangChiTiet = gioHangchiTietRepo.findBySanPhamChiTiet_IdSanPhamChiTiet(idSanPhamChiTiet);
        if (gioHangChiTiet != null) {
            gioHangchiTietRepo.delete(gioHangChiTiet);
        }
        return gioHangChiTiet;
    }

}
