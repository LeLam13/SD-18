package com.example.demo.controller.user;

import com.example.demo.entity.taikhoan;
import com.example.demo.repo.taikhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DoiMatKhauController {

    @Autowired
    private taikhoanRepo taikhoanRepo; // Tiêm repository để tương tác với cơ sở dữ liệu

    private final BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // Khởi tạo bộ mã hóa

    @GetMapping("/user/doimatkhau")
    public String viewFormDoiMatKhau(Model model) {
        return "user/resetPassword"; // Trả về trang đổi mật khẩu
    }

    @PostMapping("user/doimatkhau2")
    public String doiMatKhau(@RequestParam("currentPassword") String currentPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("verifyNewPassword") String verifyNewPassword,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy tên người dùng từ thông tin xác thực

        // Tìm người dùng theo tên đăng nhập
        taikhoan taiKhoan = taikhoanRepo.findByUsername(username);

        // Kiểm tra mật khẩu hiện tại
        if (taiKhoan != null && pe.matches(currentPassword, taiKhoan.getPassword())) {
            // Kiểm tra mật khẩu mới và xác nhận
            if (newPassword.equals(verifyNewPassword)) {
                // Mã hóa và cập nhật mật khẩu mới
                taiKhoan.setPassword(pe.encode(newPassword));
                taikhoanRepo.save(taiKhoan); // Lưu thay đổi vào cơ sở dữ liệu

                model.addAttribute("successMessage", "Đổi mật khẩu thành công!"); // Thông báo thành công
            } else {
                model.addAttribute("errorMessage", "Mật khẩu mới và xác nhận không khớp."); // Thông báo lỗi
            }
        } else {
            model.addAttribute("errorMessage", "Mật khẩu hiện tại không chính xác."); // Thông báo lỗi
        }

        return "/user/resetPassword"; // Trả về trang đổi mật khẩu
    }
}
