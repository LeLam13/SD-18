package com.example.demo.controller.admin;

import com.example.demo.Service.impl.EmailService;
import com.example.demo.Service.impl.NhanVienServiceImpl;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


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
    private EmailService emailService;

    @Autowired
    private NhanVienServiceImpl nhanVienService;

    BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // Khởi tạo BCryptPasswordEncoder

    @GetMapping("")
    public String getNhanVienView(){
        return "admin/NhanVien";
    }

    @GetMapping("thong-tin-ca-nhan")
    public String getUserDetail(Model model, Principal principal) {
        // Lấy tên người dùng (username) từ Spring Security
        String username = principal.getName();

        // Tìm thông tin nhân viên từ cơ sở dữ liệu dựa trên username
        nhanvien nhanVien = nhanVienService.findByUsername(username);

        // Thêm thông tin nhân viên vào model để hiển thị trên trang Thymeleaf
        model.addAttribute("nhanVien", nhanVien);

        return "admin/thongTinUser"; // Trả về view thongTinUser
    }

    @PostMapping("/updateUser")
    public String updateUserAndNhanVien(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Boolean trangthai,
            @ModelAttribute nhanvien nhanVien,
            Principal principal,
            Model model) {

        String username = principal.getName();

        taikhoan existingTaiKhoan = taikhoanRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại!"));
        // Kiểm tra định dạng email
        if (email != null && !email.isEmpty()) {
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!email.matches(emailRegex)) {
                model.addAttribute("error", "Email không đúng định dạng!");
                nhanvien nhanVienFromDB = nhanVienRepository.findByTaikhoanUsername(username);
                model.addAttribute("nhanVien", nhanVienFromDB); // Giữ lại thông tin nhân viên
                return "admin/thongTinUser";
            }
        }
            boolean emailExists = taikhoanRepo.existsByEmail(email);
            if (emailExists && !email.equals(existingTaiKhoan.getEmail())) {
                model.addAttribute("error", "Email đã tồn tại trên hệ thống!");
                nhanvien nhanVienFromDB = nhanVienRepository.findByTaikhoanUsername(username);
                model.addAttribute("nhanVien", nhanVienFromDB); // Giữ lại thông tin nhân viên
                return "admin/thongTinUser";
            }

            existingTaiKhoan.setEmail(email);


        if (password != null && !password.isEmpty()) {
            if (password.length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                model.addAttribute("nhanVien", nhanVien);
                return "admin/thongTinUser";
            }
            existingTaiKhoan.setPassword(password);
        }

        if (trangthai != null) {
            existingTaiKhoan.setTrangthai(trangthai);
        }

        taikhoanRepo.save(existingTaiKhoan);

        nhanvien existingNhanVien = nhanVienRepository.findByTaikhoanUsername(username);
        if (existingNhanVien != null) {
            if (nhanVien.getHoTen() == null || nhanVien.getHoTen().isEmpty()) {
                model.addAttribute("error", "Họ tên không được để trống!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }
            existingNhanVien.setHoTen(nhanVien.getHoTen());
            existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
            existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
            existingNhanVien.setSoDienThoai(nhanVien.getSoDienThoai());
            existingNhanVien.setDiaChi(nhanVien.getDiaChi());

            nhanVienRepository.save(existingNhanVien);
        }

        return "redirect:/admin/nhan-vien/thong-tin-ca-nhan";
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
