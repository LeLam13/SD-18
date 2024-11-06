package com.example.demo.controller.admin;

import com.example.demo.Service.impl.EmailService;
import com.example.demo.dto.request.NhanVienRequetsDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("${admin.domain}/nhan-vien")
public class NhanVienController {

    @Autowired
    private taikhoanRepo taikhoanRepo;

    @Autowired
    private vaitroRepo vaitroRepo;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private EmailService emailService; // Thêm Autowired cho EmailService

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
    public String addEmployee(NhanVienRequetsDTO dto, RedirectAttributes redirectAttributes) {
        if (taikhoanRepo.existsByUsername(dto.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại");
            redirectAttributes.addFlashAttribute("dto", dto);  // Lưu dữ liệu đã nhập
            return "redirect:/admin/nhan-vien"; // Quay lại trang danh sách
        }

        // Kiểm tra xem email có tồn tại không
        if (taikhoanRepo.existsByEmail(dto.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại");
            redirectAttributes.addFlashAttribute("dto", dto);  // Lưu dữ liệu đã nhập
            return "redirect:/admin/nhan-vien"; // Quay lại trang danh sách
        }

        // Tạo tài khoản và nhân viên như bình thường
        String rawPassword = RandomStringUtils.randomAlphanumeric(8);
        taikhoan newAccount = new taikhoan();
        newAccount.setUsername(dto.getUsername());
        newAccount.setPassword(pe.encode(rawPassword));
        newAccount.setEmail(dto.getEmail());
        newAccount.setTrangthai(true);

        vaitro role = vaitroRepo.findById(dto.getVaiTro())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò " + dto.getVaiTro()));
        newAccount.setVaiTro(role);
        taikhoanRepo.save(newAccount);

        nhanvien newEmployee = new nhanvien();
        newEmployee.setMaNhanVien(dto.getMaNhanVien());
        newEmployee.setHoTen(dto.getHoTen());
        newEmployee.setSoDienThoai(dto.getSoDienThoai());
        newEmployee.setNgaySinh(dto.getNgaySinh());
        newEmployee.setSoCanCuocCongDan(dto.getSoCanCuocCongDan());
        newEmployee.setDiaChi(dto.getDiaChi());
        newEmployee.setGioiTinh(dto.getGioiTinh());
        newEmployee.setTrangThai(true);
        newEmployee.setTaikhoan(newAccount);
        nhanVienRepository.save(newEmployee);

        // Gửi email với mật khẩu
        String subject = "Thông tin tài khoản của bạn";
        String body = "Chào " + dto.getHoTen() + ",\n\n"
                + "Tài khoản của bạn đã được tạo thành công.\n"
                + "Tên đăng nhập: " + dto.getUsername() + "\n"
                + "Mật khẩu: " + rawPassword + "\n\n"
                + "Vui lòng thay đổi mật khẩu sau khi đăng nhập lần đầu tiên.\n\n"
                + "Cảm ơn bạn!";
        emailService.sendEmail(dto.getEmail(), subject, body);
        return "redirect:/admin/nhan-vien";
    }






}
