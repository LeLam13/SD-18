package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${admin.domain}/lich-su-nhap-hang")
public class LichSuNhapHangController {
    @GetMapping("/add/{idSanPham}")
    public String getlichSuNhapHang(){
        return "admin/lichSuNhapHang";
    }

    @GetMapping("/view/{idSanPham}")
    public String getViewlichSuNhapHang(){
        return "admin/lichSuNhapHangView";
    }
}
