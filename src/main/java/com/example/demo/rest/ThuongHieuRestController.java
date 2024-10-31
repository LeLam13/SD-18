package com.example.demo.rest;

import com.example.demo.Service.ThuongHieuService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.ThuongHieuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.ThuongHieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ThuongHieuRestController {
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/admin/thuong-hieu/get-all")
    public ResponseEntity<?> getAll(){
        List<ThuongHieu> th=thuongHieuService.getAll();
        return ResponseEntity.ok(th);
    }

    @GetMapping("/admin/thuong-hieu/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ThuongHieu> th = thuongHieuService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(th); // Trả về trang hiện tại cùng dữ liệu
    }

    @PostMapping("/admin/thuong-hieu/add")
    public ResponseEntity<?> createThuongHieu(@RequestBody ThuongHieuRequestDTO thuongHieuRequestDTO) {
        thuongHieuService.createThuongHieu(thuongHieuRequestDTO);
        return ResponseEntity.ok(thuongHieuRequestDTO);
    }

    @GetMapping("/admin/thuong-hieu/chiTiet/{ma}")
    public ResponseEntity<?> getThuongHieu(@PathVariable("ma") String ma) {
        ThuongHieu th = thuongHieuService.getThuongHieu(ma);
        return ResponseEntity.ok(th);
    }

    @PostMapping("/admin/thuong-hieu/update/{ma}")
    public ResponseEntity<?> updateThuongHieu(@RequestBody ThuongHieuRequestDTO thuongHieuRequestDTO) {
        thuongHieuService.updateThuongHieu(thuongHieuRequestDTO);
        return ResponseEntity.ok(thuongHieuRequestDTO);
    }

    @PostMapping("/admin/thuong-hieu/updateTT/{idThuongHieu}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("ma") Integer idThuongHieu) {
        thuongHieuService.updateTrangThai(idThuongHieu);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/thuong-hieu/delete/{idThuongHieu}")
    public ResponseEntity<?> ThuongHieu(@PathVariable("idThuongHieu") Integer idThuongHieu) {
        thuongHieuService.deleteThuongHieu(idThuongHieu);
        return ResponseEntity.ok("");
    }
}
