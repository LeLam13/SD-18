package com.example.demo.repo;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    Optional<DotGiamGia> findFirstByTrangThaiInOrderByThoiGianKetThucDesc(List<Integer> trangThaiList);

    List<DotGiamGia> findByThoiGianBatDauBeforeAndThoiGianKetThucAfter(LocalDateTime startTime, LocalDateTime endTime);

    List<DotGiamGia> findByThoiGianKetThucBefore(LocalDateTime endTime);

}
