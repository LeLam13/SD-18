package com.example.demo.rest;

import com.example.demo.Service.KieuDangService;
import com.example.demo.entity.KieuDang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KieuDangRestController {
    @Autowired
    private KieuDangService kieuDangService;

    @GetMapping("/admin/kieu-dang/find-all")
    public ResponseEntity<?> findAll(){
        List<KieuDang> kd=kieuDangService.getAll();
        return ResponseEntity.ok(kd);
    }
}
