package com.example.demo.controller.admin;

import com.example.demo.entity.HoaDon;
import com.example.demo.repo.HoaDonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${admin.domain}/hoa-don")
public class HoaDonController {
    @Autowired
    HoaDonRepo hoaDonRepo;
    @GetMapping("")
    public String getHoaDonList(Model model) {
        List<HoaDon> hoaDonList = hoaDonRepo.findAll();
        model.addAttribute("hoaDons", hoaDonList);
        return "admin/hoaDon";
    }
    // sao ko return cai template ra

}
