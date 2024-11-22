package com.example.demo.Config;

import com.example.demo.Service.impl.DotGiamGiaServiceImpl;
import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.DotGiamGiaRepository;
import com.example.demo.repo.SanPhamChiTietRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Configuration
@RequiredArgsConstructor
public class ScheduledDotGiamGia {

    private final DotGiamGiaRepository dotGiamGiaRepository;

    private final SanPhamChiTietRepo sanPhamChiTietRepository;

    private final DotGiamGiaServiceImpl dgg;
    // Tác vụ kiểm tra giảm giá định kỳ
    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    public synchronized void updatePromotionStatuses() {
        int promotionPageNumber = 0;
        int promotionPageSize = 100;
        int productDetailPageSize = 100;

        boolean hasMorePromotions = true;
        while (hasMorePromotions) {
            Pageable promotionPageable = PageRequest.of(promotionPageNumber, promotionPageSize);
            Page<DotGiamGia> promotionPage = dotGiamGiaRepository.findAll(promotionPageable);
            List<DotGiamGia> promotionsToUpdate = promotionPage.getContent();

            for (DotGiamGia promotion : promotionsToUpdate) {
                dgg.updateStatus(promotion);

                boolean hasMoreProductDetails = true;
                int productDetailPageNumber = 0;

                while (hasMoreProductDetails) {
                    Pageable productDetailPageable = PageRequest.of(productDetailPageNumber, productDetailPageSize);
                    Page<SanPhamChiTiet> productDetailPage = sanPhamChiTietRepository.findByPromotionId(
                            promotion.getIdGiamGia(), productDetailPageable);
                    List<SanPhamChiTiet> productDetails = productDetailPage.getContent();

                    for (SanPhamChiTiet productDetail : productDetails) {
                        if (promotion.getTrangThai()==1) {
                            if (productDetail.getSoTienGiam() == null) {
                                productDetail.setSoTienGiam(productDetail.getGiaBan());
                            }

                            Float discountedPrice;
                            if (promotion.getLoaiGiamGia()==0) {
                                discountedPrice = (float) (productDetail.getSoTienGiam() * (1 - promotion.getGiamGia() / 100.0));
                            } else if (promotion.getLoaiGiamGia()==1) {
                                discountedPrice = (float) (productDetail.getSoTienGiam() - promotion.getGiamGia());
                            } else {
                                discountedPrice = productDetail.getSoTienGiam();
                            }


                            productDetail.setGiaBan(Math.max(discountedPrice, 0));
                        } else if (promotion.getTrangThai()==2) {
                            if (productDetail.getSoTienGiam() != null) {
                                productDetail.setGiaBan(productDetail.getSoTienGiam());
                                productDetail.setSoTienGiam(null);
                            }
                        }
                    }
                    sanPhamChiTietRepository.saveAll(productDetails);
                    hasMoreProductDetails = productDetailPage.hasNext();
                    productDetailPageNumber++;
                }
            }
            dotGiamGiaRepository.saveAll(promotionsToUpdate);
            hasMorePromotions = promotionPage.hasNext();
            promotionPageNumber++;
        }
    }

}
