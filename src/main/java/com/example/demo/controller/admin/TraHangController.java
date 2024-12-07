package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("${admin.domain}/tra-hang")
public class TraHangController {
    @GetMapping()
    public String donHang() {
        return "admin/traHang";
    }

    @GetMapping("/{id}")
    public String donHangChiTiet(@PathVariable("id") Long id) {
        //System.out.println("Đang truy cập chi tiết đơn hàng: " + id);
        return "admin/chiTietDonHang"; // Trả về trang chi tiết đơn hàng
    }

}
