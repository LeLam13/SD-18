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
import org.springframework.data.domain.Sort;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by("idKichCo").descending());
        Page<KichCo> kc = kichCoService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(kc); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/size/get-all/trang-thai")
    public ResponseEntity<?> getAllByTT() {
        List<KichCo> ms = kichCoService.getAllByTT();
        return ResponseEntity.ok(ms);
    }

    @GetMapping("/admin/size/get-all")
    public ResponseEntity<?> getAll() {
        List<KichCo> kc = kichCoService.getAll();
        return ResponseEntity.ok(kc);
    }

    @GetMapping("/size/get-all")
    public ResponseEntity<?> getAll2() {
        List<KichCo> kc = kichCoService.getAll();
        return ResponseEntity.ok(kc);
    }

    @GetMapping("/admin/size/get-all-by-chi-tiet/{idSanPham}")
    public ResponseEntity<?> getAllbyCT(@PathVariable("idSanPham") Integer idSanPham) {
        List<KichCo> ms = kichCoService.getAllbyCT(idSanPham);
        return ResponseEntity.ok(ms);
    }

    @GetMapping("/admin/size/get-all-by-mau-sac/{idSanPham}")
    public ResponseEntity<?> getAllbyMS(@PathVariable("idSanPham") Integer idSanPham,
                                        @RequestParam("idMauSac") Integer idMauSac) {
        List<KichCo> ms = kichCoService.getAllbyMS(idSanPham, idMauSac);
        return ResponseEntity.ok(ms);
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

    @GetMapping("/admin/size/search")
    public ResponseEntity<?> search(@RequestParam String query,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idKichCo").descending());
        Page<KichCo> ms = kichCoService.search(query, pageable); // Phân trang
        return ResponseEntity.ok(ms); // Trả về trang hiện tại cùng dữ liệu
    }

}
