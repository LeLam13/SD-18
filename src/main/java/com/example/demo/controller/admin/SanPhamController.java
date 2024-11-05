package com.example.demo.controller.admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${admin.domain}/san-pham")
public class SanPhamController {
    @GetMapping("")
    public String hienThi() {
        return "/admin/sanPham";
    }
    @GetMapping("/formAdd")
    public String viewAdd() {
        return "/admin/formSanPham";
    }

}
