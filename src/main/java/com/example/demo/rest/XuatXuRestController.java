package com.example.demo.rest;

import com.example.demo.Service.MauSacService;
import com.example.demo.Service.XuatXuService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.XuatXuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
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
public class XuatXuRestController {
    @Autowired
    private XuatXuService xuatXuService;


    @GetMapping("/admin/xuat-xu/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<XuatXu> xx = xuatXuService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(xx); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/xuat-xu/get-all")
    public ResponseEntity<?> getAll() {
        List<XuatXu> xx = xuatXuService.getAll();
        return ResponseEntity.ok(xx);
    }

    @PostMapping("/admin/xuat-xu/add")
    public ResponseEntity<?> createMauSac(@RequestBody XuatXuRequestDTO xuatXuRequestDTO) {
        xuatXuService.createXuatXu(xuatXuRequestDTO);
        return ResponseEntity.ok(xuatXuRequestDTO);
    }

    @GetMapping("/admin/xuat-xu/chiTiet/{ma}")
    public ResponseEntity<?> getMauSac(@PathVariable("ma") String ma) {
        XuatXu ms = xuatXuService.getXuatXu(ma);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/xuat-xu/update/{ma}")
    public ResponseEntity<?> updateMauSac(@RequestBody XuatXuRequestDTO xuatXuRequestDTO) {
        xuatXuService.updateXuatXu(xuatXuRequestDTO);
        return ResponseEntity.ok(xuatXuRequestDTO);
    }

    @PostMapping("/admin/xuat-xu/updateTT/{idXuatXu}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("idXuatXu") Integer idXuatXu) {
        xuatXuService.updateTrangThai(idXuatXu);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/xuat-xu/delete/{idXuatXu}")
    public ResponseEntity<?> MauSac(@PathVariable("idXuatXu") Integer idXuatXu) {
        xuatXuService.deleteXuatXu(idXuatXu);
        return ResponseEntity.ok("");
    }


}
