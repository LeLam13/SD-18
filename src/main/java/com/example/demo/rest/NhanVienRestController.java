package com.example.demo.rest;

import com.example.demo.Service.NhanVienService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.nhanvien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NhanVienRestController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/admin/nhan-vien/find-all")
    public ResponseEntity<?> findAll() {
        List<nhanvien> ms = nhanVienService.getAll();
        return ResponseEntity.ok(ms);
    }
    @GetMapping("/admin/nhan-vien/chiTiet/{idNhanVien}")
    public ResponseEntity<?> getNhanVien(@PathVariable("idNhanVien") Integer idNhanVien) {
        nhanvien ms = nhanVienService.getNhanVien(idNhanVien);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/nhan-vien/update/{idNhanVien}")
    public ResponseEntity<?> updateNhanVien(@RequestBody NhanVienRequetsDTO nvDTO) {
        nhanVienService.updateNhanVien(nvDTO);
        return ResponseEntity.ok(nvDTO);
    }
}

