package com.example.demo;
import com.example.demo.Service.impl.EmailService;
import com.example.demo.dto.request.UserSignupRequestDTO;
import com.example.demo.entity.khachhang;
import com.example.demo.entity.taikhoan;
import com.example.demo.entity.vaitro;
import com.example.demo.repo.*;
import com.example.demo.repo.taikhoanRepo;
import com.example.demo.repo.vaitroRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

    private UserSignupRequestDTO dto;
    @Autowired
    private EmailService emailService;

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }
    @Autowired
    private taikhoanRepo taikhoanRepo;

    @Autowired
    khachhangRePo khachHangRepo;

    @Autowired
    private vaitroRepo vaitroRepo;


    BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

    @PostMapping("/signup/customer")
    public String signupCustomer(@ModelAttribute UserSignupRequestDTO dto, Model model) {
        this.dto = dto;
        // Kiểm tra tính hợp lệ
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            model.addAttribute("errorMessage", "Username không được để trống.");
            return "signup"; // Quay lại trang đăng ký với thông báo lỗi
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            model.addAttribute("errorMessage", "Password không được để trống.");
            return "signup"; // Quay lại trang đăng ký với thông báo lỗi
        }

        // Kiểm tra tên đăng nhập đã tồn tại
        if (taikhoanRepo.existsByUsername(dto.getUsername())) {
            model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            return "signup"; // Quay lại trang đăng ký với thông báo lỗi
        }

        // Kiểm tra email đã tồn tại
        if (taikhoanRepo.existsByEmail(dto.getEmail())) {
            model.addAttribute("errorMessage", "Email đã tồn tại. Vui lòng chọn email khác.");
            return "signup"; // Quay lại trang đăng ký với thông báo lỗi
        }

        // Tạo tài khoản mới
        taikhoan taiKhoan = new taikhoan();
        taiKhoan.setUsername(dto.getUsername());
        taiKhoan.setPassword(pe.encode(dto.getPassword())); // Mã hóa mật khẩu
        taiKhoan.setEmail(dto.getEmail());
        taiKhoan.setTrangthai(true); // Thiết lập trạng thái tài khoản mặc định là true

        // Tìm vai trò CUSTOMER từ cơ sở dữ liệu
        vaitro customerVaiTro = vaitroRepo.findById("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        taiKhoan.setVaiTro(customerVaiTro); // Gán vai trò CUSTOMER cho người dùng

        // Lưu tài khoản vào cơ sở dữ liệu
        taikhoanRepo.save(taiKhoan);

        // Tạo khách hàng mới và lưu vào cơ sở dữ liệu
        khachhang khachHang = new khachhang();

        // Tạo mã khách hàng
        String maKhachHang = "KH" + taiKhoan.getUsername().toUpperCase(); // Tạo mã khách hàng từ tên đăng nhập
        khachHang.setMaKhachHang(maKhachHang); // Gán mã khách hàng

        // Thiết lập các thuộc tính khác
        khachHang.setHoTen(dto.getHoTen());

        //khachHang.setSoDienThoai(Integer.parseInt(dto.getSoDienThoai()));


        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setDiaChi(dto.getDiaChi());
        khachHang.setGioiTinh(dto.isGioiTinh());
        khachHang.setTaikhoan(taiKhoan); // Gán tài khoản cho khách hàng

        // Lưu khách hàng vào cơ sở dữ liệu
        khachHangRepo.save(khachHang);

        // Tạo thông báo thành công
        model.addAttribute("successMessage", "Đăng ký thành công!"); // Thêm thông báo thành công

        // Quay lại trang đăng ký với thông báo thành công
        return "signup"; // Trả về trang đăng ký
    }


    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "/Quenmk"; // Tạo trang để nhập email
    }

    // Xử lý khi người dùng gửi yêu cầu quên mật khẩu
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        // Kiểm tra email có tồn tại trong cơ sở dữ liệu không
        if (taikhoanRepo.existsByEmail(email)) {
            // Tìm tài khoản theo email
            taikhoan taikhoan = taikhoanRepo.findByEmail(email);

            // Tạo mật khẩu mới ngẫu nhiên
            String newPassword = RandomStringUtils.randomAlphanumeric(8); // Mật khẩu 8 ký tự ngẫu nhiên
            taikhoan.setPassword(pe.encode(newPassword)); // Mã hóa mật khẩu mới

            // Cập nhật mật khẩu trong cơ sở dữ liệu
            taikhoanRepo.save(taikhoan);

            // Gửi email với mật khẩu mới
            String subject = "Mật khẩu mới của bạn";
            String body = "Mật khẩu mới của bạn là: " + newPassword + "\nHãy đăng nhập và đổi lại mật khẩu ngay lập tức.";
            emailService.sendEmail(email, subject, body); // Gọi phương thức sendEmail từ EmailService

            // Chuyển hướng sau khi gửi email thành công
            model.addAttribute("successMessage", "Mật khẩu mới đã được gửi đến email của bạn.");
            return "/Quenmk"; // Trả về trang thông báo thành công
        } else {
            // Nếu không tìm thấy người dùng với email đã nhập
            model.addAttribute("errorMessage", "Email không tồn tại trong hệ thống.");
            return "/Quenmk"; // Quay lại trang quên mật khẩu với thông báo lỗi
        }
    }
    @GetMapping("/access-denied")
    public String loi403() {
        return "/access-denied";
    }
}