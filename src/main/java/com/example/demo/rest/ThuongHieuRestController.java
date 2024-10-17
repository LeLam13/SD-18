package com.example.demo.rest;

import com.example.demo.Service.ThuongHieuService;
import com.example.demo.entity.ThuongHieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ThuongHieuRestController {
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/admin/thuong-hieu/find-all")
    public ResponseEntity<?> findAll(){
        List<ThuongHieu> th=thuongHieuService.findAll();
        return ResponseEntity.ok(th);
    }
}
