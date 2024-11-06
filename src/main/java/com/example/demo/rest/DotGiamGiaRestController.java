package com.example.demo.rest;

import com.example.demo.Service.impl.DotGiamGiaService;
import com.example.demo.entity.DotGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@RestController
public class DotGiamGiaRestController {

    @Autowired
    private DotGiamGiaService dotGiamGiaService;

    @GetMapping("/admin/dot-giam-gia/getall")
    public ResponseEntity<List<DotGiamGia>> listDotGiamGia() {
        // Lấy tất cả các đợt giảm giá từ service
        List<DotGiamGia> dotGiamGias = dotGiamGiaService.getAllDotGiamGia();

        // Chuyển đổi các đối tượng DotGiamGia thành DotGiamGiaDTO
        List<DotGiamGia> dotGiamGia1 = dotGiamGias.stream()
                .map(dotGiamGia -> {
                    DotGiamGia dto = new DotGiamGia();
                    dto.setIdGiamGia(dotGiamGia.getIdGiamGia());
                    dto.setGiamGia(dotGiamGia.getGiamGia());
                    dto.setThoiGianBatDau(dotGiamGia.getThoiGianBatDau());
                    dto.setThoiGianKetThuc(dotGiamGia.getThoiGianKetThuc());
                    dto.setTrangThai(dotGiamGia.getTrangThai());
                    dto.setLoaiGiamGia(dotGiamGia.getLoaiGiamGia());
                    dto.setSanPhamChiTietList(dotGiamGia.getSanPhamChiTietList());
                    return dto;
                })
                .collect(Collectors.toList());

        // Trả về danh sách DTO dưới dạng JSON
        return ResponseEntity.ok(dotGiamGia1);
    }



}
