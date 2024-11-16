package com.example.demo.Service.impl;

import com.example.demo.Service.QuanLyDonHangOnlineService;
import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;


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

    @Override
    public HoaDon createInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username) {
        HoaDon hoaDon = new HoaDon();
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            hoaDon.setNhanVien(getNV);

        }

        DonHang donHang = donHangRepo.findById(hoaDonOnlineRequestDTO.getIdDonHang()).get();
        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
        //set khuyến mãi
        hoaDon.setKhuyenMai(donHang.getKhuyenMai());
        //set trang Thai
        hoaDon.setTrangThai(donHang.getTrangThai());
        //set phương thuc thanh toan
        hoaDon.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        //set don hàng
        hoaDon.setDonHang(donHang);
        //set khach hàng
        hoaDon.setKhachHang(donHang.getKhachHang());
        //set ma hd
        hoaDon.setMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        //set ten kn
        hoaDon.setTenKhachNhan(donHang.getTenKhachNhan());
        //set email kn
        hoaDon.setEmailKhachNhan(donHang.getEmailKhachNhan());
        //set sdt kn
        hoaDon.setSoDienThoaiKhachNhan(donHang.getSoDienThoaiKhachNhan());
        //set dia chỉ kn
        hoaDon.setDiaChiNhan(donHang.getDiaChiNhan());
        //tổng tiên
        hoaDon.setTongTien(donHang.getTongTien());
        //tong tiền km
        hoaDon.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        //tong tien sau km
        hoaDon.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        //tong tien phai thanh toan
        hoaDon.setTongTienThanhToan(donHang.getTongTienThanhToan());
        //phi van chuyen
        hoaDon.setPhiVanChuyen(donHang.getPhiVanChuyen());
        //ghi chu
        hoaDon.setGhiChu(donHang.getGhiChu());
        //trang thai thanh toan
        hoaDon.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        //phuong thuc nhan
        hoaDon.setPhuongThucNhan(donHang.getPhuongThucNhan());

        hoaDonRepo.save(hoaDon);


        //tạo háo đơn chi tiết
        HoaDon findHoaDon = hoaDonRepo.findByMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        List<DonHangChiTiet> donHangChiTietLits = donHangChiTietRepo.findByDonHangId(donHang.getIdDonHang());
        for(DonHangChiTiet donHangChiTiet: donHangChiTietLits){

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            hoaDonChiTiet.setMaHoaDonChiTiet(generateRandomString(8));
            hoaDonChiTiet.setHoaDon(findHoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(donHangChiTiet.getSanPhamChiTiet());
            hoaDonChiTiet.setSoLuong(donHangChiTiet.getSoLuong());
            hoaDonChiTiet.setDonGia(donHangChiTiet.getDonGia());
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTiet.setGhiChu(donHangChiTiet.getGhiChu());

            hoaDonChiTietRepo.save(hoaDonChiTiet);
        }


        return hoaDon;
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
