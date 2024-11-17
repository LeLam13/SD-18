package com.example.demo.rest;

import com.example.demo.Service.SanPhamChiTietService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SanPhamChiTietRestController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @GetMapping("admin/san-pham/{idSanPham}/find-all")
    public ResponseEntity<?> findAllChiTiet(
            @PathVariable Integer idSanPham,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println("idSanPham" + idSanPham);
        Pageable pageable = PageRequest.of(page, size, Sort.by("idSanPhamChiTiet").descending());
        Page<SanPhamChiTiet> sp = sanPhamChiTietService.findBySanPham(idSanPham, pageable); // Assuming this is your service method
//        PagedModel<SanPham> pagedModel = assembler.toModel(sp);
        System.out.println("sp:" + sp);
        return ResponseEntity.ok(sp); // Return the PagedModel
    }

    @GetMapping("admin/san-pham/{idSanPham}/find-all/{idGG}")
    public ResponseEntity<?> findSanPhamChiTietNotInDotGiamGia(
            @PathVariable Integer idSanPham,
            @PathVariable Integer idGG,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPhamChiTiet> sp = sanPhamChiTietRepo.findSanPhamChiTietNotInDotGiamGia(idSanPham,idGG, pageable); // Assuming this is your service method
        System.out.println("sp:" + sp);
        return ResponseEntity.ok(sp);
    }

    @GetMapping("/admin/san-pham/chi-tiet/get-all")
    public ResponseEntity<?> getAll() {
        List<SanPhamChiTiet> ms = sanPhamChiTietService.getAll();
        return ResponseEntity.ok(ms);
    }


    @GetMapping("/admin/chitietsanpham/{idSanPhamChiTiet}")
    public ResponseEntity<Integer> getSanPhamChiTietId(@PathVariable Integer idSanPhamChiTiet) {
        // Trả về ID của sản phẩm chi tiết
        return ResponseEntity.ok(idSanPhamChiTiet);

    }
    @PostMapping("/admin/san-pham/chi-tiet/add")
    public ResponseEntity<?> addSanPhamChiTiet(@RequestBody List<SanPhamChiTietRequestDTO> sanPhamChiTietRequestDTO) {
        sanPhamChiTietService.createSanPhamChiTietList(sanPhamChiTietRequestDTO);
        return ResponseEntity.ok(sanPhamChiTietRequestDTO);

    }


    @GetMapping("/admin/san-pham/chi-tiet/view/{ma}")
    public ResponseEntity<?> getSanPhamChiTiet(@PathVariable("ma") String ma) {
        SanPhamChiTiet ms = sanPhamChiTietService.getSanPhamChiTiet(ma);
        return ResponseEntity.ok(ms);
    }

    @PostMapping("/admin/san-pham/chi-tiet/update/{ma}")
    public ResponseEntity<?> updateMauSac(@RequestBody SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO) {
        sanPhamChiTietService.updateSanPhamChiTiet(sanPhamChiTietRequestDTO);
        return ResponseEntity.ok(sanPhamChiTietRequestDTO);
    }

    @PostMapping("/admin/san-pham/chi-tiet/updateTT/{idSanPhamChiTiet}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("idSanPhamChiTiet") Integer idSanPhamChiTiet) {
        sanPhamChiTietService.updateTrangThai(idSanPhamChiTiet);
        return ResponseEntity.ok("");
    }
}


