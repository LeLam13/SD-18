package com.example.demo.controller.admin;

import com.example.demo.entity.KhuyenMai;
import com.example.demo.Service.KhuyenMaiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("${admin.domain}/khuyen-mai")
public class KhuyenMaiController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    // Hiển thị danh sách khuyến mãi
    @GetMapping("")
    public String hienThiDanhSach(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "all") String trangThai,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "message", required = false) String message,
            Model model) {
        Page<KhuyenMai> danhSach = khuyenMaiService.timKiemPhanTrang(page, search, trangThai);
        model.addAttribute("danhSach", danhSach.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", danhSach.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("trangThai", trangThai);

        // Thêm trạng thái vào model nếu có
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }

        return "/admin/khuyenMai";
    }

    // Hiển thị form thêm mới khuyến mãi
    @GetMapping("/form")
    public String hienThiFormThem(Model model) {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setTrangThai(false); // Đặt trạng thái mặc định
        model.addAttribute("khuyenMai", khuyenMai);
        return "/admin/formKhuyenMai"; // Trang form thêm mới
    }

    // Hiển thị form sửa khuyến mãi
    @GetMapping("/form/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        Optional<KhuyenMai> khuyenMaiOpt = khuyenMaiService.findById(id);
        if (khuyenMaiOpt.isPresent()) {
            model.addAttribute("khuyenMai", khuyenMaiOpt.get());
        } else {
            return "redirect:/admin/khuyen-mai"; // Nếu không tìm thấy, quay về danh sách
        }
        return "/admin/formKhuyenMai";
    }

    // Xử lý lưu khuyến mãi (thêm/sửa)
    @PostMapping("/save")
    public String saveKhuyenMai(@ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
            @RequestParam("thoiGianBatDau") String thoiGianBatDau,
            @RequestParam("thoiGianKetThuc") String thoiGianKetThuc,
            Model model) {
        StringBuilder errorMessage = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Định dạng ngày khớp với input type="date"

        try {
            // 1. Kiểm tra dữ liệu đầu vào (validate input)

            // Kiểm tra rỗng
            if (khuyenMai.getMaKhuyenMai() == null || khuyenMai.getMaKhuyenMai().trim().isEmpty()) {
                errorMessage.append("Mã khuyến mãi không được để trống. ");
            }

            if (khuyenMai.getTenKhuyenMai() == null || khuyenMai.getTenKhuyenMai().trim().isEmpty()) {
                errorMessage.append("Tên khuyến mãi không được để trống. ");
            }

            if (thoiGianBatDau == null || thoiGianBatDau.trim().isEmpty()) {
                errorMessage.append("Thời gian bắt đầu không được để trống. ");
            }

            if (thoiGianKetThuc == null || thoiGianKetThuc.trim().isEmpty()) {
                errorMessage.append("Thời gian kết thúc không được để trống. ");
            }

            // Kiểm tra giá trị số
            if (khuyenMai.getMucGiamGia() == null || khuyenMai.getMucGiamGia() <= 0) {
                errorMessage.append("Mức giảm giá phải lớn hơn 0. ");
            }

            if (khuyenMai.getSoLuong() == null || khuyenMai.getSoLuong() <= 0) {
                errorMessage.append("Số lượng phải lớn hơn 0. ");
            }

            if (khuyenMai.getSoTienToiThieu() != null && khuyenMai.getSoTienToiThieu() < 0) {
                errorMessage.append("Số tiền tối thiểu không được nhỏ hơn 0. ");
            }

            // Kiểm tra định dạng ngày
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dateFormat.parse(thoiGianBatDau);
                endDate = dateFormat.parse(thoiGianKetThuc);
                khuyenMai.setThoiGianBatDau(startDate);
                khuyenMai.setThoiGianKetThuc(endDate);
            } catch (ParseException e) {
                errorMessage.append("Định dạng ngày không hợp lệ. Vui lòng nhập ngày theo định dạng MM/dd/yyyy. ");
            }

            // Kiểm tra logic thời gian
            if (startDate != null && endDate != null && !startDate.before(endDate)) {
                errorMessage.append("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc. ");
            }

            // Nếu có lỗi, hiển thị lại trang form và trả về thông báo lỗi
            if (errorMessage.length() > 0) {
                model.addAttribute("error", errorMessage.toString().trim());
                model.addAttribute("khuyenMai", khuyenMai);
                return "/admin/formKhuyenMai"; // Quay lại form nếu lỗi
            }

            // 2. Lưu vào cơ sở dữ liệu
            khuyenMaiService.save(khuyenMai);

            // 3. Mã hóa thông báo thành công
            String message = java.net.URLEncoder.encode("Lưu khuyến mãi thành công!", "UTF-8");
            return "redirect:/admin/khuyen-mai?status=success&message=" + message;

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã xảy ra lỗi khi lưu khuyến mãi.");
            return "/admin/formKhuyenMai";
        }
    }

    // Xóa khuyến mãi
    @PostMapping("/delete/{id}")
    public String xoaKhuyenMai(@PathVariable("id") Integer id) {
        khuyenMaiService.deleteById(id);
        return "redirect:/admin/khuyen-mai"; // Sau khi xóa, quay về danh sách
    }

    // Hiển thị chi tiết khuyến mãi
    @GetMapping("/view/{id}")
    public String xemChiTietKhuyenMai(@PathVariable("id") Integer id, Model model) throws UnsupportedEncodingException {
        Optional<KhuyenMai> khuyenMaiOpt = khuyenMaiService.findById(id);
        if (khuyenMaiOpt.isPresent()) {
            model.addAttribute("khuyenMai", khuyenMaiOpt.get());
            return "/admin/viewKhuyenMai"; // Trang hiển thị chi tiết khuyến mãi
        } else {
            // Nếu không tìm thấy, quay lại danh sách với thông báo lỗi
            String message = java.net.URLEncoder.encode("Không tìm thấy khuyến mãi với ID: " + id, "UTF-8");
            return "redirect:/admin/khuyen-mai?status=error&message=" + message;
        }
    }

}
