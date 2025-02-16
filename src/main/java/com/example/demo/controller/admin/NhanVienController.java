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
import java.time.LocalDate;


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
                model.addAttribute("nhanVien", nhanVienRepository.findByTaikhoanUsername(username));
                return "admin/thongTinUser";
            }

            boolean emailExists = taikhoanRepo.existsByEmail(email);
            if (emailExists && !email.equals(existingTaiKhoan.getEmail())) {
                model.addAttribute("error", "Email đã tồn tại trên hệ thống!");
                model.addAttribute("nhanVien", nhanVienRepository.findByTaikhoanUsername(username));
                return "admin/thongTinUser";
            }

            existingTaiKhoan.setEmail(email);
        }

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
            // Kiểm tra họ tên không để trống
            if (nhanVien.getHoTen() == null || nhanVien.getHoTen().isEmpty()) {
                model.addAttribute("error", "Họ tên không được để trống!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }
            // Kiểm tra họ tên không chứa ký tự đặc biệt
            String hoTenRegex = "^[a-zA-ZÀ-ỹ\\s]+$";
            if (!nhanVien.getHoTen().matches(hoTenRegex)) {
                model.addAttribute("error", "Họ tên không được chứa ký tự đặc biệt!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }

            // Kiểm tra số điện thoại
            if (nhanVien.getSoDienThoai() == null || nhanVien.getSoDienThoai().isEmpty()) {
                model.addAttribute("error", "Số điện thoại không được để trống!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }
            if (!nhanVien.getSoDienThoai().matches("^0\\d{9}$")) {
                model.addAttribute("error", "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }

            // Kiểm tra ngày sinh
            if (nhanVien.getNgaySinh() == null) {
                model.addAttribute("error", "Ngày sinh không được để trống!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }
            if (nhanVien.getNgaySinh().isAfter(LocalDate.now())) {
                model.addAttribute("error", "Ngày sinh không được là ngày trong tương lai!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }

            // Kiểm tra địa chỉ không để trống
            if (nhanVien.getDiaChi() == null || nhanVien.getDiaChi().isEmpty()) {
                model.addAttribute("error", "Địa chỉ không được để trống!");
                model.addAttribute("nhanVien", existingNhanVien);
                return "admin/thongTinUser";
            }

            // Cập nhật thông tin nhân viên
            existingNhanVien.setHoTen(nhanVien.getHoTen());
            existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
            existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
            existingNhanVien.setSoDienThoai(nhanVien.getSoDienThoai());
            existingNhanVien.setDiaChi(nhanVien.getDiaChi());

            nhanVienRepository.save(existingNhanVien);
        }

        return "redirect:/admin/nhan-vien/thong-tin-ca-nhan";
    }







    private String generateEmployeeCode() {
        long count = nhanVienRepository.count(); // Đếm số lượng nhân viên trong bảng
        String prefix = "NV"; // Tiền tố cho mã nhân viên
        String uniqueCode = prefix + String.format("%05d", count + 1); // Tạo mã NV00001
        return uniqueCode;
    }


    @PostMapping("/addEmployee")
    public String addEmployee(NhanVienRequetsDTO dto, RedirectAttributes redirectAttributes) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

// Kiểm tra tính hợp lệ của tên đăng nhập
        String username = dto.getUsername().trim();
        String usernameRegex = "^[a-zA-Z0-9]{5,20}$"; // Chỉ cho phép chữ cái và số, độ dài từ 5-20 ký tự

        if (!username.matches(usernameRegex)) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập không hợp lệ. Chỉ cho phép chữ cái, số, không dấu, và từ 5-20 ký tự.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        if (dto.getSoDienThoai() == null || dto.getSoDienThoai().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        if (dto.getHoTen() == null || dto.getHoTen().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Họ tên không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

// Kiểm tra họ tên không chứa ký tự đặc biệt
        String hoTen = dto.getHoTen().trim();
        String hoTenRegex = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểễếỄỈỊọỏốồổỗộớờởỡợỤỦỨỪỬỮỰỲỴÝỶỸỳỵỷỹ\\s]{1,50}$";

        if (!hoTen.matches(hoTenRegex)) {
            redirectAttributes.addFlashAttribute("error", "Họ tên không hợp lệ. Chỉ cho phép chữ cái và khoảng trắng, không chứa ký tự đặc biệt.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        if (dto.getNgaySinh() == null) {
            redirectAttributes.addFlashAttribute("error", "Ngày sinh không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        if (dto.getSoCanCuocCongDan() == null || dto.getSoCanCuocCongDan().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Căn cước công dân không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        if (dto.getDiaChi() == null || dto.getDiaChi().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Địa chỉ không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        if (dto.getVaiTro() == null) {
            redirectAttributes.addFlashAttribute("error", "Vai trò không được để trống.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }
        // Kiểm tra username
        if (taikhoanRepo.existsByUsername(dto.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        // Kiểm tra email
        if (taikhoanRepo.existsByEmail(dto.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        // Validate số điện thoại
        if (!dto.getSoDienThoai().matches("^0\\d{9}$")) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại phải bắt đầu bằng 0 và có 10 số.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        // Validate ngày sinh
        if (dto.getNgaySinh().isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày sinh không được là ngày trong tương lai.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        // Validate căn cước công dân
        if (!dto.getSoCanCuocCongDan().matches("\\d{13}")) {
            redirectAttributes.addFlashAttribute("error", "Căn cước công dân phải có 13 số.");
            redirectAttributes.addFlashAttribute("dto", dto);
            return "redirect:/admin/nhan-vien";
        }

        // Sinh mã nhân viên
        String maNhanVien = generateEmployeeCode();

        // Tạo tài khoản
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

        // Tạo nhân viên
        nhanvien newEmployee = new nhanvien();
        newEmployee.setMaNhanVien(maNhanVien); // Gán mã tự động
        newEmployee.setHoTen(dto.getHoTen());
        newEmployee.setSoDienThoai(dto.getSoDienThoai());
        newEmployee.setNgaySinh(dto.getNgaySinh());
        newEmployee.setSoCanCuocCongDan(dto.getSoCanCuocCongDan());
        newEmployee.setDiaChi(dto.getDiaChi());
        newEmployee.setGioiTinh(dto.getGioiTinh());
        newEmployee.setTrangThai(true);
        newEmployee.setTaikhoan(newAccount);
        nhanVienRepository.save(newEmployee);

        // Gửi email
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
