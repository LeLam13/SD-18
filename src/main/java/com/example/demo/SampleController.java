package com.example.demosecurity.Controller;

import com.example.demosecurity.Service.EmailService;
import com.example.demosecurity.dto.EmployeeSignupDTO;
import com.example.demosecurity.dto.UserSignupDTO;
import com.example.demosecurity.entity.nhanvien;
import com.example.demosecurity.entity.taikhoan;
import com.example.demosecurity.entity.vaitro;
import com.example.demosecurity.repo.NhanVienRepository;
import com.example.demosecurity.repo.vaitroRepo;
import com.example.demosecurity.repo.taikhoanRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

    @Autowired
    taikhoanRepo taikhoanRepo;

    @Autowired
    vaitroRepo vaitroRepo;
    @Autowired
    NhanVienRepository NhanVienRepository;

    @Autowired
    EmailService emailService; // Thêm Autowired cho EmailService

    BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // Khởi tạo BCryptPasswordEncoder

    @GetMapping("admin/home")
    public String adminHome() {
        return "views/admin/index";
    }
    @GetMapping("staff/home")
    public String staffHome() {
        return "views/staff/index";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "views/user/index";
    }



    @GetMapping("/guest/home")
    public String guestHome() {
        return "views/guest/index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req, Model model) {
        if (req.getParameter("error") != null) {
            model.addAttribute("error", "Invalid username/password");
        }
        if (req.getParameter("logout") != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "views/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "views/signup";
    }

    @PostMapping("/signup")
    public String signup(UserSignupDTO dto) {
        // TODO: validate
        taikhoan taikhoan = new taikhoan();
        taikhoan.setUsername(dto.getUsername());
        taikhoan.setPassword(pe.encode(dto.getPassword()));
        taikhoan.setEmail(dto.getEmail());

        // Tìm vai trò USER từ cơ sở dữ liệu
        vaitro userVaitro = vaitroRepo.findById("USER") // Giả sử "USER" là ID của vai trò
                .orElseThrow(() -> new RuntimeException("Role not found"));
        taikhoan.setVaitro(userVaitro); // Gán vai trò USER cho người dùng

        taikhoanRepo.save(taikhoan); // Lưu người dùng vào cơ sở dữ liệu
        return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "views/Quenmk"; // Tạo trang để nhập email
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email) {
        // Tìm người dùng theo email
        taikhoan taikhoan = taikhoanRepo.findByEmail(email);
        if (taikhoan != null) {
            // Tạo mật khẩu mới ngẫu nhiên
            String newPassword = RandomStringUtils.randomAlphanumeric(8);
            taikhoan.setPassword(pe.encode(newPassword)); // Mã hóa mật khẩu mới
            taikhoanRepo.save(taikhoan); // Cập nhật mật khẩu trong cơ sở dữ liệu

            // Gửi email với mật khẩu mới
            String subject = "Mật Khẩu Mới Của Bạn";
            String body = "Mật khẩu mới của bạn là: " + newPassword; // Gửi mật khẩu mới qua email
            emailService.sendEmail(email, subject, body); // Gọi phương thức sendEmail từ emailService
        } else {
            // Xử lý nếu không tìm thấy người dùng
            // Ví dụ: thêm thông báo lỗi vào mô hình
            return "views/forgot-password?error=user_not_found"; // Chuyển hướng lại trang quên mật khẩu với thông báo lỗi
        }
        return "redirect:/change-password"; // Chuyển hướng sau khi gửi email
    }
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "views/doimk";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("username") String username,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        // Tìm người dùng theo tên đăng nhập
        taikhoan taikhoan = taikhoanRepo.findByUsername(username);

        if (taikhoan == null) {
            // Nếu không tìm thấy người dùng
            model.addAttribute("error", "Tên đăng nhập không tồn tại!");
            return "views/doimk";
        }

        // Kiểm tra mật khẩu cũ
        if (!pe.matches(oldPassword, taikhoan.getPassword())) {
            // Nếu mật khẩu cũ không đúng
            model.addAttribute("error", "Mật khẩu cũ không chính xác!");
            return "views/doimk";
        }

        // Kiểm tra mật khẩu mới có rỗng không
        if (newPassword.isEmpty()) {
            model.addAttribute("error", "Mật khẩu mới không được để trống!");
            return "views/doimk";
        }

        // Mã hóa và cập nhật mật khẩu mới
        taikhoan.setPassword(pe.encode(newPassword));
        taikhoanRepo.save(taikhoan);

        // Thêm thông báo thành công vào model
        model.addAttribute("message1", "Mật khẩu đã được thay đổi thành công!");
        return "views/doimk"; // Trả về trang đổi mật khẩu với thông báo thành công
    }
//    @GetMapping("/addEmployee")
//    public String showAddEmployeeForm() {
//        return "views/addEmployee";  // Trả về form để thêm nhân viên
//    }
//
//    @PostMapping("/addEmployee")
//    public String addEmployee(EmployeeSignupDTO dto) {
//        // Tạo một mật khẩu ngẫu nhiên
//        String rawPassword = RandomStringUtils.randomAlphanumeric(8); // Tạo mật khẩu 8 ký tự ngẫu nhiên
//
//        // Tạo một tài khoản mới cho nhân viên
//        taikhoan newAccount = new taikhoan();
//        newAccount.setUsername(dto.getUsername());
//        newAccount.setPassword(pe.encode(rawPassword));  // Mã hóa mật khẩu
//        newAccount.setEmail(dto.getEmail());
//
//        // Thiết lập vai trò mặc định là STAFF
//        vaitro staffRole = vaitroRepo.findById("STAFF")  // Đảm bảo vai trò STAFF tồn tại
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò STAFF"));
//        newAccount.setVaitro(staffRole);  // Gán vai trò STAFF cho tài khoản mới
//
//        taikhoanRepo.save(newAccount);  // Lưu tài khoản trước
//
//        // Sau đó tạo và lưu thông tin nhân viên
//        nhanvien newEmployee = new nhanvien();
//        newEmployee.setMaNhanVien(dto.getMaNhanVien());
//        newEmployee.setHoTen(dto.getHoTen());
//        newEmployee.setSoDienThoai(dto.getSoDienThoai());
//        newEmployee.setNgaySinh(dto.getNgaySinh());
//        newEmployee.setSoCanCuocCongDan(dto.getSoCanCuocCongDan());
//        newEmployee.setDiaChi(dto.getDiaChi());
//        newEmployee.setGioiTinh(dto.getGioiTinh());
//        newEmployee.setTrangThai(true);  // Mặc định trạng thái là active
//
//        // Liên kết tài khoản với nhân viên
//        newEmployee.setTaikhoan(newAccount);
//        NhanVienRepository.save(newEmployee);  // Lưu thông tin nhân viên
//
//        // Gửi email với mật khẩu
//        String subject = "Thông tin tài khoản của bạn";
//        String body = "Chào " + dto.getHoTen() + ",\n\n"
//                + "Tài khoản của bạn đã được tạo thành công.\n"
//                + "Tên đăng nhập: " + dto.getUsername() + "\n"
//                + "Mật khẩu: " + rawPassword + "\n\n"  // Sử dụng mật khẩu ngẫu nhiên
//                + "Vui lòng thay đổi mật khẩu sau khi đăng nhập lần đầu tiên.\n\n"
//                + "Cảm ơn bạn!";
//        emailService.sendEmail(dto.getEmail(), subject, body);  // Gửi email
//
//        return "redirect:/login";  // Chuyển hướng tới trang đăng nhập
//    }

}
