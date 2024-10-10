package com.example.demo.controller.admin;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("${admin.domain}/trang-chu")
public class TrangChuController {
    @GetMapping("")
    public String show() {
            return "admin/thongke/tongQuat";
        }
    }


