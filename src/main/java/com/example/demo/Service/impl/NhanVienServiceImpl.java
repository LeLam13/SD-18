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
        Optional<nhanvien> optionalNhanVien = nhanVienRepository.findById(nhanVienRequestDTO.getIdNhanVien());
        if (!optionalNhanVien.isPresent()) {
            throw new RuntimeException("Nhân viên không tồn tại!");
        }

        nhanvien nhanVien = optionalNhanVien.get();


        // Cập nhật thông tin nhân viên
        nhanVien.setHoTen(nhanVienRequestDTO.getHoTen());
        nhanVien.setSoDienThoai(nhanVienRequestDTO.getSoDienThoai());
        nhanVien.setNgaySinh(nhanVienRequestDTO.getNgaySinh());
        nhanVien.setSoCanCuocCongDan(nhanVienRequestDTO.getSoCanCuocCongDan());
        nhanVien.setDiaChi(nhanVienRequestDTO.getDiaChi());
        nhanVien.setGioiTinh(nhanVienRequestDTO.getGioiTinh());

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
    public nhanvien softDeleteNhanVien(Integer idNhanVien) {
        Optional<nhanvien> nhanVienOpt = nhanVienRepository.findById(idNhanVien);
        if (nhanVienOpt.isPresent()) {
            nhanvien nhanVien = nhanVienOpt.get();
            nhanVien.setTrangThai(false); // Thay đổi trạng thái thành false để xóa mềm
            nhanVien.setDeleteBy("Tên người thực hiện xóa"); // Ghi nhận người thực hiện xóa nếu cần
            return nhanVienRepository.save(nhanVien); // Lưu lại thay đổi
        } else {
            throw new RuntimeException("Không tìm thấy nhân viên với ID: " + idNhanVien);
        }
    }
    @Override
    public Page<nhanvien> getActiveNhanVien(Pageable pageable) {
        return nhanVienRepository.findAllWithTrangThaiOrder(pageable); // Lấy danh sách nhân viên có trạng thái true
    }
    @Override
    public Page<nhanvien> searchNhanVien(String keyword, Pageable pageable) {
        return nhanVienRepository.findByMaNhanVienContainingOrHoTenContainingOrSoDienThoaiContainingOrderByTrangThai(
                keyword, keyword, keyword, pageable);
    }
}
