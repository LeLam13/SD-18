package com.example.demo.controller.admin;

import com.example.demo.entity.SanPham;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${admin.domain}/san-pham/{idSanPham}")
public class ChiTietSanPhamController {
    @GetMapping
    public String getChiTietSanPhamView(){
        return "admin/chiTietSanPham";
    }


}
