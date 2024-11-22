package com.example.demo.rest;

import com.example.demo.Service.impl.DotGiamGiaServiceImpl;
import com.example.demo.Service.impl.SanPhamChiTietServiceImpl;
import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
public class DotGiamGiaRestController {
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private DotGiamGiaServiceImpl dotGiamGiaService;
    @Autowired
    private SanPhamChiTietServiceImpl sanPhamChiTietService;
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

    @GetMapping("/admin/san-pham/dot-giam-gia/{id}")
    public ResponseEntity<List<SanPham>> getSanPhamById(@PathVariable Integer id) {
        List<SanPham> sanPhams = sanPhamRepo.findAllByDotGiamGia(id);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/admin/delete/{idSP}/dot-giam-gia/{idGG}")
    public ResponseEntity<?> deleteByDotGiamGiaAndSanPhamChiTiet(@PathVariable Integer idSP, @PathVariable Integer idGG) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepo.findAllBySanPhamAndDotGiamGia(idSP, idGG);
        for (SanPhamChiTiet s:sanPhamChiTietList) {
            s.setGiaBan(s.getSoTienGiam());
            s.setSoTienGiam(null);
            sanPhamChiTietRepo.save(s);
        }
        sanPhamRepo.deleteByDotGiamGiaAndSanPham(idGG,idSP);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/dotgiamgia/{idGiamGia}")
    public DotGiamGia getidDotGiamGia(@PathVariable Integer idGiamGia){
        return dotGiamGiaService.getDotGiamGiaById(idGiamGia);
    }
    // API để thêm chi tiết sản phẩm vào đợt giảm giá
    // API xử lý thêm chi tiết sản phẩm vào đợt giảm giá
    @PostMapping("/admin/dot-giam-gia/{dotGiamGiaId}/add-san-pham")
    public ResponseEntity<DotGiamGia> addSanPhamToDotGiamGia(
            @PathVariable Integer dotGiamGiaId,
            @RequestBody List<Integer> sanPhamChiTietIds) {
        DotGiamGia dotGiamGia = dotGiamGiaService.addProductDetailToPromotion(dotGiamGiaId,sanPhamChiTietIds);
        return new ResponseEntity<>(dotGiamGia,HttpStatus.OK);
    }
}