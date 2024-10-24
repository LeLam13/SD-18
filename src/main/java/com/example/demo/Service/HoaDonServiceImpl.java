package com.example.demo.Service;

import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
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
    @Transactional
    public HoaDon createHoaDon(HoaDonResquestDTO hoaDon, String username) {
        //lấy tài khoản
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        //lấy nhân viên
        nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
        //lấy khách hàng
        khachhang khachhang = khachhangRePo.findById(hoaDon.getIdKhachHang()).get();
        //lấy khuyến mãi
        KhuyenMai khuyenMai = khuyenMaiRepo.findById(hoaDon.getIdKhuyenMai()).get();
        //lấy trạng thái
        TrangThai trangThai = trangThaiRepo.findById(hoaDon.getIdTrangThai()).get();
        //lấy phương thức thanh toán
        PhuongThucThanhToan PTTT = phuongThucThanhToanRepo.findById(hoaDon.getIdPhuongThucThanhToan()).get();
        //lấy dơn hàng
        DonHang donHang = donHangRepo.findById(hoaDon.getIdDonHang()).get();

        //cập nhật dơn hàng
        donHang.setTongTien(hoaDon.getTongTien());
        donHang.setTongTienKhuyenMai(hoaDon.getTongTienKhuyenMai());
        donHang.setTongTienSauKhuyenMai(hoaDon.getTongTienSauKhuyenMai());
        donHang.setGhiChu(hoaDon.getGhiChu());
        donHang.setTrangThaiThanhToan(true);
        donHang.setNhanVien(getNV);
        donHang.setKhachHang(khachhang);
        donHang.setTrangThai(trangThai);
        donHang.setPhuongThucThanhToan(PTTT);
        donHang.setKhuyenMai(khuyenMai);
        donHangRepo.save(donHang);

        //tạo hoá đơn
        HoaDon newHoaDon = new HoaDon();
//
//        newHoaDon.setKhuyenMai(khuyenMai);
//        newHoaDon.setTrangThai(trangThai);
//        newHoaDon.setPhuongThucThanhToan(PTTT);
//        newHoaDon.setDonHang(donHang);
//        newHoaDon.setNhanVien(getNV);
//        newHoaDon.setKhachHang(khachhang);
//        newHoaDon.setMaHoaDon(hoaDon.getMaHoaDon());
//        LocalDate localDate = LocalDate.now(); // Hoặc bạn có thể sử dụng LocalDate.of(...)
//        newHoaDon.setCreateDate(localDate);
//        newHoaDon.setCreateBy(oldTaiKoan.getNhanVien().getHoTen());
        newHoaDon.setTongTien(hoaDon.getTongTien());
        newHoaDon.setTongTienKhuyenMai(hoaDon.getTongTienKhuyenMai());
        newHoaDon.setTongTienSauKhuyenMai(hoaDon.getTongTienSauKhuyenMai());
        newHoaDon.setGhiChu(hoaDon.getGhiChu());
        newHoaDon.setTrangThaiThanhToan(true);
        System.out.println("check ;log hoá đơn: "+newHoaDon);
        hoaDonRepo.save(newHoaDon);

        //lấy hoá đơn vừa tạo
        HoaDon hd = hoaDonRepo.findByMaHoaDon(hoaDon.getMaHoaDon());

        //thêm hoá đơn chi tiết
        List<DonHangChiTiet> donHangChiTietList = donHangChiTietRepo.findByDonHangId(hoaDon.getIdDonHang());
        for (DonHangChiTiet donHangChiTiet : donHangChiTietList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            // Chuyển đổi dữ liệu từ DonHangChiTiet sang HoaDonChiTiet
            String maHDCT = generateRandomString(8);
            hoaDonChiTiet.setMaHoaDonChiTiet(maHDCT);
            hoaDonChiTiet.setSoLuong(donHangChiTiet.getSoLuong());
            hoaDonChiTiet.setDonGia(donHangChiTiet.getDonGia());
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTiet.setGhiChu(donHangChiTiet.getGhiChu());
            hoaDonChiTiet.setHoaDon(hd);
            hoaDonChiTiet.setSanPhamChiTiet(donHangChiTiet.getSanPhamChiTiet()); // giả sử bạn đã có phương thức lấy SanPhamChiTiet

            // Lưu vào cơ sở dữ liệu
            hoaDonChiTietRepo.save(hoaDonChiTiet);
        }

        return null;
    }


}
