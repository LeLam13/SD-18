package com.example.demo.Service.impl;

import com.example.demo.entity.KhuyenMai;
import com.example.demo.repo.KhuyenMaiRepo;
import com.example.demo.Service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepo khuyenMaiRepository;

    @Override
    public Page<KhuyenMai> timKiemPhanTrang(int page, String search, String trangThai) {
        Pageable pageable = PageRequest.of(page, 10); // 10 items per page
        if (trangThai.equals("all")) {
            return khuyenMaiRepository.findByTenKhuyenMaiContaining(search, pageable);
        } else {
            boolean trangThaiBool = trangThai.equals("active");
            return khuyenMaiRepository.findByTenKhuyenMaiContainingAndTrangThai(search, trangThaiBool, pageable);
        }
    }

    @Override
    public Optional<KhuyenMai> findById(Integer id) {
        return khuyenMaiRepository.findById(id);
    }

    @Override
    public void save(KhuyenMai khuyenMai) {
        khuyenMaiRepository.save(khuyenMai);
    }

    @Override
    public void deleteById(Integer id) {
        khuyenMaiRepository.deleteById(id);
    }

    @Override
    public Optional<KhuyenMai> findByMaKhuyenMai(String maKhuyenMai) {
        return khuyenMaiRepository.findByMaKhuyenMai(maKhuyenMai);
    }

    @Override
    public List<KhuyenMai> findAll() {
        return khuyenMaiRepository.findAll();
    }
}
