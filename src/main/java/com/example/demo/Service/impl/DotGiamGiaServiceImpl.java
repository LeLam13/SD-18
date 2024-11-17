package com.example.demo.Service.impl;

import com.example.demo.dto.reponse.DotGiamGiaResponse;
import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.DotGiamGiaRepository;
import com.example.demo.repo.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DotGiamGiaServiceImpl {

    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepository;
    public DotGiamGia createDotGiamGia(DotGiamGia dotGiamGia) {
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
        if (dotGiamGia.getThoiGianBatDau().isAfter(dotGiamGia.getThoiGianKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
        }

        // Kiểm tra ngày bắt đầu của đợt giảm giá mới so với đợt giảm giá hiện tại hoặc sắp diễn ra
        Optional<DotGiamGia> activeDiscount = dotGiamGiaRepository
                .findFirstByTrangThaiInOrderByThoiGianKetThucDesc(List.of(0, 1));

        if (activeDiscount.isPresent()) {
            LocalDateTime endOfLastActiveDiscount = activeDiscount.get().getThoiGianKetThuc();
            if (!dotGiamGia.getThoiGianBatDau().isAfter(endOfLastActiveDiscount)) {
                throw new IllegalArgumentException(
                        "Ngày bắt đầu của đợt giảm giá mới phải lớn hơn ngày kết thúc của đợt giảm giá hiện tại hoặc sắp diễn ra.");
            }
        }

        // Cập nhật trạng thái dựa trên ngày bắt đầu và kết thúc
        updateStatus(dotGiamGia);
        return dotGiamGiaRepository.save(dotGiamGia);
    }

    public DotGiamGia getDotGiamGiaById(Integer id) {
        return dotGiamGiaRepository.findById(id).orElseThrow(()->{
            throw new RuntimeException("Không tìm thấy id");
        });
    }


    public DotGiamGia getLastActiveDotGiamGia() {
        return dotGiamGiaRepository.findFirstByTrangThaiInOrderByThoiGianKetThucDesc(List.of(0, 1)).orElse(null);
    }

    public List<DotGiamGia> getAllDotGiamGia() {
        return dotGiamGiaRepository.findAll();
    }

    public void updateStatus(DotGiamGia dotGiamGia) {
        LocalDateTime now = LocalDateTime.now();
        if (dotGiamGia.getThoiGianBatDau().isAfter(now)) {
            dotGiamGia.setTrangThai(0); // Sắp diễn ra
        } else if (dotGiamGia.getThoiGianKetThuc().isBefore(now)) {
            dotGiamGia.setTrangThai(2); // Đã kết thúc
        } else {
            dotGiamGia.setTrangThai(1); // Đang diễn ra
        }
    }

    // Scheduled method to automatically update discount statuses
    @Scheduled(fixedRate = 10000) // Chạy mỗi 10 giây
    public void updateAllDiscountStatuses() {
        List<DotGiamGia> allDiscounts = dotGiamGiaRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (DotGiamGia discount : allDiscounts) {
            updateStatus(discount); // Cập nhật trạng thái cho từng đợt giảm giá
            dotGiamGiaRepository.save(discount); // Lưu lại trạng thái đã cập nhật
        }
    }

    public DotGiamGia findById(Integer id) {
        return dotGiamGiaRepository.findById(id).orElse(null);  // Trả về null nếu không tìm thấy
    }

    // Phương thức lưu đợt giảm giá
    public DotGiamGia save(DotGiamGia dotGiamGia) {
        return dotGiamGiaRepository.save(dotGiamGia);
    }

    public DotGiamGia addProductDetailToPromotion(Integer promotionId, List<Integer> productDetailIds){
        DotGiamGia dotGiamGia = dotGiamGiaRepository.findById(promotionId).orElseThrow(() ->
                new RuntimeException("Dot giảm giá không tồn tại")
        );
        if(productDetailIds.isEmpty()){
            dotGiamGia.getSanPhamChiTietList().clear();
            dotGiamGiaRepository.save(dotGiamGia);
            return dotGiamGia;
        }
        List<SanPhamChiTiet> productDetails = sanPhamChiTietRepository.findAllByIdSanPhamChiTietIn(productDetailIds);

        List<SanPhamChiTiet> productDetailList = sanPhamChiTietRepository.findAll();

        List<Integer> foundIds = productDetails.stream()
                .map(SanPhamChiTiet::getIdSanPhamChiTiet)
                .toList();
        List<Integer> notFoundIds = productDetailIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!notFoundIds.isEmpty()) {
            throw new RuntimeException("San pham chi tiet khong ton tai");
        }
        productDetailList.forEach(productDetail -> {
            if (productDetail.getSoTienGiam() != null) {

//                productDetail.setDonGia(productDetail.getSoTienGiam());

                productDetail.setGiaBan(productDetail.getSoTienGiam());
                productDetail.setSoTienGiam(null);
            }
        });
//        dotGiamGia.getSanPhamChiTietList().clear();
        dotGiamGia.getSanPhamChiTietList().addAll(productDetails);
        dotGiamGiaRepository.save(dotGiamGia);
        return dotGiamGia;
    }





    // Áp dụng giảm giá
    public void applyDiscount(SanPhamChiTiet sanPhamChiTiet, DotGiamGia dotGiamGia) {
        if (sanPhamChiTiet.getSoTienGiam() == null) { // Chỉ áp dụng nếu chưa có giảm giá
            sanPhamChiTiet.setSoTienGiam(sanPhamChiTiet.getGiaBan()); // Lưu giá gốc

            float discountAmount;

            if (dotGiamGia.getLoaiGiamGia() == 0) { // Giảm giá theo phần trăm
                discountAmount = sanPhamChiTiet.getGiaBan() * (dotGiamGia.getGiamGia().floatValue() / 100);
            } else if (dotGiamGia.getLoaiGiamGia() == 1) { // Giảm giá theo số tiền cụ thể
                discountAmount = dotGiamGia.getGiamGia().floatValue();
            } else {
                return; // Trường hợp loại giảm giá không hợp lệ, thoát mà không thay đổi
            }

            // Đảm bảo giá bán không âm
            float newGiaBan = sanPhamChiTiet.getGiaBan() - discountAmount;
            sanPhamChiTiet.setGiaBan(Math.max(newGiaBan, 0));
        }
    }


    // Hoàn lại giá gốc sau khi kết thúc đợt giảm giá
    public void revertDiscount(SanPhamChiTiet sanPhamChiTiet) {
        if (sanPhamChiTiet.getSoTienGiam() != null) { // Chỉ hoàn lại nếu đã áp dụng giảm giá


//            sanPhamChiTiet.setDonGia(sanPhamChiTiet.getSoTienGiam()); // Khôi phục giá gốc

            sanPhamChiTiet.setGiaBan(sanPhamChiTiet.getSoTienGiam()); // Khôi phục giá gốc
            sanPhamChiTiet.setSoTienGiam(null); // Đặt lại soTienGiam thành null
        }
    }

    // Tác vụ kiểm tra giảm giá định kỳ
    @Scheduled(fixedRate = 10000) // Chạy mỗi phút
    public void updateDiscounts() {
        LocalDateTime now = LocalDateTime.now();

        List<DotGiamGia> discountEvents = dotGiamGiaRepository.findAll();
        for (DotGiamGia discountEvent : discountEvents) {
            for (SanPhamChiTiet sanPhamChiTiet : discountEvent.getSanPhamChiTietList()) {
                if (now.isAfter(discountEvent.getThoiGianBatDau()) && now.isBefore(discountEvent.getThoiGianKetThuc())) {
                    // Đợt giảm giá đang diễn ra
                    applyDiscount(sanPhamChiTiet, discountEvent);
                } else if (now.isAfter(discountEvent.getThoiGianKetThuc())) {
                    // Đợt giảm giá đã kết thúc
                    revertDiscount(sanPhamChiTiet);
                }
                sanPhamChiTietRepository.save(sanPhamChiTiet); // Lưu cập nhật vào DB
            }
        }
    }


}
