package com.example.demo.Service.impl;
import com.example.demo.entity.DotGiamGia;
import com.example.demo.repo.DotGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DotGiamGiaService {

    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

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
        return dotGiamGiaRepository.findById(id).orElse(null);



    }



    public DotGiamGia getDotGiamGiaById(int id) {
        return dotGiamGiaRepository.findById(id).orElse(null);
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
}
