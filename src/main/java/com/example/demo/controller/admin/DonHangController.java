package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("don-hang-controller-admin")
@RequestMapping("${admin.domain}/don-hang")
public class DonHangController {
    @GetMapping()
    public String donHang(){
        return "admin/traHang";
    }
    @GetMapping("ban-hang")
    public String banHang(){
        return "admin/BanHangTaiQuay";
    }
    @GetMapping("/{id}")
    public String donHangChiTiet(@PathVariable("id") Long id) {
        //System.out.println("Đang truy cập chi tiết đơn hàng: " + id);
        return "admin/chiTietDonHang"; // Trả về trang chi tiết đơn hàng
    }

}
