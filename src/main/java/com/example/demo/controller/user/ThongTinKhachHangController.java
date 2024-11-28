package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongTinKhachHangController {
    @GetMapping("thong-tin-kh")
    public  String getThongTinKh(){
        return "/user/ThongTinKhachHang";
    }
}
