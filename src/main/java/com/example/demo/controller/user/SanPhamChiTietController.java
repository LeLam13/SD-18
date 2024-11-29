package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamChiTietController {
    @GetMapping("/view-san-pham-chi-tiet/{idSanPham}")
    public String hienThi() {
        return "/user/SanPhamChiTiet";
    }
}
