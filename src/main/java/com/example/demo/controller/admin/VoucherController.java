package com.example.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("${admin.domain}/voucher")
public class VoucherController {
    @GetMapping("")
    public String hienThi() {
        return "/admin/Voucher";
    }


}
