package com.example.demo.Service.impl;

import com.example.demo.Service.SanPhamChiTietService;

import com.example.demo.dto.request.FilterRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.SanPhamChiTietRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.ChatLieuRepo;
import com.example.demo.repo.HinhAnhRepo;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private HinhAnhRepo hinhAnhRepo;

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

            HinhAnh hinhAnh = hinhAnhRepo.findByIdHinhAnh(dto.getIdHinhAnh());
            chiTiet.setIdHinhAnh(hinhAnh);

            sanPhamChiTietList.add(chiTiet);
        }

        return sanPhamChiTietRepo.saveAll(sanPhamChiTietList); // Lưu tất cả sản phẩm chi tiết
    }

    @Override
    public SanPhamChiTiet getSanPhamChiTiet(String ma) {
        return sanPhamChiTietRepo.findByMa(ma);
    }

    @Override
    public SanPhamChiTiet updateSanPhamChiTiet(SanPhamChiTietRequestDTO sanPhamChiTietRequestDTO) {
        SanPhamChiTiet ms = sanPhamChiTietRepo.findByMa(sanPhamChiTietRequestDTO.getMa());
        ms.setGiaBan(sanPhamChiTietRequestDTO.getGiaBan());
        ms.setSoLuong(sanPhamChiTietRequestDTO.getSoLuong());

        ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(sanPhamChiTietRequestDTO.getIdChatLieu());
        ms.setIdChatLieu(chatLieu);

        XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(sanPhamChiTietRequestDTO.getIdXuatXu());
        ms.setIdXuatXu(xuatXu);

        KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(sanPhamChiTietRequestDTO.getIdKieuDang());
        ms.setIdKieuDang(kieuDang);

        MauSac mauSac = mauSacRepo.findByIdMauSac(sanPhamChiTietRequestDTO.getIdMauSac());
        ms.setIdMauSac(mauSac);

        KichCo kichCo = kichCoRepo.findByIdKichCo(sanPhamChiTietRequestDTO.getIdKichCo());
        ms.setIdKichCo(kichCo);

        ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(sanPhamChiTietRequestDTO.getIdThuongHieu());
        ms.setIdThuongHieu(thuongHieu);

        ms.setUpdateDate(date);
        return sanPhamChiTietRepo.save(ms);
    }

    @Override
    public SanPhamChiTiet updateTrangThai(Integer idSanPhamChiTiet) {
        SanPhamChiTiet ms = sanPhamChiTietRepo.findByIdSanPhamChiTiet(idSanPhamChiTiet);
        if (ms.getTrangThai() == true) {
            ms.setTrangThai(false);
        } else {
            ms.setTrangThai(true);
        }
        return sanPhamChiTietRepo.save(ms);
    }

    @Override
    public Page<SanPhamChiTiet> filterProducts(FilterRequestDTO filterRequest, Pageable pageable) {
        Specification<SanPhamChiTiet> spec = Specification.where(null);



//        if (filterRequest.getTen() != null && !filterRequest.getTen().isEmpty()) {
//            // Lấy danh sách các sản phẩm theo tên
//            String tenKhongDau = removeAccents(filterRequest.getTen());
//
//            // Lấy danh sách các sản phẩm theo tên không dấu
//            List<SanPham> sanPhamList = sanPhamRepo.findByName(tenKhongDau);
//
//                // Thêm điều kiện vào specification để lọc theo idSanPham trong danh sách
//                spec = spec.and((root, query, criteriaBuilder) ->
//                        criteriaBuilder.in(root.get("idSanPham")).value(sanPhamList));
//        }

        if (filterRequest.getIdSanPham() != null) {
            SanPham sanPham = sanPhamRepo.findByIdSanPham(filterRequest.getIdSanPham());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idSanPham"), sanPham));
        }

        if (filterRequest.getGiaMin() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("giaBan"), filterRequest.getGiaMin()));
        }
        if (filterRequest.getGiaMax() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("giaBan"), filterRequest.getGiaMax()));
        }
        if (filterRequest.getIdXuatXu() != null) {
            XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(filterRequest.getIdXuatXu());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idXuatXu"), xuatXu));
        }
        if (filterRequest.getIdMauSac() != null) {
            MauSac mauSac = mauSacRepo.findByIdMauSac(filterRequest.getIdMauSac());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idMauSac"), mauSac));
        }
        if (filterRequest.getIdThuongHieu() != null) {
            ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(filterRequest.getIdThuongHieu());

            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idThuongHieu"), thuongHieu));
        }
        if (filterRequest.getIdKieuDang() != null) {
            KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(filterRequest.getIdKieuDang());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idKieuDang"), kieuDang));
        }
        if (filterRequest.getIdChatLieu() != null) {
            ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(filterRequest.getIdChatLieu());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idChatLieu"), chatLieu));
        }
        if (filterRequest.getIdKichCo() != null) {
            KichCo kichCo = kichCoRepo.findByIdKichCo(filterRequest.getIdKichCo());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idKichCo"), kichCo));
        }

        Page<SanPhamChiTiet> result = sanPhamChiTietRepo.findAll(spec, pageable);

        if (result.getTotalElements() == 0) {
            return Page.empty(); // Trả về danh sách trống nếu không có sản phẩm nào thỏa mãn
        }
        // Thêm các điều kiện khác tương tự...
        return result;
    }

    public static String removeAccents(String str) {
        if (str == null) return null;
        return str.replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

}
