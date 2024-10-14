package com.example.demo.rest;

import com.example.demo.Service.MauSacService;
import com.example.demo.entity.MauSac;
import com.example.demo.repo.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MauSacRestController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/admin/mau-sac/find-all")
    public ResponseEntity<?> findAll() {
        List<MauSac> ms = mauSacService.getAll();
        return ResponseEntity.ok(ms);
    }
}
