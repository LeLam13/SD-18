package com.example.demo.Service.impl;

import com.example.demo.Service.SanPhamService;
import com.example.demo.dto.request.FilterRequestDTO;
import com.example.demo.dto.request.SanPhamRequestDTO;
import com.example.demo.dto.request.SanPhamWithImageDto;
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
import com.example.demo.repo.KieuDangRepo;
import com.example.demo.repo.MauSacRepo;
import com.example.demo.repo.SanPhamChiTietRepo;
import com.example.demo.repo.SanPhamRepo;
import com.example.demo.repo.ThuongHieuRepo;
import com.example.demo.repo.XuatXuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SanPhamServiceImpl implements SanPhamService {
    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private MauSacRepo mauSacRepo;

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

//    public Page<SanPhamWithImageDto> findAllWithImages(Pageable pageable) {
//        // Query kết hợp sản phẩm, chi tiết sản phẩm và hình ảnh
//
//
//        return sanPhamRepo.findAllWithImages(pageable);
//    }

    @Override
    public List<SanPham> getAll() {
        return sanPhamRepo.findAll();
    }

    @Override
    public Page<SanPham> findAll(Pageable pageable) {
        return sanPhamRepo.findAll(pageable);
    }

    @Override
    public Page<SanPham> findAllWithStatistics(Pageable pageable) {
        Page<SanPham> sanPhams = sanPhamRepo.findAll(pageable);

        sanPhams.forEach(sanPham -> {
            // Lấy dữ liệu chi tiết đã bán từ truy vấn
            List<Object[]> detailedSoldData = sanPhamChiTietRepo.getDetailedTotalSoldByProduct(sanPham.getIdSanPham());

            // Tổng số lượng đã bán bằng cách gộp từ các chi tiết
            Integer totalSold = 0;
            for (Object[] row : detailedSoldData) {
                if (row[1] != null) {
                    totalSold += ((Number) row[1]).intValue();
                }
            }

            // Lấy số lượng tồn kho
            Integer totalInventory = sanPhamChiTietRepo.getTotalInventoryByProduct(sanPham.getIdSanPham());

            // Gán giá trị bổ sung vào sản phẩm
            sanPham.setTotalSold(totalSold != null ? totalSold : 0); // Tránh NullPointerException
            sanPham.setTotalInventory(totalInventory != null ? totalInventory : 0); // Tránh NullPointerException
        });

        return sanPhams;
    }

    @Override
    public Page<SanPham> findAllSanPham(Pageable pageable) {
        Page<SanPham> sanPhams = sanPhamRepo.findByTrangThaiWithChiTiet(pageable);

        sanPhams.forEach(sanPham -> {
            // Lấy sản phẩm chi tiết có giá nhỏ nhất
            List<SanPhamChiTiet> chiTietList = sanPhamChiTietRepo.findCheapestProductDetail(sanPham.getIdSanPham());

            // Lọc danh sách để chỉ giữ các mục có idHinhAnh không null
            SanPhamChiTiet cheapestDetail = chiTietList.stream()
                    .filter(chiTiet -> chiTiet.getIdHinhAnh() != null)
                    .findFirst() // Lấy mục đầu tiên sau khi lọc
                    .orElse(null);

            // Gán giá nhỏ nhất
            sanPham.setMinGiaBan(cheapestDetail != null ? cheapestDetail.getGiaBan() : null);

            // Gán hình ảnh nếu tồn tại
            sanPham.setHinhAnh(cheapestDetail != null ? cheapestDetail.getIdHinhAnh().getTen() : null);

        });


        return sanPhams;
    }

    @Override
    public SanPham createSanPham(SanPhamRequestDTO sanPhamRequestDTO) {
        SanPham sp = new SanPham();
        sp.setMa(sanPhamRequestDTO.getMa());
        sp.setTen(sanPhamRequestDTO.getTen());

        ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(sanPhamRequestDTO.getIdChatLieu());
        sp.setIdChatLieu(chatLieu);

        XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(sanPhamRequestDTO.getIdXuatXu());
        sp.setIdXuatXu(xuatXu);

        KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(sanPhamRequestDTO.getIdKieuDang());
        sp.setIdKieuDang(kieuDang);

        ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(sanPhamRequestDTO.getIdThuongHieu());
        sp.setIdThuongHieu(thuongHieu);

//        HinhAnh hinhAnh = hinhAnhRepo.findByIdHinhAnh(sanPhamRequestDTO.getIdHinhAnh());
//        sp.setIdHinhAnh(hinhAnh);

        sp.setMoTa(sanPhamRequestDTO.getMoTa());
        sp.setCreateDate(date);
        sp.setCreateBy(getCurrentUsername());
        sp.setTrangThai(true);
        return sanPhamRepo.save(sp);
    }

    @Override
    public SanPham updateSanPham(SanPhamRequestDTO sanPhamRequestDTO) {
        SanPham ms = sanPhamRepo.findByMa(sanPhamRequestDTO.getMa());
        ms.setTen(sanPhamRequestDTO.getTen());

        ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(sanPhamRequestDTO.getIdChatLieu());
        ms.setIdChatLieu(chatLieu);

        XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(sanPhamRequestDTO.getIdXuatXu());
        ms.setIdXuatXu(xuatXu);

        KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(sanPhamRequestDTO.getIdKieuDang());
        ms.setIdKieuDang(kieuDang);

        ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(sanPhamRequestDTO.getIdThuongHieu());
        ms.setIdThuongHieu(thuongHieu);

//        HinhAnh hinhAnh = hinhAnhRepo.findByIdHinhAnh(sanPhamRequestDTO.getIdHinhAnh());
//        ms.setIdHinhAnh(hinhAnh);

        ms.setMoTa(sanPhamRequestDTO.getMoTa());
        ms.setUpdateBy(getCurrentUsername());
        ms.setUpdateDate(date);
        return sanPhamRepo.save(ms);
    }

    @Override
    public SanPham updateSanPhamTheoID(Integer idSanPham, SanPhamRequestDTO sanPhamRequestDTO) {
        SanPham ms = sanPhamRepo.findByIdSanPham(idSanPham);

        ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(sanPhamRequestDTO.getIdChatLieu());
        ms.setIdChatLieu(chatLieu);

        XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(sanPhamRequestDTO.getIdXuatXu());
        ms.setIdXuatXu(xuatXu);

        KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(sanPhamRequestDTO.getIdKieuDang());
        ms.setIdKieuDang(kieuDang);

        ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(sanPhamRequestDTO.getIdThuongHieu());
        ms.setIdThuongHieu(thuongHieu);

//        HinhAnh hinhAnh = hinhAnhRepo.findByIdHinhAnh(sanPhamRequestDTO.getIdHinhAnh());
//        ms.setIdHinhAnh(hinhAnh);

        ms.setUpdateBy(getCurrentUsername());
        ms.setUpdateDate(date);
        return sanPhamRepo.save(ms);
    }

    @Override
    public SanPham getSanPham(String ma) {
        return sanPhamRepo.findByMa(ma);
    }

    @Override
    public SanPham getByIdSanPham(Integer idSanPham) {
        return sanPhamRepo.findByIdSanPham(idSanPham);
    }

    @Override
    public SanPham updateTrangThai(Integer idSanPham) {
        SanPham ms = sanPhamRepo.findByIdSanPham(idSanPham);
        if (ms.getTrangThai() == true) {
            ms.setTrangThai(false);
        } else {
            ms.setTrangThai(true);
        }
        ms.setUpdateBy(getCurrentUsername());
        ms.setUpdateDate(date);
        return sanPhamRepo.save(ms);
    }

    @Override
    public Page<SanPham> filterProducts(FilterRequestDTO filterRequest, Pageable pageable) {
        Specification<SanPham> speci = Specification.where(null);

        // Tìm kiếm theo mã hoặc tên sản phẩm
        if (filterRequest.getTen() != null && !filterRequest.getTen().isEmpty()) {
            String tenKhongDau = removeAccents(filterRequest.getTen());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("ten")), "%" + tenKhongDau.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("ma")), "%" + tenKhongDau.toLowerCase() + "%")
                    ));
        }

        // Tìm kiếm theo xuất xứ
        if (filterRequest.getIdXuatXu() != null) {
            XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(filterRequest.getIdXuatXu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idXuatXu"), xuatXu));
        }

        // Tìm kiếm theo thương hiệu
        if (filterRequest.getIdThuongHieu() != null) {
            ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(filterRequest.getIdThuongHieu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idThuongHieu"), thuongHieu));
        }

        // Tìm kiếm theo kiểu dáng
        if (filterRequest.getIdKieuDang() != null) {
            KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(filterRequest.getIdKieuDang());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idKieuDang"), kieuDang));
        }

        // Tìm kiếm theo chất liệu
        if (filterRequest.getIdChatLieu() != null) {
            ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(filterRequest.getIdChatLieu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idChatLieu"), chatLieu));
        }

        // Lấy sản phẩm từ database
        Page<SanPham> result = sanPhamRepo.findAll(speci, pageable);

        // Xử lý bổ sung thông tin sản phẩm
        List<SanPham> processedProducts = result.getContent().stream().map(sanPham -> {
            // Lấy dữ liệu chi tiết đã bán từ truy vấn
            List<Object[]> detailedSoldData = sanPhamChiTietRepo.getDetailedTotalSoldByProduct(sanPham.getIdSanPham());

            // Tổng số lượng đã bán bằng cách gộp từ các chi tiết
            Integer totalSold = detailedSoldData.stream()
                    .filter(row -> row[1] != null)
                    .mapToInt(row -> ((Number) row[1]).intValue())
                    .sum();

            // Lấy số lượng tồn kho
            Integer totalInventory = sanPhamChiTietRepo.getTotalInventoryByProduct(sanPham.getIdSanPham());

            // Gán giá trị bổ sung vào sản phẩm
            sanPham.setTotalSold(totalSold != null ? totalSold : 0); // Tránh NullPointerException
            sanPham.setTotalInventory(totalInventory != null ? totalInventory : 0); // Tránh NullPointerException

            return sanPham;
        }).collect(Collectors.toList());

        // Trả về Page đã xử lý
        return new PageImpl<>(processedProducts, pageable, result.getTotalElements());
    }


    @Override
    public Page<SanPham> filterProductsView(FilterRequestDTO filterRequest, Pageable pageable) {
        Specification<SanPham> speci = Specification.where(null);

        // Tìm kiếm theo mã hoặc tên sản phẩm
        if (filterRequest.getTen() != null && !filterRequest.getTen().isEmpty()) {
            String tenKhongDau = removeAccents(filterRequest.getTen());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("ten")), "%" + tenKhongDau.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("ma")), "%" + tenKhongDau.toLowerCase() + "%")
                    ));
        }

        // Các điều kiện lọc khác
        if (filterRequest.getIdXuatXu() != null) {
            XuatXu xuatXu = xuatXuRepo.findByIdXuatXu(filterRequest.getIdXuatXu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idXuatXu"), xuatXu));
        }

        if (filterRequest.getIdThuongHieu() != null) {
            ThuongHieu thuongHieu = thuongHieuRepo.findByIdThuongHieu(filterRequest.getIdThuongHieu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idThuongHieu"), thuongHieu));
        }

        if (filterRequest.getIdKieuDang() != null) {
            KieuDang kieuDang = kieuDangRepo.findByIdKieuDang(filterRequest.getIdKieuDang());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idKieuDang"), kieuDang));
        }

        if (filterRequest.getIdChatLieu() != null) {
            ChatLieu chatLieu = chatLieuRepo.findByIdChatLieu(filterRequest.getIdChatLieu());
            speci = speci.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("idChatLieu"), chatLieu));
        }

        // Lọc sản phẩm từ database (không dùng phân trang ở đây)
        List<SanPham> result = sanPhamRepo.findAll(speci);

        // Xử lý bổ sung thông tin sản phẩm (tính giá và hình ảnh)
        List<SanPham> processedProducts = result.stream().map(sanPham -> {
            // Lấy dữ liệu chi tiết đã bán từ truy vấn
            List<Object[]> detailedSoldData = sanPhamChiTietRepo.getDetailedTotalSoldByProduct(sanPham.getIdSanPham());

            // Tổng số lượng đã bán
            Integer totalSold = detailedSoldData.stream()
                    .filter(row -> row[1] != null)
                    .mapToInt(row -> ((Number) row[1]).intValue())
                    .sum();

            // Lấy số lượng tồn kho
            Integer totalInventory = sanPhamChiTietRepo.getTotalInventoryByProduct(sanPham.getIdSanPham());

            // Gán giá trị bổ sung vào sản phẩm
            sanPham.setTotalSold(totalSold != null ? totalSold : 0);
            sanPham.setTotalInventory(totalInventory != null ? totalInventory : 0);

            // Lấy giá bán nhỏ nhất và hình ảnh từ chi tiết sản phẩm
            List<SanPhamChiTiet> chiTietList = sanPhamChiTietRepo.findCheapestProductDetail(sanPham.getIdSanPham());
            SanPhamChiTiet cheapestDetail = chiTietList.stream()
                    .filter(chiTiet -> chiTiet.getIdHinhAnh() != null)
                    .findFirst()
                    .orElse(null);

            sanPham.setMinGiaBan(cheapestDetail != null ? cheapestDetail.getGiaBan() : null);
            sanPham.setHinhAnh(cheapestDetail != null ? cheapestDetail.getIdHinhAnh().getTen() : null);

            return sanPham;
        }).collect(Collectors.toList());

        // Lọc sản phẩm theo GiaMin và GiaMax từ giá bán (MinGiaBan)
        List<SanPham> filteredProducts = processedProducts.stream()
                .filter(sanPham -> filterRequest.getGiaMin() == null ||
                        (sanPham.getMinGiaBan() != null && sanPham.getMinGiaBan() >= filterRequest.getGiaMin()))
                .filter(sanPham -> filterRequest.getGiaMax() == null ||
                        (sanPham.getMinGiaBan() != null && sanPham.getMinGiaBan() <= filterRequest.getGiaMax()))
                .collect(Collectors.toList());

        // Tạo Pageable mới với các thông số phân trang và sắp xếp theo idSanPham giảm dần
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        // Tính toán lại số phần tử trong danh sách sau khi lọc và phân trang
        int start = (int) newPageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProducts.size());
        List<SanPham> pagedList = filteredProducts.subList(start, end);

        // Trả về một Page với các sản phẩm đã lọc và phân trang đúng
        return new PageImpl<>(pagedList, newPageable, filteredProducts.size());
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

    public String getCurrentUsername() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                // Trường hợp principal là UserDetails
                username = ((UserDetails) principal).getUsername();
                System.out.println("Username (UserDetails): " + username);
            } else {
                // Trường hợp principal là chuỗi (vd: OAuth2)
                username = principal.toString();
                System.out.println("Username (String): " + username);
            }
        }
        return username;
    }
}
