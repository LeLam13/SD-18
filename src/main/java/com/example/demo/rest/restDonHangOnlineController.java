package com.example.demo.rest;

import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class restDonHangOnlineController {
    @Autowired
    DonHangOnlineService donHangOnlineService;

    @GetMapping("/danh-sach-san-pham")
    public ResponseEntity<?> getAllProductsUser(){
        List<SanPhamChiTiet> listSanPham = donHangOnlineService.getAllProducts();
         return ResponseEntity.ok(listSanPham);
    }

    @GetMapping("/danh-sach-san-pham/{id}")
    public ResponseEntity<?> getProductByIdUser(@PathVariable("id") Integer id){
        SanPhamChiTiet sanPhamChiTiet = donHangOnlineService.getProductsByID(id);
        return ResponseEntity.ok(sanPhamChiTiet);
    }
}
