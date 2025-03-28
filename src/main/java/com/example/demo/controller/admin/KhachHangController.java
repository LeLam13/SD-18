package com.example.demo.controller.admin;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.khachhang;
import com.example.demo.repo.khachhangRePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("${admin.domain}/khach-hang")
public class KhachHangController {

    @Autowired
    private khachhangRePo khachHangRepo;

    @Autowired
    private KhachHangService khachHangService;
    private Object KhachHangResponseDTO;

    // Display customer view page
    @GetMapping("")
    public String getKhachHangView(Model model) {
        List<khachhang> khachhangList = khachHangRepo.findAll();
        model.addAttribute("khachhangList", khachhangList);
        return "admin/KhachHang";
    }

    // Fetch all customers for API
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<KhachHangResponseDTO>> getAllKhachHangApi() {
        try {
            List<KhachHangResponseDTO> khachHangList = khachHangService.getAllKhachHang();
            return ResponseEntity.ok(khachHangList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add new customer via API
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<KhachHangResponseDTO> addKhachHang(@RequestBody KhachHangRequestDTO khachHangRequestDTO) {
        try {
            KhachHangResponseDTO savedKhachHang = khachHangService.addKhachHang((com.example.demo.dto.reponse.KhachHangResponseDTO) KhachHangResponseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKhachHang);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Optional: Pagination for fetching customers
    @GetMapping("/api/page")
    @ResponseBody
    public ResponseEntity<Page<KhachHangResponseDTO>> getAllKhachHangPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangResponseDTO> pagedKhachHangList = khachHangService.getAllKhachHangPaged(pageable);
        return ResponseEntity.ok(pagedKhachHangList);
    }
}
