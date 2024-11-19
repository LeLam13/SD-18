package com.example.demo.rest;

import com.example.demo.Service.KhuyenMaiService;
import com.example.demo.entity.KhuyenMai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/khuyen-mai")
public class KhuyenMaiRestController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    // Lấy tất cả voucher
    @GetMapping
    public ResponseEntity<?> getAllKhuyenMai() {
        return ResponseEntity.ok(khuyenMaiService.findAll());
    }

    // Sử dụng voucher (trừ số lượng)
    @PostMapping("/su-dung")
    public ResponseEntity<?> suDungVoucher(@RequestParam String maKhuyenMai) {
        Optional<KhuyenMai> khuyenMai = khuyenMaiService.findByMaKhuyenMai(maKhuyenMai);

        // Kiểm tra nếu mã khuyến mãi không tồn tại
        if (khuyenMai.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mã khuyến mãi không tồn tại!");
        }

        KhuyenMai km = khuyenMai.get();

        // Kiểm tra số lượng còn lại
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã khuyến mãi đã hết số lượng!");
        }

        // Giảm số lượng và lưu lại
        km.setSoLuong(km.getSoLuong() - 1);
        khuyenMaiService.save(km);

        return ResponseEntity.ok("Sử dụng mã khuyến mãi thành công!");
    }

    // Kiểm tra mã khuyến mãi (có thêm điều kiện áp dụng)
    @GetMapping("/kiem-tra")
    public ResponseEntity<?> kiemTraMaKhuyenMai(
        @RequestParam String maKhuyenMai,
        @RequestParam(required = false) Double tongTien // Tổng tiền của đơn hàng
    ) {
        Optional<KhuyenMai> khuyenMai = khuyenMaiService.findByMaKhuyenMai(maKhuyenMai);

        // Kiểm tra nếu mã khuyến mãi không tồn tại
        if (khuyenMai.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mã khuyến mãi không hợp lệ!");
        }

        KhuyenMai km = khuyenMai.get();

        // Kiểm tra trạng thái hoạt động của mã khuyến mãi
        if ("Ngừng hoạt động".equals(km.getTrangThai())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã khuyến mãi đã ngừng hoạt động!");
        }

        // Kiểm tra thời gian bắt đầu và kết thúc của mã khuyến mãi
        Date now = new Date();
        if (now.before(km.getThoiGianBatDau()) || now.after(km.getThoiGianKetThuc())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã khuyến mãi đã hết hạn!");
        }

        // Kiểm tra số lượng còn lại
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã khuyến mãi đã hết số lượng!");
        }

        // Kiểm tra điều kiện áp dụng
        if (km.getSoTienToiThieu() != null && (tongTien == null || tongTien < km.getSoTienToiThieu())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã khuyến mãi không áp dụng cho đơn hàng có tổng tiền dưới " + km.getSoTienToiThieu() + " VND!");
        }

        // Trả về mức giảm giá nếu mã hợp lệ và còn hiệu lực
        return ResponseEntity.ok(km.getMucGiamGia());
    }
}

