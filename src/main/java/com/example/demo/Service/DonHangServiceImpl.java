package com.example.demo.Service;

import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangServiceImpl implements DonHangService{
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;

    @Override
    public List<SanPhamChiTiet> getAllProducts() {
        return sanPhamChiTietRepo.findAll();
    }

    @Override
    public DonHang createDonHAng(DonHangRequestDTO donHangDTO, String username) {
        DonHang newDonHang = new DonHang();
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());

            newDonHang.setMaDonHang(donHangDTO.getMaDonHang());
            newDonHang.setTrangThaiThanhToan(donHangDTO.getTrangThaiThanhToan());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            newDonHang.setNhanVien(getNV);
            TrangThai trangThai = trangThaiRepo.findById(donHangDTO.getIdTrangThai()).get();
            newDonHang.setTrangThai(trangThai);
        }
        return donHangRepo.save(newDonHang);
    }

    @Override
    public List<DonHangTongSoLuongResponseDTO> getTongSoLuongDonHang() {
        return donHangRepo.findTongSoLuongDonHang();
    }

    @Override
    public List<DonHangChiTiet> getAllProductsOrder(Integer id) {
        return donHangChiTietRepo.findByDonHangId(id);
    }

    @Override
    public DonHangChiTiet updateDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO) {
        DonHangChiTiet oldDonHangCT = donHangChiTietRepo.findBySanPhamID(chitietRequestDTO.getIdSanPhamChiTiet());
        System.out.println("oldDonHangCT: "+oldDonHangCT);
        oldDonHangCT.setIdDonHangChiTiet(oldDonHangCT.getIdDonHangChiTiet());
        Integer soLuong = oldDonHangCT.getSoLuong() + chitietRequestDTO.getSoLuong();
        oldDonHangCT.setSoLuong(soLuong);
        return donHangChiTietRepo.save(oldDonHangCT);
    }

    @Override
    public DonHangChiTiet createDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO) {
//        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(chitietRequestDTO.getIdSanPhamChiTiet()).get();
//        DonHangChiTiet newDonHangCT = new DonHangChiTiet();
//
//        newDonHangCT.setMaDonHangChiTiet(chitietRequestDTO.getMaDonHangChiTiet());
//        newDonHangCT.setSoLuong(chitietRequestDTO.getSoLuong());
//        newDonHangCT.setDonGia(oldSacPhamCT.getDonGia());
//
//        DonHang oldDonHang = donHangRepo.findById(chitietRequestDTO.getIdĐonHang()).get();
//        newDonHangCT.setDonHang(oldDonHang);
        return null;
    }
}