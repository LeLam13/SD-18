package com.example.demo.Service.impl;

import com.example.demo.Service.LichSuNhapHangService;
import com.example.demo.Service.SanPhamChiTietService;
import com.example.demo.dto.request.LichSuNhapHangRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.LichSuNhapHang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.KichCoRepo;
import com.example.demo.repo.LichSuNhapHangRepo;
import com.example.demo.repo.MauSacRepo;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class LichSuNhapHangServiceImpl implements LichSuNhapHangService {

    @Autowired
    private LichSuNhapHangRepo lichSuNhapHangRepo;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private MauSacRepo mauSacRepo;

    @Autowired
    private KichCoRepo kichCoRepo;

    Date date = new Date();

    @Override
    public LichSuNhapHang createLichSuNhapHang(LichSuNhapHangRequestDTO lichSuNhapHangRequestDTO) {
        LichSuNhapHang ms = new LichSuNhapHang();
        ms.setGiaNhap(lichSuNhapHangRequestDTO.getGiaNhapNhap());
        ms.setSoLuong(lichSuNhapHangRequestDTO.getSoLuongNhap());
        ms.setMa(lichSuNhapHangRequestDTO.getMa());

        SanPham sanPham = sanPhamRepo.findByIdSanPham(lichSuNhapHangRequestDTO.getIdSanPham());
        Integer idSanPham = sanPham.getIdSanPham();

        MauSac mauSac = mauSacRepo.findByIdMauSac(lichSuNhapHangRequestDTO.getIdMauSacNhap());
        Integer idMauSac = mauSac.getIdMauSac();

        KichCo kichCo = kichCoRepo.findByIdKichCo(lichSuNhapHangRequestDTO.getIdKichCoNhap());
        Integer idKichCo = kichCo.getIdKichCo();

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findCheapestProductDetail(idSanPham, idMauSac, idKichCo);
        ms.setIdSanPhamChiTiet(sanPhamChiTiet);

        ms.setCreateDate(date);
        return lichSuNhapHangRepo.save(ms);
    }

    @Override
    public Page<LichSuNhapHang> findAll(Integer idSanPham, Pageable pageable) {
        return lichSuNhapHangRepo.findByIdSanPham(idSanPham, pageable);
    }

}
