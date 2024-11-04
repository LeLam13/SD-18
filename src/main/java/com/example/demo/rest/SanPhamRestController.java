package com.example.demo.rest;

import com.example.demo.Service.SanPhamService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SanPhamRestController {
    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/admin/san-pham/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idSanPham").descending());
        Page<SanPham> sp = sanPhamService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(sp); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/san-pham/get-all")
    public ResponseEntity<?> getAll() {
        List<SanPham> sp = sanPhamService.getAll();
        return ResponseEntity.ok(sp);
    }

    @PostMapping("/admin/san-pham/add")
    public ResponseEntity<?> createSanPham(@RequestBody SanPhamRequestDTO sanPhamRequestDTO) {
        sanPhamService.createSanPham(sanPhamRequestDTO);
        return ResponseEntity.ok(sanPhamRequestDTO);
    }
}
