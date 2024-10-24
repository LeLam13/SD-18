package com.example.demo.Service.impl;


import com.example.demo.Service.NhanVienService;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.nhanvien;
import com.example.demo.repo.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private com.example.demo.repo.taikhoanRepo taikhoanRepo;

    @Override
    public Page<nhanvien> getAll(Pageable pageable) {
        return nhanVienRepository.findAll(pageable);
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
            if (nhanVienRequestDTO.getUsername() != null) {
                nhanVien.getTaikhoan().setUsername(nhanVienRequestDTO.getUsername());
                System.out.println("Cập nhật username: " + nhanVienRequestDTO.getUsername());
            }
            if (nhanVienRequestDTO.getEmail() != null && !nhanVienRequestDTO.getEmail().isEmpty()) {
                nhanVien.getTaikhoan().setEmail(nhanVienRequestDTO.getEmail());
                System.out.println("Cập nhật email: " + nhanVienRequestDTO.getEmail());
            }

            // Lưu tài khoản đã cập nhật
            taikhoanRepo.save(nhanVien.getTaikhoan());
            System.out.println("Lưu tài khoản đã cập nhật thành công");
        }
        // Cập nhật ngày cập nhật
        LocalDateTime date = LocalDateTime.now();
        nhanVien.setUpdateDate(date);

        // Lưu lại thay đổi cho nhân viên
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
