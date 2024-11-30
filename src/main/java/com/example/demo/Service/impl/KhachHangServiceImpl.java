package com.example.demo.Service.impl;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.DonHangChiTietResponseDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.reponse.LichSuMuaHangResponseDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.khachhang;
import com.example.demo.entity.taikhoan;
import com.example.demo.entity.vaitro;
import com.example.demo.repo.HoaDonChiTietRepo;
import com.example.demo.repo.HoaDonRepo;
import com.example.demo.repo.khachhangRePo;
import com.example.demo.repo.taikhoanRepo;
import com.example.demo.repo.vaitroRepo;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    private khachhangRePo khachHangRepo;

    @Autowired
    private HoaDonRepo hoaDonRepo;

    @Autowired
    private taikhoanRepo taikhoanRepo;

    @Autowired
    private HoaDonChiTietRepo hoaDonChiTietRepo;

    @Autowired
    private vaitroRepo vaitroRepo;

    @Autowired
    private EmailService emailService;

    BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

    @Override
    public List<khachhang> findAll() {
        return khachHangRepo.findAll();
    }

    @Override
    public List<KhachHangResponseDTO> getAllKhachHang() {
        List<khachhang> khachhangList = khachHangRepo.findAll();
        return khachhangList.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public KhachHangResponseDTO addKhachHang(KhachHangRequestDTO khachHangRequestDTO) {
        khachhang newKhachHang = convertToEntity(khachHangRequestDTO);
        khachhang savedKhachHang = khachHangRepo.save(newKhachHang);
        return convertToResponseDTO(savedKhachHang);
    }

    @Override
    public Page<KhachHangResponseDTO> getAllKhachHangPaged(String searchQuery, Pageable pageable) {
        Page<khachhang> khachHangPage;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Nếu có từ khóa tìm kiếm, gọi phương thức tìm kiếm tùy chỉnh
            khachHangPage = khachHangRepo.findBySearchQuery(searchQuery, pageable);
        } else {
            // Nếu không có từ khóa, trả về toàn bộ danh sách
            khachHangPage = khachHangRepo.findAll(pageable);
        }

        // Áp dụng map để chuyển đổi từ Entity sang DTO
        return khachHangPage.map(this::convertToResponseDTO);
    }

    @Override
    public KhachHangResponseDTO updateKhachHang(Long id, KhachHangRequestDTO khachHangRequestDTO) {
        // Fetch the existing customer record
        khachhang existingKhachHang = khachHangRepo.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + id + " not found"));

        // Update the fields
        existingKhachHang.setHoTen(khachHangRequestDTO.getHoTen());
        existingKhachHang.setNgaySinh(parseNgaySinh(khachHangRequestDTO.getNgaySinh()));
        existingKhachHang.setSoDienThoai(khachHangRequestDTO.getSoDienThoai());
        existingKhachHang.setGioiTinh(khachHangRequestDTO.getGioiTinh());
        existingKhachHang.setDiaChi(khachHangRequestDTO.getDiaChi());
        existingKhachHang.setEmail(khachHangRequestDTO.getEmail()); // Update email

        // Save the updated entity
        khachhang updatedKhachHang = khachHangRepo.save(existingKhachHang);
        return convertToResponseDTO(updatedKhachHang);
    }

    // Helper method to parse ngaySinh from String to LocalDate
    private LocalDate parseNgaySinh(String ngaySinh) {
        try {
            return LocalDate.parse(ngaySinh);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format for 'ngaySinh': " + ngaySinh);
        }
    }

    // Helper method to convert KhachHangRequestDTO to khachhang entity
    private khachhang convertToEntity(KhachHangRequestDTO dto) {
        khachhang entity = new khachhang();
        entity.setMaKhachHang(dto.getMaKhachHang());
        entity.setHoTen(dto.getHoTen());
        entity.setNgaySinh(parseNgaySinh(dto.getNgaySinh()));
        entity.setSoDienThoai(dto.getSoDienThoai());
        entity.setGioiTinh(dto.getGioiTinh());
        entity.setDiaChi(dto.getDiaChi());
        entity.setEmail(dto.getEmail()); // Set email

        // Kiểm tra và tạo tài khoản liên kết
        if (dto.getUsernameTaiKhoan() != null && !dto.getUsernameTaiKhoan().isEmpty()) {
            if (taikhoanRepo.existsByUsername(dto.getUsernameTaiKhoan())) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
            }
            if (taikhoanRepo.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }

            // Tạo tài khoản mới
            taikhoan newAccount = new taikhoan();
            String rawPassword = RandomStringUtils.randomAlphanumeric(8);
            newAccount.setUsername(dto.getUsernameTaiKhoan());
            newAccount.setPassword(pe.encode(rawPassword));
            newAccount.setEmail(dto.getEmail());
            newAccount.setTrangthai(true);

            vaitro role = vaitroRepo.findById("USER")
                    .orElseGet(() -> {
                        vaitro newRole = new vaitro();
                        newRole.setMa("USER");
                        newRole.setTen("Người dùng");
                        return vaitroRepo.save(newRole);
                    });
            newAccount.setVaiTro(role);
            taikhoanRepo.save(newAccount);

            entity.setTaikhoan(newAccount);

            // Gửi email thông báo
            String subject = "Thông tin tài khoản khách hàng của bạn";
            String body = "Chào " + dto.getHoTen() + ",\n\n"
                    + "Tài khoản của bạn đã được tạo thành công.\n"
                    + "Tên đăng nhập: " + dto.getUsernameTaiKhoan() + "\n"
                    + "Mật khẩu: " + rawPassword + "\n\n"
                    + "Vui lòng thay đổi mật khẩu sau khi đăng nhập lần đầu tiên.\n\n"
                    + "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!";
            emailService.sendEmail(dto.getEmail(), subject, body);

        } else {
            entity.setTaikhoan(null); // Nếu không có username thì để null
        }

        return entity;
    }

    @Override
    public List<LichSuMuaHangResponseDTO> getLichSuMuaHang(Integer idKhachHang) {
        List<HoaDon> hoaDonList = hoaDonRepo.findByKhachHang_IdKhachHang(idKhachHang);
        List<LichSuMuaHangResponseDTO> responseList = new ArrayList<>();

        for (HoaDon hoaDon : hoaDonList) {
            LichSuMuaHangResponseDTO dto = new LichSuMuaHangResponseDTO();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setNgayMua(hoaDon.getCreateDate());
            dto.setTongTien(hoaDon.getTongTien());

            List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepo.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
            List<DonHangChiTietResponseDTO> chiTietDTOList = new ArrayList<>();

            for (HoaDonChiTiet chiTiet : chiTietList) {
                DonHangChiTietResponseDTO chiTietDTO = new DonHangChiTietResponseDTO();
                chiTietDTO.setIdDonHangChiTiet(chiTiet.getIdHoaDonChiTiet());
                chiTietDTO.setMaDonHangChiTiet(chiTiet.getMaHoaDonChiTiet());
                chiTietDTO.setSoLuong(chiTiet.getSoLuong());
                chiTietDTO.setGiaBan(chiTiet.getDonGia());
                chiTietDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getIdSanPham().getTen());
                chiTietDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
                chiTietDTOList.add(chiTietDTO);
            }

            dto.setChiTietSanPham(chiTietDTOList);
            responseList.add(dto);
        }

        return responseList;
    }

    // Helper method to convert khachhang entity to KhachHangResponseDTO
    private KhachHangResponseDTO convertToResponseDTO(khachhang entity) {
        KhachHangResponseDTO responseDTO = new KhachHangResponseDTO();
        responseDTO.setId_khach_hang(entity.getIdKhachHang());
        responseDTO.setMaKhachHang(entity.getMaKhachHang());
        responseDTO.setHoTen(entity.getHoTen());
        responseDTO.setNgaySinh(entity.getNgaySinh().toString());
        responseDTO.setSoDienThoai(entity.getSoDienThoai());
        responseDTO.setGioiTinh(entity.isGioiTinh());
        responseDTO.setDiaChi(entity.getDiaChi());
        responseDTO.setEmail(entity.getEmail()); // Add email to response
        responseDTO.setUsernameTaiKhoan(
                entity.getTaikhoan() != null ? entity.getTaikhoan().getUsername() : null);
        return responseDTO;
    }
}
