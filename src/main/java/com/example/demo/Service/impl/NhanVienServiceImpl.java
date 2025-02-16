package com.example.demo.Service.impl;


import com.example.demo.Service.NhanVienService;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.nhanvien;
import com.example.demo.entity.taikhoan;
import com.example.demo.repo.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        // Lấy nhân viên hiện tại từ cơ sở dữ liệu
        Optional<nhanvien> optionalNhanVien = nhanVienRepository.findById(nhanVienRequestDTO.getIdNhanVien());
        if (!optionalNhanVien.isPresent()) {
            throw new RuntimeException("Nhân viên không tồn tại!");
        }

        nhanvien nhanVien = optionalNhanVien.get();

        // Kiểm tra nếu email thay đổi thì kiểm tra tính duy nhất
        String emailMoi = nhanVienRequestDTO.getEmail();
        String emailCu = nhanVien.getTaikhoan() != null ? nhanVien.getTaikhoan().getEmail() : "";

        // Chỉ kiểm tra email nếu email thay đổi và không phải là email của chính nhân viên đang cập nhật
        if (!emailMoi.equals(emailCu) && checkEmailExists(emailMoi)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại trong hệ thống.");
        }

        // Validate số điện thoại
        String soDienThoai = nhanVienRequestDTO.getSoDienThoai();
        if (soDienThoai == null || soDienThoai.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại không được để trống.");
        }
        if (!soDienThoai.matches("^0\\d{9}$")) {  // Kiểm tra số điện thoại bắt đầu bằng 0 và có 10 chữ số
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số.");
        }

        // Validate số căn cước công dân
        String soCanCuocCongDan = nhanVienRequestDTO.getSoCanCuocCongDan();
        if (soCanCuocCongDan == null || soCanCuocCongDan.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số căn cước công dân không được để trống.");
        }
        if (!soCanCuocCongDan.matches("^[0-9]{13}$")) {  // Kiểm tra số căn cước công dân có 13 chữ số
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số căn cước công dân phải có 13 số.");
        }

        // Cập nhật thông tin nhân viên
        nhanVien.setHoTen(nhanVienRequestDTO.getHoTen());
        nhanVien.setSoDienThoai(nhanVienRequestDTO.getSoDienThoai());
        nhanVien.setNgaySinh(nhanVienRequestDTO.getNgaySinh());
        nhanVien.setSoCanCuocCongDan(nhanVienRequestDTO.getSoCanCuocCongDan());
        nhanVien.setDiaChi(nhanVienRequestDTO.getDiaChi());
        nhanVien.setGioiTinh(nhanVienRequestDTO.getGioiTinh());

        // Cập nhật email trong bảng Tài khoản
        if (nhanVien.getTaikhoan() != null) {
            nhanVien.getTaikhoan().setEmail(nhanVienRequestDTO.getEmail());
        }

        // Cập nhật ngày cập nhật
        nhanVien.setUpdateDate(LocalDateTime.now());

        // Lưu thông tin cập nhật
        return nhanVienRepository.save(nhanVien);
    }

    public boolean checkEmailExists(String email) {
        // Kiểm tra email đã tồn tại trong hệ thống hay chưa
        taikhoan taiKhoan = taikhoanRepo.findByEmail(email);
        return taiKhoan != null;
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

    public nhanvien findByUsername(String username) {
        return nhanVienRepository.findByTaikhoanUsername(username);
    }



}
