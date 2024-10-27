package com.example.demo.controller.admin;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.khachhang;
import com.example.demo.repo.KhachHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("${admin.domain}/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangRepo khachHangRepo;

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("")
    public String getKhachHangView(Model model) {
        List<khachhang> khachhangList = khachHangRepo.findAll();
        model.addAttribute("khachhangList", khachhangList);
        return "admin/KhachHang";
    }

    @GetMapping("/api")
    @ResponseBody
    public List<KhachHangResponseDTO> getAllKhachHangApi() {
        return khachHangService.getAllKhachHang();
    }

}
