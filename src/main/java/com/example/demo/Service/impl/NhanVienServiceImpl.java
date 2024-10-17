package com.example.demo.Service.impl;


import com.example.demo.Service.NhanVienService;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.nhanvien;
import com.example.demo.repo.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Override
    public List<nhanvien> getAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public nhanvien updateNhanVien(NhanVienRequetsDTO nhanVienRequestDTO) {
        // Tìm nhân viên theo ID
        Optional<nhanvien> optionalNhanVien = nhanVienRepository.findById(nhanVienRequestDTO.getIdNhanVien());
        if (!optionalNhanVien.isPresent()) {
            throw new RuntimeException("Nhân viên không tồn tại!");
        }

        nhanvien nhanVien = optionalNhanVien.get();
        // Cập nhật thông tin
        nhanVien.setHoTen(nhanVienRequestDTO.getHoTen());
        nhanVien.setSoDienThoai(nhanVienRequestDTO.getSoDienThoai());
        nhanVien.setNgaySinh(nhanVienRequestDTO.getNgaySinh());
        nhanVien.setSoCanCuocCongDan(nhanVienRequestDTO.getSoCanCuocCongDan());
        nhanVien.setDiaChi(nhanVienRequestDTO.getDiaChi());
        nhanVien.setGioiTinh(nhanVienRequestDTO.getGioiTinh());

        // Cập nhật tài khoản nếu có
        if (nhanVien.getTaikhoan() != null) {
            nhanVien.getTaikhoan().setUsername(nhanVienRequestDTO.getUsername());
            nhanVien.getTaikhoan().setEmail(nhanVienRequestDTO.getEmail());
        }

        // Cập nhật ngày cập nhật
        LocalDateTime date = LocalDateTime.now(); // Hoặc lấy từ request DTO nếu cần
        nhanVien.setUpdateDate(date);

        // Lưu lại thay đổi
        return nhanVienRepository.save(nhanVien);
    }



    @Override
    public nhanvien getNhanVien(Integer idNhanVien) {
        return nhanVienRepository.findById(idNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + idNhanVien));
    }

    @Override
    public nhanvien deleteNhanVien(Integer idNhanVien) {
        Optional<nhanvien> nhanVien = nhanVienRepository.findById(idNhanVien);
        if (nhanVien.isPresent()) {
            nhanVienRepository.deleteById(idNhanVien);
            return nhanVien.get();
        } else {
            throw new RuntimeException("Không tìm thấy nhân viên với ID: " + idNhanVien);
        }
    }
}
