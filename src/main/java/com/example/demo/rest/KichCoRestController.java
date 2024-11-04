package com.example.demo.rest;

import com.example.demo.Service.KichCoService;
import com.example.demo.Service.MauSacService;
import com.example.demo.dto.request.KichCoRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.MauSac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KichCoRestController {
    @Autowired
    private KichCoService kichCoService;


    @GetMapping("/admin/size/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KichCo> kc = kichCoService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(kc); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/size/get-all")
    public ResponseEntity<?> getAll() {
        List<KichCo> kc = kichCoService.getAll();
        return ResponseEntity.ok(kc);
    }

    @PostMapping("/admin/size/add")
    public ResponseEntity<?> createKichCo(@RequestBody KichCoRequestDTO kichCoRequestDTO) {
        kichCoService.createKichCo(kichCoRequestDTO);
        return ResponseEntity.ok(kichCoRequestDTO);
    }

    @GetMapping("/admin/size/chiTiet/{ma}")
    public ResponseEntity<?> getKichCo(@PathVariable("ma") String ma) {
        KichCo ms = kichCoService.getKichCo(ma);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/size/update/{ma}")
    public ResponseEntity<?> updateMauSac(@RequestBody KichCoRequestDTO kichCoRequestDTO) {
        kichCoService.updateKichCo(kichCoRequestDTO);
        return ResponseEntity.ok(kichCoRequestDTO);
    }

    @PostMapping("/admin/size/updateTT/{idKichCo}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("idKichCo") Integer idKichCo) {
        kichCoService.updateTrangThai(idKichCo);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/size/delete/{idKichCo}")
    public ResponseEntity<?> MauSac(@PathVariable("idKichCo") Integer idKichCo) {
        kichCoService.deleteKichCo(idKichCo);
        return ResponseEntity.ok("");
    }


}
