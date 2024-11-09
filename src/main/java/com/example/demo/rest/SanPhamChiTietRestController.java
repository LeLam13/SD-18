package com.example.demo.rest;

import com.example.demo.Service.SanPhamChiTietService;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SanPhamChiTietRestController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("admin/san-pham/{idSanPham}/find-all")
    public ResponseEntity<?> findAllChiTiet(
            @PathVariable Integer idSanPham,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println("idSanPham" + idSanPham);
        Pageable pageable = PageRequest.of(page, size, Sort.by("idSanPhamChiTiet").descending());
        Page<SanPhamChiTiet> sp = sanPhamChiTietService.findBySanPham(idSanPham, pageable); // Assuming this is your service method
//        PagedModel<SanPham> pagedModel = assembler.toModel(sp);
        System.out.println("sp:" + sp);
        return ResponseEntity.ok(sp); // Return the PagedModel
    }

    @GetMapping("/admin/san-pham/chi-tiet/get-all")
    public ResponseEntity<?> getAll() {
        List<SanPhamChiTiet> ms = sanPhamChiTietService.getAll();
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/san-pham/chi-tiet/add")
    public ResponseEntity<?> addSanPhamChiTiet(@RequestBody SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO) {
        System.out.println("data: " + sanPhamChiTietRequestDTO);
        sanPhamChiTietService.createSanPhamChiTiet(sanPhamChiTietRequestDTO);
        return ResponseEntity.ok(sanPhamChiTietRequestDTO);
    }
}
