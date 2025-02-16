package com.example.demo.Service;

import com.example.demo.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KhuyenMaiService {
    Page<KhuyenMai> timKiemPhanTrang(int page, String search, String trangThai);

    Optional<KhuyenMai> findById(Integer id);

    void save(KhuyenMai khuyenMai);

    void deleteById(Integer id);

    Optional<KhuyenMai> findByMaKhuyenMai(String maKhuyenMai);

    List<KhuyenMai> findAll();
}
