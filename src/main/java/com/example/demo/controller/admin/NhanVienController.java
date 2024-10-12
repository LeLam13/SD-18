package com.example.demo.controller.admin;

import com.example.demo.Service.EmailService;
import com.example.demo.dto.EmployeeSignupDTO;
import com.example.demo.entity.nhanvien;
import com.example.demo.entity.taikhoan;
import com.example.demo.entity.vaitro;
import com.example.demo.repo.NhanVienRepository;
import com.example.demo.repo.taikhoanRepo;
import com.example.demo.repo.vaitroRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${admin.domain}/nhan-vien")
public class NhanVienController {


    @Autowired
    com.example.demo.repo.taikhoanRepo taikhoanRepo;

    @Autowired
    com.example.demo.repo.vaitroRepo vaitroRepo;
    @Autowired
    com.example.demo.repo.NhanVienRepository NhanVienRepository;

    @Autowired
    EmailService emailService; // Thêm Autowired cho EmailService

    BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // Khởi tạo BCryptPasswordEncoder

    @GetMapping("")
    public String getNhanVienView(){
        return "admin/NhanVien";
    }
    @GetMapping("thong-tin-ca-nhan")
    public String getUserDetail(){
        return "admin/thongTinUser";
    }


    @PostMapping("/addEmployee")
    public String addEmployee(EmployeeSignupDTO dto) {
        // Tạo một mật khẩu ngẫu nhiên
        String rawPassword = RandomStringUtils.randomAlphanumeric(8); // Tạo mật khẩu 8 ký tự ngẫu nhiên

        // Tạo một tài khoản mới cho nhân viên
        taikhoan newAccount = new taikhoan();
        newAccount.setUsername(dto.getUsername());
        newAccount.setPassword(pe.encode(rawPassword));  // Mã hóa mật khẩu
        newAccount.setEmail(dto.getEmail());
        newAccount.setTrangthai(true);
        // Thiết lập vai trò mặc định là STAFF
        vaitro staffRole = vaitroRepo.findById("STAFF")  // Đảm bảo vai trò STAFF tồn tại
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò STAFF"));
        newAccount.setVaiTro(staffRole);  // Gán vai trò STAFF cho tài khoản mới

        taikhoanRepo.save(newAccount);  // Lưu tài khoản trước

        // Sau đó tạo và lưu thông tin nhân viên
        nhanvien newEmployee = new nhanvien();
        newEmployee.setMaNhanVien(dto.getMaNhanVien());
        newEmployee.setHoTen(dto.getHoTen());
        newEmployee.setSoDienThoai(dto.getSoDienThoai());
        newEmployee.setNgaySinh(dto.getNgaySinh());
        newEmployee.setSoCanCuocCongDan(dto.getSoCanCuocCongDan());
        newEmployee.setDiaChi(dto.getDiaChi());
        newEmployee.setGioiTinh(dto.getGioiTinh());
        newEmployee.setTrangThai(true);  // Mặc định trạng thái là active

        // Liên kết tài khoản với nhân viên
        newEmployee.setTaikhoan(newAccount);
        NhanVienRepository.save(newEmployee);  // Lưu thông tin nhân viên

        // Gửi email với mật khẩu
        String subject = "Thông tin tài khoản của bạn";
        String body = "Chào " + dto.getHoTen() + ",\n\n"
                + "Tài khoản của bạn đã được tạo thành công.\n"
                + "Tên đăng nhập: " + dto.getUsername() + "\n"
                + "Mật khẩu: " + rawPassword + "\n\n"  // Sử dụng mật khẩu ngẫu nhiên
                + "Vui lòng thay đổi mật khẩu sau khi đăng nhập lần đầu tiên.\n\n"
                + "Cảm ơn bạn!";
        emailService.sendEmail(dto.getEmail(), subject, body);  // Gửi email

        return "redirect:/admin/nhan-vien";  // Chuyển hướng tới trang đăng nhập
    }


}
