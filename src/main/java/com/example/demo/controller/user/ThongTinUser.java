package com.example.demo.controller.user;

import com.example.demo.entity.khachhang;
import com.example.demo.entity.nhanvien;
import com.example.demo.entity.taikhoan;
import com.example.demo.repo.khachhangRePo;
import com.example.demo.repo.taikhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class ThongTinUser {
    @Autowired
    private khachhangRePo khachHangRepo;
    @Autowired
    private com.example.demo.repo.taikhoanRepo taikhoanRepo;
    @GetMapping("thongtinuser")
    public String trangChu(Model model, Principal principal) {
        // Lấy tên người dùng (username) từ Spring Security
        String username = principal.getName();
        // Tìm thông tin nhân viên từ cơ sở dữ liệu dựa trên username
        khachhang khachhang = khachHangRepo.findByUsername(username);
        // Thêm thông tin nhân viên vào model để hiển thị trên trang Thymeleaf
        model.addAttribute("khachHang", khachhang);
        return "/user/ThongTinKhachHang";
    }

    @PostMapping("/updatekhachhang")
    public String updateUserKhachHang(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Boolean trangthai,
            @ModelAttribute khachhang khachhang,
            Principal principal,
            Model model) {

        String username = principal.getName();

        taikhoan existingTaiKhoan = taikhoanRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại!"));

        // Validate email if provided
        if (email != null && !email.isEmpty()) {
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                model.addAttribute("error", "Email không hợp lệ!");
                model.addAttribute("khachHang", khachhang); // Giữ lại dữ liệu
                return "/user/ThongTinKhachHang";
            }

            boolean emailExists = taikhoanRepo.existsByEmail(email);
            if (emailExists && !email.equals(existingTaiKhoan.getEmail())) {
                model.addAttribute("error", "Email đã tồn tại trên hệ thống!");
                khachhang khachHangFromDB = khachHangRepo.findByTaikhoanUsername(username);
                model.addAttribute("khachHang", khachHangFromDB); // Giữ lại thông tin nhân viên
                return "/user/ThongTinKhachHang";
            }

            existingTaiKhoan.setEmail(email);
        }

        // Validate password if provided
        if (password != null && !password.isEmpty()) {
            if (password.length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                model.addAttribute("khachHang", khachhang);
                return "/user/ThongTinKhachHang";
            }
            existingTaiKhoan.setPassword(password);
        }

        // Validate trangthai if provided
        if (trangthai != null) {
            existingTaiKhoan.setTrangthai(trangthai);
        }

        taikhoanRepo.save(existingTaiKhoan);

        // Validate khachhang fields
        if (khachhang.getHoTen() == null || khachhang.getHoTen().isEmpty()) {
            model.addAttribute("error", "Họ tên không được để trống!");
            model.addAttribute("khachHang", khachhang);
            return "/user/ThongTinKhachHang";
        }

        if (khachhang.getSoDienThoai() == null || khachhang.getSoDienThoai().isEmpty()) {
            model.addAttribute("error", "Số điện thoại không được để trống!");
            model.addAttribute("khachHang", khachhang);
            return "/user/ThongTinKhachHang";
        } else if (!khachhang.getSoDienThoai().matches("^0\\d{9}$")) {
            model.addAttribute("error", "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
            model.addAttribute("khachHang", khachhang);
            return "/user/ThongTinKhachHang";
        }


        if (khachhang.getNgaySinh() == null) {
            model.addAttribute("error", "Ngày sinh không được để trống!");
            model.addAttribute("khachHang", khachhang);
            return "/user/ThongTinKhachHang";
        }

        // Validate that the date of birth is not in the future
        if (khachhang.getNgaySinh().isAfter(LocalDate.now())) {
            model.addAttribute("error", "Ngày sinh không được là ngày trong tương lai!");
            model.addAttribute("khachHang", khachhang);
            return "/user/ThongTinKhachHang";
        }

        // Update customer information
        taikhoanRepo.save(existingTaiKhoan);
        khachhang existingKhachHang = khachHangRepo.findByTaikhoanUsername(username);
        if (existingKhachHang != null) {
            existingKhachHang.setHoTen(khachhang.getHoTen());
            existingKhachHang.setGioiTinh(khachhang.isGioiTinh());
            existingKhachHang.setNgaySinh(khachhang.getNgaySinh());
            existingKhachHang.setSoDienThoai(khachhang.getSoDienThoai());

            khachHangRepo.save(existingKhachHang);
        }

        return "redirect:/thongtinuser";
    }



}
