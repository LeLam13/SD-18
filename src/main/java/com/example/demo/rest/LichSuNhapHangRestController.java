package com.example.demo.rest;

import com.example.demo.Service.LichSuNhapHangService;
import com.example.demo.Service.SanPhamChiTietService;
import com.example.demo.dto.request.LichSuNhapHangRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.entity.LichSuNhapHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
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
public class LichSuNhapHangRestController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private LichSuNhapHangService lichSuNhapHangService;

    @PostMapping("/admin/lich-su-nhap-hang/add")
    public ResponseEntity<?> addSanPhamChiTiet(@RequestBody LichSuNhapHangRequestDTO lichSuNhapHangRequestDTOS) {
        lichSuNhapHangService.createLichSuNhapHang(lichSuNhapHangRequestDTOS);
        return ResponseEntity.ok(lichSuNhapHangRequestDTOS);

    }

    @GetMapping("/admin/lich-su-nhap-hang/{idSanPham}/find-all")
    public ResponseEntity<?> findAllChiTiet(
            @PathVariable Integer idSanPham,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idLichSuNhapHang").descending());
        Page<LichSuNhapHang> sp = lichSuNhapHangService.findAll(idSanPham,pageable); // Assuming this is your service method
        return ResponseEntity.ok(sp); // Return the PagedModel
    }
}
