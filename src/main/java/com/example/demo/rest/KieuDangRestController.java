package com.example.demo.rest;

import com.example.demo.Service.KieuDangService;
import com.example.demo.dto.request.KieuDangRequestDTO;
import com.example.demo.dto.request.ThuongHieuRequestDTO;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.ThuongHieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/admin/kieu-dang/add")
    public ResponseEntity<?> createKieuDang(@RequestBody KieuDangRequestDTO kieuDangRequestDTO) {
        kieuDangService.createKieuDang(kieuDangRequestDTO);
        return ResponseEntity.ok(kieuDangRequestDTO);
    }

    @GetMapping("/admin/kieu-dang/chiTiet/{ma}")
    public ResponseEntity<?> getKieuDang(@PathVariable("ma") String ma) {
        KieuDang kd = kieuDangService.getKieuDang(ma);
        return ResponseEntity.ok(kd);
    }

    @PostMapping("/admin/kieu-dang/update/{ma}")
    public ResponseEntity<?> updateKieuDang(@RequestBody KieuDangRequestDTO kieuDangRequestDTO) {
        kieuDangService.updateKieuDang(kieuDangRequestDTO);
        return ResponseEntity.ok(kieuDangRequestDTO);
    }

    @PostMapping("/admin/kieu-dang/updateTT/{idKieuDang}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("ma") Integer idKieuDang) {
        kieuDangService.updateTrangThai(idKieuDang);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/kieu-dang/delete/{idKieuDang}")
    public ResponseEntity<?> KieuDang(@PathVariable("idKieuDang") Integer idKieuDang) {
        kieuDangService.deleteKieuDang(idKieuDang);
        return ResponseEntity.ok("");
    }
}
