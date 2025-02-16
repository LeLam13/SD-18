package com.example.demo.repo;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    Page<DotGiamGia> findAll(Pageable pageable);
    Optional<DotGiamGia> findFirstByTrangThaiInOrderByThoiGianKetThucDesc(List<Integer> trangThaiList);

    List<DotGiamGia> findByThoiGianBatDauBeforeAndThoiGianKetThucAfter(LocalDateTime startTime, LocalDateTime endTime);

    List<DotGiamGia> findByThoiGianKetThucBefore(LocalDateTime endTime);
    // Truy vấn để lấy các đợt giảm giá có ngày kết thúc trong tương lai
    @Query("SELECT d FROM DotGiamGia d WHERE d.thoiGianKetThuc > CURRENT_DATE AND d.trangThai IN (1, 0)")
    List<DotGiamGia> findActiveDotGiamGia();

    @Query("SELECT d FROM DotGiamGia d WHERE d.thoiGianBatDau > :currentDate AND d.thoiGianKetThuc > :currentDate")
    List<DotGiamGia> findUpcomingDotGiamGia(@Param("currentDate") LocalDateTime currentDate);


    @Query("SELECT p FROM DotGiamGia p WHERE " +
            "(:startDate BETWEEN p.thoiGianBatDau AND p.thoiGianKetThuc OR " +
            ":endDate BETWEEN p.thoiGianBatDau AND p.thoiGianKetThuc OR " +
            "p.thoiGianBatDau BETWEEN :startDate AND :endDate)")
    List<DotGiamGia> findOverlappingPromotions(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT p FROM DotGiamGia p WHERE p.idGiamGia <> :currentId AND "
            + "((:startDate BETWEEN p.thoiGianBatDau AND p.thoiGianKetThuc) "
            + "OR (:endDate BETWEEN p.thoiGianBatDau AND p.thoiGianKetThuc) "
            + "OR (p.thoiGianBatDau BETWEEN :startDate AND :endDate))")
    List<DotGiamGia> findOverlappingPromotionsExceptCurrent(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("currentId") Integer currentId);

}
