package com.example.demo.Service.impl;

import com.example.demo.Service.SanPhamChiTietService;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.ChatLieuRepo;
import com.example.demo.repo.KichCoRepo;
import com.example.demo.repo.KieuDangRepo;
import com.example.demo.repo.MauSacRepo;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import com.example.demo.repo.ThuongHieuRepo;
import com.example.demo.repo.XuatXuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private MauSacRepo mauSacRepo;

    @Autowired
    private KichCoRepo kichCoRepo;

    @Autowired
    private ChatLieuRepo chatLieuRepo;

    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    @Autowired
    private XuatXuRepo xuatXuRepo;

    @Autowired
    private KieuDangRepo kieuDangRepo;

    Date date = new Date();

    @Override
    public Page<SanPhamChiTiet> findBySanPham(Integer idSanPham, Pageable pageable) {
//        Pageable pageable = PageRequest.of(0, 1);
//        Page<SanPhamChiTiet> spct = sanPhamChiTietRepo.findByIdSanPham_IdSanPham(2,pageable);
//        System.out.println("check log page1: "+spct);
//        System.out.println("Total elements: " + spct.getTotalElements());
//        System.out.println("Total pages: " + spct.getTotalPages());
//        System.out.println("Content: " + spct.getContent());
        return sanPhamChiTietRepo.getByID(idSanPham, pageable);
    }

    @Override
    public List<SanPhamChiTiet> getAll() {
        return sanPhamChiTietRepo.findAll();
    }

    // Phương thức để tìm danh sách chi tiết sản phẩm từ các ID
    public List<SanPhamChiTiet> findByIds(List<Integer> sanPhamChiTietIds) {
        return sanPhamChiTietRepo.findAllByIdSanPhamChiTietIn(sanPhamChiTietIds);
    }


    @Override
    public List<SanPhamChiTiet> createSanPhamChiTietList(List<SanPhamChiTietRequestDTO> sanPhamChiTietRequestDTOList) {
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();


        for (SanPhamChiTietRequestDTO dto : sanPhamChiTietRequestDTOList) {
            SanPhamChiTiet chiTiet = new SanPhamChiTiet();
            chiTiet.setMa(dto.getMa());
            chiTiet.setCreateDate(new Date());
            chiTiet.setTrangThai(true);
            // Thực hiện ánh xạ các thuộc tính khác từ DTO vào chiTiet
            chiTiet.setSoLuong(dto.getSoLuong());
            chiTiet.setGiaNhap(dto.getGiaNhap());
            chiTiet.setGiaBan(dto.getGiaBan());

            // Gán đối tượng MauSac từ repository
            MauSac mauSac = mauSacRepo.findById(dto.getIdMauSac()).orElseThrow(() -> new RuntimeException("Màu sắc không tồn tại"));
            chiTiet.setIdMauSac(mauSac);

            // Gán đối tượng KichCo từ repository
            KichCo kichCo = kichCoRepo.findById(dto.getIdKichCo()).orElseThrow(() -> new RuntimeException("Kích cỡ không tồn tại"));
            chiTiet.setIdKichCo(kichCo);

            ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(dto.getIdChatLieu());
            chiTiet.setIdChatLieu(chatLieu);

            ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(dto.getIdThuongHieu());
            chiTiet.setIdThuongHieu(thuongHieu);

            XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(dto.getIdXuatXu());
            chiTiet.setIdXuatXu(xuatXu);

            KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(dto.getIdKieuDang());
            chiTiet.setIdKieuDang(kieuDang);

            SanPham sanPham = sanPhamRepo.findByIdSanPham(dto.getIdSanPham());
            chiTiet.setIdSanPham(sanPham);

            sanPhamChiTietList.add(chiTiet);
        }

        return sanPhamChiTietRepo.saveAll(sanPhamChiTietList); // Lưu tất cả sản phẩm chi tiết
    }
}
