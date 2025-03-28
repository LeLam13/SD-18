package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GioHangController {
    @GetMapping("/gio-hang")
    public String hienThi() {
        return "/user/gioHang";
    }
}
