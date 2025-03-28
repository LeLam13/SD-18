package com.example.demo.rest;

import com.example.demo.Service.MauSacService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.repo.MauSacRepo;
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

import java.util.Date;
import java.util.List;

@RestController
public class MauSacRestController {
    @Autowired
    private MauSacService mauSacService;


    @GetMapping("/admin/mau-sac/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MauSac> ms = mauSacService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(ms); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/mau-sac/get-all")
    public ResponseEntity<?> getAll() {
        List<MauSac> ms = mauSacService.getAll();
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/mau-sac/add")
    public ResponseEntity<?> createMauSac(@RequestBody MauSacRequestDTO msDTO) {
        mauSacService.createMauSac(msDTO);
        return ResponseEntity.ok(msDTO);
    }

    @GetMapping("/admin/mau-sac/chiTiet/{ma}")
    public ResponseEntity<?> getMauSac(@PathVariable("ma") String ma) {
        MauSac ms = mauSacService.getMauSac(ma);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/mau-sac/update/{ma}")
    public ResponseEntity<?> updateMauSac(@RequestBody MauSacRequestDTO msDTO) {
        mauSacService.updateMauSac(msDTO);
        return ResponseEntity.ok(msDTO);
    }

    @PostMapping("/admin/mau-sac/updateTT/{idMauSac}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("idMauSac") Integer idMauSac) {
        mauSacService.updateTrangThai(idMauSac);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/mau-sac/delete/{idMauSac}")
    public ResponseEntity<?> MauSac(@PathVariable("idMauSac") Integer idMauSac) {
        mauSacService.deleteMauSac(idMauSac);
        return ResponseEntity.ok("");
    }


}
