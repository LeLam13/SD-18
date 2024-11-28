package com.example.demo.rest;

import com.example.demo.Service.HinhAnhService;
import com.example.demo.dto.request.HinhAnhRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.MauSac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HinhAnhRestController {
    @Autowired
    private HinhAnhService hinhAnhService;

    @GetMapping("/admin/hinh-anh")
    public ResponseEntity<?> getAll(@RequestParam("ten")String ten) {
        HinhAnh ms = hinhAnhService.findbyTen(ten);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/hinh-anh/add")
    public ResponseEntity<?> createHinhAnh(@RequestBody HinhAnhRequestDTO hinhAnhRequestDTO) {
        hinhAnhService.createHinhAnh(hinhAnhRequestDTO);
        return ResponseEntity.ok(hinhAnhRequestDTO);
    }
}
