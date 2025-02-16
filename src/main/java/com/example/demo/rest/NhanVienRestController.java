package com.example.demo.rest;

import com.example.demo.Service.NhanVienService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.NhanVienRequetsDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.nhanvien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NhanVienRestController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/admin/nhan-vien/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<nhanvien> nhanViens = nhanVienService.getActiveNhanVien(pageable);
        return ResponseEntity.ok(nhanViens); // Trả về trang hiện tại cùng dữ liệu
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
    @PostMapping("/admin/nhan-vien/delete/{idNhanVien}")
    public ResponseEntity<?> deleteNhanVien(@PathVariable("idNhanVien") Integer idNhanVien) {
        nhanvien deletedNhanVien = nhanVienService.softDeleteNhanVien(idNhanVien);
        return ResponseEntity.ok(deletedNhanVien); // Trả về nhân viên đã được xóa mềm
    }


    @GetMapping("/admin/nhan-vien/search")
    public ResponseEntity<?> searchNhanVien(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<nhanvien> nhanViens = nhanVienService.searchNhanVien(keyword, pageable);
        return ResponseEntity.ok(nhanViens);
    }
}

