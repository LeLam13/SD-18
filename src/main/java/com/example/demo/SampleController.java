package com.example.demo;
import com.example.demo.dto.reponse.UserSignupResponseDTO;
import com.example.demo.dto.request.UserSignupRequestDTO;
import com.example.demo.entity.khachhang;
import com.example.demo.entity.taikhoan;
import com.example.demo.entity.vaitro;
import com.example.demo.repo.khachhangRePo;
import com.example.demo.repo.taikhoanRepo;
import com.example.demo.repo.vaitroRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

    private UserSignupRequestDTO dto;

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }
    @Autowired
    private taikhoanRepo taikhoanRepo;

    @Autowired
    private khachhangRePo khachhangRePo;

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
        khachHang.setSoDienThoai(Integer.parseInt(dto.getSoDienThoai()));
        khachHang.setDiaChi(dto.getDiaChi());
        khachHang.setGioiTinh(dto.isGioiTinh());
        khachHang.setTaikhoan(taiKhoan); // Gán tài khoản cho khách hàng

        // Lưu khách hàng vào cơ sở dữ liệu
        khachhangRePo.save(khachHang);

        // Tạo thông báo thành công
        model.addAttribute("successMessage", "Đăng ký thành công!"); // Thêm thông báo thành công

        // Quay lại trang đăng ký với thông báo thành công
        return "signup"; // Trả về trang đăng ký
    }

}